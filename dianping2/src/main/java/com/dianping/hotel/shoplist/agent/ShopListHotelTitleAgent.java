package com.dianping.hotel.shoplist.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.shoplist.activity.AbstractTabListActivity;
import com.dianping.base.shoplist.agent.ShopListBaseAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ButtonSearchBar.ButtonSearchBarListener;
import com.dianping.base.widget.ShopAndDealListItem;
import com.dianping.hotel.shoplist.fragement.HotelSearchSuggestFragment;
import com.dianping.hotel.shoplist.widget.HotelShopListTabView;
import com.dianping.hotel.shoplist.widget.HotelShopListTabView.TabChangeListener;
import com.dianping.model.City;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.util.LinkedList;

public class ShopListHotelTitleAgent extends ShopListBaseAgent
  implements ButtonSearchBar.ButtonSearchBarListener, HotelShopListTabView.TabChangeListener, AbstractSearchFragment.OnSearchFragmentListener
{
  public static final DPObject ALL_REGION = new DPObject("Region").edit().putInt("ID", 0).putString("Name", "全部商区").putInt("ParentID", 0).generate();
  public static final String CELL_HOTEL_TITLE_BAR = "000HotelTitleBar";
  private View contentView;
  private BaseShopListDataSource dataSource;
  private boolean isSelectedDeal;
  private ButtonSearchBar searchBar;
  private HotelSearchSuggestFragment searchFragment;
  private HotelShopListTabView tabBar;
  private TextView titleView;

  public ShopListHotelTitleAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void refreshSearchResult(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    do
    {
      return;
      if (paramString.equals("清空搜索记录"))
      {
        new SearchRecentSuggestions(getContext(), "com.dianping.app.DianpingSuggestionProvider", 3).clearHistory();
        return;
      }
      getActivity().getIntent().putExtra("keyword", paramString);
      new SearchRecentSuggestions(getContext(), "com.dianping.app.DianpingSuggestionProvider", 3).saveRecentQuery(paramString, null);
      this.dataSource.reset(true);
      this.dataSource.reload(false);
    }
    while (getActivity() == null);
    Intent localIntent = getActivity().getIntent();
    localIntent.putExtra("keyword", paramString);
    getActivity().setResult(-1, localIntent);
  }

  private void setupTitle()
  {
    this.contentView = inflater().inflate(R.layout.shop_list_hotel_title_bar, getParentView(), false);
    this.contentView.findViewById(R.id.left_view).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ShopListHotelTitleAgent.this.getActivity().onBackPressed();
      }
    });
    this.searchBar = ((ButtonSearchBar)this.contentView.findViewById(R.id.button_search_bar));
    if (TextUtils.isEmpty(getDataSource().suggestKeyword()))
    {
      ((ImageView)this.searchBar.findViewById(R.id.search_icon)).setImageResource(R.drawable.navibar_icon_search);
      this.searchBar.setButtonSearchBarListener(this);
      this.tabBar = ((HotelShopListTabView)this.contentView.findViewById(R.id.title_bar_tab));
      this.tabBar.setLeftTitleText("商户");
      if ((city().flag() & 0x8000000) == 0)
        break label248;
      this.tabBar.setRightTitleText("精选");
      this.isSelectedDeal = true;
      label150: this.tabBar.setTabChangeListener(this);
      this.titleView = ((TextView)this.contentView.findViewById(R.id.title_bar_title));
      if (!city().isTuan())
        break label266;
      this.tabBar.setVisibility(0);
      this.titleView.setVisibility(8);
      if (getDataSource() != null)
        this.tabBar.setCurIndex(getDataSource().hotelTabIndex, true);
    }
    while (true)
    {
      addCell("000HotelTitleBar", this.contentView);
      return;
      this.searchBar.setVisibility(8);
      break;
      label248: this.tabBar.setRightTitleText("团购");
      this.isSelectedDeal = false;
      break label150;
      label266: this.titleView.setVisibility(0);
      this.tabBar.setVisibility(8);
    }
  }

  protected void gotoSearch()
  {
    statisticsEvent("hotelkwlist5", "hotelkwlist5_keyword_click", "", 0);
    this.searchFragment = HotelSearchSuggestFragment.newInstance(getActivity(), null);
    this.searchFragment.setOnSearchFragmentListener(this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.dataSource = getDataSource();
    if (this.dataSource == null);
    do
      return;
    while (this.contentView != null);
    paramBundle = getActivity();
    if ((paramBundle instanceof AbstractTabListActivity))
      ((AbstractTabListActivity)paramBundle).showTabTitle(false);
    setupTitle();
  }

  public void onSearchFragmentDetach()
  {
  }

  public void onSearchRequested()
  {
    gotoSearch();
  }

  public void onTabChanged(int paramInt)
  {
    int i = 2;
    Object localObject = new GAUserInfo();
    ((GAUserInfo)localObject).category_id = Integer.valueOf(this.dataSource.curCategory().getInt("ID"));
    ((GAUserInfo)localObject).query_id = this.dataSource.queryId();
    if (paramInt == 0)
    {
      GAHelper.instance().contextStatisticsEvent(getContext(), "hotellist_tab_shop", (GAUserInfo)localObject, "tap");
      statisticsEvent("hotellist5", "hotellist5_tab", "全部商户", 0);
      ShopAndDealListItem.records.clear();
      this.dataSource.hotelTabIndex = paramInt;
      this.dataSource.setCurrentTabIndex(paramInt);
      this.dataSource.reset(true);
      this.dataSource.reload(true);
      if ((this.searchBar != null) && (TextUtils.isEmpty(getDataSource().suggestKeyword())))
        this.searchBar.setVisibility(0);
    }
    while (true)
    {
      dispatchAgentChanged("shoplist/hotelcontentlist", null);
      dispatchAgentChanged("shoplist/hotelinfo", null);
      return;
      if (paramInt != 1)
        continue;
      GAHelper.instance().contextStatisticsEvent(getContext(), "hotellist_tab_tg", (GAUserInfo)localObject, "tap");
      statisticsEvent("hotellist5", "hotellist5_tab", "只看团购", 0);
      ShopAndDealListItem.records.clear();
      localObject = this.dataSource;
      if (this.isSelectedDeal)
      {
        paramInt = 2;
        label214: ((BaseShopListDataSource)localObject).hotelTabIndex = paramInt;
        localObject = this.dataSource;
        if (!this.isSelectedDeal)
          break label297;
      }
      label297: for (paramInt = i; ; paramInt = 1)
      {
        ((BaseShopListDataSource)localObject).setCurrentTabIndex(paramInt);
        this.dataSource.reset(true);
        this.dataSource.reload(true);
        if (this.searchBar == null)
          break;
        if ((this.isSelectedDeal) || (!TextUtils.isEmpty(getDataSource().suggestKeyword())))
          break label302;
        this.searchBar.setVisibility(0);
        break;
        paramInt = 1;
        break label214;
      }
      label302: this.searchBar.setVisibility(8);
    }
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    Object localObject1 = paramDPObject.getString("Url");
    if (!TextUtils.isEmpty((CharSequence)localObject1))
    {
      startActivity((String)localObject1);
      return;
    }
    localObject1 = new StringBuilder("dianping://searchshoplist");
    ((StringBuilder)localObject1).append("?hoteltab=").append(getDataSource().hotelTabIndex);
    String str = paramDPObject.getString("Keyword");
    if (!TextUtils.isEmpty(str))
      ((StringBuilder)localObject1).append("&keyword=").append(str);
    Object localObject2 = paramDPObject.getString("SearchValue");
    if (!TextUtils.isEmpty((CharSequence)localObject2))
      ((StringBuilder)localObject1).append("&searchvalue=").append((String)localObject2);
    str = paramDPObject.getString("Value");
    if ((!TextUtils.isEmpty((CharSequence)localObject2)) || (!TextUtils.isEmpty(str)))
      ((StringBuilder)localObject1).append("&placetype=").append(2);
    ((StringBuilder)localObject1).append("&categoryid=60");
    localObject2 = getActivity().getIntent().getData();
    if (localObject2 != null)
    {
      localObject2 = ((Uri)localObject2).getQueryParameter("searcharoundcities");
      if (!TextUtils.isEmpty((CharSequence)localObject2))
        ((StringBuilder)localObject1).append("&searcharoundcities=").append(Boolean.parseBoolean((String)localObject2));
    }
    ((StringBuilder)localObject1).append("&isFromSearch=").append("true");
    ((StringBuilder)localObject1).append("&begindate=").append(String.valueOf(getDataSource().hotelCheckinTime()));
    ((StringBuilder)localObject1).append("&enddate=").append(String.valueOf(getDataSource().hotelCheckoutTime()));
    localObject2 = new Intent("android.intent.action.VIEW", Uri.parse(((StringBuilder)localObject1).toString()));
    if (!TextUtils.isEmpty(str))
    {
      ((Intent)localObject2).putExtra("value", str);
      ((StringBuilder)localObject1).append("&value=").append(str);
    }
    statisticsEvent("hotelkwlist5", "hotelkwlist5_keyword", paramDPObject.getString("Keyword"), 0);
    paramDPObject = new GAUserInfo();
    paramDPObject.category_id = Integer.valueOf(this.dataSource.curCategory().getInt("ID"));
    paramDPObject.query_id = this.dataSource.queryId();
    paramDPObject.keyword = this.dataSource.keyWordInfo();
    GAHelper.instance().contextStatisticsEvent(getContext(), "hotellist_search", paramDPObject, "tap");
    startActivity((Intent)localObject2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.agent.ShopListHotelTitleAgent
 * JD-Core Version:    0.6.0
 */