package com.dianping.advertisement;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.dianping.advertisement.common.AsyncTaskQueue;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.widget.view.GAHelper;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdClient
{
  private static List<String> actSupported;
  private static Context context;
  private static final String default_domain = "http://m.api.dianping.com";
  private static final Queue logQueue = new ConcurrentLinkedQueue();
  private static int maxNum;
  private static String oldMergeConfig;
  private static AsyncTaskQueue queue;
  private static int timerInterval;

  static
  {
    actSupported = Arrays.asList(new String[] { "1" });
    timerInterval = 2000;
    maxNum = 20;
    oldMergeConfig = "";
    context = null;
    queue = null;
  }

  private static void config()
  {
    while (true)
    {
      int i;
      synchronized (oldMergeConfig)
      {
        try
        {
          String str2 = ConfigHelper.countMergeConfig;
          if (oldMergeConfig.equals(str2))
            continue;
          Object localObject2 = new JSONObject(str2);
          Object localObject3 = ((JSONObject)localObject2).get("actSupported");
          if (localObject3 == null)
            continue;
          actSupported = new ArrayList();
          localObject3 = (JSONArray)localObject3;
          i = 0;
          if (i >= ((JSONArray)localObject3).length())
            continue;
          if (((JSONArray)localObject3).get(i) != null)
          {
            actSupported.add(((JSONArray)localObject3).get(i).toString());
            break label165;
            localObject3 = ((JSONObject)localObject2).get("timerInterval");
            if (localObject3 == null)
              continue;
            timerInterval = Integer.parseInt(localObject3.toString());
            localObject2 = ((JSONObject)localObject2).get("maxNum");
            if (localObject2 == null)
              continue;
            maxNum = Integer.parseInt(localObject2.toString());
            oldMergeConfig = str2;
            return;
          }
        }
        catch (JSONException localJSONException)
        {
          Log.e("MidasAdClient", localJSONException.getMessage());
          continue;
        }
      }
      label165: i += 1;
    }
  }

  private static AsyncTaskQueue getQueue()
  {
    if (queue == null)
      monitorenter;
    try
    {
      if (queue == null)
      {
        queue = new AsyncTaskQueue();
        queue.start(1);
        initTimer();
      }
      return queue;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  private static void initTimer()
  {
    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new AdClient.1(), 0L, timerInterval, TimeUnit.MILLISECONDS);
  }

  public static boolean initialize(Context paramContext)
  {
    context = paramContext;
    Log.e("MidasAdClient", "AdClient has been initialize before");
    return true;
  }

  private static boolean postCheck(Map<String, String> arg0)
  {
    String str = (String)???.get("act");
    synchronized (oldMergeConfig)
    {
      return (actSupported.contains(str)) && (maxNum > 0);
    }
  }

  public static void report(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      getQueue().push(new AdClient.AdClientTask(paramString));
  }

  public static void report(String paramString, Map<String, String> paramMap)
  {
    if (TextUtils.isEmpty(paramString))
    {
      Log.w("MidasAdClient", "feedback is invalid");
      return;
    }
    Object localObject2;
    Map.Entry localEntry;
    while (true)
    {
      try
      {
        localObject1 = URLDecoder.decode(paramString, "UTF-8");
        paramString = (String)localObject1;
        if (((String)localObject1).contains("?"))
          continue;
        paramString = "http://m.api.dianping.com/mlog/applog.bin?" + (String)localObject1;
        if ((paramMap == null) || (TextUtils.isEmpty(GAHelper.requestId)))
          continue;
        paramMap.put("dpreqid", GAHelper.requestId);
        i = paramString.indexOf("?");
        if (-1 == i)
        {
          localObject1 = "";
          localObject2 = new HashMap();
          paramMap = paramMap.entrySet().iterator();
          if (!paramMap.hasNext())
            break;
          localEntry = (Map.Entry)paramMap.next();
          if (localEntry.getValue() == null)
            continue;
          try
          {
            ((Map)localObject2).put(localEntry.getKey(), URLEncoder.encode((String)localEntry.getValue(), "UTF-8"));
          }
          catch (Exception localException)
          {
            Log.e("MidasAdClient", "parameter '" + (String)localEntry.getKey() + "' value [" + (String)localEntry.getValue() + "] decode failed", localException);
          }
          continue;
        }
      }
      catch (Exception paramString)
      {
        Log.e("MidasAdClient", paramString.getMessage());
        return;
      }
      localObject1 = paramString.substring(0, i);
      localObject2 = paramString.substring(i + 1);
      paramString = (String)localObject1;
      localObject1 = localObject2;
    }
    paramMap = ((String)localObject1).split("&");
    int j = paramMap.length;
    int i = 0;
    if (i < j)
    {
      localObject1 = paramMap[i].split("=");
      if (2 != localObject1.length);
      while (true)
      {
        i += 1;
        break;
        localObject1[0] = localObject1[0].trim();
        if (localObject1[0].length() == 0)
          continue;
        ((Map)localObject2).put(localObject1[0], localObject1[1]);
      }
    }
    paramMap = new StringBuilder();
    i = 0;
    Object localObject1 = ((Map)localObject2).entrySet().iterator();
    if (((Iterator)localObject1).hasNext())
    {
      localEntry = (Map.Entry)((Iterator)localObject1).next();
      if (i != 0)
        paramMap.append("&");
      while (true)
      {
        paramMap.append((String)localEntry.getKey());
        paramMap.append("=");
        paramMap.append((String)localEntry.getValue());
        break;
        i = 1;
      }
    }
    try
    {
      paramString = paramString + "?tokens=" + URLEncoder.encode(paramMap.toString(), "UTF-8");
      if (postCheck((Map)localObject2))
      {
        logQueue.offer(paramMap.toString());
        return;
      }
    }
    catch (java.io.UnsupportedEncodingException paramString)
    {
      Log.e("MidasAdClient", "path '" + paramMap.toString() + "' encode failed", paramString);
      return;
    }
    getQueue().push(new AdClient.AdClientTask(BasicMApiRequest.mapiGet(paramString, CacheType.DISABLED)));
  }

  public static void terminate()
  {
    if (queue != null)
    {
      queue.stop();
      queue = null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.advertisement.AdClient
 * JD-Core Version:    0.6.0
 */