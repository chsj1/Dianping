package com.dianping.base.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import com.dianping.adapter.BasicAdapter;
import com.dianping.base.widget.CustomGridView;

public abstract class CustomGridViewAdapter extends BasicAdapter
{
  public abstract int getColumnCount();

  public abstract View getItemView(int paramInt, View paramView, ViewGroup paramViewGroup);

  public final View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramInt % getColumnCount() == 0)
    {
      paramViewGroup = new TableRow(paramViewGroup.getContext());
      paramView = getItemView(paramInt, paramView, (TableRow)paramViewGroup);
      ((TableRow)paramViewGroup).addView(paramView);
    }
    while (true)
    {
      if ((paramInt == getCount() - 1) && (stretchLastItemView()))
      {
        TableRow.LayoutParams localLayoutParams = (TableRow.LayoutParams)paramView.getLayoutParams();
        localLayoutParams.span = ((getColumnCount() - getCount() % getColumnCount()) % getColumnCount() + 1);
        paramView.setLayoutParams(localLayoutParams);
      }
      return paramViewGroup;
      paramViewGroup = getItemView(paramInt, paramView, ((CustomGridView)paramViewGroup).getCurRow());
      paramView = paramViewGroup;
    }
  }

  public boolean stretchLastItemView()
  {
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.adapter.CustomGridViewAdapter
 * JD-Core Version:    0.6.0
 */