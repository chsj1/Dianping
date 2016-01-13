package com.umpay.paysdk.meituan;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class a
{
  private static String A;
  private static String B;
  private static ArrayList<Map<String, String>> C;
  private static boolean D;
  private static boolean E;
  public static a a;
  static Context b;
  static r d;
  static w e;
  static Handler f;
  private static String g = null;
  private static String h = null;
  private static long i = 0L;
  private static String j = null;
  private static String k = null;
  private static String l = null;
  private static String m = null;
  private static String n = null;
  private static String o = null;
  private static String p = null;
  private static long q = 0L;
  private static String r = null;
  private static long s = 0L;
  private static String t = null;
  private static int u;
  private static String v;
  private static String w;
  private static String x;
  private static String y;
  private static String z;
  l c;

  static
  {
    a = new a();
    u = 0;
    z = null;
    A = null;
    B = null;
    e = null;
    f = null;
    C = new ArrayList();
    E = false;
  }

  public static a a(Context paramContext, String paramString1, String paramString2)
  {
    b = paramContext;
    y = paramString1;
    x = paramString2;
    if ((e == null) || (!e.isAlive()))
    {
      paramContext = new w("UmpAnalysis");
      e = paramContext;
      paramContext.start();
      f = new Handler(e.getLooper());
    }
    if (f == null)
      f = new Handler(e.getLooper());
    return a;
  }

  private static JSONObject a(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("time", paramString4);
      localJSONObject.put("event_identifier", paramString2);
      if (TextUtils.isEmpty(paramString6))
        localJSONObject.put("terminalid", x);
      while (true)
      {
        localJSONObject.put("appid", y);
        localJSONObject.put("page", paramString3);
        localJSONObject.put("merid", paramString7);
        if (v != null)
          localJSONObject.put("tradeno", paramString5);
        if (paramString1 == null)
          break;
        localJSONObject.put("label", paramString1);
        return localJSONObject;
        localJSONObject.put("terminalid", paramString6);
      }
    }
    catch (JSONException paramString1)
    {
      paramString1.printStackTrace();
    }
    return localJSONObject;
  }

  public static JSONObject a(String paramString1, String paramString2, String paramString3, HashMap<String, String> paramHashMap)
  {
    JSONObject localJSONObject = new JSONObject();
    String str = m.a();
    try
    {
      localJSONObject.put("time", str);
      localJSONObject.put("event_identifier", paramString2);
      localJSONObject.put("terminalid", x);
      localJSONObject.put("appid", y);
      localJSONObject.put("page", paramString3);
      if (v != null)
        localJSONObject.put("tradeno", v);
      if (paramString1 != null)
        localJSONObject.put("label", paramString1);
      if ((paramHashMap != null) && (paramHashMap.size() > 0))
      {
        paramString1 = paramHashMap.entrySet().iterator();
        while (paramString1.hasNext())
        {
          paramString2 = (Map.Entry)paramString1.next();
          localJSONObject.put((String)paramString2.getKey(), paramString2.getValue());
        }
      }
    }
    catch (JSONException paramString1)
    {
      paramString1.printStackTrace();
    }
    return localJSONObject;
  }

  private static JSONObject a(JSONArray paramJSONArray, JSONObject paramJSONObject)
  {
    JSONObject localJSONObject = paramJSONObject;
    if (paramJSONObject == null)
      localJSONObject = new JSONObject();
    if (localJSONObject.has("eventInfo"))
    {
      new JSONArray();
      paramJSONObject = localJSONObject.getJSONArray("eventInfo");
      int i2 = paramJSONObject.length();
      int i1 = 0;
      while (i1 < paramJSONArray.length())
      {
        paramJSONObject.put(i2 + i1, paramJSONArray.get(i1));
        new StringBuilder().append((JSONObject)paramJSONArray.get(i1));
        i1 += 1;
      }
      localJSONObject.put("eventInfo", paramJSONObject);
      return localJSONObject;
    }
    localJSONObject.put("eventInfo", paramJSONArray);
    new StringBuilder("jsonobject").append(localJSONObject);
    return localJSONObject;
  }

  private static JSONObject a(JSONObject paramJSONObject, StringBuffer paramStringBuffer)
  {
    paramStringBuffer = new JSONObject(paramStringBuffer.toString());
    Iterator localIterator = paramJSONObject.keys();
    paramStringBuffer.toString();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      new JSONArray();
      JSONArray localJSONArray1 = paramJSONObject.getJSONArray(str);
      if (paramStringBuffer.has(str))
      {
        new JSONArray();
        JSONArray localJSONArray2 = paramStringBuffer.getJSONArray(str);
        new StringBuilder().append(localJSONArray1);
        if ("locInfo".equals(str))
          localJSONArray2.put(0, localJSONArray1.get(0));
        while (true)
        {
          paramStringBuffer.put(str, localJSONArray2);
          break;
          localJSONArray2.put(localJSONArray2.length(), localJSONArray1.get(0));
        }
      }
      paramStringBuffer.put(str, paramJSONObject.getJSONArray(str));
      new StringBuilder("jsonobject").append(paramStringBuffer);
    }
    return paramStringBuffer;
  }

  public static void a(Context paramContext)
  {
    a(paramContext, true);
  }

  public static void a(Context paramContext, String paramString)
  {
    Object localObject = a;
    r = m.a();
    if (paramString != null)
      m = paramString;
    localObject = m();
    ((JSONObject)localObject).toString();
    new StringBuilder().append(localObject);
    if ((1 == m.b(paramContext)) && (m.a(paramContext)))
    {
      ((JSONObject)localObject).toString();
      paramString = o.a("http://m.soopay.net/ums/log/insert.do/ums/postActivityLog", ((JSONObject)localObject).toString(), new i((JSONObject)localObject, paramContext, paramString));
      a("pageInfo", (JSONObject)localObject, paramContext, true);
      paramContext = paramString.b;
      return;
    }
    a("pageInfo", (JSONObject)localObject, paramContext, true);
  }

  public static void a(Context paramContext, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    a(a, paramContext, v, x, paramString2, paramString3, paramString1, w, paramBoolean);
  }

  public static void a(Context paramContext, String paramString1, String paramString2, boolean paramBoolean)
  {
    a(a, paramContext, v, x, paramString2, "", paramString1, w, paramBoolean);
  }

  public static void a(Context paramContext, boolean paramBoolean)
  {
    if (f == null)
      return;
    paramContext = new k(paramContext, paramBoolean);
    f.post(paramContext);
  }

  private static void a(a parama, Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, boolean paramBoolean)
  {
    try
    {
      if (f == null)
        return;
      parama = new g(parama, paramContext, paramString4, paramString3, paramString5, paramBoolean, paramString2, paramString1, paramString6);
      f.post(parama);
      return;
    }
    catch (Exception parama)
    {
      parama.printStackTrace();
    }
  }

  public static void a(String paramString)
  {
    w = paramString;
  }

  public static void a(String paramString, JSONObject paramJSONObject, Context paramContext)
  {
    a(paramString, paramJSONObject, paramContext, true);
  }

  public static void a(String paramString, JSONObject paramJSONObject, Context paramContext, boolean paramBoolean)
  {
    JSONArray localJSONArray = new JSONArray();
    try
    {
      localJSONArray.put(0, paramJSONObject);
      new StringBuilder("saveInfo").append(paramJSONObject.toString());
      paramJSONObject = new JSONObject();
      paramJSONObject.put(paramString, localJSONArray);
      if (paramBoolean)
      {
        if (f == null)
          return;
        f.post(new f(paramContext, paramJSONObject));
        return;
      }
    }
    catch (JSONException paramString)
    {
      paramString.printStackTrace();
      return;
    }
    b(paramContext, paramJSONObject);
  }

  public static boolean a(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    HashMap localHashMap = new HashMap(1);
    localHashMap.put("terminalid", paramString1);
    localHashMap.put("tradeno", paramString2);
    localHashMap.put("event_identifier", paramString3);
    localHashMap.put("label", paramString4);
    localHashMap.put("time", paramString5);
    localHashMap.put("merid", paramString6);
    new StringBuilder().append(Thread.currentThread().getId()).append("插入一条数据到缓存：").append(paramString1).append(" ").append(paramString2).append(" ").append(paramString3).append(" ").append(paramString4).append(" ").append(paramString5).append(" ").append(paramString6);
    C.add(localHashMap);
    return true;
  }

  public static String b()
  {
    return y;
  }

  public static void b(Context paramContext, String paramString)
  {
    h = m.a();
    if (paramString.lastIndexOf(".") == -1);
    for (int i1 = 0; ; i1 = paramString.lastIndexOf("."))
    {
      m = paramString.substring(i1);
      paramString = l();
      new StringBuilder().append(paramString);
      if ((1 != m.b(paramContext)) || (!m.a(paramContext)))
        break;
      paramString.toString();
      o.a("http://m.soopay.net/ums/log/insert.do/ums/postActivityLog", paramString.toString(), new j(paramString, paramContext));
      return;
    }
    a("pageInfo", paramString, paramContext, true);
  }

  private static void b(Context paramContext, JSONObject paramJSONObject)
  {
    new StringBuilder("子线程").append(Thread.currentThread().getId()).append("  ").append(Thread.currentThread().getName());
    c(paramContext, paramJSONObject);
  }

  private static void b(Context paramContext, boolean paramBoolean)
  {
    try
    {
      new StringBuilder("有无线程在上传").append(D);
      if (D)
        return;
      localFile = new File(B);
      localObject1 = new StringBuffer();
      Object localObject2;
      Object localObject3;
      if (localFile.exists())
      {
        localObject2 = new BufferedReader(new InputStreamReader(paramContext.openFileInput(A)));
        do
        {
          localObject3 = ((BufferedReader)localObject2).readLine();
          if ((localObject3 == null) || ("".equals(localObject3)))
            continue;
          ((StringBuffer)localObject1).append((String)localObject3);
        }
        while ((localObject3 != null) && (!"".equals(localObject3)));
        ((BufferedReader)localObject2).close();
        localObject1 = new JSONObject(((StringBuffer)localObject1).toString());
        new StringBuilder("其他事件JSON：").append(((JSONObject)localObject1).toString());
      }
      JSONArray localJSONArray;
      int i1;
      Object localObject4;
      int i2;
      String str1;
      String str2;
      String str3;
      String str4;
      while (true)
      {
        localJSONArray = new JSONArray();
        i1 = 0;
        new StringBuilder("线程").append(Thread.currentThread().getId()).append("准备对数据库进行操作");
        localObject4 = v.a(paramContext).c();
        i2 = ((Cursor)localObject4).getColumnCount();
        while (((Cursor)localObject4).moveToNext())
        {
          str1 = ((Cursor)localObject4).getString(((Cursor)localObject4).getColumnIndex("event_identifier"));
          str2 = ((Cursor)localObject4).getString(((Cursor)localObject4).getColumnIndex("label"));
          str3 = ((Cursor)localObject4).getString(((Cursor)localObject4).getColumnIndex("terminalid"));
          str4 = ((Cursor)localObject4).getString(((Cursor)localObject4).getColumnIndex("time"));
          String str5 = ((Cursor)localObject4).getString(((Cursor)localObject4).getColumnIndex("tradeno"));
          localObject3 = ((Cursor)localObject4).getString(((Cursor)localObject4).getColumnIndex("merid"));
          localObject2 = localObject3;
          if (TextUtils.isEmpty((CharSequence)localObject3))
            localObject2 = w;
          localObject2 = a(str2, str1, "", str4, str5, str3, (String)localObject2);
          new StringBuilder("线程").append(Thread.currentThread().getId()).append("从数据库中取出一条数据:").append(((JSONObject)localObject2).toString());
          localJSONArray.put(i1, localObject2);
          i1 += 1;
        }
        localObject1 = new JSONObject();
      }
      ((Cursor)localObject4).close();
      if (i2 > 0)
        v.a(b).b();
      if ((C != null) && (C.size() > 0))
      {
        localObject2 = C.iterator();
        while (((Iterator)localObject2).hasNext())
        {
          localObject3 = (Map)((Iterator)localObject2).next();
          localObject4 = (String)((Map)localObject3).get("terminalid");
          str1 = (String)((Map)localObject3).get("event_identifier");
          str2 = (String)((Map)localObject3).get("label");
          str3 = (String)((Map)localObject3).get("time");
          str4 = (String)((Map)localObject3).get("merid");
          localObject3 = a(str2, str1, "", str3, (String)((Map)localObject3).get("tradeno"), (String)localObject4, str4);
          new StringBuilder("从缓存中取出一条数据:").append(((JSONObject)localObject3).toString());
          localJSONArray.put(i1, localObject3);
          i1 += 1;
        }
      }
      if (localJSONArray.length() > 0)
        a(localJSONArray, (JSONObject)localObject1);
      D = true;
      if ((!m.a(paramContext)) || (((JSONObject)localObject1).length() <= 0))
        break label761;
      if (paramBoolean)
      {
        o.a("http://m.soopay.net/ums/log/insert.do", String.valueOf(localObject1), new d(localFile));
        return;
      }
    }
    catch (FileNotFoundException paramContext)
    {
      Object localObject1;
      paramContext.printStackTrace();
      return;
      paramContext = o.a("http://m.soopay.net/ums/log/insert.do", String.valueOf(localObject1));
      if (paramContext.a)
        if (!"0000".equals((String)new JSONObject(paramContext.b).get("retCode")))
        {
          o();
          return;
        }
    }
    catch (IOException paramContext)
    {
      File localFile;
      paramContext.printStackTrace();
      return;
      b(localFile);
      return;
    }
    catch (JSONException paramContext)
    {
      paramContext.printStackTrace();
      return;
    }
    o();
    return;
    label761: o();
  }

  private static void b(File paramFile)
  {
    paramFile.delete();
    v.a(b).a();
    D = false;
    if (E)
      g();
  }

  public static void b(String paramString)
  {
    v = paramString;
  }

  public static String c()
  {
    return x;
  }

  private static void c(Context paramContext, JSONObject paramJSONObject)
  {
    String str;
    try
    {
      localObject1 = new File(B);
      if (((File)localObject1).exists())
      {
        ((File)localObject1).getAbsolutePath();
        localObject1 = new BufferedReader(new InputStreamReader(paramContext.openFileInput(A)));
        localObject2 = new StringBuffer();
        do
        {
          str = ((BufferedReader)localObject1).readLine();
          if ((str == null) || ("".equals(str)))
            continue;
          ((StringBuffer)localObject2).append(str);
        }
        while ((str != null) && (!"".equals(str)));
        ((BufferedReader)localObject1).close();
        if (((StringBuffer)localObject2).length() == 0)
          break label180;
        paramJSONObject = a(paramJSONObject, (StringBuffer)localObject2);
        paramContext = new PrintWriter(paramContext.openFileOutput(A, 0));
        paramContext.write(paramJSONObject.toString());
        paramContext.flush();
        paramContext.close();
      }
    }
    catch (IOException paramContext)
    {
      try
      {
        while (true)
        {
          Thread.sleep(100L);
          return;
          ((File)localObject1).createNewFile();
        }
        paramContext = paramContext;
        paramContext.printStackTrace();
        return;
      }
      catch (InterruptedException paramContext)
      {
        paramContext.printStackTrace();
        return;
      }
    }
    catch (JSONException paramContext)
    {
      paramContext.printStackTrace();
      return;
    }
    label180: Object localObject2 = paramJSONObject.keys();
    Object localObject1 = new JSONObject();
    while (((Iterator)localObject2).hasNext())
    {
      str = (String)((Iterator)localObject2).next();
      ((JSONObject)localObject1).put(str, paramJSONObject.getJSONArray(str));
    }
    paramContext = new PrintWriter(paramContext.openFileOutput(A, 0));
    paramContext.write(((JSONObject)localObject1).toString());
    paramContext.flush();
    paramContext.close();
  }

  public static void c(String paramString)
  {
    x = paramString;
  }

  public static String d()
  {
    return m;
  }

  public static void d(String paramString)
  {
    m = paramString;
    p = m.a();
  }

  public static void e()
  {
    d.b();
  }

  public static void f()
  {
    g = m.a();
  }

  public static void g()
  {
    if (D)
    {
      E = true;
      return;
    }
    if ((f != null) && (f.getLooper() != null))
    {
      f.getLooper().quit();
      f = null;
      e = null;
    }
    new Thread(new c()).start();
  }

  public static void h()
  {
    v.a(b).d();
  }

  private static JSONObject l()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("terminalid", x);
      localJSONObject.put("tradeno", v);
      localJSONObject.put("appid", y);
      localJSONObject.put("start_millis", g);
      localJSONObject.put("end_millis", h);
      localJSONObject.put("page", m);
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
    return localJSONObject;
  }

  private static JSONObject m()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("terminalid", x);
      localJSONObject.put("tradeno", v);
      localJSONObject.put("appid", y);
      localJSONObject.put("start_millis", p);
      localJSONObject.put("end_millis", r);
      localJSONObject.put("page", m);
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
    return localJSONObject;
  }

  private static JSONObject n()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("appid", y);
      localJSONObject.put("terminalid", x);
      localJSONObject.put("tradeno", v);
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
    return localJSONObject;
  }

  private static void o()
  {
    D = false;
    if (E)
      g();
  }

  public final void a()
  {
    E = false;
    d = new r(b);
    this.c = new l(this);
    d.a(this.c);
    n localn = n.a();
    localn.a(b);
    Thread.currentThread();
    Thread.setDefaultUncaughtExceptionHandler(localn);
    z = b.getFilesDir().getAbsolutePath() + "/";
    A = "mobclick_agent_cached_" + b.getPackageName();
    B = z + A;
    u = 0;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.umpay.paysdk.meituan.a
 * JD-Core Version:    0.6.0
 */