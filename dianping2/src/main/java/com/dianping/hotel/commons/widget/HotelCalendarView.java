package com.dianping.hotel.commons.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CalendarItem;
import com.dianping.base.widget.CalendarItem.OnItemClickListener;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public abstract class HotelCalendarView extends LinearLayout
  implements View.OnClickListener
{
  protected Calendar calSelected = Calendar.getInstance();
  protected Calendar calStartDate = Calendar.getInstance();
  protected Calendar calToday = Calendar.getInstance();
  protected ArrayList<CalendarItem> days = new ArrayList();
  protected Calendar deadlineCalendar = Calendar.getInstance();
  protected DisplayMetrics dm = null;
  protected DPObject[] holidaysList;
  protected int iFirstDayOfWeek = 2;
  protected int iMonthViewCurrentMonth = 0;
  protected int iMonthViewCurrentYear = 0;
  protected boolean isAfterEnable = false;
  private boolean isDealTrave = false;
  private int itemHeight;
  protected LinearLayout layoutContent = null;
  protected Context mContext;
  protected CalendarItem.OnItemClickListener mItemClickListener = new CalendarItem.OnItemClickListener()
  {
    public void onItemClick(CalendarItem paramCalendarItem)
    {
      if ((paramCalendarItem.isbIsActiveMonth()) && (paramCalendarItem.isEnable()))
      {
        HotelCalendarView.this.calSelected.setTimeInMillis(paramCalendarItem.getDate().getTimeInMillis());
        paramCalendarItem.setbSelected(true);
        HotelCalendarView.this.dispatchOnDataChange(paramCalendarItem);
        HotelCalendarView.this.updateCalendar();
      }
    }
  };
  protected TextView monthTitle = null;
  private OnDateChangeListener onDateChangeListener;
  protected TextView tip;
  protected LinearLayout tipLayout;

  public HotelCalendarView(Context paramContext)
  {
    this(paramContext, null);
  }

  public HotelCalendarView(Context paramContext, int paramInt, boolean paramBoolean)
  {
    this(paramContext, null);
    this.itemHeight = paramInt;
    this.isDealTrave = paramBoolean;
    initCalendarView();
  }

  public HotelCalendarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  public static int compareTwoDays(Calendar paramCalendar1, Calendar paramCalendar2)
  {
    if (paramCalendar1.get(1) < paramCalendar2.get(1));
    do
    {
      do
      {
        return -1;
        if (paramCalendar1.get(1) > paramCalendar2.get(1))
          return 1;
      }
      while (paramCalendar1.get(2) < paramCalendar2.get(2));
      if (paramCalendar1.get(2) > paramCalendar2.get(2))
        return 1;
    }
    while (paramCalendar1.get(5) < paramCalendar2.get(5));
    if (paramCalendar1.get(5) > paramCalendar2.get(5))
      return 1;
    return 0;
  }

  private LinearLayout createLayout(int paramInt)
  {
    LinearLayout localLinearLayout = new LinearLayout(this.mContext);
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localLinearLayout.setBackgroundColor(-1);
    localLinearLayout.setOrientation(paramInt);
    return localLinearLayout;
  }

  private void setNextViewItem()
  {
    this.iMonthViewCurrentMonth += 1;
    if (this.iMonthViewCurrentMonth == 12)
    {
      this.iMonthViewCurrentMonth = 0;
      this.iMonthViewCurrentYear += 1;
    }
    this.calStartDate.set(2, this.iMonthViewCurrentMonth);
    this.calStartDate.set(1, this.iMonthViewCurrentYear);
    this.calStartDate.set(5, 1);
    updateStartDateForMonth();
    updateCalendar();
  }

  private void setPrevViewItem()
  {
    this.iMonthViewCurrentMonth -= 1;
    if (this.iMonthViewCurrentMonth == -1)
    {
      this.iMonthViewCurrentMonth = 11;
      this.iMonthViewCurrentYear -= 1;
    }
    this.calStartDate.set(2, this.iMonthViewCurrentMonth);
    this.calStartDate.set(1, this.iMonthViewCurrentYear);
    this.calStartDate.set(5, 1);
    updateStartDateForMonth();
    updateCalendar();
  }

  protected CalendarItem createCalendarItem(View paramView, Calendar paramCalendar)
  {
    paramView = new CalendarItem(paramView);
    int i = paramCalendar.get(1);
    int j = paramCalendar.get(2);
    int k = paramCalendar.get(5);
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (this.calToday.get(1) == i)
    {
      bool2 = bool1;
      if (this.calToday.get(2) == j)
      {
        bool2 = bool1;
        if (this.calToday.get(5) == k)
          bool2 = true;
      }
    }
    boolean bool3 = false;
    bool1 = bool3;
    if (this.isAfterEnable)
    {
      if (compareTwoDays(paramCalendar, this.deadlineCalendar) <= 0)
        break label132;
      bool1 = false;
    }
    while (true)
    {
      paramView.setDate(i, j, k, bool2, this.iMonthViewCurrentMonth, bool1);
      return paramView;
      label132: bool1 = bool3;
      if (compareTwoDays(paramCalendar, this.calToday) < 0)
        continue;
      bool1 = true;
    }
  }

  protected View createDivider()
  {
    ImageView localImageView = new ImageView(getContext());
    localImageView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localImageView.setBackgroundResource(R.drawable.divider_gray_horizontal_line);
    return localImageView;
  }

  protected void dispatchOnDataChange(CalendarItem paramCalendarItem)
  {
    if (this.onDateChangeListener != null)
      this.onDateChangeListener.onDateChange(this.calSelected, ((Integer)paramCalendarItem.getView().getTag()).intValue());
  }

  protected void initCalendarView()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.hotel_calendar_layout, this, true);
    this.monthTitle = ((TextView)findViewById(R.id.month_title));
    this.layoutContent = ((LinearLayout)findViewById(R.id.layout_content));
    this.tipLayout = ((LinearLayout)findViewById(R.id.tip_layout));
    this.tip = ((TextView)findViewById(R.id.tip));
    this.dm = this.mContext.getResources().getDisplayMetrics();
  }

  protected boolean layoutInitialize(int paramInt)
  {
    this.days.clear();
    this.layoutContent.removeAllViews();
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(this.calStartDate.getTimeInMillis());
    int i = 0;
    int k = 0;
    while (k < 6)
    {
      LinearLayout localLinearLayout = createLayout(0);
      localLinearLayout.setGravity(17);
      int m = 0;
      int j = 0;
      while (j < 7)
      {
        Object localObject = LayoutInflater.from(getContext()).inflate(R.layout.hotel_calendar_item, null, false);
        ((View)localObject).setTag(Integer.valueOf(paramInt + i));
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
        int n = (int)(this.itemHeight * 0.1D);
        localLayoutParams.topMargin = n;
        localLayoutParams.bottomMargin = n;
        localLayoutParams.weight = 1.0F;
        FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams)((View)localObject).findViewById(R.id.info).getLayoutParams();
        localLayoutParams1.height = (this.itemHeight - ViewUtils.dip2px(getContext(), 2.0F));
        if (this.isDealTrave)
        {
          localLayoutParams1.height = -2;
          ((View)localObject).findViewById(R.id.date_subinfo_2).setVisibility(0);
        }
        ((View)localObject).findViewById(R.id.info).setLayoutParams(localLayoutParams1);
        localLinearLayout.addView((View)localObject, localLayoutParams);
        localObject = createCalendarItem((View)localObject, localCalendar);
        localCalendar.add(5, 1);
        if ((j == 5) || (j == 6))
          ((CalendarItem)localObject).setWeekend(true);
        ((CalendarItem)localObject).setOnItemClickListener(this.mItemClickListener);
        this.days.add(localObject);
        n = m;
        if (((CalendarItem)localObject).isbIsActiveMonth())
        {
          n = m;
          if (((CalendarItem)localObject).isEnable())
            n = 1;
        }
        j += 1;
        i += 1;
        m = n;
      }
      if (m == 0)
      {
        m = 0;
        while (true)
        {
          j = i;
          if (m >= 7)
            break;
          j = i;
          if (this.days.size() - 1 >= 0)
          {
            this.days.remove(this.days.size() - 1);
            j = i - 1;
          }
          m += 1;
          i = j;
        }
      }
      this.layoutContent.addView(localLinearLayout);
      this.layoutContent.addView(createDivider());
      j = i;
      k += 1;
      i = j;
    }
    return (this.days == null) || (this.days.size() == 0);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.btn_pre_month)
      setPrevViewItem();
    do
    {
      return;
      if (i != R.id.btn_next_month)
        continue;
      setNextViewItem();
      return;
    }
    while (i != R.id.layout_content);
  }

  public void setHoliday(DPObject[] paramArrayOfDPObject)
  {
    this.holidaysList = paramArrayOfDPObject;
    if ((paramArrayOfDPObject != null) && (paramArrayOfDPObject.length != 0))
      updateCalendar();
  }

  public void setOnDateChangeListener(OnDateChangeListener paramOnDateChangeListener)
  {
    this.onDateChangeListener = paramOnDateChangeListener;
  }

  protected abstract void updateCalendar();

  protected void updateCurrentMonthDisplay()
  {
    try
    {
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy年MM月", Locale.getDefault());
      this.monthTitle.setText(localSimpleDateFormat.format(Long.valueOf(this.calStartDate.getTimeInMillis())));
      return;
    }
    catch (Exception localException)
    {
    }
  }

  protected void updateStartDateForMonth()
  {
    this.iMonthViewCurrentMonth = this.calStartDate.get(2);
    this.iMonthViewCurrentYear = this.calStartDate.get(1);
    this.calStartDate.set(5, 1);
    updateCurrentMonthDisplay();
    int i = 0;
    int k = this.iFirstDayOfWeek;
    int j;
    if (k == 2)
    {
      j = this.calStartDate.get(7) - 2;
      i = j;
      if (j < 0)
        i = 6;
    }
    if (k == 1)
    {
      j = this.calStartDate.get(7) - 1;
      i = j;
      if (j < 0)
        i = 6;
    }
    this.calStartDate.add(7, -i);
  }

  public static abstract interface OnDateChangeListener
  {
    public abstract void onDateChange(Calendar paramCalendar, int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.commons.widget.HotelCalendarView
 * JD-Core Version:    0.6.0
 */