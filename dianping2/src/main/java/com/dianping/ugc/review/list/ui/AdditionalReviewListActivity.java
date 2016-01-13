package com.dianping.ugc.review.list.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.dianping.base.app.NovaActivity;

public class AdditionalReviewListActivity extends NovaActivity
{
  private ReviewListFragment mListView;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    int i = getIntParam("id");
    int j = getIntParam("userid");
    if (paramBundle == null)
    {
      this.mListView = new ReviewListFragment();
      paramBundle = getSupportFragmentManager().beginTransaction();
      paramBundle.add(16908290, this.mListView, "ReviewListFragment");
      paramBundle.commitAllowingStateLoss();
    }
    while (true)
    {
      this.mListView.setShopId(i);
      this.mListView.setUserId(j);
      return;
      this.mListView = ((ReviewListFragment)getSupportFragmentManager().findFragmentByTag("ReviewListFragment"));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.list.ui.AdditionalReviewListActivity
 * JD-Core Version:    0.6.0
 */