package com.dianping.map.basic;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ButtonSearchBar.ButtonSearchBarListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.map.utils.MapUtils;
import com.dianping.map.utils.ThirdpartyMapUtils;
import com.dianping.model.City;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class CustomLocationBasicActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final int REQUEST_CODE_GOOGLE_PLACES_SEARCH = 0;
  protected String address;
  private String addressStr;
  private String geoRegionName;
  private boolean mHasMoved;
  protected TextView mInfoTextView;
  private ProgressBar mProgressBar;
  private Marker mShopPosition;
  private MapFragment mapFragment;
  private int range;
  protected String rangeStr;
  private boolean readyToCommit = false;
  private DPObject region;
  private MApiRequest request;
  private String road;
  private ButtonSearchBar searchBar;
  private DPObject selCity;
  private Button submitBtn;

  private void startRequestAddress(LatLng paramLatLng)
  {
    if (this.request != null)
      mapiService().abort(this.request, this, true);
    if (paramLatLng == null)
      return;
    double d1 = paramLatLng.latitude;
    double d2 = paramLatLng.longitude;
    this.request = BasicMApiRequest.mapiGet("http://l.api.dianping.com/rgc.bin?lat=" + d1 + "&lng=" + d2 + "&maptype=1", CacheType.NORMAL);
    mapiService().exec(this.request, this);
    statisticsEvent("localsearch5", "localsearch5_drag", d1 + "," + d2 + this.range, 0, null);
  }

  protected void finishWithGPS()
  {
    Object localObject = this.mapFragment.getMapCenter();
    double d1 = ((LatLng)localObject).latitude;
    double d2 = ((LatLng)localObject).longitude;
    localObject = new Intent();
    ((Intent)localObject).putExtra("lat", d1);
    ((Intent)localObject).putExtra("lng", d2);
    ((Intent)localObject).putExtra("range", this.range);
    ((Intent)localObject).putExtra("address", this.address);
    ((Intent)localObject).putExtra("maptype", 1);
    ((Intent)localObject).putExtra("addressStr", this.addressStr);
    ((Intent)localObject).putExtra("mapversion", "0");
    if (this.region != null)
      ((Intent)localObject).putExtra("region", this.region);
    setResult(-1, (Intent)localObject);
    finish();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt2 == -1) && (paramInt1 == 0))
    {
      this.address = paramIntent.getStringExtra("title");
      this.addressStr = this.address;
      this.searchBar.setKeyword(this.address);
      paramIntent = new LatLng(this.mapFragment.stringLatLng(paramIntent.getStringExtra("lat")), this.mapFragment.stringLatLng(paramIntent.getStringExtra("lng")));
      this.mapFragment.updateSelectedMarker(paramIntent, this.address);
      ThirdpartyMapUtils.moveToLatLng(this.mapFragment.getBasicMap(), paramIntent, true);
      this.mHasMoved = true;
      this.mInfoTextView.setText(Html.fromHtml(this.address + " 周边 "));
    }
  }

  public void onBackPressed()
  {
    if (this.mHasMoved)
    {
      new AlertDialog.Builder(this).setTitle("提示").setMessage("尚未提交，确定放弃所选择的地点吗？").setPositiveButton("确认", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          CustomLocationBasicActivity.this.finish();
        }
      }).setNegativeButton("取消", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      }).show();
      return;
    }
    super.onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (MapUtils.isMapNOTSupported())
    {
      Toast.makeText(this, "该手机不支持腾讯地图", 1).show();
      finish();
    }
    setupView();
  }

  public void onDestroy()
  {
    if (this.request != null)
      mapiService().abort(this.request, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.readyToCommit = false;
    this.mProgressBar.setVisibility(8);
    this.mInfoTextView.setText("获取地址失败");
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request == paramMApiRequest)
    {
      if ((!(paramMApiResponse.result() instanceof DPObject)) || (!((DPObject)paramMApiResponse.result()).isClass("Location")))
        break label262;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      this.address = paramMApiRequest.getString("Address");
      this.selCity = paramMApiRequest.getObject("City");
      this.geoRegionName = paramMApiRequest.getString("GeoRegionName");
      this.road = paramMApiRequest.getString("Road");
      this.region = paramMApiRequest.getObject("Region");
      if ((TextUtils.isEmpty(this.geoRegionName)) || (TextUtils.isEmpty(this.road)))
        break label172;
    }
    label172: for (this.addressStr = (this.geoRegionName + this.road); this.readyToCommit; this.addressStr = null)
    {
      this.submitBtn.performClick();
      this.readyToCommit = false;
      return;
    }
    this.mProgressBar.setVisibility(8);
    if (!TextUtils.isEmpty(this.addressStr))
      this.mInfoTextView.setText(Html.fromHtml(this.addressStr));
    while (true)
    {
      this.request = null;
      return;
      if (!TextUtils.isEmpty(this.address))
      {
        this.mInfoTextView.setText(Html.fromHtml(this.address));
        continue;
      }
      this.mInfoTextView.setText(Html.fromHtml("暂时无法获得地址信息"));
      continue;
      label262: onRequestFailed(paramMApiRequest, paramMApiResponse);
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.rangeStr = paramBundle.getString("rangeStr");
    this.address = paramBundle.getString("address");
    this.range = paramBundle.getInt("range");
    this.selCity = ((DPObject)paramBundle.getParcelable("selCity"));
    this.geoRegionName = paramBundle.getString("geoRegionName");
    this.road = paramBundle.getString("road");
    this.region = ((DPObject)paramBundle.getParcelable("region"));
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("rangeStr", this.rangeStr);
    paramBundle.putString("address", this.address);
    paramBundle.putInt("range", this.range);
    paramBundle.putParcelable("selCity", this.selCity);
    paramBundle.putString("geoRegionName", this.geoRegionName);
    paramBundle.putString("road", this.road);
    paramBundle.putParcelable("region", this.region);
    super.onSaveInstanceState(paramBundle);
  }

  protected void setupView()
  {
    setContentView(R.layout.customloction_mapbar);
    this.mapFragment = new MapFragment();
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.setCustomAnimations(17432576, 17432577);
    localFragmentTransaction.replace(R.id.id_content, this.mapFragment);
    localFragmentTransaction.commit();
    this.searchBar = ((ButtonSearchBar)findViewById(R.id.button_search_bar));
    this.searchBar.setHint("输入你想查找的地点");
    this.searchBar.setButtonSearchBarListener(new ButtonSearchBar.ButtonSearchBarListener()
    {
      public void onSearchRequested()
      {
        Object localObject = CustomLocationBasicActivity.this.mapFragment.getMapCenter();
        double d1 = ((LatLng)localObject).latitude;
        double d2 = ((LatLng)localObject).longitude;
        localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://googleplacessearch?lat=" + d1 + "&lng=" + d2 + "&gaaction=localsearch5_keyword_map&mapType=" + 1));
        CustomLocationBasicActivity.this.startActivityForResult((Intent)localObject, 0);
      }
    });
    this.submitBtn = ((Button)findViewById(R.id.btn_submit));
    this.submitBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (CustomLocationBasicActivity.this.selCity == null)
        {
          CustomLocationBasicActivity.this.showToast("正在确认所选位置，请稍候...");
          CustomLocationBasicActivity.access$202(CustomLocationBasicActivity.this, true);
          return;
        }
        if (CustomLocationBasicActivity.this.selCity.getInt("ID") == CustomLocationBasicActivity.this.cityId())
        {
          CustomLocationBasicActivity.this.finishWithGPS();
          return;
        }
        new AlertDialog.Builder(CustomLocationBasicActivity.this).setTitle("提示").setMessage("您所选择的位置在" + CustomLocationBasicActivity.this.selCity.getString("Name") + "，是否切换到" + CustomLocationBasicActivity.this.selCity.getString("Name") + "？").setPositiveButton("切换", new CustomLocationBasicActivity.4.2(this)).setNegativeButton("取消", new CustomLocationBasicActivity.4.1(this)).show();
      }
    });
    this.mInfoTextView = ((TextView)findViewById(R.id.current_map_address));
    this.mProgressBar = ((ProgressBar)findViewById(R.id.progress));
  }

  public class MapFragment extends MapBasicFragment
  {
    public MapFragment()
    {
    }

    private void initFirstLocation()
    {
      double d1 = CustomLocationBasicActivity.this.getIntent().getDoubleExtra("latitude", -1.0D);
      double d2 = CustomLocationBasicActivity.this.getIntent().getDoubleExtra("longitude", -1.0D);
      if ((d1 != -1.0D) && (d2 != -1.0D));
      for (LatLng localLatLng = new LatLng(d1, d2); ; localLatLng = new LatLng(city().latitude(), city().longitude()))
      {
        ThirdpartyMapUtils.moveToLatLng(getBasicMap(), localLatLng, true);
        updateSelectedMarker(localLatLng, CustomLocationBasicActivity.this.getIntent().getStringExtra("name"));
        CustomLocationBasicActivity.this.startRequestAddress(localLatLng);
        return;
      }
    }

    protected LatLng gotoMyLocation()
    {
      updateMyLocationMarker();
      return super.gotoMyLocation();
    }

    public void onCreate(Bundle paramBundle)
    {
      super.onCreate(paramBundle);
      locationService().addListener(this);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
    {
      paramLayoutInflater = paramLayoutInflater.inflate(R.layout.customloction_map, paramViewGroup, false);
      setBasicMapView((MapView)paramLayoutInflater.findViewById(R.id.map));
      setBasicMap(getBasicMapView().getMap());
      getBasicMap().setOnCameraChangeListener(new CustomLocationBasicActivity.MapFragment.1(this));
      paramLayoutInflater.findViewById(R.id.my_location).setOnClickListener(new CustomLocationBasicActivity.MapFragment.2(this));
      updateMyLocationMarker();
      initFirstLocation();
      return paramLayoutInflater;
    }

    public void onDestroy()
    {
      super.onDestroy();
      locationService().removeListener(this);
    }

    public void onLocationChanged(LocationService paramLocationService)
    {
      if ((paramLocationService.status() != 2) || (!paramLocationService.hasLocation()))
        return;
      updateMyLocationMarker();
    }

    protected void setUpMap()
    {
      ThirdpartyMapUtils.zoomTo(getBasicMap(), getBasicMap().getMaxZoomLevel() - 4.0F);
    }

    protected void updateSelectedMarker(LatLng paramLatLng, String paramString)
    {
      if (paramLatLng == null);
      while (true)
      {
        return;
        if (CustomLocationBasicActivity.this.mShopPosition != null)
        {
          CustomLocationBasicActivity.this.mShopPosition.setPosition(paramLatLng);
          CustomLocationBasicActivity.this.mShopPosition.setTitle(paramString);
        }
        while (TextUtils.isEmpty(paramString))
        {
          CustomLocationBasicActivity.this.mShopPosition.remove();
          return;
          CustomLocationBasicActivity.access$702(CustomLocationBasicActivity.this, getBasicMap().addMarker(new MarkerOptions().position(paramLatLng).anchor(0.5F, 1.0F).title(paramString).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_green))));
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.basic.CustomLocationBasicActivity
 * JD-Core Version:    0.6.0
 */