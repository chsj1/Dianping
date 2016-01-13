package com.dianping.loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.text.TextUtils;
import com.dianping.app.Environment;
import com.dianping.loader.model.FileSpec;
import com.dianping.loader.model.FragmentSpec;
import com.dianping.loader.model.SiteSpec;
import com.dianping.util.Log;
import com.dianping.util.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RepositoryManager
{
  static final String STATUS_DONE = "DONE";
  static final String STATUS_IDLE = "IDLE";
  static final String STATUS_RUNNING = "RUNNING";
  private final ConnectivityManager connManager;
  private final Context context;
  private final ArrayList<StatusChangeListener> listeners = new ArrayList();
  private final HashMap<String, FileSpec> map = new HashMap();
  private final LinkedList<FileSpec> order = new LinkedList();
  private final File repoDir;
  private final HashMap<String, Integer> require = new HashMap();
  private Worker running;
  private final HashMap<String, String> status = new HashMap();
  private final File tmpDir;

  public RepositoryManager(Context paramContext)
  {
    this.context = paramContext;
    Object localObject = null;
    try
    {
      ConnectivityManager localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
      localObject = localConnectivityManager;
      this.connManager = localObject;
      this.repoDir = new File(paramContext.getFilesDir(), "repo");
      this.repoDir.mkdir();
      this.tmpDir = new File(this.repoDir, "tmp");
      this.tmpDir.mkdir();
      paramContext = this.tmpDir.listFiles();
      if (paramContext != null)
      {
        int j = paramContext.length;
        int i = 0;
        while (i < j)
        {
          paramContext[i].delete();
          i += 1;
        }
      }
    }
    catch (Exception localException)
    {
      while (true)
        Log.w("loader", "repository manager start without connectivity manager", localException);
      disableConnectionReuseIfNecessary();
    }
  }

  public static boolean appendDepsList(HashMap<String, FileSpec> paramHashMap, List<FileSpec> paramList, String paramString)
  {
    paramString = (FileSpec)paramHashMap.get(paramString);
    if (paramString == null);
    label74: 
    do
    {
      return false;
      if (paramList.contains(paramString))
        return true;
      if (paramString.deps() == null)
        continue;
      String[] arrayOfString = paramString.deps();
      int j = arrayOfString.length;
      int i = 0;
      while (true)
      {
        if (i >= j)
          break label74;
        if (!appendDepsList(paramHashMap, paramList, arrayOfString[i]))
          break;
        i += 1;
      }
    }
    while (paramList.contains(paramString));
    paramList.add(paramString);
    return true;
  }

  private void disableConnectionReuseIfNecessary()
  {
    if (Integer.parseInt(Build.VERSION.SDK) < 8)
      System.setProperty("http.keepAlive", "false");
  }

  private FileSpec pickFromQueue()
  {
    monitorenter;
    int i = -1;
    try
    {
      Iterator localIterator = this.order.iterator();
      FileSpec localFileSpec;
      Object localObject1;
      int j;
      while (true)
        if (localIterator.hasNext())
        {
          localFileSpec = (FileSpec)localIterator.next();
          if (getStatus(localFileSpec.id()) != "IDLE")
            continue;
          localObject1 = (Integer)this.require.get(localFileSpec.id());
          if (localObject1 == null)
            break;
          j = ((Integer)localObject1).intValue();
          if (j <= 0)
            break;
          localObject1 = localFileSpec;
        }
      while (true)
      {
        return localObject1;
        localObject1 = localFileSpec;
        if (localFileSpec.down() >= 5)
          continue;
        if (localFileSpec.down() <= 0)
          break;
        j = i;
        if (i < 0)
          j = getNetworkType();
        i = localFileSpec.down();
        switch (i)
        {
        default:
          i = j;
          break;
        case 1:
          i = j;
          if (j <= 3)
            break;
          localObject1 = localFileSpec;
          break;
        case 2:
          i = j;
          if (j <= 2)
            break;
          localObject1 = localFileSpec;
          continue;
          localObject1 = null;
        }
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject2;
  }

  public void addFiles(SiteSpec paramSiteSpec)
  {
    monitorenter;
    try
    {
      FileSpec[] arrayOfFileSpec = paramSiteSpec.files();
      int j = arrayOfFileSpec.length;
      int i = 0;
      while (i < j)
      {
        FileSpec localFileSpec = arrayOfFileSpec[i];
        this.map.put(localFileSpec.id(), localFileSpec);
        i += 1;
      }
      paramSiteSpec = paramSiteSpec.fragments(Environment.versionCode());
      j = paramSiteSpec.length;
      i = 0;
      while (i < j)
      {
        arrayOfFileSpec = paramSiteSpec[i];
        if (arrayOfFileSpec.code() != null)
          appendDepsList(this.map, this.order, arrayOfFileSpec.code());
        i += 1;
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw paramSiteSpec;
  }

  @Deprecated
  public void addFiles(FileSpec[] paramArrayOfFileSpec)
  {
    monitorenter;
    try
    {
      int j = paramArrayOfFileSpec.length;
      int i = 0;
      FileSpec localFileSpec;
      while (i < j)
      {
        localFileSpec = paramArrayOfFileSpec[i];
        this.map.put(localFileSpec.id(), localFileSpec);
        i += 1;
      }
      j = paramArrayOfFileSpec.length;
      i = 0;
      while (i < j)
      {
        localFileSpec = paramArrayOfFileSpec[i];
        appendDepsList(this.map, this.order, localFileSpec.id());
        i += 1;
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw paramArrayOfFileSpec;
  }

  void addListener(StatusChangeListener paramStatusChangeListener)
  {
    this.listeners.add(paramStatusChangeListener);
  }

  public boolean appendDepsList(List<FileSpec> paramList, String paramString)
  {
    return appendDepsList(this.map, paramList, paramString);
  }

  public void dismiss(FileSpec[] paramArrayOfFileSpec)
  {
    monitorenter;
    try
    {
      int j = paramArrayOfFileSpec.length;
      int i = 0;
      while (i < j)
      {
        FileSpec localFileSpec = paramArrayOfFileSpec[i];
        Integer localInteger = (Integer)this.require.get(localFileSpec.id());
        if ((localInteger != null) && (localInteger.intValue() > 0))
          this.require.put(localFileSpec.id(), Integer.valueOf(localInteger.intValue() - 1));
        i += 1;
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw paramArrayOfFileSpec;
  }

  File getDir()
  {
    return this.repoDir;
  }

  int getNetworkType()
  {
    int i = 1;
    try
    {
      NetworkInfo localNetworkInfo = this.connManager.getActiveNetworkInfo();
      if (localNetworkInfo.getType() == 0);
      switch (localNetworkInfo.getSubtype())
      {
      case 3:
        i = localNetworkInfo.getType();
        if (i == 1)
          return 3;
      case 5:
      case 6:
      case 8:
      case 9:
      case 10:
      default:
      case 1:
      case 2:
      case 4:
      case 7:
      case 11:
      }
    }
    catch (Exception localException)
    {
    }
    return 0;
    i = 2;
    return i;
  }

  public File getPath(FileSpec paramFileSpec)
  {
    File localFile = new File(this.repoDir, paramFileSpec.id());
    if (TextUtils.isEmpty(paramFileSpec.md5()));
    for (paramFileSpec = "1.apk"; ; paramFileSpec = paramFileSpec.md5() + ".apk")
      return new File(localFile, paramFileSpec);
  }

  java.net.Proxy getProxy(String paramString)
  {
    try
    {
      if (this.connManager.getActiveNetworkInfo().getType() == 0)
      {
        paramString = android.net.Proxy.getDefaultHost();
        int i = android.net.Proxy.getDefaultPort();
        if ((TextUtils.isEmpty(paramString)) || (i < 0))
          return java.net.Proxy.NO_PROXY;
        paramString = new java.net.Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(paramString, i));
        return paramString;
      }
    }
    catch (Exception paramString)
    {
      return java.net.Proxy.NO_PROXY;
    }
    paramString = java.net.Proxy.NO_PROXY;
    return paramString;
  }

  String getStatus(String paramString)
  {
    monitorenter;
    try
    {
      Object localObject;
      if (this.status.get(paramString) == null)
      {
        localObject = (FileSpec)this.map.get(paramString);
        if (localObject == null)
          break label79;
        localObject = getPath((FileSpec)localObject);
        HashMap localHashMap = this.status;
        if (!((File)localObject).isFile())
          break label73;
        localObject = "DONE";
        localHashMap.put(paramString, localObject);
      }
      label73: label79: for (paramString = (String)this.status.get(paramString); ; paramString = null)
      {
        return paramString;
        localObject = "IDLE";
        break;
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }

  public void notifyConnectivityChanged()
  {
    monitorenter;
    try
    {
      Worker localWorker = this.running;
      if (localWorker != null);
      while (true)
      {
        return;
        if (pickFromQueue() == null)
          continue;
        start();
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  void removeListener(StatusChangeListener paramStatusChangeListener)
  {
    this.listeners.remove(paramStatusChangeListener);
  }

  public void require(FileSpec[] paramArrayOfFileSpec)
  {
    monitorenter;
    while (true)
    {
      int i;
      try
      {
        this.order.removeAll(Arrays.asList(paramArrayOfFileSpec));
        i = paramArrayOfFileSpec.length - 1;
        if (i >= 0)
        {
          FileSpec localFileSpec = paramArrayOfFileSpec[i];
          this.order.addFirst(localFileSpec);
          Integer localInteger = (Integer)this.require.get(localFileSpec.id());
          if (localInteger != null)
            continue;
          this.require.put(localFileSpec.id(), Integer.valueOf(1));
          break label128;
          this.require.put(localFileSpec.id(), Integer.valueOf(localInteger.intValue() + 1));
        }
      }
      finally
      {
        monitorexit;
      }
      this.running = new Worker(null);
      this.running.start();
      monitorexit;
      return;
      label128: i -= 1;
    }
  }

  int runningCount()
  {
    monitorenter;
    int i = 0;
    try
    {
      Iterator localIterator = this.status.values().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if ("RUNNING" != str)
          continue;
        i += 1;
      }
      return i;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  void start()
  {
    monitorenter;
    try
    {
      if (this.running == null)
      {
        if (this.status.size() != this.map.size())
          break label80;
        int i = 0;
        Iterator localIterator = this.status.values().iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          if ("IDLE" != str)
            continue;
          i += 1;
        }
        if (i != 0)
          break label80;
      }
      while (true)
      {
        return;
        label80: this.running = new Worker(null);
        this.running.start();
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  int totalCount()
  {
    monitorenter;
    try
    {
      int i = this.map.size();
      monitorexit;
      return i;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  static abstract interface StatusChangeListener
  {
    public abstract void onStatusChanged(FileSpec paramFileSpec, String paramString);
  }

  private class Worker extends Thread
  {
    private int failCounter = 0;

    private Worker()
    {
    }

    public void run()
    {
      while (RepositoryManager.this.running == this)
      {
        FileSpec localFileSpec = RepositoryManager.this.pickFromQueue();
        if (localFileSpec == null)
          break;
        Log.i("loader", "start download " + localFileSpec.id() + " from " + localFileSpec.url());
        long l1 = SystemClock.elapsedRealtime();
        RepositoryManager.this.status.put(localFileSpec.id(), "RUNNING");
        ??? = RepositoryManager.this.listeners.iterator();
        while (((Iterator)???).hasNext())
          ((RepositoryManager.StatusChangeListener)((Iterator)???).next()).onStatusChanged(localFileSpec, "RUNNING");
        ??? = Integer.toHexString(new Random(System.currentTimeMillis()).nextInt(61440) + 4096);
        ??? = new File(RepositoryManager.this.tmpDir, localFileSpec.id() + "." + (String)???);
        boolean bool2 = false;
        try
        {
          HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(localFileSpec.url()).openConnection(RepositoryManager.this.getProxy(localFileSpec.url()));
          localHttpURLConnection.setConnectTimeout(15000);
          InputStream localInputStream = localHttpURLConnection.getInputStream();
          localObject4 = new FileOutputStream((File)???);
          byte[] arrayOfByte2 = new byte[4096];
          try
          {
            while (true)
            {
              i = localInputStream.read(arrayOfByte2, 0, arrayOfByte2.length);
              if (i == -1)
                break;
              ((FileOutputStream)localObject4).write(arrayOfByte2, 0, i);
            }
          }
          finally
          {
            ((FileOutputStream)localObject4).close();
            localInputStream.close();
          }
        }
        catch (Exception localObject3)
        {
          Object localObject4;
          int i;
          Log.w("loader", "fail to download " + localFileSpec.id() + " from " + localFileSpec.url(), localException1);
          boolean bool1 = bool2;
          byte[] arrayOfByte1;
          if (((File)???).length() > 0L)
          {
            bool1 = bool2;
            if (!TextUtils.isEmpty(localFileSpec.md5()))
            {
              bool2 = false;
              bool1 = bool2;
              try
              {
                MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
                bool1 = bool2;
                localMessageDigest.reset();
                bool1 = bool2;
                localObject4 = new FileInputStream((File)???);
                bool1 = bool2;
                arrayOfByte1 = new byte[4096];
                while (true)
                {
                  bool1 = bool2;
                  i = ((FileInputStream)localObject4).read(arrayOfByte1, 0, arrayOfByte1.length);
                  if (i == -1)
                    break;
                  bool1 = bool2;
                  localMessageDigest.update(arrayOfByte1, 0, i);
                }
              }
              catch (Exception localException2)
              {
                Log.e("loader", "fail to verify file " + ((File)???).getAbsolutePath(), localException2);
              }
            }
          }
          label479: Object localObject3 = RepositoryManager.this.getPath(localFileSpec);
          bool2 = bool1;
          if (bool1)
          {
            ((File)localObject3).getParentFile().mkdir();
            bool1 = ((File)???).renameTo((File)localObject3);
            bool2 = bool1;
            if (!bool1)
            {
              Log.e("loader", "fail to move " + localFileSpec.id() + " from " + ((File)???).getAbsolutePath() + " to " + ((File)localObject3).getAbsolutePath());
              bool2 = bool1;
            }
          }
          if (!bool2)
            ((File)???).delete();
          bool1 = ((File)localObject3).isFile();
          if (bool1)
          {
            ??? = "DONE";
            label601: RepositoryManager.this.status.put(localFileSpec.id(), ???);
            if (!bool1)
              break label871;
            long l2 = SystemClock.elapsedRealtime();
            Log.i("loader", localFileSpec.id() + " (" + ((File)localObject3).length() + " bytes) finished in " + (l2 - l1) + "ms");
            if (this.failCounter > 0)
              this.failCounter -= 1;
          }
          while (true)
          {
            localObject3 = RepositoryManager.this.listeners.iterator();
            while (((Iterator)localObject3).hasNext())
              ((RepositoryManager.StatusChangeListener)((Iterator)localObject3).next()).onStatusChanged(localFileSpec, (String)???);
            ((FileOutputStream)localObject4).close();
            ((InputStream)localObject3).close();
            arrayOfByte1.disconnect();
            bool2 = true;
            break;
            bool1 = bool2;
            ((FileInputStream)localObject4).close();
            bool1 = bool2;
            localObject3 = StringUtil.byteArrayToHexString(((MessageDigest)localObject3).digest());
            bool1 = bool2;
            bool2 = localFileSpec.md5().equals(localObject3);
            bool1 = bool2;
            if (bool2)
              break label479;
            bool1 = bool2;
            Log.e("loader", "fail to match " + localFileSpec.id() + " md5, " + (String)localObject3 + " / " + localFileSpec.md5());
            bool1 = bool2;
            break label479;
            ??? = "IDLE";
            break label601;
            label871: RepositoryManager.this.order.remove(localFileSpec);
            RepositoryManager.this.order.addLast(localFileSpec);
            this.failCounter += 1;
          }
        }
        if (this.failCounter < 3)
          continue;
        Log.w("loader", "download fail 3 times, abort");
      }
      synchronized (RepositoryManager.this)
      {
        if (RepositoryManager.this.running == this)
          RepositoryManager.access$102(RepositoryManager.this, null);
        return;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.RepositoryManager
 * JD-Core Version:    0.6.0
 */