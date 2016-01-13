package com.dianping.base.util;

import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.cache.DPCache;
import com.dianping.util.FileUtils;
import com.dianping.util.Log;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class HistoryHelper
{
  private static final boolean DEBUG = true;
  private static final String SHOP_CACHE_KEYHEADER = "main://history/shop";
  private static final int SHOP_LIMIT = 20;
  private static final String TAG = "HistoryHelper";
  private static File historyFile = DPApplication.instance().getFileStreamPath("history.id");
  private static HistoryHelper instance;
  private Queue<Integer> ids = new LinkedList();

  private HistoryHelper()
  {
    initIds();
  }

  public static HistoryHelper getInstance()
  {
    if (instance == null)
      instance = new HistoryHelper();
    return instance;
  }

  private boolean initIds()
  {
    int m = 1;
    monitorenter;
    try
    {
      Log.d("HistoryHelper", "init ids");
      clearIds();
      byte[] arrayOfByte = FileUtils.getBytes(historyFile);
      int k;
      if (arrayOfByte == null)
        k = m;
      while (true)
      {
        return k;
        try
        {
          ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
          int j = arrayOfByte.length / 4;
          int i = 0;
          while (true)
          {
            k = m;
            if (i >= j)
              break;
            addId(localByteBuffer.getInt());
            i += 1;
          }
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
          historyFile.delete();
          k = 0;
        }
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void addId(int paramInt)
  {
    synchronized (this.ids)
    {
      this.ids.add(Integer.valueOf(paramInt));
      Log.d("HistoryHelper", "addId id=" + paramInt);
      if (this.ids.size() > 20)
      {
        Integer localInteger = (Integer)this.ids.poll();
        if (localInteger != null)
        {
          removeShop(localInteger.intValue());
          Log.d("HistoryHelper", "remove oldest shop id=" + paramInt);
        }
      }
      return;
    }
  }

  public boolean addShop(int paramInt, DPObject paramDPObject)
  {
    return DPCache.getInstance().put("main://history/shop" + paramInt, null, paramDPObject, 31539600000L);
  }

  public void clearIds()
  {
    synchronized (this.ids)
    {
      this.ids.clear();
      return;
    }
  }

  public boolean containId(int paramInt)
  {
    synchronized (this.ids)
    {
      boolean bool = this.ids.contains(Integer.valueOf(paramInt));
      return bool;
    }
  }

  public boolean flushIds()
  {
    monitorenter;
    try
    {
      byte[] arrayOfByte = new byte[this.ids.size() * 4];
      ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
      Iterator localIterator = this.ids.iterator();
      while (localIterator.hasNext())
      {
        Integer localInteger = (Integer)localIterator.next();
        localByteBuffer.putInt(localInteger.intValue());
        Log.d("HistoryHelper", "flush id=" + localInteger.intValue());
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      for (boolean bool = false; ; bool = FileUtils.put(historyFile, localException))
        return bool;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public Integer[] getIds()
  {
    synchronized (this.ids)
    {
      Integer[] arrayOfInteger = (Integer[])this.ids.toArray(new Integer[0]);
      int j = arrayOfInteger.length;
      ??? = new Integer[j];
      int i = 0;
      if (i < j)
      {
        ???[i] = arrayOfInteger[(j - i - 1)];
        i += 1;
      }
    }
    return (Integer)???;
  }

  public DPObject getShopById(int paramInt)
  {
    return (DPObject)DPCache.getInstance().getParcelable("main://history/shop" + paramInt, null, 31539600000L, DPObject.CREATOR);
  }

  public boolean removeId(int paramInt)
  {
    synchronized (this.ids)
    {
      boolean bool = this.ids.remove(Integer.valueOf(paramInt));
      return bool;
    }
  }

  public boolean removeShop(int paramInt)
  {
    return DPCache.getInstance().remove("main://history/shop" + paramInt, null, 31539600000L);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.HistoryHelper
 * JD-Core Version:    0.6.0
 */