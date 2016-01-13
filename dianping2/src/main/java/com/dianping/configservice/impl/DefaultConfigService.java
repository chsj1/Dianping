package com.dianping.configservice.impl;

import android.content.Context;
import android.os.Looper;
import com.dianping.configservice.ConfigChangeListener;
import com.dianping.configservice.ConfigService;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import org.json.JSONObject;

public abstract class DefaultConfigService
  implements ConfigService, RequestHandler<MApiRequest, MApiResponse>
{
  private Context context;
  private JSONObject dump;
  private HashMap<String, ArrayList<ConfigChangeListener>> listeners;
  private MApiService mapiService;
  private MApiRequest request;

  public DefaultConfigService(Context paramContext, MApiService paramMApiService)
  {
    this.context = paramContext;
    this.mapiService = paramMApiService;
    this.listeners = new HashMap();
  }

  private File getConfigFile()
  {
    return new File(getConfigDir(), "1");
  }

  private JSONObject read()
  {
    Object localObject1 = getConfigFile();
    Object localObject2;
    if (!((File)localObject1).exists())
    {
      localObject2 = new File(this.context.getFilesDir(), "KFSDF09D0234GDSDSYERRA");
      if (((File)localObject2).exists())
        ((File)localObject2).renameTo((File)localObject1);
    }
    if (((File)localObject1).exists())
      try
      {
        localObject1 = new FileInputStream((File)localObject1);
        if (((FileInputStream)localObject1).available() > 1000000)
          throw new IOException();
        localObject2 = new byte[((FileInputStream)localObject1).available()];
        ((FileInputStream)localObject1).read(localObject2);
        ((FileInputStream)localObject1).close();
        localObject1 = new JSONObject(new String(localObject2, "UTF-8"));
        return localObject1;
      }
      catch (Exception localException)
      {
      }
    return (JSONObject)(JSONObject)null;
  }

  private boolean write(JSONObject paramJSONObject, File paramFile)
  {
    try
    {
      paramFile = new FileOutputStream(paramFile);
      paramFile.write(paramJSONObject.toString().getBytes("UTF-8"));
      paramFile.close();
      return true;
    }
    catch (Exception paramJSONObject)
    {
    }
    return false;
  }

  public void addListener(String paramString, ConfigChangeListener paramConfigChangeListener)
  {
    synchronized (this.listeners)
    {
      ArrayList localArrayList2 = (ArrayList)this.listeners.get(paramString);
      ArrayList localArrayList1 = localArrayList2;
      if (localArrayList2 == null)
      {
        localArrayList1 = new ArrayList();
        this.listeners.put(paramString, localArrayList1);
      }
      localArrayList1.add(paramConfigChangeListener);
      return;
    }
  }

  protected abstract MApiRequest createRequest();

  public JSONObject dump()
  {
    if (this.dump == null)
    {
      JSONObject localJSONObject2 = read();
      JSONObject localJSONObject1 = localJSONObject2;
      if (localJSONObject2 == null)
        localJSONObject1 = new JSONObject();
      this.dump = localJSONObject1;
    }
    return this.dump;
  }

  protected File getConfigDir()
  {
    File localFile = new File(this.context.getFilesDir(), "config");
    if (!localFile.isDirectory())
    {
      localFile.delete();
      localFile.mkdir();
    }
    return localFile;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    Log.i("config", "fail to refresh config from " + paramMApiRequest);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof String))
    {
      paramMApiResponse = (String)paramMApiResponse.result();
      try
      {
        setConfig(new JSONObject(paramMApiResponse));
        return;
      }
      catch (Exception paramMApiResponse)
      {
        Log.w("config", "result from " + paramMApiRequest + " is not a json object", paramMApiResponse);
        return;
      }
    }
    Log.w("config", "result from " + paramMApiRequest + " is not a string");
  }

  public void refresh()
  {
    if (this.request != null)
      this.mapiService.abort(this.request, this, true);
    this.request = createRequest();
    this.mapiService.exec(this.request, this);
  }

  public void removeListener(String paramString, ConfigChangeListener paramConfigChangeListener)
  {
    synchronized (this.listeners)
    {
      ArrayList localArrayList = (ArrayList)this.listeners.get(paramString);
      if (localArrayList != null)
      {
        localArrayList.remove(paramConfigChangeListener);
        if (localArrayList.isEmpty())
          this.listeners.remove(paramString);
      }
      return;
    }
  }

  public void setConfig(JSONObject paramJSONObject)
  {
    label4: Object localObject1;
    Object localObject2;
    if (paramJSONObject == null)
    {
      return;
    }
    else
    {
      while (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId())
      {
        Log.w("config", "setConfig must be run under main thread");
        if (Log.LEVEL >= 2147483647)
          continue;
        throw new RuntimeException("setConfig must be run under main thread");
      }
      localObject1 = new File(getConfigDir(), new Random(System.currentTimeMillis()).nextInt() + ".tmp");
      if (!write(paramJSONObject, (File)localObject1))
      {
        Log.w("config", "fail to write config to " + localObject1);
        return;
      }
      if (!((File)localObject1).renameTo(getConfigFile()))
      {
        Log.w("config", "fail to move config file " + localObject1);
        return;
      }
      localObject1 = this.dump;
      this.dump = paramJSONObject;
      localObject2 = (ArrayList)this.listeners.get("*");
      if (localObject2 != null)
      {
        localObject2 = ((ArrayList)localObject2).iterator();
        while (((Iterator)localObject2).hasNext())
          ((ConfigChangeListener)((Iterator)localObject2).next()).onConfigChange("*", localObject1, paramJSONObject);
      }
      localObject2 = this.listeners.entrySet().iterator();
    }
    label434: 
    while (true)
    {
      if (!((Iterator)localObject2).hasNext())
        break label4;
      Object localObject5 = (Map.Entry)((Iterator)localObject2).next();
      String str = (String)((Map.Entry)localObject5).getKey();
      if ("*".equals(str))
        break;
      Object localObject3 = ((JSONObject)localObject1).opt(str);
      Object localObject4 = paramJSONObject.opt(str);
      boolean bool;
      if (localObject3 == null)
        if (localObject4 == null)
          bool = true;
      while (true)
      {
        if (bool)
          break label434;
        localObject5 = (ArrayList)((Map.Entry)localObject5).getValue();
        Log.i("config", "config changed, " + str + " has " + ((ArrayList)localObject5).size() + " listeners");
        localObject5 = ((ArrayList)localObject5).iterator();
        while (((Iterator)localObject5).hasNext())
          ((ConfigChangeListener)((Iterator)localObject5).next()).onConfigChange(str, localObject3, localObject4);
        break;
        bool = false;
        continue;
        bool = localObject3.equals(localObject4);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.configservice.impl.DefaultConfigService
 * JD-Core Version:    0.6.0
 */