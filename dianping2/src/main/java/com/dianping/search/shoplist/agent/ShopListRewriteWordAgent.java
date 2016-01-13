package com.dianping.search.shoplist.agent;

import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.LayoutParams;
import com.dianping.base.shoplist.agent.ShopListHeaderAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.util.CustomClickSpan;
import com.dianping.search.util.CustomClickSpan.SpanClick;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaTextView;

public class ShopListRewriteWordAgent extends ShopListHeaderAgent
  implements CustomClickSpan.SpanClick
{
  private static final String CELL_REWRITEWORD = "011Rewriteword";
  private NewShopListDataSource dataSource;
  NovaTextView mKeywordView;
  View mRewritewordLayout;
  String mSearchKeyword;

  public ShopListRewriteWordAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected View createSearchKeywordBar()
  {
    View localView = inflater().inflate(R.layout.search_rewriteword, getParentView(), false);
    this.mRewritewordLayout = localView.findViewById(R.id.rewriteword_layout);
    AbsListView.LayoutParams localLayoutParams = new AbsListView.LayoutParams(-1, -2);
    this.mRewritewordLayout.setLayoutParams(localLayoutParams);
    this.mKeywordView = ((NovaTextView)localView.findViewById(R.id.rewriteword));
    this.mKeywordView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        try
        {
          paramView = (NovaTextView)paramView;
          Object localObject = paramView.getText();
          if ((localObject instanceof Spanned))
          {
            localObject = (Spanned)localObject;
            int i = paramMotionEvent.getAction();
            if ((i == 1) || (i == 0))
            {
              int j = (int)paramMotionEvent.getX();
              int k = (int)paramMotionEvent.getY();
              int m = paramView.getTotalPaddingLeft();
              int n = paramView.getTotalPaddingTop();
              int i1 = paramView.getScrollX();
              int i2 = paramView.getScrollY();
              paramMotionEvent = paramView.getLayout();
              j = paramMotionEvent.getOffsetForHorizontal(paramMotionEvent.getLineForVertical(k - n + i2), j - m + i1);
              paramMotionEvent = (ClickableSpan[])((Spanned)localObject).getSpans(j, j, ClickableSpan.class);
              if ((paramMotionEvent.length != 0) && (i == 1))
                paramMotionEvent[0].onClick(paramView);
            }
          }
          return true;
        }
        catch (java.lang.Exception paramView)
        {
        }
        return true;
      }
    });
    addListHeader(localView);
    return localView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getActivity() == null) || (!fetchListView()));
    do
    {
      do
        return;
      while (!(getDataSource() instanceof NewShopListDataSource));
      this.dataSource = ((NewShopListDataSource)getDataSource());
      if (this.mRewritewordLayout == null)
        createSearchKeywordBar();
      this.mSearchKeyword = getDataSource().keyWordInfo();
      this.mKeywordView.setVisibility(8);
    }
    while ((TextUtils.isEmpty(this.mSearchKeyword)) || (TextUtils.isEmpty(getDataSource().suggestKeyword())));
    paramBundle = new CustomClickSpan(this);
    SpannableStringBuilder localSpannableStringBuilder = paramBundle.makePartTextClick(getActivity(), this.mSearchKeyword, R.color.light_red, R.color.editprofile_blue);
    this.mKeywordView.setText(localSpannableStringBuilder);
    if (paramBundle.gaType() == 1)
      this.mKeywordView.setGAString("auto_rewrite");
    while (true)
    {
      this.mKeywordView.gaUserInfo.keyword = getDataSource().suggestKeyword();
      GAHelper.instance().statisticsEvent(this.mKeywordView, "view");
      this.mKeywordView.setMovementMethod(LinkMovementMethod.getInstance());
      this.mKeywordView.setFocusable(false);
      this.mKeywordView.setHighlightColor(0);
      this.mKeywordView.setVisibility(0);
      return;
      if (paramBundle.gaType() == 0)
      {
        this.mKeywordView.setGAString("advised_rewrite");
        continue;
      }
      this.mKeywordView.setGAString("null_rewrite");
    }
  }

  public void onSpanClick(String paramString, char paramChar)
  {
    int i = 0;
    if (paramChar == '1')
      i = 1;
    this.mKeywordView.gaUserInfo.keyword = paramString;
    GAHelper.instance().statisticsEvent(this.mKeywordView, "tap");
    this.dataSource.setSuggestKeyword(paramString);
    this.dataSource.disableRewrite = i;
    this.dataSource.reset(true);
    this.dataSource.reload(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListRewriteWordAgent
 * JD-Core Version:    0.6.0
 */