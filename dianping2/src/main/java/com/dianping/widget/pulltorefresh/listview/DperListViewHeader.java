package com.dianping.widget.pulltorefresh.listview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;

public class DperListViewHeader extends ListViewHeader
{
  static final int TOTAL_FRAME = 10;
  private ImageView mBackgroundImage;
  private LinearLayout mContainer;
  private ImageView mHeaderImage;
  private Matrix mHeaderImageMatrix;
  private TextView mHintTextView;
  private float mPivotX;
  private float mPivotY;

  public DperListViewHeader(Context paramContext)
  {
    super(paramContext);
    initView(paramContext);
  }

  public DperListViewHeader(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private void initView(Context paramContext)
  {
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 0);
    this.mContainer = ((LinearLayout)LayoutInflater.from(paramContext).inflate(R.layout.listview_header_dper, null));
    addView(this.mContainer, localLayoutParams);
    setGravity(80);
    this.mBackgroundImage = ((ImageView)findViewById(R.id.listview_header_background));
    this.mHeaderImage = ((ImageView)findViewById(R.id.listview_header_image));
    this.mHintTextView = ((TextView)findViewById(R.id.listview_header_hint_textview));
    paramContext = getResources().getDrawable(getDefaultDrawableResId());
    this.mHeaderImage.setImageDrawable(paramContext);
    this.mHeaderImage.setScaleType(ImageView.ScaleType.MATRIX);
    this.mHeaderImageMatrix = new Matrix(this.mHeaderImage.getImageMatrix());
    onLoadingDrawableSet(paramContext);
  }

  protected int getDefaultDrawableResId()
  {
    return R.drawable.dropdown_anim_00;
  }

  public int getVisiableHeight()
  {
    return this.mContainer.getLayoutParams().height;
  }

  public void onLoadingDrawableSet(Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      this.mPivotX = Math.round(paramDrawable.getIntrinsicWidth() / 2.0F);
      this.mPivotY = Math.round(paramDrawable.getIntrinsicHeight() / 2.0F);
    }
  }

  public void onPullImpl(float paramFloat)
  {
    this.mHeaderImageMatrix.setScale(paramFloat, paramFloat, this.mPivotX, this.mPivotY);
    this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
    this.mHeaderImage.setImageResource((int)(paramFloat / 1.0F * 10.0F) + getDefaultDrawableResId());
  }

  public void reset()
  {
    this.mHeaderImage.setImageResource(getDefaultDrawableResId());
    this.mHeaderImage.clearAnimation();
    if (this.mHeaderImageMatrix != null)
    {
      this.mHeaderImageMatrix.reset();
      this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
    }
  }

  public void setLoadingDrawable(Drawable paramDrawable)
  {
    this.mBackgroundImage.setBackgroundDrawable(paramDrawable);
  }

  public void setState(int paramInt)
  {
    if (paramInt == this.mState)
      return;
    switch (paramInt)
    {
    default:
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      this.mState = paramInt;
      return;
      this.mHintTextView.setText(R.string.listview_header_hint_normal);
      continue;
      this.mHeaderImageMatrix.setScale(1.0F, 1.0F, this.mPivotX, this.mPivotY);
      this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
      this.mHintTextView.setText(R.string.listview_header_hint_ready);
      continue;
      this.mHintTextView.setText(R.string.listview_header_hint_loading);
      this.mHeaderImage.setImageResource(R.anim.pull_loading);
      ((AnimationDrawable)this.mHeaderImage.getDrawable()).start();
    }
  }

  public void setVisiableHeight(int paramInt)
  {
    int i = paramInt;
    if (paramInt < 0)
      i = 0;
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.mContainer.getLayoutParams();
    localLayoutParams.height = i;
    this.mContainer.setLayoutParams(localLayoutParams);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.listview.DperListViewHeader
 * JD-Core Version:    0.6.0
 */