package com.github.mmin18.layoutcast.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class EmbedHttpServer
  implements Runnable
{
  private int port;
  private ServerSocket serverSocket;

  public EmbedHttpServer(int paramInt)
  {
    this.port = paramInt;
  }

  protected void handle(String paramString1, String paramString2, HashMap<String, String> paramHashMap, InputStream paramInputStream, ResponseOutputStream paramResponseOutputStream)
    throws Exception
  {
  }

  public void run()
  {
    ServerSocket localServerSocket = this.serverSocket;
    while (true)
      if (localServerSocket == this.serverSocket)
      {
        Object localObject1 = null;
        try
        {
          localSocket = localServerSocket.accept();
          String str2 = null;
          String str1 = null;
          localObject1 = localSocket;
          localHashMap = new HashMap();
          localObject1 = localSocket;
          Object localObject3 = localSocket.getInputStream();
          localObject1 = localSocket;
          localObject4 = new StringBuilder(512);
          while (true)
          {
            localObject1 = localSocket;
            i = ((InputStream)localObject3).read();
            if (i != -1)
            {
              if (i != 10)
                break label443;
              localObject1 = localSocket;
              if (((StringBuilder)localObject4).length() > 0)
              {
                localObject1 = localSocket;
                if (((StringBuilder)localObject4).charAt(((StringBuilder)localObject4).length() - 1) == '\r')
                {
                  localObject1 = localSocket;
                  ((StringBuilder)localObject4).setLength(((StringBuilder)localObject4).length() - 1);
                }
              }
              localObject1 = localSocket;
              if (((StringBuilder)localObject4).length() != 0);
            }
            else
            {
              i = 0;
              localObject1 = localSocket;
              localObject4 = (String)localHashMap.get("Content-Length");
              if (localObject4 != null)
              {
                localObject1 = localSocket;
                i = Integer.parseInt((String)localObject4);
              }
              localObject1 = localSocket;
              localObject4 = localSocket.getOutputStream();
              localObject1 = localSocket;
              if ("100-Continue".equalsIgnoreCase((String)localHashMap.get("Expect")))
              {
                localObject1 = localSocket;
                ((OutputStream)localObject4).write("HTTP/1.1 100 Continue\r\n\r\n".getBytes("ASCII"));
                localObject1 = localSocket;
                ((OutputStream)localObject4).flush();
              }
              localObject1 = localSocket;
              localObject3 = new BodyInputStream((InputStream)localObject3, i);
              localObject1 = localSocket;
              localObject4 = new ResponseOutputStream((OutputStream)localObject4);
              localObject1 = localSocket;
              handle(str2, str1, localHashMap, (InputStream)localObject3, (ResponseOutputStream)localObject4);
              localObject1 = localSocket;
              ((ResponseOutputStream)localObject4).close();
              localObject1 = localSocket;
              localSocket.close();
              if ((localServerSocket.isBound()) && (!localServerSocket.isClosed()))
                break;
              this.serverSocket = null;
              break;
            }
            if (str2 != null)
              break label396;
            localObject1 = localSocket;
            i = ((StringBuilder)localObject4).indexOf(" ");
            localObject1 = localSocket;
            str2 = ((StringBuilder)localObject4).substring(0, i);
            localObject1 = localSocket;
            str1 = ((StringBuilder)localObject4).substring(i + 1, ((StringBuilder)localObject4).lastIndexOf(" HTTP/")).trim();
            localObject1 = localSocket;
            ((StringBuilder)localObject4).setLength(0);
          }
        }
        catch (Exception localException2)
        {
          while (true)
          {
            Socket localSocket;
            HashMap localHashMap;
            Object localObject4;
            if (localObject1 == null)
              continue;
            try
            {
              localObject1.close();
            }
            catch (Exception localException1)
            {
            }
            continue;
            label396: Object localObject2 = localSocket;
            int i = ((StringBuilder)localObject4).indexOf(":");
            localObject2 = localSocket;
            localHashMap.put(((StringBuilder)localObject4).substring(0, i).trim(), ((StringBuilder)localObject4).substring(i + 1).trim());
            continue;
            label443: localObject2 = localSocket;
            ((StringBuilder)localObject4).append((char)i);
          }
        }
      }
  }

  public void start()
    throws IOException
  {
    if (this.serverSocket == null)
    {
      this.serverSocket = new ServerSocket(this.port);
      new Thread(this, "embed-http-server").start();
    }
  }

  public void stop()
    throws IOException
  {
    if (this.serverSocket != null)
    {
      this.serverSocket.close();
      this.serverSocket = null;
    }
  }

  private static class BodyInputStream extends InputStream
  {
    private InputStream ins;
    private int n;

    public BodyInputStream(InputStream paramInputStream, int paramInt)
    {
      this.ins = paramInputStream;
      this.n = paramInt;
    }

    public int available()
      throws IOException
    {
      return this.n;
    }

    public void close()
      throws IOException
    {
      this.ins.close();
    }

    public void mark(int paramInt)
    {
      monitorenter;
      try
      {
        throw new UnsupportedOperationException();
      }
      finally
      {
      }
      throw localObject;
    }

    public boolean markSupported()
    {
      return false;
    }

    public int read()
      throws IOException
    {
      int i;
      if (this.n <= 0)
        i = -1;
      int j;
      do
      {
        return i;
        j = this.ins.read();
        i = j;
      }
      while (j == -1);
      this.n -= 1;
      return j;
    }

    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (this.n <= 0)
      {
        paramInt1 = -1;
        return paramInt1;
      }
      InputStream localInputStream = this.ins;
      if (paramInt2 < this.n);
      while (true)
      {
        paramInt2 = localInputStream.read(paramArrayOfByte, paramInt1, paramInt2);
        paramInt1 = paramInt2;
        if (paramInt2 == -1)
          break;
        this.n -= paramInt2;
        return paramInt2;
        paramInt2 = this.n;
      }
    }

    public void reset()
      throws IOException
    {
      monitorenter;
      try
      {
        throw new IOException("unsupported");
      }
      finally
      {
      }
      throw localObject;
    }

    public long skip(long paramLong)
      throws IOException
    {
      throw new IOException("unsupported");
    }
  }

  public static class ResponseOutputStream extends OutputStream
  {
    private static final byte[] CRLF = { 13, 10 };
    private int lv;
    private OutputStream os;

    public ResponseOutputStream(OutputStream paramOutputStream)
    {
      this.os = paramOutputStream;
    }

    public void close()
      throws IOException
    {
      if (this.lv < 1)
        setStatusCode(404);
      if (this.lv < 2)
      {
        this.os.write(CRLF);
        this.lv = 2;
      }
      if (this.lv < 3)
      {
        this.os.close();
        this.lv = 3;
      }
    }

    public void flush()
      throws IOException
    {
      this.os.flush();
    }

    public void setContentEncoding(String paramString)
      throws IOException
    {
      setHeader("Content-Encoding", paramString);
    }

    public void setContentLength(int paramInt)
      throws IOException
    {
      setHeader("Content-Length", String.valueOf(paramInt));
    }

    public void setContentType(String paramString)
      throws IOException
    {
      setHeader("Content-Type", paramString);
    }

    public void setContentTypeBinary()
      throws IOException
    {
      setContentType("application/octet-stream");
    }

    public void setContentTypeHtml()
      throws IOException
    {
      setContentType("text/html");
    }

    public void setContentTypeHtmlUtf8()
      throws IOException
    {
      setContentType("text/html; charset=utf-8");
    }

    public void setContentTypeJpeg()
      throws IOException
    {
      setContentType("image/jpeg");
    }

    public void setContentTypeJson()
      throws IOException
    {
      setContentType("application/json");
    }

    public void setContentTypePng()
      throws IOException
    {
      setContentType("image/png");
    }

    public void setContentTypeText()
      throws IOException
    {
      setContentType("text/plain");
    }

    public void setContentTypeTextUtf8()
      throws IOException
    {
      setContentType("text/plain; charset=utf-8");
    }

    public void setContentTypeXml()
      throws IOException
    {
      setContentType("text/xml");
    }

    public void setContentTypeZip()
      throws IOException
    {
      setContentType("application/zip");
    }

    public void setHeader(String paramString1, String paramString2)
      throws IOException
    {
      if (this.lv < 1)
        setStatusCode(200);
      if (this.lv == 1)
      {
        this.os.write(paramString1.getBytes("ASCII"));
        this.os.write(58);
        this.os.write(32);
        this.os.write(paramString2.getBytes("ASCII"));
        this.os.write(CRLF);
        return;
      }
      throw new IOException("headers is already set");
    }

    public void setStatusCode(int paramInt)
      throws IOException
    {
      switch (paramInt)
      {
      default:
        setStatusLine(String.valueOf(paramInt));
        return;
      case 200:
        setStatusLine("200 OK");
        return;
      case 201:
        setStatusLine("201 Created");
        return;
      case 202:
        setStatusLine("202 Accepted");
        return;
      case 301:
        setStatusLine("301 Moved Permanently");
        return;
      case 304:
        setStatusLine("304 Not Modified");
        return;
      case 400:
        setStatusLine("400 Bad Request");
        return;
      case 401:
        setStatusLine("401 Unauthorized");
        return;
      case 403:
        setStatusLine("403 Forbidden");
        return;
      case 404:
        setStatusLine("404 Not Found");
        return;
      case 405:
        setStatusLine("405 Method Not Allowed");
        return;
      case 500:
        setStatusLine("500 Internal Server Error");
        return;
      case 501:
      }
      setStatusLine("501 Not Implemented");
    }

    public void setStatusLine(String paramString)
      throws IOException
    {
      if (this.lv == 0)
      {
        this.os.write("HTTP/1.1 ".getBytes("ASCII"));
        this.os.write(paramString.getBytes("ASCII"));
        this.os.write(CRLF);
        this.lv = 1;
        return;
      }
      throw new IOException("status line is already set");
    }

    public void write(int paramInt)
      throws IOException
    {
      if (this.lv < 1)
        setStatusCode(200);
      if (this.lv < 2)
      {
        this.os.write(CRLF);
        this.lv = 2;
      }
      this.os.write(paramInt);
    }

    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (this.lv < 1)
        setStatusCode(200);
      if (this.lv < 2)
      {
        this.os.write(CRLF);
        this.lv = 2;
      }
      this.os.write(paramArrayOfByte, paramInt1, paramInt2);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.github.mmin18.layoutcast.util.EmbedHttpServer
 * JD-Core Version:    0.6.0
 */