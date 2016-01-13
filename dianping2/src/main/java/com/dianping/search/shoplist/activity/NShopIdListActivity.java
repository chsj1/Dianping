package com.dianping.search.shoplist.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
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
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NShopIdListActivity extends NovaActivity
  implements DataSource.DataLoader, RequestHandler<MApiRequest, MApiResponse>, ShopListFragment.OnShopItemClickListener
{
  private static final String TAG = NShopIdListActivity.class.getSimpleName();
  private int dealid = 0;
  private DPObject entryShop;
  private String ids;
  private int istuan = 0;
  private View mExtraView;
  private DPObject prePromo;
  private int shopId = 0;
  private DefaultShopListDataSource shopListDataSource;
  private ShopListFragment shopListFragment;
  private MApiRequest shopRequest;
  private boolean showAddBranchShop;
  private String urlExtra;

  private View getLastExtrView()
  {
    if (this.mExtraView != null)
      return this.mExtraView;
    this.mExtraView = getLayoutInflater().inflate(R.layout.add_shop_item, null, false);
    this.mExtraView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        NShopIdListActivity.this.startAddBranchShopActivity();
      }
    });
    return this.mExtraView;
  }

  private void startAddBranchShopActivity()
  {
    String str = "dianping://web?url=http://m.dianping.com/poi/app/shop/addShop?newtoken=!";
    if (this.entryShop != null)
    {
      int i = this.entryShop.getInt("GroupID");
      str = this.entryShop.getString("Name");
      str = "dianping://web?url=http://m.dianping.com/poi/app/shop/addShop?newtoken=!" + "&groupId=" + i + "&shopName=" + str;
    }
    startActivity(str);
  }

  public void loadData(int paramInt, boolean paramBoolean)
  {
    DecimalFormat localDecimalFormat = Location.FMT;
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    if (this.shopId > 0)
    {
      localStringBuilder.append("getshopbranches.bin?shopid=").append(this.shopId);
      localStringBuilder.append("&dealid=").append(this.dealid);
      localStringBuilder.append("&istuan=").append(this.istuan);
      Location localLocation = location();
      if (localLocation != null)
      {
        localStringBuilder.append("&lat=").append(localDecimalFormat.format(localLocation.latitude()));
        localStringBuilder.append("&lng=").append(localDecimalFormat.format(localLocation.longitude()));
      }
      localStringBuilder.append("&start=").append(paramInt);
      if (this.urlExtra != null)
        localStringBuilder.append("&extra=").append(URLEncoder.encode(this.urlExtra));
      if (this.shopId <= 0)
        break label220;
    }
    label220: for (this.shopRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED); ; this.shopRequest = BasicMApiRequest.mapiPost(localStringBuilder.toString(), new String[] { "ids", this.ids }))
    {
      if (paramBoolean)
        mapiCacheService().remove(this.shopRequest);
      mapiService().exec(this.shopRequest, this);
      return;
      localStringBuilder.append("shoplist.bin?");
      break;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.shop_id_list);
    if (getIntent().getData() == null)
    {
      this.ids = getIntent().getStringExtra("ids");
      this.prePromo = ((DPObject)getIntent().getParcelableExtra("promo"));
      this.showAddBranchShop = getIntent().getBooleanExtra("showAddBranchShop", false);
      this.entryShop = ((DPObject)getIntent().getParcelableExtra("shop"));
      if (this.showAddBranchShop)
      {
        setTitle("分店列表");
        setRightTitleButton(R.drawable.ic_btn_add_shop, new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            NShopIdListActivity.this.startAddBranchShopActivity();
          }
        });
      }
    }
    while (true)
    {
      this.shopListDataSource = new DefaultShopListDataSource(this);
      this.shopListDataSource.setDataLoader(this);
      this.shopListDataSource.setShowDistance(true);
      if (this.showAddBranchShop)
        this.shopListDataSource.setLastExtraView(getLastExtrView());
      if (paramBundle != null)
        this.shopListDataSource.onRestoreInstanceState(paramBundle);
      paramBundle = location();
      if (paramBundle != null)
        this.shopListDataSource.setOffsetGPS(paramBundle.offsetLatitude(), paramBundle.offsetLongitude());
      this.shopListFragment = ((ShopListFragment)getSupportFragmentManager().findFragmentById(R.id.shop_list_fragment));
      this.shopListFragment.setShopListDataSource(this.shopListDataSource);
      this.shopListFragment.setOnShopItemClickListener(this);
      if ((this.ids == null) && (this.shopId <= 0))
        finish();
      return;
      this.ids = getIntent().getData().getQueryParameter("ids");
      if (!TextUtils.isEmpty(getIntent().getData().getQueryParameter("shopid")))
        this.shopId = Integer.parseInt(getIntent().getData().getQueryParameter("shopid"));
      if (!TextUtils.isEmpty(getIntent().getData().getQueryParameter("dealid")))
        this.dealid = Integer.parseInt(getIntent().getData().getQueryParameter("dealid"));
      if (!TextUtils.isEmpty(getIntent().getData().getQueryParameter("istuan")))
        this.istuan = Integer.parseInt(getIntent().getData().getQueryParameter("istuan"));
      if (this.ids == null)
        this.ids = getIntent().getStringExtra("ids");
      this.prePromo = ((DPObject)getIntent().getParcelableExtra("promo"));
      this.urlExtra = getIntent().getData().getQueryParameter("extra");
      this.showAddBranchShop = getIntent().getBooleanExtra("showAddBranchShop", false);
      this.entryShop = ((DPObject)getIntent().getParcelableExtra("shop"));
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.shopRequest != null)
      mapiService().abort(this.shopRequest, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest)
    {
      this.shopRequest = null;
      paramMApiRequest = "错误";
      paramMApiResponse = paramMApiResponse.message();
      if (paramMApiResponse != null)
      {
        Log.i(TAG, paramMApiResponse.toString());
        paramMApiRequest = paramMApiResponse.toString();
      }
      if (this.shopListDataSource != null)
        this.shopListDataSource.setError(paramMApiRequest);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopRequest)
    {
      this.shopRequest = null;
      if ((!(paramMApiResponse.result() instanceof DPObject)) || (this.shopListDataSource == null))
        break label49;
      this.shopListDataSource.appendShops((DPObject)paramMApiResponse.result());
    }
    label49: 
    do
      return;
    while (this.shopListDataSource == null);
    this.shopListDataSource.setError("错误");
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.shopListDataSource.onRestoreInstanceState(paramBundle);
    this.showAddBranchShop = paramBundle.getBoolean("showAddBranchShop");
    this.entryShop = ((DPObject)paramBundle.getParcelable("entryShop"));
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.shopListDataSource.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("showAddBranchShop", this.showAddBranchShop);
    paramBundle.putParcelable("entryShop", this.entryShop);
  }

  public void onShopItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong, DPObject paramDPObject)
  {
    if (paramDPObject != null)
    {
      paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramDPObject.getInt("ID")));
      paramAdapterView.putExtra("shop", paramDPObject);
      if (this.prePromo != null)
      {
        paramAdapterView.putExtra("promoid", this.prePromo.getInt("ID"));
        paramAdapterView.putExtra("promo", this.prePromo);
      }
      paramAdapterView.putExtra("urlExtra", this.urlExtra);
      if (this.shopListDataSource.hasSearchDate())
      {
        paramAdapterView.putExtra("hotelBooking", true);
        paramAdapterView.putExtra("checkinTime", this.shopListFragment.checkinTimeMills());
        paramAdapterView.putExtra("checkoutTime", this.shopListFragment.checkoutTimeMills());
      }
      startActivity(paramAdapterView);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.activity.NShopIdListActivity
 * JD-Core Version:    0.6.0
 */