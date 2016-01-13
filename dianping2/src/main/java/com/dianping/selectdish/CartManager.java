package com.dianping.selectdish;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.model.GiftInfo;
import com.dianping.selectdish.model.GroupOnItem;
import com.dianping.selectdish.model.SetItem;
import com.dianping.util.FileUtils;
import com.dianping.util.Log;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.List<Lcom.dianping.selectdish.model.CartFreeItem;>;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CartManager
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final File CART_CACHE_FOLDER;
  private static final Integer FREE_DISH_ID = Integer.valueOf(0);
  private static final String TAG = "CartManager";
  private static final int VERSION = 120;
  public double average = 0.0D;
  private Set<Integer> cachedDishIds = new HashSet();
  private ArrayList<CartChangedListener> cartChangedListeners = new ArrayList();
  private LinkedHashMap<Integer, CartItem> dish_cart = new LinkedHashMap();
  private int exchangedGiftId = 0;
  private LinkedHashMap<Integer, ArrayList<CartFreeItem>> free_cart = new LinkedHashMap();
  public GiftInfo[] giftsInEditOrder = null;
  public int groupOnDealId = 0;
  private GroupOnItem groupOnInfo = null;
  public int groupOnOrderId = 0;
  public boolean isEstimate = false;
  private BigDecimal oldtotalPrice = new BigDecimal(0);
  public String orderButtonSubTitle = null;
  private String orderId = null;
  public String orderViewId = null;
  private LinkedHashMap<Integer, CartItem> setDishInfos = new LinkedHashMap();
  private int shopId = 0;
  private String shopName = null;
  private boolean supportPrepay;
  private boolean supportTable;
  public String tableId;
  private int totalDishCount = 0;
  private int totalFreeDishCount = 0;
  private BigDecimal totalPrice = new BigDecimal(0);
  private int totalSelectedFreeDishCount = 0;
  private int totalSingleDishCount = 0;
  private MApiRequest updateDishRequest = null;
  private int updateShopId = 0;

  static
  {
    CART_CACHE_FOLDER = new File(DPApplication.instance().getCacheDir(), "selectdish120");
    if (!CART_CACHE_FOLDER.exists())
      CART_CACHE_FOLDER.mkdirs();
  }

  private ArrayList<Integer> getDishIds()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.dish_cart.keySet().iterator();
    while (localIterator.hasNext())
      localArrayList.add(localIterator.next());
    return localArrayList;
  }

  public static CartManager getInstance()
  {
    return CartManagerInner.INSTANCE;
  }

  private int getSelectedCountInCartFreeItems(List<CartFreeItem> paramList)
  {
    int j;
    if (paramList == null)
    {
      j = 0;
      return j;
    }
    int i = 0;
    paramList = paramList.iterator();
    while (true)
    {
      j = i;
      if (!paramList.hasNext())
        break;
      if (!((CartFreeItem)paramList.next()).use)
        continue;
      i += 1;
    }
  }

  private boolean hasFreeDish()
  {
    int j = 0;
    Iterator localIterator = this.free_cart.keySet().iterator();
    int i;
    while (true)
    {
      i = j;
      if (!localIterator.hasNext())
        break;
      if (((ArrayList)this.free_cart.get(localIterator.next())).size() <= 0)
        continue;
      i = 1;
    }
    return i;
  }

  private void loadCart()
  {
    File localFile = new File(CART_CACHE_FOLDER, String.valueOf(this.shopId) + "dishes");
    if ((localFile.exists()) && ((FileUtils.getSerializable(localFile) instanceof LinkedHashMap)))
    {
      this.dish_cart = ((LinkedHashMap)FileUtils.getSerializable(localFile));
      updateFreeCart();
    }
    refreshCart();
  }

  private void notifyCartChanged()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((CartChangedListener)localIterator.next()).onCartChanged();
  }

  private void notifyCountChanged()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((CartChangedListener)localIterator.next()).onCountChanged(this.totalDishCount, this.totalSelectedFreeDishCount, this.totalFreeDishCount, this.totalSingleDishCount);
  }

  private void notifyDishAdded(CartItem paramCartItem)
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((CartChangedListener)localIterator.next()).onDishAdded(paramCartItem);
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
      ((CartChangedListener)localIterator.next()).onFreeDishUpdated(getFreeDishes());
  }

  private void notifyGroupOnOrSetChanged()
  {
    Iterator localIterator = this.cartChangedListeners.iterator();
    while (localIterator.hasNext())
      ((CartChangedListener)localIterator.next()).onGroupOnOrSetChanged();
  }

  private void operateDish(Operation paramOperation, DishInfo paramDishInfo, int paramInt, boolean paramBoolean)
  {
    int i = 0;
    label59: CartItem localCartItem2;
    CartItem localCartItem1;
    if (paramOperation == Operation.ADD)
    {
      i = 1;
      this.totalDishCount += paramInt * i;
      if (paramDishInfo.isValidity)
      {
        if (paramDishInfo.dishType != 1)
          break label290;
        this.totalSingleDishCount += paramDishInfo.getSetDishesCount() * paramInt * i;
      }
      localCartItem2 = (CartItem)this.dish_cart.get(Integer.valueOf(paramDishInfo.dishId));
      if (paramOperation != Operation.ADD)
        break label323;
      if (paramDishInfo.isValidity)
      {
        this.totalPrice = this.totalPrice.add(new BigDecimal(paramDishInfo.currentPrice).multiply(new BigDecimal(paramInt)));
        this.oldtotalPrice = this.oldtotalPrice.add(new BigDecimal(paramDishInfo.oldPrice).multiply(new BigDecimal(paramInt)));
      }
      if (localCartItem2 != null)
        break label306;
      localCartItem2 = new CartItem(paramDishInfo);
      this.dish_cart.put(Integer.valueOf(paramDishInfo.dishId), localCartItem2);
      localCartItem2.addItem(paramInt);
      localCartItem1 = localCartItem2;
      i = paramInt;
      if (paramBoolean)
      {
        notifyDishAdded(localCartItem2);
        i = paramInt;
        localCartItem1 = localCartItem2;
      }
    }
    while (true)
    {
      operateGroupOnOrSet(paramDishInfo, paramOperation, i);
      if ((updateFreeCart(paramDishInfo)) && (paramBoolean))
        notifyFreeDishChanged();
      if (paramBoolean)
      {
        notifyDishChanged(localCartItem1);
        notifyCountChanged();
      }
      return;
      if ((paramOperation != Operation.REDUCE) && (paramOperation != Operation.CLEAR) && (paramOperation != Operation.CLEAR_AND_REMOVE))
        break;
      i = -1;
      break;
      label290: this.totalSingleDishCount += paramInt * i;
      break label59;
      label306: localCartItem2.addItem(paramInt);
      localCartItem1 = localCartItem2;
      i = paramInt;
      continue;
      label323: if (paramOperation == Operation.REDUCE)
      {
        localCartItem1 = localCartItem2;
        i = paramInt;
        if (localCartItem2 == null)
          continue;
        paramInt = Math.min(paramInt, localCartItem2.getItemCount());
        if (paramDishInfo.isValidity)
        {
          this.totalPrice = this.totalPrice.subtract(new BigDecimal(paramDishInfo.currentPrice).multiply(new BigDecimal(paramInt)));
          this.oldtotalPrice = this.oldtotalPrice.subtract(new BigDecimal(paramDishInfo.oldPrice).multiply(new BigDecimal(paramInt)));
        }
        localCartItem1 = localCartItem2;
        i = paramInt;
        if (localCartItem2 == null)
          continue;
        localCartItem1 = localCartItem2;
        i = paramInt;
        if (localCartItem2.reduceItem(paramInt) != 0)
          continue;
        this.dish_cart.remove(Integer.valueOf(paramDishInfo.dishId));
        localCartItem1 = localCartItem2;
        i = paramInt;
        continue;
      }
      if (paramOperation == Operation.CLEAR)
      {
        localCartItem1 = localCartItem2;
        i = paramInt;
        if (localCartItem2 == null)
          continue;
        if (paramDishInfo.isValidity)
        {
          this.totalPrice = this.totalPrice.subtract(new BigDecimal(paramDishInfo.currentPrice).multiply(new BigDecimal(localCartItem2.getItemCount())));
          this.oldtotalPrice = this.oldtotalPrice.subtract(new BigDecimal(paramDishInfo.oldPrice).multiply(new BigDecimal(localCartItem2.getItemCount())));
        }
        localCartItem2.setItemCount(0);
        localCartItem1 = localCartItem2;
        i = paramInt;
        continue;
      }
      if (paramOperation == Operation.REMOVE)
      {
        localCartItem1 = localCartItem2;
        i = paramInt;
        if (localCartItem2 == null)
          continue;
        localCartItem2.setItemCount(0);
        this.dish_cart.remove(Integer.valueOf(paramDishInfo.dishId));
        localCartItem1 = localCartItem2;
        i = paramInt;
        continue;
      }
      localCartItem1 = localCartItem2;
      i = paramInt;
      if (paramOperation != Operation.CLEAR_AND_REMOVE)
        continue;
      localCartItem1 = localCartItem2;
      i = paramInt;
      if (localCartItem2 == null)
        continue;
      if (paramDishInfo.isValidity)
      {
        this.totalPrice = this.totalPrice.subtract(new BigDecimal(paramDishInfo.currentPrice).multiply(new BigDecimal(localCartItem2.getItemCount())));
        this.oldtotalPrice = this.oldtotalPrice.subtract(new BigDecimal(paramDishInfo.oldPrice).multiply(new BigDecimal(localCartItem2.getItemCount())));
      }
      localCartItem2.setItemCount(0);
      this.dish_cart.remove(Integer.valueOf(paramDishInfo.dishId));
      localCartItem1 = localCartItem2;
      i = paramInt;
    }
  }

  private void operateGroupOnOrSet(DishInfo paramDishInfo, Operation paramOperation, int paramInt)
  {
    CartItem localCartItem;
    if (paramDishInfo.dishType == 1)
    {
      localCartItem = (CartItem)this.setDishInfos.get(Integer.valueOf(paramDishInfo.dishId));
      if (paramOperation != Operation.ADD)
        break label85;
      if (localCartItem != null)
        break label75;
      paramOperation = new CartItem(paramDishInfo);
      this.setDishInfos.put(Integer.valueOf(paramDishInfo.dishId), paramOperation);
      paramOperation.addItem(paramInt);
    }
    while (true)
    {
      notifyGroupOnOrSetChanged();
      return;
      label75: localCartItem.addItem(paramInt);
      continue;
      label85: if (paramOperation == Operation.REDUCE)
      {
        if ((localCartItem == null) || (localCartItem.reduceItem(Math.min(paramInt, localCartItem.getItemCount())) != 0))
          continue;
        this.setDishInfos.remove(Integer.valueOf(paramDishInfo.dishId));
        continue;
      }
      if ((paramOperation != Operation.CLEAR_AND_REMOVE) || (localCartItem == null))
        continue;
      localCartItem.setItemCount(0);
      this.setDishInfos.remove(Integer.valueOf(paramDishInfo.dishId));
    }
  }

  private void refreshCart()
  {
    this.totalDishCount = 0;
    this.totalSingleDishCount = 0;
    this.totalFreeDishCount = 0;
    this.totalSelectedFreeDishCount = 0;
    this.totalPrice = new BigDecimal(0);
    this.oldtotalPrice = new BigDecimal(0);
    Iterator localIterator = this.dish_cart.keySet().iterator();
    Object localObject1;
    Object localObject2;
    if (localIterator.hasNext())
    {
      localObject1 = (CartItem)this.dish_cart.get(localIterator.next());
      if (!((CartItem)localObject1).dishInfo.soldout)
      {
        this.totalDishCount += ((CartItem)localObject1).getItemCount();
        if (((CartItem)localObject1).dishInfo.isValidity)
        {
          if (((CartItem)localObject1).dishInfo.dishType != 1)
            break label279;
          this.totalSingleDishCount += ((CartItem)localObject1).getItemCount() * ((CartItem)localObject1).dishInfo.getSetDishesCount();
        }
      }
      while (true)
      {
        this.totalPrice = this.totalPrice.add(new BigDecimal(((CartItem)localObject1).dishInfo.currentPrice).multiply(new BigDecimal(((CartItem)localObject1).getItemCount())));
        this.oldtotalPrice = this.oldtotalPrice.add(new BigDecimal(((CartItem)localObject1).dishInfo.oldPrice).multiply(new BigDecimal(((CartItem)localObject1).getItemCount())));
        if (((CartItem)localObject1).dishInfo.dishType != 1)
          break;
        localObject2 = new CartItem(((CartItem)localObject1).dishInfo);
        ((CartItem)localObject2).setItemCount(((CartItem)localObject1).getItemCount());
        this.setDishInfos.put(Integer.valueOf(((CartItem)localObject1).dishInfo.dishId), localObject2);
        break;
        label279: this.totalSingleDishCount += ((CartItem)localObject1).getItemCount();
      }
    }
    localIterator = this.free_cart.keySet().iterator();
    while (localIterator.hasNext())
    {
      localObject1 = (ArrayList)this.free_cart.get(localIterator.next());
      localObject2 = ((ArrayList)localObject1).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        CartFreeItem localCartFreeItem = (CartFreeItem)((Iterator)localObject2).next();
        if ((!localCartFreeItem.use) || (localCartFreeItem.expired) || (localCartFreeItem.soldout))
          continue;
        this.totalSelectedFreeDishCount += 1;
      }
      this.totalFreeDishCount += ((ArrayList)localObject1).size();
    }
  }

  private boolean refreshFreeDishesByDish(DishInfo paramDishInfo)
  {
    ArrayList localArrayList2 = (ArrayList)this.free_cart.get(Integer.valueOf(paramDishInfo.dishId));
    ArrayList localArrayList1 = localArrayList2;
    if (localArrayList2 == null)
    {
      localArrayList1 = new ArrayList();
      this.free_cart.put(Integer.valueOf(paramDishInfo.dishId), localArrayList1);
    }
    this.totalFreeDishCount -= localArrayList1.size();
    localArrayList1.clear();
    updateFreeCart(paramDishInfo);
    return true;
  }

  private void setCartItemsForEditOrder(CartItem[] paramArrayOfCartItem)
  {
    if ((this.updateShopId != 0) && (this.updateShopId == this.shopId))
    {
      resetCart();
      int j = paramArrayOfCartItem.length;
      int i = 0;
      while (i < j)
      {
        CartItem localCartItem = paramArrayOfCartItem[i];
        operateDish(Operation.ADD, localCartItem.dishInfo, localCartItem.getItemCount(), false);
        i += 1;
      }
      sortDishes();
      notifyFreeDishChanged();
      refreshCart();
      notifyCartChanged();
      notifyCountChanged();
    }
  }

  private void updateDishes(CartItem[] paramArrayOfCartItem)
  {
    if ((this.updateShopId != 0) && (this.updateShopId == this.shopId))
    {
      int k = 0;
      int i = 0;
      int i1 = 0;
      int n;
      int i2;
      label37: CartItem localCartItem1;
      CartItem localCartItem2;
      int j;
      int i3;
      int i4;
      if (paramArrayOfCartItem == null)
      {
        n = 0;
        i2 = 0;
        if (i2 >= n)
          break label269;
        localCartItem1 = paramArrayOfCartItem[i2];
        this.cachedDishIds.remove(Integer.valueOf(localCartItem1.dishInfo.dishId));
        localCartItem2 = (CartItem)this.dish_cart.get(Integer.valueOf(localCartItem1.dishInfo.dishId));
        j = k;
        i3 = i;
        i4 = i1;
        if (localCartItem2 != null)
        {
          if (localCartItem1.dishInfo.status == 0)
            break label171;
          operateDish(Operation.CLEAR_AND_REMOVE, localCartItem2.dishInfo, localCartItem2.getItemCount(), false);
          j = 1;
          i4 = i1;
          i3 = i;
        }
      }
      while (true)
      {
        i2 += 1;
        k = j;
        i = i3;
        i1 = i4;
        break label37;
        n = paramArrayOfCartItem.length;
        break;
        label171: if (localCartItem2.updateBasicInfo(localCartItem1))
          k = 1;
        int m = i;
        if (localCartItem2.updateEventInfo(localCartItem1))
        {
          m = i;
          if (refreshFreeDishesByDish(localCartItem2.dishInfo))
            m = 1;
        }
        j = k;
        i3 = m;
        i4 = i1;
        if (localCartItem1.dishInfo.isValidity == localCartItem2.dishInfo.isValidity)
          continue;
        localCartItem2.dishInfo.isValidity = localCartItem1.dishInfo.isValidity;
        i4 = 1;
        j = k;
        i3 = m;
      }
      label269: paramArrayOfCartItem = this.cachedDishIds.iterator();
      while (paramArrayOfCartItem.hasNext())
      {
        localCartItem1 = (CartItem)this.dish_cart.get(paramArrayOfCartItem.next());
        if (localCartItem1 == null)
          continue;
        operateDish(Operation.CLEAR_AND_REMOVE, localCartItem1.dishInfo, localCartItem1.getItemCount(), true);
      }
      if (i1 != 0)
        sortDishes();
      if (i != 0)
        notifyFreeDishChanged();
      if (k != 0)
      {
        refreshCart();
        notifyCartChanged();
        notifyCountChanged();
      }
    }
  }

  private boolean updateFreeCart()
  {
    int i = 0;
    Iterator localIterator = this.dish_cart.keySet().iterator();
    while (localIterator.hasNext())
    {
      if (!updateFreeCart(((CartItem)this.dish_cart.get(localIterator.next())).dishInfo))
        continue;
      i = 1;
    }
    return i;
  }

  private boolean updateFreeCart(DishInfo paramDishInfo)
  {
    Log.d("CartManager", "updateFreeCart start");
    boolean bool2 = true;
    Object localObject = (CartItem)this.dish_cart.get(Integer.valueOf(paramDishInfo.dishId));
    label69: boolean bool1;
    if (localObject != null)
    {
      i = ((CartItem)localObject).getItemCount();
      k = paramDishInfo.bought;
      if (paramDishInfo.targetCount == 0)
        break label288;
      j = (i + k) / paramDishInfo.targetCount * paramDishInfo.freeCount;
      Log.d("CartManager", "updateFreeCart summary of the dish " + paramDishInfo.dishId + "=" + " currently ordered=" + i + " ordered=" + k + " you can get=" + j);
      ArrayList localArrayList = (ArrayList)this.free_cart.get(Integer.valueOf(paramDishInfo.dishId));
      localObject = localArrayList;
      if (localArrayList == null)
      {
        localObject = new ArrayList();
        this.free_cart.put(Integer.valueOf(paramDishInfo.dishId), localObject);
      }
      i = ((ArrayList)localObject).size();
      k = j - i;
      this.totalFreeDishCount += k;
      Log.d("CartManager", "updateFreeCart oldFreeCount=" + i + " currentFreeCount=" + j);
      if (k != 0)
        break label294;
      bool1 = false;
    }
    label288: label294: 
    do
    {
      Log.d("CartManager", "updateFreeCart finish modified=" + bool1);
      return bool1;
      i = 0;
      break;
      j = 0;
      break label69;
      if (k > 0)
      {
        i = 0;
        while (true)
        {
          bool1 = bool2;
          if (i >= k)
            break;
          ((ArrayList)localObject).add(new CartFreeItem(paramDishInfo.freeItem));
          i += 1;
        }
      }
      i = Math.abs(k);
      j = ((ArrayList)localObject).size() - 1;
      while ((j >= 0) && (i > 0))
      {
        k = i;
        if (!((CartFreeItem)((ArrayList)localObject).get(j)).use)
        {
          ((ArrayList)localObject).remove(j);
          k = i - 1;
        }
        j -= 1;
        i = k;
      }
      bool1 = bool2;
    }
    while (i <= 0);
    int k = ((ArrayList)localObject).size() - 1;
    int j = i;
    int i = k;
    while (true)
    {
      bool1 = bool2;
      if (i < 0)
        break;
      bool1 = bool2;
      if (j <= 0)
        break;
      if (((CartFreeItem)((ArrayList)localObject).get(i)).use)
        this.totalSelectedFreeDishCount -= 1;
      ((ArrayList)localObject).remove(i);
      j -= 1;
      i -= 1;
    }
  }

  private void updateFreeDishes(CartFreeItem[] paramArrayOfCartFreeItem)
  {
    int j = 0;
    Iterator localIterator1 = this.free_cart.keySet().iterator();
    while (localIterator1.hasNext())
    {
      ArrayList localArrayList = (ArrayList)this.free_cart.get(localIterator1.next());
      if (localArrayList == null)
        continue;
      int m = paramArrayOfCartFreeItem.length;
      int k = 0;
      int i = j;
      while (k < m)
      {
        CartFreeItem localCartFreeItem1 = paramArrayOfCartFreeItem[k];
        Iterator localIterator2 = localArrayList.iterator();
        while (localIterator2.hasNext())
        {
          CartFreeItem localCartFreeItem2 = (CartFreeItem)localIterator2.next();
          if ((localCartFreeItem2.giftInfo.dishId != localCartFreeItem1.giftInfo.dishId) || (localCartFreeItem2.giftInfo.giftId != localCartFreeItem1.giftInfo.giftId))
            continue;
          if (localCartFreeItem2.soldout != localCartFreeItem1.soldout)
          {
            localCartFreeItem2.soldout = localCartFreeItem1.soldout;
            i = 1;
          }
          if (localCartFreeItem2.expired != localCartFreeItem1.expired)
          {
            localCartFreeItem2.expired = localCartFreeItem1.expired;
            i = 1;
          }
          j = i;
          if (localCartFreeItem1.giftInfo.name != null)
          {
            j = i;
            if (!localCartFreeItem1.giftInfo.name.equals(localCartFreeItem2.giftInfo.name))
            {
              localCartFreeItem2.giftInfo.name = localCartFreeItem1.giftInfo.name;
              j = 1;
            }
          }
          if (!localCartFreeItem1.soldout)
          {
            i = j;
            if (!localCartFreeItem1.expired)
              continue;
          }
          localCartFreeItem2.use = false;
          i = j;
        }
        k += 1;
      }
      k = localArrayList.size() - 1;
      while (true)
      {
        j = i;
        if (k < 0)
          break;
        if (((CartFreeItem)localArrayList.get(k)).expired)
          localArrayList.remove(k);
        k -= 1;
      }
    }
    if (j != 0)
      notifyFreeDishChanged();
  }

  public void addCartChangedListener(CartChangedListener paramCartChangedListener)
  {
    if (!this.cartChangedListeners.contains(paramCartChangedListener))
      this.cartChangedListeners.add(paramCartChangedListener);
  }

  public void addDish(DishInfo paramDishInfo)
  {
    operateDish(Operation.ADD, paramDishInfo, 1, true);
  }

  public void deleteDish(DishInfo paramDishInfo)
  {
    CartItem localCartItem = (CartItem)this.dish_cart.get(Integer.valueOf(paramDishInfo.dishId));
    if (localCartItem != null)
      operateDish(Operation.CLEAR_AND_REMOVE, paramDishInfo, localCartItem.getItemCount(), true);
  }

  public int getDishCountById(int paramInt)
  {
    CartItem localCartItem = (CartItem)this.dish_cart.get(Integer.valueOf(paramInt));
    if (localCartItem == null)
      return 0;
    return localCartItem.getItemCount();
  }

  public ArrayList<CartItem> getDishes()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.dish_cart.keySet().iterator();
    while (localIterator.hasNext())
      localArrayList.add(this.dish_cart.get(localIterator.next()));
    return localArrayList;
  }

  public int getExchangedGiftId()
  {
    return this.exchangedGiftId;
  }

  public ArrayList<CartFreeItem> getFreeDishes()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.free_cart.keySet().iterator();
    while (localIterator.hasNext())
      localArrayList.addAll((Collection)this.free_cart.get(localIterator.next()));
    return localArrayList;
  }

  public GroupOnItem getGroupOnInfo()
  {
    return this.groupOnInfo;
  }

  public List<CartFreeItem> getSelectedFreeDishes()
  {
    Object localObject = getFreeDishes();
    ArrayList localArrayList = new ArrayList(20);
    localObject = ((List)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      CartFreeItem localCartFreeItem = (CartFreeItem)((Iterator)localObject).next();
      if (!localCartFreeItem.use)
        continue;
      localArrayList.add(localCartFreeItem);
    }
    return (List<CartFreeItem>)localArrayList;
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

  public boolean hasOwnedFreeDish()
  {
    ArrayList localArrayList = (ArrayList)this.free_cart.get(Integer.valueOf(FREE_DISH_ID.intValue()));
    if (localArrayList == null);
    do
      return false;
    while (localArrayList.size() <= 0);
    return true;
  }

  public int isDishBought(int paramInt)
  {
    int j;
    int i;
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
            break label85;
          if (paramInt == this.groupOnInfo.groupOnSet[i].getObject("Dish").getInt("Id"))
            break;
          i += 1;
        }
      }
    }
    label85: Iterator localIterator = this.setDishInfos.keySet().iterator();
    while (localIterator.hasNext())
    {
      CartItem localCartItem = (CartItem)this.setDishInfos.get(localIterator.next());
      j = localCartItem.dishInfo.setItems.size();
      i = 0;
      while (i < j)
      {
        if (paramInt == ((SetItem)localCartItem.dishInfo.setItems.get(i)).id)
          return 2;
        i += 1;
      }
    }
    return 0;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.updateDishRequest)
    {
      this.updateDishRequest = null;
      this.updateShopId = 0;
      this.cachedDishIds.clear();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.updateDishRequest)
    {
      this.updateDishRequest = null;
      paramMApiRequest = paramMApiResponse.result();
      if ((paramMApiRequest instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiRequest;
        paramMApiResponse = new LinkedHashMap(20);
        DPObject[] arrayOfDPObject = paramMApiRequest.getArray("MenuCountItemList");
        int i;
        if ((arrayOfDPObject != null) && (arrayOfDPObject.length >= 1))
        {
          i = 0;
          while (i < arrayOfDPObject.length)
          {
            paramMApiResponse.put(Integer.valueOf(arrayOfDPObject[i].getInt("DishId")), Integer.valueOf(arrayOfDPObject[i].getInt("Count")));
            i += 1;
          }
        }
        arrayOfDPObject = paramMApiRequest.getArray("MenuItemList");
        CartItem[] arrayOfCartItem;
        if (arrayOfDPObject != null)
        {
          arrayOfCartItem = new CartItem[arrayOfDPObject.length];
          i = 0;
          while (i < arrayOfDPObject.length)
          {
            CartItem localCartItem = new CartItem(new DishInfo(arrayOfDPObject[i]));
            Integer localInteger = (Integer)paramMApiResponse.get(Integer.valueOf(localCartItem.dishInfo.dishId));
            if (localInteger != null)
              localCartItem.setItemCount(localInteger.intValue());
            arrayOfCartItem[i] = localCartItem;
            i += 1;
          }
          if (!TextUtils.isEmpty(this.orderViewId))
            break label284;
          updateDishes(arrayOfCartItem);
        }
        while (true)
        {
          paramMApiRequest = paramMApiRequest.getArray("MenuGiftList");
          if ((paramMApiRequest == null) || (paramMApiRequest.length < 1))
            break;
          paramMApiResponse = new GiftInfo[paramMApiRequest.length];
          i = 0;
          while (true)
            if (i < paramMApiRequest.length)
            {
              paramMApiResponse[i] = new GiftInfo(paramMApiRequest[i]);
              i += 1;
              continue;
              label284: setCartItemsForEditOrder(arrayOfCartItem);
              break;
            }
          if (!hasFreeDish())
            break label332;
          setGiftsSelectedInOrderEditStatus(paramMApiResponse);
          if (hasOwnedFreeDish())
            break;
        }
      }
    }
    label332: for (this.giftsInEditOrder = paramMApiResponse; ; this.giftsInEditOrder = paramMApiResponse)
    {
      this.updateShopId = 0;
      this.cachedDishIds.clear();
      return;
    }
  }

  public void recorrectDish(CartItem[] paramArrayOfCartItem, CartFreeItem[] paramArrayOfCartFreeItem)
  {
    this.updateDishRequest = null;
    this.updateShopId = this.shopId;
    this.cachedDishIds.clear();
    Object localObject = getDishIds();
    if (!((ArrayList)localObject).isEmpty())
    {
      localObject = ((ArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext())
      {
        Integer localInteger = (Integer)((Iterator)localObject).next();
        this.cachedDishIds.add(localInteger);
      }
    }
    updateDishes(paramArrayOfCartItem);
    updateFreeDishes(paramArrayOfCartFreeItem);
    refreshCart();
    notifyCountChanged();
  }

  public void reduceDish(DishInfo paramDishInfo)
  {
    operateDish(Operation.REDUCE, paramDishInfo, 1, true);
  }

  public void removeCartCache()
  {
    new File(CART_CACHE_FOLDER, String.valueOf(this.shopId) + "dishes").delete();
  }

  public void removeCartChangedListener(CartChangedListener paramCartChangedListener)
  {
    this.cartChangedListeners.remove(paramCartChangedListener);
  }

  public void resetCart()
  {
    this.orderId = null;
    this.totalDishCount = 0;
    this.totalSingleDishCount = 0;
    this.totalFreeDishCount = 0;
    this.totalSelectedFreeDishCount = 0;
    this.totalPrice = new BigDecimal(0);
    this.oldtotalPrice = new BigDecimal(0);
    this.dish_cart.clear();
    this.free_cart.clear();
    this.groupOnInfo = null;
    this.setDishInfos.clear();
    notifyCountChanged();
    notifyCartChanged();
  }

  public void saveCart()
  {
    if (this.shopId == 0);
    do
    {
      return;
      localObject = new LinkedHashMap();
      ((LinkedHashMap)localObject).putAll(this.free_cart);
      ((LinkedHashMap)localObject).remove(FREE_DISH_ID);
    }
    while (((this.dish_cart.size() == 0) && (((LinkedHashMap)localObject).size() == 0)) || (!CART_CACHE_FOLDER.exists()));
    Object localObject = new File(CART_CACHE_FOLDER, String.valueOf(this.shopId) + "dishes");
    if (!((File)localObject).exists());
    try
    {
      ((File)localObject).createNewFile();
      FileUtils.put((File)localObject, this.dish_cart);
      return;
    }
    catch (IOException localIOException)
    {
      while (true)
        localIOException.printStackTrace();
    }
  }

  public void selectFreeDish(CartFreeItem paramCartFreeItem, boolean paramBoolean)
  {
    Iterator localIterator = this.free_cart.keySet().iterator();
    while (localIterator.hasNext())
    {
      if (!((ArrayList)this.free_cart.get(localIterator.next())).contains(paramCartFreeItem))
        continue;
      if (paramCartFreeItem.use == paramBoolean)
        break;
      paramCartFreeItem.use = paramBoolean;
      if (!paramCartFreeItem.use)
        break label80;
      this.totalSelectedFreeDishCount += 1;
    }
    while (true)
    {
      notifyCountChanged();
      return;
      label80: this.totalSelectedFreeDishCount -= 1;
    }
  }

  public void setExchangedGiftId(int paramInt)
  {
    this.exchangedGiftId = paramInt;
  }

  public void setGiftsSelectedInOrderEditStatus(GiftInfo[] paramArrayOfGiftInfo)
  {
    if (paramArrayOfGiftInfo.length == 0);
    ArrayList localArrayList1;
    do
    {
      return;
      localArrayList1 = getFreeDishes();
    }
    while (localArrayList1.isEmpty());
    Object localObject1 = localArrayList1.iterator();
    while (((Iterator)localObject1).hasNext())
      ((CartFreeItem)((Iterator)localObject1).next()).use = false;
    this.totalSelectedFreeDishCount = 0;
    Object localObject2 = new ArrayList(Arrays.asList(paramArrayOfGiftInfo));
    ArrayList localArrayList2 = (ArrayList)this.free_cart.get(Integer.valueOf(FREE_DISH_ID.intValue()));
    Iterator localIterator1;
    if (localArrayList2 != null)
    {
      int j = paramArrayOfGiftInfo.length;
      int i = 0;
      while (i < j)
      {
        GiftInfo localGiftInfo = paramArrayOfGiftInfo[i];
        localIterator1 = null;
        Iterator localIterator2 = localArrayList2.iterator();
        do
        {
          localObject1 = localIterator1;
          if (!localIterator2.hasNext())
            break;
          localObject1 = (CartFreeItem)localIterator2.next();
        }
        while (localGiftInfo.giftId != ((CartFreeItem)localObject1).giftInfo.giftId);
        if (localObject1 != null)
        {
          if (!((CartFreeItem)localObject1).use)
          {
            ((CartFreeItem)localObject1).use = true;
            this.totalSelectedFreeDishCount += 1;
          }
          ((ArrayList)localObject2).remove(localGiftInfo);
        }
        i += 1;
      }
    }
    paramArrayOfGiftInfo = ((ArrayList)localObject2).iterator();
    while (true)
    {
      if (!paramArrayOfGiftInfo.hasNext())
        break label298;
      localObject1 = (GiftInfo)paramArrayOfGiftInfo.next();
      localIterator1 = localArrayList1.iterator();
      if (!localIterator1.hasNext())
        continue;
      localObject2 = (CartFreeItem)localIterator1.next();
      if ((((GiftInfo)localObject1).dishId != ((CartFreeItem)localObject2).giftInfo.dishId) || (((CartFreeItem)localObject2).use))
        break;
      ((CartFreeItem)localObject2).use = true;
      this.totalSelectedFreeDishCount += 1;
    }
    label298: notifyFreeDishChanged();
    notifyCountChanged();
  }

  public void setGroupOnInfo(DPObject paramDPObject)
  {
    if (paramDPObject.getInt("DishId") >= 0)
      this.groupOnInfo = new GroupOnItem(paramDPObject);
  }

  public void setOrderId(String paramString)
  {
    this.orderId = paramString;
  }

  public void setShopId(int paramInt)
  {
    resetCart();
    this.shopId = paramInt;
    loadCart();
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
    int j = 0;
    if (paramArrayOfGiftInfo != null);
    for (int i = paramArrayOfGiftInfo.length; i == 0; i = 0)
      return;
    ArrayList localArrayList1 = (ArrayList)this.free_cart.get(Integer.valueOf(FREE_DISH_ID.intValue()));
    ArrayList localArrayList2 = new ArrayList(i);
    i = j;
    if (localArrayList1 != null)
      i = localArrayList1.size();
    this.totalFreeDishCount -= i;
    this.totalSelectedFreeDishCount -= getSelectedCountInCartFreeItems(localArrayList1);
    int k = paramArrayOfGiftInfo.length;
    j = 0;
    if (j < k)
    {
      GiftInfo localGiftInfo = paramArrayOfGiftInfo[j];
      Object localObject2 = null;
      Object localObject1 = localObject2;
      if (i >= 1)
      {
        Iterator localIterator = localArrayList1.iterator();
        while (true)
        {
          localObject1 = localObject2;
          if (!localIterator.hasNext())
            break;
          CartFreeItem localCartFreeItem = (CartFreeItem)localIterator.next();
          if (!localGiftInfo.equals(localCartFreeItem.giftInfo))
            continue;
          localObject1 = localCartFreeItem;
          localArrayList1.remove(localCartFreeItem);
        }
      }
      localObject2 = localObject1;
      if (localObject1 == null)
        localObject2 = new CartFreeItem(localGiftInfo);
      if (((CartFreeItem)localObject2).use)
        this.totalSelectedFreeDishCount += 1;
      while (true)
      {
        localArrayList2.add(localObject2);
        j += 1;
        break;
        if ((this.exchangedGiftId == 0) || (this.exchangedGiftId != ((CartFreeItem)localObject2).giftInfo.giftId))
          continue;
        ((CartFreeItem)localObject2).use = true;
        this.totalSelectedFreeDishCount += 1;
      }
    }
    this.free_cart.put(Integer.valueOf(FREE_DISH_ID.intValue()), localArrayList2);
    this.totalFreeDishCount += localArrayList2.size();
    notifyFreeDishChanged();
    notifyCountChanged();
  }

  public void sortDishes()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.dish_cart.entrySet().iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (Map.Entry)localIterator.next();
      if (((CartItem)((Map.Entry)localObject).getValue()).dishInfo.isValidity)
        continue;
      localArrayList.add(((Map.Entry)localObject).getKey());
    }
    localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      localObject = (Integer)localIterator.next();
      CartItem localCartItem = (CartItem)this.dish_cart.remove(localObject);
      this.dish_cart.put(localObject, localCartItem);
    }
    if (localArrayList.size() > 0)
      notifyCartChanged();
  }

  public void submit(Context paramContext, String paramString)
  {
    this.updateDishRequest = null;
    SubmitOrderManager localSubmitOrderManager = new SubmitOrderManager();
    JSONObject localJSONObject1 = new JSONObject();
    Object localObject2 = new JSONArray();
    JSONArray localJSONArray1 = new JSONArray();
    Object localObject3;
    JSONObject localJSONObject2;
    JSONArray localJSONArray2;
    try
    {
      localJSONObject1.put("dishInfos", localObject2);
      localJSONObject1.put("giftInfos", localJSONArray1);
      localObject3 = getDishes().iterator();
      if (((Iterator)localObject3).hasNext())
      {
        Object localObject4 = (CartItem)((Iterator)localObject3).next();
        localJSONObject2 = new JSONObject();
        localJSONObject2.put("dishId", ((CartItem)localObject4).dishInfo.dishId);
        localJSONObject2.put("dishName", ((CartItem)localObject4).dishInfo.name);
        localJSONObject2.put("originPrice", ((CartItem)localObject4).dishInfo.oldPriceForSubmit);
        localJSONObject2.put("price", ((CartItem)localObject4).dishInfo.currentPrice);
        localJSONObject2.put("count", ((CartItem)localObject4).getItemCount());
        localJSONObject2.put("dishType", ((CartItem)localObject4).dishInfo.dishType);
        if (((CartItem)localObject4).dishInfo.dishType == 1)
        {
          localJSONArray2 = new JSONArray();
          localObject4 = ((CartItem)localObject4).dishInfo.setItems.iterator();
          while (((Iterator)localObject4).hasNext())
          {
            SetItem localSetItem = (SetItem)((Iterator)localObject4).next();
            JSONObject localJSONObject3 = new JSONObject();
            localJSONObject3.put("dishId", localSetItem.id);
            localJSONObject3.put("dishName", localSetItem.name);
            localJSONObject3.put("price", localSetItem.originPrice);
            localJSONObject3.put("count", localSetItem.count);
            localJSONObject3.put("unit", localSetItem.unit);
            localJSONArray2.put(localJSONObject3);
          }
        }
      }
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
    while (true)
    {
      Object localObject1 = new SubmitOrderManager.SubmitOrderInfo();
      ((SubmitOrderManager.SubmitOrderInfo)localObject1).shopName = this.shopName;
      ((SubmitOrderManager.SubmitOrderInfo)localObject1).shopId = this.shopId;
      ((SubmitOrderManager.SubmitOrderInfo)localObject1).tableId = paramString;
      ((SubmitOrderManager.SubmitOrderInfo)localObject1).orderId = this.orderId;
      ((SubmitOrderManager.SubmitOrderInfo)localObject1).supportPrePay = this.supportPrepay;
      ((SubmitOrderManager.SubmitOrderInfo)localObject1).supportTable = this.supportTable;
      ((SubmitOrderManager.SubmitOrderInfo)localObject1).total = String.valueOf(this.totalPrice.doubleValue());
      ((SubmitOrderManager.SubmitOrderInfo)localObject1).oldtotal = String.valueOf(this.oldtotalPrice.doubleValue());
      ((SubmitOrderManager.SubmitOrderInfo)localObject1).cartInfo = localJSONObject1.toString();
      localSubmitOrderManager.submit(paramContext, (SubmitOrderManager.SubmitOrderInfo)localObject1);
      return;
      localJSONObject2.put("packageDishInfos", localJSONArray2);
      ((JSONArray)localObject2).put(localJSONObject2);
      break;
      localObject2 = getFreeDishes().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (CartFreeItem)((Iterator)localObject2).next();
        if ((!((CartFreeItem)localObject3).use) || (((CartFreeItem)localObject3).soldout))
          continue;
        localJSONObject2 = new JSONObject();
        localJSONObject2.put("dishId", ((CartFreeItem)localObject3).giftInfo.dishId);
        localJSONObject2.put("dishName", ((CartFreeItem)localObject3).giftInfo.name);
        localJSONObject2.put("giftId", ((CartFreeItem)localObject3).giftInfo.giftId);
        localJSONObject2.put("actId", ((CartFreeItem)localObject3).giftInfo.activityId);
        ((JSONArray)localObject1).put(localJSONObject2);
      }
      if (this.groupOnDealId == 0)
        continue;
      localObject1 = new JSONObject();
      ((JSONObject)localObject1).put("dealId", this.groupOnDealId);
      ((JSONObject)localObject1).put("orderId", this.groupOnOrderId);
      localJSONObject1.put("groupOnInfo", localObject1);
    }
  }

  public void updateDishHistory(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    if ((paramArrayOfInt1 != null) && (paramArrayOfInt2 != null) && (paramArrayOfInt1.length == paramArrayOfInt2.length))
    {
      int j = 0;
      int i = 0;
      while (i < paramArrayOfInt1.length)
      {
        CartItem localCartItem = (CartItem)this.dish_cart.get(Integer.valueOf(paramArrayOfInt1[i]));
        int k = j;
        if (localCartItem != null)
        {
          k = j;
          if (localCartItem.dishInfo.bought != paramArrayOfInt2[i])
          {
            localCartItem.dishInfo.bought = paramArrayOfInt2[i];
            k = j;
            if (updateFreeCart(localCartItem.dishInfo))
              k = 1;
          }
        }
        i += 1;
        j = k;
      }
      if (j != 0)
        notifyFreeDishChanged();
    }
  }

  public void updateDishes()
  {
    if (this.updateDishRequest != null)
      DPApplication.instance().mapiService().abort(this.updateDishRequest, this, false);
    this.updateShopId = 0;
    this.cachedDishIds.clear();
    Object localObject = getDishIds();
    if ((((ArrayList)localObject).isEmpty()) && (TextUtils.isEmpty(this.orderViewId)))
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/querydish.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    if (TextUtils.isEmpty(this.orderViewId))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localObject = ((ArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext())
      {
        Integer localInteger = (Integer)((Iterator)localObject).next();
        localStringBuilder.append(localInteger).append(",");
        this.cachedDishIds.add(localInteger);
      }
      if (localStringBuilder.length() > 0)
        localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
      localBuilder.appendQueryParameter("dishides", localStringBuilder.toString());
    }
    while (true)
    {
      this.updateDishRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      this.updateShopId = this.shopId;
      DPApplication.instance().mapiService().exec(this.updateDishRequest, this);
      return;
      localBuilder.appendQueryParameter("orderViewId", this.orderViewId);
    }
  }

  public static abstract interface CartChangedListener
  {
    public abstract void onCartChanged();

    public abstract void onCountChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public abstract void onDishAdded(CartItem paramCartItem);

    public abstract void onDishChanged(CartItem paramCartItem);

    public abstract void onFreeDishUpdated(ArrayList<CartFreeItem> paramArrayList);

    public abstract void onGroupOnOrSetChanged();
  }

  private static class CartManagerInner
  {
    private static CartManager INSTANCE = new CartManager(null);
  }

  private static enum Operation
  {
    static
    {
      CLEAR = new Operation("CLEAR", 1);
      ADD = new Operation("ADD", 2);
      REDUCE = new Operation("REDUCE", 3);
      CLEAR_AND_REMOVE = new Operation("CLEAR_AND_REMOVE", 4);
      $VALUES = new Operation[] { REMOVE, CLEAR, ADD, REDUCE, CLEAR_AND_REMOVE };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.CartManager
 * JD-Core Version:    0.6.0
 */