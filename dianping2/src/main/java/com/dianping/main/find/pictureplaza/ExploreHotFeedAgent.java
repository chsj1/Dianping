package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableRow;
import com.dianping.adapter.BasicAdapter;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.CustomGridView;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;

public class ExploreHotFeedAgent extends LazyLoadAdapterCellAgent
{
  private static final String CELL_PLAZA_EXPLORE_HOT_FEED = "10explore.10hotfeed";
  private HotFeedAdapter hotFeedAdapter;

  public ExploreHotFeedAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initAdapter()
  {
    this.hotFeedAdapter = new HotFeedAdapter(getFragment().getActivity());
    addCell("10explore.10hotfeed", this.hotFeedAdapter);
  }

  protected void onRefresh()
  {
    super.onRefresh();
    this.hotFeedAdapter.pullToReset(true);
    onRefreshStart();
  }

  public void requestData()
  {
    initAdapter();
  }

  public class HotFeedAdapter extends BasicLoadAdapter
  {
    private SparseArray<ExploreHotFeedItem> hotfeedArray = new SparseArray(2);

    public HotFeedAdapter(Context arg2)
    {
      super();
    }

    public void appendData(DPObject paramDPObject)
    {
      if (this.mIsPullToRefresh)
        this.mIsPullToRefresh = false;
      this.mEmptyMsg = paramDPObject.getString("EmptyMsg");
      this.mNextStartIndex = paramDPObject.getInt("NextStartIndex");
      this.mRecordCount = paramDPObject.getInt("RecordCount");
      this.mQueryId = paramDPObject.getString("QueryID");
      paramDPObject = paramDPObject.getArray("List");
      if ((paramDPObject != null) && (paramDPObject.length != 0))
      {
        this.mData.clear();
        this.hotfeedArray.clear();
        appendDataToList(paramDPObject);
      }
      do
        return;
      while ((this.mData.size() != 0) || (this.mEmptyMsg != null));
      this.mEmptyMsg = "数据为空";
    }

    public MApiRequest createRequest(int paramInt)
    {
      return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/plaza/getdiscoveryitem.bin").buildUpon().build().toString(), CacheType.DISABLED);
    }

