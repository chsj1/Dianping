package com.tencent.open;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebChromeClient;

public abstract class c extends Dialog
{
  protected a jsBridge;

  @SuppressLint({"NewApi"})
  protected final WebChromeClient mChromeClient = new b(this);

  public c(Context paramContext)
  {
    super(paramContext);
  }

  public c(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }

  protected abstract void onConsoleMessage(String paramString);

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.jsBridge = new a();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.open.c
 * JD-Core Version:    0.6.0
 */