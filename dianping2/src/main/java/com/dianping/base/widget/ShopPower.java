package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.dianping.v1.R.drawable;

public class ShopPower extends ImageView
{
  private static Drawable PR0;
  private static Drawable PR10;
  private static Drawable PR20;
  private static Drawable PR30;
  private static Drawable PR35;
  private static Drawable PR40;
  private static Drawable PR45;
  private static Drawable PR50;
  private int power;

  public ShopPower(Context paramContext)
  {
    super(paramContext);
  }

  public ShopPower(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ShopPower(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public int getPower()
  {
    return this.power;
  }

  public void setPower(int paramInt)
  {
    this.power = paramInt;
    switch (paramInt)
    {
    default:
      setImageDrawable(null);
      return;
    case 0:
      if (PR0 == null)
        PR0 = getResources().getDrawable(R.drawable.star0);
      setImageDrawable(PR0);
      return;
    case 10:
      if (PR10 == null)
        PR10 = getResources().getDrawable(R.drawable.star10);
      setImageDrawable(PR10);
      return;
    case 20:
      if (PR20 == null)
        PR20 = getResources().getDrawable(R.drawable.star20);
      setImageDrawable(PR20);
      return;
    case 30:
      if (PR30 == null)
        PR30 = getResources().getDrawable(R.drawable.star30);
      setImageDrawable(PR30);
      return;
    case 35:
      if (PR35 == null)
        PR35 = getResources().getDrawable(R.drawable.star35);
      setImageDrawable(PR35);
      return;
    case 40:
      if (PR40 == null)
        PR40 = getResources().getDrawable(R.drawable.star40);
      setImageDrawable(PR40);
      return;
    case 45:
      if (PR45 == null)
        PR45 = getResources().getDrawable(R.drawable.star45);
      setImageDrawable(PR45);
      return;
    case 50:
    }
    if (PR50 == null)
      PR50 = getResources().getDrawable(R.drawable.star50);
    setImageDrawable(PR50);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ShopPower
 * JD-Core Version:    0.6.0
 */