package com.dianping.app;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.configservice.ConfigService;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.image.ImageService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.CrashReportHelper;
import com.dianping.util.DPUrl;
import com.dianping.util.Log;
import com.dianping.util.mapi.MApiRequestManager;
import java.util.List;
import org.apache.http.NameValuePair;

public class DPFragment extends Fragment
  implements LocationListener
{
  private static final String TAG = DPFragment.class.getSimpleName();
  private AccountService accountService;
  private CacheService cacheService;
  private ConfigService configService;
  private HttpService httpService;
  private ImageService imageService;
  private boolean isRequestingGps;
  private boolean isRequestingGpsDialogShown;
  private LocationService locationService;
  private MApiRequestManager mMApiRequestManager;
  private MApiService mapiService;
  private StatisticsService statisticsService;

  public AccountService accountService()
  {
    if (this.accountService == null)
      this.accountService = ((AccountService)getService("account"));
    return this.accountService;
  }

  public CacheService cacheService()
  {
    if (this.cacheService == null)
      this.cacheService = ((CacheService)getService("mapi_cache"));
    return this.cacheService;
  }

  public City city()
  {
    return DPApplication.instance().cityConfig().currentCity();
  }

  public CityConfig cityConfig()
  {
    return DPApplication.instance().cityConfig();
  }

  public ConfigService configService()
  {
    if (this.configService == null)
      this.configService = ((ConfigService)getService("config"));
    return this.configService;
  }

  public boolean getBooleanParam(String paramString)
  {
    return getBooleanParam(paramString, false);
  }

  public boolean getBooleanParam(String paramString, boolean paramBoolean)
  {
    if ((getArguments() != null) && (getArguments().containsKey(paramString)))
      return getArguments().getBoolean(paramString);
    return ((DPActivity)getActivity()).getBooleanParam(paramString, paramBoolean);
  }

  public byte getByteParam(String paramString)
  {
    return getByteParam(paramString, 0);
  }

  public byte getByteParam(String paramString, byte paramByte)
  {
    if ((getArguments() != null) && (getArguments().containsKey(paramString)))
      return getArguments().getByte(paramString);
    return ((DPActivity)getActivity()).getByteParam(paramString, paramByte);
  }

  public char getCharParam(String paramString)
  {
    return getCharParam(paramString, '\000');
  }

  public char getCharParam(String paramString, char paramChar)
  {
    if ((getArguments() != null) && (getArguments().containsKey(paramString)))
      return getArguments().getChar(paramString);
    return ((DPActivity)getActivity()).getCharParam(paramString, paramChar);
  }

  public double getDoubleParam(String paramString)
  {
    return getDoubleParam(paramString, 0.0D);
  }

  public double getDoubleParam(String paramString, double paramDouble)
  {
    if ((getArguments() != null) && (getArguments().containsKey(paramString)))
      return getArguments().getDouble(paramString);
    return ((DPActivity)getActivity()).getDoubleParam(paramString, paramDouble);
  }

  public float getFloatParam(String paramString)
  {
    return getFloatParam(paramString, 0.0F);
  }

  public float getFloatParam(String paramString, float paramFloat)
  {
    if ((getArguments() != null) && (getArguments().containsKey(paramString)))
      return getArguments().getFloat(paramString);
    return ((DPActivity)getActivity()).getFloatParam(paramString, paramFloat);
  }

  public int getIntParam(String paramString)
  {
    return getIntParam(paramString, 0);
  }

  public int getIntParam(String paramString, int paramInt)
  {
    if ((getArguments() != null) && (getArguments().containsKey(paramString)))
      return getArguments().getInt(paramString);
    return ((DPActivity)getActivity()).getIntParam(paramString, paramInt);
  }

  public long getLongParam(String paramString)
  {
    return getLongParam(paramString, 0L);
  }

  public long getLongParam(String paramString, long paramLong)
  {
    if ((getArguments() != null) && (getArguments().containsKey(paramString)))
      return getArguments().getLong(paramString);
    return ((DPActivity)getActivity()).getLongParam(paramString, paramLong);
  }

  public DPObject getObjectParam(String paramString)
  {
    if ((getArguments() != null) && (getArguments().containsKey(paramString)))
      return (DPObject)getArguments().getParcelable(paramString);
    return ((DPActivity)getActivity()).getObjectParam(paramString);
  }

  public Object getService(String paramString)
  {
    if ((getActivity() instanceof DPActivity))
      return ((DPActivity)getActivity()).getService(paramString);
    return DPApplication.instance().getService(paramString);
  }

  public short getShortParam(String paramString)
  {
    return getShortParam(paramString, 0);
  }

  public short getShortParam(String paramString, short paramShort)
  {
    if ((getArguments() != null) && (getArguments().containsKey(paramString)))
      return getArguments().getShort(paramString);
    return ((DPActivity)getActivity()).getShortParam(paramString, paramShort);
  }

  public String getStringParam(String paramString)
  {
    if ((getArguments() != null) && (getArguments().containsKey(paramString)))
      return getArguments().getString(paramString);
    return ((DPActivity)getActivity()).getStringParam(paramString);
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

  public Location location()
  {
    if (locationService().location() == null)
      return null;
    try
    {
      Location localLocation = (Location)locationService().location().decodeToObject(Location.DECODER);
      return localLocation;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public boolean locationCare()
  {
    return false;
  }

  public LocationService locationService()
  {
    if (this.locationService == null)
      this.locationService = ((LocationService)getService("location"));
    return this.locationService;
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

  public void onDestroyView()
  {
    if (this.mMApiRequestManager != null)
      this.mMApiRequestManager.clearAll();
    super.onDestroyView();
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    if (!locationCare());
  }

  public void onPause()
  {
    super.onPause();
    locationService().removeListener(this);
  }

  protected void onRequestGpsCanceled()
  {
  }

  public void onResume()
  {
    super.onResume();
    locationService().addListener(this);
    if (location() != null)
      onLocationChanged(locationService());
    if (locationCare())
      startLocate();
    if (this.isRequestingGps)
    {
      this.isRequestingGps = false;
      startLocate();
    }
  }

  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter)
  {
    return getActivity().registerReceiver(paramBroadcastReceiver, paramIntentFilter);
  }

  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, String paramString, Handler paramHandler)
  {
    return getActivity().registerReceiver(paramBroadcastReceiver, paramIntentFilter, paramString, paramHandler);
  }

  public void requestGpsSwitchOn()
  {
    if (this.isRequestingGpsDialogShown)
      return;
    Object localObject = new AlertDialog.Builder(getActivity());
    ((AlertDialog.Builder)localObject).setTitle("错误");
    ((AlertDialog.Builder)localObject).setMessage("无法获得您的位置信息，必须打开设置中的位置选项。\n现在就进行设置?");
    ((AlertDialog.Builder)localObject).setPositiveButton(17039379, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        try
        {
          DPFragment.this.startActivity(paramDialogInterface);
          DPFragment.access$002(DPFragment.this, true);
          return;
        }
        catch (android.content.ActivityNotFoundException paramDialogInterface)
        {
        }
      }
    });
    ((AlertDialog.Builder)localObject).setNegativeButton(17039360, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    });
    localObject = ((AlertDialog.Builder)localObject).create();
    ((AlertDialog)localObject).setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        DPFragment.this.onRequestGpsCanceled();
      }
    });
    ((AlertDialog)localObject).setOnDismissListener(new DialogInterface.OnDismissListener()
    {
      public void onDismiss(DialogInterface paramDialogInterface)
      {
        DPFragment.access$102(DPFragment.this, false);
      }
    });
    this.isRequestingGpsDialogShown = true;
    ((AlertDialog)localObject).show();
  }

  public void startActivity(Intent paramIntent)
  {
    if (isAdded())
    {
      if (paramIntent == null)
        return;
      CrashReportHelper.putUrlSchema(paramIntent.getDataString());
      try
      {
        super.startActivity(paramIntent);
        return;
      }
      catch (Exception paramIntent)
      {
        return;
      }
    }
    Log.e(TAG, "startActivity java.lang.IllegalStateException: Fragment xxx not attached to Activity ");
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
    if (isAdded())
    {
      if (paramIntent == null)
        return;
      CrashReportHelper.putUrlSchema(paramIntent.getDataString());
      super.startActivityForResult(paramIntent, paramInt);
      return;
    }
    Log.e(TAG, "startActivity java.lang.IllegalStateException: Fragment xxx not attached to Activity ");
  }

  public void startActivityForResult(DPUrl paramDPUrl, int paramInt)
  {
    startActivityForResult(paramDPUrl.getIntent(), paramInt);
  }

  public void startActivityForResult(String paramString, int paramInt)
  {
    startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse(paramString)), paramInt);
  }

  public void startLocate()
  {
    if ((locationService().status() <= 0) && (!locationService().start()));
  }

  public void statisticsEvent(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    statisticsEvent(paramString1, paramString2, paramString3, paramInt, null);
  }

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

  public void stopLocate()
  {
  }

  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver)
  {
    getActivity().unregisterReceiver(paramBroadcastReceiver);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.app.DPFragment
 * JD-Core Version:    0.6.0
 */