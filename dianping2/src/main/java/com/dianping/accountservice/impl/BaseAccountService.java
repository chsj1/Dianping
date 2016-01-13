package com.dianping.accountservice.impl;

import I;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.util.URLBase64;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class BaseAccountService
  implements AccountService
{
  protected Context context;
  protected SharedPreferences prefs;

  public BaseAccountService(Context paramContext)
  {
    this.context = paramContext;
    this.prefs = paramContext.getSharedPreferences("account", 0);
    upgradeIfNecessary(this.prefs);
  }

  private void upgradeIfNecessary(SharedPreferences paramSharedPreferences)
  {
    if (paramSharedPreferences.getInt("version", 0) > 0);
    SharedPreferences localSharedPreferences;
    do
    {
      int i;
      do
      {
        return;
        localSharedPreferences = this.context.getSharedPreferences(this.context.getPackageName(), 3);
        i = localSharedPreferences.getInt("my_uid", 0);
      }
      while ((paramSharedPreferences.getInt("userId", 0) != 0) || (i == 0));
      Object localObject = localSharedPreferences.getString("my_nick", null);
      String str1 = localSharedPreferences.getString("my_avatar", null);
      int j = localSharedPreferences.getInt("my_power", 0);
      int k = localSharedPreferences.getInt("my_level", 0);
      int m = localSharedPreferences.getInt("my_badge", 0);
      int n = localSharedPreferences.getInt("my_score", 0);
      int i1 = localSharedPreferences.getInt("my_honey", 0);
      int i2 = localSharedPreferences.getInt("my_fans", 0);
      int i3 = localSharedPreferences.getInt("my_checkin", 0);
      int i4 = localSharedPreferences.getInt("my_cityId", 1);
      int i5 = localSharedPreferences.getInt("my_feed_flag", 0);
      int i6 = localSharedPreferences.getInt("my_mayor", 0);
      String str2 = localSharedPreferences.getString("email", null);
      int i7 = localSharedPreferences.getInt("my_favoCount", 0);
      int i8 = localSharedPreferences.getInt("my_reviewCount", 0);
      int i9 = localSharedPreferences.getInt("my_photoCount", 0);
      int i10 = localSharedPreferences.getInt("my_couponCount", 0);
      String str3 = localSharedPreferences.getString("my_phoneNo", null);
      String str4 = localSharedPreferences.getString("grouponPhone", null);
      boolean bool = localSharedPreferences.getBoolean("grouponIsLocked", false);
      localObject = new DPObject("UserProfile").edit().putInt("UserID", i).putString("NickName", (String)localObject).putString("Avatar", str1).putInt("UserPower", j).putInt("CityID", i4).putInt("Level", k).putInt("Score", n).putInt("CheckInCount", i3).putInt("BadgeCount", m).putInt("HoneyCount", i1).putInt("FansCount", i2).putInt("FeedFlag", i5).putInt("MayorCount", i6).putInt("FavoCount", i7).putInt("ReviewCount", i8).putInt("PhotoCount", i9).putInt("CouponCount", i10).putString("Email", str2).putString("PhoneNo", str3).putString("GrouponPhone", str4).putBoolean("GrouponIsLocked", bool).generate();
      str1 = localSharedPreferences.getString("my_token", null);
      paramSharedPreferences = paramSharedPreferences.edit();
      paramSharedPreferences.putInt("version", 1);
      paramSharedPreferences.putInt("userId", i);
      paramSharedPreferences.putString("token", str1);
      paramSharedPreferences.putString("profile", URLBase64.encode(((DPObject)localObject).toByteArray()));
    }
    while (!paramSharedPreferences.commit());
    paramSharedPreferences = localSharedPreferences.edit();
    paramSharedPreferences.remove("my_uid");
    paramSharedPreferences.remove("my_nick");
    paramSharedPreferences.remove("my_avatar");
    paramSharedPreferences.remove("my_power");
    paramSharedPreferences.remove("my_level");
    paramSharedPreferences.remove("my_badge");
    paramSharedPreferences.remove("my_score");
    paramSharedPreferences.remove("my_honey");
    paramSharedPreferences.remove("my_fans");
    paramSharedPreferences.remove("my_checkin");
    paramSharedPreferences.remove("my_cityId");
    paramSharedPreferences.remove("my_token");
    paramSharedPreferences.remove("my_feed_flag");
    paramSharedPreferences.remove("my_mayor");
    paramSharedPreferences.remove("my_phoneNo");
    paramSharedPreferences.commit();
  }

  protected abstract void dispatchAccountChanged();

  protected abstract void dispatchProfileChanged();

  public int id()
  {
    return this.prefs.getInt("userId", 0);
  }

  public void logout()
  {
    int i = id();
    this.prefs.edit().remove("userId").remove("token").remove("newToken").remove("profile").commit();
    if (i != 0)
      dispatchAccountChanged();
  }

  protected DPObject mergeDPObject(DPObject paramDPObject1, DPObject paramDPObject2, int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null);
    int i;
    Object localObject1;
    int k;
    int j;
    int m;
    for (paramArrayOfInt = new int[0]; ; paramArrayOfInt = (I)localObject1)
    {
      i = 0;
      localObject1 = paramDPObject1.edit();
      k = paramArrayOfInt.length;
      j = 0;
      while (j < k)
      {
        m = paramArrayOfInt[j];
        if (paramDPObject1.contains(m))
        {
          ((DPObject.Editor)localObject1).remove(m);
          i = 1;
        }
        j += 1;
      }
      localObject1 = new int[paramArrayOfInt.length];
      System.arraycopy(paramArrayOfInt, 0, localObject1, 0, paramArrayOfInt.length);
      Arrays.sort(localObject1);
    }
    Iterator localIterator = paramDPObject2.iterator();
    label795: 
    while (localIterator.hasNext())
    {
      j = ((Integer)((Map.Entry)localIterator.next()).getKey()).intValue();
      if (Arrays.binarySearch(paramArrayOfInt, j) >= 0)
        continue;
      Object localObject2;
      Object localObject3;
      boolean bool1;
      long l1;
      long l2;
      switch (paramDPObject2.getType(j))
      {
      case 67:
      case 69:
      case 70:
      case 71:
      case 72:
      case 74:
      case 75:
      case 77:
      case 80:
      case 81:
      case 82:
      case 84:
      default:
        Log.w("account", "merge unknown DPObject type, key = " + j);
        break;
      case 73:
        k = paramDPObject1.getInt(j);
        m = paramDPObject2.getInt(j);
        if (k == m)
          continue;
        ((DPObject.Editor)localObject1).putInt(j, m);
        i = 1;
        break;
      case 83:
        localObject2 = paramDPObject1.getString(j);
        localObject3 = paramDPObject2.getString(j);
        if (localObject2 == null)
          if (localObject3 == null)
            bool1 = true;
        while (!bool1)
        {
          ((DPObject.Editor)localObject1).putString(j, (String)localObject3);
          i = 1;
          break;
          bool1 = false;
          continue;
          bool1 = ((String)localObject2).equals(localObject3);
        }
      case 66:
        bool1 = paramDPObject1.getBoolean(j);
        boolean bool2 = paramDPObject2.getBoolean(j);
        if (bool1 == bool2)
          continue;
        ((DPObject.Editor)localObject1).putBoolean(j, bool2);
        i = 1;
        break;
      case 76:
        l1 = paramDPObject1.getLong(j);
        l2 = paramDPObject2.getLong(j);
        if (l1 == l2)
          continue;
        ((DPObject.Editor)localObject1).putLong(j, l2);
        i = 1;
        break;
      case 68:
        double d1 = paramDPObject1.getDouble(j);
        double d2 = paramDPObject2.getDouble(j);
        if (d1 == d2)
          continue;
        ((DPObject.Editor)localObject1).putDouble(j, d2);
        i = 1;
        break;
      case 78:
        if (!paramDPObject1.contains(j))
          continue;
        ((DPObject.Editor)localObject1).remove(j);
        i = 1;
        break;
      case 85:
        l1 = paramDPObject1.getTime(j);
        l2 = paramDPObject2.getTime(j);
        if (l1 == l2)
          continue;
        ((DPObject.Editor)localObject1).putTime(j, l2);
        i = 1;
        break;
      case 79:
        localObject2 = paramDPObject1.getObject(j);
        localObject3 = paramDPObject2.getObject(j);
        if (localObject2 == null)
          if (localObject3 == null)
            bool1 = true;
        while (!bool1)
        {
          ((DPObject.Editor)localObject1).putObject(j, (DPObject)localObject3);
          i = 1;
          break;
          bool1 = false;
          continue;
          if (localObject3 == null)
          {
            bool1 = false;
            continue;
          }
          bool1 = Arrays.equals(((DPObject)localObject2).toByteArray(), ((DPObject)localObject3).toByteArray());
        }
      case 65:
        localObject2 = paramDPObject1.getArray(j);
        localObject3 = paramDPObject2.getArray(j);
        if (localObject2 == null)
          if (localObject3 == null)
            bool1 = true;
        while (true)
        {
          if (bool1)
            break label795;
          ((DPObject.Editor)localObject1).putArray(j, localObject3);
          i = 1;
          break;
          bool1 = false;
          continue;
          if (localObject3 == null)
          {
            bool1 = false;
            continue;
          }
          bool1 = Arrays.equals(new DPObject().edit().putArray(0, localObject2).generate().toByteArray(), new DPObject().edit().putArray(0, localObject3).generate().toByteArray());
        }
      }
    }
    if (i != 0)
      paramDPObject1 = ((DPObject.Editor)localObject1).generate();
    return (DPObject)(DPObject)(DPObject)paramDPObject1;
  }

  public String newToken()
  {
    return this.prefs.getString("newToken", null);
  }

  public DPObject profile()
  {
    Object localObject = this.prefs.getString("profile", null);
    if (localObject == null)
      return null;
    localObject = URLBase64.decode((String)localObject);
    return (DPObject)new DPObject(localObject, 0, localObject.length);
  }

  public void setNewToken(String paramString)
  {
    if (id() != 0)
      this.prefs.edit().putString("newToken", paramString).commit();
  }

  public String token()
  {
    return this.prefs.getString("token", null);
  }

  public void update(DPObject paramDPObject)
  {
    int i = paramDPObject.getInt("UserID");
    Object localObject;
    if (i != id())
    {
      localObject = paramDPObject.getString("Token");
      String str = paramDPObject.getString("NewToken");
      paramDPObject = paramDPObject.edit().remove("Token").remove("NewToken").generate();
      SharedPreferences.Editor localEditor = this.prefs.edit();
      localEditor.putInt("userId", i);
      localEditor.putString("token", (String)localObject);
      if (str != null)
        localEditor.putString("newToken", str);
      localEditor.putString("profile", URLBase64.encode(paramDPObject.toByteArray()));
      localEditor.commit();
      dispatchAccountChanged();
    }
    do
    {
      return;
      localObject = profile();
      paramDPObject = mergeDPObject((DPObject)localObject, paramDPObject, new int[] { DPObject.hash16("Token"), DPObject.hash16("NewToken") });
    }
    while (paramDPObject == localObject);
    this.prefs.edit().putString("profile", URLBase64.encode(paramDPObject.toByteArray())).commit();
    dispatchProfileChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.accountservice.impl.BaseAccountService
 * JD-Core Version:    0.6.0
 */