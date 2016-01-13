package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.dianping.util.ViewUtils;

public class AddViewContainer extends LinearLayout
{
  private int availableWidth = ViewUtils.getScreenWidthPixels(getContext());

  public AddViewContainer(Context paramContext)
  {
    super(paramContext);
  }

  public AddViewContainer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void addView(View paramView)
  {
    Log.d("addviewcontainer", "container width before=" + ViewUtils.getViewWidth(this));
    if (ViewUtils.getViewWidth(this) < this.availableWidth)
    {
      super.addView(paramView);
      Log.d("addviewcontainer", "container width after=" + ViewUtils.getViewWidth(this));
      if (ViewUtils.getViewWidth(this) > this.availableWidth)
        removeView(paramView);
    }
  }

  public void setAvailableWidth(int paramInt)
  {
    this.availableWidth = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.AddViewContainer
 * JD-Core Version:    0.6.0
 */