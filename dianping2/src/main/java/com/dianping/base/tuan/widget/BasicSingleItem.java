package com.dianping.base.tuan.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;

public class BasicSingleItem extends LinearLayout
{
  protected TextView mCountTextView;
  protected NetworkThumbView mIconImageView;
  protected ImageView mMoreImageView;
  protected TextView mSubTitleTextView;
  protected TextView mTitleTextView;

  public BasicSingleItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public BasicSingleItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    inflateLayout(paramContext);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.BasicSingleItem);
    int i = paramContext.getResourceId(R.styleable.BasicSingleItem_item_icon, 0);
    paramAttributeSet = paramContext.getString(R.styleable.BasicSingleItem_item_title);
    int j = (int)TypedValue.applyDimension(2, 18.0F, getResources().getDisplayMetrics());
    j = paramContext.getDimensionPixelSize(R.styleable.BasicSingleItem_item_titleSize, j);
    int k = paramContext.getColor(R.styleable.BasicSingleItem_item_titleColor, -13487566);
    String str1 = paramContext.getString(R.styleable.BasicSingleItem_item_subTitle);
    int m = (int)TypedValue.applyDimension(2, 16.0F, getResources().getDisplayMetrics());
    m = paramContext.getDimensionPixelSize(R.styleable.BasicSingleItem_item_subTitleSize, m);
    int n = paramContext.getColor(R.styleable.BasicSingleItem_item_subTitleColor, -7895161);
    String str2 = paramContext.getString(R.styleable.BasicSingleItem_item_count);
    int i1 = (int)TypedValue.applyDimension(2, 18.0F, getResources().getDisplayMetrics());
    i1 = paramContext.getDimensionPixelSize(R.styleable.BasicSingleItem_item_countSize, i1);
    int i2 = paramContext.getColor(R.styleable.BasicSingleItem_item_countColor, -7895161);
    int i3 = paramContext.getResourceId(R.styleable.BasicSingleItem_item_indicator, 0);
    paramContext.recycle();
    this.mIconImageView = ((NetworkThumbView)findViewById(R.id.s_icon));
    setLeftImageView(i);
    this.mTitleTextView = ((TextView)findViewById(R.id.s_title));
    setTitle(paramAttributeSet);
    setTitleSize(j);
    setTitleColor(k);
    this.mSubTitleTextView = ((TextView)findViewById(R.id.s_subtitle));
    setSubTitle(str1);
    setSubTitleSize(m);
    setSubTitleColor(n);
    this.mCountTextView = ((TextView)findViewById(R.id.s_count));
    setCount(str2);
    setCountColor(i2);
    setCountSize(i1);
    this.mMoreImageView = ((ImageView)findViewById(R.id.s_more));
    setIndicator(i3);
  }

  public void clearTitle()
  {
    setTitle(null);
    setSubTitle(null);
  }

  public TextView getCountView()
  {
    return this.mCountTextView;
  }

  public NetworkThumbView getLeftImageView()
  {
    return this.mIconImageView;
  }

  public ImageView getRightImageView()
  {
    return this.mMoreImageView;
  }

  public TextView getSubTitleView()
  {
    return this.mSubTitleTextView;
  }

  public TextView getTitleView()
  {
    return this.mTitleTextView;
  }

  protected void inflateLayout(Context paramContext)
  {
    inflate(paramContext, R.layout.basic_single_item, this);
  }

  public boolean isLeftImageViewEnabled()
  {
    return this.mIconImageView.isEnabled();
  }

  public boolean isLeftImageViewSelected()
  {
    return this.mIconImageView.isSelected();
  }

  public boolean isRightImageViewSelected()
  {
    return this.mMoreImageView.isSelected();
  }

  public boolean isRightViewImageEnabled()
  {
    return this.mMoreImageView.isEnabled();
  }

  public void setClickable(boolean paramBoolean)
  {
    super.setClickable(paramBoolean);
    setLeftImageViewEnable(paramBoolean);
    setRightImageViewEnable(paramBoolean);
  }

  public void setCount(CharSequence paramCharSequence)
  {
    this.mCountTextView.setText(paramCharSequence);
    if (TextUtils.isEmpty(paramCharSequence))
    {
      this.mCountTextView.setVisibility(8);
      return;
    }
    this.mCountTextView.setVisibility(0);
  }

  public void setCountColor(int paramInt)
  {
    this.mCountTextView.setTextColor(paramInt);
  }

  public void setCountSize(int paramInt)
  {
    this.mCountTextView.setTextSize(0, paramInt);
  }

  public void setImageByUrl(String paramString)
  {
    this.mIconImageView.setImage(paramString);
    this.mIconImageView.setVisibility(0);
  }

  public void setIndicator(int paramInt)
  {
    if (paramInt > 0)
      this.mMoreImageView.setImageResource(paramInt);
    ImageView localImageView = this.mMoreImageView;
    if (paramInt == 0);
    for (paramInt = 8; ; paramInt = 0)
    {
      localImageView.setVisibility(paramInt);
      return;
    }
  }

  public void setIndicator(Drawable paramDrawable)
  {
    this.mMoreImageView.setImageDrawable(paramDrawable);
  }

  public void setLeftImageView(int paramInt)
  {
    if (paramInt > 0)
      this.mIconImageView.setLocalDrawable(getResources().getDrawable(paramInt));
    NetworkThumbView localNetworkThumbView = this.mIconImageView;
    if (paramInt == 0);
    for (paramInt = 8; ; paramInt = 0)
    {
      localNetworkThumbView.setVisibility(paramInt);
      return;
    }
  }

  public void setLeftImageView(Drawable paramDrawable)
  {
    this.mIconImageView.setLocalDrawable(paramDrawable);
  }

  public void setLeftImageViewEnable(boolean paramBoolean)
  {
    this.mIconImageView.setEnabled(paramBoolean);
  }

  public void setLeftImageViewSelected(boolean paramBoolean)
  {
    this.mIconImageView.setSelected(paramBoolean);
  }

  public void setRightImageViewEnable(boolean paramBoolean)
  {
    this.mMoreImageView.setEnabled(paramBoolean);
  }

  public void setRightImageViewSelected(boolean paramBoolean)
  {
    this.mMoreImageView.setSelected(paramBoolean);
  }

  public void setSubTitle(CharSequence paramCharSequence)
  {
    this.mSubTitleTextView.setText(paramCharSequence);
    if (TextUtils.isEmpty(paramCharSequence))
    {
      this.mSubTitleTextView.setVisibility(8);
      return;
    }
    this.mSubTitleTextView.setVisibility(0);
  }

  public void setSubTitleColor(int paramInt)
  {
    this.mSubTitleTextView.setTextColor(paramInt);
  }

  public void setSubTitleSize(int paramInt)
  {
    this.mSubTitleTextView.setTextSize(0, paramInt);
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    this.mTitleTextView.setText(paramCharSequence);
  }

  public void setTitleColor(int paramInt)
  {
    this.mTitleTextView.setTextColor(paramInt);
  }

  public void setTitleSize(int paramInt)
  {
    this.mTitleTextView.setTextSize(0, paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.widget.BasicSingleItem
 * JD-Core Version:    0.6.0
 */