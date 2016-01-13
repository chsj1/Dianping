package com.dianping.configservice.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiFormInputStream;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.DateUtil;
import com.dianping.util.Log;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

public class MyConfigService extends DefaultConfigService
{
  public static final String ACTION_UPDATE_CONFIG = "com.dianping.action.UPDATE_CONFIG";
  public static final String TAG = MyConfigService.class.getSimpleName();
  private Context context;

  public MyConfigService(Context paramContext, MApiService paramMApiService)
  {
    super(paramContext, paramMApiService);
    this.context = paramContext;
    ConfigHelper.refresh(dump());
  }

  protected MApiRequest createRequest()
  {
    Object localObject3 = new StringBuilder("http://m.api.dianping.com/appconfig.bin?cityid=").append(DPApplication.instance().city().id());
    Object localObject2 = null;
    try
    {
      Object localObject1 = (Location)DPApplication.instance().locationService().location().decodeToObject(Location.DECODER);
      localObject2 = localObject1;
      label47: localObject1 = Environment.phone();
      if ((localObject2 != null) && ((((Location)localObject2).latitude() != 0.0D) || (((Location)localObject2).longitude() != 0.0D)))
      {
        DecimalFormat localDecimalFormat = Location.FMT;
        String str = ((StringBuilder)localObject3).toString();
        localObject3 = localObject1;
        if (localObject1 == null)
          localObject3 = "";
        return new BasicMApiRequest(str, "POST", new MApiFormInputStream(new String[] { "phone", localObject3, "lat", localDecimalFormat.format(((Location)localObject2).latitude()), "lng", localDecimalFormat.format(((Location)localObject2).longitude()) }), CacheType.DISABLED, false, null);
      }
      localObject3 = ((StringBuilder)localObject3).toString();
      localObject2 = localObject1;
      if (localObject1 == null)
        localObject2 = "";
      return new BasicMApiRequest((String)localObject3, "POST", new MApiFormInputStream(new String[] { "phone", localObject2 }), CacheType.DISABLED, false, null);
    }
    catch (Exception localException)
    {
      break label47;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    super.onRequestFinish(paramMApiRequest, paramMApiResponse);
    ConfigHelper.refresh(dump());
    try
    {
      paramMApiRequest = paramMApiResponse.headers();
      Log.i(TAG, "pair" + paramMApiRequest);
      paramMApiRequest = paramMApiRequest.iterator();
      while (paramMApiRequest.hasNext())
      {
        paramMApiResponse = (NameValuePair)paramMApiRequest.next();
        if (!"Date".equals(paramMApiResponse.getName()))
          continue;
        DateUtil.setHttpResponseDate(paramMApiResponse.getValue());
      }
      return;
    }
    catch (Exception paramMApiRequest)
    {
    }
  }

  @TargetApi(11)
  public void setConfig(JSONObject paramJSONObject)
  {
    JSONObject localJSONObject = dump();
    super.setConfig(paramJSONObject);
    Intent localIntent = new Intent("com.dianping.action.UPDATE_CONFIG");
    localIntent.putExtra("oldConfig", localJSONObject.toString());
    localIntent.putExtra("newConfig", dump().toString());
    if (Build.VERSION.SDK_INT >= 11);
    for (paramJSONObject = this.context.getSharedPreferences("dppushservice", 4); ; paramJSONObject = this.context.getSharedPreferences("dppushservice", 0))
    {
      paramJSONObject.edit().putString("config", dump().toString()).commit();
      this.context.sendBroadcast(localIntent);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.configservice.impl.MyConfigService
 * JD-Core Version:    0.6.0
 */