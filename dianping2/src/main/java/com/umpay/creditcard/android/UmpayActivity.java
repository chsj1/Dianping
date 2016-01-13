package com.umpay.creditcard.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.umpay.creditcard.android.util.j;
import com.umpay.creditcard.android.util.l;
import com.umpay.paysdk.meituan.ad;
import com.umpay.paysdk.meituan.aj;
import com.umpay.paysdk.meituan.ao;

public class UmpayActivity extends Activity
{
  public static String d = null;
  public String a;
  public String b = "";
  public String c = "";
  public ad e;
  public ao f;
  private a g;

  public final void a()
  {
    View localView = this.g.a();
    if (localView != null)
      com.umpay.paysdk.meituan.a.a(this, f.a(localView.getClass()), "10000003", "支付要素页面", false);
    this.g.a(new c(this));
  }

  public final void a(a parama)
  {
    this.g = parama;
  }

  public final void a(String paramString1, String paramString2)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("umpResultMessage", paramString2);
    localIntent.putExtra("umpResultCode", paramString1);
    setResult(88888, localIntent);
    l.a().c();
    com.umpay.paysdk.meituan.a.a(this, f.a(ao.class), "10000011", paramString2, false);
    finish();
  }

  public final void b()
  {
    ao localao = this.f;
    String str1 = Float.parseFloat(this.e.h()) / 100.0F;
    String str2 = this.a;
    localao.a(str1);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    d = new j(this).a("terminalId");
    paramBundle = getIntent();
    if ((paramBundle.hasExtra("tradeNo")) && (paramBundle.hasExtra("cardType")) && (paramBundle.hasExtra("bankName")))
    {
      this.a = paramBundle.getStringExtra("tradeNo");
      this.c = paramBundle.getStringExtra("cardType");
      this.b = paramBundle.getStringExtra("bankName");
      paramBundle = this.a;
      String str1 = this.b;
      String str2 = this.c;
      int i;
      if ((TextUtils.isEmpty(paramBundle)) || (paramBundle.length() < 4) || (TextUtils.isEmpty(str2)) || (str2.length() <= 0) || (TextUtils.isEmpty(str1)) || (str1.length() <= 0))
        i = 0;
      while (i == 0)
      {
        Log.e("UMPAY", "支付参数格式不正确");
        finish();
        return;
        if (("0".equals(str2)) || ("1".equals(str2)));
        for (i = 1; ; i = 0)
        {
          if (i != 0)
            break label200;
          i = 0;
          break;
        }
        label200: i = 1;
      }
    }
    com.umpay.paysdk.meituan.a.a(this, "100008_1_3", d).a();
    com.umpay.paysdk.meituan.a.b(this.a);
    if ((!TextUtils.isEmpty(this.a)) && (!TextUtils.isEmpty(this.c)))
    {
      if (this.b == null);
      for (paramBundle = "null"; ; paramBundle = this.b)
      {
        com.umpay.paysdk.meituan.a.a(this, f.a(ao.class), "10000010", this.c + "," + paramBundle, false);
        com.umpay.paysdk.meituan.a.a(this);
        if (this.b == null)
          this.b = "";
        requestWindowFeature(1);
        this.f = new ao(this, this.b);
        new aj(this);
        com.umpay.paysdk.meituan.a.d(f.a(aj.class));
        setContentView(this.f);
        return;
      }
    }
    if (this.b == null);
    for (paramBundle = "null"; ; paramBundle = this.b)
    {
      com.umpay.paysdk.meituan.a.a(this, f.a(ao.class), "10000010", paramBundle + "，传入参数错误", false);
      break;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    com.umpay.paysdk.meituan.a.g();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      a();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onPause()
  {
    super.onPause();
    com.umpay.paysdk.meituan.a.b(this, "U付无线支付SDK");
    com.umpay.paysdk.meituan.a.a(this);
  }

  protected void onResume()
  {
    if (getRequestedOrientation() != 1)
      setRequestedOrientation(1);
    super.onResume();
    com.umpay.paysdk.meituan.a.f();
  }

  protected void onStop()
  {
    super.onStop();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.umpay.creditcard.android.UmpayActivity
 * JD-Core Version:    0.6.0
 */