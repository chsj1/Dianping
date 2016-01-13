package com.github.mmin18.layoutcast.server;

import android.content.res.Resources;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class IdProfileBuilder
{
  private Resources res;

  public IdProfileBuilder(Resources paramResources)
  {
    this.res = paramResources;
  }

  private void buildIds(StringBuilder paramStringBuilder, Class<?> paramClass)
    throws Exception
  {
    int i = 0;
    int k = 0;
    paramClass = paramClass.getDeclaredFields();
    int i3 = paramClass.length;
    int m = 0;
    while (m < i3)
    {
      Object localObject = paramClass[m];
      int i2 = k;
      int n = i;
      if (Integer.TYPE.equals(localObject.getType()))
      {
        i2 = k;
        n = i;
        if (Modifier.isStatic(localObject.getModifiers()))
        {
          i2 = k;
          n = i;
          if (Modifier.isPublic(localObject.getModifiers()))
          {
            int i1 = localObject.getInt(null);
            i2 = k;
            n = i;
            if ((i1 & 0x7F000000) == 2130706432)
            {
              int j;
              if (i != 0)
              {
                j = i;
                if (i1 >= i);
              }
              else
              {
                j = i1;
              }
              if (k != 0)
              {
                i2 = k;
                n = j;
                if (i1 <= k);
              }
              else
              {
                n = j;
                i2 = i1;
              }
            }
          }
        }
      }
      m += 1;
      k = i2;
      i = n;
    }
    while ((i > 0) && (i <= k))
    {
      paramStringBuilder.append("  <item type=\"");
      paramStringBuilder.append(this.res.getResourceTypeName(i));
      paramStringBuilder.append("\" name=\"");
      paramStringBuilder.append(this.res.getResourceEntryName(i));
      paramStringBuilder.append("\" />\n");
      i += 1;
    }
  }

  private void buildPublic(StringBuilder paramStringBuilder, Class<?> paramClass, String paramString)
    throws Exception
  {
    int i = 0;
    int k = 0;
    paramClass = paramClass.getDeclaredFields();
    int i3 = paramClass.length;
    int m = 0;
    while (m < i3)
    {
      paramString = paramClass[m];
      int i2 = k;
      int n = i;
      if (Integer.TYPE.equals(paramString.getType()))
      {
        i2 = k;
        n = i;
        if (Modifier.isStatic(paramString.getModifiers()))
        {
          i2 = k;
          n = i;
          if (Modifier.isPublic(paramString.getModifiers()))
          {
            int i1 = paramString.getInt(null);
            i2 = k;
            n = i;
            if ((i1 & 0x7F000000) == 2130706432)
            {
              int j;
              if (i != 0)
              {
                j = i;
                if (i1 >= i);
              }
              else
              {
                j = i1;
              }
              if (k != 0)
              {
                i2 = k;
                n = j;
                if (i1 <= k);
              }
              else
              {
                n = j;
                i2 = i1;
              }
            }
          }
        }
      }
      m += 1;
      k = i2;
      i = n;
    }
    while ((i > 0) && (i <= k))
    {
      paramStringBuilder.append("  <public type=\"");
      paramStringBuilder.append(this.res.getResourceTypeName(i));
      paramStringBuilder.append("\" name=\"");
      paramStringBuilder.append(this.res.getResourceEntryName(i));
      paramStringBuilder.append("\" id=\"0x");
      paramStringBuilder.append(Integer.toHexString(i));
      paramStringBuilder.append("\" />\n");
      i += 1;
    }
  }

  public String buildIds(Class<?> paramClass)
    throws Exception
  {
    ClassLoader localClassLoader = paramClass.getClassLoader();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    localStringBuilder.append("<resources>\n");
    Object localObject = null;
    try
    {
      paramClass = localClassLoader.loadClass(paramClass.getName() + "$id");
      if (paramClass != null)
        buildIds(localStringBuilder, paramClass);
      localStringBuilder.append("</resources>");
      return localStringBuilder.toString();
    }
    catch (ClassNotFoundException paramClass)
    {
      while (true)
        paramClass = localObject;
    }
  }

  public String buildPublic(Class<?> paramClass)
    throws Exception
  {
    ClassLoader localClassLoader = paramClass.getClassLoader();
    String[] arrayOfString = new String[15];
    arrayOfString[0] = "attr";
    arrayOfString[1] = "id";
    arrayOfString[2] = "style";
    arrayOfString[3] = "string";
    arrayOfString[4] = "dimen";
    arrayOfString[5] = "color";
    arrayOfString[6] = "array";
    arrayOfString[7] = "drawable";
    arrayOfString[8] = "layout";
    arrayOfString[9] = "anim";
    arrayOfString[10] = "integer";
    arrayOfString[11] = "animator";
    arrayOfString[12] = "interpolator";
    arrayOfString[13] = "transition";
    arrayOfString[14] = "raw";
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    localStringBuilder.append("<resources>\n");
    int j = arrayOfString.length;
    int i = 0;
    while (true)
    {
      String str;
      Object localObject;
      if (i < j)
      {
        str = arrayOfString[i];
        localObject = null;
      }
      try
      {
        Class localClass = localClassLoader.loadClass(paramClass.getName() + "$" + str);
        localObject = localClass;
        label196: if (localObject == null);
        while (true)
        {
          i += 1;
          break;
          buildPublic(localStringBuilder, localObject, str);
        }
        localStringBuilder.append("</resources>");
        return localStringBuilder.toString();
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        break label196;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.github.mmin18.layoutcast.server.IdProfileBuilder
 * JD-Core Version:    0.6.0
 */