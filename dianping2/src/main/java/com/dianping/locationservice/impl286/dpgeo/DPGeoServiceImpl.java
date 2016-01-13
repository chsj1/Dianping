package com.dianping.locationservice.impl286.dpgeo;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiFormInputStream;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.impl286.geo.GeoListener;
import com.dianping.locationservice.impl286.geo.GeoService;
import com.dianping.locationservice.impl286.geo.GeoServiceImpl;
import com.dianping.locationservice.impl286.util.CoordUtil;
import com.dianping.locationservice.impl286.util.StatUtil;
import com.dianping.model.City;
import com.dianping.model.GPSCoordinate;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.message.BasicNameValuePair;

public class DPGeoServiceImpl extends Handler
  implements DPGeoService, GeoListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String DP_LOC_URL = "http://l.api.dianping.com/locate.bin";
  private static final String DP_RGC_URL = "http://l.api.dianping.com/rgc.bin";
  public static final int IMPL = 286;
  private static final int MAX_RETRY_COUNT = 1;
  private static final int MSG_DP_LOC_FAIL = 1002;
  private static final int MSG_DP_LOC_SUCCESS = 1001;
  private static final int MSG_DP_RGC_FAIL = 1004;
  private static final int MSG_DP_RGC_SUCCESS = 1003;
  private static DPGeoServiceImpl sInstance;
  private long mDPLocElapse;
  private final ConcurrentHashMap<String, String> mGeoParams = new ConcurrentHashMap();
  private GeoService mGeoService = GeoServiceImpl.getInstance(DPApplication.instance());
  private DPObject mLastLocObj;
  private String mLastSessionId = "";
  private List<DPGeoListener> mListenerList = new ArrayList();
  private DPObject mLocObj;
  private MApiRequest mLocReq;
  private int mLocRetryCount;
  private MApiService mMApiService;
  private MApiRequest mRgcReq;
  private StatisticsService mStatService;

  private DPGeoServiceImpl()
  {
    this.mGeoService.addListener(this);
    this.mMApiService = ((MApiService)DPApplication.instance().getService("mapi"));
    this.mStatService = ((StatisticsService)DPApplication.instance().getService("statistics"));
  }

  public static DPGeoServiceImpl getInstance()
  {
    monitorenter;
    try
    {
      if (sInstance == null)
        sInstance = new DPGeoServiceImpl();
      DPGeoServiceImpl localDPGeoServiceImpl = sInstance;
      return localDPGeoServiceImpl;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private void handleLocFail()
  {
    Object localObject = null;
    try
    {
      String str = Environment.sessionId();
      localObject = str;
      if ((localObject != null) && (this.mLastSessionId != null) && (localObject.equals(this.mLastSessionId)))
        this.mLocObj = this.mLastLocObj;
      if (this.mLocObj != null)
      {
        sendEmptyMessage(1001);
        return;
      }
    }
    catch (Throwable localThrowable)
    {
      while (true)
        Log.e(localThrowable.toString());
      sendEmptyMessage(1002);
    }
  }

  private void notifyFail()
  {
    Iterator localIterator = this.mListenerList.iterator();
    while (localIterator.hasNext())
      ((DPGeoListener)localIterator.next()).onLocateFail();
  }

  private void notifyFinish()
  {
    Iterator localIterator = this.mListenerList.iterator();
    while (localIterator.hasNext())
      ((DPGeoListener)localIterator.next()).onLocateFinish(this.mLocObj);
  }

  private void onLocFail(String paramString)
  {
    Log.e("dp locate fail, reason: " + paramString);
    this.mDPLocElapse = (System.currentTimeMillis() - this.mDPLocElapse);
    StatUtil.uploadElapse(this.mStatService, this.mDPLocElapse);
    this.mLocRetryCount += 1;
    if (this.mLocRetryCount <= 1)
    {
      Log.i("dp locate retry " + this.mLocRetryCount + "/" + 1);
      sendLocateReq();
      return;
    }
    this.mLocReq = null;
    this.mLocRetryCount = 0;
    handleLocFail();
  }

  private void onLocFinish(DPObject paramDPObject)
  {
    this.mDPLocElapse = (System.currentTimeMillis() - this.mDPLocElapse);
    StatUtil.uploadElapse(this.mStatService, this.mDPLocElapse);
    this.mLocReq = null;
    this.mLocRetryCount = 0;
    if (paramDPObject == null)
    {
      Log.w("no locate result");
      handleLocFail();
      return;
    }
    paramDPObject = paramDPObject.getArray("Locations");
    if ((paramDPObject == null) || (paramDPObject.length == 0))
    {
      Log.w("no locate Locations result");
      handleLocFail();
      return;
    }
    this.mLocObj = paramDPObject[0];
    this.mLastLocObj = this.mLocObj;
    try
    {
      this.mLastSessionId = Environment.sessionId();
      sendEmptyMessage(1001);
      return;
    }
    catch (Throwable paramDPObject)
    {
      while (true)
        Log.e(paramDPObject.toString());
    }
  }

  private void onRgcFail(String paramString)
  {
    Log.w("dp rgc fail, reason: " + paramString);
    this.mRgcReq = null;
    sendEmptyMessage(1004);
  }

  private void onRgcFinish(DPObject paramDPObject)
  {
    this.mRgcReq = null;
    if (paramDPObject == null)
    {
      Log.w("no rgc result");
      sendEmptyMessage(1004);
      return;
    }
    if (TextUtils.isEmpty(paramDPObject.getString("Address")))
    {
      Log.w("no rgc addr result");
      sendEmptyMessage(1004);
      return;
    }
    this.mLocObj = paramDPObject;
    this.mLastLocObj = this.mLocObj;
    sendEmptyMessage(1003);
  }

  public boolean addListener(DPGeoListener paramDPGeoListener)
  {
    return this.mListenerList.add(paramDPGeoListener);
  }

  public void doRgc(double paramDouble1, double paramDouble2, int paramInt1, String paramString, int paramInt2)
  {
    int i = 0;
    try
    {
      int j = DPApplication.instance().city().id();
      i = j;
      sendRgcReq(i, paramDouble1, paramDouble2, paramInt1, paramString, paramInt2);
      return;
    }
    catch (Throwable localThrowable)
    {
      while (true)
        Log.e(localThrowable.toString());
    }
  }

  public void handleMessage(Message paramMessage)
  {
    switch (paramMessage.what)
    {
    default:
      return;
    case 1001:
      sendRgcReq(this.mLocObj);
      notifyFinish();
      return;
    case 1002:
      notifyFail();
      return;
    case 1003:
      notifyFinish();
      return;
    case 1004:
    }
    notifyFinish();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.error().toString();
    if (paramMApiRequest == this.mLocReq)
      onLocFail(paramMApiResponse);
    do
      return;
    while (paramMApiRequest != this.mRgcReq);
    onRgcFail(paramMApiResponse);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    Object localObject = paramMApiResponse.result();
    paramMApiResponse = null;
    if ((localObject instanceof DPObject))
      paramMApiResponse = (DPObject)localObject;
    if (paramMApiRequest == this.mLocReq)
      onLocFinish(paramMApiResponse);
    do
      return;
    while (paramMApiRequest != this.mRgcReq);
    onRgcFinish(paramMApiResponse);
  }

  public void onRequestGeoParamsFinish(Map<String, String> paramMap)
  {
    this.mGeoParams.clear();
    this.mGeoParams.putAll(paramMap);
    sendLocateReq();
  }

  public boolean removeListener(DPGeoListener paramDPGeoListener)
  {
    return this.mListenerList.remove(paramDPGeoListener);
  }

  public void sendLocateReq()
  {
    if (this.mMApiService == null)
      return;
    if (this.mLocReq != null)
    {
      this.mMApiService.abort(this.mLocReq, this, true);
      this.mLocReq = null;
    }
    this.mDPLocElapse = System.currentTimeMillis();
    Uri.Builder localBuilder = Uri.parse("http://l.api.dianping.com/locate.bin").buildUpon();
    localBuilder.appendQueryParameter("action", "loc");
    localBuilder.appendQueryParameter("impl", String.valueOf(286));
    ArrayList localArrayList = new ArrayList();
    try
    {
      localArrayList.add(new BasicNameValuePair("city", String.valueOf(DPApplication.instance().city().id())));
    }
    catch (Throwable localThrowable2)
    {
      try
      {
        if (Environment.isDebug());
        String str2;
        for (String str1 = "1"; ; str2 = "0")
        {
          localBuilder.appendQueryParameter("debug", str1);
          localArrayList.add(new BasicNameValuePair("session", Environment.sessionId()));
          localArrayList.add(new BasicNameValuePair("elapse", String.valueOf(System.currentTimeMillis())));
          localArrayList.add(new BasicNameValuePair("cellScanElapse", (String)this.mGeoParams.get("cellScanElapse")));
          localArrayList.add(new BasicNameValuePair("wifiScanElapse", (String)this.mGeoParams.get("wifiScanElapse")));
          localArrayList.add(new BasicNameValuePair("localLocElapse", (String)this.mGeoParams.get("localLocElapse")));
          localArrayList.add(new BasicNameValuePair("no_sim", (String)this.mGeoParams.get("noSimInfo")));
          localArrayList.add(new BasicNameValuePair("gsm", (String)this.mGeoParams.get("gsmInfo")));
          localArrayList.add(new BasicNameValuePair("cdma", (String)this.mGeoParams.get("cdmaInfo")));
          localArrayList.add(new BasicNameValuePair("lte", (String)this.mGeoParams.get("lteInfo")));
          localArrayList.add(new BasicNameValuePair("wcdma", (String)this.mGeoParams.get("wcdmaInfo")));
          localArrayList.add(new BasicNameValuePair("wifi", (String)this.mGeoParams.get("wifiInfo")));
          localArrayList.add(new BasicNameValuePair("coord_gps", (String)this.mGeoParams.get("coordGps")));
          localArrayList.add(new BasicNameValuePair("coord_network", (String)this.mGeoParams.get("coordNetwork")));
          localArrayList.add(new BasicNameValuePair("coord_bwifi", (String)this.mGeoParams.get("coordBMap")));
          Log.i("noSim: " + (String)this.mGeoParams.get("noSimInfo"));
          Log.i("gsm: " + (String)this.mGeoParams.get("gsmInfo"));
          Log.i("cdma: " + (String)this.mGeoParams.get("cdmaInfo"));
          Log.i("lte: " + (String)this.mGeoParams.get("lteInfo"));
          Log.i("wcdma: " + (String)this.mGeoParams.get("wcdmaInfo"));
          Log.i("coordGps: " + (String)this.mGeoParams.get("coordGps"));
          Log.i("coordNetwork: " + (String)this.mGeoParams.get("coordNetwork"));
          Log.i("coordBMap: " + (String)this.mGeoParams.get("coordBMap"));
          Log.i("cellScanElapse: " + (String)this.mGeoParams.get("cellScanElapse"));
          Log.i("wifiScanElapse: " + (String)this.mGeoParams.get("wifiScanElapse"));
          Log.i("localLocElapse: " + (String)this.mGeoParams.get("localLocElapse"));
          this.mLocReq = new BasicMApiRequest(localBuilder.build().toString(), "POST", new MApiFormInputStream(localArrayList), CacheType.DISABLED, false, null);
          try
          {
            this.mLocReq.input().reset();
            this.mMApiService.exec(this.mLocReq, this);
            return;
          }
          catch (IOException localIOException)
          {
            Log.e(localIOException.toString());
            return;
          }
          localThrowable1 = localThrowable1;
          Log.e(localThrowable1.toString());
          break;
        }
      }
      catch (Throwable localThrowable2)
      {
        while (true)
          Log.e(localThrowable2.toString());
      }
    }
  }

  public void sendRgcReq(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, String paramString, int paramInt3)
  {
    if (this.mMApiService == null)
      return;
    if (this.mRgcReq != null)
    {
      this.mMApiService.abort(this.mRgcReq, this, true);
      this.mRgcReq = null;
    }
    Uri.Builder localBuilder = Uri.parse("http://l.api.dianping.com/rgc.bin").buildUpon();
    localBuilder.appendQueryParameter("impl", String.valueOf(286));
    if (paramInt1 != 0)
      localBuilder.appendQueryParameter("city", String.valueOf(paramInt1));
    try
    {
      if (Environment.isDebug());
      for (String str = "1"; ; str = "0")
      {
        localBuilder.appendQueryParameter("debug", str);
        localBuilder.appendQueryParameter("lat", String.valueOf(paramDouble1));
        localBuilder.appendQueryParameter("lng", String.valueOf(paramDouble2));
        localBuilder.appendQueryParameter("acc", String.valueOf(paramInt2));
        localBuilder.appendQueryParameter("source", paramString);
        if (paramInt3 >= 0)
          localBuilder.appendQueryParameter("maptype", String.valueOf(paramInt3));
        this.mRgcReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
        this.mMApiService.exec(this.mRgcReq, this);
        return;
      }
    }
    catch (Throwable localThrowable)
    {
      while (true)
        Log.e(localThrowable.toString());
    }
  }

  public void sendRgcReq(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    GPSCoordinate localGPSCoordinate = CoordUtil.coordinate(paramDPObject);
    int i = 0;
    paramDPObject = paramDPObject.getObject("City");
    if (paramDPObject != null)
      i = paramDPObject.getInt("ID");
    while (true)
    {
      sendRgcReq(i, localGPSCoordinate.latitude(), localGPSCoordinate.longitude(), localGPSCoordinate.accuracy(), localGPSCoordinate.source(), -1);
      return;
      try
      {
        int j = DPApplication.instance().city().id();
        i = j;
      }
      catch (Throwable paramDPObject)
      {
        Log.e(paramDPObject.toString());
      }
    }
  }

  public void start()
  {
    this.mGeoService.requestGeoParams();
  }

  public void stop()
  {
    if ((this.mLocReq != null) && (this.mMApiService != null))
    {
      this.mMApiService.abort(this.mLocReq, this, true);
      this.mLocReq = null;
    }
    if ((this.mRgcReq != null) && (this.mMApiService != null))
    {
      this.mMApiService.abort(this.mRgcReq, this, true);
      this.mRgcReq = null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.dpgeo.DPGeoServiceImpl
 * JD-Core Version:    0.6.0
 */