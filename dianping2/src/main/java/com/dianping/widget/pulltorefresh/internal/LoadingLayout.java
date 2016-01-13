package com.dianping.widget.pulltorefresh.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.v1.R.styleable;
import com.dianping.widget.pulltorefresh.ILoadingLayout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Orientation;

@SuppressLint({"ViewConstructor"})
public abstract class LoadingLayout extends FrameLayout
  implements ILoadingLayout
{
  static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
  static final String LOG_TAG = "PullToRefresh-LoadingLayout";
  protected final ImageView mHeaderImage;
  protected final ProgressBar mHeaderProgress;
  private final TextView mHeaderText;
  private FrameLayout mInnerLayout;
  protected final PullToRefreshBase.Mode mMode;
  private CharSequence mPullLabel;
  private CharSequence mRefreshingLabel;
  private CharSequence mReleaseLabel;
  protected final PullToRefreshBase.Orientation mScrollDirection;
  private final TextView mSubHeaderText;

  public LoadingLayout(Context paramContext, PullToRefreshBase.Mode paramMode, PullToRefreshBase.Orientation paramOrientation, TypedArray paramTypedArray)
  {
    super(paramContext);
    this.mMode = paramMode;
    this.mScrollDirection = paramOrientation;
    FrameLayout.LayoutParams localLayoutParams;
    int i;
    switch (1.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[paramOrientation.ordinal()])
    {
    default:
      LayoutInflater.from(paramContext).inflate(getDefaultVerticalLayout(), this);
      this.mInnerLayout = ((FrameLayout)findViewById(R.id.fl_inner));
      this.mHeaderText = ((TextView)this.mInnerLayout.findViewById(R.id.pull_to_refresh_text));
      this.mHeaderProgress = ((ProgressBar)this.mInnerLayout.findViewById(R.id.pull_to_refresh_progress));
      this.mSubHeaderText = ((TextView)this.mInnerLayout.findViewById(R.id.pull_to_refresh_sub_text));
      this.mHeaderImage = ((ImageView)this.mInnerLayout.findViewById(R.id.pull_to_refresh_image));
      localLayoutParams = (FrameLayout.LayoutParams)this.mInnerLayout.getLayoutParams();
      switch (1.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Mode[paramMode.ordinal()])
      {
      default:
        if (paramOrientation == PullToRefreshBase.Orientation.VERTICAL)
        {
          i = 80;
          label183: localLayoutParams.gravity = i;
          this.mPullLabel = paramContext.getString(R.string.pull_to_refresh_pull_label);
          this.mRefreshingLabel = paramContext.getString(R.string.pull_to_refresh_refreshing_label);
          this.mReleaseLabel = paramContext.getString(R.string.pull_to_refresh_release_label);
          if (paramTypedArray.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground))
          {
            paramOrientation = paramTypedArray.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
            if (paramOrientation != null)
              ViewCompat.setBackground(this, paramOrientation);
          }
          if (paramTypedArray.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance))
          {
            paramOrientation = new TypedValue();
            paramTypedArray.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance, paramOrientation);
            setTextAppearance(paramOrientation.data);
          }
          if (paramTypedArray.hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance))
          {
            paramOrientation = new TypedValue();
            paramTypedArray.getValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance, paramOrientation);
            setSubTextAppearance(paramOrientation.data);
          }
          if (paramTypedArray.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor))
          {
            paramOrientation = paramTypedArray.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
            if (paramOrientation != null)
              setTextColor(paramOrientation);
          }
          if (paramTypedArray.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor))
          {
            paramOrientation = paramTypedArray.getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
            if (paramOrientation != null)
              setSubTextColor(paramOrientation);
          }
          paramOrientation = null;
          if (paramTypedArray.hasValue(R.styleable.PullToRefresh_ptrDrawable))
            paramOrientation = paramTypedArray.getDrawable(R.styleable.PullToRefresh_ptrDrawable);
          switch (1.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Mode[paramMode.ordinal()])
          {
          default:
            if (paramTypedArray.hasValue(R.styleable.PullToRefresh_ptrDrawableStart))
              paramMode = paramTypedArray.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
          case 1:
          }
        }
      case 1:
      }
    case 1:
    }
    while (true)
    {
      paramOrientation = paramMode;
      if (paramMode == null)
        paramOrientation = paramContext.getResources().getDrawable(getDefaultDrawableResId());
      setLoadingDrawable(paramOrientation);
      reset();
      return;
      LayoutInflater.from(paramContext).inflate(R.layout.pull_to_refresh_header_horizontal, this);
      break;
      if (paramOrientation == PullToRefreshBase.Orientation.VERTICAL);
      for (i = 48; ; i = 3)
      {
        localLayoutParams.gravity = i;
        this.mPullLabel = paramContext.getString(R.string.pull_to_refresh_from_bottom_pull_label);
        this.mRefreshingLabel = paramContext.getString(R.string.pull_to_refresh_from_bottom_refreshing_label);
        this.mReleaseLabel = paramContext.getString(R.string.pull_to_refresh_from_bottom_release_label);
        break;
      }
      i = 5;
      break label183;
      paramMode = paramOrientation;
      if (!paramTypedArray.hasValue(R.styleable.PullToRefresh_ptrDrawableTop))
        continue;
      Utils.warnDeprecation("ptrDrawableTop", "ptrDrawableStart");
      paramMode = paramTypedArray.getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
      continue;
      if (paramTypedArray.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd))
      {
        paramMode = paramTypedArray.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
        continue;
      }
      paramMode = paramOrientation;
      if (!paramTypedArray.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom))
        continue;
      Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
      paramMode = paramTypedArray.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
    }
  }

  private void setSubHeaderText(CharSequence paramCharSequence)
  {
    if (this.mSubHeaderText != null)
    {
      if (!TextUtils.isEmpty(paramCharSequence))
        break label24;
      this.mSubHeaderText.setVisibility(8);
    }
    label24: 
    do
    {
      return;
      this.mSubHeaderText.setText(paramCharSequence);
    }
    while (8 != this.mSubHeaderText.getVisibility());
    this.mSubHeaderText.setVisibility(0);
  }

  private void setSubTextAppearance(int paramInt)
  {
    if (this.mSubHeaderText != null)
      this.mSubHeaderText.setTextAppearance(getContext(), paramInt);
  }

  private void setSubTextColor(ColorStateList paramColorStateList)
  {
    if (this.mSubHeaderText != null)
      this.mSubHeaderText.setTextColor(paramColorStateList);
  }

  private void setTextAppearance(int paramInt)
  {
    if (this.mHeaderText != null)
      this.mHeaderText.setTextAppearance(getContext(), paramInt);
    if (this.mSubHeaderText != null)
      this.mSubHeaderText.setTextAppearance(getContext(), paramInt);
  }

  private void setTextColor(ColorStateList paramColorStateList)
  {
    if (this.mHeaderText != null)
      this.mHeaderText.setTextColor(paramColorStateList);
    if (this.mSubHeaderText != null)
      this.mSubHeaderText.setTextColor(paramColorStateList);
  }

  public final int getContentSize()
  {
    switch (1.$SwitchMap$com$dianping$widget$pulltorefresh$PullToRefreshBase$Orientation[this.mScrollDirection.ordinal()])
    {
    default:
      return this.mInnerLayout.getHeight();
    case 1:
    }
    return this.mInnerLayout.getWidth();
  }

  protected abstract int getDefaultDrawableResId();

  protected int getDefaultVerticalLayout()
  {
    return R.layout.pull_to_refresh_header_vertical;
  }

  public final void hideAllViews()
  {
    if (this.mHeaderText.getVisibility() == 0)
      this.mHeaderText.setVisibility(4);
    if (this.mHeaderProgress.getVisibility() == 0)
      this.mHeaderProgress.setVisibility(4);
    if (this.mHeaderImage.getVisibility() == 0)
      this.mHeaderImage.setVisibility(4);
    if (this.mSubHeaderText.getVisibility() == 0)
      this.mSubHeaderText.setVisibility(4);
  }

  protected abstract void onLoadingDrawableSet(Drawable paramDrawable);

  public final void onPull(float paramFloat)
  {
    onPullImpl(paramFloat);
  }

  protected abstract void onPullImpl(float paramFloat);

  public final void pullToRefresh()
  {
    if (this.mHeaderText != null)
      this.mHeaderText.setText(this.mPullLabel);
    pullToRefreshImpl();
  }

  protected abstract void pullToRefreshImpl();

  public final void refreshing()
  {
    if (this.mHeaderText != null)
      this.mHeaderText.setText(this.mRefreshingLabel);
    refreshingImpl();
    if (this.mSubHeaderText != null)
      this.mSubHeaderText.setVisibility(8);
  }

  protected abstract void refreshingImpl();

  public final void releaseToRefresh()
  {
    if (this.mHeaderText != null)
      this.mHeaderText.setText(this.mReleaseLabel);
    releaseToRefreshImpl();
  }

  protected abstract void releaseToRefreshImpl();

  public final void reset()
  {
    if (this.mHeaderText != null)
      this.mHeaderText.setText(this.mPullLabel);
    this.mHeaderImage.setVisibility(0);
    resetImpl();
    if (this.mSubHeaderText != null)
    {
      if (TextUtils.isEmpty(this.mSubHeaderText.getText()))
        this.mSubHeaderText.setVisibility(8);
    }
    else
      return;
    this.mSubHeaderText.setVisibility(0);
  }

  protected abstract void resetImpl();

  public void setBackgroundColor(int paramInt)
  {
    super.setBackgroundColor(paramInt);
  }

  public final void setHeight(int paramInt)
  {
    getLayoutParams().height = paramInt;
    requestLayout();
  }

  public void setLastUpdatedLabel(CharSequence paramCharSequence)
  {
    setSubHeaderText(paramCharSequence);
  }

  public final void setLoadingDrawable(Drawable paramDrawable)
  {
    this.mHeaderImage.setImageDrawable(paramDrawable);
    onLoadingDrawableSet(paramDrawable);
  }

  public void setLoadingLayoutBackground(Drawable paramDrawable)
  {
    ((ImageView)findViewById(R.id.image_background)).setImageDrawable(paramDrawable);
  }

  public void setPullLabel(CharSequence paramCharSequence)
  {
    this.mPullLabel = paramCharSequence;
  }

  public void setRefreshingLabel(CharSequence paramCharSequence)
  {
    this.mRefreshingLabel = paramCharSequence;
  }

  public void setReleaseLabel(CharSequence paramCharSequence)
  {
    this.mReleaseLabel = paramCharSequence;
  }

  public void setTextTypeface(Typeface paramTypeface)
  {
    this.mHeaderText.setTypeface(paramTypeface);
  }

  public final void setWidth(int paramInt)
  {
    getLayoutParams().width = paramInt;
    requestLayout();
  }

  public final void showInvisibleViews()
  {
    if (4 == this.mHeaderText.getVisibility())
      this.mHeaderText.setVisibility(0);
    if (4 == this.mHeaderProgress.getVisibility())
      this.mHeaderProgress.setVisibility(0);
    if (4 == this.mHeaderImage.getVisibility())
      this.mHeaderImage.setVisibility(0);
    if (4 == this.mSubHeaderText.getVisibility())
      this.mSubHeaderText.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.internal.LoadingLayout
 * JD-Core Version:    0.6.0
 */