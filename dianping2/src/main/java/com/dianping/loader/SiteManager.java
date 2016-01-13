package com.dianping.loader;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import com.dianping.app.Environment;
import com.dianping.loader.model.FileSpec;
import com.dianping.loader.model.FragmentSpec;
import com.dianping.loader.model.SiteSpec;
import com.dianping.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;

public class SiteManager
{
  public static final String STATE_FAILED = "FAILED";
  public static final String STATE_NONE = "NONE";
  public static final String STATE_RUNNING = "RUNNING";
  public static final String STATE_SUCCEED = "SUCCEED";
  private final Context context;
  private long lastSucceedMillis = 0L;
  private final ArrayList<StatusChangeListener> listeners = new ArrayList();
  private final RepositoryManager repoManager;
  private SiteSpec site;
  private String state = "NONE";

  public SiteManager(Context paramContext)
  {
    this.context = paramContext;
    this.repoManager = new RepositoryManager(paramContext);
  }

  private SiteSpec read()
  {
    Object localObject3 = null;
    File localFile = new File(new File(this.context.getFilesDir(), "repo"), "site.txt");
    Object localObject1 = localObject3;
    if (localFile.length() > 0L);
    try
    {
      localObject1 = new FileInputStream(localFile);
      byte[] arrayOfByte = new byte[((FileInputStream)localObject1).available()];
      try
      {
        int i = ((FileInputStream)localObject1).read(arrayOfByte);
        ((FileInputStream)localObject1).close();
        localObject1 = new SiteSpec(new JSONObject(new String(arrayOfByte, 0, i, "UTF-8")));
        localObject3 = localObject1;
        if (localObject1 == null)
          localObject3 = new SiteSpec("empty.0", "0", new FileSpec[0], new FragmentSpec[0]);
        return localObject3;
      }
      finally
      {
        ((FileInputStream)localObject1).close();
      }
    }
    catch (Exception localObject2)
    {
      while (true)
      {
        Log.w("loader", "fail to load site.txt from " + localFile, localException);
        Object localObject2 = localObject3;
      }
    }
  }

