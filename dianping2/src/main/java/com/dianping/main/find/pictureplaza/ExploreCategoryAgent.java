package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.adapter.BasicAdapter;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.MeasuredListView;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.List;

public class ExploreCategoryAgent extends LazyLoadAdapterCellAgent
{
  private static final String CELL_PLAZA_EXPLORE_CATEGORY = "20explore.20category";
  private static final int EXPLORE_CATEGORY_CARD_ID = 1;
  private static final String EXPLORE_CATEGORY_CARD_NEW = "ExploreCategoryCardNew";
  private CategoryAdapter categoryAdapter;

  public ExploreCategoryAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initAdapter()
  {
    this.categoryAdapter = new CategoryAdapter(getFragment().getActivity());
    addCell("20explore.20category", this.categoryAdapter);
  }

  protected void onRefresh()
  {
    super.onRefresh();
    this.categoryAdapter.pullToReset(true);
    onRefreshStart();
  }

  public void requestData()
  {
    initAdapter();
  }

  public class CategoryAdapter extends BasicLoadAdapter
  {
    public CategoryAdapter(Context arg2)
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
        appendDataToList(paramDPObject);
      }
      do
        return;
      while ((this.mData.size() != 0) || (this.mEmptyMsg != null));
      this.mEmptyMsg = "数据为空";
    }

    public MApiRequest createRequest(int paramInt)
    {
      return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/plaza/getdiscoverycategoryItem.bin").buildUpon().build().toString(), CacheType.DISABLED);
    }

    public int getCount()
    {
      if (this.mIsEnd)
      {
        if (this.mData.size() == 0)
          return 1;
        return this.mData.size();
      }
      if ((!ExploreCategoryAgent.this.isRefresh) || (this.mData.size() == 0))
        return this.mData.size() + 1;
      return this.mData.size();
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = (DPObject)getItem(paramInt);
      SharedPreferences localSharedPreferences;
      int i;
      String str;
      if ((paramView instanceof ExploreCategoryItem))
      {
        paramDPObject = (ExploreCategoryItem)paramView;
        paramDPObject.setCategoryInfo((DPObject)localObject);
        localSharedPreferences = DPActivity.preferences(ExploreCategoryAgent.this.getContext());
        i = ((DPObject)localObject).getInt("CategoryId");
        str = ((DPObject)localObject).getString("CategoryName");
        if (i != 1)
          break label169;
        if (localSharedPreferences.getBoolean("ExploreCategoryCardNew", true))
          paramDPObject.setNewImageViewVisibility(true);
      }
      while (true)
      {
        paramDPObject.getTitleView().setGAString("tag_total", str, paramInt);
        paramDPObject.setTitleOnClickListener(new View.OnClickListener(str, i, localSharedPreferences, paramDPObject)
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://plazaexploretopiclist").buildUpon().appendQueryParameter("categoryname", this.val$categoryName).appendQueryParameter("categoryid", this.val$categoryId + "").build());
            ExploreCategoryAgent.this.getContext().startActivity(paramView);
            if (this.val$categoryId == 1)
            {
              this.val$sharedPreferences.edit().putBoolean("ExploreCategoryCardNew", false).commit();
              this.val$exploreCategoryItem.setNewImageViewVisibility(false);
            }
          }
        });
        localObject = ((DPObject)localObject).getArray("PlazaTopicList");
        if (localObject != null)
          break label177;
        return getEmptyView("没有图趣话题，刷新下试试", "", paramViewGroup, paramView);
        paramDPObject = (ExploreCategoryItem)ExploreCategoryAgent.this.res.inflate(ExploreCategoryAgent.this.getContext(), R.layout.plaza_explore_category_item, paramViewGroup, false);
        break;
        label169: paramDPObject.setNewImageViewVisibility(false);
      }
      label177: paramView = new ArrayList(2);
      paramInt = 0;
      while ((paramInt < 2) && (paramInt < localObject.length))
      {
        paramView.add(localObject[paramInt]);
        paramInt += 1;
      }
      paramView = new ExploreCategoryAgent.TopicListItemAdapter(ExploreCategoryAgent.this, paramDPObject, paramView);
      paramDPObject.getTopicListView().setAdapter(paramView);
      paramView.notifyDataSetChanged();
      return (View)paramDPObject;
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
      if ((!ExploreCategoryAgent.this.isRefresh) || (this.mData.size() == 0))
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
        ExploreCategoryAgent.this.onRefreshComplete();
        return;
        if (!ExploreCategoryAgent.this.isRefresh)
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
          ExploreCategoryAgent.this.showToast("网络不给力哦");
          continue;
        }
        this.mIsEnd = true;
        ExploreCategoryAgent.this.showToast("网络不给力哦");
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

  public class TopicListItemAdapter extends BasicAdapter
  {
    private int categoryId;
    private ExploreCategoryItem exploreCategoryItem;
    private List<DPObject> topicList;

    public TopicListItemAdapter(List<DPObject> arg2)
    {
      Object localObject1;
      this.categoryId = localObject1.getCategoryId();
      this.exploreCategoryItem = localObject1;
      Object localObject2;
      this.topicList = localObject2;
    }

    public int getCount()
    {
      if (this.topicList != null)
        return this.topicList.size();
      return 0;
    }

    public DPObject getItem(int paramInt)
    {
      if ((this.topicList != null) && (paramInt < this.topicList.size()))
        return (DPObject)this.topicList.get(paramInt);
      return null;
    }

    public long getItemId(int paramInt)
    {
      if ((this.topicList != null) && (paramInt < this.topicList.size()))
        return paramInt;
      return -1L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof ExploreTopicListViewItem));
      for (paramView = (ExploreTopicListViewItem)paramView; ; paramView = (ExploreTopicListViewItem)ExploreCategoryAgent.this.res.inflate(ExploreCategoryAgent.this.getContext(), R.layout.plaza_explore_topic_listview_item, paramViewGroup, false))
      {
        paramViewGroup = getItem(paramInt);
        paramView.setCategoryId(this.categoryId);
        paramView.setPlazaTopicInfor(paramViewGroup);
        paramView.setOnClickListener(new View.OnClickListener(paramView)
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$exploreTopicListViewItem.getUrl()));
            ExploreCategoryAgent.this.getContext().startActivity(paramView);
            if (ExploreCategoryAgent.TopicListItemAdapter.this.categoryId == 1)
            {
              DPActivity.preferences(ExploreCategoryAgent.this.getContext()).edit().putBoolean("ExploreCategoryCardNew", false).commit();
              ExploreCategoryAgent.TopicListItemAdapter.this.exploreCategoryItem.setNewImageViewVisibility(false);
            }
          }
        });
        paramView.gaUserInfo.biz_id = String.valueOf(this.exploreCategoryItem.getCategoryId());
        paramView.setGAString("tag", paramViewGroup.getString("Title"));
        ((NovaActivity)ExploreCategoryAgent.this.getContext()).addGAView(paramView, paramInt);
        return paramView;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreCategoryAgent
 * JD-Core Version:    0.6.0
 */