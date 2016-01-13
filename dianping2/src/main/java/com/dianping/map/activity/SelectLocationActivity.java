package com.dianping.map.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.app.CityConfig;
import com.dianping.base.app.NovaActivity;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.GPSCoordinate;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.DPBasicItem;

public class SelectLocationActivity extends NovaActivity
  implements LocationListener
{
  private static final int REQ_CODE_SELECT_CITY = 100;
  private static final int REQ_CODE_SELECT_ROAD = 101;
  private static final String SELECT_LOCATION = "com.dianping.SELECT_LOCATION";
  private boolean mHasChangeCity;
  boolean mHasCommit;
  double mLat;
  double mLng;
  private City mOldCity;
  DPBasicItem mSelectCity;
  DPBasicItem mSelectRoad;

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (-1 == paramInt2)
    {
      if (paramInt1 != 100)
        break label78;
      paramIntent = (City)paramIntent.getParcelableExtra("city");
      if (paramIntent.id() != this.mOldCity.id())
        this.mHasChangeCity = true;
      this.mSelectCity.setTitle(paramIntent.name());
      this.mSelectRoad.setTitle("选择您所处的位置");
      this.mLat = 0.0D;
      this.mLng = 0.0D;
    }
    label78: 
    do
      return;
    while (paramInt1 != 101);
    this.mSelectRoad.setTitle(paramIntent.getStringExtra("address"));
    this.mSelectCity.setTitle(city().name());
    this.mLat = paramIntent.getDoubleExtra("lat", 0.0D);
    this.mLng = paramIntent.getDoubleExtra("lng", 0.0D);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.selectlocation);
    this.mOldCity = city();
    this.mSelectCity = ((DPBasicItem)findViewById(R.id.select_city));
    if ((city() != null) && (city().name() != null))
      this.mSelectCity.setTitle(city().name());
    this.mSelectCity.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SelectLocationActivity.this.statisticsEvent("shoplist5", "shoplist5_relocate_nearby_city", "", 0);
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://switchcity"));
        SelectLocationActivity.this.startActivityForResult(paramView, 100);
      }
    });
    this.mSelectRoad = ((DPBasicItem)findViewById(R.id.select_road));
    this.mSelectRoad.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SelectLocationActivity.this.statisticsEvent("shoplist5", "shoplist5_relocate_nearby_address", "", 0);
        if ("城市".equals(SelectLocationActivity.this.mSelectCity.getTitle()))
        {
          SelectLocationActivity.this.showToast("请先选择城市");
          return;
        }
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://customlocationlist"));
        SelectLocationActivity.this.startActivityForResult(paramView, 101);
      }
    });
    findViewById(R.id.commit).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SelectLocationActivity.this.statisticsEvent("shoplist5", "shoplist5_relocate_nearby_submit", "", 0);
        if ("选择您所处的位置".equals(SelectLocationActivity.this.mSelectRoad.getTitle()))
        {
          SelectLocationActivity.this.showToast("请选择路名");
          return;
        }
        SelectLocationActivity.this.showProgressDialog("正在设置您选择的位置...");
        SelectLocationActivity.this.mHasCommit = true;
        SelectLocationActivity.this.locationService().selectCoordinate(65281, new GPSCoordinate(SelectLocationActivity.this.mLat, SelectLocationActivity.this.mLng, 0, 0L, "custom"));
      }
    });
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if ((!this.mHasCommit) && (this.mHasChangeCity))
      cityConfig().switchCity(this.mOldCity);
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    if (paramLocationService.status() == 2)
      if ((location() != null) && (this.mHasCommit))
      {
        dismissDialog();
        sendBroadcast(new Intent("com.dianping.SELECT_LOCATION"));
        setResult(-1);
        finish();
      }
    do
      return;
    while ((paramLocationService.status() != -1) || (!this.mHasCommit));
    dismissDialog();
    showToast("请求失败，请重试");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.activity.SelectLocationActivity
 * JD-Core Version:    0.6.0
 */