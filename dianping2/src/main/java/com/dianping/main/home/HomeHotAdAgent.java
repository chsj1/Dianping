package com.dianping.main.home;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import com.dianping.app.DPActivity;
import com.dianping.util.ThirdGaUtil.AdvertisementGa;
import com.dianping.v1.R.array;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeHotAdAgent extends HomeAgent
{
  public static final String HOTAD_TAG = "40Hotad";
  HotAdapter hotAdapter;
  private List<Boolean> markHotFlagList = new ArrayList();

  public HomeHotAdAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendCPMView(HotAdTemplate paramHotAdTemplate)
  {
    if (getContext() == null)
      return;
    TypedArray localTypedArray = getContext().getResources().obtainTypedArray(R.array.hotad_template_item);
    int i = 0;
    while (i < this.markHotFlagList.size())
    {
      if (((Boolean)this.markHotFlagList.get(i)).booleanValue())
      {
        HotAdItem localHotAdItem = (HotAdItem)paramHotAdTemplate.findViewById(localTypedArray.getResourceId(i, 0));
        if (GAHelper.instance().isViewOnScreen((DPActivity)getContext(), localHotAdItem))
        {
          record(3, localHotAdItem, i, localHotAdItem.feedback);
          this.markHotFlagList.set(i, Boolean.valueOf(false));
        }
      }
      i += 1;
    }
    localTypedArray.recycle();
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getDataChange()) && (getHomeData() != null))
    {
      paramBundle = getHomeData().optJSONArray("richUnits");
      if (paramBundle != null)
      {
        this.markHotFlagList.clear();
        int i = 0;
        while (i < paramBundle.length())
        {
          Object localObject = paramBundle.optJSONObject(i);
          record(1, localObject, i, ((JSONObject)localObject).optString("cpmFeedback"));
          this.markHotFlagList.add(i, Boolean.valueOf(true));
          localObject = ((JSONObject)localObject).optString("adViewUrl");
          if (!TextUtils.isEmpty((CharSequence)localObject))
            new AdvertisementGa().sendAdGA((String)localObject);
          i += 1;
        }
      }
    }
    this.hotAdapter.setHotItems(getHomeData());
    this.hotAdapter.notifyMergeItemRangeChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.hotAdapter = new HotAdapter(null);
    addCell("40Hotad", this.hotAdapter);
  }

  private class HotAdapter extends HomeAgent.HomeAgentAdapter
  {
    private JSONArray hotItems;

    private HotAdapter()
    {
      super();
    }

    private void setHotItems(JSONObject paramJSONObject)
    {
      if (paramJSONObject == null);
      for (paramJSONObject = null; ; paramJSONObject = paramJSONObject.optJSONArray("richUnits"))
      {
        this.hotItems = paramJSONObject;
        return;
      }
    }

    public int getCount()
    {
      if ((this.hotItems != null) && (this.hotItems.length() > 0))
        return 1;
      return 0;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      paramViewHolder = (BasicRecyclerAdapter.BasicHolder)paramViewHolder;
      ((HotAdTemplate)paramViewHolder.view).setHotAds(this.hotItems);
      new Handler().postDelayed(new Runnable(paramViewHolder)
      {
        public void run()
        {
          HomeHotAdAgent.this.sendCPMView((HotAdTemplate)this.val$hotAdHolder.view);
        }
      }
      , 500L);
    }

    public BasicRecyclerAdapter.BasicHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new BasicRecyclerAdapter.BasicHolder(this, new HotAdTemplate(HomeHotAdAgent.this.getContext()));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeHotAdAgent
 * JD-Core Version:    0.6.0
 */