    public int getCount()
    {
      if (this.mIsEnd)
      {
        if (this.mData.size() == 0)
          return 1;
        return this.mData.size();
      }
      if ((!ExploreHotFeedAgent.this.isRefresh) || (this.mData.size() == 0))
        return this.mData.size() + 1;
      return this.mData.size();
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramDPObject = (DPObject)getItem(paramInt);
      if (this.hotfeedArray.get(paramInt) != null)
      {
        paramDPObject = (ExploreHotFeedItem)this.hotfeedArray.get(paramInt);
        return paramDPObject;
      }
      ExploreHotFeedItem localExploreHotFeedItem = (ExploreHotFeedItem)ExploreHotFeedAgent.this.res.inflate(ExploreHotFeedAgent.this.getContext(), R.layout.plaza_explore_hotfeed_item, paramViewGroup, false);
      Object localObject = localExploreHotFeedItem.getCustomGridView();
      ExploreHotFeedAgent.HotFeedImageItemAdapter localHotFeedImageItemAdapter = new ExploreHotFeedAgent.HotFeedImageItemAdapter(ExploreHotFeedAgent.this);
      ((CustomGridView)localObject).setAdapter(localHotFeedImageItemAdapter);
      localObject = paramDPObject.getStringArray("PicList");
      if (localObject == null)
        return getEmptyView("没有图趣图片，刷新下试试", "", paramViewGroup, paramView);
      paramView = new ArrayList(4);
      int i = 0;
      while ((i < 4) && (i < localObject.length))
      {
        paramView.add(localObject[i]);
        i += 1;
      }
      i = paramDPObject.getInt("Type");
      localHotFeedImageItemAdapter.setAdapterData(paramView);
      localHotFeedImageItemAdapter.notifyDataSetChanged();
      localExploreHotFeedItem.setTopicInfo(paramDPObject);
      if (i == 1);
      for (paramDPObject = "hot"; ; paramDPObject = "new")
      {
        localExploreHotFeedItem.setGAString(paramDPObject);
        localExploreHotFeedItem.setOnClickListener(new View.OnClickListener(i)
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://plazafeedlist").buildUpon().appendQueryParameter("feedtype", this.val$feedType + "").build());
            ExploreHotFeedAgent.this.getContext().startActivity(paramView);
          }
        });
        this.hotfeedArray.put(paramInt, localExploreHotFeedItem);
        paramDPObject = localExploreHotFeedItem;
        break;
      }
    }

    protected boolean loadNewPage()
    {
      if (this.mIsEnd);
      do
        return false;
      while (this.mReq != null);
      this.mErrorMsg = null;
      this.mReq = createRequest(this.mNextStartIndex);
      if (this.mReq != null)
        this.mapiService.exec(this.mReq, this);
      if ((!ExploreHotFeedAgent.this.isRefresh) || (this.mData.size() == 0))
        notifyDataSetChanged();
      return true;
    }

    protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestComplete(paramBoolean, paramMApiRequest, paramMApiResponse);
      if (paramBoolean)
      {
        this.mIsEnd = true;
        notifyDataSetChanged();
      }
      while (true)
      {
        ExploreHotFeedAgent.this.onRefreshComplete();
        return;
        if (!ExploreHotFeedAgent.this.isRefresh)
        {
          this.mIsEnd = false;
          this.mErrorMsg = "网络连接失败，点击重新加载";
          notifyDataSetChanged();
          continue;
        }
        if (this.mData.size() == 0)
        {
          this.mIsEnd = false;
          this.mErrorMsg = "网络连接失败，点击重新加载";
          notifyDataSetChanged();
          ExploreHotFeedAgent.this.showToast("网络不给力哦");
          continue;
        }
        this.mIsEnd = true;
        ExploreHotFeedAgent.this.showToast("网络不给力哦");
      }
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      this.mErrorMsg = "网络连接失败，点击重新加载";
      this.mReq = null;
      onRequestComplete(false, paramMApiRequest, paramMApiResponse);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
        appendData((DPObject)paramMApiResponse.result());
      while (true)
      {
        this.mReq = null;
        onRequestComplete(true, paramMApiRequest, paramMApiResponse);
        return;
        this.mErrorMsg = "网络连接失败，点击重新加载";
      }
    }
  }

  public class HotFeedImageItemAdapter extends BasicAdapter
  {
    ExploreHotFeedGridViewItem hotFeedGridViewItem = null;
    private List<String> picList;

    public HotFeedImageItemAdapter()
    {
    }

    public int getCount()
    {
      if (this.picList != null)
        return this.picList.size();
      return 0;
    }

    public String getItem(int paramInt)
    {
      if ((this.picList != null) && (paramInt < this.picList.size()))
        return (String)this.picList.get(paramInt);
      return null;
    }

    public long getItemId(int paramInt)
    {
      if ((this.picList != null) && (paramInt < this.picList.size()))
        return paramInt;
      return -1L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramInt == 0)
      {
        paramView = new TableRow(ExploreHotFeedAgent.this.getContext());
        this.hotFeedGridViewItem = ((ExploreHotFeedGridViewItem)ExploreHotFeedAgent.this.res.inflate(ExploreHotFeedAgent.this.getContext(), R.layout.plaza_explore_hotfeed_gridview_item, (TableRow)paramView, false));
        ((TableRow)paramView).addView(this.hotFeedGridViewItem);
      }
      while (true)
      {
        paramViewGroup = getItem(paramInt);
        this.hotFeedGridViewItem.setImageUrl(paramViewGroup);
        return paramView;
        this.hotFeedGridViewItem = ((ExploreHotFeedGridViewItem)ExploreHotFeedAgent.this.res.inflate(ExploreHotFeedAgent.this.getContext(), R.layout.plaza_explore_hotfeed_gridview_item, ((CustomGridView)paramViewGroup).getCurRow(), false));
        paramView = this.hotFeedGridViewItem;
      }
    }

    public void setAdapterData(List<String> paramList)
    {
      this.picList = paramList;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreHotFeedAgent
 * JD-Core Version:    0.6.0
 */