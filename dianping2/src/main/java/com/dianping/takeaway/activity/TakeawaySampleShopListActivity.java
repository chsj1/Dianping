package com.dianping.takeaway.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.widget.TitleBar;
import com.dianping.takeaway.entity.TakeawayListAdapter;
import com.dianping.takeaway.entity.TakeawaySampleShoplistDataSource;
import com.dianping.takeaway.entity.TakeawaySampleShoplistDataSource.ResultStatus;
import com.dianping.takeaway.entity.TakeawaySampleShoplistDataSource.TaShoplistLoadListener;
import com.dianping.takeaway.view.TAFilterBar;
import com.dianping.takeaway.view.TAFilterBar.FilterChangeListener;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;

public class TakeawaySampleShopListActivity extends NovaActivity
  implements TakeawaySampleShoplistDataSource.TaShoplistLoadListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, ShopListAdapter.ShopListReloadHandler, TAFilterBar.FilterChangeListener
{
  private static final int REQUEST_CHANGE_ADDRESS = 1;
  private RelativeLayout errorView;
  private TAFilterBar filterBarFix;
  private TAFilterBar filterBarFloat;
  private View filterView;
  protected LinearLayout footerViewLayout;
  protected LinearLayout headerViewLayout;
  private TakeawayListAdapter listAdapter;
  private ListView shopListView;

  private String getTitleContent()
  {
    if (getDataSource().isShopListPage)
    {
      if (TextUtils.isEmpty(getDataSource().curAddress))
        return "未知地址";
      return getDataSource().curAddress;
    }
    return getDataSource().searchKey;
  }

  private void initErrorView()
  {
    this.errorView = ((RelativeLayout)findViewById(R.id.takeaway_error_view));
  }

  private void intShoplistView()
  {
    this.shopListView = ((ListView)findViewById(R.id.takeaway_shoplistview));
    this.shopListView.setOnScrollListener(this);
    this.shopListView.setOnItemClickListener(this);
    if (this.headerViewLayout != null)
      this.shopListView.addHeaderView(this.headerViewLayout);
    if (this.filterView != null)
      this.shopListView.addHeaderView(this.filterView);
    if ((getDataSource().isShopListPage) && (this.footerViewLayout != null))
      this.shopListView.addFooterView(this.footerViewLayout);
    getListAdapter().setShopList(getDataSource());
    this.shopListView.setAdapter(getListAdapter());
    this.shopListView.setDividerHeight(0);
  }

  protected void fetchParams(Bundle paramBundle)
  {
    if (paramBundle == null)
    {
      String str = super.getStringParam("keyword");
      paramBundle = super.getStringParam("address");
      DPObject localDPObject = (DPObject)getIntent().getParcelableExtra("shopFilter");
      getDataSource().forceFinish = super.getBooleanParam("onlyfinish");
      TakeawaySampleShoplistDataSource localTakeawaySampleShoplistDataSource = getDataSource();
      if (paramBundle != null);
      while (true)
      {
        localTakeawaySampleShoplistDataSource.curAddress = paramBundle;
        getDataSource().searchKey = str;
        getDataSource().lat = super.getStringParam("lat");
        getDataSource().lng = super.getStringParam("lng");
        getDataSource().geoType = super.getIntParam("geotype", 1);
        getDataSource().isShopListPage = TextUtils.isEmpty(str);
        getDataSource().initFilter(localDPObject, super.getStringParam("parentid"), super.getStringParam("categoryid"), super.getStringParam("sortid"), super.getStringParam("multifilterids"));
        getDataSource().noShopReason = super.getIntParam("noshopreason", -1);
        getDataSource().extraInfo = super.getStringParam("extrainfo");
        return;
        paramBundle = "";
      }
    }
    getDataSource().onRestoreInstanceState(paramBundle);
  }

  public void filterChange()
  {
    updateFilterBarView();
    getDataSource().reset(true);
    getDataSource().setLoadingDataStatus();
    getListAdapter().setShopList(getDataSource());
    getDataSource().loadData(0, false);
  }

  protected TakeawaySampleShoplistDataSource getDataSource()
  {
    return null;
  }

  public GAUserInfo getGAUserInfo()
  {
    return getDataSource().getGAUserInfo();
  }

  protected String getItemClickElementId()
  {
    return "shop";
  }

  protected TakeawayListAdapter getListAdapter()
  {
    if (this.listAdapter == null)
      this.listAdapter = new TakeawayListAdapter(this, this);
    return this.listAdapter;
  }

  protected void gotoAddressManager()
  {
    StringBuilder localStringBuilder = new StringBuilder("dianping://takeawayaddress");
    localStringBuilder.append("?source=1");
    localStringBuilder.append("&queryid=").append(getDataSource().queryId());
    startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse(localStringBuilder.toString())), 1);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void initFilterBarView()
  {
    this.filterBarFix = ((TAFilterBar)findViewById(R.id.takeaway_shoplist_filterbar_fix));
    this.filterBarFix.setTakeawayDataSource(getDataSource());
    this.filterBarFix.addItem("catagory", getDataSource().curCategory().getString("Name"));
    this.filterBarFix.addItem("sort", getDataSource().curSort().getString("Name"));
    this.filterBarFix.addItem("multifilter", false);
    this.filterBarFix.setFilterChangeListener(this);
    this.filterView = LayoutInflater.from(this).inflate(R.layout.takeawaymain_ptrlv_headerviewc, this.shopListView, false);
    this.filterBarFloat = ((TAFilterBar)this.filterView.findViewById(R.id.takeaway_shoplist_filterbar_float));
    this.filterBarFloat.setTakeawayDataSource(getDataSource());
    this.filterBarFloat.addItem("catagory", getDataSource().curCategory().getString("Name"));
    this.filterBarFloat.addItem("sort", getDataSource().curSort().getString("Name"));
    this.filterBarFloat.addItem("multifilter", false);
    this.filterBarFloat.setFilterChangeListener(this);
  }

  protected void initShoplistFooterView()
  {
  }

  protected void initShoplistHeaderView()
  {
  }

  protected void initTitleBarView()
  {
  }

  protected void initViews()
  {
    initTitleBarView();
    initShoplistHeaderView();
    initShoplistFooterView();
    initFilterBarView();
    intShoplistView();
    initErrorView();
  }

  public void loadShopListFinsh(TakeawaySampleShoplistDataSource.ResultStatus paramResultStatus, Object paramObject)
  {
    switch (4.$SwitchMap$com$dianping$takeaway$entity$TakeawaySampleShoplistDataSource$ResultStatus[paramResultStatus.ordinal()])
    {
    default:
      return;
    case 1:
    case 2:
      if (getDataSource().noShopReason != 0)
        break;
      paramResultStatus = getGAUserInfo();
      paramResultStatus.title = (getDataSource().curCategory().getInt("ID") + "&" + getDataSource().curMultiFilterIds());
      GAHelper.instance().contextStatisticsEvent(this, "noresult", paramResultStatus, "view");
    case 3:
      updateTitleBarView(getTitleContent());
      updateShoplistHeaderView();
      updateFilterBarView();
      getListAdapter().setShopList(getDataSource());
      resetPageVisibility(true);
      return;
    case 4:
      updateTitleBarView(getTitleContent());
      updateErrorView("网络不给力哦", paramResultStatus);
      resetPageVisibility(false);
      return;
    case 5:
    }
    updateTitleBarView("未知地址");
    updateErrorView("无法定位/定位动能被关闭", paramResultStatus);
    resetPageVisibility(false);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 1) && (paramInt2 == -1) && (paramIntent != null))
    {
      String str = paramIntent.getStringExtra("Address");
      if (!getDataSource().curAddress.equals(str))
      {
        getDataSource().curAddress = str;
        getDataSource().lat = String.valueOf(paramIntent.getDoubleExtra("Lat", 0.0D));
        getDataSource().lng = String.valueOf(paramIntent.getDoubleExtra("Lng", 0.0D));
        getDataSource().setTaDefaultFilter();
      }
    }
    else if ((paramInt2 == -1) && (paramIntent != null))
    {
      getDataSource().reset(true);
      getListAdapter().setShopList(getDataSource());
      getDataSource().reload(true);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    onSetContentView();
    super.getWindow().setBackgroundDrawable(null);
    getDataSource().setTaShoplistLoadListener(this);
    getDataSource().setLastExtraView(R.layout.takeaway_shoplist_last_extra_view, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
      }
    }
    , this);
    fetchParams(paramBundle);
    initViews();
    updateErrorView("", TakeawaySampleShoplistDataSource.ResultStatus.INITIAL);
    resetPageVisibility(false);
    getDataSource().loadData(0, true);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    int i = this.shopListView.getHeaderViewsCount();
    if ((paramAdapterView instanceof DPObject))
    {
      paramAdapterView = (DPObject)paramAdapterView;
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawaydishlist"));
      paramView.putExtra("lat", getDataSource().lat);
      paramView.putExtra("lng", getDataSource().lng);
      paramView.putExtra("shopid", String.valueOf(paramAdapterView.getInt("ID")));
      paramView.putExtra("shopname", paramAdapterView.getString("Name"));
      paramView.putExtra("source", 1);
      paramView.putExtra("address", getDataSource().curAddress);
      paramView.putExtra("queryid", getDataSource().queryId());
      startActivity(paramView);
      paramView = getGAUserInfo();
      paramView.shop_id = Integer.valueOf(paramAdapterView.getInt("ID"));
      paramView.index = Integer.valueOf(paramInt - i);
      GAHelper.instance().contextStatisticsEvent(this, getItemClickElementId(), paramView, "tap");
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    getDataSource().onSaveInstanceState(paramBundle);
    super.onSaveInstanceState(paramBundle);
  }

  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (this.filterBarFix != null)
    {
      if (paramInt1 >= this.shopListView.getHeaderViewsCount() - 1)
        break label40;
      if (this.filterBarFix.isShown())
        this.filterBarFix.setVisibility(8);
    }
    label40: 
    do
      return;
    while (this.filterBarFix.isShown());
    this.filterBarFix.setVisibility(0);
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
    if (paramInt == 0)
    {
      int i = this.shopListView.getFirstVisiblePosition();
      int j = this.shopListView.getLastVisiblePosition();
      int k = this.shopListView.getHeaderViewsCount();
      paramInt = i;
      while (paramInt <= j)
      {
        addGAView(this.shopListView.getChildAt(paramInt - i), paramInt - k);
        paramInt += 1;
      }
    }
  }

  protected void onSetContentView()
  {
  }

  public void reload(boolean paramBoolean)
  {
    if (getDataSource() != null)
      getDataSource().reload(paramBoolean);
  }

  protected void resetPageVisibility(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.errorView.setVisibility(8);
      if (!this.shopListView.isShown())
        this.shopListView.setVisibility(0);
    }
    do
    {
      return;
      this.shopListView.setVisibility(8);
      if (this.filterBarFix == null)
        continue;
      this.filterBarFix.setVisibility(8);
    }
    while (this.errorView.isShown());
    this.errorView.setVisibility(0);
  }

  protected void updateErrorView(String paramString, TakeawaySampleShoplistDataSource.ResultStatus paramResultStatus)
  {
    View localView = null;
    if ((paramResultStatus == TakeawaySampleShoplistDataSource.ResultStatus.ERROR_NETWORK) || (paramResultStatus == TakeawaySampleShoplistDataSource.ResultStatus.ERROR_LOCATE))
    {
      localView = getLayoutInflater().inflate(R.layout.takeaway_empty_item, null);
      ((TextView)localView.findViewById(16908308)).setText(paramString);
      paramString = (Button)localView.findViewById(R.id.btn_change);
      if (paramResultStatus == TakeawaySampleShoplistDataSource.ResultStatus.ERROR_NETWORK)
      {
        paramString.setText("重新加载");
        paramString.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            TakeawaySampleShopListActivity.this.getDataSource().reload(true);
            TakeawaySampleShopListActivity.this.updateErrorView("", TakeawaySampleShoplistDataSource.ResultStatus.INITIAL);
          }
        });
        paramString = localView;
      }
    }
    while (true)
    {
      this.errorView.removeAllViews();
      if (paramString != null)
        this.errorView.addView(paramString, new RelativeLayout.LayoutParams(-1, -1));
      return;
      paramString.setText("手动输入地址");
      paramString.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawaySampleShopListActivity.this.gotoAddressManager();
        }
      });
      paramString = localView;
      continue;
      paramString = localView;
      if (paramResultStatus != TakeawaySampleShoplistDataSource.ResultStatus.INITIAL)
        continue;
      paramString = getLayoutInflater().inflate(R.layout.loading_item_fullscreen, null);
    }
  }

  protected void updateFilterBarView()
  {
    if (this.filterBarFix != null)
      this.filterBarFix.updateFilter();
    if (this.filterBarFloat != null)
      this.filterBarFloat.updateFilter();
  }

  protected void updateShoplistHeaderView()
  {
  }

  protected void updateTitleBarView(String paramString)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawaySampleShopListActivity
 * JD-Core Version:    0.6.0
 */