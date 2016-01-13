package com.dianping.pay.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.thirdparty.wxapi.WXHelper;
import com.dianping.pay.entity.PayWxNoPwdSource;
import com.dianping.pay.entity.PayWxNoPwdSource.WxNoPwdResultListener;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.tencent.mm.sdk.modelbiz.OpenWebview.Req;
import com.tencent.mm.sdk.openapi.IWXAPI;

public class PayWxNoPwdActivity extends NovaActivity
  implements View.OnClickListener, PayWxNoPwdSource.WxNoPwdResultListener
{
  private PayWxNoPwdSource dataSource;
  private boolean intentWxFlag = false;
  private LinearLayout ll_wx_free_pwd_not_open;
  private LinearLayout ll_wx_free_pwd_opened;
  private String resultMsg;
  private String resultTitle;
  private TextView tv_day_limit;
  private TextView tv_once_limit;
  private TextView tv_once_limit_not_open;
  private String wxNoPwdBindUrl;

  private void initView()
  {
    this.ll_wx_free_pwd_opened = ((LinearLayout)findViewById(R.id.ll_wx_free_pwd_opened));
    this.ll_wx_free_pwd_not_open = ((LinearLayout)findViewById(R.id.ll_wx_free_pwd_not_open));
    this.tv_once_limit = ((TextView)findViewById(R.id.tv_once_limit));
    this.tv_day_limit = ((TextView)findViewById(R.id.tv_day_limit));
    this.tv_once_limit_not_open = ((TextView)findViewById(R.id.tv_once_limit_not_open));
    findViewById(R.id.nbtn_close_wx_free_pwd).setOnClickListener(this);
    findViewById(R.id.nbtn_open_wx_free_pwd).setOnClickListener(this);
    this.dataSource.reqIsWxNoPwd();
  }

  public void notOpen(DPObject paramDPObject)
  {
    this.ll_wx_free_pwd_opened.setVisibility(8);
    this.ll_wx_free_pwd_not_open.setVisibility(0);
    this.wxNoPwdBindUrl = paramDPObject.getString("WxNoPwdBindUrl");
    paramDPObject = paramDPObject.getString("PerDayLimit");
    this.tv_once_limit_not_open.setText(paramDPObject);
    setTitle("微信免密支付");
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.nbtn_open_wx_free_pwd)
    {
      paramView = new OpenWebview.Req();
      paramView.url = this.wxNoPwdBindUrl;
      WXHelper.getWXAPI(this).sendReq(paramView);
      this.intentWxFlag = true;
    }
    do
      return;
    while (i != R.id.nbtn_close_wx_free_pwd);
    showSimpleAlertDialog(this.resultTitle, this.resultMsg, "取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        PayWxNoPwdActivity.this.dismissDialog();
      }
    }
    , "确认关闭", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        PayWxNoPwdActivity.this.dataSource.reqUnBindWxNoPwd();
      }
    });
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.pay_wx_no_pwd_activity);
    this.dataSource = new PayWxNoPwdSource(this);
    this.dataSource.setWxNoPwdResultListener(this);
    initView();
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.dataSource.releaseRequests();
    setResult(-1);
  }

  protected void onResume()
  {
    super.onResume();
    if (this.intentWxFlag)
    {
      this.dataSource.reqIsWxNoPwd();
      this.intentWxFlag = false;
    }
  }

  public void opened(DPObject paramDPObject)
  {
    this.ll_wx_free_pwd_opened.setVisibility(0);
    this.ll_wx_free_pwd_not_open.setVisibility(8);
    setTitle("微信免密支付管理");
    String str1 = paramDPObject.getString("PerOrderLimit");
    String str2 = paramDPObject.getString("PerDayLimit");
    this.resultMsg = paramDPObject.getString("ResultMsg");
    this.resultTitle = paramDPObject.getString("ResultTitle");
    this.tv_once_limit.setText(str1);
    this.tv_day_limit.setText(str2);
  }

  public void resendReq()
  {
    this.dataSource.reqIsWxNoPwd();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.activity.PayWxNoPwdActivity
 * JD-Core Version:    0.6.0
 */