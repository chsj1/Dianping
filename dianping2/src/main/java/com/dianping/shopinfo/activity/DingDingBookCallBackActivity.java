package com.dianping.shopinfo.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.util.Log;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.web.util.WebUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DingDingBookCallBackActivity extends NovaActivity
{
  private boolean isNewInstance;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.isNewInstance = true;
    String str2 = getStringParam("state");
    String str3 = getStringParam("duration");
    paramBundle = getStringParam("session_id");
    paramBundle = WebUtils.getJsLocalStorage(getApplicationContext(), paramBundle, "dianping://home");
    try
    {
      String str1 = URLDecoder.decode(paramBundle, "utf-8");
      paramBundle = str1;
      paramBundle = new Intent("android.intent.action.VIEW", Uri.parse(paramBundle).buildUpon().appendQueryParameter("state", str2).appendQueryParameter("duration", str3).build());
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      try
      {
        startActivity(paramBundle);
        return;
        localUnsupportedEncodingException = localUnsupportedEncodingException;
        localUnsupportedEncodingException.printStackTrace();
      }
      catch (Exception paramBundle)
      {
        Log.e("DINGDING_CALLBACK_EXCEPTION", paramBundle.toString());
      }
    }
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
 * Qualified Name:     com.dianping.shopinfo.activity.DingDingBookCallBackActivity
 * JD-Core Version:    0.6.0
 */