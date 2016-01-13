package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.dianping.app.DPApplication;
import com.dianping.statistics.StatisticsService;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class SegmentForRoute extends LinearLayout
{
  private Button[] buttons;
  int currentIndex;
  Button item1;
  Button item2;
  Button item3;
  OnSegmentClickListener mListener;

  public SegmentForRoute(Context paramContext)
  {
    super(paramContext);
  }

  public SegmentForRoute(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public int getCurrentItemIndex()
  {
    return this.currentIndex;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.item1 = ((Button)findViewById(R.id.item1));
    this.item2 = ((Button)findViewById(R.id.item2));
    this.item3 = ((Button)findViewById(R.id.item3));
    this.buttons = new Button[] { this.item1, this.item2, this.item3 };
    this.item1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (SegmentForRoute.this.currentIndex == 0);
        do
          return;
        while (SegmentForRoute.this.mListener == null);
        SegmentForRoute.this.currentIndex = 0;
        SegmentForRoute.this.updateSeletedState(SegmentForRoute.this.item1);
        SegmentForRoute.this.mListener.onSegmentItemClicked(0);
        ((StatisticsService)DPApplication.instance().getService("statistics")).event("shopinfo5", "shopinfo5_route_tab", "公交地铁", 0, null);
      }
    });
    this.item2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (SegmentForRoute.this.currentIndex == 1);
        do
          return;
        while (SegmentForRoute.this.mListener == null);
        SegmentForRoute.this.currentIndex = 1;
        SegmentForRoute.this.updateSeletedState(SegmentForRoute.this.item2);
        SegmentForRoute.this.mListener.onSegmentItemClicked(1);
        ((StatisticsService)DPApplication.instance().getService("statistics")).event("shopinfo5", "shopinfo5_route_tab", "驾车", 0, null);
      }
    });
    this.item3.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (SegmentForRoute.this.currentIndex == 2);
        do
          return;
        while (SegmentForRoute.this.mListener == null);
        SegmentForRoute.this.currentIndex = 2;
        SegmentForRoute.this.updateSeletedState(SegmentForRoute.this.item3);
        SegmentForRoute.this.mListener.onSegmentItemClicked(2);
        ((StatisticsService)DPApplication.instance().getService("statistics")).event("shopinfo5", "shopinfo5_route_tab", "步行", 0, null);
      }
    });
  }

  public void setCurrentItemIndex(int paramInt, boolean paramBoolean)
  {
    if (paramInt == 0)
    {
      this.currentIndex = 0;
      updateSeletedState(this.item1);
      if (paramBoolean)
        this.mListener.onSegmentItemClicked(0);
    }
    do
    {
      do
        while (true)
        {
          return;
          if (paramInt != 1)
            break;
          this.currentIndex = 1;
          updateSeletedState(this.item2);
          if (!paramBoolean)
            continue;
          this.mListener.onSegmentItemClicked(1);
          return;
        }
      while (paramInt != 2);
      this.currentIndex = 2;
      updateSeletedState(this.item3);
    }
    while (!paramBoolean);
    this.mListener.onSegmentItemClicked(2);
  }

  public void setOnSegmentClickListener(OnSegmentClickListener paramOnSegmentClickListener)
  {
    this.mListener = paramOnSegmentClickListener;
  }

  void updateSeletedState(Button paramButton)
  {
    Button[] arrayOfButton = this.buttons;
    int j = arrayOfButton.length;
    int i = 0;
    if (i < j)
    {
      Button localButton = arrayOfButton[i];
      if (paramButton == localButton)
      {
        localButton.setTextColor(getResources().getColor(R.color.light_red));
        localButton.setBackgroundResource(R.drawable.tab_bkg_selected);
      }
      while (true)
      {
        i += 1;
        break;
        localButton.setTextColor(getResources().getColor(R.color.black));
        localButton.setBackgroundResource(R.drawable.tab_bkg_line);
      }
    }
  }

  public static abstract interface OnSegmentClickListener
  {
    public abstract void onSegmentItemClicked(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.SegmentForRoute
 * JD-Core Version:    0.6.0
 */