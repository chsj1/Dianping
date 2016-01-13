package com.dianping.dataservice.mapi.impl;

import com.dianping.util.Log;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class BaseLogger
  implements Runnable
{
  protected String command;
  protected String dpid;
  protected byte[] logInfo;
  protected String netInfo;

  public BaseLogger(String paramString1, String paramString2, String paramString3)
  {
    this.dpid = paramString1;
    this.netInfo = paramString3;
    this.command = paramString2;
  }

  public static String getCommand(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return "";
    int j = paramString.indexOf('?');
    int i = j;
    if (j < 0)
      i = paramString.length();
    int k = paramString.lastIndexOf('/', i);
    j = k;
    if (k < 0)
      j = -1;
    return paramString.substring(j + 1, i);
  }

  public abstract byte[] buildLogInfo();

  public void run()
  {
    try
    {
      HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL("http://114.80.165.63/broker-service/log").openConnection();
      localHttpURLConnection.addRequestProperty("Content-Type", "text/plain");
      localHttpURLConnection.setDoOutput(true);
      localHttpURLConnection.setRequestMethod("POST");
      localHttpURLConnection.setReadTimeout(15000);
      localHttpURLConnection.getOutputStream().write(buildLogInfo());
      int i = localHttpURLConnection.getResponseCode();
      localHttpURLConnection.disconnect();
      Log.i("cat " + getClass() + " log " + i);
      return;
    }
    catch (Exception localException)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.impl.BaseLogger
 * JD-Core Version:    0.6.0
 */