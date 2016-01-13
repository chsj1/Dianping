package com.dianping.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.adapter.MergeAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.travel.view.CommonListView;
import com.dianping.travel.view.GridButtonView;
import com.dianping.travel.view.GridImageView;
import com.dianping.travel.view.ListImageView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;

public class TravelGuideActivity extends NovaListActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private int guideType;
  private View mFeedback;
  private MApiRequest mReq;

  private void handlerResult(DPObject paramDPObject)
  {
    int i = 0;
    Object localObject = LayoutInflater.from(this).inflate(R.layout.localflavour_head, this.listView, false);
    if (localObject != null)
    {
      ((TextView)((View)localObject).findViewById(16908308)).setText(paramDPObject.getString("Intro"));
      if (!TextUtils.isEmpty(paramDPObject.getString("Intro")))
        this.listView.addHeaderView((View)localObject, null, false);
    }
    localObject = new MergeAdapter();
    View localView;
    if (paramDPObject.contains("HasFeedBack"))
    {
      localView = this.mFeedback;
      if (!paramDPObject.getBoolean("HasFeedBack"))
        break label112;
    }
    while (true)
    {
      localView.setVisibility(i);
      paramDPObject = paramDPObject.getArray("FlavourDetailList");
      if (paramDPObject != null)
        break;
      return;
      label112: i = 8;
    }
    int j = paramDPObject.length;
    i = 0;
    if (i < j)
    {
      localView = paramDPObject[i];
      if (localView.getInt("Type") == 1)
        ((MergeAdapter)localObject).addAdapter(new ListImageAdapter(localView));
      while (true)
      {
        i += 1;
        break;
        if (localView.getInt("Type") == 2)
        {
          ((MergeAdapter)localObject).addAdapter(new CommonListAdapter(localView));
          continue;
        }
        if (localView.getInt("Type") == 3)
        {
          ((MergeAdapter)localObject).addAdapter(new GridButtonAdapter(localView));
          continue;
        }
        if (localView.getInt("Type") != 4)
          continue;
        ((MergeAdapter)localObject).addAdapter(new GridImageAdapter(localView));
      }
    }
    this.listView.setAdapter((ListAdapter)localObject);
    ((MergeAdapter)localObject).notifyDataSetChanged();
  }

  private void requestData()
  {
    this.emptyView.removeAllViews();
    this.emptyView.addView(getLoadingView());
    if (this.mReq != null)
      mapiService().abort(this.mReq, this, true);
    this.mReq = BasicMApiRequest.mapiGet("http://m.api.dianping.com/getflavourguide.hotel?cityid=" + cityId() + "&guidetype=" + this.guideType, CacheType.NORMAL);
    mapiService().exec(this.mReq, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setTitle(getIntent().getData().getQueryParameter("title"));
    this.guideType = Integer.parseInt(getIntent().getData().getQueryParameter("guidetype"));
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
        TravelGuideActivity.this.requestData();
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
        TravelGuideActivity.this.requestData();
      }
    }));
  }

  protected void setEmptyView()
  {
    super.setEmptyView();
    this.listView.setDivider(null);
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.emptyView.getLayoutParams();
    if (localLayoutParams != null)
    {
      localLayoutParams.gravity = 17;
      this.emptyView.setLayoutParams(localLayoutParams);
    }
    this.mFeedback = findViewById(R.id.feedback);
    this.mFeedback.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW");
        paramView.setData(Uri.parse("dianping://feedback?flag=6"));
        TravelGuideActivity.this.startActivity(paramView);
      }
    });
  }

  protected void setupView()
  {
    super.setContentView(R.layout.local_flavour_list_frame);
  }

  class CommonListAdapter extends BasicAdapter
  {
    private DPObject mData;

    public CommonListAdapter(DPObject arg2)
    {
      Object localObject;
      this.mData = localObject;
    }

    public int getCount()
    {
      return 2;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt == 0)
        return this.mData.getString("Title");
      return this.mData.getArray("Items");
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      if (paramInt == 0)
        return 0;
      return 1;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if (paramInt == 0)
      {
        paramView = TravelGuideActivity.this.getLayoutInflater().inflate(R.layout.header_textview, paramViewGroup, false);
        paramViewGroup = paramView;
        if (paramView != null)
        {
          ((TextView)paramView).setText((String)localObject);
          paramViewGroup = paramView;
        }
        return paramViewGroup;
      }
      if ((paramView instanceof CommonListView));
      while (true)
      {
        paramViewGroup = paramView;
        if (!(paramView instanceof CommonListView))
          break;
        ((CommonListView)paramView).setData((DPObject[])(DPObject[])localObject);
        ((CommonListView)paramView).setOnItemClickListener(new TravelGuideActivity.CommonListAdapter.1(this));
        return paramView;
        paramView = TravelGuideActivity.this.getLayoutInflater().inflate(R.layout.common_list_view, paramViewGroup, false);
      }
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public boolean isEnabled(int paramInt)
    {
      return paramInt != 0;
    }
  }

  class GridButtonAdapter extends BasicAdapter
  {
    private DPObject mData;

    public GridButtonAdapter(DPObject arg2)
    {
      Object localObject;
      this.mData = localObject;
    }

    public int getCount()
    {
      return 2;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt == 0)
        return this.mData.getString("Title");
      return this.mData.getArray("Items");
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      if (paramInt == 0)
        return 0;
      return 1;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if (paramInt == 0)
      {
        paramView = TravelGuideActivity.this.getLayoutInflater().inflate(R.layout.header_textview, paramViewGroup, false);
        paramViewGroup = paramView;
        if (paramView != null)
        {
          ((TextView)paramView).setText((String)localObject);
          paramViewGroup = paramView;
        }
        return paramViewGroup;
      }
      if ((paramView instanceof GridButtonView));
      while (true)
      {
        paramViewGroup = paramView;
        if (!(paramView instanceof GridButtonView))
          break;
        ((GridButtonView)paramView).setData((DPObject[])(DPObject[])localObject);
        ((GridButtonView)paramView).setOnItemClickListener(new TravelGuideActivity.GridButtonAdapter.1(this));
        return paramView;
        paramView = TravelGuideActivity.this.getLayoutInflater().inflate(R.layout.grid_button_view, paramViewGroup, false);
      }
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public boolean isEnabled(int paramInt)
    {
      return paramInt != 0;
    }
  }

  class GridImageAdapter extends BasicAdapter
  {
    private DPObject mData;

    public GridImageAdapter(DPObject arg2)
    {
      Object localObject;
      this.mData = localObject;
    }

    public int getCount()
    {
      return 2;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt == 0)
        return this.mData.getString("Title");
      return this.mData.getArray("Items");
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      if (paramInt == 0)
        return 0;
      return 1;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if (paramInt == 0)
      {
        paramView = TravelGuideActivity.this.getLayoutInflater().inflate(R.layout.header_textview, paramViewGroup, false);
        paramViewGroup = paramView;
        if (paramView != null)
        {
          ((TextView)paramView).setText((String)localObject);
          paramViewGroup = paramView;
        }
        return paramViewGroup;
      }
      if ((paramView instanceof GridImageView));
      while (true)
      {
        paramViewGroup = paramView;
        if (!(paramView instanceof GridImageView))
          break;
        ((GridImageView)paramView).setData((DPObject[])(DPObject[])localObject);
        ((GridImageView)paramView).setOnItemClickListener(new TravelGuideActivity.GridImageAdapter.1(this));
        return paramView;
        paramView = TravelGuideActivity.this.getLayoutInflater().inflate(R.layout.grid_image_view, paramViewGroup, false);
      }
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public boolean isEnabled(int paramInt)
    {
      return paramInt != 0;
    }
  }

  class ListImageAdapter extends BasicAdapter
  {
    private DPObject mData;

    public ListImageAdapter(DPObject arg2)
    {
      Object localObject;
      this.mData = localObject;
    }

    public int getCount()
    {
      return 2;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt == 0)
        return this.mData.getString("Title");
      return this.mData.getArray("Items");
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      if (paramInt == 0)
        return 0;
      return 1;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if (paramInt == 0)
      {
        paramView = TravelGuideActivity.this.getLayoutInflater().inflate(R.layout.header_textview, paramViewGroup, false);
        paramViewGroup = paramView;
        if (paramView != null)
        {
          ((TextView)paramView).setText((String)localObject);
          paramViewGroup = paramView;
        }
        return paramViewGroup;
      }
      if ((paramView instanceof ListImageView));
      while (true)
      {
        paramViewGroup = paramView;
        if (!(paramView instanceof ListImageView))
          break;
        ((ListImageView)paramView).setData((DPObject[])(DPObject[])localObject);
        ((ListImageView)paramView).setOnItemClickListener(new TravelGuideActivity.ListImageAdapter.1(this));
        return paramView;
        paramView = TravelGuideActivity.this.getLayoutInflater().inflate(R.layout.list_image_view, paramViewGroup, false);
      }
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public boolean isEnabled(int paramInt)
    {
      return paramInt != 0;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.TravelGuideActivity
 * JD-Core Version:    0.6.0
 */