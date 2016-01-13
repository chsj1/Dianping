package com.dianping.base.app.loader;

import android.support.v7.widget.RecyclerView;

public class RecyclerAdapterCellAgent extends CellAgent
{
  public RecyclerAdapterCellAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void addCell(String paramString, RecyclerView.Adapter paramAdapter)
  {
    if ((this.fragment instanceof RecyclerAdapterAgentFreagment))
      ((RecyclerAdapterAgentFreagment)this.fragment).addCell(this, paramString, paramAdapter);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.RecyclerAdapterCellAgent
 * JD-Core Version:    0.6.0
 */