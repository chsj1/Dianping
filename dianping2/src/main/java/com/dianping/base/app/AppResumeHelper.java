package com.dianping.base.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import com.dianping.app.CityConfig;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.push.localpush.LocalpushHelper;
import com.dianping.base.push.localpush.LocalpushHelper.LocalpushInfo;
import com.dianping.cache.DPCache;
import com.dianping.configservice.ConfigService;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;

public class AppResumeHelper
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final long INTERNAL_WAIT_TIME = 300000L;
  private static AppResumeHelper instance;
  private MApiRequest appLocalPushRequest;
  private Context context;
  private MApiRequest getCityInfoRequest;
  private MApiRequest getShopInfoAgentsRequest;

  private CityConfig cityConfig()
  {
    return DPApplication.instance().cityConfig();
  }

  private ConfigService configService()
  {
    return (ConfigService)DPApplication.instance().getService("config");
  }

  private boolean hasWaitForEnoughTime()
  {
    SharedPreferences localSharedPreferences = DPActivity.preferences(this.context);
    return System.currentTimeMillis() - localSharedPreferences.getLong("appresume_last_request_time", 0L) > 300000L;
  }

  public static AppResumeHelper instance()
  {
    if (instance == null)
      instance = new AppResumeHelper();
    return instance;
  }

  private MApiService mapiService()
  {
    return (MApiService)DPApplication.instance().getService("mapi");
  }

  private void requestCityInfo()
  {
    if (cityConfig().currentCity().id() == 0)
      return;
    if (this.getCityInfoRequest != null)
    {
      mapiService().abort(this.getCityInfoRequest, this, true);
      this.getCityInfoRequest = null;
    }
    this.getCityInfoRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/common/cityinfo.bin?cityid=" + cityConfig().currentCity().id(), CacheType.DISABLED);
    mapiService().exec(this.getCityInfoRequest, this);
  }

  private void requestLocalPush()
  {
    if (this.appLocalPushRequest != null)
    {
      mapiService().abort(this.appLocalPushRequest, this, true);
      this.appLocalPushRequest = null;
    }
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/localpush.bin").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(DPApplication.instance().cityConfig().currentCity().id()));
    this.appLocalPushRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.appLocalPushRequest, this);
  }

  private void requestShopInfoAgents()
  {
    if (this.getShopInfoAgentsRequest != null)
    {
      mapiService().abort(this.getShopInfoAgentsRequest, this, true);
      this.getShopInfoAgentsRequest = null;
    }
    this.getShopInfoAgentsRequest = BasicMApiRequest.mapiGet("http://mapi.dianping.com/mapi/framework/modulesconfig.bin", CacheType.SERVICE);
    mapiService().exec(this.getShopInfoAgentsRequest, this);
  }

  private void updateCity(DPObject paramDPObject)
  {
    if (paramDPObject != null);
    try
    {
      paramDPObject = (City)paramDPObject.decodeToObject(City.DECODER);
      cityConfig().updateCurrentCity(paramDPObject);
      return;
    }
    catch (java.lang.Exception paramDPObject)
    {
    }
  }

  private void updateLocalPush(DPObject[] paramArrayOfDPObject)
  {
    int k = 0;
    LocalpushHelper localLocalpushHelper = LocalpushHelper.instance(this.context.getApplicationContext());
    if (paramArrayOfDPObject == null);
    for (int i = 0; ; i = paramArrayOfDPObject.length)
    {
      int j = 0;
      while (j < i)
      {
        DPObject localDPObject = paramArrayOfDPObject[j];
        int m = k;
        if (localLocalpushHelper.insert(new LocalpushHelper.LocalpushInfo(String.valueOf(localDPObject.getInt("ID")), localDPObject.getLong("StartTime"), localDPObject.getLong("EndTime"), localDPObject.getString("Content"), localDPObject.getString("Url"), localDPObject.getInt("TriggerType"), localDPObject.getLong("TriggerTime"))))
        {
          m = k;
          if (k == 0)
            m = 1;
        }
        j += 1;
        k = m;
      }
    }
    if (k != 0)
    {
      paramArrayOfDPObject = new Intent("com.dianping.action.Intent.ACTION_UPDATE");
      this.context.sendBroadcast(paramArrayOfDPObject);
    }
  }

  public void init(Context paramContext)
  {
    this.context = paramContext;
  }

  public void onApplicationResume()
  {
    if (!hasWaitForEnoughTime())
      return;
    configService().refresh();
    requestCityInfo();
    requestLocalPush();
    requestShopInfoAgents();
    Monitor.instance().requestNewMessage();
    DPActivity.preferences(this.context).edit().putLong("appresume_last_request_time", System.currentTimeMillis()).apply();
  }

  public void onApplicationStop()
  {
    if (this.context != null)
      DPActivity.preferences(this.context).edit().putLong("appresume_last_request_time", 0L).apply();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.getCityInfoRequest == paramMApiRequest)
      this.getCityInfoRequest = null;
    do
    {
      return;
      if (paramMApiRequest != this.appLocalPushRequest)
        continue;
      this.appLocalPushRequest = null;
      return;
    }
    while (paramMApiRequest != this.getShopInfoAgentsRequest);
    this.getShopInfoAgentsRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.getCityInfoRequest == paramMApiRequest)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
        updateCity((DPObject)paramMApiResponse.result());
      this.getCityInfoRequest = null;
    }
    do
    {
      return;
      if (this.appLocalPushRequest != paramMApiRequest)
        continue;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
        updateLocalPush(paramMApiRequest.getArray("LocalPushList"));
      this.appLocalPushRequest = null;
      return;
    }
    while ((paramMApiRequest != this.getShopInfoAgentsRequest) || (!(paramMApiResponse.result() instanceof DPObject)) || (paramMApiResponse.result() == null));
    paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("Config");
    DPCache.getInstance().put("shopinfoagents", null, paramMApiRequest, 31539600000L, false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.AppResumeHelper
 * JD-Core Version:    0.6.0
 */