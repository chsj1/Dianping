package com.dianping.main.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.layout;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeNewUserAdAgent extends HomeAgent
{
  private static final String NEW_USER_AD_TAG = "30NewUserAd";
  Adapter adapter;

  public HomeNewUserAdAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.adapter.notifyMergeItemRangeChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.adapter = new Adapter(null);
    addCell("30NewUserAd", this.adapter);
  }

  private class Adapter extends HomeAgent.HomeAgentAdapter
  {
    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      int i = 1;
      if (HomeNewUserAdAgent.this.getHomeData() == null);
      for (JSONArray localJSONArray = null; ; localJSONArray = HomeNewUserAdAgent.this.getHomeData().optJSONArray("bodyUnits"))
      {
        if ((localJSONArray == null) || (localJSONArray.length() != 1))
          i = 0;
        return i;
      }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      ((NewUserAdTemplate)((BasicRecyclerAdapter.BasicHolder)paramViewHolder).view).setNewUserAd(HomeNewUserAdAgent.this.getHomeData());
    }

    public BasicRecyclerAdapter.BasicHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new BasicRecyclerAdapter.BasicHolder(this, HomeNewUserAdAgent.this.res.inflate(HomeNewUserAdAgent.this.getContext(), R.layout.home_newuser_template, paramViewGroup, false));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeNewUserAdAgent
 * JD-Core Version:    0.6.0
 */