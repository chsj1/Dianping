package cloudwns.l;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Environment;
import android.util.Log;
import cloudwns.b.a;
import cloudwns.b.h;
import cloudwns.d.n;
import cloudwns.d.o;
import com.tencent.base.Global;
import com.tencent.wns.client.data.Option;
import com.tencent.wns.client.inte.WnsService.GlobalListener;
import com.tencent.wns.data.e.c;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class c
  implements SharedPreferences.OnSharedPreferenceChangeListener
{
  protected static final cloudwns.b.c CLIENT_CONFIG;
  public static final long HOUR = 3600000L;
  protected static final cloudwns.b.c SERVICE_CONFIG;
  private static c a = null;
  private volatile boolean b = true;
  private volatile boolean c = true;
  private volatile boolean d;
  protected a fileTracer;

  static
  {
    int i = Option.getInt("debug.file.blockcount", 24);
    long l = Option.getLong("debug.file.keepperiod", 604800000L);
    File localFile = getLogFilePath();
    CLIENT_CONFIG = new cloudwns.b.c(localFile, i, 262144, 8192, "Wns.Client.File.Tracer", 10000L, 10, ".app.log", l);
    SERVICE_CONFIG = new cloudwns.b.c(localFile, i, 262144, 8192, "Wns.File.Tracer", 10000L, 10, ".wns.log", l);
  }

  public c()
  {
    if (Global.isDebug());
    while (true)
    {
      this.d = bool;
      Option.startListen(this);
      return;
      bool = false;
    }
  }

  private void a(int paramInt, String paramString1, String paramString2, Throwable paramThrowable)
  {
    int j = 2;
    WnsService.GlobalListener localGlobalListener = Global.getGlobalListener();
    int i = j;
    switch (paramInt)
    {
    default:
      i = j;
    case 1:
    case 2:
    case 4:
    case 8:
    case 16:
    case 32:
    }
    while (true)
    {
      localGlobalListener.onPrintLog(i, paramString1, paramString2, paramThrowable);
      return;
      i = 3;
      continue;
      i = 4;
      continue;
      i = 5;
      continue;
      i = 6;
      continue;
      i = 7;
    }
  }

  // ERROR //
  private static boolean a(File paramFile, String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull +12 -> 13
    //   4: aload_1
    //   5: invokestatic 106	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   8: ifeq +5 -> 13
    //   11: iconst_0
    //   12: ireturn
    //   13: aconst_null
    //   14: astore_2
    //   15: new 108	java/io/FileOutputStream
    //   18: dup
    //   19: aload_0
    //   20: iconst_1
    //   21: invokespecial 111	java/io/FileOutputStream:<init>	(Ljava/io/File;Z)V
    //   24: astore_0
    //   25: aload_0
    //   26: aload_1
    //   27: ldc 113
    //   29: invokevirtual 119	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   32: invokevirtual 123	java/io/FileOutputStream:write	([B)V
    //   35: aload_0
    //   36: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   39: ireturn
    //   40: astore_0
    //   41: aconst_null
    //   42: astore_0
    //   43: aload_0
    //   44: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   47: ireturn
    //   48: astore_0
    //   49: aconst_null
    //   50: astore_0
    //   51: aload_0
    //   52: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   55: ireturn
    //   56: astore_1
    //   57: aload_2
    //   58: astore_0
    //   59: aload_0
    //   60: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   63: pop
    //   64: aload_1
    //   65: athrow
    //   66: astore_1
    //   67: goto -8 -> 59
    //   70: astore_1
    //   71: goto -20 -> 51
    //   74: astore_1
    //   75: goto -32 -> 43
    //
    // Exception table:
    //   from	to	target	type
    //   15	25	40	java/io/FileNotFoundException
    //   15	25	48	java/io/IOException
    //   15	25	56	finally
    //   25	35	66	finally
    //   25	35	70	java/io/IOException
    //   25	35	74	java/io/FileNotFoundException
  }

  // ERROR //
  private static boolean a(List paramList, File paramFile, String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnull +17 -> 18
    //   4: aload_0
    //   5: invokeinterface 137 1 0
    //   10: iconst_1
    //   11: if_icmplt +7 -> 18
    //   14: aload_1
    //   15: ifnonnull +5 -> 20
    //   18: iconst_0
    //   19: ireturn
    //   20: new 108	java/io/FileOutputStream
    //   23: dup
    //   24: aload_1
    //   25: iconst_1
    //   26: invokespecial 111	java/io/FileOutputStream:<init>	(Ljava/io/File;Z)V
    //   29: astore_1
    //   30: aload_2
    //   31: invokestatic 106	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   34: ifne +13 -> 47
    //   37: aload_1
    //   38: aload_2
    //   39: ldc 113
    //   41: invokevirtual 119	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   44: invokevirtual 123	java/io/FileOutputStream:write	([B)V
    //   47: sipush 4096
    //   50: newarray byte
    //   52: astore_2
    //   53: iconst_0
    //   54: istore 4
    //   56: iload 4
    //   58: aload_0
    //   59: invokeinterface 137 1 0
    //   64: if_icmpge +67 -> 131
    //   67: new 139	java/io/FileInputStream
    //   70: dup
    //   71: aload_0
    //   72: iload 4
    //   74: invokeinterface 143 2 0
    //   79: checkcast 145	java/io/File
    //   82: invokespecial 148	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   85: astore_3
    //   86: aload_3
    //   87: aload_2
    //   88: iconst_0
    //   89: aload_2
    //   90: arraylength
    //   91: invokevirtual 152	java/io/FileInputStream:read	([BII)I
    //   94: istore 5
    //   96: iload 5
    //   98: ifle +20 -> 118
    //   101: aload_1
    //   102: aload_2
    //   103: iconst_0
    //   104: iload 5
    //   106: invokevirtual 155	java/io/FileOutputStream:write	([BII)V
    //   109: goto -23 -> 86
    //   112: astore_0
    //   113: aload_1
    //   114: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   117: ireturn
    //   118: aload_3
    //   119: invokevirtual 158	java/io/FileInputStream:close	()V
    //   122: iload 4
    //   124: iconst_1
    //   125: iadd
    //   126: istore 4
    //   128: goto -72 -> 56
    //   131: aload_1
    //   132: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   135: ireturn
    //   136: astore_0
    //   137: aconst_null
    //   138: astore_1
    //   139: aload_1
    //   140: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   143: ireturn
    //   144: astore_0
    //   145: aconst_null
    //   146: astore_1
    //   147: aload_1
    //   148: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   151: ireturn
    //   152: astore_0
    //   153: aconst_null
    //   154: astore_1
    //   155: aload_1
    //   156: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   159: pop
    //   160: aload_0
    //   161: athrow
    //   162: astore_0
    //   163: goto -8 -> 155
    //   166: astore_0
    //   167: goto -20 -> 147
    //   170: astore_0
    //   171: goto -32 -> 139
    //   174: astore_0
    //   175: aconst_null
    //   176: astore_1
    //   177: goto -64 -> 113
    //
    // Exception table:
    //   from	to	target	type
    //   30	47	112	java/io/FileNotFoundException
    //   47	53	112	java/io/FileNotFoundException
    //   56	86	112	java/io/FileNotFoundException
    //   86	96	112	java/io/FileNotFoundException
    //   101	109	112	java/io/FileNotFoundException
    //   118	122	112	java/io/FileNotFoundException
    //   20	30	136	java/io/UnsupportedEncodingException
    //   20	30	144	java/io/IOException
    //   20	30	152	finally
    //   30	47	162	finally
    //   47	53	162	finally
    //   56	86	162	finally
    //   86	96	162	finally
    //   101	109	162	finally
    //   118	122	162	finally
    //   30	47	166	java/io/IOException
    //   47	53	166	java/io/IOException
    //   56	86	166	java/io/IOException
    //   86	96	166	java/io/IOException
    //   101	109	166	java/io/IOException
    //   118	122	166	java/io/IOException
    //   30	47	170	java/io/UnsupportedEncodingException
    //   47	53	170	java/io/UnsupportedEncodingException
    //   56	86	170	java/io/UnsupportedEncodingException
    //   86	96	170	java/io/UnsupportedEncodingException
    //   101	109	170	java/io/UnsupportedEncodingException
    //   118	122	170	java/io/UnsupportedEncodingException
    //   20	30	174	java/io/FileNotFoundException
  }

  public static void autoTrace(int paramInt, String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (a != null)
      a.trace(paramInt, paramString1, paramString2, paramThrowable);
  }

  public static void cleanClientLog()
  {
    Object localObject = CLIENT_CONFIG.a(System.currentTimeMillis());
    localObject = CLIENT_CONFIG.b((File)localObject);
    if (localObject != null)
    {
      int i = 0;
      while (i < localObject.length)
      {
        deleteFile(localObject[i]);
        i += 1;
      }
    }
  }

  public static void cleanWnsLog()
  {
    Object localObject = SERVICE_CONFIG.a(System.currentTimeMillis());
    localObject = SERVICE_CONFIG.b((File)localObject);
    if (localObject != null)
    {
      int i = 0;
      while (i < localObject.length)
      {
        deleteFile(localObject[i]);
        i += 1;
      }
    }
  }

  public static void deleteFile(File paramFile)
  {
    if ((paramFile == null) || (!paramFile.exists()));
    while (true)
    {
      return;
      if (paramFile.isFile())
      {
        paramFile.delete();
        return;
      }
      paramFile = paramFile.listFiles();
      int j = paramFile.length;
      int i = 0;
      while (i < j)
      {
        deleteFile(paramFile[i]);
        i += 1;
      }
    }
  }

  public static BufferedReader getClientLogReader(int paramInt)
  {
    Object localObject = CLIENT_CONFIG.a(System.currentTimeMillis());
    if ((localObject == null) || (!((File)localObject).isDirectory()))
      return null;
    localObject = CLIENT_CONFIG.b((File)localObject);
    localObject = CLIENT_CONFIG.a(localObject);
    if ((paramInt >= 0) && (paramInt < localObject.length))
    {
      localObject = localObject[(localObject.length - paramInt - 1)];
      try
      {
        localObject = new BufferedReader(new FileReader((File)localObject));
        return localObject;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        return null;
      }
    }
    return (BufferedReader)null;
  }

  public static File getLogFilePath()
  {
    int j = 0;
    String str = e.c.a + File.separator + Global.getPackageName();
    o localo = n.c();
    int i = j;
    if (localo != null)
    {
      i = j;
      if (localo.b() > 8388608L)
        i = 1;
    }
    if (i != 0)
      return new File(Environment.getExternalStorageDirectory(), str);
    return new File(Global.getFilesDir(), str);
  }

  public static BufferedReader getWnsLogReader(int paramInt)
  {
    Object localObject1 = SERVICE_CONFIG.a(System.currentTimeMillis());
    localObject1 = SERVICE_CONFIG.b((File)localObject1);
    if (localObject1 == null)
      return null;
    localObject1 = SERVICE_CONFIG.a(localObject1);
    if ((paramInt >= 0) && (paramInt < localObject1.length));
    Object localObject2;
    for (localObject1 = localObject1[(localObject1.length - paramInt - 1)]; ; localObject2 = null)
      try
      {
        localObject1 = new BufferedReader(new FileReader((File)localObject1));
        return localObject1;
      }
      catch (FileNotFoundException localObject2)
      {
        while (true)
          localObject2 = null;
      }
  }

  public static File prepareReportLogFile(long paramLong)
  {
    int j = 1;
    long l = paramLong;
    if (paramLong < 1L)
      l = System.currentTimeMillis();
    Object localObject1 = CLIENT_CONFIG;
    Object localObject2 = SERVICE_CONFIG;
    File localFile1 = new File(getLogFilePath(), "report.log");
    File[] arrayOfFile2;
    File[] arrayOfFile1;
    if (localFile1.exists())
    {
      localFile1.delete();
      File localFile2 = ((cloudwns.b.c)localObject1).a(l);
      arrayOfFile2 = ((cloudwns.b.c)localObject1).b(localFile2);
      arrayOfFile1 = arrayOfFile2;
      if (arrayOfFile2 != null)
        arrayOfFile1 = ((cloudwns.b.c)localObject1).a(arrayOfFile2);
      arrayOfFile2 = ((cloudwns.b.c)localObject2).b(localFile2);
      if (arrayOfFile2 == null)
        break label486;
      arrayOfFile2 = ((cloudwns.b.c)localObject1).a(arrayOfFile2);
    }
    label164: label169: label479: label486: 
    while (true)
    {
      float f1;
      label125: float f2;
      if (arrayOfFile1 != null)
      {
        f1 = arrayOfFile1.length;
        if (arrayOfFile2 == null)
          break label164;
        f2 = arrayOfFile2.length;
      }
      while (true)
      {
        while (true)
        {
          if (f1 + f2 > 0.0F)
            break label169;
          return localFile1;
          try
          {
            localFile1.createNewFile();
          }
          catch (IOException localIOException)
          {
            return null;
          }
        }
        f1 = 0.0F;
        break label125;
        f2 = 0.0F;
      }
      float f3 = f1 + f2;
      int k = Math.round(f1 / f3 * 24.0F);
      int i = Math.round(f2 / f3 * 24.0F);
      if ((k == 0) && (localIOException != null) && (localIOException.length > 0))
        i -= 1;
      while (true)
      {
        localObject1 = new ArrayList();
        localObject2 = new ArrayList();
        if (localIOException != null)
          while (true)
            if (j > 0)
            {
              k = j - 1;
              j = k;
              if (((List)localObject1).size() >= localIOException.length)
                continue;
              ((List)localObject1).add(0, localIOException[(localIOException.length - localObject1.size() - 1)]);
              j = k;
              continue;
              if ((i != 0) || (arrayOfFile2 == null) || (arrayOfFile2.length <= 0))
                break label479;
              j = k - 1;
              i = 1;
              break;
            }
        if (arrayOfFile2 != null)
          while (i > 0)
          {
            j = i - 1;
            i = j;
            if (((List)localObject2).size() >= arrayOfFile2.length)
              continue;
            ((List)localObject2).add(0, arrayOfFile2[(arrayOfFile2.length - localObject2.size() - 1)]);
            i = j;
          }
        a((List)localObject1, localFile1, "------app log. block count:" + ((List)localObject1).size() + "------\n");
        a((List)localObject2, localFile1, "\n------wns log. block count:" + ((List)localObject2).size() + "------\n");
        return localFile1;
        j = k;
      }
    }
  }

  public static File prepareReportLogFileBySize(long paramLong, int paramInt)
  {
    if (paramInt < 0)
      return prepareReportLogFile(paramLong);
    long l = paramLong;
    if (paramLong < 1L)
      l = System.currentTimeMillis();
    cloudwns.b.c localc1 = CLIENT_CONFIG;
    cloudwns.b.c localc2 = SERVICE_CONFIG;
    File localFile1 = new File(getLogFilePath(), "report.log");
    if (localFile1.exists())
      localFile1.delete();
    int i;
    ArrayList localArrayList1;
    ArrayList localArrayList2;
    ArrayList localArrayList3;
    ArrayList localArrayList4;
    int j;
    int n;
    while (true)
    {
      i = 0;
      localArrayList1 = new ArrayList();
      localArrayList2 = new ArrayList();
      localArrayList3 = new ArrayList();
      localArrayList4 = new ArrayList();
      j = 0;
      if (i < paramInt)
      {
        n = j + 1;
        if ((j < 7) && ((localc1.b(l)) || (localc2.b(l))))
          break;
      }
      else
      {
        if ((localArrayList1.size() != 0) || (localArrayList2.size() != 0))
          break label620;
        return null;
        try
        {
          localFile1.createNewFile();
        }
        catch (IOException localIOException)
        {
          return null;
        }
      }
    }
    localArrayList3.clear();
    localArrayList4.clear();
    File localFile2 = localc1.a(l);
    File[] arrayOfFile = localc1.b(localFile2);
    Object localObject = arrayOfFile;
    if (arrayOfFile != null)
      localObject = localc1.a(arrayOfFile);
    arrayOfFile = localc2.b(localFile2);
    if (arrayOfFile != null)
      arrayOfFile = localc1.a(arrayOfFile);
    while (true)
    {
      float f1;
      label261: float f2;
      if (localObject != null)
      {
        f1 = localObject.length;
        if (arrayOfFile == null)
          break label293;
        f2 = arrayOfFile.length;
      }
      while (true)
      {
        if (f1 + f2 > 0.0F)
          break label299;
        j = n;
        break;
        f1 = 0.0F;
        break label261;
        label293: f2 = 0.0F;
      }
      label299: float f3 = f1 + f2;
      int m = Math.round(f1 / f3 * 24.0F);
      int i1 = Math.round(f2 / f3 * 24.0F);
      int k;
      if ((m == 0) && (localObject != null) && (localObject.length > 0))
      {
        k = 1;
        j = i1 - 1;
      }
      while (true)
      {
        m = i;
        if (localObject == null)
          break;
        while (true)
        {
          m = i;
          if (k <= 0)
            break;
          m = k - 1;
          k = m;
          if (localArrayList3.size() >= localObject.length)
            continue;
          localFile2 = localObject[(localObject.length - localArrayList3.size() - 1)];
          localArrayList3.add(0, localFile2);
          i = (int)(i + localFile2.length());
          k = m;
        }
        j = i1;
        k = m;
        if (i1 != 0)
          continue;
        j = i1;
        k = m;
        if (arrayOfFile == null)
          continue;
        j = i1;
        k = m;
        if (arrayOfFile.length <= 0)
          continue;
        j = 1;
        k = m - 1;
      }
      i = m;
      if (arrayOfFile != null)
        while (true)
        {
          i = m;
          if (j <= 0)
            break;
          i = j - 1;
          j = i;
          if (localArrayList4.size() >= arrayOfFile.length)
            continue;
          localObject = arrayOfFile[(arrayOfFile.length - localArrayList4.size() - 1)];
          localArrayList4.add(0, localObject);
          m = (int)(m + ((File)localObject).length());
          j = i;
        }
      l -= 86400000L;
      localArrayList1.addAll(localArrayList3);
      localArrayList2.addAll(localArrayList4);
      j = n;
      break;
      label620: a(localArrayList1, localFile1, "------app log. block count:" + localArrayList1.size() + "------\n");
      a(localArrayList2, localFile1, "\n------wns log. block count:" + localArrayList2.size() + "------\n");
      return localFile1;
    }
  }

  public static File prepareReportLogFileByTime(long paramLong1, long paramLong2)
  {
    long l3 = paramLong2;
    if (paramLong2 <= 0L)
      l3 = 86400000L;
    if (paramLong1 < 1L)
      paramLong2 = System.currentTimeMillis();
    while (true)
    {
      long l6 = paramLong1 - l3;
      Log.d("WnsTracer", "准备日志合并，时间点A [" + printTimeStr(paramLong1) + "] 时间点B [" + printTimeStr(l6) + "] 时间差[" + (float)l3 * 1.0F / 3600000.0F + "小时]");
      cloudwns.b.c localc1 = CLIENT_CONFIG;
      cloudwns.b.c localc2 = SERVICE_CONFIG;
      File localFile1 = new File(getLogFilePath(), "report.log");
      ArrayList localArrayList1;
      ArrayList localArrayList2;
      ArrayList localArrayList3;
      ArrayList localArrayList4;
      long l4;
      long l2;
      int k;
      int j;
      int i;
      long l1;
      if (localFile1.exists())
      {
        localFile1.delete();
        localArrayList1 = new ArrayList();
        localArrayList2 = new ArrayList();
        localArrayList3 = new ArrayList();
        localArrayList4 = new ArrayList();
        l4 = paramLong1;
        l2 = paramLong1;
        k = 0;
        j = 0;
        i = 0;
        l1 = paramLong2;
        paramLong2 = l4;
      }
      while (true)
      {
        int n;
        while (true)
        {
          if ((j == 0) || (i == 0))
          {
            n = k + 1;
            if (k < 7)
            {
              if ((localc1.b(l1)) || (localc2.b(l1)))
                break;
              l1 -= 86400000L;
              if (l1 >= l6 - 86400000L)
                break label1144;
            }
          }
          if ((localArrayList1.size() != 0) || (localArrayList2.size() != 0))
            break label985;
          a(localFile1, "日志为空，时间点A [" + printTimeStr(l6) + "] 时间点B [" + printTimeStr(paramLong1) + "] 时间差[" + (float)l3 * 1.0F / 3600000.0F + "小时]\n");
          Log.d("WnsTracer", "写入日志为空的提示信息");
          return localFile1;
          try
          {
            localFile1.createNewFile();
          }
          catch (IOException localIOException)
          {
            return null;
          }
        }
        localArrayList3.clear();
        localArrayList4.clear();
        File localFile2 = localc1.a(l1);
        File[] arrayOfFile = localc1.b(localFile2);
        Object localObject = arrayOfFile;
        if (arrayOfFile != null)
          localObject = localc1.a(arrayOfFile);
        arrayOfFile = localc2.b(localFile2);
        if (arrayOfFile != null)
          arrayOfFile = localc1.a(arrayOfFile);
        while (true)
        {
          int i1;
          if (localObject != null)
          {
            i1 = localObject.length;
            label453: if (arrayOfFile == null)
              break label484;
          }
          label484: for (int i2 = arrayOfFile.length; ; i2 = 0)
          {
            if (i1 + i2 > 0)
              break label490;
            k = n;
            break;
            i1 = 0;
            break label453;
          }
          label490: float f = i1 + i2;
          int m;
          if ((i1 == 0) && (localObject != null) && (localObject.length > 0))
          {
            m = 1;
            k = i2 - 1;
          }
          long l5;
          while (true)
          {
            i1 = j;
            l4 = l2;
            if (localObject == null)
              break;
            while (true)
            {
              i1 = j;
              l4 = l2;
              if (m <= 0)
                break;
              i1 = j;
              l4 = l2;
              if (j != 0)
                break;
              i1 = m - 1;
              m = i1;
              if (localArrayList3.size() >= localObject.length)
                continue;
              localFile2 = localObject[(localObject.length - localArrayList3.size() - 1)];
              l5 = readLogFileTime(localFile2);
              l4 = l2;
              if (l2 > l5)
                l4 = l5;
              if (l4 < l6)
                j = 1;
              Log.d("WnsTracer", "添加了日志文件<" + localFile2 + ">, 时间[" + printTimeStr(l5) + "]");
              localArrayList3.add(localFile2);
              m = i1;
              l2 = l4;
            }
            k = i2;
            m = i1;
            if (i2 != 0)
              continue;
            k = i2;
            m = i1;
            if (arrayOfFile == null)
              continue;
            k = i2;
            m = i1;
            if (arrayOfFile.length <= 0)
              continue;
            k = 1;
            m = i1 - 1;
          }
          if (arrayOfFile != null)
            while (true)
            {
              j = i;
              l2 = paramLong2;
              if (k <= 0)
                break;
              j = i;
              l2 = paramLong2;
              if (i != 0)
                break;
              j = k - 1;
              k = j;
              if (localArrayList4.size() >= arrayOfFile.length)
                continue;
              localObject = arrayOfFile[(arrayOfFile.length - localArrayList4.size() - 1)];
              l5 = readLogFileTime((File)localObject);
              l2 = paramLong2;
              if (paramLong2 > l5)
                l2 = l5;
              if (l2 < l6)
                i = 1;
              Log.d("WnsTracer", "添加了日志文件<" + localObject + ">, 时间[" + printTimeStr(l5) + "]");
              localArrayList4.add(localObject);
              k = j;
              paramLong2 = l2;
            }
          l2 = paramLong2;
          j = i;
          localArrayList1.addAll(localArrayList3);
          localArrayList2.addAll(localArrayList4);
          l1 -= 86400000L;
          paramLong2 = l2;
          i = j;
          k = n;
          j = i1;
          l2 = l4;
          break;
          label985: Log.d("WnsTracer", "全部添加完毕，APP日志最后时间[" + printTimeStr(l2) + "], WNS日志最后时间[" + printTimeStr(paramLong2) + "]");
          localObject = new d();
          Collections.sort(localArrayList1, (Comparator)localObject);
          Collections.sort(localArrayList2, (Comparator)localObject);
          a(localArrayList1, localFile1, "------app log. block count:" + localArrayList1.size() + "------\n");
          a(localArrayList2, localFile1, "\n------wns log. block count:" + localArrayList2.size() + "------\n");
          return localFile1;
        }
        label1144: k = n;
      }
      paramLong2 = paramLong1;
    }
  }

  public static String printTimeStr(long paramLong)
  {
    try
    {
      String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(paramLong));
      return str;
    }
    catch (Exception localException)
    {
    }
    return String.valueOf(paramLong);
  }

  // ERROR //
  public static long readLogFileTime(File paramFile)
  {
    // Byte code:
    //   0: new 202	java/io/BufferedReader
    //   3: dup
    //   4: new 204	java/io/FileReader
    //   7: dup
    //   8: aload_0
    //   9: invokespecial 205	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   12: invokespecial 208	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   15: astore_2
    //   16: aload_2
    //   17: astore_1
    //   18: aload_2
    //   19: invokevirtual 388	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   22: invokevirtual 391	java/lang/String:trim	()Ljava/lang/String;
    //   25: iconst_2
    //   26: ldc_w 370
    //   29: invokevirtual 393	java/lang/String:length	()I
    //   32: iconst_2
    //   33: iadd
    //   34: invokevirtual 397	java/lang/String:substring	(II)Ljava/lang/String;
    //   37: astore_3
    //   38: aload_2
    //   39: astore_1
    //   40: new 368	java/text/SimpleDateFormat
    //   43: dup
    //   44: ldc_w 370
    //   47: invokespecial 373	java/text/SimpleDateFormat:<init>	(Ljava/lang/String;)V
    //   50: aload_3
    //   51: invokevirtual 401	java/text/SimpleDateFormat:parse	(Ljava/lang/String;)Ljava/util/Date;
    //   54: invokevirtual 404	java/util/Date:getTime	()J
    //   57: lstore 4
    //   59: aload_2
    //   60: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   63: pop
    //   64: lload 4
    //   66: lreturn
    //   67: astore_3
    //   68: aconst_null
    //   69: astore_2
    //   70: aload_2
    //   71: astore_1
    //   72: ldc_w 307
    //   75: new 210	java/lang/StringBuilder
    //   78: dup
    //   79: invokespecial 211	java/lang/StringBuilder:<init>	()V
    //   82: ldc_w 406
    //   85: invokevirtual 220	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: aload_0
    //   89: invokevirtual 345	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   92: ldc_w 408
    //   95: invokevirtual 220	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: invokevirtual 230	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   101: aload_3
    //   102: invokestatic 412	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   105: pop
    //   106: aload_2
    //   107: astore_1
    //   108: invokestatic 169	java/lang/System:currentTimeMillis	()J
    //   111: lstore 4
    //   113: aload_2
    //   114: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   117: pop
    //   118: lload 4
    //   120: lreturn
    //   121: astore_0
    //   122: aconst_null
    //   123: astore_1
    //   124: aload_1
    //   125: invokestatic 128	com/tencent/base/util/a:a	(Ljava/lang/Object;)Z
    //   128: pop
    //   129: aload_0
    //   130: athrow
    //   131: astore_0
    //   132: goto -8 -> 124
    //   135: astore_3
    //   136: goto -66 -> 70
    //
    // Exception table:
    //   from	to	target	type
    //   0	16	67	java/lang/Exception
    //   0	16	121	finally
    //   18	38	131	finally
    //   40	59	131	finally
    //   72	106	131	finally
    //   108	113	131	finally
    //   18	38	135	java/lang/Exception
    //   40	59	135	java/lang/Exception
  }

  public static void setFileTraceLevel(int paramInt)
  {
    int i;
    if (paramInt <= 63)
    {
      i = paramInt;
      if (paramInt >= 0);
    }
    else
    {
      i = 63;
    }
    Option.putInt("debug.file.tracelevel", i).commit();
  }

  public static void setInstance(c paramc)
  {
    a = paramc;
  }

  public static void setMaxFolderSize(long paramLong)
  {
    int j = (int)(paramLong / 262144L);
    int i = j;
    if (j < 1)
      i = 24;
    Option.putInt("debug.file.blockcount", i).commit();
  }

  public static void setMaxKeepPeriod(long paramLong)
  {
    long l = paramLong;
    if (paramLong < 86400000L)
      l = 604800000L;
    Option.putLong("debug.file.keepperiod", l).commit();
  }

  public void flush()
  {
    if (this.fileTracer != null)
      this.fileTracer.a();
  }

  public final boolean isEnabled()
  {
    return this.b;
  }

  public final boolean isFileTracerEnabled()
  {
    return this.c;
  }

  public final boolean isLogcatTracerEnabled()
  {
    return this.d;
  }

  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    if (("debug.file.tracelevel".equals(paramString)) || (paramString == null))
    {
      int i = Option.getInt("debug.file.tracelevel", 63);
      trace(16, "WnsTracer", "File Trace Level Changed = " + i, null);
      this.fileTracer.a(i);
    }
  }

  public final void setEnabled(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }

  public final void setFileTracerEnabled(boolean paramBoolean)
  {
    this.fileTracer.a();
    this.c = paramBoolean;
  }

  public final void setFileTracerLevel(int paramInt)
  {
    this.fileTracer.a(paramInt);
  }

  public final void setLogcatTracerEnabled(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }

  public void stop()
  {
    if (this.fileTracer != null)
    {
      this.fileTracer.a();
      this.fileTracer.b();
    }
  }

  public void trace(int paramInt, String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (isEnabled())
    {
      if ((isFileTracerEnabled()) && (this.fileTracer != null))
        this.fileTracer.b(paramInt, Thread.currentThread(), System.currentTimeMillis(), paramString1, paramString2, paramThrowable);
      if (isLogcatTracerEnabled())
        h.a.b(paramInt, Thread.currentThread(), System.currentTimeMillis(), paramString1, paramString2, paramThrowable);
      a(paramInt, paramString1, paramString2, paramThrowable);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     cloudwns.l.c
 * JD-Core Version:    0.6.0
 */