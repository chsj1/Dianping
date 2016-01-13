package com.dianping.membercard.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;

public class EmptyHeaderHolder
  implements ViewHolder
{
  public static int DEFAULT_HEIGHT = 20;
  private int mDpHeight;
  private View mView;

  public EmptyHeaderHolder(int paramInt)
  {
    this.mDpHeight = paramInt;
  }

  public View getView()
  {
    return this.mView;
  }

  public View inflate(Context paramContext, View paramView, ViewGroup paramViewGroup)
  {
    if (this.mView == null)
    {
      this.mView = new View(paramContext);
      paramContext = new AbsListView.LayoutParams(-1, ViewUtils.dip2px(paramContext, this.mDpHeight));
      this.mView.setLayoutParams(paramContext);
      this.mView.setBackgroundResource(R.drawable.main_background);
    }
    return this.mView;
  }

  public void setView(View paramView)
  {
    this.mView = paramView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.EmptyHeaderHolder
 * JD-Core Version:    0.6.0
 */