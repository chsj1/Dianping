package com.dianping.dataservice.mapi.impl;

import android.os.SystemClock;
import android.util.Base64;
import com.dianping.dataservice.http.HttpResponse;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.NameValuePair;

class HttpRequestInfoLogger extends BaseLogger
{
  private static final int LEAD_BYTE = 0;
  private static final int TRAIL_BYTE = 2;
  private static final int TRAIL_BYTE_1 = 1;
  static final int[] bytesFromUTF8 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5 };
  private static final AtomicLong time = new AtomicLong();
  private String name;
  private HttpResponse resp;
  private int tunnel;

  public HttpRequestInfoLogger(String paramString1, String paramString2, String paramString3, HttpResponse paramHttpResponse, int paramInt, String paramString4)
  {
    super(paramString2, paramString3, paramString4);
    this.name = paramString1;
    this.tunnel = paramInt;
    this.resp = paramHttpResponse;
  }

  public static boolean isText(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return false;
    int k = 0;
    int i2 = paramArrayOfByte.length;
    int m = 0;
    int j = 0;
    int i = 0;
    label19: if (k < 0 + i2)
    {
      int i1 = paramArrayOfByte[k] & 0xFF;
      switch (i)
      {
      default:
      case 0:
      case 1:
      case 2:
      }
      while (true)
      {
        k += 1;
        break label19;
        int n = i1;
        j = bytesFromUTF8[i1];
        switch (j)
        {
        default:
          return false;
        case 0:
          m = n;
          if (n > 127)
            return false;
        case 1:
          if ((n < 194) || (n > 223))
            break;
          i = 1;
          m = n;
          break;
        case 2:
          if ((n < 224) || (n > 239))
            break;
          i = 1;
          m = n;
          break;
        case 3:
          if ((n < 240) || (n > 244))
            break;
          i = 1;
          m = n;
          continue;
          if (((m == 240) && (i1 < 144)) || ((m == 244) && (i1 > 143)) || ((m == 224) && (i1 < 160)) || ((m == 237) && (i1 > 159)) || (i1 < 128) || (i1 > 191))
            break;
          j -= 1;
          if (j == 0)
          {
            i = 0;
            continue;
          }
          i = 2;
        }
      }
    }
    return true;
  }

  public static boolean setAndStart(long paramLong)
  {
    long l1 = time.get();
    long l2 = SystemClock.elapsedRealtime();
    if (l2 > l1 + paramLong)
      return time.compareAndSet(l1, l2);
    return false;
  }

  public byte[] buildLogInfo()
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte;
    boolean bool;
    try
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[[[").append(this.name).append(" - ");
      localStringBuilder.append(this.resp.statusCode());
      localStringBuilder.append(": ").append(this.command).append('\n');
      arrayOfByte = (byte[])(byte[])this.resp.result();
      bool = isText(arrayOfByte);
      localStringBuilder.append("Dpid: ").append(this.dpid).append('\n');
      localStringBuilder.append("Network: ").append(this.netInfo).append('\n');
      localStringBuilder.append("Tunnel: ").append(this.tunnel).append('\n');
      if (this.resp.headers() == null)
        break label238;
      Iterator localIterator = this.resp.headers().iterator();
      while (localIterator.hasNext())
      {
        NameValuePair localNameValuePair = (NameValuePair)localIterator.next();
        localStringBuilder.append(localNameValuePair.getName()).append(": ").append(localNameValuePair.getValue()).append('\n');
      }
    }
    catch (Exception localException)
    {
    }
    return localByteArrayOutputStream.toByteArray();
    label238: localException.append('\n');
    localByteArrayOutputStream.write(localException.toString().getBytes("US-ASCII"));
    if (bool)
      localByteArrayOutputStream.write(arrayOfByte);
    while (true)
    {
      localByteArrayOutputStream.write("]]]\n\n".getBytes("US-ASCII"));
      break;
      localByteArrayOutputStream.write(Base64.encode(arrayOfByte, 0));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.impl.HttpRequestInfoLogger
 * JD-Core Version:    0.6.0
 */