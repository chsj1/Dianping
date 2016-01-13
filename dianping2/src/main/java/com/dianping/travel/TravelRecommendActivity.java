package com.dianping.travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.travel.view.TravelRecommendItem;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;

public class TravelRecommendActivity extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private MApiRequest mReq;

  private void handlerResult(DPObject[] paramArrayOfDPObject)
  {
    paramArrayOfDPObject = new TravelRecommendAdapter(paramArrayOfDPObject);
    this.listView.setAdapter(paramArrayOfDPObject);
    paramArrayOfDPObject.notifyDataSetChanged();
  }

  private void requestData()
  {
    this.emptyView.removeAllViews();
    this.emptyView.addView(getLoadingView());
    if (this.mReq != null)
      mapiService().abort(this.mReq, this, true);
    this.mReq = BasicMApiRequest.mapiGet("http://m.api.dianping.com/getrecomroute.hotel?cityid=" + cityId(), CacheType.NORMAL);
    mapiService().exec(this.mReq, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestData();
  }

  protected void onDestroy()
  {
    if (this.mReq != null)
      mapiService().abort(this.mReq, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.emptyView.removeAllViews();
    this.emptyView.addView(getFailedView(paramMApiResponse.message().content(), new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        TravelRecommendActivity.this.requestData();
      }
    }));
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.emptyView.removeAllViews();
    if ((paramMApiResponse.result() instanceof DPObject[]))
    {
      handlerResult((DPObject[])(DPObject[])paramMApiResponse.result());
      return;
    }
    this.emptyView.addView(getFailedView("未知错误", new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        TravelRecommendActivity.this.requestData();
      }
    }));
  }

  protected void setEmptyView()
  {
    super.setEmptyView();
    this.listView.setDivider(null);
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.emptyView.getLayoutParams();
    if (localLayoutParams != null)
      localLayoutParams.gravity = 17;
    this.emptyView.setLayoutParams(localLayoutParams);
  }

  protected void setupView()
  {
    super.setContentView(R.layout.travel_recommend);
  }

  class TravelRecommendAdapter extends BasicAdapter
  {
    private DPObject[] recommends;

    public TravelRecommendAdapter(DPObject[] arg2)
    {
      Object localObject;
      this.recommends = localObject;
    }

    public int getCount()
    {
      if (this.recommends == null)
        return 0;
      return this.recommends.length;
    }

    public DPObject getItem(int paramInt)
    {
      return this.recommends[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof TravelRecommendItem));
      while (true)
      {
        if ((paramView instanceof TravelRecommendItem))
          ((TravelRecommendItem)paramView).setTravelRecommendItem(getItem(paramInt));
        return paramView;
        paramView = TravelRecommendActivity.this.getLayoutInflater().inflate(R.layout.travel_recommend_item, paramViewGroup, false);
      }
    }

    public boolean isEnabled(int paramInt)
    {
      return false;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.TravelRecommendActivity
 * JD-Core Version:    0.6.0
 */