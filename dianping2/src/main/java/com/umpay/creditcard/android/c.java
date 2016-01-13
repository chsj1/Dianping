package com.umpay.creditcard.android;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;
import com.umpay.paysdk.meituan.ai;

final class c
  implements b
{
  c(UmpayActivity paramUmpayActivity)
  {
  }

  public final void a(View paramView)
  {
    if (paramView != null)
    {
      int i = this.a.getResources().getConfiguration().orientation;
      ((ai)paramView).a();
      com.umpay.paysdk.meituan.a.a(this.a, f.a(UmpayActivity.a(this.a).a().getClass()));
    }
    while (true)
    {
      if (paramView != null)
      {
        UmpayActivity localUmpayActivity = this.a;
        com.umpay.paysdk.meituan.a.d(f.a(paramView.getClass()));
      }
      return;
      UmpayActivity.b(this.a);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.umpay.creditcard.android.c
 * JD-Core Version:    0.6.0
 */