package com.dianping.mall.nearby;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.ShopListAdapter;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.ExtendableListView.LayoutParams;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class MallNearByActivity extends NovaActivity
  implements DataLoadListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, ShopListAdapter.ShopListReloadHandler, MallNearByFilterBar.FilterChangeListener
{
  public static int FROM_RELOAD = 1;
  public static int FROM_SCROLL = 2;
  private int categoryType;
  private Context context = this;
  private View dividerLine;
  private RelativeLayout errorView;
  private View filterView;
  private MallNearByFilterView filterViewFix;
  private MallNearByFilterView filterViewFloat;
  private View headerView;
  private ShopListAdapter mListAdapter;
  private int mallId;
  private MallNearByDataSource mallNearByDataSource;
  private RelativeLayout.LayoutParams relativeParams;
  private ListView shopListView;

  private void dealErrorCondition(DataStatus paramDataStatus, Object paramObject)
  {
    switch (3.$SwitchMap$com$dianping$mall$nearby$DataStatus[paramDataStatus.ordinal()])
    {
    default:
      return;
    case 1:
    }
    setupMallNearByEmptyView("网络不给力哦", paramDataStatus);
    setPageIfNormal(false);
  }

  private void fetchParams(Bundle paramBundle)
  {
    this.mallId = getIntParam("shopid");
    this.categoryType = getIntParam("categorytype");
    this.mallNearByDataSource = new MallNearByDataSource(this);
    this.mallNearByDataSource.setDataLoadListener(this);
    this.mallNearByDataSource.setMallId(this.mallId);
    this.mallNearByDataSource.setCategoryType(this.categoryType);
    this.mallNearByDataSource.setDefaultFilter();
    if (paramBundle == null)
    {
      paramBundle = (DPObject)getIntent().getParcelableExtra("shopFilter");
      this.mallNearByDataSource.lat = super.getStringParam("lat");
      this.mallNearByDataSource.lng = super.getStringParam("lng");
      this.mallNearByDataSource.geoType = super.getIntParam("geotype", 1);
      this.mallNearByDataSource.initFilter(paramBundle, super.getStringParam("categoryid"), super.getStringParam("sortid"), super.getStringParam("floor"));
      return;
    }
    this.mallNearByDataSource.onRestoreInstanceState(paramBundle);
  }

  private void initView()
  {
    if (this.categoryType == 20)
      setTitle("商场购物");
    this.relativeParams = new RelativeLayout.LayoutParams(-1, -1);
    if (this.headerView == null)
      GAHelper.instance().contextStatisticsEvent(this.context, "gotomall", null, "view");
    this.headerView = LayoutInflater.from(this.context).inflate(R.layout.mall_nearby_header, null, false);
    this.headerView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (MallNearByActivity.this.mallId <= 0)
          return;
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + MallNearByActivity.this.mallId));
        MallNearByActivity.this.startActivity(paramView);
        GAHelper.instance().contextStatisticsEvent(MallNearByActivity.this.context, "gotomall", null, "tap");
      }
    });
    this.filterViewFix = ((MallNearByFilterView)findViewById(R.id.mall_nearby_shoplist_filterviewbar_fix));
    this.filterViewFix.setMallNearByDataSource(this.mallNearByDataSource);
    this.filterViewFix.getFilterBar().setFilterChangeListener(this);
    this.filterViewFix.getFilterBar().addItem("catagory", this.mallNearByDataSource.curCategory().getString("Name"));
    this.filterViewFix.getFilterBar().addItem("floor", this.mallNearByDataSource.curFloor().getString("Name"));
    this.filterViewFix.getFilterBar().addItem("sort", this.mallNearByDataSource.curSort().getString("Name"));
    this.filterView = LayoutInflater.from(this.context).inflate(R.layout.mall_nearby_shop_filter_float, this.shopListView, false);
    this.filterViewFloat = ((MallNearByFilterView)this.filterView.findViewById(R.id.mall_nearby_shoplist_filterviewbar_float));
    this.filterViewFloat.setMallNearByDataSource(this.mallNearByDataSource);
    this.filterViewFloat.getFilterBar().addItem("catagory", this.mallNearByDataSource.curCategory().getString("Name"));
    this.filterViewFloat.getFilterBar().addItem("floor", this.mallNearByDataSource.curFloor().getString("Name"));
    this.filterViewFloat.getFilterBar().addItem("sort", this.mallNearByDataSource.curSort().getString("Name"));
    this.filterViewFloat.getFilterBar().setFilterChangeListener(this);
    this.shopListView = ((ListView)findViewById(R.id.mall_nearby_shoplistview));
    this.shopListView.setOnScrollListener(this);
    this.shopListView.setOnItemClickListener(this);
    this.shopListView.addHeaderView(this.headerView);
    this.shopListView.addHeaderView(this.filterView);
    this.dividerLine = new View(this.context);
    ExtendableListView.LayoutParams localLayoutParams = new ExtendableListView.LayoutParams(-1, 0);
    this.dividerLine.setLayoutParams(localLayoutParams);
    this.dividerLine.setBackgroundColor(getResources().getColor(R.color.gray_light_background));
    this.dividerLine.setVisibility(0);
    this.shopListView.addHeaderView(this.dividerLine);
    this.shopListView.setBackgroundColor(-1);
    this.mListAdapter = new ShopListAdapter(this);
    this.mallNearByDataSource.setShowDistance(true);
    this.mListAdapter.setShopList(this.mallNearByDataSource);
    this.mListAdapter.setShouldShowImage(NovaConfigUtils.isShowImageInMobileNetwork());
    this.shopListView.setAdapter(this.mListAdapter);
    this.shopListView.setHeaderDividersEnabled(false);
    this.shopListView.setDivider(null);
    this.errorView = ((RelativeLayout)findViewById(R.id.mall_nearby_error_view));
  }

  private void setPageIfNormal(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.errorView.setVisibility(8);
      if (!this.shopListView.isShown())
        this.shopListView.setVisibility(0);
      return;
    }
    if (!this.errorView.isShown())
      this.errorView.setVisibility(0);
    this.shopListView.setVisibility(8);
    this.filterViewFix.setVisibility(8);
  }

  private void setupMallNearByEmptyView(String paramString, DataStatus paramDataStatus)
  {
    Object localObject = null;
    if (paramDataStatus == DataStatus.ERROR_NETWORK)
    {
      paramDataStatus = getLayoutInflater().inflate(R.layout.mall_nearby_shop_empty, null);
      ((TextView)paramDataStatus.findViewById(16908308)).setText(paramString);
      paramString = paramDataStatus;
    }
    while (true)
    {
      this.errorView.removeAllViews();
      this.errorView.addView(paramString, this.relativeParams);
      return;
      paramString = localObject;
      if (paramDataStatus != DataStatus.INITIAL)
        continue;
      paramString = getLayoutInflater().inflate(R.layout.loading_item_fullscreen, null);
    }
  }

  private void updateFilter()
  {
    this.filterViewFix.updateFilter();
    this.filterViewFloat.updateFilter();
  }

  private void updateHeadView()
  {
    TextView localTextView = (TextView)this.headerView.findViewById(R.id.shop_name);
    Object localObject1 = "";
    if (!TextUtils.isEmpty(this.mallNearByDataSource.getShopName()))
      localObject1 = this.mallNearByDataSource.getShopName();
    Object localObject2 = localObject1;
    if (!TextUtils.isEmpty((CharSequence)localObject1))
    {
      localObject2 = localObject1;
      if (!TextUtils.isEmpty(this.mallNearByDataSource.getBranchName()))
        localObject2 = (String)localObject1 + "(" + this.mallNearByDataSource.getBranchName() + ")";
    }
    localTextView.setText((CharSequence)localObject2);
    localObject1 = (TextView)this.headerView.findViewById(R.id.distance);
    if (!TextUtils.isEmpty(this.mallNearByDataSource.getDistance()))
    {
      ((TextView)localObject1).setText(this.mallNearByDataSource.getDistance());
      ((TextView)localObject1).setVisibility(0);
    }
    localObject1 = (BlurImageView)this.headerView.findViewById(R.id.mall_nearby_header_icon);
    if (!TextUtils.isEmpty(this.mallNearByDataSource.getDefaultPic()))
      ((BlurImageView)localObject1).setImage(this.mallNearByDataSource.getDefaultPic());
  }

  public void filterChange()
  {
    updateFilter();
    this.mallNearByDataSource.reset(true);
    this.mallNearByDataSource.setLoadingDataStatus();
    this.mListAdapter.setShopList(this.mallNearByDataSource);
    this.mallNearByDataSource.loadData(0, false);
  }

  public void loadShopListFinsh(DataStatus paramDataStatus, Object paramObject)
  {
    switch (3.$SwitchMap$com$dianping$mall$nearby$DataStatus[paramDataStatus.ordinal()])
    {
    default:
      return;
    case 2:
      setPageIfNormal(true);
      updateFilter();
      updateHeadView();
      this.mListAdapter.setShopList(this.mallNearByDataSource);
      sendAdClientGA(FROM_RELOAD);
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          MallNearByActivity.this.onScrollStateChanged(MallNearByActivity.this.shopListView, 0);
        }
      }
      , 500L);
      return;
    case 1:
    }
    dealErrorCondition(paramDataStatus, paramObject);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt2 == -1) && (paramIntent != null))
    {
      this.mallNearByDataSource.reset(true);
      this.mListAdapter.setShopList(this.mallNearByDataSource);
      this.mallNearByDataSource.reload(true);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.mall_nearby_shop_main_activity);
    super.getWindow().setBackgroundDrawable(null);
    fetchParams(paramBundle);
    initView();
    setupMallNearByEmptyView("", DataStatus.INITIAL);
    setPageIfNormal(false);
    this.mallNearByDataSource.loadData(0, true);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramView = paramAdapterView.getItemAtPosition(paramInt);
    paramAdapterView = paramView;
    if ((paramView instanceof ShopDataModel))
      paramAdapterView = ((ShopDataModel)paramView).shopObj;
    if (((paramAdapterView instanceof DPObject)) && (((DPObject)paramAdapterView).getInt("ID") > 0))
    {
      paramAdapterView = (DPObject)paramAdapterView;
      if (paramAdapterView.getBoolean("IsAdShop"))
        AdClientUtils.sendAdGa(paramAdapterView, "2", String.valueOf(paramAdapterView.getInt("ListPosition") + 1));
      startShopInfoActivity(paramAdapterView);
    }
  }

  public void onResume()
  {
    super.onResume();
    onScrollStateChanged(this.shopListView, 0);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    this.mallNearByDataSource.onSaveInstanceState(paramBundle);
    super.onSaveInstanceState(paramBundle);
  }

  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 < this.shopListView.getHeaderViewsCount() - 2)
      if (this.filterViewFix.isShown())
        this.filterViewFix.setVisibility(8);
    do
      return;
    while (this.filterViewFix.isShown());
    this.filterViewFix.setVisibility(0);
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
    if (paramInt == 0)
      sendAdClientGA(FROM_SCROLL);
  }

  public void reload(boolean paramBoolean)
  {
    if (this.mallNearByDataSource != null)
      this.mallNearByDataSource.reload(paramBoolean);
  }

  protected void sendAdClientGA(int paramInt)
  {
    if ((this.shopListView == null) || (this.mallNearByDataSource == null) || (this.mListAdapter == null))
      return;
    int j = this.shopListView.getFirstVisiblePosition();
    int k = this.shopListView.getLastVisiblePosition();
    int i;
    if (paramInt == FROM_RELOAD)
    {
      if (this.mallNearByDataSource.startIndex() == 0)
        this.mallNearByDataSource.mGaRecordTables.clear();
      i = this.mallNearByDataSource.startIndex();
    }
    while (true)
    {
      if ((i >= this.mallNearByDataSource.nextStartIndex()) || (this.mallNearByDataSource.shops().size() <= i))
      {
        if (paramInt != FROM_SCROLL)
          break;
        paramInt = j;
        label110: if (paramInt > k)
          break;
      }
      try
      {
        Object localObject = this.shopListView.getItemAtPosition(paramInt);
        if ((localObject instanceof DPObject))
        {
          localObject = (DPObject)localObject;
          if ((((DPObject)localObject).getBoolean("IsAdShop")) && (!this.mallNearByDataSource.mGaRecordTables.get(((DPObject)localObject).getInt("ID"))))
          {
            AdClientUtils.sendAdGa((DPObject)localObject, "3", String.valueOf(((DPObject)localObject).getInt("ListPosition") + 1));
            this.mallNearByDataSource.mGaRecordTables.put(((DPObject)localObject).getInt("ID"), true);
          }
        }
        paramInt += 1;
        break label110;
        localObject = (DPObject)this.mallNearByDataSource.shops().get(i);
        if (((DPObject)localObject).getBoolean("IsAdShop"))
          AdClientUtils.sendAdGa((DPObject)localObject, "1", String.valueOf(((DPObject)localObject).getInt("ListPosition") + 1));
        i += 1;
      }
      catch (Exception localException)
      {
      }
    }
  }

  protected void startShopInfoActivity(DPObject paramDPObject)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramDPObject.getInt("ID")));
    localIntent.putExtra("shopId", paramDPObject.getInt("ID"));
    localIntent.putExtra("shop", paramDPObject);
    if (paramDPObject.getBoolean("IsAdShop"))
    {
      paramDPObject = paramDPObject.getString("ExtraJson");
      if (TextUtils.isEmpty(paramDPObject));
    }
    try
    {
      paramDPObject = new JSONObject(paramDPObject).optString("Feedback");
      if (!TextUtils.isEmpty(paramDPObject))
        localIntent.putExtra("_fb_", paramDPObject);
      startActivity(localIntent);
      GAHelper.instance().contextStatisticsEvent(this.context, "extended_shop_item", null, "tap");
      return;
    }
    catch (JSONException paramDPObject)
    {
      while (true)
        paramDPObject.printStackTrace();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.mall.nearby.MallNearByActivity
 * JD-Core Version:    0.6.0
 */