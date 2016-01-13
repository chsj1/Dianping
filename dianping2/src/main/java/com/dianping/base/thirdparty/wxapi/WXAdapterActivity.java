package com.dianping.base.thirdparty.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class WXAdapterActivity extends Activity
{
  private boolean isNewInstance;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.isNewInstance = true;
    paramBundle = new Intent("android.intent.action.VIEW", Uri.parse("dianping://wxshoplist?utm_=w_wxplus"));
    Intent localIntent = getIntent();
    if ((localIntent == null) || (localIntent.getExtras() == null))
    {
      finish();
      return;
    }
    paramBundle.putExtras(getIntent().getExtras());
    paramBundle.putExtra("fromWX", true);
    startActivity(paramBundle);
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    finish();
  }

  public void onResume()
  {
    super.onResume();
    if (this.isNewInstance)
    {
      this.isNewInstance = false;
      return;
    }
    finish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.thirdparty.wxapi.WXAdapterActivity
 * JD-Core Version:    0.6.0
 */