package com.dianping.dataservice.mapi.impl;

import android.os.Environment;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.SimpleMsg;
import com.dianping.util.URLBase64;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;

public class LogTool
{
  static final DateFormat FMT_DATE;
  static final DateFormat FMT_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  static File logFile;

  static
  {
    FMT_DATE = new SimpleDateFormat("yyyyMMdd.HHmm");
    logFile = null;
  }

  public static void log(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, MApiResponse paramMApiResponse)
  {
    try
    {
      if (logFile == null)
        logFile = new File(Environment.getExternalStorageDirectory(), FMT_DATE.format(new Date()) + ".log");
      FileOutputStream localFileOutputStream = new FileOutputStream(logFile, true);
      OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(localFileOutputStream, "UTF-8");
      log(localOutputStreamWriter, paramHttpRequest, paramHttpResponse, paramMApiResponse);
      localOutputStreamWriter.close();
      localFileOutputStream.close();
      return;
    }
    catch (Exception paramHttpRequest)
    {
    }
  }

  public static void log(Writer paramWriter, HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, MApiResponse paramMApiResponse)
    throws IOException
  {
    paramWriter.append("========== ").append(FMT_TIME.format(new Date())).append(" ==========\n");
    paramWriter.append(paramHttpRequest.method()).append(" ").append(paramHttpRequest.url()).append('\n');
    Object localObject;
    if (paramHttpRequest.headers() != null)
    {
      localObject = paramHttpRequest.headers().iterator();
      while (((Iterator)localObject).hasNext())
      {
        NameValuePair localNameValuePair = (NameValuePair)((Iterator)localObject).next();
        paramWriter.append(localNameValuePair.getName()).append(": ").append(localNameValuePair.getValue()).append('\n');
      }
    }
    paramWriter.append('\n');
    if (paramHttpRequest.input() != null)
      paramWriter.append(String.valueOf(paramHttpRequest.input()));
    paramWriter.append('\n').append('\n');
    paramWriter.append("" + paramHttpResponse.statusCode()).append('\n');
    if (paramHttpResponse.headers() != null)
    {
      paramHttpRequest = paramHttpResponse.headers().iterator();
      while (paramHttpRequest.hasNext())
      {
        localObject = (NameValuePair)paramHttpRequest.next();
        paramWriter.append(((NameValuePair)localObject).getName()).append(": ").append(((NameValuePair)localObject).getValue()).append('\n');
      }
    }
    paramWriter.append('\n');
    if (paramHttpResponse.error() != null)
    {
      if ((paramHttpResponse.error() instanceof Exception))
      {
        paramHttpRequest = new PrintWriter(paramWriter);
        ((Exception)paramHttpResponse.error()).printStackTrace(paramHttpRequest);
        paramHttpRequest.close();
      }
      while (true)
      {
        paramWriter.append('\n');
        paramWriter.append('\n').append('\n');
        if (paramMApiResponse != null)
          break;
        return;
        paramWriter.append(String.valueOf(paramHttpResponse.error()));
      }
    }
    paramHttpRequest = (byte[])(byte[])paramHttpResponse.result();
    int m = Math.min(paramHttpRequest.length, 512);
    int j = 0;
    int i = 0;
    while (i < m)
    {
      int n = paramHttpRequest[i];
      int k;
      if (((n < 32) || (n > 126)) && (n != 10) && (n != 13))
      {
        k = j;
        if (n != 9);
      }
      else
      {
        k = j + 1;
      }
      i += 1;
      j = k;
    }
    if (j * 100 / m > 95)
      paramWriter.append(new String(paramHttpRequest, "UTF-8"));
    while (true)
    {
      paramWriter.append('\n');
      break;
      paramWriter.append(URLBase64.encode(paramHttpRequest));
    }
    paramWriter.append("" + paramMApiResponse.statusCode()).append('\n');
    if (paramMApiResponse.error() != null)
    {
      if ((paramMApiResponse.error() instanceof SimpleMsg))
        paramWriter.append(String.valueOf(paramMApiResponse.error()));
      paramWriter.append('\n').append('\n');
      return;
    }
    if ((paramMApiResponse.result() instanceof DPObject))
      paramWriter.append("DPObject, type=" + Integer.toHexString(((DPObject)paramMApiResponse.result()).getClassHash16()));
    while (true)
    {
      paramWriter.append('\n');
      break;
      if ((paramMApiResponse.result() instanceof DPObject[]))
      {
        paramHttpRequest = (DPObject[])(DPObject[])paramMApiResponse.result();
        if (paramHttpRequest.length == 0)
        {
          paramWriter.append("DPObject[0]\n");
          continue;
        }
        paramWriter.append("DPObject[" + paramHttpRequest.length + "], type=" + Integer.toHexString(paramHttpRequest[0].getClassHash16()));
        continue;
      }
      if (!(paramMApiResponse.result() instanceof String))
        continue;
      paramWriter.append((String)paramMApiResponse.result());
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.impl.LogTool
 * JD-Core Version:    0.6.0
 */