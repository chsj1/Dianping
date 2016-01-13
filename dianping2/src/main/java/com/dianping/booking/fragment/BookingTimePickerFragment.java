package com.dianping.booking.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.CalendarView;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.booking.BookingInfoActivity;
import com.dianping.booking.util.BookableTime;
import com.dianping.booking.util.BookingConfig;
import com.dianping.booking.util.BookingUtil;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.DateUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BookingTimePickerFragment extends NovaFragment
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int COLOR_BLACK = -13487566;
  private static final int COLOR_GREY = -7566196;
  private static final int COLOR_LIGHT_BLUE = -10777680;
  private static final int COLOR_ORANGE_RED = -39373;
  private static final int COLOR_WHITE = -1;
  private static final int MORE_DATE_LAYOUT_WIDTH = 71;
  private static final int PERIOD_NUM = 4;
  private static final String UNBOOKABLE = "不可订";
  private static final int VISIBLE_NUM = 3;
  private int bookingNum;
  private Dialog calendarDialog;
  private CalendarView calendarView;
  private BookingConfig config;
  private Button confirmPickingBtn;
  private LinearLayout confirmPickingLayout;
  private ConcurrentHashMap<String, DPObject[]> couponInfo = new ConcurrentHashMap();
  private MApiRequest couponInfoRequest;
  private HorizontalScrollView dateInfoItemHSV;
  private LinearLayout dateInfoItemLayout;
  private int dateInfoItemWidth;
  private List<BookingTimePickerFragment.DateItemViewHolder> dateItems = new ArrayList();
  private ImageView dayPeriodIcon;
  private TextView dayPeriodText;
  private LinearLayout dayPeriodView;
  private int fromType;
  private ImageView fullImageView;
  private DPObject[] holidaysList;
  private Handler mHandler = new BookingTimePickerFragment.1(this);
  private LinearLayout moreDateLayout;
  private ImageView nightPeriodIcon;
  private TextView nightPeriodText;
  private LinearLayout nightPeriodView;
  private LinearLayout periodInfoLayout;
  private int roomFlag;
  private Calendar selectedDate;
  private Calendar selectedTime;
  private int shopId;
  private LinearLayout timeInfoLayout;
  private ScrollView timeInfoSV;
  private List<BookingTimePickerFragment.TimeItemViewHolder> timeItems = new ArrayList();
  private int timePeriodItemWidth;
  private TextView tipContentView;
  private TextView tipExtraView;
  private TextView tipTitleView;
  private View tipViewContainer;

  private View createDateInfoItemView()
  {
    return LayoutInflater.from(getActivity()).inflate(R.layout.booking_time_picker_date_info_item, null, false);
  }

  private LinearLayout createLayout(int paramInt)
  {
    LinearLayout localLinearLayout = new LinearLayout(getActivity());
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
    localLinearLayout.setOrientation(paramInt);
    return localLinearLayout;
  }

  private View createTimePeriodItemView()
  {
    return LayoutInflater.from(getActivity()).inflate(R.layout.booking_time_picker_time_period_item, null, false);
  }

  private void dateChange(int paramInt1, int paramInt2, int paramInt3)
  {
    this.selectedDate.set(paramInt1, paramInt2, paramInt3);
    int j = 0;
    int i = 0;
    while (i < this.dateItems.size())
    {
      localObject = (BookingTimePickerFragment.DateItemViewHolder)this.dateItems.get(i);
      int k = j;
      if (BookingTimePickerFragment.DateItemViewHolder.access$1000((BookingTimePickerFragment.DateItemViewHolder)localObject) == paramInt3)
      {
        k = j;
        if (BookingTimePickerFragment.DateItemViewHolder.access$900((BookingTimePickerFragment.DateItemViewHolder)localObject) == paramInt2)
        {
          k = j;
          if (BookingTimePickerFragment.DateItemViewHolder.access$800((BookingTimePickerFragment.DateItemViewHolder)localObject) == paramInt1)
            k = i;
        }
      }
      i += 1;
      j = k;
    }
    scrollToDateItem(j);
    Object localObject = BookingUtil.getBookableDate(this.config);
    BookableTime localBookableTime = BookingUtil.getBookableTime(this.config, this.bookingNum, this.roomFlag, this.selectedDate);
    Set localSet1 = BookingUtil.getOneDayRebate(this.config, this.selectedDate);
    Set localSet2 = BookingUtil.getOneDayHot(this.config, this.selectedDate);
    if (!isDateBookable((List)localObject, this.selectedDate))
    {
      this.fullImageView.setVisibility(0);
      this.fullImageView.setImageResource(R.drawable.bookingdetail_timepage_unaccept);
      this.confirmPickingLayout.setVisibility(8);
      this.timeInfoLayout.setVisibility(8);
      this.periodInfoLayout.setVisibility(8);
      return;
    }
    if ((localBookableTime == null) || (localBookableTime.getBookableTimeCount(0, 24) == 0))
    {
      this.fullImageView.setVisibility(0);
      this.fullImageView.setImageResource(R.drawable.bookingdetail_timepage_full);
      this.confirmPickingLayout.setVisibility(8);
      this.timeInfoLayout.setVisibility(8);
      this.periodInfoLayout.setVisibility(8);
      return;
    }
    if ((localBookableTime.getBookableTimeCount(0, 24) > 16) && (localBookableTime.getBookableTimeCount(0, 16) != 0) && (localBookableTime.getBookableTimeCount(16, 24) != 0))
    {
      this.periodInfoLayout.setVisibility(0);
      if ((DateUtils.isSameDay(this.selectedDate, this.selectedTime)) && (this.selectedTime.get(11) >= 16))
      {
        setupTimePeriodView(this.selectedTime, localBookableTime, localSet1, localSet2, 16, 24);
        dayPeriodViewSelected(false);
        return;
      }
      setupTimePeriodView(this.selectedTime, localBookableTime, localSet1, localSet2, 0, 16);
      dayPeriodViewSelected(true);
      return;
    }
    this.periodInfoLayout.setVisibility(8);
    setupTimePeriodView(this.selectedTime, localBookableTime, localSet1, localSet2, 0, 24);
  }

  private void dateChange(Calendar paramCalendar)
  {
    dateChange(paramCalendar.get(1), paramCalendar.get(2), paramCalendar.get(5));
  }

  private void dayPeriodViewSelected(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.dayPeriodIcon.setSelected(true);
      this.dayPeriodText.setTextColor(-39373);
      this.nightPeriodIcon.setSelected(false);
      this.nightPeriodText.setTextColor(-7566196);
      return;
    }
    this.dayPeriodIcon.setSelected(false);
    this.dayPeriodText.setTextColor(-7566196);
    this.nightPeriodIcon.setSelected(true);
    this.nightPeriodText.setTextColor(-10777680);
  }

  private void getCouponInfo(int paramInt, Calendar paramCalendar)
  {
    if (this.fromType != 0)
      return;
    getCouponInfo(getParam(paramInt, paramCalendar));
  }

  private void getCouponInfo(String paramString)
  {
    if (this.couponInfoRequest != null);
    do
      return;
    while (this.couponInfo.contains(paramString));
    this.couponInfoRequest = BasicMApiRequest.mapiGet("http://rs.api.dianping.com/getonedayperiodbookinfo.yy?" + paramString, CacheType.DISABLED);
    mapiService().exec(this.couponInfoRequest, this);
  }

  private String getDateInfo(List<Calendar> paramList, Calendar paramCalendar)
  {
    if (!isDateBookable(paramList, paramCalendar))
    {
      paramList = "不可订";
      return paramList;
    }
    if ((this.holidaysList != null) && (this.holidaysList.length != 0))
    {
      localObject = this.holidaysList;
      int j = localObject.length;
      int i = 0;
      while (true)
      {
        if (i >= j)
          break label100;
        paramList = localObject[i];
        long l = paramList.getTime("Day");
        paramList = paramList.getString("Title");
        localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(l);
        if (DateUtils.isSameDay(localCalendar, paramCalendar))
          break;
        i += 1;
      }
    }
    label100: paramList = Calendar.getInstance();
    Object localObject = Calendar.getInstance();
    ((Calendar)localObject).add(5, 1);
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.add(5, 2);
    if (DateUtils.isSameDay(paramCalendar, paramList))
      return "今天";
    if (DateUtils.isSameDay(paramCalendar, (Calendar)localObject))
      return "明天";
    if (DateUtils.isSameDay(paramCalendar, localCalendar))
      return "后天";
    return (String)DateFormat.format("E", paramCalendar).toString();
  }

  private int getDateInfoItemWidth()
  {
    DisplayMetrics localDisplayMetrics = getActivity().getResources().getDisplayMetrics();
    int i = localDisplayMetrics.widthPixels;
    float f = localDisplayMetrics.density;
    return (int)((i - 71.0F * f) / 3.0F);
  }

  private String getParam(int paramInt, Calendar paramCalendar)
  {
    return "shopid=" + paramInt + "&datestring=" + DateFormat.format("yyyy-MM-dd", paramCalendar).toString();
  }

  private int getTimePeriodItemWidth()
  {
    new DisplayMetrics();
    return getActivity().getResources().getDisplayMetrics().widthPixels / 4;
  }

  private void initDateItem(List<Calendar> paramList1, List<Calendar> paramList2, Calendar paramCalendar)
  {
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(this.dateInfoItemWidth, -1);
    int j = 0;
    int k = paramList1.size();
    int i = 0;
    if (i < k)
    {
      Calendar localCalendar = (Calendar)paramList1.get(i);
      BookingTimePickerFragment.DateItemViewHolder localDateItemViewHolder = new BookingTimePickerFragment.DateItemViewHolder(createDateInfoItemView());
      localDateItemViewHolder.setOnClickListener(new BookingTimePickerFragment.3(this));
      this.dateItems.add(localDateItemViewHolder);
      this.dateInfoItemLayout.addView(BookingTimePickerFragment.DateItemViewHolder.access$1500(localDateItemViewHolder), localLayoutParams);
      localDateItemViewHolder.setDate(localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
      localDateItemViewHolder.setDateInfo(getDateInfo(paramList2, localCalendar));
      if (DateUtils.isSameDay(localCalendar, paramCalendar))
      {
        localDateItemViewHolder.setSelected(true);
        j = i;
      }
      while (true)
      {
        i += 1;
        break;
        localDateItemViewHolder.setSelected(false);
      }
    }
    paramList1 = Message.obtain();
    paramList1.what = 0;
    paramList1.arg1 = j;
    this.mHandler.sendMessageDelayed(paramList1, 500L);
  }

  private boolean isDateBookable(List<Calendar> paramList, Calendar paramCalendar)
  {
    paramList = paramList.iterator();
    while (paramList.hasNext())
      if (DateUtils.isSameDay(paramCalendar, (Calendar)paramList.next()))
        return true;
    return false;
  }

  private void scrollToDateItem(int paramInt)
  {
    if (paramInt == this.dateItems.size() - 1)
      paramInt = this.dateItems.size() - 3;
    while (true)
    {
      this.dateInfoItemHSV.smoothScrollTo(this.dateInfoItemWidth * paramInt, 0);
      return;
      if (paramInt == 0)
      {
        paramInt = 0;
        continue;
      }
      paramInt -= 1;
    }
  }

  private void scrollToTimePeriodItem(int paramInt)
  {
    this.timeInfoSV.smoothScrollTo(0, paramInt / 4 * this.timePeriodItemWidth);
  }

  private void setTipContent(String paramString)
  {
    if (paramString != null)
    {
      this.tipContentView.setText(paramString);
      this.tipContentView.setVisibility(0);
      return;
    }
    this.tipContentView.setVisibility(8);
  }

  private void setTipExtra(String paramString)
  {
    if (paramString != null)
    {
      this.tipExtraView.setText(paramString);
      this.tipExtraView.setVisibility(0);
      return;
    }
    this.tipExtraView.setVisibility(8);
  }

  private void setTipTitle(String paramString)
  {
    if (paramString != null)
    {
      this.tipTitleView.setText(paramString);
      this.tipTitleView.setVisibility(0);
      return;
    }
    this.tipTitleView.setVisibility(8);
  }

  private void setUpViews(View paramView)
  {
    this.dateInfoItemHSV = ((HorizontalScrollView)paramView.findViewById(R.id.date_info_item_hsv));
    this.dateInfoItemHSV.setSmoothScrollingEnabled(true);
    this.dateInfoItemLayout = ((LinearLayout)paramView.findViewById(R.id.date_info_item_layout));
    this.moreDateLayout = ((LinearLayout)paramView.findViewById(R.id.more_date_layout));
    this.periodInfoLayout = ((LinearLayout)paramView.findViewById(R.id.period_info_layout));
    this.dayPeriodView = ((LinearLayout)paramView.findViewById(R.id.day_period_view));
    this.dayPeriodIcon = ((ImageView)paramView.findViewById(R.id.day_period_icon));
    this.dayPeriodText = ((TextView)paramView.findViewById(R.id.day_period_text));
    this.nightPeriodView = ((LinearLayout)paramView.findViewById(R.id.night_period_view));
    this.nightPeriodIcon = ((ImageView)paramView.findViewById(R.id.night_period_icon));
    this.nightPeriodText = ((TextView)paramView.findViewById(R.id.night_period_text));
    this.timeInfoSV = ((ScrollView)paramView.findViewById(R.id.time_info_sv));
    this.timeInfoSV.setSmoothScrollingEnabled(true);
    this.timeInfoLayout = ((LinearLayout)paramView.findViewById(R.id.time_info_layout));
    this.confirmPickingLayout = ((LinearLayout)paramView.findViewById(R.id.confirm_picking_layout));
    this.confirmPickingBtn = ((Button)paramView.findViewById(R.id.confirm_picking_btn));
    this.dateInfoItemWidth = getDateInfoItemWidth();
    this.timePeriodItemWidth = getTimePeriodItemWidth();
    this.fullImageView = ((ImageView)paramView.findViewById(R.id.image_full));
    this.tipViewContainer = LayoutInflater.from(getActivity()).inflate(R.layout.booking_time_picker_time_period_tip, null, false);
    this.tipTitleView = ((TextView)this.tipViewContainer.findViewById(R.id.time_period_tip_title));
    this.tipContentView = ((TextView)this.tipViewContainer.findViewById(R.id.time_period_tip_content));
    this.tipExtraView = ((TextView)this.tipViewContainer.findViewById(R.id.time_period_tip_extra));
    this.tipViewContainer.setVisibility(8);
    this.confirmPickingBtn.setOnClickListener(this);
    this.moreDateLayout.setOnClickListener(this);
    this.dayPeriodView.setOnClickListener(this);
    this.nightPeriodView.setOnClickListener(this);
    setupTimePeriodItem();
    setupDateItem();
    setupCalendarView();
  }

  private void setupCalendarView()
  {
    this.calendarDialog = new Dialog(getActivity(), R.style.dialog);
    this.calendarDialog.setCanceledOnTouchOutside(true);
    this.calendarView = new CalendarView(getActivity());
    this.calendarDialog.setContentView(this.calendarView);
    Calendar localCalendar = Calendar.getInstance();
    this.calendarView.setDate(localCalendar, this.selectedDate, BookingUtil.getBookableDate(this.config), this.config.dayAfter);
    this.calendarView.setHoliday(this.holidaysList);
    this.calendarView.setOnDateChangeListener(new BookingTimePickerFragment.4(this));
  }

  private void setupDateItem()
  {
    int i = 3;
    List localList = BookingUtil.getBookableDate(this.config);
    int m = this.config.dayAfter;
    int j = this.config.dayIn;
    int k = 0;
    ArrayList localArrayList = new ArrayList();
    if (j - m >= 3)
      i = j - m;
    j = m;
    while (j < m + i)
    {
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.add(5, j);
      localArrayList.add(localCalendar);
      if (DateUtils.isSameDay(localCalendar, this.selectedTime))
        k = 1;
      j += 1;
    }
    if (k == 0)
      this.selectedDate.setTimeInMillis(((Calendar)localArrayList.get(0)).getTimeInMillis());
    initDateItem(localArrayList, localList, this.selectedDate);
  }

  private void setupTimePeriodItem()
  {
    int n = this.config.timeInterval;
    int i = 0;
    LinearLayout localLinearLayout = null;
    int j = 0;
    while (j < 24)
    {
      int k = 0;
      while (k < 60)
      {
        View localView = createTimePeriodItemView();
        BookingTimePickerFragment.TimeItemViewHolder localTimeItemViewHolder = new BookingTimePickerFragment.TimeItemViewHolder(localView);
        localTimeItemViewHolder.setOnClickListener(new BookingTimePickerFragment.2(this));
        this.timeItems.add(localTimeItemViewHolder);
        if (i == 0)
          localLinearLayout = createLayout(0);
        localLinearLayout.addView(localView, new LinearLayout.LayoutParams(this.timePeriodItemWidth, this.timePeriodItemWidth));
        int m = i + 1;
        i = m;
        if (m == 4)
        {
          this.timeInfoLayout.addView(localLinearLayout);
          i = 0;
        }
        k += n;
      }
      j += 1;
    }
  }

  private void setupTimePeriodView(Calendar paramCalendar, BookableTime paramBookableTime, Set<String> paramSet1, Set<String> paramSet2, int paramInt1, int paramInt2)
  {
    this.fullImageView.setVisibility(8);
    this.confirmPickingLayout.setVisibility(0);
    this.timeInfoLayout.setVisibility(0);
    this.tipViewContainer.setVisibility(8);
    this.timeInfoLayout.removeView(this.tipViewContainer);
    paramBookableTime = paramBookableTime.getHourMinuteMap();
    int i = 0;
    int m = this.timeItems.size();
    String str1 = DateUtils.formatTime(paramCalendar);
    int j = 0;
    if (j < paramBookableTime.size())
    {
      int n = paramBookableTime.keyAt(j);
      int k = i;
      if (n >= paramInt1)
      {
        if (n < paramInt2)
          break label125;
        k = i;
      }
      label125: Iterator localIterator;
      do
      {
        j += 1;
        i = k;
        break;
        localIterator = ((Set)paramBookableTime.valueAt(j)).iterator();
        k = i;
      }
      while (!localIterator.hasNext());
      Object localObject = (Integer)localIterator.next();
      BookingTimePickerFragment.TimeItemViewHolder localTimeItemViewHolder;
      if (i < m)
      {
        localTimeItemViewHolder = (BookingTimePickerFragment.TimeItemViewHolder)this.timeItems.get(i);
        String str2 = DateUtils.formatTime(n, ((Integer)localObject).intValue());
        localTimeItemViewHolder.setText(DateUtils.formatTime(n, ((Integer)localObject).intValue()));
        localTimeItemViewHolder.setVisible(true);
        localTimeItemViewHolder.setRebate(paramSet1.contains(str2));
        localTimeItemViewHolder.setHot(paramSet2.contains(str2));
        if ((!DateUtils.isSameDay(paramCalendar, this.selectedDate)) || (!str1.equals(str2)))
          break label332;
      }
      label332: for (boolean bool = true; ; bool = false)
      {
        localTimeItemViewHolder.setSelected(bool);
        if (bool)
        {
          localObject = Message.obtain();
          ((Message)localObject).what = 1;
          ((Message)localObject).arg1 = i;
          ((Message)localObject).obj = paramCalendar;
          this.mHandler.sendMessageDelayed((Message)localObject, 500L);
        }
        i += 1;
        break;
      }
    }
    while (i < m)
    {
      ((BookingTimePickerFragment.TimeItemViewHolder)this.timeItems.get(i)).setVisible(false);
      i += 1;
    }
  }

  private void showTipView(Calendar paramCalendar, int paramInt)
  {
    this.timeInfoLayout.removeView(this.tipViewContainer);
    long l = paramCalendar.getTimeInMillis();
    paramCalendar = (DPObject[])this.couponInfo.get(getParam(this.shopId, paramCalendar));
    if (paramCalendar != null)
    {
      int k = paramCalendar.length;
      int i = 0;
      if (i < k)
      {
        Object localObject = paramCalendar[i];
        long[] arrayOfLong = localObject.getTimeArray("TimeList");
        if (arrayOfLong == null);
        while (true)
        {
          i += 1;
          break;
          int m = arrayOfLong.length;
          int j = 0;
          while (j < m)
          {
            if (Math.abs(arrayOfLong[j] - l) <= 60000L)
              showTips(localObject.getString("Title"), localObject.getString("Content"), localObject.getString("Extra"), paramInt);
            j += 1;
          }
        }
      }
    }
  }

  private void showTips(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    setTipTitle(paramString1);
    setTipContent(paramString2);
    setTipExtra(paramString3);
    switch (paramInt % 4)
    {
    default:
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      this.tipViewContainer.setVisibility(0);
      this.timeInfoLayout.addView(this.tipViewContainer, paramInt / 4 + 1);
      return;
      this.tipViewContainer.setBackgroundResource(R.drawable.bookingdetail_timepage_bkg01);
      continue;
      this.tipViewContainer.setBackgroundResource(R.drawable.bookingdetail_timepage_bkg02);
      continue;
      this.tipViewContainer.setBackgroundResource(R.drawable.bookingdetail_timepage_bkg03);
      continue;
      this.tipViewContainer.setBackgroundResource(R.drawable.bookingdetail_timepage_bkg04);
    }
  }

  private void updateCalendarView()
  {
    Calendar localCalendar = Calendar.getInstance();
    this.calendarView.setDate(localCalendar, this.selectedDate, BookingUtil.getBookableDate(this.config), this.config.dayAfter);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.more_date_layout)
    {
      statisticsEvent("booking6", "booking6_time_calendar", "", 0);
      this.calendarDialog.show();
    }
    do
    {
      return;
      if (i == R.id.day_period_view)
      {
        dayPeriodViewSelected(true);
        paramView = BookingUtil.getBookableTime(this.config, this.bookingNum, this.roomFlag, this.selectedDate);
        setupTimePeriodView(this.selectedTime, paramView, BookingUtil.getOneDayRebate(this.config, this.selectedDate), BookingUtil.getOneDayHot(this.config, this.selectedDate), 0, 16);
        return;
      }
      if (i != R.id.night_period_view)
        continue;
      dayPeriodViewSelected(false);
      paramView = BookingUtil.getBookableTime(this.config, this.bookingNum, this.roomFlag, this.selectedDate);
      setupTimePeriodView(this.selectedTime, paramView, BookingUtil.getOneDayRebate(this.config, this.selectedDate), BookingUtil.getOneDayHot(this.config, this.selectedDate), 16, 24);
      return;
    }
    while (i != R.id.confirm_picking_btn);
    statisticsEvent("booking6", "booking6_time_select", "", 0);
    paramView = null;
    if (this.fromType == 0)
      paramView = new Intent("com.dianping.booking.BOOKING_TIME_PICKED");
    while (true)
    {
      paramView.putExtra("selectCal", this.selectedTime.getTimeInMillis());
      getActivity().sendBroadcast(paramView);
      getActivity().getSupportFragmentManager().popBackStack();
      if (this.fromType != 0)
        break;
      ((BookingInfoActivity)getActivity()).gotoBookingInfo();
      paramView = ((NovaActivity)getActivity()).getTitleBar().findRightViewItemByTag("groupon");
      if (paramView == null)
        break;
      paramView.setVisibility(0);
      return;
      if (this.fromType != 1)
        continue;
      paramView = new Intent("com.dianping.booking.GROUPON_BOOKING_TIME_PICKED");
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.booking_time_picker_layout, paramViewGroup, false);
    ((NovaActivity)getActivity()).getTitleBar().setTitle("时间");
    paramViewGroup = getArguments();
    this.fromType = paramViewGroup.getInt("fromType", 0);
    this.shopId = paramViewGroup.getInt("shopId", 0);
    this.bookingNum = paramViewGroup.getInt("bookingNum", 0);
    this.roomFlag = paramViewGroup.getInt("roomFlag", 0);
    this.selectedTime = Calendar.getInstance();
    this.selectedTime.setTimeInMillis(paramViewGroup.getLong("setCalendar", System.currentTimeMillis()));
    this.selectedDate = Calendar.getInstance();
    this.selectedDate.setTimeInMillis(this.selectedTime.getTimeInMillis());
    paramBundle = paramViewGroup.getParcelableArray("holidaysList");
    if ((paramBundle != null) && (paramBundle.length != 0))
    {
      this.holidaysList = new DPObject[paramBundle.length];
      int i = 0;
      while (i < paramBundle.length)
      {
        this.holidaysList[i] = ((DPObject)paramBundle[i]);
        i += 1;
      }
    }
    paramViewGroup = (DPObject)paramViewGroup.getParcelable("bookingConfig");
    if (paramViewGroup == null)
    {
      Toast.makeText(getActivity(), "暂时无法选择日期，请稍后再试", 0).show();
      getActivity().getSupportFragmentManager().popBackStack();
    }
    this.config = new BookingConfig(paramViewGroup);
    setUpViews(paramLayoutInflater);
    getCouponInfo(this.shopId, this.selectedDate);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.couponInfoRequest != null)
    {
      mapiService().abort(this.couponInfoRequest, this, true);
      this.couponInfoRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.couponInfoRequest)
    {
      super.showToast("优惠信息获取失败");
      this.couponInfoRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.couponInfoRequest)
    {
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        paramMApiResponse = (DPObject)paramMApiResponse.result();
        paramMApiRequest = paramMApiRequest.url().substring(paramMApiRequest.url().indexOf("?") + 1);
        this.couponInfo.put(paramMApiRequest, paramMApiResponse.getArray("InfoList"));
      }
      this.couponInfoRequest = null;
    }
  }

  public void onResume()
  {
    super.onResume();
    if (this.fromType == 0)
    {
      View localView = ((NovaActivity)getActivity()).getTitleBar().findRightViewItemByTag("groupon");
      if (localView != null)
        localView.setVisibility(4);
    }
    dateChange(this.selectedDate);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.fragment.BookingTimePickerFragment
 * JD-Core Version:    0.6.0
 */