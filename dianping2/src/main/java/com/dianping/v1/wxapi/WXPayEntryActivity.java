package com.dianping.v1.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.dianping.base.thirdparty.wxapi.WXHelper;
import com.dianping.base.tuan.pay.PayManager;
import com.dianping.util.Log;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity
  implements IWXAPIEventHandler
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.requestWindowFeature(1);
    WXHelper.getWXAPI(this).handleIntent(getIntent(), this);
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
    WXHelper.getWXAPI(this).handleIntent(paramIntent, this);
  }

  public void onReq(BaseReq paramBaseReq)
  {
  }

  public void onResp(BaseResp paramBaseResp)
  {
    Log.i("wxpayentry", paramBaseResp.errStr + ">>" + paramBaseResp.errCode);
    if (paramBaseResp.getType() == 5)
    {
      Intent localIntent = new Intent("com.dianping.base.thirdparty.wxapi.WXPAY");
      Bundle localBundle = new Bundle();
      paramBaseResp.toBundle(localBundle);
      localIntent.putExtras(localBundle);
      sendBroadcast(localIntent);
      PayManager.instance().weixinPayResult(localBundle);
    }
    finish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.v1.wxapi.WXPayEntryActivity
 * JD-Core Version:    0.6.0
 */