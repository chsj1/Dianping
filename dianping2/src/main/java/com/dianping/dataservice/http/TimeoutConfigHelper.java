package com.dianping.dataservice.http;

import android.text.TextUtils;
import com.dianping.configservice.ConfigChangeListener;
import com.dianping.configservice.ConfigService;
import org.json.JSONObject;

public class TimeoutConfigHelper
{
  private ConfigService configService;
  private NetworkInfoHelper networkInfoHelper;
  private JSONObject object;

  public TimeoutConfigHelper(ConfigService paramConfigService, NetworkInfoHelper paramNetworkInfoHelper)
  {
    this.configService = paramConfigService;
    this.networkInfoHelper = paramNetworkInfoHelper;
    paramConfigService.addListener("androidTimeout", new ConfigChangeListener()
    {
      public void onConfigChange(String paramString, Object paramObject1, Object paramObject2)
      {
        TimeoutConfigHelper.access$002(TimeoutConfigHelper.this, null);
      }
    });
  }

  private String getNetworkInfo()
  {
    switch (this.networkInfoHelper.getNetworkType())
    {
    default:
      return "UNKNOWN";
    case 2:
      return "2G";
    case 3:
      return "3G";
    case 4:
      return "4G";
    case 1:
    }
    return "WIFI";
  }

  private int getTimeout(String paramString)
  {
    try
    {
      if (this.object == null)
      {
        String str = this.configService.dump().getString("androidTimeout");
        if (!TextUtils.isEmpty(str))
          this.object = new JSONObject(str.replaceAll("'", "\""));
      }
      int i = this.object.getJSONObject(paramString).getInt(getNetworkInfo());
      return i;
    }
    catch (java.lang.Exception paramString)
    {
    }
    return 0;
  }

  public int getHttpHold()
  {
    return getTimeout("httpHold");
  }

  public int getHttpTimeout()
  {
    return getTimeout("http");
  }

  public int getHttpsTimeout()
  {
    return getTimeout("https");
  }

  public int getTunnelTimeout()
  {
    return getTimeout("tunnel");
  }

  public int getUtnHold()
  {
    return getTimeout("utnHold");
  }

  public int getWnsTimeout()
  {
    return getTimeout("wns");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.TimeoutConfigHelper
 * JD-Core Version:    0.6.0
 */