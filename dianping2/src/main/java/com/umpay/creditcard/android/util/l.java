package com.umpay.creditcard.android.util;

import android.graphics.Color;
import android.os.Handler;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class l
{
  private static l c;
  private List<Button> a = new ArrayList();
  private int b = 60;
  private Thread d = null;
  private int e = 0;
  private boolean f = false;
  private int g = 0;
  private Handler h = new m(this);
  private Runnable i = new n(this);

  public static l a()
  {
    monitorenter;
    try
    {
      if (c == null)
        c = new l();
      l locall = c;
      return locall;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public final void a(Button paramButton)
  {
    if ((this.f) && (this.e > 0))
    {
      paramButton.setEnabled(false);
      paramButton.setText(this.e);
      paramButton.setTextColor(Color.parseColor("#848584"));
    }
    this.a.add(paramButton);
  }

  public final void b()
  {
    if (this.f)
      return;
    this.f = true;
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      Button localButton = (Button)localIterator.next();
      if (localButton == null)
        continue;
      localButton.setEnabled(false);
    }
    this.d = new Thread(this.i);
    this.d.start();
  }

  public final void c()
  {
    this.f = false;
    this.d = null;
    this.a.clear();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.umpay.creditcard.android.util.l
 * JD-Core Version:    0.6.0
 */