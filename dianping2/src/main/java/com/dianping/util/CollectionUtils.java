package com.dianping.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CollectionUtils
{
  public static String list2Str(List<String> paramList, String paramString)
  {
    if ((paramList == null) || (paramList.size() == 0))
      return "";
    StringBuffer localStringBuffer = new StringBuffer();
    paramList = paramList.iterator();
    while (paramList.hasNext())
      localStringBuffer.append((String)paramList.next()).append(paramString);
    paramList = localStringBuffer.toString();
    return paramList.substring(0, paramList.length() - 1);
  }

  public static String[] str2Array(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return null;
    return paramString1.split(paramString2);
  }

  public static ArrayList<String> str2ArrayList(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return null;
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(Arrays.asList(str2Array(paramString1, paramString2)));
    return localArrayList;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.CollectionUtils
 * JD-Core Version:    0.6.0
 */