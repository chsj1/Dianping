package com.github.mmin18.layoutcast.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.SparseArray;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class ResUtils
{
  private static HashMap<String, Integer> layoutIds;
  private static SparseArray<String> layoutNames;
  private static final HashMap<String, WeakReference<Resources>> resources = new HashMap();

  public static int getLayoutId(Context paramContext, Resources paramResources, String paramString)
    throws Exception
  {
    if (layoutIds == null)
      getLayoutName(paramContext, paramResources, 0);
    paramContext = (Integer)layoutIds.get(paramString);
    if (paramContext == null)
      return 0;
    return paramContext.intValue();
  }

  public static String getLayoutName(Context paramContext, Resources paramResources, int paramInt)
    throws Exception
  {
    if ((layoutIds == null) || (layoutNames == null))
    {
      paramResources = new HashMap();
      SparseArray localSparseArray = new SparseArray();
      String str = paramContext.getPackageName() + ".R.layout";
      paramContext = paramContext.getClassLoader().loadClass(str).getDeclaredFields();
      int j = paramContext.length;
      int i = 0;
      while (i < j)
      {
        str = paramContext[i];
        if ((Modifier.isStatic(str.getModifiers())) && (str.getType() == Integer.TYPE))
        {
          Integer localInteger = (Integer)str.get(null);
          paramResources.put(str.getName(), Integer.valueOf(localInteger.intValue()));
          localSparseArray.put(localInteger.intValue(), str.getName());
        }
        i += 1;
      }
      layoutIds = paramResources;
      layoutNames = localSparseArray;
    }
    return (String)layoutNames.get(paramInt);
  }

  public static Resources getResources(Context paramContext, File paramFile)
    throws Exception
  {
    String str = paramFile.getAbsolutePath();
    Object localObject = (WeakReference)resources.get(str);
    if (localObject != null)
    {
      localObject = (Resources)((WeakReference)localObject).get();
      if (localObject != null)
        return localObject;
    }
    localObject = (AssetManager)AssetManager.class.newInstance();
    localObject.getClass().getMethod("addAssetPath", new Class[] { String.class }).invoke(localObject, new Object[] { paramFile.getAbsolutePath() });
    paramContext = paramContext.getResources();
    paramContext = new Resources((AssetManager)localObject, paramContext.getDisplayMetrics(), paramContext.getConfiguration());
    resources.put(str, new WeakReference(paramContext));
    return (Resources)paramContext;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.github.mmin18.layoutcast.util.ResUtils
 * JD-Core Version:    0.6.0
 */