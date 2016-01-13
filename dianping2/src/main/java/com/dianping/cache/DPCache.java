package com.dianping.cache;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.util.LruCache;
import com.dianping.app.DPApplication;
import com.dianping.util.DateUtil;
import com.dianping.util.FileUtils;
import com.dianping.util.Log;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DPCache
{
  private static final boolean DEBUG = true;
  private static final boolean DEBUG_CONCURRENT = false;
  private static final boolean DEBUG_MEMORY = true;
  private static final File DPCacheFolder = new File(DPApplication.instance().getCacheDir(), "cache");
  private static final File DPCachePermanentFolder = new File(DPApplication.instance().getFilesDir(), "permanent");
  private static final int MEMORY_CACHE_SIZE = 40;
  private static final String TAG = "DPCache";
  private final LruCache<String, DPCache.CacheInfo> cache0 = new LruCache(20);
  private final LruCache<String, DPCache.CacheInfo> cache1 = new LruCache(20);
  Handler handler = new DPCache.1(this, Looper.getMainLooper());
  private final HashMap<String, Object> locks = new HashMap();
  ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(5, 10, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
  private int runnings = 0;

  private DPCache()
  {
    if (!DPCacheFolder.exists())
      DPCacheFolder.mkdir();
    if (!DPCachePermanentFolder.exists())
      DPCachePermanentFolder.mkdir();
  }

  static File createCacheFile(String paramString1, String paramString2, long paramLong)
  {
    paramString2 = getCacheFolder(paramString2, paramLong);
    if (paramString2 == null)
      return null;
    return new File(paramString2, generateCacheFilename(paramString1));
  }

  private void deleteFile(File paramFile, long paramLong1, long paramLong2)
  {
    if ((paramFile == null) || (!paramFile.exists()))
      return;
    while (true)
    {
      synchronized (getLock(paramFile.getName()))
      {
        if (paramFile.isFile())
        {
          paramLong1 -= paramFile.lastModified();
          if (paramLong1 < paramLong2)
            continue;
          boolean bool = paramFile.delete();
          Log.w("DPCache", "remove file of sinceLastModified =" + paramLong1 + " result=" + bool);
          clearLock();
          return;
        }
      }
      if (!paramFile.isDirectory())
        continue;
      File[] arrayOfFile2 = paramFile.listFiles();
      File[] arrayOfFile1 = arrayOfFile2;
      if (arrayOfFile2 != null)
      {
        int j = arrayOfFile2.length;
        int i = 0;
        while (i < j)
        {
          deleteFile(arrayOfFile2[i], paramLong1, paramLong2);
          i += 1;
        }
        arrayOfFile1 = paramFile.listFiles();
      }
      if ((arrayOfFile1 != null) && (arrayOfFile1.length != 0))
        continue;
      paramFile.delete();
    }
  }

  private static String generateCacheFilename(String paramString)
  {
    return String.valueOf(paramString.hashCode());
  }

  private static String generateMemoryCacheKey(String paramString1, String paramString2)
  {
    if (paramString2 == null)
      return "_" + paramString1;
    return paramString2 + "_" + paramString1;
  }

  private static File getCacheFile(String paramString1, String paramString2, long paramLong)
  {
    Log.d("DPCache", "getCacheFile key=" + paramString1 + " category=" + paramString2 + " cacheType=" + paramLong);
    if (paramString1 == null)
      paramString1 = null;
    while (true)
    {
      return paramString1;
      paramString2 = getCacheFolder(paramString2, paramLong);
      if (paramString2 == null)
        break label222;
      paramString2 = new File(paramString2, generateCacheFilename(paramString1));
      if (!paramString2.exists())
        break;
      Log.d("DPCache", "getCacheFile key=" + paramString1 + " lastModified=" + paramString2.lastModified() + " currentTime=" + System.currentTimeMillis());
      paramString1 = paramString2;
      if (isValid(paramString2.lastModified(), paramLong))
        continue;
      boolean bool = paramString2.delete();
      Log.d("DPCache", "getCacheFile file=" + paramString2.getAbsolutePath() + " is expired" + " removed result=" + bool);
      return null;
    }
    Log.d("DPCache", "getCacheFile file=" + paramString2.getAbsolutePath() + " is not existed");
    label222: return null;
  }

  private static File getCacheFolder(String paramString, long paramLong)
  {
    Object localObject2 = null;
    Object localObject1;
    if (paramLong >= 31539600000L)
    {
      localObject1 = DPCachePermanentFolder;
      if (paramString == null)
        break label87;
      localObject1 = new File((File)localObject1, paramString);
      paramString = (String)localObject1;
      if (!((File)localObject1).exists())
        ((File)localObject1).mkdirs();
    }
    label87: for (paramString = (String)localObject1; ; paramString = (String)localObject1)
    {
      localObject1 = localObject2;
      if (paramString.exists())
      {
        localObject1 = localObject2;
        if (paramString.isDirectory())
          localObject1 = paramString;
      }
      do
      {
        return localObject1;
        localObject1 = localObject2;
      }
      while (paramLong < 3600000L);
      localObject1 = DPCacheFolder;
      break;
    }
  }

  private static String getCategoryFromMemoryCacheKey(String paramString)
  {
    int i = paramString.indexOf('_');
    if (i != -1)
      return paramString.substring(0, i);
    return null;
  }

  private Object getFromMemoryCache(String paramString, long paramLong)
  {
    if (paramString == null);
    LruCache localLruCache;
    while (true)
    {
      return null;
      DPCache.CacheInfo localCacheInfo;
      if ((paramString.hashCode() & 0x1) == 0)
        localCacheInfo = (DPCache.CacheInfo)this.cache0.get(paramString);
      for (localLruCache = this.cache0; localCacheInfo != null; localLruCache = this.cache1)
      {
        Log.d("DPCache", "key=" + paramString + " hit memory cache");
        if (!isValid(localCacheInfo.updateTime, paramLong))
          break label110;
        return localCacheInfo.value;
        localCacheInfo = (DPCache.CacheInfo)this.cache1.get(paramString);
      }
    }
    label110: localLruCache.remove(paramString);
    Log.d("DPCache", "cache of key=" + paramString + " is expired in memory cache");
    return null;
  }

  public static DPCache getInstance()
  {
    return DPCache.DPCacheInnerClass.DPCacheInstance;
  }

  private static boolean isValid(long paramLong1, long paramLong2)
  {
    long l = System.currentTimeMillis();
    if (paramLong2 >= 31539600000L);
    do
      while (true)
      {
        return true;
        if (paramLong2 != 86400000L)
          break;
        paramLong2 = DateUtil.getNextDayTimeMillis();
        if ((paramLong2 - 86400000L > paramLong1) || (paramLong1 >= paramLong2))
          return false;
      }
    while (paramLong1 + paramLong2 > l);
    return false;
  }

  private void putToMemoryCache(String paramString, Object paramObject)
  {
    if ((paramString == null) || (paramObject == null))
      return;
    if ((paramString.hashCode() & 0x1) == 0)
    {
      this.cache0.put(paramString, new DPCache.CacheInfo(paramObject));
      paramObject = new StringBuilder().append("put value of key ").append(paramString).append(" to memory ");
      if ((paramString.hashCode() & 0x1) != 0)
        break label106;
    }
    label106: for (paramString = "cache0"; ; paramString = "cache1")
    {
      Log.d("DPCache", paramString);
      return;
      this.cache1.put(paramString, new DPCache.CacheInfo(paramObject));
      break;
    }
  }

  private void removeFromMemoryCache(String paramString)
  {
    if (paramString == null)
      return;
    if ((paramString.hashCode() & 0x1) == 0)
      this.cache0.remove(paramString);
    while (true)
    {
      Log.d("DPCache", "remove value of " + paramString + " from memory cache");
      return;
      this.cache1.remove(paramString);
    }
  }

  public void clearByCategory(String paramString)
  {
    if (paramString == null)
      return;
    LruCache[] arrayOfLruCache = new LruCache[2];
    arrayOfLruCache[0] = this.cache0;
    arrayOfLruCache[1] = this.cache1;
    int i = 0;
    while (i < arrayOfLruCache.length)
    {
      LruCache localLruCache = arrayOfLruCache[i];
      Iterator localIterator = localLruCache.snapshot().keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if (!paramString.equals(getCategoryFromMemoryCacheKey(str)))
          continue;
        localLruCache.remove(str);
      }
      i += 1;
    }
    this.mExecutor.submit(new DPCache.18(this, paramString));
  }

  public void clearExpiredCache()
  {
    clearExpiredCacheByDays(15);
  }

  public void clearExpiredCacheByDays(int paramInt)
  {
    this.mExecutor.submit(new DPCache.19(this, paramInt));
  }

  public void clearExpiredCacheByMills(long paramLong)
  {
    this.mExecutor.submit(new DPCache.20(this, paramLong));
  }

  public void clearFileCache()
  {
    Log.w("DPCache", "clearFileCache");
    FileUtils.removeAllFiles(DPCacheFolder);
  }

  void clearLock()
  {
    synchronized (this.locks)
    {
      this.runnings -= 1;
      if (this.runnings == 0)
        this.locks.clear();
      return;
    }
  }

  public void clearMemoryCache()
  {
    Log.w("DPCache", "clearMemoryCache");
    this.cache0.evictAll();
    this.cache1.evictAll();
  }

  public boolean containsKey(String paramString1, String paramString2, long paramLong)
  {
    if (paramString1 == null);
    do
      return false;
    while ((!memoryContainsKey(paramString1, paramString2, paramLong)) && (getCacheFile(paramString1, paramString2, paramLong) == null));
    return true;
  }

  public void dumpCache()
  {
    Object localObject = this.cache0.snapshot().keySet().iterator();
    Log.d("DPCache", "cache0 summary " + this.cache0.toString());
    while (((Iterator)localObject).hasNext())
      Log.d("DPCache", "cache0 key " + (String)((Iterator)localObject).next());
    localObject = this.cache1.snapshot().keySet().iterator();
    Log.d("DPCache", "cache1 summary " + this.cache1.toString());
    while (((Iterator)localObject).hasNext())
      Log.d("DPCache", "cache1 key " + (String)((Iterator)localObject).next());
    localObject = DPCachePermanentFolder.listFiles();
    int j;
    int i;
    if (localObject != null)
    {
      j = localObject.length;
      i = 0;
      while (i < j)
      {
        Log.d("DPCache", "permanent file=" + localObject[i] + " size=" + localObject[i].length());
        i += 1;
      }
    }
    localObject = DPCacheFolder.listFiles();
    if (localObject != null)
    {
      j = localObject.length;
      i = 0;
      while (i < j)
      {
        Log.d("DPCache", "cache file=" + localObject[i] + " size=" + localObject[i].length());
        i += 1;
      }
    }
    Log.d("DPCache", "remain locks " + this.runnings);
    localObject = this.locks.keySet().iterator();
    while (((Iterator)localObject).hasNext())
      Log.d("DPCache", "lock key=" + (String)((Iterator)localObject).next());
  }

  public Bitmap getBitmap(String paramString1, String paramString2, long paramLong)
  {
    return getBitmap(paramString1, paramString2, paramLong, true);
  }

  public Bitmap getBitmap(String paramString1, String paramString2, long paramLong, boolean paramBoolean)
  {
    if (paramString1 == null)
      return null;
    if (paramBoolean)
    {
      localObject1 = getFromMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramLong);
      if ((localObject1 instanceof Bitmap))
        return (Bitmap)localObject1;
    }
    Object localObject1 = null;
    synchronized (getLock(paramString1))
    {
      Object localObject2 = getCacheFile(paramString1, paramString2, paramLong);
      if (localObject2 != null)
      {
        localObject2 = FileUtils.getBitmap((File)localObject2);
        localObject1 = localObject2;
        if (localObject2 != null)
        {
          localObject1 = localObject2;
          if (paramBoolean)
          {
            putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), localObject2);
            localObject1 = localObject2;
          }
        }
      }
      clearLock();
      return localObject1;
    }
  }

  public void getBitmap(String paramString1, String paramString2, long paramLong, DPCache.OnGetListener<Bitmap> paramOnGetListener)
  {
    getBitmap(paramString1, paramString2, paramLong, true, paramOnGetListener);
  }

  public void getBitmap(String paramString1, String paramString2, long paramLong, boolean paramBoolean, DPCache.OnGetListener<Bitmap> paramOnGetListener)
  {
    if (paramOnGetListener != null)
      this.mExecutor.execute(new DPCache.11(this, paramString1, paramString2, paramLong, paramBoolean, paramOnGetListener));
  }

  public void getBytes(String paramString1, String paramString2, long paramLong, DPCache.OnGetListener<byte[]> paramOnGetListener)
  {
    getBytes(paramString1, paramString2, paramLong, true, paramOnGetListener);
  }

  public void getBytes(String paramString1, String paramString2, long paramLong, boolean paramBoolean, DPCache.OnGetListener<byte[]> paramOnGetListener)
  {
    if (paramOnGetListener != null)
      this.mExecutor.execute(new DPCache.9(this, paramString1, paramString2, paramLong, paramBoolean, paramOnGetListener));
  }

  public byte[] getBytes(String paramString1, String paramString2, long paramLong)
  {
    return getBytes(paramString1, paramString2, paramLong, true);
  }

  public byte[] getBytes(String paramString1, String paramString2, long paramLong, boolean paramBoolean)
  {
    if (paramString1 == null)
      return null;
    if (paramBoolean)
    {
      localObject1 = getFromMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramLong);
      if ((localObject1 instanceof byte[]))
        return (byte[])(byte[])localObject1;
    }
    Object localObject1 = null;
    synchronized (getLock(paramString1))
    {
      Object localObject2 = getCacheFile(paramString1, paramString2, paramLong);
      if (localObject2 != null)
      {
        localObject2 = FileUtils.getBytes((File)localObject2);
        localObject1 = localObject2;
        if (localObject2 != null)
        {
          localObject1 = localObject2;
          if (paramBoolean)
          {
            putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), localObject2);
            localObject1 = localObject2;
          }
        }
      }
      clearLock();
      return localObject1;
    }
  }

  public Drawable getDrawable(String paramString1, String paramString2, long paramLong)
  {
    return getDrawable(paramString1, paramString2, paramLong, true);
  }

  public Drawable getDrawable(String paramString1, String paramString2, long paramLong, boolean paramBoolean)
  {
    if (paramString1 == null)
      return null;
    if (paramBoolean)
    {
      localObject1 = getFromMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramLong);
      if ((localObject1 instanceof Drawable))
        return (Drawable)localObject1;
    }
    Object localObject1 = null;
    synchronized (getLock(paramString1))
    {
      Object localObject2 = getCacheFile(paramString1, paramString2, paramLong);
      if (localObject2 != null)
      {
        localObject2 = FileUtils.getDrawable((File)localObject2);
        localObject1 = localObject2;
        if (localObject2 != null)
        {
          localObject1 = localObject2;
          if (paramBoolean)
          {
            putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), localObject2);
            localObject1 = localObject2;
          }
        }
      }
      clearLock();
      return localObject1;
    }
  }

  public void getDrawable(String paramString1, String paramString2, long paramLong, DPCache.OnGetListener<Drawable> paramOnGetListener)
  {
    getDrawable(paramString1, paramString2, paramLong, true, paramOnGetListener);
  }

  public void getDrawable(String paramString1, String paramString2, long paramLong, boolean paramBoolean, DPCache.OnGetListener<Drawable> paramOnGetListener)
  {
    if (paramOnGetListener != null)
      this.mExecutor.execute(new DPCache.12(this, paramString1, paramString2, paramLong, paramBoolean, paramOnGetListener));
  }

  Object getLock(String paramString)
  {
    synchronized (this.locks)
    {
      this.runnings += 1;
      Object localObject2 = this.locks.get(paramString);
      Object localObject1 = localObject2;
      if (localObject2 == null)
      {
        localObject1 = new Object();
        this.locks.put(paramString, localObject1);
      }
      return localObject1;
    }
  }

  public <T> T getParcelable(String paramString1, String paramString2, long paramLong, Parcelable.Creator<T> paramCreator)
  {
    return getParcelable(paramString1, paramString2, paramLong, true, paramCreator);
  }

  public <T> T getParcelable(String paramString1, String paramString2, long paramLong, boolean paramBoolean, Parcelable.Creator<T> paramCreator)
  {
    if (paramString1 == null)
      localObject1 = null;
    do
    {
      return localObject1;
      if (!paramBoolean)
        break;
      ??? = getFromMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramLong);
      localObject1 = ???;
    }
    while (??? != null);
    Object localObject1 = null;
    synchronized (getLock(paramString1))
    {
      File localFile = getCacheFile(paramString1, paramString2, paramLong);
      if (localFile != null)
      {
        paramCreator = FileUtils.getParcelable(localFile, paramCreator);
        localObject1 = paramCreator;
        if (paramCreator != null)
        {
          localObject1 = paramCreator;
          if (paramBoolean)
          {
            putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramCreator);
            localObject1 = paramCreator;
          }
        }
      }
      clearLock();
      return localObject1;
    }
  }

  public <T> void getParcelable(String paramString1, String paramString2, long paramLong, Parcelable.Creator<T> paramCreator, DPCache.OnGetListener<T> paramOnGetListener)
  {
    getParcelable(paramString1, paramString2, paramLong, true, paramCreator, paramOnGetListener);
  }

  public <T> void getParcelable(String paramString1, String paramString2, long paramLong, boolean paramBoolean, Parcelable.Creator<T> paramCreator, DPCache.OnGetListener<T> paramOnGetListener)
  {
    if (paramOnGetListener != null)
      this.mExecutor.execute(new DPCache.13(this, paramString1, paramString2, paramLong, paramBoolean, paramCreator, paramOnGetListener));
  }

  public <T> List<T> getParcelableArray(String paramString1, String paramString2, long paramLong, Parcelable.Creator<T> paramCreator)
  {
    return getParcelableArray(paramString1, paramString2, paramLong, true, paramCreator);
  }

  public <T> List<T> getParcelableArray(String paramString1, String paramString2, long paramLong, boolean paramBoolean, Parcelable.Creator<T> paramCreator)
  {
    if (paramString1 == null)
      return null;
    if (paramBoolean)
    {
      localObject1 = getFromMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramLong);
      if (localObject1 != null)
        try
        {
          paramString1 = (List)localObject1;
          return paramString1;
        }
        catch (ClassCastException paramString1)
        {
          paramString1.printStackTrace();
          return null;
        }
    }
    Object localObject2 = null;
    Object localObject1 = null;
    synchronized (getLock(paramString1))
    {
      File localFile = getCacheFile(paramString1, paramString2, paramLong);
      if (localFile != null)
        localObject1 = localObject2;
      try
      {
        paramCreator = FileUtils.getParcelableArray(localFile, paramCreator);
        localObject1 = paramCreator;
        if (paramCreator != null)
        {
          localObject1 = paramCreator;
          if (paramBoolean)
          {
            localObject1 = paramCreator;
            Log.d("DPCache", "getParcelableArray valueFromFile's length=" + paramCreator.size() + " file's length=" + localFile.length());
            localObject1 = paramCreator;
            putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramCreator);
            localObject1 = paramCreator;
          }
        }
        clearLock();
        return localObject1;
      }
      catch (ClassCastException paramString1)
      {
        while (true)
          paramString1.printStackTrace();
      }
    }
  }

  public <T> void getParcelableArray(String paramString1, String paramString2, long paramLong, Parcelable.Creator<T> paramCreator, DPCache.OnGetListener<List<T>> paramOnGetListener)
  {
    getParcelableArray(paramString1, paramString2, paramLong, true, paramCreator, paramOnGetListener);
  }

  public <T> void getParcelableArray(String paramString1, String paramString2, long paramLong, boolean paramBoolean, Parcelable.Creator<T> paramCreator, DPCache.OnGetListener<List<T>> paramOnGetListener)
  {
    if (paramOnGetListener != null)
      this.mExecutor.execute(new DPCache.14(this, paramString1, paramString2, paramLong, paramBoolean, paramCreator, paramOnGetListener));
  }

  public Object getSerializable(String paramString1, String paramString2, long paramLong)
  {
    return getSerializable(paramString1, paramString2, paramLong, true);
  }

  public Object getSerializable(String paramString1, String paramString2, long paramLong, boolean paramBoolean)
  {
    if (paramString1 == null)
      localObject1 = null;
    Object localObject2;
    do
    {
      return localObject1;
      if (!paramBoolean)
        break;
      localObject2 = getFromMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramLong);
      localObject1 = localObject2;
    }
    while (localObject2 != null);
    Object localObject1 = null;
    synchronized (getLock(paramString1))
    {
      localObject2 = getCacheFile(paramString1, paramString2, paramLong);
      if (localObject2 != null)
      {
        localObject2 = FileUtils.getSerializable((File)localObject2);
        localObject1 = localObject2;
        if (localObject2 != null)
        {
          localObject1 = localObject2;
          if (paramBoolean)
          {
            putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), localObject2);
            localObject1 = localObject2;
          }
        }
      }
      clearLock();
      return localObject1;
    }
  }

  public void getSerializable(String paramString1, String paramString2, long paramLong, DPCache.OnGetListener<Object> paramOnGetListener)
  {
    getSerializable(paramString1, paramString2, paramLong, true, paramOnGetListener);
  }

  public void getSerializable(String paramString1, String paramString2, long paramLong, boolean paramBoolean, DPCache.OnGetListener<Object> paramOnGetListener)
  {
    if (paramOnGetListener != null)
      this.mExecutor.execute(new DPCache.15(this, paramString1, paramString2, paramLong, paramBoolean, paramOnGetListener));
  }

  public String getString(String paramString1, String paramString2, long paramLong)
  {
    return getString(paramString1, paramString2, paramLong, true);
  }

  public String getString(String paramString1, String paramString2, long paramLong, boolean paramBoolean)
  {
    if (paramString1 == null)
      return null;
    if (paramBoolean)
    {
      localObject1 = getFromMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramLong);
      if ((localObject1 instanceof String))
        return (String)localObject1;
    }
    Object localObject1 = null;
    synchronized (getLock(paramString1))
    {
      Object localObject2 = getCacheFile(paramString1, paramString2, paramLong);
      if (localObject2 != null)
      {
        localObject2 = FileUtils.getString((File)localObject2);
        localObject1 = localObject2;
        if (localObject2 != null)
        {
          localObject1 = localObject2;
          if (paramBoolean)
          {
            putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), localObject2);
            localObject1 = localObject2;
          }
        }
      }
      clearLock();
      return localObject1;
    }
  }

  public void getString(String paramString1, String paramString2, long paramLong, DPCache.OnGetListener<String> paramOnGetListener)
  {
    getString(paramString1, paramString2, paramLong, true, paramOnGetListener);
  }

  public void getString(String paramString1, String paramString2, long paramLong, boolean paramBoolean, DPCache.OnGetListener<String> paramOnGetListener)
  {
    if (paramOnGetListener != null)
      this.mExecutor.execute(new DPCache.10(this, paramString1, paramString2, paramLong, paramBoolean, paramOnGetListener));
  }

  public boolean memoryContainsKey(String paramString1, String paramString2, long paramLong)
  {
    if (paramString1 == null);
    while (true)
    {
      return false;
      if ((paramString1.hashCode() & 0x1) == 0);
      for (paramString1 = (DPCache.CacheInfo)this.cache0.get(generateMemoryCacheKey(paramString1, paramString2)); (paramString1 != null) && (isValid(paramString1.updateTime, paramLong)); paramString1 = (DPCache.CacheInfo)this.cache1.get(generateMemoryCacheKey(paramString1, paramString2)))
        return true;
    }
  }

  public boolean put(String paramString1, String paramString2, Bitmap paramBitmap, long paramLong)
  {
    return put(paramString1, paramString2, paramBitmap, paramLong, true);
  }

  public boolean put(String paramString1, String paramString2, Bitmap paramBitmap, long paramLong, boolean paramBoolean)
  {
    if ((paramString1 == null) || (paramBitmap == null))
      return false;
    if (paramBoolean)
      putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramBitmap);
    this.mExecutor.submit(new DPCache.5(this, paramString1, paramString2, paramLong, paramBitmap));
    return true;
  }

  public boolean put(String paramString1, String paramString2, Drawable paramDrawable, long paramLong)
  {
    return put(paramString1, paramString2, paramDrawable, paramLong, true);
  }

  public boolean put(String paramString1, String paramString2, Drawable paramDrawable, long paramLong, boolean paramBoolean)
  {
    if ((paramString1 == null) || (paramDrawable == null))
      return false;
    if (paramBoolean)
      putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramDrawable);
    this.mExecutor.submit(new DPCache.4(this, paramString1, paramString2, paramLong, paramDrawable));
    return true;
  }

  public boolean put(String paramString1, String paramString2, Parcelable paramParcelable, long paramLong)
  {
    return put(paramString1, paramString2, paramParcelable, paramLong, true);
  }

  public boolean put(String paramString1, String paramString2, Parcelable paramParcelable, long paramLong, boolean paramBoolean)
  {
    if ((paramString1 == null) || (paramParcelable == null))
      return false;
    if (paramBoolean)
      putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramParcelable);
    this.mExecutor.submit(new DPCache.6(this, paramString1, paramString2, paramLong, paramParcelable));
    return true;
  }

  public boolean put(String paramString1, String paramString2, Serializable paramSerializable, long paramLong)
  {
    return put(paramString1, paramString2, paramSerializable, paramLong, true);
  }

  public boolean put(String paramString1, String paramString2, Serializable paramSerializable, long paramLong, boolean paramBoolean)
  {
    if ((paramString1 == null) || (paramSerializable == null))
      return false;
    if (paramBoolean)
      putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramSerializable);
    this.mExecutor.submit(new DPCache.8(this, paramString1, paramString2, paramLong, paramSerializable));
    return true;
  }

  public boolean put(String paramString1, String paramString2, String paramString3, long paramLong)
  {
    return put(paramString1, paramString2, paramString3, paramLong, true);
  }

  public boolean put(String paramString1, String paramString2, String paramString3, long paramLong, boolean paramBoolean)
  {
    if ((paramString1 == null) || (paramString3 == null))
      return false;
    if (paramBoolean)
      putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramString3);
    this.mExecutor.submit(new DPCache.3(this, paramString1, paramString2, paramLong, paramString3));
    return true;
  }

  public boolean put(String paramString1, String paramString2, byte[] paramArrayOfByte, long paramLong)
  {
    return put(paramString1, paramString2, paramArrayOfByte, paramLong, true);
  }

  public boolean put(String paramString1, String paramString2, byte[] paramArrayOfByte, long paramLong, boolean paramBoolean)
  {
    if ((paramString1 == null) || (paramArrayOfByte == null))
      return false;
    if (paramBoolean)
      putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), paramArrayOfByte);
    this.mExecutor.submit(new DPCache.2(this, paramString1, paramString2, paramLong, paramArrayOfByte));
    return true;
  }

  public boolean put(String paramString1, String paramString2, Parcelable[] paramArrayOfParcelable, long paramLong)
  {
    return put(paramString1, paramString2, paramArrayOfParcelable, paramLong, true);
  }

  public boolean put(String paramString1, String paramString2, Parcelable[] paramArrayOfParcelable, long paramLong, boolean paramBoolean)
  {
    if ((paramString1 == null) || (paramArrayOfParcelable == null))
      return false;
    if (paramBoolean)
      putToMemoryCache(generateMemoryCacheKey(paramString1, paramString2), Arrays.asList(paramArrayOfParcelable));
    this.mExecutor.submit(new DPCache.7(this, paramString1, paramString2, paramLong, paramArrayOfParcelable));
    return true;
  }

  public boolean remove(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return false;
    removeFromMemoryCache(generateMemoryCacheKey(paramString1, paramString2));
    this.mExecutor.submit(new DPCache.16(this, paramString1, paramString2));
    return true;
  }

  public boolean remove(String paramString1, String paramString2, long paramLong)
  {
    if (paramString1 == null)
      return false;
    removeFromMemoryCache(generateMemoryCacheKey(paramString1, paramString2));
    this.mExecutor.submit(new DPCache.17(this, paramString1, paramString2, paramLong));
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.cache.DPCache
 * JD-Core Version:    0.6.0
 */