package com.dianping.base.ugc.widget;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
import com.dianping.base.basic.TabPagerFragment;
import com.dianping.base.basic.TabPagerFragment.TabsAdapter;

public class PhotoTabsAdapter extends TabPagerFragment.TabsAdapter
{
  private TabHost mHost;

  public PhotoTabsAdapter(TabPagerFragment paramTabPagerFragment, TabHost paramTabHost, ViewPager paramViewPager)
  {
    super(paramTabPagerFragment, paramTabHost, paramViewPager);
    this.mHost = paramTabHost;
  }

  public void onPageSelected(int paramInt)
  {
    super.onPageSelected(paramInt);
    Object localObject2 = this.mHost.getTabWidget();
    View localView2 = null;
    try
    {
      View localView3 = (View)((TabWidget)localObject2).getParent();
      Object localObject1 = localView2;
      if ((localView3 instanceof HorizontalScrollView))
        localObject1 = (HorizontalScrollView)localView3;
      if (localObject1 == null);
      do
      {
        return;
        localView2 = ((TabWidget)localObject2).getChildTabViewAt(paramInt);
        localObject2 = new int[2];
        localView2.getLocationOnScreen(localObject2);
        paramInt = localView2.getLeft();
        if (localObject2[0] >= 0)
          continue;
        ((HorizontalScrollView)localObject1).smoothScrollTo(paramInt, 0);
      }
      while (localObject2[0] + localView2.getWidth() <= this.mHost.getWidth());
      ((HorizontalScrollView)localObject1).smoothScrollTo(paramInt - this.mHost.getWidth() + localView2.getWidth(), 0);
      return;
    }
    catch (Exception localView1)
    {
      while (true)
        View localView1 = localView2;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.ugc.widget.PhotoTabsAdapter
 * JD-Core Version:    0.6.0
 */