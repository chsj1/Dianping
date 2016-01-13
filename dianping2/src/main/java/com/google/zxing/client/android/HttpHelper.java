package com.google.zxing.client.android;

import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public final class HttpHelper
{
  private static final Collection<String> REDIRECTOR_DOMAINS;
  private static final String TAG = HttpHelper.class.getSimpleName();

  static
  {
    REDIRECTOR_DOMAINS = new HashSet(Arrays.asList(new String[] { "amzn.to", "bit.ly", "bitly.com", "fb.me", "goo.gl", "is.gd", "j.mp", "lnkd.in", "ow.ly", "R.BEETAGG.COM", "r.beetagg.com", "SCN.BY", "su.pr", "t.co", "tinyurl.com", "tr.im" }));
  }

  // ERROR //
  private static CharSequence consume(URLConnection paramURLConnection, int paramInt)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 85	com/google/zxing/client/android/HttpHelper:getEncoding	(Ljava/net/URLConnection;)Ljava/lang/String;
    //   4: astore 4
    //   6: new 87	java/lang/StringBuilder
    //   9: dup
    //   10: invokespecial 88	java/lang/StringBuilder:<init>	()V
    //   13: astore_3
    //   14: aconst_null
    //   15: astore_2
    //   16: new 90	java/io/InputStreamReader
    //   19: dup
    //   20: aload_0
    //   21: invokevirtual 96	java/net/URLConnection:getInputStream	()Ljava/io/InputStream;
    //   24: aload 4
    //   26: invokespecial 99	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   29: astore_0
    //   30: sipush 1024
    //   33: newarray char
    //   35: astore_2
    //   36: aload_3
    //   37: invokevirtual 103	java/lang/StringBuilder:length	()I
    //   40: iload_1
    //   41: if_icmpge +42 -> 83
    //   44: aload_0
    //   45: aload_2
    //   46: invokevirtual 109	java/io/Reader:read	([C)I
    //   49: istore 5
    //   51: iload 5
    //   53: ifle +30 -> 83
    //   56: aload_3
    //   57: aload_2
    //   58: iconst_0
    //   59: iload 5
    //   61: invokevirtual 113	java/lang/StringBuilder:append	([CII)Ljava/lang/StringBuilder;
    //   64: pop
    //   65: goto -29 -> 36
    //   68: astore_3
    //   69: aload_0
    //   70: astore_2
    //   71: aload_3
    //   72: astore_0
    //   73: aload_2
    //   74: ifnull +7 -> 81
    //   77: aload_2
    //   78: invokevirtual 116	java/io/Reader:close	()V
    //   81: aload_0
    //   82: athrow
    //   83: aload_0
    //   84: ifnull +7 -> 91
    //   87: aload_0
    //   88: invokevirtual 116	java/io/Reader:close	()V
    //   91: aload_3
    //   92: areturn
    //   93: astore_0
    //   94: aload_3
    //   95: areturn
    //   96: astore_0
    //   97: aload_3
    //   98: areturn
    //   99: astore_2
    //   100: goto -19 -> 81
    //   103: astore_2
    //   104: goto -23 -> 81
    //   107: astore_0
    //   108: goto -35 -> 73
    //
    // Exception table:
    //   from	to	target	type
    //   30	36	68	finally
    //   36	51	68	finally
    //   56	65	68	finally
    //   87	91	93	java/io/IOException
    //   87	91	96	java/lang/NullPointerException
    //   77	81	99	java/io/IOException
    //   77	81	103	java/lang/NullPointerException
    //   16	30	107	finally
  }

  public static CharSequence downloadViaHttp(String paramString, ContentType paramContentType)
    throws IOException
  {
    return downloadViaHttp(paramString, paramContentType, 2147483647);
  }

  public static CharSequence downloadViaHttp(String paramString, ContentType paramContentType, int paramInt)
    throws IOException
  {
    switch (1.$SwitchMap$com$google$zxing$client$android$HttpHelper$ContentType[paramContentType.ordinal()])
    {
    default:
      paramContentType = "text/*,*/*";
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      return downloadViaHttp(paramString, paramContentType, paramInt);
      paramContentType = "application/xhtml+xml,text/html,text/*,*/*";
      continue;
      paramContentType = "application/json,text/*,*/*";
      continue;
      paramContentType = "application/xml,text/*,*/*";
    }
  }

  private static CharSequence downloadViaHttp(String paramString1, String paramString2, int paramInt)
    throws IOException
  {
    int i = 0;
    HttpURLConnection localHttpURLConnection;
    if (i < 5)
    {
      localHttpURLConnection = safelyOpenConnection(new URL(paramString1));
      localHttpURLConnection.setInstanceFollowRedirects(true);
      localHttpURLConnection.setRequestProperty("Accept", paramString2);
      localHttpURLConnection.setRequestProperty("Accept-Charset", "utf-8,*");
      localHttpURLConnection.setRequestProperty("User-Agent", "ZXing (Android)");
    }
    while (true)
    {
      try
      {
        int j = safelyConnect(localHttpURLConnection);
        switch (j)
        {
        case 200:
          throw new IOException("Bad HTTP response: " + j);
        case 302:
        }
      }
      finally
      {
        localHttpURLConnection.disconnect();
      }
      paramString1 = consume(localHttpURLConnection, paramInt);
      localHttpURLConnection.disconnect();
      return paramString1;
      paramString1 = localHttpURLConnection.getHeaderField("Location");
      if (paramString1 != null)
      {
        i += 1;
        localHttpURLConnection.disconnect();
        break;
      }
      throw new IOException("No Location");
      throw new IOException("Too many redirects");
    }
  }

  private static String getEncoding(URLConnection paramURLConnection)
  {
    paramURLConnection = paramURLConnection.getHeaderField("Content-Type");
    if (paramURLConnection != null)
    {
      int i = paramURLConnection.indexOf("charset=");
      if (i >= 0)
        return paramURLConnection.substring("charset=".length() + i);
    }
    return "UTF-8";
  }

  // ERROR //
  private static int safelyConnect(HttpURLConnection paramHttpURLConnection)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 228	java/net/HttpURLConnection:connect	()V
    //   4: aload_0
    //   5: invokevirtual 231	java/net/HttpURLConnection:getResponseCode	()I
    //   8: istore_1
    //   9: iload_1
    //   10: ireturn
    //   11: astore_0
    //   12: new 79	java/io/IOException
    //   15: dup
    //   16: aload_0
    //   17: invokespecial 234	java/io/IOException:<init>	(Ljava/lang/Throwable;)V
    //   20: athrow
    //   21: astore_0
    //   22: new 79	java/io/IOException
    //   25: dup
    //   26: aload_0
    //   27: invokespecial 234	java/io/IOException:<init>	(Ljava/lang/Throwable;)V
    //   30: athrow
    //   31: astore_0
    //   32: goto -20 -> 12
    //   35: astore_0
    //   36: goto -24 -> 12
    //   39: astore_0
    //   40: goto -28 -> 12
    //   43: astore_0
    //   44: goto -22 -> 22
    //   47: astore_0
    //   48: goto -26 -> 22
    //
    // Exception table:
    //   from	to	target	type
    //   0	4	11	java/lang/NullPointerException
    //   4	9	21	java/lang/NullPointerException
    //   0	4	31	java/lang/IllegalArgumentException
    //   0	4	35	java/lang/IndexOutOfBoundsException
    //   0	4	39	java/lang/SecurityException
    //   4	9	43	java/lang/StringIndexOutOfBoundsException
    //   4	9	47	java/lang/IllegalArgumentException
  }

  private static HttpURLConnection safelyOpenConnection(URL paramURL)
    throws IOException
  {
    try
    {
      URLConnection localURLConnection = paramURL.openConnection();
      if (!(localURLConnection instanceof HttpURLConnection))
        throw new IOException();
    }
    catch (NullPointerException localNullPointerException)
    {
      Log.w(TAG, "Bad URI? " + paramURL);
      throw new IOException(localNullPointerException);
    }
    return (HttpURLConnection)localNullPointerException;
  }

  // ERROR //
  public static java.net.URI unredirect(java.net.URI paramURI)
    throws IOException
  {
    // Byte code:
    //   0: getstatic 72	com/google/zxing/client/android/HttpHelper:REDIRECTOR_DOMAINS	Ljava/util/Collection;
    //   3: aload_0
    //   4: invokevirtual 259	java/net/URI:getHost	()Ljava/lang/String;
    //   7: invokeinterface 265 2 0
    //   12: ifne +5 -> 17
    //   15: aload_0
    //   16: areturn
    //   17: aload_0
    //   18: invokevirtual 269	java/net/URI:toURL	()Ljava/net/URL;
    //   21: invokestatic 150	com/google/zxing/client/android/HttpHelper:safelyOpenConnection	(Ljava/net/URL;)Ljava/net/HttpURLConnection;
    //   24: astore_1
    //   25: aload_1
    //   26: iconst_0
    //   27: invokevirtual 156	java/net/HttpURLConnection:setInstanceFollowRedirects	(Z)V
    //   30: aload_1
    //   31: iconst_0
    //   32: invokevirtual 272	java/net/HttpURLConnection:setDoInput	(Z)V
    //   35: aload_1
    //   36: ldc_w 274
    //   39: invokevirtual 277	java/net/HttpURLConnection:setRequestMethod	(Ljava/lang/String;)V
    //   42: aload_1
    //   43: ldc 168
    //   45: ldc 170
    //   47: invokevirtual 162	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   50: aload_1
    //   51: invokestatic 174	com/google/zxing/client/android/HttpHelper:safelyConnect	(Ljava/net/HttpURLConnection;)I
    //   54: istore_3
    //   55: iload_3
    //   56: tableswitch	default:+48 -> 104, 300:+54->110, 301:+54->110, 302:+54->110, 303:+54->110, 304:+48->104, 305:+48->104, 306:+48->104, 307:+54->110
    //   105: invokevirtual 189	java/net/HttpURLConnection:disconnect	()V
    //   108: aload_0
    //   109: areturn
    //   110: aload_1
    //   111: ldc 193
    //   113: invokevirtual 197	java/net/HttpURLConnection:getHeaderField	(Ljava/lang/String;)Ljava/lang/String;
    //   116: astore_2
    //   117: aload_2
    //   118: ifnull -14 -> 104
    //   121: new 256	java/net/URI
    //   124: dup
    //   125: aload_2
    //   126: invokespecial 278	java/net/URI:<init>	(Ljava/lang/String;)V
    //   129: astore_2
    //   130: aload_1
    //   131: invokevirtual 189	java/net/HttpURLConnection:disconnect	()V
    //   134: aload_2
    //   135: areturn
    //   136: astore_0
    //   137: aload_1
    //   138: invokevirtual 189	java/net/HttpURLConnection:disconnect	()V
    //   141: aload_0
    //   142: athrow
    //   143: astore_2
    //   144: goto -40 -> 104
    //
    // Exception table:
    //   from	to	target	type
    //   50	55	136	finally
    //   110	117	136	finally
    //   121	130	136	finally
    //   121	130	143	java/net/URISyntaxException
  }

  public static enum ContentType
  {
    static
    {
      TEXT = new ContentType("TEXT", 3);
      $VALUES = new ContentType[] { HTML, JSON, XML, TEXT };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.HttpHelper
 * JD-Core Version:    0.6.0
 */