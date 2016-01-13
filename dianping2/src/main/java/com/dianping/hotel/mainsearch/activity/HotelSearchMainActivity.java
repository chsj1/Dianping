package com.dianping.hotel.mainsearch.activity;

import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.hotel.mainsearch.fragment.HotelSearchMainFragment;

public class HotelSearchMainActivity extends AgentActivity
{
  HotelSearchMainFragment contentFragment;

  protected AgentFragment getAgentFragment()
  {
    return new HotelSearchMainFragment();
  }

  protected boolean needTitleBarShadow()
  {
    return false;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    hideTitleBar();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.mainsearch.activity.HotelSearchMainActivity
 * JD-Core Version:    0.6.0
 */