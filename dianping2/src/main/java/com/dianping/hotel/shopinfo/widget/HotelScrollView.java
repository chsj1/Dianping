package com.dianping.hotel.shopinfo.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import com.dianping.widget.MyScrollView;

public class HotelScrollView extends MyScrollView
{
  public HotelScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected int computeScrollDeltaToGetChildRectOnScreen(Rect paramRect)
  {
    return 0;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shopinfo.widget.HotelScrollView
 * JD-Core Version:    0.6.0
 */