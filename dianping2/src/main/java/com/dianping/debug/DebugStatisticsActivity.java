package com.dianping.debug;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import com.dianping.app.DPApplication;
import com.dianping.base.app.NovaActivity;
import com.dianping.statistics.impl.MyStatisticsService;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class DebugStatisticsActivity extends NovaActivity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.debug_statistics);
    String str2 = ((MyStatisticsService)DPApplication.instance().getService("statistics")).uploadUrl();
    if (Build.VERSION.SDK_INT >= 11);
    for (paramBundle = getSharedPreferences("dppushservice", 4); ; paramBundle = getSharedPreferences("dppushservice", 0))
    {
      String str1 = paramBundle.getString("pushStatsUrl", "");
      paramBundle = str1;
      if (TextUtils.isEmpty(str1))
        paramBundle = "http://stat.api.dianping.com/utm.js?v=androidpush";
      ((DebugDomainItem)findViewById(R.id.statistics_item)).setDomain(str2);
      ((DebugDomainItem)findViewById(R.id.push_statistics_item)).setDomain(paramBundle);
      return;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    Object localObject = ((DebugDomainItem)findViewById(R.id.statistics_item)).getCurrentDomain();
    ((MyStatisticsService)DPApplication.instance().getService("statistics")).setUploadUrl((String)localObject);
    ((MyStatisticsService)DPApplication.instance().getService("statistics")).setUploadInterval(2500);
    if (Build.VERSION.SDK_INT >= 11);
    for (localObject = getSharedPreferences("dppushservice", 4); ; localObject = getSharedPreferences("dppushservice", 0))
    {
      ((SharedPreferences)localObject).edit().putString("pushStatsUrl", ((DebugDomainItem)findViewById(R.id.push_statistics_item)).getCurrentDomain()).commit();
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugStatisticsActivity
 * JD-Core Version:    0.6.0
 */