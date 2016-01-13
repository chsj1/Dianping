package com.dianping.membercard.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract interface ViewHolder
{
  public abstract View getView();

  public abstract View inflate(Context paramContext, View paramView, ViewGroup paramViewGroup);

  public abstract void setView(View paramView);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.ViewHolder
 * JD-Core Version:    0.6.0
 */