package com.dianping.movie.view;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.movie.util.MovieUtil;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class MovieSelectSeatView extends NovaLinearLayout
{
  private static final int DRAG = 1;
  private static final int MSG_WHAT_CHECKDOBULECLICK = 1;
  private static final int NONE = 0;
  private static final int ZOOM = 2;
  private static int mode;
  static float originalScale = 1.0F;
  DPObject dpSeatPlan;
  boolean firstClick = true;
  private Handler handler = new MovieSelectSeatView.1(this);
  long lastDownTime = -1L;
  long lastUpTime = -1L;
  PointF mid = new PointF();
  NavigateImageView navImageView;
  float oldDist = 1.0F;
  float scaleMate = 1.0F;
  int scaleType = 1;
  private float seatDensityFactor;
  FrameLayout seatFrame;
  SeatImageView seatImageView;
  private boolean seatPlanLoaded = false;
  TextView seatThirdPartyName;
  SeatThumbnailView seatThumbnailView;
  boolean singleClick = false;
  PointF start = new PointF();
  long startTime;
  PointF tmpStart = new PointF();

  static
  {
    mode = 0;
  }

  public MovieSelectSeatView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieSelectSeatView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.seatDensityFactor = MovieUtil.getSeatDensityFactor(paramContext);
    initView();
  }

  private int caculateScaleType(int paramInt1, int paramInt2)
  {
    if (paramInt1 * paramInt2 <= 4194304.0F * this.seatDensityFactor * this.seatDensityFactor)
      return 1;
    return 2;
  }

  private void initView()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.movie_select_seat_view, this, true);
    this.navImageView = ((NavigateImageView)findViewById(R.id.nav_img));
    this.seatFrame = ((FrameLayout)findViewById(R.id.seat_frame));
    this.seatImageView = ((SeatImageView)findViewById(R.id.seat_img));
    this.seatThirdPartyName = ((TextView)findViewById(R.id.seat_thirdpartyname));
    this.seatThumbnailView = ((SeatThumbnailView)findViewById(R.id.seat_thumbnail));
  }

  private void midPoint(PointF paramPointF, MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getX(0);
    float f2 = paramMotionEvent.getX(1);
    float f3 = paramMotionEvent.getY(0);
    float f4 = paramMotionEvent.getY(1);
    paramPointF.set((f1 + f2) / 2.0F, (f3 + f4) / 2.0F);
  }

  private float spacing(MotionEvent paramMotionEvent)
  {
    try
    {
      float f1 = paramMotionEvent.getX(0) - paramMotionEvent.getX(1);
      float f2 = paramMotionEvent.getY(0) - paramMotionEvent.getY(1);
      double d = Math.sqrt(f1 * f1 + f2 * f2);
      return (float)d;
    }
    catch (Exception paramMotionEvent)
    {
      paramMotionEvent.printStackTrace();
    }
    return 0.0F;
  }

  public void drawView(DPObject paramDPObject, String paramString1, String paramString2)
  {
    if (paramDPObject != null)
    {
      this.dpSeatPlan = paramDPObject;
      this.navImageView.reset();
      this.seatImageView.reset();
      this.seatThumbnailView.reset();
      this.seatPlanLoaded = true;
      paramDPObject = this.dpSeatPlan.edit();
      paramDPObject.putInt("Height", (int)(((this.dpSeatPlan.getInt("MaxRowIndex") + 3 + 1) * 78 - 18) * this.seatDensityFactor));
      paramDPObject.putInt("Width", (int)(((this.dpSeatPlan.getInt("MaxColumnIndex") + 1 + 1) * 78 - 18) * this.seatDensityFactor));
      this.dpSeatPlan = paramDPObject.generate();
      this.scaleType = caculateScaleType(this.dpSeatPlan.getInt("Width"), this.dpSeatPlan.getInt("Height"));
      if (this.scaleType != 2)
        break label342;
      paramDPObject = this.dpSeatPlan.edit();
      paramDPObject.putInt("Height", (int)(((this.dpSeatPlan.getInt("MaxRowIndex") + 3 + 1) * 52 - 12) * this.seatDensityFactor));
      paramDPObject.putInt("Width", (int)(((this.dpSeatPlan.getInt("MaxColumnIndex") + 1 + 1) * 52 - 12) * this.seatDensityFactor));
      this.dpSeatPlan = paramDPObject.generate();
    }
    label342: for (this.scaleMate = 0.66F; ; this.scaleMate = 1.0F)
    {
      this.seatImageView.setSeatDrawDelegate(new MovieSelectSeatView.2(this));
      this.seatImageView.addOnItemSelectListener(new MovieSelectSeatView.3(this));
      this.seatImageView.drawView(this.dpSeatPlan, this.scaleMate, this.scaleType, paramString1);
      if (!TextUtils.isEmpty(paramString2))
        this.seatThirdPartyName.setText("该场次信息由" + paramString2 + "提供");
      return;
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.seatPlanLoaded);
    float f;
    do
    {
      do
      {
        do
        {
          return true;
          switch (paramMotionEvent.getAction() & 0xFF)
          {
          case 6:
          case 3:
          case 4:
          default:
            return true;
          case 0:
            this.start.set(paramMotionEvent.getX(), paramMotionEvent.getY());
            this.tmpStart.set(this.start);
            mode = 1;
            this.singleClick = true;
            long l = System.currentTimeMillis();
            if (l - this.lastDownTime > 200L)
            {
              this.handler.removeMessages(1);
              paramMotionEvent = Message.obtain();
              paramMotionEvent.what = 1;
              Bundle localBundle = new Bundle();
              localBundle.putLong("downTime", l);
              localBundle.putFloat("scale", this.seatImageView.getScale());
              localBundle.putFloat("centerX", this.start.x);
              localBundle.putFloat("centerY", this.start.y);
              paramMotionEvent.setData(localBundle);
              this.handler.sendMessageDelayed(paramMotionEvent, 200L);
            }
            this.lastDownTime = l;
            return true;
          case 5:
            this.seatThumbnailView.setViewVisible(true);
            this.oldDist = spacing(paramMotionEvent);
          case 1:
          case 2:
          }
        }
        while (this.oldDist <= 10.0F);
        originalScale = this.seatImageView.getScale();
        midPoint(this.mid, paramMotionEvent);
        mode = 2;
        return true;
        if (!this.singleClick)
        {
          this.seatImageView.rebound();
          this.navImageView.rebound();
        }
        this.seatThumbnailView.setViewVisible(false);
        mode = 0;
        return true;
        if ((Math.abs(paramMotionEvent.getX() - this.tmpStart.x) >= 10.0F) || (Math.abs(paramMotionEvent.getY() - this.tmpStart.y) >= 10.0F))
          this.singleClick = false;
        if (mode != 1)
          continue;
        if ((Math.abs(paramMotionEvent.getX() - this.start.x) > 3.0F) || (Math.abs(paramMotionEvent.getY() - this.start.y) > 3.0F))
          this.seatThumbnailView.setViewVisible(true);
        this.seatImageView.moveView(paramMotionEvent.getX() - this.start.x, paramMotionEvent.getY() - this.start.y);
        this.navImageView.moveView(0.0F, paramMotionEvent.getY() - this.start.y);
        this.seatThumbnailView.updatePosition(this.seatImageView.getPositionParam());
        this.start.set(paramMotionEvent.getX(), paramMotionEvent.getY());
        return true;
      }
      while (mode != 2);
      f = spacing(paramMotionEvent);
    }
    while (f <= 10.0F);
    f /= this.oldDist;
    this.seatImageView.zoomTo(originalScale * f, this.mid.x, this.mid.y);
    this.navImageView.zoomTo(originalScale * f, ViewUtils.dip2px(getContext(), 30.0F) / 2, this.mid.y);
    this.seatThumbnailView.updatePosition(this.seatImageView.getPositionParam());
    return true;
  }

  public void recycle()
  {
    if (this.navImageView != null)
      this.navImageView.recycle();
    if (this.seatImageView != null)
      this.seatImageView.recycle();
    if (this.seatThumbnailView != null)
      this.seatThumbnailView.recycle();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.MovieSelectSeatView
 * JD-Core Version:    0.6.0
 */