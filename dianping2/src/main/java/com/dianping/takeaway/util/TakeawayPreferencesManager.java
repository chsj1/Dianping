package com.dianping.takeaway.util;

import android.content.Context;
import android.content.SharedPreferences;

public class TakeawayPreferencesManager
{
  public static SharedPreferences getTakeawayBannerPreferences(Context paramContext)
  {
    return paramContext.getSharedPreferences("takeawaybanner", 0);
  }

  public static SharedPreferences getTakeawayDeliveryPreferences(Context paramContext)
  {
    return paramContext.getSharedPreferences("takeawaydelivery", 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.util.TakeawayPreferencesManager
 * JD-Core Version:    0.6.0
 */