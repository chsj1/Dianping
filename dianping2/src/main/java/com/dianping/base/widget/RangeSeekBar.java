package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.dianping.util.Log;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;

public class RangeSeekBar extends View
{
  private static final String LOG_TAG = RangeSeekBar.class.getSimpleName();
  private int balID = 0;
  private int ballHeight;
  private int ballWidth;
  private int barLength;
  private Context context;
  private ImageLayer[] controlBalls = new ImageLayer[2];
  private PaintFlagsDrawFilter filter;
  private boolean hasSetuped;
  private int highField;
  private String highString = "不限";
  private int highValue = 100;
  private int lowField;
  private String lowString = "0";
  private int lowValue = 0;
  private boolean needComputNewPos = false;
  private Paint paint;
  RangeSeekListener rangeSeekListener;

  public RangeSeekBar(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
    init();
  }

  public RangeSeekBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public RangeSeekBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.context = paramContext;
    init();
  }

  private void computNewPosition()
  {
    if (this.lowField == 0)
    {
      this.controlBalls[0].setX(getPaddingLeft() + this.ballWidth * this.lowValue / 100);
      if (this.highField != 2)
        break label126;
      this.controlBalls[1].setX(getPaddingLeft() + this.barLength - this.ballWidth * 2 + this.ballWidth * this.highValue / 100);
    }
    while (true)
    {
      this.needComputNewPos = false;
      return;
      this.controlBalls[0].setX(getPaddingLeft() + this.ballWidth + (this.barLength - this.ballWidth * 3) * this.lowValue / 100);
      break;
      label126: this.controlBalls[1].setX(getPaddingLeft() + this.ballWidth + (this.barLength - this.ballWidth * 3) * this.highValue / 100);
    }
  }

  private void init()
  {
    this.controlBalls[0] = new ImageLayer(this.context, R.drawable.range_seek_bar_imb, 1);
    this.controlBalls[1] = new ImageLayer(this.context, R.drawable.range_seek_bar_imb, 2);
  }

  private void setupView()
  {
    if (this.hasSetuped)
      return;
    this.ballWidth = this.controlBalls[0].width;
    this.ballHeight = this.controlBalls[0].height;
    this.barLength = (getWidth() - getPaddingLeft() - getPaddingRight());
    int i = (getBottom() - getTop()) / 2 - this.ballHeight / 2;
    if (this.controlBalls[0].getX() == 0)
      this.controlBalls[0].setX(getPaddingLeft());
    this.controlBalls[0].setY(i);
    if (this.controlBalls[1].getX() == 0)
      this.controlBalls[1].setX(this.barLength - this.ballWidth + getPaddingLeft());
    this.controlBalls[1].setY(i);
    this.paint = new Paint();
    this.paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_small));
    this.paint.setColor(-7829368);
    this.filter = new PaintFlagsDrawFilter(0, 3);
    this.hasSetuped = true;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    setupView();
    paramCanvas.setDrawFilter(this.filter);
    if (this.needComputNewPos)
      computNewPosition();
    this.paint.setColor(getResources().getColor(R.color.light_gray));
    this.paint.setStyle(Paint.Style.STROKE);
    this.paint.setStrokeWidth(8.0F);
    float f1 = getPaddingLeft() + this.ballWidth / 2;
    float f2 = this.controlBalls[0].getY() + this.ballHeight / 2;
    paramCanvas.drawLine(f1, f2, getPaddingLeft() + this.barLength - this.ballWidth / 2, f2, this.paint);
    this.paint.reset();
    this.paint.setColor(getResources().getColor(R.color.orange));
    this.paint.setStyle(Paint.Style.STROKE);
    this.paint.setStrokeWidth(8.0F);
    f1 = this.controlBalls[0].getX() + this.ballWidth / 2;
    f2 = this.controlBalls[0].getY() + this.ballHeight / 2;
    paramCanvas.drawLine(f1, f2, this.controlBalls[1].getX() + this.ballWidth / 2, f2, this.paint);
    this.paint.reset();
    ImageLayer[] arrayOfImageLayer = this.controlBalls;
    int j = arrayOfImageLayer.length;
    int i = 0;
    while (i < j)
    {
      ImageLayer localImageLayer = arrayOfImageLayer[i];
      paramCanvas.drawBitmap(localImageLayer.getBitmap(), localImageLayer.getX(), localImageLayer.getY(), null);
      i += 1;
    }
    this.paint.setColor(getResources().getColor(R.color.light_gray));
    this.paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_small));
    this.paint.setTextAlign(Paint.Align.CENTER);
    if (this.lowString != null)
      paramCanvas.drawText(this.lowString, this.controlBalls[0].getX() + this.ballWidth / 2, this.controlBalls[0].getY() - 8, this.paint);
    if (this.highString != null)
      paramCanvas.drawText(this.highString, this.controlBalls[1].getX() + this.ballWidth / 2, this.controlBalls[0].getY() - 8, this.paint);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    int j = (int)paramMotionEvent.getX();
    int k = (int)paramMotionEvent.getY();
    switch (i)
    {
    case 1:
    case 3:
    case 4:
    default:
    case 0:
    case 2:
    }
    while (true)
    {
      invalidate();
      return true;
      this.balID = 0;
      paramMotionEvent = this.controlBalls;
      int m = paramMotionEvent.length;
      i = 0;
      while (i < m)
      {
        Object localObject = paramMotionEvent[i];
        int n = localObject.getX() + this.ballWidth / 2;
        int i1 = localObject.getY() + this.ballWidth / 2;
        if (Math.sqrt((n - j) * (n - j) + (i1 - k) * (i1 - k)) < this.ballWidth)
        {
          this.balID = localObject.getID();
          break;
        }
        i += 1;
      }
      Log.d("Alex", "ACTION_MOVE");
      if (this.balID <= 0)
        continue;
      if (this.balID == 1)
        if (j < this.ballWidth / 2 + getPaddingLeft())
        {
          i = this.ballWidth / 2 + getPaddingLeft();
          label220: this.controlBalls[(this.balID - 1)].setX(i - this.ballWidth / 2);
          j = this.barLength - this.ballWidth;
          i = (this.controlBalls[0].getX() - getPaddingLeft()) * 100 / j;
          j = (this.controlBalls[1].getX() - getPaddingLeft()) * 100 / j;
          if (this.rangeSeekListener == null)
            continue;
          this.rangeSeekListener.onRangeChanged(i, j);
          if ((i == 0) && (this.controlBalls[1].getX() - this.controlBalls[0].getX() == this.ballWidth))
            this.rangeSeekListener.onSelectedMinFiled();
          if ((j == 100) && (this.controlBalls[1].getX() - this.controlBalls[0].getX() == this.ballWidth))
            this.rangeSeekListener.onSelectedMaxFiled();
          if (this.balID == 1)
          {
            if (this.controlBalls[0].getX() >= getPaddingLeft() + this.ballWidth)
              break label709;
            j = (this.controlBalls[0].getX() - getPaddingLeft()) * 100 / this.ballWidth;
            i = j;
            if (j < 0)
              i = 0;
            this.rangeSeekListener.onLeftValueChanged(i, 0);
          }
        }
      while (true)
      {
        if (this.balID != 2)
          break label756;
        if (this.controlBalls[1].getX() <= getPaddingLeft() + this.barLength - this.ballWidth * 2)
          break label758;
        j = (this.controlBalls[1].getX() - getPaddingLeft() - this.barLength + this.ballWidth * 2) * 100 / this.ballWidth;
        i = j;
        if (j > 100)
          i = 100;
        this.rangeSeekListener.onRightValueChanged(i, 2);
        break;
        i = j;
        if (j < this.controlBalls[1].getX() - this.ballWidth / 2)
          break label220;
        i = this.controlBalls[1].getX() - this.ballWidth / 2;
        break label220;
        if (j > this.barLength - this.ballWidth / 2 + getPaddingLeft())
        {
          i = this.barLength - this.ballWidth / 2 + getPaddingLeft();
          break label220;
        }
        i = j;
        if (j > this.controlBalls[0].getX() + this.ballWidth * 1.5D)
          break label220;
        i = (int)(this.controlBalls[0].getX() + this.ballWidth * 1.5D);
        break label220;
        label709: i = (this.controlBalls[0].getX() - getPaddingLeft() - this.ballWidth) * 100 / (this.barLength - this.ballWidth * 3);
        this.rangeSeekListener.onLeftValueChanged(i, 1);
      }
      label756: continue;
      label758: i = (this.controlBalls[1].getX() - getPaddingLeft() - this.ballWidth) * 100 / (this.barLength - this.ballWidth * 3);
      this.rangeSeekListener.onRightValueChanged(i, 1);
    }
  }

  public void setHighString(String paramString)
  {
    if (!"不限".equals(paramString))
    {
      this.highString = ("¥" + paramString);
      return;
    }
    this.highString = paramString;
  }

  public void setHighValue(int paramInt1, int paramInt2)
  {
    if (paramInt1 > 100)
    {
      Log.e(LOG_TAG, "the value for setValue can't be bigger than 100");
      return;
    }
    this.highValue = paramInt1;
    this.highField = paramInt2;
    this.needComputNewPos = true;
  }

  public void setLowString(String paramString)
  {
    this.lowString = ("¥" + paramString);
  }

  public void setLowValue(int paramInt1, int paramInt2)
  {
    if (paramInt1 < 0)
    {
      Log.e(LOG_TAG, "the value for setValue can't be smaller than 0");
      return;
    }
    this.lowValue = paramInt1;
    this.lowField = paramInt2;
    this.needComputNewPos = true;
  }

  public void setRangeSeekListener(RangeSeekListener paramRangeSeekListener)
  {
    this.rangeSeekListener = paramRangeSeekListener;
  }

  class ImageLayer
  {
    private int coordX = 0;
    private int coordY = 0;
    int height = 0;
    private int id;
    private Bitmap img;
    int width = 0;

    public ImageLayer(Context paramInt1, int paramInt2, int arg4)
    {
      new BitmapFactory.Options().inJustDecodeBounds = true;
      this.img = BitmapFactory.decodeResource(paramInt1.getResources(), paramInt2);
      this.width = this.img.getWidth();
      this.height = this.img.getHeight();
      int i;
      this.id = i;
    }

    public Bitmap getBitmap()
    {
      return this.img;
    }

    public int getID()
    {
      return this.id;
    }

    public int getWidth()
    {
      return this.width;
    }

    public int getX()
    {
      return this.coordX;
    }

    public int getY()
    {
      return this.coordY;
    }

    void setX(int paramInt)
    {
      this.coordX = paramInt;
    }

    void setY(int paramInt)
    {
      this.coordY = paramInt;
    }
  }

  public static abstract interface RangeSeekListener
  {
    public abstract void onLeftValueChanged(int paramInt1, int paramInt2);

    public abstract void onRangeChanged(int paramInt1, int paramInt2);

    public abstract void onRightValueChanged(int paramInt1, int paramInt2);

    public abstract void onSelectedMaxFiled();

    public abstract void onSelectedMinFiled();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.RangeSeekBar
 * JD-Core Version:    0.6.0
 */