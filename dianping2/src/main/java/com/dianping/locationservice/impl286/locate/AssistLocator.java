package com.dianping.locationservice.impl286.locate;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import com.dianping.locationservice.impl286.model.CellModel;
import com.dianping.locationservice.impl286.model.CoordModel;
import com.dianping.locationservice.impl286.model.WifiModel;
import com.dianping.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class AssistLocator extends BaseLocator
  implements Callable<List<CoordModel>>
{
  private static final int TIMEOUT = 1000;
  protected final List<CellModel> mCellModelList = new ArrayList();
  protected ConnectivityManager mConnectManager;
  protected TelephonyManager mTelephonyManager;
  protected final List<WifiModel> mWifiModelList = new ArrayList();

  public AssistLocator(Context paramContext, List<CellModel> paramList, List<WifiModel> paramList1)
  {
    super(paramContext);
    this.mTelephonyManager = ((TelephonyManager)paramContext.getSystemService("phone"));
    this.mConnectManager = ((ConnectivityManager)paramContext.getSystemService("connectivity"));
    if (paramList != null)
      this.mCellModelList.addAll(paramList);
    if (paramList1 != null)
      this.mWifiModelList.addAll(paramList1);
  }

  private void doAssistLocate()
  {
    Object localObject5 = null;
    HttpURLConnection localHttpURLConnection1 = null;
    Object localObject4 = null;
    OutputStream localOutputStream2 = null;
    HttpURLConnection localHttpURLConnection2 = localHttpURLConnection1;
    OutputStream localOutputStream1 = localOutputStream2;
    Object localObject2 = localObject5;
    Object localObject3 = localObject4;
    try
    {
      URL localURL = new URL(getHttpPostUrl());
      localHttpURLConnection2 = localHttpURLConnection1;
      localOutputStream1 = localOutputStream2;
      localObject2 = localObject5;
      localObject3 = localObject4;
      Proxy localProxy = getProxy();
      if (localProxy != null)
      {
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localObject5;
        localObject3 = localObject4;
        localHttpURLConnection1 = (HttpURLConnection)localURL.openConnection(localProxy);
      }
      while (true)
      {
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localHttpURLConnection1;
        localObject3 = localObject4;
        localHttpURLConnection1.setUseCaches(false);
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localHttpURLConnection1;
        localObject3 = localObject4;
        localHttpURLConnection1.setConnectTimeout(1000);
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localHttpURLConnection1;
        localObject3 = localObject4;
        localHttpURLConnection1.setReadTimeout(1000);
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localHttpURLConnection1;
        localObject3 = localObject4;
        localHttpURLConnection1.setDoInput(true);
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localHttpURLConnection1;
        localObject3 = localObject4;
        localHttpURLConnection1.setDoOutput(true);
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localHttpURLConnection1;
        localObject3 = localObject4;
        localHttpURLConnection1.setRequestMethod("POST");
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localHttpURLConnection1;
        localObject3 = localObject4;
        localHttpURLConnection1.connect();
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localHttpURLConnection1;
        localObject3 = localObject4;
        localOutputStream2 = localHttpURLConnection1.getOutputStream();
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localHttpURLConnection1;
        localObject3 = localOutputStream2;
        localOutputStream2.write(formatRequest().getBytes("UTF-8"));
        localHttpURLConnection2 = localHttpURLConnection1;
        localOutputStream1 = localOutputStream2;
        localObject2 = localHttpURLConnection1;
        localObject3 = localOutputStream2;
        if (localHttpURLConnection1.getResponseCode() == 200)
        {
          localHttpURLConnection2 = localHttpURLConnection1;
          localOutputStream1 = localOutputStream2;
          localObject2 = localHttpURLConnection1;
          localObject3 = localOutputStream2;
          handleResponse(localHttpURLConnection1.getInputStream());
        }
        if (localHttpURLConnection1 != null)
          localHttpURLConnection1.disconnect();
        if (localOutputStream2 != null);
        try
        {
          localOutputStream2.close();
          return;
          localHttpURLConnection2 = localHttpURLConnection1;
          localOutputStream1 = localOutputStream2;
          localObject2 = localObject5;
          localObject3 = localObject4;
          localHttpURLConnection1 = (HttpURLConnection)localURL.openConnection();
        }
        catch (IOException localIOException1)
        {
          Log.e(localIOException1.toString());
          return;
        }
      }
    }
    catch (IOException localIOException3)
    {
      do
      {
        localObject2 = localHttpURLConnection2;
        localObject3 = localOutputStream1;
        Log.e(localIOException2.toString());
        if (localHttpURLConnection2 == null)
          continue;
        localHttpURLConnection2.disconnect();
      }
      while (localOutputStream1 == null);
      try
      {
        localOutputStream1.close();
        return;
      }
      catch (IOException localIOException3)
      {
        Log.e(localIOException3.toString());
        return;
      }
    }
    finally
    {
      if (localObject2 != null)
        ((HttpURLConnection)localObject2).disconnect();
      if (localObject3 == null);
    }
    try
    {
      ((OutputStream)localObject3).close();
      throw localObject1;
    }
    catch (IOException localIOException4)
    {
      while (true)
        Log.e(localIOException4.toString());
    }
  }

  public List<CoordModel> call()
    throws Exception
  {
    locate(null);
    return this.mResultList;
  }

  protected abstract String formatRequest();

  protected abstract String getHttpPostUrl();

  protected Proxy getProxy()
  {
    if (this.mConnectManager == null)
      return null;
    try
    {
      Object localObject1 = this.mConnectManager.getActiveNetworkInfo();
      if (localObject1 == null)
        return null;
      if (((NetworkInfo)localObject1).getType() == 1)
        return null;
      if (((NetworkInfo)localObject1).getType() == 0)
      {
        localObject1 = ((NetworkInfo)localObject1).getExtraInfo();
        if (localObject1 == null)
          return null;
        localObject1 = ((String)localObject1).toLowerCase();
        if (((String)localObject1).contains("cmnet"))
          return null;
        if (!((String)localObject1).contains("cmwap"))
          break label110;
        localObject1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
        return localObject1;
      }
    }
    catch (Exception localException)
    {
      Log.e(localException.toString());
    }
    label110: 
    do
    {
      return null;
      if (localException.contains("3gnet"))
        return null;
      if (localException.contains("3gwap"))
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
      if (localException.contains("uninet"))
        return null;
      if (localException.contains("uniwap"))
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80));
      if (localException.contains("ctnet"))
        return null;
      if (localException.contains("ctwap"))
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80));
    }
    while (!localException.contains("#777"));
    Object localObject2 = this.mContext.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), new String[] { "proxy", "port" }, null, null, null);
    if (((Cursor)localObject2).moveToFirst())
    {
      String str = ((Cursor)localObject2).getString(0);
      int i = str.length();
      if (i > 3)
      {
        i = 0;
        try
        {
          int j = Integer.parseInt(((Cursor)localObject2).getString(1));
          i = j;
          localObject2 = Proxy.Type.HTTP;
          if (i > 0)
            return new Proxy((Proxy.Type)localObject2, new InetSocketAddress(str, i));
        }
        catch (NumberFormatException localNumberFormatException)
        {
          while (true)
          {
            Log.e(localNumberFormatException.toString());
            continue;
            i = 80;
          }
        }
      }
    }
    return (Proxy)(Proxy)null;
  }

  protected abstract void handleResponse(InputStream paramInputStream);

  protected void onStartLocate()
  {
    NetworkInfo localNetworkInfo = this.mConnectManager.getActiveNetworkInfo();
    if ((localNetworkInfo == null) || (!localNetworkInfo.isConnected()))
    {
      this.mErrorMsg = "Network is not available";
      return;
    }
    doAssistLocate();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.locate.AssistLocator
 * JD-Core Version:    0.6.0
 */