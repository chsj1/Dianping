package com.dianping.travel;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.dianping.travel.view.TravelValuableInfoItem;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;

public class TravelValuableInfoActivity extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  private Adapter adapter;
  private MApiRequest mReq;

  private void handlerResult(DPObject paramDPObject)
  {
    if (paramDPObject != null)
    {
      this.adapter = new Adapter(paramDPObject.getArray("Infos"));
      this.listView.setAdapter(this.adapter);
      this.listView.setOnItemClickListener(this);
    }
  }

  private void requestData()
  {
    this.emptyView.removeAllViews();
    this.emptyView.addView(getLoadingView());
    if (this.mReq != null)
      mapiService().abort(this.mReq, this, true);
    this.mReq = BasicMApiRequest.mapiGet("http://m.api.dianping.com/getlocalinfo.hotel?cityid=" + cityId(), CacheType.NORMAL);
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

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.adapter.getItem(paramInt);
    if (paramAdapterView != null)
    {
      paramView = paramAdapterView.getString("Url");
      if (!TextUtils.isEmpty(paramView))
      {
        startActivity(paramView);
        statisticsEvent("travel5", "travel5_channel_guide_item", paramAdapterView.getString("Name"), 0);
      }
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.emptyView.removeAllViews();
    this.emptyView.addView(getFailedView(paramMApiResponse.message().content(), new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        TravelValuableInfoActivity.this.requestData();
      }
    }));
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.emptyView.removeAllViews();
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      handlerResult((DPObject)paramMApiResponse.result());
      return;
    }
    this.emptyView.addView(getFailedView("未知错误", new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        TravelValuableInfoActivity.this.requestData();
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
    super.setContentView(R.layout.travel_valuable_info);
  }

  class Adapter extends BasicAdapter
  {
    private DPObject[] infos;

    public Adapter(DPObject[] arg2)
    {
      Object localObject;
      this.infos = localObject;
    }

    public int getCount()
    {
      if (this.infos == null)
        return 0;
      return this.infos.length;
    }

    public DPObject getItem(int paramInt)
    {
      return this.infos[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof TravelValuableInfoItem));
      while (true)
      {
        if ((paramView instanceof TravelValuableInfoItem))
        {
          ((TravelValuableInfoItem)paramView).setTravelValuableInfoItem(getItem(paramInt));
          if (paramInt != getCount() - 1)
            break;
          paramView.findViewById(R.id.line).setVisibility(8);
        }
        return paramView;
        paramView = TravelValuableInfoActivity.this.getLayoutInflater().inflate(R.layout.travel_valuable_info_item, paramViewGroup, false);
      }
      paramView.findViewById(R.id.line).setVisibility(0);
      return paramView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.TravelValuableInfoActivity
 * JD-Core Version:    0.6.0
 */