package com.dianping.pm.fragment;

import android.os.Bundle;
import android.view.View;
import com.dianping.base.tuan.fragment.BaseTuanTabPagerFragment;
import com.dianping.v1.R.layout;
import java.util.HashMap;
import java.util.Map;

public class PmOrderTabFragment extends BaseTuanTabPagerFragment
{
  private Map<String, PmOrderListFragment> mProductListFragments;

  private PmOrderListFragment addPmProductTab(String paramString1, int paramInt, String paramString2)
  {
    PmOrderListFragment localPmOrderListFragment = new PmOrderListFragment();
    Bundle localBundle = new Bundle();
    localBundle.putInt("orderFilter", paramInt);
    localBundle.putString("tab", paramString1);
    localPmOrderListFragment.setArguments(localBundle);
    addTab(paramString2, R.layout.tuan_tab_indicator, localPmOrderListFragment, null);
    this.mProductListFragments.put(paramString1, localPmOrderListFragment);
    return localPmOrderListFragment;
  }

  private void setupViews()
  {
    this.mProductListFragments = new HashMap(3);
    this.mProductListFragments.put("unused", addPmProductTab("unused", 0, "未消费"));
    this.mProductListFragments.put("all", addPmProductTab("all", 1, "全部订单"));
  }

  public void onDestroyView()
  {
    if (this.mProductListFragments != null)
      this.mProductListFragments.clear();
    super.onDestroyView();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    if (this.mProductListFragments == null)
      setupViews();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pm.fragment.PmOrderTabFragment
 * JD-Core Version:    0.6.0
 */