package com.dianping.util;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Parcelable;
import java.lang.reflect.Method;
import java.util.HashMap;

public class SearchUtils
{
  private static Method getHintId;
  private static HashMap<String, Method> getMethods = new HashMap();
  private static Method getSearchActivity;
  private static Method getSearchableInfo1;
  private static Method getSearchableInfo2;

  public static String getFromSearchable(Object paramObject, String paramString)
  {
    if (paramObject == null)
      return null;
    try
    {
      Method localMethod2 = (Method)getMethods.get(paramString);
      Method localMethod1 = localMethod2;
      if (localMethod2 == null)
      {
        localMethod1 = paramObject.getClass().getMethod(paramString, new Class[0]);
        getMethods.put(paramString, localMethod1);
      }
      paramObject = (String)localMethod1.invoke(paramObject, new Object[0]);
      return paramObject;
    }
    catch (Exception paramObject)
    {
      paramObject.printStackTrace();
    }
    return null;
  }

  public static int getHintId(Object paramObject)
  {
    if (paramObject == null)
      return 0;
    try
    {
      if (getHintId == null)
        getHintId = paramObject.getClass().getMethod("getHintId", new Class[0]);
      int i = ((Integer)getHintId.invoke(paramObject, new Object[0])).intValue();
      return i;
    }
    catch (Exception paramObject)
    {
      paramObject.printStackTrace();
    }
    return 0;
  }

  public static ComponentName getSearchActivity(Object paramObject)
  {
    if (paramObject == null)
      return null;
    try
    {
      if (getSearchActivity == null)
        getSearchActivity = paramObject.getClass().getMethod("getSearchActivity", new Class[0]);
      paramObject = (ComponentName)getSearchActivity.invoke(paramObject, new Object[0]);
      return paramObject;
    }
    catch (Exception paramObject)
    {
      paramObject.printStackTrace();
    }
    return null;
  }

  public static Parcelable getSearchableInfo(Context paramContext, ComponentName paramComponentName)
  {
    paramContext = (SearchManager)paramContext.getSystemService("search");
    try
    {
      if (getSearchableInfo1 == null)
        getSearchableInfo1 = SearchManager.class.getMethod("getSearchableInfo", new Class[] { ComponentName.class });
      Parcelable localParcelable = (Parcelable)getSearchableInfo1.invoke(paramContext, new Object[] { paramComponentName });
      return localParcelable;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      try
      {
        if (getSearchableInfo2 == null)
          getSearchableInfo2 = SearchManager.class.getMethod("getSearchableInfo", new Class[] { ComponentName.class, Boolean.TYPE });
        paramContext = (Parcelable)getSearchableInfo2.invoke(paramContext, new Object[] { paramComponentName, Boolean.valueOf(false) });
        return paramContext;
      }
      catch (Exception paramContext)
      {
        paramContext.printStackTrace();
      }
    }
    return null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.SearchUtils
 * JD-Core Version:    0.6.0
 */