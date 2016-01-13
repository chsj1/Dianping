package com.dianping.base.adapter;

import android.widget.ListAdapter;
import com.dianping.adapter.MergeAdapter;
import com.dianping.base.app.loader.AdapterAgentFragment;
import java.util.ArrayList;
import java.util.Iterator;

public class TypeMergeAdapter extends MergeAdapter
{
  private AdapterAgentFragment fragment;

  public TypeMergeAdapter(AdapterAgentFragment paramAdapterAgentFragment)
  {
    if (paramAdapterAgentFragment == null)
      throw new RuntimeException("TypeMergeAdapter fragment == null");
    this.fragment = paramAdapterAgentFragment;
  }

  public int getItemViewType(int paramInt)
  {
    Iterator localIterator = this.pieces.iterator();
    while (localIterator.hasNext())
    {
      ListAdapter localListAdapter = (ListAdapter)localIterator.next();
      int i = localListAdapter.getCount();
      if (paramInt < i)
        return localListAdapter.getItemViewType(paramInt);
      paramInt -= i;
    }
    return 0;
  }

  public int getViewTypeCount()
  {
    return this.fragment.getAdapterType();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.adapter.TypeMergeAdapter
 * JD-Core Version:    0.6.0
 */