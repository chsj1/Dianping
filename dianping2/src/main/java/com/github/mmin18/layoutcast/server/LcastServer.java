package com.github.mmin18.layoutcast.server;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import com.github.mmin18.layoutcast.LayoutCast;
import com.github.mmin18.layoutcast.context.OverrideContext;
import com.github.mmin18.layoutcast.util.EmbedHttpServer;
import com.github.mmin18.layoutcast.util.EmbedHttpServer.ResponseOutputStream;
import com.github.mmin18.layoutcast.util.ResUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.json.JSONObject;

public class LcastServer extends EmbedHttpServer
{
  public static final int PORT_FROM = 41128;
  public static Application app;
  private static LcastServer runningServer;
  final Context context;
  File latestPushFile;

  private LcastServer(Context paramContext, int paramInt)
  {
    super(paramInt);
    this.context = paramContext;
  }

  private static String byteArrayToHex(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramArrayOfByte.length * 2);
    int j = paramArrayOfByte.length;
    int i = 0;
    while (i < j)
    {
      localStringBuilder.append(String.format("%02x", new Object[] { Integer.valueOf(paramArrayOfByte[i] & 0xFF) }));
      i += 1;
    }
    return localStringBuilder.toString();
  }

  public static void cleanCache(Context paramContext)
  {
    paramContext = new File(paramContext.getCacheDir(), "lcast").listFiles();
    if (paramContext != null)
    {
      int j = paramContext.length;
      int i = 0;
      while (i < j)
      {
        rm(paramContext[i]);
        i += 1;
      }
    }
  }

  private static void rm(File paramFile)
  {
    if (paramFile.isDirectory())
    {
      File[] arrayOfFile = paramFile.listFiles();
      int j = arrayOfFile.length;
      int i = 0;
      while (i < j)
      {
        rm(arrayOfFile[i]);
        i += 1;
      }
      paramFile.delete();
    }
    do
      return;
    while (!paramFile.getName().endsWith(".apk"));
    paramFile.delete();
  }

  public static void start(Context paramContext)
  {
    if (runningServer != null)
      Log.d("lcast", "lcast server is already running");
    while (true)
    {
      return;
      int i = 0;
      while (i < 100)
      {
        LcastServer localLcastServer = new LcastServer(paramContext, 41128 + i);
        try
        {
          localLcastServer.start();
          runningServer = localLcastServer;
          Log.d("lcast", "lcast server running on port " + (41128 + i));
          return;
        }
        catch (Exception localException)
        {
          i += 1;
        }
      }
    }
  }

  protected void handle(String paramString1, String paramString2, HashMap<String, String> paramHashMap, InputStream paramInputStream, EmbedHttpServer.ResponseOutputStream paramResponseOutputStream)
    throws Exception
  {
    if (paramString2.equalsIgnoreCase("/packagename"))
    {
      paramResponseOutputStream.setContentTypeText();
      paramResponseOutputStream.write(this.context.getPackageName().getBytes("utf-8"));
      return;
    }
    if (paramString2.equalsIgnoreCase("/appstate"))
    {
      paramResponseOutputStream.setContentTypeText();
      paramResponseOutputStream.write(String.valueOf(OverrideContext.getApplicationState()).getBytes("utf-8"));
      return;
    }
    if ("/vmversion".equalsIgnoreCase(paramString2))
    {
      paramString1 = System.getProperty("java.vm.version");
      paramResponseOutputStream.setContentTypeText();
      if (paramString1 == null)
      {
        paramResponseOutputStream.write(48);
        return;
      }
      paramResponseOutputStream.write(paramString1.getBytes("utf-8"));
      return;
    }
    if ("/launcher".equalsIgnoreCase(paramString2))
    {
      paramString1 = app.getPackageManager();
      paramString2 = new Intent("android.intent.action.MAIN");
      paramString2.addCategory("android.intent.category.LAUNCHER");
      paramString2.setPackage(app.getPackageName());
      paramString1 = paramString1.resolveActivity(paramString2, 0);
      new Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER");
      paramResponseOutputStream.setContentTypeText();
      paramResponseOutputStream.write(paramString1.activityInfo.name.getBytes("utf-8"));
      return;
    }
    int i;
    if ((("post".equalsIgnoreCase(paramString1)) || ("put".equalsIgnoreCase(paramString1))) && (paramString2.equalsIgnoreCase("/pushres")))
    {
      paramString1 = new File(this.context.getCacheDir(), "lcast");
      paramString1.mkdir();
      if (new File(paramString1, "dex.ped").length() > 0L);
      for (paramString1 = new File(paramString1, "res.ped"); ; paramString1 = new File(paramString1, Integer.toHexString((int)(System.currentTimeMillis() / 100L) & 0xFFF) + ".apk"))
      {
        paramString2 = new FileOutputStream(paramString1);
        paramHashMap = new byte[4096];
        while (true)
        {
          i = paramInputStream.read(paramHashMap);
          if (i == -1)
            break;
          paramString2.write(paramHashMap, 0, i);
        }
      }
      paramString2.close();
      this.latestPushFile = paramString1;
      paramResponseOutputStream.setStatusCode(201);
      Log.d("lcast", "lcast resources file received (" + paramString1.length() + " bytes): " + paramString1);
      return;
    }
    if ((("post".equalsIgnoreCase(paramString1)) || ("put".equalsIgnoreCase(paramString1))) && (paramString2.equalsIgnoreCase("/pushdex")))
    {
      paramString1 = new File(this.context.getCacheDir(), "lcast");
      paramString1.mkdir();
      paramString1 = new File(paramString1, "dex.ped");
      paramString2 = new FileOutputStream(paramString1);
      paramHashMap = new byte[4096];
      while (true)
      {
        i = paramInputStream.read(paramHashMap);
        if (i == -1)
          break;
        paramString2.write(paramHashMap, 0, i);
      }
      paramString2.close();
      paramResponseOutputStream.setStatusCode(201);
      Log.d("lcast", "lcast dex file received (" + paramString1.length() + " bytes)");
      return;
    }
    if ("/pcast".equalsIgnoreCase(paramString2))
    {
      LayoutCast.restart(false);
      paramResponseOutputStream.setStatusCode(200);
      return;
    }
    if ("/lcast".equalsIgnoreCase(paramString2))
    {
      paramString1 = new File(this.context.getCacheDir(), "lcast");
      if (new File(paramString1, "dex.ped").length() > 0L)
      {
        if (this.latestPushFile != null)
        {
          paramString1 = new File(paramString1, "res.ped");
          this.latestPushFile.renameTo(paramString1);
        }
        Log.i("lcast", "cast with dex changes, need to restart the process (activity stack will be reserved)");
        if (LayoutCast.restart(true));
        for (i = 200; ; i = 500)
        {
          paramResponseOutputStream.setStatusCode(i);
          return;
        }
      }
      OverrideContext.setGlobalResources(ResUtils.getResources(app, this.latestPushFile));
      paramResponseOutputStream.setStatusCode(200);
      paramResponseOutputStream.write(String.valueOf(this.latestPushFile).getBytes("utf-8"));
      Log.i("lcast", "cast with only res changes, just recreate the running activity.");
      return;
    }
    if ("/reset".equalsIgnoreCase(paramString2))
    {
      OverrideContext.setGlobalResources(null);
      paramResponseOutputStream.setStatusCode(200);
      paramResponseOutputStream.write("OK".getBytes("utf-8"));
      return;
    }
    if ("/ids.xml".equalsIgnoreCase(paramString2))
    {
      paramString1 = app.getPackageName() + ".R";
      paramString1 = app.getClassLoader().loadClass(paramString1);
      paramString1 = new IdProfileBuilder(this.context.getResources()).buildIds(paramString1);
      paramResponseOutputStream.setStatusCode(200);
      paramResponseOutputStream.setContentTypeText();
      paramResponseOutputStream.write(paramString1.getBytes("utf-8"));
      return;
    }
    if ("/public.xml".equalsIgnoreCase(paramString2))
    {
      paramString1 = app.getPackageName() + ".R";
      paramString1 = app.getClassLoader().loadClass(paramString1);
      paramString1 = new IdProfileBuilder(this.context.getResources()).buildPublic(paramString1);
      paramResponseOutputStream.setStatusCode(200);
      paramResponseOutputStream.setContentTypeText();
      paramResponseOutputStream.write(paramString1.getBytes("utf-8"));
      return;
    }
    if ("/apkinfo".equalsIgnoreCase(paramString2))
    {
      paramString2 = new File(app.getApplicationInfo().sourceDir);
      paramString1 = new JSONObject();
      paramString1.put("size", paramString2.length());
      paramString1.put("lastModified", paramString2.lastModified());
      paramString2 = new FileInputStream(paramString2);
      paramHashMap = MessageDigest.getInstance("MD5");
      paramInputStream = new byte[4096];
      while (true)
      {
        i = paramString2.read(paramInputStream);
        if (i == -1)
          break;
        paramHashMap.update(paramInputStream, 0, i);
      }
      paramString2.close();
      paramString1.put("md5", byteArrayToHex(paramHashMap.digest()));
      paramResponseOutputStream.setStatusCode(200);
      paramResponseOutputStream.setContentTypeJson();
      paramResponseOutputStream.write(paramString1.toString().getBytes("utf-8"));
      return;
    }
    if ("/apkraw".equalsIgnoreCase(paramString2))
    {
      paramString1 = new FileInputStream(app.getApplicationInfo().sourceDir);
      paramResponseOutputStream.setStatusCode(200);
      paramResponseOutputStream.setContentTypeBinary();
      paramString2 = new byte[4096];
      while (true)
      {
        i = paramString1.read(paramString2);
        if (i == -1)
          break;
        paramResponseOutputStream.write(paramString2, 0, i);
      }
    }
    if (paramString2.startsWith("/fileinfo/"))
    {
      paramString1 = new JarFile(new File(app.getApplicationInfo().sourceDir));
      paramString2 = paramString1.getJarEntry(paramString2.substring("/fileinfo/".length()));
      paramInputStream = paramString1.getInputStream(paramString2);
      paramHashMap = MessageDigest.getInstance("MD5");
      byte[] arrayOfByte = new byte[4096];
      i = 0;
      while (true)
      {
        int j = paramInputStream.read(arrayOfByte);
        if (j == -1)
          break;
        paramHashMap.update(arrayOfByte, 0, j);
        i += j;
      }
      paramInputStream.close();
      paramString1.close();
      paramString1 = new JSONObject();
      paramString1.put("size", i);
      paramString1.put("time", paramString2.getTime());
      paramString1.put("crc", paramString2.getCrc());
      paramString1.put("md5", byteArrayToHex(paramHashMap.digest()));
      paramResponseOutputStream.setStatusCode(200);
      paramResponseOutputStream.setContentTypeJson();
      paramResponseOutputStream.write(paramString1.toString().getBytes("utf-8"));
      return;
    }
    if (paramString2.startsWith("/fileraw/"))
    {
      paramString1 = new JarFile(new File(app.getApplicationInfo().sourceDir));
      paramString1 = paramString1.getInputStream(paramString1.getJarEntry(paramString2.substring("/fileraw/".length())));
      paramResponseOutputStream.setStatusCode(200);
      paramResponseOutputStream.setContentTypeBinary();
      paramString2 = new byte[4096];
      while (true)
      {
        i = paramString1.read(paramString2);
        if (i == -1)
          break;
        paramResponseOutputStream.write(paramString2, 0, i);
      }
    }
    super.handle(paramString1, paramString2, paramHashMap, paramInputStream, paramResponseOutputStream);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.github.mmin18.layoutcast.server.LcastServer
 * JD-Core Version:    0.6.0
 */