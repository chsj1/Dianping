package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class FatherTabViewPager extends ViewPager
{
  public FatherTabViewPager(Context paramContext)
  {
    super(paramContext);
  }

  public FatherTabViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramView != this) && ((paramView instanceof ViewPager)))
      return true;
    return super.canScroll(paramView, paramBoolean, paramInt1, paramInt2, paramInt3);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.FatherTabViewPager
 * JD-Core Version:    0.6.0
 */