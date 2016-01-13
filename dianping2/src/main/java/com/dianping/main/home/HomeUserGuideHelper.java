package com.dianping.main.home;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.NovaFragment;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;

public class HomeUserGuideHelper
{
  private Context mContext;
  private ViewGroup mContextView;
  private View mGuideBackground;
  private View mGuideContent;
  private ViewGroup mRootView;

  public HomeUserGuideHelper(Context paramContext, ViewGroup paramViewGroup1, ViewGroup paramViewGroup2)
  {
    this.mContext = paramContext;
    this.mContextView = paramViewGroup1;
    this.mRootView = paramViewGroup2;
  }

  public HomeUserGuideHelper(NovaActivity paramNovaActivity)
  {
    if (paramNovaActivity != null)
    {
      this.mContext = paramNovaActivity;
      this.mContextView = getContextView(paramNovaActivity);
      this.mRootView = getRootView(this.mContextView);
    }
  }

  public HomeUserGuideHelper(NovaFragment paramNovaFragment)
  {
    if ((paramNovaFragment != null) && (paramNovaFragment.getActivity() != null))
    {
      this.mContext = paramNovaFragment.getActivity();
      this.mContextView = getContextView((NovaActivity)this.mContext);
      this.mRootView = getRootView((ViewGroup)paramNovaFragment.getView());
    }
  }

  private ViewGroup getContextView(NovaActivity paramNovaActivity)
  {
    if (paramNovaActivity != null)
      return (ViewGroup)paramNovaActivity.getWindow().getDecorView();
    return null;
  }

  private ViewGroup getRootView(ViewGroup paramViewGroup)
  {
    if ((paramViewGroup != null) && (paramViewGroup.getChildCount() > 0))
      return (ViewGroup)paramViewGroup.getChildAt(0);
    return paramViewGroup;
  }

  public void hideGuide()
  {
    if (this.mGuideBackground != null)
      this.mContextView.removeView(this.mGuideBackground);
    if (this.mGuideContent != null)
      this.mRootView.removeView(this.mGuideContent);
  }

  public boolean showGuide(UserGuideImageParams paramUserGuideImageParams, int paramInt)
  {
    if ((this.mContext == null) || (this.mContextView == null) || (this.mRootView == null))
      return false;
    if ((paramUserGuideImageParams == null) || (paramUserGuideImageParams.resource == 0))
    {
      Log.e("invalid image params provided");
      return false;
    }
    hideGuide();
    View localView = new View(this.mContext);
    ImageView localImageView = new ImageView(this.mContext);
    localView.setLayoutParams(new FrameLayout.LayoutParams(ViewUtils.getScreenWidthPixels(this.mContext), ViewUtils.getScreenHeightPixels(this.mContext)));
    localView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HomeUserGuideHelper.this.hideGuide();
        HomeUserGuideHelper.access$102(HomeUserGuideHelper.this, null);
        HomeUserGuideHelper.access$202(HomeUserGuideHelper.this, null);
      }
    });
    localView.setBackgroundColor(paramInt);
    this.mContextView.addView(localView);
    localImageView.setImageResource(paramUserGuideImageParams.resource);
    localImageView.setScaleType(paramUserGuideImageParams.scaleType);
    localImageView.setScaleX(paramUserGuideImageParams.scaleX);
    localImageView.setScaleY(paramUserGuideImageParams.scaleY);
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
    paramInt = paramUserGuideImageParams.gravity;
    int i = paramUserGuideImageParams.gravity;
    switch (paramInt & 0x7)
    {
    default:
      localLayoutParams.addRule(9);
      localLayoutParams.leftMargin = paramUserGuideImageParams.leftMargin;
      switch (i & 0x70)
      {
      default:
        localLayoutParams.addRule(10);
        localLayoutParams.topMargin = paramUserGuideImageParams.topMargin;
      case 16:
      case 80:
      }
    case 1:
    case 5:
    }
    while (true)
    {
      this.mRootView.addView(localImageView, localLayoutParams);
      this.mGuideBackground = localView;
      this.mGuideContent = localImageView;
      return true;
      localLayoutParams.addRule(14);
      break;
      localLayoutParams.addRule(11);
      localLayoutParams.rightMargin = paramUserGuideImageParams.rightMargin;
      break;
      localLayoutParams.addRule(15);
      continue;
      localLayoutParams.addRule(12);
      localLayoutParams.bottomMargin = paramUserGuideImageParams.bottomMargin;
    }
  }

  public class UserGuideImageParams
  {
    public int bottomMargin;
    public int gravity;
    public int leftMargin;
    private int resource;
    public int rightMargin;
    public ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
    public float scaleX = 1.0F;
    public float scaleY = 1.0F;
    public int topMargin;

    public UserGuideImageParams(int arg2)
    {
      int i;
      this.resource = i;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeUserGuideHelper
 * JD-Core Version:    0.6.0
 */