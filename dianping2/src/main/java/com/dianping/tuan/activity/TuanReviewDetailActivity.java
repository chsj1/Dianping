package com.dianping.tuan.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TuanReviewListItem;
import com.dianping.base.widget.TuanTitleProgressView;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.City;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

public class TuanReviewDetailActivity extends BaseTuanPtrListActivity
{
  static final int MAX_PROGRESS = 10;
  int dealId;
  TableView headerLayout;
  View headerView;
  MyAdapter mAdapter;
  LinearLayout rInfoLayout;
  TextView rMTextView;
  TextView rvTextView;
  TextView tRTextView;

  private void addHeaderView()
  {
    this.headerView = LayoutInflater.from(this).inflate(R.layout.review_info_head_view, null, false);
    this.headerView.setVisibility(8);
    this.headerLayout = ((TableView)this.headerView.findViewById(R.id.header_layout));
    this.rvTextView = ((TextView)this.headerView.findViewById(R.id.recommend_value));
    this.rMTextView = ((TextView)this.headerView.findViewById(R.id.recommend_number));
    this.rInfoLayout = ((LinearLayout)this.headerView.findViewById(R.id.review_info));
    this.listView.addHeaderView(this.headerView, null, false);
    this.tRTextView = ((TextView)this.headerView.findViewById(R.id.total_review));
  }

  private void setupView()
  {
    this.listView.setBackgroundColor(getResources().getColor(R.color.common_bk_color));
    this.listView.setMode(PullToRefreshBase.Mode.DISABLED);
    addHeaderView();
    this.mAdapter = new MyAdapter(this);
    this.listView.setAdapter(this.mAdapter);
  }

  private void updateHeaderView(DPObject paramDPObject)
  {
    this.headerView.setVisibility(0);
    int i = paramDPObject.getInt("TotalReview");
    int j = paramDPObject.getInt("TotalRecommend");
    Object localObject = paramDPObject.getString("ReviewRatio");
    if ((i <= 10) || (j <= 0))
    {
      this.headerLayout.setVisibility(8);
      this.tRTextView.setVisibility(8);
      return;
    }
    if ((i > 0) && (localObject != null))
      this.rvTextView.setText((CharSequence)localObject);
    this.rMTextView.setText(j + "人");
    this.tRTextView.setText("共" + i + "个消费评价");
    this.rInfoLayout.removeAllViews();
    paramDPObject = paramDPObject.getArray("Items");
    if ((paramDPObject != null) && (paramDPObject.length > 0))
      i = 0;
    while (true)
    {
      double d1;
      if (i < paramDPObject.length)
      {
        localObject = new TuanTitleProgressView(this);
        ((TuanTitleProgressView)localObject).setTitle(paramDPObject[i].getString("Name"));
        ((TuanTitleProgressView)localObject).setContent(paramDPObject[i].getString("ID"));
        d1 = 0.0D;
      }
      try
      {
        double d2 = Double.parseDouble(paramDPObject[i].getString("ID"));
        d1 = d2;
        label231: ((TuanTitleProgressView)localObject).setProgress(10, (int)Math.round(d1));
        this.rInfoLayout.addView((View)localObject);
        i += 1;
        continue;
        this.rInfoLayout.setVisibility(0);
        return;
        this.rInfoLayout.setVisibility(8);
        return;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        break label231;
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.dealId = getIntParam("dealid");
    setupView();
  }

  public void onDestroy()
  {
    this.mAdapter.cancelLoad();
    super.onDestroy();
  }

  class MyAdapter extends BasicLoadAdapter
  {
    public MyAdapter(Context arg2)
    {
      super();
    }

    public MApiRequest createRequest(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://app.t.dianping.com/");
      localStringBuilder.append("reviewsgn.bin");
      localStringBuilder.append("?dealid=").append(TuanReviewDetailActivity.this.dealId);
      localStringBuilder.append("&start=").append(paramInt);
      localStringBuilder.append("&cityid=").append(TuanReviewDetailActivity.this.city().id());
      return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (TuanReviewDetailActivity.this.isDPObjectof(paramDPObject, "TuanReview"))
      {
        if ((paramView instanceof TuanReviewListItem));
        for (paramView = (TuanReviewListItem)paramView; ; paramView = null)
        {
          paramViewGroup = paramView;
          if (paramView == null)
            paramViewGroup = (TuanReviewListItem)LayoutInflater.from(TuanReviewDetailActivity.this).inflate(R.layout.tuan_review_list_item, null, false);
          paramViewGroup.setDealId(TuanReviewDetailActivity.this.dealId);
          paramViewGroup.showItem(paramDPObject);
          return paramViewGroup;
        }
      }
      return null;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        TuanReviewDetailActivity.this.updateHeaderView((DPObject)paramMApiResponse.result());
        if (Build.VERSION.SDK_INT < 11)
          TuanReviewDetailActivity.this.listView.postDelayed(new TuanReviewDetailActivity.MyAdapter.1(this), 100L);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanReviewDetailActivity
 * JD-Core Version:    0.6.0
 */