package com.dianping.main.guide.guidance;

import android.content.Context;
import android.view.LayoutInflater;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.NavigationDot;

public class GuidanceNewComerActivity extends GuidanceActivity
{
  private NavigationDot mNavigationDot;

  public String getPageName()
  {
    return "newguidance";
  }

  public void setupView()
  {
    this.mFlipper = new NewComerGuidanceFlipper(this);
    this.mBackgrounds = new int[] { R.drawable.new_comer_guide_bg, R.drawable.new_comer_guide_bg, R.drawable.new_comer_guide_bg, R.drawable.new_comer_guide_bg };
    this.mForgrounds = new int[] { R.drawable.new_comer_guide_fg_1, R.drawable.new_comer_guide_fg_2, R.drawable.new_comer_guide_fg_3, R.drawable.new_comer_guide_fg_4 };
    this.mViewCount = this.mBackgrounds.length;
    this.mImageAdapter = new AutoFlipImageAdapter(this);
  }

  class AutoFlipImageAdapter extends GuidanceActivity.ImageAdapter
  {
    public AutoFlipImageAdapter(Context arg2)
    {
      super(localContext);
    }

    public void onMoved(Integer paramInteger1, Integer paramInteger2)
    {
    }

    public void onMoving(Integer paramInteger1, Integer paramInteger2)
    {
      if ((GuidanceNewComerActivity.this.mNavigationDot != null) && (paramInteger2 != null))
      {
        if (paramInteger2.intValue() != GuidanceNewComerActivity.this.mViewCount - 1)
          break label44;
        ((GuidanceNewComerActivity.NewComerGuidanceFlipper)GuidanceNewComerActivity.this.mFlipper).hideNavigationDotView();
      }
      label44: 
      do
        return;
      while (GuidanceNewComerActivity.this.mFlipper.navigationDot != null);
      GuidanceNewComerActivity.this.mFlipper.enableNavigationDotView(GuidanceNewComerActivity.this.mViewCount);
      GuidanceNewComerActivity.this.mFlipper.setCurrentDot(paramInteger1.intValue());
    }
  }

  class NewComerGuidanceFlipper extends GuidanceActivity.GuidanceFlipper
  {
    public NewComerGuidanceFlipper(Context arg2)
    {
      super(localContext);
    }

    public void enableNavigationDotView(int paramInt)
    {
      if (paramInt > 1)
      {
        if (this.navigationDot == null)
        {
          this.navigationDot = ((NavigationDot)LayoutInflater.from(getContext()).inflate(R.layout.navigation_dot, this, false));
          this.navigationDot.setDotNormalId(R.drawable.home_serve_dot);
          this.navigationDot.setDotPressedId(R.drawable.home_serve_dot_pressed);
          addView(this.navigationDot);
          GuidanceNewComerActivity.access$002(GuidanceNewComerActivity.this, this.navigationDot);
        }
        this.navigationDot.setTotalDot(paramInt);
      }
    }

    public void hideNavigationDotView()
    {
      if (this.navigationDot != null)
      {
        removeView(this.navigationDot);
        this.navigationDot = null;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.guide.guidance.GuidanceNewComerActivity
 * JD-Core Version:    0.6.0
 */