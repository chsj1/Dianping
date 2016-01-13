package com.dianping.movie.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SeatImageView extends ScaleImageView
{
  private static final DPObject NULL = new DPObject().edit().putInt("Status", 2).generate();
  private final int MIDLINE_HALL_SEAT_DISTANCE_FULL = 30;
  private final int MIDLINE_HALL_SEAT_DISTANCE_SHRINK = 10;
  private final int MIDLINE_SEAT_BOTTOM_DISTANCE = 20;
  Bitmap bm1;
  Bitmap bm2;
  Bitmap bm3;
  Bitmap bm4;
  Bitmap bm5;
  private boolean canDraw = false;
  private SeatImageView.SeatDrawDelegate delegate;
  DisplayMetrics dm;
  DPObject dpSeatPlan;
  Bitmap hallImageBitmap;
  String hallName;
  private Paint hallPaint;
  private ArrayList<OnItemSelectListener> mListenerArray = new ArrayList();
  private int midLineHallSeatDistance = 30;
  private Paint midLinePaint;
  Bitmap s_bm1;
  Bitmap s_bm2;
  Bitmap s_bm3;
  Bitmap s_bm4;
  Bitmap s_bm5;
  float scaleMate;
  int scaleType;
  Bitmap seatImageBitmap;
  int seatImageBitmapHeight = 0;
  int seatImageBitmapWidth = 0;
  DPObject[] seatList;
  int[] seatStatusList;
  ArrayList<DPObject> selectSeats;
  private Paint textHallPaint;

  public SeatImageView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SeatImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }

  private DPObject getLeft(List<DPObject> paramList, DPObject paramDPObject)
  {
    int i = paramList.indexOf(paramDPObject);
    if (i > 0)
    {
      paramList = (DPObject)paramList.get(i - 1);
      if (paramDPObject.getInt("ColumnIndex") - paramList.getInt("ColumnIndex") == 1)
        return paramList;
      return NULL;
    }
    return NULL;
  }

  private DPObject getRight(List<DPObject> paramList, DPObject paramDPObject)
  {
    int i = paramList.indexOf(paramDPObject);
    if (i < paramList.size() - 1)
    {
      paramList = (DPObject)paramList.get(i + 1);
      if (paramList.getInt("ColumnIndex") - paramDPObject.getInt("ColumnIndex") == 1)
        return paramList;
      return NULL;
    }
    return NULL;
  }

  private void initView()
  {
    this.dm = new DisplayMetrics();
    this.dm = getResources().getDisplayMetrics();
    this.selectSeats = new ArrayList();
    this.hallPaint = new Paint();
    this.hallPaint.setARGB(255, 242, 242, 242);
    this.textHallPaint = new Paint();
    this.textHallPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_16));
    this.textHallPaint.setARGB(255, 115, 115, 115);
    this.textHallPaint.setAntiAlias(true);
    this.midLinePaint = new Paint();
    this.midLinePaint.setARGB(255, 255, 255, 255);
    this.midLinePaint.setStyle(Paint.Style.STROKE);
    this.midLinePaint.setStrokeWidth(3.0F);
    this.midLinePaint.setAntiAlias(true);
  }

  private boolean isAvailable(List<DPObject> paramList, DPObject paramDPObject)
  {
    if (paramDPObject.getInt("Status") == 0)
    {
      int i = paramList.indexOf(paramDPObject);
      return (i == -1) || (3 != this.seatStatusList[i]);
    }
    return false;
  }

  private boolean isLeftLinked(List<DPObject> paramList, DPObject paramDPObject)
  {
    while (isRequested(paramList, paramDPObject))
      paramDPObject = getLeft(paramList, paramDPObject);
    return isNotAvailable(paramList, paramDPObject);
  }

  private boolean isNotAvailable(List<DPObject> paramList, DPObject paramDPObject)
  {
    if ((1 == paramDPObject.getInt("Status")) || (2 == paramDPObject.getInt("Status")));
    while (true)
    {
      return true;
      int i = paramList.indexOf(paramDPObject);
      if (i == -1)
        break;
      if (3 != this.seatStatusList[i])
        return false;
    }
    return false;
  }

  private boolean isRequested(List<DPObject> paramList, DPObject paramDPObject)
  {
    int k = 0;
    int i = paramList.indexOf(paramDPObject);
    int j = k;
    if (i != -1)
    {
      j = k;
      if (3 == this.seatStatusList[i])
        j = 1;
    }
    return j;
  }

  private boolean isRightLinked(List<DPObject> paramList, DPObject paramDPObject)
  {
    while (isRequested(paramList, paramDPObject))
      paramDPObject = getRight(paramList, paramDPObject);
    return isNotAvailable(paramList, paramDPObject);
  }

  private void recyleBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap != null)
      paramBitmap.recycle();
  }

  public void addOnItemSelectListener(OnItemSelectListener paramOnItemSelectListener)
  {
    this.mListenerArray.add(paramOnItemSelectListener);
  }

  public void drawView(DPObject paramDPObject, float paramFloat, int paramInt, String paramString)
  {
    this.dpSeatPlan = paramDPObject;
    this.scaleMate = paramFloat;
    this.scaleType = paramInt;
    this.hallName = paramString;
    if (paramInt == 2)
      this.midLineHallSeatDistance = 10;
    this.midLineHallSeatDistance = (int)(this.midLineHallSeatDistance * this.seatDensityFactor);
    this.seatImageBitmapWidth = paramDPObject.getInt("Width");
    this.seatImageBitmapHeight = paramDPObject.getInt("Height");
    this.bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.movie_seat_rest);
    this.bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.movie_seat_selected);
    this.bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.movie_seat_sold);
    this.bm4 = BitmapFactory.decodeResource(getResources(), R.drawable.movie_seat_lovers);
    this.bm5 = BitmapFactory.decodeResource(getResources(), R.drawable.movie_seat_broken);
    paramString = new Matrix();
    paramString.postScale(this.scaleMate, this.scaleMate);
    this.s_bm1 = Bitmap.createBitmap(this.bm1, 0, 0, this.bm1.getWidth(), this.bm1.getHeight(), paramString, true);
    this.s_bm2 = Bitmap.createBitmap(this.bm2, 0, 0, this.bm2.getWidth(), this.bm2.getHeight(), paramString, true);
    this.s_bm3 = Bitmap.createBitmap(this.bm3, 0, 0, this.bm3.getWidth(), this.bm3.getHeight(), paramString, true);
    this.s_bm4 = Bitmap.createBitmap(this.bm4, 0, 0, this.bm4.getWidth(), this.bm4.getHeight(), paramString, true);
    this.s_bm5 = Bitmap.createBitmap(this.bm5, 0, 0, this.bm5.getWidth(), this.bm5.getHeight(), paramString, true);
    this.seatList = paramDPObject.getArray("SeatList");
    paramDPObject = new DPObject[this.seatList.length];
    int i = 0;
    if (i < this.seatList.length)
    {
      paramString = this.seatList[i].edit();
      if (paramInt == 2)
      {
        paramString.putInt("Row", (int)((this.seatList[i].getInt("RowIndex") - 1 + 3) * 52 * this.seatDensityFactor));
        paramString.putInt("Column", (int)((this.seatList[i].getInt("ColumnIndex") - 1 + 1) * 52 * this.seatDensityFactor));
      }
      while (true)
      {
        paramDPObject[i] = paramString.generate();
        i += 1;
        break;
        paramString.putInt("Row", (int)((this.seatList[i].getInt("RowIndex") - 1 + 3) * 78 * this.seatDensityFactor));
        paramString.putInt("Column", (int)((this.seatList[i].getInt("ColumnIndex") - 1 + 1) * 78 * this.seatDensityFactor));
      }
    }
    this.seatList = paramDPObject;
    this.seatStatusList = new int[this.seatList.length];
    paramInt = 0;
    while (paramInt < this.seatList.length)
    {
      this.seatStatusList[paramInt] = this.seatList[paramInt].getInt("Status");
      paramInt += 1;
    }
    this.seatImageBitmap = Bitmap.createBitmap(this.seatImageBitmapWidth, this.seatImageBitmapHeight, Bitmap.Config.ARGB_4444);
    paramString = new Canvas(this.seatImageBitmap);
    paramInt = 0;
    if (paramInt < this.seatList.length)
    {
      i = this.seatList[paramInt].getInt("Row");
      int j = this.seatList[paramInt].getInt("Column");
      int k = this.seatList[paramInt].getInt("LoveFlag");
      switch (this.seatList[paramInt].getInt("Status"))
      {
      default:
        paramDPObject = this.s_bm5;
      case 0:
      case 1:
      case 2:
      }
      while (true)
      {
        paramString.drawBitmap(paramDPObject, j, i, null);
        paramInt += 1;
        break;
        if (k > 0)
        {
          paramDPObject = this.s_bm4;
          continue;
        }
        paramDPObject = this.s_bm1;
        continue;
        paramDPObject = this.s_bm3;
        continue;
        paramDPObject = this.s_bm5;
      }
    }
    setImageBitmap(this.seatImageBitmap);
    this.hallImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cinema_hall_arrow);
    invalidate();
  }

  public float[] getPositionParam()
  {
    return new float[] { getBitmapWidth(), getBitmapHeight(), getScale(getImageViewMatrix()), getTranslateX(getImageViewMatrix()), getTranslateY(getImageViewMatrix()), getWidth(), getHeight() };
  }

  public void getProperBaseMatrix(Bitmap paramBitmap, Matrix paramMatrix)
  {
    if (paramBitmap == null)
      return;
    float f3 = getWidth();
    float f1 = getHeight();
    float f4 = paramBitmap.getWidth();
    f1 = paramBitmap.getHeight();
    paramMatrix.reset();
    float f2 = f3 / f4;
    if (this.scaleType == 1)
      if (f2 < 0.33F)
        f1 = 0.33F;
    while (true)
    {
      ScaleImageView.setZoom(f1, this.scaleType);
      paramMatrix.postScale(f1, f1);
      paramMatrix.postTranslate((f3 - f4 * f1) / 2.0F, 0.0F);
      if (this.delegate == null)
        break;
      this.delegate.seatDrawCallBack();
      return;
      f1 = f2;
      if (f2 <= 1.0F)
        continue;
      f1 = 1.0F;
      continue;
      f1 = f2;
      if (this.scaleType != 2)
        continue;
      if (f2 < 0.4F)
      {
        f1 = 0.4F;
        continue;
      }
      f1 = f2;
      if (f2 <= 1.0F)
        continue;
      f1 = 1.0F;
    }
  }

  public Bitmap getSeatImageBitmap()
  {
    return this.seatImageBitmap;
  }

  public int getSeatImageBitmapHeight()
  {
    return this.seatImageBitmap.getHeight();
  }

  public int getSeatImageBitmapWidth()
  {
    return this.seatImageBitmap.getWidth();
  }

  public ArrayList<DPObject> getSelectSeats()
  {
    return this.selectSeats;
  }

  public boolean isAllowed()
  {
    List localList = Arrays.asList(this.seatList);
    Iterator localIterator = this.selectSeats.iterator();
    while (localIterator.hasNext())
    {
      DPObject localDPObject1 = (DPObject)localIterator.next();
      DPObject localDPObject2 = getLeft(localList, localDPObject1);
      if (isAvailable(localList, localDPObject2))
      {
        localDPObject2 = getLeft(localList, localDPObject2);
        if ((!isRequested(localList, localDPObject2)) && ((!isNotAvailable(localList, localDPObject2)) || (isRightLinked(localList, localDPObject1))));
      }
      do
      {
        return false;
        localDPObject2 = getRight(localList, localDPObject1);
        if (!isAvailable(localList, localDPObject2))
          break;
        localDPObject2 = getRight(localList, localDPObject2);
      }
      while (isRequested(localList, localDPObject2));
      if ((isNotAvailable(localList, localDPObject2)) && (!isLeftLinked(localList, localDPObject1)))
        return false;
    }
    return true;
  }

  public boolean isScale()
  {
    return getScale(this.mSuppMatrix) > 1.0F;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (!this.canDraw)
      this.canDraw = true;
    do
      return;
    while (this.dpSeatPlan == null);
    float f4 = getScale(getImageViewMatrix());
    float f1 = getTranslateX(getImageViewMatrix());
    float f3 = getTranslateY(getImageViewMatrix());
    float f2;
    label120: float f5;
    label143: float f6;
    float f7;
    label216: String str;
    float f9;
    float f8;
    int i;
    int j;
    if (f4 <= 0.5F)
    {
      f1 = this.seatImageBitmapWidth * f4 / 2.0F + f1 + 1.0F;
      f2 = f1;
      if (this.dpSeatPlan.getInt("MaxColumnIndex") % 2 == 1)
      {
        if (this.scaleType != 1)
          break label462;
        f2 = f1 - 78.0F * f4 / 2.0F * this.seatDensityFactor;
      }
      if (this.scaleType != 1)
        break label491;
      f1 = f3 + 216.0F * f4 * this.seatDensityFactor;
      f5 = this.hallImageBitmap.getWidth() / 2;
      f6 = f1 - this.midLineHallSeatDistance * f4;
      f7 = this.hallImageBitmap.getHeight();
      if (this.scaleType != 1)
        break label523;
      f1 = this.seatImageBitmapHeight * f4 + f3 - 60.0F * f4 * this.seatDensityFactor + 20.0F * f4;
      str = this.hallName + "银幕";
      f9 = this.textHallPaint.measureText(str);
      f3 = this.textHallPaint.descent();
      f4 = this.textHallPaint.ascent();
      f8 = f9 / 2.0F;
      i = (int)(ViewUtils.dip2px(getContext(), 20.0F) + f9);
      j = ViewUtils.dip2px(getContext(), 150.0F);
      if (i <= j)
        break label580;
    }
    while (true)
    {
      f9 = f2 - i / 2;
      float f10 = f6 - ViewUtils.dip2px(getContext(), 25.0F);
      paramCanvas.drawRect(f9, f10, f9 + i, f6, this.hallPaint);
      paramCanvas.drawText(str, f2 - f8, f10 + (f3 - f4), this.textHallPaint);
      paramCanvas.drawBitmap(this.hallImageBitmap, f2 - f5, f6, this.textHallPaint);
      paramCanvas.drawLine(f2, f7 + f6 - 2.0F, f2, f1, this.midLinePaint);
      return;
      if (f4 <= 0.75F)
      {
        f1 = this.seatImageBitmapWidth * f4 / 2.0F + f1 + 2.0F;
        break;
      }
      f1 = this.seatImageBitmapWidth * f4 / 2.0F + f1 + 3.0F;
      break;
      label462: f2 = f1;
      if (this.scaleType != 2)
        break label120;
      f2 = f1 - 52.0F * f4 / 2.0F * this.seatDensityFactor;
      break label120;
      label491: if (this.scaleType == 2)
      {
        f1 = f3 + 144.0F * f4 * this.seatDensityFactor;
        break label143;
      }
      f1 = f3;
      break label143;
      label523: if (this.scaleType == 2)
      {
        f1 = this.seatImageBitmapHeight * f4 + f3 - 40.0F * f4 * this.seatDensityFactor + 20.0F * f4;
        break label216;
      }
      f1 = this.seatImageBitmapHeight * f4 + f3;
      break label216;
      label580: i = j;
    }
  }

  public void recycle()
  {
    recyleBitmap(this.seatImageBitmap);
    recyleBitmap(this.bm1);
    recyleBitmap(this.bm2);
    recyleBitmap(this.bm3);
    recyleBitmap(this.bm4);
    recyleBitmap(this.bm5);
    recyleBitmap(this.s_bm1);
    recyleBitmap(this.s_bm2);
    recyleBitmap(this.s_bm3);
    recyleBitmap(this.s_bm4);
    recyleBitmap(this.s_bm5);
  }

  public void reset()
  {
    recycle();
    this.selectSeats.clear();
    this.seatList = null;
    this.seatStatusList = null;
  }

  public void selectItem(float paramFloat1, float paramFloat2)
  {
    Canvas localCanvas = new Canvas(this.mBitmapDisplayed);
    float f1 = getValue(getImageViewMatrix(), 2);
    float f2 = getValue(getImageViewMatrix(), 5);
    float f3 = this.s_bm1.getWidth();
    float f4 = this.s_bm1.getHeight();
    int j = (int)((paramFloat1 - f1) / getScale(getImageViewMatrix()));
    int k = (int)((paramFloat2 - f2) / getScale(getImageViewMatrix()));
    int i = 0;
    while (true)
    {
      int m;
      DPObject localDPObject1;
      if (i < this.seatList.length)
      {
        m = this.seatList[i].getInt("Row");
        int n = this.seatList[i].getInt("Column");
        if ((j < n) || (j > n + f3) || (k < m) || (k > m + f4))
          break label1117;
        localDPObject1 = this.seatList[i];
        j = this.seatStatusList[i];
        k = localDPObject1.getInt("LoveFlag");
        if (this.dpSeatPlan == null)
          return;
        m = this.dpSeatPlan.getInt("SeatCountLimit");
        if (j != 0)
          break label649;
        if (this.selectSeats.size() >= m)
          break label605;
        if (k <= 0)
          break label556;
        if (this.selectSeats.size() >= m - 1)
          break label539;
        if (k != 1)
          break label421;
        this.seatStatusList[i] = 3;
        this.seatStatusList[(i + 1)] = 3;
        this.selectSeats.add(localDPObject1);
        this.selectSeats.add(this.seatList[(i + 1)]);
        localCanvas.drawBitmap(this.s_bm2, localDPObject1.getInt("Column"), localDPObject1.getInt("Row"), null);
        localCanvas.drawBitmap(this.s_bm2, this.seatList[(i + 1)].getInt("Column"), this.seatList[(i + 1)].getInt("Row"), null);
      }
      label421: label556: label605: Object localObject;
      label539: 
      do
        while (this.mListenerArray != null)
        {
          i = 0;
          while (i < this.mListenerArray.size())
          {
            ((OnItemSelectListener)this.mListenerArray.get(i)).onItemSelect();
            i += 1;
          }
          this.seatStatusList[i] = 3;
          this.seatStatusList[(i - 1)] = 3;
          this.selectSeats.add(this.seatList[(i - 1)]);
          this.selectSeats.add(localDPObject1);
          localCanvas.drawBitmap(this.s_bm2, localDPObject1.getInt("Column"), localDPObject1.getInt("Row"), null);
          localCanvas.drawBitmap(this.s_bm2, this.seatList[(i - 1)].getInt("Column"), this.seatList[(i - 1)].getInt("Row"), null);
          continue;
          Toast.makeText(getContext(), "此情况下不能购买情侣座", 0).show();
          continue;
          this.seatStatusList[i] = 3;
          this.selectSeats.add(localDPObject1);
          localCanvas.drawBitmap(this.s_bm2, localDPObject1.getInt("Column"), localDPObject1.getInt("Row"), null);
          continue;
          localObject = String.format("一次只能买%d张,超出%d张请分批购买", new Object[] { Integer.valueOf(m), Integer.valueOf(m) });
          Toast.makeText(getContext(), (CharSequence)localObject, 0).show();
        }
      while ((j == 1) || (j == 2) || (j != 3));
      label649: if (k > 0)
      {
        if (k == 1)
        {
          localObject = localDPObject1;
          localDPObject1 = this.seatList[(i + 1)];
          this.seatStatusList[i] = ((DPObject)localObject).getInt("Status");
          this.seatStatusList[(i + 1)] = localDPObject1.getInt("Status");
        }
        while (true)
        {
          Iterator localIterator = this.selectSeats.iterator();
          DPObject localDPObject2;
          while (localIterator.hasNext())
          {
            localDPObject2 = (DPObject)localIterator.next();
            if (localDPObject2.getInt("ID") != ((DPObject)localObject).getInt("ID"))
              continue;
            this.selectSeats.remove(localDPObject2);
          }
          localIterator = this.selectSeats.iterator();
          while (localIterator.hasNext())
          {
            localDPObject2 = (DPObject)localIterator.next();
            if (localDPObject2.getInt("ID") != localDPObject1.getInt("ID"))
              continue;
            this.selectSeats.remove(localDPObject2);
          }
          localCanvas.drawBitmap(this.s_bm4, ((DPObject)localObject).getInt("Column"), ((DPObject)localObject).getInt("Row"), null);
          localCanvas.drawBitmap(this.s_bm4, localDPObject1.getInt("Column"), localDPObject1.getInt("Row"), null);
          break;
          localObject = this.seatList[(i - 1)];
          this.seatStatusList[(i - 1)] = ((DPObject)localObject).getInt("Status");
          this.seatStatusList[i] = localDPObject1.getInt("Status");
        }
      }
      j = 0;
      while (j < this.selectSeats.size())
      {
        if (((DPObject)this.selectSeats.get(j)).getInt("ID") == localDPObject1.getInt("ID"))
          this.selectSeats.remove(j);
        j += 1;
      }
      this.seatStatusList[i] = localDPObject1.getInt("Status");
      if (this.seatStatusList[i] == 0)
        localObject = this.s_bm1;
      while (true)
      {
        localCanvas.drawBitmap((Bitmap)localObject, localDPObject1.getInt("Column"), localDPObject1.getInt("Row"), null);
        break;
        if (this.seatStatusList[i] == 1)
        {
          localObject = this.s_bm3;
          continue;
        }
        if (this.seatStatusList[i] == 2)
        {
          localObject = this.s_bm5;
          continue;
        }
        localObject = this.s_bm5;
      }
      label1117: i += 1;
    }
    invalidate();
  }

  public void setSeatDrawDelegate(SeatImageView.SeatDrawDelegate paramSeatDrawDelegate)
  {
    this.delegate = paramSeatDrawDelegate;
  }

  public static abstract interface OnItemSelectListener
  {
    public abstract void onItemSelect();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.SeatImageView
 * JD-Core Version:    0.6.0
 */