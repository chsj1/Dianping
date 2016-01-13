package com.dianping.search.shoplist.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.util.QQAio;

public class QQAioAdapterActivity extends NovaActivity
{
  private boolean isNewInstance;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.isNewInstance = true;
    paramBundle = new Intent("android.intent.action.VIEW", Uri.parse("dianping://qqshoplist?utm_=w_mqqaio"));
    QQAio.getInstance().setQQIntent(getIntent());
    Intent localIntent = getIntent();
    if ((localIntent == null) || (localIntent.getExtras() == null))
    {
      finish();
      return;
    }
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
 * Qualified Name:     com.dianping.search.shoplist.activity.QQAioAdapterActivity
 * JD-Core Version:    0.6.0
 */