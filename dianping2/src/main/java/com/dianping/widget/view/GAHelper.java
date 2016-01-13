package com.dianping.widget.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.StringInputStream;
import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.http.NetworkInfoHelper;
import com.dianping.dataservice.mapi.MApiDebugAgent;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.statistics.impl.NewStatisticsService;
import com.dianping.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import org.json.JSONObject;

public class GAHelper
{
  public static final String ACTION_SLIDE = "slide";
  public static final String ACTION_TAP = "tap";
  public static final String ACTION_VIEW = "view";
  private static String GAPagerName;
  private static String GAReferPagerName;
  private static String referRequestId;
  public static String requestId = "";
  private AccountService mAccountService;
  private LocationService mLocationService;
  private NetworkInfoHelper mNetworkInfoHelper;
  private NewStatisticsService mStatisticsService;

  static
  {
    referRequestId = "";
    GAReferPagerName = null;
    GAPagerName = null;
  }

  private void AddGAUserInfo(Map<String, String> paramMap, GAUserInfo paramGAUserInfo1, GAUserInfo paramGAUserInfo2)
  {
    if (paramGAUserInfo2 == null);
    while (true)
    {
      return;
      try
      {
        Field[] arrayOfField = paramGAUserInfo2.getClass().getFields();
        Object localObject2 = null;
        int j = arrayOfField.length;
        int i = 0;
        while (i < j)
        {
          Field localField = arrayOfField[i];
          if (paramGAUserInfo1 != null)
            localObject2 = localField.get(paramGAUserInfo1);
          Object localObject1 = localObject2;
          if (localObject2 == null)
            localObject1 = localField.get(paramGAUserInfo2);
          localObject2 = localObject1;
          if (localObject1 != null)
          {
            paramMap.put(localField.getName(), localObject1.toString());
            localObject2 = null;
          }
          i += 1;
        }
      }
      catch (Exception paramMap)
      {
        Log.e(paramMap.toString());
      }
    }
  }

  private AccountService accountService()
  {
    if (this.mAccountService == null)
      this.mAccountService = DPApplication.instance().accountService();
    return this.mAccountService;
  }

