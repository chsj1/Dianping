package com.umpay.creditcard.android;

import com.umpay.paysdk.meituan.ao;
import com.umpay.paysdk.meituan.aq;
import com.umpay.paysdk.meituan.as;

public final class f
{
  public static String a(Class<? extends Object> paramClass)
  {
    String str = null;
    if (ao.class.getSimpleName().equals(paramClass.getSimpleName()))
      str = "支付要素页面";
    do
    {
      return str;
      if (aq.class.getSimpleName().equals(paramClass.getSimpleName()))
        return "支付要素页面";
    }
    while (!as.class.getSimpleName().equals(paramClass.getSimpleName()));
    return "支付要素页面";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.umpay.creditcard.android.f
 * JD-Core Version:    0.6.0
 */