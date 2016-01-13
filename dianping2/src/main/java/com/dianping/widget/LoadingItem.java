package com.dianping.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class LoadingItem extends NovaLinearLayout
{
  public LoadingItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public LoadingItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    Animation localAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate_alpha);
    localAnimation.setInterpolator(new LinearInterpolator());
    findViewById(R.id.anim_icon).startAnimation(localAnimation);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.LoadingItem
 * JD-Core Version:    0.6.0
 */