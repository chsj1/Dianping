package com.umpay.creditcard.android;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class d
  implements DialogInterface.OnClickListener
{
  d(UmpayActivity paramUmpayActivity)
  {
  }

  public final void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    com.umpay.paysdk.meituan.a.a(this.a, f.a(UmpayActivity.a(this.a).a().getClass()));
    this.a.a("1001", "用户取消");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.umpay.creditcard.android.d
 * JD-Core Version:    0.6.0
 */