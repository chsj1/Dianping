package com.dianping.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.dianping.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public final class Environment
{
  private static String buildNumber;
  private static boolean buildNumberInited;
  private static String efteVersion;
  private static String imei;
  private static String mapiUserAgent;
  private static String oldUdid;
  private static boolean oldUdidInited;
  private static PackageInfo packageInfo;
  private static String screenInfo;
  private static String source;
  private static String source2;
  private static boolean sourceInited;
  private static String uuid;

  public static String buildNumber()
  {
    Object localObject3;
    Object localObject1;
    if (!buildNumberInited)
    {
      localObject3 = null;
      localObject1 = null;
    }
    try
    {
      InputStream localInputStream = DPApplication._instance().getAssets().open("touch-version.txt");
      localObject1 = localInputStream;
      localObject3 = localInputStream;
      byte[] arrayOfByte = new byte[256];
      localObject1 = localInputStream;
      localObject3 = localInputStream;
      int i = localInputStream.read(arrayOfByte);
      if (i > 0)
      {
        localObject1 = localInputStream;
        localObject3 = localInputStream;
        buildNumber = new String(arrayOfByte, 0, i);
      }
      if (localInputStream != null);
      try
      {
        localInputStream.close();
        buildNumberInited = true;
        return buildNumber;
      }
      catch (IOException localIOException1)
      {
        while (true)
          localIOException1.printStackTrace();
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        if (localIOException1 == null)
          continue;
        try
        {
          localIOException1.close();
        }
        catch (IOException localIOException2)
        {
          localIOException2.printStackTrace();
        }
      }
    }
    finally
    {
      if (localException == null);
    }
    try
    {
      localException.close();
      throw localObject2;
    }
    catch (IOException localIOException3)
    {
      while (true)
        localIOException3.printStackTrace();
    }
  }

  @Deprecated
  public static String deviceId()
  {
    return imei();
  }

  @Deprecated
  public static String deviceId2()
  {
    return uuid();
  }

  public static String efteVersion()
  {
    return efteVersion;
  }

  private static String escapeSource(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    paramString = paramString.toCharArray();
    int j = paramString.length;
    int i = 0;
    if (i < j)
    {
      char c = paramString[i];
      if ((c >= 'a') && (c <= 'z'))
        localStringBuilder.append(c);
      while (true)
      {
        i += 1;
        break;
        if ((c >= 'A') && (c <= 'Z'))
        {
          localStringBuilder.append(c);
          continue;
        }
        if ((c >= '0') && (c <= '9'))
        {
          localStringBuilder.append(c);
          continue;
        }
        if ((c == '.') || (c == '_') || (c == '-') || (c == '/'))
        {
          localStringBuilder.append(c);
          continue;
        }
        if (c != ' ')
          continue;
        localStringBuilder.append('_');
      }
    }
    return localStringBuilder.toString();
  }

  // ERROR //
  public static String execCommand(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore_2
    //   6: invokestatic 104	java/lang/Runtime:getRuntime	()Ljava/lang/Runtime;
    //   9: aload_0
    //   10: invokevirtual 108	java/lang/Runtime:exec	(Ljava/lang/String;)Ljava/lang/Process;
    //   13: astore_0
    //   14: aload_0
    //   15: astore_2
    //   16: aload_0
    //   17: astore_1
    //   18: aload_0
    //   19: astore_3
    //   20: new 110	java/io/BufferedReader
    //   23: dup
    //   24: new 112	java/io/InputStreamReader
    //   27: dup
    //   28: aload_0
    //   29: invokevirtual 118	java/lang/Process:getInputStream	()Ljava/io/InputStream;
    //   32: invokespecial 121	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   35: invokespecial 124	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   38: astore 4
    //   40: aload_0
    //   41: astore_2
    //   42: aload_0
    //   43: astore_1
    //   44: aload_0
    //   45: astore_3
    //   46: sipush 5000
    //   49: newarray char
    //   51: astore 5
    //   53: aload_0
    //   54: astore_2
    //   55: aload_0
    //   56: astore_1
    //   57: aload_0
    //   58: astore_3
    //   59: new 126	java/lang/StringBuffer
    //   62: dup
    //   63: invokespecial 127	java/lang/StringBuffer:<init>	()V
    //   66: astore 6
    //   68: aload_0
    //   69: astore_2
    //   70: aload_0
    //   71: astore_1
    //   72: aload_0
    //   73: astore_3
    //   74: aload 4
    //   76: aload 5
    //   78: invokevirtual 130	java/io/BufferedReader:read	([C)I
    //   81: istore 7
    //   83: iload 7
    //   85: ifle +46 -> 131
    //   88: aload_0
    //   89: astore_2
    //   90: aload_0
    //   91: astore_1
    //   92: aload_0
    //   93: astore_3
    //   94: aload 6
    //   96: aload 5
    //   98: iconst_0
    //   99: iload 7
    //   101: invokevirtual 133	java/lang/StringBuffer:append	([CII)Ljava/lang/StringBuffer;
    //   104: pop
    //   105: goto -37 -> 68
    //   108: astore_0
    //   109: aload_2
    //   110: astore_1
    //   111: new 135	java/lang/RuntimeException
    //   114: dup
    //   115: aload_0
    //   116: invokespecial 138	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   119: athrow
    //   120: astore_0
    //   121: aload_1
    //   122: ifnull +7 -> 129
    //   125: aload_1
    //   126: invokevirtual 141	java/lang/Process:destroy	()V
    //   129: aload_0
    //   130: athrow
    //   131: aload_0
    //   132: astore_2
    //   133: aload_0
    //   134: astore_1
    //   135: aload_0
    //   136: astore_3
    //   137: aload 4
    //   139: invokevirtual 142	java/io/BufferedReader:close	()V
    //   142: aload_0
    //   143: astore_2
    //   144: aload_0
    //   145: astore_1
    //   146: aload_0
    //   147: astore_3
    //   148: aload_0
    //   149: invokevirtual 146	java/lang/Process:waitFor	()I
    //   152: pop
    //   153: aload_0
    //   154: astore_2
    //   155: aload_0
    //   156: astore_1
    //   157: aload_0
    //   158: astore_3
    //   159: aload 6
    //   161: invokevirtual 147	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   164: astore 4
    //   166: aload_0
    //   167: ifnull +7 -> 174
    //   170: aload_0
    //   171: invokevirtual 141	java/lang/Process:destroy	()V
    //   174: aload 4
    //   176: areturn
    //   177: astore_0
    //   178: aload_3
    //   179: astore_1
    //   180: new 135	java/lang/RuntimeException
    //   183: dup
    //   184: aload_0
    //   185: invokespecial 138	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   188: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   6	14	108	java/io/IOException
    //   20	40	108	java/io/IOException
    //   46	53	108	java/io/IOException
    //   59	68	108	java/io/IOException
    //   74	83	108	java/io/IOException
    //   94	105	108	java/io/IOException
    //   137	142	108	java/io/IOException
    //   148	153	108	java/io/IOException
    //   159	166	108	java/io/IOException
    //   6	14	120	finally
    //   20	40	120	finally
    //   46	53	120	finally
    //   59	68	120	finally
    //   74	83	120	finally
    //   94	105	120	finally
    //   111	120	120	finally
    //   137	142	120	finally
    //   148	153	120	finally
    //   159	166	120	finally
    //   180	189	120	finally
    //   6	14	177	java/lang/InterruptedException
    //   20	40	177	java/lang/InterruptedException
    //   46	53	177	java/lang/InterruptedException
    //   59	68	177	java/lang/InterruptedException
    //   74	83	177	java/lang/InterruptedException
    //   94	105	177	java/lang/InterruptedException
    //   137	142	177	java/lang/InterruptedException
    //   148	153	177	java/lang/InterruptedException
    //   159	166	177	java/lang/InterruptedException
  }

  public static String getAndroidId(Context paramContext)
  {
    return Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
  }

  public static String getDeviceSerialNum()
  {
    return Build.SERIAL;
  }

  private static String getVersionNum(String paramString)
  {
    int i = 0;
    if (i < paramString.length())
    {
      int j = paramString.charAt(i);
      if ((j >= 48) && (j <= 57));
      do
      {
        i += 1;
        break;
        if ((j != 46) || (i == 0) || (i >= paramString.length() - 1))
          break label75;
        j = paramString.charAt(i + 1);
      }
      while ((j >= 48) && (j <= 57));
    }
    label75: return paramString.substring(0, i);
  }

  public static String getWifiMac()
  {
    return execCommand("cat /sys/class/net/wlan0/address");
  }

  // ERROR //
  public static String imei()
  {
    // Byte code:
    //   0: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   3: ifnonnull +362 -> 365
    //   6: new 83	java/lang/StringBuilder
    //   9: dup
    //   10: invokespecial 84	java/lang/StringBuilder:<init>	()V
    //   13: getstatic 193	android/os/Build$VERSION:RELEASE	Ljava/lang/String;
    //   16: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   19: ldc 198
    //   21: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: getstatic 201	android/os/Build:MODEL	Ljava/lang/String;
    //   27: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   30: ldc 198
    //   32: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: getstatic 204	android/os/Build:BRAND	Ljava/lang/String;
    //   38: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: invokevirtual 95	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   44: astore_1
    //   45: aload_1
    //   46: astore_0
    //   47: aload_1
    //   48: invokevirtual 173	java/lang/String:length	()I
    //   51: bipush 64
    //   53: if_icmple +11 -> 64
    //   56: aload_1
    //   57: iconst_0
    //   58: bipush 64
    //   60: invokevirtual 181	java/lang/String:substring	(II)Ljava/lang/String;
    //   63: astore_0
    //   64: aload_0
    //   65: astore_1
    //   66: aload_0
    //   67: bipush 10
    //   69: invokevirtual 208	java/lang/String:indexOf	(I)I
    //   72: iflt +12 -> 84
    //   75: aload_0
    //   76: bipush 10
    //   78: bipush 32
    //   80: invokevirtual 212	java/lang/String:replace	(CC)Ljava/lang/String;
    //   83: astore_1
    //   84: aconst_null
    //   85: astore_2
    //   86: aconst_null
    //   87: astore_3
    //   88: aload_2
    //   89: astore_0
    //   90: new 214	java/io/FileInputStream
    //   93: dup
    //   94: new 216	java/io/File
    //   97: dup
    //   98: invokestatic 38	com/dianping/app/DPApplication:_instance	()Lcom/dianping/app/DPApplication;
    //   101: invokevirtual 220	com/dianping/app/DPApplication:getCacheDir	()Ljava/io/File;
    //   104: ldc 222
    //   106: invokespecial 225	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   109: invokespecial 228	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   112: astore 4
    //   114: aload_2
    //   115: astore_0
    //   116: sipush 1024
    //   119: newarray byte
    //   121: astore 5
    //   123: aload 4
    //   125: aload 5
    //   127: invokevirtual 229	java/io/FileInputStream:read	([B)I
    //   130: istore 6
    //   132: aload_2
    //   133: astore_0
    //   134: aload 4
    //   136: invokevirtual 230	java/io/FileInputStream:close	()V
    //   139: aload_2
    //   140: astore_0
    //   141: new 58	java/lang/String
    //   144: dup
    //   145: aload 5
    //   147: iconst_0
    //   148: iload 6
    //   150: ldc 232
    //   152: invokespecial 235	java/lang/String:<init>	([BIILjava/lang/String;)V
    //   155: astore 4
    //   157: aload_2
    //   158: astore_0
    //   159: aload 4
    //   161: bipush 10
    //   163: invokevirtual 208	java/lang/String:indexOf	(I)I
    //   166: istore 6
    //   168: aload_2
    //   169: astore_0
    //   170: aload 4
    //   172: iconst_0
    //   173: iload 6
    //   175: invokevirtual 181	java/lang/String:substring	(II)Ljava/lang/String;
    //   178: astore_2
    //   179: aload_2
    //   180: astore_0
    //   181: aload 4
    //   183: iload 6
    //   185: iconst_1
    //   186: iadd
    //   187: aload 4
    //   189: bipush 10
    //   191: iload 6
    //   193: iconst_1
    //   194: iadd
    //   195: invokevirtual 238	java/lang/String:indexOf	(II)I
    //   198: invokevirtual 181	java/lang/String:substring	(II)Ljava/lang/String;
    //   201: astore 4
    //   203: aload 4
    //   205: astore_3
    //   206: aload_2
    //   207: astore_0
    //   208: aload_1
    //   209: aload_0
    //   210: invokevirtual 242	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   213: ifeq +178 -> 391
    //   216: aload_3
    //   217: putstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   220: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   223: ifnonnull +130 -> 353
    //   226: invokestatic 38	com/dianping/app/DPApplication:_instance	()Lcom/dianping/app/DPApplication;
    //   229: ldc 244
    //   231: invokevirtual 248	com/dianping/app/DPApplication:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   234: checkcast 250	android/telephony/TelephonyManager
    //   237: astore_0
    //   238: invokestatic 253	com/dianping/app/DPApplication:instance	()Lcom/dianping/app/DPApplication;
    //   241: ldc 255
    //   243: invokestatic 261	com/dianping/util/PermissionCheckHelper:isPermissionGranted	(Landroid/content/Context;Ljava/lang/String;)Z
    //   246: ifeq +10 -> 256
    //   249: aload_0
    //   250: invokevirtual 264	android/telephony/TelephonyManager:getDeviceId	()Ljava/lang/String;
    //   253: putstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   256: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   259: ifnull +18 -> 277
    //   262: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   265: invokevirtual 173	java/lang/String:length	()I
    //   268: bipush 8
    //   270: if_icmpge +128 -> 398
    //   273: aconst_null
    //   274: putstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   277: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   280: ifnull +208 -> 488
    //   283: new 266	java/io/FileOutputStream
    //   286: dup
    //   287: new 216	java/io/File
    //   290: dup
    //   291: invokestatic 38	com/dianping/app/DPApplication:_instance	()Lcom/dianping/app/DPApplication;
    //   294: invokevirtual 220	com/dianping/app/DPApplication:getCacheDir	()Ljava/io/File;
    //   297: ldc 222
    //   299: invokespecial 225	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   302: invokespecial 267	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   305: astore_0
    //   306: new 83	java/lang/StringBuilder
    //   309: dup
    //   310: invokespecial 84	java/lang/StringBuilder:<init>	()V
    //   313: aload_1
    //   314: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   317: ldc_w 269
    //   320: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   326: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   329: ldc_w 269
    //   332: invokevirtual 196	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   335: invokevirtual 95	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   338: astore_1
    //   339: aload_0
    //   340: aload_1
    //   341: ldc 232
    //   343: invokevirtual 273	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   346: invokevirtual 277	java/io/FileOutputStream:write	([B)V
    //   349: aload_0
    //   350: invokevirtual 278	java/io/FileOutputStream:close	()V
    //   353: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   356: ifnonnull +9 -> 365
    //   359: ldc_w 280
    //   362: putstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   365: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   368: areturn
    //   369: astore 5
    //   371: aload_2
    //   372: astore_0
    //   373: aload 4
    //   375: invokevirtual 230	java/io/FileInputStream:close	()V
    //   378: aload_2
    //   379: astore_0
    //   380: aload 5
    //   382: athrow
    //   383: astore_2
    //   384: aload_2
    //   385: invokevirtual 281	java/lang/Exception:printStackTrace	()V
    //   388: goto -180 -> 208
    //   391: aconst_null
    //   392: putstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   395: goto -175 -> 220
    //   398: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   401: iconst_0
    //   402: invokevirtual 177	java/lang/String:charAt	(I)C
    //   405: istore 9
    //   407: iconst_1
    //   408: istore 8
    //   410: iconst_0
    //   411: istore 6
    //   413: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   416: invokevirtual 173	java/lang/String:length	()I
    //   419: istore 10
    //   421: iload 8
    //   423: istore 7
    //   425: iload 6
    //   427: iload 10
    //   429: if_icmpge +19 -> 448
    //   432: iload 9
    //   434: getstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   437: iload 6
    //   439: invokevirtual 177	java/lang/String:charAt	(I)C
    //   442: if_icmpeq +22 -> 464
    //   445: iconst_0
    //   446: istore 7
    //   448: iload 7
    //   450: ifeq -173 -> 277
    //   453: aconst_null
    //   454: putstatic 188	com/dianping/app/Environment:imei	Ljava/lang/String;
    //   457: goto -180 -> 277
    //   460: astore_0
    //   461: goto -184 -> 277
    //   464: iload 6
    //   466: iconst_1
    //   467: iadd
    //   468: istore 6
    //   470: goto -49 -> 421
    //   473: astore_1
    //   474: aload_0
    //   475: invokevirtual 278	java/io/FileOutputStream:close	()V
    //   478: aload_1
    //   479: athrow
    //   480: astore_0
    //   481: aload_0
    //   482: invokevirtual 281	java/lang/Exception:printStackTrace	()V
    //   485: goto -132 -> 353
    //   488: new 216	java/io/File
    //   491: dup
    //   492: invokestatic 38	com/dianping/app/DPApplication:_instance	()Lcom/dianping/app/DPApplication;
    //   495: invokevirtual 220	com/dianping/app/DPApplication:getCacheDir	()Ljava/io/File;
    //   498: ldc 222
    //   500: invokespecial 225	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   503: invokevirtual 285	java/io/File:delete	()Z
    //   506: pop
    //   507: goto -154 -> 353
    //
    // Exception table:
    //   from	to	target	type
    //   123	132	369	finally
    //   90	114	383	java/lang/Exception
    //   116	123	383	java/lang/Exception
    //   134	139	383	java/lang/Exception
    //   141	157	383	java/lang/Exception
    //   159	168	383	java/lang/Exception
    //   170	179	383	java/lang/Exception
    //   181	203	383	java/lang/Exception
    //   373	378	383	java/lang/Exception
    //   380	383	383	java/lang/Exception
    //   226	256	460	java/lang/Exception
    //   256	277	460	java/lang/Exception
    //   398	407	460	java/lang/Exception
    //   413	421	460	java/lang/Exception
    //   432	445	460	java/lang/Exception
    //   453	457	460	java/lang/Exception
    //   339	349	473	finally
    //   283	339	480	java/lang/Exception
    //   349	353	480	java/lang/Exception
    //   474	480	480	java/lang/Exception
  }

  @Deprecated
  public static String imsi()
  {
    monitorenter;
    monitorexit;
    return "460000000000000";
  }

  public static boolean isDebug()
  {
    return Log.LEVEL < 2147483647;
  }

  public static String mapiUserAgent()
  {
    StringBuilder localStringBuilder;
    if (mapiUserAgent == null)
      localStringBuilder = new StringBuilder("MApi 1.0 (");
    try
    {
      localObject = DPApplication._instance();
      localStringBuilder.append(((Context)localObject).getPackageManager().getPackageInfo(((Context)localObject).getPackageName(), 0).packageName);
      localStringBuilder.append(" ").append(getVersionNum(versionName()));
    }
    catch (Exception localException2)
    {
      try
      {
        Object localObject = source();
        if (localObject != null)
          localStringBuilder.append(" ").append((String)localObject);
        while (true)
        {
          localStringBuilder.append(" ").append(source2());
          localStringBuilder.append("; Android ");
          localStringBuilder.append(Build.VERSION.RELEASE);
          localStringBuilder.append(")");
          mapiUserAgent = localStringBuilder.toString();
          return mapiUserAgent;
          localException2 = localException2;
          localStringBuilder.append("com.dianping.v1 5.6");
          break;
          localStringBuilder.append(" null");
        }
      }
      catch (Exception localException1)
      {
        while (true)
          mapiUserAgent = "MApi 1.0 (com.dianping.v1 5.6 null null; Android " + Build.VERSION.RELEASE + ")";
      }
    }
  }

  private static String oldUdid()
  {
    Object localObject1;
    SharedPreferences localSharedPreferences;
    String str;
    if ((!oldUdidInited) && (DPApplication._instance() != null))
    {
      localObject1 = DPApplication._instance();
      localSharedPreferences = ((Context)localObject1).getSharedPreferences(((Context)localObject1).getPackageName(), 0);
      str = localSharedPreferences.getString("deviceId", null);
      localObject1 = str;
      if (str == null);
    }
    try
    {
      UUID.fromString(str);
      localObject1 = str;
      oldUdid = (String)localObject1;
      oldUdidInited = true;
      return oldUdid;
    }
    catch (Exception localObject2)
    {
      while (true)
      {
        localSharedPreferences.edit().remove("deviceId").commit();
        Object localObject2 = null;
      }
    }
  }

  @Deprecated
  public static String phone()
  {
    return null;
  }

  private static PackageInfo pkgInfo()
  {
    if (packageInfo == null);
    try
    {
      DPApplication localDPApplication = DPApplication._instance();
      packageInfo = localDPApplication.getPackageManager().getPackageInfo(localDPApplication.getPackageName(), 0);
      label25: return packageInfo;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      break label25;
    }
  }

  public static String screenInfo()
  {
    if (TextUtils.isEmpty(screenInfo))
    {
      DisplayMetrics localDisplayMetrics = DPApplication._instance().getResources().getDisplayMetrics();
      screenInfo = "screenwidth=" + localDisplayMetrics.widthPixels + "&screenheight=" + localDisplayMetrics.heightPixels + "&screendensity=" + localDisplayMetrics.density;
    }
    return screenInfo;
  }

  public static String sessionId()
  {
    DPApplication localDPApplication = DPApplication._instance();
    if (localDPApplication == null)
      return "";
    return localDPApplication.sessionId();
  }

  public static void setEfteVersion(String paramString)
  {
    efteVersion = paramString;
  }

  public static String source()
  {
    Object localObject3;
    Object localObject1;
    if (!sourceInited)
    {
      localObject3 = null;
      localObject1 = null;
    }
    try
    {
      InputStream localInputStream = DPApplication._instance().getAssets().open("source.txt");
      localObject1 = localInputStream;
      localObject3 = localInputStream;
      byte[] arrayOfByte = new byte[256];
      localObject1 = localInputStream;
      localObject3 = localInputStream;
      int i = localInputStream.read(arrayOfByte);
      if (i > 0)
      {
        localObject1 = localInputStream;
        localObject3 = localInputStream;
        source = escapeSource(new String(arrayOfByte, 0, i));
      }
      if (localInputStream != null);
      try
      {
        localInputStream.close();
        sourceInited = true;
        return source;
      }
      catch (IOException localIOException1)
      {
        while (true)
          localIOException1.printStackTrace();
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        if (localIOException1 == null)
          continue;
        try
        {
          localIOException1.close();
        }
        catch (IOException localIOException2)
        {
          localIOException2.printStackTrace();
        }
      }
    }
    finally
    {
      if (localException == null);
    }
    try
    {
      localException.close();
      throw localObject2;
    }
    catch (IOException localIOException3)
    {
      while (true)
        localIOException3.printStackTrace();
    }
  }

  public static String source2()
  {
    if (source2 == null)
      source2 = escapeSource(Build.MODEL);
    return source2;
  }

  public static String uuid()
  {
    Object localObject1;
    SharedPreferences localSharedPreferences;
    Object localObject2;
    if (uuid == null)
    {
      localObject1 = DPApplication._instance();
      if (localObject1 == null)
        return null;
      localSharedPreferences = ((DPApplication)localObject1).getApplicationContext().getSharedPreferences("bookinguuid", 0);
      localObject2 = localSharedPreferences.getString("uuid", "");
      localObject1 = localObject2;
      if (TextUtils.isEmpty((CharSequence)localObject2))
      {
        localObject2 = oldUdid();
        localObject1 = localObject2;
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          localSharedPreferences.edit().putString("uuid", (String)localObject2).commit();
          localObject1 = localObject2;
        }
      }
      localObject2 = localObject1;
      if (TextUtils.isEmpty((CharSequence)localObject1));
    }
    try
    {
      UUID.fromString((String)localObject1);
      localObject2 = localObject1;
      localObject1 = localObject2;
      if (TextUtils.isEmpty((CharSequence)localObject2))
      {
        localObject1 = UUID.randomUUID().toString();
        localObject2 = localSharedPreferences.edit();
        ((SharedPreferences.Editor)localObject2).putString("uuid", (String)localObject1);
        ((SharedPreferences.Editor)localObject2).commit();
      }
      uuid = (String)localObject1;
      return uuid;
    }
    catch (Exception localException)
    {
      while (true)
        localObject2 = null;
    }
  }

  @Deprecated
  public static String version()
  {
    return pkgInfo().versionName;
  }

  public static int versionCode()
  {
    return pkgInfo().versionCode;
  }

  public static String versionName()
  {
    return pkgInfo().versionName;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.app.Environment
 * JD-Core Version:    0.6.0
 */