  private boolean verify(String paramString)
    throws Exception
  {
    while (paramString.length() > 0)
    {
      i = paramString.charAt(paramString.length() - 1);
      if ((i != 32) && (i != 10) && (i != 13) && (i != 9))
        break;
      paramString = paramString.substring(0, paramString.length() - 1);
    }
    if (!paramString.endsWith("\"}"))
      return false;
    int i = paramString.lastIndexOf(",\"signature\":\"");
    if (i < 0)
      return false;
    byte[] arrayOfByte = Base64.decode(paramString.substring(",\"signature\":\"".length() + i, paramString.length() - 2));
    paramString = paramString.substring(0, i).getBytes("UTF-8");
    PublicKey localPublicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger("126249047213733400394832927062191718124106502252016376335239201017879741479212826889792269518812011930954260815405734979029054948897271479164405774366450123492170170778643868244690050242889774706020933108419828451869350849709235837709591309556789408682416107830970490153274673044649213948647637707735591672551"), new BigInteger("65537")));
    Signature localSignature = Signature.getInstance("SHA1WithRSA");
    localSignature.initVerify(localPublicKey);
    localSignature.update(paramString);
    return localSignature.verify(arrayOfByte);
  }

  public void addListener(StatusChangeListener paramStatusChangeListener)
  {
    this.listeners.add(paramStatusChangeListener);
  }

  public void removeListener(StatusChangeListener paramStatusChangeListener)
  {
    this.listeners.remove(paramStatusChangeListener);
  }

  public RepositoryManager repositoryManager()
  {
    return this.repoManager;
  }

  public SiteSpec site()
  {
    monitorenter;
    try
    {
      if (this.site == null)
        this.site = read();
      SiteSpec localSiteSpec = this.site;
      return localSiteSpec;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void start(long paramLong)
  {
    monitorenter;
    if (paramLong > 0L);
    while (true)
    {
      try
      {
        long l1 = SystemClock.elapsedRealtime();
        long l2 = this.lastSucceedMillis;
        if (l1 < l2 + paramLong)
          return;
        if (this.state == "RUNNING")
          continue;
        Object localObject = url();
        if (TextUtils.isEmpty((CharSequence)localObject))
        {
          this.state = "SUCCEED";
          localObject = this.listeners.iterator();
          if (!((Iterator)localObject).hasNext())
            continue;
          ((StatusChangeListener)((Iterator)localObject).next()).onStatusChanged("SUCCEED");
          continue;
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      this.state = "RUNNING";
      new Worker(str).start();
    }
  }

  public String state()
  {
    monitorenter;
    try
    {
      String str = this.state;
      monitorexit;
      return str;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public String url()
  {
    return null;
  }

  public static abstract interface StatusChangeListener
  {
    public abstract void onStatusChanged(String paramString);
  }

  private class Worker extends Thread
  {
    private String url;

    public Worker(String arg2)
    {
      Object localObject;
      this.url = localObject;
    }

    private String get(String paramString)
    {
      Object localObject2;
      do
      {
        ByteArrayOutputStream localByteArrayOutputStream;
        try
        {
          HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(paramString).openConnection(SiteManager.this.repoManager.getProxy(paramString));
          localHttpURLConnection.setConnectTimeout(15000);
          localHttpURLConnection.setRequestProperty("User-Agent", Environment.mapiUserAgent());
          localHttpURLConnection.setRequestProperty("pragma-os", Environment.mapiUserAgent());
          localHttpURLConnection.setRequestProperty("pragma-device", Environment.imei());
          localHttpURLConnection.setRequestProperty("pragma-uuid", Environment.uuid());
          localObject2 = localHttpURLConnection.getInputStream();
          localByteArrayOutputStream = new ByteArrayOutputStream(16384);
          byte[] arrayOfByte = new byte[4096];
          while (true)
          {
            int i = ((InputStream)localObject2).read(arrayOfByte, 0, arrayOfByte.length);
            if (i == -1)
              break;
            localByteArrayOutputStream.write(arrayOfByte, 0, i);
          }
        }
        catch (Exception localObject1)
        {
          Log.w("loader", "fail to download site from " + paramString, localException);
          localObject1 = null;
          return localObject1;
        }
        localByteArrayOutputStream.close();
        ((InputStream)localObject2).close();
        localObject1.disconnect();
        localObject2 = new String(localByteArrayOutputStream.toByteArray(), "UTF-8");
        Object localObject1 = localObject2;
      }
      while (SiteManager.this.verify((String)localObject2));
      Log.w("loader", "unsigned site.txt from " + paramString);
      Log.w("loader", (String)localObject2);
      throw new Exception("unsigned site.txt");
    }

    public void run()
    {
      try
      {
        localObject4 = this.url;
        localObject3 = get((String)localObject4);
        Object localObject1 = localObject3;
        if (localObject3 == null)
        {
          localObject1 = localObject3;
          if (((String)localObject4).startsWith("http://m.api.dianping.com/"))
            localObject1 = get("http://180.153.132.52/" + ((String)localObject4).substring("http://m.api.dianping.com/".length()));
        }
        if (localObject1 == null)
          throw new Exception("site.txt fail");
      }
      catch (Exception localException1)
      {
        Log.w("loader", "fail to process site.txt", localException1);
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
          public void run()
          {
            SiteManager.access$402(SiteManager.this, "FAILED");
            Iterator localIterator = SiteManager.this.listeners.iterator();
            while (localIterator.hasNext())
              ((SiteManager.StatusChangeListener)localIterator.next()).onStatusChanged("FAILED");
          }
        });
        return;
      }
      Object localObject3 = new SiteSpec(new JSONObject(localException1));
      Object localObject4 = new File(SiteManager.this.context.getFilesDir(), "repo");
      ((File)localObject4).mkdir();
      File localFile = new File((File)localObject4, "site.txt");
      localObject4 = new File((File)localObject4, "site_tmp");
      try
      {
        FileOutputStream localFileOutputStream = new FileOutputStream((File)localObject4);
        try
        {
          localFileOutputStream.write(localException1.getBytes("UTF-8"));
          localFileOutputStream.close();
          ((File)localObject4).renameTo(localFile);
          Log.i("loader", "site.xml updated to " + ((SiteSpec)localObject3).id());
          new Handler(Looper.getMainLooper()).post(new Runnable((SiteSpec)localObject3)
          {
            public void run()
            {
              SiteManager.access$302(SiteManager.this, this.val$fSite);
              SiteManager.access$402(SiteManager.this, "SUCCEED");
              SiteManager.access$502(SiteManager.this, SystemClock.elapsedRealtime());
              Iterator localIterator = SiteManager.this.listeners.iterator();
              while (localIterator.hasNext())
                ((SiteManager.StatusChangeListener)localIterator.next()).onStatusChanged("SUCCEED");
              SiteManager.this.repoManager.addFiles(SiteManager.this.site);
              SiteManager.this.repoManager.start();
            }
          });
          return;
        }
        finally
        {
          localFileOutputStream.close();
        }
      }
      catch (Exception localException2)
      {
        while (true)
          ((File)localObject4).delete();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.SiteManager
 * JD-Core Version:    0.6.0
 */