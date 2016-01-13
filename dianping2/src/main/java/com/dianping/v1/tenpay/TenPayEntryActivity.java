package com.dianping.v1.tenpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.dianping.base.thirdparty.qqapi.TenPayHelper;
import com.dianping.base.tuan.pay.PayManager;
import com.tencent.mobileqq.openpay.api.IOpenApi;
import com.tencent.mobileqq.openpay.api.IOpenApiListener;
import com.tencent.mobileqq.openpay.data.base.BaseResponse;

public class TenPayEntryActivity extends Activity
  implements IOpenApiListener
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    TenPayHelper.getQQAPI(this).handleIntent(getIntent(), this);
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
    TenPayHelper.getQQAPI(this).handleIntent(paramIntent, this);
  }

  public void onOpenResponse(BaseResponse paramBaseResponse)
  {
    PayManager.instance().tenPayResult(paramBaseResponse);
    finish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.v1.tenpay.TenPayEntryActivity
 * JD-Core Version:    0.6.0
 */