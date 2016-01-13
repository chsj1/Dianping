package com.dianping.wed.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class HomeMainFragment extends AgentFragment
{
  private int cityId;
  ViewGroup contentView;
  ViewGroup mContainer;
  ViewGroup mEmptyContainer;
  public View mFragmentView;

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new HomeMainFragment.1(this));
    return localArrayList;
  }

  public int getCityId()
  {
    if (this.cityId == 0)
      return cityId();
    return this.cityId;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    TitleBar localTitleBar = ((NovaActivity)getActivity()).getTitleBar();
    super.onActivityCreated(paramBundle);
    localTitleBar.setTitle("家装首页");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.cityId = getIntParam("cityid");
    this.mFragmentView = paramLayoutInflater.inflate(R.layout.wedhome_main_layout, null, false);
    this.contentView = ((ViewGroup)this.mFragmentView.findViewById(16908290));
    this.mEmptyContainer = ((ViewGroup)this.mFragmentView.findViewById(R.id.empty));
    this.mContainer = ((ViewGroup)this.mFragmentView.findViewById(R.id.container));
    this.mContainer.setPadding(0, 0, 0, 0);
    setAgentContainerView(this.contentView);
    return this.mFragmentView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.home.fragment.HomeMainFragment
 * JD-Core Version:    0.6.0
 */