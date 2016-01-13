package com.dianping.main.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaLoadActivity;
import com.dianping.base.shoplist.data.DataSource.DataLoader;
import com.dianping.base.shoplist.data.DefaultShopListDataSource;
import com.dianping.base.shoplist.fragment.ShopListFragment;
import com.dianping.base.shoplist.fragment.ShopListFragment.OnShopItemClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CommendAddPhotoActivity extends NovaLoadActivity
  implements RequestHandler<MApiRequest, MApiResponse>, DataSource.DataLoader, ShopListFragment.OnShopItemClickListener
{
  private static final String TAG = CommendAddPhotoActivity.class.getSimpleName();
  private String errorMsg;
  boolean isNeedReload;
  private MApiRequest mAddPhotoRequest;
  Button retryButton;
  private DefaultShopListDataSource shopListDataSource;
  private ShopListFragment shopListFragment;

  public void loadData(int paramInt, boolean paramBoolean)
  {
    if (this.mAddPhotoRequest != null)
    {
      Log.w(TAG, "already requesting");
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    localStringBuilder.append("commendphotoshop.bin?");
    localStringBuilder.append("start=").append(paramInt);
    Location localLocation = location();
    if (localLocation != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localStringBuilder.append("&lat=").append(String.valueOf(localDecimalFormat.format(localLocation.latitude())));
      localStringBuilder.append("&lng=").append(String.valueOf(localDecimalFormat.format(localLocation.longitude())));
    }
    this.mAddPhotoRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    if (paramBoolean)
      mapiCacheService().remove(this.mAddPhotoRequest);
    mapiService().exec(this.mAddPhotoRequest, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("上传照片");
    this.shopListDataSource = new DefaultShopListDataSource(this);
    this.shopListDataSource.setIsRank(true);
    this.shopListDataSource.setShowDistance(true);
    this.shopListDataSource.setDataLoader(this);
    Location localLocation = location();
    if (localLocation != null)
      this.shopListDataSource.setOffsetGPS(localLocation.offsetLatitude(), localLocation.offsetLongitude());
    if (paramBundle != null)
      this.shopListDataSource.onRestoreInstanceState(paramBundle);
    this.shopListFragment = ((ShopListFragment)getSupportFragmentManager().findFragmentById(R.id.shop_list_fragment));
    this.shopListFragment.setShopListDataSource(this.shopListDataSource);
    this.shopListFragment.setOnShopItemClickListener(this);
    this.retryButton = ((Button)findViewById(R.id.btn_retry));
    this.retryButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        CommendAddPhotoActivity.this.locationService().refresh();
        CommendAddPhotoActivity.this.findViewById(R.id.error_bad).setVisibility(8);
        CommendAddPhotoActivity.this.retryButton.setVisibility(8);
        CommendAddPhotoActivity.this.isNeedReload = true;
        CommendAddPhotoActivity.this.showLocating(null);
      }
    });
  }

  protected void onDestroy()
  {
    if (this.mAddPhotoRequest != null)
      mapiService().abort(this.mAddPhotoRequest, this, true);
    super.onDestroy();
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    if (paramLocationService == null);
    do
    {
      do
        return;
      while (!this.isNeedReload);
      if (paramLocationService.status() != 2)
        continue;
      this.shopListDataSource.setOffsetGPS(location().offsetLatitude(), location().offsetLongitude());
      startReload();
      this.isNeedReload = false;
      return;
    }
    while (paramLocationService.status() != -1);
    showEmpty("没有定位信息,请重试...", true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mAddPhotoRequest)
    {
      this.mAddPhotoRequest = null;
      paramMApiRequest = "错误";
      paramMApiResponse = paramMApiResponse.message();
      if (paramMApiResponse != null)
      {
        Log.i(TAG, paramMApiResponse.toString());
        paramMApiRequest = paramMApiResponse.toString();
      }
      if (this.shopListDataSource != null)
        this.shopListDataSource.setError(paramMApiRequest);
      showError(paramMApiRequest);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mAddPhotoRequest)
    {
      this.mAddPhotoRequest = null;
      if ((!(paramMApiResponse.result() instanceof DPObject)) || (this.shopListDataSource == null))
        break label78;
      this.shopListDataSource.appendShops((DPObject)paramMApiResponse.result());
      if (this.shopListDataSource.shops().isEmpty())
        showEmpty(this.shopListDataSource.emptyMsg());
    }
    else
    {
      return;
    }
    showContent();
    return;
    label78: if (this.shopListDataSource != null)
      this.shopListDataSource.setError("错误");
    showError("错误");
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    if (isRetrieved())
    {
      paramBundle = paramBundle.getString("error");
      if (paramBundle != null)
        showError(paramBundle);
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    if (this.shopListDataSource != null)
      this.shopListDataSource.onSaveInstanceState(paramBundle);
    if (isRetrieved())
    {
      paramBundle.putBoolean("retrieved", true);
      if (this.errorMsg != null)
        paramBundle.putString("error", this.errorMsg);
    }
    super.onSaveInstanceState(paramBundle);
  }

  public void onShopItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong, DPObject paramDPObject)
  {
    statisticsEvent("commendphoto5", "commendphoto5_item", "" + paramDPObject.getInt("ID"), 0);
    paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramDPObject.getInt("ID")));
    paramAdapterView.putExtra("shopId", paramDPObject.getInt("ID"));
    paramAdapterView.putExtra("shop", paramDPObject);
    if (this.shopListDataSource.hasSearchDate())
    {
      paramAdapterView.putExtra("hotelBooking", true);
      paramAdapterView.putExtra("checkinTime", this.shopListFragment.checkinTimeMills());
      paramAdapterView.putExtra("checkoutTime", this.shopListFragment.checkoutTimeMills());
    }
    startActivity(paramAdapterView);
  }

  protected void reloadContent()
  {
    findViewById(R.id.error_bad).setVisibility(8);
    if (locationService().status() == 2)
    {
      this.shopListDataSource.reload(false);
      showLoading(null);
      return;
    }
    if (locationService().status() == 1)
    {
      this.isNeedReload = true;
      showLocating(null);
      return;
    }
    showEmpty("没有定位信息,请重试...", true);
    this.isNeedReload = true;
  }

  protected void setupView()
  {
    super.setContentView(R.layout.commend_add_photo);
  }

  protected void showEmpty(String paramString)
  {
    findViewById(R.id.content).setVisibility(8);
    findViewById(R.id.empty).setVisibility(8);
    findViewById(R.id.error_bad).setVisibility(0);
    if (!TextUtils.isEmpty(paramString))
      ((TextView)findViewById(R.id.error_text_bad)).setText(paramString);
  }

  protected void showEmpty(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
      this.retryButton.setVisibility(0);
    showEmpty(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.CommendAddPhotoActivity
 * JD-Core Version:    0.6.0
 */