package com.dianping.base.widget;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.v1.R.id;
import java.util.Calendar;

public class CalendarItem
  implements View.OnClickListener
{
  private boolean bIsActiveMonth = false;
  private boolean bSelected = false;
  private boolean bToday = false;
  private String dateSubInfo;
  private boolean enable = false;
  private int iDateDay = 0;
  private int iDateMonth = 0;
  private int iDateYear = 0;
  private boolean isWeekend = false;
  protected View itemView;
  private OnItemClickListener onItemClickListener;

  public CalendarItem(View paramView)
  {
    this.itemView = paramView;
    this.itemView.setOnClickListener(this);
  }

  public Calendar getDate()
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.clear();
    localCalendar.set(1, this.iDateYear);
    localCalendar.set(2, this.iDateMonth);
    localCalendar.set(5, this.iDateDay);
    return localCalendar;
  }

  public String getDateSubInfo()
  {
    return this.dateSubInfo;
  }

  public TextView getDateSubTextView()
  {
    return (TextView)this.itemView.findViewById(R.id.date_subinfo);
  }

  public TextView getDateTextView()
  {
    return (TextView)this.itemView.findViewById(R.id.date_info);
  }

  public View getView()
  {
    return this.itemView;
  }

  public int getiDateDay()
  {
    return this.iDateDay;
  }

  public int getiDateMonth()
  {
    return this.iDateMonth;
  }

  public int getiDateYear()
  {
    return this.iDateYear;
  }

  public boolean isEnable()
  {
    return this.enable;
  }

  public boolean isWeekend()
  {
    return this.isWeekend;
  }

  public boolean isbIsActiveMonth()
  {
    return this.bIsActiveMonth;
  }

  public void onClick(View paramView)
  {
    if ((paramView == this.itemView) && (this.onItemClickListener != null))
      this.onItemClickListener.onItemClick(this);
  }

  public void setDate(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, int paramInt4, boolean paramBoolean2)
  {
    this.iDateYear = paramInt1;
    this.iDateMonth = paramInt2;
    this.iDateDay = paramInt3;
    if (paramInt2 == paramInt4);
    for (boolean bool = true; ; bool = false)
    {
      this.bIsActiveMonth = bool;
      this.bToday = paramBoolean1;
      this.enable = paramBoolean2;
      return;
    }
  }

  public void setDateSubInfo(String paramString)
  {
    this.dateSubInfo = paramString;
  }

  public void setEnable(boolean paramBoolean)
  {
    this.enable = paramBoolean;
  }

  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener)
  {
    this.onItemClickListener = paramOnItemClickListener;
  }

  public void setWeekend(boolean paramBoolean)
  {
    this.isWeekend = paramBoolean;
  }

  public void setbIsActiveMonth(boolean paramBoolean)
  {
    this.bIsActiveMonth = paramBoolean;
  }

  public void setbSelected(boolean paramBoolean)
  {
    this.bSelected = paramBoolean;
  }

  public static abstract interface OnItemClickListener
  {
    public abstract void onItemClick(CalendarItem paramCalendarItem);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CalendarItem
 * JD-Core Version:    0.6.0
 */