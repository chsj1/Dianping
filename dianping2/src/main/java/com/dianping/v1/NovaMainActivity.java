package com.dianping.v1;

import android.os.Bundle;
import com.dianping.app.ChannelEvents;
import com.dianping.main.guide.MainActivity;

public class NovaMainActivity extends MainActivity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    ChannelEvents.performEventsOfMainActivity(this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.v1.NovaMainActivity
 * JD-Core Version:    0.6.0
 */