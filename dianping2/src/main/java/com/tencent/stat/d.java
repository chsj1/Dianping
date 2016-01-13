package com.tencent.stat;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import com.tencent.stat.common.StatLogger;
import com.tencent.stat.common.k;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.List<Ljava.lang.String;>;
import java.util.zip.GZIPOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

class d
{
  private static StatLogger c = k.b();
  private static long d = -1L;
  private static d e = null;
  private static Context f = null;
  DefaultHttpClient a = null;
  Handler b = null;

  private d()
  {
    try
    {
      Object localObject = new HandlerThread("StatDispatcher");
      ((HandlerThread)localObject).start();
      d = ((HandlerThread)localObject).getId();
      this.b = new Handler(((HandlerThread)localObject).getLooper());
      localObject = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout((HttpParams)localObject, 10000);
      HttpConnectionParams.setSoTimeout((HttpParams)localObject, 10000);
      this.a = new DefaultHttpClient((HttpParams)localObject);
      this.a.setKeepAliveStrategy(new e(this));
      if (StatConfig.b() != null)
        this.a.getParams().setParameter("http.route.default-proxy", StatConfig.b());
      return;
    }
    catch (Throwable localThrowable)
    {
      c.e(localThrowable);
    }
  }

  static Context a()
  {
    return f;
  }

  static void a(Context paramContext)
  {
    f = paramContext.getApplicationContext();
  }

