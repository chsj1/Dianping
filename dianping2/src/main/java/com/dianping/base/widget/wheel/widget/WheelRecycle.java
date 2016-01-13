package com.dianping.base.widget.wheel.widget;

import android.view.View;
import android.widget.LinearLayout;
import com.dianping.base.widget.wheel.adapter.WheelViewAdapter;
import java.util.LinkedList;
import java.util.List;
import java.util.List<Landroid.view.View;>;

public class WheelRecycle
{
  private List<View> emptyItems;
  private List<View> items;
  private WheelView wheel;

  public WheelRecycle(WheelView paramWheelView)
  {
    this.wheel = paramWheelView;
  }

  private List<View> addView(View paramView, List<View> paramList)
  {
    Object localObject = paramList;
    if (paramList == null)
      localObject = new LinkedList();
    ((List)localObject).add(paramView);
    return (List<View>)localObject;
  }

  private View getCachedView(List<View> paramList)
  {
    if ((paramList != null) && (paramList.size() > 0))
    {
      View localView = (View)paramList.get(0);
      paramList.remove(0);
      return localView;
    }
    return null;
  }

  private void recycleView(View paramView, int paramInt)
  {
    int j = this.wheel.getViewAdapter().getItemsCount();
    int i;
    if (paramInt >= 0)
    {
      i = paramInt;
      if (paramInt < j);
    }
    else
    {
      i = paramInt;
      if (!this.wheel.isCyclic())
      {
        this.emptyItems = addView(paramView, this.emptyItems);
        return;
      }
    }
    while (i < 0)
      i += j;
    this.items = addView(paramView, this.items);
  }

  public void clearAll()
  {
    if (this.items != null)
      this.items.clear();
    if (this.emptyItems != null)
      this.emptyItems.clear();
  }

  public View getEmptyItem()
  {
    return getCachedView(this.emptyItems);
  }

  public View getItem()
  {
    return getCachedView(this.items);
  }

  public int recycleItems(LinearLayout paramLinearLayout, int paramInt, ItemsRange paramItemsRange)
  {
    int j = paramInt;
    int k = 0;
    int i = paramInt;
    paramInt = j;
    j = k;
    if (j < paramLinearLayout.getChildCount())
    {
      int m;
      if (!paramItemsRange.contains(paramInt))
      {
        recycleView(paramLinearLayout.getChildAt(j), paramInt);
        paramLinearLayout.removeViewAt(j);
        k = j;
        m = i;
        if (j == 0)
        {
          m = i + 1;
          k = j;
        }
      }
      while (true)
      {
        paramInt += 1;
        j = k;
        i = m;
        break;
        k = j + 1;
        m = i;
      }
    }
    return i;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.wheel.widget.WheelRecycle
 * JD-Core Version:    0.6.0
 */