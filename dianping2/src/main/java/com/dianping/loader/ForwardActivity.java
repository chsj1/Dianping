package com.dianping.loader;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Log;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;

public class ForwardActivity extends DPActivity
  implements SiteManager.StatusChangeListener
{
  private FrameLayout rootView;

  protected void doForward()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", getIntent().getData());
    ArrayList localArrayList;
    StringBuilder localStringBuilder;
    try
    {
      startActivity(localIntent);
      finish();
      return;
    }
    catch (Exception localException)
    {
      localObject = new TextView(this);
      ((TextView)localObject).setText("无法载入页面 #402");
      if (Environment.isDebug())
        ((TextView)localObject).append("\n" + localException.toString());
      ((TextView)localObject).setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
      this.rootView.addView((View)localObject);
      Log.e("app", "unable to forward " + getIntent(), localException);
      localArrayList = new ArrayList(4);
      localArrayList.add(new BasicNameValuePair("error_code", "402"));
      localStringBuilder = new StringBuilder();
      if (!"android.intent.action.VIEW".equals(localIntent.getAction()))
        break label307;
    }
    Object localObject = "";
    localStringBuilder.append((String)localObject);
    localStringBuilder.append('|');
    ComponentName localComponentName = localIntent.getComponent();
    if (localComponentName == null)
    {
      localObject = "";
      label208: localStringBuilder.append((String)localObject);
      localStringBuilder.append('|');
      if (localComponentName != null)
        break label324;
    }
    label307: label324: for (localObject = ""; ; localObject = localComponentName.getClassName())
    {
      localStringBuilder.append((String)localObject);
      localStringBuilder.append('|');
      localStringBuilder.append(localIntent.getData());
      localArrayList.add(new BasicNameValuePair("error_msg", localStringBuilder.toString()));
      localArrayList.add(new BasicNameValuePair("error_exception", localException.toString()));
      statisticsService().record(localArrayList);
      return;
      localObject = localIntent.getAction();
      break;
      localObject = localComponentName.getPackageName();
      break label208;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.rootView = new FrameLayout(this);
    this.rootView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    this.rootView.setId(16908300);
    setContentView(this.rootView);
    if (!(getApplication() instanceof DPApplication))
    {
      paramBundle = new TextView(this);
      paramBundle.setText("无法载入页面 #401");
      paramBundle.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
      this.rootView.addView(paramBundle);
      paramBundle = new ArrayList(4);
      paramBundle.add(new BasicNameValuePair("error_code", "401"));
      statisticsService().record(paramBundle);
      return;
    }
    paramBundle = ((DPApplication)getApplication()).siteManager();
    if (paramBundle.state() == "NONE")
      paramBundle.start(0L);
    if (paramBundle.state() == "RUNNING")
    {
      paramBundle.addListener(this);
      paramBundle = new ProgressBar(this);
      paramBundle.setIndeterminate(true);
      paramBundle.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
      this.rootView.addView(paramBundle);
      return;
    }
    doForward();
  }

  protected void onDestroy()
  {
    ((DPApplication)getApplication()).siteManager().removeListener(this);
    super.onDestroy();
  }

  public void onStatusChanged(String paramString)
  {
    doForward();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.ForwardActivity
 * JD-Core Version:    0.6.0
 */