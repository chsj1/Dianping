package com.dianping.base.widget.tagflow;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

public class TagView extends FrameLayout
  implements Checkable
{
  private static final int[] CHECK_STATE = { 16842912 };
  private boolean isChecked;

  public TagView(Context paramContext)
  {
    super(paramContext);
  }

  public View getTagView()
  {
    return getChildAt(0);
  }

  public boolean isChecked()
  {
    return this.isChecked;
  }

  public int[] onCreateDrawableState(int paramInt)
  {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (isChecked())
      mergeDrawableStates(arrayOfInt, CHECK_STATE);
    return arrayOfInt;
  }

  public void setChecked(boolean paramBoolean)
  {
    if (this.isChecked != paramBoolean)
    {
      this.isChecked = paramBoolean;
      refreshDrawableState();
    }
  }

  public void toggle()
  {
    if (!this.isChecked);
    for (boolean bool = true; ; bool = false)
    {
      setChecked(bool);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.tagflow.TagView
 * JD-Core Version:    0.6.0
 */