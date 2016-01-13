package com.dianping.queue.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.queue.util.QueueViewUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class QueueResultActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final int MSG_REQ = 0;
  private int displayCode = 0;
  NovaButton failedBackButton;
  private int firstLoopInterval = 1000;
  private int furtherLoopInterval = 3000;
  private MApiRequest getHobbitEntryReq;
  private MApiRequest getQueueOrderReq;
  final Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 0)
        QueueResultActivity.this.getQueueOrderTask();
    }
  };
  NovaButton orderButton;
  private String orderId;
  private DPObject orderInfoObj;
  private DPObject queueOrderObj;
  private String shopId;
  NovaButton successButton;
  private final Timer timer = new Timer();
  NovaButton waitButton;

  private void backToQueueMain()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(String.format("dianping://queuemain?shopid=%s", new Object[] { this.shopId })));
    localIntent.setFlags(67108864);
    startActivity(localIntent);
    finish();
  }

  private void backToShopinfo()
  {
    Object localObject = String.format("dianping://shopinfo?shopid=%s", new Object[] { this.shopId });
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
      ((Intent)localObject).setFlags(67108864);
      startActivity((Intent)localObject);
    }
    finish();
  }

  private void convertStateToDisplayCode(int paramInt)
  {
    switch (paramInt)
    {
    default:
      this.displayCode = 2;
      return;
    case 0:
    case 1:
      this.displayCode = 0;
      return;
    case 2:
    case 3:
    case 4:
    case 6:
    case 7:
    case 501:
    case 502:
      this.displayCode = 1;
      return;
    case 11:
    }
    this.displayCode = 2;
  }

  private void displayFailedLayout()
  {
    super.setContentView(R.layout.queue_result_failed_layout);
    super.setTitle("排队取号");
    super.addTitleBarShadow();
    this.failedBackButton = ((NovaButton)findViewById(R.id.fail));
    this.failedBackButton.setOnClickListener(this);
    if (this.queueOrderObj != null)
      ViewUtils.setVisibilityAndContent((TextView)findViewById(R.id.queue_failed_desc), this.queueOrderObj.getString("FailedDesc"));
  }

  private void displayHobbitEntry(DPObject paramDPObject)
  {
    paramDPObject = paramDPObject.getString("Content");
    ViewUtils.setVisibilityAndContent((TextView)findViewById(R.id.menu_tip), paramDPObject);
  }

  private void displayLoadingLayout()
  {
    super.setContentView(R.layout.queue_result_loading_layout);
    super.setTitle("排队取号");
    this.waitButton = ((NovaButton)findViewById(R.id.later));
    this.waitButton.setOnClickListener(this);
  }

  private void displayMainLayout(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return;
    case 0:
      displayLoadingLayout();
      return;
    case 1:
      GAHelper.instance().contextStatisticsEvent(this, "success", "", 0, "view");
      displaySuccessLayout();
      this.timer.cancel();
      this.handler.removeMessages(0);
      return;
    case 2:
    }
    GAHelper.instance().contextStatisticsEvent(this, "fail", "", 0, "view");
    displayFailedLayout();
    this.timer.cancel();
    this.handler.removeMessages(0);
  }

  private void displaySuccessLayout()
  {
    super.setContentView(R.layout.queue_result_success_layout);
    super.setTitle("排队取号");
    super.addTitleBarShadow();
    TextView localTextView1 = (TextView)findViewById(R.id.queue_shopname);
    TextView localTextView2 = (TextView)findViewById(R.id.queue_num);
    TextView localTextView3 = (TextView)findViewById(R.id.queue_wait_num);
    TextView localTextView4 = (TextView)findViewById(R.id.queue_wait_time);
    TextView localTextView5 = (TextView)findViewById(R.id.queue_time);
    this.successButton = ((NovaButton)findViewById(R.id.order));
    this.successButton.setOnClickListener(this);
    this.orderButton = ((NovaButton)findViewById(R.id.menu));
    this.orderButton.setOnClickListener(this);
    String str3;
    if (this.queueOrderObj != null)
    {
      String str1 = this.queueOrderObj.getString("ShopName");
      String str2 = this.queueOrderObj.getString("TableNum") + this.queueOrderObj.getString("TableName");
      str3 = this.queueOrderObj.getString("Wait");
      String str4 = this.queueOrderObj.getString("WaitTime");
      String str5 = this.queueOrderObj.getString("CreateTime");
      QueueViewUtils.setTextViewToLine(localTextView1, str1);
      QueueViewUtils.setTextViewToLine(localTextView2, str2);
      QueueViewUtils.setTextViewToLine(localTextView4, str4);
      QueueViewUtils.setTextViewToLine(localTextView5, str5);
      if (!TextUtils.isEmpty(str3))
        break label304;
      localTextView3.setText("--");
    }
    while (this.orderInfoObj != null)
    {
      this.successButton.setTextAppearance(this, R.style.NovaLightButtonTheme);
      this.successButton.setBackgroundResource(R.drawable.btn_light);
      this.orderButton.setText(this.orderInfoObj.getString("ResButtonName"));
      this.orderButton.setVisibility(0);
      return;
      label304: localTextView3.setText(str3 + "桌");
    }
    this.orderButton.setVisibility(8);
  }

  private void getHobbitEntryTask()
  {
    if (this.getHobbitEntryReq != null)
      return;
    this.getHobbitEntryReq = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/orderdish/getquhbtdishfloatinfo.hbt?").buildUpon().appendQueryParameter("shopid", this.shopId).toString(), CacheType.DISABLED);
    mapiService().exec(this.getHobbitEntryReq, this);
  }

  private void getQueueOrderTask()
  {
    if (this.getQueueOrderReq != null)
      mapiService().abort(this.getQueueOrderReq, this, true);
    this.getQueueOrderReq = BasicMApiRequest.mapiGet(Uri.parse("http://mapi.dianping.com/queue/getorder.qu?").buildUpon().appendQueryParameter("orderid", String.valueOf(this.orderId)).toString(), CacheType.DISABLED);
    mapiService().exec(this.getQueueOrderReq, this);
  }

  private void onBackButtonClicked()
  {
    if (this.displayCode == 2)
    {
      backToQueueMain();
      return;
    }
    backToShopinfo();
  }

  public String getPageName()
  {
    return "queueresult";
  }

  public void onClick(View paramView)
  {
    if (paramView == this.waitButton)
    {
      statisticsEvent("queue", "queue_later", "", Integer.parseInt(this.shopId));
      backToShopinfo();
    }
    do
    {
      return;
      if (paramView == this.successButton)
      {
        if (!TextUtils.isEmpty("dianping://myqueuelist"))
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://myqueuelist"));
          paramView.setFlags(67108864);
          startActivity(paramView);
        }
        finish();
        return;
      }
      if (paramView != this.failedBackButton)
        continue;
      backToQueueMain();
      return;
    }
    while (paramView != this.orderButton);
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.orderInfoObj.getString("SchemaUrl"))));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.getWindow().setBackgroundDrawable(null);
    if (paramBundle == null)
    {
      this.orderId = getStringParam("orderid");
      this.shopId = getStringParam("shopid");
      this.firstLoopInterval = Integer.parseInt(getStringParam("firstloopinterval"));
      this.furtherLoopInterval = Integer.parseInt(getStringParam("furtherloopinterval"));
    }
    while (this.displayCode == 0)
    {
      this.timer.scheduleAtFixedRate(new QueryTimer(), this.firstLoopInterval, this.furtherLoopInterval);
      displayMainLayout(this.displayCode);
      return;
      this.orderId = paramBundle.getString("orderid");
      this.shopId = paramBundle.getString("shopid");
      this.firstLoopInterval = paramBundle.getInt("firstloopinterval");
      this.furtherLoopInterval = paramBundle.getInt("furtherloopinterval");
      this.displayCode = paramBundle.getInt("displayCode");
      this.queueOrderObj = ((DPObject)paramBundle.getParcelable("queueOrderObj"));
    }
    displayMainLayout(this.displayCode);
  }

  protected void onDestroy()
  {
    if (this.getQueueOrderReq != null)
    {
      mapiService().abort(this.getQueueOrderReq, this, true);
      this.getQueueOrderReq = null;
    }
    this.timer.cancel();
    this.handler.removeMessages(0);
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      onBackButtonClicked();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onLeftTitleButtonClicked()
  {
    onBackButtonClicked();
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    GAUserInfo localGAUserInfo1 = paramGAUserInfo;
    GAUserInfo localGAUserInfo2 = localGAUserInfo1;
    if (localGAUserInfo1 == null)
      localGAUserInfo2 = new GAUserInfo();
    localGAUserInfo2.shop_id = Integer.valueOf(Integer.parseInt(this.shopId));
    GAHelper.instance().setGAPageName(getPageName());
    GAHelper.instance().setRequestId(this, UUID.randomUUID().toString(), localGAUserInfo2, false);
    paramGAUserInfo.utm = null;
    paramGAUserInfo.marketing_source = null;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getQueueOrderReq)
    {
      showToast("网络连接出错");
      this.getQueueOrderReq = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.getQueueOrderReq)
    {
      this.queueOrderObj = ((DPObject)paramMApiResponse.result());
      this.shopId = String.valueOf(this.queueOrderObj.getInt("ShopId"));
      convertStateToDisplayCode(this.queueOrderObj.getInt("State"));
      this.orderInfoObj = this.queueOrderObj.getObject("MenuOrderDo");
      displayMainLayout(this.displayCode);
      if (this.displayCode == 1)
        sendBroadcast(new Intent("com.dianping.queue.QUEUE_STATE_REFRESH"));
      this.getQueueOrderReq = null;
      if ((this.displayCode == 1) && (this.orderInfoObj != null))
        getHobbitEntryTask();
    }
    do
      return;
    while (paramMApiRequest != this.getHobbitEntryReq);
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest != null)
      displayHobbitEntry(paramMApiRequest);
    this.getHobbitEntryReq = null;
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelable("queueSummaryObj", this.queueOrderObj);
    paramBundle.putString("orderid", this.orderId);
    paramBundle.putString("shopid", this.shopId);
    paramBundle.putInt("furtherloopinterval", this.furtherLoopInterval);
    paramBundle.putInt("displayCode", this.displayCode);
    super.onSaveInstanceState(paramBundle);
  }

  class QueryTimer extends TimerTask
  {
    QueryTimer()
    {
    }

    public void run()
    {
      QueueResultActivity.this.handler.sendEmptyMessage(0);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.queue.activity.QueueResultActivity
 * JD-Core Version:    0.6.0
 */