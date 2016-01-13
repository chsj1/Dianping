package com.dianping.base.app.loader;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.dianping.adapter.MergeAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public abstract class AdapterAgentFragment extends AgentFragment
{
  private boolean isSetList;
  protected ListView listView;
  protected MergeAdapter mergeAdapter;
  private final Runnable notifyCellChanged = new Runnable()
  {
    public void run()
    {
      AgentFragment.handler.removeCallbacks(this);
      AdapterAgentFragment.this.updateAgentAdapter();
    }
  };

  public void addCell(CellAgent paramCellAgent, String paramString, ListAdapter paramListAdapter)
  {
    Cell localCell = new Cell();
    localCell.owner = paramCellAgent;
    localCell.name = paramString;
    localCell.adpater = paramListAdapter;
    this.cells.put(getCellName(paramCellAgent, paramString), localCell);
    notifyAdapterCellChanged();
  }

  protected void addCellToAdapterContainerView(String paramString, Cell paramCell)
  {
    this.mergeAdapter.addAdapter(paramCell.adpater);
  }

  public int getAdapterType()
  {
    return 0;
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
      if (paramCellAgent.adpater == null)
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
    this.mergeAdapter = new MergeAdapter();
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

  public void setAgentContainerListView(ListView paramListView)
  {
    this.isSetList = true;
    this.listView = paramListView;
  }

  protected void updateAgentAdapter()
  {
    Object localObject = new ArrayList(this.cells.values());
    Collections.sort((List)localObject, cellComparator);
    resetAgentAdapterContainerView();
    localObject = ((ArrayList)localObject).iterator();
    Cell localCell;
    String str;
    while (true)
      if (((Iterator)localObject).hasNext())
      {
        localCell = (Cell)((Iterator)localObject).next();
        if (localCell.adpater == null)
          continue;
        str = findHostForCell(localCell.owner);
        if (!TextUtils.isEmpty(str))
          break;
      }
    do
    {
      return;
      addCellToAdapterContainerView(str, localCell);
      break;
      this.mergeAdapter.notifyDataSetChanged();
    }
    while (!this.isSetList);
    this.listView.setAdapter(this.mergeAdapter);
    this.isSetList = false;
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
        if (localCell.adpater != null)
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
 * Qualified Name:     com.dianping.base.app.loader.AdapterAgentFragment
 * JD-Core Version:    0.6.0
 */