  static d b()
  {
    monitorenter;
    try
    {
      if (e == null)
        e = new d();
      d locald = e;
      return locald;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  void a(com.tencent.stat.a.e parame, c paramc)
  {
    b(Arrays.asList(new String[] { parame.d() }), paramc);
  }

  void a(List<String> paramList, c paramc)
  {
    int j = 0;
    Object localObject2;
    int i;
    Object localObject1;
    Object localObject3;
    long l;
    while (true)
    {
      ByteArrayOutputStream localByteArrayOutputStream;
      try
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("[");
        i = 0;
        if (i >= paramList.size())
          continue;
        ((StringBuilder)localObject2).append((String)paramList.get(i));
        if (i == paramList.size() - 1)
          break label1085;
        ((StringBuilder)localObject2).append(",");
        break label1085;
        ((StringBuilder)localObject2).append("]");
        paramList = StatConfig.getStatReportUrl();
        c.i("[" + paramList + "]Send request(" + ((StringBuilder)localObject2).toString().length() + "bytes):" + ((StringBuilder)localObject2).toString());
        localObject1 = new HttpPost(paramList);
        ((HttpPost)localObject1).addHeader("Accept-Encoding", "gzip");
        ((HttpPost)localObject1).setHeader("Connection", "Keep-Alive");
        ((HttpPost)localObject1).removeHeaders("Cache-Control");
        localObject3 = k.a(f);
        i = j;
        if (localObject3 == null)
          continue;
        this.a.getParams().setParameter("http.route.default-proxy", k.a(f));
        ((HttpPost)localObject1).addHeader("X-Online-Host", "pingma.qq.com:80");
        ((HttpPost)localObject1).addHeader("Accept", "*/*");
        ((HttpPost)localObject1).addHeader("Content-Type", "json");
        i = 1;
        localByteArrayOutputStream = new ByteArrayOutputStream();
        paramList = ((StringBuilder)localObject2).toString().getBytes("UTF-8");
        j = paramList.length;
        if (((StringBuilder)localObject2).length() < 256)
        {
          if (localObject3 != null)
            continue;
          ((HttpPost)localObject1).addHeader("Content-Encoding", "rc4");
          ((HttpPost)localObject1).setEntity(new ByteArrayEntity(com.tencent.stat.common.e.a(paramList)));
          paramList = this.a.execute((HttpUriRequest)localObject1);
          if (i == 0)
            continue;
          this.a.getParams().removeParameter("http.route.default-proxy");
          localObject1 = paramList.getEntity();
          i = paramList.getStatusLine().getStatusCode();
          l = ((HttpEntity)localObject1).getContentLength();
          c.i("recv response status code:" + i + ", content length:" + l);
          if (l != 0L)
            break;
          EntityUtils.toString((HttpEntity)localObject1);
          if (i != 200)
            break label596;
          if (paramc == null)
            continue;
          paramc.a();
          label425: localByteArrayOutputStream.close();
          return;
          ((HttpPost)localObject1).addHeader("X-Content-Encoding", "rc4");
          continue;
        }
      }
      catch (Throwable paramList)
      {
        c.e(paramList);
        if (paramc == null)
          break label1094;
        try
        {
          paramc.b();
          return;
        }
        catch (Throwable paramList)
        {
          c.e(paramList);
          return;
        }
      }
      finally
      {
      }
      if (localObject3 == null)
        ((HttpPost)localObject1).addHeader("Content-Encoding", "rc4,gzip");
      while (true)
      {
        localByteArrayOutputStream.write(new byte[4]);
        localObject2 = new GZIPOutputStream(localByteArrayOutputStream);
        ((GZIPOutputStream)localObject2).write(paramList);
        ((GZIPOutputStream)localObject2).close();
        paramList = localByteArrayOutputStream.toByteArray();
        ByteBuffer.wrap(paramList, 0, 4).putInt(j);
        c.d("before Gzip:" + j + " bytes, after Gzip:" + paramList.length + " bytes");
        break;
        ((HttpPost)localObject1).addHeader("X-Content-Encoding", "rc4,gzip");
      }
      label596: c.error("Server response error code:" + i);
    }
    if (l > 0L)
    {
      localObject2 = ((HttpEntity)localObject1).getContent();
      localObject3 = new DataInputStream((InputStream)localObject2);
      localObject1 = new byte[(int)((HttpEntity)localObject1).getContentLength()];
      ((DataInputStream)localObject3).readFully(localObject1);
      ((InputStream)localObject2).close();
      ((DataInputStream)localObject3).close();
      localObject3 = paramList.getFirstHeader("Content-Encoding");
      paramList = (List<String>)localObject1;
      if (localObject3 != null)
      {
        if (((Header)localObject3).getValue().equalsIgnoreCase("gzip,rc4"))
          paramList = com.tencent.stat.common.e.b(k.a(localObject1));
      }
      else
        label719: if (i != 200)
          break label1028;
    }
    while (true)
    {
      try
      {
        paramList = new String(paramList, "UTF-8");
        c.d(paramList);
        localObject1 = new JSONObject(paramList);
        if (((JSONObject)localObject1).isNull("cfg"))
          continue;
        StatConfig.a(((JSONObject)localObject1).getJSONObject("cfg"));
        if ((((JSONObject)localObject1).isNull("et")) || (((JSONObject)localObject1).isNull("st")))
          continue;
        c.d("get mid respone:" + paramList);
        if (((JSONObject)localObject1).getInt("et") != com.tencent.stat.a.f.b.a())
          continue;
        i = ((JSONObject)localObject1).getInt("st");
        switch (i)
        {
        case -1:
          c.e("error type for st:" + i);
          if (paramc == null)
            continue;
          paramc.a();
          ((InputStream)localObject2).close();
          break;
          if (!((Header)localObject3).getValue().equalsIgnoreCase("rc4,gzip"))
            continue;
          paramList = k.a(com.tencent.stat.common.e.b(localObject1));
          break label719;
          if (!((Header)localObject3).getValue().equalsIgnoreCase("gzip"))
            continue;
          paramList = k.a(localObject1);
          break label719;
          paramList = (List<String>)localObject1;
          if (!((Header)localObject3).getValue().equalsIgnoreCase("rc4"))
            break label719;
          paramList = com.tencent.stat.common.e.b(localObject1);
          break;
        case 0:
          if (((JSONObject)localObject1).isNull("mid"))
            continue;
          StatMid.updateDeviceInfo(f, ((JSONObject)localObject1).getString("mid"));
          continue;
        }
      }
      catch (Throwable paramList)
      {
        c.i(paramList.toString());
        continue;
      }
      label1028: c.error("Server response error code:" + i + ", error:" + new String(paramList, "UTF-8"));
      continue;
      EntityUtils.toString((HttpEntity)localObject1);
      break label425;
      label1085: i += 1;
      break;
      label1094: return;
    }
  }

  void b(List<String> paramList, c paramc)
  {
    if ((paramList.isEmpty()) || (this.b == null))
      return;
    this.b.post(new f(this, paramList, paramc));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.stat.d
 * JD-Core Version:    0.6.0
 */