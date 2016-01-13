package com.dianping.base.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.List<Ljava.lang.String;>;

public class FavoriteHelper
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final boolean DEBUG = true;
  private static final String TAG = "FavoriteHelper";
  public static final int TYPE_CATEGORY = 3;
  public static final int TYPE_CITY = 0;
  public static final int TYPE_INVALID = -1;
  public static final int TYPE_REGION = 1;
  public static final int TYPE_TYPE = 2;
  private static ArrayList<FavoriteHelper.ShopInfo> shopinfos = new ArrayList();
  private MApiRequest getFavoriteShopsRequest;
  private int mCurrentUserid;
  private FavoriteListener mFavoriteListener = null;
  private MApiService mapiServie = DPApplication.instance().mapiService();

  public FavoriteHelper(Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      shopinfos = paramBundle.getParcelableArrayList("shopinfo");
      return;
    }
    shopinfos.clear();
  }

  public static void addFavoriteShop(String paramString, int paramInt)
  {
    int i = 0;
    paramString = DPApplication.instance().getSharedPreferences("favorite", 0);
    String str2 = paramString.getString("ids", null);
    if ((str2 == null) || (str2.length() == 0))
    {
      paramString.edit().putString("ids", String.valueOf(paramInt)).apply();
      Log.d("FavoriteHelper", "addFavoriteShop add the first shop " + paramInt);
      return;
    }
    String str1 = String.valueOf(paramInt);
    int m = 0;
    String[] arrayOfString = str2.split(",");
    if (arrayOfString != null)
      i = arrayOfString.length;
    int j = 0;
    while (true)
    {
      int k = m;
      if (j < i)
      {
        if (arrayOfString[j].equals(str1))
        {
          k = 1;
          Log.d("FavoriteHelper", "addFavoriteShop shop " + paramInt + " has already been existed.");
        }
      }
      else
      {
        if (k != 0)
          break;
        str2 = str2 + "," + str1;
        paramString.edit().putString("ids", String.valueOf(str2)).apply();
        Log.d("FavoriteHelper", "addFavoriteShop add a new shop " + str1 + " current ids " + paramString.getString("ids", null));
        return;
      }
      j += 1;
    }
  }

  public static void delAllFavoriteShops()
  {
    DPApplication.instance().getSharedPreferences("favorite", 0).edit().clear().apply();
  }

  public static void delFavoriteShop(String paramString, int paramInt)
  {
    paramString = DPApplication.instance().getSharedPreferences("favorite", 0);
    Object localObject = paramString.getString("ids", null);
    int i;
    int k;
    if ((localObject == null) || (((String)localObject).length() == 0))
    {
      Log.d("FavoriteHelper", "delFavoriteShop no shopids exist");
      i = 0;
      k = -1;
      paramString = shopinfos.iterator();
    }
    while (true)
    {
      int j = k;
      if (paramString.hasNext())
      {
        if (((FavoriteHelper.ShopInfo)paramString.next()).id == paramInt)
          j = i;
      }
      else
      {
        if (j != -1)
          shopinfos.remove(j);
        String str;
        do
        {
          return;
          str = String.valueOf(paramInt);
          localObject = ((String)localObject).split(",");
        }
        while (localObject == null);
        int m = localObject.length;
        k = -1;
        i = 0;
        while (true)
        {
          j = k;
          StringBuilder localStringBuilder;
          if (i < m)
          {
            if (localObject[i].equals(str))
              j = i;
          }
          else
          {
            if (j == -1)
              break;
            localStringBuilder = new StringBuilder();
            i = 0;
            while (i < j)
            {
              localStringBuilder.append(localObject[i]).append(',');
              i += 1;
            }
          }
          i += 1;
          continue;
          i = j + 1;
          while (i < m)
          {
            localStringBuilder.append(localObject[i]).append(',');
            i += 1;
          }
          if ((localStringBuilder.length() > 0) && (localStringBuilder.charAt(localStringBuilder.length() - 1) == ','))
            paramString.edit().putString("ids", localStringBuilder.substring(0, localStringBuilder.length() - 1)).apply();
          while (true)
          {
            Log.d("FavoriteHelper", "delFavoriteShop delete a shop " + str + " current ids " + paramString.getString("ids", null));
            break;
            paramString.edit().putString("ids", localStringBuilder.toString()).apply();
          }
        }
        Log.d("FavoriteHelper", "delFavoriteShop can't find shop " + paramInt);
        break;
      }
      i += 1;
    }
  }

  public static List<String> getFavoriteShop()
  {
    Object localObject = DPApplication.instance().getSharedPreferences("favorite", 0);
    Log.d("FavoriteHelper", "favorite shops=" + ((SharedPreferences)localObject).getString("ids", null));
    localObject = ((SharedPreferences)localObject).getString("ids", null);
    if ((localObject == null) || (((String)localObject).length() == 0));
    do
    {
      return null;
      localObject = ((String)localObject).split(",");
    }
    while (localObject == null);
    return (List<String>)Arrays.asList(localObject);
  }

  public static boolean isFavoriteShop(int paramInt)
  {
    return isFavoriteShop(((AccountService)DPApplication.instance().getService("account")).token(), String.valueOf(paramInt));
  }

  public static boolean isFavoriteShop(String paramString, int paramInt)
  {
    return isFavoriteShop(paramString, String.valueOf(paramInt));
  }

  public static boolean isFavoriteShop(String paramString1, String paramString2)
  {
    if (paramString1 == null);
    paramString1 = DPApplication.instance().getSharedPreferences("favorite", 0);
    Log.d("FavoriteHelper", "isFavoriteShop shopid=" + paramString2 + " ids=" + paramString1.getString("ids", null));
    paramString1 = paramString1.getString("ids", null);
    if ((paramString1 == null) || (paramString1.length() == 0));
    while (true)
    {
      return false;
      paramString2 = String.valueOf(paramString2);
      paramString1 = paramString1.split(",");
      if (paramString1 == null)
        continue;
      int j = paramString1.length;
      int i = 0;
      while (i < j)
      {
        if (paramString1[i].equals(paramString2))
          return true;
        i += 1;
      }
    }
  }

  public static void saveFavoriteShops(String paramString)
  {
    Log.d("FavoriteHelper", "saveFavoriteShops ids=" + paramString);
    DPApplication.instance().getSharedPreferences("favorite", 0).edit().putString("ids", paramString).apply();
  }

  public HashSet<Integer> getMatchedShops(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = shopinfos.iterator();
    while (localIterator.hasNext())
    {
      FavoriteHelper.ShopInfo localShopInfo = (FavoriteHelper.ShopInfo)localIterator.next();
      int j = 1;
      int i = j;
      if (1 != 0)
      {
        i = j;
        if (paramString1 != null)
        {
          i = j;
          if (!paramString1.equals(localShopInfo.city))
            i = 0;
        }
      }
      j = i;
      if (i != 0)
      {
        j = i;
        if (paramString2 != null)
        {
          j = i;
          if (!paramString2.equals(localShopInfo.region))
            j = 0;
        }
      }
      i = j;
      if (j != 0)
      {
        i = j;
        if (paramString3 != null)
        {
          i = j;
          if (!paramString3.equals(localShopInfo.category))
            i = 0;
        }
      }
      j = i;
      if (i != 0)
      {
        j = i;
        if (paramString4 != null)
        {
          j = i;
          if (!paramString4.equals(localShopInfo.type))
            j = 0;
        }
      }
      if (j == 0)
        continue;
      localHashSet.add(Integer.valueOf(localShopInfo.id));
    }
    return localHashSet;
  }

  public boolean hasCategory(String paramString)
  {
    Iterator localIterator = shopinfos.iterator();
    while (localIterator.hasNext())
      if (paramString.equals(((FavoriteHelper.ShopInfo)localIterator.next()).category))
        return true;
    return false;
  }

  public boolean hasCity(String paramString)
  {
    Iterator localIterator = shopinfos.iterator();
    while (localIterator.hasNext())
      if (paramString.equals(((FavoriteHelper.ShopInfo)localIterator.next()).city))
        return true;
    return false;
  }

  public boolean hasFilterItem(String paramString1, int paramInt1, String paramString2, int paramInt2)
  {
    if ((paramString1 == null) || (paramInt1 == -1) || (paramString2 == null) || (paramInt2 == -1))
      return false;
    Iterator localIterator = shopinfos.iterator();
    while (localIterator.hasNext())
    {
      FavoriteHelper.ShopInfo localShopInfo = (FavoriteHelper.ShopInfo)localIterator.next();
      String str1 = null;
      String str2;
      if (paramInt1 == 0)
      {
        str1 = localShopInfo.city;
        str2 = null;
        if (paramInt2 != 0)
          break label137;
        str2 = localShopInfo.city;
      }
      while (true)
      {
        if ((str1 != null) && (str2 != null))
          break label185;
        return false;
        if (paramInt1 == 1)
        {
          str1 = localShopInfo.region;
          break;
        }
        if (paramInt1 == 2)
        {
          str1 = localShopInfo.type;
          break;
        }
        if (paramInt1 != 3)
          break;
        str1 = localShopInfo.category;
        break;
        label137: if (paramInt2 == 1)
        {
          str2 = localShopInfo.region;
          continue;
        }
        if (paramInt2 == 2)
        {
          str2 = localShopInfo.type;
          continue;
        }
        if (paramInt2 != 3)
          continue;
        str2 = localShopInfo.category;
      }
      label185: if ((paramString1.equals(str1)) && (paramString2.equals(str2)))
        return true;
    }
    return false;
  }

  public boolean hasRegion(String paramString)
  {
    Iterator localIterator = shopinfos.iterator();
    while (localIterator.hasNext())
      if (paramString.equals(((FavoriteHelper.ShopInfo)localIterator.next()).region))
        return true;
    return false;
  }

  public boolean hasType(String paramString)
  {
    Iterator localIterator = shopinfos.iterator();
    while (localIterator.hasNext())
      if (paramString.equals(((FavoriteHelper.ShopInfo)localIterator.next()).type))
        return true;
    return false;
  }

  public boolean isMatchedShopByCategory(int paramInt, String paramString)
  {
    Iterator localIterator = shopinfos.iterator();
    while (localIterator.hasNext())
    {
      FavoriteHelper.ShopInfo localShopInfo = (FavoriteHelper.ShopInfo)localIterator.next();
      if (localShopInfo.id == paramInt)
        return paramString.equals(localShopInfo.category);
    }
    return false;
  }

  public boolean isMatchedShopByType(int paramInt, String paramString)
  {
    Iterator localIterator = shopinfos.iterator();
    while (localIterator.hasNext())
    {
      FavoriteHelper.ShopInfo localShopInfo = (FavoriteHelper.ShopInfo)localIterator.next();
      if (localShopInfo.id == paramInt)
        return paramString.equals(localShopInfo.type);
    }
    return false;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getFavoriteShopsRequest)
    {
      this.getFavoriteShopsRequest = null;
      if (this.mFavoriteListener != null)
        this.mFavoriteListener.onRefreshComplete(null, null);
      DPApplication.instance().getSharedPreferences("favorite", 0).edit().putLong("lastupdatetime", 0L).apply();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    DPObject localDPObject;
    StringBuilder localStringBuilder;
    if (paramMApiRequest == this.getFavoriteShopsRequest)
    {
      this.getFavoriteShopsRequest = null;
      localDPObject = (DPObject)paramMApiResponse.result();
      if (localDPObject != null)
      {
        DPObject[] arrayOfDPObject = localDPObject.getArray("List");
        localStringBuilder = new StringBuilder();
        if (arrayOfDPObject != null)
        {
          paramMApiResponse = new FavoriteHelper.FavoriteInfo(arrayOfDPObject.length);
          int j = arrayOfDPObject.length;
          int i = 0;
          while (true)
          {
            paramMApiRequest = paramMApiResponse;
            if (i >= j)
              break;
            paramMApiRequest = arrayOfDPObject[i];
            int k = paramMApiRequest.getInt("ID");
            String str1 = paramMApiRequest.getString("Type");
            String str2 = paramMApiRequest.getString("CategoryName");
            String str3 = paramMApiRequest.getString("CityName");
            String str4 = paramMApiRequest.getString("RegionName");
            Log.d("FavoriteHelper", "get favorite shop id=" + k + " type=" + str1 + " category=" + str2 + " cityName=" + str3 + " regionName=" + str4);
            paramMApiResponse.ids.add(String.valueOf(k));
            if (!paramMApiResponse.types.contains(str1))
              paramMApiResponse.types.add(str1);
            if (!paramMApiResponse.categories.contains(str2))
              paramMApiResponse.categories.add(str2);
            if (!paramMApiResponse.cities.contains(str3))
              paramMApiResponse.cities.add(str3);
            ArrayList localArrayList = (ArrayList)paramMApiResponse.regions.get(str3);
            paramMApiRequest = localArrayList;
            if (localArrayList == null)
            {
              paramMApiRequest = new ArrayList();
              paramMApiResponse.regions.put(str3, paramMApiRequest);
            }
            if (!paramMApiRequest.contains(str4))
              paramMApiRequest.add(str4);
            shopinfos.add(new FavoriteHelper.ShopInfo(k, str3, str1, str2, str4));
            localStringBuilder.append(k).append(",");
            i += 1;
          }
        }
        paramMApiRequest = new FavoriteHelper.FavoriteInfo(0);
        if (this.mCurrentUserid == 0)
        {
          if (localStringBuilder.length() <= 0)
            break label449;
          saveFavoriteShops(localStringBuilder.substring(0, localStringBuilder.length() - 1));
        }
      }
    }
    while (true)
    {
      if (this.mFavoriteListener != null)
      {
        paramMApiResponse = localDPObject.getString("EmptyMsg");
        this.mFavoriteListener.onRefreshComplete(paramMApiRequest, paramMApiResponse);
      }
      return;
      label449: saveFavoriteShops(localStringBuilder.toString());
    }
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    if (paramMApiRequest == this.getFavoriteShopsRequest)
    {
      shopinfos.clear();
      if (this.mFavoriteListener != null)
        this.mFavoriteListener.onRefreshStart();
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelableArrayList("shopinfo", shopinfos);
  }

  public void refresh(int paramInt, boolean paramBoolean)
  {
    String str = DPApplication.instance().accountService().token();
    if (TextUtils.isEmpty(str))
      return;
    Object localObject;
    if (!paramBoolean)
    {
      localObject = DPApplication.instance().getSharedPreferences("favorite", 0);
      long l1 = ((SharedPreferences)localObject).getLong("lastupdatetime", 0L);
      long l2 = System.currentTimeMillis();
      Log.d("FavoriteHelper", "lastupdatetime=" + l1 + " currenttime=" + l2);
      if (l2 - l1 > 86400000L)
      {
        ((SharedPreferences)localObject).edit().putLong("lastupdatetime", l2).apply();
        Log.d("FavoriteHelper", "reset upate time to " + l2);
      }
    }
    else
    {
      if (this.getFavoriteShopsRequest != null)
      {
        this.mapiServie.abort(this.getFavoriteShopsRequest, this, true);
        this.getFavoriteShopsRequest = null;
      }
      localObject = new StringBuilder("http://m.api.dianping.com/");
      ((StringBuilder)localObject).append("favoriteshop.bin?token=").append(str);
      ((StringBuilder)localObject).append("&userid=").append(paramInt);
      this.getFavoriteShopsRequest = BasicMApiRequest.mapiGet(((StringBuilder)localObject).toString(), CacheType.DISABLED);
      this.mapiServie.exec(this.getFavoriteShopsRequest, this);
      this.mCurrentUserid = paramInt;
      return;
    }
    Log.d("FavoriteHelper", "Time is not up to fetch favorite shops");
  }

  public void removeFavoriteListener(FavoriteListener paramFavoriteListener)
  {
    if (paramFavoriteListener == this.mFavoriteListener)
      this.mFavoriteListener = null;
  }

  public void setFavoriteListener(FavoriteListener paramFavoriteListener)
  {
    this.mFavoriteListener = paramFavoriteListener;
  }

  public void shutdown()
  {
    if (this.getFavoriteShopsRequest != null)
    {
      this.mapiServie.abort(this.getFavoriteShopsRequest, this, true);
      this.getFavoriteShopsRequest = null;
    }
  }

  public void updateFavoriteShops(Integer[] paramArrayOfInteger)
  {
    if ((paramArrayOfInteger == null) || (paramArrayOfInteger.length == 0))
      return;
    int n = paramArrayOfInteger.length;
    int j = 0;
    if (j < n)
    {
      int i1 = paramArrayOfInteger[j].intValue();
      int i = 0;
      int m = -1;
      localIterator = shopinfos.iterator();
      while (true)
      {
        int k = m;
        if (localIterator.hasNext())
        {
          if (((FavoriteHelper.ShopInfo)localIterator.next()).id == i1)
            k = i;
        }
        else
        {
          if (k != -1)
            shopinfos.remove(k);
          j += 1;
          break;
        }
        i += 1;
      }
    }
    paramArrayOfInteger = new StringBuilder();
    Iterator localIterator = shopinfos.iterator();
    while (localIterator.hasNext())
      paramArrayOfInteger.append(((FavoriteHelper.ShopInfo)localIterator.next()).id).append(",");
    if (paramArrayOfInteger.length() > 0)
    {
      saveFavoriteShops(paramArrayOfInteger.substring(0, paramArrayOfInteger.length() - 1));
      return;
    }
    saveFavoriteShops(paramArrayOfInteger.toString());
  }

  public static abstract interface FavoriteListener
  {
    public abstract void onRefreshComplete(FavoriteHelper.FavoriteInfo paramFavoriteInfo, String paramString);

    public abstract void onRefreshStart();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.FavoriteHelper
 * JD-Core Version:    0.6.0
 */