package com.dianping.base.tuan.fragment;

import android.text.TextUtils;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.base.app.loader.AdapterAgentFragment;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.agent.TuanAdapterCellAgent.OnCellRefreshListener;
import com.dianping.locationservice.LocationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class TuanAdapterAgentFragment extends AdapterAgentFragment
{
  public boolean isLogined()
  {
    if (getAccount() == null);
    do
      return false;
    while (TextUtils.isEmpty(accountService().token()));
    return true;
  }

  public boolean locationCare()
  {
    return true;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this instanceof CityConfig.SwitchListener))
      cityConfig().removeListener((CityConfig.SwitchListener)this);
    if ((this instanceof AccountListener))
      accountService().removeListener((AccountListener)this);
    locationService().removeListener(this);
  }

  public final void onRefresh()
  {
    if (getActivity() == null);
    while (true)
    {
      return;
      Iterator localIterator = this.agentList.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (String)localIterator.next();
        localObject = (CellAgent)this.agents.get(localObject);
        if (!(localObject instanceof TuanAdapterCellAgent.OnCellRefreshListener))
          continue;
        ((TuanAdapterCellAgent.OnCellRefreshListener)localObject).onRefresh();
      }
    }
  }

  public void onRefreshComplete()
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.fragment.TuanAdapterAgentFragment
 * JD-Core Version:    0.6.0
 */