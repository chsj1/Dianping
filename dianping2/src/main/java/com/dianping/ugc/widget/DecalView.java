package com.dianping.ugc.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.dianping.util.Log;
import com.dianping.v1.R.color;

public class DecalView extends FrameLayout
{
  private static final String TAG = "DecalView";
  private int decalHeight;
  private int decalWidth;
  private String mCategory;
  private String mDecalName;
  private ImageView mDecalView;
  private View.OnClickListener onClickListener;

  public DecalView(Context paramContext)
  {
    super(paramContext);
  }

  public DecalView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public String getCategory()
  {
    return this.mCategory;
  }

  public Bitmap getDecal()
  {
    Drawable localDrawable = this.mDecalView.getDrawable();
    if (localDrawable != null)
      return ((BitmapDrawable)localDrawable).getBitmap();
    return null;
  }

  public String getDecalName()
  {
    return this.mDecalName;
  }

  public void setCategory(String paramString)
  {
    this.mCategory = paramString;
  }

  public void setDecal(Bitmap paramBitmap)
  {
    if (paramBitmap != null)
    {
      this.mDecalView.setImageBitmap(paramBitmap);
      setState(State.DONE);
      return;
    }
    setState(State.ERROR);
  }

  public void setDecalName(String paramString)
  {
    this.mDecalName = paramString;
  }

  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    super.setOnClickListener(paramOnClickListener);
    this.onClickListener = paramOnClickListener;
  }

  public void setSize(int paramInt1, int paramInt2)
  {
    this.decalWidth = paramInt1;
    this.decalHeight = paramInt2;
  }

  public void setState(State paramState)
  {
    if (paramState == State.IDLE)
    {
      if (this.mDecalView == null)
      {
        this.mDecalView = new ImageView(getContext());
        this.mDecalView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.mDecalView.setBackgroundResource(R.color.gray);
        paramState = new FrameLayout.LayoutParams(this.decalWidth, this.decalHeight);
        addView(this.mDecalView, 0, paramState);
      }
      setOnClickListener(null);
      return;
    }
    if (paramState == State.DONE)
    {
      setOnClickListener(this.onClickListener);
      this.mDecalView.setBackgroundDrawable(null);
      return;
    }
    if (paramState == State.ERROR)
    {
      setOnClickListener(null);
      this.mDecalView.setBackgroundDrawable(null);
      return;
    }
    Log.w("DecalView", "unknown state");
    setOnClickListener(null);
    this.mDecalView.setBackgroundDrawable(null);
  }

  public static enum State
  {
    static
    {
      DONE = new State("DONE", 2);
      ERROR = new State("ERROR", 3);
      $VALUES = new State[] { IDLE, LOADING, DONE, ERROR };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.widget.DecalView
 * JD-Core Version:    0.6.0
 */