package com.dianping.util.network;

import android.text.TextUtils;
import java.util.List;

public class WifiModel
{
  private String mBssid;
  private int mLevel;
  private String mSsid;

  public WifiModel(String paramString1, String paramString2, int paramInt)
  {
    this.mSsid = paramString1.replace("|", "-").replace(",", "_").replace("&", "-");
    this.mBssid = paramString2;
    this.mLevel = paramInt;
  }

  public static String listToString(List<WifiModel> paramList)
  {
    String str = "";
    if (paramList == null)
      return "";
    int i = 0;
    if (i < paramList.size())
    {
      WifiModel localWifiModel = (WifiModel)paramList.get(i);
      if (localWifiModel == null);
      while (true)
      {
        i += 1;
        break;
        if (i == 0)
        {
          str = localWifiModel.toDPString();
          continue;
        }
        str = String.format("%s|%s", new Object[] { str, localWifiModel.toDPString() });
      }
    }
    return str;
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject);
    do
    {
      return true;
      if (!(paramObject instanceof WifiModel))
        break;
      paramObject = (WifiModel)paramObject;
    }
    while (this.mBssid.equals(paramObject.getBssid()));
    return false;
  }

  public String getBssid()
  {
    return this.mBssid;
  }

  public int getLevel()
  {
    return this.mLevel;
  }

  public String getSsid()
  {
    return this.mSsid;
  }

  public int hashCode()
  {
    return this.mLevel;
  }

  public String toDPString()
  {
    if (TextUtils.isEmpty(this.mSsid))
      return "";
    return String.format("%s,%s,%d", new Object[] { this.mSsid, this.mBssid, Integer.valueOf(this.mLevel) });
  }

  public String toString()
  {
    return String.format("{ssid:%s,mac:%s,dBm:%d}", new Object[] { this.mSsid, this.mBssid, Integer.valueOf(this.mLevel) });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.network.WifiModel
 * JD-Core Version:    0.6.0
 */