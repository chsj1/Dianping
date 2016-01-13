package com.dianping.travel;

import com.dianping.base.basic.TabPagerFragment;

public class RankPagerFragment extends TabPagerFragment
{
  public void onDetach()
  {
    onFragmentDetach();
    dismissProgressDialog();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.RankPagerFragment
 * JD-Core Version:    0.6.0
 */