package com.dianping.travel.abroad.activity;

import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.model.City;
import com.dianping.travel.abroad.fragment.TravelGuideFragment;

public class TravelGuideActivity extends AgentActivity
{
  private TravelGuideFragment travelGuideFragment = new TravelGuideFragment();

  protected AgentFragment getAgentFragment()
  {
    return this.travelGuideFragment;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (city() == null)
      finish();
    setTitle(city().name() + "指南攻略");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.abroad.activity.TravelGuideActivity
 * JD-Core Version:    0.6.0
 */