package com.dianping.hotel.ugc;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TuanReviewListItem;
import com.dianping.base.widget.TuanTitleProgressView;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.City;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;

public class HotelTuanReviewDetailFragment extends NovaFragment
  implements AdapterView.OnItemClickListener
{
  static final int MAX_PROGRESS = 10;
  int dealId;
  private TextView emptyTextView;
  private View emptyView;
  TableView headerLayout;
  View headerView;
  protected PullToRefreshListView listView;
  MyAdapter mAdapter;
  LinearLayout rInfoLayout;
  TextView rMTextView;
  TextView rvTextView;
  TextView tRTextView;

  private void addHeaderView()
  {
    this.headerView = LayoutInflater.from(getActivity()).inflate(R.layout.review_info_head_view, null, false);
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
    this.mAdapter = new MyAdapter(getActivity());
    this.listView.setAdapter(this.mAdapter);
    this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        if (paramInt < 2)
          return;
        int i = paramInt - 2;
        paramAdapterView = HotelTuanReviewDetailFragment.MyAdapter.access$000(HotelTuanReviewDetailFragment.this.mAdapter);
        if (((Integer)HotelTuanReviewDetailFragment.MyAdapter.access$000(HotelTuanReviewDetailFragment.this.mAdapter).get(i)).intValue() > 0);
        for (paramInt = 0; ; paramInt = 1)
        {
          paramAdapterView.set(i, Integer.valueOf(paramInt));
          HotelTuanReviewDetailFragment.this.mAdapter.notifyDataSetChanged();
          return;
        }
      }
    });
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
        localObject = new TuanTitleProgressView(getActivity());
        ((TuanTitleProgressView)localObject).setTitle(paramDPObject[i].getString("Name"));
        ((TuanTitleProgressView)localObject).setContent(paramDPObject[i].getString("ID"));
        d1 = 0.0D;
      }
      try
      {
        double d2 = Double.parseDouble(paramDPObject[i].getString("ID"));
        d1 = d2;
        label234: ((TuanTitleProgressView)localObject).setProgress(10, (int)Math.round(d1));
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
        break label234;
      }
    }
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.dealId = ((DPActivity)getActivity()).getIntParam("dealId");
    setupView();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = (LinearLayout)paramLayoutInflater.inflate(R.layout.tuan_ptr_list_frame, null);
    this.listView = ((PullToRefreshListView)paramLayoutInflater.findViewById(R.id.list));
    if (this.listView == null)
      this.listView = ((PullToRefreshListView)paramLayoutInflater.findViewById(16908298));
    this.emptyView = paramLayoutInflater.findViewById(R.id.empty);
    this.emptyTextView = ((TextView)paramLayoutInflater.findViewById(R.id.empty_textview));
    this.listView.setSelector(R.drawable.list_item);
    this.listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
    this.listView.setDivider(getResources().getDrawable(R.drawable.list_divider_right_inset));
    this.listView.setFastScrollEnabled(true);
    this.listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
      {
        HotelTuanReviewDetailFragment.this.onPullToRefresh();
      }
    });
    this.listView.setOnItemClickListener(this);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.mAdapter.cancelLoad();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
  }

  protected void onPullToRefresh()
  {
  }

  protected void setEmpty(String paramString)
  {
    if (this.emptyView != null)
      this.listView.setEmptyView(this.emptyView);
    if (this.emptyTextView != null)
      this.emptyTextView.setText(paramString);
  }

  class MyAdapter extends BasicLoadAdapter
  {
    private List<Integer> statusList = new ArrayList();

    public MyAdapter(Context arg2)
    {
      super();
    }

    public void appendData(DPObject paramDPObject)
    {
      if ((DPObjectUtils.isDPObjectof(paramDPObject)) && (paramDPObject.getArray("List") != null))
      {
        int i = 0;
        while (i < paramDPObject.getArray("List").length)
        {
          this.statusList.add(Integer.valueOf(0));
          i += 1;
        }
      }
      super.appendData(paramDPObject);
    }

    public MApiRequest createRequest(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://app.t.dianping.com/");
      localStringBuilder.append("reviewsgn.bin");
      localStringBuilder.append("?dealid=").append(HotelTuanReviewDetailFragment.this.dealId);
      localStringBuilder.append("&start=").append(paramInt);
      localStringBuilder.append("&cityid=").append(HotelTuanReviewDetailFragment.this.city().id());
      return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (HotelTuanReviewDetailFragment.this.isDPObjectof(paramDPObject, "TuanReview"))
      {
        if ((paramView instanceof TuanReviewListItem));
        for (paramView = (TuanReviewListItem)paramView; ; paramView = null)
        {
          paramViewGroup = paramView;
          if (paramView == null)
            paramViewGroup = (TuanReviewListItem)LayoutInflater.from(HotelTuanReviewDetailFragment.this.getActivity()).inflate(R.layout.tuan_review_list_item, null, false);
          paramViewGroup.showItem(paramDPObject);
          return paramViewGroup;
        }
      }
      return null;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
        HotelTuanReviewDetailFragment.this.updateHeaderView((DPObject)paramMApiResponse.result());
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      if (Build.VERSION.SDK_INT < 11)
        HotelTuanReviewDetailFragment.this.listView.postDelayed(new Runnable()
        {
          public void run()
          {
            if (HotelTuanReviewDetailFragment.this.mAdapter != null)
              HotelTuanReviewDetailFragment.this.mAdapter.notifyDataSetChanged();
          }
        }
        , 100L);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.ugc.HotelTuanReviewDetailFragment
 * JD-Core Version:    0.6.0
 */