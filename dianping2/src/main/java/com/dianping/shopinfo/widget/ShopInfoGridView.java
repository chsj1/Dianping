package com.dianping.shopinfo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.v1.R.color;

public class ShopInfoGridView extends MeasuredGridView
{
  private boolean mIsDisplayDivider = true;

  public ShopInfoGridView(Context paramContext)
  {
    super(paramContext);
  }

  public ShopInfoGridView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ShopInfoGridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    if (this.mIsDisplayDivider)
    {
      Object localObject = getChildAt(0);
      int j = getWidth() / ((View)localObject).getWidth();
      int k = getChildCount();
      localObject = new Paint();
      ((Paint)localObject).setStyle(Paint.Style.STROKE);
      ((Paint)localObject).setColor(getContext().getResources().getColor(R.color.inner_divider));
      int i = 0;
      View localView;
      if (i < k)
      {
        localView = getChildAt(i);
        if ((i + 1) % j == 0)
          paramCanvas.drawLine(localView.getLeft(), localView.getBottom(), localView.getRight(), localView.getBottom(), (Paint)localObject);
        while (true)
        {
          i += 1;
          break;
          if (i + 1 > k - k % j)
          {
            paramCanvas.drawLine(localView.getRight(), localView.getTop(), localView.getRight(), localView.getBottom(), (Paint)localObject);
            continue;
          }
          paramCanvas.drawLine(localView.getRight(), localView.getTop(), localView.getRight(), localView.getBottom(), (Paint)localObject);
          paramCanvas.drawLine(localView.getLeft(), localView.getBottom(), localView.getRight(), localView.getBottom(), (Paint)localObject);
        }
      }
      if (k % j != 0)
      {
        i = 0;
        while (i < j - k % j)
        {
          localView = getChildAt(k - 1);
          paramCanvas.drawLine(localView.getRight() + localView.getWidth() * i, localView.getTop(), localView.getRight() + localView.getWidth() * i, localView.getBottom(), (Paint)localObject);
          i += 1;
        }
      }
    }
  }

  public boolean isIsDisplayDivider()
  {
    return this.mIsDisplayDivider;
  }

  public void setIsDisplayDivider(boolean paramBoolean)
  {
    this.mIsDisplayDivider = paramBoolean;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.ShopInfoGridView
 * JD-Core Version:    0.6.0
 */