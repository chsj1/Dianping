package com.dianping.main.find.pictureplaza;

import com.dianping.base.app.loader.AdapterAgentFragment;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class PlazaAdapterAgentFragment extends AdapterAgentFragment
{
  private boolean isRefresh = false;
  private int refreshingAgentCount = 0;

  public void onAgentRefreshComplete()
  {
    if (this.isRefresh)
    {
      this.refreshingAgentCount -= 1;
      if (this.refreshingAgentCount <= 0)
      {
        this.isRefresh = false;
        onRefreshComplete();
      }
    }
  }

  public void onAgentRefreshStart()
  {
    if (this.isRefresh)
      this.refreshingAgentCount += 1;
  }

  abstract void onRefreshComplete();

  protected void startRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    if (getActivity() == null);
    while (true)
    {
      return;
      this.isRefresh = true;
      this.refreshingAgentCount = 0;
      paramPullToRefreshListView = this.agentList.iterator();
      while (paramPullToRefreshListView.hasNext())
      {
        Object localObject = (String)paramPullToRefreshListView.next();
        localObject = (CellAgent)this.agents.get(localObject);
        if (!(localObject instanceof PlazaAdapterCellAgent))
          continue;
        ((PlazaAdapterCellAgent)localObject).onRefresh();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaAdapterAgentFragment
 * JD-Core Version:    0.6.0
 */