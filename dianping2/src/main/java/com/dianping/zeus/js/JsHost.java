package com.dianping.zeus.js;

import android.content.Context;
import android.content.Intent;
import com.dianping.zeus.js.jshandler.JsHandler;
import com.dianping.zeus.ui.ITitleBar;
import com.dianping.zeus.widget.BaseTitleBar;
import org.json.JSONObject;

public abstract interface JsHost
{
  public abstract int configDomainPermission(String paramString);

  public abstract void finish();

  public abstract void ga();

  public abstract Context getContext();

  public abstract int getDefaultBackIcon();

  public abstract int getDefaultCloseIcon();

  public abstract int getDefaultCustomBackIcon();

  public abstract int getDefaultSearchIcon();

  public abstract int getDefaultShareIcon();

  public abstract int getDomainPermission();

  public abstract JsHandler getJsHandler(String paramString);

  public abstract ITitleBar getTitleBarHost();

  public abstract boolean isOnScroll();

  public abstract void loadJs(String paramString);

  public abstract void loadUrl(String paramString);

  public abstract void post(Runnable paramRunnable);

  public abstract void publish(String paramString);

  public abstract void publish(JSONObject paramJSONObject);

  public abstract void putJsHandler(JsHandler paramJsHandler);

  public abstract void replaceTitleBar(BaseTitleBar paramBaseTitleBar);

  public abstract String requestId();

  public abstract void resetJsHandler();

  public abstract void setBackgroundColor(int paramInt);

  public abstract void setOnScroll(boolean paramBoolean);

  public abstract void startActivity(Intent paramIntent);

  public abstract void startActivityForResult(Intent paramIntent, int paramInt);

  public abstract void subscribe(String paramString, JsHandler paramJsHandler);

  public abstract void unsubscribe(String paramString);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.zeus.js.JsHost
 * JD-Core Version:    0.6.0
 */