package com.dianping.widget.pulltorefresh.listview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class RotateListViewHeader extends ListViewHeader
{
  private RelativeLayout mContainer;
  private ImageView mHeaderImage;
  private Matrix mHeaderImageMatrix;
  private float mPivotX;
  private float mPivotY;
  private Animation mRotateAnimation;

  public RotateListViewHeader(Context paramContext)
  {
    super(paramContext);
    initView(paramContext);
  }

  public RotateListViewHeader(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private void initView(Context paramContext)
  {
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, 0);
    this.mContainer = ((RelativeLayout)LayoutInflater.from(paramContext).inflate(R.layout.listview_header_rotate, null));
    addView(this.mContainer, localLayoutParams);
    setGravity(80);
    this.mHeaderImage = ((ImageView)findViewById(R.id.listview_header_image));
    paramContext = getResources().getDrawable(getDefaultDrawableResId());
    this.mHeaderImage.setImageDrawable(paramContext);
    this.mHeaderImage.setScaleType(ImageView.ScaleType.MATRIX);
    this.mHeaderImageMatrix = new Matrix(this.mHeaderImage.getImageMatrix());
    this.mRotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate_alpha);
    this.mRotateAnimation.setInterpolator(new LinearInterpolator());
    onLoadingDrawableSet(paramContext);
  }

  protected int getDefaultDrawableResId()
  {
    return R.drawable.refresh_loading_small;
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
    this.mHeaderImageMatrix.setRotate(paramFloat * 360.0F, this.mPivotX, this.mPivotY);
    this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
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

  public void setState(int paramInt)
  {
    if (paramInt == this.mState)
      return;
    switch (paramInt)
    {
    case 0:
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      this.mState = paramInt;
      return;
      this.mHeaderImageMatrix.setScale(1.0F, 1.0F, this.mPivotX, this.mPivotY);
      this.mHeaderImage.setImageMatrix(this.mHeaderImageMatrix);
      continue;
      this.mHeaderImage.startAnimation(this.mRotateAnimation);
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
 * Qualified Name:     com.dianping.widget.pulltorefresh.listview.RotateListViewHeader
 * JD-Core Version:    0.6.0
 */