package com.dianping.hotel.shopinfo.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.dianping.base.basic.TabPagerFragment;
import com.dianping.base.ugc.photo.ShopPhotoGalleryActivity;
import com.dianping.base.ugc.widget.PhotoTabsAdapter;
import com.dianping.hotel.shopinfo.fragment.HotelPhotoGalleryFragment;
import com.dianping.util.ViewUtils;

public class HotelPhotoGalleryActivity extends ShopPhotoGalleryActivity
{
  protected Class getPhotoGalleryFragment()
  {
    return HotelPhotoGalleryFragment.class;
  }

  protected HotelPhotoTabsAdapter getmTabsAdapter()
  {
    return new HotelPhotoTabsAdapter(this.tabPagerFragment, this.tabPagerFragment.tabHost(), this.tabPagerFragment.viewPager());
  }

  public void onTabChanged(String paramString)
  {
    super.onTabChanged(paramString);
    statisticsEvent("shopinfo5", "shopinfo5_photo_tag", getShopId() + "", 0);
  }

  public class HotelPhotoTabsAdapter extends PhotoTabsAdapter
  {
    private static final int MAX_TAB_COUNT = 5;
    private final FragmentActivity mContext;
    private final TabHost mTabHost;

    public HotelPhotoTabsAdapter(TabPagerFragment paramTabHost, TabHost paramViewPager, ViewPager arg4)
    {
      super(paramViewPager, localViewPager);
      this.mContext = paramTabHost.getActivity();
      this.mTabHost = paramViewPager;
    }

    private void resizeIndicatorWidth()
    {
      TabWidget localTabWidget = this.mTabHost.getTabWidget();
      int i = Math.min(localTabWidget.getTabCount(), 5);
      float f1 = (i * 2 + 1) / (i * 2 * (i + 1));
      float f2 = 1.0F / i;
      int j = ViewUtils.getScreenWidthPixels(this.mContext);
      i = 0;
      if (i < localTabWidget.getTabCount())
      {
        View localView = localTabWidget.getChildTabViewAt(i);
        ViewGroup.LayoutParams localLayoutParams = localView.getLayoutParams();
        CharSequence localCharSequence = ((TextView)localView).getText();
        if ((localCharSequence != null) && (localCharSequence.length() <= 2));
        for (localLayoutParams.width = (int)(j * f1); ; localLayoutParams.width = (int)(j * f2))
        {
          localView.setLayoutParams(localLayoutParams);
          i += 1;
          break;
        }
      }
    }

    public void notifyDataSetChanged()
    {
      super.notifyDataSetChanged();
      resizeIndicatorWidth();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shopinfo.activity.HotelPhotoGalleryActivity
 * JD-Core Version:    0.6.0
 */