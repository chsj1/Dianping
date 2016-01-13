package com.dianping.search.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.base.widget.WrapHeightViewPager;
import com.dianping.search.shoplist.data.model.SearchDirectZoneModel;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NavigationDot;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NearbyMallsItem extends NovaLinearLayout
  implements ViewPager.OnPageChangeListener
{
  private final SearchDirectZoneModel EMPTY = new SearchDirectZoneModel();
  private SparseArray<SearchDirectZoneModel> mPagerAdapterItemTags = new SparseArray();
  HashMap<Integer, Integer> mRepeatMap = null;
  private ArrayList<SearchDirectZoneModel> models = new ArrayList();
  private NavigationDot navDots;
  private NearbyMallsPagerAdapter pagerAdapter;
  private WrapHeightViewPager viewPager;

  public NearbyMallsItem(Context paramContext)
  {
    super(paramContext);
  }

  public NearbyMallsItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public NearbyMallsItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private void sendAdGA(int paramInt, Object paramObject)
  {
    if ((paramObject instanceof SearchDirectZoneModel))
    {
      Log.d("debug_AdGA", "Impression-GA-Ext");
      paramObject = (SearchDirectZoneModel)paramObject;
      this.mRepeatMap.put(Integer.valueOf(paramInt), Integer.valueOf(1));
      HashMap localHashMap = new HashMap();
      localHashMap.put("act", "3");
      localHashMap.put("adidx", String.valueOf(paramObject.index + 1));
      AdClientUtils.report(paramObject.mFeedback, localHashMap);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.viewPager = ((WrapHeightViewPager)findViewById(R.id.nearby_malls_slide));
    this.viewPager.setOffscreenPageLimit(5);
    this.viewPager.setOnPageChangeListener(this);
    this.pagerAdapter = new NearbyMallsPagerAdapter();
    this.viewPager.setAdapter(this.pagerAdapter);
    this.navDots = ((NavigationDot)findViewById(R.id.nearby_malls_nav_dots));
    this.navDots.setDotNormalId(R.drawable.home_serve_dot);
    this.navDots.setDotPressedId(R.drawable.home_serve_dot_pressed);
  }

  public void onPageScrollStateChanged(int paramInt)
  {
    if (paramInt == 0)
    {
      if (this.viewPager.getCurrentItem() != 0)
        break label32;
      this.viewPager.setCurrentItem(this.pagerAdapter.getCount() - 2, false);
    }
    label32: 
    do
      return;
    while (this.viewPager.getCurrentItem() != this.pagerAdapter.getCount() - 1);
    this.viewPager.setCurrentItem(1, false);
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
  }

  public void onPageSelected(int paramInt)
  {
    this.navDots.setCurrentIndex(paramInt - 1);
    if (paramInt == this.pagerAdapter.getCount() - 1)
      this.navDots.setCurrentIndex(0);
    SearchDirectZoneModel localSearchDirectZoneModel;
    Object localObject;
    do
    {
      do
      {
        do
        {
          return;
          if (paramInt == 0)
          {
            this.navDots.setCurrentIndex(this.pagerAdapter.getCount() - 3);
            return;
          }
          localSearchDirectZoneModel = (SearchDirectZoneModel)this.models.get(this.pagerAdapter.getModelIndex(paramInt));
        }
        while (localSearchDirectZoneModel == null);
        localObject = new GAUserInfo();
        ((GAUserInfo)localObject).index = Integer.valueOf(localSearchDirectZoneModel.index);
        ((GAUserInfo)localObject).query_id = localSearchDirectZoneModel.queryId;
        ((GAUserInfo)localObject).keyword = localSearchDirectZoneModel.keyword;
        GAHelper.instance().contextStatisticsEvent(getContext(), "direct_zone", (GAUserInfo)localObject, "view");
      }
      while ((TextUtils.isEmpty(localSearchDirectZoneModel.mFeedback)) || (this.mRepeatMap == null));
      localObject = (Integer)this.mRepeatMap.get(Integer.valueOf(paramInt));
      if (localObject != null)
        continue;
      sendAdGA(paramInt, localSearchDirectZoneModel);
      return;
    }
    while (((Integer)localObject).intValue() != 0);
    sendAdGA(paramInt, localSearchDirectZoneModel);
  }

  public void setModels(ArrayList<SearchDirectZoneModel> paramArrayList)
  {
    this.models.clear();
    this.models.addAll(paramArrayList);
    this.mRepeatMap = new HashMap();
    updateViewPager();
  }

  public void updateViewPager()
  {
    this.mPagerAdapterItemTags.clear();
    if (this.models.isEmpty())
    {
      this.models.add(this.EMPTY);
      this.viewPager.setVisibility(8);
      this.navDots.setVisibility(8);
      this.pagerAdapter.notifyDataSetChanged();
      return;
    }
    this.viewPager.setVisibility(0);
    if (this.models.size() == 1)
      this.navDots.setVisibility(8);
    while (true)
    {
      this.viewPager.setCurrentItem(1);
      break;
      this.navDots.setVisibility(0);
      this.navDots.setTotalDot(this.pagerAdapter.getCount() - 2);
    }
  }

  class NearbyMallsPagerAdapter extends PagerAdapter
  {
    SparseArray<SearchDirectZoneItem> views = new SparseArray();

    NearbyMallsPagerAdapter()
    {
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      paramViewGroup.removeView((View)paramObject);
      this.views.remove(paramInt);
    }

    public int getCount()
    {
      if (NearbyMallsItem.this.models.size() == 1)
        return 1;
      return NearbyMallsItem.this.models.size() + 2;
    }

    public int getItemPosition(Object paramObject)
    {
      int i = 0;
      while (i < NearbyMallsItem.this.mPagerAdapterItemTags.size())
      {
        if (((View)paramObject).findViewWithTag(NearbyMallsItem.this.mPagerAdapterItemTags.get(i)) != null)
          return -1;
        i += 1;
      }
      return -2;
    }

    protected int getModelIndex(int paramInt)
    {
      if (getCount() == 1);
      do
      {
        return 0;
        if (paramInt == 0)
          return getCount() - 2 - 1;
      }
      while (paramInt == getCount() - 1);
      return paramInt - 1;
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      SearchDirectZoneItem localSearchDirectZoneItem2 = (SearchDirectZoneItem)this.views.get(paramInt);
      SearchDirectZoneModel localSearchDirectZoneModel = (SearchDirectZoneModel)NearbyMallsItem.this.models.get(getModelIndex(paramInt));
      SearchDirectZoneItem localSearchDirectZoneItem1 = localSearchDirectZoneItem2;
      if (localSearchDirectZoneItem2 == null)
      {
        localSearchDirectZoneItem1 = (SearchDirectZoneItem)View.inflate(NearbyMallsItem.this.getContext(), R.layout.shoplist_direct_zone_layout, null);
        if (localSearchDirectZoneModel != NearbyMallsItem.this.EMPTY)
          localSearchDirectZoneItem1.setDirectZone(localSearchDirectZoneModel, false, false, false);
        this.views.put(paramInt, localSearchDirectZoneItem1);
      }
      paramViewGroup.addView(localSearchDirectZoneItem1);
      localSearchDirectZoneItem1.setTag(localSearchDirectZoneItem1);
      NearbyMallsItem.this.mPagerAdapterItemTags.put(paramInt, localSearchDirectZoneModel);
      return localSearchDirectZoneItem1;
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.NearbyMallsItem
 * JD-Core Version:    0.6.0
 */