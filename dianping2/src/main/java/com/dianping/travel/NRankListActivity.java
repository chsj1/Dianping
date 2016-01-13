package com.dianping.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
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
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class NRankListActivity extends NovaLoadActivity
  implements RequestHandler<MApiRequest, MApiResponse>, DataSource.DataLoader, ShopListFragment.OnShopItemClickListener
{
  private static final String TAG = NRankListActivity.class.getSimpleName();
  private String errorMsg;
  private MApiRequest mRankRequest;
  private DefaultShopListDataSource shopListDataSource;
  private ShopListFragment shopListFragment;
  private DPObject uriPair;

  public void loadData(int paramInt, boolean paramBoolean)
  {
    if (this.mRankRequest != null)
    {
      Log.w(TAG, "already requesting");
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
    localStringBuilder.append("searchrank.bin?");
    localStringBuilder.append("rankid=").append(this.uriPair.getString("ID"));
    localStringBuilder.append("&start=").append(paramInt);
    this.mRankRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    if (paramBoolean)
      mapiCacheService().remove(this.mRankRequest);
    mapiService().exec(this.mRankRequest, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject2 = getIntent().getData();
    if (localObject2 != null)
    {
      localObject1 = ((Uri)localObject2).getQueryParameter("id");
      localObject2 = ((Uri)localObject2).getQueryParameter("name");
      if (!TextUtils.isEmpty((CharSequence)localObject1))
        this.uriPair = new DPObject("Pair").edit().putString("ID", (String)localObject1).putString("Name", (String)localObject2).putInt("Type", 0).generate();
    }
    if (this.uriPair == null)
    {
      finish();
      return;
    }
    setTitle(this.uriPair.getString("Name"));
    this.shopListDataSource = new DefaultShopListDataSource(this);
    this.shopListDataSource.setIsRank(true);
    this.shopListDataSource.setShowDistance(true);
    this.shopListDataSource.setDataLoader(this);
    Object localObject1 = location();
    if (localObject1 != null)
      this.shopListDataSource.setOffsetGPS(((Location)localObject1).offsetLatitude(), ((Location)localObject1).offsetLongitude());
    if (paramBundle != null)
      this.shopListDataSource.onRestoreInstanceState(paramBundle);
    this.shopListFragment = ((ShopListFragment)getSupportFragmentManager().findFragmentById(R.id.shop_list_fragment));
    this.shopListFragment.setShopListDataSource(this.shopListDataSource);
    this.shopListFragment.setOnShopItemClickListener(this);
  }

  protected void onDestroy()
  {
    if (this.mRankRequest != null)
      mapiService().abort(this.mRankRequest, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRankRequest)
    {
      this.mRankRequest = null;
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
    if (paramMApiRequest == this.mRankRequest)
    {
      this.mRankRequest = null;
      if (((paramMApiResponse.result() instanceof DPObject)) && (this.shopListDataSource != null))
      {
        this.shopListDataSource.appendShops((DPObject)paramMApiResponse.result());
        showContent();
      }
    }
    else
    {
      return;
    }
    if (this.shopListDataSource != null)
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
    statisticsEvent("rank5", "rank5_item", "" + paramDPObject.getInt("ID"), 0);
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
    this.shopListDataSource.reload(false);
    showLoading(null);
  }

  protected void setupView()
  {
    super.setContentView(R.layout.rank_shop_list);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.NRankListActivity
 * JD-Core Version:    0.6.0
 */