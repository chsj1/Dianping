package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.dianping.v1.R.drawable;

public class HotelShopPower extends ImageView
{
  protected static Drawable PR0;
  protected static Drawable PR10;
  protected static Drawable PR20;
  protected static Drawable PR30;
  protected static Drawable PR35;
  protected static Drawable PR40;
  protected static Drawable PR45;
  protected static Drawable PR50;
  protected int power;

  public HotelShopPower(Context paramContext)
  {
    super(paramContext);
  }

  public HotelShopPower(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public HotelShopPower(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
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
        PR0 = getResources().getDrawable(R.drawable.star0_hotel);
      setImageDrawable(PR0);
      return;
    case 10:
      if (PR10 == null)
        PR10 = getResources().getDrawable(R.drawable.star10_hotel);
      setImageDrawable(PR10);
      return;
    case 20:
      if (PR20 == null)
        PR20 = getResources().getDrawable(R.drawable.star20_hotel);
      setImageDrawable(PR20);
      return;
    case 30:
      if (PR30 == null)
        PR30 = getResources().getDrawable(R.drawable.star30_hotel);
      setImageDrawable(PR30);
      return;
    case 35:
      if (PR35 == null)
        PR35 = getResources().getDrawable(R.drawable.star35_hotel);
      setImageDrawable(PR35);
      return;
    case 40:
      if (PR40 == null)
        PR40 = getResources().getDrawable(R.drawable.star40_hotel);
      setImageDrawable(PR40);
      return;
    case 45:
      if (PR45 == null)
        PR45 = getResources().getDrawable(R.drawable.star45_hotel);
      setImageDrawable(PR45);
      return;
    case 50:
    }
    if (PR50 == null)
      PR50 = getResources().getDrawable(R.drawable.star50_hotel);
    setImageDrawable(PR50);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.HotelShopPower
 * JD-Core Version:    0.6.0
 */