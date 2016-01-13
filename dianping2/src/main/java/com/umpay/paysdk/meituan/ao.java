package com.umpay.paysdk.meituan;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.umpay.creditcard.android.UmpayActivity;
import com.umpay.creditcard.android.a;
import com.umpay.creditcard.android.util.b;
import com.umpay.creditcard.android.util.o;
import java.io.PrintStream;
import java.util.ArrayList;

public class ao extends RelativeLayout
  implements View.OnClickListener
{
  private UmpayActivity a;
  private as b;
  private ar c;

  public ao(UmpayActivity paramUmpayActivity, String paramString)
  {
    super(paramUmpayActivity);
    this.a = paramUmpayActivity;
    setLayoutParams(new LinearLayout.LayoutParams(ah.a));
    setBackgroundResource(o.a(paramUmpayActivity, "color", "ump_content_bg"));
    Object localObject1 = new RelativeLayout(this.a);
    ((RelativeLayout)localObject1).setId(1);
    paramUmpayActivity = new RelativeLayout.LayoutParams(ah.c);
    paramUmpayActivity.addRule(10);
    ((RelativeLayout)localObject1).setLayoutParams(paramUmpayActivity);
    paramUmpayActivity.height = b.a(this.a, 48.0F);
    ((RelativeLayout)localObject1).setBackgroundResource(o.a(this.a, "color", "ump_title_bg"));
    ((RelativeLayout)localObject1).setPadding(0, 0, 0, 0);
    Object localObject2 = new TextView(this.a);
    Object localObject3 = ai.e();
    ((RelativeLayout.LayoutParams)localObject3).addRule(1, 4);
    ((RelativeLayout.LayoutParams)localObject3).addRule(15);
    ((TextView)localObject2).setTextColor(o.a(this.a, "color", "ump_color_bank_title_text"));
    if (("1".equals(this.a.c)) || ("2".equals(this.a.c)))
      paramUmpayActivity = paramString + "信用卡 ";
    while (true)
    {
      ((TextView)localObject2).setText(paramUmpayActivity);
      ((TextView)localObject2).setTextSize(24.0F * b.a);
      ((TextView)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject3);
      ((RelativeLayout)localObject1).addView((View)localObject2);
      localObject3 = new ImageView(this.a);
      ((ImageView)localObject3).setImageResource(o.a(this.a, "drawable", "ump_header_back_btn_normal"));
      paramString = new ImageView(this.a);
      paramString.setImageResource(o.a(this.a, "drawable", "ump_icon_title"));
      localObject2 = this.a;
      paramUmpayActivity = new LinearLayout((Context)localObject2);
      ai.h();
      paramUmpayActivity.setOrientation(0);
      LinearLayout.LayoutParams localLayoutParams = ai.h();
      localLayoutParams.leftMargin = b.a((Context)localObject2, 6.0F);
      localLayoutParams.rightMargin = b.a((Context)localObject2, 2.0F);
      paramUmpayActivity.addView((View)localObject3);
      ((ImageView)localObject3).setLayoutParams(localLayoutParams);
      ((ImageView)localObject3).setFocusable(false);
      ((ImageView)localObject3).setFocusableInTouchMode(false);
      localObject3 = ai.h();
      ((LinearLayout.LayoutParams)localObject3).rightMargin = b.a((Context)localObject2, 4.0F);
      paramString.setLayoutParams((ViewGroup.LayoutParams)localObject3);
      paramString.setFocusable(false);
      paramString.setFocusableInTouchMode(false);
      paramUmpayActivity.addView(paramString);
      paramUmpayActivity.setClickable(true);
      paramUmpayActivity.setFocusableInTouchMode(false);
      paramUmpayActivity.setFocusable(false);
      paramUmpayActivity.setGravity(16);
      paramString = new StateListDrawable();
      localObject2 = new ColorDrawable(2147483647);
      paramString.addState(new int[] { 16842919 }, (Drawable)localObject2);
      localObject2 = new ColorDrawable(0);
      paramString.addState(new int[] { 16842910 }, (Drawable)localObject2);
      paramUmpayActivity.setBackgroundDrawable(paramString);
      paramString = new RelativeLayout.LayoutParams(ah.b);
      paramString.addRule(9);
      paramString.addRule(15);
      paramUmpayActivity.setOnClickListener(this);
      paramUmpayActivity.setLayoutParams(paramString);
      paramUmpayActivity.setId(4);
      ((RelativeLayout)localObject1).addView(paramUmpayActivity);
      paramUmpayActivity.post(new ap(this, paramString, (RelativeLayout)localObject1));
      addView((View)localObject1);
      paramUmpayActivity = new LinearLayout(this.a);
      paramString = new RelativeLayout.LayoutParams(ah.a);
      paramString.addRule(3, 1);
      paramUmpayActivity.setLayoutParams(paramString);
      addView(paramUmpayActivity);
      paramUmpayActivity = new a(this.a, paramUmpayActivity, this);
      this.a.a(paramUmpayActivity);
      int i = b.a(this.a, 20.0F);
      paramString = new ScrollView(this.a);
      paramString.setLayoutParams(a());
      paramString.setFillViewport(true);
      paramString.setVerticalScrollBarEnabled(false);
      localObject1 = new LinearLayout(this.a);
      ((LinearLayout)localObject1).setLayoutParams(a());
      ((LinearLayout)localObject1).setOrientation(1);
      this.c = new ar(this.a);
      ((LinearLayout)localObject1).addView(this.c);
      localObject2 = new LinearLayout(this.a);
      localObject3 = a();
      ((LinearLayout.LayoutParams)localObject3).leftMargin = i;
      ((LinearLayout.LayoutParams)localObject3).rightMargin = i;
      ((LinearLayout)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject3);
      this.b = new as(this.a, this.a.c);
      ((LinearLayout)localObject2).addView(this.b.i());
      ((LinearLayout)localObject1).addView((View)localObject2);
      paramString.addView((View)localObject1);
      localObject1 = new aq(this.a);
      ((aq)localObject1).addView(paramString);
      try
      {
        paramUmpayActivity.a((View)localObject1);
        return;
        paramUmpayActivity = paramString + "储蓄卡 ";
      }
      catch (Exception paramUmpayActivity)
      {
        paramUmpayActivity.printStackTrace();
      }
    }
  }

  private static LinearLayout.LayoutParams a()
  {
    return new LinearLayout.LayoutParams(-1, -1);
  }

  public final void a(String paramString)
  {
    this.c.a(paramString);
    paramString = this.a.e.a();
    if ((paramString == null) || (paramString.size() <= 0));
    for (paramString = null; ; paramString = (ac)paramString.get(0))
    {
      if (paramString != null)
      {
        String str1 = paramString.a();
        String str2 = paramString.c();
        if ((!TextUtils.isEmpty(str1)) && (this.a.c.equals(str2)))
        {
          paramString = paramString.b();
          this.b.a(str1, paramString);
        }
      }
      return;
    }
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
      return;
    case 4:
    }
    System.out.println("点击返回按钮");
    this.a.a();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.umpay.paysdk.meituan.ao
 * JD-Core Version:    0.6.0
 */