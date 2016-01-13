package com.umpay.paysdk.meituan;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.umpay.creditcard.android.UmpayActivity;
import com.umpay.creditcard.android.util.b;
import com.umpay.creditcard.android.util.d;
import com.umpay.creditcard.android.util.j;
import com.umpay.creditcard.android.util.k;
import com.umpay.creditcard.android.util.o;
import java.util.ArrayList;
import java.util.Map;

public class aj extends RelativeLayout
  implements bs
{
  public String a;
  private UmpayActivity b;
  private Context c;
  private bt d;

  public aj(Context paramContext)
  {
    super(paramContext);
    this.c = paramContext;
    this.b = ((UmpayActivity)paramContext);
    this.d = new bt(this.b);
    int i = o.a(this.b, "drawable", "ump_sdk_loading");
    Object localObject4;
    Object localObject3;
    String str1;
    Object localObject5;
    Object localObject2;
    Object localObject1;
    label190: String str3;
    String str2;
    if (i == 0)
    {
      this.d.a("正在进入安全支付环境...");
      a.a(paramContext, com.umpay.creditcard.android.f.a(aj.class), "10200001", "(" + "101011" + ")" + UmpayActivity.d, false);
      localObject4 = new ae();
      localObject3 = new j(this.b);
      str1 = ((j)localObject3).a("terminalId");
      localObject5 = this.b;
      localObject2 = new j((Context)localObject5);
      if ((str1 == null) || ("".equals(str1)))
        break label744;
      localObject2 = ((j)localObject2).a("umpuuid");
      if (localObject2 != null)
      {
        localObject1 = localObject2;
        if (!"".equals(localObject2));
      }
      else
      {
        localObject1 = d.b((Context)localObject5);
      }
      this.a = ((String)localObject1);
      str3 = ((TelephonyManager)this.b.getSystemService("phone")).getSubscriberId();
      localObject5 = k.a(Build.MODEL);
      str2 = k.a(Build.VERSION.RELEASE);
      localObject1 = ((j)localObject3).a("newestUmpVer");
      localObject3 = ((j)localObject3).a("newestUpopVer");
      if (!TextUtils.isEmpty((CharSequence)localObject1))
        break label768;
      localObject1 = "1";
    }
    label768: 
    while (true)
    {
      localObject2 = localObject3;
      if (TextUtils.isEmpty((CharSequence)localObject3))
        localObject2 = "1";
      ((ae)localObject4).g("100008");
      ((ae)localObject4).a(this.b.b);
      ((ae)localObject4).d("300000");
      ((ae)localObject4).j("1");
      ((ae)localObject4).k("3");
      ((ae)localObject4).e(this.a);
      ((ae)localObject4).f(str3);
      localObject3 = ((WindowManager)this.c.getSystemService("window")).getDefaultDisplay();
      localObject3 = new Rect(0, 0, ((Display)localObject3).getWidth(), ((Display)localObject3).getHeight());
      ((ae)localObject4).b(((Rect)localObject3).right + "x" + ((Rect)localObject3).bottom);
      if ("1".equals(this.b.c));
      for (localObject3 = "1"; ; localObject3 = "8")
      {
        ((ae)localObject4).o((String)localObject3);
        ((ae)localObject4).i((String)localObject5);
        ((ae)localObject4).h(str2);
        ((ae)localObject4).c(str1);
        ((ae)localObject4).n(this.b.a);
        ((ae)localObject4).l((String)localObject1);
        ((ae)localObject4).m((String)localObject2);
        new StringBuilder("初始化请求参数").append("101011").append(((ae)localObject4).toString().replaceAll("&", "\n"));
        bu.a(paramContext).a(paramContext, "http://m.soopay.net:8080/wirelessbusi/commenurl", new cc("101011", localObject4), new bz(this));
        return;
        localObject1 = this.d;
        localObject2 = new LinearLayout(this.c);
        ((LinearLayout)localObject2).setBackgroundResource(o.a(this.c, "drawable", "ump_sdk_loading_bg"));
        ((LinearLayout)localObject2).setOrientation(1);
        ((LinearLayout)localObject2).setGravity(1);
        localObject3 = new ImageView(this.c);
        localObject4 = ai.h();
        ((LinearLayout.LayoutParams)localObject4).setMargins(0, b.a(this.c, 14.0F), 0, 0);
        ((ImageView)localObject3).setLayoutParams((ViewGroup.LayoutParams)localObject4);
        ((ImageView)localObject3).setImageResource(i);
        ((LinearLayout)localObject2).addView((View)localObject3);
        localObject3 = new TextView(this.c);
        ((TextView)localObject3).setText("正在进入安全支付环境...");
        ((TextView)localObject3).setTextSize(17.0F);
        ((TextView)localObject3).setTextColor(-16777216);
        ((TextView)localObject3).setSingleLine(true);
        localObject4 = ai.h();
        ((LinearLayout.LayoutParams)localObject4).setMargins(b.a(this.c, 35.0F), b.a(this.c, 17.0F), b.a(this.c, 35.0F), b.a(this.c, 17.0F));
        ((TextView)localObject3).setLayoutParams((ViewGroup.LayoutParams)localObject4);
        ((LinearLayout)localObject2).addView((View)localObject3);
        ((bt)localObject1).a((View)localObject2);
        break;
        label744: localObject1 = d.b((Context)localObject5);
        ((j)localObject2).a("umpuuid", (String)localObject1);
        break label190;
      }
    }
  }

  public final void a(int paramInt)
  {
    if (this.b.isFinishing())
      return;
    this.d.a();
    a.a(this.c, com.umpay.creditcard.android.f.a(aj.class), "10200002", "网络异常", false);
    d.a(this.b, "提示", "网络异常，请重试", new ak(this), new al(this));
  }

  public final void a(int paramInt, Object paramObject)
  {
    if (this.b.isFinishing())
      return;
    this.d.a();
    j localj = new j(this.b);
    Object localObject = com.umpay.creditcard.android.util.f.a((String)paramObject);
    if (localObject != null)
    {
      paramObject = new ad();
      if (((Map)localObject).get("retCode") != null)
        paramObject.o(((Map)localObject).get("retCode").toString());
      if (((Map)localObject).get("retMsg") != null)
        paramObject.p(((Map)localObject).get("retMsg").toString());
      if (((Map)localObject).get("terminalId") != null)
        paramObject.c(((Map)localObject).get("terminalId").toString());
      if (((Map)localObject).get("newestUmpVer") != null)
        paramObject.d(((Map)localObject).get("newestUmpVer").toString());
      if (((Map)localObject).get("newestUmpSeq") != null)
        paramObject.e(((Map)localObject).get("newestUmpSeq").toString());
      if (((Map)localObject).get("newestUpopSeq") != null)
        paramObject.g(((Map)localObject).get("newestUpopSeq").toString());
      if (((Map)localObject).get("newestUpopVer") != null)
        paramObject.f(((Map)localObject).get("newestUpopVer").toString());
      if (((Map)localObject).get("merId") != null)
        paramObject.h(((Map)localObject).get("merId").toString());
      if (((Map)localObject).get("orderId") != null)
        paramObject.i(((Map)localObject).get("orderId").toString());
      if (((Map)localObject).get("amount") != null)
        paramObject.j(((Map)localObject).get("amount").toString());
      if (((Map)localObject).get("merName") != null)
        paramObject.k(((Map)localObject).get("merName").toString());
      if (((Map)localObject).get("goodsName") != null)
        paramObject.l(((Map)localObject).get("goodsName").toString());
      if (((Map)localObject).get("orderDate") != null)
        paramObject.m(((Map)localObject).get("orderDate").toString());
      if (((Map)localObject).get("cardId") != null)
        paramObject.n(((Map)localObject).get("cardId").toString());
      if (((Map)localObject).get("gateId") != null)
        paramObject.a(((Map)localObject).get("gateId").toString());
      if (((Map)localObject).get("mobileId") != null)
        paramObject.b(((Map)localObject).get("mobileId").toString());
      if (((Map)localObject).get("rpid") != null)
        paramObject.q(((Map)localObject).get("rpid").toString());
      if (((Map)localObject).get("hisBankList") != null)
        paramObject.a((ArrayList)((Map)localObject).get("hisBankList"));
      a.a(paramObject.g());
      a.a(this.c, com.umpay.creditcard.android.f.a(aj.class), "10200002", "(" + paramObject.i() + ")" + paramObject.j(), false);
      if (!"0000".equals(paramObject.i()))
      {
        if ("00040008".equals(paramObject.i()))
        {
          localj.a("terminalId", "");
          localj.a("umpuuid", "");
        }
        d.a(this.b, "提示", paramObject.j(), new am(this), new an(this));
        return;
      }
      this.b.e = paramObject;
      localObject = paramObject.b();
      if ((localObject != null) && (!"".equals(localObject)))
      {
        localj.a("terminalId", (String)localObject);
        a.c((String)localObject);
      }
      if (!TextUtils.isEmpty(paramObject.d()))
      {
        localj.a("newestUmpSeq", paramObject.d());
        if (!TextUtils.isEmpty(paramObject.c()))
          localj.a("newestUmpVer", paramObject.c());
      }
      if (!TextUtils.isEmpty(paramObject.f()))
      {
        localj.a("newestUpopSeq", paramObject.f());
        if (!TextUtils.isEmpty(paramObject.e()))
          localj.a("newestUpopVer", paramObject.e());
      }
      paramObject = this.b;
      a.e();
      a.a(this.c, com.umpay.creditcard.android.f.a(aj.class));
      this.b.b();
      return;
    }
    a.a(this.c, com.umpay.creditcard.android.f.a(aj.class), "10200002", d.c((String)paramObject), false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.umpay.paysdk.meituan.aj
 * JD-Core Version:    0.6.0
 */