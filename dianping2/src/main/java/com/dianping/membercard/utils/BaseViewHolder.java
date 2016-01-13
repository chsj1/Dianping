package com.dianping.membercard.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class BaseViewHolder
  implements ViewHolder
{
  private View mView;

  public BaseViewHolder(View paramView)
  {
    this.mView = paramView;
  }

  public View getView()
  {
    return this.mView;
  }

  public View inflate(Context paramContext, View paramView, ViewGroup paramViewGroup)
  {
    return this.mView;
  }

  public void setView(View paramView)
  {
    this.mView = paramView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.BaseViewHolder
 * JD-Core Version:    0.6.0
 */