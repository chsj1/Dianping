package com.dianping.ugc.review;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.model.Location;
import java.text.DecimalFormat;

public class CommendShopSearchResultFragment extends CommendShopListFragment
{
  DecimalFormat FMT = Location.FMT;
  private double firstLat = 0.0D;
  private double firstLng = 0.0D;
  private String keyword;

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.source = "commendshopsearchresult";
    this.adapter = new CommendShopSearchResultFragment.CommendShopSearchResultAdapter(this);
    setListAdapter(this.adapter);
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    paramListView = this.adapter.getItem(paramInt);
    if ((paramListView instanceof DPObject))
    {
      paramListView = (DPObject)paramListView;
      AddReviewUtil.addReview(getActivity(), paramListView.getInt("ID"), DPObjectUtils.getShopFullName(paramListView), this.source, null);
      statisticsEvent("commendreview5", "commendreview5_keyword_item ", "" + paramListView.getInt("ID"), 0);
    }
  }

  public void refresh()
  {
    this.adapter.reset();
  }

  public void setKeyword(String paramString)
  {
    this.keyword = paramString;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.CommendShopSearchResultFragment
 * JD-Core Version:    0.6.0
 */