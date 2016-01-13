package com.dianping.main.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import com.dianping.base.app.NovaActivity;
import com.dianping.loader.MyResources;
import com.dianping.util.ThirdGaUtil.AdvertisementGa;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeCompetitiveAgent extends HomeAgent
{
  public static final String COMPETITIVE_TAG = "45competitive.";
  public static int adapterTypeCount = 2;
  CompetitiveAdapter competitiveAdapter;
  private List<Boolean> markCompetitiveFlagList = new ArrayList();
  private String titleString = "精彩推荐";

  public HomeCompetitiveAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getDataChange()) && (getHomeData() != null))
    {
      paramBundle = getHomeData().optJSONArray("recomUnits");
      if (paramBundle != null)
      {
        this.markCompetitiveFlagList.clear();
        int i = 0;
        while (i < paramBundle.length())
        {
          Object localObject = paramBundle.optJSONObject(i);
          record(1, localObject, i, ((JSONObject)localObject).optString("cpmFeedback"));
          this.markCompetitiveFlagList.add(i, Boolean.valueOf(true));
          localObject = ((JSONObject)localObject).optString("adViewUrl");
          if (!TextUtils.isEmpty((CharSequence)localObject))
            new AdvertisementGa().sendAdGA((String)localObject);
          i += 1;
        }
      }
    }
    this.competitiveAdapter.setCompetitiveItems(getHomeData());
    this.competitiveAdapter.notifyMergeItemRangeChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.competitiveAdapter = new CompetitiveAdapter();
    addCell("45competitive.", this.competitiveAdapter);
  }

  class CompetitiveAdapter extends HomeAgent.HomeAgentAdapter
  {
    private JSONArray mList;
    int type_item = 1;
    int type_title = 0;

    CompetitiveAdapter()
    {
      super();
      this.type_title += getDefaultType();
      this.type_item += getDefaultType();
    }

    private void setCompetitiveItems(JSONObject paramJSONObject)
    {
      if (paramJSONObject == null);
      for (paramJSONObject = null; ; paramJSONObject = paramJSONObject.optJSONArray("recomUnits"))
      {
        this.mList = paramJSONObject;
        return;
      }
    }

    public int getCount()
    {
      if ((this.mList == null) || (this.mList.length() <= 0))
        return 0;
      return this.mList.length() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt == 0)
        return HomeCompetitiveAgent.this.titleString;
      return this.mList.optJSONObject(paramInt - 1);
    }

    public int getItemViewType(int paramInt)
    {
      if (paramInt == 0)
        return this.type_title;
      return this.type_item;
    }

    public int getViewTypeCount()
    {
      return HomeCompetitiveAgent.adapterTypeCount;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      int i = getItemViewType(paramInt);
      if (i == this.type_title)
        ((TitleHolder)paramViewHolder).title.setText((String)getItem(paramInt));
      do
        return;
      while (i != this.type_item);
      paramViewHolder = (BasicRecyclerAdapter.BasicHolder)paramViewHolder;
      HotCompetitiveItem localHotCompetitiveItem = (HotCompetitiveItem)paramViewHolder.view;
      JSONObject localJSONObject = (JSONObject)getItem(paramInt);
      if (paramInt == getItemCount() - 1);
      for (boolean bool = true; ; bool = false)
      {
        localHotCompetitiveItem.setCompetitiveAd(localJSONObject, bool, paramInt - 1);
        ((NovaActivity)HomeCompetitiveAgent.this.getContext()).addGAView(paramViewHolder.view, paramInt - 1, "home", "home".equals(((NovaActivity)HomeCompetitiveAgent.this.getContext()).getPageName()));
        if (((Boolean)HomeCompetitiveAgent.this.markCompetitiveFlagList.get(paramInt - 1)).booleanValue())
          break;
        HomeAgent.record(3, getItem(paramInt), paramInt - 1, ((JSONObject)getItem(paramInt)).optString("cpmFeedback"));
        HomeCompetitiveAgent.this.markCompetitiveFlagList.set(paramInt - 1, Boolean.valueOf(false));
        return;
      }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      TitleHolder localTitleHolder = null;
      if (paramInt == this.type_title)
        localTitleHolder = new TitleHolder(HomeCompetitiveAgent.this.res.inflate(HomeCompetitiveAgent.this.getContext(), R.layout.home_competitive_title, paramViewGroup, false));
      do
        return localTitleHolder;
      while (paramInt != this.type_item);
      return new BasicRecyclerAdapter.BasicHolder(this, HomeCompetitiveAgent.this.res.inflate(HomeCompetitiveAgent.this.getContext(), R.layout.home_competitive_list_item, paramViewGroup, false));
    }

    class TitleHolder extends BasicRecyclerAdapter.BasicHolder
    {
      TextView title;

      public TitleHolder(View arg2)
      {
        super(localView);
        this.title = ((TextView)localView.findViewById(R.id.title));
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeCompetitiveAgent
 * JD-Core Version:    0.6.0
 */