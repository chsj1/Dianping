package com.dianping.main.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.layout;
import org.json.JSONObject;

public class HomePromotionAdAgent extends HomeAgent
{
  private static final String PROMOTION_AD_TAG = "28PromotionAd";
  private Adapter mAdapter;

  public HomePromotionAdAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.mAdapter.notifyMergeItemRangeChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mAdapter = new Adapter(null);
    addCell("28PromotionAd", this.mAdapter);
  }

  private class Adapter extends HomeAgent.HomeAgentAdapter
  {
    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      if ((HomePromotionAdAgent.this.getHomeData() != null) && (HomePromotionAdAgent.this.getHomeData().optJSONArray("promotionUnits") != null))
        return 1;
      return 0;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      ((PromotionAdTemplate)((BasicRecyclerAdapter.BasicHolder)paramViewHolder).view).SetPromotionAd(HomePromotionAdAgent.this.getHomeData());
    }

    public BasicRecyclerAdapter.BasicHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new BasicRecyclerAdapter.BasicHolder(this, HomePromotionAdAgent.this.res.inflate(HomePromotionAdAgent.this.getContext(), R.layout.home_promotionad_template, paramViewGroup, false));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomePromotionAdAgent
 * JD-Core Version:    0.6.0
 */