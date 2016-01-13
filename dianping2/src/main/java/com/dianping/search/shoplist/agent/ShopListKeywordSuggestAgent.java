package com.dianping.search.shoplist.agent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;
import com.dianping.base.shoplist.agent.ShopListHeaderAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ShopListKeywordSuggestAgent extends ShopListHeaderAgent
{
  private View keywordLayout;
  private TextView keywordTextView;

  public ShopListKeywordSuggestAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void updateKeyWordInfoView()
  {
    if ((android.text.TextUtils.isEmpty(getDataSource().keyWordInfo())) || (getDataSource().targetType == 2))
    {
      if (this.keywordLayout != null)
      {
        this.keywordLayout.setVisibility(8);
        this.keywordTextView.setVisibility(8);
      }
      return;
    }
    if (this.keywordLayout == null)
    {
      this.keywordLayout = LayoutInflater.from(getActivity()).inflate(R.layout.keyword_layout, getParentView(), false);
      this.keywordLayout.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(getContext(), 0.0F)));
      addListHeader(this.keywordLayout);
    }
    this.keywordTextView = ((TextView)this.keywordLayout.findViewById(R.id.keyword_suggest_tv));
    this.keywordTextView.setText(com.dianping.util.TextUtils.highLightShow(getContext(), getDataSource().keyWordInfo(), R.color.tuan_common_orange));
    this.keywordLayout.setVisibility(0);
    this.keywordTextView.setVisibility(0);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getDataSource() == null);
    do
      return;
    while ((getDataSource().startIndex() != 0) || (!fetchListView()));
    updateKeyWordInfoView();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListKeywordSuggestAgent
 * JD-Core Version:    0.6.0
 */