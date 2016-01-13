package com.dianping.tunnel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.json.JSONObject;

public class TunnelConnection
{
  Exception error;
  int failCode;
  private String headers = "{}";
  long lastPingRespTime;
  private long lastPingRtt;
  long lastPingSendTime;
  final Socket socket;
  final Tunnel tunnel;

  public TunnelConnection(Tunnel paramTunnel, Socket paramSocket)
  {
    this.tunnel = paramTunnel;
    this.socket = paramSocket;
    new TunnelConnection.1(this, "tunnel_in").start();
  }

  private void log(String paramString)
  {
    this.tunnel.log(this + " " + paramString);
  }

  private boolean loggable()
  {
    return this.tunnel.loggable();
  }

  private int read(InputStream paramInputStream, StringBuilder paramStringBuilder, ByteArrayOutputStream paramByteArrayOutputStream)
    throws IOException
  {
    paramStringBuilder.setLength(0);
    if (paramByteArrayOutputStream != null)
      paramByteArrayOutputStream.reset();
    byte[] arrayOfByte = new byte[4096];
    int i = 0;
    while (true)
    {
      int j = paramInputStream.read();
      if (j != -1)
      {
        if (j != 0)
          break label383;
        if (i == 0)
          i = 0;
      }
      do
      {
        while (true)
        {
          return i;
          j = arrayOfByte[0];
          paramStringBuilder.append(new String(arrayOfByte, 1, i - 1, "UTF-8"));
          if ((j < 100) || (j >= 200))
            break;
          i = paramInputStream.read();
          int k = paramInputStream.read();
          int m = paramInputStream.read();
          int n = paramInputStream.read();
          if ((i < 0) || (k < 0) || (m < 0) || (n < 0))
          {
            if (loggable())
              log("<EOF");
            return -1;
          }
          k = i << 24 | (k & 0xFF) << 16 | (m & 0xFF) << 8 | n & 0xFF;
          if (k > 4194304)
            throw new IOException("buffer > 4m");
          arrayOfByte = new byte[4096];
          i = 0;
          while (i < k)
          {
            m = paramInputStream.read(arrayOfByte, 0, Math.min(k - i, arrayOfByte.length));
            if (paramByteArrayOutputStream != null)
              paramByteArrayOutputStream.write(arrayOfByte, 0, m);
            if (m == -1)
            {
              if (loggable())
                log("<EOF");
              return -1;
            }
            i += m;
          }
          i = j;
          if (!loggable())
            continue;
          log("<" + j + paramStringBuilder + "   " + k + " bytes");
          return j;
        }
        i = j;
      }
      while (!loggable());
      log("<" + j + paramStringBuilder);
      return j;
      label383: if (i == arrayOfByte.length - 1)
      {
        if (loggable())
          log("<OVERFLOW");
        return -1;
      }
      arrayOfByte[i] = (byte)j;
      i += 1;
    }
  }

  private long timestamp()
  {
    return this.tunnel.timestamp();
  }

  private void write(OutputStream paramOutputStream, int paramInt, String paramString)
    throws IOException
  {
    write(paramOutputStream, paramInt, paramString, null, 0, 0);
  }

