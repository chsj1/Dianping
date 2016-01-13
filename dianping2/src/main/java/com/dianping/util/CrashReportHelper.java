package com.dianping.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.StringInputStream;
import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.dataservice.http.NetworkInfoHelper;
import com.dianping.dataservice.mapi.impl.DefaultMApiService;
import com.dianping.util.log.FileAppender;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

public class CrashReportHelper
{
  private static final String TAG;
  public static boolean debug;
  private static Thread.UncaughtExceptionHandler defaultHandler;
  private static final SimpleDateFormat fmt;
  public static long lastOutOfMemoryMills;
  public static final LinkedList<String> listSchema = new LinkedList();
  private static final LinkedList<String> listSchemaAll = new LinkedList();
  public static File reportFile;
  private static final CrashHandler unknownCrashHandler;
  public static int versionCode;
  public static String versionName;

  static
  {
    TAG = CrashReportHelper.class.getSimpleName();
    fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    unknownCrashHandler = new CrashHandler();
    Thread.setDefaultUncaughtExceptionHandler(unknownCrashHandler);
    versionCode = 0;
    versionName = null;
    debug = true;
  }

  static AccountService accountService()
  {
    return DPApplication.instance().accountService();
  }

  public static boolean deleteReport()
  {
    if (reportFile == null)
      return false;
    File localFile = new File(reportFile.getParent(), reportFile.getName() + ".bak");
    localFile.delete();
    return reportFile.renameTo(localFile);
  }

  private static String dpid()
  {
    String str = null;
    Object localObject = DPApplication.instance().getService("mapi_original");
    if ((localObject instanceof DefaultMApiService))
      str = ((DefaultMApiService)localObject).getDpid();
    do
    {
      return str;
      localObject = DPApplication.instance().getService("mapi");
    }
    while (!(localObject instanceof DefaultMApiService));
    return ((DefaultMApiService)localObject).getDpid();
  }

  // ERROR //
  public static String getReport()
  {
    // Byte code:
    //   0: getstatic 110	com/dianping/util/CrashReportHelper:reportFile	Ljava/io/File;
    //   3: ifnonnull +5 -> 8
    //   6: aconst_null
    //   7: areturn
    //   8: aconst_null
    //   9: astore_0
    //   10: aconst_null
    //   11: astore_2
    //   12: new 158	java/io/FileInputStream
    //   15: dup
    //   16: getstatic 110	com/dianping/util/CrashReportHelper:reportFile	Ljava/io/File;
    //   19: invokespecial 161	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   22: astore_1
    //   23: aload_1
    //   24: invokevirtual 165	java/io/FileInputStream:available	()I
    //   27: istore_3
    //   28: iload_3
    //   29: ldc 166
    //   31: if_icmple +20 -> 51
    //   34: aload_1
    //   35: ifnull -29 -> 6
    //   38: aload_1
    //   39: invokevirtual 169	java/io/FileInputStream:close	()V
    //   42: aconst_null
    //   43: areturn
    //   44: astore_0
    //   45: aload_0
    //   46: invokevirtual 172	java/io/IOException:printStackTrace	()V
    //   49: aconst_null
    //   50: areturn
    //   51: aload_1
    //   52: invokevirtual 165	java/io/FileInputStream:available	()I
    //   55: newarray byte
    //   57: astore_0
    //   58: aload_1
    //   59: aload_0
    //   60: invokevirtual 176	java/io/FileInputStream:read	([B)I
    //   63: pop
    //   64: new 178	java/lang/String
    //   67: dup
    //   68: aload_0
    //   69: invokespecial 181	java/lang/String:<init>	([B)V
    //   72: astore_0
    //   73: aload_1
    //   74: ifnull +7 -> 81
    //   77: aload_1
    //   78: invokevirtual 169	java/io/FileInputStream:close	()V
    //   81: aload_0
    //   82: areturn
    //   83: astore_1
    //   84: aload_1
    //   85: invokevirtual 172	java/io/IOException:printStackTrace	()V
    //   88: goto -7 -> 81
    //   91: astore_0
    //   92: aload_2
    //   93: astore_1
    //   94: aload_0
    //   95: astore_2
    //   96: aload_1
    //   97: astore_0
    //   98: aload_2
    //   99: invokevirtual 172	java/io/IOException:printStackTrace	()V
    //   102: aload_1
    //   103: ifnull -97 -> 6
    //   106: aload_1
    //   107: invokevirtual 169	java/io/FileInputStream:close	()V
    //   110: aconst_null
    //   111: areturn
    //   112: astore_0
    //   113: aload_0
    //   114: invokevirtual 172	java/io/IOException:printStackTrace	()V
    //   117: aconst_null
    //   118: areturn
    //   119: astore_1
    //   120: aload_0
    //   121: ifnull +7 -> 128
    //   124: aload_0
    //   125: invokevirtual 169	java/io/FileInputStream:close	()V
    //   128: aload_1
    //   129: athrow
    //   130: astore_0
    //   131: aload_0
    //   132: invokevirtual 172	java/io/IOException:printStackTrace	()V
    //   135: goto -7 -> 128
    //   138: astore_2
    //   139: aload_1
    //   140: astore_0
    //   141: aload_2
    //   142: astore_1
    //   143: goto -23 -> 120
    //   146: astore_2
    //   147: goto -51 -> 96
    //
    // Exception table:
    //   from	to	target	type
    //   38	42	44	java/io/IOException
    //   77	81	83	java/io/IOException
    //   12	23	91	java/io/IOException
    //   106	110	112	java/io/IOException
    //   12	23	119	finally
    //   98	102	119	finally
    //   124	128	130	java/io/IOException
    //   23	28	138	finally
    //   51	73	138	finally
    //   23	28	146	java/io/IOException
    //   51	73	146	java/io/IOException
  }

