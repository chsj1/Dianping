package com.dianping.search.shoplist.fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.dataservice.mapi.MApiRequest;

public class NearBySearchFragment extends AbstractSearchFragment
  implements CustomGridView.OnItemClickListener
{
  private int mCategoryId = 0;

  public static NearBySearchFragment newInstance(FragmentActivity paramFragmentActivity, int paramInt)
  {
    NearBySearchFragment localNearBySearchFragment = new NearBySearchFragment();
    localNearBySearchFragment.mCategoryId = paramInt;
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localNearBySearchFragment);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commitAllowingStateLoss();
    return localNearBySearchFragment;
  }

  public MApiRequest createRequest(String paramString)
  {
    return null;
  }

  public String getFileName()
  {
    return "find_main_search_fragment,find_tuan_search_fragment";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.NearBySearchFragment
 * JD-Core Version:    0.6.0
 */