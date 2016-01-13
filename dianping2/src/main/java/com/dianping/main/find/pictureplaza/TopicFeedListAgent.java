package com.dianping.main.find.pictureplaza;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.HashMap;

public class TopicFeedListAgent extends PlazaAdapterCellAgent
{
  private static final String FEED_LIST_TAG = "040FeedList";
  private static final String FEED_TAB_TAG = "030FeedTab";
  private int DATA_TYPE_LOCAL = 1;
  private int DATA_TYPE_TOTAL = 0;
  private int mDataType = -1;
  private FeedListAdapter mLocalAdapter;
  private View mLocalIndicator;
  private NovaTextView mLocalText;
  final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.uploadedcommunityphoto".equals(paramIntent.getAction()))
        if ((TopicFeedListAgent.this.mTotalAdapter != null) && (TopicFeedListAgent.this.mLocalAdapter != null))
        {
          TopicFeedListAgent.this.mTotalAdapter.reset();
          TopicFeedListAgent.this.mLocalAdapter.reset();
          TopicFeedListAgent.this.setCurrentTab(TopicFeedListAgent.this.DATA_TYPE_TOTAL);
        }
      int i;
      boolean bool;
      do
      {
        do
          return;
        while (!"com.dianping.action.PlazaFeedLikeStateChange".equals(paramIntent.getAction()));
        i = paramIntent.getIntExtra("position", 0);
        paramContext = paramIntent.getStringExtra("feedid");
        bool = paramIntent.getBooleanExtra("islike", false);
        if (TopicFeedListAgent.this.mDataType != TopicFeedListAgent.this.DATA_TYPE_TOTAL)
          continue;
        TopicFeedListAgent.this.mTotalAdapter.updateLikeStatus(i, paramContext, bool);
        return;
      }
      while (TopicFeedListAgent.this.mDataType != TopicFeedListAgent.this.DATA_TYPE_LOCAL);
      TopicFeedListAgent.this.mLocalAdapter.updateLikeStatus(i, paramContext, bool);
    }
  };
  View mTabView;
  private int mTopicId;
  private FeedListAdapter mTotalAdapter;
  private View mTotalIndicator;
  private NovaTextView mTotalText;

  public TopicFeedListAgent(Object paramObject)
  {
    super(paramObject);
  }

  View createTabView(ViewGroup paramViewGroup)
  {
    this.mTabView = this.res.inflate(getContext(), R.layout.find_plaza_topic_feed, paramViewGroup, false);
    this.mTotalIndicator = this.mTabView.findViewById(R.id.feed_indicator_total);
    this.mLocalIndicator = this.mTabView.findViewById(R.id.feed_indicator_local);
    this.mTotalText = ((NovaTextView)this.mTabView.findViewById(R.id.global_list));
    this.mTotalText.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TopicFeedListAgent.this.setCurrentTab(TopicFeedListAgent.this.DATA_TYPE_TOTAL);
      }
    });
    this.mTotalText.gaUserInfo.biz_id = String.valueOf(this.mTopicId);
    this.mLocalText = ((NovaTextView)this.mTabView.findViewById(R.id.local_list));
    this.mLocalText.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TopicFeedListAgent.this.setCurrentTab(TopicFeedListAgent.this.DATA_TYPE_LOCAL);
      }
    });
    this.mLocalText.gaUserInfo.biz_id = String.valueOf(this.mTopicId);
    setCurrentTab(this.DATA_TYPE_TOTAL);
    return this.mTabView;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mTopicId = getFragment().getIntParam("topicid", 1);
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.uploadedcommunityphoto");
    paramBundle.addAction("com.dianping.action.PlazaFeedLikeStateChange");
    LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mReceiver, paramBundle);
    this.mTotalAdapter = new FeedListAdapter(getContext(), this.DATA_TYPE_TOTAL);
    this.mLocalAdapter = new FeedListAdapter(getContext(), this.DATA_TYPE_LOCAL);
    addCell("030FeedTab", new FeedTabAdapter());
    addCell("040FeedList", this.mTotalAdapter);
  }

  public void onDestroy()
  {
    super.onDestroy();
    try
    {
      LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mReceiver);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void onRefresh()
  {
    if ((this.mTotalAdapter != null) && (this.mLocalAdapter != null))
    {
      this.mTotalAdapter.reset();
      this.mLocalAdapter.reset();
      setCurrentTab(this.DATA_TYPE_TOTAL);
    }
  }

  void setCurrentTab(int paramInt)
  {
    if (paramInt == this.mDataType)
      return;
    if (paramInt == this.DATA_TYPE_TOTAL)
    {
      this.mTotalText.setTextColor(getResources().getColor(R.color.tips_text_red));
      this.mLocalText.setTextColor(getResources().getColor(R.color.deep_gray));
      this.mTotalIndicator.setVisibility(0);
      this.mLocalIndicator.setVisibility(4);
      addCell("040FeedList", this.mTotalAdapter);
    }
    while (true)
    {
      this.mDataType = paramInt;
      return;
      if (paramInt != this.DATA_TYPE_LOCAL)
        continue;
      this.mTotalText.setTextColor(getResources().getColor(R.color.deep_gray));
      this.mLocalText.setTextColor(getResources().getColor(R.color.tips_text_red));
      this.mTotalIndicator.setVisibility(4);
      this.mLocalIndicator.setVisibility(0);
      addCell("040FeedList", this.mLocalAdapter);
    }
  }

  class FeedListAdapter extends FeedBasicAdapter
  {
    private Context context;
    private int dataType;

    public FeedListAdapter(Context paramInt, int arg3)
    {
      super();
      this.context = paramInt;
      int i;
      this.dataType = i;
    }

    public MApiRequest createRequest(int paramInt)
    {
      String str1 = "";
      String str2 = "";
      if (TopicFeedListAgent.this.location() != null)
      {
        str1 = TopicFeedListAgent.this.location().latitude() + "";
        str2 = TopicFeedListAgent.this.location().longitude() + "";
      }
      Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/plaza/getplazafeedlist.bin").buildUpon();
      if (this.dataType == TopicFeedListAgent.this.DATA_TYPE_TOTAL)
        localBuilder.appendQueryParameter("samecity", "false");
      while (true)
      {
        localBuilder.appendQueryParameter("cityid", String.valueOf(TopicFeedListAgent.this.cityId()));
        localBuilder.appendQueryParameter("topicid", String.valueOf(TopicFeedListAgent.this.mTopicId));
        localBuilder.appendQueryParameter("start", String.valueOf(paramInt)).appendQueryParameter("lng", str2).appendQueryParameter("lat", str1);
        return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
        localBuilder.appendQueryParameter("samecity", "true");
      }
    }

    protected void dismissFeedProgressDialog()
    {
      TopicFeedListAgent.this.getFragment().dismissDialog();
    }

    protected TextView getEmptyView(String paramString1, String paramString2, ViewGroup paramViewGroup, View paramView)
    {
      paramString1 = null;
      if (paramView == null);
      while (true)
      {
        paramString2 = paramString1;
        if (paramString1 == null)
        {
          paramString2 = new TextView(paramViewGroup.getContext());
          paramString2.setTag(EMPTY);
          paramString2.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
          paramString2.setPadding(0, ViewUtils.dip2px(TopicFeedListAgent.this.getContext(), 30.0F), 0, 0);
          paramString2.setGravity(17);
          paramString2.setTextSize(0, TopicFeedListAgent.this.getResources().getDimensionPixelSize(R.dimen.text_size_14));
          paramString2.setTextColor(TopicFeedListAgent.this.getResources().getColor(R.color.text_color_light_gray));
        }
        paramString2.setText("这里没有更多图趣了，去别的地方看看吧");
        return paramString2;
        if (paramView.getTag() != EMPTY)
          continue;
        paramString1 = (TextView)paramView;
      }
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof PlazaFeedItem));
      for (paramView = (PlazaFeedItem)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (PlazaFeedItem)LayoutInflater.from(this.context).inflate(R.layout.plaza_home_feed_item, paramViewGroup, false);
        ((PlazaFeedItem)localObject).setFeedUgc(paramDPObject, paramInt, this.expandMaps, this.imageIndexMaps, true, TopicFeedListAgent.this.mTopicId);
        ((PlazaFeedItem)localObject).setFeedItemListener(this.feedItemListener);
        ((PlazaFeedItem)localObject).setBackgroundColor(TopicFeedListAgent.this.res.getColor(R.color.white));
        return localObject;
      }
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (this.deleteRequest == paramMApiRequest)
      {
        TopicFeedListAgent.this.dismissDialog();
        this.deleteRequest = null;
        return;
      }
      if (this.mReqLike == paramMApiRequest)
      {
        this.mReqLike = null;
        return;
      }
      if ((paramMApiResponse.message() == null) || (paramMApiResponse.message().content() == null));
      for (String str = "请求失败，请稍后再试"; ; str = paramMApiResponse.message().content())
      {
        setErrorMsg(str);
        this.mReq = null;
        onRequestComplete(false, paramMApiRequest, paramMApiResponse);
        return;
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (this.deleteRequest == paramMApiRequest)
      {
        updateDeleteStatus();
        TopicFeedListAgent.this.dismissDialog();
        this.deleteRequest = null;
        return;
      }
      if (this.mReqLike == paramMApiRequest)
      {
        this.mReqLike = null;
        return;
      }
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        appendData((DPObject)paramMApiResponse.result());
        this.mReq = null;
        onRequestComplete(true, paramMApiRequest, paramMApiResponse);
        return;
      }
      if (paramMApiResponse.message() == null);
      for (String str = "请求失败，请稍后再试"; ; str = paramMApiResponse.message().content())
      {
        setErrorMsg(str);
        break;
      }
    }

    protected void showFeedProgressDialog(String paramString)
    {
      TopicFeedListAgent.this.getFragment().showProgressDialog(paramString);
    }

    public void updateDeleteStatus()
    {
      if (this.curFeedId == null);
      DPObject localDPObject;
      do
      {
        do
          return;
        while (!(getItem(this.curPosition) instanceof DPObject));
        localDPObject = (DPObject)getItem(this.curPosition);
      }
      while (!(localDPObject.getInt("FeedId") + "").equals(this.curFeedId));
      this.mData.remove(localDPObject);
      notifyDataSetChanged();
      if ((this.expandMaps != null) && (this.expandMaps.size() > 0) && (this.expandMaps.containsKey(Integer.valueOf(this.curPosition))))
        this.expandMaps.remove(Integer.valueOf(this.curPosition));
      if ((this.imageIndexMaps != null) && (this.imageIndexMaps.size() > 0) && (this.imageIndexMaps.containsKey(Integer.valueOf(this.curPosition))))
        this.imageIndexMaps.remove(Integer.valueOf(this.curPosition));
      this.curFeedId = null;
      this.curPosition = 0;
    }

    public void updateLikeStatus(int paramInt, String paramString, boolean paramBoolean)
    {
      Object localObject;
      if ((getItem(paramInt) instanceof DPObject))
      {
        localObject = (DPObject)getItem(paramInt);
        if ((((DPObject)localObject).getInt("FeedId") + "").equals(paramString));
      }
      else
      {
        return;
      }
      int i = ((DPObject)localObject).getInt("LikeCount");
      if (paramBoolean)
      {
        i -= 1;
        paramString = this.mData;
        localObject = ((DPObject)localObject).edit();
        if (paramBoolean)
          break label139;
      }
      label139: for (paramBoolean = true; ; paramBoolean = false)
      {
        paramString.set(paramInt, ((DPObject.Editor)localObject).putBoolean("IsLike", paramBoolean).putInt("LikeCount", i).generate());
        notifyDataSetChanged();
        return;
        i += 1;
        break;
      }
    }
  }

  class FeedTabAdapter extends BaseAdapter
  {
    FeedTabAdapter()
    {
    }

    public int getCount()
    {
      return 1;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (TopicFeedListAgent.this.mTabView == null)
        return TopicFeedListAgent.this.createTabView(paramViewGroup);
      return TopicFeedListAgent.this.mTabView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.TopicFeedListAgent
 * JD-Core Version:    0.6.0
 */