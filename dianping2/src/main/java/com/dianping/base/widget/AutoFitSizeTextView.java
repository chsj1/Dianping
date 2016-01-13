package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoFitSizeTextView extends TextView
{
  private static float DEFAULT_MAX_TEXT_SIZE;
  private static float DEFAULT_MIN_TEXT_SIZE = 1.0F;
  private float maxTextSize;
  private float minTextSize;
  private Paint testPaint;

  static
  {
    DEFAULT_MAX_TEXT_SIZE = 20.0F;
  }

  public AutoFitSizeTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initialise();
  }

  private void initialise()
  {
    this.testPaint = new Paint();
    this.testPaint.set(getPaint());
    this.maxTextSize = getTextSize();
    if (this.maxTextSize <= DEFAULT_MIN_TEXT_SIZE)
      this.maxTextSize = DEFAULT_MAX_TEXT_SIZE;
    this.minTextSize = DEFAULT_MIN_TEXT_SIZE;
  }

  private void refitText(String paramString, int paramInt)
  {
    int i;
    int j;
    float f2;
    float f1;
    if (paramInt > 0)
    {
      i = getPaddingLeft();
      j = getPaddingRight();
      f2 = this.maxTextSize;
      f1 = f2;
      if (this.testPaint != null)
        this.testPaint.setTextSize(f2);
    }
    while (true)
    {
      f1 = f2;
      if (f2 > this.minTextSize)
      {
        f1 = f2;
        if (this.testPaint.measureText(paramString) > paramInt - i - j)
        {
          f2 -= 1.0F;
          if (f2 > this.minTextSize)
            break label104;
          f1 = this.minTextSize;
        }
      }
      setTextSize(f1);
      return;
      label104: this.testPaint.setTextSize(f2);
    }
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramInt1 != paramInt3)
      refitText(getText().toString(), 80);
  }

  protected void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    super.onTextChanged(paramCharSequence, paramInt1, paramInt2, paramInt3);
    refitText(paramCharSequence.toString(), 80);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.AutoFitSizeTextView
 * JD-Core Version:    0.6.0
 */