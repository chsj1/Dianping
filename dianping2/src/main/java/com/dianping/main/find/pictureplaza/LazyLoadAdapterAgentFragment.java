package com.dianping.main.find.pictureplaza;

import com.dianping.base.app.loader.CellAgent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class LazyLoadAdapterAgentFragment extends PlazaAdapterAgentFragment
{
  protected boolean isVisible = false;
  protected boolean isloaded = false;

  protected void loadData()
  {
    if (getActivity() != null)
    {
      Iterator localIterator = this.agentList.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (String)localIterator.next();
        localObject = (CellAgent)this.agents.get(localObject);
        if (!(localObject instanceof LazyLoadAdapterCellAgent))
          continue;
        ((LazyLoadAdapterCellAgent)localObject).requestData();
      }
    }
  }

  public void setUserVisibleHint(boolean paramBoolean)
  {
    super.setUserVisibleHint(paramBoolean);
    this.isVisible = paramBoolean;
    if ((!this.isloaded) && (paramBoolean))
    {
      loadData();
      this.isloaded = true;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.LazyLoadAdapterAgentFragment
 * JD-Core Version:    0.6.0
 */