  // ERROR //
  public static String getReportBak()
  {
    // Byte code:
    //   0: getstatic 110	com/dianping/util/CrashReportHelper:reportFile	Ljava/io/File;
    //   3: ifnonnull +5 -> 8
    //   6: aconst_null
    //   7: areturn
    //   8: new 112	java/io/File
    //   11: dup
    //   12: getstatic 110	com/dianping/util/CrashReportHelper:reportFile	Ljava/io/File;
    //   15: invokevirtual 115	java/io/File:getParent	()Ljava/lang/String;
    //   18: new 117	java/lang/StringBuilder
    //   21: dup
    //   22: invokespecial 118	java/lang/StringBuilder:<init>	()V
    //   25: getstatic 110	com/dianping/util/CrashReportHelper:reportFile	Ljava/io/File;
    //   28: invokevirtual 121	java/io/File:getName	()Ljava/lang/String;
    //   31: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: ldc 127
    //   36: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   39: invokevirtual 130	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   42: invokespecial 133	java/io/File:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   45: astore_1
    //   46: aconst_null
    //   47: astore_0
    //   48: aconst_null
    //   49: astore_2
    //   50: new 158	java/io/FileInputStream
    //   53: dup
    //   54: aload_1
    //   55: invokespecial 161	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   58: astore_1
    //   59: aload_1
    //   60: invokevirtual 165	java/io/FileInputStream:available	()I
    //   63: istore_3
    //   64: iload_3
    //   65: ldc 166
    //   67: if_icmple +20 -> 87
    //   70: aload_1
    //   71: ifnull -65 -> 6
    //   74: aload_1
    //   75: invokevirtual 169	java/io/FileInputStream:close	()V
    //   78: aconst_null
    //   79: areturn
    //   80: astore_0
    //   81: aload_0
    //   82: invokevirtual 172	java/io/IOException:printStackTrace	()V
    //   85: aconst_null
    //   86: areturn
    //   87: aload_1
    //   88: invokevirtual 165	java/io/FileInputStream:available	()I
    //   91: newarray byte
    //   93: astore_0
    //   94: aload_1
    //   95: aload_0
    //   96: invokevirtual 176	java/io/FileInputStream:read	([B)I
    //   99: pop
    //   100: new 178	java/lang/String
    //   103: dup
    //   104: aload_0
    //   105: invokespecial 181	java/lang/String:<init>	([B)V
    //   108: astore_0
    //   109: aload_1
    //   110: ifnull +7 -> 117
    //   113: aload_1
    //   114: invokevirtual 169	java/io/FileInputStream:close	()V
    //   117: aload_0
    //   118: areturn
    //   119: astore_1
    //   120: aload_1
    //   121: invokevirtual 172	java/io/IOException:printStackTrace	()V
    //   124: goto -7 -> 117
    //   127: astore_0
    //   128: aload_2
    //   129: astore_1
    //   130: aload_0
    //   131: astore_2
    //   132: aload_1
    //   133: astore_0
    //   134: aload_2
    //   135: invokevirtual 172	java/io/IOException:printStackTrace	()V
    //   138: aload_1
    //   139: ifnull -133 -> 6
    //   142: aload_1
    //   143: invokevirtual 169	java/io/FileInputStream:close	()V
    //   146: aconst_null
    //   147: areturn
    //   148: astore_0
    //   149: aload_0
    //   150: invokevirtual 172	java/io/IOException:printStackTrace	()V
    //   153: aconst_null
    //   154: areturn
    //   155: astore_1
    //   156: aload_0
    //   157: ifnull +7 -> 164
    //   160: aload_0
    //   161: invokevirtual 169	java/io/FileInputStream:close	()V
    //   164: aload_1
    //   165: athrow
    //   166: astore_0
    //   167: aload_0
    //   168: invokevirtual 172	java/io/IOException:printStackTrace	()V
    //   171: goto -7 -> 164
    //   174: astore_2
    //   175: aload_1
    //   176: astore_0
    //   177: aload_2
    //   178: astore_1
    //   179: goto -23 -> 156
    //   182: astore_2
    //   183: goto -51 -> 132
    //
    // Exception table:
    //   from	to	target	type
    //   74	78	80	java/io/IOException
    //   113	117	119	java/io/IOException
    //   50	59	127	java/io/IOException
    //   142	146	148	java/io/IOException
    //   50	59	155	finally
    //   134	138	155	finally
    //   160	164	166	java/io/IOException
    //   59	64	174	finally
    //   87	109	174	finally
    //   59	64	182	java/io/IOException
    //   87	109	182	java/io/IOException
  }

