package com.dianping.selectdish;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiFormInputStream;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.model.CartOperation;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.model.GiftInfo;
import com.dianping.selectdish.model.SetItem;
import java.io.File;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TogetherCartManager
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final File CART_CACHE_FOLDER = new File(DPApplication.instance().getCacheDir(), "selectdish");
  private static final int FREE_DISH_ID = 0;
  public static final int MISTAKE_CODE_ROOM_INVALID = 100;
  public static final int MISTAKE_CODE_SERVER_ERROR = 500;
  public static final int MISTAKE_CODE_SERVER_SUCCESS = 200;
  private static final int MSG_CART_POLLING = 1;
  private static final int ORDER_AVAILABLE_TYPE = 0;
  public double average = 0.0D;
  private ArrayList<TogetherCartChangedListener> cartChangedListeners = new ArrayList();
  private final SparseIntArray dishCountInGenusTypeArray = new SparseIntArray();
  private final SparseArray<ArrayList<CartFreeItem>> free_cart = new SparseArray();
  private SparseIntArray genusInOtherDish = new SparseIntArray();
  private MyHandler handler = new MyHandler(this);
  public int interval = 2000;
  public boolean isEstimate = false;
  public int isOwner = 0;
  private boolean isRequiring;
  private final ArrayList<CartOperation> operation_after = new ArrayList();
  private final ArrayList<CartOperation> operation_before = new ArrayList();
  public String orderButtonSubTitle = null;
  private int otherTotalCount;
  private BigDecimal otherTotalOriginPrice = new BigDecimal(0);
  private BigDecimal otherTotalPrice = new BigDecimal(0);
  private final SparseArray<CartItem> other_dish_cart = new SparseArray();
  public int peopleNum = 0;
  public int roomId;
  private final SparseArray<CartItem> self_dish_cart = new SparseArray();
  private final SparseArray<CartItem> setDishInfos = new SparseArray();
  private int shopId = 0;
  private String shopName = null;
  public boolean supportPrepay;
  public boolean supportTable;
  private MApiRequest syncDishRequest = null;
  public String tableId;
  private int totalDishCount = 0;
  private int totalFreeDishCount = 0;
  private BigDecimal totalOriginPrice = new BigDecimal(0);
  private BigDecimal totalPrice = new BigDecimal(0);
  private int totalSelectedFreeDishCount = 0;
  private int totalSingleDishCount = 0;
  private final SparseArray<CartItem> total_dish_cart = new SparseArray();

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

  private void calOtherCartData()
  {
    this.otherTotalCount = 0;
    this.otherTotalPrice = new BigDecimal(0);
    this.otherTotalOriginPrice = new BigDecimal(0);
    int i = 0;
    while (i < this.other_dish_cart.size())
    {
      CartItem localCartItem = (CartItem)this.other_dish_cart.valueAt(i);
      if (!localCartItem.dishInfo.soldout)
      {
        this.otherTotalCount += localCartItem.getItemCount();
        if (localCartItem.dishInfo.isValidity)
        {
          this.otherTotalPrice = this.otherTotalPrice.add(new BigDecimal(localCartItem.dishInfo.currentPrice).multiply(new BigDecimal(localCartItem.getItemCount())));
          this.otherTotalOriginPrice = this.otherTotalOriginPrice.add(new BigDecimal(localCartItem.dishInfo.oldPrice).multiply(new BigDecimal(localCartItem.getItemCount())));
        }
      }
      i += 1;
    }
  }

  private void calcGenusFromOtherDish()
  {
    this.genusInOtherDish.clear();
    int n = this.other_dish_cart.size();
    int i = 0;
    int m = 0;
    int k = 0;
    int j = 0;
    if (j < n)
    {
      CartItem localCartItem = (CartItem)this.other_dish_cart.valueAt(j);
      if (localCartItem.dishInfo.genusType == 3)
        i += localCartItem.getItemCount();
      while (true)
      {
        j += 1;
        break;
        if (localCartItem.dishInfo.genusType == 2)
        {
          m += localCartItem.getItemCount();
          continue;
        }
        k += localCartItem.getItemCount();
      }
    }
    this.genusInOtherDish.put(3, i);
    this.genusInOtherDish.put(2, m);
    this.genusInOtherDish.put(-1, k);
  }

  private void clearCartInfo()
  {
    this.totalDishCount = 0;
    this.totalSingleDishCount = 0;
    this.totalFreeDishCount = 0;
    this.totalSelectedFreeDishCount = 0;
    this.dishCountInGenusTypeArray.clear();
    this.totalPrice = new BigDecimal(0);
    this.totalOriginPrice = new BigDecimal(0);
    this.self_dish_cart.clear();
    this.other_dish_cart.clear();
    this.total_dish_cart.clear();
    this.operation_before.clear();
    this.operation_after.clear();
    this.free_cart.clear();
    this.setDishInfos.clear();
  }

  private boolean compareCart(DPObject[] paramArrayOfDPObject1, DPObject[] paramArrayOfDPObject2)
  {
    int k = paramArrayOfDPObject1.length;
    int j = paramArrayOfDPObject2.length;
    if (this.isOwner == 1)
    {
      localSparseArray = new SparseArray();
      i = 0;
      while (i < k)
      {
        localSparseArray.put(paramArrayOfDPObject1[i].getObject("MenuItem").getInt("Id"), Integer.valueOf(paramArrayOfDPObject1[i].getInt("Count")));
        i += 1;
      }
      i = 0;
      if (i < j)
      {
        k = paramArrayOfDPObject2[i].getObject("MenuItem").getInt("Id");
        m = paramArrayOfDPObject2[i].getInt("Count");
        paramArrayOfDPObject1 = (Integer)localSparseArray.get(k);
        if (paramArrayOfDPObject1 == null)
          localSparseArray.put(k, Integer.valueOf(m));
        while (true)
        {
          i += 1;
          break;
          localSparseArray.put(k, Integer.valueOf(paramArrayOfDPObject1.intValue() + m));
        }
      }
      if (localSparseArray.size() != this.total_dish_cart.size())
        return true;
      j = this.total_dish_cart.size();
      i = 0;
      while (i < j)
      {
        if ((localSparseArray.keyAt(i) != this.total_dish_cart.keyAt(i)) || (!((Integer)localSparseArray.valueAt(i)).equals(Integer.valueOf(((CartItem)this.total_dish_cart.valueAt(i)).getItemCount()))))
          return true;
        i += 1;
      }
      return false;
    }
    if ((k != this.total_dish_cart.size()) || (j != this.other_dish_cart.size()))
      return true;
    int m = this.total_dish_cart.size();
    SparseArray localSparseArray = new SparseArray();
    int i = 0;
    while (i < k)
    {
      localSparseArray.put(paramArrayOfDPObject1[i].getObject("MenuItem").getInt("Id"), Integer.valueOf(paramArrayOfDPObject1[i].getInt("Count")));
      i += 1;
    }
    i = 0;
    while (i < m)
    {
      if ((localSparseArray.keyAt(i) != this.total_dish_cart.keyAt(i)) || (!((Integer)localSparseArray.valueAt(i)).equals(Integer.valueOf(((CartItem)this.total_dish_cart.valueAt(i)).getItemCount()))))
        return true;
      i += 1;
    }
    paramArrayOfDPObject1 = new SparseArray();
    i = 0;
    while (i < j)
    {
      paramArrayOfDPObject1.put(paramArrayOfDPObject2[i].getObject("MenuItem").getInt("Id"), Integer.valueOf(paramArrayOfDPObject2[i].getInt("Count")));
      i += 1;
    }
    i = 0;
    while (i < j)
    {
      if ((paramArrayOfDPObject1.keyAt(i) != this.other_dish_cart.keyAt(i)) || (!((Integer)paramArrayOfDPObject1.valueAt(i)).equals(Integer.valueOf(((CartItem)this.other_dish_cart.valueAt(i)).getItemCount()))))
        return true;
      i += 1;
    }
    return false;
  }

  private void correctDishes(CartItem[] paramArrayOfCartItem)
  {
    int k = 0;
    int j = 0;
    int i1 = 0;
    Object localObject = new HashSet(this.total_dish_cart.size());
    int i = 0;
    while (i < this.total_dish_cart.size())
    {
      ((Set)localObject).add(Integer.valueOf(this.total_dish_cart.keyAt(i)));
      i += 1;
    }
    int n = 0;
    i = k;
    if (n < paramArrayOfCartItem.length)
    {
      CartItem localCartItem1 = paramArrayOfCartItem[n];
      CartItem localCartItem2 = (CartItem)this.total_dish_cart.get(localCartItem1.dishInfo.dishId);
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
      localObject = (CartItem)this.total_dish_cart.get(((Integer)localObject).intValue());
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

  public static TogetherCartManager getInstance()
  {
    return TogetherCartManagerInner.INSTANCE;
  }

  private void notifyCountChanged()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((TogetherCartChangedListener)localIterator.next()).onCountChanged();
  }

  private void notifyDishChanged(CartItem paramCartItem)
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((TogetherCartChangedListener)localIterator.next()).onDishChanged(paramCartItem);
  }

  private void notifyFreeDishChanged()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((TogetherCartChangedListener)localIterator.next()).onFreeDishChanged(getAllFreeDishes());
  }

  private void notifyGroupOnOrSetChanged()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((TogetherCartChangedListener)localIterator.next()).onGroupOnOrSetChanged();
  }

  private void notifyManulSyncComplete()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((TogetherCartChangedListener)localIterator.next()).onManulRefreshComplete();
  }

  private void notifyMistake(int paramInt, String paramString)
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((TogetherCartChangedListener)localIterator.next()).onMistakeRecieved(paramInt, paramString);
  }

  private void notifySyncComplete()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((TogetherCartChangedListener)localIterator.next()).onSyncComplete();
  }

  private void operateDish(DishInfo paramDishInfo, Operation paramOperation, int paramInt, boolean paramBoolean)
  {
    CartItem localCartItem = null;
    if (paramOperation != Operation.ADD)
      localCartItem = (CartItem)this.total_dish_cart.get(paramDishInfo.dishId);
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
      localCartItem = (CartItem)this.total_dish_cart.get(paramDishInfo.dishId);
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

  private void updateAllFreeDishes()
  {
    if (this.isOwner == 1)
    {
      int j = this.total_dish_cart.size();
      int i = 0;
      while (i < j)
      {
        updateFreeDish(((CartItem)this.total_dish_cart.valueAt(i)).dishInfo);
        i += 1;
      }
    }
  }

  private boolean updateDishCartDataWithDishOperation(DishInfo paramDishInfo, Operation paramOperation)
  {
    int i = 1;
    CartItem localCartItem = (CartItem)this.total_dish_cart.get(paramDishInfo.dishId);
    switch (1.$SwitchMap$com$dianping$selectdish$TogetherCartManager$Operation[paramOperation.ordinal()])
    {
    default:
      i = 0;
    case 1:
    case 2:
      do
      {
        return i;
        paramOperation = localCartItem;
        if (localCartItem == null)
        {
          paramOperation = new CartItem(paramDishInfo);
          this.total_dish_cart.put(paramDishInfo.dishId, paramOperation);
        }
        storeOperation(paramOperation, 1);
        paramOperation.addItem(1);
        return true;
        if (localCartItem == null)
          return false;
        storeOperation(localCartItem, -1);
      }
      while (localCartItem.reduceItem(1) != 0);
      this.total_dish_cart.remove(paramDishInfo.dishId);
      return true;
    case 3:
    }
    if (localCartItem == null)
      return false;
    storeOperation(localCartItem, -localCartItem.getItemCount());
    localCartItem.setItemCount(0);
    this.total_dish_cart.remove(paramDishInfo.dishId);
    return true;
  }

  private boolean updateFreeDish(DishInfo paramDishInfo)
  {
    Object localObject = (CartItem)this.total_dish_cart.get(paramDishInfo.dishId);
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
    if (i < this.total_dish_cart.size())
    {
      localObject1 = (CartItem)this.total_dish_cart.valueAt(i);
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

  public void addCartChangedListener(TogetherCartChangedListener paramTogetherCartChangedListener)
  {
    if (!this.cartChangedListeners.contains(paramTogetherCartChangedListener))
      this.cartChangedListeners.add(paramTogetherCartChangedListener);
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

  public void deleteDish(DishInfo paramDishInfo)
  {
    CartItem localCartItem = (CartItem)this.total_dish_cart.get(paramDishInfo.dishId);
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

  public ArrayList<CartItem> getAllDishesinOtherDish()
  {
    int j = this.other_dish_cart.size();
    ArrayList localArrayList = new ArrayList(j);
    int i = 0;
    while (i < j)
    {
      localArrayList.add(this.other_dish_cart.valueAt(i));
      i += 1;
    }
    return localArrayList;
  }

  public ArrayList<CartItem> getAllDishesinSelfDish()
  {
    int j = this.self_dish_cart.size();
    ArrayList localArrayList = new ArrayList(j);
    int i = 0;
    while (i < j)
    {
      localArrayList.add(this.self_dish_cart.valueAt(i));
      i += 1;
    }
    return localArrayList;
  }

  public ArrayList<CartItem> getAllDishesinTotalDish()
  {
    int j = this.total_dish_cart.size();
    ArrayList localArrayList = new ArrayList(j);
    int i = 0;
    while (i < j)
    {
      localArrayList.add(this.total_dish_cart.valueAt(i));
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

  public int getDishCountByDishIdinOtherDish(int paramInt)
  {
    CartItem localCartItem = (CartItem)this.other_dish_cart.get(paramInt);
    if (localCartItem == null)
      return 0;
    return localCartItem.getItemCount();
  }

  public int getDishCountByDishIdinSelfDish(int paramInt)
  {
    CartItem localCartItem = (CartItem)this.self_dish_cart.get(paramInt);
    if (localCartItem == null)
      return 0;
    return localCartItem.getItemCount();
  }

  public int getDishCountByDishIdinTotalDish(int paramInt)
  {
    CartItem localCartItem = (CartItem)this.total_dish_cart.get(paramInt);
    if (localCartItem == null)
      return 0;
    return localCartItem.getItemCount();
  }

  public int getGenusFromOtherDish(int paramInt)
  {
    return this.genusInOtherDish.get(paramInt);
  }

  public int getGenusTypeDishCount(int paramInt)
  {
    return this.dishCountInGenusTypeArray.get(paramInt);
  }

  public int getOtherTotalCount()
  {
    return this.otherTotalCount;
  }

  public double getOtherTotalOriginPrice()
  {
    this.otherTotalOriginPrice = this.otherTotalOriginPrice.setScale(2, 4);
    return this.otherTotalOriginPrice.doubleValue();
  }

  public double getOtherTotalPrice()
  {
    this.otherTotalPrice = this.otherTotalPrice.setScale(2, 4);
    return this.otherTotalPrice.doubleValue();
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

  public double getTotalOriginPrice()
  {
    this.totalOriginPrice = this.totalOriginPrice.setScale(2, 4);
    return this.totalOriginPrice.doubleValue();
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
    int i = 0;
    Object localObject;
    int k;
    int j;
    while (i < this.setDishInfos.size())
    {
      localObject = (CartItem)this.setDishInfos.valueAt(i);
      k = ((CartItem)localObject).dishInfo.setItems.size();
      j = 0;
      while (j < k)
      {
        if (paramInt == ((SetItem)((CartItem)localObject).dishInfo.setItems.get(j)).id)
          return 2;
        j += 1;
      }
      i += 1;
    }
    if (this.isOwner == 0)
    {
      k = this.other_dish_cart.size();
      i = 0;
      while (i < k)
      {
        localObject = ((CartItem)this.other_dish_cart.valueAt(i)).dishInfo;
        if (((DishInfo)localObject).dishType == 1)
        {
          int m = ((DishInfo)localObject).setItems.size();
          j = 0;
          while (j < m)
          {
            if (paramInt == ((SetItem)((DishInfo)localObject).setItems.get(j)).id)
              return 2;
            j += 1;
          }
        }
        i += 1;
      }
    }
    return 0;
  }

  public boolean isDishboughtByPartners(int paramInt)
  {
    return (CartItem)this.other_dish_cart.get(paramInt) != null;
  }

  public void mergeCart()
  {
    int i;
    CartItem localCartItem;
    if (this.isOwner == 1)
    {
      i = 0;
      while (i < this.self_dish_cart.size())
      {
        localCartItem = new CartItem(((CartItem)this.self_dish_cart.valueAt(i)).dishInfo);
        localCartItem.setItemCount(((CartItem)this.self_dish_cart.valueAt(i)).getItemCount());
        this.total_dish_cart.put(this.self_dish_cart.keyAt(i), localCartItem);
        i += 1;
      }
      i = 0;
      if (i < this.other_dish_cart.size())
      {
        int j = this.other_dish_cart.keyAt(i);
        localCartItem = (CartItem)this.total_dish_cart.get(j);
        if (localCartItem == null)
        {
          localCartItem = new CartItem(((CartItem)this.other_dish_cart.valueAt(i)).dishInfo);
          localCartItem.setItemCount(((CartItem)this.other_dish_cart.valueAt(i)).getItemCount());
          this.total_dish_cart.put(j, localCartItem);
        }
        while (true)
        {
          i += 1;
          break;
          localCartItem.addItem(((CartItem)this.other_dish_cart.valueAt(i)).getItemCount());
        }
      }
    }
    else
    {
      i = 0;
      while (i < this.self_dish_cart.size())
      {
        localCartItem = new CartItem(((CartItem)this.self_dish_cart.valueAt(i)).dishInfo);
        localCartItem.setItemCount(((CartItem)this.self_dish_cart.valueAt(i)).getItemCount());
        this.total_dish_cart.put(this.self_dish_cart.keyAt(i), localCartItem);
        i += 1;
      }
    }
  }

  public void mergeOperation()
  {
    SparseArray localSparseArray = new SparseArray();
    int j = this.operation_before.size();
    int i = 0;
    if (i < j)
    {
      int k = ((CartOperation)this.operation_before.get(i)).cartItem.dishInfo.dishId;
      CartOperation localCartOperation = (CartOperation)localSparseArray.get(k);
      if (localCartOperation == null)
        localSparseArray.put(k, this.operation_before.get(i));
      while (true)
      {
        i += 1;
        break;
        k = localCartOperation.countChange;
        localCartOperation.countChange = (((CartOperation)this.operation_before.get(i)).countChange + k);
      }
    }
    this.operation_before.clear();
    i = 0;
    while (i < localSparseArray.size())
    {
      this.operation_before.add(localSparseArray.valueAt(i));
      i += 1;
    }
  }

  public void mergeOperationToTotalCart(ArrayList<CartOperation> paramArrayList)
  {
    int k = paramArrayList.size();
    int i = 0;
    if (i < k)
    {
      int m = ((CartOperation)paramArrayList.get(i)).cartItem.dishInfo.dishId;
      int n = ((CartOperation)paramArrayList.get(i)).countChange;
      CartItem localCartItem = (CartItem)this.total_dish_cart.get(m);
      int j;
      if (localCartItem == null)
      {
        j = 0;
        localCartItem = ((CartOperation)paramArrayList.get(i)).cartItem;
        this.total_dish_cart.put(localCartItem.dishInfo.dishId, localCartItem);
        label93: if (n + j > 0)
          break label126;
        this.total_dish_cart.remove(m);
      }
      while (true)
      {
        i += 1;
        break;
        j = localCartItem.getItemCount();
        break label93;
        label126: localCartItem.setItemCount(n + j);
      }
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.syncDishRequest = null;
    this.operation_before.addAll(this.operation_after);
    this.operation_after.clear();
    this.handler.sendEmptyMessageDelayed(1, this.interval);
    this.isRequiring = false;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.syncDishRequest = null;
    paramMApiRequest = paramMApiResponse.result();
    Object localObject1;
    if ((paramMApiRequest instanceof DPObject))
    {
      localObject1 = (DPObject)paramMApiRequest;
      switch (((DPObject)localObject1).getInt("Code"))
      {
      default:
      case 100:
      case 500:
      case 200:
      }
    }
    while (true)
    {
      this.isRequiring = false;
      this.handler.sendEmptyMessageDelayed(1, this.interval);
      return;
      notifyMistake(100, ((DPObject)localObject1).getString("Message"));
      continue;
      notifyMistake(500, ((DPObject)localObject1).getString("Message"));
      this.operation_before.addAll(this.operation_after);
      this.operation_after.clear();
      continue;
      paramMApiResponse = ((DPObject)localObject1).getArray("SelfCartInfos");
      paramMApiRequest = ((DPObject)localObject1).getArray("OthersCartInfos");
      if ((this.peopleNum != ((DPObject)localObject1).getInt("PeopleNumber")) || (compareCart(paramMApiResponse, paramMApiRequest)));
      Object localObject2;
      for (int i = 1; ; i = 0)
      {
        this.peopleNum = ((DPObject)localObject1).getInt("PeopleNumber");
        this.operation_before.clear();
        this.operation_before.addAll(this.operation_after);
        this.operation_after.clear();
        this.self_dish_cart.clear();
        this.other_dish_cart.clear();
        this.total_dish_cart.clear();
        k = paramMApiResponse.length;
        j = 0;
        while (j < k)
        {
          localObject1 = paramMApiResponse[j];
          localObject2 = new DishInfo(((DPObject)localObject1).getObject("MenuItem"));
          CartItem localCartItem = new CartItem((DishInfo)localObject2);
          localCartItem.setItemCount(((DPObject)localObject1).getInt("Count"));
          this.self_dish_cart.put(((DishInfo)localObject2).dishId, localCartItem);
          j += 1;
        }
      }
      int k = paramMApiRequest.length;
      int j = 0;
      while (j < k)
      {
        paramMApiResponse = paramMApiRequest[j];
        localObject1 = new DishInfo(paramMApiResponse.getObject("MenuItem"));
        localObject2 = new CartItem((DishInfo)localObject1);
        ((CartItem)localObject2).setItemCount(paramMApiResponse.getInt("Count"));
        this.other_dish_cart.put(((DishInfo)localObject1).dishId, localObject2);
        j += 1;
      }
      calcGenusFromOtherDish();
      calOtherCartData();
      mergeCart();
      mergeOperation();
      mergeOperationToTotalCart(this.operation_before);
      updateAllFreeDishes();
      updateTotalInfoWhenCartChanged();
      if (i != 0)
        notifySyncComplete();
      notifyManulSyncComplete();
    }
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
    if ((CartItem)this.total_dish_cart.get(paramDishInfo.dishId) == null)
      return;
    operateDish(paramDishInfo, Operation.REDUCE, 1, true);
  }

  public void removeCartChangedListener(TogetherCartChangedListener paramTogetherCartChangedListener)
  {
    this.cartChangedListeners.remove(paramTogetherCartChangedListener);
  }

  public void setDishHistoryCount(int paramInt1, int paramInt2)
  {
    CartItem localCartItem = (CartItem)this.total_dish_cart.get(paramInt1);
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

  public void setShopIdandDealId(int paramInt1, int paramInt2)
  {
    this.shopId = paramInt1;
    this.roomId = paramInt2;
    clearCartInfo();
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
      ArrayList localArrayList2 = (ArrayList)this.free_cart.get(0);
      ArrayList localArrayList1 = localArrayList2;
      if (localArrayList2 == null)
      {
        localArrayList1 = new ArrayList();
        this.free_cart.put(0, localArrayList1);
      }
      int i = 0;
      while (i < paramArrayOfGiftInfo.length)
      {
        localArrayList1.add(new CartFreeItem(paramArrayOfGiftInfo[i]));
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
    int m = this.total_dish_cart.size();
    int k = 0;
    int i = 0;
    if (i < m)
    {
      int n = this.total_dish_cart.keyAt(k);
      CartItem localCartItem = (CartItem)this.total_dish_cart.valueAt(k);
      if (!localCartItem.dishInfo.isValidity)
      {
        this.total_dish_cart.removeAt(k);
        this.total_dish_cart.put(n, localCartItem);
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

  public void startPollingRequeset()
  {
    this.handler.removeMessages(1);
    syncCart();
  }

  public void stopPollingRequeset()
  {
    this.handler.removeMessages(1);
    if (this.syncDishRequest != null)
    {
      DPApplication.instance().mapiService().abort(this.syncDishRequest, this, true);
      this.syncDishRequest = null;
    }
  }

  public void storeOperation(CartItem paramCartItem, int paramInt)
  {
    if (this.isRequiring)
    {
      this.operation_after.add(new CartOperation(paramCartItem, paramInt));
      return;
    }
    this.operation_before.add(new CartOperation(paramCartItem, paramInt));
  }

  public void syncCart()
  {
    if (this.syncDishRequest != null)
      DPApplication.instance().mapiService().abort(this.syncDishRequest, this, false);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/uploadcartinfo.hbt").buildUpon();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("roomid");
    localArrayList.add(String.valueOf(this.roomId));
    localArrayList.add("shopid");
    localArrayList.add(String.valueOf(this.shopId));
    localArrayList.add("cartinfo");
    JSONArray localJSONArray = new JSONArray();
    mergeOperation();
    while (true)
    {
      Object localObject;
      try
      {
        Iterator localIterator = this.operation_before.iterator();
        if (!localIterator.hasNext())
          continue;
        CartOperation localCartOperation = (CartOperation)localIterator.next();
        int j = localCartOperation.cartItem.dishInfo.dishId;
        localObject = (CartItem)this.self_dish_cart.get(j);
        if (localObject == null)
        {
          i = 0;
          localObject = new JSONObject();
          ((JSONObject)localObject).put("dishId", j);
          ((JSONObject)localObject).put("fromCount", i);
          ((JSONObject)localObject).put("toCount", localCartOperation.countChange + i);
          localJSONArray.put(localObject);
          continue;
        }
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
        localArrayList.add(localJSONArray.toString());
        this.syncDishRequest = new BasicMApiRequest(localBuilder.toString(), "POST", new MApiFormInputStream((String[])localArrayList.toArray(new String[localArrayList.size()])), CacheType.DISABLED, false, null, 15000L);
        DPApplication.instance().mapiService().exec(this.syncDishRequest, this);
        this.isRequiring = true;
        return;
      }
      int i = ((CartItem)localObject).getItemCount();
    }
  }

  private static class MyHandler extends Handler
  {
    private final WeakReference<TogetherCartManager> cartManager;

    public MyHandler(TogetherCartManager paramTogetherCartManager)
    {
      this.cartManager = new WeakReference(paramTogetherCartManager);
    }

    public void handleMessage(Message paramMessage)
    {
      TogetherCartManager localTogetherCartManager = (TogetherCartManager)this.cartManager.get();
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
      }
      localTogetherCartManager.syncCart();
    }
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

  public static abstract interface TogetherCartChangedListener
  {
    public abstract void onCountChanged();

    public abstract void onDishChanged(CartItem paramCartItem);

    public abstract void onFreeDishChanged(ArrayList<CartFreeItem> paramArrayList);

    public abstract void onGroupOnOrSetChanged();

    public abstract void onManulRefreshComplete();

    public abstract void onMistakeRecieved(int paramInt, String paramString);

    public abstract void onSyncComplete();
  }

  private static class TogetherCartManagerInner
  {
    private static TogetherCartManager INSTANCE = new TogetherCartManager(null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.TogetherCartManager
 * JD-Core Version:    0.6.0
 */