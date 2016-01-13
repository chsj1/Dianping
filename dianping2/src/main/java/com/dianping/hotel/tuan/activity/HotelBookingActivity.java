package com.dianping.hotel.tuan.activity;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.CalendarView;
import com.dianping.base.widget.CalendarView.OnDateChangeListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.hotel.tuan.widget.HotelTouristInfo;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotelBookingActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private final DateFormat VALID_DATE = new SimpleDateFormat("yyyy-MM-dd E");
  Button bookBtn;
  private Dialog calendarDialog;
  CalendarView calendarView;
  MApiRequest createReservationRequest;
  View dayPicker;
  TextView dayText;
  DPObject dpReservation;
  Calendar endDate = Calendar.getInstance();
  List<Calendar> exDates = new ArrayList();
  int orderId;
  TextView orderNum;
  EditText peopleID;
  View peopleIDLayout;
  EditText peopleName;
  TextView peopleNum;
  EditText phoneNum;
  Calendar selectDate = Calendar.getInstance();
  Calendar startDate = Calendar.getInstance();
  MApiRequest submitReservationRequest;
  TextView ticketNum;
  TextView title;
  ArrayList<HotelTouristInfo> touristInfos = new ArrayList();
  LinearLayout touristLayout;
  TextView touristTip;

  private boolean checkInput()
  {
    if (!validateName(this.peopleName))
      return false;
    if (!validatePhone(this.phoneNum))
      return false;
    boolean bool1 = this.dpReservation.getBoolean("IsRealName");
    boolean bool2 = this.dpReservation.getBoolean("NeedIdCard");
    if (((bool1) || (bool2)) && (!validateIDs(this.peopleID)))
      return false;
    int i = 0;
    while (i < this.touristInfos.size())
    {
      if (!((HotelTouristInfo)this.touristInfos.get(i)).checkInput())
        return false;
      i += 1;
    }
    if (this.touristInfos.size() > 0)
    {
      i = 0;
      while (i < this.touristInfos.size())
      {
        if (this.peopleName.getText().toString().trim().equals(((HotelTouristInfo)this.touristInfos.get(i)).getName()))
        {
          new AlertDialog.Builder(this).setTitle("错误").setMessage("出行人姓名不可重复").show();
          return false;
        }
        i += 1;
      }
      i = 0;
      while (i < this.touristInfos.size() - 1)
      {
        int j = i + 1;
        while (j < this.touristInfos.size())
        {
          if (((HotelTouristInfo)this.touristInfos.get(i)).getName().equals(((HotelTouristInfo)this.touristInfos.get(j)).getName()))
          {
            new AlertDialog.Builder(this).setTitle("错误").setMessage("出行人姓名不可重复").show();
            return false;
          }
          j += 1;
        }
        i += 1;
      }
    }
    return true;
  }

  private void createReservation()
  {
    if (this.createReservationRequest != null)
      return;
    this.createReservationRequest = BasicMApiRequest.mapiGet("http://app.t.dianping.com/createreservationgn.bin?token=" + accountService().token() + "&orderid=" + this.orderId, CacheType.DISABLED);
    mapiService().exec(this.createReservationRequest, this);
    showProgressDialog("请稍候...");
  }

  private void hideKeyBoard()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
    if (this.peopleName != null)
      localInputMethodManager.hideSoftInputFromWindow(this.peopleName.getWindowToken(), 0);
    if (this.peopleID != null)
      localInputMethodManager.hideSoftInputFromInputMethod(this.peopleID.getWindowToken(), 0);
    if (this.phoneNum != null)
      localInputMethodManager.hideSoftInputFromWindow(this.phoneNum.getWindowToken(), 0);
    int i = 0;
    while (i < this.touristInfos.size())
    {
      ((HotelTouristInfo)this.touristInfos.get(i)).hideKeyBoard();
      i += 1;
    }
  }

  private void setupCalendarView()
  {
    this.calendarDialog = new Dialog(this, R.style.dialog);
    this.calendarDialog.setCanceledOnTouchOutside(true);
    this.calendarView = new CalendarView(this);
    this.calendarDialog.setContentView(this.calendarView);
    this.calendarView.setDate(Calendar.getInstance());
    this.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
    {
      public void onDateChange(Calendar paramCalendar)
      {
        HotelBookingActivity.this.selectDate.setTimeInMillis(paramCalendar.getTimeInMillis());
        HotelBookingActivity.this.updateDateView();
        HotelBookingActivity.this.calendarDialog.dismiss();
      }
    });
  }

  private void setupView()
  {
    this.title = ((TextView)findViewById(R.id.title));
    this.orderNum = ((TextView)findViewById(R.id.order_num));
    this.ticketNum = ((TextView)findViewById(R.id.ticket_num));
    this.peopleNum = ((TextView)findViewById(R.id.people_num));
    this.touristTip = ((TextView)findViewById(R.id.tourist_tip));
    this.dayText = ((TextView)findViewById(R.id.day));
    this.phoneNum = ((EditText)findViewById(R.id.phone));
    this.peopleName = ((EditText)findViewById(R.id.people_name));
    this.peopleID = ((EditText)findViewById(R.id.people_id));
    this.bookBtn = ((Button)findViewById(R.id.button));
    this.bookBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (!HotelBookingActivity.this.checkInput())
          return;
        HotelBookingActivity.this.hideKeyBoard();
        HotelBookingActivity.this.submitReservation();
      }
    });
    this.dayPicker = findViewById(R.id.day_picker);
    this.touristLayout = ((LinearLayout)findViewById(R.id.tourist_layout));
    this.peopleIDLayout = findViewById(R.id.people_id_layout);
    this.dayPicker.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HotelBookingActivity.this.calendarDialog.show();
      }
    });
  }

  private void submitReservation()
  {
    if (this.submitReservationRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(accountService().token());
    Object localObject = new SimpleDateFormat("yyyy-MM-dd");
    localArrayList.add("date");
    localArrayList.add(((DateFormat)localObject).format(this.selectDate.getTime()));
    localArrayList.add("orderid");
    localArrayList.add(String.valueOf(this.orderId));
    localArrayList.add("count");
    localArrayList.add(String.valueOf(this.dpReservation.getInt("TouristNum")));
    localArrayList.add("contactinfo");
    localArrayList.add(this.peopleName.getText().toString().trim() + ":" + this.phoneNum.getText().toString().trim() + ":" + this.peopleID.getText().toString().trim());
    if (this.touristInfos.size() > 0)
    {
      localArrayList.add("tourists");
      localObject = "" + ((HotelTouristInfo)this.touristInfos.get(0)).getName() + ":" + ((HotelTouristInfo)this.touristInfos.get(0)).getPhone();
      int i = 1;
      while (i < this.touristInfos.size())
      {
        localObject = (String)localObject + "|" + ((HotelTouristInfo)this.touristInfos.get(i)).getName() + ":" + ((HotelTouristInfo)this.touristInfos.get(i)).getPhone();
        i += 1;
      }
      localArrayList.add(localObject);
    }
    this.submitReservationRequest = BasicMApiRequest.mapiPost("http://app.t.dianping.com/submitreservationgn.bin", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.submitReservationRequest, this);
    showProgressDialog("请稍候...");
  }

  private void updateCalendarView()
  {
    this.startDate.setTimeInMillis(this.dpReservation.getTime("BeginDate"));
    this.endDate.setTimeInMillis(this.dpReservation.getTime("EndDate"));
    this.selectDate.setTimeInMillis(this.dpReservation.getTime("BeginDate"));
    long[] arrayOfLong = this.dpReservation.getTimeArray("DisableDates");
    this.exDates.clear();
    if (arrayOfLong != null)
    {
      int i = 0;
      while (i < arrayOfLong.length)
      {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(arrayOfLong[i]);
        this.exDates.add(localCalendar);
        i += 1;
      }
    }
    this.calendarView.setDate(this.startDate, this.endDate, this.selectDate, this.exDates);
    updateDateView();
  }

  private void updateDateView()
  {
    this.dayText.setText(this.VALID_DATE.format(this.selectDate.getTime()));
  }

  private void updateView()
  {
    int j;
    boolean bool;
    if (this.dpReservation != null)
    {
      updateCalendarView();
      this.orderNum.setText("" + this.orderId);
      j = this.dpReservation.getInt("TouristNum");
      this.title.setText(this.dpReservation.getString("Title"));
      this.ticketNum.setText("" + this.dpReservation.getInt("ReceiptNum"));
      this.peopleNum.setText("" + j);
      this.phoneNum.setText(this.dpReservation.getString("PhoneNumber"));
      this.touristTip.setText(this.dpReservation.getString("Extra"));
      bool = this.dpReservation.getBoolean("NeedIdCard");
      if (!this.dpReservation.getBoolean("IsRealName"))
        break label322;
      this.peopleIDLayout.setVisibility(0);
      if (j > 1)
        break label212;
      this.touristLayout.setVisibility(8);
    }
    while (true)
    {
      return;
      label212: this.touristLayout.setVisibility(0);
      this.touristLayout.removeAllViews();
      this.touristInfos.clear();
      int i = 2;
      while (i <= j)
      {
        HotelTouristInfo localHotelTouristInfo = new HotelTouristInfo(this);
        localHotelTouristInfo.setTextView1("出行人" + i);
        localHotelTouristInfo.setTextView2("手机号" + i);
        this.touristInfos.add(localHotelTouristInfo);
        this.touristLayout.addView(localHotelTouristInfo);
        i += 1;
      }
    }
    label322: if (bool)
    {
      this.peopleIDLayout.setVisibility(0);
      this.touristLayout.setVisibility(8);
      this.touristLayout.removeAllViews();
      this.touristInfos.clear();
      return;
    }
    this.peopleIDLayout.setVisibility(8);
    this.touristLayout.setVisibility(8);
    this.touristLayout.removeAllViews();
    this.touristInfos.clear();
  }

  private boolean validateIDs(EditText paramEditText)
  {
    String str = paramEditText.getText().toString();
    Object localObject2 = null;
    Object localObject1;
    if (TextUtils.isEmpty(str))
      localObject1 = "身份证不能为空";
    while (localObject1 != null)
    {
      paramEditText.requestFocus();
      paramEditText.setError(Html.fromHtml("<font color=#ff0000>" + (String)localObject1 + "</font>"));
      return false;
      localObject1 = localObject2;
      if (str.length() == 18)
        continue;
      localObject1 = localObject2;
      if (str.length() == 15)
        continue;
      localObject1 = "身份证必须为15位或18位";
    }
    return true;
  }

  private boolean validateName(EditText paramEditText)
  {
    String str2 = paramEditText.getText().toString();
    String str1 = null;
    if (TextUtils.isEmpty(str2))
      str1 = "姓名不能为空";
    while (str1 != null)
    {
      paramEditText.requestFocus();
      paramEditText.setError(Html.fromHtml("<font color=#ff0000>" + str1 + "</font>"));
      return false;
      str2 = str2.trim();
      if (Pattern.compile("^[a-zA-Z一-龥]+$").matcher(str2).matches())
        continue;
      str1 = "姓名只能为英文和汉字";
    }
    return true;
  }

  private boolean validatePhone(EditText paramEditText)
  {
    String str2 = paramEditText.getText().toString().trim();
    String str1 = null;
    if (str2.length() != 11)
      str1 = "手机号码必须为11位";
    if (!Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$").matcher(str2).matches())
      str1 = "手机号格式不正确";
    if (str1 != null)
    {
      paramEditText.requestFocus();
      paramEditText.setError(Html.fromHtml("<font color=#ff0000>" + str1 + "</font>"));
      return false;
    }
    return true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.orderId = getIntent().getIntExtra("orderId", 0);
    if (this.orderId == 0)
    {
      paramBundle = getIntent().getData().getQueryParameter("orderid");
      if (!TextUtils.isEmpty(paramBundle))
        this.orderId = Integer.valueOf(paramBundle).intValue();
    }
    setContentView(R.layout.hotel_booking);
    setupView();
    setupCalendarView();
    createReservation();
  }

  protected void onLeftTitleButtonClicked()
  {
    hideKeyBoard();
    super.onLeftTitleButtonClicked();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.createReservationRequest)
      this.createReservationRequest = null;
    do
      return;
    while (paramMApiRequest != this.submitReservationRequest);
    Toast.makeText(this, paramMApiResponse.message().toString(), 0).show();
    this.submitReservationRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.createReservationRequest)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.dpReservation = ((DPObject)paramMApiResponse.result());
        updateView();
      }
      this.createReservationRequest = null;
    }
    do
      return;
    while (paramMApiRequest != this.submitReservationRequest);
    paramMApiRequest = new Intent("android.intent.action.VIEW", Uri.parse("dianping://ttreservationresult"));
    paramMApiRequest.putExtra("couponid", this.dpReservation.getIntArray("ReceiptIds")[0]);
    startActivity(paramMApiRequest);
    this.submitReservationRequest = null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.tuan.activity.HotelBookingActivity
 * JD-Core Version:    0.6.0
 */