package com.google.zxing.client.android.camera;

import android.content.SharedPreferences;

public enum FrontLightMode
{
  static
  {
    AUTO = new FrontLightMode("AUTO", 1);
    OFF = new FrontLightMode("OFF", 2);
    $VALUES = new FrontLightMode[] { ON, AUTO, OFF };
  }

  private static FrontLightMode parse(String paramString)
  {
    if (paramString == null)
      return OFF;
    return valueOf(paramString);
  }

  public static FrontLightMode readPref(SharedPreferences paramSharedPreferences)
  {
    return parse(paramSharedPreferences.getString("preferences_front_light_mode", OFF.toString()));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.camera.FrontLightMode
 * JD-Core Version:    0.6.0
 */