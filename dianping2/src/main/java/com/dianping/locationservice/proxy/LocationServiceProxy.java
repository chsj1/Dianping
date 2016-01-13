package com.dianping.locationservice.proxy;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.configservice.ConfigChangeListener;
import com.dianping.configservice.ConfigService;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.StringInputStream;
import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.http.NetworkInfoHelper;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.locationservice.impl286.main.DPLocationService;
import com.dianping.locationservice.realtime.DpIdSupplier;
import com.dianping.locationservice.realtime.RealTimeLocator;
import com.dianping.locationservice.realtime.RunMode;
import com.dianping.model.GPSCoordinate;
import com.dianping.util.DateUtil;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.StringUtil;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import org.json.JSONObject;

public class LocationServiceProxy
  implements LocationService
{
  private static final String K_CLASSNAME = "locDex5ClassName";
  private static final String K_MD5 = "locDex5MD5";
  private static final String K_UPDATENOW = "locDex5UpdateNow";
  private static final String K_URL = "locDex5Url";
  private static final String K_VERSION = "locDex5Version";
  private static final int MIN_LOC_DEX_VERSION = 286;
  private static final String TAG = "location";
  private final RequestHandler<HttpRequest, HttpResponse> apkHandler = new RequestHandler()
  {
    public void onRequestFailed(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      Log.w("location", "fail to download apk from " + paramHttpRequest.url());
    }

    public void onRequestFinish(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      Object localObject1;
      String str;
      int i;
      boolean bool;
      try
      {
        localObject1 = (byte[])(byte[])paramHttpResponse.result();
        str = LocationServiceProxy.this.config.getString("locDex5Url");
        if (!paramHttpRequest.url().equals(str))
          return;
        i = LocationServiceProxy.this.config.getInt("locDex5Version");
        paramHttpResponse = LocationServiceProxy.this.config.optString("locDex5MD5");
        bool = LocationServiceProxy.this.config.optBoolean("locDex5UpdateNow");
        if (!TextUtils.isEmpty(paramHttpResponse))
        {
          localObject2 = MessageDigest.getInstance("MD5");
          ((MessageDigest)localObject2).reset();
          ((MessageDigest)localObject2).update(localObject1);
          localObject2 = StringUtil.byteArrayToHexString(((MessageDigest)localObject2).digest());
          if (!paramHttpResponse.equals(localObject2))
          {
            paramHttpResponse = str + "'s md5 not match, length=" + localObject1.length + ", " + (String)localObject2 + " != " + paramHttpResponse;
            Log.w("location", paramHttpResponse);
            throw new Exception(paramHttpResponse);
          }
        }
      }
      catch (Exception paramHttpResponse)
      {
        Log.w("location", "fail to save apk from " + paramHttpRequest.url(), paramHttpResponse);
        LocationServiceProxy.this.uploadException(LocationServiceProxy.this.config, paramHttpResponse);
        return;
      }
      Object localObject2 = new File(LocationServiceProxy.this.dir, "v" + i);
      ((File)localObject2).mkdir();
      if (TextUtils.isEmpty(paramHttpResponse));
      File localFile;
      for (paramHttpResponse = "1.apk"; ; paramHttpResponse = paramHttpResponse + ".apk")
      {
        localFile = new File((File)localObject2, paramHttpResponse);
        if (!localFile.exists())
          break;
        Log.w("location", "apk already exists, " + localFile);
        return;
      }
      paramHttpResponse = new File((File)localObject2, "tmp.apk");
      localObject2 = new FileOutputStream(paramHttpResponse);
      try
      {
        ((FileOutputStream)localObject2).write(localObject1);
        ((FileOutputStream)localObject2).close();
        if (!LocationServiceProxy.this.verify(paramHttpResponse))
        {
          localObject1 = "illegal apk file from " + str;
          Log.w("location", (String)localObject1);
          paramHttpResponse.delete();
          throw new Exception((String)localObject1);
        }
      }
      finally
      {
        ((FileOutputStream)localObject2).close();
      }
      if (!paramHttpResponse.renameTo(localFile))
      {
        localObject1 = "fail to move apk from " + paramHttpResponse + " to " + localFile;
        Log.w("location", (String)localObject1);
        paramHttpResponse.delete();
        throw new Exception((String)localObject1);
      }
      Log.i("location", "apk saved to " + localFile);
      if (bool)
        LocationServiceProxy.this.updateNow();
    }
  };
  private JSONObject config;
  private ConfigChangeListener configChangeListener = new ConfigChangeListener()
  {
    public void onConfigChange(String paramString, Object paramObject1, Object paramObject2)
    {
      LocationServiceProxy.this.checkLocationConfig();
    }
  };
  private long configLastUpdateTime;
  private final Context context;
  private final File dir;
  private final LocationListener listener = new LocationListener()
  {
    public void onLocationChanged(LocationService paramLocationService)
    {
      Iterator localIterator = LocationServiceProxy.this.listeners.iterator();
      while (localIterator.hasNext())
        ((LocationListener)localIterator.next()).onLocationChanged(paramLocationService);
    }
  };
  private final ArrayList<LocationListener> listeners = new ArrayList();
  private LocationService service;
  private int serviceVersion;

  public LocationServiceProxy(Context paramContext)
  {
    this.context = paramContext.getApplicationContext();
    deleteOldDex();
    this.dir = new File(this.context.getFilesDir(), "location");
    this.dir.mkdir();
    try
    {
      this.config = readConfig();
      if (this.config == null)
        this.config = new JSONObject();
      return;
    }
    catch (Exception paramContext)
    {
      while (true)
        Log.e("location", "unable to read config at " + new File(this.dir, "config"), paramContext);
    }
  }

  private AccountService accountService()
  {
    if ((this.context instanceof DPApplication))
      return (AccountService)((DPApplication)this.context).getService("account");
    return null;
  }

  private ConfigService configService()
  {
    if ((this.context instanceof DPApplication))
      return (ConfigService)((DPApplication)this.context).getService("config");
    return null;
  }

  private void deleteDir(File paramFile)
  {
    if (paramFile.isDirectory())
    {
      File[] arrayOfFile = paramFile.listFiles();
      int j = arrayOfFile.length;
      int i = 0;
      while (i < j)
      {
        deleteDir(arrayOfFile[i]);
        i += 1;
      }
    }
    paramFile.delete();
  }

  private void deleteOldDex()
  {
    File localFile = this.context.getDir("dex", 0);
    if (localFile.exists());
    try
    {
      deleteDir(localFile);
      localFile = this.context.getDir("dexout", 0);
      if (!localFile.exists());
    }
    catch (Exception localException2)
    {
      try
      {
        deleteDir(localFile);
        return;
        localException1 = localException1;
      }
      catch (Exception localException2)
      {
      }
    }
  }

  private HttpService httpService()
  {
    if ((this.context instanceof DPApplication))
      return (HttpService)((DPApplication)this.context).getService("http");
    return null;
  }

  @TargetApi(14)
  private Class<?> loadClass(int paramInt, String paramString1, String paramString2)
    throws Exception
  {
    Object localObject = new File(this.dir, "v" + paramInt);
    if (TextUtils.isEmpty(paramString1));
    for (paramString1 = "1.apk"; ; paramString1 = paramString1 + ".apk")
    {
      paramString1 = new File((File)localObject, paramString1);
      if (paramString1.length() != 0L)
        break;
      throw new FileNotFoundException(paramString1 + " not found");
    }
    localObject = new File((File)localObject, "dexout");
    ((File)localObject).mkdir();
    localObject = new DexClassLoader(paramString1.getAbsolutePath(), ((File)localObject).getAbsolutePath(), null, this.context.getClassLoader()).loadClass(paramString2);
    Log.i("location", paramString2 + " loaded from " + paramString1 + ", version=" + paramInt);
    return (Class<?>)localObject;
  }

  // ERROR //
  private JSONObject readConfig()
    throws Exception
  {
    // Byte code:
    //   0: new 97	java/io/File
    //   3: dup
    //   4: aload_0
    //   5: getfield 106	com/dianping/locationservice/proxy/LocationServiceProxy:dir	Ljava/io/File;
    //   8: ldc 129
    //   10: invokespecial 104	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   13: astore_1
    //   14: aload_1
    //   15: invokevirtual 238	java/io/File:length	()J
    //   18: lconst_0
    //   19: lcmp
    //   20: ifne +13 -> 33
    //   23: new 118	org/json/JSONObject
    //   26: dup
    //   27: invokespecial 119	org/json/JSONObject:<init>	()V
    //   30: astore_1
    //   31: aload_1
    //   32: areturn
    //   33: aconst_null
    //   34: astore_3
    //   35: new 276	java/io/FileInputStream
    //   38: dup
    //   39: aload_1
    //   40: invokespecial 278	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   43: astore_2
    //   44: aload_2
    //   45: invokevirtual 282	java/io/FileInputStream:available	()I
    //   48: newarray byte
    //   50: astore_1
    //   51: aload_2
    //   52: aload_1
    //   53: invokevirtual 286	java/io/FileInputStream:read	([B)I
    //   56: pop
    //   57: aload_2
    //   58: invokevirtual 289	java/io/FileInputStream:close	()V
    //   61: new 118	org/json/JSONObject
    //   64: dup
    //   65: new 291	java/lang/String
    //   68: dup
    //   69: aload_1
    //   70: ldc_w 293
    //   73: invokespecial 296	java/lang/String:<init>	([BLjava/lang/String;)V
    //   76: invokespecial 297	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   79: astore_3
    //   80: aload_3
    //   81: astore_1
    //   82: aload_2
    //   83: ifnull -52 -> 31
    //   86: aload_2
    //   87: invokevirtual 289	java/io/FileInputStream:close	()V
    //   90: aload_3
    //   91: areturn
    //   92: astore_1
    //   93: aload_1
    //   94: invokevirtual 300	java/lang/Exception:printStackTrace	()V
    //   97: aload_3
    //   98: areturn
    //   99: astore_1
    //   100: aload_3
    //   101: astore_2
    //   102: aload_2
    //   103: ifnull +7 -> 110
    //   106: aload_2
    //   107: invokevirtual 289	java/io/FileInputStream:close	()V
    //   110: aload_1
    //   111: athrow
    //   112: astore_2
    //   113: aload_2
    //   114: invokevirtual 300	java/lang/Exception:printStackTrace	()V
    //   117: goto -7 -> 110
    //   120: astore_1
    //   121: goto -19 -> 102
    //
    // Exception table:
    //   from	to	target	type
    //   86	90	92	java/lang/Exception
    //   35	44	99	finally
    //   106	110	112	java/lang/Exception
    //   44	80	120	finally
  }

  // ERROR //
  private void saveConfig(JSONObject paramJSONObject)
    throws Exception
  {
    // Byte code:
    //   0: new 97	java/io/File
    //   3: dup
    //   4: aload_0
    //   5: getfield 106	com/dianping/locationservice/proxy/LocationServiceProxy:dir	Ljava/io/File;
    //   8: ldc 129
    //   10: invokespecial 104	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   13: astore 4
    //   15: new 97	java/io/File
    //   18: dup
    //   19: aload_0
    //   20: getfield 106	com/dianping/locationservice/proxy/LocationServiceProxy:dir	Ljava/io/File;
    //   23: ldc_w 304
    //   26: invokespecial 104	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   29: astore_3
    //   30: aload_1
    //   31: invokevirtual 305	org/json/JSONObject:toString	()Ljava/lang/String;
    //   34: ldc_w 293
    //   37: invokevirtual 309	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   40: astore_1
    //   41: new 311	java/io/FileOutputStream
    //   44: dup
    //   45: aload_3
    //   46: invokespecial 312	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   49: astore_2
    //   50: aload_2
    //   51: aload_1
    //   52: invokevirtual 316	java/io/FileOutputStream:write	([B)V
    //   55: aload_2
    //   56: invokevirtual 317	java/io/FileOutputStream:close	()V
    //   59: aload_3
    //   60: aload 4
    //   62: invokevirtual 320	java/io/File:renameTo	(Ljava/io/File;)Z
    //   65: ifne +60 -> 125
    //   68: new 65	java/lang/Exception
    //   71: dup
    //   72: new 121	java/lang/StringBuilder
    //   75: dup
    //   76: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   79: ldc_w 322
    //   82: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: aload_3
    //   86: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   89: ldc_w 324
    //   92: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   95: aload 4
    //   97: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   100: invokevirtual 136	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   103: invokespecial 325	java/lang/Exception:<init>	(Ljava/lang/String;)V
    //   106: athrow
    //   107: aconst_null
    //   108: astore_2
    //   109: astore_1
    //   110: aload_2
    //   111: ifnull +7 -> 118
    //   114: aload_2
    //   115: invokevirtual 317	java/io/FileOutputStream:close	()V
    //   118: aload_3
    //   119: invokevirtual 199	java/io/File:delete	()Z
    //   122: pop
    //   123: aload_1
    //   124: athrow
    //   125: iconst_0
    //   126: ifeq +11 -> 137
    //   129: new 327	java/lang/NullPointerException
    //   132: dup
    //   133: invokespecial 328	java/lang/NullPointerException:<init>	()V
    //   136: athrow
    //   137: aload_3
    //   138: invokevirtual 199	java/io/File:delete	()Z
    //   141: pop
    //   142: return
    //   143: astore_1
    //   144: goto -7 -> 137
    //   147: astore_2
    //   148: goto -30 -> 118
    //   151: astore_1
    //   152: goto -42 -> 110
    //
    // Exception table:
    //   from	to	target	type
    //   30	50	107	finally
    //   59	107	107	finally
    //   129	137	143	java/lang/Exception
    //   114	118	147	java/lang/Exception
    //   50	59	151	finally
  }

  private void updateNow()
  {
    new Handler().post(new Runnable()
    {
      public void run()
      {
        Log.i("location", "restart service from version " + LocationServiceProxy.this.serviceVersion + " to " + LocationServiceProxy.this.config.optInt("locDex5Version"));
        LocationServiceProxy.this.stop();
        LocationServiceProxy.this.start();
      }
    });
  }

  private void uploadException(Object paramObject, Throwable paramThrowable)
  {
    StringWriter localStringWriter = new StringWriter();
    try
    {
      PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
      localPrintWriter.print("===============================");
      localPrintWriter.print(UUID.randomUUID().toString());
      localPrintWriter.println("============================");
      if (Environment.isDebug())
        localPrintWriter.println("debug=true");
      localPrintWriter.print("addtime=");
      localPrintWriter.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(DateUtil.currentTimeMillis())));
      localPrintWriter.print("user-agent=");
      localPrintWriter.println(Environment.mapiUserAgent());
      localPrintWriter.print("deviceid=");
      localPrintWriter.println(Environment.imei());
      localPrintWriter.print("sessionid=");
      localPrintWriter.println(Environment.sessionId());
      localPrintWriter.print("cityid=");
      localPrintWriter.println(DPApplication.instance().city());
      localPrintWriter.print("token=");
      Object localObject = accountService();
      if (localObject == null);
      for (localObject = ""; ; localObject = ((AccountService)localObject).token())
      {
        localPrintWriter.println((String)localObject);
        localPrintWriter.print("network=");
        localPrintWriter.println(new NetworkInfoHelper(DPApplication.instance()).getNetworkInfo());
        localPrintWriter.print("os-version=");
        localPrintWriter.println(Build.VERSION.RELEASE);
        localPrintWriter.print("os-build=");
        localPrintWriter.println(Build.ID);
        localPrintWriter.print("device-brand=");
        localPrintWriter.println(Build.BRAND);
        localPrintWriter.print("device-model=");
        localPrintWriter.println(Build.MODEL);
        localPrintWriter.print("device-fingerprint=");
        localPrintWriter.println(Build.FINGERPRINT);
        localPrintWriter.print("config=");
        localPrintWriter.println(String.valueOf(paramObject));
        localPrintWriter.println();
        paramThrowable.printStackTrace(localPrintWriter);
        localPrintWriter.println();
        localPrintWriter.println();
        localPrintWriter.close();
        label340: paramObject = new BasicHttpRequest("http://stat.api.dianping.com/utm.js?v=android_location_crash", "POST", new StringInputStream(localStringWriter.toString(), "UTF-8"));
        httpService().exec(paramObject, new RequestHandler()
        {
          public void onRequestFailed(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
          {
          }

          public void onRequestFinish(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
          {
          }
        });
        return;
      }
    }
    catch (Exception paramObject)
    {
      break label340;
    }
  }

  // ERROR //
  private boolean verify(File paramFile)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore 4
    //   5: new 503	java/util/jar/JarFile
    //   8: dup
    //   9: aload_1
    //   10: invokespecial 504	java/util/jar/JarFile:<init>	(Ljava/io/File;)V
    //   13: astore_3
    //   14: aload_3
    //   15: invokevirtual 508	java/util/jar/JarFile:entries	()Ljava/util/Enumeration;
    //   18: astore_2
    //   19: aload_2
    //   20: invokeinterface 513 1 0
    //   25: ifeq +139 -> 164
    //   28: aload_2
    //   29: invokeinterface 517 1 0
    //   34: checkcast 519	java/util/jar/JarEntry
    //   37: astore 4
    //   39: aload 4
    //   41: invokevirtual 522	java/util/jar/JarEntry:getName	()Ljava/lang/String;
    //   44: ldc_w 524
    //   47: invokevirtual 528	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   50: ifeq -31 -> 19
    //   53: sipush 8192
    //   56: newarray byte
    //   58: astore 5
    //   60: aload_3
    //   61: aload 4
    //   63: invokevirtual 532	java/util/jar/JarFile:getInputStream	(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;
    //   66: astore 6
    //   68: aload 6
    //   70: aload 5
    //   72: iconst_0
    //   73: aload 5
    //   75: arraylength
    //   76: invokevirtual 537	java/io/InputStream:read	([BII)I
    //   79: iconst_m1
    //   80: if_icmpne -12 -> 68
    //   83: aload 6
    //   85: invokevirtual 538	java/io/InputStream:close	()V
    //   88: aload 4
    //   90: invokevirtual 542	java/util/jar/JarEntry:getCertificates	()[Ljava/security/cert/Certificate;
    //   93: astore 4
    //   95: aload 4
    //   97: arraylength
    //   98: istore 8
    //   100: iconst_0
    //   101: istore 7
    //   103: iload 7
    //   105: iload 8
    //   107: if_icmpge -88 -> 19
    //   110: aload 4
    //   112: iload 7
    //   114: aaload
    //   115: invokevirtual 548	java/security/cert/Certificate:getEncoded	()[B
    //   118: invokestatic 554	com/dianping/util/StringUtil:byteArrayToHexString	([B)Ljava/lang/String;
    //   121: invokevirtual 557	java/lang/String:hashCode	()I
    //   124: istore 9
    //   126: iload 9
    //   128: ldc_w 558
    //   131: if_icmpne +24 -> 155
    //   134: aload_3
    //   135: ifnull +7 -> 142
    //   138: aload_3
    //   139: invokevirtual 559	java/util/jar/JarFile:close	()V
    //   142: iconst_1
    //   143: ireturn
    //   144: astore_1
    //   145: aload_1
    //   146: invokevirtual 560	java/io/IOException:toString	()Ljava/lang/String;
    //   149: invokestatic 562	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   152: goto -10 -> 142
    //   155: iload 7
    //   157: iconst_1
    //   158: iadd
    //   159: istore 7
    //   161: goto -58 -> 103
    //   164: aload_3
    //   165: ifnull +111 -> 276
    //   168: aload_3
    //   169: invokevirtual 559	java/util/jar/JarFile:close	()V
    //   172: iconst_0
    //   173: ireturn
    //   174: astore_1
    //   175: aload_1
    //   176: invokevirtual 560	java/io/IOException:toString	()Ljava/lang/String;
    //   179: invokestatic 562	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   182: goto -10 -> 172
    //   185: astore_2
    //   186: aload 4
    //   188: astore_3
    //   189: aload_2
    //   190: astore 4
    //   192: aload_3
    //   193: astore_2
    //   194: ldc 40
    //   196: new 121	java/lang/StringBuilder
    //   199: dup
    //   200: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   203: ldc_w 564
    //   206: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: aload_1
    //   210: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   213: invokevirtual 136	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   216: aload 4
    //   218: invokestatic 567	com/dianping/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   221: aload_3
    //   222: ifnull -50 -> 172
    //   225: aload_3
    //   226: invokevirtual 559	java/util/jar/JarFile:close	()V
    //   229: goto -57 -> 172
    //   232: astore_1
    //   233: aload_1
    //   234: invokevirtual 560	java/io/IOException:toString	()Ljava/lang/String;
    //   237: invokestatic 562	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   240: goto -68 -> 172
    //   243: astore_1
    //   244: aload_2
    //   245: ifnull +7 -> 252
    //   248: aload_2
    //   249: invokevirtual 559	java/util/jar/JarFile:close	()V
    //   252: aload_1
    //   253: athrow
    //   254: astore_2
    //   255: aload_2
    //   256: invokevirtual 560	java/io/IOException:toString	()Ljava/lang/String;
    //   259: invokestatic 562	com/dianping/util/Log:e	(Ljava/lang/String;)V
    //   262: goto -10 -> 252
    //   265: astore_1
    //   266: aload_3
    //   267: astore_2
    //   268: goto -24 -> 244
    //   271: astore 4
    //   273: goto -81 -> 192
    //   276: goto -104 -> 172
    //
    // Exception table:
    //   from	to	target	type
    //   138	142	144	java/io/IOException
    //   168	172	174	java/io/IOException
    //   5	14	185	java/lang/Exception
    //   225	229	232	java/io/IOException
    //   5	14	243	finally
    //   194	221	243	finally
    //   248	252	254	java/io/IOException
    //   14	19	265	finally
    //   19	68	265	finally
    //   68	100	265	finally
    //   110	126	265	finally
    //   14	19	271	java/lang/Exception
    //   19	68	271	java/lang/Exception
    //   68	100	271	java/lang/Exception
    //   110	126	271	java/lang/Exception
  }

  public void addListener(LocationListener paramLocationListener)
  {
    this.listeners.add(paramLocationListener);
  }

  public String address()
  {
    if (this.service == null)
      return null;
    return this.service.address();
  }

  public void checkLocationConfig()
  {
    while (true)
    {
      try
      {
        JSONObject localJSONObject = configService().dump();
        int j = localJSONObject.getInt("locDex5Version");
        Object localObject = localJSONObject.optString("locDex5MD5");
        saveConfig(localJSONObject);
        this.config = localJSONObject;
        Log.i("location", "config updated");
        Log.i("location", String.valueOf(configService().dump()));
        i = 0;
        if (j > 286)
          continue;
        j = 0;
        i = 1;
        if ((i == 0) || (!localJSONObject.optBoolean("locDex5UpdateNow")) || (j == this.serviceVersion))
          break;
        updateNow();
        return;
        File localFile = new File(this.dir, "v" + j);
        if (TextUtils.isEmpty((CharSequence)localObject))
        {
          localObject = "1.apk";
          if (new File(localFile, (String)localObject).length() != 0L)
            break label294;
          localObject = localJSONObject.getString("locDex5Url");
          Log.i("location", "require apk from " + (String)localObject);
          localObject = BasicHttpRequest.httpGet((String)localObject);
          httpService().exec((Request)localObject, this.apkHandler);
          continue;
        }
      }
      catch (Exception localException)
      {
        Log.w("location", "malformed config from configService");
        Log.w("location", configService().dump().toString());
        Log.w("location", "", localException);
        uploadException(configService().dump().toString(), localException);
        return;
      }
      String str = localException + ".apk";
      continue;
      label294: int i = 1;
    }
  }

  public DPObject city()
  {
    if (this.service == null)
      return null;
    return this.service.city();
  }

  public boolean hasLocation()
  {
    if (this.service == null)
      return false;
    return this.service.hasLocation();
  }

  public DPObject location()
  {
    if (this.service == null)
      return null;
    return this.service.location();
  }

  public GPSCoordinate offsetCoordinate()
  {
    if (this.service == null)
      return null;
    return this.service.offsetCoordinate();
  }

  public GPSCoordinate realCoordinate()
  {
    if (this.service == null)
      return null;
    return this.service.realCoordinate();
  }

  public boolean refresh()
  {
    if (this.service == null)
      return true;
    return this.service.refresh();
  }

  public void removeListener(LocationListener paramLocationListener)
  {
    this.listeners.remove(paramLocationListener);
  }

  public void selectCoordinate(int paramInt, GPSCoordinate paramGPSCoordinate)
  {
    if (this.service != null)
      this.service.selectCoordinate(paramInt, paramGPSCoordinate);
  }

  public boolean start()
  {
    int j = this.config.optInt("locDex5Version", 0);
    int i = j;
    if (j <= 286)
      i = 0;
    if (i != this.serviceVersion);
    try
    {
      LocationService localLocationService = (LocationService)loadClass(i, this.config.optString("locDex5MD5"), this.config.getString("locDex5ClassName")).getConstructor(new Class[] { Context.class }).newInstance(new Object[] { this.context });
      if (this.service != null)
      {
        this.service.stop();
        this.service.removeListener(this.listener);
      }
      this.service = localLocationService;
      this.serviceVersion = i;
      localLocationService.addListener(this.listener);
      if (this.service == null)
      {
        this.service = new DPLocationService();
        this.serviceVersion = i;
        this.service.addListener(this.listener);
        Log.i("location", "DefaultLocationService loaded, version=" + i);
      }
      if ((System.currentTimeMillis() > this.configLastUpdateTime + 300000L) || (Environment.isDebug()))
        checkLocationConfig();
      configService().addListener("locDex5Version", this.configChangeListener);
      RealTimeLocator.getInstance(new DpIdSupplier()
      {
        public String getDpId()
        {
          return DeviceUtils.dpid();
        }
      }
      , DPApplication.instance(), RunMode.IN_MAIN_PROCESS).start();
      return this.service.start();
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      while (true)
        Log.w("location", localFileNotFoundException.getMessage());
    }
    catch (Exception localException)
    {
      while (true)
      {
        Log.e("location", "dex class load fail", localException);
        uploadException(this.config, localException);
      }
    }
  }

  public int status()
  {
    if (this.service == null)
      return 0;
    return this.service.status();
  }

  public void stop()
  {
    if (this.service != null)
      this.service.stop();
    configService().removeListener("locDex5Version", this.configChangeListener);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.proxy.LocationServiceProxy
 * JD-Core Version:    0.6.0
 */