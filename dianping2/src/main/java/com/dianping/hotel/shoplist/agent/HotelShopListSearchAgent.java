package com.dianping.hotel.shoplist.agent;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.shoplist.activity.AbstractTabListActivity;
import com.dianping.base.shoplist.agent.ShopListBaseAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.ShopListConst;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.hotel.shoplist.fragement.HotelSearchSuggestFragment;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;

public class HotelShopListSearchAgent extends ShopListBaseAgent
  implements View.OnClickListener, AbstractSearchFragment.OnSearchFragmentListener
{
  private static final String CELL_SEARCH_BAR = "000TitleBar";
  private TextView mCountView;
  protected View mKeywordLayout;
  private TextView mKeywordView;
  protected String mSearchKeyword;

  public HotelShopListSearchAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setKeyword(String paramString)
  {
    if (!android.text.TextUtils.isEmpty(paramString))
    {
      if (this.mKeywordLayout == null)
      {
        View localView = inflater().inflate(R.layout.hotel_search_keyword_bar, getParentView(), false);
        this.mKeywordLayout = localView.findViewById(R.id.search_bar);
        this.mKeywordLayout.setOnClickListener(this);
        this.mKeywordView = ((TextView)localView.findViewById(R.id.keyword));
        this.mCountView = ((TextView)localView.findViewById(R.id.count));
        ((ImageView)localView.findViewById(R.id.search_bar_clear)).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            HotelSearchSuggestFragment.newInstance(HotelShopListSearchAgent.this.getActivity()).setOnSearchFragmentListener(HotelShopListSearchAgent.this);
          }
        });
        addCell("000TitleBar", localView);
      }
      if ((getDataSource() != null) && (!android.text.TextUtils.isEmpty(getDataSource().keyWordInfo())))
      {
        this.mKeywordView.setText(com.dianping.util.TextUtils.highLightShow(getContext(), getDataSource().keyWordInfo(), R.color.tuan_common_orange));
        statisticsEvent("shoplist5", "shoplist5_correct", "", 0);
        this.mKeywordLayout.setVisibility(0);
      }
    }
    do
    {
      return;
      this.mKeywordView.setText(paramString);
      break;
    }
    while (this.mKeywordLayout == null);
    this.mKeywordLayout.setVisibility(8);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getActivity() == null);
    do
    {
      do
        return;
      while (getDataSource() == null);
      if ((paramBundle != null) && (paramBundle.getBoolean("needSearch")))
      {
        HotelSearchSuggestFragment.newInstance(getActivity(), this.mSearchKeyword).setOnSearchFragmentListener(this);
        return;
      }
      this.mSearchKeyword = getDataSource().suggestKeyword();
      if ((paramBundle != null) && (paramBundle.getBoolean("onFocus")))
      {
        paramBundle = null;
        if ((getActivity() instanceof AbstractTabListActivity))
          paramBundle = ((AbstractTabListActivity)getActivity()).getKeyword();
        if ((android.text.TextUtils.isEmpty(this.mSearchKeyword)) && (android.text.TextUtils.isEmpty(paramBundle)))
        {
          refreshSearchResult(this.mSearchKeyword, false);
          return;
        }
        if ((!android.text.TextUtils.isEmpty(this.mSearchKeyword)) && (this.mSearchKeyword.equals(paramBundle)))
        {
          refreshSearchResult(this.mSearchKeyword, false);
          return;
        }
        refreshSearchResult(paramBundle, true);
        return;
      }
      if ((!android.text.TextUtils.isEmpty(this.mSearchKeyword)) && ((getActivity() instanceof AbstractTabListActivity)))
        ((AbstractTabListActivity)getActivity()).setKeyword(this.mSearchKeyword);
      setKeyword(this.mSearchKeyword);
    }
    while (getDataSource().status() != 2);
    Log.d("debug_ga_tab", "shoplist5_tab_" + getGATag() + " bundle=" + paramBundle);
    statisticsEvent("shoplist5", "shoplist5_tab_" + getGATag(), "全部商户", 0);
  }

  public void onClick(View paramView)
  {
    HotelSearchSuggestFragment.newInstance(getActivity(), this.mSearchKeyword).setOnSearchFragmentListener(this);
  }

  public void onSearchFragmentDetach()
  {
    GAHelper.instance().setGAPageName("shoplist");
  }

  protected void refreshSearchResult(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      getDataSource().setCurRange(null);
      getDataSource().setCurRegion(ShopListConst.ALL_REGION);
      getDataSource().setCurSort(null);
      getDataSource().setCurSelectNav(null);
      getDataSource().setMaxPrice(-1);
      getDataSource().setMinPrice(-1);
      getDataSource().setCityId(0);
    }
    if (paramString != null)
    {
      if ((paramString.equals("清除搜索记录")) || (paramString.equals("清空搜索记录")))
      {
        new SearchRecentSuggestions(getActivity(), "com.dianping.app.DianpingSuggestionProvider", 3).clearHistory();
        return;
      }
      if ((getActivity() != null) && (getActivity().getIntent() != null))
      {
        getActivity().getIntent().putExtra("keyword", paramString);
        new SearchRecentSuggestions(getActivity(), "com.dianping.app.DianpingSuggestionProvider", 3).saveRecentQuery(paramString, null);
      }
    }
    if ((getActivity() instanceof AbstractTabListActivity))
      ((AbstractTabListActivity)getActivity()).setKeyword(paramString);
    if (android.text.TextUtils.isEmpty(paramString))
    {
      getDataSource().setSuggestKeyword("");
      getDataSource().setSuggestValue("");
      if (((getFragment() instanceof AbstractShopListAgentFragment)) && (((AbstractShopListAgentFragment)getFragment()).isCurrentCity()))
      {
        if ((getDataSource().curRegion() == null) || (getDataSource().curRegion() == ShopListConst.ALL_REGION))
          getDataSource().setCurRegion(ShopListConst.NEARBY);
        getDataSource().setNeedLocalRegion(true);
      }
      if ((getFragment() instanceof AbstractShopListAgentFragment))
        ((AbstractShopListAgentFragment)getFragment()).GATag = "nearby";
    }
    while (true)
    {
      setKeyword(paramString);
      getDataSource().reset(true);
      getDataSource().reload(false);
      return;
      getDataSource().setSuggestKeyword(paramString);
      getDataSource().setNeedLocalRegion(false);
      if (!(getFragment() instanceof AbstractShopListAgentFragment))
        continue;
      ((AbstractShopListAgentFragment)getFragment()).GATag = "search";
    }
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    String str = paramDPObject.getString("Value");
    getDataSource().setSuggestValue(str);
    if (!android.text.TextUtils.isEmpty(str))
    {
      getDataSource().setCurRegion(ShopListConst.ALL_REGION);
      getDataSource().setPlaceType(2);
    }
    paramDPObject = paramDPObject.getString("Keyword");
    refreshSearchResult(paramDPObject, true);
    statisticsEvent("shoplist5", "shoplist5_keyword_keyword", paramDPObject, 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.agent.HotelShopListSearchAgent
 * JD-Core Version:    0.6.0
 */