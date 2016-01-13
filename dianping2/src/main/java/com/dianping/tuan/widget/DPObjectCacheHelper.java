package com.dianping.tuan.widget;

import com.dianping.archive.DPObject;
import com.dianping.cache.DPCache;

public class DPObjectCacheHelper
{
  private static DPObjectCacheHelper instance;

  public static DPObjectCacheHelper getInstance()
  {
    if (instance == null)
      instance = new DPObjectCacheHelper();
    return instance;
  }

  public DPObject getCache(String paramString)
  {
    DPObject localDPObject = (DPObject)DPCache.getInstance().getParcelable(paramString, null, 31539600000L, DPObject.CREATOR);
    if (localDPObject == null)
      DPCache.getInstance().remove(paramString, null, 31539600000L);
    return localDPObject;
  }

  public void getCache(String paramString, DPObjectCacheHelper.OnRestoreCacheListener paramOnRestoreCacheListener)
  {
    DPCache.getInstance().getParcelable(paramString, null, 31539600000L, DPObject.CREATOR, new DPObjectCacheHelper.1(this, paramOnRestoreCacheListener));
  }

  public void writeCache(DPObject paramDPObject, String paramString)
  {
    if (paramDPObject == null)
    {
      DPCache.getInstance().remove(paramString, null, 31539600000L);
      return;
    }
    DPCache.getInstance().put(paramString, null, paramDPObject, 31539600000L);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.widget.DPObjectCacheHelper
 * JD-Core Version:    0.6.0
 */