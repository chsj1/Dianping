package com.dianping.booking;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.wheel.adapter.AbstractWheelTextAdapter;
import com.dianping.base.widget.wheel.widget.OnWheelScrollListener;
import com.dianping.base.widget.wheel.widget.WheelView;
import com.dianping.booking.fragment.BookingTimePickerFragment;
import com.dianping.booking.util.BookingUtil;
import com.dianping.booking.util.TrimRunnable;
import com.dianping.booking.view.BookingCountryCodeView;
import com.dianping.booking.view.CustomDialogView;
import com.dianping.booking.view.PeoplePickerView;
import com.dianping.booking.view.ShopPickerView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.DateUtil;
import com.dianping.util.DateUtils;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.PermissionCheckHelper;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class GrouponBookingInfoActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  static final String COUNTRY_CODE_KEY = "account:countryCode";
  private static final String HALL_FULL_TIP = "餐厅已订满，请尝试其他时间";
  private static final int NEED_VALIDATION = 1;
  private static final int REQUEST_CODE = 100;
  private static final String UNBOOKABLE_TIP = "当前时间餐厅不接受订座";
  private BookingCountryCodeView bookingCountryCode;
  private MApiRequest bookingHolidaysRequest;
  private EditText bookingName;
  String bookingNameStr;
  private int bookingNum;
  private EditText bookingPhone;
  String bookingPhoneStr;
  private ImageView brandingClose;
  private DPObject brandingDetail;
  private NetworkImageView brandingImage;
  private RelativeLayout brandingLayout;
  private MApiRequest brandingRequest;
  private String compartmentStr = "";
  private Calendar curCalendar;
  private TextView dateDetailInfo;
  private View dateDetailView;
  String defaultDealGroupID;
  String defaultDealID;
  private int defaultNum;
  private IntentFilter filter = null;
  private int gender = 10;
  private RadioGroup genderView;
  private DPObject grouponBookingConfig;
  private GrouponBookingDataManager grouponBookingDataManager;
  private ScrollView grouponBookingInfo;
  private RelativeLayout grouponBookingInfoView;
  private Animation grouponBookingPushInAnimation;
  private Animation grouponBookingPushOutAnimation;
  private DPObject grouponBookingRecord;
  private MApiRequest grouponBookingRequest;
  private DPObject grouponBookingResult;
  private LinearLayout grouponBookingResultView;
  private RelativeLayout grouponBookingShopPickerView;
  private MApiRequest grouponBookingShopReq;
  private TextView grouponBookingTips;
  private DPObject[] grouponLists;
  private ArrayList<DPObject> grouponShopList;
  private LinearLayout grouponsView;
  private DPObject[] holidaysList;
  private String imageUrl = null;
  boolean isSetRoom = false;
  private TrimRunnable mTrimRunnable;
  private Dialog peopleNumDialog;
  private TextView peoplePicker;
  private PeoplePickerView peoplePickerDialogView;
  private View peoplePickerView;
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.booking.GROUPON_BOOKING_TIME_PICKED"))
      {
        long l = paramIntent.getLongExtra("selectCal", GrouponBookingInfoActivity.this.setCalendar.getTimeInMillis());
        GrouponBookingInfoActivity.this.setCalendar.setTimeInMillis(l);
        GrouponBookingInfoActivity.this.grouponBookingDataManager.setGrouponBookingDate(GrouponBookingInfoActivity.this.setCalendar, GrouponBookingInfoActivity.this.roomFlag, GrouponBookingInfoActivity.this.bookingNum);
        GrouponBookingInfoActivity.this.setRoomsInfo();
        GrouponBookingInfoActivity.this.dateDetailInfo.setText(GrouponBookingInfoActivity.this.formateDateDetailInfo(GrouponBookingInfoActivity.this.setCalendar));
      }
    }
  };
  String recordShopName;
  private View resultView;
  private int roomFlag = 10;
  private Calendar setCalendar;
  private int setDealId;
  private Animation shineAnimation;
  private DPObject shop;
  int shopId;
  String shopName;
  private TextView shopNamePickerView;
  private Dialog shopPickDialog;
  private ShopPickerView shopPickerDialogView;
  ShopWheelArrayAdapter shopWheelArrayAdapter;
  private Button submitGrouponBooking;
  private Handler trimHandler = new Handler();
  private Handler waitingHintHandler = new Handler()
  {
    private int index;
    private String[] messages = { "网速怎有点慢,请稍等...", "再等我一下...", "客官等等,出来了!", "正在整理包房...", "服务员正在列队中..." };

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        super.handleMessage(paramMessage);
        return;
      case 1000:
      }
      String[] arrayOfString;
      int i;
      if ((GrouponBookingInfoActivity.this.managedDialog instanceof ProgressDialog))
      {
        paramMessage = (ProgressDialog)GrouponBookingInfoActivity.this.managedDialog;
        arrayOfString = this.messages;
        i = this.index;
        this.index = (i + 1);
        paramMessage.setMessage(arrayOfString[(i % this.messages.length)]);
      }
      while (true)
      {
        sendEmptyMessageDelayed(1000, 2000L);
        return;
        paramMessage = GrouponBookingInfoActivity.this;
        arrayOfString = this.messages;
        i = this.index;
        this.index = (i + 1);
        paramMessage.showProgressDialog(arrayOfString[(i % this.messages.length)]);
        GrouponBookingInfoActivity.this.managedDialog.setCanceledOnTouchOutside(false);
      }
    }
  };

  private boolean checkInput()
  {
    this.bookingName.setError(null);
    this.bookingPhone.setError(null);
    ForegroundColorSpan localForegroundColorSpan = new ForegroundColorSpan(-16777216);
    if (this.bookingNum <= 0)
    {
      this.peoplePicker.setText("请选择就餐人数");
      this.peoplePicker.startAnimation(this.shineAnimation);
      statisticsEvent("booking5", "booking5_groupon_submit_failure", "1", 0);
      return false;
    }
    String str = this.bookingName.getText().toString().trim();
    if (TextUtils.isEmpty(str))
    {
      this.bookingName.requestFocus();
      localObject = new SpannableStringBuilder("姓名不能为空");
      ((SpannableStringBuilder)localObject).setSpan(localForegroundColorSpan, 0, "姓名不能为空".length(), 0);
      this.bookingName.setError((CharSequence)localObject);
      statisticsEvent("booking5", "booking5_groupon_submit_failure", "6", 0);
      return false;
    }
    if (BookingUtil.isEnglish(str));
    for (Object localObject = "^[a-zA-Z一-龥\\s]+$"; !Pattern.compile((String)localObject).matcher(str).matches(); localObject = "^[a-zA-Z一-龥]+$")
    {
      this.bookingName.requestFocus();
      localObject = new SpannableStringBuilder("姓名只能为英文和汉字");
      ((SpannableStringBuilder)localObject).setSpan(localForegroundColorSpan, 0, "姓名只能为英文和汉字".length(), 0);
      this.bookingName.setError((CharSequence)localObject);
      statisticsEvent("booking5", "booking5_groupon_submit_failure", "6", 0);
      return false;
    }
    if (TextUtils.isEmpty(this.bookingPhone.getText().toString().trim()))
    {
      this.bookingPhone.requestFocus();
      localObject = new SpannableStringBuilder("手机号码不能为空");
      ((SpannableStringBuilder)localObject).setSpan(localForegroundColorSpan, 0, "手机号码不能为空".length(), 0);
      this.bookingPhone.setError((CharSequence)localObject);
      statisticsEvent("booking5", "booking5_groupon_submit_failure", "2", 0);
      return false;
    }
    return true;
  }

  private int compareTwoDays(Calendar paramCalendar1, Calendar paramCalendar2)
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

  private String formateDateDetailInfo(Calendar paramCalendar)
  {
    String str = DateUtils.formatDate(paramCalendar);
    Object localObject = Calendar.getInstance();
    Calendar localCalendar1 = Calendar.getInstance();
    localCalendar1.add(5, 1);
    Calendar localCalendar2 = Calendar.getInstance();
    localCalendar2.add(5, 2);
    if (compareTwoDays(paramCalendar, (Calendar)localObject) == 0)
      localObject = "今天";
    while (true)
    {
      paramCalendar = paramCalendar.get(11) + ":" + String.format("%02d", new Object[] { Integer.valueOf(paramCalendar.get(12)) });
      return str + " " + (String)localObject + " " + paramCalendar;
      if (compareTwoDays(paramCalendar, localCalendar1) == 0)
      {
        localObject = "明天";
        continue;
      }
      if (compareTwoDays(paramCalendar, localCalendar2) == 0)
      {
        localObject = "后天";
        continue;
      }
      localObject = DateFormat.format("E", paramCalendar).toString();
    }
  }

  private int getBookingGender()
  {
    return getSharedPreferences("bookinginfo", 0).getInt("gender", 10);
  }

  private String getBookingName()
  {
    return getSharedPreferences("bookinginfo", 0).getString("name", "");
  }

  private String getCountryCode()
  {
    String str = getSharedPreferences("jsbridge_storage", 0).getString("account:countryCode", "");
    if (!TextUtils.isEmpty(str))
      try
      {
        str = new JSONObject(str).optString("code");
        boolean bool = TextUtils.isEmpty(str);
        if (!bool)
          return str;
      }
      catch (JSONException localJSONException)
      {
      }
    return "86";
  }

  private void getGrouponBookingHolidaysTask()
  {
    if (this.bookingHolidaysRequest != null)
      return;
    this.bookingHolidaysRequest = BasicMApiRequest.mapiGet("http://rs.api.dianping.com/getbookingholidays.yy", CacheType.NORMAL);
    mapiService().exec(this.bookingHolidaysRequest, this);
  }

  private void getGrouponBookingShopTask()
  {
    if (this.grouponBookingShopReq != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://rs.api.dianping.com/getgrouponbookableshop.yy").buildUpon().appendQueryParameter("cityid", String.valueOf(cityId())).appendQueryParameter("dealgroupid", this.defaultDealGroupID);
    Location localLocation = location();
    if (localLocation != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localBuilder.appendQueryParameter("latitude", localDecimalFormat.format(localLocation.latitude())).appendQueryParameter("longitude", localDecimalFormat.format(localLocation.longitude()));
    }
    this.grouponBookingShopReq = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.grouponBookingShopReq, this);
  }

  private String getPhone()
  {
    Object localObject2 = getSharedPreferences("bookinginfo", 0).getString("phone", "");
    Object localObject1 = localObject2;
    if (TextUtils.isEmpty((CharSequence)localObject2))
    {
      if (accountService().token() == null)
        break label107;
      localObject2 = accountService().profile().getString("PhoneNo");
      localObject1 = localObject2;
      if (!TextUtils.isEmpty((CharSequence)localObject2));
    }
    label107: for (localObject1 = getPhoneFromSIM(); ; localObject1 = getPhoneFromSIM())
    {
      localObject2 = localObject1;
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        localObject2 = localObject1;
        if (((String)localObject1).startsWith("+86"))
          localObject2 = ((String)localObject1).substring(((String)localObject1).indexOf("+86") + 3);
      }
      return localObject2;
    }
  }

  private String getPhoneFromSIM()
  {
    String str = "";
    if (PermissionCheckHelper.isPermissionGranted(this, "android.permission.READ_PHONE_STATE"));
    try
    {
      str = ((TelephonyManager)getSystemService("phone")).getLine1Number();
      return str;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return "";
  }

  private void gotoTimePicker(Bundle paramBundle)
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    BookingTimePickerFragment localBookingTimePickerFragment = new BookingTimePickerFragment();
    localBookingTimePickerFragment.setArguments(paramBundle);
    localFragmentTransaction.add(R.id.groupon_booking_info_view, localBookingTimePickerFragment);
    localFragmentTransaction.setTransition(4097);
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.commit();
  }

  private void hideSoftKeyBoard()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
    if (this.bookingName != null)
      localInputMethodManager.hideSoftInputFromWindow(this.bookingName.getWindowToken(), 0);
    if (this.bookingPhone != null)
      localInputMethodManager.hideSoftInputFromInputMethod(this.bookingPhone.getWindowToken(), 0);
  }

  private void initPeopleNumDialog()
  {
    this.peopleNumDialog = new Dialog(this, R.style.dialog);
    this.peopleNumDialog.setCanceledOnTouchOutside(true);
    this.peoplePickerDialogView = ((PeoplePickerView)LayoutInflater.from(this).inflate(R.layout.people_picker_view, null, false));
    CustomDialogView localCustomDialogView = (CustomDialogView)LayoutInflater.from(this).inflate(R.layout.number_picker_dialog, null, false);
    localCustomDialogView.setTitle("选择就餐人数").setView(this.peoplePickerDialogView).setPositiveButton("确定", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        GrouponBookingInfoActivity.this.peopleNumDialog.dismiss();
        GrouponBookingInfoActivity.this.isSetRoom = false;
        GrouponBookingInfoActivity.this.updatePeopleNum();
        GrouponBookingInfoActivity.this.grouponBookingDataManager.setGrouponBookingDate(GrouponBookingInfoActivity.this.setCalendar, GrouponBookingInfoActivity.this.roomFlag, GrouponBookingInfoActivity.this.bookingNum);
        GrouponBookingInfoActivity.this.setRoomsInfo();
      }
    });
    this.peopleNumDialog.setContentView(localCustomDialogView);
  }

  private void initShopPickDialog()
  {
    this.shopPickDialog = new Dialog(this, R.style.dialog);
    this.shopPickDialog.setCanceledOnTouchOutside(true);
    this.shopPickerDialogView = ((ShopPickerView)LayoutInflater.from(this).inflate(R.layout.booking_grouponshop_picker_view, null, false));
    this.shopWheelArrayAdapter = new ShopWheelArrayAdapter(this, this.grouponShopList);
    this.shopPickerDialogView.setAdapter(this.shopWheelArrayAdapter);
    this.shopPickerDialogView.pickerView.addScrollingListener(new OnWheelScrollListener()
    {
      public void onScrollingFinished(WheelView paramWheelView)
      {
        GrouponBookingInfoActivity.this.shopPickerDialogView.setAdapter(GrouponBookingInfoActivity.this.shopWheelArrayAdapter);
      }

      public void onScrollingStarted(WheelView paramWheelView)
      {
      }
    });
    CustomDialogView localCustomDialogView = (CustomDialogView)LayoutInflater.from(this).inflate(R.layout.number_picker_dialog, null, false);
    localCustomDialogView.setTitle("选择订座门店").setView(this.shopPickerDialogView).setPositiveButton("确定", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        GrouponBookingInfoActivity.this.shopPickDialog.dismiss();
        GrouponBookingInfoActivity.this.setupViewData(((DPObject)GrouponBookingInfoActivity.this.grouponShopList.get(GrouponBookingInfoActivity.this.shopPickerDialogView.getCurrentItem())).getInt("ShopId"));
        GrouponBookingInfoActivity.this.shopNamePickerView.setText(((DPObject)GrouponBookingInfoActivity.this.grouponShopList.get(GrouponBookingInfoActivity.this.shopPickerDialogView.getCurrentItem())).getString("ShopName"));
        GrouponBookingInfoActivity.access$202(GrouponBookingInfoActivity.this, 0);
      }
    });
    this.shopPickDialog.setContentView(localCustomDialogView);
  }

  private void saveUserInfo()
  {
    SharedPreferences.Editor localEditor = getSharedPreferences("bookinginfo", 0).edit();
    localEditor.putString("name", this.bookingName.getText().toString().trim());
    localEditor.putInt("gender", this.gender);
    localEditor.putString("phone", this.bookingPhone.getText().toString().trim());
    localEditor.commit();
  }

  private void setBookingNumText()
  {
    if (this.bookingNum == 0)
    {
      this.peoplePicker.setText(String.valueOf(this.defaultNum));
      this.bookingNum = this.defaultNum;
      this.grouponBookingDataManager.setGrouponBookingDate(this.setCalendar, this.roomFlag, this.bookingNum);
      setRoomsInfo();
      return;
    }
    this.peoplePicker.setText(String.valueOf(this.bookingNum));
  }

  private void setContactData()
  {
    this.bookingNameStr = this.bookingName.getText().toString().trim();
    this.bookingPhoneStr = this.bookingPhone.getText().toString().trim();
    if (!TextUtils.isEmpty(this.bookingNameStr))
    {
      this.bookingName.setText(this.bookingNameStr);
      this.gender = getBookingGender();
      if (this.gender != 10)
        break label123;
      ((RadioButton)this.genderView.getChildAt(0)).setChecked(true);
    }
    while (true)
    {
      if (TextUtils.isEmpty(this.bookingPhoneStr))
        break label141;
      this.bookingPhone.setText(this.bookingPhoneStr);
      return;
      this.bookingName.setText(getBookingName());
      break;
      label123: ((RadioButton)this.genderView.getChildAt(1)).setChecked(true);
    }
    label141: this.bookingPhone.setText(getPhone());
  }

  private void setGrouponBookingInfo()
  {
    long l = this.grouponBookingDataManager.defaultBookingTime.getTimeInMillis();
    this.setCalendar.setTimeInMillis(l);
    this.grouponBookingDataManager.setGrouponBookingDate(this.setCalendar, this.roomFlag, this.bookingNum);
    setRoomsInfo();
    this.dateDetailInfo.setText(formateDateDetailInfo(this.setCalendar));
    this.grouponLists = this.grouponBookingDataManager.grouponLists;
    this.grouponsView.removeAllViews();
    if ((this.grouponLists != null) && (this.grouponLists.length != 0))
    {
      ArrayList localArrayList = new ArrayList();
      int i = 0;
      if (i < this.grouponLists.length)
      {
        if ((!TextUtils.isEmpty(this.defaultDealID)) && (this.defaultDealID.equals(String.valueOf(this.grouponLists[i].getInt("DealID")))))
          localArrayList.add(0, this.grouponLists[i]);
        while (true)
        {
          i += 1;
          break;
          localArrayList.add(this.grouponLists[i]);
        }
      }
      CheckBox[] arrayOfCheckBox = new CheckBox[this.grouponLists.length];
      int j;
      label241: RelativeLayout localRelativeLayout;
      label319: TextView localTextView;
      if ((!TextUtils.isEmpty(this.defaultDealID)) && (localArrayList.size() != 1))
      {
        i = 1;
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(this, 50.0F));
        j = 0;
        if (j >= localArrayList.size())
          break label490;
        localRelativeLayout = (RelativeLayout)getLayoutInflater().inflate(R.layout.booking_groupon_list_cell, null, false);
        localRelativeLayout.setLayoutParams(localLayoutParams);
        if ((TextUtils.isEmpty(this.defaultDealID)) || (!this.defaultDealID.equals(String.valueOf(((DPObject)localArrayList.get(j)).getInt("DealID")))))
          break label445;
        k = 1;
        localTextView = (TextView)(TextView)localRelativeLayout.findViewById(R.id.has_buy);
        if (k == 0)
          break label451;
      }
      label445: label451: for (Object localObject = "[已买]"; ; localObject = "")
      {
        localTextView.setText((CharSequence)localObject);
        ((TextView)(TextView)localRelativeLayout.findViewById(R.id.groupon_name)).setText(((DPObject)localArrayList.get(j)).getString("Title"));
        arrayOfCheckBox[j] = ((CheckBox)(CheckBox)localRelativeLayout.findViewById(R.id.has_choose));
        if (j != 0)
          break label457;
        arrayOfCheckBox[j].setChecked(true);
        localRelativeLayout.setVisibility(0);
        this.grouponsView.addView(localRelativeLayout);
        j += 1;
        break label241;
        i = 0;
        break;
        k = 0;
        break label319;
      }
      label457: arrayOfCheckBox[j].setChecked(false);
      if (i != 0);
      for (int k = 8; ; k = 0)
      {
        localRelativeLayout.setVisibility(k);
        break;
      }
      label490: localObject = (RelativeLayout)findViewById(R.id.click_more);
      if (i != 0)
      {
        ((RelativeLayout)localObject).setVisibility(0);
        ((RelativeLayout)localObject).setOnClickListener(new View.OnClickListener(localArrayList, (RelativeLayout)localObject)
        {
          public void onClick(View paramView)
          {
            int i = 1;
            while (i < this.val$dealedGrouponLists.size())
            {
              GrouponBookingInfoActivity.this.grouponsView.getChildAt(i).setVisibility(0);
              i += 1;
            }
            this.val$clickMoreView.setVisibility(8);
          }
        });
      }
      while (true)
      {
        this.setDealId = ((DPObject)localArrayList.get(0)).getInt("DealID");
        i = 0;
        while (i < localArrayList.size())
        {
          this.grouponsView.getChildAt(i).setOnClickListener(new View.OnClickListener(localArrayList, arrayOfCheckBox, i)
          {
            public void onClick(View paramView)
            {
              int i = 0;
              while (i < this.val$dealedGrouponLists.size())
              {
                this.val$grouponCheckboxs[i].setChecked(false);
                i += 1;
              }
              this.val$grouponCheckboxs[this.val$finalI].setChecked(true);
              GrouponBookingInfoActivity.access$2202(GrouponBookingInfoActivity.this, ((DPObject)this.val$dealedGrouponLists.get(this.val$finalI)).getInt("DealID"));
            }
          });
          i += 1;
        }
        ((RelativeLayout)localObject).setVisibility(8);
      }
    }
  }

  private void setPeopleNumValue()
  {
    this.defaultNum = this.grouponBookingDataManager.defaultPeopleCount;
    if (this.bookingNum == 0)
      this.bookingNum = this.defaultNum;
    this.peoplePicker.setText("" + this.defaultNum);
    this.peoplePickerDialogView.setNumViewWithTip(this.grouponBookingDataManager.peopleMinCount, this.grouponBookingDataManager.peopleMaxCount);
  }

  private void setRoomsInfo()
  {
    GrouponBookingDataManager.RoomStatus localRoomStatus = this.grouponBookingDataManager.hallRoom;
    if ((this.roomFlag != 10) || (localRoomStatus == null))
      return;
    if (localRoomStatus.status == -1)
    {
      this.grouponBookingTips.setVisibility(0);
      this.grouponBookingTips.setText("当前时间餐厅不接受订座");
      return;
    }
    if (localRoomStatus.status == -2)
    {
      this.grouponBookingTips.setVisibility(0);
      this.grouponBookingTips.setText("餐厅已订满，请尝试其他时间");
      return;
    }
    this.grouponBookingTips.setVisibility(8);
  }

  private void setupViewData(int paramInt)
  {
    this.grouponBookingPushInAnimation = AnimationUtils.loadAnimation(this, R.anim.booking_push_up_in);
    this.grouponBookingPushOutAnimation = AnimationUtils.loadAnimation(this, R.anim.booking_push_up_out);
    this.grouponBookingPushInAnimation.setAnimationListener(new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
      }

      public void onAnimationRepeat(Animation paramAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnimation)
      {
      }
    });
    this.grouponBookingPushOutAnimation.setAnimationListener(new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
        GrouponBookingInfoActivity.this.grouponBookingInfoView.setVisibility(8);
      }

      public void onAnimationRepeat(Animation paramAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnimation)
      {
      }
    });
    this.grouponBookingDataManager = GrouponBookingDataManager.getInstance(this);
    this.curCalendar = Calendar.getInstance();
    this.setCalendar = Calendar.getInstance();
    this.curCalendar.setTimeInMillis(DateUtil.currentTimeMillis());
    this.setCalendar.setTimeInMillis(this.curCalendar.getTimeInMillis());
    this.dateDetailInfo.setText(formateDateDetailInfo(this.curCalendar));
    this.shopNamePickerView.setText(this.shopName);
    initPeopleNumDialog();
    setContactData();
    this.grouponBookingDataManager.setOnGrouponBookingRequestHandlerListener(new GrouponBookingDataManager.OnGrouponBookingRequestHandlerListener()
    {
      public void onFinish(boolean paramBoolean)
      {
        GrouponBookingInfoActivity.this.waitingHintHandler.removeMessages(1000);
        GrouponBookingInfoActivity.this.dismissDialog();
        GrouponBookingInfoActivity.this.setPeopleNumValue();
        if (paramBoolean)
          GrouponBookingInfoActivity.this.setGrouponBookingInfo();
        do
          return;
        while (GrouponBookingInfoActivity.this.isDestroyed);
        new AlertDialog.Builder(GrouponBookingInfoActivity.this).setMessage("抱歉，没有找到可以预订的团购套餐~").setPositiveButton("知道了", new GrouponBookingInfoActivity.13.1(this)).show().setCanceledOnTouchOutside(false);
      }

      public void onStart()
      {
        GrouponBookingInfoActivity.this.waitingHintHandler.sendEmptyMessage(1000);
      }
    });
    this.shineAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_shine);
    this.grouponBookingDataManager.getGrouponBookingConfigTask(paramInt);
  }

  private void setupViews()
  {
    this.grouponBookingShopPickerView = ((RelativeLayout)findViewById(R.id.shop_picker_view));
    this.grouponBookingShopPickerView.setOnClickListener(this);
    this.shopNamePickerView = ((TextView)findViewById(R.id.shop_name_view));
    if (!TextUtils.isEmpty(this.defaultDealGroupID))
      this.grouponBookingShopPickerView.setVisibility(0);
    while (true)
    {
      this.grouponBookingInfoView = ((RelativeLayout)findViewById(R.id.groupon_booking_info_view));
      this.grouponBookingResultView = ((LinearLayout)findViewById(R.id.groupon_booking_result_view));
      this.bookingName = ((EditText)findViewById(R.id.booking_name));
      this.bookingName.addTextChangedListener(new TextWatcher()
      {
        public void afterTextChanged(Editable paramEditable)
        {
          if ((GrouponBookingInfoActivity.this.bookingName.getText().toString().contains(" ")) && ((GrouponBookingInfoActivity.this.bookingName.getSelectionStart() > GrouponBookingInfoActivity.this.bookingName.getText().toString().trim().length()) || (GrouponBookingInfoActivity.this.bookingName.getSelectionStart() <= GrouponBookingInfoActivity.this.bookingName.getText().toString().length() - GrouponBookingInfoActivity.this.bookingName.getText().toString().trim().length())))
          {
            GrouponBookingInfoActivity.this.trimHandler.removeCallbacks(GrouponBookingInfoActivity.this.mTrimRunnable);
            GrouponBookingInfoActivity.access$1102(GrouponBookingInfoActivity.this, new TrimRunnable(GrouponBookingInfoActivity.this.bookingName));
            GrouponBookingInfoActivity.this.trimHandler.postDelayed(GrouponBookingInfoActivity.this.mTrimRunnable, 4000L);
          }
        }

        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
        {
        }

        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
        {
        }
      });
      this.bookingName.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          GrouponBookingInfoActivity.this.bookingName.setError(null);
          return false;
        }
      });
      this.grouponBookingInfo = ((ScrollView)findViewById(R.id.groupon_booking_info));
      this.grouponBookingInfo.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          paramView = (InputMethodManager)GrouponBookingInfoActivity.this.getSystemService("input_method");
          if ((paramView != null) && (GrouponBookingInfoActivity.this.getCurrentFocus() != null) && (GrouponBookingInfoActivity.this.getCurrentFocus().getWindowToken() != null))
          {
            paramView.hideSoftInputFromWindow(GrouponBookingInfoActivity.this.getCurrentFocus().getWindowToken(), 2);
            GrouponBookingInfoActivity.this.findViewById(R.id.booking_name).getParent().requestDisallowInterceptTouchEvent(false);
            GrouponBookingInfoActivity.this.findViewById(R.id.booking_phone).getParent().requestDisallowInterceptTouchEvent(false);
          }
          return false;
        }
      });
      this.dateDetailInfo = ((TextView)findViewById(R.id.date_detail_info));
      this.peoplePicker = ((TextView)findViewById(R.id.people_num));
      this.peoplePickerView = findViewById(R.id.people_picker_view);
      this.dateDetailView = findViewById(R.id.date_detail_view);
      this.dateDetailView.setOnClickListener(this);
      this.peoplePickerView.setOnClickListener(this);
      this.grouponBookingTips = ((TextView)findViewById(R.id.groupon_tips));
      this.genderView = ((RadioGroup)findViewById(R.id.gender));
      this.genderView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
      {
        public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt)
        {
          if (((RadioButton)GrouponBookingInfoActivity.this.genderView.getChildAt(0)).isChecked())
          {
            GrouponBookingInfoActivity.access$1402(GrouponBookingInfoActivity.this, 10);
            return;
          }
          GrouponBookingInfoActivity.access$1402(GrouponBookingInfoActivity.this, 20);
        }
      });
      this.bookingCountryCode = ((BookingCountryCodeView)findViewById(R.id.booking_phone_country_code));
      this.bookingCountryCode.setCurrentMode("booking");
      this.bookingCountryCode.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          GrouponBookingInfoActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=http://m.dianping.com/login/choosecountry")));
          GrouponBookingInfoActivity.this.getSharedPreferences("bookingSharedPreference", 0).edit().putString("currentModify", "booking").commit();
        }
      });
      this.bookingPhone = ((EditText)findViewById(R.id.booking_phone));
      this.bookingPhone.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          GrouponBookingInfoActivity.this.bookingPhone.setError(null);
          return false;
        }
      });
      this.grouponsView = ((LinearLayout)findViewById(R.id.groupon_lists));
      this.submitGrouponBooking = ((Button)findViewById(R.id.submit_groupon_booking));
      this.submitGrouponBooking.setOnClickListener(this);
      this.leftTitleButton.setOnClickListener(this);
      this.bookingName.setOnFocusChangeListener(new View.OnFocusChangeListener()
      {
        public void onFocusChange(View paramView, boolean paramBoolean)
        {
          if (!paramBoolean)
            GrouponBookingInfoActivity.this.bookingName.setError(null);
        }
      });
      this.bookingPhone.setOnFocusChangeListener(new View.OnFocusChangeListener()
      {
        public void onFocusChange(View paramView, boolean paramBoolean)
        {
          if (!paramBoolean)
            GrouponBookingInfoActivity.this.bookingPhone.setError(null);
        }
      });
      setupViewData(this.shopId);
      return;
      this.grouponBookingShopPickerView.setVisibility(8);
    }
  }

  private void showBookingResult(DPObject paramDPObject)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("result", paramDPObject);
    localBundle.putInt("shopId", this.shopId);
    Intent localIntent = new Intent();
    localIntent.putExtra("recordId", paramDPObject.getObject("Record").getInt("ID"));
    setResult(-1, localIntent);
    paramDPObject = new Intent();
    paramDPObject.putExtras(localBundle);
    paramDPObject.setData(Uri.parse("dianping://bookingresult"));
    startActivity(paramDPObject);
    super.finish();
  }

  private void submitGrouponBooking()
  {
    GAHelper.instance().contextStatisticsEvent(this, "book", null, 2, "tap");
    if (!checkInput());
    GrouponBookingDataManager.RoomStatus localRoomStatus;
    do
    {
      return;
      localRoomStatus = this.grouponBookingDataManager.hallRoom;
      if (localRoomStatus != null)
        continue;
      Log.e("GrouponBookingInfoActivity.submitGrouponBooking() hallRoom is null!");
      return;
    }
    while (this.roomFlag != 10);
    if (localRoomStatus.status == -1)
    {
      new AlertDialog.Builder(this).setMessage("当前时间餐厅不接受订座").setPositiveButton("确定", null).show();
      statisticsEvent("booking5", "booking5_groupon_submit_failure", "5", 0);
      return;
    }
    if (localRoomStatus.status == -2)
    {
      new AlertDialog.Builder(this).setMessage("餐厅已订满，请尝试其他时间").setPositiveButton("确定", null).show();
      statisticsEvent("booking5", "booking5_groupon_submit_failure", "4", 0);
      return;
    }
    submitGrouponBookingTask(0, null);
  }

  private void submitGrouponBookingTask(int paramInt, String paramString)
  {
    hideSoftKeyBoard();
    showProgressDialog("正在提交订单，请稍候...");
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("shopid");
    localArrayList.add(String.valueOf(this.shopId));
    localArrayList.add("shopname");
    localArrayList.add(this.shopName);
    if (accountService().token() != null)
    {
      localArrayList.add("token");
      localArrayList.add(accountService().token());
    }
    localArrayList.add("deviceid");
    localArrayList.add(Environment.imei());
    localArrayList.add("clientUUID");
    localArrayList.add(Environment.uuid());
    localArrayList.add("name");
    String str = this.bookingName.getText().toString().trim();
    paramString = str;
    if (str.length() > 30)
      paramString = str.substring(0, 30);
    localArrayList.add(paramString);
    localArrayList.add("gender");
    localArrayList.add(String.valueOf(this.gender));
    localArrayList.add("phone");
    localArrayList.add(getCountryCode() + "_" + this.bookingPhone.getText().toString().trim());
    localArrayList.add("peopleNumber");
    localArrayList.add(String.valueOf(this.bookingNum));
    localArrayList.add("positionType");
    localArrayList.add(String.valueOf(this.roomFlag));
    localArrayList.add("bookingtime");
    localArrayList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Long.valueOf(this.setCalendar.getTimeInMillis())).toString());
    localArrayList.add("forcebook");
    localArrayList.add(String.valueOf(paramInt));
    localArrayList.add("dealid");
    localArrayList.add(String.valueOf(this.setDealId));
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("booking"));
    this.grouponBookingRequest = BasicMApiRequest.mapiPost("http://rs.api.dianping.com/book.yy", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.grouponBookingRequest, this);
  }

  private void updatePeopleNum()
  {
    this.bookingNum = this.peoplePickerDialogView.getValue();
    setBookingNumText();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 100) && (paramInt2 == -1))
      submitGrouponBooking();
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.date_detail_view)
    {
      Bundle localBundle = new Bundle();
      localBundle.putInt("shopId", this.shopId);
      localBundle.putInt("bookingNum", this.bookingNum);
      localBundle.putInt("roomFlag", this.roomFlag);
      localBundle.putLong("setCalendar", this.setCalendar.getTimeInMillis());
      if (this.grouponBookingConfig == null)
      {
        paramView = this.grouponBookingDataManager.grouponBookingConfig;
        localBundle.putParcelable("bookingConfig", paramView);
        localBundle.putParcelableArray("holidaysList", this.holidaysList);
        localBundle.putInt("fromType", 1);
        gotoTimePicker(localBundle);
      }
    }
    do
    {
      return;
      paramView = this.grouponBookingConfig;
      break;
      if (i == R.id.people_picker_view)
      {
        statisticsEvent("booking6", "booking6_groupon_num", "", 0);
        if (this.bookingNum > 0)
          this.peoplePickerDialogView.setValue(this.bookingNum);
        while (true)
        {
          this.peopleNumDialog.show();
          return;
          this.peoplePickerDialogView.setValue(this.defaultNum);
        }
      }
      if (i == R.id.left_title_button)
      {
        onLeftTitleButtonClicked();
        return;
      }
      if (i != R.id.submit_groupon_booking)
        continue;
      submitGrouponBooking();
      return;
    }
    while ((i != R.id.shop_picker_view) || (this.shopPickDialog == null));
    this.shopPickDialog.show();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.online_groupon_booking);
    super.getWindow().setBackgroundDrawable(null);
    this.filter = new IntentFilter("com.dianping.booking.GROUPON_BOOKING_TIME_PICKED");
    registerReceiver(this.receiver, this.filter);
    this.shop = ((DPObject)getIntent().getExtras().getParcelable("shop"));
    this.defaultDealGroupID = super.getStringParam("dealgroupid");
    this.defaultDealID = super.getStringParam("dealid");
    if (this.shop != null)
    {
      this.shopId = this.shop.getInt("ID");
      this.shopName = this.shop.getString("Name");
    }
    while (true)
    {
      setupViews();
      getGrouponBookingHolidaysTask();
      if (!TextUtils.isEmpty(this.defaultDealGroupID))
        getGrouponBookingShopTask();
      return;
      paramBundle = super.getStringParam("shopid");
      try
      {
        this.shopId = Integer.parseInt(paramBundle);
        this.shopName = super.getStringParam("shopname");
        if (!TextUtils.isEmpty(this.shopName))
          continue;
        finish();
      }
      catch (NumberFormatException paramBundle)
      {
        while (true)
        {
          paramBundle.printStackTrace();
          finish();
        }
      }
    }
  }

  protected void onDestroy()
  {
    if (this.receiver != null)
      unregisterReceiver(this.receiver);
    if (this.grouponBookingDataManager != null)
      this.grouponBookingDataManager.abortGrouponBookingConfigTask();
    if (this.grouponBookingRequest != null)
    {
      mapiService().abort(this.grouponBookingRequest, this, true);
      this.grouponBookingRequest = null;
    }
    if (this.bookingHolidaysRequest != null)
    {
      mapiService().abort(this.bookingHolidaysRequest, this, true);
      this.bookingHolidaysRequest = null;
    }
    if (this.brandingRequest != null)
    {
      mapiService().abort(this.brandingRequest, this, true);
      this.brandingRequest = null;
    }
    if (this.grouponBookingShopReq != null)
    {
      mapiService().abort(this.grouponBookingShopReq, this, true);
      this.grouponBookingShopReq = null;
    }
    this.trimHandler.removeCallbacks(this.mTrimRunnable);
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      if (getSupportFragmentManager().getBackStackEntryCount() == 0)
      {
        statisticsEvent("booking6", "booking6_groupon_cancel", "", 0);
        finish();
      }
      while (true)
      {
        return true;
        getSupportFragmentManager().popBackStackImmediate();
      }
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onLeftTitleButtonClicked()
  {
    if (getSupportFragmentManager().getBackStackEntryCount() == 0)
    {
      statisticsEvent("booking6", "booking6_groupon_cancel", "", 0);
      if (ViewUtils.isShow(this.grouponBookingInfoView))
        hideSoftKeyBoard();
      while (true)
      {
        finish();
        return;
        setResult(-1);
      }
    }
    getSupportFragmentManager().popBackStackImmediate();
  }

  public void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    this.waitingHintHandler.removeMessages(1000);
    if (this.grouponBookingDataManager != null)
      this.grouponBookingDataManager.abortGrouponBookingConfigTask();
    finish();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.grouponBookingRequest)
    {
      dismissDialog();
      showShortToast(paramMApiResponse.message().toString());
      this.grouponBookingRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    int i;
    int j;
    if (paramMApiRequest == this.grouponBookingRequest)
    {
      dismissDialog();
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        i = paramMApiRequest.getInt("IsSuccess");
        paramMApiRequest.getBoolean("ShowAD");
        j = i / 10;
        paramMApiResponse = paramMApiRequest.getString("Message");
        if (paramMApiRequest.getInt("ValidationStatus") == 1)
        {
          statisticsEvent("booking5", "booking5_phonebind", "", 0);
          paramMApiRequest = new Intent("android.intent.action.VIEW", Uri.parse("dianping://bookingbindphone"));
          paramMApiRequest.putExtra("phone", this.bookingPhone.getText().toString());
          paramMApiRequest.putExtra("editable", false);
          paramMApiRequest.putExtra("validation", 0);
          startActivityForResult(paramMApiRequest, 100);
        }
      }
    }
    do
    {
      return;
      switch (j)
      {
      case 7:
      case 8:
      default:
      case 1:
      case 2:
      case 4:
      case 3:
      case 5:
      case 6:
      case 9:
      }
      while (true)
      {
        saveUserInfo();
        statisticsEvent("booking5", "booking5_groupon_submit", "", 0);
        this.grouponBookingRequest = null;
        return;
        switch (i % 10)
        {
        default:
          break;
        case 0:
          showBookingResult(paramMApiRequest);
          break;
        case 1:
          showBookingResult(paramMApiRequest);
          continue;
          new AlertDialog.Builder(this).setMessage(paramMApiResponse).setPositiveButton("确定", null).show();
          continue;
          new AlertDialog.Builder(this).setMessage(paramMApiResponse).setPositiveButton("是", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              GrouponBookingInfoActivity.this.submitGrouponBookingTask(1, GrouponBookingInfoActivity.this.compartmentStr);
            }
          }).setNegativeButton("否", null).show();
          continue;
          this.grouponBookingResult = paramMApiRequest;
          this.grouponBookingRecord = this.grouponBookingResult.getObject("Record");
          continue;
          this.grouponBookingResult = paramMApiRequest;
          this.grouponBookingRecord = this.grouponBookingResult.getObject("Record");
          continue;
          switch (i % 10)
          {
          default:
            break;
          case 0:
            new AlertDialog.Builder(this).setTitle("提示").setMessage(paramMApiResponse).setPositiveButton("知道了", null).show();
            statisticsEvent("booking5", "booking5_grouponuser", "", 0);
            break;
          case 1:
          case 2:
            new AlertDialog.Builder(this).setMessage(paramMApiResponse).setPositiveButton("确定", null).show();
          }
        }
      }
      if (paramMApiRequest != this.bookingHolidaysRequest)
        continue;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        this.holidaysList = ((DPObject)paramMApiResponse.result()).getArray("List");
      this.bookingHolidaysRequest = null;
      return;
    }
    while (paramMApiRequest != this.grouponBookingShopReq);
    if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      this.grouponShopList = new ArrayList();
      paramMApiRequest = ((DPObject)paramMApiResponse.result()).getArray("List");
      this.grouponShopList.addAll(Arrays.asList(paramMApiRequest));
      initShopPickDialog();
    }
    this.grouponBookingShopReq = null;
  }

  public class ShopWheelArrayAdapter extends AbstractWheelTextAdapter
  {
    private ArrayList<DPObject> lists;

    protected ShopWheelArrayAdapter(ArrayList<DPObject> arg2)
    {
      super(R.layout.booking_grouponshop_single_picker_item, 0);
      setItemTextResource(R.id.text1);
      Object localObject;
      this.lists = localObject;
    }

    @TargetApi(11)
    public View getItem(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (TextView)super.getItem(paramInt, paramView, paramViewGroup);
      if (Build.VERSION.SDK_INT >= 11)
        paramView.setAlpha(1.0F);
      return paramView;
    }

    protected CharSequence getItemText(int paramInt)
    {
      return ((DPObject)this.lists.get(paramInt)).getString("ShopName");
    }

    public int getItemsCount()
    {
      return this.lists.size();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.GrouponBookingInfoActivity
 * JD-Core Version:    0.6.0
 */