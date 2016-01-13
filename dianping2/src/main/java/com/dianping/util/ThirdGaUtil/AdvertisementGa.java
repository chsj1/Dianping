package com.dianping.util.ThirdGaUtil;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.dianping.util.DeviceUtils;
import com.dianping.util.encrypt.Md5;

public class AdvertisementGa
{
  private String parseUrl(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return null;
    String str = paramString;
    if (paramString.contains("$(mac_md5)"))
      str = paramString.replace("$(mac_md5)", Md5.md5(DeviceUtils.mac().toUpperCase()));
    paramString = str;
    if (str.contains("$(mac)"))
      paramString = str.replace("$(mac)", DeviceUtils.mac().toUpperCase());
    str = paramString;
    if (paramString.contains("$(idfa_md5)"))
      str = paramString.replace("$(idfa_md5)", "");
    paramString = str;
    if (str.contains("$(idfa)"))
      paramString = str.replace("$(idfa)", "");
    str = paramString;
    if (paramString.contains("$(imei_md5)"))
      str = paramString.replace("$(imei_md5)", Md5.md5(DeviceUtils.imei()));
    paramString = str;
    if (str.contains("$(imei)"))
      paramString = str.replace("$(imei)", DeviceUtils.imei());
    str = paramString;
    if (paramString.contains("$(dpid_md5)"))
      str = paramString.replace("$(dpid_md5)", Md5.md5(DeviceUtils.dpid()));
    paramString = str;
    if (str.contains("$(dpid)"))
      paramString = str.replace("$(dpid)", DeviceUtils.dpid());
    str = paramString;
    if (paramString.contains("$(deviceid_md5)"))
      str = paramString.replace("$(deviceid_md5)", Md5.md5(DeviceUtils.imei()));
    paramString = str;
    if (str.contains("$(deviceid)"))
      paramString = str.replace("$(deviceid)", DeviceUtils.imei());
    return paramString;
  }

  public void sendAdGA(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    new GaHttpTask().execute(new String[] { parseUrl(paramString) });
  }

  class GaHttpTask extends AsyncTask<String, Integer, String>
  {
    GaHttpTask()
    {
    }

    // ERROR //
    protected String doInBackground(String[] paramArrayOfString)
    {
      // Byte code:
      //   0: aload_1
      //   1: arraylength
      //   2: ifne +5 -> 7
      //   5: aconst_null
      //   6: areturn
      //   7: aload_1
      //   8: iconst_0
      //   9: aaload
      //   10: astore_2
      //   11: aload_2
      //   12: invokestatic 37	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
      //   15: ifne -10 -> 5
      //   18: new 39	org/apache/http/impl/client/DefaultHttpClient
      //   21: dup
      //   22: invokespecial 40	org/apache/http/impl/client/DefaultHttpClient:<init>	()V
      //   25: astore_1
      //   26: new 42	org/apache/http/client/methods/HttpPost
      //   29: dup
      //   30: aload_2
      //   31: invokespecial 45	org/apache/http/client/methods/HttpPost:<init>	(Ljava/lang/String;)V
      //   34: astore_2
      //   35: iconst_0
      //   36: istore 4
      //   38: iload 4
      //   40: iconst_3
      //   41: if_icmpge -36 -> 5
      //   44: aload_1
      //   45: aload_2
      //   46: invokeinterface 51 2 0
      //   51: invokeinterface 57 1 0
      //   56: invokeinterface 63 1 0
      //   61: istore 5
      //   63: iload 5
      //   65: sipush 200
      //   68: if_icmpeq -63 -> 5
      //   71: iload 4
      //   73: iconst_1
      //   74: iadd
      //   75: istore 4
      //   77: goto -39 -> 38
      //   80: astore_3
      //   81: aload_3
      //   82: invokevirtual 66	org/apache/http/client/ClientProtocolException:printStackTrace	()V
      //   85: goto -14 -> 71
      //   88: astore_1
      //   89: aload_1
      //   90: invokevirtual 67	java/lang/Exception:printStackTrace	()V
      //   93: aconst_null
      //   94: areturn
      //   95: astore_3
      //   96: aload_3
      //   97: invokevirtual 68	java/io/IOException:printStackTrace	()V
      //   100: goto -29 -> 71
      //
      // Exception table:
      //   from	to	target	type
      //   44	63	80	org/apache/http/client/ClientProtocolException
      //   18	35	88	java/lang/Exception
      //   44	63	88	java/lang/Exception
      //   81	85	88	java/lang/Exception
      //   96	100	88	java/lang/Exception
      //   44	63	95	java/io/IOException
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.ThirdGaUtil.AdvertisementGa
 * JD-Core Version:    0.6.0
 */