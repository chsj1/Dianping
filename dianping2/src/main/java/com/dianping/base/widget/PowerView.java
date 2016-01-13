package com.dianping.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.dianping.v1.R.styleable;

public class PowerView extends LinearLayout
  implements View.OnClickListener
{
  private Drawable halfselected;
  private ImageView[] mImages;
  private OnPowerChangedListener mPowerChangedListener;
  private int power = 0;
  private boolean selecteable;
  private Drawable selected;
  private int size = 0;
  private Drawable unselected;
  private int weight = 1;

  public PowerView(Context paramContext)
  {
    super(paramContext);
  }

  public PowerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PowerView);
    this.selected = paramContext.getDrawable(R.styleable.PowerView_selected);
    this.unselected = paramContext.getDrawable(R.styleable.PowerView_unselected);
    this.halfselected = paramContext.getDrawable(R.styleable.PowerView_halfselected);
    this.weight = paramContext.getInt(R.styleable.PowerView_weight, 1);
    this.size = paramContext.getInt(R.styleable.PowerView_size, 0);
    this.selecteable = paramContext.getBoolean(R.styleable.PowerView_selectable, false);
    setSize(this.size);
    paramContext.recycle();
  }

  public int getPower()
  {
    return this.power;
  }

  public void onClick(View paramView)
  {
    paramView = paramView.getTag();
    if ((paramView instanceof Integer))
      setPower((((Integer)paramView).intValue() + 1) * this.weight);
  }

  public void setOnPowerChangedListener(OnPowerChangedListener paramOnPowerChangedListener)
  {
    this.mPowerChangedListener = paramOnPowerChangedListener;
  }

  public void setPower(int paramInt)
  {
    setSize(paramInt);
    int k = paramInt / this.weight;
    if (k < 0);
    label115: 
    do
    {
      return;
      if (paramInt % this.weight != 0);
      for (int i = 1; ; i = 0)
      {
        j = 0;
        while (j < k)
        {
          this.mImages[j].setImageDrawable(this.selected);
          j += 1;
        }
      }
      int j = k;
      if (j >= this.size)
        continue;
      Drawable localDrawable;
      if (j == k)
      {
        ImageView localImageView = this.mImages[j];
        if ((i != 0) && (this.halfselected != null))
        {
          localDrawable = this.halfselected;
          localImageView.setImageDrawable(localDrawable);
        }
      }
      while (true)
      {
        j += 1;
        break;
        localDrawable = this.unselected;
        break label115;
        if (this.unselected == null)
          continue;
        this.mImages[j].setImageDrawable(this.unselected);
      }
    }
    while (this.mPowerChangedListener == null);
    this.mPowerChangedListener.onPowerChanged(paramInt);
  }

  public void setSize(int paramInt)
  {
    removeAllViews();
    this.size = paramInt;
    this.mImages = new ImageView[paramInt];
    int i = 0;
    while (i < paramInt)
    {
      this.mImages[i] = new ImageView(getContext());
      if (this.unselected != null)
        this.mImages[i].setImageDrawable(this.unselected);
      if (this.selecteable)
      {
        this.mImages[i].setTag(Integer.valueOf(i));
        this.mImages[i].setOnClickListener(this);
      }
      i += 1;
    }
    i = 0;
    while (i < paramInt)
    {
      addView(this.mImages[i]);
      i += 1;
    }
  }

  public static abstract interface OnPowerChangedListener
  {
    public abstract void onPowerChanged(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.PowerView
 * JD-Core Version:    0.6.0
 */