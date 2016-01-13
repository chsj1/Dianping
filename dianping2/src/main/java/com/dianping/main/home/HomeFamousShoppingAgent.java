package com.dianping.main.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeFamousShoppingAgent extends HomeAgent
{
  Adapter adapter;
  private boolean isDataChange;
  private FamousShopTemplate mTemplate;

  public HomeFamousShoppingAgent(Object paramObject)
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
    addCell("35FamousShop", this.adapter);
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
      int j = 1;
      int i;
      if ((HomeFamousShoppingAgent.this.getHomeData() == null) || (HomeFamousShoppingAgent.this.getHomeData().optJSONArray("homeHotActivities") == null))
        i = 0;
      int k;
      do
      {
        JSONObject localJSONObject;
        do
        {
          return i;
          localJSONObject = HomeFamousShoppingAgent.this.getHomeData().optJSONObject("leftTimerUnit");
          k = HomeFamousShoppingAgent.this.getHomeData().optJSONArray("homeHotActivities").length();
          if (k < 1)
            break;
          i = j;
        }
        while (localJSONObject != null);
        i = j;
      }
      while (k >= 2);
      return 0;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      if (HomeFamousShoppingAgent.this.isDataChange)
      {
        MainHomeFragment localMainHomeFragment = (MainHomeFragment)HomeFamousShoppingAgent.this.getFragment();
        if ((localMainHomeFragment != null) && (localMainHomeFragment.getRequestStatus() != 3))
          ((FamousShopTemplate)((BasicRecyclerAdapter.BasicHolder)paramViewHolder).view).setShopTemplate(HomeFamousShoppingAgent.this.getHomeData());
        HomeFamousShoppingAgent.access$202(HomeFamousShoppingAgent.this, false);
      }
    }

    public BasicRecyclerAdapter.BasicHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new BasicRecyclerAdapter.BasicHolder(new FamousShopTemplate(HomeFamousShoppingAgent.this.getContext()))
      {
        public void init(View paramView)
        {
          HomeFamousShoppingAgent.access$102(HomeFamousShoppingAgent.this, (FamousShopTemplate)paramView);
        }
      };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeFamousShoppingAgent
 * JD-Core Version:    0.6.0
 */