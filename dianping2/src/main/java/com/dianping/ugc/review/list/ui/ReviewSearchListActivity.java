package com.dianping.ugc.review.list.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.ugc.review.ReviewShop;
import com.dianping.base.ugc.review.fragment.ReviewSearchFragment;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ButtonSearchBar.ButtonSearchBarListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.id;

public class ReviewSearchListActivity extends NovaActivity
  implements ReviewShop
{
  private ReviewListFragment mListView;
  private String mSearchKeyword;
  private int mShopId;

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 5);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mShopId = getIntParam("shopid");
    this.mSearchKeyword = getStringParam("keyword");
    if (paramBundle == null)
    {
      this.mListView = new ReviewListFragment();
      paramBundle = getSupportFragmentManager().beginTransaction();
      paramBundle.add(16908290, this.mListView, "ReviewListFragment");
      paramBundle.commitAllowingStateLoss();
    }
    while (true)
    {
      this.mListView.setShopId(this.mShopId);
      this.mListView.setKeyword(this.mSearchKeyword);
      this.mListView.setNeedFilter(false);
      this.mListView.setFilterId(0);
      paramBundle = (ButtonSearchBar)findViewById(R.id.button_search_bar);
      if (paramBundle != null)
      {
        paramBundle.setKeyword(this.mSearchKeyword);
        paramBundle.setButtonSearchBarListener(new ButtonSearchBar.ButtonSearchBarListener(paramBundle)
        {
          public void onSearchRequested()
          {
            ReviewSearchListActivity.this.hideTitleBar();
            ReviewSearchFragment localReviewSearchFragment = ReviewSearchFragment.newInstance(ReviewSearchListActivity.this);
            localReviewSearchFragment.setKeyword(ReviewSearchListActivity.this.mSearchKeyword);
            localReviewSearchFragment.setOnSearchFragmentListener(new ReviewSearchListActivity.1.1(this));
          }
        });
      }
      return;
      this.mListView = ((ReviewListFragment)getSupportFragmentManager().findFragmentByTag("ReviewListFragment"));
    }
  }

  public DPObject shop()
  {
    return null;
  }

  public int shopId()
  {
    return this.mShopId;
  }

  public String shopName()
  {
    return null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.list.ui.ReviewSearchListActivity
 * JD-Core Version:    0.6.0
 */