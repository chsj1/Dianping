package com.umpay.creditcard.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class j
{
  private Context a;

  public j(Context paramContext)
  {
    this.a = paramContext;
  }

  public final String a(String paramString)
  {
    SharedPreferences localSharedPreferences = this.a.getSharedPreferences("UmpaySDKConfig", 0);
    if (localSharedPreferences.contains(paramString))
      return localSharedPreferences.getString(paramString, "");
    return "";
  }

  public final void a(String paramString1, String paramString2)
  {
    SharedPreferences.Editor localEditor = this.a.getSharedPreferences("UmpaySDKConfig", 0).edit();
    localEditor.putString(paramString1, paramString2);
    localEditor.commit();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.umpay.creditcard.android.util.j
 * JD-Core Version:    0.6.0
 */