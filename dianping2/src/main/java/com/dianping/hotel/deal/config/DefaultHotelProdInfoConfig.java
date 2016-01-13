package com.dianping.hotel.deal.config;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.hotel.deal.fragment.HotelProdInfoAgentFragment;
import java.util.Map;

public class DefaultHotelProdInfoConfig extends BaseHotelProdInfoConfig
{
  public DefaultHotelProdInfoConfig(HotelProdInfoAgentFragment paramHotelProdInfoAgentFragment)
  {
    super(paramHotelProdInfoAgentFragment);
  }

  private void gotoSchemaUrl()
  {
    FragmentActivity localFragmentActivity = this.mFragment.getActivity();
    if ("dianping://tuandeal?id=13684375" != null)
      localFragmentActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal?id=13684375")));
    localFragmentActivity.finish();
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    return null;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    return null;
  }

  public boolean shouldShow()
  {
    gotoSchemaUrl();
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.config.DefaultHotelProdInfoConfig
 * JD-Core Version:    0.6.0
 */