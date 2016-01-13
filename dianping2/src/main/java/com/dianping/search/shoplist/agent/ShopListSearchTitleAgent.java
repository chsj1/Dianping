package com.dianping.search.shoplist.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.basic.MainSearchFragment;
import com.dianping.base.shoplist.agent.ShopListAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.ShopListConst;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ButtonSearchBar.ButtonSearchBarListener;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ShopListSearchTitleAgent extends ShopListAgent
  implements AbstractSearchFragment.OnSearchFragmentListener, ButtonSearchBar.ButtonSearchBarListener
{
  private static final String CELL_TITLE_BAR = "000TitleBar";
  private View contentView;
  private ButtonSearchBar mSearchBar;
  private String mSearchKeyword = null;

  public ShopListSearchTitleAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setupTitle()
  {
    this.contentView = inflater().inflate(R.layout.shop_list_search_title_bar, getParentView(), false);
    this.mSearchBar = ((ButtonSearchBar)this.contentView.findViewById(R.id.button_search_bar));
    Object localObject2 = getActivity().getIntent().getStringExtra("keyword");
    Object localObject1 = localObject2;
    if (localObject2 == null)
      localObject1 = getActivity().getIntent().getData().getQueryParameter("keyword");
    localObject2 = localObject1;
    if (localObject1 == null)
      localObject2 = getActivity().getIntent().getData().getQueryParameter("q");
    this.mSearchKeyword = ((String)localObject2);
    this.mSearchBar.setKeyword((String)localObject2);
    this.mSearchBar.setHint("输入商户名搜索");
    this.mSearchBar.setButtonSearchBarListener(this);
    this.contentView.findViewById(R.id.left_title_button).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ShopListSearchTitleAgent.this.getActivity().onBackPressed();
      }
    });
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.contentView == null)
    {
      setupTitle();
      addCell("000TitleBar", this.contentView);
    }
  }

  public void onSearchFragmentDetach()
  {
  }

  public void onSearchRequested()
  {
    MainSearchFragment localMainSearchFragment = MainSearchFragment.newInstance(getActivity(), ShopListUtils.getCategoryIdFromDataSource(getDataSource()));
    localMainSearchFragment.setKeyword(this.mSearchKeyword);
    localMainSearchFragment.setOnSearchFragmentListener(this);
  }

  protected void refreshSearchResult(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    if (paramString.equals("清空搜索记录"))
    {
      new SearchRecentSuggestions(getActivity(), "com.dianping.app.DianpingSuggestionProvider", 3).clearHistory();
      return;
    }
    if ((getActivity() != null) && (getActivity().getIntent() != null))
    {
      getActivity().getIntent().putExtra("keyword", paramString);
      new SearchRecentSuggestions(getActivity(), "com.dianping.app.DianpingSuggestionProvider", 3).saveRecentQuery(paramString, null);
    }
    if (this.mSearchBar != null)
      this.mSearchBar.setKeyword(paramString);
    getDataSource().reset(true);
    getDataSource().reload(false);
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.mSearchKeyword = paramDPObject.getString("Keyword");
    String str = paramDPObject.getString("Value");
    getDataSource().setSuggestKeyword(this.mSearchKeyword);
    getDataSource().setSuggestValue(str);
    getDataSource().setCurRange(null);
    getDataSource().setCurCategory(null);
    getDataSource().setCurRegion(null);
    getDataSource().setCurSort(null);
    getDataSource().setCurSelectNav(null);
    getDataSource().setMaxPrice(-1);
    getDataSource().setMinPrice(-1);
    getDataSource().setCityId(0);
    getDataSource().setFilterNavi(null);
    if (!TextUtils.isEmpty(str))
    {
      getDataSource().setCurRegion(ShopListConst.ALL_REGION);
      getDataSource().setPlaceType(2);
    }
    refreshSearchResult(paramDPObject.getString("Keyword"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListSearchTitleAgent
 * JD-Core Version:    0.6.0
 */