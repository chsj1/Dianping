package com.dianping.base.tuan.widget;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.base.tuan.adapter.GridLinearLayoutAdapter;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;

@Deprecated
public class GridLinearLayout extends LinearLayout
{
  protected GridLinearLayoutAdapter customTableAdapter;
  protected boolean displayDivider;
  protected int divider = R.drawable.gray_horizontal_line;
  protected int dividerLeftPadding = 5;
  protected int endDivider = R.drawable.gray_horizontal_line;
  protected Handler handler = new GridLinearLayout.2(this);
  protected Context mContext;
  protected final DataSetObserver observer = new GridLinearLayout.1(this);
  protected GridLinearLayout.OnItemClickListener onItemClickListener;

  public GridLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
    this.mContext = paramContext;
  }

  protected void drawDivider()
  {
    if ((this.displayDivider) && (this.divider > 0))
    {
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
      localLayoutParams.setMargins(ViewUtils.dip2px(this.mContext, this.dividerLeftPadding), 0, 0, 0);
      ImageView localImageView = new ImageView(this.mContext);
      localImageView.setScaleType(ImageView.ScaleType.FIT_XY);
      localImageView.setBackgroundResource(this.divider);
      addView(localImageView, localLayoutParams);
    }
  }

  protected void drawEndDivider()
  {
    if ((this.displayDivider) && (this.endDivider > 0))
    {
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
      ImageView localImageView = new ImageView(this.mContext);
      localImageView.setScaleType(ImageView.ScaleType.FIT_XY);
      localImageView.setBackgroundResource(this.endDivider);
      addView(localImageView, localLayoutParams);
    }
  }

  public GridLinearLayoutAdapter getAdapter()
  {
    return this.customTableAdapter;
  }

  public void refresh()
  {
    this.observer.onChanged();
  }

  public void setAdapter(GridLinearLayoutAdapter paramGridLinearLayoutAdapter)
  {
    if (this.customTableAdapter != null)
      this.customTableAdapter.unregisterDataSetObserver(this.observer);
    this.customTableAdapter = paramGridLinearLayoutAdapter;
    if (this.customTableAdapter != null)
      this.customTableAdapter.registerDataSetObserver(this.observer);
    this.observer.onChanged();
  }

  public void setDisplayDivider(boolean paramBoolean)
  {
    if (this.displayDivider == paramBoolean)
      return;
    this.displayDivider = paramBoolean;
    this.observer.onChanged();
  }

  public void setDivider(int paramInt)
  {
    try
    {
      getResources().getDrawable(paramInt);
      if (this.divider == paramInt)
        return;
      this.divider = paramInt;
      this.observer.onChanged();
      return;
    }
    catch (Exception localException)
    {
      while (true)
        this.divider = 0;
    }
  }

  public void setDividerLeftPadding(int paramInt)
  {
    this.dividerLeftPadding = paramInt;
  }

  public void setEndDivider(int paramInt)
  {
    try
    {
      getResources().getDrawable(paramInt);
      if (this.endDivider == paramInt)
        return;
      this.endDivider = paramInt;
      return;
    }
    catch (Exception localException)
    {
      this.endDivider = R.drawable.gray_horizontal_line;
    }
  }

  public void setOnItemClickListener(GridLinearLayout.OnItemClickListener paramOnItemClickListener)
  {
    if (this.onItemClickListener == paramOnItemClickListener)
      return;
    this.onItemClickListener = paramOnItemClickListener;
    refresh();
  }

  protected void setTable()
  {
    removeAllViews();
    int i;
    int i1;
    int i5;
    int i6;
    int i2;
    int i3;
    int j;
    if (this.customTableAdapter.getColumnWidth() > 0)
    {
      i = ViewUtils.dip2px(this.mContext, this.customTableAdapter.getColumnWidth());
      i1 = ViewUtils.dip2px(this.mContext, this.customTableAdapter.getHorizontalSpacing());
      i5 = ViewUtils.dip2px(this.mContext, this.customTableAdapter.getVerticalSpacing());
      i6 = this.customTableAdapter.getCount();
      i2 = this.customTableAdapter.getColumnCount();
      setOrientation(1);
      i3 = i6 / i2;
      j = 0;
    }
    label129: LinearLayout localLinearLayout;
    label168: int n;
    label300: label323: int k;
    while (true)
    {
      if (j >= i3)
        break label332;
      localObject1 = new LinearLayout.LayoutParams(-1, -2);
      int m;
      if (j == 0)
      {
        ((LinearLayout.LayoutParams)localObject1).setMargins(0, 0, 0, 0);
        localLinearLayout = new LinearLayout(getContext());
        localLinearLayout.setOrientation(0);
        addView(localLinearLayout, (ViewGroup.LayoutParams)localObject1);
        if (j != i3 - 1)
          drawDivider();
        m = 0;
        if (m >= i2)
          break label323;
        localObject2 = this.customTableAdapter.getView(j * i2 + m, null, this);
        if (i <= 0)
          break label300;
      }
      for (localObject1 = new LinearLayout.LayoutParams(i, -2); ; localObject1 = new LinearLayout.LayoutParams(0, -2, this.customTableAdapter.getWeight(j)))
      {
        if (m != 0)
          ((LinearLayout.LayoutParams)localObject1).setMargins(i1, 0, 0, 0);
        localLinearLayout.addView((View)localObject2, (ViewGroup.LayoutParams)localObject1);
        i4 = j * i2 + m;
        if ((this.onItemClickListener != null) && (this.customTableAdapter.isEnabled(i4)))
          ((View)localObject2).setOnClickListener(new GridLinearLayout.3(this, i4));
        m += 1;
        break label168;
        i = 0;
        break;
        ((LinearLayout.LayoutParams)localObject1).setMargins(0, i5, 0, 0);
        break label129;
      }
      j += 1;
    }
    label332: int i4 = i6 - i3 * i2;
    if (i4 == 0)
      drawEndDivider();
    label414: label548: 
    do
    {
      return;
      localObject1 = new LinearLayout.LayoutParams(-1, -2);
      if (i4 == i6)
      {
        ((LinearLayout.LayoutParams)localObject1).setMargins(0, 0, 0, 0);
        localLinearLayout = new LinearLayout(getContext());
        localLinearLayout.setWeightSum(i2);
        localLinearLayout.setOrientation(0);
        addView(localLinearLayout, (ViewGroup.LayoutParams)localObject1);
        n = 0;
        k = 0;
        if (k >= i4)
          continue;
        localObject2 = this.customTableAdapter.getView(i3 * i2 + k, null, this);
        if (i <= 0)
          break label548;
      }
      for (localObject1 = new LinearLayout.LayoutParams(i, -2); ; localObject1 = new LinearLayout.LayoutParams(0, -2, this.customTableAdapter.getWeight(i3 * i2 + k)))
      {
        if (k != 0)
          ((LinearLayout.LayoutParams)localObject1).setMargins(i1, 0, 0, 0);
        localLinearLayout.addView((View)localObject2, (ViewGroup.LayoutParams)localObject1);
        i5 = k + i3 * i2;
        if ((this.onItemClickListener != null) && (this.customTableAdapter.isEnabled(i5)))
          ((View)localObject2).setOnClickListener(new GridLinearLayout.4(this, i5));
        drawEndDivider();
        k += 1;
        break label414;
        ((LinearLayout.LayoutParams)localObject1).setMargins(0, i5, 0, 0);
        drawDivider();
        break;
        n = (int)(n + this.customTableAdapter.getWeight(i3 * i2 + k));
      }
    }
    while (n <= 0);
    Object localObject1 = new TextView(this.mContext);
    ((TextView)localObject1).setText("All");
    ((TextView)localObject1).setVisibility(4);
    Object localObject2 = new LinearLayout.LayoutParams(0, -2, i2 - n);
    ((LinearLayout.LayoutParams)localObject2).setMargins(i1, 0, 0, 0);
    localLinearLayout.addView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.widget.GridLinearLayout
 * JD-Core Version:    0.6.0
 */