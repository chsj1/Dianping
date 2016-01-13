package com.dianping.main.city;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.dianping.app.CityConfig;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.content.CityUtils;
import com.dianping.content.TuanCityUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.util.DPUrl;

public class CityListSwitchActivity extends CityListPickerActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final int MSG_FINISH_ACTIVITY = 0;
  private int area = 0;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 0)
      {
        CityListSwitchActivity.this.dismissDialog();
        CityListSwitchActivity.this.finish();
      }
    }
  };
  private String mIntentDataString;
  private int source = 0;

  private void recordSelectedCity(String paramString, City paramCity)
  {
    StringBuilder localStringBuilder = new StringBuilder("");
    Object localObject = this.mPref.getString(paramString, "");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      localObject = ((String)localObject).split(",");
      int i = 0;
      while (i < localObject.length)
      {
        if (!String.valueOf(paramCity.id()).equalsIgnoreCase(localObject[i]))
        {
          if (!"".endsWith(localStringBuilder.toString()))
            localStringBuilder.append(",");
          localStringBuilder.append(localObject[i]);
        }
        i += 1;
      }
    }
    if (!"".endsWith(localStringBuilder.toString()))
      localStringBuilder.append(",");
    localStringBuilder.append("" + paramCity.id());
    this.mPref.edit().putString(paramString, localStringBuilder.toString()).commit();
  }

  public void doClickCity()
  {
    if (TextUtils.isEmpty(((City)this.selectedItem).url()))
    {
      doSwitch((City)this.selectedItem);
      return;
    }
    startActivity(new DPUrl(((City)this.selectedItem).url()).putParam("isneedcity", false));
  }

  protected void doSwitch(City paramCity)
  {
    if (paramCity == null)
      return;
    showProgressDialog("正在切换城市请稍候...");
    Object localObject = CityUtils.getCityById(paramCity.id());
    if (localObject != null)
      paramCity = (City)localObject;
    cityConfig().switchCity(paramCity);
    recordSelectedCity("select_all_city", paramCity);
    if ((paramCity.isForeign()) && (TextUtils.isEmpty(paramCity.url())))
      recordSelectedCity("select_foreign_city", paramCity);
    localObject = new Intent();
    ((Intent)localObject).putExtra("city", paramCity);
    setResult(-1, (Intent)localObject);
    if (!TextUtils.isEmpty(this.mIntentDataString))
      startActivityForResult(this.mIntentDataString, 101);
    this.mHandler.sendEmptyMessage(0);
  }

  protected City getCityById(int paramInt)
  {
    if (this.source == 1)
      return TuanCityUtils.getCityById(paramInt);
    return super.getCityById(paramInt);
  }

  protected City[] getSortBy1stChar()
  {
    if (this.source == 1)
      return TuanCityUtils.getSortBy1stChar();
    return super.getSortBy1stChar();
  }

  protected boolean isNeedCity()
  {
    return false;
  }

  public void onClick(View paramView)
  {
    this.selectedItem = paramView.getTag();
    if ((this.selectedItem instanceof City))
    {
      if (((City)this.selectedItem).id() == -1)
        this.mTabBar.setCurIndex(1);
    }
    else
      return;
    doClickCity();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.source = getIntParam("source", 0);
    this.area = getIntParam("area", 0);
    this.mIntentDataString = getIntent().getStringExtra("intent");
    if ((this.area == 0) || (this.area == 1))
      this.mTabBar.setCurIndex(this.area);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    this.selectedItem = this.listView.getItemAtPosition(paramInt);
    if ((this.selectedItem instanceof City))
      doClickCity();
    if ((this.selectedItem == GPS_CITY) && (this.gpsCity != null))
      if (this.gpsCity.id() <= 0)
        Toast.makeText(this, "正在精确定位，请稍候...", 0).show();
    do
    {
      return;
      doSwitch(this.gpsCity);
      return;
    }
    while ((this.selectedItem != GPS_CITY) || (locationService().status() != -1));
    paramAdapterView = (LocationManager)getSystemService("location");
    if ((paramAdapterView != null) && (!paramAdapterView.isProviderEnabled("gps")))
    {
      new AlertDialog.Builder(this).setTitle("定位服务未开启").setMessage("请在系统设置中开启定位服务").setPositiveButton("去设置", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          CityListSwitchActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        }
      }).setNegativeButton("暂不", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      }).show();
      return;
    }
    showShortToast("请检查你的定位权限或网络是否正常");
  }

  protected void setCities(DPObject[] paramArrayOfDPObject, byte[] paramArrayOfByte)
  {
    if (this.source == 1)
    {
      TuanCityUtils.setCities(paramArrayOfDPObject, paramArrayOfByte);
      if (getForeignCities().length == 0)
        this.mTabBar.setVisibility(4);
      return;
    }
    super.setCities(paramArrayOfDPObject, paramArrayOfByte);
  }

  protected void updateCityFromWeb(int paramInt, String paramString)
  {
    if (paramInt <= 0)
      return;
    doSwitch(CityUtils.getCityById(paramInt));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.city.CityListSwitchActivity
 * JD-Core Version:    0.6.0
 */