  private static boolean hasOutOfMemoryError(Throwable paramThrowable)
  {
    if (paramThrowable == null);
    label53: 
    while (true)
    {
      return false;
      Throwable localThrowable2 = paramThrowable.getCause();
      int i = 0;
      Throwable localThrowable1 = paramThrowable;
      paramThrowable = localThrowable2;
      while (true)
      {
        if (i >= 15)
          break label53;
        if ((localThrowable1 instanceof OutOfMemoryError))
          return true;
        if ((paramThrowable == null) || (paramThrowable == localThrowable1))
          break;
        localThrowable1 = paramThrowable;
        paramThrowable = localThrowable1.getCause();
        i += 1;
      }
    }
  }

  static HttpService httpService()
  {
    return DPApplication.instance().httpService();
  }

  public static void initialize(Context paramContext)
  {
    if (reportFile != null)
      return;
    PackageInfo localPackageInfo = DeviceUtils.getPackageInfo(paramContext);
    versionCode = Environment.versionCode();
    versionName = Environment.versionName();
    if (localPackageInfo.applicationInfo != null)
      if ((localPackageInfo.applicationInfo.flags & 0x2) == 0)
        break label101;
    label101: for (boolean bool = true; ; bool = false)
    {
      debug = bool;
      reportFile = new File(paramContext.getFilesDir(), "crash_report");
      paramContext = new File(reportFile.getParent(), "out_of_memory");
      if (!paramContext.exists())
        break;
      lastOutOfMemoryMills = paramContext.lastModified();
      paramContext.delete();
      return;
    }
  }

  public static void installSafeLooper()
  {
    SafeLooper.install();
    if (!Environment.isDebug())
      defaultHandler = null;
    SafeLooper.setUncaughtExceptionHandler(unknownCrashHandler);
  }

  public static boolean isAvailable()
  {
    return (reportFile != null) && (reportFile.exists());
  }

  public static boolean isReportOn()
  {
    if (ConfigHelper.crashReportCount == 0)
      return true;
    Object localObject = DPApplication.instance();
    localObject = ((DPApplication)localObject).getSharedPreferences(((DPApplication)localObject).getPackageName(), 0);
    long l1 = System.currentTimeMillis();
    long l2 = ((SharedPreferences)localObject).getLong("baseReportTime", 0L);
    int i = ((SharedPreferences)localObject).getInt("reportCount", 0);
    localObject = ((SharedPreferences)localObject).edit();
    if ((l1 - l2 > 0L) && (l1 - l2 < 86400000L))
    {
      if (i < ConfigHelper.crashReportCount)
      {
        ((SharedPreferences.Editor)localObject).putInt("reportCount", i + 1);
        ((SharedPreferences.Editor)localObject).apply();
        return true;
      }
      return false;
    }
    ((SharedPreferences.Editor)localObject).putLong("baseReportTime", l1);
    ((SharedPreferences.Editor)localObject).putInt("reportCount", 1);
    ((SharedPreferences.Editor)localObject).apply();
    return true;
  }

