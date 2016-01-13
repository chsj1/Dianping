package com.dianping.moduleconfig;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import com.dianping.archive.DPObject;
import com.dianping.archive.Unarchiver;
import com.dianping.cache.DPCache;
import com.dianping.v1.R.raw;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.List<Ljava.util.ArrayList<Ljava.lang.String;>;>;
import org.json.JSONArray;
import org.json.JSONObject;

public class AgentHelper
{
  private static final String TAG = "AgentHelper";
  private static AgentHelper instance;

  public static AgentHelper getInstance()
  {
    if (instance == null)
      instance = new AgentHelper();
    return instance;
  }

  private String loadFromRaw(Context paramContext)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    InputStream localInputStream = paramContext.getResources().openRawResource(R.raw.shopagentinfo);
    paramContext = (Context)localObject1;
    try
    {
      localObject1 = new byte[localInputStream.available()];
      paramContext = (Context)localObject1;
      localInputStream.read(localObject1);
      paramContext = (Context)localObject1;
      localInputStream.close();
      paramContext = (Context)localObject1;
      label44: localObject1 = new Unarchiver(paramContext);
      paramContext = null;
      try
      {
        localObject1 = ((Unarchiver)localObject1).readDPObject();
        paramContext = (Context)localObject1;
        localObject1 = localObject2;
        if (paramContext != null)
          localObject1 = paramContext.getString("Config");
        return localObject1;
      }
      catch (Exception localException1)
      {
        while (true)
          Log.e("AgentHelper", "loadFromFile failed", localException1);
      }
    }
    catch (Exception localException2)
    {
      break label44;
    }
  }

  public List<ArrayList<String>> getAgentList(Context paramContext, String paramString)
  {
    ArrayList localArrayList1 = new ArrayList();
    Object localObject = loadFromCacheFile();
    try
    {
      localObject = new JSONObject((String)localObject).getJSONArray(paramString);
      i = 0;
      while (i < ((JSONArray)localObject).length())
      {
        ArrayList localArrayList2 = new ArrayList();
        j = 0;
        while (j < ((JSONArray)localObject).getJSONArray(i).length())
        {
          localArrayList2.add(((JSONArray)localObject).getJSONArray(i).get(j));
          j += 1;
        }
        localArrayList1.add(localArrayList2);
        i += 1;
      }
    }
    catch (Exception localException)
    {
      int i;
      int j;
      Log.d("AgentHelper", "getAgentList from cacheFile failed----------" + localException.getMessage());
      localArrayList1.clear();
      paramContext = loadFromRaw(paramContext);
      try
      {
        paramString = new JSONObject(paramContext).getJSONArray(paramString);
        i = 0;
        while (true)
        {
          paramContext = localArrayList1;
          if (i >= paramString.length())
            break;
          paramContext = new ArrayList();
          j = 0;
          while (j < paramString.getJSONArray(i).length())
          {
            paramContext.add(paramString.getJSONArray(i).get(j));
            j += 1;
          }
          localArrayList1.add(paramContext);
          i += 1;
        }
      }
      catch (Exception paramContext)
      {
        Log.d("AgentHelper", "getAgentList failed both----------" + paramContext.getMessage());
        paramContext = null;
      }
      return paramContext;
    }
    Log.d("AgentHelper", "" + localArrayList1.size());
    return (List<ArrayList<String>>)localArrayList1;
  }

  public String loadFromCacheFile()
  {
    String str = DPCache.getInstance().getString("shopinfoagents", null, 31539600000L);
    if (TextUtils.isEmpty(str))
    {
      Log.d("AgentHelper", "loadFromCacheFile failed");
      return str;
    }
    Log.d("AgentHelper", "loadFromCacheFile success");
    return str;
  }

  public void save2CacheFile(String paramString)
  {
    if (DPCache.getInstance().put("shopinfoagents", null, paramString, 31539600000L))
    {
      Log.d("AgentHelper", "save2CacheFile success");
      return;
    }
    Log.d("AgentHelper", "save2CacheFile failed");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.moduleconfig.AgentHelper
 * JD-Core Version:    0.6.0
 */