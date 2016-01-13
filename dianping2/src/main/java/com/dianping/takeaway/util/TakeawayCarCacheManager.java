package com.dianping.takeaway.util;

import android.content.Context;
import android.text.TextUtils;
import com.dianping.takeaway.entity.TakeawayDishInfo;
import com.dianping.takeaway.entity.TakeawayDishInfo.Builder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TakeawayCarCacheManager
{
  private static final String TA_INOF_DIVIDER = ",";

  public static void addCacheMenu(Context paramContext, MenuCache paramMenuCache)
  {
    if ((TextUtils.isEmpty(paramMenuCache.shopId)) || (paramMenuCache.dishList == null) || (paramMenuCache.dishList.isEmpty()))
      return;
    paramContext = new StringBuilder();
    paramContext.append(paramMenuCache.shopId);
    paramMenuCache = paramMenuCache.dishList.iterator();
    while (paramMenuCache.hasNext())
    {
      TakeawayDishInfo localTakeawayDishInfo = (TakeawayDishInfo)paramMenuCache.next();
      paramContext.append(",").append(localTakeawayDishInfo.id).append(",").append(localTakeawayDishInfo.selectedNum);
    }
    putFileContent(paramContext.toString());
  }

  public static void clearCacheMenu(Context paramContext)
  {
    putFileContent("");
  }

  private static String getFileContent()
  {
    ArrayList localArrayList = new TakeawayFileRWer("ta_menu_cache").readFileToList(new TakeawayFileRWer.FileLineParser()
    {
      public String parse(String paramString)
      {
        return paramString;
      }
    });
    if ((localArrayList == null) || (localArrayList.isEmpty()))
      return "";
    return (String)localArrayList.get(0);
  }

  private static void putFileContent(String paramString)
  {
    TakeawayFileRWer localTakeawayFileRWer = new TakeawayFileRWer("ta_menu_cache");
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(paramString);
    localTakeawayFileRWer.writeListToFile(localArrayList);
  }

  public static MenuCache readCacheMenu(Context paramContext)
  {
    paramContext = new MenuCache();
    Object localObject = getFileContent();
    if (TextUtils.isEmpty((CharSequence)localObject));
    while (true)
    {
      return paramContext;
      localObject = ((String)localObject).split(",");
      paramContext.shopId = localObject[0];
      int i = 1;
      while (i < localObject.length)
      {
        int j = Integer.parseInt(localObject[i]);
        int k = Integer.parseInt(localObject[(i + 1)]);
        paramContext.dishMap.put(Integer.valueOf(j), TakeawayDishInfo.Builder.buildTitleItemInfo(j, k));
        i += 2;
      }
    }
  }

  public static class MenuCache
  {
    public List<TakeawayDishInfo> dishList;
    public HashMap<Integer, TakeawayDishInfo> dishMap;
    public String shopId;

    public MenuCache()
    {
      this.shopId = "";
      this.dishList = new ArrayList(0);
      this.dishMap = new HashMap(0);
    }

    public MenuCache(String paramString, List<TakeawayDishInfo> paramList)
    {
      this.shopId = paramString;
      this.dishList = paramList;
      this.dishMap = new HashMap(0);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.util.TakeawayCarCacheManager
 * JD-Core Version:    0.6.0
 */