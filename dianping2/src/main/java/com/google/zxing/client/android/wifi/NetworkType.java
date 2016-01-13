package com.google.zxing.client.android.wifi;

 enum NetworkType
{
  static
  {
    NO_PASSWORD = new NetworkType("NO_PASSWORD", 2);
    $VALUES = new NetworkType[] { WEP, WPA, NO_PASSWORD };
  }

  static NetworkType forIntentValue(String paramString)
  {
    if (paramString == null)
      return NO_PASSWORD;
    if ("WPA".equals(paramString))
      return WPA;
    if ("WEP".equals(paramString))
      return WEP;
    if ("nopass".equals(paramString))
      return NO_PASSWORD;
    throw new IllegalArgumentException(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.wifi.NetworkType
 * JD-Core Version:    0.6.0
 */