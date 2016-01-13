package com.dianping.shopinfo.wed.baby.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.base.widget.wed.WedBaseAdapter;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.layout;

public class BabyPriceAdapter extends WedBaseAdapter<String>
{
  private int cols;
  private boolean isExpand = false;
  private int itemWidth;
  private int remainder = 0;

  public BabyPriceAdapter(Context paramContext, String[] paramArrayOfString, int paramInt)
  {
    this.context = paramContext;
    this.adapterData = paramArrayOfString;
    this.cols = paramInt;
    int i = paramContext.getResources().getDisplayMetrics().widthPixels;
    this.remainder = ((i - ViewUtils.dip2px(paramContext, 30.0F) - 2) % paramInt);
    this.itemWidth = ((i - ViewUtils.dip2px(paramContext, 30.0F) - 2) / paramInt);
  }

  public int getCount()
  {
    if (this.isExpand)
      return ((String[])this.adapterData).length;
    return this.cols * 4;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    if (paramView == null)
    {
      paramView = LayoutInflater.from(this.context).inflate(R.layout.textview_babyprice_item, paramViewGroup, false);
      localView = paramView;
      if ((paramInt + 1) % this.cols == 0)
      {
        localView = paramView;
        if (paramView.getLayoutParams() != null)
        {
          paramView.getLayoutParams().width = (this.itemWidth + this.remainder);
          localView = paramView;
        }
      }
    }
    paramView = (TextView)localView;
    if (paramView != null)
    {
      paramView.setText(((String[])this.adapterData)[paramInt]);
      if (paramInt < this.cols)
        paramView.setTextColor(this.context.getResources().getColor(R.color.light_gray));
    }
    else
    {
      return localView;
    }
    paramView.setTextColor(this.context.getResources().getColor(R.color.deep_gray));
    return localView;
  }

  public boolean isExpand()
  {
    return this.isExpand;
  }

  public void setExpand(boolean paramBoolean)
  {
    this.isExpand = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.widget.BabyPriceAdapter
 * JD-Core Version:    0.6.0
 */