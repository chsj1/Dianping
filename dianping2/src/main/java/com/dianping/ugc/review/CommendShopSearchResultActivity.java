package com.dianping.ugc.review;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ButtonSearchBar.ButtonSearchBarListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class CommendShopSearchResultActivity extends NovaActivity
  implements AbstractSearchFragment.OnSearchFragmentListener
{
  private CommendShopSearchResultFragment commendShopSearchResultFragment;
  private String keyword = "";
  private ButtonSearchBar mSearchBar;
  private CommendShopSearchFragment searchFragment;

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 5);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.commend_shop_search_result);
    this.commendShopSearchResultFragment = ((CommendShopSearchResultFragment)getSupportFragmentManager().findFragmentById(R.id.shop_list_fragment));
    this.mSearchBar = ((ButtonSearchBar)findViewById(R.id.button_search_bar));
    if (this.mSearchBar != null)
    {
      paramBundle = getIntent().getStringExtra("keyword");
      this.mSearchBar.setKeyword(paramBundle);
      this.mSearchBar.setButtonSearchBarListener(new ButtonSearchBar.ButtonSearchBarListener()
      {
        public void onSearchRequested()
        {
          CommendShopSearchResultActivity.access$002(CommendShopSearchResultActivity.this, CommendShopSearchFragment.newInstance(CommendShopSearchResultActivity.this));
          CommendShopSearchResultActivity.this.searchFragment.setKeyword(CommendShopSearchResultActivity.this.keyword);
          CommendShopSearchResultActivity.this.searchFragment.setOnSearchFragmentListener(CommendShopSearchResultActivity.this);
          CommendShopSearchResultActivity.this.setTitleVisibility(8);
        }
      });
    }
    this.keyword = getIntent().getStringExtra("keyword");
    this.commendShopSearchResultFragment.setKeyword(this.keyword);
  }

  public void onSearchFragmentDetach()
  {
    setTitleVisibility(0);
  }

  public void startSearch(DPObject paramDPObject)
  {
    this.keyword = paramDPObject.getString("Keyword");
    this.commendShopSearchResultFragment.setKeyword(this.keyword);
    if (!TextUtils.isEmpty(this.keyword))
    {
      this.mSearchBar.setKeyword(this.keyword);
      this.commendShopSearchResultFragment.refresh();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.CommendShopSearchResultActivity
 * JD-Core Version:    0.6.0
 */