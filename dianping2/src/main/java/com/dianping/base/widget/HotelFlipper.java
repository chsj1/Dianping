package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.adapter.HotelFlipperAdapter;
import com.dianping.v1.R.id;

public class HotelFlipper<T> extends Flipper<T>
{
  private DPObject dpDeal;
  private final GestureDetector gestureDetector = new GestureDetector(this.gestureListener);
  private final GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener()
  {
    public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      HotelFlipper.this.onFling(paramFloat1);
      return true;
    }

    public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      HotelFlipper.access$002(HotelFlipper.this, true);
      HotelFlipper.this.onScrollX(paramMotionEvent1, paramMotionEvent2, paramFloat1);
      return true;
    }

    public void onShowPress(MotionEvent paramMotionEvent)
    {
      HotelFlipper.this.animationMode = 0;
      super.onShowPress(paramMotionEvent);
    }

    public boolean onSingleTapUp(MotionEvent paramMotionEvent)
    {
      HotelFlipper.this.onTap();
      return true;
    }
  };
  private boolean isScrolling;

  public HotelFlipper(Context paramContext)
  {
    this(paramContext, null);
  }

  public HotelFlipper(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean moveToNext(boolean paramBoolean)
  {
    if (((HotelFlipperAdapter)this.adapter).isLastItem(this.currentItem))
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hoteldealdetailmore"));
      localIntent.putExtra("mDeal", this.dpDeal);
      localIntent.putExtra("fromFlipper", true);
      getContext().startActivity(localIntent);
      restorePosition(paramBoolean);
      return true;
    }
    return super.moveToNext(paramBoolean);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.gestureDetector.onTouchEvent(paramMotionEvent))
    {
      if ((paramMotionEvent.getAction() == 1) && (this.isScrolling))
      {
        onScrollXEnd();
        this.isScrolling = false;
      }
      if (paramMotionEvent.getAction() == 3)
      {
        onScrollXEnd();
        this.isScrolling = false;
      }
    }
    return true;
  }

  public void restorePosition(boolean paramBoolean)
  {
    View localView = this.currentView.findViewById(R.id.last_pic_text);
    if (localView != null)
    {
      if (!((HotelFlipperAdapter)this.adapter).isLastItem(this.currentItem))
        break label45;
      localView.setVisibility(0);
    }
    while (true)
    {
      super.restorePosition(paramBoolean);
      return;
      label45: localView.setVisibility(8);
    }
  }

  public void setDpDeal(DPObject paramDPObject)
  {
    this.dpDeal = paramDPObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.HotelFlipper
 * JD-Core Version:    0.6.0
 */