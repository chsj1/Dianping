package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.dianping.widget.view.NovaLinearLayout;

public abstract class UserProfileBaseItem extends NovaLinearLayout
{
  public UserProfileBaseItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public abstract String getTitle();

  public abstract boolean isRedMarkVisible();

  public abstract void setRedAlert(boolean paramBoolean, String paramString);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.UserProfileBaseItem
 * JD-Core Version:    0.6.0
 */