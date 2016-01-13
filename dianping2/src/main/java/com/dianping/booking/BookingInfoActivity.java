package com.dianping.booking;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.booking.fragment.BookingInfoFragment;
import com.dianping.booking.fragment.BookingTimePickerFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.view.GAUserInfo;

public class BookingInfoActivity extends AgentActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static final int REQ_CODE_PREPAY = 2;
  private MApiRequest bookingHolidaysRequest;
  private BookingInfoFragment bookingInfoFragment = new BookingInfoFragment();
  private DPObject bookingRecord;
  private MApiRequest getBookingConfigRequest;
  private MApiRequest getNewUserTicketInfoRequest;
  public DPObject[] holidaysList;
  private BroadcastReceiver killSelfReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ((paramIntent.getAction() != null) && ("finish_booking_on_line_activity".equals(paramIntent.getAction())))
        BookingInfoActivity.this.finish();
    }
  };
  public int ordersource;
  public DPObject shop;
  public int shopId;
  public String shopName;
  private Dialog ticketPopUpDialog;
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
      if ((BookingInfoActivity.this.managedDialog instanceof ProgressDialog))
      {
        paramMessage = (ProgressDialog)BookingInfoActivity.this.managedDialog;
        arrayOfString = this.messages;
        i = this.index;
        this.index = (i + 1);
        paramMessage.setMessage(arrayOfString[(i % this.messages.length)]);
      }
      while (true)
      {
        sendEmptyMessageDelayed(1000, 2000L);
        return;
        paramMessage = BookingInfoActivity.this;
        arrayOfString = this.messages;
        i = this.index;
        this.index = (i + 1);
        paramMessage.showProgressDialog(arrayOfString[(i % this.messages.length)]);
        if (BookingInfoActivity.this.managedDialog == null)
          continue;
        BookingInfoActivity.this.managedDialog.setCanceledOnTouchOutside(false);
      }
    }
  };

  private void exitConfirm()
  {
    if (getSupportFragmentManager().getBackStackEntryCount() == 0)
    {
      finish();
      if (!isModifyRecord())
        statisticsEvent("booking5", "booking5_submit_shopinfo", "", 0);
    }
    View localView;
    do
    {
      return;
      getSupportFragmentManager().popBackStackImmediate();
      localView = super.getTitleBar().findRightViewItemByTag("groupon");
    }
    while (localView == null);
    localView.setVisibility(0);
  }

  private void getBookingConfigFailed()
  {
    new AlertDialog.Builder(this).setMessage("网络开小差啦，请检查网络，重新进入试试~").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        BookingInfoActivity.this.finish();
      }
    }).show();
  }

  private void getBookingHolidaysTask()
  {
    if (this.bookingHolidaysRequest != null)
      return;
    this.bookingHolidaysRequest = BasicMApiRequest.mapiGet("http://rs.api.dianping.com/getbookingholidays.yy", CacheType.NORMAL);
    mapiService().exec(this.bookingHolidaysRequest, this);
  }

  private void getNewUserTicketInfoTask(String paramString, int paramInt)
  {
    if (this.getNewUserTicketInfoRequest != null)
      return;
    this.getNewUserTicketInfoRequest = BasicMApiRequest.mapiGet(String.format("http://rs.api.dianping.com/genticket4userbyshopid.yy?token=%s&shopID=%s", new Object[] { paramString, Integer.valueOf(paramInt) }), CacheType.DISABLED);
    mapiService().exec(this.getNewUserTicketInfoRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        BookingInfoActivity.access$502(BookingInfoActivity.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest != null)
          {
            BookingInfoActivity.this.setupTicketInfoData(paramMApiRequest);
            BookingInfoActivity.this.statisticsEvent("booking6", "booking6_fanpiao_fornewuser", "bookinginfo", 0);
          }
        }
        BookingInfoActivity.access$502(BookingInfoActivity.this, null);
      }
    });
  }

  private boolean isModifyRecord()
  {
    return this.bookingRecord != null;
  }

  private void setupTicketInfoData(DPObject paramDPObject)
  {
    View localView = LayoutInflater.from(this).inflate(R.layout.booking_channel_newuser_ticket, null, false);
    Object localObject = (TextView)localView.findViewById(R.id.ticket_price);
    TextView localTextView1 = (TextView)localView.findViewById(R.id.ticket_use_rule);
    TextView localTextView2 = (TextView)localView.findViewById(R.id.congratulation_text1);
    TextView localTextView3 = (TextView)localView.findViewById(R.id.congratulation_text2);
    TextView localTextView4 = (TextView)localView.findViewById(R.id.congratulation_text3);
    Button localButton = (Button)localView.findViewById(R.id.ticket_known);
    this.ticketPopUpDialog = new Dialog(this, R.style.dialog);
    this.ticketPopUpDialog.setContentView(localView);
    ((TextView)localObject).setText(Integer.toString(paramDPObject.getInt("Deduce")));
    localTextView1.setText(paramDPObject.getString("UnderSectionMessage"));
    localTextView2.setText(paramDPObject.getString("Message"));
    localTextView3.setText(paramDPObject.getString("SubMessage"));
    localTextView4.setText(paramDPObject.getString("ThirdMessage"));
    paramDPObject = this.ticketPopUpDialog.getWindow();
    localObject = paramDPObject.getAttributes();
    ((WindowManager.LayoutParams)localObject).dimAmount = 0.8F;
    paramDPObject.setAttributes((WindowManager.LayoutParams)localObject);
    paramDPObject.setFlags(2, 2);
    this.ticketPopUpDialog.show();
    localView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.booking_channel_ticket_popup));
    localButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingInfoActivity.this.ticketPopUpDialog.dismiss();
      }
    });
    this.ticketPopUpDialog.setCanceledOnTouchOutside(false);
  }

  protected AgentFragment getAgentFragment()
  {
    return this.bookingInfoFragment;
  }

  public void getBookingConfigTask()
  {
    this.waitingHintHandler.sendEmptyMessage(1000);
    if (this.getBookingConfigRequest != null)
      return;
    this.getBookingConfigRequest = BasicMApiRequest.mapiGet("http://rs.api.dianping.com/getbookingconfig.yy?shopID=" + this.shopId, CacheType.DISABLED);
    mapiService().exec(this.getBookingConfigRequest, this);
  }

  public String getPageName()
  {
    return "onlinebooking";
  }

  public void gotoBookingInfo()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.show(this.bookingInfoFragment);
    localFragmentTransaction.commit();
  }

  public void gotoBookingPrepay(Bundle paramBundle)
  {
    Intent localIntent = new Intent();
    localIntent.setData(Uri.parse("dianping://bookingprepaycashier"));
    localIntent.putExtras(paramBundle);
    startActivityForResult(localIntent, 2);
  }

  public void gotoBookingResult(Bundle paramBundle)
  {
    Intent localIntent = new Intent();
    localIntent.putExtras(paramBundle);
    localIntent.setData(Uri.parse("dianping://bookingresult"));
    startActivity(localIntent);
    overridePendingTransition(R.anim.booking_push_up_in, R.anim.booking_push_up_out);
    super.finish();
  }

  public void gotoTimePicker(Bundle paramBundle)
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    BookingTimePickerFragment localBookingTimePickerFragment = new BookingTimePickerFragment();
    localBookingTimePickerFragment.setArguments(paramBundle);
    localFragmentTransaction.hide(this.bookingInfoFragment);
    localFragmentTransaction.add(16908300, localBookingTimePickerFragment);
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.commit();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 2) && (paramInt2 == -1))
      gotoBookingResult(paramIntent.getExtras());
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().setBackgroundDrawable(null);
    paramBundle = getIntent().getExtras();
    this.ordersource = super.getIntParam("ordersource", -1);
    this.bookingRecord = ((DPObject)paramBundle.getParcelable("bookingRecord"));
    this.shop = ((DPObject)paramBundle.getParcelable("shop"));
    if (this.shop != null)
      this.shopId = this.shop.getInt("ID");
    for (this.shopName = this.shop.getString("Name"); ; this.shopName = super.getStringParam("shopname"))
    {
      if ((this.shopId < 0) || (TextUtils.isEmpty(this.shopName)))
        finish();
      if (paramBundle.getParcelable("config") == null)
        getBookingConfigTask();
      getBookingHolidaysTask();
      if ((getAccount() != null) && (getAccount().token() != null))
        getNewUserTicketInfoTask(getAccount().token(), this.shopId);
      registerReceiver(this.killSelfReceiver, new IntentFilter("finish_booking_on_line_activity"));
      return;
      this.shopId = super.getIntParam("shopid", -1);
    }
  }

  protected void onDestroy()
  {
    if (this.getBookingConfigRequest != null)
    {
      mapiService().abort(this.getBookingConfigRequest, this, true);
      this.getBookingConfigRequest = null;
    }
    if (this.bookingHolidaysRequest != null)
    {
      mapiService().abort(this.bookingHolidaysRequest, this, true);
      this.bookingHolidaysRequest = null;
    }
    if (this.getNewUserTicketInfoRequest != null)
    {
      mapiService().abort(this.getNewUserTicketInfoRequest, this, true);
      this.getNewUserTicketInfoRequest = null;
    }
    unregisterReceiver(this.killSelfReceiver);
    this.killSelfReceiver = null;
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      exitConfirm();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onLeftTitleButtonClicked()
  {
    exitConfirm();
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    paramGAUserInfo.shop_id = Integer.valueOf(this.shopId);
    super.onNewGAPager(paramGAUserInfo);
  }

  public void onProgressDialogCancel()
  {
    super.onProgressDialogCancel();
    this.waitingHintHandler.removeMessages(1000);
    finish();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getBookingConfigRequest)
    {
      this.waitingHintHandler.removeMessages(1000);
      dismissDialog();
      getBookingConfigFailed();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getBookingConfigRequest)
    {
      if ((paramMApiResponse.statusCode() != 200) || (!(paramMApiResponse.result() instanceof DPObject)))
        break label118;
      DPObject localDPObject = (DPObject)paramMApiResponse.result();
      this.waitingHintHandler.removeMessages(1000);
      dismissDialog();
      this.bookingInfoFragment.onBookingConfigChanged(localDPObject);
    }
    while (true)
    {
      this.getBookingConfigRequest = null;
      if (paramMApiRequest == this.bookingHolidaysRequest)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
          this.holidaysList = ((DPObject)paramMApiResponse.result()).getArray("List");
        this.bookingHolidaysRequest = null;
      }
      return;
      label118: getBookingConfigFailed();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingInfoActivity
 * JD-Core Version:    0.6.0
 */