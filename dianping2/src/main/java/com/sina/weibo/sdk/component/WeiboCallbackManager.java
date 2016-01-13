package com.sina.weibo.sdk.component;

import android.content.Context;
import android.text.TextUtils;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import java.util.HashMap;
import java.util.Map;

public class WeiboCallbackManager
{
  private static WeiboCallbackManager sInstance;
  private Context mContext;
  private Map<String, WeiboAuthListener> mWeiboAuthListenerMap;
  private Map<String, WidgetRequestParam.WidgetRequestCallback> mWidgetRequestCallbackMap;

  private WeiboCallbackManager(Context paramContext)
  {
    this.mContext = paramContext;
    this.mWeiboAuthListenerMap = new HashMap();
    this.mWidgetRequestCallbackMap = new HashMap();
  }

  public static WeiboCallbackManager getInstance(Context paramContext)
  {
    monitorenter;
    try
    {
      if (sInstance == null)
        sInstance = new WeiboCallbackManager(paramContext);
      paramContext = sInstance;
      return paramContext;
    }
    finally
    {
      monitorexit;
    }
    throw paramContext;
  }

  public String genCallbackKey()
  {
    return String.valueOf(System.currentTimeMillis());
  }

  public WeiboAuthListener getWeiboAuthListener(String paramString)
  {
    monitorenter;
    try
    {
      boolean bool = TextUtils.isEmpty(paramString);
      if (bool);
      for (paramString = null; ; paramString = (WeiboAuthListener)this.mWeiboAuthListenerMap.get(paramString))
        return paramString;
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }

  public WidgetRequestParam.WidgetRequestCallback getWidgetRequestCallback(String paramString)
  {
    monitorenter;
    try
    {
      boolean bool = TextUtils.isEmpty(paramString);
      if (bool);
      for (paramString = null; ; paramString = (WidgetRequestParam.WidgetRequestCallback)this.mWidgetRequestCallbackMap.get(paramString))
        return paramString;
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }

  public void removeWeiboAuthListener(String paramString)
  {
    monitorenter;
    try
    {
      boolean bool = TextUtils.isEmpty(paramString);
      if (bool);
      while (true)
      {
        return;
        this.mWeiboAuthListenerMap.remove(paramString);
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }

  public void removeWidgetRequestCallback(String paramString)
  {
    monitorenter;
    try
    {
      boolean bool = TextUtils.isEmpty(paramString);
      if (bool);
      while (true)
      {
        return;
        this.mWidgetRequestCallbackMap.remove(paramString);
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }

  public void setWeiboAuthListener(String paramString, WeiboAuthListener paramWeiboAuthListener)
  {
    monitorenter;
    try
    {
      boolean bool = TextUtils.isEmpty(paramString);
      if ((bool) || (paramWeiboAuthListener == null));
      while (true)
      {
        return;
        this.mWeiboAuthListenerMap.put(paramString, paramWeiboAuthListener);
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }

  public void setWidgetRequestCallback(String paramString, WidgetRequestParam.WidgetRequestCallback paramWidgetRequestCallback)
  {
    monitorenter;
    try
    {
      boolean bool = TextUtils.isEmpty(paramString);
      if ((bool) || (paramWidgetRequestCallback == null));
      while (true)
      {
        return;
        this.mWidgetRequestCallbackMap.put(paramString, paramWidgetRequestCallback);
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.sina.weibo.sdk.component.WeiboCallbackManager
 * JD-Core Version:    0.6.0
 */