package com.dianping.main.home;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.dianping.widget.NetworkImageView;

public class TopCropNetworkImageView extends NetworkImageView
{
  public TopCropNetworkImageView(Context paramContext)
  {
    super(paramContext);
  }

  public TopCropNetworkImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public TopCropNetworkImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected boolean setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (getDrawable() != null)
    {
      Matrix localMatrix = getImageMatrix();
      float f = getWidth() / getDrawable().getIntrinsicWidth();
      localMatrix.setScale(f, f, 0.0F, 0.0F);
      if (getDrawable().getIntrinsicWidth() * getHeight() > getDrawable().getIntrinsicHeight() * getWidth())
        localMatrix.postTranslate(0.0F, Math.round((getHeight() - getDrawable().getIntrinsicHeight() * f) * 0.5F));
      setImageMatrix(localMatrix);
    }
    return super.setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.TopCropNetworkImageView
 * JD-Core Version:    0.6.0
 */