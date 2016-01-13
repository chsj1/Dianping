package com.dianping.search.shoplist.agent;

import android.view.View;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.search.shoplist.fragment.NearBySearchFragment;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;

public class ShopListNoKeywordSearchAgent extends ShopListSearchAgent
{
  private static final String CELL_SEARCH_BAR = "010SearchBar";

  public ShopListNoKeywordSearchAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected View createSearchKeywordBar()
  {
    View localView = super.createSearchKeywordBar();
    setBackBtnGone();
    return localView;
  }

  public void onClick(View paramView)
  {
    NearBySearchFragment localNearBySearchFragment = NearBySearchFragment.newInstance(getActivity(), ShopListUtils.getCategoryIdFromDataSource(getDataSource()));
    if (paramView.getId() == R.id.search_bar_clear)
      localNearBySearchFragment.setKeyword("");
    while (true)
    {
      localNearBySearchFragment.setOnSearchFragmentListener(this);
      GAHelper.instance().statisticsEvent(this.mKeywordLayout, "tap");
      return;
      if (paramView.getId() != R.id.keyword)
        continue;
      localNearBySearchFragment.setKeyword(this.mSearchKeyword);
    }
  }

  protected void setKeyword(String paramString)
  {
    if (this.mKeywordLayout == null)
      addCell("010SearchBar", createSearchKeywordBar());
    this.mKeywordView.setHint(R.string.default_search_hint);
    if ((getDataSource() != null) && (!android.text.TextUtils.isEmpty(getDataSource().keyWordInfo())))
      this.mKeywordView.setText(com.dianping.util.TextUtils.highLightShow(getContext(), getDataSource().keyWordInfo(), R.color.tuan_common_orange));
    while (true)
    {
      this.mKeywordLayout.setVisibility(0);
      ((NovaLinearLayout)this.mKeywordLayout).setGAString("search_box");
      return;
      this.mKeywordView.setText(paramString);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListNoKeywordSearchAgent
 * JD-Core Version:    0.6.0
 */