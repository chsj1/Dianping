package com.dianping.tuan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.fragment.BaseTuanFragment;
import com.dianping.model.City;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TuanHomeFragment extends BaseTuanFragment
  implements CityConfig.SwitchListener
{
  private DealMainFragment dealMainFragment;
  private TuanHomeAgentFragment tuanHomeAgentFragment;

  private void switMode()
  {
    if (!isAdded())
      return;
    FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
    if (city().isTuan())
    {
      localFragmentTransaction.hide(this.dealMainFragment);
      localFragmentTransaction.show(this.tuanHomeAgentFragment);
    }
    while (true)
    {
      localFragmentTransaction.commitAllowingStateLoss();
      return;
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://home")));
    }
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    switMode();
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    switMode();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    cityConfig().addListener(this);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.tuan_home_fragment, paramViewGroup, false);
    this.dealMainFragment = ((DealMainFragment)getChildFragmentManager().findFragmentById(R.id.deal_main_fragment));
    this.tuanHomeAgentFragment = ((TuanHomeAgentFragment)getChildFragmentManager().findFragmentById(R.id.tuan_home_agent_fragment));
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    cityConfig().removeListener(this);
  }

  public void onHiddenChanged(boolean paramBoolean)
  {
    super.onHiddenChanged(paramBoolean);
    if (!paramBoolean)
      new Handler().postDelayed(new TuanHomeFragment.1(this), 500L);
  }

  public void setFilterSelectedNavs(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3, String paramString)
  {
    if ((this.dealMainFragment != null) && (this.dealMainFragment.isVisible()))
      if ((!this.dealMainFragment.setExtraFilterString(paramString)) && (!this.dealMainFragment.setSelectedNavs(paramDPObject1, paramDPObject2, paramDPObject3)))
        break label71;
    label71: for (int i = 1; ; i = 0)
    {
      if ((i != 0) && (this.dealMainFragment.getDealAdapter() != null))
        this.dealMainFragment.getDealAdapter().reset();
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.TuanHomeFragment
 * JD-Core Version:    0.6.0
 */