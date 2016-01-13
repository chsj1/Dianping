package com.dianping.main.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.MainBannerView;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.color;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeBannerAgent extends HomeAgent
{
  private Adapter adapter;

  public HomeBannerAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.adapter.setAnnounce(getHomeData());
    this.adapter.notifyMergeItemRangeChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.adapter = new Adapter(null);
    addCell("10Banner", this.adapter);
  }

  private class Adapter extends HomeAgent.HomeAgentAdapter
  {
    private JSONArray bannerList;

    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      if ((HomeBannerAgent.this.getFragment() != null) && (HomeBannerAgent.this.getContext() != null) && (MainBannerView.shouldShowAnnounces(this.bannerList, HomeBannerAgent.this.getFragment().preferences(HomeBannerAgent.this.getContext()))))
        return 1;
      return 0;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      paramViewHolder = (BasicRecyclerAdapter.BasicHolder)paramViewHolder;
      ((MainBannerView)paramViewHolder.view).setAnnounce(this.bannerList, HomeBannerAgent.this.getFragment().preferences(HomeBannerAgent.this.getContext()));
      if (this.bannerList.length() > 1)
        ((MainBannerView)paramViewHolder.view).startAutoFlip();
    }

    public BasicRecyclerAdapter.BasicHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new BasicRecyclerAdapter.BasicHolder(new MainBannerView(HomeBannerAgent.this.getContext()))
      {
        public void init(View paramView)
        {
          paramView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
          paramView.setBackgroundColor(HomeBannerAgent.this.getResources().getColor(R.color.common_bk_color));
          ((MainBannerView)paramView).setBannerCloseListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              HomeBannerAgent.Adapter.this.notifyMergeItemRangeChanged();
            }
          });
        }
      };
    }

    public void setAnnounce(JSONObject paramJSONObject)
    {
      this.bannerList = null;
      if (paramJSONObject != null);
      try
      {
        this.bannerList = paramJSONObject.optJSONObject("bannerInfo").optJSONArray("bannerItemList");
        return;
      }
      catch (java.lang.Exception paramJSONObject)
      {
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeBannerAgent
 * JD-Core Version:    0.6.0
 */