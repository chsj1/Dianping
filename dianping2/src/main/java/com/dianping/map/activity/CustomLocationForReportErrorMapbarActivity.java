package com.dianping.map.activity;

import android.content.Intent;
import com.dianping.map.basic.CustomLocationBasicActivity;

public class CustomLocationForReportErrorMapbarActivity extends CustomLocationBasicActivity
{
  public void setupView()
  {
    super.setupView();
    setTitle("地图位置报错");
    setSubtitle(getIntent().getStringExtra("name"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.activity.CustomLocationForReportErrorMapbarActivity
 * JD-Core Version:    0.6.0
 */