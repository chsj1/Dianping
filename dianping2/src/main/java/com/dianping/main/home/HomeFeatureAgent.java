package com.dianping.main.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeFeatureAgent extends HomeAgent
{
  public static final String FEATURE_TAG = "35Hotad";
  private FeatureAdapter featureAdapter;

  public HomeFeatureAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.featureAdapter.setFeatureData(getHomeData());
    this.featureAdapter.notifyMergeItemRangeChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.featureAdapter = new FeatureAdapter(null);
    addCell("35Hotad", this.featureAdapter);
  }

  private class FeatureAdapter extends HomeAgent.HomeAgentAdapter
  {
    private JSONObject featureObject;

    private FeatureAdapter()
    {
      super();
    }

    private void setFeatureData(JSONObject paramJSONObject)
    {
      this.featureObject = paramJSONObject;
    }

    public int getCount()
    {
      if ((this.featureObject != null) && (this.featureObject.optJSONArray("bodyUnits") != null) && (this.featureObject.optJSONArray("bodyUnits").length() > 1))
        return 1;
      return 0;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      ((FeatureTemplate)((BasicRecyclerAdapter.BasicHolder)paramViewHolder).view).setFeatureObject(this.featureObject);
    }

    public BasicRecyclerAdapter.BasicHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new BasicRecyclerAdapter.BasicHolder(this, new FeatureTemplate(HomeFeatureAgent.this.getContext()));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeFeatureAgent
 * JD-Core Version:    0.6.0
 */