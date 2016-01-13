package com.dianping.main.user.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
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
import com.dianping.model.UserProfile;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class AddClosedShopsActivity extends NovaLoadActivity
  implements RequestHandler<MApiRequest, MApiResponse>, DataSource.DataLoader, ShopListFragment.OnShopItemClickListener
{
  private MApiRequest addClosedShopRequest;
  private MApiRequest getClosedShopRequest;
  boolean isNeedReload;
  Button retryButton;
  DPObject selectedShop;
  private ShopListFragment shopListFragment;
  private DefaultShopListDataSource shopLstDataSource;

  public void addClosedRequest(DPObject paramDPObject)
  {
    int i = paramDPObject.getInt("ID");
    if (getAccount() == null)
    {
      paramDPObject = Environment.imei();
      if (getAccount() != null)
        break label143;
    }
    label143: for (String str = ""; ; str = accountService().token())
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("flag");
      localArrayList.add("1");
      localArrayList.add("shopid");
      localArrayList.add(String.valueOf(i));
      localArrayList.add("email");
      localArrayList.add(paramDPObject);
      localArrayList.add("token");
      localArrayList.add(str);
      this.addClosedShopRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/addshopfeedback.bin", (String[])localArrayList.toArray(new String[localArrayList.size()]));
      mapiService().exec(this.addClosedShopRequest, this);
      return;
      paramDPObject = getAccount().email();
      break;
    }
  }

  public void loadData(int paramInt, boolean paramBoolean)
  {
    if (this.getClosedShopRequest != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    localStringBuilder.append("getclosedshops.bin?");
    if (locationService().city() != null)
      localStringBuilder.append("cityid=").append(locationService().city().getInt("ID"));
    while (true)
    {
      localStringBuilder.append("&start=").append(paramInt);
      Location localLocation = location();
      if (localLocation != null)
      {
        DecimalFormat localDecimalFormat = Location.FMT;
        localStringBuilder.append("&lat=").append(String.valueOf(localDecimalFormat.format(localLocation.latitude())));
        localStringBuilder.append("&lng=").append(String.valueOf(localDecimalFormat.format(localLocation.longitude())));
      }
      this.getClosedShopRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
      if (paramBoolean)
        mapiCacheService().remove(this.getClosedShopRequest);
      mapiService().exec(this.getClosedShopRequest, this);
      return;
      localStringBuilder.append("cityid=").append(cityId());
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("找出附近已关闭的商户");
    this.shopLstDataSource = new DefaultShopListDataSource(this);
    this.shopLstDataSource.setIsRank(true);
    this.shopLstDataSource.setShowDistance(true);
    this.shopLstDataSource.setDataLoader(this);
    Location localLocation = location();
    if (localLocation != null)
      this.shopLstDataSource.setOffsetGPS(localLocation.offsetLatitude(), localLocation.offsetLongitude());
    if (paramBundle != null)
      this.shopLstDataSource.onRestoreInstanceState(paramBundle);
    this.shopListFragment = ((ShopListFragment)getSupportFragmentManager().findFragmentById(R.id.shop_list_fragment));
    this.shopListFragment.setShopListDataSource(this.shopLstDataSource);
    this.shopListFragment.setOnShopItemClickListener(this);
    this.shopListFragment.setNeedDefaultGA(false);
    this.retryButton = ((Button)findViewById(R.id.btn_retry));
    this.retryButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        AddClosedShopsActivity.this.locationService().refresh();
        AddClosedShopsActivity.this.findViewById(R.id.error_bad).setVisibility(8);
        AddClosedShopsActivity.this.retryButton.setVisibility(8);
        AddClosedShopsActivity.this.isNeedReload = true;
        AddClosedShopsActivity.this.showLocating(null);
      }
    });
  }

  protected void onDestroy()
  {
    if (this.getClosedShopRequest != null)
      mapiService().abort(this.getClosedShopRequest, this, true);
    if (this.addClosedShopRequest != null)
      mapiService().abort(this.addClosedShopRequest, this, true);
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
      this.shopLstDataSource.setOffsetGPS(location().offsetLatitude(), location().offsetLongitude());
      startReload();
      this.isNeedReload = false;
      return;
    }
    while (paramLocationService.status() != -1);
    showEmpty("没有定位信息,请重试...", true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getClosedShopRequest)
    {
      this.getClosedShopRequest = null;
      paramMApiRequest = "错误";
      paramMApiResponse = paramMApiResponse.message();
      if (paramMApiResponse != null)
        paramMApiRequest = paramMApiResponse.toString();
      if (this.shopLstDataSource != null)
        this.shopLstDataSource.setError(paramMApiRequest);
      showError(paramMApiRequest);
    }
    do
      return;
    while (paramMApiRequest != this.addClosedShopRequest);
    Toast.makeText(this, paramMApiResponse.message().content(), 1).show();
    this.addClosedShopRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getClosedShopRequest)
    {
      this.getClosedShopRequest = null;
      if (((paramMApiResponse.result() instanceof DPObject)) && (this.shopLstDataSource != null))
      {
        this.shopLstDataSource.appendShops((DPObject)paramMApiResponse.result());
        if (this.shopLstDataSource.shops().isEmpty())
          showEmpty(this.shopLstDataSource.emptyMsg());
      }
    }
    do
    {
      return;
      showContent();
      return;
      if (this.shopLstDataSource != null)
        this.shopLstDataSource.setError("错误");
      showError("错误");
      return;
    }
    while (paramMApiRequest != this.addClosedShopRequest);
    Toast.makeText(this, ((DPObject)paramMApiResponse.result()).getString("Content"), 1).show();
    this.addClosedShopRequest = null;
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
    if (this.shopLstDataSource != null)
      this.shopLstDataSource.onSaveInstanceState(paramBundle);
    if (isRetrieved())
      paramBundle.putBoolean("retrieved", true);
    super.onSaveInstanceState(paramBundle);
  }

  public void onShopItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong, DPObject paramDPObject)
  {
    statisticsEvent("more5", "more5_closedshop_item", String.valueOf(paramDPObject.getInt("ID")), 0);
    this.selectedShop = paramDPObject;
    new AlertDialog.Builder(paramView.getContext()).setTitle("提示").setMessage("确认该商户已关闭？").setNegativeButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        AddClosedShopsActivity.this.addClosedRequest(AddClosedShopsActivity.this.selectedShop);
      }
    }).setPositiveButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    }).show();
  }

  protected void reloadContent()
  {
    findViewById(R.id.error_bad).setVisibility(8);
    if (locationService().status() == 2)
    {
      this.shopLstDataSource.reload(false);
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
    super.setContentView(R.layout.add_closed_shops);
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
 * Qualified Name:     com.dianping.main.user.activity.AddClosedShopsActivity
 * JD-Core Version:    0.6.0
 */