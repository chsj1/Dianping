package com.dianping.ugc.review;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;

public class CommendAddReviewActivity extends NovaActivity
  implements AbstractSearchFragment.OnSearchFragmentListener
{
  void gotoSearch()
  {
    CommendShopSearchFragment.newInstance(this).setOnSearchFragmentListener(this);
    setTitleVisibility(8);
    statisticsEvent("commendreview5", "commendreview5_keyword", "", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setRightTitleButton(R.drawable.navibar_icon_search, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        CommendAddReviewActivity.this.gotoSearch();
      }
    });
    super.setContentView(R.layout.commend_add_review);
  }

  public void onSearchFragmentDetach()
  {
    setTitleVisibility(0);
  }

  public void startSearch(DPObject paramDPObject)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("dianping://commendshopsearchresult?keyword=" + paramDPObject));
    localIntent.putExtra("keyword", paramDPObject.getString("Keyword"));
    startActivity(localIntent);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.CommendAddReviewActivity
 * JD-Core Version:    0.6.0
 */