package com.dianping.travel.view;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CalendarItem;
import com.dianping.base.widget.CalendarView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PriceCalendarView extends CalendarView
  implements RequestHandler<MApiRequest, MApiResponse>
{
  MApiService mapiService;
  private OnDateChangeListener onDateChangeListener;
  int policyID;
  Map<Long, Integer> priceMap = new HashMap();
  MApiRequest request;

  public PriceCalendarView(Context paramContext)
  {
    this(paramContext, null);
  }

  public PriceCalendarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void requestData()
  {
    requestData(0);
  }

  private void requestData(int paramInt)
  {
    resetPrices();
    if (this.request != null)
      this.mapiService.abort(this.request, this, true);
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(this.iMonthViewCurrentYear, this.iMonthViewCurrentMonth, 1);
    this.request = BasicMApiRequest.mapiGet("http://m.api.dianping.com/getpricecalendar.bin?policyid=" + this.policyID + "&startdate=" + new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(localCalendar.getTime()), CacheType.NORMAL);
    this.mapiService.exec(this.request, this);
  }

  private void resetPrices()
  {
    int i = 0;
    while (i < this.days.size())
    {
      ((PriceCalendarItem)this.days.get(i)).setPrice(0);
      i += 1;
    }
  }

  private void updatePrices()
  {
    int i = 0;
    while (i < this.days.size())
    {
      PriceCalendarItem localPriceCalendarItem = (PriceCalendarItem)this.days.get(i);
      Integer localInteger;
      View localView;
      TextView localTextView;
      if ((localPriceCalendarItem.isbIsActiveMonth()) && (localPriceCalendarItem.isEnable()))
      {
        localInteger = (Integer)this.priceMap.get(Long.valueOf(localPriceCalendarItem.getDate().getTimeInMillis()));
        localView = localPriceCalendarItem.getView();
        localTextView = localPriceCalendarItem.getDateTextView();
        if (localInteger == null)
        {
          localPriceCalendarItem.setPrice(0);
          localPriceCalendarItem.setbIsActiveMonth(false);
          localPriceCalendarItem.setEnable(false);
          localTextView.setTextColor(-7829368);
          localView.setBackgroundResource(R.drawable.calendar_item_bg_disable);
        }
      }
      else
      {
        i += 1;
        continue;
      }
      localPriceCalendarItem.setPrice(localInteger.intValue());
      localPriceCalendarItem.setbIsActiveMonth(true);
      localPriceCalendarItem.setEnable(true);
      if (localPriceCalendarItem.isWeekend())
        localTextView.setTextColor(-65536);
      while (true)
      {
        localView.setBackgroundResource(R.drawable.calendar_item_bg);
        break;
        localTextView.setTextColor(-16777216);
      }
    }
  }

  protected View creatItemView()
  {
    return LayoutInflater.from(this.mContext).inflate(R.layout.price_calendar_item, null);
  }

  protected CalendarItem createCalendarItem(View paramView)
  {
    return new PriceCalendarItem(paramView);
  }

  protected void dispatchOnDataChange(CalendarItem paramCalendarItem)
  {
    if (this.onDateChangeListener != null)
      this.onDateChangeListener.onDateChange(this.calSelected, ((PriceCalendarItem)paramCalendarItem).getPrice());
  }

  protected void initCalendarView()
  {
    super.initCalendarView();
    this.mapiService = ((DPActivity)getContext()).mapiService();
  }

  public void onClick(View paramView)
  {
    super.onClick(paramView);
    int i = paramView.getId();
    if (i == R.id.btn_pre_month)
      requestData(0);
    do
      return;
    while (i != R.id.btn_next_month);
    requestData(1);
  }

  protected void onDetachedFromWindow()
  {
    if (this.request != null)
      this.mapiService.abort(this.request, this, true);
    super.onDetachedFromWindow();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    Log.e("price calendar view", "request failed:" + paramMApiResponse.message().content());
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject[]))
    {
      paramMApiRequest = (DPObject[])(DPObject[])paramMApiResponse.result();
      int j = paramMApiRequest.length;
      int i = 0;
      while (i < j)
      {
        paramMApiResponse = paramMApiRequest[i];
        this.priceMap.put(Long.valueOf(paramMApiResponse.getTime("Time")), Integer.valueOf(paramMApiResponse.getInt("Price")));
        i += 1;
      }
      updatePrices();
    }
  }

  public void setDate(Calendar paramCalendar1, Calendar paramCalendar2, boolean paramBoolean, int paramInt1, int paramInt2)
  {
    super.setDate(paramCalendar1, paramCalendar2, paramBoolean);
    this.policyID = paramInt1;
    requestData();
  }

  public void setOnDateChangeListener(OnDateChangeListener paramOnDateChangeListener)
  {
    this.onDateChangeListener = paramOnDateChangeListener;
  }

  protected void updateCurrentMonthDisplay()
  {
    this.monthTitle.setText(DateFormat.format("yyyy年MM月", this.calStartDate));
    if (this.isNext)
    {
      this.preMonth.setVisibility(0);
      this.nextMonth.setVisibility(8);
      return;
    }
    this.preMonth.setVisibility(8);
    this.nextMonth.setVisibility(0);
  }

  public static abstract interface OnDateChangeListener
  {
    public abstract void onDateChange(Calendar paramCalendar, int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.view.PriceCalendarView
 * JD-Core Version:    0.6.0
 */