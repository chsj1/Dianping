package com.dianping.base.app.loader;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import com.dianping.widget.recyclerview.MergeRecyclerAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public abstract class RecyclerAdapterAgentFreagment extends AgentFragment
{
  protected MergeRecyclerAdapter mergeAdapter;
  private final Runnable notifyCellChanged = new Runnable()
  {
    public void run()
    {
      AgentFragment.handler.removeCallbacks(this);
      RecyclerAdapterAgentFreagment.this.updateAgentAdapter();
    }
  };
  protected RecyclerView recyclerView;

  public void addCell(CellAgent paramCellAgent, String paramString, RecyclerView.Adapter paramAdapter)
  {
    Cell localCell = new Cell();
    localCell.owner = paramCellAgent;
    localCell.name = paramString;
    localCell.recyclerViewAdapter = paramAdapter;
    this.cells.put(getCellName(paramCellAgent, paramString), localCell);
    notifyAdapterCellChanged();
  }

  protected void addCellToAdapterContainerView(String paramString, Cell paramCell)
  {
    this.mergeAdapter.addAdapter(paramCell.recyclerViewAdapter);
  }

  protected void notifyAdapterCellChanged()
  {
    handler.removeCallbacks(this.notifyCellChanged);
    handler.post(this.notifyCellChanged);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    resetAdapter();
  }

  public void removeAllCells(CellAgent paramCellAgent)
  {
    Iterator localIterator = this.cells.entrySet().iterator();
    while (localIterator.hasNext())
    {
      if (((Cell)((Map.Entry)localIterator.next()).getValue()).owner != paramCellAgent)
        continue;
      localIterator.remove();
    }
    notifyCellChanged();
    notifyAdapterCellChanged();
  }

  public void removeCell(CellAgent paramCellAgent, String paramString)
  {
    if (hasCell(paramCellAgent, paramString))
    {
      paramCellAgent = (Cell)this.cells.get(getCellName(paramCellAgent, paramString));
      this.cells.remove(paramCellAgent);
      if (paramCellAgent.recyclerViewAdapter == null)
        notifyCellChanged();
    }
    else
    {
      return;
    }
    notifyAdapterCellChanged();
  }

  public void resetAdapter()
  {
    this.mergeAdapter = new MergeRecyclerAdapter();
  }

  protected void resetAgentAdapterContainerView()
  {
    this.mergeAdapter.clear();
  }

  public void resetAgents(Bundle paramBundle)
  {
    super.resetAgents(paramBundle);
    notifyAdapterCellChanged();
  }

  public void setAgentContainerRecyclerView(RecyclerView paramRecyclerView)
  {
    this.recyclerView = paramRecyclerView;
    paramRecyclerView.setAdapter(this.mergeAdapter);
  }

  protected void updateAgentAdapter()
  {
    Object localObject = new ArrayList(this.cells.values());
    Collections.sort((List)localObject, cellComparator);
    resetAgentAdapterContainerView();
    localObject = ((ArrayList)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      Cell localCell = (Cell)((Iterator)localObject).next();
      if (localCell.recyclerViewAdapter == null)
        continue;
      String str = findHostForCell(localCell.owner);
      if (TextUtils.isEmpty(str))
        return;
      addCellToAdapterContainerView(str, localCell);
    }
    this.mergeAdapter.notifyDataSetChanged();
  }

  protected void updateAgentContainer()
  {
    Object localObject = new ArrayList(this.cells.values());
    Collections.sort((List)localObject, cellComparator);
    resetAgentContainerView();
    localObject = ((ArrayList)localObject).iterator();
    while (true)
    {
      Cell localCell;
      String str;
      if (((Iterator)localObject).hasNext())
      {
        localCell = (Cell)((Iterator)localObject).next();
        if (localCell.recyclerViewAdapter != null)
          continue;
        str = findHostForCell(localCell.owner);
        if (!TextUtils.isEmpty(str));
      }
      else
      {
        return;
      }
      addCellToContainerView(str, localCell);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.RecyclerAdapterAgentFreagment
 * JD-Core Version:    0.6.0
 */