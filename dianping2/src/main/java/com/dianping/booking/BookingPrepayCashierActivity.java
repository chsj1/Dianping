package com.dianping.booking;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.alipay.sdk.app.PayTask;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.thirdparty.wxapi.WXHelper;
import com.dianping.base.tuan.pay.AlipayResult;
import com.dianping.base.tuan.widget.PayChanelItem;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class BookingPrepayCashierActivity extends NovaActivity
  implements View.OnClickListener, TableView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int ALIPAYSDK_PAY_FLAG = 100;
  private static final int CODE_PREPAY_REQ = 1;
  private static final DateFormat DATE_FMT = new SimpleDateFormat("MM月dd日 HH:mm");
  private static final int MAX_RETRY = 6;
  private static final String PREPAY_REQ = "http://rs.api.dianping.com/doprepay.yy?";
  private static final String PREPAY_STATUS_REQ = "http://rs.api.dianping.com/queryprepaystatus.yy?";
  private String bookName;
  private String bookPhone;
  private DPObject bookResult;
  private String bookTime;
  private View btnSubmit;
  private final Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
        BookingPrepayCashierActivity.this.reqPrepayStatus();
        return;
      case 100:
      }
      paramMessage = new AlipayResult((String)paramMessage.obj);
      paramMessage.getResult();
      paramMessage = paramMessage.getResultStatus();
      if (TextUtils.equals(paramMessage, "9000"))
      {
        BookingPrepayCashierActivity.this.reqPrepayStatus();
        return;
      }
      if (TextUtils.equals(paramMessage, "8000"))
      {
        BookingPrepayCashierActivity.this.dismissDialog();
        BookingPrepayCashierActivity.this.showToast("支付结果确认中");
        return;
      }
      BookingPrepayCashierActivity.this.dismissDialog();
      BookingPrepayCashierActivity.this.showToast("支付失败");
    }
  };
  private boolean isInsteadRecord = false;
  private boolean isQuerying = false;
  private int payOrderId;
  private PayToolAdapter payToolAdapter;
  private int peopleNum;
  private String prepayAmount;
  private PrepayDescAdapter prepayDescAdapter;
  private String[] prepayDescs;
  private int prepayRecordId;
  private MApiRequest prepayReq;
  private MApiRequest prepayStatusReq;
  private int retryTimes = 6;
  private String roomType;
  DPObject selectedPayTool;
  private int shopId;
  private String shopName;
  private TableView tvPayTools;
  private TableView tvPrepayDescs;
  private final BroadcastReceiver weixinPayReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      BookingPrepayCashierActivity.this.dismissDialog();
      paramContext = new PayResp(paramIntent.getExtras());
      BookingPrepayCashierActivity.this.dismissDialog();
      if (paramContext.errCode == 0)
      {
        if (TextUtils.isEmpty(paramContext.errStr));
        for (paramContext = "微信支付成功"; ; paramContext = paramContext.errStr)
        {
          BookingPrepayCashierActivity.this.showToast(paramContext);
          BookingPrepayCashierActivity.this.reqPrepayStatus();
          return;
        }
      }
      if (TextUtils.isEmpty(paramContext.errStr))
      {
        BookingPrepayCashierActivity.this.showToast("微信支付取消");
        return;
      }
      BookingPrepayCashierActivity.this.showToast(paramContext.errStr);
    }
  };

  private void aliPay(String paramString)
  {
    new Thread(new Runnable(paramString)
    {
      public void run()
      {
        String str = new PayTask(BookingPrepayCashierActivity.this).pay(this.val$payContent);
        Message localMessage = new Message();
        localMessage.what = 100;
        localMessage.obj = str;
        BookingPrepayCashierActivity.this.handler.sendMessage(localMessage);
      }
    }).start();
  }

  private void displayBadNetworkDlg()
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle("提示");
    localBuilder.setMessage("网络不给力，请稍后查看订座结果");
    localBuilder.setPositiveButton("知道了", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        BookingPrepayCashierActivity.this.startActivity("dianping://bookinglist");
        BookingPrepayCashierActivity.this.finish();
      }
    });
    localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        BookingPrepayCashierActivity.this.startActivity("dianping://bookinglist");
        BookingPrepayCashierActivity.this.finish();
      }
    });
    localBuilder.create().show();
  }

  private void gotoPrepayFail()
  {
    Intent localIntent = new Intent();
    localIntent.setData(Uri.parse("dianping://bookingprepayfail"));
    localIntent.putExtra("shopid", this.shopId);
    localIntent.putExtra("shopname", this.shopName);
    DPObject localDPObject = this.bookResult.getObject("Record").getObject("PrepayInfo");
    if (TextUtils.isEmpty(localDPObject.getString("Title")))
    {
      str = "非常抱歉，定金支付失败";
      localIntent.putExtra("title", str);
      if (localDPObject.getStringArray("Desc").length <= 0)
        break label134;
    }
    label134: for (String str = TextUtils.join("，", localDPObject.getStringArray("Desc")); ; str = "您的支付金额未扣除，仍在您的支付账户中，不用担心哦。")
    {
      localIntent.putExtra("desc", str);
      startActivity(localIntent);
      super.finish();
      return;
      str = localDPObject.getString("Title");
      break;
    }
  }

  private void gotoPrepaySuccess()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("result", this.bookResult);
    localBundle.putInt("shopId", this.shopId);
    Intent localIntent = new Intent();
    localIntent.putExtras(localBundle);
    super.setResult(-1, localIntent);
    super.finish();
  }

  private void initData()
  {
    this.bookResult = getObjectParam("result");
    DPObject localDPObject = this.bookResult.getObject("Record");
    this.shopId = localDPObject.getInt("ShopID");
    this.shopName = localDPObject.getString("ShopName");
    this.bookTime = DATE_FMT.format(new Date(localDPObject.getTime("BookingTime")));
    this.peopleNum = localDPObject.getInt("PeopleNumber");
    this.isInsteadRecord = localDPObject.getBoolean("InsteadRecord");
    StringBuilder localStringBuilder;
    switch (localDPObject.getInt("PositionType"))
    {
    default:
      this.roomType = "大厅";
      localStringBuilder = new StringBuilder().append(localDPObject.getString("BookerName"));
      if (localDPObject.getInt("BookerGender") != 10)
        break;
    case 10:
    case 20:
    case 30:
    }
    for (Object localObject = "女士"; ; localObject = "先生")
    {
      this.bookName = ((String)localObject);
      this.bookPhone = localDPObject.getString("BookerPhone");
      localObject = localDPObject.getObject("PrepayInfo");
      this.prepayAmount = ((DPObject)localObject).getString("PrepayAmount");
      this.prepayDescs = ((DPObject)localObject).getStringArray("Desc");
      this.prepayRecordId = ((DPObject)localObject).getInt("PrepayRecordId");
      this.prepayDescAdapter = new PrepayDescAdapter(this.prepayDescs);
      this.payToolAdapter = new PayToolAdapter();
      this.selectedPayTool = ((DPObject)this.payToolAdapter.getItem(0));
      this.payToolAdapter.notifyDataSetChanged();
      return;
      this.roomType = "大厅";
      break;
      this.roomType = "包房优先";
      break;
      this.roomType = "包房必须";
      break;
    }
  }

  private void initViews()
  {
    ((TextView)findViewById(R.id.shop_name)).setText(this.shopName);
    ((TextView)findViewById(R.id.book_time)).setText(this.bookTime);
    ((TextView)findViewById(R.id.people_num)).setText(this.peopleNum + "人｜" + this.roomType);
    ((TextView)findViewById(R.id.book_name)).setText(this.bookName);
    if (this.isInsteadRecord)
      super.findViewById(R.id.instead_icon).setVisibility(0);
    ((TextView)findViewById(R.id.book_phone)).setText(this.bookPhone);
    ((TextView)findViewById(R.id.prepay_amount)).setText(this.prepayAmount);
    this.tvPrepayDescs = ((TableView)findViewById(R.id.prepay_descs));
    this.tvPrepayDescs.setAdapter(this.prepayDescAdapter);
    this.tvPrepayDescs.setDivider(null);
    this.tvPayTools = ((TableView)findViewById(R.id.pay_tools));
    this.tvPayTools.setAdapter(this.payToolAdapter);
    this.tvPayTools.setOnItemClickListener(this);
    this.btnSubmit = findViewById(R.id.submit);
    this.btnSubmit.setOnClickListener(this);
  }

  private void reqPrepay()
  {
    if (this.prepayReq != null)
      mapiService().abort(this.prepayReq, this, true);
    Uri.Builder localBuilder = Uri.parse("http://rs.api.dianping.com/doprepay.yy?").buildUpon();
    localBuilder.appendQueryParameter("amount", this.prepayAmount);
    localBuilder.appendQueryParameter("channel", Integer.toString(this.selectedPayTool.getInt("PaymentTool")));
    localBuilder.appendQueryParameter("prepayrecordid", Integer.toString(this.prepayRecordId));
    localBuilder.appendQueryParameter("clientuuid", Environment.uuid());
    localBuilder.appendQueryParameter("cx", DeviceUtils.cxInfo("booking"));
    this.prepayReq = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.prepayReq, this);
  }

  private void reqPrepayStatus()
  {
    if (!this.isQuerying)
    {
      showProgressDialog("正在查询订单支付结果...");
      this.isQuerying = true;
    }
    if (this.prepayStatusReq != null)
      mapiService().abort(this.prepayStatusReq, this, true);
    Uri.Builder localBuilder = Uri.parse("http://rs.api.dianping.com/queryprepaystatus.yy?").buildUpon();
    localBuilder.appendQueryParameter("payorderid", Integer.toString(this.payOrderId));
    this.prepayStatusReq = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.prepayStatusReq, this);
  }

  private void weixinPay(String paramString)
  {
    while (true)
    {
      int i;
      try
      {
        if (WXHelper.isSupportPay(this))
          continue;
        dismissDialog();
        return;
        HashMap localHashMap = new HashMap();
        paramString = paramString.split("&");
        i = 0;
        if (i >= paramString.length)
          continue;
        int j = paramString[i].indexOf("=");
        if (j > 0)
        {
          localHashMap.put(paramString[i].substring(0, j), paramString[i].substring(j + 1));
          break label199;
          paramString = new PayReq();
          paramString.appId = "wx8e251222d6836a60";
          paramString.partnerId = ((String)localHashMap.get("partnerid"));
          paramString.prepayId = ((String)localHashMap.get("prepayid"));
          paramString.nonceStr = ((String)localHashMap.get("noncestr"));
          paramString.timeStamp = ((String)localHashMap.get("timestamp"));
          paramString.packageValue = ((String)localHashMap.get("package"));
          paramString.sign = ((String)localHashMap.get("sign"));
          WXHelper.getWXAPI(this).sendReq(paramString);
          return;
        }
      }
      catch (java.lang.Exception paramString)
      {
        showToast("支付失败，请选择其他支付方式");
        return;
      }
      label199: i += 1;
    }
  }

  void backtoBooking()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://onlinebooking?shopid=" + this.shopId + "&shopname=" + this.shopName));
    localIntent.setFlags(67108864);
    startActivity(localIntent);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.btnSubmit)
    {
      showProgressDialog("载入中...");
      reqPrepay();
      this.btnSubmit.setEnabled(false);
      statisticsEvent("booking7", "booking7_prepay_submit", "", 0);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.booking_prepay_cashier);
    super.getWindow().setBackgroundDrawable(null);
    initData();
    initViews();
    super.registerReceiver(this.weixinPayReceiver, new IntentFilter("com.dianping.base.thirdparty.wxapi.WXPAY"));
    statisticsEvent("booking7", "booking7_prepay_detail", "", 0);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.prepayReq != null)
    {
      mapiService().abort(this.prepayReq, this, true);
      this.prepayReq = null;
    }
    super.unregisterReceiver(this.weixinPayReceiver);
    this.handler.removeMessages(100);
  }

  public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
  {
    if (paramTableView == this.tvPayTools)
    {
      this.selectedPayTool = ((DPObject)this.payToolAdapter.getItem(paramInt));
      this.payToolAdapter.notifyDataSetChanged();
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.prepayReq)
    {
      this.prepayReq = null;
      showMessageDialog(paramMApiResponse.message(), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          BookingPrepayCashierActivity.this.backtoBooking();
        }
      });
    }
    do
      return;
    while (paramMApiRequest != this.prepayStatusReq);
    int i = this.retryTimes;
    this.retryTimes = (i - 1);
    if (i > 0)
    {
      this.handler.sendEmptyMessageDelayed(1, 10000L);
      return;
    }
    this.retryTimes = 6;
    this.isQuerying = false;
    dismissDialog();
    displayBadNetworkDlg();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest == this.prepayReq)
    {
      this.prepayReq = null;
      paramMApiRequest = paramMApiResponse.getString("PayContent");
      if (TextUtils.isEmpty(paramMApiRequest))
        showToast("获取订单号失败");
    }
    do
    {
      return;
      dismissDialog();
      this.btnSubmit.setEnabled(true);
      this.payOrderId = paramMApiResponse.getInt("OrderId");
      i = paramMApiResponse.getInt("Status");
      paramMApiResponse = paramMApiResponse.getString("Msg");
      if (i != 1)
      {
        showAlertDialog(paramMApiResponse, null);
        return;
      }
      switch (this.selectedPayTool.getInt("PaymentTool"))
      {
      default:
        aliPay(paramMApiRequest);
        return;
      case 5:
        aliPay(paramMApiRequest);
        return;
      case 11:
      }
      weixinPay(paramMApiRequest);
      return;
    }
    while (paramMApiRequest != this.prepayStatusReq);
    this.prepayStatusReq = null;
    int i = paramMApiResponse.getObject("Record").getObject("PrepayInfo").getInt("PayStatus");
    this.bookResult = paramMApiResponse;
    switch (i)
    {
    default:
      i = this.retryTimes;
      this.retryTimes = (i - 1);
      if (i <= 0)
        break;
      this.handler.sendEmptyMessageDelayed(1, 10000L);
      return;
    case 10:
      this.isQuerying = false;
      dismissDialog();
      gotoPrepaySuccess();
      return;
    case 20:
    case 30:
    case 40:
      this.isQuerying = false;
      dismissDialog();
      gotoPrepayFail();
      return;
    }
    this.retryTimes = 6;
    this.isQuerying = false;
    dismissDialog();
    displayBadNetworkDlg();
  }

  public void showMessageDialog(SimpleMsg paramSimpleMsg, DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    this.dlgMessage = paramSimpleMsg;
    paramSimpleMsg = new AlertDialog.Builder(this);
    paramSimpleMsg.setMessage(this.dlgMessage.content());
    paramSimpleMsg.setPositiveButton("好的", paramOnClickListener);
    paramSimpleMsg = paramSimpleMsg.create();
    paramSimpleMsg.setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        BookingPrepayCashierActivity.this.onMessageConfirm();
        if (BookingPrepayCashierActivity.this.managedDialogId == 64006)
          BookingPrepayCashierActivity.access$602(BookingPrepayCashierActivity.this, 0);
        BookingPrepayCashierActivity.access$702(BookingPrepayCashierActivity.this, null);
      }
    });
    this.managedDialogId = 64006;
    this.managedDialog = paramSimpleMsg;
    paramSimpleMsg.show();
  }

  public void showProgressDialog(String paramString)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    this.dlgProgressTitle = paramString;
    paramString = new ProgressDialog(this);
    paramString.setCanceledOnTouchOutside(false);
    paramString.setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        if (BookingPrepayCashierActivity.this.managedDialogId == 64005)
          BookingPrepayCashierActivity.access$302(BookingPrepayCashierActivity.this, 0);
        BookingPrepayCashierActivity.access$402(BookingPrepayCashierActivity.this, null);
        BookingPrepayCashierActivity.this.onProgressDialogCancel();
      }
    });
    paramString.setMessage(this.dlgProgressTitle);
    this.managedDialogId = 64005;
    this.managedDialog = paramString;
    paramString.show();
  }

  class PayToolAdapter extends BasicAdapter
  {
    private DPObject[] payTools = { new DPObject("PaymentTool").edit().putString("Title", "微信支付").putString("SubTitle", "推荐已安装微信的用户使用").putString("ID", "11:1:null").putInt("Type", 1).putInt("PaymentTool", 11).generate(), new DPObject("PaymentTool").edit().putString("Title", "支付宝支付").putInt("PaymentTool", 5).putString("SubTitle", "推荐支付宝用户使用").putString("ID", "5:1:null").putInt("Type", 2).generate() };

    public PayToolAdapter()
    {
    }

    public int getCount()
    {
      return this.payTools.length;
    }

    public Object getItem(int paramInt)
    {
      return this.payTools[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = this.payTools[paramInt];
      paramViewGroup = new PayChanelItem(BookingPrepayCashierActivity.this);
      if (BookingPrepayCashierActivity.this.selectedPayTool == paramView);
      for (boolean bool = true; ; bool = false)
      {
        paramViewGroup.setChecked(bool);
        paramViewGroup.setPaymentTool(paramView);
        paramViewGroup.setClickable(true);
        return paramViewGroup;
      }
    }
  }

  class PrepayDescAdapter extends BasicAdapter
  {
    private String[] mDescs;

    public PrepayDescAdapter(String[] arg2)
    {
      Object localObject;
      this.mDescs = new String[localObject.length];
      System.arraycopy(localObject, 0, this.mDescs, 0, localObject.length);
    }

    public int getCount()
    {
      return this.mDescs.length;
    }

    public Object getItem(int paramInt)
    {
      return this.mDescs[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = BookingPrepayCashierActivity.this.getLayoutInflater().inflate(R.layout.booking_prepay_desc, null);
      ((TextView)paramViewGroup.findViewById(R.id.desc)).setText(this.mDescs[paramInt]);
      return paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingPrepayCashierActivity
 * JD-Core Version:    0.6.0
 */