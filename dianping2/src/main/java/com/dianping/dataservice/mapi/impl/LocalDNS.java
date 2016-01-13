package com.dianping.dataservice.mapi.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.dianping.dataservice.http.TimeoutConfigHelper;
import com.dianping.monitor.MonitorService;
import com.dianping.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.List<Ljava.lang.String;>;
import javax.net.ssl.HttpsURLConnection;

public class LocalDNS
{
  private static Handler handler = new Handler(Looper.getMainLooper());
  private final Runnable callback = new Runnable()
  {
    public void run()
    {
      Object localObject3 = LocalDNS.this.tasks;
      if ((localObject3 == null) || (((List)localObject3).isEmpty()));
      while (true)
      {
        return;
        int i = 0;
        Object localObject1 = null;
        long l = 9223372036854775807L;
        Iterator localIterator = ((List)localObject3).iterator();
        Object localObject2;
        while (localIterator.hasNext())
        {
          localObject2 = (LocalDNS.Ping)localIterator.next();
          if (((LocalDNS.Ping)localObject2).done)
          {
            if ((((LocalDNS.Ping)localObject2).result == null) || (((LocalDNS.Ping)localObject2).doneTime >= l))
              continue;
            localObject1 = localObject2;
            l = ((LocalDNS.Ping)localObject2).doneTime;
            continue;
          }
          i += 1;
        }
        l = SystemClock.elapsedRealtime() - LocalDNS.this.lastTime;
        if (localObject1 != null)
        {
          localObject2 = ((List)localObject3).iterator();
          while (((Iterator)localObject2).hasNext())
          {
            localObject3 = (LocalDNS.Ping)((Iterator)localObject2).next();
            if (((LocalDNS.Ping)localObject3).done)
              continue;
            ((LocalDNS.Ping)localObject3).abort();
          }
          LocalDNS.access$302(LocalDNS.this, null);
          Log.i("local_dns", "ping " + localObject1.ip + " succeed in " + LocalDNS.this.name);
          LocalDNS.this.prefs.edit().putString(LocalDNS.this.name, localObject1.ip).putString("ip", localObject1.result).commit();
          if (LocalDNS.this.monitor == null)
            continue;
          i = 0;
        }
        try
        {
          int j = localObject1.ip.lastIndexOf('.');
          j = Integer.parseInt(localObject1.ip.substring(j + 1));
          i = j;
          label302: LocalDNS.this.monitor.pv(0L, "local_dns", 0, 0, i, 0, 0, (int)l);
          return;
          if (i != 0)
            continue;
          LocalDNS.access$302(LocalDNS.this, null);
          Log.i("local_dns", "ping all ip failed in " + LocalDNS.this.name);
          LocalDNS.this.prefs.edit().remove(LocalDNS.this.name).commit();
          LocalDNS.this.monitor.pv(0L, "local_dns", 0, 0, -1, 0, 0, (int)l);
          return;
        }
        catch (Exception localException)
        {
          break label302;
        }
      }
    }
  };
  private Context context;
  private Handler httpsHandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramMessage)
    {
      super.handleMessage(paramMessage);
      int i = paramMessage.what;
      long l1 = SystemClock.elapsedRealtime();
      long l2 = LocalDNS.this.httpsStartTime;
      Log.i("ip.txt", "ping https://mapi.dianping.com/ip.txt succeed in " + LocalDNS.this.name);
      LocalDNS.this.monitor.pv(0L, "ip.txt", 0, 8, i, 0, paramMessage.arg1, (int)(l1 - l2));
    }
  };
  private long httpsStartTime;
  private long lastTime = 9223372036854775807L;
  private MonitorService monitor;
  private String name;
  private SharedPreferences prefs;
  private List<Ping> tasks;
  public long timeout = 300000L;
  private TimeoutConfigHelper timeoutConfig;

  public LocalDNS(String paramString, Context paramContext, MonitorService paramMonitorService, TimeoutConfigHelper paramTimeoutConfigHelper)
  {
    this.name = paramString;
    this.monitor = paramMonitorService;
    this.context = paramContext;
    this.timeoutConfig = paramTimeoutConfigHelper;
    this.prefs = paramContext.getSharedPreferences("local_dns", 0);
  }

  private void abortAll()
  {
    Object localObject = this.tasks;
    this.tasks = null;
    if ((localObject == null) || (((List)localObject).isEmpty()));
    while (true)
    {
      return;
      localObject = ((List)localObject).iterator();
      while (((Iterator)localObject).hasNext())
      {
        Ping localPing = (Ping)((Iterator)localObject).next();
        if (localPing.done)
          continue;
        localPing.abort();
      }
    }
  }

  private List<String> getIpList()
  {
    ArrayList localArrayList = new ArrayList();
    try
    {
      Object localObject1 = this.prefs.getString("ip", null);
      ((String)localObject1).charAt(0);
      localObject1 = new BufferedReader(new StringReader((String)localObject1));
      while (true)
      {
        localObject3 = ((BufferedReader)localObject1).readLine();
        if (localObject3 == null)
          break;
        localObject3 = ((String)localObject3).trim();
        if ((((String)localObject3).length() < 6) || (((String)localObject3).charAt(0) == '#'))
          continue;
        localArrayList.add(localObject3);
      }
    }
    catch (Exception localException2)
    {
      Object localObject3;
      if (localArrayList.isEmpty())
        try
        {
          Object localObject2 = this.context.getAssets().open("ip.txt");
          localObject3 = new byte[4096];
          localObject2 = new BufferedReader(new StringReader(new String(localObject3, 0, ((InputStream)localObject2).read(localObject3), "utf-8")));
          while (true)
          {
            localObject3 = ((BufferedReader)localObject2).readLine();
            if (localObject3 == null)
              break;
            localObject3 = ((String)localObject3).trim();
            if ((((String)localObject3).length() < 6) || (((String)localObject3).charAt(0) == '#'))
              continue;
            localArrayList.add(localObject3);
          }
        }
        catch (Exception localException2)
        {
        }
    }
    return (List<String>)(List<String>)(List<String>)localArrayList;
  }

  private void sendIpTextHttps()
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        Object localObject3 = null;
        InputStream localInputStream4 = null;
        InputStream localInputStream3 = localInputStream4;
        Object localObject1 = localObject3;
        InputStream localInputStream2;
        try
        {
          Log.i("ip.txt", "start ping https ip list");
          localInputStream3 = localInputStream4;
          localObject1 = localObject3;
          LocalDNS.access$002(LocalDNS.this, SystemClock.elapsedRealtime());
          localInputStream3 = localInputStream4;
          localObject1 = localObject3;
          HttpsURLConnection localHttpsURLConnection = (HttpsURLConnection)new URL("https://mapi.dianping.com/ip.txt").openConnection();
          localInputStream3 = localInputStream4;
          localObject1 = localObject3;
          int i = LocalDNS.this.timeoutConfig.getHttpsTimeout();
          if (i > 0);
          while (true)
          {
            localInputStream3 = localInputStream4;
            localObject1 = localObject3;
            localHttpsURLConnection.setConnectTimeout(i);
            localInputStream3 = localInputStream4;
            localObject1 = localObject3;
            localHttpsURLConnection.setReadTimeout(i);
            localInputStream3 = localInputStream4;
            localObject1 = localObject3;
            localHttpsURLConnection.setDoInput(true);
            localInputStream3 = localInputStream4;
            localObject1 = localObject3;
            i = localHttpsURLConnection.getResponseCode();
            localInputStream3 = localInputStream4;
            localObject1 = localObject3;
            int j = localHttpsURLConnection.getContentLength();
            localInputStream3 = localInputStream4;
            localObject1 = localObject3;
            localInputStream4 = localHttpsURLConnection.getInputStream();
            localInputStream3 = localInputStream4;
            localObject1 = localInputStream4;
            localInputStream4.read();
            localInputStream3 = localInputStream4;
            localObject1 = localInputStream4;
            LocalDNS.this.httpsHandler.sendMessage(LocalDNS.this.httpsHandler.obtainMessage(i, j, 0));
            if (localInputStream4 != null);
            try
            {
              localInputStream4.close();
              return;
              i = 60000;
            }
            catch (IOException localIOException1)
            {
              localIOException1.printStackTrace();
              return;
            }
          }
        }
        catch (Exception localException)
        {
          InputStream localInputStream1 = localInputStream3;
          localException.printStackTrace();
          localInputStream1 = localInputStream3;
          if ((localException instanceof SocketTimeoutException))
          {
            localInputStream1 = localInputStream3;
            LocalDNS.this.httpsHandler.sendEmptyMessage(-103);
          }
          while (localInputStream3 != null)
          {
            try
            {
              localInputStream3.close();
              return;
            }
            catch (IOException localIOException2)
            {
              localIOException2.printStackTrace();
              return;
            }
            localInputStream2 = localInputStream3;
            LocalDNS.this.httpsHandler.sendEmptyMessage(-105);
          }
        }
        finally
        {
          if (localInputStream2 == null);
        }
        try
        {
          localInputStream2.close();
          throw localObject2;
        }
        catch (IOException localIOException3)
        {
          while (true)
            localIOException3.printStackTrace();
        }
      }
    }).start();
  }

  private void startPingIfNecessary()
  {
    monitorenter;
    while (true)
    {
      try
      {
        long l1 = SystemClock.elapsedRealtime();
        long l2 = this.lastTime;
        long l3 = this.timeout;
        if (l1 - l2 < l3)
          return;
        this.lastTime = l1;
        abortAll();
        StringBuilder localStringBuilder = new StringBuilder(this.name);
        localObject3 = getIpList();
        if (((List)localObject3).isEmpty())
        {
          this.prefs.edit().remove(this.name).commit();
          Log.i("local_dns", "ip list is empty, use mapi.dianping.com");
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      localObject1.append(" starts ping for ");
      Object localObject2 = new ArrayList(((List)localObject3).size());
      Object localObject3 = ((List)localObject3).iterator();
      while (((Iterator)localObject3).hasNext())
      {
        String str = (String)((Iterator)localObject3).next();
        ((List)localObject2).add(new Ping(str));
        localObject1.append(str).append(',');
      }
      this.tasks = ((List)localObject2);
      localObject2 = ((List)localObject2).iterator();
      while (((Iterator)localObject2).hasNext())
        ((Ping)((Iterator)localObject2).next()).start();
      sendIpTextHttps();
      Log.i("local_dns", localObject1.toString());
    }
  }

  String getCurrentIp()
  {
    if (this.lastTime == 0L)
      return null;
    return this.prefs.getString(this.name, null);
  }

  public String getHost()
  {
    String str2 = this.prefs.getString(this.name, null);
    String str1 = str2;
    if (str2 == null)
      str1 = "mapi.dianping.com";
    startPingIfNecessary();
    return str1;
  }

  public void reset()
  {
    long l = SystemClock.elapsedRealtime() - this.lastTime;
    if ((l >= 0L) && (l < 2000L))
      return;
    abortAll();
    this.lastTime = 0L;
  }

  boolean verify(String paramString)
    throws Exception
  {
    for (paramString = paramString.replace("\r\n", "\n"); paramString.length() > 0; paramString = paramString.substring(0, paramString.length() - 1))
    {
      i = paramString.charAt(paramString.length() - 1);
      if ((i != 32) && (i != 10) && (i != 13) && (i != 9))
        break;
    }
    int i = paramString.lastIndexOf("#signature:");
    if (i < 0)
      return false;
    byte[] arrayOfByte = Base64.decode(paramString.substring("#signature:".length() + i));
    paramString = paramString.substring(0, i).getBytes("UTF-8");
    PublicKey localPublicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger("126249047213733400394832927062191718124106502252016376335239201017879741479212826889792269518812011930954260815405734979029054948897271479164405774366450123492170170778643868244690050242889774706020933108419828451869350849709235837709591309556789408682416107830970490153274673044649213948647637707735591672551"), new BigInteger("65537")));
    Signature localSignature = Signature.getInstance("SHA1WithRSA");
    localSignature.initVerify(localPublicKey);
    localSignature.update(paramString);
    return localSignature.verify(arrayOfByte);
  }

  private class Ping extends Thread
  {
    boolean abort;
    boolean done;
    long doneTime;
    String ip;
    String result;

    public Ping(String arg2)
    {
      super();
      Object localObject;
      this.ip = localObject;
    }

    private String getIp(String paramString)
      throws Exception
    {
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("GET /ip.txt HTTP/1.1\nHost: ");
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("\n\n");
      paramString = new Socket(paramString, 80);
      try
      {
        paramString.getOutputStream().write(((StringBuilder)localObject1).toString().getBytes("ascii"));
        localObject1 = new byte[4096];
        localObject1 = new String(localObject1, 0, paramString.getInputStream().read(localObject1), "ascii");
        int i = ((String)localObject1).indexOf("\r\n\r\n");
        if (i > 0)
        {
          localObject1 = ((String)localObject1).substring(i + 4);
          return localObject1;
        }
        i = ((String)localObject1).indexOf("\n\n");
        if (i > 0)
        {
          localObject1 = ((String)localObject1).substring(i + 2);
          return localObject1;
        }
        return null;
      }
      finally
      {
        paramString.close();
      }
      throw localObject2;
    }

    public void abort()
    {
      this.abort = true;
    }

    public void run()
    {
      try
      {
        String str = getIp(this.ip);
        if (LocalDNS.this.verify(str))
          this.result = str;
        while (true)
        {
          this.done = true;
          this.doneTime = SystemClock.elapsedRealtime();
          if (!this.abort)
            break;
          return;
          Log.e("local_dns", "invalid signature in http://" + this.ip + "/ip.txt");
        }
      }
      catch (Exception localException)
      {
        while (true)
        {
          this.done = true;
          this.doneTime = SystemClock.elapsedRealtime();
        }
      }
      finally
      {
        this.done = true;
        this.doneTime = SystemClock.elapsedRealtime();
      }
      LocalDNS.handler.removeCallbacks(LocalDNS.this.callback);
      LocalDNS.handler.post(LocalDNS.this.callback);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.impl.LocalDNS
 * JD-Core Version:    0.6.0
 */