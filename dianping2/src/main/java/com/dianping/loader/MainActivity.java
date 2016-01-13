package com.dianping.loader;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.app.Environment;
import com.dianping.loader.model.FileSpec;
import com.dianping.loader.model.SiteSpec;
import com.dianping.statistics.StatisticsService;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class MainActivity extends DPActivity
  implements MyResources.ResourceOverrideable
{
  private AssetManager assetManager;
  private MyClassLoader classLoader;
  private DispatchInterceptor dispatchInterceptor;
  private FileSpec file;
  private String fragmentName;
  private boolean loaded;
  private MyResources myResources;
  private Resources resources;
  private FrameLayout rootView;
  private SiteSpec site;
  private Resources.Theme theme;

  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if ((this.dispatchInterceptor != null) && (this.dispatchInterceptor.dispatchGenericMotionEvent(paramMotionEvent)))
      return true;
    if (Build.VERSION.SDK_INT >= 12)
      return super.dispatchGenericMotionEvent(paramMotionEvent);
    return false;
  }

  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    if ((this.dispatchInterceptor != null) && (this.dispatchInterceptor.dispatchKeyEvent(paramKeyEvent)))
      return true;
    return super.dispatchKeyEvent(paramKeyEvent);
  }

  public boolean dispatchKeyShortcutEvent(KeyEvent paramKeyEvent)
  {
    if ((this.dispatchInterceptor != null) && (this.dispatchInterceptor.dispatchKeyShortcutEvent(paramKeyEvent)))
      return true;
    if (Build.VERSION.SDK_INT >= 11)
      return super.dispatchKeyShortcutEvent(paramKeyEvent);
    return false;
  }

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.dispatchInterceptor != null) && (this.dispatchInterceptor.dispatchTouchEvent(paramMotionEvent)))
      return true;
    return super.dispatchTouchEvent(paramMotionEvent);
  }

  public boolean dispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    if ((this.dispatchInterceptor != null) && (this.dispatchInterceptor.dispatchTrackballEvent(paramMotionEvent)))
      return true;
    return super.dispatchTrackballEvent(paramMotionEvent);
  }

  public AssetManager getAssets()
  {
    if (this.assetManager == null)
      return super.getAssets();
    return this.assetManager;
  }

  public ClassLoader getClassLoader()
  {
    if (this.classLoader == null)
      return super.getClassLoader();
    return this.classLoader;
  }

  public FileSpec getFile()
  {
    return this.file;
  }

  public String getFragmentName()
  {
    return this.fragmentName;
  }

  public MyResources getOverrideResources()
  {
    return this.myResources;
  }

  public Resources getResources()
  {
    if (this.resources == null)
      return super.getResources();
    return this.resources;
  }

  public SiteSpec getSite()
  {
    return this.site;
  }

  public Resources.Theme getTheme()
  {
    if (this.theme == null)
      return super.getTheme();
    return this.theme;
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    if (this.dispatchInterceptor != null)
      this.dispatchInterceptor.onConfigurationChanged(paramConfiguration);
  }

  public void onCreate(Bundle paramBundle)
  {
    Object localObject1 = getIntent();
    int i = 0;
    this.site = ((SiteSpec)((Intent)localObject1).getParcelableExtra("_site"));
    Object localObject2;
    if (this.site == null)
    {
      i = 201;
      super.onCreate(paramBundle);
      requestWindowFeature(7);
      this.rootView = new FrameLayout(this);
      this.rootView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
      this.rootView.setId(16908300);
      setContentView(this.rootView);
      getWindow().setFeatureInt(7, R.layout.custom_titlebar);
      if (this.loaded)
        break label502;
      localObject1 = new TextView(this);
      localObject2 = new StringBuilder().append("无法载入页面");
      if (i != 0)
        break label454;
      paramBundle = "";
      label139: ((TextView)localObject1).setText(paramBundle);
      ((TextView)localObject1).setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
      this.rootView.addView((View)localObject1);
      if (i > 0)
      {
        localObject1 = new ArrayList(4);
        ((ArrayList)localObject1).add(new BasicNameValuePair("error_code", String.valueOf(i)));
        localObject2 = new StringBuilder();
        if (this.site != null)
        {
          if ((this.site.version() != null) && (!this.site.id().endsWith(this.site.version())))
            break label479;
          ((StringBuilder)localObject2).append(this.site.id());
        }
        label266: ((StringBuilder)localObject2).append('|');
        paramBundle = getIntent().getData();
        if (paramBundle != null)
          break label494;
        paramBundle = "";
        label288: ((StringBuilder)localObject2).append(paramBundle);
        ((ArrayList)localObject1).add(new BasicNameValuePair("error_msg", ((StringBuilder)localObject2).toString()));
        statisticsService().record((List)localObject1);
      }
    }
    label454: label479: label494: label502: 
    do
    {
      return;
      this.fragmentName = ((Intent)localObject1).getStringExtra("_fragment");
      if (TextUtils.isEmpty(this.fragmentName))
      {
        i = 202;
        break;
      }
      localObject1 = ((Intent)localObject1).getStringExtra("_code");
      if (TextUtils.isEmpty((CharSequence)localObject1))
      {
        this.loaded = true;
        break;
      }
      this.file = this.site.getFile((String)localObject1);
      if (this.file == null)
      {
        i = 205;
        break;
      }
      this.classLoader = MyClassLoader.getClassLoader(this.site, this.file);
      if (this.classLoader != null);
      for (boolean bool = true; ; bool = false)
      {
        this.loaded = bool;
        if (this.loaded)
          break;
        i = 210;
        break;
      }
      paramBundle = " #" + i;
      break label139;
      ((StringBuilder)localObject2).append(this.site.version());
      break label266;
      paramBundle = paramBundle.toString();
      break label288;
    }
    while (paramBundle != null);
    StringBuilder localStringBuilder;
    try
    {
      paramBundle = (Fragment)getClassLoader().loadClass(this.fragmentName).newInstance();
      localObject1 = getSupportFragmentManager().beginTransaction();
      ((FragmentTransaction)localObject1).replace(16908300, paramBundle);
      ((FragmentTransaction)localObject1).commit();
      return;
    }
    catch (Exception localException)
    {
      this.loaded = false;
      this.classLoader = null;
      paramBundle = new TextView(this);
      paramBundle.setText("无法载入页面 #" + 211);
      if (Environment.isDebug())
        paramBundle.append("\n" + localException);
      paramBundle.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
      this.rootView.addView(paramBundle);
      localObject2 = new ArrayList(4);
      ((ArrayList)localObject2).add(new BasicNameValuePair("error_code", String.valueOf(211)));
      localStringBuilder = new StringBuilder();
      if (this.site.version() == null)
        break label715;
    }
    if (this.site.id().endsWith(this.site.version()))
    {
      label715: localStringBuilder.append(this.site.id());
      localStringBuilder.append('|');
      paramBundle = getIntent().getData();
      if (paramBundle != null)
        break label823;
    }
    label823: for (paramBundle = ""; ; paramBundle = paramBundle.toString())
    {
      localStringBuilder.append(paramBundle);
      ((ArrayList)localObject2).add(new BasicNameValuePair("error_msg", localStringBuilder.toString()));
      ((ArrayList)localObject2).add(new BasicNameValuePair("error_exception", localException.toString()));
      statisticsService().record((List)localObject2);
      return;
      localStringBuilder.append(this.site.version());
      break;
    }
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((this.dispatchInterceptor != null) && (this.dispatchInterceptor.onKeyDown(paramInt, paramKeyEvent)))
      return true;
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public void setDispatchInterceptor(DispatchInterceptor paramDispatchInterceptor)
  {
    this.dispatchInterceptor = paramDispatchInterceptor;
  }

  public void setOverrideResources(MyResources paramMyResources)
  {
    if (paramMyResources == null)
    {
      this.myResources = null;
      this.resources = null;
      this.assetManager = null;
      this.theme = null;
      return;
    }
    this.myResources = paramMyResources;
    this.resources = paramMyResources.getResources();
    this.assetManager = paramMyResources.getAssets();
    paramMyResources = paramMyResources.getResources().newTheme();
    paramMyResources.setTo(getTheme());
    this.theme = paramMyResources;
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    super.setTitle(paramCharSequence);
    View localView = findViewById(16908310);
    if (localView != null)
    {
      localView = localView.findViewWithTag("title");
      if ((localView instanceof TextView))
        ((TextView)localView).setText(paramCharSequence);
    }
  }

  public Intent urlMap(Intent paramIntent)
  {
    Uri localUri = paramIntent.getData();
    if (localUri == null);
    while (true)
    {
      return super.urlMap(paramIntent);
      if ((localUri.getScheme() == null) || ((!"dianping".equalsIgnoreCase(localUri.getScheme())) && (!"dpinner".equalsIgnoreCase(localUri.getScheme()))))
        continue;
      paramIntent.putExtra("_site", this.site);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.MainActivity
 * JD-Core Version:    0.6.0
 */