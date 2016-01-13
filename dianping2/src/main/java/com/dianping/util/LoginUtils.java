package com.dianping.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginUtils
{
  public static String getLoginGASource(Context paramContext)
  {
    Object localObject = paramContext.getSharedPreferences(paramContext.getPackageName(), 0);
    paramContext = ((SharedPreferences)localObject).getString("login_ga_source", "other");
    localObject = ((SharedPreferences)localObject).edit();
    ((SharedPreferences.Editor)localObject).putString("login_ga_source", "other");
    ((SharedPreferences.Editor)localObject).commit();
    return (String)paramContext;
  }

  public static void setLoginGASource(Context paramContext, String paramString)
  {
    paramContext = paramContext.getSharedPreferences(paramContext.getPackageName(), 0).edit();
    paramContext.putString("login_ga_source", paramString);
    paramContext.commit();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.LoginUtils
 * JD-Core Version:    0.6.0
 */