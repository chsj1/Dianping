package com.dianping.takeaway.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;

public class TAStarView extends LinearLayout
{
  private ImageView[] rankImageViews = new ImageView[5];
  private int score;

  public TAStarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  private void initView(Context paramContext)
  {
    setOrientation(0);
    int i = 0;
    while (i < this.rankImageViews.length)
    {
      this.rankImageViews[i] = new ImageView(paramContext);
      if (i != this.rankImageViews.length - 1)
        this.rankImageViews[i].setPadding(0, 0, ViewUtils.dip2px(paramContext, 2.0F), 0);
      addView(this.rankImageViews[i]);
      i += 1;
    }
  }

  private void invalidateStar()
  {
    int k = this.score / 10;
    if (this.score % 10 == 5);
    for (int i = 1; ; i = 0)
    {
      j = 0;
      while (j < k)
      {
        this.rankImageViews[j].setImageResource(R.drawable.wm_shoplist_star_full);
        j += 1;
      }
    }
    int j = k;
    while (j < k + i)
    {
      this.rankImageViews[j].setImageResource(R.drawable.wm_shoplist_star_half);
      j += 1;
    }
    i = k + i;
    while (i < this.rankImageViews.length)
    {
      this.rankImageViews[i].setImageResource(R.drawable.wm_shoplist_star_empty);
      i += 1;
    }
  }

  public int getScore()
  {
    return this.score;
  }

  public void setScore(int paramInt)
  {
    if (paramInt < 0)
      this.score = 0;
    while (true)
    {
      invalidateStar();
      return;
      if (paramInt > 50)
      {
        this.score = 50;
        continue;
      }
      this.score = paramInt;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.view.TAStarView
 * JD-Core Version:    0.6.0
 */