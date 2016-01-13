package com.tencent.open.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsynLoadImg
{
  private static String c;
  private String a;
  private AsynLoadImgBack b;
  private long d;
  private Handler e;
  private Runnable f = new Runnable()
  {
    public void run()
    {
      Log.v("AsynLoadImg", "saveFileRunnable:");
      String str1 = Util.encrypt(AsynLoadImg.b(AsynLoadImg.this));
      str1 = "share_qq_" + str1 + ".jpg";
      String str2 = AsynLoadImg.a() + str1;
      Object localObject = new File(str2);
      Message localMessage = AsynLoadImg.c(AsynLoadImg.this).obtainMessage();
      if (((File)localObject).exists())
      {
        localMessage.arg1 = 0;
        localMessage.obj = str2;
        Log.v("AsynLoadImg", "file exists: time:" + (System.currentTimeMillis() - AsynLoadImg.d(AsynLoadImg.this)));
        AsynLoadImg.c(AsynLoadImg.this).sendMessage(localMessage);
        return;
      }
      localObject = AsynLoadImg.getbitmap(AsynLoadImg.b(AsynLoadImg.this));
      boolean bool;
      if (localObject != null)
      {
        bool = AsynLoadImg.this.saveFile((Bitmap)localObject, str1);
        label181: if (!bool)
          break label248;
        localMessage.arg1 = 0;
        localMessage.obj = str2;
      }
      while (true)
      {
        Log.v("AsynLoadImg", "file not exists: download time:" + (System.currentTimeMillis() - AsynLoadImg.d(AsynLoadImg.this)));
        break;
        Log.v("AsynLoadImg", "saveFileRunnable:get bmp fail---");
        bool = false;
        break label181;
        label248: localMessage.arg1 = 1;
      }
    }
  };

  public AsynLoadImg(Activity paramActivity)
  {
    this.e = new Handler(paramActivity.getMainLooper())
    {
      public void handleMessage(Message paramMessage)
      {
        Log.v("AsynLoadImg", "handleMessage:" + paramMessage.arg1);
        if (paramMessage.arg1 == 0)
        {
          AsynLoadImg.a(AsynLoadImg.this).saved(paramMessage.arg1, (String)paramMessage.obj);
          return;
        }
        AsynLoadImg.a(AsynLoadImg.this).saved(paramMessage.arg1, null);
      }
    };
  }

  public static Bitmap getbitmap(String paramString)
  {
    Log.v("AsynLoadImg", "getbitmap:" + paramString);
    try
    {
      Object localObject = (HttpURLConnection)new URL(paramString).openConnection();
      ((HttpURLConnection)localObject).setDoInput(true);
      ((HttpURLConnection)localObject).connect();
      localObject = ((HttpURLConnection)localObject).getInputStream();
      Bitmap localBitmap = BitmapFactory.decodeStream((InputStream)localObject);
      ((InputStream)localObject).close();
      Log.v("AsynLoadImg", "image download finished." + paramString);
      return localBitmap;
    }
    catch (IOException paramString)
    {
      paramString.printStackTrace();
      Log.v("AsynLoadImg", "getbitmap bmp fail---");
    }
    return (Bitmap)null;
  }

  public void save(String paramString, AsynLoadImgBack paramAsynLoadImgBack)
  {
    Log.v("AsynLoadImg", "--save---");
    if ((paramString == null) || (paramString.equals("")))
    {
      paramAsynLoadImgBack.saved(1, null);
      return;
    }
    if (!Util.hasSDCard())
    {
      paramAsynLoadImgBack.saved(2, null);
      return;
    }
    c = Environment.getExternalStorageDirectory() + "/tmp/";
    this.d = System.currentTimeMillis();
    this.a = paramString;
    this.b = paramAsynLoadImgBack;
    new Thread(this.f).start();
  }

  public boolean saveFile(Bitmap paramBitmap, String paramString)
  {
    String str = c;
    try
    {
      File localFile = new File(str);
      if (!localFile.exists())
        localFile.mkdir();
      str = str + paramString;
      Log.v("AsynLoadImg", "saveFile:" + paramString);
      paramString = new BufferedOutputStream(new FileOutputStream(new File(str)));
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, 80, paramString);
      paramString.flush();
      paramString.close();
      return true;
    }
    catch (IOException paramBitmap)
    {
      paramBitmap.printStackTrace();
      Log.v("AsynLoadImg", "saveFile bmp fail---");
    }
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.open.utils.AsynLoadImg
 * JD-Core Version:    0.6.0
 */