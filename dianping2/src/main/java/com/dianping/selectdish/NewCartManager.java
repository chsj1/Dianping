package com.dianping.selectdish;

import android.os.Parcel;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.model.GiftInfo;
import com.dianping.selectdish.model.GroupOnItem;
import com.dianping.selectdish.model.SetItem;
import com.dianping.util.FileUtils;
import com.dianping.util.FileUtils.ParcelableUtils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NewCartManager
{
  private static final File CART_CACHE_FOLDER = new File(DPApplication.instance().getCacheDir(), "selectdish796");
  private static final int DISH_MENU_ENTRANCE_SHOW = 1;
  private static final int FREE_DISH_ID = 0;
  private static final int ORDER_AVAILABLE_TYPE = 0;
  private static final int VERSION = 796;
  public double average = 0.0D;
  private ArrayList<CartChangedListener> cartChangedListeners = new ArrayList();
  private final SparseIntArray dishCountInGenusTypeArray = new SparseIntArray();
  private DPObject dishMenuEntrance = null;
  private final SparseArray<CartItem> dish_cart = new SparseArray();
  public int exchangedGiftId = 0;
  private final SparseArray<ArrayList<CartFreeItem>> free_cart = new SparseArray();
  public int groupOnDealId = 0;
  private GroupOnItem groupOnInfo = null;
  public int groupOnOrderId = 0;
  public boolean isEstimate = false;
  public String orderButtonSubTitle = null;
  public String orderId = null;
  public String orderViewId = null;
  private final SparseArray<CartItem> setDishInfos = new SparseArray();
  private int shopId = 0;
  private String shopName = null;
  public boolean supportPrepay;
  public boolean supportTable;
  public String tableId;
  private int totalDishCount = 0;
  private int totalFreeDishCount = 0;
  private BigDecimal totalOriginPrice = new BigDecimal(0);
  private BigDecimal totalPrice = new BigDecimal(0);
  private int totalSelectedFreeDishCount = 0;
  private int totalSingleDishCount = 0;
  public int updateShopId = 0;

  static
  {
    if (!CART_CACHE_FOLDER.exists())
      CART_CACHE_FOLDER.mkdirs();
  }

  private void addFreeDish(int paramInt, CartFreeItem paramCartFreeItem)
  {
    ArrayList localArrayList2 = (ArrayList)this.free_cart.get(paramInt);
    ArrayList localArrayList1 = localArrayList2;
    if (localArrayList2 == null)
    {
      localArrayList1 = new ArrayList();
      this.free_cart.put(paramInt, localArrayList1);
    }
    localArrayList1.add(paramCartFreeItem);
    this.totalFreeDishCount += 1;
  }

  private void addUserHistoryGift(GiftInfo paramGiftInfo)
  {
    ArrayList localArrayList2 = (ArrayList)this.free_cart.get(0);
    ArrayList localArrayList1 = localArrayList2;
    if (localArrayList2 == null)
    {
      localArrayList1 = new ArrayList();
      this.free_cart.put(0, localArrayList1);
    }
    localArrayList1.add(new CartFreeItem(paramGiftInfo));
    this.totalFreeDishCount += 1;
  }

  private void clearCartInfo()
  {
    this.orderId = null;
    this.totalDishCount = 0;
    this.totalSingleDishCount = 0;
    this.dishCountInGenusTypeArray.clear();
    this.totalFreeDishCount = 0;
    this.totalSelectedFreeDishCount = 0;
    this.totalPrice = new BigDecimal(0);
    this.totalOriginPrice = new BigDecimal(0);
    this.dish_cart.clear();
    this.free_cart.clear();
    this.groupOnInfo = null;
    this.dishMenuEntrance = null;
    this.setDishInfos.clear();
  }

  private void correctDishes(CartItem[] paramArrayOfCartItem)
  {
    int k = 0;
    int j = 0;
    int i1 = 0;
    Object localObject = new HashSet(this.dish_cart.size());
    int i = 0;
    while (i < this.dish_cart.size())
    {
      ((Set)localObject).add(Integer.valueOf(this.dish_cart.keyAt(i)));
      i += 1;
    }
    int n = 0;
    i = k;
    if (n < paramArrayOfCartItem.length)
    {
      CartItem localCartItem1 = paramArrayOfCartItem[n];
      CartItem localCartItem2 = (CartItem)this.dish_cart.get(localCartItem1.dishInfo.dishId);
      if (localCartItem2 == null);
      while (true)
      {
        n += 1;
        break;
        ((Set)localObject).remove(Integer.valueOf(localCartItem1.dishInfo.dishId));
        if (localCartItem1.dishInfo.status != 0)
        {
          operateDish(localCartItem2.dishInfo, Operation.REMOVE, localCartItem2.getItemCount(), false);
          i = 1;
          continue;
        }
        k = i;
        if (localCartItem2.updateBasicInfo(localCartItem1))
          k = 1;
        int m = j;
        if (localCartItem2.updateEventInfo(localCartItem1))
        {
          m = j;
          if (updateFreeDish(localCartItem2.dishInfo))
            m = 1;
        }
        i = k;
        j = m;
        if (localCartItem2.dishInfo.isValidity == localCartItem1.dishInfo.isValidity)
          continue;
        localCartItem2.dishInfo.isValidity = localCartItem1.dishInfo.isValidity;
        i1 = 1;
        i = k;
        j = m;
      }
    }
    paramArrayOfCartItem = ((Set)localObject).iterator();
    while (paramArrayOfCartItem.hasNext())
    {
      localObject = (Integer)paramArrayOfCartItem.next();
      localObject = (CartItem)this.dish_cart.get(((Integer)localObject).intValue());
      operateDish(((CartItem)localObject).dishInfo, Operation.REMOVE, ((CartItem)localObject).getItemCount(), false);
      i = 1;
    }
    if (i1 != 0)
      sortDishes();
    if (j != 0)
      notifyFreeDishChanged();
    if (i != 0)
      notifyDishChanged(null);
    if ((j != 0) || (i != 0))
      notifyCountChanged();
  }

  private boolean correctFreeDish(CartFreeItem paramCartFreeItem1, CartFreeItem paramCartFreeItem2)
  {
    int i = 0;
    if (paramCartFreeItem1.soldout != paramCartFreeItem2.soldout)
    {
      paramCartFreeItem1.soldout = paramCartFreeItem2.soldout;
      i = 1;
    }
    if (paramCartFreeItem1.expired != paramCartFreeItem2.expired)
    {
      paramCartFreeItem1.expired = paramCartFreeItem2.expired;
      i = 1;
    }
    int j = i;
    if (paramCartFreeItem2.giftInfo.name != null)
    {
      j = i;
      if (!paramCartFreeItem2.giftInfo.name.equals(paramCartFreeItem1.giftInfo.name))
      {
        paramCartFreeItem1.giftInfo.name = paramCartFreeItem2.giftInfo.name;
        j = 1;
      }
    }
    if ((paramCartFreeItem2.soldout) || (paramCartFreeItem2.expired))
      paramCartFreeItem1.use = false;
    return j;
  }

  private void correctFreeDishes(CartFreeItem[] paramArrayOfCartFreeItem)
  {
    int i = 0;
    int k = 0;
    while (k < paramArrayOfCartFreeItem.length)
    {
      int j;
      if (paramArrayOfCartFreeItem[k].giftInfo.giftId != 0)
      {
        localArrayList = (ArrayList)this.free_cart.get(0);
        n = localArrayList.size();
        m = 0;
        if (m < n)
        {
          j = i;
          if (((CartFreeItem)localArrayList.get(m)).giftInfo.giftId == paramArrayOfCartFreeItem[k].giftInfo.giftId)
          {
            j = i;
            if (((CartFreeItem)localArrayList.get(m)).giftInfo.dishId == paramArrayOfCartFreeItem[k].giftInfo.dishId)
              if ((!correctFreeDish((CartFreeItem)localArrayList.get(m), paramArrayOfCartFreeItem[k])) && (i == 0))
                break label154;
          }
          label154: for (j = 1; ; j = 0)
          {
            m += 1;
            i = j;
            break;
          }
        }
        m = localArrayList.size() - 1;
        while (true)
        {
          j = i;
          if (m < 0)
            break;
          if (((CartFreeItem)localArrayList.get(m)).expired)
            localArrayList.remove(m);
          m -= 1;
        }
      }
      ArrayList localArrayList = (ArrayList)this.free_cart.get(paramArrayOfCartFreeItem[k].giftInfo.dishId);
      int n = localArrayList.size();
      int m = 0;
      if (m < n)
      {
        j = i;
        if (((CartFreeItem)localArrayList.get(m)).giftInfo.dishId == paramArrayOfCartFreeItem[k].giftInfo.dishId)
          if ((!correctFreeDish((CartFreeItem)localArrayList.get(m), paramArrayOfCartFreeItem[k])) && (i == 0))
            break label314;
        label314: for (j = 1; ; j = 0)
        {
          m += 1;
          i = j;
          break;
        }
      }
      m = localArrayList.size() - 1;
      while (true)
      {
        j = i;
        if (m < 0)
          break;
        if (((CartFreeItem)localArrayList.get(m)).expired)
          localArrayList.remove(m);
        m -= 1;
      }
      k += 1;
      i = j;
    }
    if (i != 0)
      notifyFreeDishChanged();
  }

  private void deleteFreeDish(int paramInt)
  {
    ArrayList localArrayList = (ArrayList)this.free_cart.get(paramInt);
    if (localArrayList == null)
      return;
    paramInt = localArrayList.size() - 1;
    while (paramInt >= 0)
    {
      if (!((CartFreeItem)localArrayList.get(paramInt)).use)
      {
        localArrayList.remove(paramInt);
        this.totalFreeDishCount -= 1;
        return;
      }
      paramInt -= 1;
    }
    GiftInfo localGiftInfo = ((CartFreeItem)localArrayList.get(0)).giftInfo;
    localArrayList.remove(0);
    this.totalFreeDishCount -= 1;
    this.totalSelectedFreeDishCount -= 1;
    updateGenusTypeCountWhenSelectedGiftCountChanged(localGiftInfo, -1);
  }

  public static NewCartManager getInstance()
  {
    return NewCartManagerInner.INSTANCE;
  }

  private void loadCartFromFile()
  {
    this.dish_cart.clear();
    Object localObject = new File(CART_CACHE_FOLDER, String.valueOf(this.shopId) + String.valueOf(this.groupOnDealId) + "dishes");
    if (!((File)localObject).exists());
    while (true)
    {
      return;
      localObject = FileUtils.getBytes((File)localObject);
      if (localObject == null)
        continue;
      localObject = FileUtils.ParcelableUtils.unmarshall(localObject);
      int i = ((Parcel)localObject).readInt();
      if (i < 0)
        continue;
      while (i > 0)
      {
        int j = ((Parcel)localObject).readInt();
        Serializable localSerializable = ((Parcel)localObject).readSerializable();
        if ((localSerializable instanceof CartItem))
          this.dish_cart.put(j, (CartItem)localSerializable);
        i -= 1;
      }
    }
  }

  private void notifyCountChanged()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((CartChangedListener)localIterator.next()).onCountChanged();
  }

  private void notifyDishChanged(CartItem paramCartItem)
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((CartChangedListener)localIterator.next()).onDishChanged(paramCartItem);
  }

  private void notifyFreeDishChanged()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((CartChangedListener)localIterator.next()).onFreeDishChanged(getAllFreeDishes());
  }

  private void notifyGroupOnOrSetChanged()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((CartChangedListener)localIterator.next()).onGroupOnOrSetChanged();
  }

  private void operateDish(DishInfo paramDishInfo, Operation paramOperation, int paramInt, boolean paramBoolean)
  {
    CartItem localCartItem = null;
    if (paramOperation != Operation.ADD)
      localCartItem = (CartItem)this.dish_cart.get(paramDishInfo.dishId);
    if (!updateDishCartDataWithDishOperation(paramDishInfo, paramOperation));
    do
    {
      return;
      updateTotalInfoWhenCartItemCountChanged(paramDishInfo, paramOperation, paramInt);
      if ((operateSetDish(paramDishInfo, paramOperation, paramInt)) && (paramBoolean))
        notifyGroupOnOrSetChanged();
      if ((updateFreeDish(paramDishInfo)) && (paramBoolean))
        notifyFreeDishChanged();
      if (paramOperation != Operation.ADD)
        continue;
      localCartItem = (CartItem)this.dish_cart.get(paramDishInfo.dishId);
    }
    while (!paramBoolean);
    notifyDishChanged(localCartItem);
    notifyCountChanged();
  }

  private boolean operateSetDish(DishInfo paramDishInfo, Operation paramOperation, int paramInt)
  {
    if (paramDishInfo.dishType == 1)
    {
      CartItem localCartItem = (CartItem)this.setDishInfos.get(paramDishInfo.dishId);
      if (paramOperation == Operation.ADD)
        if (localCartItem == null)
        {
          paramOperation = new CartItem(paramDishInfo);
          this.setDishInfos.put(paramDishInfo.dishId, paramOperation);
          paramOperation.addItem(paramInt);
        }
      do
        while (true)
        {
          return true;
          localCartItem.addItem(paramInt);
          return true;
          if (paramOperation != Operation.REDUCE)
            break;
          if ((localCartItem == null) || (localCartItem.reduceItem(Math.min(paramInt, localCartItem.getItemCount())) != 0))
            continue;
          this.setDishInfos.remove(paramDishInfo.dishId);
          return true;
        }
      while ((paramOperation != Operation.REMOVE) || (localCartItem == null));
      localCartItem.setItemCount(0);
      this.setDishInfos.remove(paramDishInfo.dishId);
      return true;
    }
    return false;
  }

  private boolean updateDishCartDataWithDishOperation(DishInfo paramDishInfo, Operation paramOperation)
  {
    CartItem localCartItem = (CartItem)this.dish_cart.get(paramDishInfo.dishId);
    if (paramOperation == Operation.ADD)
    {
      paramOperation = localCartItem;
      if (localCartItem == null)
      {
        paramOperation = new CartItem(paramDishInfo);
        this.dish_cart.put(paramDishInfo.dishId, paramOperation);
      }
      paramOperation.addItem(1);
    }
    while (true)
    {
      return true;
      if (paramOperation != Operation.REDUCE)
        break;
      if (localCartItem == null)
        return false;
      if (localCartItem.reduceItem(1) != 0)
        continue;
      this.dish_cart.remove(paramDishInfo.dishId);
      return true;
    }
    if (paramOperation == Operation.REMOVE)
    {
      if (localCartItem == null)
        return false;
      localCartItem.setItemCount(0);
      this.dish_cart.remove(paramDishInfo.dishId);
      return true;
    }
    return false;
  }

  private boolean updateFreeDish(DishInfo paramDishInfo)
  {
    Object localObject = (CartItem)this.dish_cart.get(paramDishInfo.dishId);
    if (localObject != null)
    {
      i = ((CartItem)localObject).getItemCount();
      j = paramDishInfo.bought;
      if (paramDishInfo.targetCount == 0)
        break label118;
    }
    label118: for (int i = (i + j) / paramDishInfo.targetCount * paramDishInfo.freeCount; ; i = 0)
    {
      ArrayList localArrayList = (ArrayList)this.free_cart.get(paramDishInfo.dishId);
      localObject = localArrayList;
      if (localArrayList == null)
      {
        localObject = new ArrayList();
        this.free_cart.put(paramDishInfo.dishId, localObject);
      }
      j = i - ((ArrayList)localObject).size();
      if (j != 0)
        break label124;
      return false;
      i = 0;
      break;
    }
    label124: if (j > 0)
    {
      i = 0;
      while (i < j)
      {
        addFreeDish(paramDishInfo.dishId, new CartFreeItem(paramDishInfo.freeItem));
        i += 1;
      }
      return true;
    }
    int j = Math.abs(j);
    i = 0;
    while (i < j)
    {
      deleteFreeDish(paramDishInfo.dishId);
      i += 1;
    }
    return true;
  }

  private void updateGenusTypeCountWhenDishCountChanged(DishInfo paramDishInfo, int paramInt)
  {
    int i = this.dishCountInGenusTypeArray.get(paramDishInfo.genusType);
    this.dishCountInGenusTypeArray.put(paramDishInfo.genusType, i + paramInt);
  }

  private void updateGenusTypeCountWhenSelectedGiftCountChanged(GiftInfo paramGiftInfo, int paramInt)
  {
    int i = this.dishCountInGenusTypeArray.get(paramGiftInfo.genusType);
    this.dishCountInGenusTypeArray.put(paramGiftInfo.genusType, i + paramInt);
  }

  private void updateTotalInfoWhenCartChanged()
  {
    this.totalDishCount = 0;
    this.totalSingleDishCount = 0;
    this.dishCountInGenusTypeArray.clear();
    this.totalFreeDishCount = 0;
    this.totalSelectedFreeDishCount = 0;
    this.totalPrice = new BigDecimal(0);
    this.totalOriginPrice = new BigDecimal(0);
    this.setDishInfos.clear();
    int i = 0;
    Object localObject1;
    Object localObject2;
    if (i < this.dish_cart.size())
    {
      localObject1 = (CartItem)this.dish_cart.valueAt(i);
      if (!((CartItem)localObject1).dishInfo.soldout)
      {
        this.totalDishCount += ((CartItem)localObject1).getItemCount();
        updateGenusTypeCountWhenDishCountChanged(((CartItem)localObject1).dishInfo, ((CartItem)localObject1).getItemCount());
        if (((CartItem)localObject1).dishInfo.isValidity)
        {
          if (((CartItem)localObject1).dishInfo.dishType != 1)
            break label296;
          this.totalSingleDishCount += ((CartItem)localObject1).getItemCount() * ((CartItem)localObject1).dishInfo.getSetDishesCount();
        }
      }
      while (true)
      {
        this.totalPrice = this.totalPrice.add(new BigDecimal(((CartItem)localObject1).dishInfo.currentPrice).multiply(new BigDecimal(((CartItem)localObject1).getItemCount())));
        this.totalOriginPrice = this.totalOriginPrice.add(new BigDecimal(((CartItem)localObject1).dishInfo.oldPrice).multiply(new BigDecimal(((CartItem)localObject1).getItemCount())));
        if (((CartItem)localObject1).dishInfo.dishType == 1)
        {
          localObject2 = new CartItem(((CartItem)localObject1).dishInfo);
          ((CartItem)localObject2).setItemCount(((CartItem)localObject1).getItemCount());
          this.setDishInfos.put(((CartItem)localObject1).dishInfo.dishId, localObject2);
        }
        i += 1;
        break;
        label296: this.totalSingleDishCount += ((CartItem)localObject1).getItemCount();
      }
    }
    i = 0;
    while (i < this.free_cart.size())
    {
      localObject1 = (ArrayList)this.free_cart.valueAt(i);
      localObject2 = ((ArrayList)localObject1).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        CartFreeItem localCartFreeItem = (CartFreeItem)((Iterator)localObject2).next();
        if ((!localCartFreeItem.use) || (localCartFreeItem.expired) || (localCartFreeItem.soldout))
          continue;
        this.totalSelectedFreeDishCount += 1;
        updateGenusTypeCountWhenSelectedGiftCountChanged(localCartFreeItem.giftInfo, 1);
      }
      this.totalFreeDishCount += ((ArrayList)localObject1).size();
      i += 1;
    }
  }

  private void updateTotalInfoWhenCartItemCountChanged(DishInfo paramDishInfo, Operation paramOperation, int paramInt)
  {
    int j = 1;
    int i = 1;
    if (paramOperation == Operation.ADD)
    {
      this.totalDishCount += paramInt;
      updateGenusTypeCountWhenDishCountChanged(paramDishInfo, paramInt);
      if (paramDishInfo.isValidity)
      {
        j = this.totalSingleDishCount;
        if (paramDishInfo.dishType == 1)
          i = paramDishInfo.getSetDishesCount();
        this.totalSingleDishCount = (i * paramInt + j);
        this.totalPrice = this.totalPrice.add(new BigDecimal(paramDishInfo.currentPrice).multiply(new BigDecimal(paramInt)));
        this.totalOriginPrice = this.totalOriginPrice.add(new BigDecimal(paramDishInfo.oldPrice).multiply(new BigDecimal(paramInt)));
      }
    }
    do
    {
      do
        return;
      while ((paramOperation != Operation.REDUCE) && (paramOperation != Operation.REMOVE));
      this.totalDishCount -= paramInt;
      updateGenusTypeCountWhenDishCountChanged(paramDishInfo, -paramInt);
    }
    while (!paramDishInfo.isValidity);
    int k = this.totalSingleDishCount;
    i = j;
    if (paramDishInfo.dishType == 1)
      i = paramDishInfo.getSetDishesCount();
    this.totalSingleDishCount = (k - i * paramInt);
    this.totalPrice = this.totalPrice.subtract(new BigDecimal(paramDishInfo.currentPrice).multiply(new BigDecimal(paramInt)));
    this.totalOriginPrice = this.totalOriginPrice.subtract(new BigDecimal(paramDishInfo.oldPrice).multiply(new BigDecimal(paramInt)));
  }

  public void addCartChangedListener(CartChangedListener paramCartChangedListener)
  {
    if (!this.cartChangedListeners.contains(paramCartChangedListener))
      this.cartChangedListeners.add(paramCartChangedListener);
  }

  public void addDish(DishInfo paramDishInfo)
  {
    operateDish(paramDishInfo, Operation.ADD, 1, true);
  }

  public void correctAll(CartItem[] paramArrayOfCartItem, CartFreeItem[] paramArrayOfCartFreeItem)
  {
    if (paramArrayOfCartItem != null)
      correctDishes(paramArrayOfCartItem);
    if (paramArrayOfCartFreeItem != null)
    {
      correctFreeDishes(paramArrayOfCartFreeItem);
      updateTotalInfoWhenCartChanged();
      notifyCountChanged();
    }
  }

  public void deleteCartStorage()
  {
    new File(CART_CACHE_FOLDER, String.valueOf(this.shopId) + String.valueOf(this.groupOnDealId) + "dishes").delete();
  }

  public void deleteDish(DishInfo paramDishInfo)
  {
    CartItem localCartItem = (CartItem)this.dish_cart.get(paramDishInfo.dishId);
    if (localCartItem == null)
      return;
    operateDish(paramDishInfo, Operation.REMOVE, localCartItem.getItemCount(), true);
  }

  public void emptyCart()
  {
    clearCartInfo();
    notifyDishChanged(null);
    notifyCountChanged();
  }

  public ArrayList<CartItem> getAllDishes()
  {
    ArrayList localArrayList = new ArrayList(this.dish_cart.size());
    int i = 0;
    while (i < this.dish_cart.size())
    {
      localArrayList.add(this.dish_cart.valueAt(i));
      i += 1;
    }
    return localArrayList;
  }

  public ArrayList<CartFreeItem> getAllFreeDishes()
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i < this.free_cart.size())
    {
      localArrayList.addAll((Collection)this.free_cart.valueAt(i));
      i += 1;
    }
    return localArrayList;
  }

  public ArrayList<CartFreeItem> getAllUsedFreeDished()
  {
    ArrayList localArrayList1 = new ArrayList();
    int i = 0;
    while (i < this.free_cart.size())
    {
      ArrayList localArrayList2 = (ArrayList)this.free_cart.valueAt(i);
      int j = 0;
      while (j < localArrayList2.size())
      {
        if (((CartFreeItem)localArrayList2.get(j)).use)
          localArrayList1.add(localArrayList2.get(j));
        j += 1;
      }
      i += 1;
    }
    return localArrayList1;
  }

  public int getDishCountByDishId(int paramInt)
  {
    CartItem localCartItem = (CartItem)this.dish_cart.get(paramInt);
    if (localCartItem == null)
      return 0;
    return localCartItem.getItemCount();
  }

  public GroupOnItem getGroupOnInfo()
  {
    return this.groupOnInfo;
  }

  public DPObject[] getMenuEntranceList()
  {
    if (this.dishMenuEntrance == null)
      return new DPObject[0];
    if (this.dishMenuEntrance.getInt("Show") != 1)
      return new DPObject[0];
    return this.dishMenuEntrance.getArray("DishMenuEntranceList");
  }

  public int getShopId()
  {
    return this.shopId;
  }

  public String getShopName()
  {
    return this.shopName;
  }

  public int getTotalDishCount()
  {
    return this.totalDishCount;
  }

  public int getTotalFreeDishCount()
  {
    return this.totalFreeDishCount;
  }

  public int getTotalMeatTypeDishCount()
  {
    return this.dishCountInGenusTypeArray.get(2);
  }

  public double getTotalOriginPrice()
  {
    this.totalOriginPrice = this.totalOriginPrice.setScale(2, 4);
    return this.totalOriginPrice.doubleValue();
  }

  public int getTotalOtherTypeDishCount()
  {
    return this.dishCountInGenusTypeArray.get(-1);
  }

  public double getTotalPrice()
  {
    this.totalPrice = this.totalPrice.setScale(2, 4);
    return this.totalPrice.doubleValue();
  }

  public int getTotalSelectFreeDishCount()
  {
    return this.totalSelectedFreeDishCount;
  }

  public int getTotalSingleDishCount()
  {
    return this.totalSingleDishCount;
  }

  public int getTotalVegetableTypeDishCount()
  {
    return this.dishCountInGenusTypeArray.get(3);
  }

  public boolean hasHistoryFreeDish()
  {
    ArrayList localArrayList = (ArrayList)this.free_cart.get(0);
    if (localArrayList == null);
    do
      return false;
    while (localArrayList.size() <= 0);
    return true;
  }

  public int isDishBought(int paramInt)
  {
    int j;
    if (this.groupOnInfo != null)
    {
      if (paramInt == this.groupOnInfo.dishId)
        return 1;
      if (this.groupOnInfo.groupOnSet != null)
      {
        j = this.groupOnInfo.groupOnSet.length;
        i = 0;
        while (true)
        {
          if (i >= j)
            break label80;
          if (paramInt == this.groupOnInfo.groupOnSet[i].getObject("Dish").getInt("Id"))
            break;
          i += 1;
        }
      }
    }
    label80: int i = 0;
    while (i < this.setDishInfos.size())
    {
      CartItem localCartItem = (CartItem)this.setDishInfos.valueAt(i);
      int k = localCartItem.dishInfo.setItems.size();
      j = 0;
      while (j < k)
      {
        if (paramInt == ((SetItem)localCartItem.dishInfo.setItems.get(j)).id)
          return 2;
        j += 1;
      }
      i += 1;
    }
    return 0;
  }

  public void operateFreeDish(CartFreeItem paramCartFreeItem)
  {
    int j = 0;
    int i = 0;
    if (i < this.free_cart.size())
    {
      boolean bool;
      if (((ArrayList)this.free_cart.valueAt(i)).contains(paramCartFreeItem))
      {
        if (paramCartFreeItem.use)
          break label84;
        bool = true;
        label43: paramCartFreeItem.use = bool;
        j = 1;
        if (!paramCartFreeItem.use)
          break label90;
        this.totalSelectedFreeDishCount += 1;
        updateGenusTypeCountWhenSelectedGiftCountChanged(paramCartFreeItem.giftInfo, 1);
      }
      while (true)
      {
        i += 1;
        break;
        label84: bool = false;
        break label43;
        label90: this.totalSelectedFreeDishCount -= 1;
        updateGenusTypeCountWhenSelectedGiftCountChanged(paramCartFreeItem.giftInfo, -1);
      }
    }
    if (j != 0)
      notifyCountChanged();
  }

  public void reduceDish(DishInfo paramDishInfo)
  {
    if ((CartItem)this.dish_cart.get(paramDishInfo.dishId) == null)
      return;
    operateDish(paramDishInfo, Operation.REDUCE, 1, true);
  }

  public void removeCartChangedListener(CartChangedListener paramCartChangedListener)
  {
    this.cartChangedListeners.remove(paramCartChangedListener);
  }

  public void setDishCartItems(CartItem[] paramArrayOfCartItem)
  {
    if (paramArrayOfCartItem == null)
      return;
    clearCartInfo();
    int j = paramArrayOfCartItem.length;
    int i = 0;
    while (i < j)
    {
      CartItem localCartItem = paramArrayOfCartItem[i];
      operateDish(localCartItem.dishInfo, Operation.ADD, localCartItem.getItemCount(), false);
      i += 1;
    }
    sortDishes();
    notifyDishChanged(null);
    notifyFreeDishChanged();
    notifyGroupOnOrSetChanged();
    notifyCountChanged();
  }

  public void setDishHistoryCount(int paramInt1, int paramInt2)
  {
    CartItem localCartItem = (CartItem)this.dish_cart.get(paramInt1);
    if (localCartItem == null);
    do
    {
      do
        return;
      while (localCartItem.dishInfo.bought == paramInt2);
      localCartItem.dishInfo.bought = paramInt2;
    }
    while (!updateFreeDish(localCartItem.dishInfo));
    notifyFreeDishChanged();
    notifyCountChanged();
  }

  public void setGroupOnInfo(DPObject paramDPObject)
  {
    if (paramDPObject.getInt("DishId") >= 0)
      this.groupOnInfo = new GroupOnItem(paramDPObject);
  }

  public void setMenuEntrance(DPObject paramDPObject)
  {
    this.dishMenuEntrance = paramDPObject;
  }

  public void setOrderId(String paramString)
  {
    this.orderId = paramString;
  }

  public void setShopIdandDealId(int paramInt1, int paramInt2)
  {
    this.shopId = paramInt1;
    this.groupOnDealId = paramInt2;
    clearCartInfo();
    loadCartFromFile();
    paramInt1 = 0;
    while (paramInt1 < this.dish_cart.size())
    {
      updateFreeDish(((CartItem)this.dish_cart.valueAt(paramInt1)).dishInfo);
      paramInt1 += 1;
    }
    updateTotalInfoWhenCartChanged();
  }

  public void setShopName(String paramString)
  {
    this.shopName = paramString;
  }

  public void setSupportPrePay(boolean paramBoolean)
  {
    this.supportPrepay = paramBoolean;
  }

  public void setSupportTable(boolean paramBoolean)
  {
    this.supportTable = paramBoolean;
  }

  public void setUserHistoryGifts(GiftInfo[] paramArrayOfGiftInfo)
  {
    if (paramArrayOfGiftInfo != null)
    {
      Object localObject2 = (ArrayList)this.free_cart.get(0);
      Object localObject1 = localObject2;
      if (localObject2 == null)
      {
        localObject1 = new ArrayList();
        this.free_cart.put(0, localObject1);
      }
      int i = 0;
      while (i < paramArrayOfGiftInfo.length)
      {
        localObject2 = new CartFreeItem(paramArrayOfGiftInfo[i]);
        if ((this.exchangedGiftId != 0) && (this.exchangedGiftId == paramArrayOfGiftInfo[i].giftId))
        {
          ((CartFreeItem)localObject2).use = true;
          this.totalSelectedFreeDishCount += 1;
          updateGenusTypeCountWhenSelectedGiftCountChanged(((CartFreeItem)localObject2).giftInfo, 1);
        }
        ((ArrayList)localObject1).add(localObject2);
        this.totalFreeDishCount += 1;
        i += 1;
      }
      notifyFreeDishChanged();
      notifyCountChanged();
    }
  }

  public void sortDishes()
  {
    int j = 0;
    int m = this.dish_cart.size();
    int k = 0;
    int i = 0;
    if (i < m)
    {
      int n = this.dish_cart.keyAt(k);
      CartItem localCartItem = (CartItem)this.dish_cart.valueAt(k);
      if (!localCartItem.dishInfo.isValidity)
      {
        this.dish_cart.removeAt(k);
        this.dish_cart.put(n, localCartItem);
        j = 1;
      }
      while (true)
      {
        i += 1;
        break;
        k += 1;
      }
    }
    if (j != 0)
      notifyDishChanged(null);
  }

  public void storeCart()
  {
    if (this.shopId == 0)
      return;
    int k = 1;
    int i = 0;
    while (true)
    {
      int j = k;
      File localFile;
      if (i < this.free_cart.size())
      {
        if (this.free_cart.keyAt(i) != 0)
          j = 0;
      }
      else
      {
        if (((this.dish_cart.size() == 0) && (j != 0)) || (!CART_CACHE_FOLDER.exists()))
          break;
        localFile = new File(CART_CACHE_FOLDER, String.valueOf(this.shopId) + String.valueOf(this.groupOnDealId) + "dishes");
        if (localFile.exists());
      }
      try
      {
        localFile.createNewFile();
        Parcel localParcel = Parcel.obtain();
        j = this.dish_cart.size();
        localParcel.writeInt(j);
        i = 0;
        while (i < j)
        {
          localParcel.writeInt(this.dish_cart.keyAt(i));
          localParcel.writeSerializable((Serializable)this.dish_cart.valueAt(i));
          i += 1;
          continue;
          i += 1;
        }
      }
      catch (IOException localIOException)
      {
        while (true)
          localIOException.printStackTrace();
        localIOException.setDataPosition(0);
        byte[] arrayOfByte = localIOException.marshall();
        localIOException.recycle();
        FileUtils.put(localFile, arrayOfByte);
      }
    }
  }

  public static abstract interface CartChangedListener
  {
    public abstract void onCountChanged();

    public abstract void onDishChanged(CartItem paramCartItem);

    public abstract void onFreeDishChanged(ArrayList<CartFreeItem> paramArrayList);

    public abstract void onGroupOnOrSetChanged();
  }

  private static class NewCartManagerInner
  {
    private static NewCartManager INSTANCE = new NewCartManager(null);
  }

  private static enum Operation
  {
    static
    {
      ADD = new Operation("ADD", 1);
      REDUCE = new Operation("REDUCE", 2);
      $VALUES = new Operation[] { REMOVE, ADD, REDUCE };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.NewCartManager
 * JD-Core Version:    0.6.0
 */