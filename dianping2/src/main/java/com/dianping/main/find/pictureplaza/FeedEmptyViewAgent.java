package com.dianping.main.find.pictureplaza;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AdapterCellAgent.BasicCellAgentAdapter;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;

public class FeedEmptyViewAgent extends AdapterCellAgent
{
  private static final String FEED_EMPTY_VIEW_TAG = "080EmptyView";
  private Adapter mAdapter;
  private TextView mAgentView;
  private int mCommentCount = 2147483647;
  private boolean mIsRegister = false;
  private int mLikeCount = 2147483647;
  final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.PlazaFeedLikeCount".equals(paramIntent.getAction()))
        FeedEmptyViewAgent.access$002(FeedEmptyViewAgent.this, paramIntent.getIntExtra("likecount", 2147483647));
      while (true)
      {
        FeedEmptyViewAgent.this.mAdapter.notifyDataSetChanged();
        return;
        if (!"com.dianping.action.PlazaFeedCommentCount".equals(paramIntent.getAction()))
          continue;
        FeedEmptyViewAgent.access$102(FeedEmptyViewAgent.this, paramIntent.getIntExtra("commentcount", 2147483647));
      }
    }
  };

  public FeedEmptyViewAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.PlazaFeedLikeCount");
    paramBundle.addAction("com.dianping.action.PlazaFeedCommentCount");
    LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mReceiver, paramBundle);
    this.mIsRegister = true;
    this.mAdapter = new Adapter(null);
    addCell("080EmptyView", this.mAdapter);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mIsRegister);
    try
    {
      getFragment().getActivity().unregisterReceiver(this.mReceiver);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  private class Adapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      if ((FeedEmptyViewAgent.this.mLikeCount == 0) && (FeedEmptyViewAgent.this.mCommentCount == 0))
        return 1;
      return 0;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (FeedEmptyViewAgent.this.mAgentView == null)
      {
        FeedEmptyViewAgent.access$402(FeedEmptyViewAgent.this, new TextView(FeedEmptyViewAgent.this.getContext()));
        paramView = new AbsListView.LayoutParams(-1, -2);
        FeedEmptyViewAgent.this.mAgentView.setLayoutParams(paramView);
        FeedEmptyViewAgent.this.mAgentView.setPadding(0, ViewUtils.dip2px(FeedEmptyViewAgent.this.getContext(), 100.0F), 0, 0);
        FeedEmptyViewAgent.this.mAgentView.setGravity(17);
        FeedEmptyViewAgent.this.mAgentView.setTextSize(0, FeedEmptyViewAgent.this.getResources().getDimensionPixelSize(R.dimen.text_size_14));
        FeedEmptyViewAgent.this.mAgentView.setTextColor(FeedEmptyViewAgent.this.getResources().getColor(R.color.text_color_light_gray));
        FeedEmptyViewAgent.this.mAgentView.setText("暂时还没有评论\n做第一个评论的人吧");
      }
      return FeedEmptyViewAgent.this.mAgentView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.FeedEmptyViewAgent
 * JD-Core Version:    0.6.0
 */