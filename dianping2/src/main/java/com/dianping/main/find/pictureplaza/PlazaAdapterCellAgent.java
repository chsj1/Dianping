package com.dianping.main.find.pictureplaza;

import com.dianping.base.app.loader.AdapterCellAgent;

public abstract class PlazaAdapterCellAgent extends AdapterCellAgent
{
  protected boolean isRefresh = false;
  PlazaAdapterAgentFragment plazaFragment;

  public PlazaAdapterCellAgent(Object paramObject)
  {
    super(paramObject);
    if ((this.fragment instanceof PlazaAdapterAgentFragment))
    {
      this.plazaFragment = ((PlazaAdapterAgentFragment)this.fragment);
      return;
    }
    throw new RuntimeException();
  }

  protected void onRefresh()
  {
    this.isRefresh = true;
  }

  protected void onRefreshComplete()
  {
    if (this.isRefresh)
    {
      this.isRefresh = false;
      this.plazaFragment.onAgentRefreshComplete();
    }
  }

  protected void onRefreshStart()
  {
    if (this.isRefresh)
      this.plazaFragment.onAgentRefreshStart();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaAdapterCellAgent
 * JD-Core Version:    0.6.0
 */