  public static void putUrlSchema(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return;
    putUrlSchemeInternal("start: " + paramString);
    Log.d(TAG, "urlSchema: " + paramString);
    if (listSchema.size() > 10)
      listSchema.removeLast();
    listSchema.addFirst(paramString);
  }

  public static void putUrlSchemaOnShow(String paramString)
  {
    if (android.text.TextUtils.isEmpty(paramString))
      return;
    putUrlSchemeInternal("show: " + paramString);
  }

  private static void putUrlSchemeInternal(String paramString)
  {
    if (android.text.TextUtils.isEmpty(paramString));
    do
    {
      return;
      if (listSchemaAll.size() > 20)
        listSchemaAll.removeLast();
      listSchemaAll.addFirst(paramString + " " + fmt.format(new Date(DateUtil.currentTimeMillis())));
    }
    while (!Environment.isDebug());
    Log.d(TAG, "putUrlSchemeInternal: " + paramString + " " + fmt.format(new Date(DateUtil.currentTimeMillis())));
  }

  public static void sendAndDelete()
  {
    if (!isAvailable());
    String str;
    do
    {
      return;
      str = getReport();
      deleteReport();
      Log.i(TAG, str);
    }
    while ((str == null) || (!str.startsWith("====")) || (str.contains("debug=true")));
    new Thread("Send Crash Report", str)
    {
      public void run()
      {
        try
        {
          BasicHttpRequest localBasicHttpRequest = new BasicHttpRequest("http://stat.api.dianping.com/utm.js?v=androidv1c", "POST", new StringInputStream(this.val$report, "UTF-8"));
          if (((HttpResponse)CrashReportHelper.httpService().execSync(localBasicHttpRequest)).result() != null)
          {
            Log.d(CrashReportHelper.TAG, "Crash report send success");
            return;
          }
          Log.e(CrashReportHelper.TAG, "Failed to send crash report");
          return;
        }
        catch (Exception localException)
        {
          Log.e(CrashReportHelper.TAG, "Failed to send crash report " + localException);
        }
      }
    }
    .start();
  }

