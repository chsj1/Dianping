package com.dianping.hotel.commons.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.CalendarItem;
import com.dianping.base.widget.CalendarItem.OnItemClickListener;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.hotel.commons.widget.HotelCalendarView;
import com.dianping.hotel.commons.widget.HotelCalendarView.OnDateChangeListener;
import com.dianping.model.UserProfile;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HotelBookingPickTimeActivity extends NovaActivity
  implements HotelCalendarView.OnDateChangeListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int DEALBOOKING = 2;
  private static final int DEALTRAVE = 1;
  private static final int LOADVIEW = 1;
  private static final int NORMAL = 0;
  private static final int TIPS = 0;
  private ArrayList<CalendarItem> allDays = new ArrayList();
  private Map<String, DPObject> bookingStatusMap = new HashMap();
  private ArrayList<Calendar> calendarArrayList = new ArrayList();
  private Calendar checkinCalendar;
  private long checkinTimeCommited;
  private Calendar checkoutCalendar;
  private long checkoutTimeCommited;
  private LinearLayout contentView;
  private Calendar deadlineCalendar;
  private MApiRequest dealBookingReq;
  private DPObject[] festivals;
  private MApiRequest festivalsReq;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      super.handleMessage(paramMessage);
      if (paramMessage.what == 0)
        HotelBookingPickTimeActivity.this.hideTips();
      do
      {
        return;
        if (paramMessage.what != 1)
          continue;
        int i = paramMessage.getData().getInt("index");
        HotelBookingPickTimeActivity.this.addCalendarViewAsync(i, i + 1);
        return;
      }
      while (paramMessage.what != 2);
      HotelBookingPickTimeActivity.access$202(HotelBookingPickTimeActivity.this, false);
      HotelBookingPickTimeActivity.access$302(HotelBookingPickTimeActivity.this, false);
      HotelBookingPickTimeActivity.this.cleanLastSelectedDays();
      HotelBookingPickTimeActivity.this.titleButton.setEnabled(true);
      HotelBookingPickTimeActivity.access$602(HotelBookingPickTimeActivity.this, -1);
      HotelBookingPickTimeActivity.access$602(HotelBookingPickTimeActivity.this, -1);
      HotelBookingPickTimeActivity.access$702(HotelBookingPickTimeActivity.this, -1L);
      HotelBookingPickTimeActivity.access$802(HotelBookingPickTimeActivity.this, -1L);
    }
  };
  private DPObject[] hotelDetailArray;
  private int index_checkin = -1;
  private int index_checkout = -1;
  private boolean isCheckinTimeSetted;
  private boolean isCheckoutTimeSetted;
  private boolean isFirstLodad = true;
  private boolean isReseted;
  private boolean isTipsAnimationEnd = true;
  private int itemWidth;
  private ArrayList<CalendarItem> last_days = new ArrayList();
  private String pageFrom;
  private DPObject[] restDays;
  private FrameLayout tipView;
  private Calendar todayCalendar;
  private TextView tv_tip;
  private int type = 0;

  private void addCalendarViewAsync(int paramInt1, int paramInt2)
  {
    if (paramInt2 < this.calendarArrayList.size());
    for (int i = paramInt2; ; i = this.calendarArrayList.size())
    {
      if (paramInt1 >= i)
        return;
      Calendar localCalendar = (Calendar)this.calendarArrayList.get(paramInt1);
      Object localObject = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)localObject);
      this.itemWidth = (((DisplayMetrics)localObject).widthPixels / 7);
      localObject = new HotelBookingCalendarView(this, this.itemWidth);
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
      localLayoutParams.leftMargin = ViewUtils.dip2px(this, 20.0F);
      ((HotelBookingCalendarView)localObject).setLayoutParams(localLayoutParams);
      ((HotelBookingCalendarView)localObject).setGravity(1);
      ((HotelBookingCalendarView)localObject).setOnDateChangeListener(this);
      ((HotelBookingCalendarView)localObject).setHoliday(this.festivals);
      if (!((HotelBookingCalendarView)localObject).setDate(this.todayCalendar, localCalendar))
        this.contentView.addView((View)localObject);
      paramInt1 += 1;
      break;
    }
  }

  private boolean checkIsLocked()
  {
    if (accountService().token() == null);
    do
      return false;
    while (!getAccount().grouponIsLocked());
    new AlertDialog.Builder(this).setTitle("提示").setMessage("您的账户存在异常已被锁定，请联系客服为您解除锁定。").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if ((HotelBookingPickTimeActivity.this instanceof Activity))
          HotelBookingPickTimeActivity.this.finish();
      }
    }).setCancelable(false).show();
    return true;
  }

  private void cleanLastSelectedDays()
  {
    Iterator localIterator = this.last_days.iterator();
    Object localObject1;
    while (true)
    {
      if (localIterator.hasNext())
      {
        localObject1 = (CalendarItem)localIterator.next();
        if (localObject1 != null);
      }
      else
      {
        this.last_days.clear();
        return;
      }
      if (this.type == 1)
      {
        ((CalendarItem)localObject1).getDateTextView().setTextColor(getResources().getColor(R.color.hotel_datepicker_color));
        ((CalendarItem)localObject1).getDateSubTextView().setTextColor(getResources().getColor(R.color.hotel_roomlist_price_color));
        ((TextView)((CalendarItem)localObject1).getView().findViewById(R.id.date_subinfo_2)).setTextColor(getResources().getColor(R.color.hotel_roomlist_price_color));
        ((CalendarItem)localObject1).getView().findViewById(R.id.info).setBackgroundColor(-1);
        continue;
      }
      if (((CalendarItem)localObject1).isbIsActiveMonth())
        break;
      ((CalendarItem)localObject1).getView().findViewById(R.id.info).setBackgroundColor(-1);
      ((CalendarItem)localObject1).getView().findViewById(R.id.empty_left).setBackgroundColor(-1);
      ((CalendarItem)localObject1).getView().findViewById(R.id.empty_right).setBackgroundColor(-1);
    }
    Object localObject3 = ((CalendarItem)localObject1).getDateTextView();
    ViewUtils.px2dip(this, this.itemWidth);
    ((TextView)localObject3).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
    ((TextView)localObject3).setText(String.valueOf(((CalendarItem)localObject1).getiDateDay()));
    TextView localTextView = ((CalendarItem)localObject1).getDateSubTextView();
    localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_13));
    localTextView.setText("");
    Object localObject2 = Calendar.getInstance();
    int j = 0;
    int i = j;
    if (((Calendar)localObject2).get(1) == ((CalendarItem)localObject1).getiDateYear())
    {
      i = j;
      if (((Calendar)localObject2).get(2) == ((CalendarItem)localObject1).getiDateMonth())
      {
        i = j;
        if (((Calendar)localObject2).get(5) == ((CalendarItem)localObject1).getiDateDay())
          i = 1;
      }
    }
    localObject2 = ((CalendarItem)localObject1).getView();
    ((View)localObject2).setSelected(false);
    ((CalendarItem)localObject1).setbSelected(false);
    if (((CalendarItem)localObject1).isEnable())
    {
      ((TextView)localObject3).setTextColor(-16777216);
      localTextView.setTextColor(-16777216);
    }
    while (true)
    {
      if (i != 0)
      {
        ((TextView)localObject3).setText("今天");
        if (this.type == 2)
        {
          i = -16777216;
          ((TextView)localObject3).setTextColor(i);
        }
      }
      else
      {
        if ((this.festivals == null) || (this.festivals.length == 0))
          break label564;
        i = 0;
        label401: if (i >= this.festivals.length)
          break label564;
      }
      try
      {
        Object localObject4 = this.festivals[i].getString("Date");
        String str = this.festivals[i].getString("Name");
        Calendar localCalendar = Calendar.getInstance();
        localObject4 = ((String)localObject4).split("-");
        localCalendar.set(1, Integer.parseInt(localObject4[0]));
        localCalendar.set(2, Integer.parseInt(localObject4[1]) - 1);
        localCalendar.set(5, Integer.parseInt(localObject4[2]));
        if (HotelCalendarView.compareTwoDays(localCalendar, ((CalendarItem)localObject1).getDate()) == 0)
          ((TextView)localObject3).setText(str);
        i += 1;
        break label401;
        ((TextView)localObject3).setTextColor(-7829368);
        localTextView.setTextColor(-7829368);
        continue;
        i = -65536;
      }
      catch (Exception localException)
      {
        while (true)
          Log.e(localException.toString());
      }
    }
    label564: int k;
    if (this.type == 2)
    {
      i = ((CalendarItem)localObject1).getiDateYear();
      j = ((CalendarItem)localObject1).getiDateMonth() + 1;
      k = ((CalendarItem)localObject1).getiDateDay();
      localObject3 = new StringBuilder().append(i).append("-");
      if (j >= 10)
        break label804;
      localObject1 = "0" + j;
      label641: localObject3 = ((StringBuilder)localObject3).append(localObject1).append("-");
      if (k >= 10)
        break label813;
      localObject1 = "0" + k;
      label684: localObject1 = localObject1;
      if (this.bookingStatusMap.containsKey(localObject1))
      {
        i = ((DPObject)this.bookingStatusMap.get(localObject1)).getInt("Status");
        localObject1 = "";
        switch (i)
        {
        default:
        case 0:
        case 1:
        case 2:
        }
      }
    }
    while (true)
    {
      localTextView.setText((CharSequence)localObject1);
      ((View)localObject2).findViewById(R.id.info).setBackgroundColor(-1);
      ((View)localObject2).findViewById(R.id.empty_left).setBackgroundColor(-1);
      ((View)localObject2).findViewById(R.id.empty_right).setBackgroundColor(-1);
      break;
      label804: localObject1 = Integer.valueOf(j);
      break label641;
      label813: localObject1 = Integer.valueOf(k);
      break label684;
      localObject1 = "满房";
      localTextView.setTextColor(getResources().getColor(R.color.hotel_light_gray));
      continue;
      localObject1 = "有房";
      continue;
      localObject1 = "满房";
    }
  }

  private void hideTips()
  {
    TranslateAnimation localTranslateAnimation = new TranslateAnimation(0.0F, 0.0F, 0.0F, -120.0F);
    localTranslateAnimation.setDuration(500L);
    localTranslateAnimation.setAnimationListener(new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
        HotelBookingPickTimeActivity.this.tipView.clearAnimation();
        paramAnimation = (FrameLayout.LayoutParams)HotelBookingPickTimeActivity.this.tipView.getLayoutParams();
        paramAnimation.topMargin = -120;
        HotelBookingPickTimeActivity.this.tipView.setLayoutParams(paramAnimation);
        HotelBookingPickTimeActivity.access$902(HotelBookingPickTimeActivity.this, true);
      }

      public void onAnimationRepeat(Animation paramAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnimation)
      {
      }
    });
    this.tipView.startAnimation(localTranslateAnimation);
  }

  private void initCalendarView()
  {
    this.contentView = ((LinearLayout)findViewById(R.id.content));
    this.todayCalendar = Calendar.getInstance();
    this.deadlineCalendar = Calendar.getInstance();
    Calendar localCalendar1 = this.deadlineCalendar;
    int i;
    if (this.type == 2)
    {
      i = 30;
      localCalendar1.add(6, i);
      this.calendarArrayList.clear();
      this.calendarArrayList.add(this.todayCalendar);
      localCalendar1 = Calendar.getInstance();
      label74: localCalendar1.set(2, localCalendar1.get(2) + 1);
      localCalendar1.set(5, 1);
      Calendar localCalendar2 = Calendar.getInstance();
      localCalendar2.setTimeInMillis(localCalendar1.getTimeInMillis());
      this.calendarArrayList.add(localCalendar2);
      if ((localCalendar2.get(1) != this.deadlineCalendar.get(1)) || (localCalendar2.get(2) != this.deadlineCalendar.get(2)))
        break label175;
    }
    while (true)
    {
      this.contentView.removeAllViews();
      new AsyncTask()
      {
        protected Integer doInBackground(Void[] paramArrayOfVoid)
        {
          int i = 0;
          while (true)
            if (i < HotelBookingPickTimeActivity.this.calendarArrayList.size())
            {
              paramArrayOfVoid = new Message();
              paramArrayOfVoid.what = 1;
              Bundle localBundle = new Bundle();
              localBundle.putInt("index", i);
              paramArrayOfVoid.setData(localBundle);
              HotelBookingPickTimeActivity.this.handler.sendMessage(paramArrayOfVoid);
              try
              {
                Thread.sleep(100L);
                i += 1;
              }
              catch (InterruptedException paramArrayOfVoid)
              {
                while (true)
                  paramArrayOfVoid.printStackTrace();
              }
            }
          if (((HotelBookingPickTimeActivity.this.restDays == null) || (HotelBookingPickTimeActivity.this.restDays.length <= 0) || (HotelBookingPickTimeActivity.this.festivals == null) || (HotelBookingPickTimeActivity.this.festivals.length <= 0)) && (HotelBookingPickTimeActivity.this.type != 2))
            HotelBookingPickTimeActivity.this.sendFestivalsReq();
          if (HotelBookingPickTimeActivity.this.type == 2);
          return null;
        }
      }
      .execute(new Void[0]);
      return;
      i = 90;
      break;
      label175: if (localCalendar1.compareTo(this.deadlineCalendar) < 0)
        break label74;
    }
  }

  private void refreshTitle()
  {
    int i;
    if (this.isCheckoutTimeSetted)
    {
      i = (int)((this.checkoutCalendar.getTimeInMillis() - this.checkinCalendar.getTimeInMillis()) / 86400000L);
      super.setTitle(getTitle() + "" + i + "晚");
      return;
    }
    if (this.isCheckinTimeSetted)
    {
      i = this.checkinCalendar.get(2);
      int j = this.checkinCalendar.get(5);
      super.setTitle(i + 1 + "月" + j + "日入住");
      return;
    }
    super.setTitle("选择时间");
  }

  private void savaData()
  {
    getSharedPreferences("hotel_booking", 0).edit().putLong("checkin_time", this.checkinTimeCommited).putLong("checkout_time", this.checkoutTimeCommited).commit();
  }

  private void sendDealBookingReq()
  {
    if (this.dealBookingReq != null)
      mapiService().abort(this.dealBookingReq, this, true);
    this.dealBookingReq = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/hoteltg/hoteldealbookingcalendar.hoteltg").buildUpon().appendQueryParameter("orderid", getIntParam("orderid") + "").appendQueryParameter("dealgroupid", getIntParam("dealgroupid") + "").appendQueryParameter("dealid", getIntParam("dealid") + "").toString(), CacheType.DISABLED);
    mapiService().exec(this.dealBookingReq, this);
  }

  private void sendFestivalsReq()
  {
    if (this.festivalsReq != null)
      mapiService().abort(this.festivalsReq, this, true);
    Object localObject2 = Calendar.getInstance();
    ((Calendar)localObject2).set(5, 1);
    Object localObject1 = Calendar.getInstance();
    ((Calendar)localObject1).add(2, 4);
    ((Calendar)localObject1).set(5, 1);
    ((Calendar)localObject1).add(6, -1);
    localObject2 = ((Calendar)localObject2).get(1) + "-" + ((Calendar)localObject2).get(2) + "-" + ((Calendar)localObject2).get(5);
    localObject1 = ((Calendar)localObject1).get(1) + "-" + (((Calendar)localObject1).get(2) + 1) + "-" + ((Calendar)localObject1).get(5);
    this.festivalsReq = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/hotel/getfestivals.hotel").buildUpon().appendQueryParameter("startdate", (String)localObject2).appendQueryParameter("enddate", (String)localObject1).toString(), CacheType.DAILY);
    mapiService().exec(this.festivalsReq, this);
  }

  private void showTips(String paramString)
  {
    this.tv_tip.setText(paramString);
    this.isTipsAnimationEnd = false;
    paramString = new TranslateAnimation(0.0F, 0.0F, 0.0F, 120.0F);
    paramString.setDuration(500L);
    paramString.setAnimationListener(new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
        HotelBookingPickTimeActivity.this.tipView.clearAnimation();
        paramAnimation = (FrameLayout.LayoutParams)HotelBookingPickTimeActivity.this.tipView.getLayoutParams();
        paramAnimation.topMargin = 0;
        HotelBookingPickTimeActivity.this.tipView.setLayoutParams(paramAnimation);
        new AsyncTask()
        {
          protected Integer doInBackground(Void[] paramArrayOfVoid)
          {
            try
            {
              Thread.sleep(1000L);
              HotelBookingPickTimeActivity.this.handler.sendEmptyMessage(0);
              return null;
            }
            catch (InterruptedException paramArrayOfVoid)
            {
              while (true)
                paramArrayOfVoid.printStackTrace();
            }
          }
        }
        .execute(new Void[0]);
      }

      public void onAnimationRepeat(Animation paramAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnimation)
      {
      }
    });
    this.tipView.startAnimation(paramString);
  }

  private void updateFestivalsOrDealBookingStatus()
  {
    Iterator localIterator = this.allDays.iterator();
    if (localIterator.hasNext())
    {
      CalendarItem localCalendarItem = (CalendarItem)localIterator.next();
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.set(1, localCalendarItem.getiDateYear());
      localCalendar.set(2, localCalendarItem.getiDateMonth());
      localCalendar.set(5, localCalendarItem.getiDateDay());
      int j;
      int k;
      StringBuilder localStringBuilder;
      if (this.type == 2)
      {
        int i = localCalendarItem.getiDateYear();
        j = localCalendarItem.getiDateMonth() + 1;
        k = localCalendarItem.getiDateDay();
        localStringBuilder = new StringBuilder().append(i).append("-");
        if (j >= 10)
          break label221;
        localObject = "0" + j;
        label139: localStringBuilder = localStringBuilder.append(localObject).append("-");
        if (k >= 10)
          break label230;
      }
      label221: label230: for (Object localObject = "0" + k; ; localObject = Integer.valueOf(k))
      {
        localObject = localObject;
        if (this.bookingStatusMap.containsKey(localObject))
          localCalendarItem.setEnable(true);
        updateDay(localCalendar, localCalendarItem, false);
        break;
        localObject = Integer.valueOf(j);
        break label139;
      }
    }
  }

  public void finish()
  {
    savaData();
    Intent localIntent = new Intent();
    localIntent.putExtra("checkin_time", this.checkinTimeCommited);
    localIntent.putExtra("checkout_time", this.checkoutTimeCommited);
    setResult(-1, localIntent);
    if (TextUtils.isEmpty(this.pageFrom))
      statisticsEvent("reserve5", "reserve5_back", "", 0);
    super.finish();
  }

  public void onBackPressed()
  {
    getSupportFragmentManager().popBackStack();
    super.onBackPressed();
    statisticsEvent("tuan5", "tuan5_reserve5_back", "", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.pageFrom = getIntent().getStringExtra("pagefrom");
    int i = getIntParam("channel");
    if (i == 4)
    {
      this.type = 1;
      super.setContentView(R.layout.activity_hotel_booking_pick_time);
      if ((this.type != 0) && (this.type != 2))
        break label429;
      setTitleButton("确定", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (!HotelBookingPickTimeActivity.this.isCheckinTimeSetted)
            if (HotelBookingPickTimeActivity.this.isTipsAnimationEnd)
              HotelBookingPickTimeActivity.this.showTips("请选择入住时间");
          while (true)
          {
            return;
            if (HotelBookingPickTimeActivity.this.isCheckoutTimeSetted)
              break;
            if (!HotelBookingPickTimeActivity.this.isTipsAnimationEnd)
              continue;
            HotelBookingPickTimeActivity.this.showTips("请选择退房时间");
            return;
          }
          HotelBookingPickTimeActivity.access$702(HotelBookingPickTimeActivity.this, HotelBookingPickTimeActivity.this.checkinCalendar.getTimeInMillis());
          HotelBookingPickTimeActivity.access$802(HotelBookingPickTimeActivity.this, HotelBookingPickTimeActivity.this.checkoutCalendar.getTimeInMillis());
          if (HotelBookingPickTimeActivity.this.type == 2)
          {
            paramView = new Intent();
            paramView.putExtra("checkinTime", HotelBookingPickTimeActivity.this.checkinTimeCommited);
            paramView.putExtra("checkoutTime", HotelBookingPickTimeActivity.this.checkoutTimeCommited);
            HotelBookingPickTimeActivity.this.setResult(-1, paramView);
            HotelBookingPickTimeActivity.this.finish();
            return;
          }
          if (HotelBookingPickTimeActivity.this.hotelDetailArray == null)
          {
            paramView = new Intent();
            paramView.putExtra("checkin_time", HotelBookingPickTimeActivity.this.checkinCalendar.getTimeInMillis());
            paramView.putExtra("checkout_time", HotelBookingPickTimeActivity.this.checkoutCalendar.getTimeInMillis());
            paramView.setAction("com.dianping.action.HOTEL_BOOKING_TIME_CHANGE");
            HotelBookingPickTimeActivity.this.sendBroadcast(paramView);
            HotelBookingPickTimeActivity.this.finish();
            if (!TextUtils.isEmpty(HotelBookingPickTimeActivity.this.pageFrom))
              break label370;
            HotelBookingPickTimeActivity.this.statisticsEvent("hoteldate5", "hoteldate5_success", "" + (HotelBookingPickTimeActivity.this.checkoutCalendar.get(6) - HotelBookingPickTimeActivity.this.checkinCalendar.get(6)), 0);
          }
          while (true)
          {
            HotelBookingPickTimeActivity.this.finish();
            return;
            paramView = new Intent();
            paramView.putExtra("checkin_time", HotelBookingPickTimeActivity.this.checkinCalendar.getTimeInMillis());
            paramView.putExtra("checkout_time", HotelBookingPickTimeActivity.this.checkoutCalendar.getTimeInMillis());
            paramView.setAction("com.dianping.action.HOTEL_BOOKING_TIME_CHANGE");
            HotelBookingPickTimeActivity.this.sendBroadcast(paramView);
            break;
            label370: if (!"tuan5".equalsIgnoreCase(HotelBookingPickTimeActivity.this.pageFrom))
              continue;
            HotelBookingPickTimeActivity.this.statisticsEvent("tuan5", "tuan5_hoteldate_done", "" + (HotelBookingPickTimeActivity.this.checkoutCalendar.get(6) - HotelBookingPickTimeActivity.this.checkinCalendar.get(6)), 0);
          }
        }
      });
      label74: this.checkinCalendar = Calendar.getInstance();
      this.checkoutCalendar = Calendar.getInstance();
      if ((this.type == 0) || (this.type == 2))
      {
        this.isCheckinTimeSetted = true;
        this.isCheckoutTimeSetted = true;
      }
      if (paramBundle != null)
        break label494;
      if (getIntent().getParcelableArrayListExtra("hotelDetailArray") != null)
        this.hotelDetailArray = ((DPObject[])getIntent().getParcelableArrayListExtra("hotelDetailArray").toArray(new DPObject[0]));
      if (TextUtils.isEmpty(getIntent().getData().getQueryParameter("checkin_time")))
        break label482;
      this.checkinCalendar.setTimeInMillis(Long.valueOf(getIntent().getData().getQueryParameter("checkin_time")).longValue());
      this.checkoutCalendar.setTimeInMillis(Long.valueOf(getIntent().getData().getQueryParameter("checkout_time")).longValue());
      this.isCheckinTimeSetted = true;
      this.isCheckoutTimeSetted = true;
      label235: this.checkinTimeCommited = this.checkinCalendar.getTimeInMillis();
      this.checkoutTimeCommited = this.checkoutCalendar.getTimeInMillis();
      if (this.type == 2)
        break label578;
      initCalendarView();
    }
    while (true)
    {
      this.tipView = new FrameLayout(this);
      this.tipView.setBackgroundColor(-1);
      paramBundle = new FrameLayout.LayoutParams(-1, 120);
      paramBundle.gravity = 48;
      paramBundle.topMargin = -120;
      this.tipView.setBackgroundResource(R.drawable.hotel_date_tips);
      this.tv_tip = new TextView(this);
      FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, -2);
      localLayoutParams.gravity = 17;
      this.tv_tip.setLayoutParams(localLayoutParams);
      this.tv_tip.setText("请选择退房日期");
      this.tv_tip.setGravity(17);
      this.tv_tip.setTextColor(getResources().getColor(R.color.hotel_roomlist_price_color));
      this.tipView.addView(this.tv_tip);
      addContentView(this.tipView, paramBundle);
      return;
      if (i != 5)
        break;
      this.type = 2;
      break;
      label429: if (this.type != 1)
        break label74;
      findViewById(R.id.confirm).setVisibility(0);
      findViewById(R.id.confirm).findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HotelBookingPickTimeActivity.this.statisticsEvent("tuan5", "tuan5_calendar_success", "", 0);
          if (HotelBookingPickTimeActivity.this.index_checkin == -1)
            if (HotelBookingPickTimeActivity.this.isTipsAnimationEnd)
              HotelBookingPickTimeActivity.this.showTips("请选择出发日期");
          label290: label301: label310: label702: 
          while (true)
          {
            return;
            Object localObject1 = HotelBookingPickTimeActivity.this.getObjectParam("dpDeal");
            label68: int i;
            Object localObject2;
            int k;
            if (localObject1 == null)
            {
              paramView = null;
              if (paramView != null)
                break label290;
              j = 0;
              int m = 0;
              i = m;
              if (paramView != null)
              {
                i = m;
                if (paramView.getArray("DayDealList") != null)
                {
                  localObject2 = paramView.getArray("DayDealList");
                  int n = localObject2.length;
                  k = 0;
                  label106: i = m;
                  if (k < n)
                  {
                    Object localObject3 = localObject2[k];
                    long l = localObject3.getTime("Date");
                    Calendar localCalendar = Calendar.getInstance();
                    localCalendar.setTimeInMillis(l);
                    if (HotelCalendarView.compareTwoDays(localCalendar, HotelBookingPickTimeActivity.this.checkinCalendar) != 0)
                      break label301;
                    i = localObject3.getInt("DealId");
                  }
                }
              }
              if (j != 1)
                break label360;
              if (paramView != null)
                break label310;
              paramView = "";
            }
            while (true)
            {
              localObject1 = new SimpleDateFormat("yyyy-MM-dd").format(HotelBookingPickTimeActivity.this.checkinCalendar.getTime());
              paramView = paramView + "?dealgroupid=" + i + "&orderdate=" + (String)localObject1;
              paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelbookingweb").buildUpon().appendQueryParameter("url", Uri.parse(paramView).toString()).build());
              HotelBookingPickTimeActivity.this.startActivity(paramView);
              return;
              paramView = ((DPObject)localObject1).getObject("Calendar");
              break;
              j = paramView.getInt("OrderSwitcher");
              break label68;
              k += 1;
              break label106;
              if (paramView.getObject("OrderUrl") == null)
              {
                paramView = "";
                continue;
              }
              if (paramView.getObject("OrderUrl").getString("Url") == null)
              {
                paramView = "";
                continue;
              }
              paramView = paramView.getObject("OrderUrl").getString("Url");
            }
            label360: if ((j != 0) || (localObject1 == null))
              continue;
            if ((ConfigHelper.dynamicLogin) && (((DPObject)localObject1).getInt("DealType") != 2));
            for (int j = 1; (!HotelBookingPickTimeActivity.this.isLogined()) && (j == 0); j = 0)
            {
              HotelBookingPickTimeActivity.this.accountService().login(HotelBookingPickTimeActivity.this);
              return;
            }
            if (!HotelBookingPickTimeActivity.this.checkIsLocked())
            {
              if ((((DPObject)localObject1).getArray("DealSelectList") == null) || (((DPObject)localObject1).getArray("DealSelectList").length <= 1))
                break label632;
              localObject2 = ((DPObject)localObject1).getArray("DealSelectList");
              k = localObject2.length;
              j = 0;
              if (j < k)
              {
                paramView = localObject2[j];
                if (i != paramView.getInt("ID"))
                  break label623;
                localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
                ((Intent)localObject2).putExtra("dealSelect", paramView);
                ((Intent)localObject2).putExtra("deal", (Parcelable)localObject1);
                HotelBookingPickTimeActivity.this.startActivity((Intent)localObject2);
              }
            }
            if (((DPObject)localObject1).getInt("DealType") == 3)
              DPApplication.instance().statisticsEvent("tuan5", "tuan5_detail_lotterybuy", "" + ((DPObject)localObject1).getInt("ID"), 0);
            while (true)
            {
              if (((DPObject)localObject1).getInt("dealchannel") != 1)
                break label702;
              DPApplication.instance().statisticsEvent("tuan5", "hotel_tuan5_detail_buy", ((DPObject)localObject1).getInt("ID") + "", 2);
              return;
              label623: j += 1;
              break;
              label632: paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
              paramView.putExtra("order", (Parcelable)localObject1);
              HotelBookingPickTimeActivity.this.startActivity(paramView);
              break label532;
              DPApplication.instance().statisticsEvent("tuan5", "tuan5_detail_buy", "" + ((DPObject)localObject1).getInt("ID"), 0);
            }
          }
        }
      });
      super.setTitle("可选出发日期");
      break label74;
      label482: this.checkoutCalendar.add(5, 1);
      break label235;
      label494: this.isCheckinTimeSetted = paramBundle.getBoolean("isCheckinTimeSetted");
      this.isCheckoutTimeSetted = paramBundle.getBoolean("isCheckoutTimeSettedByUser");
      this.checkinCalendar.setTimeInMillis(paramBundle.getLong("checkin_time"));
      this.checkoutCalendar.setTimeInMillis(paramBundle.getLong("checkout_time"));
      if (paramBundle.getParcelableArrayList("hotelDetailArray") == null)
        break label235;
      this.hotelDetailArray = ((DPObject[])paramBundle.getParcelableArrayList("hotelDetailArray").toArray(new DPObject[0]));
      break label235;
      label578: sendDealBookingReq();
    }
  }

  public void onDateChange(Calendar paramCalendar, int paramInt)
  {
    if (this.type == 1)
    {
      cleanLastSelectedDays();
      if (HotelCalendarView.compareTwoDays(paramCalendar, this.checkinCalendar) == 0)
      {
        this.index_checkin = -1;
        this.checkinCalendar.setTimeInMillis(-1L);
        statisticsEvent("tuan5", "tuan5_calendar_click", "退房", 0);
        return;
      }
      statisticsEvent("tuan5", "tuan5_calendar_click", "入住", 0);
      this.checkinCalendar.setTimeInMillis(paramCalendar.getTimeInMillis());
      this.index_checkin = paramInt;
      return;
    }
    if (!this.isCheckinTimeSetted)
    {
      this.checkinCalendar.setTimeInMillis(paramCalendar.getTimeInMillis());
      this.checkoutCalendar.setTimeInMillis(paramCalendar.getTimeInMillis());
      this.isCheckinTimeSetted = true;
      this.isReseted = false;
      this.index_checkin = paramInt;
      this.index_checkout = -1;
      if (TextUtils.isEmpty(this.pageFrom))
        statisticsEvent("hoteldate5", "hoteldate5_day", "入住", 0);
      cleanLastSelectedDays();
    }
    while (true)
    {
      refreshTitle();
      return;
      if (!this.isCheckoutTimeSetted)
      {
        if (paramCalendar.compareTo(this.checkinCalendar) < 0)
        {
          this.checkinCalendar.setTimeInMillis(paramCalendar.getTimeInMillis());
          this.isReseted = false;
          cleanLastSelectedDays();
          this.index_checkin = paramInt;
          this.index_checkout = -1;
          if (!TextUtils.isEmpty(this.pageFrom))
            continue;
          statisticsEvent("hoteldate5", "hoteldate5_day", "入住", 0);
          continue;
        }
        if (paramCalendar.compareTo(this.checkinCalendar) > 0)
        {
          this.checkoutCalendar.setTimeInMillis(paramCalendar.getTimeInMillis());
          this.isCheckoutTimeSetted = true;
          this.isReseted = false;
          this.index_checkout = paramInt;
          if ((this.checkinCalendar.get(2) == this.checkoutCalendar.get(2)) || (!TextUtils.isEmpty(this.pageFrom)))
            continue;
          statisticsEvent("hoteldate5", "hoteldate5_day", "退房", 0);
          continue;
        }
        this.isReseted = true;
        this.isCheckinTimeSetted = false;
        this.isCheckoutTimeSetted = false;
        cleanLastSelectedDays();
        this.index_checkout = -1;
        this.index_checkin = -1;
        continue;
      }
      this.checkinCalendar.setTimeInMillis(paramCalendar.getTimeInMillis());
      this.checkoutCalendar.setTimeInMillis(paramCalendar.getTimeInMillis());
      this.isCheckinTimeSetted = true;
      this.isCheckoutTimeSetted = false;
      this.isReseted = false;
      cleanLastSelectedDays();
      this.index_checkin = paramInt;
      this.index_checkout = -1;
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.festivalsReq != null)
    {
      mapiService().abort(this.festivalsReq, this, true);
      this.festivalsReq = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.festivalsReq)
      this.festivalsReq = null;
    do
      return;
    while (paramMApiRequest != this.dealBookingReq);
    this.dealBookingReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    label123: if (paramMApiRequest == this.festivalsReq)
    {
      this.festivalsReq = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        this.festivals = paramMApiRequest.getArray("Festivals");
        this.restDays = paramMApiRequest.getArray("RestDays");
        updateFestivalsOrDealBookingStatus();
      }
    }
    while (true)
    {
      return;
      if (paramMApiRequest != this.dealBookingReq)
        continue;
      this.dealBookingReq = null;
      DPObject localDPObject = (DPObject)paramMApiResponse.result();
      DPObject[] arrayOfDPObject = localDPObject.getArray("BookingStatusList");
      if ((arrayOfDPObject == null) || (arrayOfDPObject.length < 1))
        break;
      paramMApiRequest = "";
      int j = 0;
      int m = arrayOfDPObject.length;
      int i = 0;
      String str1;
      String str2;
      int k;
      if (i < m)
      {
        paramMApiResponse = arrayOfDPObject[i];
        str1 = paramMApiResponse.getString("Date");
        this.bookingStatusMap.put(str1, paramMApiResponse);
        str2 = str1.replace("-", "");
        k = j;
        paramMApiResponse = paramMApiRequest;
      }
      try
      {
        if (Integer.parseInt(str2) > j)
        {
          paramMApiResponse = str1;
          paramMApiRequest = paramMApiResponse;
          k = Integer.parseInt(str2);
        }
        i += 1;
        j = k;
        paramMApiRequest = paramMApiResponse;
        break label123;
        if (!paramMApiRequest.equals(""))
        {
          paramMApiResponse = ((DPObject)this.bookingStatusMap.get(paramMApiRequest)).edit().putInt("Status", 3).generate();
          this.bookingStatusMap.put(paramMApiRequest, paramMApiResponse);
        }
        initCalendarView();
        paramMApiRequest = localDPObject.getString("Tips");
        if (TextUtils.isEmpty(paramMApiRequest))
          continue;
        paramMApiResponse = (TextView)findViewById(R.id.tips);
        paramMApiResponse.setVisibility(0);
        paramMApiResponse.setText(paramMApiRequest);
        return;
      }
      catch (Exception paramMApiResponse)
      {
        while (true)
        {
          k = j;
          paramMApiResponse = paramMApiRequest;
        }
      }
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("isCheckinTimeSetted", this.isCheckinTimeSetted);
    paramBundle.putBoolean("isCheckoutTimeSettedByUser", this.isCheckoutTimeSetted);
    paramBundle.putLong("checkin_time", this.checkinCalendar.getTimeInMillis());
    paramBundle.putLong("checkout_time", this.checkoutCalendar.getTimeInMillis());
    if (this.hotelDetailArray != null)
    {
      ArrayList localArrayList = new ArrayList();
      DPObject[] arrayOfDPObject = this.hotelDetailArray;
      int j = arrayOfDPObject.length;
      int i = 0;
      while (i < j)
      {
        localArrayList.add(arrayOfDPObject[i]);
        i += 1;
      }
      paramBundle.putParcelableArrayList("hotelDetailArray", localArrayList);
    }
  }

  protected void onStop()
  {
    super.onStop();
    savaData();
  }

  public void updateDay(Calendar paramCalendar, CalendarItem paramCalendarItem, boolean paramBoolean)
  {
    int k = paramCalendar.get(1);
    int m = paramCalendar.get(2);
    int n = paramCalendar.get(5);
    int j = 0;
    Object localObject1 = Calendar.getInstance();
    int i = j;
    if (((Calendar)localObject1).get(1) == k)
    {
      i = j;
      if (((Calendar)localObject1).get(2) == m)
      {
        i = j;
        if (((Calendar)localObject1).get(5) == n)
          i = 1;
      }
    }
    TextView localTextView1 = paramCalendarItem.getDateTextView();
    ViewUtils.px2dip(this, this.itemWidth);
    float f;
    if (this.type == 1)
      f = getResources().getDimensionPixelSize(R.dimen.text_size_15);
    View localView;
    TextView localTextView2;
    label167: Object localObject2;
    label259: Object localObject4;
    while (true)
    {
      localTextView1.setTextSize(0, f);
      localTextView1.setText(String.valueOf(paramCalendarItem.getiDateDay()));
      localView = paramCalendarItem.getView();
      localTextView2 = paramCalendarItem.getDateSubTextView();
      if (this.type == 1)
      {
        f = getResources().getDimensionPixelSize(R.dimen.text_size_14);
        localTextView2.setTextSize(0, f);
        localTextView2.setText("");
        if (i != 0)
        {
          localTextView1.setText("今天");
          if (!localView.isSelected())
            localTextView1.setTextColor(-65536);
        }
        if (this.type != 1)
          break label748;
        if (!paramCalendarItem.isbIsActiveMonth())
          break label739;
        localTextView1.setTextColor(getResources().getColor(R.color.hotel_light_gray));
        localObject1 = getObjectParam("dpDeal");
        if (localObject1 != null)
          break label658;
        localObject1 = null;
        if ((localObject1 != null) && (((DPObject)localObject1).getArray("DayDealList") != null))
        {
          localObject2 = ((DPObject)localObject1).getArray("DayDealList");
          j = localObject2.length;
          i = 0;
          if (i < j)
          {
            localObject1 = localObject2[i];
            long l = ((DPObject)localObject1).getTime("Date");
            localObject4 = Calendar.getInstance();
            ((Calendar)localObject4).setTimeInMillis(l);
            if (HotelCalendarView.compareTwoDays((Calendar)localObject4, paramCalendar) != 0)
              break label719;
            localTextView2.setText("¥" + ((DPObject)localObject1).getInt("Price"));
            localObject2 = (TextView)paramCalendarItem.getView().findViewById(R.id.date_subinfo_2);
            ((TextView)localObject2).setText("余" + ((DPObject)localObject1).getInt("Remain"));
            if (HotelCalendarView.compareTwoDays(paramCalendar, this.checkinCalendar) != 0)
              break label671;
            localView.findViewById(R.id.info).setBackgroundResource(R.drawable.bg_hotel_calendar_item_selected);
            localTextView1.setTextColor(-1);
            localTextView2.setTextColor(-1);
            ((TextView)localObject2).setTextColor(-1);
            this.last_days.add(paramCalendarItem);
          }
        }
        label469: if ((this.festivals == null) || (this.festivals.length == 0))
          break label747;
        i = 0;
        label487: if (i >= this.festivals.length)
          break label747;
      }
      try
      {
        localObject2 = this.festivals[i].getString("Date");
        paramCalendar = this.festivals[i].getString("Name");
        localObject1 = Calendar.getInstance();
        localObject2 = ((String)localObject2).split("-");
        ((Calendar)localObject1).set(1, Integer.parseInt(localObject2[0]));
        ((Calendar)localObject1).set(2, Integer.parseInt(localObject2[1]) - 1);
        ((Calendar)localObject1).set(5, Integer.parseInt(localObject2[2]));
        if (HotelCalendarView.compareTwoDays((Calendar)localObject1, paramCalendarItem.getDate()) == 0)
        {
          localTextView1.setText(paramCalendar);
          localTextView1.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
        }
        i += 1;
        break label487;
        f = getResources().getDimensionPixelSize(R.dimen.text_size_14);
        continue;
        f = getResources().getDimensionPixelSize(R.dimen.text_size_13);
        break label167;
        label658: localObject1 = ((DPObject)localObject1).getObject("Calendar");
        break label259;
        label671: localTextView1.setTextColor(getResources().getColor(R.color.hotel_datepicker_color));
        localTextView2.setTextColor(getResources().getColor(R.color.hotel_roomlist_price_color));
        ((TextView)localObject2).setTextColor(getResources().getColor(R.color.hotel_roomlist_price_color));
        break label469;
        label719: i += 1;
      }
      catch (Exception paramCalendar)
      {
        while (true)
          Log.e(paramCalendar.toString());
      }
    }
    label739: localTextView1.setText("");
    label747: label748: label1266: label1529: label1535: 
    do
    {
      while (true)
      {
        return;
        if (!paramCalendarItem.isbIsActiveMonth())
          break label2108;
        k = 0;
        i = 0;
        localObject1 = "";
        localObject2 = localObject1;
        j = k;
        Object localObject3;
        if (this.festivals != null)
        {
          localObject2 = localObject1;
          j = k;
          if (this.festivals.length != 0)
          {
            k = 0;
            while (true)
            {
              localObject2 = localObject1;
              j = i;
              if (k < this.festivals.length)
                try
                {
                  localObject2 = this.festivals[k].getString("Date");
                  localObject4 = this.festivals[k].getString("Name");
                  Calendar localCalendar = Calendar.getInstance();
                  localObject2 = ((String)localObject2).split("-");
                  localCalendar.set(1, Integer.parseInt(localObject2[0]));
                  localCalendar.set(2, Integer.parseInt(localObject2[1]) - 1);
                  localCalendar.set(5, Integer.parseInt(localObject2[2]));
                  localObject2 = localObject1;
                  j = i;
                  if (HotelCalendarView.compareTwoDays(localCalendar, paramCalendarItem.getDate()) == 0)
                  {
                    localTextView1.setText((CharSequence)localObject4);
                    j = 1;
                    localObject2 = localObject4;
                  }
                  k += 1;
                  localObject1 = localObject2;
                  i = j;
                }
                catch (Exception localObject3)
                {
                  while (true)
                  {
                    Log.e(localException.toString());
                    localObject3 = localObject1;
                    j = i;
                  }
                }
            }
          }
        }
        k = -1;
        i = k;
        if (this.type == 2)
        {
          i = paramCalendarItem.getiDateYear();
          m = paramCalendarItem.getiDateMonth() + 1;
          n = paramCalendarItem.getiDateDay();
          localObject4 = new StringBuilder().append(i).append("-");
          if (m >= 10)
            break label1420;
          localObject1 = "0" + m;
          localObject4 = ((StringBuilder)localObject4).append(localObject1).append("-");
          if (n >= 10)
            break label1430;
          localObject1 = "0" + n;
          localObject1 = localObject1;
          i = k;
          if (this.bookingStatusMap.containsKey(localObject1))
          {
            i = ((DPObject)this.bookingStatusMap.get(localObject1)).getInt("Status");
            localObject1 = "";
          }
        }
        switch (i)
        {
        default:
          localTextView2.setText((CharSequence)localObject1);
          if ((this.isReseted) || (HotelCalendarView.compareTwoDays(paramCalendar, this.checkinCalendar) != 0))
            break label1541;
          localView.setSelected(true);
          paramCalendarItem.setbSelected(true);
          this.last_days.add(paramCalendarItem);
          localTextView1.setTextColor(-1);
          paramCalendar = localView.findViewById(R.id.info);
          if (i != 0)
            break;
          k = R.drawable.bg_hotel_deal_calendar_item_full;
          paramCalendar.setBackgroundResource(k);
          localView.findViewById(R.id.empty_left).setBackgroundColor(-1);
          if (this.isCheckoutTimeSetted)
          {
            localView.findViewById(R.id.empty_right).setBackgroundResource(R.drawable.bg_hotel_calendar_selected_inner);
            if (i != 0)
              break label1510;
            k = 1;
            if (i != 2)
              break label1516;
            m = 1;
            if ((m | k) == 0)
              break label1522;
            paramCalendar = "满房";
            localTextView2.setText(paramCalendar);
            if ((this.type == 2) && (i == 1))
              localTextView2.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
            if (i != 0)
              break label1529;
            k = 1;
            if (i != 2)
              break label1535;
          }
        case 0:
        case 1:
        case 2:
        case 3:
        }
        for (i = 1; ; i = 0)
        {
          if ((i | k) != 0)
            this.isCheckinTimeSetted = false;
          localTextView2.setTextColor(-1);
          if (j == 0)
            break;
          localTextView1.setText(localObject3);
          return;
          localObject1 = Integer.valueOf(m);
          break label1061;
          localObject1 = Integer.valueOf(n);
          break label1106;
          localObject1 = "满房";
          localTextView2.setTextColor(getResources().getColor(R.color.hotel_light_gray));
          break label1196;
          localObject1 = "有房";
          break label1196;
          localObject1 = "满房";
          break label1196;
          localObject1 = "";
          break label1196;
          k = R.drawable.bg_hotel_calendar_item_selected;
          break label1266;
          localView.findViewById(R.id.empty_right).setBackgroundColor(-1);
          break label1305;
          k = 0;
          break label1313;
          m = 0;
          break label1322;
          paramCalendar = "入住";
          break label1334;
          k = 0;
          break label1379;
        }
        if ((this.isReseted) || (!this.isCheckoutTimeSetted) || (HotelCalendarView.compareTwoDays(paramCalendar, this.checkinCalendar) <= 0) || (HotelCalendarView.compareTwoDays(paramCalendar, this.checkoutCalendar) > 0))
          break;
        if (!this.isFirstLodad)
          continue;
        localView.setSelected(true);
        paramCalendarItem.setbSelected(true);
        this.last_days.add(paramCalendarItem);
        localView.findViewById(R.id.info).setBackgroundResource(R.drawable.bg_hotel_calendar_selected_inner);
        localView.findViewById(R.id.empty_left).setBackgroundResource(R.drawable.bg_hotel_calendar_selected_inner);
        localView.findViewById(R.id.empty_right).setBackgroundResource(R.drawable.bg_hotel_calendar_selected_inner);
        localTextView2.setTextColor(-1);
        localTextView1.setTextColor(-1);
        if (this.type == 2)
        {
          i = paramCalendarItem.getiDateYear();
          k = paramCalendarItem.getiDateMonth() + 1;
          m = paramCalendarItem.getiDateDay();
          localObject1 = new StringBuilder().append(i).append("-");
          if (k >= 10)
            break label1980;
          paramCalendarItem = "0" + k;
          localObject1 = ((StringBuilder)localObject1).append(paramCalendarItem).append("-");
          if (m >= 10)
            break label1989;
        }
        for (paramCalendarItem = "0" + m; ; paramCalendarItem = Integer.valueOf(m))
        {
          paramCalendarItem = paramCalendarItem;
          if (this.bookingStatusMap.containsKey(paramCalendarItem))
          {
            i = ((DPObject)this.bookingStatusMap.get(paramCalendarItem)).getInt("Status");
            if ((i == 0) || (i == 2))
            {
              localView.findViewById(R.id.empty_left).setBackgroundColor(-1);
              localView.findViewById(R.id.info).setBackgroundResource(R.drawable.bg_hotel_deal_calendar_item_full);
              localTextView1.setTextColor(-1);
              localTextView2.setTextColor(-1);
            }
          }
          if (HotelCalendarView.compareTwoDays(paramCalendar, this.checkoutCalendar) != 0)
            break;
          localView.findViewById(R.id.info).setBackgroundResource(R.drawable.bg_hotel_calendar_item_selected);
          localView.findViewById(R.id.empty_left).setBackgroundResource(R.drawable.bg_hotel_calendar_selected_inner);
          localView.findViewById(R.id.empty_right).setBackgroundColor(-1);
          localTextView1.setTextColor(-1);
          localTextView2.setText("退房");
          if (this.type == 2)
            localTextView2.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
          localTextView2.setTextColor(-1);
          if (j == 0)
            break;
          localTextView1.setText(localObject3);
          return;
          paramCalendarItem = Integer.valueOf(k);
          break label1735;
        }
      }
      localView.setSelected(false);
      paramCalendarItem.setbSelected(false);
      localView.setBackgroundColor(-1);
      if (paramCalendarItem.isEnable())
      {
        localTextView1.setTextColor(-16777216);
        localTextView2.setTextColor(-16777216);
      }
      while (true)
      {
        localView.findViewById(R.id.info).setBackgroundColor(-1);
        localView.findViewById(R.id.empty_right).setBackgroundColor(-1);
        localView.findViewById(R.id.empty_left).setBackgroundColor(-1);
        return;
        localTextView1.setTextColor(getResources().getColor(R.color.hotel_light_gray));
        localTextView2.setTextColor(getResources().getColor(R.color.hotel_light_gray));
      }
      localTextView1.setText("");
      localTextView1.setTextColor(-7829368);
      localTextView2.setTextColor(-7829368);
    }
    while (!paramBoolean);
    label1061: label1106: label1379: label1510: label1516: label1522: localView.findViewById(R.id.info).setBackgroundResource(R.color.hotel_calender_select_inner_1);
    label1196: label1334: label1735: localView.findViewById(R.id.empty_left).setBackgroundResource(R.color.hotel_calender_select_inner_1);
    label1305: label1313: label1322: label1980: label1989: localView.findViewById(R.id.empty_right).setBackgroundResource(R.color.hotel_calender_select_inner_1);
    label1420: label1430: label2108: this.last_days.add(paramCalendarItem);
    label1541:
  }

  private class HotelBookingCalendarView extends HotelCalendarView
  {
    public HotelBookingCalendarView(Context paramInt, int arg3)
    {
    }

    private void addDateToCalendarView()
    {
      int i = 0;
      while (i < this.days.size())
      {
        HotelBookingPickTimeActivity.this.updateDay(((CalendarItem)this.days.get(i)).getDate(), (CalendarItem)this.days.get(i), false);
        i += 1;
      }
    }

    protected CalendarItem createCalendarItem(View paramView, Calendar paramCalendar)
    {
      CalendarItem localCalendarItem = super.createCalendarItem(paramView, paramCalendar);
      int i;
      int j;
      int k;
      if (HotelBookingPickTimeActivity.this.type == 2)
      {
        i = paramCalendar.get(1);
        j = paramCalendar.get(2) + 1;
        k = paramCalendar.get(5);
        paramCalendar = new StringBuilder().append(i).append("-");
        if (j < 10)
        {
          paramView = "0" + j;
          paramCalendar = paramCalendar.append(paramView).append("-");
          if (k >= 10)
            break label167;
          paramView = "0" + k;
          label126: paramView = paramView;
          if (HotelBookingPickTimeActivity.this.bookingStatusMap.containsKey(paramView))
            break label176;
          localCalendarItem.setEnable(false);
        }
      }
      label167: label303: 
      while (true)
      {
        return localCalendarItem;
        paramView = Integer.valueOf(j);
        break;
        paramView = Integer.valueOf(k);
        break label126;
        label176: localCalendarItem.setEnable(true);
        return localCalendarItem;
        if (HotelBookingPickTimeActivity.this.type != 1)
          continue;
        localCalendarItem.setEnable(false);
        paramView = HotelBookingPickTimeActivity.this.getObjectParam("dpDeal");
        if (paramView == null);
        for (paramView = null; ; paramView = paramView.getObject("Calendar"))
        {
          if ((paramView == null) || (paramView.getArray("DayDealList") == null))
            break label303;
          paramView = paramView.getArray("DayDealList");
          j = paramView.length;
          i = 0;
          while (i < j)
          {
            long l = paramView[i].getTime("Date");
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.setTimeInMillis(l);
            if (HotelCalendarView.compareTwoDays(localCalendar, paramCalendar) == 0)
              localCalendarItem.setEnable(true);
            i += 1;
          }
          break;
        }
      }
    }

    public boolean setDate(Calendar paramCalendar1, Calendar paramCalendar2)
    {
      this.isAfterEnable = true;
      this.calToday.setTimeInMillis(paramCalendar1.getTimeInMillis());
      this.calStartDate.setTimeInMillis(paramCalendar2.getTimeInMillis());
      this.deadlineCalendar.setTimeInMillis(HotelBookingPickTimeActivity.this.deadlineCalendar.getTimeInMillis());
      updateStartDateForMonth();
      boolean bool = layoutInitialize(HotelBookingPickTimeActivity.this.allDays.size());
      HotelBookingPickTimeActivity.this.allDays.addAll(this.days);
      addDateToCalendarView();
      return bool;
    }

    protected void updateCalendar()
    {
      int j;
      if (HotelBookingPickTimeActivity.this.index_checkin >= 0)
      {
        int i = 0;
        j = 0;
        int i2 = 0;
        int i1 = HotelBookingPickTimeActivity.this.index_checkin;
        label54: boolean bool;
        label140: int i3;
        int i4;
        int n;
        int m;
        label237: int i5;
        if (HotelBookingPickTimeActivity.this.index_checkout >= HotelBookingPickTimeActivity.this.index_checkin)
        {
          k = HotelBookingPickTimeActivity.this.index_checkout;
          if (i1 > k)
            break label593;
          localObject1 = Calendar.getInstance();
          CalendarItem localCalendarItem = (CalendarItem)HotelBookingPickTimeActivity.this.allDays.get(i1);
          ((Calendar)localObject1).set(1, localCalendarItem.getiDateYear());
          ((Calendar)localObject1).set(2, localCalendarItem.getiDateMonth());
          ((Calendar)localObject1).set(5, localCalendarItem.getiDateDay());
          Object localObject2 = HotelBookingPickTimeActivity.this;
          if ((i1 == HotelBookingPickTimeActivity.this.index_checkout) || (i1 == HotelBookingPickTimeActivity.this.index_checkin))
            break label557;
          bool = true;
          ((HotelBookingPickTimeActivity)localObject2).updateDay((Calendar)localObject1, localCalendarItem, bool);
          i3 = i;
          i4 = i2;
          n = j;
          if (HotelBookingPickTimeActivity.this.type == 2)
          {
            k = localCalendarItem.getiDateYear();
            m = localCalendarItem.getiDateMonth() + 1;
            n = localCalendarItem.getiDateDay();
            localObject2 = new StringBuilder().append(k).append("-");
            if (m >= 10)
              break label563;
            localObject1 = "0" + m;
            localObject2 = ((StringBuilder)localObject2).append(localObject1).append("-");
            if (n >= 10)
              break label572;
            localObject1 = "0" + n;
            label276: localObject1 = localObject1;
            i3 = i;
            i4 = i2;
            n = j;
            if (HotelBookingPickTimeActivity.this.bookingStatusMap.containsKey(localObject1))
            {
              i3 = i;
              i4 = i2;
              n = j;
              if (localCalendarItem.isbIsActiveMonth())
              {
                i5 = ((DPObject)HotelBookingPickTimeActivity.this.bookingStatusMap.get(localObject1)).getInt("Status");
                if (i5 != 0)
                {
                  k = i;
                  m = j;
                  if (i5 != 2);
                }
                else
                {
                  n = i;
                  if (HotelBookingPickTimeActivity.this.index_checkin > 0)
                  {
                    n = i;
                    if (HotelBookingPickTimeActivity.this.index_checkin < i1)
                      if (HotelBookingPickTimeActivity.this.index_checkout < HotelBookingPickTimeActivity.this.index_checkin)
                        break label581;
                  }
                }
              }
            }
          }
        }
        label557: label563: label572: label581: for (int k = HotelBookingPickTimeActivity.this.index_checkout; ; k = HotelBookingPickTimeActivity.this.index_checkin)
        {
          n = i;
          if (i1 < k)
            n = 1;
          k = n;
          m = j;
          if (HotelBookingPickTimeActivity.this.index_checkin == i1)
          {
            m = 1;
            k = n;
          }
          i3 = k;
          i4 = i2;
          n = m;
          if (i5 == 3)
          {
            i3 = k;
            i4 = i2;
            n = m;
            if (HotelBookingPickTimeActivity.this.index_checkin == i1)
            {
              i4 = 1;
              n = m;
              i3 = k;
            }
          }
          i1 += 1;
          i = i3;
          i2 = i4;
          j = n;
          break;
          k = HotelBookingPickTimeActivity.this.index_checkin;
          break label54;
          bool = false;
          break label140;
          localObject1 = Integer.valueOf(m);
          break label237;
          localObject1 = Integer.valueOf(n);
          break label276;
        }
        label593: if (HotelBookingPickTimeActivity.this.type == 2)
        {
          if (i2 != 0)
          {
            HotelBookingPickTimeActivity.this.handler.sendEmptyMessage(2);
            localObject1 = Toast.makeText(HotelBookingPickTimeActivity.this, "本日暂不接受在线预订", 0);
            ((Toast)localObject1).setGravity(17, 0, 0);
            ((Toast)localObject1).show();
            HotelBookingPickTimeActivity.this.titleButton.setEnabled(false);
            new Thread()
            {
              public void run()
              {
                super.run();
                try
                {
                  Thread.sleep(1000L);
                  HotelBookingPickTimeActivity.this.handler.sendEmptyMessage(2);
                  return;
                }
                catch (InterruptedException localInterruptedException)
                {
                  while (true)
                    localInterruptedException.printStackTrace();
                }
              }
            }
            .start();
          }
          if (i == 0)
            break label718;
          localObject1 = Toast.makeText(HotelBookingPickTimeActivity.this, "包含满房日期", 0);
          ((Toast)localObject1).setGravity(17, 0, 0);
          ((Toast)localObject1).show();
          HotelBookingPickTimeActivity.this.titleButton.setEnabled(false);
          new Thread()
          {
            public void run()
            {
              super.run();
              try
              {
                Thread.sleep(1000L);
                HotelBookingPickTimeActivity.this.handler.sendEmptyMessage(2);
                return;
              }
              catch (InterruptedException localInterruptedException)
              {
                while (true)
                  localInterruptedException.printStackTrace();
              }
            }
          }
          .start();
        }
      }
      label718: 
      do
        return;
      while (j == 0);
      Object localObject1 = Toast.makeText(HotelBookingPickTimeActivity.this, "入住日期已满房", 0);
      ((Toast)localObject1).setGravity(17, 0, 0);
      ((Toast)localObject1).show();
      HotelBookingPickTimeActivity.this.titleButton.setEnabled(false);
      new Thread()
      {
        public void run()
        {
          super.run();
          try
          {
            Thread.sleep(1000L);
            HotelBookingPickTimeActivity.this.handler.sendEmptyMessage(2);
            return;
          }
          catch (InterruptedException localInterruptedException)
          {
            while (true)
              localInterruptedException.printStackTrace();
          }
        }
      }
      .start();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.commons.activity.HotelBookingPickTimeActivity
 * JD-Core Version:    0.6.0
 */