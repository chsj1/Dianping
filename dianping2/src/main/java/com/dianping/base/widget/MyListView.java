package com.dianping.base.widget;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.ListView;

public class MyListView extends ListView
{
  public MyListView(Context paramContext)
  {
    super(paramContext);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(536870911, -2147483648));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.MyListView
 * JD-Core Version:    0.6.0
 */