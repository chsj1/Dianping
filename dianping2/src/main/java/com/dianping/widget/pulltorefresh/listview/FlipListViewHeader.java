package com.dianping.widget.pulltorefresh.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class FlipListViewHeader extends ListViewHeader
{
  static final int FLIP_ANIMATION_DURATION = 150;
  private LinearLayout childView = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.listview_header_flip, this, false);
  private ImageView mFlipImageView = (ImageView)this.childView.findViewById(R.id.listview_header_image);
  private TextView mHintTextView = (TextView)this.childView.findViewById(R.id.pull_to_refresh_text);
  private ProgressBar mLoadProgressBar = (ProgressBar)this.childView.findViewById(R.id.pull_to_refresh_progress);
  private final Animation mResetRotateAnimation;
  private final Animation mRotateAnimation;

  public FlipListViewHeader(Context paramContext)
  {
    super(paramContext);
    this.mFlipImageView.setImageResource(getDefaultDrawableResId());
    this.mHintTextView.setText("继续拖动，返回产品详情");
    paramContext = new LinearLayout.LayoutParams(-1, 0);
    addView(this.childView, paramContext);
    this.mRotateAnimation = new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
    this.mRotateAnimation.setInterpolator(new LinearInterpolator());
    this.mRotateAnimation.setDuration(150L);
    this.mRotateAnimation.setFillAfter(true);
    this.mResetRotateAnimation = new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1, 0.5F);
    this.mResetRotateAnimation.setInterpolator(new LinearInterpolator());
    this.mResetRotateAnimation.setDuration(150L);
    this.mResetRotateAnimation.setFillAfter(true);
  }

  protected int getDefaultDrawableResId()
  {
    return R.drawable.load_flip_arrow;
  }

  public int getVisiableHeight()
  {
    return this.childView.getLayoutParams().height;
  }

  public void onPullImpl(float paramFloat)
  {
    if (this.mRotateAnimation == this.mFlipImageView.getAnimation())
      this.mFlipImageView.startAnimation(this.mResetRotateAnimation);
  }

  public void reset()
  {
    this.mFlipImageView.clearAnimation();
    this.mFlipImageView.setVisibility(0);
    this.mLoadProgressBar.setVisibility(8);
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
      this.mHintTextView.setText("继续拖动，返回产品详情");
      continue;
      this.mHintTextView.setText("释放拖动， 返回产品详情");
      this.mFlipImageView.startAnimation(this.mRotateAnimation);
      continue;
      this.mHintTextView.setText("正在加载产品详情...");
      this.mFlipImageView.clearAnimation();
      this.mFlipImageView.setVisibility(8);
      this.mLoadProgressBar.setVisibility(0);
    }
  }

  public void setVisiableHeight(int paramInt)
  {
    int i = paramInt;
    if (paramInt < 0)
      i = 0;
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.childView.getLayoutParams();
    localLayoutParams.height = i;
    this.childView.setLayoutParams(localLayoutParams);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.pulltorefresh.listview.FlipListViewHeader
 * JD-Core Version:    0.6.0
 */