package com.github.mmin18.layoutcast;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.github.mmin18.layoutcast.context.OverrideContext;
import com.github.mmin18.layoutcast.inflater.BootInflater;
import com.github.mmin18.layoutcast.server.LcastServer;
import com.github.mmin18.layoutcast.util.ArtUtils;
import com.github.mmin18.layoutcast.util.ResUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LayoutCast
{
  private static Context appContext;
  private static boolean inited;

  public static void init(Context paramContext)
  {
    if (inited)
      return;
    Application localApplication;
    if ((paramContext instanceof Application))
      localApplication = (Application)paramContext;
    while (true)
    {
      appContext = localApplication;
      LcastServer.cleanCache(localApplication);
      File localFile2 = new File(localApplication.getCacheDir(), "lcast");
      File localFile3 = new File(localFile2, "dex.ped");
      File localFile1 = new File(localFile2, "res.ped");
      Object localObject = System.getProperty("java.vm.version");
      pathch(localFile2, paramContext, localApplication, "Hack.apk");
      if (localFile3.length() > 0L)
      {
        paramContext = new File(localFile2, "dex.apk");
        localFile3.renameTo(paramContext);
        localFile3 = new File(localFile2, "opt");
        localFile3.mkdirs();
        if ((localObject != null) && (((String)localObject).startsWith("2")))
          ArtUtils.overrideClassLoader(localApplication.getClassLoader(), paramContext, localFile3);
      }
      else
      {
        OverrideContext.initApplication(localApplication);
        BootInflater.initApplication(localApplication);
        if (localFile1.length() <= 0L);
      }
      try
      {
        paramContext = new File(localFile2, "res.apk");
        localFile1.renameTo(paramContext);
        OverrideContext.setGlobalResources(ResUtils.getResources(localApplication, paramContext));
        LcastServer.app = localApplication;
        LcastServer.start(localApplication);
        inited = true;
        return;
        localApplication = (Application)paramContext.getApplicationContext();
        continue;
        localObject = new File(localFile2, "sam.dex");
        paramContext.renameTo((File)localObject);
        ArtUtils.overrideClassLoader(localApplication.getClassLoader(), (File)localObject, localFile3);
      }
      catch (Exception paramContext)
      {
        while (true)
          Log.e("lcast", "fail to cast " + localFile1, paramContext);
      }
    }
  }

  public static boolean pathch(File paramFile, Context paramContext, Application paramApplication, String paramString)
  {
    File localFile = new File(paramFile, paramString);
    try
    {
      if (!localFile.exists())
      {
        if (!localFile.getParentFile().exists())
          localFile.getParentFile().mkdir();
        localFile.createNewFile();
      }
      localFileOutputStream = new FileOutputStream(localFile);
      paramContext = paramContext.getAssets().open(paramString);
      paramString = new byte[1024];
      while (true)
      {
        int i = paramContext.read(paramString);
        if (i == -1)
          break;
        localFileOutputStream.write(paramString, 0, i);
      }
    }
    catch (FileNotFoundException paramFile)
    {
      FileOutputStream localFileOutputStream;
      paramFile.printStackTrace();
      return false;
      paramContext.close();
      localFileOutputStream.close();
      paramFile = new File(paramFile, "opt");
      paramFile.mkdirs();
      ArtUtils.overrideClassLoader(paramApplication.getClassLoader(), localFile, paramFile);
      return true;
    }
    catch (IOException paramFile)
    {
      paramFile.printStackTrace();
    }
    return false;
  }

  public static boolean restart(boolean paramBoolean)
  {
    Object localObject = OverrideContext.getTopActivity();
    if ((localObject instanceof ResetActivity))
    {
      ((ResetActivity)localObject).reset();
      return true;
    }
    localObject = appContext;
    try
    {
      Intent localIntent = new Intent((Context)localObject, ResetActivity.class);
      localIntent.setFlags(268435456);
      localIntent.putExtra("reset", paramBoolean);
      ((Context)localObject).startActivity(localIntent);
      return true;
    }
    catch (Exception str)
    {
      String str = "Fail to cast dex, make sure you have <Activity android:name=\"" + ResetActivity.class.getName() + "\"/> registered in AndroidManifest.xml";
      Log.e("lcast", str);
      new Handler(Looper.getMainLooper()).post(new Runnable(str)
      {
        public void run()
        {
          Toast.makeText(LayoutCast.appContext, this.val$str, 1).show();
        }
      });
    }
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.github.mmin18.layoutcast.LayoutCast
 * JD-Core Version:    0.6.0
 */