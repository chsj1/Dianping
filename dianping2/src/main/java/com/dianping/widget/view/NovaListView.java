package com.dianping.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class NovaListView extends ListView
{
  public NovaListView(Context paramContext)
  {
    super(paramContext);
  }

  public NovaListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public NovaListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public boolean performItemClick(View paramView, int paramInt, long paramLong)
  {
    if (super.performItemClick(paramView, paramInt, paramLong))
    {
      if (GAHelper.instance().getIndexByView(paramView) == 2147483647)
        GAHelper.instance().statisticsEvent(paramView, paramInt, "tap");
      while (true)
      {
        return true;
        GAHelper.instance().statisticsEvent(paramView, "tap");
      }
    }
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.view.NovaListView
 * JD-Core Version:    0.6.0
 */