package com.dianping.movie.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.base.widget.FilterBar;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;

public class CinemaFilterBar extends FilterBar
{
  public CinemaFilterBar(Context paramContext)
  {
    super(paramContext, null);
  }

  public CinemaFilterBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void addItem(Object paramObject, int paramInt)
  {
    if (getChildCount() > 0)
    {
      localImageView = new ImageView(getContext());
      localImageView.setImageDrawable(getResources().getDrawable(R.drawable.filter_bar_divider));
      localImageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
      addView(localImageView);
    }
    ImageView localImageView = (ImageView)LayoutInflater.from(getContext()).inflate(R.layout.filter_bar_image_item, this, false);
    localImageView.setTag(paramObject);
    localImageView.setImageResource(paramInt);
    localImageView.setOnClickListener(this.handler);
    addView(localImageView);
  }

  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    int i = 0;
    while (i < getChildCount())
    {
      getChildAt(i).setEnabled(paramBoolean);
      i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.CinemaFilterBar
 * JD-Core Version:    0.6.0
 */