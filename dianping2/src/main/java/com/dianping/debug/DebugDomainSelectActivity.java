package com.dianping.debug;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.dianping.app.DPActivity;
import com.dianping.app.Environment;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.mapi.MApiDebugAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.Iterator;
import java.util.List;

public class DebugDomainSelectActivity extends NovaActivity
{
  MApiDebugAgent debugAgent;
  private boolean isTesting;

  private boolean isServiceRunning(Class<?> paramClass)
  {
    Iterator localIterator = ((ActivityManager)getSystemService("activity")).getRunningServices(2147483647).iterator();
    while (localIterator.hasNext())
    {
      ActivityManager.RunningServiceInfo localRunningServiceInfo = (ActivityManager.RunningServiceInfo)localIterator.next();
      if (paramClass.getName().equals(localRunningServiceInfo.service.getClassName()))
        return true;
    }
    return false;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.debug_domain_select);
    this.isTesting = isServiceRunning(UrlschemeService.class);
    this.debugAgent = ((MApiDebugAgent)getService("mapi_debug"));
    ((DebugDomainItem)findViewById(R.id.api_item)).setDomain(this.debugAgent.switchDomain());
    ((DebugDomainItem)findViewById(R.id.mapi_item)).setDomain(this.debugAgent.mapiDomain());
    ((DebugDomainItem)findViewById(R.id.booking_item)).setDomain(this.debugAgent.bookingDebugDomain());
    ((DebugDomainItem)findViewById(R.id.tuan_item)).setDomain(this.debugAgent.tDebugDomain());
    ((DebugDomainItem)findViewById(R.id.pay_item)).setDomain(this.debugAgent.payDebugDomain());
    ((DebugDomainItem)findViewById(R.id.movie_item)).setDomain(this.debugAgent.movieDebugDomain());
    ((DebugDomainItem)findViewById(R.id.membercard_item)).setDomain(this.debugAgent.membercardDebugDomain());
    ((DebugDomainItem)findViewById(R.id.takeaway_item)).setDomain(this.debugAgent.takeawayDebugDomain());
    ((DebugDomainItem)findViewById(R.id.huihui_item)).setDomain(this.debugAgent.huihuiDebugDomain());
    ((DebugDomainItem)findViewById(R.id.beauty_item)).setDomain(this.debugAgent.beautyDebugDomain());
    ((DebugDomainItem)findViewById(R.id.locate_item)).setDomain(this.debugAgent.locateDebugDomain());
    ((DebugDomainItem)findViewById(R.id.config_item)).setDomain(this.debugAgent.configDebugDomain());
    ((DebugDomainItem)findViewById(R.id.ga_item)).setDomain(this.debugAgent.newGADebugDomain());
    ((TextView)findViewById(R.id.debug_proxy)).setText(this.debugAgent.proxy());
    ((TextView)findViewById(R.id.debug_proxy_port)).setText("" + this.debugAgent.proxyPort());
    ((ToggleButton)findViewById(R.id.test)).setChecked(this.isTesting);
    ((ToggleButton)findViewById(R.id.test)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
      {
        if (paramBoolean)
        {
          paramCompoundButton = new Intent(DebugDomainSelectActivity.this, UrlschemeService.class);
          String str = ((EditText)DebugDomainSelectActivity.this.findViewById(R.id.autotest)).getText().toString();
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("dpid=").append(DPActivity.preferences().getString("dpid", "")).append("&");
          localStringBuilder.append("deviceinfo=").append(Build.MODEL).append('&');
          localStringBuilder.append("version=").append(Environment.versionName());
          paramCompoundButton.putExtra("device", localStringBuilder.toString());
          paramCompoundButton.putExtra("domain", str);
          try
          {
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.api_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.mapi_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.booking_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.tuan_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.pay_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.movie_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.membercard_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.takeaway_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.huihui_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.beauty_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.locate_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.config_item)).setDomain(str);
            ((DebugDomainItem)DebugDomainSelectActivity.this.findViewById(R.id.ga_item)).setDomain(str);
            AdLogger.testStarted();
            DebugDomainSelectActivity.access$002(DebugDomainSelectActivity.this, true);
            DebugDomainSelectActivity.this.startService(paramCompoundButton);
            DebugDomainSelectActivity.this.finish();
            return;
          }
          catch (Exception localException)
          {
            while (true)
              localException.printStackTrace();
          }
        }
        AdLogger.testFinished();
        DebugDomainSelectActivity.access$002(DebugDomainSelectActivity.this, false);
        paramCompoundButton = new Intent(DebugDomainSelectActivity.this, UrlschemeService.class);
        paramCompoundButton.putExtra("Command", "Exit");
        DebugDomainSelectActivity.this.startService(paramCompoundButton);
      }
    });
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.debugAgent.setSwitchDomain(((DebugDomainItem)findViewById(R.id.api_item)).getCurrentDomain());
    this.debugAgent.setMapiDomain(((DebugDomainItem)findViewById(R.id.mapi_item)).getCurrentDomain());
    this.debugAgent.setBookingDebugDomain(((DebugDomainItem)findViewById(R.id.booking_item)).getCurrentDomain());
    this.debugAgent.setTDebugDomain(((DebugDomainItem)findViewById(R.id.tuan_item)).getCurrentDomain());
    this.debugAgent.setPayDebugDomain(((DebugDomainItem)findViewById(R.id.pay_item)).getCurrentDomain());
    this.debugAgent.setMovieDebugDomain(((DebugDomainItem)findViewById(R.id.movie_item)).getCurrentDomain());
    this.debugAgent.setMembercardDebugDomain(((DebugDomainItem)findViewById(R.id.membercard_item)).getCurrentDomain());
    this.debugAgent.setTakeawayDebugDomain(((DebugDomainItem)findViewById(R.id.takeaway_item)).getCurrentDomain());
    this.debugAgent.setHuihuiDebugDomain(((DebugDomainItem)findViewById(R.id.huihui_item)).getCurrentDomain());
    this.debugAgent.setBeautyDebugDomain(((DebugDomainItem)findViewById(R.id.beauty_item)).getCurrentDomain());
    this.debugAgent.setLocateDebugDomain(((DebugDomainItem)findViewById(R.id.locate_item)).getCurrentDomain());
    this.debugAgent.setNewGADebugDomain(((DebugDomainItem)findViewById(R.id.ga_item)).getCurrentDomain());
    int i = 0;
    try
    {
      int j = Integer.parseInt(((TextView)findViewById(R.id.debug_proxy_port)).getText().toString());
      i = j;
      label294: this.debugAgent.setProxy(((TextView)findViewById(R.id.debug_proxy)).getText().toString(), i);
      return;
    }
    catch (Exception localException)
    {
      break label294;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugDomainSelectActivity
 * JD-Core Version:    0.6.0
 */