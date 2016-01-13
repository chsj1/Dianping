package com.dianping.pay.activity;

import android.content.Intent;
import android.os.Bundle;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.pay.PayManager;
import com.dianping.base.widget.TitleBar;

public class PayWebankEntryActivity extends NovaActivity
{
  protected Boolean isSuccess;
  protected int resultCode;
  protected String resultMsg;
  protected String resultTitle;

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.requestWindowFeature(1);
    this.resultCode = getIntParam("resultcode");
    this.isSuccess = Boolean.valueOf(getBooleanParam("success"));
    this.resultMsg = getStringParam("resultmsg");
    this.resultTitle = getStringParam("resulttitle");
    PayManager.instance().webankPayResult(this.isSuccess.booleanValue(), this.resultCode, this.resultMsg);
    finish();
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
    finish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.activity.PayWebankEntryActivity
 * JD-Core Version:    0.6.0
 */