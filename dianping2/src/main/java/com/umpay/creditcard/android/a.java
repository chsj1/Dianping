package com.umpay.creditcard.android;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import com.umpay.paysdk.meituan.ao;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class a
{
  private ViewGroup a;
  private View b = null;
  private int c = 0;
  private Map<String, View> d = new LinkedHashMap();
  private List<String> e = new ArrayList();
  private Context f = null;
  private ao g = null;

  public a(Context paramContext, ViewGroup paramViewGroup, ao paramao)
  {
    this.f = paramContext;
    this.a = paramViewGroup;
    this.g = paramao;
  }

  private void a(View paramView1, View paramView2)
  {
    if (paramView2 != null);
    for (paramView2 = f.a(paramView1.getClass()); ; paramView2 = "支付要素页面")
    {
      com.umpay.paysdk.meituan.a.a(this.f, f.a(paramView1.getClass()), "10000001", paramView2, false);
      return;
    }
  }

  private void b(View paramView)
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)this.f.getSystemService("input_method");
    if ((this.f instanceof Activity))
    {
      localInputMethodManager.hideSoftInputFromWindow(((Activity)this.f).getWindow().getDecorView().getWindowToken(), 2);
      return;
    }
    localInputMethodManager.hideSoftInputFromWindow(paramView.getWindowToken(), 2);
  }

  public final View a()
  {
    return this.b;
  }

  public final void a(View paramView)
  {
    if (paramView == null)
      throw new Exception("view is null");
    b(this.b);
    this.a.removeAllViews();
    a(paramView, this.b);
    this.a.addView(paramView);
    View localView;
    Object localObject;
    if (this.b != null)
    {
      localView = this.b;
      if (localView != null)
      {
        localObject = localView.getTag();
        if ((localObject == null) || ("".equals(localObject.toString())))
          break label118;
        this.e.add(localObject.toString());
        this.d.put(localObject.toString(), localView);
      }
    }
    while (true)
    {
      this.b = paramView;
      return;
      label118: localObject = new StringBuilder("default");
      int i = this.c;
      this.c = (i + 1);
      localObject = i;
      this.e.add(localObject);
      this.d.put(localObject, localView);
    }
  }

  public final void a(b paramb)
  {
    int i;
    Object localObject;
    if (this.e.size() > 0)
    {
      i = 1;
      if (i == 0)
        break label136;
      localObject = (String)this.e.remove(this.e.size() - 1);
      localObject = (View)this.d.remove(localObject);
      label56: if (localObject == null)
        break label141;
      b((View)localObject);
      this.a.removeAllViews();
      if (paramb != null)
        paramb.a((View)localObject);
      a((View)localObject, this.b);
      this.a.addView((View)localObject);
      this.b = ((View)localObject);
      this.b.clearFocus();
      if (this.b.hasFocus());
    }
    label136: label141: label168: 
    do
    {
      this.b.requestFocus();
      return;
      while (this.b == null)
      {
        i = 0;
        break;
        localObject = null;
        break label56;
        if (paramb == null)
          break label168;
        paramb.a((View)localObject);
      }
      b(this.b);
      return;
    }
    while (!(this.f instanceof Activity));
    ((Activity)this.f).finish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.umpay.creditcard.android.a
 * JD-Core Version:    0.6.0
 */