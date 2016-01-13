package com.dianping.loader;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.loader.model.FileSpec;
import com.dianping.loader.model.FragmentSpec;
import com.dianping.loader.model.SiteSpec;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class LoaderActivity extends DPActivity
{
  private boolean loaded;
  private Loader loader;
  private FrameLayout rootView;
  private SiteSpec site;

  private void setFail(int paramInt, Exception paramException, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.rootView.removeAllViews();
    Object localObject1 = new TextView(this);
    Object localObject2 = new StringBuilder().append("无法载入页面 #");
    int i;
    Object localObject3;
    if (paramInt > 0)
    {
      i = paramInt;
      ((TextView)localObject1).setText(i);
      if ((Environment.isDebug()) && (paramException != null))
      {
        ((TextView)localObject1).append("\n");
        ((TextView)localObject1).append(paramException.toString());
      }
      if (!paramBoolean1)
        break label475;
      ((TextView)localObject1).setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1.0F));
      localObject2 = new Button(this);
      ((Button)localObject2).setText("重试");
      ((Button)localObject2).setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1.0F));
      ((Button)localObject2).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          LoaderActivity.this.startLoader();
        }
      });
      localObject3 = new LinearLayout(this);
      ((LinearLayout)localObject3).setOrientation(1);
      ((LinearLayout)localObject3).setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
      ((LinearLayout)localObject3).addView((View)localObject1);
      ((LinearLayout)localObject3).addView((View)localObject2);
      this.rootView.addView((View)localObject3);
      label204: if (!Environment.isDebug())
        break label557;
      localObject2 = this.rootView.getChildAt(0);
      if (!(localObject2 instanceof LinearLayout))
        break label505;
      localObject1 = (LinearLayout)localObject2;
      label235: localObject2 = new Button(this);
      ((Button)localObject2).setText("Skip");
      ((Button)localObject2).setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1.0F));
      ((Button)localObject2).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          LoaderActivity.this.skipIntent();
        }
      });
      ((LinearLayout)localObject1).addView((View)localObject2);
      label289: if (paramBoolean2)
      {
        localObject2 = new ArrayList(4);
        if (paramInt <= 0)
          break label565;
        label308: ((ArrayList)localObject2).add(new BasicNameValuePair("error_code", String.valueOf(paramInt)));
        localObject3 = new StringBuilder();
        if ((this.site.version() != null) && (!this.site.id().endsWith(this.site.version())))
          break label571;
        ((StringBuilder)localObject3).append(this.site.id());
        label379: ((StringBuilder)localObject3).append('|');
        localObject1 = getIntent().getData();
        if (localObject1 != null)
          break label587;
      }
    }
    label557: label565: label571: label587: for (localObject1 = ""; ; localObject1 = ((Uri)localObject1).toString())
    {
      ((StringBuilder)localObject3).append((String)localObject1);
      ((ArrayList)localObject2).add(new BasicNameValuePair("error_msg", ((StringBuilder)localObject3).toString()));
      if (paramException != null)
        ((ArrayList)localObject2).add(new BasicNameValuePair("error_exception", paramException.toString()));
      statisticsService().record((List)localObject2);
      return;
      i = 100;
      break;
      label475: ((TextView)localObject1).setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
      this.rootView.addView((View)localObject1);
      break label204;
      label505: localObject1 = new LinearLayout(this);
      ((LinearLayout)localObject1).setOrientation(1);
      ((LinearLayout)localObject1).setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
      this.rootView.removeViewAt(0);
      ((LinearLayout)localObject1).addView((View)localObject2);
      break label235;
      skipIntent();
      break label289;
      paramInt = 100;
      break label308;
      ((StringBuilder)localObject3).append(this.site.version());
      break label379;
    }
  }

  private boolean skipIntent()
  {
    Intent localIntent = new Intent(getIntent().getAction(), getIntent().getData());
    if (getIntent().getExtras() != null)
      localIntent.putExtras(getIntent().getExtras());
    if (getPackageManager().queryIntentActivities(localIntent, 0).size() > 0)
    {
      localIntent.putExtra("noUrlMapping", true);
      startActivityForResult(localIntent, 1);
      return true;
    }
    return false;
  }

  private void startLoader()
  {
    if (this.loader != null)
      this.loader.stop();
    this.loader = new Loader();
    this.loader.start();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 1)
    {
      setResult(paramInt2, paramIntent);
      finish();
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.rootView = new FrameLayout(this);
    this.rootView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    this.rootView.setId(16908300);
    setContentView(this.rootView);
    this.site = ((SiteSpec)getIntent().getParcelableExtra("_site"));
    if (!(getApplication() instanceof DPApplication))
    {
      setFail(101, null, false, true);
      paramBundle = new ArrayList(4);
      paramBundle.add(new BasicNameValuePair("error_code", "101"));
      statisticsService().record(paramBundle);
      return;
    }
    startLoader();
  }

  protected void onDestroy()
  {
    if (this.loader != null)
      this.loader.stop();
    super.onDestroy();
  }

  private class Loader
    implements RepositoryManager.StatusChangeListener
  {
    FileSpec[] depsList;
    FragmentSpec fragment;
    Handler handler;
    boolean isDownloading;
    RepositoryManager repoManager = ((DPApplication)LoaderActivity.this.getApplication()).repositoryManager();
    SiteManager siteManager = ((DPApplication)LoaderActivity.this.getApplication()).siteManager();

    public Loader()
    {
    }

    private void doRepo()
    {
      int i = 0;
      Object localObject1 = LoaderActivity.this.getIntent().getData();
      if (localObject1 == null)
        i = 105;
      while (i > 0)
      {
        setFail(i, null, false, true);
        return;
        localObject1 = ((Uri)localObject1).getHost();
        if (localObject1 == null)
        {
          i = 106;
          continue;
        }
        this.fragment = LoaderActivity.this.site.getFragment((String)localObject1, Environment.versionCode());
        if (this.fragment == null)
        {
          i = 107;
          continue;
        }
        if (TextUtils.isEmpty(this.fragment.code()))
        {
          setDone();
          return;
        }
        if (this.repoManager.getStatus(this.fragment.code()) == null)
          this.repoManager.addFiles(LoaderActivity.this.site);
        localObject1 = new ArrayList();
        if (!this.repoManager.appendDepsList((List)localObject1, this.fragment.code()))
        {
          i = 108;
          continue;
        }
        this.depsList = ((FileSpec[])((ArrayList)localObject1).toArray(new FileSpec[((ArrayList)localObject1).size()]));
      }
      int k = 0;
      localObject1 = this.depsList;
      int m = localObject1.length;
      i = 0;
      while (true)
      {
        int j = k;
        if (i < m)
        {
          Object localObject2 = localObject1[i];
          if (this.repoManager.getStatus(localObject2.id()) != "DONE")
            j = 1;
        }
        else
        {
          if (j == 0)
            break label299;
          this.isDownloading = true;
          this.repoManager.addListener(this);
          this.repoManager.require(this.depsList);
          if (this.fragment.timeout() <= 0)
            break;
          this.handler.postDelayed(new Runnable()
          {
            public void run()
            {
              if ((LoaderActivity.this.loader == LoaderActivity.Loader.this) && (LoaderActivity.Loader.this.isDownloading) && (LoaderActivity.this.skipIntent()))
                LoaderActivity.Loader.this.stop();
            }
          }
          , this.fragment.timeout());
          return;
        }
        i += 1;
      }
      label299: setDone();
    }

    private void doSite()
    {
      LoaderActivity.access$202(LoaderActivity.this, this.siteManager.site());
      if (LoaderActivity.this.site == null)
      {
        setFail(102, null, false, true);
        return;
      }
      this.repoManager.addFiles(LoaderActivity.this.site);
      doRepo();
    }

    private void setDone()
    {
      if (LoaderActivity.this.loader != this)
        return;
      LoaderActivity.this.rootView.removeAllViews();
      Object localObject1 = LoaderActivity.this.site.getFile(this.fragment.code());
      localObject1 = MyClassLoader.getClassLoader(LoaderActivity.this.site, (FileSpec)localObject1);
      Object localObject2 = LoaderActivity.this;
      boolean bool;
      if (localObject1 != null)
        bool = true;
      while (true)
      {
        LoaderActivity.access$602((LoaderActivity)localObject2, bool);
        if (!LoaderActivity.this.loaded)
          break label304;
        localObject1 = new Intent(LoaderActivity.this.getIntent().getAction(), LoaderActivity.this.getIntent().getData());
        if (LoaderActivity.this.getIntent().getExtras() != null)
          ((Intent)localObject1).putExtras(LoaderActivity.this.getIntent().getExtras());
        ((Intent)localObject1).putExtra("_site", LoaderActivity.this.site);
        localObject1 = LoaderActivity.this.urlMap((Intent)localObject1);
        try
        {
          localObject2 = LoaderActivity.this.getPackageManager().queryIntentActivities((Intent)localObject1, 0);
          if (((List)localObject2).size() != 1)
            break label271;
          localObject2 = (ResolveInfo)((List)localObject2).get(0);
          if ((!LoaderActivity.this.getPackageName().equals(((ResolveInfo)localObject2).activityInfo.packageName)) || (!LoaderActivity.this.getClass().getName().equals(((ResolveInfo)localObject2).activityInfo.name)))
            break;
          setFail(121, null, false, true);
          return;
        }
        catch (Exception localException)
        {
          setFail(122, null, false, true);
          Log.e("loader", "fail to start activity", localException);
          return;
        }
        bool = false;
        continue;
        label271: if (((List)localObject2).size() <= 1)
          break;
      }
      LoaderActivity.this.startActivityForResult(localException, 1);
      LoaderActivity.this.overridePendingTransition(0, 0);
      stop();
      return;
      label304: setFail(120, null, false, true);
    }

    private void setFail(int paramInt, Exception paramException, boolean paramBoolean1, boolean paramBoolean2)
    {
      if (LoaderActivity.this.loader != this)
        return;
      LoaderActivity.this.setFail(paramInt, paramException, paramBoolean1, paramBoolean2);
      stop();
    }

    private void setLoading()
    {
      if (LoaderActivity.this.loader != this)
        return;
      LoaderActivity.this.rootView.removeAllViews();
      ProgressBar localProgressBar = new ProgressBar(LoaderActivity.this);
      localProgressBar.setIndeterminate(true);
      localProgressBar.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
      LoaderActivity.this.rootView.addView(localProgressBar);
    }

    public void onStatusChanged(FileSpec paramFileSpec, String paramString)
    {
      if ("RUNNING".equalsIgnoreCase(paramString));
      int j;
      do
      {
        FileSpec[] arrayOfFileSpec;
        do
        {
          return;
          arrayOfFileSpec = this.depsList;
        }
        while ((!this.isDownloading) || (arrayOfFileSpec == null) || (!Arrays.asList(arrayOfFileSpec).contains(paramFileSpec)));
        if ("IDLE".equalsIgnoreCase(paramString))
        {
          this.handler.post(new Runnable()
          {
            public void run()
            {
              LoaderActivity.Loader.this.setFail(110, null, true, false);
            }
          });
          return;
        }
        int k = 0;
        j = 0;
        int i1 = arrayOfFileSpec.length;
        int i = 0;
        if (i < i1)
        {
          paramFileSpec = arrayOfFileSpec[i];
          paramFileSpec = this.repoManager.getStatus(paramFileSpec.id());
          int m;
          int n;
          if ("DONE".equalsIgnoreCase(paramFileSpec))
          {
            m = k + 1;
            n = j;
          }
          while (true)
          {
            i += 1;
            k = m;
            j = n;
            break;
            m = k;
            n = j;
            if (!"RUNNING".equalsIgnoreCase(paramFileSpec))
              continue;
            n = j + 1;
            m = k;
          }
        }
        if (k != arrayOfFileSpec.length)
          continue;
        this.handler.post(new Runnable()
        {
          public void run()
          {
            LoaderActivity.Loader.this.setDone();
          }
        });
        return;
      }
      while (("DONE".equalsIgnoreCase(paramString)) || (j != 0));
      this.handler.post(new Runnable()
      {
        public void run()
        {
          LoaderActivity.Loader.this.setFail(111, null, true, false);
        }
      });
    }

    public void start()
    {
      this.handler = new Handler(Looper.getMainLooper());
      setLoading();
      if (LoaderActivity.this.site == null)
      {
        doSite();
        return;
      }
      doRepo();
    }

    public void stop()
    {
      if ((this.isDownloading) && (this.depsList != null))
      {
        this.repoManager.dismiss(this.depsList);
        this.depsList = null;
      }
      this.isDownloading = false;
      if (LoaderActivity.this.loader == this)
        LoaderActivity.access$302(LoaderActivity.this, null);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.LoaderActivity
 * JD-Core Version:    0.6.0
 */