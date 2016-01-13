package com.dianping.main.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HomeRefreshAgent extends HomeAgent
{
  public static final int REFRESH_REQUEST_COUNT = 3;
  public static final String REFRESH_TAG = "47refresh.";
  public final int REFRESHINGE = 1;
  public final int REFRESH_GONE = 0;
  public final int REFRESH_VISIABLE = 2;
  private Adapter adapter;
  Animation animation;
  private int refreshFlag = 0;
  private boolean refreshStatus;
  int request = 0;

  public HomeRefreshAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((paramBundle != null) && ("refreshflag".equals(paramBundle.getString("flag"))))
      this.request += 1;
    boolean bool = ((MainHomeFragment)getFragment()).getShowRefresh();
    if ((this.request >= 3) || (this.refreshStatus != bool))
    {
      this.refreshStatus = bool;
      if (!this.refreshStatus)
        break label110;
    }
    label110: for (int i = 2; ; i = 0)
    {
      this.refreshFlag = i;
      paramBundle = new Bundle();
      paramBundle.putString("flag", "refreshflag");
      dispatchAgentChanged("home/HomeGuesslikeSection", paramBundle);
      this.adapter.notifyMergeItemRangeChanged();
      return;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.adapter = new Adapter(null);
    addCell("47refresh.", this.adapter);
    this.animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate_alpha);
    this.animation.setInterpolator(new LinearInterpolator());
  }

  void requestRefresh()
  {
    if ((getFragment() != null) && ((getFragment() instanceof MainHomeFragment)))
      ((MainHomeFragment)getFragment()).onRetry();
  }

  private class Adapter extends HomeAgent.HomeAgentAdapter
  {
    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      if (HomeRefreshAgent.this.refreshFlag != 0)
        return 1;
      return 0;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      paramViewHolder = (RefreshHolder)paramViewHolder;
      if (HomeRefreshAgent.this.refreshFlag == 1)
      {
        paramViewHolder.animIcon.setVisibility(0);
        paramViewHolder.refreshLayout.setVisibility(4);
        paramViewHolder.animIcon.startAnimation(HomeRefreshAgent.this.animation);
        return;
      }
      paramViewHolder.animIcon.setVisibility(8);
      paramViewHolder.refreshLayout.setVisibility(0);
      paramViewHolder.animIcon.clearAnimation();
    }

    public RefreshHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new RefreshHolder(HomeRefreshAgent.this.res.inflate(HomeRefreshAgent.this.getContext(), R.layout.home_refresh_lay, paramViewGroup, false));
    }

    class RefreshHolder extends BasicRecyclerAdapter.BasicHolder
    {
      ImageView animIcon;
      LinearLayout refreshLayout;

      public RefreshHolder(View arg2)
      {
        super(localView);
        this.animIcon = ((ImageView)localView.findViewById(R.id.anim_icon));
        this.refreshLayout = ((LinearLayout)localView.findViewById(R.id.refresh_layout));
        localView.setOnClickListener(new View.OnClickListener(HomeRefreshAgent.Adapter.this)
        {
          public void onClick(View paramView)
          {
            HomeRefreshAgent.this.requestRefresh();
            HomeRefreshAgent.this.request = 0;
            HomeRefreshAgent.access$102(HomeRefreshAgent.this, 1);
            HomeRefreshAgent.this.adapter.notifyMergeItemRangeChanged();
          }
        });
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeRefreshAgent
 * JD-Core Version:    0.6.0
 */