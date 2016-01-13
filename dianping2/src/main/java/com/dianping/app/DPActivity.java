package com.dianping.app;

import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.configservice.ConfigService;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.image.ImageService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.ContextMApiService;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.dataservice.mapi.SealedMApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.statistics.StatisticsService;
import com.dianping.statistics.impl.PVProcessStatisticsService;
import com.dianping.util.CrashReportHelper;
import com.dianping.util.DPUrl;
import com.dianping.util.Log;
import com.dianping.util.URLBase64;
import com.dianping.util.log.NovaLog;
import com.dianping.util.mapi.MApiRequestManager;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class DPActivity extends FragmentActivity
{
  private static final HashMap<String, String> manifestUrlMapping = new HashMap();
  private static SharedPreferences prefs;
  private AccountService accountService;
  protected String callId;
  private ConfigService configService;
  private ContextMApiService contextMApiService;
  private final Handler durlElapseHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 5)
        if (DPActivity.this.durlForm != null)
        {
          DPActivity.this.statisticsService().record(DPActivity.this.durlForm);
          DPActivity.access$002(DPActivity.this, null);
        }
      do
      {
        return;
        if (paramMessage.what == 3)
        {
          DPActivity.access$102(DPActivity.this, SystemClock.elapsedRealtime());
          sendEmptyMessageDelayed(4, 600L);
          return;
        }
        if (paramMessage.what == 4)
        {
          if ((DPActivity.this.myMApiService != null) && (DPActivity.MyMApiService.access$300(DPActivity.this.myMApiService) != null))
          {
            sendEmptyMessageDelayed(5, 20000L);
            return;
          }
          if (DPActivity.this.durlForm != null)
          {
            l1 = DPActivity.this.resumeTime - DPActivity.this.startTime;
            if (l1 > 0L)
            {
              DPActivity.this.durlForm.add(new BasicNameValuePair("elapse", String.valueOf(l1)));
              Log.i("pv", DPActivity.this.getMyUrl() + " loaded in " + l1 + "ms (without mapi request)");
            }
            DPActivity.this.statisticsService().record(DPActivity.this.durlForm);
          }
          DPActivity.access$002(DPActivity.this, null);
          return;
        }
        if (paramMessage.what != 1)
          continue;
        sendEmptyMessage(2);
        return;
      }
      while ((paramMessage.what != 2) || (DPActivity.this.durlForm == null));
      long l1 = SystemClock.elapsedRealtime();
      long l2 = l1 - DPActivity.this.startTime;
      if (l2 > 0L)
      {
        DPActivity.this.durlForm.add(new BasicNameValuePair("elapse", String.valueOf(l2)));
        paramMessage = new StringBuilder();
        paramMessage.append(DPActivity.this.getMyUrl()).append(" loaded in ").append(l2).append("ms");
        if ((DPActivity.this.resumeTime > 0L) && (DPActivity.this.responseTime > DPActivity.this.resumeTime))
          paramMessage.append(" (").append(DPActivity.this.resumeTime - DPActivity.this.startTime).append('+').append(DPActivity.this.responseTime - DPActivity.this.resumeTime).append('+').append(l1 - DPActivity.this.responseTime).append(')');
        Log.i("pv", paramMessage.toString());
      }
      DPActivity.this.statisticsService().record(DPActivity.this.durlForm);
      DPActivity.access$002(DPActivity.this, null);
    }
  };
  private ArrayList<NameValuePair> durlForm;
  private boolean forceUploadGa = false;
  public HashMap<String, View> gAViews = new HashMap();
  public GAUserInfo gaExtra = new GAUserInfo();
  public ArrayList<String> gaViewMarked = new ArrayList();
  private HttpService httpService;
  private ImageService imageService;
  private Boolean isActivityLoaded;
  protected boolean isDestroyed;
  public boolean isResumed = false;
  private LocationService locationService;
  private MApiRequestManager mMApiRequestManager;
  private CacheService mapiCacheService;
  private MApiService mapiService;
  private MyMApiService myMApiService;
  private MyStatisticsService myStatService;
  private String pageId;
  private PVProcessStatisticsService pvProcess;
  private String referPageId;
  private long responseTime;
  private long resumeTime;
  private boolean setupTimeoutOnResume;
  private long startTime;
  private StatisticsService statisticsService;

  private String getManifestUrl(String paramString)
  {
    String str2 = (String)manifestUrlMapping.get(paramString);
    if (str2 != null)
      return str2;
    Object localObject3 = null;
    Object localObject2 = null;
    while (true)
    {
      int j;
      String str1;
      try
      {
        XmlResourceParser localXmlResourceParser = createPackageContext(getPackageName(), 0).getAssets().openXmlResourceParser("AndroidManifest.xml");
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        j = localXmlResourceParser.getEventType();
        Object localObject1 = null;
        int i = 0;
        break label499;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        j = localXmlResourceParser.nextToken();
        localObject1 = localObject4;
        break label499;
        if (i == 0)
          continue;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        if (!paramString.equals(localObject1))
          continue;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        if (!localXmlResourceParser.getName().equals("data"))
          continue;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        localObject4 = localXmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "scheme");
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        String str3 = localXmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "host");
        if ((localObject4 == null) || (str3 == null))
          continue;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        if (((String)localObject4).startsWith("http"))
          continue;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        localObject1 = (String)localObject4 + "://" + str3;
        localObject2 = localObject1;
        localObject1 = localObject2;
        if (localXmlResourceParser == null)
          continue;
        localXmlResourceParser.close();
        localObject1 = localObject2;
        manifestUrlMapping.put(paramString, localObject1);
        return localObject1;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        if (!localXmlResourceParser.getName().equals("activity"))
          continue;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        localObject4 = localXmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "name");
        localObject1 = localObject4;
        if (localObject4 == null)
          continue;
        localObject1 = localObject4;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        if (!((String)localObject4).startsWith("."))
          continue;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        localObject1 = getPackageName() + (String)localObject4;
        localObject4 = localObject1;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        if (!localXmlResourceParser.getName().equals("intent-filter"))
          continue;
        i = 1;
        localObject4 = localObject1;
        continue;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        if (!localXmlResourceParser.getName().equals("activity"))
          continue;
        localObject1 = null;
        localObject2 = localXmlResourceParser;
        localObject3 = localXmlResourceParser;
        boolean bool = localXmlResourceParser.getName().equals("intent-filter");
        localObject4 = localObject1;
        if (!bool)
          continue;
        i = 0;
        localObject4 = localObject1;
        continue;
      }
      catch (Exception str1)
      {
        localObject3 = localObject2;
        localException.printStackTrace();
        str1 = str2;
        if (localObject2 == null)
          continue;
        ((XmlResourceParser)localObject2).close();
        str1 = str2;
        continue;
      }
      finally
      {
        if (localObject3 == null)
          continue;
        localObject3.close();
      }
      label499: localObject2 = str2;
      if (j == 1)
        continue;
      switch (j)
      {
      case 2:
      case 3:
      }
      Object localObject4 = str1;
    }
  }

  public static SharedPreferences preferences()
  {
    if (prefs == null)
      prefs = preferences(DPApplication.instance());
    return prefs;
  }

  public static SharedPreferences preferences(Context paramContext)
  {
    return paramContext.getSharedPreferences(paramContext.getPackageName(), 0);
  }

  public AccountService accountService()
  {
    if (this.accountService == null)
      this.accountService = ((AccountService)getService("account"));
    return this.accountService;
  }

  @Deprecated
  public void addGAView(View paramView, int paramInt)
  {
    addGAView(paramView, paramInt, null, true);
  }

  @Deprecated
  public void addGAView(View paramView, int paramInt, String paramString, boolean paramBoolean)
  {
    GAHelper.instance().addGAView(this, paramView, paramInt, paramString, paramBoolean);
  }

  public String callId()
  {
    return this.callId;
  }

  public City city()
  {
    return DPApplication.instance().cityConfig().currentCity();
  }

  public CityConfig cityConfig()
  {
    return DPApplication.instance().cityConfig();
  }

  public int cityId()
  {
    return city().id();
  }

  public ConfigService configService()
  {
    if (this.configService == null)
      this.configService = ((ConfigService)getService("config"));
    return this.configService;
  }

  public void generateCallId()
  {
    this.callId = UUID.randomUUID().toString();
  }

  public boolean getBooleanParam(String paramString)
  {
    return getBooleanParam(paramString, false);
  }

  public boolean getBooleanParam(String paramString, boolean paramBoolean)
  {
    Intent localIntent = getIntent();
    try
    {
      Object localObject = localIntent.getData();
      if (localObject != null)
      {
        localObject = ((Uri)localObject).getQueryParameter(paramString);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          boolean bool = Boolean.parseBoolean((String)localObject);
          return bool;
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localIntent.getBooleanExtra(paramString, paramBoolean);
  }

  public byte getByteParam(String paramString)
  {
    return getByteParam(paramString, 0);
  }

  public byte getByteParam(String paramString, byte paramByte)
  {
    Intent localIntent = getIntent();
    try
    {
      Object localObject = localIntent.getData();
      if (localObject != null)
      {
        localObject = ((Uri)localObject).getQueryParameter(paramString);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          int i = Byte.parseByte((String)localObject);
          return i;
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localIntent.getByteExtra(paramString, paramByte);
  }

  public char getCharParam(String paramString)
  {
    return getCharParam(paramString, '\000');
  }

  public char getCharParam(String paramString, char paramChar)
  {
    Intent localIntent = getIntent();
    try
    {
      Object localObject = localIntent.getData();
      if (localObject != null)
      {
        localObject = ((Uri)localObject).getQueryParameter(paramString);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          int i = ((String)localObject).charAt(0);
          return i;
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localIntent.getCharExtra(paramString, paramChar);
  }

  public GAUserInfo getCloneUserInfo()
  {
    return (GAUserInfo)this.gaExtra.clone();
  }

  public double getDoubleParam(String paramString)
  {
    return getDoubleParam(paramString, 0.0D);
  }

  public double getDoubleParam(String paramString, double paramDouble)
  {
    Intent localIntent = getIntent();
    try
    {
      Object localObject = localIntent.getData();
      if (localObject != null)
      {
        localObject = ((Uri)localObject).getQueryParameter(paramString);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          double d = Double.parseDouble((String)localObject);
          return d;
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localIntent.getDoubleExtra(paramString, paramDouble);
  }

  public float getFloatParam(String paramString)
  {
    return getFloatParam(paramString, 0.0F);
  }

  public float getFloatParam(String paramString, float paramFloat)
  {
    Intent localIntent = getIntent();
    try
    {
      Object localObject = localIntent.getData();
      if (localObject != null)
      {
        localObject = ((Uri)localObject).getQueryParameter(paramString);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          float f = Float.parseFloat((String)localObject);
          return f;
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localIntent.getFloatExtra(paramString, paramFloat);
  }

  public int getIntParam(String paramString)
  {
    return getIntParam(paramString, 0);
  }

  public int getIntParam(String paramString, int paramInt)
  {
    Intent localIntent = getIntent();
    try
    {
      Object localObject = localIntent.getData();
      if (localObject != null)
      {
        localObject = ((Uri)localObject).getQueryParameter(paramString);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          int i = Integer.parseInt((String)localObject);
          return i;
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localIntent.getIntExtra(paramString, paramInt);
  }

  public long getLongParam(String paramString)
  {
    return getLongParam(paramString, 0L);
  }

  public long getLongParam(String paramString, long paramLong)
  {
    Intent localIntent = getIntent();
    try
    {
      Object localObject = localIntent.getData();
      if (localObject != null)
      {
        localObject = ((Uri)localObject).getQueryParameter(paramString);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          long l = Long.parseLong((String)localObject);
          return l;
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localIntent.getLongExtra(paramString, paramLong);
  }

  public String getMyUrl()
  {
    Object localObject;
    if (getIntent().getDataString() != null)
      localObject = getIntent().getDataString();
    String str2;
    String str1;
    do
    {
      return localObject;
      str2 = getClass().getName();
      str1 = getManifestUrl(str2);
      localObject = str1;
    }
    while (str1 != null);
    return (String)("class://" + str2);
  }

  public DPObject getObjectParam(String paramString)
  {
    Intent localIntent = getIntent();
    try
    {
      Object localObject = localIntent.getData();
      if (localObject != null)
      {
        localObject = ((Uri)localObject).getQueryParameter(paramString);
        if (localObject != null)
        {
          localObject = URLBase64.decode((String)localObject);
          localObject = new DPObject(localObject, 0, localObject.length);
          return localObject;
        }
      }
    }
    catch (Exception localException)
    {
    }
    return (DPObject)(DPObject)localIntent.getParcelableExtra(paramString);
  }

  public String getPageName()
  {
    if (getIntent().getData() != null)
      return getIntent().getData().getHost();
    return getMyUrl();
  }

  public Object getService(String paramString)
  {
    if ("mapi".equals(paramString))
    {
      if (this.myMApiService == null)
      {
        this.contextMApiService = new ContextMApiService(this, (MApiService)DPApplication.instance().getService("mapi"));
        this.myMApiService = new MyMApiService(this.contextMApiService);
      }
      return this.myMApiService;
    }
    if ("statistics".equals(paramString))
    {
      if (this.myStatService == null)
        this.myStatService = new MyStatisticsService((StatisticsService)DPApplication.instance().getService("statistics"));
      return this.myStatService;
    }
    return DPApplication.instance().getService(paramString);
  }

  public short getShortParam(String paramString)
  {
    return getShortParam(paramString, 0);
  }

  public short getShortParam(String paramString, short paramShort)
  {
    Intent localIntent = getIntent();
    try
    {
      Object localObject = localIntent.getData();
      if (localObject != null)
      {
        localObject = ((Uri)localObject).getQueryParameter(paramString);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          int i = Short.parseShort((String)localObject);
          return i;
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localIntent.getShortExtra(paramString, paramShort);
  }

  public String getStringParam(String paramString)
  {
    Intent localIntent = getIntent();
    try
    {
      Object localObject = localIntent.getData();
      if (localObject != null)
      {
        localObject = ((Uri)localObject).getQueryParameter(paramString);
        if (localObject != null)
          return localObject;
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return (String)localIntent.getStringExtra(paramString);
  }

  public HttpService httpService()
  {
    if (this.httpService == null)
      this.httpService = ((HttpService)getService("http"));
    return this.httpService;
  }

  public ImageService imageService()
  {
    if (this.imageService == null)
      this.imageService = ((ImageService)getService("image"));
    return this.imageService;
  }

  public LocationService locationService()
  {
    if (this.locationService == null)
      this.locationService = ((LocationService)getService("location"));
    return this.locationService;
  }

  public CacheService mapiCacheService()
  {
    if (this.mapiCacheService == null)
      this.mapiCacheService = ((CacheService)getService("mapi_cache"));
    return this.mapiCacheService;
  }

  public MApiRequest mapiGet(RequestHandler<MApiRequest, MApiResponse> paramRequestHandler, String paramString, CacheType paramCacheType)
  {
    paramString = BasicMApiRequest.mapiGet(paramString, paramCacheType);
    if (this.mMApiRequestManager == null)
      this.mMApiRequestManager = new MApiRequestManager(mapiService());
    this.mMApiRequestManager.addRequest(paramString, paramRequestHandler);
    return paramString;
  }

  public MApiRequest mapiPost(RequestHandler<MApiRequest, MApiResponse> paramRequestHandler, String paramString, String[] paramArrayOfString)
  {
    paramString = BasicMApiRequest.mapiPost(paramString, paramArrayOfString);
    if (this.mMApiRequestManager == null)
      this.mMApiRequestManager = new MApiRequestManager(mapiService());
    this.mMApiRequestManager.addRequest(paramString, paramRequestHandler);
    return paramString;
  }

  public MApiService mapiService()
  {
    if (this.mapiService == null)
      this.mapiService = ((MApiService)getService("mapi"));
    return this.mapiService;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DPApplication.instance().activityOnCreate(this);
    prefs = preferences(this);
    this.pvProcess = ((PVProcessStatisticsService)getService("pvprocess"));
    Object localObject;
    label55: ArrayList localArrayList;
    if (paramBundle == null)
    {
      localObject = UUID.randomUUID().toString();
      this.callId = ((String)localObject);
      if (paramBundle != null)
        break label342;
      localObject = null;
      this.pageId = ((String)localObject);
      this.referPageId = getIntent().getStringExtra("_referId");
      if (paramBundle == null)
      {
        paramBundle = getMyUrl();
        if (paramBundle != null)
        {
          localObject = getIntent().getStringExtra("_refer");
          String str = this.referPageId;
          localArrayList = new ArrayList(2);
          localArrayList.add(new BasicNameValuePair("durl", paramBundle));
          if (localObject == null)
            break label353;
          localArrayList.add(new BasicNameValuePair("page", ""));
          localArrayList.add(new BasicNameValuePair("prevpage", (String)localObject));
          if (str != null)
            localArrayList.add(new BasicNameValuePair("prevpageid", str));
          Log.i("pv", "durl = " + paramBundle + " & refer = " + (String)localObject);
        }
      }
    }
    while (true)
    {
      this.durlForm = localArrayList;
      this.startTime = getIntent().getLongExtra("_startTime", SystemClock.elapsedRealtime());
      this.setupTimeoutOnResume = true;
      paramBundle = getIntent().getStringExtra("_startActivityWithUrlWarning");
      if (!TextUtils.isEmpty(paramBundle))
      {
        localObject = new AlertDialog.Builder(this);
        ((AlertDialog.Builder)localObject).setTitle("开发警告");
        ((AlertDialog.Builder)localObject).setIcon(17301543);
        ((AlertDialog.Builder)localObject).setMessage(paramBundle);
        ((AlertDialog.Builder)localObject).show();
      }
      this.forceUploadGa = GAHelper.instance().getUtmAndMarketingSource(this.gaExtra, getIntent().getData());
      return;
      localObject = paramBundle.getString("callid");
      break;
      label342: localObject = paramBundle.getString("pageid");
      break label55;
      label353: Log.i("pv", "durl = " + paramBundle);
      localArrayList.add(new BasicNameValuePair("page", ""));
      localArrayList.add(new BasicNameValuePair("prevpage", ""));
      localArrayList.add(new BasicNameValuePair("prevpageid", ""));
    }
  }

  protected void onDestroy()
  {
    this.callId = null;
    DPApplication.instance().activityOnDestory(this);
    this.isDestroyed = true;
    this.pvProcess.unregister(this);
    if (this.myMApiService != null)
      this.myMApiService.onDestroy();
    if (this.durlForm != null)
    {
      statisticsService().record(this.durlForm);
      this.durlForm = null;
      this.durlElapseHandler.removeMessages(4);
      this.durlElapseHandler.removeMessages(5);
    }
    if (this.mMApiRequestManager != null)
      this.mMApiRequestManager.clearAll();
    super.onDestroy();
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    GAHelper.instance().onNewGAPager(paramGAUserInfo, this, this.forceUploadGa);
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
    this.forceUploadGa = GAHelper.instance().getUtmAndMarketingSource(this.gaExtra, getIntent().getData());
  }

  protected void onPause()
  {
    DPApplication.instance().activityOnPause(this);
    super.onPause();
    this.isResumed = false;
  }

  protected void onResume()
  {
    super.onResume();
    CrashReportHelper.putUrlSchemaOnShow(getMyUrl());
    if ((this.setupTimeoutOnResume) && (this.isActivityLoaded == null))
    {
      this.setupTimeoutOnResume = false;
      this.durlElapseHandler.sendEmptyMessage(3);
    }
    DPApplication.instance().activityOnResume(this);
    onNewGAPager(this.gaExtra);
    showGAViewOnResume(null);
    this.isResumed = true;
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("callid", this.callId);
    paramBundle.putString("pageid", this.pageId);
    super.onSaveInstanceState(paramBundle);
  }

  public String pageId()
  {
    if (TextUtils.isEmpty(this.pageId))
      this.pageId = UUID.randomUUID().toString();
    return this.pageId;
  }

  public String prevPageId()
  {
    return this.referPageId;
  }

  @Deprecated
  public void removeGAView(String paramString)
  {
    GAHelper.instance().removeGAView(this, paramString);
  }

  public void resetPageId()
  {
    this.pageId = null;
  }

  public void setActivityIsLoaded(boolean paramBoolean)
  {
    if (this.durlForm == null)
      return;
    this.isActivityLoaded = Boolean.valueOf(paramBoolean);
    if (paramBoolean)
      this.durlElapseHandler.sendEmptyMessage(1);
    this.durlElapseHandler.removeMessages(4);
    this.durlElapseHandler.removeMessages(5);
  }

  public void setPageId(String paramString)
  {
    this.pageId = paramString;
  }

  @Deprecated
  public void showGAView(String paramString)
  {
    GAHelper.instance().showGAView(this, paramString);
  }

  protected void showGAViewOnResume(String paramString)
  {
    GAHelper.instance().showGAView(this, null);
  }

  public void startActivity(Intent paramIntent)
  {
    super.startActivity(paramIntent);
  }

  public void startActivity(DPUrl paramDPUrl)
  {
    startActivity(paramDPUrl.getIntent());
  }

  public void startActivity(String paramString)
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramString)));
  }

  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    if (this.durlForm != null)
    {
      statisticsService().record(this.durlForm);
      this.durlForm = null;
    }
    paramIntent = urlMap(paramIntent);
    paramIntent.putExtra("_refer", getMyUrl());
    paramIntent.putExtra("_referId", pageId());
    paramIntent.putExtra("_startTime", SystemClock.elapsedRealtime());
    CrashReportHelper.putUrlSchema(paramIntent.getDataString());
    try
    {
      super.startActivityForResult(paramIntent, paramInt);
      return;
    }
    catch (Exception paramIntent)
    {
      NovaLog.e("START ACTIVITY", "Exception e: " + paramIntent.toString() + " this activity: " + getPageName());
    }
  }

  public void startActivityForResult(DPUrl paramDPUrl, int paramInt)
  {
    startActivityForResult(paramDPUrl.getIntent(), paramInt);
  }

  public void startActivityForResult(String paramString, int paramInt)
  {
    startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse(paramString)), paramInt);
  }

  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt)
  {
    if (this.durlForm != null)
    {
      statisticsService().record(this.durlForm);
      this.durlForm = null;
    }
    paramIntent = urlMap(paramIntent);
    paramIntent.putExtra("_refer", getMyUrl());
    paramIntent.putExtra("_referId", pageId());
    paramIntent.putExtra("_startTime", SystemClock.elapsedRealtime());
    try
    {
      super.startActivityFromFragment(paramFragment, paramIntent, paramInt);
      return;
    }
    catch (Exception paramFragment)
    {
      NovaLog.e("START ACTIVITY", "Exception e: " + paramFragment.toString() + " this activity: " + getPageName());
    }
  }

  @Deprecated
  public void statisticsEvent(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    statisticsEvent(paramString1, paramString2, paramString3, paramInt, null);
  }

  @Deprecated
  public void statisticsEvent(String paramString1, String paramString2, String paramString3, int paramInt, List<NameValuePair> paramList)
  {
    ((StatisticsService)getService("statistics")).event(paramString1, paramString2, paramString3, paramInt, paramList);
  }

  public StatisticsService statisticsService()
  {
    if (this.statisticsService == null)
      this.statisticsService = ((StatisticsService)getService("statistics"));
    return this.statisticsService;
  }

  public Intent urlMap(Intent paramIntent)
  {
    if (!Environment.isDebug());
    while (true)
    {
      Object localObject1 = getApplication();
      Intent localIntent = paramIntent;
      if ((localObject1 instanceof DPApplication))
        localIntent = ((DPApplication)localObject1).urlMap(paramIntent);
      return localIntent;
      if ((!TextUtils.isEmpty(paramIntent.getDataString())) || (paramIntent.getComponent() == null) || (!getPackageName().equals(paramIntent.getComponent().getPackageName())) || (TextUtils.isEmpty(paramIntent.getComponent().getClassName())))
        continue;
      localObject1 = getManifestUrl(paramIntent.getComponent().getClassName());
      if (TextUtils.isEmpty((CharSequence)localObject1))
        continue;
      Object localObject2 = new Exception().getStackTrace();
      localIntent = null;
      int i = 0;
      while (i < localObject2.length)
      {
        if (localObject2[i].getMethodName().startsWith("startActivity"))
          localIntent = localObject2[(i + 1)];
        i += 1;
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("不规范的 startActivity 方式\n");
      ((StringBuilder)localObject2).append("可以由 ").append((String)localObject1).append(" 替代\n");
      if (localIntent != null)
        ((StringBuilder)localObject2).append(localIntent.getFileName()).append(" [L").append(localIntent.getLineNumber()).append(']');
      i = 0;
      try
      {
        boolean bool = DPActivity.class.isAssignableFrom(Class.forName(paramIntent.getComponent().getClassName()));
        if (bool)
          i = 1;
        label253: if (i != 0)
        {
          paramIntent.putExtra("_startActivityWithUrlWarning", ((StringBuilder)localObject2).toString());
          continue;
        }
        Toast.makeText(this, ((StringBuilder)localObject2).toString(), 1).show();
      }
      catch (Exception localException)
      {
        break label253;
      }
    }
  }

  private class MyMApiService extends SealedMApiService
  {
    private MApiRequest firstRequest;
    private String resetPageIdBin;

    public MyMApiService(MApiService arg2)
    {
      super(localMApiService);
    }

    private String getBinName(String paramString)
    {
      try
      {
        int i = paramString.indexOf('?');
        paramString = paramString.substring(paramString.lastIndexOf('/', i - 1) + 1, i);
        return paramString;
      }
      catch (Exception paramString)
      {
      }
      return null;
    }

    private boolean isFirstPage(String paramString)
    {
      return (paramString.contains("&start=0")) || (paramString.contains("?start=0"));
    }

    private boolean resetPageIdTrigger(String paramString)
    {
      Object localObject = ConfigHelper.resetPageIdTrigger;
      if (TextUtils.isEmpty((CharSequence)localObject));
      while (true)
      {
        return false;
        String str1 = DPActivity.this.getMyUrl();
        try
        {
          localObject = new StringTokenizer((String)localObject, "|");
          boolean bool;
          do
          {
            String str3;
            String str2;
            do
            {
              if (!((StringTokenizer)localObject).hasMoreTokens())
                break;
              str3 = ((StringTokenizer)localObject).nextToken();
              int i = str3.indexOf('#');
              str2 = str3.substring(0, i);
              str3 = str3.substring(i + 1);
            }
            while (!str1.contains(str2));
            bool = paramString.contains(str3);
          }
          while (!bool);
          return true;
        }
        catch (Exception paramString)
        {
        }
      }
      return false;
    }

    public void abort(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler, boolean paramBoolean)
    {
      super.abort(paramMApiRequest, paramRequestHandler, paramBoolean);
      if (paramMApiRequest == this.firstRequest)
      {
        DPActivity.access$402(DPActivity.this, 0L);
        if ((DPActivity.this.durlForm != null) && (DPActivity.this.isActivityLoaded == null))
        {
          DPActivity.this.durlElapseHandler.sendEmptyMessage(1);
          DPActivity.this.durlElapseHandler.removeMessages(4);
          DPActivity.this.durlElapseHandler.removeMessages(5);
        }
      }
    }

    public void exec(MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler)
    {
      boolean bool2 = false;
      boolean bool1;
      if (this.firstRequest == null)
      {
        String str = paramMApiRequest.url();
        bool1 = bool2;
        if (isFirstPage(str))
        {
          this.resetPageIdBin = getBinName(str);
          bool1 = bool2;
        }
      }
      while (true)
      {
        bool2 = bool1;
        if (!bool1)
        {
          bool2 = bool1;
          if (isFirstPage(paramMApiRequest.url()))
            bool2 = resetPageIdTrigger(paramMApiRequest.url());
        }
        if (bool2)
          DPActivity.this.resetPageId();
        if (!DPActivity.this.isDestroyed)
          DPActivity.this.pvProcess.register(DPActivity.this, paramMApiRequest);
        super.exec(paramMApiRequest, paramRequestHandler);
        if (this.firstRequest == null)
          this.firstRequest = paramMApiRequest;
        return;
        bool1 = bool2;
        if (this.resetPageIdBin == null)
          continue;
        bool1 = bool2;
        if (!isFirstPage(paramMApiRequest.url()))
          continue;
        bool1 = bool2;
        if (!this.resetPageIdBin.equals(getBinName(paramMApiRequest.url())))
          continue;
        bool1 = true;
      }
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFailed(paramMApiRequest, paramMApiResponse);
      if ((paramMApiRequest == this.firstRequest) && (DPActivity.this.durlForm != null) && (DPActivity.this.isActivityLoaded == null))
      {
        DPActivity.access$502(DPActivity.this, SystemClock.elapsedRealtime());
        DPActivity.this.durlElapseHandler.sendEmptyMessage(1);
        DPActivity.this.durlElapseHandler.removeMessages(4);
        DPActivity.this.durlElapseHandler.removeMessages(5);
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      if ((paramMApiRequest == this.firstRequest) && (DPActivity.this.durlForm != null) && (DPActivity.this.isActivityLoaded == null))
      {
        DPActivity.access$502(DPActivity.this, SystemClock.elapsedRealtime());
        DPActivity.this.durlElapseHandler.sendEmptyMessage(1);
        DPActivity.this.durlElapseHandler.removeMessages(4);
        DPActivity.this.durlElapseHandler.removeMessages(5);
      }
    }
  }

  private class MyStatisticsService
    implements StatisticsService
  {
    StatisticsService stat;

    public MyStatisticsService(StatisticsService arg2)
    {
      Object localObject;
      this.stat = localObject;
    }

    public void event(String paramString1, String paramString2, String paramString3, int paramInt, List<NameValuePair> paramList)
    {
      this.stat.event(paramString1, paramString2, paramString3, paramInt, page(paramList));
    }

    public void flush()
    {
      this.stat.flush();
    }

    List<NameValuePair> page(List<NameValuePair> paramList)
    {
      String str = DPActivity.this.prevPageId();
      if (paramList == null)
      {
        paramList = new ArrayList(3);
        paramList.add(new BasicNameValuePair("page", DPActivity.this.getMyUrl()));
        paramList.add(new BasicNameValuePair("pageid", DPActivity.this.pageId()));
        if (str != null)
          paramList.add(new BasicNameValuePair("prevpageid", str));
        return paramList;
      }
      int k = 0;
      int j = 0;
      int i = 0;
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        NameValuePair localNameValuePair = (NameValuePair)localIterator.next();
        m = k;
        if ("page".equals(localNameValuePair.getName()))
          m = 1;
        int n = j;
        if ("pageid".equals(localNameValuePair.getName()))
          n = 1;
        k = m;
        j = n;
        if (!"prevpageid".equals(localNameValuePair.getName()))
          continue;
        i = 1;
        k = m;
        j = n;
      }
      int m = i;
      if (i == 0)
      {
        m = i;
        if (str == null)
          m = 1;
      }
      if ((k != 0) && (j != 0) && (m != 0))
        return paramList;
      paramList = new ArrayList(paramList);
      if (k == 0)
        paramList.add(new BasicNameValuePair("page", DPActivity.this.getMyUrl()));
      if (j == 0)
        paramList.add(new BasicNameValuePair("pageid", DPActivity.this.pageId()));
      if ((m == 0) && (str != null))
        paramList.add(new BasicNameValuePair("prevpageid", str));
      return paramList;
    }

    public void pageView(String paramString, List<NameValuePair> paramList)
    {
      this.stat.pageView(paramString, page(paramList));
    }

    public void push(String paramString)
    {
      this.stat.push(paramString);
    }

    public void record(List<NameValuePair> paramList)
    {
      this.stat.record(page(paramList));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.app.DPActivity
 * JD-Core Version:    0.6.0
 */