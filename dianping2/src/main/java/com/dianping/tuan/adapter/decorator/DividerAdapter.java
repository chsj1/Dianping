package com.dianping.tuan.adapter.decorator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class DividerAdapter extends DecroatorAdapter
{
  protected ArrayList<DividerAdapter.ViewType> displayData = new ArrayList();
  protected HashMap<Integer, Integer> positionMap = new HashMap();

  public DividerAdapter(Context paramContext, ListAdapter paramListAdapter)
  {
    super(paramContext, paramListAdapter);
    parseData();
  }

  public int getCount()
  {
    return this.displayData.size();
  }

  protected int getDataPosition(int paramInt)
  {
    return ((Integer)this.positionMap.get(Integer.valueOf(paramInt))).intValue();
  }

  public abstract View getDividerAfter(int paramInt, View paramView, ViewGroup paramViewGroup);

  public abstract View getDividerBefore(int paramInt, View paramView, ViewGroup paramViewGroup);

  public abstract int getDividerTypeAfter(int paramInt);

  public abstract int getDividerTypeBefore(int paramInt);

  public abstract int getDividerTypeCount();

  public Object getItem(int paramInt)
  {
    if ((!this.displayData.isEmpty()) && (this.displayData.get(paramInt) == DividerAdapter.ViewType.VIEW))
      return this.mDataAdapter.getItem(getDataPosition(paramInt));
    return null;
  }

  public long getItemId(int paramInt)
  {
    if ((!this.displayData.isEmpty()) && (this.displayData.get(paramInt) == DividerAdapter.ViewType.VIEW))
      return this.mDataAdapter.getItemId(getDataPosition(paramInt));
    return 0L;
  }

  public int getItemViewType(int paramInt)
  {
    DividerAdapter.ViewType localViewType;
    if (!this.displayData.isEmpty())
      localViewType = (DividerAdapter.ViewType)this.displayData.get(paramInt);
    switch (DividerAdapter.1.$SwitchMap$com$dianping$tuan$adapter$decorator$DividerAdapter$ViewType[localViewType.ordinal()])
    {
    default:
      return 0;
    case 1:
      return getDividerTypeBefore(getDataPosition(paramInt));
    case 2:
      return getDividerTypeAfter(getDataPosition(paramInt));
    case 3:
    }
    return this.mDataAdapter.getItemViewType(getDataPosition(paramInt)) + getDividerTypeCount();
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = new View(this.context);
    DividerAdapter.ViewType localViewType;
    if (!this.displayData.isEmpty())
      localViewType = (DividerAdapter.ViewType)this.displayData.get(paramInt);
    switch (DividerAdapter.1.$SwitchMap$com$dianping$tuan$adapter$decorator$DividerAdapter$ViewType[localViewType.ordinal()])
    {
    default:
      return localView;
    case 1:
      return getDividerBefore(getDataPosition(paramInt), paramView, paramViewGroup);
    case 2:
      return getDividerAfter(getDataPosition(paramInt), paramView, paramViewGroup);
    case 3:
    }
    return this.mDataAdapter.getView(getDataPosition(paramInt), paramView, paramViewGroup);
  }

  public int getViewTypeCount()
  {
    return this.mDataAdapter.getViewTypeCount() + getDividerTypeCount();
  }

  public abstract boolean hasDividerAfter(int paramInt);

  public abstract boolean hasDividerBefore(int paramInt);

  public boolean hasStableIds()
  {
    return this.mDataAdapter.hasStableIds();
  }

  public boolean isEnabled(int paramInt)
  {
    return (!this.displayData.isEmpty()) && (this.displayData.get(paramInt) == DividerAdapter.ViewType.VIEW) && (this.mDataAdapter.isEnabled(getDataPosition(paramInt)));
  }

  public void onDataChanged()
  {
    parseData();
    notifyDataSetChanged();
  }

  public void onDataInvalidated()
  {
    parseData();
    notifyDataSetInvalidated();
  }

  protected void parseData()
  {
    int i = 0;
    this.displayData.clear();
    this.positionMap.clear();
    int j = 0;
    while (j < this.mDataAdapter.getCount())
    {
      int k = i;
      if (hasDividerBefore(j))
      {
        this.displayData.add(DividerAdapter.ViewType.DIVIDER_BEFORE);
        this.positionMap.put(Integer.valueOf(i), Integer.valueOf(j));
        k = i + 1;
      }
      this.displayData.add(DividerAdapter.ViewType.VIEW);
      this.positionMap.put(Integer.valueOf(k), Integer.valueOf(j));
      k += 1;
      i = k;
      if (hasDividerAfter(j))
      {
        this.displayData.add(DividerAdapter.ViewType.DIVIDER_AFTER);
        this.positionMap.put(Integer.valueOf(k), Integer.valueOf(j));
        i = k + 1;
      }
      j += 1;
    }
  }

  public void setDataAdapter(ListAdapter paramListAdapter)
  {
    parseData();
    super.setDataAdapter(paramListAdapter);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.adapter.decorator.DividerAdapter
 * JD-Core Version:    0.6.0
 */