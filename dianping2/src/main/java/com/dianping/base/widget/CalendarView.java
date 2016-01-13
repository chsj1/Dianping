package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarView extends LinearLayout
  implements View.OnClickListener
{
  private static final String NOT_OPTIONAL_COLOR = "#d7d7d7";
  private static final String TODAY_TEXT_COLOR = "#FFFF8000";
  protected Calendar calCalendar = Calendar.getInstance();
  protected Calendar calSelected = Calendar.getInstance();
  protected Calendar calStartDate = Calendar.getInstance();
  protected Calendar calToday = Calendar.getInstance();
  private int dayAfter;
  protected ArrayList<CalendarItem> days = new ArrayList();
  private Calendar endDate;
  private List<Calendar> exceptDates;
  private DPObject[] holidaysList;
  protected int iFirstDayOfWeek = 2;
  protected int iMonthViewCurrentMonth = 0;
  protected int iMonthViewCurrentYear = 0;
  protected boolean isAfterEnable = false;
  protected boolean isList = false;
  protected boolean isNext = false;
  private boolean isRange = false;
  LinearLayout layoutContent = null;
  private List<Calendar> lists = new ArrayList();
  protected Context mContext;
  private CalendarItem.OnItemClickListener mItemClickListener = new CalendarItem.OnItemClickListener()
  {
    public void onItemClick(CalendarItem paramCalendarItem)
    {
      if ((paramCalendarItem.isbIsActiveMonth()) && (paramCalendarItem.isEnable()))
      {
        CalendarView.this.calSelected.setTimeInMillis(paramCalendarItem.getDate().getTimeInMillis());
        paramCalendarItem.setbSelected(true);
        CalendarView.this.dispatchOnDataChange(paramCalendarItem);
        CalendarView.this.updateCalendar();
      }
    }
  };
  protected TextView monthTitle = null;
  protected ImageButton nextMonth;
  private OnDateChangeListener onDateChangeListener;
  protected ImageButton preMonth;
  protected Calendar splitDay;
  private Calendar startDate;
  protected TextView tip;
  protected LinearLayout tipLayout;

  public CalendarView(Context paramContext)
  {
    this(paramContext, null);
  }

  public CalendarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    initCalendarView();
  }

  private boolean afterEndCalendar(Calendar paramCalendar)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.clear();
    return paramCalendar.after(localCalendar);
  }

  private boolean beforeToday(Calendar paramCalendar)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.clear();
    localCalendar.set(1, this.calToday.get(1));
    localCalendar.set(2, this.calToday.get(2));
    localCalendar.set(5, this.calToday.get(5));
    return paramCalendar.before(localCalendar);
  }

  private void checkMonth(Calendar paramCalendar1, Calendar paramCalendar2, Calendar paramCalendar3)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(1, paramCalendar1.get(1));
    localCalendar.set(2, paramCalendar1.get(2));
    localCalendar.set(5, 1);
    paramCalendar1 = Calendar.getInstance();
    paramCalendar1.setTimeInMillis(localCalendar.getTimeInMillis());
    paramCalendar1.add(2, 1);
    if ((compareTwoDays(localCalendar, paramCalendar3) <= 0) && (compareTwoDays(paramCalendar1, paramCalendar3) > 0))
      this.preMonth.setVisibility(8);
    while (true)
    {
      paramCalendar1 = Calendar.getInstance();
      paramCalendar1.set(1, paramCalendar2.get(1));
      paramCalendar1.set(2, paramCalendar2.get(2));
      paramCalendar1.set(5, 1);
      paramCalendar2 = Calendar.getInstance();
      paramCalendar2.setTimeInMillis(paramCalendar1.getTimeInMillis());
      paramCalendar2.add(2, 1);
      if ((compareTwoDays(paramCalendar1, paramCalendar3) > 0) || (compareTwoDays(paramCalendar2, paramCalendar3) <= 0))
        break;
      this.nextMonth.setVisibility(8);
      return;
      this.preMonth.setVisibility(0);
    }
    this.nextMonth.setVisibility(0);
  }

  private LinearLayout createLayout(int paramInt)
  {
    LinearLayout localLinearLayout = new LinearLayout(this.mContext);
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localLinearLayout.setOrientation(paramInt);
    return localLinearLayout;
  }

  private void layoutInitialize()
  {
    this.days.clear();
    int i = 0;
    while (i < 6)
    {
      LinearLayout localLinearLayout = createLayout(0);
      localLinearLayout.setGravity(17);
      int j = 0;
      while (j < 7)
      {
        Object localObject = creatItemView();
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
        localLayoutParams.weight = 1.0F;
        localLinearLayout.addView((View)localObject, localLayoutParams);
        localObject = createCalendarItem((View)localObject);
        if ((j == 5) || (j == 6))
          ((CalendarItem)localObject).setWeekend(true);
        ((CalendarItem)localObject).setOnItemClickListener(this.mItemClickListener);
        this.days.add(localObject);
        j += 1;
      }
      this.layoutContent.addView(localLinearLayout);
      i += 1;
    }
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
    this.isNext = true;
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
    this.isNext = false;
    updateStartDateForMonth();
    updateCalendar();
  }

  private void setTodayViewItem()
  {
    this.calToday.setTimeInMillis(System.currentTimeMillis());
    this.calToday.setFirstDayOfWeek(this.iFirstDayOfWeek);
    this.calStartDate.setTimeInMillis(this.calToday.getTimeInMillis());
    this.calStartDate.setFirstDayOfWeek(this.iFirstDayOfWeek);
    updateStartDateForMonth();
    updateCalendar();
  }

  protected int compareTwoDays(Calendar paramCalendar1, Calendar paramCalendar2)
  {
    if (paramCalendar1.get(1) < paramCalendar2.get(1));
    do
    {
      return -1;
      if (paramCalendar1.get(1) > paramCalendar2.get(1))
        return 1;
    }
    while (paramCalendar1.get(6) < paramCalendar2.get(6));
    if (paramCalendar1.get(6) > paramCalendar2.get(6))
      return 1;
    return 0;
  }

  protected View creatItemView()
  {
    return LayoutInflater.from(getContext()).inflate(R.layout.booking_calendar_item, null, false);
  }

  protected CalendarItem createCalendarItem(View paramView)
  {
    return new CalendarItem(paramView);
  }

  protected void dispatchOnDataChange(CalendarItem paramCalendarItem)
  {
    if (this.onDateChangeListener != null)
      this.onDateChangeListener.onDateChange(this.calSelected);
  }

  protected void initCalendarView()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.calendar_layout, this, true);
    this.preMonth = ((ImageButton)findViewById(R.id.btn_pre_month));
    this.nextMonth = ((ImageButton)findViewById(R.id.btn_next_month));
    this.monthTitle = ((TextView)findViewById(R.id.month_title));
    this.preMonth.setOnClickListener(this);
    this.nextMonth.setOnClickListener(this);
    this.layoutContent = ((LinearLayout)findViewById(R.id.layout_content));
    this.tipLayout = ((LinearLayout)findViewById(R.id.tip_layout));
    this.tip = ((TextView)findViewById(R.id.tip));
    layoutInitialize();
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

  public void setDate(Calendar paramCalendar)
  {
    this.isList = false;
    this.calToday.setTimeInMillis(paramCalendar.getTimeInMillis());
    updateStartDateForMonth();
    updateCalendar();
  }

  public void setDate(Calendar paramCalendar1, Calendar paramCalendar2, Calendar paramCalendar3, List<Calendar> paramList)
  {
    this.isList = false;
    this.isRange = true;
    this.startDate = paramCalendar1;
    this.endDate = paramCalendar2;
    this.exceptDates = paramList;
    this.calStartDate.setTimeInMillis(paramCalendar1.getTimeInMillis());
    this.calSelected.setTimeInMillis(paramCalendar3.getTimeInMillis());
    updateStartDateForMonth();
    updateCalendar();
  }

  public void setDate(Calendar paramCalendar1, Calendar paramCalendar2, List<Calendar> paramList, int paramInt)
  {
    this.isList = true;
    this.isNext = false;
    this.lists = paramList;
    this.dayAfter = paramInt;
    this.calToday.setTimeInMillis(paramCalendar1.getTimeInMillis());
    this.calStartDate.setTimeInMillis(paramCalendar1.getTimeInMillis());
    this.calSelected.setTimeInMillis(paramCalendar2.getTimeInMillis());
    if (this.calStartDate.get(2) == this.calSelected.get(2))
    {
      updateStartDateForMonth();
      updateCalendar();
      return;
    }
    this.iMonthViewCurrentMonth = this.calStartDate.get(2);
    this.iMonthViewCurrentYear = this.calStartDate.get(1);
    setNextViewItem();
  }

  public void setDate(Calendar paramCalendar1, Calendar paramCalendar2, boolean paramBoolean)
  {
    this.isAfterEnable = paramBoolean;
    this.calToday.setTimeInMillis(paramCalendar1.getTimeInMillis());
    this.splitDay = paramCalendar2;
    this.calSelected.setTimeInMillis(paramCalendar2.getTimeInMillis());
    updateStartDateForMonth();
    updateCalendar();
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

  protected void updateCalendar()
  {
    int i;
    int m;
    int n;
    int i1;
    int j;
    int i2;
    int i3;
    int i4;
    CalendarItem localCalendarItem;
    boolean bool1;
    boolean bool2;
    boolean bool3;
    int k;
    if (this.calSelected.getTimeInMillis() != 0L)
    {
      i = 1;
      m = this.calSelected.get(1);
      n = this.calSelected.get(2);
      i1 = this.calSelected.get(5);
      this.calCalendar.setTimeInMillis(this.calStartDate.getTimeInMillis());
      this.calCalendar.setFirstDayOfWeek(this.iFirstDayOfWeek);
      j = 0;
      if (j >= this.days.size())
        return;
      i2 = this.calCalendar.get(1);
      i3 = this.calCalendar.get(2);
      i4 = this.calCalendar.get(5);
      this.calCalendar.get(7);
      localCalendarItem = (CalendarItem)this.days.get(j);
      bool1 = false;
      bool2 = bool1;
      if (this.calToday.get(1) == i2)
      {
        bool2 = bool1;
        if (this.calToday.get(2) == i3)
        {
          bool2 = bool1;
          if (this.calToday.get(5) == i4)
            bool2 = true;
        }
      }
      bool3 = false;
      if (!this.isRange)
        break label491;
      if ((compareTwoDays(this.calCalendar, this.startDate) < 0) || (compareTwoDays(this.calCalendar, this.endDate) > 0))
        break label485;
      bool3 = true;
      bool1 = bool3;
      if (this.exceptDates != null)
      {
        k = 0;
        label252: bool1 = bool3;
        if (k < this.exceptDates.size())
        {
          if (compareTwoDays(this.calCalendar, (Calendar)this.exceptDates.get(k)) != 0)
            break label476;
          bool1 = false;
        }
      }
    }
    while (true)
    {
      localCalendarItem.setDate(i2, i3, i4, bool2, this.iMonthViewCurrentMonth, bool1);
      bool3 = false;
      bool1 = bool3;
      if (i != 0)
      {
        bool1 = bool3;
        if (i1 == i4)
        {
          bool1 = bool3;
          if (n == i3)
          {
            bool1 = bool3;
            if (m == i2)
              bool1 = true;
          }
        }
      }
      localCalendarItem.setbSelected(bool1);
      localCalendarItem.setDateSubInfo("");
      if ((this.holidaysList == null) || (this.holidaysList.length == 0))
        break label620;
      k = 0;
      while (k < this.holidaysList.length)
      {
        long l = this.holidaysList[k].getTime("Day");
        localObject1 = this.holidaysList[k].getString("Title");
        localObject2 = Calendar.getInstance();
        ((Calendar)localObject2).setTimeInMillis(l);
        if (compareTwoDays((Calendar)localObject2, localCalendarItem.getDate()) == 0)
          localCalendarItem.setDateSubInfo((String)localObject1);
        k += 1;
      }
      i = 0;
      break;
      label476: k += 1;
      break label252;
      label485: bool1 = false;
      continue;
      label491: if (this.isList)
      {
        k = 0;
        while (true)
        {
          bool1 = bool3;
          if (k >= this.lists.size())
            break;
          localObject1 = Calendar.getInstance();
          ((Calendar)localObject1).setTimeInMillis(((Calendar)this.lists.get(k)).getTimeInMillis());
          if (compareTwoDays(this.calCalendar, (Calendar)localObject1) == 0)
          {
            bool1 = true;
            break;
          }
          k += 1;
        }
      }
      if (this.splitDay != null)
      {
        bool1 = bool3;
        if (!this.isAfterEnable)
          continue;
        bool1 = bool3;
        if (compareTwoDays(this.splitDay, this.calCalendar) > 0)
          continue;
        bool1 = true;
        continue;
      }
      bool1 = true;
    }
    label620: this.calCalendar.add(5, 1);
    Object localObject1 = ((CalendarItem)this.days.get(j)).getView();
    Object localObject2 = ((CalendarItem)this.days.get(j)).getDateTextView();
    ((TextView)localObject2).setText(String.valueOf(localCalendarItem.getiDateDay()));
    if (localCalendarItem.isbIsActiveMonth())
      if (bool1)
      {
        ((View)localObject1).setSelected(true);
        ((View)localObject1).setBackgroundResource(R.drawable.yy_calendar_chosen);
        ((TextView)localObject2).setTextColor(-1);
        if (bool2)
          ((TextView)localObject2).setText("今天");
        label713: localObject1 = ((CalendarItem)this.days.get(j)).getDateSubTextView();
        if (localObject1 != null)
        {
          if (localCalendarItem.getDateSubInfo() == null)
            break label901;
          ((TextView)localObject1).setText(localCalendarItem.getDateSubInfo());
          label748: if (!localCalendarItem.isbIsActiveMonth())
            break label974;
          if (!bool1)
            break label911;
          ((TextView)localObject1).setTextColor(-1);
        }
      }
    while (true)
    {
      j += 1;
      break;
      ((View)localObject1).setSelected(false);
      if (localCalendarItem.isEnable())
      {
        if (bool2)
        {
          ((TextView)localObject2).setText("今天");
          ((View)localObject1).setBackgroundResource(R.drawable.yy_calendar_today_optional);
          ((TextView)localObject2).setTextColor(Color.parseColor("#FFFF8000"));
          break label713;
        }
        ((View)localObject1).setBackgroundResource(R.drawable.yy_calendar_optional);
        ((TextView)localObject2).setTextColor(-16777216);
        break label713;
      }
      if (bool2)
      {
        ((TextView)localObject2).setText("今天");
        ((View)localObject1).setBackgroundResource(R.drawable.yy_calendar_today_notoptional);
        ((TextView)localObject2).setTextColor(Color.parseColor("#FFFF8000"));
        break label713;
      }
      ((View)localObject1).setBackgroundResource(R.drawable.yy_calendar_notoptional);
      ((TextView)localObject2).setTextColor(Color.parseColor("#d7d7d7"));
      break label713;
      ((TextView)localObject2).setText("");
      ((View)localObject1).setBackgroundResource(R.drawable.yy_calendar_notoptional);
      break label713;
      label901: ((TextView)localObject1).setText("");
      break label748;
      label911: if (localCalendarItem.isEnable())
      {
        if (bool2)
        {
          ((TextView)localObject1).setTextColor(Color.parseColor("#FFFF8000"));
          continue;
        }
        ((TextView)localObject1).setTextColor(-16777216);
        continue;
      }
      if (bool2)
      {
        ((TextView)localObject1).setTextColor(Color.parseColor("#FFFF8000"));
        continue;
      }
      ((TextView)localObject1).setTextColor(Color.parseColor("#d7d7d7"));
      continue;
      label974: ((TextView)localObject1).setText("");
    }
  }

  protected void updateCurrentMonthDisplay()
  {
    this.monthTitle.setText(DateFormat.format("yyyy年MM月", this.calStartDate));
    if (this.isList)
    {
      if (this.isNext)
      {
        this.preMonth.setVisibility(0);
        this.nextMonth.setVisibility(8);
        return;
      }
      this.preMonth.setVisibility(8);
      this.nextMonth.setVisibility(0);
      return;
    }
    if ((this.startDate != null) && (this.endDate != null))
    {
      checkMonth(this.startDate, this.endDate, this.calStartDate);
      return;
    }
    this.preMonth.setVisibility(0);
    this.nextMonth.setVisibility(0);
  }

  protected void updateStartDateForMonth()
  {
    this.iMonthViewCurrentMonth = this.calStartDate.get(2);
    this.iMonthViewCurrentYear = this.calStartDate.get(1);
    this.calStartDate.set(5, 1);
    updateCurrentMonthDisplay();
    updateTipDisplay();
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

  protected void updateTipDisplay()
  {
    if (this.dayAfter > 0)
    {
      this.tipLayout.setVisibility(0);
      this.tip.setText("本店需提前" + this.dayAfter + "天预订");
      return;
    }
    this.tipLayout.setVisibility(8);
  }

  public static abstract interface OnDateChangeListener
  {
    public abstract void onDateChange(Calendar paramCalendar);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CalendarView
 * JD-Core Version:    0.6.0
 */