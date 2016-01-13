package com.dianping.base.push.pushservice.dp;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Handler;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.base.push.pushservice.dp.impl3v8.PushServiceImpl;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.util.Log;
import com.dianping.util.StringUtil;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.security.MessageDigest;
import org.json.JSONException;
import org.json.JSONObject;

public class PushServiceProxy
  implements PushService
{
  public static final String ACTION_UPDATE_DPPUSH = "com.dianping.action.UPDATE_DPPUSH";
  private static final String K_CLASSNAME = "pushDex5ClassName";
  static final String K_MD5 = "pushDex5MD5";
  static final String K_URL = "pushDex5Url";
  static final String K_VERSION = "pushDex5Version";
  private static final int MIN_PUSH_DEX_VERSION = 38;
  static final String TAG = PushServiceProxy.class.getSimpleName();
  private final RequestHandler<HttpRequest, HttpResponse> apkHandler = new RequestHandler()
  {
    public void onRequestFailed(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      Log.w(PushServiceProxy.TAG, "fail to download apk from " + paramHttpRequest.url());
    }

    public void onRequestFinish(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse)
    {
      Object localObject1;
      String str;
      int i;
      try
      {
        localObject2 = PushServiceProxy.this.getConfig();
        localObject1 = (byte[])(byte[])paramHttpResponse.result();
        str = ((JSONObject)localObject2).optString("pushDex5Url");
        if (!paramHttpRequest.url().equals(str))
          return;
        i = ((JSONObject)localObject2).optInt("pushDex5Version");
        paramHttpResponse = ((JSONObject)localObject2).optString("pushDex5MD5");
        if (!TextUtils.isEmpty(paramHttpResponse))
        {
          localObject2 = MessageDigest.getInstance("MD5");
          ((MessageDigest)localObject2).reset();
          ((MessageDigest)localObject2).update(localObject1);
          localObject2 = StringUtil.byteArrayToHexString(((MessageDigest)localObject2).digest());
          if (!paramHttpResponse.equals(localObject2))
          {
            paramHttpResponse = str + "'s md5 not match, length=" + localObject1.length + ", " + (String)localObject2 + " != " + paramHttpResponse;
            Log.w(PushServiceProxy.TAG, paramHttpResponse);
            throw new Exception(paramHttpResponse);
          }
        }
      }
      catch (Exception paramHttpResponse)
      {
        Log.w(PushServiceProxy.TAG, "fail to save apk from " + paramHttpRequest.url(), paramHttpResponse);
        return;
      }
      Object localObject2 = new File(PushServiceProxy.this.dir, "v" + i);
      ((File)localObject2).mkdir();
      if (TextUtils.isEmpty(paramHttpResponse));
      File localFile;
      for (paramHttpResponse = "1.apk"; ; paramHttpResponse = paramHttpResponse + ".apk")
      {
        localFile = new File((File)localObject2, paramHttpResponse);
        if (!localFile.exists())
          break;
        Log.w(PushServiceProxy.TAG, "apk already exists, " + localFile);
        return;
      }
      paramHttpResponse = new File((File)localObject2, "tmp.apk");
      localObject2 = new FileOutputStream(paramHttpResponse);
      try
      {
        ((FileOutputStream)localObject2).write(localObject1);
        ((FileOutputStream)localObject2).close();
        if (!PushServiceProxy.this.verify(paramHttpResponse))
        {
          localObject1 = "illegal apk file from " + str;
          Log.w(PushServiceProxy.TAG, (String)localObject1);
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
        Log.w(PushServiceProxy.TAG, (String)localObject1);
        paramHttpResponse.delete();
        throw new Exception((String)localObject1);
      }
      Log.i(PushServiceProxy.TAG, "apk saved to " + localFile);
      PushServiceProxy.this.updateNow();
    }
  };
  final Context context;
  File dir;
  private HttpRequest downReq;
  private PushService pushService;
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.UPDATE_DPPUSH".equals(paramIntent.getAction()))
      {
        Log.i(PushServiceProxy.TAG, "dppushservice version updated");
        Log.i(PushServiceProxy.TAG, String.valueOf(PushServiceProxy.this.getConfig()));
        PushServiceProxy.this.checkPushConfig();
      }
    }
  };
  int serviceVersion;

  public PushServiceProxy(Context paramContext)
  {
    this.context = paramContext;
  }

  private HttpService httpService()
  {
    if ((this.context.getApplicationContext() instanceof DPApplication))
      return (HttpService)((DPApplication)this.context.getApplicationContext()).getService("http");
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
    Log.i(TAG, paramString2 + " loaded from " + paramString1 + ", version=" + paramInt);
    return (Class<?>)localObject;
  }

  public void checkPushConfig()
  {
    while (true)
    {
      try
      {
        JSONObject localJSONObject = getConfig();
        int j = localJSONObject.optInt("pushDex5Version");
        String str1 = localJSONObject.optString("pushDex5MD5");
        i = 0;
        if (j > 38)
          continue;
        j = 0;
        i = 1;
        if ((i == 0) || (j == this.serviceVersion))
          break;
        updateNow();
        return;
        File localFile = new File(this.dir, "v" + j);
        if (TextUtils.isEmpty(str1))
        {
          str1 = "1.apk";
          if (new File(localFile, str1).length() != 0L)
            break label247;
          str1 = localJSONObject.optString("pushDex5Url");
          Log.i(TAG, "require apk from " + str1);
          this.downReq = BasicHttpRequest.httpGet(str1);
          httpService().abort(this.downReq, this.apkHandler, true);
          httpService().exec(this.downReq, this.apkHandler);
          continue;
        }
      }
      catch (Exception localException)
      {
        Log.w(TAG, "malformed config from configService");
        Log.w(TAG, getConfig().toString());
        Log.w(TAG, "", localException);
        return;
      }
      String str2 = localException + ".apk";
      continue;
      label247: int i = 1;
    }
  }

  @TargetApi(11)
  JSONObject getConfig()
  {
    Object localObject;
    if (Build.VERSION.SDK_INT >= 11)
      localObject = this.context.getSharedPreferences("dppushservice", 4);
    while (true)
    {
      localObject = ((SharedPreferences)localObject).getString("config", "");
      try
      {
        localObject = new JSONObject((String)localObject);
        return localObject;
        localObject = this.context.getSharedPreferences("dppushservice", 0);
      }
      catch (JSONException localJSONException)
      {
      }
    }
    return (JSONObject)new JSONObject();
  }

  public void onCreate(Service paramService)
  {
    this.context.registerReceiver(this.receiver, new IntentFilter("com.dianping.action.UPDATE_DPPUSH"));
    Log.i(TAG, "onCteate");
    this.dir = new File(this.context.getFilesDir(), "pushservice");
    this.dir.mkdir();
    Object localObject = getConfig();
    int j = ((JSONObject)localObject).optInt("pushDex5Version", 0);
    Log.i(TAG, "version=" + j);
    Log.i(TAG, "serviceVersion=" + this.serviceVersion);
    int i = j;
    if (j <= 38)
      i = 0;
    if (i != this.serviceVersion);
    try
    {
      localObject = (PushService)loadClass(i, ((JSONObject)localObject).optString("pushDex5MD5"), ((JSONObject)localObject).optString("pushDex5ClassName")).getConstructor(new Class[0]).newInstance(new Object[0]);
      if (this.pushService != null)
        this.pushService.stopService(this.context);
      this.pushService = ((PushService)localObject);
      this.serviceVersion = i;
      if (this.pushService == null)
      {
        this.pushService = new PushServiceImpl();
        this.serviceVersion = i;
        Log.i(TAG, "DefaultPushService loaded, version=" + i);
      }
      checkPushConfig();
      this.pushService.onCreate(paramService);
      return;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      while (true)
        Log.w(TAG, localFileNotFoundException.getMessage());
    }
    catch (Exception localException)
    {
      while (true)
        Log.e(TAG, "dex class load fail", localException);
    }
  }

  public void onDestroy(Service paramService)
  {
    this.context.unregisterReceiver(this.receiver);
    if (this.downReq != null)
      httpService().abort(this.downReq, this.apkHandler, true);
    this.pushService.onDestroy(paramService);
  }

  public int onStartCommand(Service paramService, Intent paramIntent, int paramInt1, int paramInt2)
  {
    Log.i(TAG, "onStartCommand");
    return this.pushService.onStartCommand(paramService, paramIntent, paramInt1, paramInt2);
  }

  public void startService(Context paramContext)
  {
    this.pushService.startService(paramContext);
  }

  public void stopService(Context paramContext)
  {
    this.pushService.stopService(paramContext);
  }

  void updateNow()
  {
    new Handler().post(new Runnable()
    {
      public void run()
      {
        Log.i(PushServiceProxy.TAG, "restart service from version " + PushServiceProxy.this.serviceVersion + " to " + PushServiceProxy.this.getConfig().optInt("pushDex5Version"));
        PushServiceProxy.this.stopService(PushServiceProxy.this.context);
        PushServiceProxy.this.startService(PushServiceProxy.this.context);
      }
    });
  }

  // ERROR //
  boolean verify(File paramFile)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore 4
    //   5: new 339	java/util/jar/JarFile
    //   8: dup
    //   9: aload_1
    //   10: invokespecial 342	java/util/jar/JarFile:<init>	(Ljava/io/File;)V
    //   13: astore_3
    //   14: aload_3
    //   15: invokevirtual 346	java/util/jar/JarFile:entries	()Ljava/util/Enumeration;
    //   18: astore_2
    //   19: aload_2
    //   20: invokeinterface 351 1 0
    //   25: ifeq +136 -> 161
    //   28: aload_2
    //   29: invokeinterface 355 1 0
    //   34: checkcast 357	java/util/jar/JarEntry
    //   37: astore 4
    //   39: aload 4
    //   41: invokevirtual 360	java/util/jar/JarEntry:getName	()Ljava/lang/String;
    //   44: ldc_w 362
    //   47: invokevirtual 368	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   50: ifeq -31 -> 19
    //   53: sipush 8192
    //   56: newarray byte
    //   58: astore 5
    //   60: aload_3
    //   61: aload 4
    //   63: invokevirtual 372	java/util/jar/JarFile:getInputStream	(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;
    //   66: astore 6
    //   68: aload 6
    //   70: aload 5
    //   72: iconst_0
    //   73: aload 5
    //   75: arraylength
    //   76: invokevirtual 378	java/io/InputStream:read	([BII)I
    //   79: iconst_m1
    //   80: if_icmpne -12 -> 68
    //   83: aload 6
    //   85: invokevirtual 381	java/io/InputStream:close	()V
    //   88: aload 4
    //   90: invokevirtual 385	java/util/jar/JarEntry:getCertificates	()[Ljava/security/cert/Certificate;
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
    //   115: invokevirtual 391	java/security/cert/Certificate:getEncoded	()[B
    //   118: invokestatic 397	com/dianping/util/StringUtil:byteArrayToHexString	([B)Ljava/lang/String;
    //   121: invokevirtual 401	java/lang/String:hashCode	()I
    //   124: istore 9
    //   126: iload 9
    //   128: ldc_w 402
    //   131: if_icmpne +21 -> 152
    //   134: aload_3
    //   135: ifnull +7 -> 142
    //   138: aload_3
    //   139: invokevirtual 403	java/util/jar/JarFile:close	()V
    //   142: iconst_1
    //   143: ireturn
    //   144: astore_1
    //   145: aload_1
    //   146: invokevirtual 406	java/io/IOException:printStackTrace	()V
    //   149: goto -7 -> 142
    //   152: iload 7
    //   154: iconst_1
    //   155: iadd
    //   156: istore 7
    //   158: goto -55 -> 103
    //   161: aload_3
    //   162: ifnull +103 -> 265
    //   165: aload_3
    //   166: invokevirtual 403	java/util/jar/JarFile:close	()V
    //   169: iconst_0
    //   170: ireturn
    //   171: astore_1
    //   172: aload_1
    //   173: invokevirtual 406	java/io/IOException:printStackTrace	()V
    //   176: goto -7 -> 169
    //   179: astore_2
    //   180: aload 4
    //   182: astore_3
    //   183: aload_2
    //   184: astore 4
    //   186: aload_3
    //   187: astore_2
    //   188: getstatic 56	com/dianping/base/push/pushservice/dp/PushServiceProxy:TAG	Ljava/lang/String;
    //   191: new 102	java/lang/StringBuilder
    //   194: dup
    //   195: invokespecial 103	java/lang/StringBuilder:<init>	()V
    //   198: ldc_w 408
    //   201: invokevirtual 109	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   204: aload_1
    //   205: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   208: invokevirtual 115	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   211: aload 4
    //   213: invokestatic 228	com/dianping/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   216: aload_3
    //   217: ifnull -48 -> 169
    //   220: aload_3
    //   221: invokevirtual 403	java/util/jar/JarFile:close	()V
    //   224: goto -55 -> 169
    //   227: astore_1
    //   228: aload_1
    //   229: invokevirtual 406	java/io/IOException:printStackTrace	()V
    //   232: goto -63 -> 169
    //   235: astore_1
    //   236: aload_2
    //   237: ifnull +7 -> 244
    //   240: aload_2
    //   241: invokevirtual 403	java/util/jar/JarFile:close	()V
    //   244: aload_1
    //   245: athrow
    //   246: astore_2
    //   247: aload_2
    //   248: invokevirtual 406	java/io/IOException:printStackTrace	()V
    //   251: goto -7 -> 244
    //   254: astore_1
    //   255: aload_3
    //   256: astore_2
    //   257: goto -21 -> 236
    //   260: astore 4
    //   262: goto -76 -> 186
    //   265: goto -96 -> 169
    //
    // Exception table:
    //   from	to	target	type
    //   138	142	144	java/io/IOException
    //   165	169	171	java/io/IOException
    //   5	14	179	java/lang/Exception
    //   220	224	227	java/io/IOException
    //   5	14	235	finally
    //   188	216	235	finally
    //   240	244	246	java/io/IOException
    //   14	19	254	finally
    //   19	68	254	finally
    //   68	100	254	finally
    //   110	126	254	finally
    //   14	19	260	java/lang/Exception
    //   19	68	260	java/lang/Exception
    //   68	100	260	java/lang/Exception
    //   110	126	260	java/lang/Exception
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.dp.PushServiceProxy
 * JD-Core Version:    0.6.0
 */