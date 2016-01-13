package com.dianping.main.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import com.dianping.app.CityConfig;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.util.BluetoothHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.TextUtils;
import com.dianping.util.network.NetworkUtils;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.json.JSONArray;

public class SceneModeAgent extends HomeAgent
  implements RequestHandler<MApiRequest, MApiResponse>, HomeAgent.OnCellRefreshListener
{
  private static final String CELL_NAME = "21SceneMode.";
  private static final String Tag = "SceneModeAgent";
  public static int adapterTypeCount = 2;
  private SceneModeAdapter adapter;
  private final DPObject emptyObj = new DPObject();
  private String latitude;
  LocationListener locationListener;
  private String longitude;
  private MApiRequest mHomeSceneModeRequest;
  private BroadcastReceiver mScanBleReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.SCAN_BLE_FINISH".equals(paramIntent.getAction()))
      {
        Log.i("SceneModeAgent", "mScanBleReceiver onReceive");
        SceneModeAgent.this.sendHomeSceneModeRequest();
      }
    }
  };
  private Object responseError = null;
  private DPObject responseResult = null;

  public SceneModeAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void checkIsSendRequest()
  {
    if ((this.responseError == null) && (this.responseResult == null) && (this.mHomeSceneModeRequest == null))
    {
      if ((!BluetoothHelper.hasBleFeature(getContext())) || ((BluetoothHelper.hasBleFeature(getContext())) && (!BluetoothHelper.isScaning())))
      {
        Log.i("SceneModeAgent", "checkIsSendRequest,sendHomeSceneModeRequest");
        sendHomeSceneModeRequest();
      }
    }
    else
      return;
    Log.i("SceneModeAgent", "checkIsSendRequest,waiting scan bluetooth data,return");
  }

  private void stopHomeSceneModeRequest()
  {
    Log.i("SceneModeAgent", "stopHomeSceneModeRequest");
    if (this.mHomeSceneModeRequest != null)
    {
      mapiService().abort(this.mHomeSceneModeRequest, this, true);
      this.mHomeSceneModeRequest = null;
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    Log.i("SceneModeAgent", "onAgentChanged,responseError=" + this.responseError + ",responseResult=" + this.responseResult + ",mHomeSceneModeRequest=" + this.mHomeSceneModeRequest);
    checkIsSendRequest();
    SceneModeAdapter localSceneModeAdapter;
    if (this.responseResult != null)
    {
      localSceneModeAdapter = this.adapter;
      if (!this.responseResult.equals(this.emptyObj))
        break label107;
    }
    label107: for (paramBundle = null; ; paramBundle = this.responseResult)
    {
      localSceneModeAdapter.setContent(paramBundle);
      this.adapter.notifyMergeItemRangeChanged();
      this.responseResult = null;
      return;
    }
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    super.onCitySwitched(paramCity1, paramCity2);
    Log.i("SceneModeAgent", "onCitySwitched");
    sendHomeSceneModeRequest();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Log.i("SceneModeAgent", "onCreate");
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.SCAN_BLE_FINISH");
    getContext().registerReceiver(this.mScanBleReceiver, paramBundle);
    this.locationListener = new LocationListener()
    {
      public void onLocationChanged(LocationService paramLocationService)
      {
        Log.i("SceneModeAgent", "onLocationChanged");
        Object localObject = null;
        try
        {
          paramLocationService = (Location)paramLocationService.location().decodeToObject(Location.DECODER);
          if (paramLocationService == null);
          do
            return;
          while ((Location.FMT.format(paramLocationService.latitude()).equals(SceneModeAgent.this.latitude)) && (Location.FMT.format(paramLocationService.longitude()).equals(SceneModeAgent.this.longitude)));
          SceneModeAgent.this.checkIsSendRequest();
          return;
        }
        catch (java.lang.Exception paramLocationService)
        {
          while (true)
            paramLocationService = localObject;
        }
      }
    };
    getFragment().locationService().addListener(this.locationListener);
    DPApplication.instance().cityConfig().addListener(this);
    this.adapter = new SceneModeAdapter(this);
    addCell("21SceneMode.", this.adapter);
  }

  public void onDestroy()
  {
    Log.i("SceneModeAgent", "onDestory");
    getContext().unregisterReceiver(this.mScanBleReceiver);
    DPApplication.instance().cityConfig().removeListener(this);
    if (this.locationListener != null)
      getFragment().locationService().removeListener(this.locationListener);
    stopHomeSceneModeRequest();
    this.adapter.closeResources();
    super.onDestroy();
  }

  public void onRefresh()
  {
    Log.i("SceneModeAgent", "onRefresh ");
    this.isRefresh = true;
    if (BluetoothHelper.hasBleFeature(getContext()))
    {
      BluetoothHelper.scanLeDevice(getContext());
      return;
    }
    sendHomeSceneModeRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mHomeSceneModeRequest)
    {
      Log.i("SceneModeAgent", "onRequestFailed " + paramMApiResponse.message().content());
      this.mHomeSceneModeRequest = null;
      this.responseError = paramMApiResponse.error();
      if (this.responseError == null)
        this.responseError = new Object();
      dispatchAgentChanged(false);
      onRefreshComplete();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mHomeSceneModeRequest)
    {
      Log.i("SceneModeAgent", "onRequestFinish");
      this.responseResult = null;
      this.mHomeSceneModeRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.responseResult = ((DPObject)paramMApiResponse.result());
        if (this.responseResult != null)
        {
          paramMApiRequest = this.responseResult.getArray("List");
          paramMApiResponse = this.responseResult.getArray("HeadlineList");
          if ((paramMApiRequest == null) || (paramMApiRequest.length <= 0) || (paramMApiRequest[0] == null))
            break label131;
        }
      }
    }
    for (this.responseResult = paramMApiRequest[0]; ; this.responseResult = this.emptyObj)
      label131: 
      do
      {
        if (this.responseResult == null)
          this.responseError = new DPObject();
        dispatchAgentChanged(false);
        onRefreshComplete();
        return;
      }
      while ((paramMApiResponse != null) && (paramMApiResponse.length >= 1));
  }

  void sendHomeSceneModeRequest()
  {
    if (this.mHomeSceneModeRequest != null)
      Log.i("SceneModeAgent", "sendHomeSceneModeRequest,mHomeSceneModeRequest != null,return");
    Object localObject1;
    do
    {
      return;
      this.responseError = null;
      this.responseResult = null;
      double d2 = 0.0D;
      double d1 = 0.0D;
      localObject1 = location();
      int j = 0;
      i = 0;
      if (localObject1 != null)
      {
        double d3 = ((Location)localObject1).latitude();
        double d4 = ((Location)localObject1).longitude();
        if (location().city() != null)
          i = ((Location)localObject1).city().id();
        d1 = d3;
        d2 = d4;
        j = i;
        if (d3 != 0.0D)
        {
          d1 = d3;
          d2 = d4;
          j = i;
          if (d4 != 0.0D)
          {
            d1 = d3;
            d2 = d4;
            j = i;
            if (d3 != (-1.0D / 0.0D))
            {
              d1 = d3;
              d2 = d4;
              j = i;
              if (d3 != (1.0D / 0.0D))
              {
                d1 = d3;
                d2 = d4;
                j = i;
                if (d4 != (-1.0D / 0.0D))
                {
                  d1 = d3;
                  d2 = d4;
                  j = i;
                  if (d4 != (1.0D / 0.0D))
                  {
                    this.latitude = (Location.FMT.format(d3) + "");
                    this.longitude = (Location.FMT.format(d4) + "");
                    j = i;
                    d2 = d4;
                    d1 = d3;
                  }
                }
              }
            }
          }
        }
      }
      if ((d1 == 0.0D) || (d2 == 0.0D))
      {
        Log.i("SceneModeAgent", "lat =0,lng=0,return");
        return;
      }
      if (j != cityId())
      {
        this.adapter.setContent(null);
        this.adapter.notifyMergeItemRangeChanged();
        return;
      }
      localObject1 = new ArrayList();
      ((ArrayList)localObject1).add("longitude");
      ((ArrayList)localObject1).add("" + this.longitude);
      ((ArrayList)localObject1).add("latitude");
      ((ArrayList)localObject1).add("" + this.latitude);
      ((ArrayList)localObject1).add("cityid");
      ((ArrayList)localObject1).add("" + cityId());
      localObject2 = NetworkUtils.wifiScanBySceneMode2JsonArray(getContext());
      if ((localObject2 != null) && (((JSONArray)localObject2).length() > 0))
      {
        ((ArrayList)localObject1).add("wifi");
        ((ArrayList)localObject1).add(((JSONArray)localObject2).toString());
      }
      if (!BluetoothHelper.hasBleFeature(getContext()))
        continue;
      localObject2 = BluetoothHelper.getIbeaconList();
      if (TextUtils.isEmpty((CharSequence)localObject2))
        continue;
      ((ArrayList)localObject1).add("beacon");
      ((ArrayList)localObject1).add(localObject2);
    }
    while (((ArrayList)localObject1).size() == 0);
    Object localObject2 = new String[((ArrayList)localObject1).size()];
    int i = 0;
    while (i < localObject2.length)
    {
      localObject2[i] = ((String)((ArrayList)localObject1).get(i));
      i += 1;
    }
    this.mHomeSceneModeRequest = getFragment().mapiPost(this, "http://m.api.dianping.com/intelliindex.bin", localObject2);
    getFragment().mapiService().exec(this.mHomeSceneModeRequest, this);
    Log.i("SceneModeAgent", "sendHomeSceneModeRequest url = " + "http://m.api.dianping.com/intelliindex.bin" + ",paramlist=" + ((ArrayList)localObject1).toString());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.SceneModeAgent
 * JD-Core Version:    0.6.0
 */