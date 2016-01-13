package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.v1.R.drawable;

public class UserPowers extends LinearLayout
{
  private Context mContext;
  private int power;

  public UserPowers(Context paramContext)
  {
    this(paramContext, null);
  }

  public UserPowers(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  private void setViews(int paramInt)
  {
    removeAllViews();
    ImageView localImageView = new ImageView(this.mContext);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.gravity = 16;
    switch (paramInt)
    {
    default:
      return;
    case 10:
      localImageView.setImageResource(R.drawable.user_level10);
      addView(localImageView, localLayoutParams);
      return;
    case 20:
      localImageView.setImageResource(R.drawable.user_level20);
      addView(localImageView, localLayoutParams);
      return;
    case 30:
      localImageView.setImageResource(R.drawable.user_level30);
      addView(localImageView, localLayoutParams);
      return;
    case 40:
      localImageView.setImageResource(R.drawable.user_level40);
      addView(localImageView, localLayoutParams);
      return;
    case 45:
      localImageView.setImageResource(R.drawable.user_level45);
      addView(localImageView, localLayoutParams);
      return;
    case 50:
      localImageView.setImageResource(R.drawable.user_level50);
      addView(localImageView, localLayoutParams);
      return;
    case 60:
    }
    localImageView.setImageResource(R.drawable.user_level60);
    addView(localImageView, localLayoutParams);
  }

  public int getPower()
  {
    return this.power;
  }

  public void setPower(int paramInt)
  {
    this.power = paramInt;
    setViews(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.UserPowers
 * JD-Core Version:    0.6.0
 */