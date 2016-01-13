package com.dianping.main.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeFeaturedShoppingAgent extends HomeAgent
{
  Adapter adapter;
  private boolean isDataChange;
  FeaturedShopTemplate mTemplate;

  public HomeFeaturedShoppingAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.isDataChange = getDataChange();
    if (((this.isDataChange) && (getHomeData() != null)) || (getHomeData() == null))
      this.adapter.notifyMergeItemRangeChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.adapter = new Adapter(null);
    addCell("35TripShop", this.adapter);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mTemplate != null)
      this.mTemplate.resetCount();
  }

  public void onPause()
  {
    super.onPause();
    if (this.mTemplate != null)
      this.mTemplate.stopCount();
  }

  public void onResume()
  {
    super.onResume();
    if (this.mTemplate != null)
      this.mTemplate.restartCount();
  }

  private class Adapter extends HomeAgent.HomeAgentAdapter
  {
    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      int j = 0;
      int i = j;
      if (HomeFeaturedShoppingAgent.this.getHomeData() != null)
      {
        i = j;
        if (HomeFeaturedShoppingAgent.this.getHomeData().optJSONObject("leftTimerUnit") != null)
        {
          i = j;
          if (HomeFeaturedShoppingAgent.this.getHomeData().optJSONArray("richUnits") != null)
          {
            i = j;
            if (HomeFeaturedShoppingAgent.this.getHomeData().optJSONArray("richUnits").length() >= 2)
              i = 1;
          }
        }
      }
      return i;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      if (HomeFeaturedShoppingAgent.this.isDataChange)
      {
        MainHomeFragment localMainHomeFragment = (MainHomeFragment)HomeFeaturedShoppingAgent.this.getFragment();
        if ((localMainHomeFragment != null) && (localMainHomeFragment.getRequestStatus() != 3))
          ((FeaturedShopTemplate)((BasicRecyclerAdapter.BasicHolder)paramViewHolder).view).setShopTemplate(HomeFeaturedShoppingAgent.this.getHomeData());
        HomeFeaturedShoppingAgent.access$102(HomeFeaturedShoppingAgent.this, false);
      }
    }

    public BasicRecyclerAdapter.BasicHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new BasicRecyclerAdapter.BasicHolder(new FeaturedShopTemplate(HomeFeaturedShoppingAgent.this.getContext()))
      {
        public void init(View paramView)
        {
          HomeFeaturedShoppingAgent.this.mTemplate = ((FeaturedShopTemplate)paramView);
        }
      };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeFeaturedShoppingAgent
 * JD-Core Version:    0.6.0
 */