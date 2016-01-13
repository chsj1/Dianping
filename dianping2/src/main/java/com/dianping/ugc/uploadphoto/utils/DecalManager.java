package com.dianping.ugc.uploadphoto.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import com.dianping.cache.DPCache;
import com.dianping.util.Log;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DecalManager
{
  private static final String TAG = "DecalManager";
  private ExecutorService executor = Executors.newFixedThreadPool(8);
  private ArrayList<OnDownloadFinishListener> listeners = new ArrayList();
  private Handler mainHandler = new Handler(Looper.getMainLooper());
  private HashSet<String> runningDecals = new HashSet();

  private Runnable getDecalTask(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean)
  {
    return new DecalManager.1(this, paramString3, paramString1, paramString2, paramInt, paramBoolean);
  }

  public static DecalManager getInstance()
  {
    return DecalManager.DecalManagerInner.access$100();
  }

  private void notifyDownload(String paramString1, String paramString2, Bitmap paramBitmap, int paramInt, boolean paramBoolean)
  {
    this.mainHandler.post(new DecalManager.2(this, paramString1, paramString2, paramBitmap, paramInt, paramBoolean));
  }

  public static Bitmap resizeImage(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    float f1 = paramInt1 / i;
    float f2 = paramInt2 / j;
    if (f1 > f2);
    while (f1 > 1.0F)
    {
      return paramBitmap;
      f1 = f2;
    }
    Object localObject = new Matrix();
    ((Matrix)localObject).postScale(f1, f1);
    localObject = Bitmap.createBitmap(paramBitmap, 0, 0, i, j, (Matrix)localObject, true);
    paramBitmap.recycle();
    return (Bitmap)localObject;
  }

  public void addOnDownloadFinishListener(OnDownloadFinishListener paramOnDownloadFinishListener)
  {
    synchronized (this.listeners)
    {
      if (!this.listeners.contains(paramOnDownloadFinishListener))
        this.listeners.add(paramOnDownloadFinishListener);
      return;
    }
  }

  public void getDecal(String paramString1, String paramString2, String paramString3)
  {
    getDecal(paramString1, paramString2, paramString3, -1, true);
  }

  public void getDecal(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean)
  {
    monitorenter;
    try
    {
      if (!this.runningDecals.contains(paramString3))
      {
        this.runningDecals.add(paramString3);
        Log.d("DecalManager", "start to fetch a decal id=" + paramString1 + " url=" + paramString3);
        this.executor.execute(getDecalTask(paramString1, paramString2, paramString3, paramInt, paramBoolean));
      }
      while (true)
      {
        return;
        Log.d("DecalManager", "A task of fetching the decal id=" + paramString1 + " url=" + paramString3 + " is running.");
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramString1;
  }

  public Bitmap getDecalBitmapById(String paramString)
  {
    return DPCache.getInstance().getBitmap(paramString, "decal", 31539600000L);
  }

  public int getRunningTask()
  {
    return this.runningDecals.size();
  }

  public void removeOnDownloadFinishListener(OnDownloadFinishListener paramOnDownloadFinishListener)
  {
    synchronized (this.listeners)
    {
      this.listeners.remove(paramOnDownloadFinishListener);
      return;
    }
  }

  public static abstract interface OnDownloadFinishListener
  {
    public abstract void onDownloadFinish(String paramString1, String paramString2, Bitmap paramBitmap, int paramInt, boolean paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.uploadphoto.utils.DecalManager
 * JD-Core Version:    0.6.0
 */