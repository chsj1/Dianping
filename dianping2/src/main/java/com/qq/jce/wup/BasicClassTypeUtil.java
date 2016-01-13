package com.qq.jce.wup;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BasicClassTypeUtil
{
  private static ClassLoader mClassLoader = null;
  private static boolean mClassLoaderInitialize = true;

  private static void addType(ArrayList<String> paramArrayList, String paramString)
  {
    int i = paramString.length();
    int j;
    do
    {
      j = i;
      if (paramString.charAt(i - 1) != '>')
        break;
      j = i - 1;
      i = j;
    }
    while (j != 0);
    paramArrayList.add(0, uni2JavaType(paramString.substring(0, j)));
  }

  public static Object createClassByName(String paramString, boolean paramBoolean, ClassLoader paramClassLoader)
    throws ObjectCreateException
  {
    Object localObject;
    if (paramString.equals("java.lang.Integer"))
      localObject = Integer.valueOf(0);
    do
    {
      return localObject;
      if (paramString.equals("java.lang.Boolean"))
        return Boolean.valueOf(false);
      if (paramString.equals("java.lang.Byte"))
        return Byte.valueOf(0);
      if (paramString.equals("java.lang.Double"))
        return Double.valueOf(0.0D);
      if (paramString.equals("java.lang.Float"))
        return Float.valueOf(0.0F);
      if (paramString.equals("java.lang.Long"))
        return Long.valueOf(0L);
      if (paramString.equals("java.lang.Short"))
        return Short.valueOf(0);
      if (paramString.equals("java.lang.Character"))
        throw new IllegalArgumentException("can not support java.lang.Character");
      if (paramString.equals("java.lang.String"))
        return "";
      if (paramString.equals("java.util.List"))
        return new ArrayList();
      if (paramString.equals("java.util.Map"))
        return new HashMap();
      if (paramString.equals("Array"))
        return "Array";
      localObject = paramString;
    }
    while (paramString.equals("?"));
    if (paramClassLoader != null);
    try
    {
      paramString = Class.forName(paramString, paramBoolean, paramClassLoader);
      while (true)
      {
        return paramString.getConstructor(new Class[0]).newInstance(new Object[0]);
        if (mClassLoader != null)
        {
          paramString = Class.forName(paramString, mClassLoaderInitialize, mClassLoader);
          continue;
        }
        paramString = Class.forName(paramString);
      }
    }
    catch (Exception paramString)
    {
      paramString.printStackTrace();
    }
    throw new ObjectCreateException(paramString);
  }

  public static Object createClassByUni(String paramString, boolean paramBoolean, ClassLoader paramClassLoader)
    throws ObjectCreateException
  {
    paramString = getTypeList(paramString);
    Object localObject2 = null;
    Iterator localIterator = paramString.iterator();
    paramString = null;
    Object localObject1 = null;
    Object localObject3;
    if (localIterator.hasNext())
    {
      localObject2 = createClassByName((String)localIterator.next(), paramBoolean, paramClassLoader);
      if ((localObject2 instanceof String))
        if ("Array".equals((String)(String)localObject2))
        {
          if (localObject1 != null)
            break label316;
          localObject2 = Array.newInstance(Byte.class, 0);
          localObject3 = localObject1;
          localObject1 = paramString;
          paramString = (String)localObject3;
        }
    }
    while (true)
    {
      localObject3 = localObject1;
      localObject1 = paramString;
      paramString = (String)localObject3;
      break;
      if ("?".equals((String)(String)localObject2))
      {
        localObject3 = paramString;
        paramString = (String)localObject1;
        localObject1 = localObject3;
        continue;
      }
      if (localObject1 == null)
      {
        localObject1 = localObject2;
        localObject3 = paramString;
        paramString = (String)localObject1;
        localObject1 = localObject3;
        continue;
      }
      paramString = (String)localObject2;
      continue;
      if ((localObject2 instanceof List))
      {
        if ((localObject1 != null) && ((localObject1 instanceof Byte)))
        {
          localObject3 = Array.newInstance(Byte.class, 1);
          Array.set(localObject3, 0, localObject1);
          localObject2 = paramString;
          paramString = (String)localObject1;
          localObject1 = localObject2;
          localObject2 = localObject3;
          continue;
        }
        if (localObject1 != null)
          ((List)localObject2).add(localObject1);
        localObject3 = null;
        localObject1 = paramString;
        paramString = (String)localObject3;
        continue;
      }
      if ((localObject2 instanceof Map))
      {
        int i;
        if (localObject1 != null)
        {
          i = 1;
          label242: if (paramString == null)
            break label283;
        }
        label283: for (int j = 1; ; j = 0)
        {
          if ((j & i) != 0)
            ((Map)localObject2).put(localObject1, paramString);
          paramString = null;
          localObject1 = null;
          break;
          i = 0;
          break label242;
        }
      }
      if (localObject1 == null)
      {
        localObject1 = localObject2;
        localObject3 = paramString;
        paramString = (String)localObject1;
        localObject1 = localObject3;
        continue;
      }
      paramString = (String)localObject2;
      continue;
      return localObject2;
      label316: localObject3 = paramString;
      paramString = (String)localObject1;
      localObject1 = localObject3;
    }
  }

  public static String getClassTransName(String paramString)
  {
    String str;
    if (paramString.equals("int"))
      str = "Integer";
    do
    {
      return str;
      if (paramString.equals("boolean"))
        return "Boolean";
      if (paramString.equals("byte"))
        return "Byte";
      if (paramString.equals("double"))
        return "Double";
      if (paramString.equals("float"))
        return "Float";
      if (paramString.equals("long"))
        return "Long";
      if (paramString.equals("short"))
        return "Short";
      str = paramString;
    }
    while (!paramString.equals("char"));
    return "Character";
  }

  public static ArrayList<String> getTypeList(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    int m = 0;
    int i = paramString.indexOf("<");
    while (m < i)
    {
      addType(localArrayList, paramString.substring(m, i));
      int n = i + 1;
      i = paramString.indexOf("<", n);
      int k = paramString.indexOf(",", n);
      int j = i;
      if (i == -1)
        j = k;
      m = n;
      i = j;
      if (k == -1)
        continue;
      m = n;
      i = j;
      if (k >= j)
        continue;
      i = k;
      m = n;
    }
    addType(localArrayList, paramString.substring(m, paramString.length()));
    return localArrayList;
  }

  public static String getVariableInit(String paramString1, String paramString2)
  {
    if (paramString2.equals("int"))
      return paramString2 + " " + paramString1 + "=0 ;\n";
    if (paramString2.equals("boolean"))
      return paramString2 + " " + paramString1 + "=false ;\n";
    if (paramString2.equals("byte"))
      return paramString2 + " " + paramString1 + " ;\n";
    if (paramString2.equals("double"))
      return paramString2 + " " + paramString1 + "=0 ;\n";
    if (paramString2.equals("float"))
      return paramString2 + " " + paramString1 + "=0 ;\n";
    if (paramString2.equals("long"))
      return paramString2 + " " + paramString1 + "=0 ;\n";
    if (paramString2.equals("short"))
      return paramString2 + " " + paramString1 + "=0 ;\n";
    if (paramString2.equals("char"))
      return paramString2 + " " + paramString1 + " ;\n";
    return paramString2 + " " + paramString1 + " = null ;\n";
  }

  public static boolean isBasicType(String paramString)
  {
    if (paramString.equals("int"));
    do
      return true;
    while ((paramString.equals("boolean")) || (paramString.equals("byte")) || (paramString.equals("double")) || (paramString.equals("float")) || (paramString.equals("long")) || (paramString.equals("short")) || (paramString.equals("char")) || (paramString.equals("Integer")) || (paramString.equals("Boolean")) || (paramString.equals("Byte")) || (paramString.equals("Double")) || (paramString.equals("Float")) || (paramString.equals("Long")) || (paramString.equals("Short")) || (paramString.equals("Char")));
    return false;
  }

  public static String java2UniType(String paramString)
  {
    String str;
    if ((paramString.equals("java.lang.Integer")) || (paramString.equals("int")))
      str = "int32";
    do
    {
      return str;
      if ((paramString.equals("java.lang.Boolean")) || (paramString.equals("boolean")))
        return "bool";
      if ((paramString.equals("java.lang.Byte")) || (paramString.equals("byte")))
        return "char";
      if ((paramString.equals("java.lang.Double")) || (paramString.equals("double")))
        return "double";
      if ((paramString.equals("java.lang.Float")) || (paramString.equals("float")))
        return "float";
      if ((paramString.equals("java.lang.Long")) || (paramString.equals("long")))
        return "int64";
      if ((paramString.equals("java.lang.Short")) || (paramString.equals("short")))
        return "short";
      if (paramString.equals("java.lang.Character"))
        throw new IllegalArgumentException("can not support java.lang.Character");
      if (paramString.equals("java.lang.String"))
        return "string";
      if (paramString.equals("java.util.List"))
        return "list";
      str = paramString;
    }
    while (!paramString.equals("java.util.Map"));
    return "map";
  }

  public static void main(String[] paramArrayOfString)
  {
    paramArrayOfString = new ArrayList();
    paramArrayOfString.add("char");
    paramArrayOfString.add("list<char>");
    paramArrayOfString.add("list<list<char>>");
    paramArrayOfString.add("map<short,string>");
    paramArrayOfString.add("map<double,map<float,list<bool>>>");
    paramArrayOfString.add("map<int64,list<Test.UserInfo>>");
    paramArrayOfString.add("map<short,Test.FriendInfo>");
    paramArrayOfString = paramArrayOfString.iterator();
    while (paramArrayOfString.hasNext())
    {
      ArrayList localArrayList = getTypeList((String)paramArrayOfString.next());
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        System.out.println(str);
      }
      Collections.reverse(localArrayList);
      System.out.println("-------------finished " + transTypeList(localArrayList));
    }
  }

  public static void setClassLoader(boolean paramBoolean, ClassLoader paramClassLoader)
  {
    mClassLoaderInitialize = paramBoolean;
    mClassLoader = paramClassLoader;
  }

  public static String transTypeList(ArrayList<String> paramArrayList)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    while (i < paramArrayList.size())
    {
      paramArrayList.set(i, java2UniType((String)paramArrayList.get(i)));
      i += 1;
    }
    Collections.reverse(paramArrayList);
    i = 0;
    if (i < paramArrayList.size())
    {
      String str = (String)paramArrayList.get(i);
      if (str.equals("list"))
      {
        paramArrayList.set(i - 1, "<" + (String)paramArrayList.get(i - 1));
        paramArrayList.set(0, (String)paramArrayList.get(0) + ">");
      }
      while (true)
      {
        i += 1;
        break;
        if (str.equals("map"))
        {
          paramArrayList.set(i - 1, "<" + (String)paramArrayList.get(i - 1) + ",");
          paramArrayList.set(0, (String)paramArrayList.get(0) + ">");
          continue;
        }
        if (!str.equals("Array"))
          continue;
        paramArrayList.set(i - 1, "<" + (String)paramArrayList.get(i - 1));
        paramArrayList.set(0, (String)paramArrayList.get(0) + ">");
      }
    }
    Collections.reverse(paramArrayList);
    paramArrayList = paramArrayList.iterator();
    while (paramArrayList.hasNext())
      localStringBuffer.append((String)paramArrayList.next());
    return localStringBuffer.toString();
  }

  public static String uni2JavaType(String paramString)
  {
    String str;
    if (paramString.equals("int32"))
      str = "java.lang.Integer";
    do
    {
      return str;
      if (paramString.equals("bool"))
        return "java.lang.Boolean";
      if (paramString.equals("char"))
        return "java.lang.Byte";
      if (paramString.equals("double"))
        return "java.lang.Double";
      if (paramString.equals("float"))
        return "java.lang.Float";
      if (paramString.equals("int64"))
        return "java.lang.Long";
      if (paramString.equals("short"))
        return "java.lang.Short";
      if (paramString.equals("string"))
        return "java.lang.String";
      if (paramString.equals("list"))
        return "java.util.List";
      str = paramString;
    }
    while (!paramString.equals("map"));
    return "java.util.Map";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.qq.jce.wup.BasicClassTypeUtil
 * JD-Core Version:    0.6.0
 */