  // ERROR //
  private void contextStatisticsEvent(Context paramContext, String paramString1, String paramString2, int paramInt, GAUserInfo paramGAUserInfo, String paramString3)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload 5
    //   4: astore 7
    //   6: aload 5
    //   8: ifnonnull +28 -> 36
    //   11: new 107	com/dianping/widget/view/GAUserInfo
    //   14: dup
    //   15: invokespecial 108	com/dianping/widget/view/GAUserInfo:<init>	()V
    //   18: astore 7
    //   20: aload 7
    //   22: aload_3
    //   23: putfield 111	com/dianping/widget/view/GAUserInfo:title	Ljava/lang/String;
    //   26: aload 7
    //   28: iload 4
    //   30: invokestatic 117	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   33: putfield 121	com/dianping/widget/view/GAUserInfo:index	Ljava/lang/Integer;
    //   36: aload_0
    //   37: aload_1
    //   38: aload_2
    //   39: aload 6
    //   41: aload 7
    //   43: iconst_0
    //   44: invokespecial 125	com/dianping/widget/view/GAHelper:uploadGA	(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Lcom/dianping/widget/view/GAUserInfo;Z)V
    //   47: aload_0
    //   48: monitorexit
    //   49: return
    //   50: astore_1
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_1
    //   54: athrow
    //   55: astore_1
    //   56: goto -5 -> 51
    //
    // Exception table:
    //   from	to	target	type
    //   11	20	50	finally
    //   36	47	50	finally
    //   20	36	55	finally
  }

  private JSONObject getGAJson(Map<String, String> paramMap1, Map<String, String> paramMap2)
  {
    JSONObject localJSONObject1 = new JSONObject();
    JSONObject localJSONObject3 = new JSONObject();
    JSONObject localJSONObject2 = new JSONObject();
    try
    {
      paramMap2 = paramMap2.entrySet().iterator();
      while (paramMap2.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)paramMap2.next();
        if (localEntry.getValue() == null)
          continue;
        localJSONObject3.put((String)localEntry.getKey(), localEntry.getValue());
      }
    }
    catch (Exception paramMap1)
    {
      paramMap1.printStackTrace();
      return localJSONObject1;
    }
    localJSONObject1.put("ga", localJSONObject3);
    paramMap1 = paramMap1.entrySet().iterator();
    while (paramMap1.hasNext())
    {
      paramMap2 = (Map.Entry)paramMap1.next();
      if (paramMap2.getValue() == null)
        continue;
      localJSONObject2.put((String)paramMap2.getKey(), paramMap2.getValue());
    }
    localJSONObject1.put("essential", localJSONObject2);
    return localJSONObject1;
  }

  public static GAHelper instance()
  {
    return GAHelperInner.instance;
  }

  private LocationService locationService()
  {
    if (this.mLocationService == null)
      this.mLocationService = DPApplication.instance().locationService();
    return this.mLocationService;
  }

  private NetworkInfoHelper networkInfoHelper()
  {
    if (this.mNetworkInfoHelper == null)
      this.mNetworkInfoHelper = new NetworkInfoHelper(DPApplication.instance());
    return this.mNetworkInfoHelper;
  }

  private void putEnvironment(Map<String, String> paramMap)
  {
    paramMap.put("city_id", String.valueOf(DPApplication.instance().city().id()));
    paramMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
    if (locationService().city() != null)
      paramMap.put("locate_city_id", String.valueOf(locationService().city().getInt("ID")));
    if (locationService().hasLocation())
    {
      paramMap.put("longitude", String.valueOf(locationService().location().getDouble("Lat")));
      paramMap.put("latitude", String.valueOf(locationService().location().getDouble("Lng")));
    }
    paramMap.put("userid", String.valueOf(accountService().id()));
    paramMap.put("dpid", DPActivity.preferences(DPApplication.instance()).getString("dpid", ""));
    paramMap.put("network", networkInfoHelper().getNetworkInfo());
    paramMap.put("log_id", UUID.randomUUID().toString());
    if (Log.LEVEL <= 6)
      paramMap.put("debug", "1");
  }

  private String removeDpHost(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      return paramString.replaceFirst("dianping://", "");
    return null;
  }

  private NewStatisticsService statisticsService()
  {
    if (this.mStatisticsService == null)
      this.mStatisticsService = ((NewStatisticsService)DPApplication.instance().getService("statistics_new"));
    return this.mStatisticsService;
  }

  private void uploadGA(Context paramContext, String paramString1, String paramString2, GAUserInfo paramGAUserInfo, boolean paramBoolean)
  {
    monitorenter;
    try
    {
      HashMap localHashMap = new HashMap();
      Object localObject = null;
      if (!TextUtils.isEmpty(paramString1))
        localHashMap.put("element_id", paramString1);
      if (!TextUtils.isEmpty(paramString2))
        localHashMap.put("event_type", paramString2);
      paramContext = getDpActivity(paramContext);
      if ((paramContext instanceof DPActivity))
      {
        AddGAUserInfo(localHashMap, paramGAUserInfo, ((DPActivity)paramContext).getCloneUserInfo());
        localHashMap.put("page_name", removeDpHost(GAPagerName));
        localHashMap.put("refer_page_name", removeDpHost(GAReferPagerName));
        localHashMap.put("request_id", requestId);
        localHashMap.put("refer_request_id", referRequestId);
      }
      while (true)
      {
        paramContext = localObject;
        if (Environment.isDebug())
          paramContext = (Map)localHashMap.clone();
        putEnvironment(localHashMap);
        statisticsService().record(localHashMap);
        if (paramBoolean)
          statisticsService().flush();
        if (Environment.isDebug())
        {
          Log.i("GA_ALL", paramContext.toString());
          uploadGAToMock(paramContext, localHashMap);
        }
        return;
        AddGAUserInfo(localHashMap, paramGAUserInfo, new GAUserInfo());
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramContext;
  }

  private void uploadGAToMock(Map<String, String> paramMap1, Map<String, String> paramMap2)
  {
    Object localObject = (MApiDebugAgent)DPApplication.instance().getService("mapi_debug");
    String str = ((MApiDebugAgent)localObject).switchDomain();
    localObject = switchNewGADomain(((MApiDebugAgent)localObject).newGADebugDomain());
    paramMap1 = getGAJson(paramMap1, paramMap2);
    paramMap2 = DPApplication.instance().httpService();
    1 local1 = new RequestHandler()
    {
      public void onRequestFailed(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
      {
      }

      public void onRequestFinish(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
      {
      }
    };
    if (!TextUtils.isEmpty(str))
      paramMap2.exec(new BasicHttpRequest(str + "mobile-watch-auto-ga.js", "POST", new StringInputStream(paramMap1.toString(), "UTF-8")), local1);
    if (!TextUtils.isEmpty((CharSequence)localObject))
      paramMap2.exec(new BasicHttpRequest((String)localObject, "POST", new StringInputStream(paramMap1.toString(), "UTF-8")), local1);
  }

  public void addGAView(DPActivity paramDPActivity, View paramView, int paramInt, String paramString, boolean paramBoolean)
  {
    ViewStatistics.instance().addGAView(paramDPActivity, paramView, paramInt, paramString, paramBoolean);
  }

  public void contextStatisticsEvent(Context paramContext, String paramString1, GAUserInfo paramGAUserInfo, String paramString2)
  {
    contextStatisticsEvent(paramContext, paramString1, null, 2147483647, paramGAUserInfo, paramString2);
  }

  public void contextStatisticsEvent(Context paramContext, String paramString1, String paramString2, int paramInt, String paramString3)
  {
    contextStatisticsEvent(paramContext, paramString1, paramString2, paramInt, null, paramString3);
  }

  public Context getDpActivity(Context paramContext)
  {
    Context localContext = paramContext;
    if (!(paramContext instanceof DPActivity))
    {
      localContext = paramContext;
      if ((paramContext instanceof ContextWrapper))
        localContext = ((ContextWrapper)paramContext).getBaseContext();
    }
    return localContext;
  }

  // ERROR //
  public String getElementIdByView(View paramView)
  {
    // Byte code:
    //   0: ldc 35
    //   2: astore_2
    //   3: aload_1
    //   4: invokevirtual 57	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: ldc_w 458
    //   10: iconst_0
    //   11: anewarray 59	java/lang/Class
    //   14: invokevirtual 462	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   17: aload_1
    //   18: iconst_0
    //   19: anewarray 4	java/lang/Object
    //   22: invokevirtual 468	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   25: checkcast 160	java/lang/String
    //   28: astore_3
    //   29: aload_3
    //   30: astore_2
    //   31: aload_2
    //   32: astore_3
    //   33: aload_2
    //   34: invokestatic 308	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   37: ifeq +29 -> 66
    //   40: aload_1
    //   41: invokevirtual 474	android/view/View:getResources	()Landroid/content/res/Resources;
    //   44: aload_1
    //   45: invokevirtual 477	android/view/View:getId	()I
    //   48: invokevirtual 482	android/content/res/Resources:getResourceName	(I)Ljava/lang/String;
    //   51: astore_1
    //   52: aload_1
    //   53: aload_1
    //   54: ldc_w 484
    //   57: invokevirtual 487	java/lang/String:lastIndexOf	(Ljava/lang/String;)I
    //   60: iconst_1
    //   61: iadd
    //   62: invokevirtual 490	java/lang/String:substring	(I)Ljava/lang/String;
    //   65: astore_3
    //   66: aload_3
    //   67: areturn
    //   68: astore_3
    //   69: invokestatic 358	com/dianping/app/Environment:isDebug	()Z
    //   72: ifeq +12 -> 84
    //   75: ldc_w 492
    //   78: invokestatic 89	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   81: goto -50 -> 31
    //   84: ldc 35
    //   86: areturn
    //   87: astore_1
    //   88: ldc_w 494
    //   91: invokestatic 497	com/dianping/util/Log:d	(Ljava/lang/String;)V
    //   94: aload_2
    //   95: astore_3
    //   96: goto -30 -> 66
    //
    // Exception table:
    //   from	to	target	type
    //   3	29	68	java/lang/Exception
    //   33	66	87	java/lang/Exception
  }

  public int getIndexByView(View paramView)
  {
    try
    {
      paramView = (GAUserInfo)paramView.getClass().getMethod("getGAUserInfo", new Class[0]).invoke(paramView, new Object[0]);
      if (paramView != null)
      {
        if (paramView.index == null)
          return 2147483647;
        int i = paramView.index.intValue();
        return i;
      }
    }
    catch (Exception paramView)
    {
      Log.d("contextStatisticsEvent getMethod error: " + paramView);
    }
    return 2147483647;
  }

  public boolean getUtmAndMarketingSource(GAUserInfo paramGAUserInfo, Uri paramUri)
  {
    return ViewStatistics.instance().getUtmAndMarketingSource(paramGAUserInfo, paramUri);
  }

  public boolean isViewOnScreen(DPActivity paramDPActivity, View paramView)
  {
    return ViewStatistics.instance().isViewOnScreen(paramDPActivity, paramView);
  }

  public void onNewGAPager(GAUserInfo paramGAUserInfo, DPActivity paramDPActivity, boolean paramBoolean)
  {
    ViewStatistics.instance().onNewGAPager(paramGAUserInfo, paramDPActivity, paramBoolean);
  }

  public void removeGAView(DPActivity paramDPActivity, String paramString)
  {
    ViewStatistics.instance().removeGAView(paramDPActivity, paramString);
  }

  public void setGAPageName(String paramString)
  {
    GAReferPagerName = GAPagerName;
    GAPagerName = paramString;
  }

  public void setRequestId(Context paramContext, String paramString, GAUserInfo paramGAUserInfo, boolean paramBoolean)
  {
    monitorenter;
    try
    {
      paramContext = getDpActivity(paramContext);
      if ((paramContext instanceof DPActivity))
      {
        referRequestId = requestId;
        requestId = paramString;
        uploadGA((DPActivity)paramContext, "pageview", "view", paramGAUserInfo, true);
      }
      monitorexit;
      return;
    }
    finally
    {
      paramContext = finally;
      monitorexit;
    }
    throw paramContext;
  }

  public void showGAView(DPActivity paramDPActivity, String paramString)
  {
    ViewStatistics.instance().showGAView(paramDPActivity, paramString);
  }

  // ERROR //
  public void statisticsEvent(View paramView, int paramInt, String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnonnull +6 -> 9
    //   6: aload_0
    //   7: monitorexit
    //   8: return
    //   9: aconst_null
    //   10: astore 4
    //   12: aload_1
    //   13: invokevirtual 57	java/lang/Object:getClass	()Ljava/lang/Class;
    //   16: ldc_w 501
    //   19: iconst_0
    //   20: anewarray 59	java/lang/Class
    //   23: invokevirtual 462	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   26: aload_1
    //   27: iconst_0
    //   28: anewarray 4	java/lang/Object
    //   31: invokevirtual 468	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   34: checkcast 107	com/dianping/widget/view/GAUserInfo
    //   37: invokevirtual 536	com/dianping/widget/view/GAUserInfo:clone	()Ljava/lang/Object;
    //   40: checkcast 107	com/dianping/widget/view/GAUserInfo
    //   43: astore 5
    //   45: aload 5
    //   47: astore 4
    //   49: aload 4
    //   51: ifnonnull +46 -> 97
    //   54: ldc_w 538
    //   57: invokestatic 89	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   60: goto -54 -> 6
    //   63: astore_1
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_1
    //   67: athrow
    //   68: astore 5
    //   70: new 407	java/lang/StringBuilder
    //   73: dup
    //   74: invokespecial 408	java/lang/StringBuilder:<init>	()V
    //   77: ldc_w 506
    //   80: invokevirtual 412	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: aload 5
    //   85: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   88: invokevirtual 415	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   91: invokestatic 497	com/dianping/util/Log:d	(Ljava/lang/String;)V
    //   94: goto -45 -> 49
    //   97: iload_2
    //   98: ldc_w 445
    //   101: if_icmpne +55 -> 156
    //   104: aload 4
    //   106: getfield 121	com/dianping/widget/view/GAUserInfo:index	Ljava/lang/Integer;
    //   109: ifnull +23 -> 132
    //   112: ldc_w 445
    //   115: aload 4
    //   117: getfield 121	com/dianping/widget/view/GAUserInfo:index	Ljava/lang/Integer;
    //   120: invokevirtual 504	java/lang/Integer:intValue	()I
    //   123: if_icmpne +9 -> 132
    //   126: aload 4
    //   128: aconst_null
    //   129: putfield 121	com/dianping/widget/view/GAUserInfo:index	Ljava/lang/Integer;
    //   132: aload_0
    //   133: aload_1
    //   134: invokevirtual 540	com/dianping/widget/view/GAHelper:getElementIdByView	(Landroid/view/View;)Ljava/lang/String;
    //   137: astore 5
    //   139: aload_0
    //   140: aload_1
    //   141: invokevirtual 543	android/view/View:getContext	()Landroid/content/Context;
    //   144: aload 5
    //   146: aload_3
    //   147: aload 4
    //   149: iconst_0
    //   150: invokespecial 125	com/dianping/widget/view/GAHelper:uploadGA	(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Lcom/dianping/widget/view/GAUserInfo;Z)V
    //   153: goto -147 -> 6
    //   156: aload 4
    //   158: iload_2
    //   159: invokestatic 117	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   162: putfield 121	com/dianping/widget/view/GAUserInfo:index	Ljava/lang/Integer;
    //   165: goto -33 -> 132
    //
    // Exception table:
    //   from	to	target	type
    //   12	45	63	finally
    //   54	60	63	finally
    //   70	94	63	finally
    //   104	132	63	finally
    //   132	153	63	finally
    //   156	165	63	finally
    //   12	45	68	java/lang/Exception
  }

  public void statisticsEvent(View paramView, String paramString)
  {
    statisticsEvent(paramView, 2147483647, paramString);
  }

  public String switchNewGADomain(String paramString)
  {
    String str;
    if (TextUtils.isEmpty(paramString))
    {
      str = null;
      return str;
    }
    if (paramString.startsWith("http://"));
    while (true)
    {
      str = paramString;
      if (paramString.endsWith("/"))
        break;
      return paramString + "/";
      paramString = "http://" + paramString;
    }
  }

  private static class GAHelperInner
  {
    static final GAHelper instance = new GAHelper(null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.view.GAHelper
 * JD-Core Version:    0.6.0
 */