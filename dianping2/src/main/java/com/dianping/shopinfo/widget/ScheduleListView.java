package com.dianping.shopinfo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ExpandAnimation;
import com.dianping.base.widget.ExpandAnimation.OnExpendActionListener;
import com.dianping.base.widget.ExpandAnimation.OnExpendAnimationListener;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public abstract class ScheduleListView extends NovaLinearLayout
  implements View.OnClickListener, ExpandAnimation.OnExpendActionListener, ExpandAnimation.OnExpendAnimationListener
{
  static final int DEFAULT_MAX_SHOW_NUMBER_FROM_SHOP = 3;
  private static final int EXPAND_STATUS_ANIMATE = 1;
  private static final int EXPAND_STATUS_NORMAL = 0;
  private int defaultScheduleMaxShowNumber = 3;
  private DPObject[] dpScheduleDatas;
  private int expandStatus = 0;
  private ExpandView expandView;
  ScrollView fragmentScrollView;
  private boolean hasSetExpand = false;
  private boolean isExpand = false;
  private LinearLayout layerScheduleExpand;
  View viewShowAtScrollView;

  public ScheduleListView(Context paramContext)
  {
    super(paramContext);
  }

  public ScheduleListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void scrollToCenter()
  {
    if ((this.fragmentScrollView != null) && (this.viewShowAtScrollView != null) && (this.isExpand))
      this.viewShowAtScrollView.postDelayed(new Runnable()
      {
        public void run()
        {
          ScheduleListView.this.fragmentScrollView.setSmoothScrollingEnabled(true);
          try
          {
            ScheduleListView.this.fragmentScrollView.requestChildFocus(ScheduleListView.this.viewShowAtScrollView, ScheduleListView.this.viewShowAtScrollView);
            return;
          }
          catch (Exception localException)
          {
          }
        }
      }
      , 300L);
  }

  private void setExpandAction()
  {
    if ((this.layerScheduleExpand == null) || (this.expandStatus == 1))
      return;
    ExpandAnimation localExpandAnimation = new ExpandAnimation(this.layerScheduleExpand, 300);
    localExpandAnimation.setOnAnimationListener(this);
    localExpandAnimation.setOnExpendActionListener(this);
    this.layerScheduleExpand.startAnimation(localExpandAnimation);
  }

  abstract View createListItem(DPObject paramDPObject);

  public void onAnimationEnd()
  {
    this.expandStatus = 0;
    this.expandView.setExpandViewSpread(this.isExpand);
  }

  public void onAnimationStart()
  {
    this.expandStatus = 1;
  }

  public void onClick(View paramView)
  {
    boolean bool2 = true;
    boolean bool1 = true;
    if (paramView.getTag() == "EXPAND")
    {
      if (this.hasSetExpand != true)
        break label51;
      setIsExpandState();
      if (this.isExpand)
        break label46;
    }
    while (true)
    {
      this.isExpand = bool1;
      setExpandAction();
      scrollToCenter();
      return;
      label46: bool1 = false;
    }
    label51: if (!this.isExpand);
    for (bool1 = bool2; ; bool1 = false)
    {
      this.isExpand = bool1;
      break;
    }
  }

  public void onExpendAction(View paramView)
  {
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
  }

  public void setDefaultScheduleMaxShowNumber(int paramInt)
  {
    this.defaultScheduleMaxShowNumber = paramInt;
  }

  public void setExpandValue(boolean paramBoolean)
  {
    this.isExpand = paramBoolean;
    this.hasSetExpand = true;
  }

  public void setExpandView(ExpandView paramExpandView)
  {
    this.expandView = paramExpandView;
  }

  abstract void setIsExpandState();

  public void setScheduleDatas(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject != null)
    {
      this.dpScheduleDatas = paramArrayOfDPObject;
      removeAllViews();
      setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      setOrientation(1);
      int i = 0;
      while ((i < this.dpScheduleDatas.length) && (i < this.defaultScheduleMaxShowNumber))
      {
        addView(createListItem(this.dpScheduleDatas[i]));
        i += 1;
      }
      if (this.dpScheduleDatas.length > this.defaultScheduleMaxShowNumber)
      {
        this.layerScheduleExpand = new LinearLayout(getContext());
        this.layerScheduleExpand.setOrientation(1);
        this.layerScheduleExpand.removeAllViews();
        paramArrayOfDPObject = new LinearLayout.LayoutParams(-1, -2);
        if ((!this.isExpand) && (this.dpScheduleDatas.length > 0))
        {
          paramArrayOfDPObject.bottomMargin = ((int)(-createListItem(this.dpScheduleDatas[0]).getLayoutParams().height) * (this.dpScheduleDatas.length - this.defaultScheduleMaxShowNumber));
          this.layerScheduleExpand.setVisibility(8);
        }
        this.layerScheduleExpand.setLayoutParams(paramArrayOfDPObject);
        i = this.defaultScheduleMaxShowNumber;
        while (i < this.dpScheduleDatas.length)
        {
          this.layerScheduleExpand.addView(createListItem(this.dpScheduleDatas[i]));
          i += 1;
        }
        addView(this.layerScheduleExpand);
        if (this.expandView == null)
          this.expandView = ((ExpandView)LayoutInflater.from(getContext()).inflate(R.layout.shop_expand_view, this, false));
        this.expandView.setTag("EXPAND");
        this.expandView.setClickable(true);
        this.expandView.setOnClickListener(this);
        addView(this.expandView);
        this.expandView.setExpandViewSpread(this.isExpand);
      }
    }
  }

  public void setViewShowAtScollView(ScrollView paramScrollView, View paramView)
  {
    this.fragmentScrollView = paramScrollView;
    this.viewShowAtScrollView = paramView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.ScheduleListView
 * JD-Core Version:    0.6.0
 */