  public static class CrashHandler
    implements Thread.UncaughtExceptionHandler
  {
    public final void uncaughtException(Thread paramThread, Throwable paramThrowable)
    {
      int j = 0;
      boolean bool2 = false;
      boolean bool1 = bool2;
      int i = j;
      try
      {
        boolean bool3 = CrashReportHelper.isReportOn();
        if (!bool3)
          if (0 != 0)
            Process.killProcess(Process.myPid());
        while (true)
        {
          do
            return;
          while (CrashReportHelper.defaultHandler == null);
          CrashReportHelper.defaultHandler.uncaughtException(paramThread, paramThrowable);
          return;
          bool1 = bool2;
          i = j;
          FileAppender.getInstance().flush();
          bool1 = bool2;
          i = j;
          localObject1 = CrashReportHelper.reportFile;
          if (localObject1 != null)
            break;
          if (0 != 0)
          {
            Process.killProcess(Process.myPid());
            return;
          }
          if (CrashReportHelper.defaultHandler == null)
            continue;
          CrashReportHelper.defaultHandler.uncaughtException(paramThread, paramThrowable);
          return;
        }
        bool1 = bool2;
        i = j;
        bool2 = CrashReportHelper.access$100(paramThrowable);
        if (bool2)
        {
          bool1 = bool2;
          i = bool2;
          localObject1 = new File(CrashReportHelper.reportFile.getParent(), "out_of_memory");
          bool1 = bool2;
          i = bool2;
          ((File)localObject1).delete();
          bool1 = bool2;
          i = bool2;
          ((File)localObject1).createNewFile();
        }
        bool1 = bool2;
        i = bool2;
        Object localObject1 = new PrintWriter(CrashReportHelper.reportFile, "utf-8");
        bool1 = bool2;
        i = bool2;
        ((PrintWriter)localObject1).print("===============================");
        bool1 = bool2;
        i = bool2;
        ((PrintWriter)localObject1).print(UUID.randomUUID().toString());
        bool1 = bool2;
        i = bool2;
        ((PrintWriter)localObject1).println("============================");
        bool1 = bool2;
        i = bool2;
        if (CrashReportHelper.debug)
        {
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println("debug=true");
        }
        bool1 = bool2;
        i = bool2;
        ((PrintWriter)localObject1).print("efte-version=");
        bool1 = bool2;
        i = bool2;
        ((PrintWriter)localObject1).println(Environment.efteVersion());
        bool1 = bool2;
        i = bool2;
        ((PrintWriter)localObject1).print("addtime=");
        bool1 = bool2;
        i = bool2;
        ((PrintWriter)localObject1).println(CrashReportHelper.fmt.format(new Date(DateUtil.currentTimeMillis())));
        bool1 = bool2;
        i = bool2;
        if ("huidutest".equalsIgnoreCase(Environment.source()))
        {
          bool1 = bool2;
          i = bool2;
          Object localObject3 = Environment.mapiUserAgent();
          bool1 = bool2;
          i = bool2;
          if (!TextUtils.isEmpty((CharSequence)localObject3))
          {
            bool1 = bool2;
            i = bool2;
            String str = Environment.versionName();
            bool1 = bool2;
            i = bool2;
            localObject3 = ((String)localObject3).replace(str, str + "_huidutest");
            bool1 = bool2;
            i = bool2;
            ((PrintWriter)localObject1).print("user-agent=");
            bool1 = bool2;
            i = bool2;
            ((PrintWriter)localObject1).println((String)localObject3);
          }
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("deviceid=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(Environment.deviceId());
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("dpid=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(CrashReportHelper.access$300());
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("sessionid=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(Environment.sessionId());
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("cityid=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(DPApplication.instance().city());
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("token=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(CrashReportHelper.accountService().token());
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("network=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(new NetworkInfoHelper(DPApplication.instance()).getNetworkInfo());
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("os-version=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(Build.VERSION.RELEASE);
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("os-build=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(Build.ID);
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("device-brand=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(Build.BRAND);
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("device-model=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(Build.MODEL);
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("device-fingerprint=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(Build.FINGERPRINT);
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("thread=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(paramThread.getName());
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).print("buildNumber=");
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println(Environment.buildNumber());
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println();
          bool1 = bool2;
          i = bool2;
          paramThrowable.printStackTrace((PrintWriter)localObject1);
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println();
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println();
          bool1 = bool2;
          i = bool2;
          ((PrintWriter)localObject1).println("Url Schema history full:");
          bool1 = bool2;
          i = bool2;
          localObject3 = CrashReportHelper.listSchemaAll.iterator();
          while (true)
          {
            bool1 = bool2;
            i = bool2;
            if (!((Iterator)localObject3).hasNext())
              break;
            bool1 = bool2;
            i = bool2;
            ((PrintWriter)localObject1).println((String)((Iterator)localObject3).next());
          }
        }
      }
      catch (Throwable localThrowable)
      {
        while (true)
        {
          i = bool1;
          localThrowable.printStackTrace();
          if (!bool1)
            break;
          Process.killProcess(Process.myPid());
          return;
          bool1 = bool2;
          i = bool2;
          localThrowable.print("user-agent=");
          bool1 = bool2;
          i = bool2;
          localThrowable.println(Environment.mapiUserAgent());
        }
      }
      finally
      {
        if (i == 0)
          break label1117;
      }
      Process.killProcess(Process.myPid());
      while (true)
      {
        throw localObject2;
        bool1 = bool2;
        i = bool2;
        localObject2.close();
        if (bool2)
        {
          Process.killProcess(Process.myPid());
          return;
        }
        if (CrashReportHelper.defaultHandler == null)
          break;
        CrashReportHelper.defaultHandler.uncaughtException(paramThread, paramThrowable);
        return;
        if (CrashReportHelper.defaultHandler == null)
          break;
        CrashReportHelper.defaultHandler.uncaughtException(paramThread, paramThrowable);
        return;
        label1117: if (CrashReportHelper.defaultHandler == null)
          continue;
        CrashReportHelper.defaultHandler.uncaughtException(paramThread, paramThrowable);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.CrashReportHelper
 * JD-Core Version:    0.6.0
 */