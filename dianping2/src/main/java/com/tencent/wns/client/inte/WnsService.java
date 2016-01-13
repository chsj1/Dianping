package com.tencent.wns.client.inte;

import android.app.Application;
import java.net.URL;
import java.util.HashMap;
import org.apache.http.conn.scheme.SocketFactory;

public abstract interface WnsService
{
  public static final int HTTP_UNAVAILABLE = 503;
  public static final String KEY_HTTP_CMD = "wns-http-reqcmd";
  public static final String KEY_HTTP_RESULT = "wns-http-result";

  public abstract void bind(String paramString, IWnsCallback.WnsBindCallback paramWnsBindCallback);

  public abstract void cancelRequest(int paramInt);

  public abstract WnsAsyncHttpRequest createWnsAsyncHttpRequest(int paramInt, String paramString);

  public abstract void enableDebugMode(boolean paramBoolean);

  public abstract String getStatus();

  public abstract long getWid();

  public abstract URL getWnsHttpUrl(String paramString);

  public abstract int getWnsServicePid();

  public abstract SocketFactory getWnsSocketFactory();

  public abstract void initAndStartWns(Application paramApplication, WnsAppInfo paramWnsAppInfo);

  public abstract void initWnsWithAppInfo(int paramInt, String paramString1, String paramString2, boolean paramBoolean);

  public abstract void initWnsWithAppInfo(int paramInt1, String paramString1, String paramString2, boolean paramBoolean, int paramInt2);

  public abstract void reportDebugLog(String paramString, long paramLong, HashMap paramHashMap);

  public abstract void reset();

  public abstract int sendRequest(String paramString, int paramInt, byte[] paramArrayOfByte, IWnsCallback.WnsTransferCallback paramWnsTransferCallback);

  public abstract void setBackgroundMode(boolean paramBoolean);

  public abstract void setDebugIp(String paramString, int paramInt);

  public abstract void setStatusCallback(WnsService.WnsSDKStatusListener paramWnsSDKStatusListener);

  public abstract void startWnsService();

  public abstract void stopWnsService();

  public abstract void unbind(String paramString, IWnsCallback.WnsUnbindCallback paramWnsUnbindCallback);

  public static abstract interface GlobalListener
  {
    public abstract void onPrintLog(int paramInt, String paramString1, String paramString2, Throwable paramThrowable);

    public abstract void showDialog(String paramString1, String paramString2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.client.inte.WnsService
 * JD-Core Version:    0.6.0
 */