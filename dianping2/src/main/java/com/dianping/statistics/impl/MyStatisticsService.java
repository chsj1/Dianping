package com.dianping.statistics.impl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.configservice.ConfigService;
import com.dianping.dataservice.http.NetworkInfoHelper;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.util.DateUtil;
import com.dianping.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class MyStatisticsService extends DefaultStatisticsService
{
  private AccountService accountService;
  private LocationService locationService;
  private NetworkInfoHelper networkInfo;
  private SharedPreferences prefs;
  private Context theContext;
  private String url;

  public MyStatisticsService(Context paramContext, String paramString)
  {
    this(paramContext, "statistics", paramString);
  }

  public MyStatisticsService(Context paramContext, String paramString1, String paramString2)
  {
    super(paramContext, paramString1, paramString2);
    this.theContext = paramContext;
    this.url = paramString2;
    this.networkInfo = new NetworkInfoHelper(paramContext);
    this.prefs = paramContext.getSharedPreferences(paramContext.getPackageName(), 0);
  }

  private String dateFormat(Date paramDate)
  {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(paramDate);
  }

  public void event(String paramString1, String paramString2, String paramString3, int paramInt, List<NameValuePair> paramList)
  {
    if (DPApplication.instance().tunnelConfigService().dump().optBoolean("ShouldUseJudasOnly", true))
      return;
    ArrayList localArrayList = new ArrayList(10);
    localArrayList.add(new BasicNameValuePair("event_category", paramString1));
    localArrayList.add(new BasicNameValuePair("event_action", paramString2));
    if ((paramString3 != null) && (paramString3.length() > 0))
      localArrayList.add(new BasicNameValuePair("event_label", paramString3));
    if (paramInt != 0)
      localArrayList.add(new BasicNameValuePair("event_value", String.valueOf(paramInt)));
    if (paramList != null)
      localArrayList.addAll(paramList);
    if (Environment.isDebug())
    {
      Intent localIntent = new Intent();
      localIntent.setAction("com.dianping.action.ADD_STATISTICS");
      Bundle localBundle = new Bundle();
      localBundle.putString("category", paramString1);
      localBundle.putString("action", paramString2);
      localBundle.putString("label", paramString3);
      localBundle.putInt("value", paramInt);
      localBundle.putInt("mode", 0);
      if (paramList != null)
        localBundle.putString("extra", paramList.toString());
      localIntent.putExtras(localBundle);
      this.theContext.sendBroadcast(localIntent);
      Log.v("GA", localArrayList.toString());
    }
    record(localArrayList);
  }

  public void pageView(String paramString, List<NameValuePair> paramList)
  {
    ArrayList localArrayList = new ArrayList(10);
    localArrayList.add(new BasicNameValuePair("url", paramString));
    if (paramList != null)
      localArrayList.addAll(paramList);
    record(localArrayList);
  }

  public void record(List<NameValuePair> paramList)
  {
    int i = 0;
    if (DPApplication.instance().tunnelConfigService().dump().optBoolean("ShouldUseJudasOnly", true))
      return;
    ArrayList localArrayList = new ArrayList(20);
    HashMap localHashMap = new HashMap(8);
    localArrayList.add(new BasicNameValuePair("trainid", "7"));
    paramList = paramList.iterator();
    if (paramList.hasNext())
    {
      NameValuePair localNameValuePair = (NameValuePair)paramList.next();
      if ("trainid".equals(localNameValuePair.getName()))
        localArrayList.set(0, localNameValuePair);
      while (true)
      {
        localHashMap.put(localNameValuePair.getName(), localNameValuePair);
        break;
        localArrayList.add(localNameValuePair);
      }
    }
    if (!localHashMap.containsKey("addtime"))
      localArrayList.add(new BasicNameValuePair("addtime", dateFormat(new Date(DateUtil.currentTimeMillis()))));
    if (!localHashMap.containsKey("deviceid"))
      localArrayList.add(new BasicNameValuePair("deviceid", Environment.uuid()));
    if (!localHashMap.containsKey("deviceid2"))
    {
      paramList = Environment.imei();
      if (paramList != null)
        localArrayList.add(new BasicNameValuePair("deviceid2", paramList));
    }
    paramList = this.prefs.getString("dpid", null);
    if (!TextUtils.isEmpty(paramList))
      localArrayList.add(new BasicNameValuePair("dpid", paramList));
    if (!localHashMap.containsKey("sessionid"))
    {
      paramList = Environment.sessionId();
      if (paramList != null)
        localArrayList.add(new BasicNameValuePair("sessionid", paramList));
    }
    if ((Log.LEVEL <= 6) && (!localHashMap.containsKey("debug")))
      localArrayList.add(new BasicNameValuePair("debug", "1"));
    if (!localHashMap.containsKey("token"))
    {
      if (this.accountService == null)
        this.accountService = ((AccountService)DPApplication.instance().getService("account"));
      paramList = this.accountService.token();
      if (paramList != null)
        localArrayList.add(new BasicNameValuePair("token", paramList));
    }
    if (!localHashMap.containsKey("cityid"))
      localArrayList.add(new BasicNameValuePair("cityid", String.valueOf(DPApplication.instance().city().id())));
    if (!localHashMap.containsKey("cityid2"))
    {
      if (this.locationService == null)
        this.locationService = ((LocationService)DPApplication.instance().getService("location"));
      paramList = this.locationService.city();
      if (paramList != null)
        break label677;
    }
    while (true)
    {
      if (i > 0)
        localArrayList.add(new BasicNameValuePair("cityid2", String.valueOf(i)));
      if (!localHashMap.containsKey("network"))
        localArrayList.add(new BasicNameValuePair("network", this.networkInfo.getNetworkInfo()));
      if (!localHashMap.containsKey("useragent"))
        localArrayList.add(new BasicNameValuePair("useragent", Environment.mapiUserAgent()));
      if (!localHashMap.containsKey("version"))
        localArrayList.add(new BasicNameValuePair("version", Environment.versionName()));
      if (!localHashMap.containsKey("source"))
      {
        paramList = Environment.source();
        if (paramList != null)
          localArrayList.add(new BasicNameValuePair("source", paramList));
      }
      if (!localHashMap.containsKey("source2"))
      {
        paramList = Environment.source2();
        if (paramList != null)
          localArrayList.add(new BasicNameValuePair("source2", paramList));
      }
      push(localArrayList);
      return;
      label677: i = paramList.getInt("ID");
    }
  }

  public void setUploadInterval(int paramInt)
  {
    super.setUploadInterval(paramInt);
  }

  public void setUploadUrl(String paramString)
  {
    super.setUploadUrl(paramString);
    this.url = paramString;
  }

  public String uploadUrl()
  {
    return this.url;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.statistics.impl.MyStatisticsService
 * JD-Core Version:    0.6.0
 */