package com.dianping.shopinfo.mall.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NavigationDot;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;

public class EventSlidesView extends NovaLinearLayout
  implements ViewPager.OnPageChangeListener
{
  private int NUM_PER_SLIDE = 3;
  private Context context;
  private int currentViewPagerPosition = 1;
  private DPObject[] eventInfoList = new DPObject[this.NUM_PER_SLIDE];
  private SparseArray<NovaRelativeLayout> mPagerAdapterItemTags = new SparseArray();
  private NavigationDot navigationDot;
  private MyPagerAdapter pagerAdapter;
  private ViewPager viewPager;

  public EventSlidesView(Context paramContext)
  {
    super(paramContext, null);
    this.context = paramContext;
    init();
  }

  private void init()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_mall_event_slides_view, this, true);
    this.viewPager = ((ViewPager)findViewById(R.id.poster_slide));
    this.viewPager.setOffscreenPageLimit(3);
    this.viewPager.setOnPageChangeListener(this);
    this.pagerAdapter = new MyPagerAdapter();
    this.viewPager.setAdapter(this.pagerAdapter);
    this.navigationDot = ((NavigationDot)findViewById(R.id.poster_navigation_dots));
    this.navigationDot.setDotNormalId(R.drawable.home_serve_dot);
    this.navigationDot.setDotPressedId(R.drawable.home_serve_dot_pressed);
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
    if ((this.pagerAdapter.getCount() == 1) || (getContext() == null))
      return;
    this.navigationDot.setCurrentIndex(paramInt - 1);
    if (paramInt == this.pagerAdapter.getCount() - 1)
      this.navigationDot.setCurrentIndex(0);
    while (true)
    {
      GAHelper.instance().contextStatisticsEvent(getContext(), "mallevent", null, 0, "slide");
      return;
      if (paramInt == 0)
      {
        this.navigationDot.setCurrentIndex(this.pagerAdapter.getCount() - 3);
        continue;
      }
      this.currentViewPagerPosition = paramInt;
    }
  }

  public void setEventInfoList(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      return;
    this.mPagerAdapterItemTags.clear();
    this.eventInfoList = paramArrayOfDPObject;
    this.pagerAdapter.notifyDataSetChanged();
    if (this.pagerAdapter.getCount() == 1)
    {
      this.navigationDot.setVisibility(8);
      return;
    }
    if (this.currentViewPagerPosition > this.pagerAdapter.getCount() - 2)
      this.currentViewPagerPosition = (this.pagerAdapter.getCount() - 2);
    this.navigationDot.setVisibility(0);
    this.navigationDot.setTotalDot(this.pagerAdapter.getCount() - 2);
    this.viewPager.setCurrentItem(this.currentViewPagerPosition);
  }

  class MyPagerAdapter extends PagerAdapter
  {
    private NetworkImageView eventImage;
    private SparseArray<NovaRelativeLayout> eventSlideItems = new SparseArray();
    private TextView eventSubTitle;
    private TextView eventTitle;
    private ImageView tag;
    private TextView time;

    MyPagerAdapter()
    {
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      paramViewGroup.removeView((View)paramObject);
      EventSlidesView.this.mPagerAdapterItemTags.remove(paramInt);
    }

    public int getCount()
    {
      if (EventSlidesView.this.eventInfoList.length <= 1)
        return 1;
      return EventSlidesView.this.eventInfoList.length + 2;
    }

    public int getItemPosition(Object paramObject)
    {
      int i = 0;
      while (i < EventSlidesView.this.mPagerAdapterItemTags.size())
      {
        if (((View)paramObject).findViewWithTag(EventSlidesView.this.mPagerAdapterItemTags.get(i)) != null)
          return -1;
        i += 1;
      }
      return -2;
    }

    public DPObject getRealObject(int paramInt)
    {
      int i = paramInt;
      if (getCount() != 1)
      {
        if (paramInt != 0)
          break label33;
        i = getCount() - 2 - 1;
      }
      while (true)
      {
        return EventSlidesView.this.eventInfoList[i];
        label33: if (paramInt == getCount() - 1)
        {
          i = 0;
          continue;
        }
        i = paramInt - 1;
      }
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      NovaRelativeLayout localNovaRelativeLayout;
      DPObject localDPObject;
      if (this.eventSlideItems.get(paramInt) == null)
      {
        localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(EventSlidesView.this.getContext()).inflate(R.layout.shopinfo_mall_event_slide_item, null, false);
        this.eventImage = ((NetworkImageView)localNovaRelativeLayout.findViewById(R.id.event_item_icon));
        this.tag = ((ImageView)localNovaRelativeLayout.findViewById(R.id.event_item_status));
        this.eventTitle = ((TextView)localNovaRelativeLayout.findViewById(R.id.event_item_title));
        this.eventSubTitle = ((TextView)localNovaRelativeLayout.findViewById(R.id.event_item_subtitle));
        this.time = ((TextView)localNovaRelativeLayout.findViewById(R.id.event_item_time));
        localDPObject = getRealObject(paramInt);
        if (!TextUtils.isEmpty(localDPObject.getString("Pic")))
          this.eventImage.setImage(localDPObject.getString("Pic"));
        if (!TextUtils.isEmpty(localDPObject.getString("Tag")))
        {
          if (localDPObject.getString("Tag").equals("NEW"))
          {
            this.tag.setVisibility(0);
            this.tag.setImageResource(R.drawable.mall_event_tag_new);
          }
        }
        else
        {
          if (!TextUtils.isEmpty(localDPObject.getString("Title")))
            this.eventTitle.setText(localDPObject.getString("Title"));
          if (!TextUtils.isEmpty(localDPObject.getString("SubTitle")))
            this.eventSubTitle.setText(localDPObject.getString("SubTitle"));
          if (!TextUtils.isEmpty(localDPObject.getString("Time")))
          {
            this.time.setText(localDPObject.getString("Time"));
            this.time.setVisibility(0);
          }
          localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(localDPObject)
          {
            public void onClick(View paramView)
            {
              paramView = this.val$eventInfo.getString("Url");
              if (!TextUtils.isEmpty(paramView))
              {
                Intent localIntent = new Intent("android.intent.action.VIEW");
                localIntent.setData(Uri.parse(paramView));
                EventSlidesView.this.getContext().startActivity(localIntent);
              }
            }
          });
          this.eventSlideItems.put(paramInt, localNovaRelativeLayout);
        }
      }
      while (true)
      {
        localNovaRelativeLayout.setGAString("mallevent", "", paramInt);
        paramViewGroup.addView(localNovaRelativeLayout);
        EventSlidesView.this.mPagerAdapterItemTags.put(paramInt, localNovaRelativeLayout);
        return localNovaRelativeLayout;
        if (!localDPObject.getString("Tag").equals("HOT"))
          break;
        this.tag.setVisibility(0);
        this.tag.setImageResource(R.drawable.mall_event_tag_hot);
        break;
        localNovaRelativeLayout = (NovaRelativeLayout)this.eventSlideItems.get(paramInt);
      }
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.mall.view.EventSlidesView
 * JD-Core Version:    0.6.0
 */