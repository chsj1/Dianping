package com.dianping.main.home;

import android.os.Bundle;

import com.dianping.adapter.BasicRecyclerAdapter;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.app.CityConfig;
import com.dianping.base.app.loader.RecyclerAdapterCellAgent;
import com.dianping.model.City;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class HomeAgent extends RecyclerAdapterCellAgent
  implements CityConfig.SwitchListener
{
  public static final int AD_GA_CLICK = 2;
  public static final int AD_GA_REVEAL = 3;
  public static final int AD_GA_SHOW = 1;
  public static int adapterTypeCount = 1;
  boolean isRefresh = false;
  public int isRequestStatus = 0;
  private MainHomeFragment mainHomeFragment;

  public HomeAgent(Object paramObject)
  {
    super(paramObject);
    if ((this.fragment instanceof MainHomeFragment))
    {
      this.mainHomeFragment = ((MainHomeFragment)this.fragment);
      return;
    }
    throw new RuntimeException();
  }

  public static void record(int paramInt1, Object paramObject, int paramInt2, String paramString)
  {
    if (paramObject == null)
      return;
    paramObject = new HashMap();
    paramObject.put("act", String.valueOf(paramInt1));
    paramObject.put("adidx", String.valueOf(paramInt2 + 1));
    AdClientUtils.report(paramString, paramObject);
  }

  public boolean getDataChange()
  {
    return this.mainHomeFragment.dataChanged;
  }

  public JSONObject getHomeData()
  {
    return (JSONObject)this.mainHomeFragment.homeData.get(this.hostName);
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    onRefreshComplete();
  }

  public void onRefreshComplete()
  {
    if (this.isRefresh)
    {
      this.mainHomeFragment.onRefreshComplete();
      this.isRefresh = false;
    }
  }

  public void onRefreshRequest()
  {
    if (this.isRefresh)
      this.mainHomeFragment.onRefreshRequest();
  }

  public void sendRefreshShowMessage()
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("flag", "refreshflag");
    dispatchAgentChanged("home/HomeRetrySection", localBundle);
  }

  public boolean showRetry()
  {
    return false;
  }

  public abstract class HomeAgentAdapter extends BasicRecyclerAdapter
  {
    public int mOldCount = 0;

    public HomeAgentAdapter()
    {
    }

    public abstract int getCount();

    public int getDefaultType()
    {
      String str = HomeAgent.this.hostName;
      if (HomeAgent.this.hostName.indexOf(";") != -1)
        str = HomeAgent.this.hostName.substring(0, HomeAgent.this.hostName.indexOf(";"));
      return ((Integer)MainHomeFragment.agentAdpaterTypeMap.get(str)).intValue();
    }

    public final int getItemCount()
    {
      this.mOldCount = getCount();
      return this.mOldCount;
    }

    public int getItemViewType(int paramInt)
    {
      return getDefaultType();
    }

    public int getViewTypeCount()
    {
      return HomeAgent.adapterTypeCount;
    }

    public void notifyMergeItemRangeChanged()
    {
      int i = 0;
      Iterator localIterator = HomeAgent.this.mainHomeFragment.getMergeAdapter().iterator();
      int j;
      while (true)
      {
        HomeAgentAdapter localHomeAgentAdapter;
        if (localIterator.hasNext())
        {
          localHomeAgentAdapter = (HomeAgentAdapter)localIterator.next();
          if (localHomeAgentAdapter != this);
        }
        else
        {
          j = this.mOldCount;
          if (j != getItemCount())
            break;
          notifyItemRangeChanged(i, getItemCount());
          return;
        }
        i += localHomeAgentAdapter.getItemCount();
      }
      if (j < getItemCount())
        notifyItemRangeInserted(getItemCount() + i, getItemCount() - j);
      while (true)
      {
        notifyItemRangeChanged(i, getItemCount());
        return;
        notifyItemRangeRemoved(getItemCount() + i, j - getItemCount());
      }
    }
  }

  public static abstract interface OnCellRefreshListener
  {
    public abstract void onRefresh();
  }

  public static abstract interface OnCellRetryListener
  {
    public abstract void onRetry();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeAgent
 * JD-Core Version:    0.6.0
 */