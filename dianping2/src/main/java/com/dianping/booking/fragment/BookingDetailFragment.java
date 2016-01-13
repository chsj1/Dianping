package com.dianping.booking.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.Cell;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.widget.TitleBar;
import com.dianping.booking.adapter.BookingDialogAdapter;
import com.dianping.booking.agent.BookingOrderBasicInfoAgent;
import com.dianping.booking.agent.BookingOrderBasicOperationAgent;
import com.dianping.booking.agent.BookingOrderLotteryAgent;
import com.dianping.booking.agent.BookingOrderOnlinePayAgent;
import com.dianping.booking.agent.BookingOrderPreDepositAgent;
import com.dianping.booking.agent.BookingOrderPromotionAgent;
import com.dianping.booking.agent.BookingOrderReminderAgent;
import com.dianping.booking.util.BookingRecord;
import com.dianping.booking.util.BookingShareUtil;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.SimpleMsg;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import com.dianping.widget.view.GAHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingDetailFragment extends AgentFragment
{
  private static final float ACC_THRESHOLD = 17.0F;
  private static final String BOOKING_COMPLAIN = "com.dianping.booking.BOOKING_COMPLAIN";
  private static final String BOOKING_LOTTERY = "booking:BOOKING_LOTTERY";
  private static final int REQUEST_CODE_MODIFY_RECORD = 101;
  private static final int SENSOR_SHAKE = 10;
  private Handler autoRefreshHandler = new Handler();
  private int bookingId;
  private DPObject bookingRecord;
  private MApiRequest cancelBookingRequest;
  private MApiRequest deleteBookingRequest;
  private Button failedConfirmedBtn;
  private IntentFilter filter;
  private MApiRequest getBookingRecordRequest;
  private boolean hasRegistered = false;
  private boolean isReminded;
  private boolean isWorkTime;
  private int lastStatus;
  private View loadingLayout;
  private MApiRequest modifyCheckRequest;
  private MApiRequest queryWeatherRequest;
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.booking.BOOKING_COMPLAIN".equals(paramIntent.getAction()))
      {
        BookingDetailFragment.access$002(BookingDetailFragment.this, (DPObject)paramIntent.getExtras().getParcelable("bookingRecord"));
        if (BookingDetailFragment.this.bookingRecord != null)
          BookingDetailFragment.this.onBookingRecordUpdated();
      }
      do
        return;
      while (!"booking:BOOKING_LOTTERY".equals(paramIntent.getAction()));
      BookingDetailFragment.this.getBookingRecordTask(Environment.uuid(), BookingDetailFragment.this.serializedId);
    }
  };
  private MApiRequest remindBookingRequest;
  private Dialog reminderFailedDialog;
  private View reminderFailedDialogView;
  private Dialog reminderSuccessDialog;
  private View reminderSuccessDialogView;
  private final Runnable runnable = new Runnable()
  {
    public void run()
    {
      BookingDetailFragment.this.getBookingRecordTask(Environment.uuid(), BookingDetailFragment.this.serializedId);
      BookingDetailFragment.this.autoRefresh();
    }
  };
  private SensorEventListener sensorEventListener = new SensorEventListener()
  {
    public void onAccuracyChanged(Sensor paramSensor, int paramInt)
    {
    }

    public void onSensorChanged(SensorEvent paramSensorEvent)
    {
      if ((!BookingDetailFragment.this.isWorkTime) || (BookingDetailFragment.this.isReminded));
      float f1;
      float f2;
      float f3;
      do
      {
        do
          return;
        while (BookingDetailFragment.this.showingAlertDialog);
        f1 = paramSensorEvent.values[0];
        f2 = paramSensorEvent.values[1];
        f3 = paramSensorEvent.values[2];
      }
      while ((Math.abs(f1) <= 17.0F) && (Math.abs(f2) <= 17.0F) && (Math.abs(f3) <= 17.0F));
      paramSensorEvent = new Bundle();
      paramSensorEvent.putInt("type", 102);
      paramSensorEvent.putInt("mode", 10);
      BookingDetailFragment.this.dispatchAgentChanged("booking_detail/reminder", paramSensorEvent);
    }
  };
  private SensorManager sensorManager;
  private String serializedId;
  private boolean showingAlertDialog = false;
  private int status;
  private Button successConfirmBtn;
  private PullToRefreshScrollView wholeLayout;

  private void autoRefresh()
  {
    if ((this.bookingRecord != null) && (this.bookingRecord.getBoolean("NeedAutoRefresh")))
    {
      this.autoRefreshHandler.postDelayed(this.runnable, this.bookingRecord.getInt("AutoRefreshInterval") * 1000);
      return;
    }
    this.autoRefreshHandler.removeCallbacks(this.runnable);
  }

  private void cancelBookingTask(int paramInt, String paramString1, String paramString2, DPObject paramDPObject)
  {
    if (this.cancelBookingRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("bookingRecordID");
    localArrayList.add(String.valueOf(paramInt));
    localArrayList.add("clientUUID");
    localArrayList.add(paramString1);
    localArrayList.add("token");
    localArrayList.add(paramString2);
    if (paramDPObject != null)
    {
      localArrayList.add("cancelfree");
      localArrayList.add(String.valueOf(paramDPObject.getInt("CancelFree")));
    }
    this.cancelBookingRequest = BasicMApiRequest.mapiPost("http://rs.api.dianping.com/cancelbooking.yy", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.cancelBookingRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        BookingDetailFragment.this.dismissDialog();
        Toast.makeText(BookingDetailFragment.this.getActivity(), "订单取消失败", 0).show();
        BookingDetailFragment.access$1502(BookingDetailFragment.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        BookingDetailFragment.this.dismissDialog();
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest.getInt("ResultCode") == 10)
          {
            Toast.makeText(BookingDetailFragment.this.getActivity(), "订单取消成功", 0).show();
            BookingDetailFragment.this.updateBookingRecord(paramMApiRequest.getObject("BookingRecord"));
          }
        }
        while (true)
        {
          BookingDetailFragment.access$1502(BookingDetailFragment.this, null);
          return;
          if (paramMApiRequest.getInt("ResultCode") == 20)
          {
            new AlertDialog.Builder(BookingDetailFragment.this.getActivity()).setTitle(paramMApiRequest.getString("Title")).setMessage(paramMApiRequest.getString("Content")).setPositiveButton("确定", new BookingDetailFragment.11.1(this)).show();
            continue;
          }
          Toast.makeText(BookingDetailFragment.this.getActivity(), "订单取消失败", 0).show();
          continue;
          Toast.makeText(BookingDetailFragment.this.getActivity(), "订单取消失败", 0).show();
        }
      }
    });
  }

  private void deleteBookingRecordTask(String paramString1, String paramString2, int paramInt)
  {
    if (this.deleteBookingRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    if (!TextUtils.isEmpty(paramString1))
    {
      localArrayList.add("token");
      localArrayList.add(paramString1);
    }
    localArrayList.add("clientUUID");
    localArrayList.add(paramString2);
    localArrayList.add("bookingRecordID");
    localArrayList.add(String.valueOf(paramInt));
    this.deleteBookingRequest = BasicMApiRequest.mapiPost("http://rs.api.dianping.com/deletebookingrecord.yy", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.deleteBookingRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        BookingDetailFragment.this.dismissDialog();
        Toast.makeText(BookingDetailFragment.this.getActivity(), "订单删除失败", 0).show();
        BookingDetailFragment.access$1702(BookingDetailFragment.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        BookingDetailFragment.this.dismissDialog();
        paramMApiRequest = new Intent("com.dianping.orderlist.removeitem");
        paramMApiRequest.putExtra("bookingId", BookingDetailFragment.this.bookingId);
        BookingDetailFragment.this.getActivity().sendBroadcast(paramMApiRequest);
        BookingDetailFragment.this.getActivity().finish();
        BookingDetailFragment.access$1702(BookingDetailFragment.this, null);
      }
    });
  }

  private int getTipOccurrenceNum()
  {
    return getActivity().getSharedPreferences("bookingdetail", 0).getInt("tipOccurrenceNum", 0);
  }

  private String getToken()
  {
    if (accountService() == null)
      return "";
    return accountService().token();
  }

  private void modifyCheckTask(String paramString)
  {
    if (this.modifyCheckRequest != null)
      return;
    this.modifyCheckRequest = BasicMApiRequest.mapiGet(String.format("%sphone=%s", new Object[] { "http://rs.api.dianping.com/modifycheck.yy?", paramString }), CacheType.DISABLED);
    mapiService().exec(this.modifyCheckRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.error() instanceof SimpleMsg)))
        {
          paramMApiRequest = ((SimpleMsg)paramMApiResponse.error()).content();
          if (!TextUtils.isEmpty(paramMApiRequest))
            new AlertDialog.Builder(BookingDetailFragment.this.getActivity()).setMessage(paramMApiRequest).setPositiveButton("知道了", null).show().setCanceledOnTouchOutside(true);
        }
        while (true)
        {
          BookingDetailFragment.access$1802(BookingDetailFragment.this, null);
          return;
          Toast.makeText(BookingDetailFragment.this.getActivity(), "网络不给力，再试试吧", 0).show();
        }
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiResponse != null)
        {
          paramMApiRequest = new Intent("android.intent.action.VIEW", Uri.parse("dianping://onlinebooking?shopid=" + BookingDetailFragment.this.bookingRecord.getInt("ShopID") + "&shopname=" + BookingDetailFragment.this.bookingRecord.getString("ShopName")));
          paramMApiRequest.putExtra("bookingRecord", BookingDetailFragment.this.bookingRecord);
          BookingDetailFragment.this.startActivityForResult(paramMApiRequest, 101);
          BookingDetailFragment.this.statisticsEvent("mybooking6", "mybooking6_orderssucceed_modify", "", 0);
        }
        BookingDetailFragment.access$1802(BookingDetailFragment.this, null);
      }
    });
  }

  private void onBookingRecordUpdated()
  {
    boolean bool2 = false;
    Object localObject;
    if (this.bookingRecord != null)
    {
      this.bookingId = this.bookingRecord.getInt("ID");
      this.serializedId = this.bookingRecord.getString("SerialNumber");
      this.status = this.bookingRecord.getInt("Status");
      localObject = this.bookingRecord.getObject("BookingRecordFlowList");
      if ((localObject == null) || (!((DPObject)localObject).getBoolean("ShouldShowFlow")))
        break label265;
    }
    label265: for (boolean bool1 = true; ; bool1 = false)
    {
      this.isWorkTime = bool1;
      bool1 = bool2;
      if (localObject != null)
      {
        bool1 = bool2;
        if (((DPObject)localObject).getBoolean("IsReminded"))
          bool1 = true;
      }
      this.isReminded = bool1;
      localObject = new Bundle();
      ((Bundle)localObject).putInt("type", 100);
      ((Bundle)localObject).putParcelable("record", this.bookingRecord);
      ((Bundle)localObject).putBoolean("isBookingCity", city().isBookingCity());
      dispatchAgentChanged(null, (Bundle)localObject);
      setupView(this.bookingRecord);
      if ((this.status / 10 == 1) || (this.status / 10 == 2))
        queryWeatherTask(this.bookingRecord.getTime("BookingTime"), this.bookingRecord.getInt("ShopID"));
      if ((!this.hasRegistered) && (this.sensorManager != null) && (this.isWorkTime) && (!this.isReminded))
      {
        this.sensorManager.registerListener(this.sensorEventListener, this.sensorManager.getDefaultSensor(1), 3);
        this.hasRegistered = true;
      }
      return;
    }
  }

  private void queryWeatherTask(long paramLong, int paramInt)
  {
    if (this.queryWeatherRequest != null)
      return;
    this.queryWeatherRequest = BasicMApiRequest.mapiGet(String.format("%sdate=%s&shopid=%s", new Object[] { "http://rs.api.dianping.com/queryweather.yy?", Long.valueOf(paramLong), Integer.valueOf(paramInt) }), CacheType.DISABLED);
    mapiService().exec(this.queryWeatherRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        BookingDetailFragment.access$1402(BookingDetailFragment.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = new Bundle();
          paramMApiRequest.putInt("type", 108);
          paramMApiRequest.putParcelable("weatherinfo", (DPObject)paramMApiResponse.result());
          BookingDetailFragment.this.dispatchAgentChanged("booking_detail/info", paramMApiRequest);
        }
        BookingDetailFragment.access$1402(BookingDetailFragment.this, null);
      }
    });
  }

  private void remindBookingRecordTask(String paramString1, String paramString2, String paramString3)
  {
    if (this.remindBookingRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("serializedid");
    localArrayList.add(paramString3);
    localArrayList.add("clientUUID");
    localArrayList.add(paramString2);
    localArrayList.add("token");
    localArrayList.add(paramString1);
    this.remindBookingRequest = BasicMApiRequest.mapiPost("http://rs.api.dianping.com/remindBookingRecord.yy", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.remindBookingRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        paramMApiRequest = new AlertDialog.Builder(BookingDetailFragment.this.getActivity()).setMessage("遇到问题啦，请下拉刷新试试").setPositiveButton("知道了", null).show();
        BookingDetailFragment.access$602(BookingDetailFragment.this, true);
        paramMApiRequest.setCanceledOnTouchOutside(true);
        paramMApiRequest.setOnDismissListener(new BookingDetailFragment.14.1(this));
        BookingDetailFragment.access$2102(BookingDetailFragment.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          if (BookingDetailFragment.this.sensorManager != null)
          {
            BookingDetailFragment.this.sensorManager.unregisterListener(BookingDetailFragment.this.sensorEventListener);
            BookingDetailFragment.access$1902(BookingDetailFragment.this, null);
          }
          BookingDetailFragment.this.updateBookingRecord((DPObject)paramMApiResponse.result());
        }
        BookingDetailFragment.access$2102(BookingDetailFragment.this, null);
      }
    });
  }

  private void saveTipOccurrenceNum()
  {
    SharedPreferences.Editor localEditor = getActivity().getSharedPreferences("bookingdetail", 0).edit();
    localEditor.putInt("tipOccurrenceNum", getTipOccurrenceNum() + 1);
    localEditor.commit();
  }

  private void setupView(DPObject paramDPObject)
  {
    this.wholeLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
      {
        BookingDetailFragment.this.getBookingRecordTask(Environment.uuid(), BookingDetailFragment.this.serializedId);
      }
    });
    if (this.lastStatus / 10 == 1)
    {
      if (this.status / 10 == 2)
      {
        showReminderSuccessDialog();
        statisticsEvent("mybooking6", "mybooking6_orderwait_orderssucceed", "", 0);
      }
    }
    else
    {
      if ((!paramDPObject.getBoolean("IsActive")) || (this.status / 10 != 2))
        break label186;
      GAHelper.instance().contextStatisticsEvent(getActivity(), "friends", null, "view");
      super.getTitleBar().addRightViewItem("friends", R.drawable.ic_action_share, new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          BookingDetailFragment.this.showShareBookingInfoToFriendsDialog(new BookingRecord(this.val$bookingRecord));
          BookingDetailFragment.this.statisticsEvent("mybooking6", "mybooking6_orderssucceed_informfriends", "", 0);
        }
      });
      if (getTipOccurrenceNum() < 1)
      {
        showGuideInfoView();
        saveTipOccurrenceNum();
      }
    }
    while (true)
    {
      GAHelper.instance().contextStatisticsEvent(getActivity(), "pageview", null, this.status, "view");
      return;
      if (this.status / 10 != 3)
        break;
      showReminderFailedDialog();
      statisticsEvent("mybooking6", "mybooking6_orderwait_failed", "", 0);
      break;
      label186: if (super.getTitleBar() == null)
        continue;
      super.getTitleBar().removeRightViewItem("share");
    }
  }

  private void showGuideInfoView()
  {
    Dialog localDialog = new Dialog(getActivity(), R.style.dialog);
    View localView = LayoutInflater.from(getActivity()).inflate(R.layout.booking_detail_guide, null);
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(ViewUtils.getScreenWidthPixels(getActivity()), ViewUtils.getScreenHeightPixels(getActivity()));
    localView.setOnTouchListener(new View.OnTouchListener(localDialog)
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (this.val$guideInfoDlg.isShowing())
          this.val$guideInfoDlg.dismiss();
        return false;
      }
    });
    localDialog.setContentView(localView, localLayoutParams);
    localDialog.setCancelable(true);
    localDialog.show();
  }

  private void showReminderFailedDialog()
  {
    this.reminderFailedDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.booking_detail_reminder_result_failed, null, false);
    this.failedConfirmedBtn = ((Button)this.reminderFailedDialogView.findViewById(R.id.reminder_failed_reselect_btn));
    this.failedConfirmedBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingDetailFragment.this.reminderFailedDialog.dismiss();
        if (BookingDetailFragment.this.city().isBookingCity());
        for (paramView = "dianping://bookingshoplist"; ; paramView = "dianping://home")
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
          paramView.setFlags(67108864);
          BookingDetailFragment.this.startActivity(paramView);
          BookingDetailFragment.this.statisticsEvent("mybooking6", "mybooking6_orderwait_failed_reselectshop", "", 0);
          return;
        }
      }
    });
    this.reminderFailedDialog = new Dialog(getActivity(), R.style.dialog);
    this.reminderFailedDialog.setContentView(this.reminderFailedDialogView);
    this.reminderFailedDialog.setCanceledOnTouchOutside(true);
    this.reminderFailedDialog.show();
  }

  private void showReminderSuccessDialog()
  {
    this.reminderSuccessDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.booking_detail_reminder_result_success, null, false);
    this.successConfirmBtn = ((Button)this.reminderSuccessDialogView.findViewById(R.id.reminder_success_confirm_btn));
    this.successConfirmBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingDetailFragment.this.reminderSuccessDialog.dismiss();
      }
    });
    this.reminderSuccessDialog = new Dialog(getActivity(), R.style.dialog);
    this.reminderSuccessDialog.setContentView(this.reminderSuccessDialogView);
    this.reminderSuccessDialog.setCanceledOnTouchOutside(true);
    this.reminderSuccessDialog.show();
  }

  private void showShareBookingInfoToFriendsDialog(BookingRecord paramBookingRecord)
  {
    Object localObject = new ArrayList();
    ((ArrayList)localObject).add("发送短信");
    ((ArrayList)localObject).add("发给微信好友");
    ((ArrayList)localObject).add("发送邮件");
    localObject = new BookingDialogAdapter(getActivity(), (ArrayList)localObject);
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    localBuilder.setTitle("发给好友").setAdapter((ListAdapter)localObject, new DialogInterface.OnClickListener(paramBookingRecord)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface = new SimpleDateFormat("MM月dd日 E HH:mm", Locale.getDefault());
        ShareHolder localShareHolder = new ShareHolder();
        switch (paramInt)
        {
        default:
          return;
        case 0:
          localObject = "吃饭地点订好啦!【" + this.val$bookingRecord.shopName + "】" + paramDialogInterface.format(Long.valueOf(this.val$bookingRecord.bookingTime)) + " " + this.val$bookingRecord.peopleNum + "人座位。地址:" + this.val$bookingRecord.shopAddress;
          paramDialogInterface = (DialogInterface)localObject;
          if (!TextUtils.isEmpty(this.val$bookingRecord.shopContact))
            paramDialogInterface = (String)localObject + ",电话:" + this.val$bookingRecord.shopContact;
          localShareHolder.desc = paramDialogInterface;
          BookingShareUtil.shareToSms(BookingDetailFragment.this.getActivity(), localShareHolder);
          BookingDetailFragment.this.statisticsEvent("mybooking6", "mybooking6_orderssucceed_informfriends_submit", "sms", 0);
          return;
        case 1:
          paramDialogInterface = "吃饭地点订好啦!" + this.val$bookingRecord.shopName + paramDialogInterface.format(Long.valueOf(this.val$bookingRecord.bookingTime)) + "," + this.val$bookingRecord.peopleNum + "人座位";
          BookingShareUtil.shareToWX(BookingDetailFragment.this.getActivity(), "大众点评餐厅订座", paramDialogInterface, R.drawable.booking_icon_feed, "http://m.api.dianping.com/weixinshop?shopid=" + this.val$bookingRecord.shopId);
          BookingDetailFragment.this.statisticsEvent("mybooking6", "mybooking6_orderssucceed_informfriends_submit", "weixin", 0);
          return;
        case 2:
        }
        paramDialogInterface = new SimpleDateFormat("yyyy-MM-dd E HH:mm", Locale.getDefault());
        Object localObject = "Hi!\n我订了" + paramDialogInterface.format(Long.valueOf(this.val$bookingRecord.bookingTime)) + this.val$bookingRecord.shopName + "的座位,欢迎届时光临!\n地址:" + this.val$bookingRecord.shopAddress + "\n";
        paramDialogInterface = (DialogInterface)localObject;
        if (!TextUtils.isEmpty(this.val$bookingRecord.shopContact))
          paramDialogInterface = (String)localObject + "联系电话:" + this.val$bookingRecord.shopContact + "\n";
        localObject = paramDialogInterface;
        if (!TextUtils.isEmpty(this.val$bookingRecord.shopUrl))
          localObject = paramDialogInterface + this.val$bookingRecord.shopUrl;
        localShareHolder.title = ("我订了" + this.val$bookingRecord.shopName + "的座位，一定要来哦~");
        localShareHolder.desc = ((String)localObject);
        BookingShareUtil.shareToEmail(BookingDetailFragment.this.getActivity(), localShareHolder);
        BookingDetailFragment.this.statisticsEvent("mybooking6", "mybooking6_orderssucceed_informfriends_submit", "email", 0);
      }
    });
    paramBookingRecord = localBuilder.create();
    paramBookingRecord.setCanceledOnTouchOutside(true);
    paramBookingRecord.show();
  }

  private void updateBookingRecord(DPObject paramDPObject)
  {
    if (paramDPObject != null)
    {
      int i = this.bookingId;
      this.lastStatus = this.status;
      this.bookingRecord = paramDPObject;
      onBookingRecordUpdated();
      paramDPObject = new Intent("com.dianping.booking.BOOKING_DETAIL_REFRESH");
      Bundle localBundle = new Bundle();
      localBundle.putParcelable("bookingRecord", this.bookingRecord);
      localBundle.putInt("replaceId", i);
      paramDPObject.putExtras(localBundle);
      getActivity().sendBroadcast(paramDPObject);
    }
  }

  protected void addCellToContainerView(String paramString, Cell paramCell)
  {
    ((ViewGroup)agentContainerView().findViewById(R.id.agent_container_layout)).addView(paramCell.view);
  }

  public void dispatchCellChanged(CellAgent paramCellAgent, Bundle paramBundle)
  {
    super.dispatchCellChanged(paramCellAgent, paramBundle);
    if (paramBundle != null);
    switch (paramBundle.getInt("type"))
    {
    case 102:
    case 103:
    case 104:
    default:
      return;
    case 101:
      remindBookingRecordTask(getToken(), Environment.uuid(), this.serializedId);
      return;
    case 105:
      modifyCheckTask(paramBundle.getString("phone"));
      return;
    case 106:
      deleteBookingRecordTask(getToken(), Environment.uuid(), this.bookingId);
      showProgressDialog("正在删除订单，请稍候...");
      return;
    case 107:
    }
    cancelBookingTask(this.bookingId, Environment.uuid(), getToken(), this.bookingRecord.getObject("PrepayInfo"));
    showProgressDialog("正在取消订单，请稍候...");
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new AgentListConfig()
    {
      public Map<String, AgentInfo> getAgentInfoList()
      {
        return null;
      }

      public Map<String, Class<? extends CellAgent>> getAgentList()
      {
        HashMap localHashMap = new HashMap();
        localHashMap.put("booking_detail/info", BookingOrderBasicInfoAgent.class);
        localHashMap.put("booking_detail/predeposit", BookingOrderPreDepositAgent.class);
        localHashMap.put("booking_detail/reminder", BookingOrderReminderAgent.class);
        localHashMap.put("booking_detail/onlinepay", BookingOrderOnlinePayAgent.class);
        localHashMap.put("booking_detail/promotion", BookingOrderPromotionAgent.class);
        localHashMap.put("booking_detail/lottery", BookingOrderLotteryAgent.class);
        localHashMap.put("booking_detail/operation", BookingOrderBasicOperationAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  protected void getBookingRecordTask(String paramString1, String paramString2)
  {
    if (this.getBookingRecordRequest != null)
      return;
    this.getBookingRecordRequest = BasicMApiRequest.mapiGet(String.format("%sclientUUID=%s&serializedid=%s", new Object[] { "http://rs.api.dianping.com/loadbooking.yy?", paramString1, paramString2 }), CacheType.DISABLED);
    mapiService().exec(this.getBookingRecordRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        BookingDetailFragment.this.dismissProgressDialog();
        BookingDetailFragment.this.wholeLayout.onRefreshComplete();
        BookingDetailFragment.this.loadingLayout.setVisibility(8);
        Toast.makeText(BookingDetailFragment.this.getActivity(), "订单信息获取失败", 0).show();
        BookingDetailFragment.access$1302(BookingDetailFragment.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        BookingDetailFragment.this.dismissProgressDialog();
        if (BookingDetailFragment.this.wholeLayout != null)
          BookingDetailFragment.this.wholeLayout.onRefreshComplete();
        BookingDetailFragment.this.loadingLayout.setVisibility(8);
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
          BookingDetailFragment.this.updateBookingRecord((DPObject)paramMApiResponse.result());
        BookingDetailFragment.access$1302(BookingDetailFragment.this, null);
      }
    });
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.sensorManager = ((SensorManager)getActivity().getSystemService("sensor"));
    paramBundle = getActivity().getIntent();
    if ((paramBundle.getExtras() != null) && (paramBundle.getExtras().getParcelable("bookingRecord") != null))
    {
      this.bookingRecord = ((DPObject)paramBundle.getExtras().getParcelable("bookingRecord"));
      onBookingRecordUpdated();
    }
    while (true)
    {
      paramBundle = ((DPActivity)getActivity()).getStringParam("push");
      if (!TextUtils.isEmpty(paramBundle))
        statisticsEvent("mybooking6", "mybooking6_order_push", paramBundle, 0);
      return;
      paramBundle = ((DPActivity)getActivity()).getStringParam("serializedid");
      if (TextUtils.isEmpty(paramBundle))
        continue;
      this.serializedId = paramBundle;
      this.loadingLayout.setVisibility(0);
      getBookingRecordTask(Environment.uuid(), this.serializedId);
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 101)
    {
      if (paramInt2 == -1)
      {
        super.showProgressDialog("正在更新订单信息");
        getBookingRecordTask(Environment.uuid(), this.serializedId);
      }
      return;
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getActivity().getWindow().setBackgroundDrawable(null);
    setHost("booking_detail");
    this.filter = new IntentFilter();
    this.filter.addAction("com.dianping.booking.BOOKING_COMPLAIN");
    this.filter.addAction("booking:BOOKING_LOTTERY");
    registerReceiver(this.receiver, this.filter);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = (ViewGroup)paramLayoutInflater.inflate(R.layout.booking_detail_layout, paramViewGroup, false);
    setAgentContainerView(paramLayoutInflater);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.receiver != null)
      super.unregisterReceiver(this.receiver);
    if (this.sensorManager != null)
    {
      this.sensorManager.unregisterListener(this.sensorEventListener);
      this.sensorManager = null;
    }
    if (this.getBookingRecordRequest != null)
    {
      mapiService().abort(this.getBookingRecordRequest, null, true);
      this.getBookingRecordRequest = null;
    }
    if (this.queryWeatherRequest != null)
    {
      mapiService().abort(this.queryWeatherRequest, null, true);
      this.queryWeatherRequest = null;
    }
    if (this.cancelBookingRequest != null)
    {
      mapiService().abort(this.cancelBookingRequest, null, true);
      this.cancelBookingRequest = null;
    }
    if (this.deleteBookingRequest != null)
    {
      mapiService().abort(this.deleteBookingRequest, null, true);
      this.deleteBookingRequest = null;
    }
    if (this.modifyCheckRequest != null)
    {
      mapiService().abort(this.modifyCheckRequest, null, true);
      this.modifyCheckRequest = null;
    }
    if (this.remindBookingRequest != null)
    {
      mapiService().abort(this.remindBookingRequest, null, true);
      this.remindBookingRequest = null;
    }
  }

  public void onPause()
  {
    super.onPause();
    this.autoRefreshHandler.removeCallbacks(this.runnable);
  }

  public void onResume()
  {
    super.onResume();
    if ((this.bookingRecord != null) && (this.bookingRecord.getBoolean("NeedRequestMore")))
      getBookingRecordTask(Environment.uuid(), this.serializedId);
    autoRefresh();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.loadingLayout = paramView.findViewById(R.id.loading_data);
    this.wholeLayout = ((PullToRefreshScrollView)paramView.findViewById(R.id.whole_layout));
  }

  protected void resetAgentContainerView()
  {
    ((ViewGroup)agentContainerView().findViewById(R.id.agent_container_layout)).removeAllViews();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.fragment.BookingDetailFragment
 * JD-Core Version:    0.6.0
 */