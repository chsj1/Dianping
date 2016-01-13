package com.dianping.loader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.loader.model.SiteSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONObject;

public class DevLoaderActivity extends DPActivity
{
  private RepositoryManager repoManager;
  private MyHttpServer server;
  private SiteSpec site;
  private TextView text;

  private void print(Object paramObject)
  {
    if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId())
    {
      this.text.append(String.valueOf(paramObject));
      return;
    }
    runOnUiThread(new Runnable(paramObject)
    {
      public void run()
      {
        DevLoaderActivity.this.print(this.val$obj);
      }
    });
  }

  private void println(Object paramObject)
  {
    if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId())
    {
      this.text.append(String.valueOf(paramObject));
      this.text.append("\n");
      return;
    }
    runOnUiThread(new Runnable(paramObject)
    {
      public void run()
      {
        DevLoaderActivity.this.println(this.val$obj);
      }
    });
  }

  public void onCreate(Bundle paramBundle)
  {
    DPApplication.DEV_LOADER = true;
    super.onCreate(paramBundle);
    this.repoManager = ((DPApplication)getApplication()).repositoryManager();
    this.text = new TextView(this);
    this.text.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    setContentView(this.text);
    int i = getIntent().getIntExtra("port", 5036);
    this.server = new MyHttpServer(i);
    try
    {
      this.server.start();
      println("server started on port " + i);
      return;
    }
    catch (Exception paramBundle)
    {
      print("unable to start server on port " + i + ": ");
      println(paramBundle);
    }
  }

  protected void onDestroy()
  {
    try
    {
      this.server.stop();
      label7: super.onDestroy();
      return;
    }
    catch (Exception localException)
    {
      break label7;
    }
  }

  private class MyHttpServer extends EmbedHttpServer
  {
    public MyHttpServer(int arg2)
    {
      super();
    }

    private int delete(File paramFile)
    {
      int k;
      if (paramFile.isFile())
      {
        paramFile.delete();
        k = 1;
        return k;
      }
      paramFile = paramFile.listFiles();
      if (paramFile == null)
        return 0;
      int i = 0;
      int m = paramFile.length;
      int j = 0;
      while (true)
      {
        k = i;
        if (j >= m)
          break;
        i += delete(paramFile[j]);
        j += 1;
      }
    }

    @SuppressLint({"NewApi"})
    protected void handle(String paramString1, String paramString2, HashMap<String, String> paramHashMap, InputStream paramInputStream, EmbedHttpServer.ResponseOutputStream paramResponseOutputStream)
      throws Exception
    {
      DevLoaderActivity.this.println(paramString1 + " " + paramString2);
      String str = paramString2.toLowerCase(Locale.US);
      paramString2 = "";
      int i = str.indexOf('?');
      paramHashMap = str;
      if (i != -1)
      {
        paramString2 = str.substring(i + 1);
        paramHashMap = str.substring(0, i);
      }
      i = paramString2.indexOf('#');
      str = paramString2;
      if (i != -1)
        str = paramString2.substring(0, i);
      int j;
      Object localObject1;
      Object localObject2;
      if (("GET".equalsIgnoreCase(paramString1)) && ("/list".equalsIgnoreCase(paramHashMap)))
      {
        paramResponseOutputStream.setContentTypeText();
        paramString2 = DevLoaderActivity.this.repoManager.getDir();
        paramString2.mkdirs();
        i = 0;
        paramString2 = paramString2.listFiles();
        int n = paramString2.length;
        j = 0;
        while (j < n)
        {
          localObject1 = paramString2[j];
          int m = i;
          if (((File)localObject1).isDirectory())
          {
            localObject2 = ((File)localObject1).listFiles();
            int i1 = localObject2.length;
            int k = 0;
            while (true)
            {
              m = i;
              if (k >= i1)
                break;
              Object localObject3 = localObject2[k];
              m = i;
              if (localObject3.isFile())
              {
                m = i;
                if (localObject3.getName().endsWith(".apk"))
                {
                  paramResponseOutputStream.write((((File)localObject1).getName() + "/" + localObject3.getName() + "\n").getBytes("ASCII"));
                  m = i + 1;
                }
              }
              k += 1;
              i = m;
            }
          }
          j += 1;
          i = m;
        }
        DevLoaderActivity.this.println(i + " files listed");
      }
      if (paramHashMap.startsWith("/repo/"))
      {
        paramString2 = DevLoaderActivity.this.repoManager.getDir();
        localObject1 = paramHashMap.substring("/repo/".length());
        paramString2 = new File(paramString2.getAbsolutePath() + "/" + (String)localObject1);
        if ("PUT".equalsIgnoreCase(paramString1))
        {
          paramString2.getParentFile().mkdirs();
          localObject1 = new FileOutputStream(paramString2);
          localObject2 = new byte[4096];
          while (true)
          {
            i = paramInputStream.read(localObject2);
            if (i == -1)
              break;
            ((FileOutputStream)localObject1).write(localObject2, 0, i);
          }
          ((FileOutputStream)localObject1).close();
          DevLoaderActivity.this.println(paramString2.length() + " bytes writed");
        }
        if ("GET".equalsIgnoreCase(paramString1))
        {
          if (!paramString2.isFile())
            break label761;
          paramResponseOutputStream.setContentLength((int)paramString2.length());
          paramResponseOutputStream.setContentTypeBinary();
          localObject1 = new FileInputStream(paramString2);
          localObject2 = new byte[4096];
          while (true)
          {
            i = paramInputStream.read(localObject2);
            if (i == -1)
              break;
            paramResponseOutputStream.write(localObject2, 0, i);
          }
          ((FileInputStream)localObject1).close();
          DevLoaderActivity.this.println(paramString2.length() + " bytes read");
        }
        if ("DELETE".equalsIgnoreCase(paramString1))
        {
          i = delete(paramString2);
          boolean bool = paramString2.exists();
          paramResponseOutputStream = DevLoaderActivity.this;
          localObject1 = new StringBuilder().append(i).append(" files deleted");
          if (!bool)
            break label781;
        }
      }
      label781: for (paramString2 = " (fail)"; ; paramString2 = "")
      {
        paramResponseOutputStream.println(paramString2);
        if ((!"PUT".equalsIgnoreCase(paramString1)) || ((!"/site".equals(paramHashMap)) && (!"/site.txt".equals(paramHashMap))))
          break label921;
        paramString2 = new byte[paramInputStream.available()];
        i = 0;
        while (true)
        {
          j = paramInputStream.read(paramString2, i, paramString2.length - i);
          if (j == -1)
            break;
          i += j;
        }
        label761: paramResponseOutputStream.setStatusCode(404);
        DevLoaderActivity.this.println("404 not found");
        break;
      }
      paramInputStream = new String(paramString2, 0, i, Charset.forName("UTF-8"));
      try
      {
        paramInputStream = new JSONObject(paramInputStream);
        DevLoaderActivity.access$302(DevLoaderActivity.this, new SiteSpec(paramInputStream));
        paramInputStream = new FileOutputStream(new File(new File(DevLoaderActivity.this.getFilesDir(), "repo"), "site.txt"));
        paramInputStream.write(paramString2, 0, i);
        paramInputStream.close();
        DevLoaderActivity.this.println("site.txt is ready (" + DevLoaderActivity.this.site + ")");
        label921: if (("GET".equalsIgnoreCase(paramString1)) && ((paramHashMap.startsWith("/go/")) || (paramHashMap.startsWith("/debug/"))))
        {
          paramString1 = new StringBuilder("dianping");
          paramString1.append("://").append(paramHashMap.substring(paramHashMap.indexOf('/', 1) + 1));
          if (!TextUtils.isEmpty(str))
            paramString1.append('?').append(str);
          paramString1 = new Intent("android.intent.action.VIEW", Uri.parse(paramString1.toString()));
          if (DevLoaderActivity.this.site != null)
            paramString1.putExtra("_site", DevLoaderActivity.this.site);
          if (paramHashMap.startsWith("/debug/"))
            new DevLoaderActivity.WaitForDebugger(DevLoaderActivity.this, paramString1).start();
        }
        else
        {
          return;
        }
      }
      catch (Exception paramString2)
      {
        while (true)
        {
          DevLoaderActivity.this.println("malformed site.txt");
          DevLoaderActivity.this.println(paramString2.toString());
        }
        DevLoaderActivity.this.startActivity(paramString1);
      }
    }
  }

  private class WaitForDebugger extends Handler
  {
    AlertDialog dialog;
    Intent intent;
    private final Runnable looper = new Runnable()
    {
      public void run()
      {
        while ((DevLoaderActivity.WaitForDebugger.this.dialog != null) && (!Debug.isDebuggerConnected()))
          try
          {
            Thread.sleep(200L);
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        DevLoaderActivity.WaitForDebugger.this.sendEmptyMessage(2);
      }
    };

    public WaitForDebugger(Intent arg2)
    {
      super();
      Object localObject;
      this.intent = localObject;
    }

    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1)
      {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(DevLoaderActivity.this);
        localBuilder.setTitle("Waiting For Debugger..");
        localBuilder.setIcon(17301543);
        localBuilder.setMessage(Html.fromHtml("Go to eclipse, open DDMS perspective, debug <b>" + DevLoaderActivity.this.getPackageName() + "</b> in Devices panel"));
        localBuilder.setNegativeButton("Skip", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            DevLoaderActivity.WaitForDebugger.this.sendEmptyMessage(2);
          }
        });
        localBuilder.setCancelable(false);
        this.dialog = localBuilder.create();
        this.dialog.show();
        new Thread(this.looper, "wait-for-debugger").start();
      }
      if ((paramMessage.what == 2) && (this.dialog != null))
      {
        this.dialog.dismiss();
        this.dialog = null;
        DevLoaderActivity.this.startActivity(this.intent);
      }
    }

    public void start()
    {
      sendEmptyMessage(1);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.DevLoaderActivity
 * JD-Core Version:    0.6.0
 */