  private void write(OutputStream paramOutputStream, int paramInt1, String paramString, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
    throws IOException
  {
    if (paramInt1 == 0)
      paramOutputStream.write(0);
    do
      while (true)
      {
        return;
        paramOutputStream.write(paramInt1);
        paramOutputStream.write(paramString.getBytes("UTF-8"));
        paramOutputStream.write(0);
        if ((paramInt1 < 100) || (paramInt1 >= 200))
          break;
        paramOutputStream.write((byte)(paramInt3 >>> 24));
        paramOutputStream.write((byte)(paramInt3 >>> 16));
        paramOutputStream.write((byte)(paramInt3 >>> 8));
        paramOutputStream.write((byte)paramInt3);
        if (paramInt3 > 0)
          paramOutputStream.write(paramArrayOfByte, paramInt2, paramInt3);
        if (!loggable())
          continue;
        log(">" + paramInt1 + paramString + "   " + paramInt3 + " bytes");
        return;
      }
    while (!loggable());
    log(">" + paramInt1 + paramString);
  }

  public void close()
  {
    try
    {
      this.socket.close();
      label7: this.tunnel.postBroke(this);
      return;
    }
    catch (Exception localException)
    {
      break label7;
    }
  }

  public void loadbalance()
    throws Exception
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{d:\"").append(this.tunnel.getDpid()).append("\",");
    String str = this.tunnel.getToken();
    if (str != null)
      localStringBuilder.append("t:\"").append(str).append("\",");
    localStringBuilder.append("b:\"1\",");
    localStringBuilder.append("v:\"").append(this.tunnel.getVersion()).append("\"}");
    write(this.socket.getOutputStream(), 80, localStringBuilder.toString());
  }

  public void ping()
    throws Exception
  {
    this.lastPingSendTime = timestamp();
    write(this.socket.getOutputStream(), 0, null);
  }

  public void register()
    throws Exception
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{d:\"").append(this.tunnel.getDpid()).append("\",");
    String str = this.tunnel.getToken();
    if (str != null)
      localStringBuilder.append("t:\"").append(str).append("\",");
    localStringBuilder.append("b:\"1\",");
    localStringBuilder.append("v:\"").append(this.tunnel.getVersion()).append("\"}");
    write(this.socket.getOutputStream(), 1, localStringBuilder.toString());
  }

  public int rtt()
  {
    if (this.lastPingSendTime == 0L)
      return 2147483647;
    long l1 = this.lastPingRespTime - this.lastPingSendTime;
    if (l1 < 0L)
    {
      l1 = timestamp();
      long l2 = this.lastPingSendTime;
      return (int)Math.min(2147483647L, Math.max(this.lastPingRtt, l1 - l2));
    }
    return (int)Math.min(2147483647L, l1);
  }

  public void send(TunnelRequest paramTunnelRequest)
    throws Exception
  {
    Object localObject1;
    Object localObject2;
    label103: byte[] arrayOfByte;
    if (paramTunnelRequest.headers == null)
    {
      localObject1 = "{}";
      if (!((String)localObject1).equals(this.headers))
      {
        write(this.socket.getOutputStream(), 101, (String)localObject1, null, 0, 0);
        this.headers = ((String)localObject1);
      }
      localObject2 = new JSONObject();
      ((JSONObject)localObject2).put("m", paramTunnelRequest.method);
      ((JSONObject)localObject2).put("u", paramTunnelRequest.url);
      ((JSONObject)localObject2).put("i", paramTunnelRequest.id);
      if (paramTunnelRequest.timeout <= 0)
        break label157;
      ((JSONObject)localObject2).put("t", paramTunnelRequest.timeout);
      localObject1 = this.socket.getOutputStream();
      localObject2 = ((JSONObject)localObject2).toString();
      arrayOfByte = paramTunnelRequest.buffer;
      if (paramTunnelRequest.buffer != null)
        break label185;
    }
    label157: label185: for (int i = 0; ; i = paramTunnelRequest.buffer.length)
    {
      write((OutputStream)localObject1, 102, (String)localObject2, arrayOfByte, 0, i);
      return;
      localObject1 = paramTunnelRequest.headers.toString();
      break;
      if (this.tunnel.defaultServerTimeout() <= 0)
        break label103;
      ((JSONObject)localObject2).put("t", this.tunnel.defaultServerTimeout());
      break label103;
    }
  }

  public String toString()
  {
    if (this.socket.getRemoteSocketAddress() == null)
      return this.socket.toString();
    return this.socket.getRemoteSocketAddress().toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tunnel.TunnelConnection
 * JD-Core Version:    0.6.0
 */