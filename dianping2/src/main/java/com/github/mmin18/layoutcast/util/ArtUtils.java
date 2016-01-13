package com.github.mmin18.layoutcast.util;

import android.util.Log;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class ArtUtils
{
  public static boolean overrideClassLoader(ClassLoader paramClassLoader, File paramFile1, File paramFile2)
  {
    try
    {
      Object localObject3 = paramClassLoader.getParent();
      Field localField2 = BaseDexClassLoader.class.getDeclaredField("pathList");
      localField2.setAccessible(true);
      Object localObject1 = localField2.get(paramClassLoader);
      Field localField1 = ((ClassLoader)localObject3).loadClass("dalvik.system.DexPathList").getDeclaredField("dexElements");
      localField1.setAccessible(true);
      Object localObject2 = localField1.get(localObject1);
      paramFile2 = Array.get(localField1.get(localField2.get(new DexClassLoader(paramFile1.getAbsolutePath(), paramFile2.getAbsolutePath(), null, (ClassLoader)localObject3))), 0);
      int j = Array.getLength(localObject2) + 1;
      localObject3 = Array.newInstance(localField1.getType().getComponentType(), j);
      Array.set(localObject3, 0, paramFile2);
      int i = 0;
      while (i < j - 1)
      {
        Array.set(localObject3, i + 1, Array.get(localObject2, i));
        i += 1;
      }
      localField1.set(localObject1, localObject3);
      return true;
    }
    catch (java.lang.Exception paramFile2)
    {
      Log.e("lcast", "fail to override classloader " + paramClassLoader + " with " + paramFile1, paramFile2);
    }
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.github.mmin18.layoutcast.util.ArtUtils
 * JD-Core Version:    0.6.0
 */