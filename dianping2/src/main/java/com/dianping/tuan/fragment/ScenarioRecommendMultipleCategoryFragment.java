package com.dianping.tuan.fragment;

import android.widget.TabHost;
import android.widget.TabWidget;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.fragment.BaseTuanTabPagerFragment;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class ScenarioRecommendMultipleCategoryFragment extends BaseTuanTabPagerFragment
{
  private ArrayList<DPObject> dpScenarioRecommendDealList = new ArrayList();
  private ArrayList<ScenarioRecommendSingleCategoryFragment> recommendCategoryFragmentList = new ArrayList();

  public void setRecommendCategory(ArrayList<DPObject> paramArrayList)
  {
    if ((paramArrayList == null) || (paramArrayList.size() == 0));
    while (true)
    {
      return;
      this.recommendCategoryFragmentList.clear();
      this.dpScenarioRecommendDealList = paramArrayList;
      this.mTabHost.setVisibility(0);
      if (this.mTabHost.getTabWidget().getChildCount() != 0)
        continue;
      int i = 0;
      while (i < this.dpScenarioRecommendDealList.size())
      {
        paramArrayList = ScenarioRecommendSingleCategoryFragment.newInstance(getActivity(), (DPObject)this.dpScenarioRecommendDealList.get(i), i);
        addTab(((DPObject)this.dpScenarioRecommendDealList.get(i)).getString("TabName"), R.layout.tuan_tab_indicator, paramArrayList, null);
        i += 1;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.ScenarioRecommendMultipleCategoryFragment
 * JD-Core Version:    0.6.0
 */