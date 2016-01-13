package com.dianping.hotel.deal.activity;

import android.os.Bundle;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.hotel.deal.fragment.HotelProdInfoAgentFragment;

public class HotelProdInfoAgentActivity extends TuanAgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new HotelProdInfoAgentFragment();
    return this.mFragment;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("");
  }

  protected void onDestroy()
  {
    super.onDestroy();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.activity.HotelProdInfoAgentActivity
 * JD-Core Version:    0.6.0
 */