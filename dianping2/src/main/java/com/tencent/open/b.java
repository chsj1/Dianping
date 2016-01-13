package com.tencent.open;

import android.os.Build.VERSION;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

class b extends WebChromeClient
{
  b(c paramc)
  {
  }

  public void onConsoleMessage(String paramString1, int paramInt, String paramString2)
  {
    Log.i("WebConsole", paramString1 + " -- From 222 line " + paramInt + " of " + paramString2);
    if (Build.VERSION.SDK_INT == 7)
      this.a.onConsoleMessage(paramString1);
  }

  public boolean onConsoleMessage(ConsoleMessage paramConsoleMessage)
  {
    Log.i("WebConsole", paramConsoleMessage.message() + " -- From  111 line " + paramConsoleMessage.lineNumber() + " of " + paramConsoleMessage.sourceId());
    c localc;
    if (Build.VERSION.SDK_INT > 7)
    {
      localc = this.a;
      if (paramConsoleMessage != null)
        break label74;
    }
    label74: for (paramConsoleMessage = ""; ; paramConsoleMessage = paramConsoleMessage.message())
    {
      localc.onConsoleMessage(paramConsoleMessage);
      return true;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.open.b
 * JD-Core Version:    0.6.0
 */