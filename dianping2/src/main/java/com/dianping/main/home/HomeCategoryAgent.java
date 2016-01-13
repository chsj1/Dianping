package com.dianping.main.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import com.dianping.app.CityConfig;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.CategoryIconView;
import com.dianping.base.widget.CustomLinearLayout;
import com.dianping.base.widget.CustomLinearLayout.OnItemClickListener;
import com.dianping.cache.DPCache;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.main.guide.SkinManager;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NavigationDot;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeCategoryAgent extends HomeAgent
  implements RequestHandler<MApiRequest, MApiResponse>, AccountListener, HomeAgent.OnCellRefreshListener, HomeAgent.OnCellRetryListener, ViewPager.OnPageChangeListener, CustomLinearLayout.OnItemClickListener
{
  private static final int CATEGORY_ICON_NUM_ALL = 8;
  private static final int CATEGORY_ICON_NUM_PER_LINE = 4;
  private static final DPObject EMPTY_ITEM = new DPObject();
  private static final DPObject FATE_ITEM = new DPObject();
  private int currentViewPagerPosition = 1;
  private Adapter listAdapter;
  private MApiRequest mHomeCategoryRequest;
  View.OnTouchListener mIconOnTouchListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      if (paramMotionEvent.getAction() == 0)
        ((CategoryIconView)paramView.findViewById(16908294)).setColorFilter(-7829368, PorterDuff.Mode.MULTIPLY);
      while (true)
      {
        return false;
        if ((paramMotionEvent.getAction() != 1) && (paramMotionEvent.getAction() != 3))
          continue;
        ((CategoryIconView)paramView.findViewById(16908294)).setColorFilter(null);
      }
    }
  };
  private ArrayList<DPObject> mIconsList = new ArrayList();
  private NavigationDot mNavigationDot;
  private SparseArray<CustomLinearLayout> mPagerAdapterItemTags = new SparseArray();
  private int mRealPosition = -1;
  private BroadcastReceiver mResidenceReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.RESIDENCE_CHANGE".equals(paramIntent.getAction()))
        HomeCategoryAgent.this.sendHomeCategoryRequest();
    }
  };
  private HomeServicePagerAdapter mServiceIconAdapter;
  private ViewPager mServiceIconViewPager;

  public HomeCategoryAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void gaView(int paramInt)
  {
    if (getContext() == null);
    while (true)
    {
      return;
      CustomLinearLayout localCustomLinearLayout = (CustomLinearLayout)this.mPagerAdapterItemTags.get(paramInt);
      this.mRealPosition = -1;
      if (localCustomLinearLayout == null)
        continue;
      ((DPActivity)getContext()).removeGAView("nearby");
      paramInt = 0;
      while (paramInt < localCustomLinearLayout.getChildCount())
      {
        LinearLayout localLinearLayout = (LinearLayout)localCustomLinearLayout.getChildAt(paramInt);
        int i = 0;
        while (i < localLinearLayout.getChildCount())
        {
          if (localLinearLayout.getChildAt(i).getTag() != null)
            ((DPActivity)getContext()).addGAView(localLinearLayout.getChildAt(i), paramInt * 4 + ((this.currentViewPagerPosition - 1) * 8 + i), "home", "home".equals(((DPActivity)getContext()).getPageName()));
          i += 1;
        }
        paramInt += 1;
      }
    }
  }

  private List<DPObject> generateHomePageObjFromDisk(int paramInt)
  {
    return DPCache.getInstance().getParcelableArray(String.valueOf(paramInt) + Environment.versionCode(), "homeIcons", 31539600000L, DPObject.CREATOR);
  }

  private void saveHomePageObjRawData(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject == null)
      return;
    DPCache.getInstance().put(String.valueOf(cityId()) + Environment.versionCode(), "homeIcons", paramArrayOfDPObject, 31539600000L);
  }

  private void stopHomeCategoryRequest()
  {
    if (this.mHomeCategoryRequest != null)
    {
      mapiService().abort(this.mHomeCategoryRequest, this, true);
      this.mHomeCategoryRequest = null;
    }
  }

  private void updateIcons()
  {
    int i;
    if (this.mIconsList.size() == 0)
    {
      i = 0;
      while (i < 8)
      {
        this.mIconsList.add(FATE_ITEM);
        i += 1;
      }
    }
    int j = this.mIconsList.size() % 8;
    if (j > 0)
    {
      i = 0;
      while (i < 8 - j)
      {
        this.mIconsList.add(EMPTY_ITEM);
        i += 1;
      }
    }
    if (this.mServiceIconAdapter != null)
      this.mServiceIconAdapter.notifyDataSetChanged();
  }

  private void updateLocalIcons()
  {
    List localList = generateHomePageObjFromDisk(cityId());
    if (localList != null)
    {
      this.mIconsList.clear();
      this.mIconsList.addAll(localList);
    }
  }

  public ArrayList<DPObject> getsubArray(ArrayList<DPObject> paramArrayList, int paramInt1, int paramInt2)
  {
    Object localObject = null;
    if (paramArrayList == null)
      return localObject;
    int i;
    if (paramInt2 != -1)
    {
      i = paramInt2;
      if (paramInt2 <= paramArrayList.size() - 1);
    }
    else
    {
      i = paramArrayList.size() - 1;
    }
    if (paramInt1 > i)
    {
      Log.e("homecategoryagent,getsubArray error");
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      localObject = localArrayList;
      if (paramInt1 > i)
        break;
      localArrayList.add(paramArrayList.get(paramInt1));
      paramInt1 += 1;
    }
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    boolean bool = this.isRefresh;
    this.isRefresh = false;
    stopHomeCategoryRequest();
    sendHomeCategoryRequest();
    this.isRefresh = bool;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.listAdapter.notifyMergeItemRangeChanged();
    onRefreshComplete();
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    super.onCitySwitched(paramCity1, paramCity2);
    this.mIconsList.clear();
    this.mPagerAdapterItemTags.clear();
    this.currentViewPagerPosition = 1;
    updateLocalIcons();
    updateIcons();
    sendHomeCategoryRequest();
    dispatchAgentChanged(false);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.RESIDENCE_CHANGE");
    getContext().registerReceiver(this.mResidenceReceiver, paramBundle);
    accountService().addListener(this);
    DPApplication.instance().cityConfig().addListener(this);
    updateLocalIcons();
    updateIcons();
    this.listAdapter = new Adapter(null);
    addCell("20Category", this.listAdapter);
  }

  public void onDestroy()
  {
    DPApplication.instance().cityConfig().removeListener(this);
    accountService().removeListener(this);
    getContext().unregisterReceiver(this.mResidenceReceiver);
    stopHomeCategoryRequest();
    super.onDestroy();
  }

  public void onItemClick(LinearLayout paramLinearLayout, View paramView, int paramInt, long paramLong)
  {
    paramLinearLayout = (DPObject)paramView.getTag();
    if (paramLinearLayout == null)
      return;
    startActivity(paramLinearLayout.getString("Url"));
  }

  public void onPageScrollStateChanged(int paramInt)
  {
    if (paramInt == 0)
    {
      if (this.mServiceIconViewPager.getCurrentItem() != 0)
        break label32;
      this.mServiceIconViewPager.setCurrentItem(this.mServiceIconAdapter.getCount() - 2, false);
    }
    label32: 
    do
      return;
    while (this.mServiceIconViewPager.getCurrentItem() != this.mServiceIconAdapter.getCount() - 1);
    this.mServiceIconViewPager.setCurrentItem(1, false);
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
  }

  public void onPageSelected(int paramInt)
  {
    if ((this.mServiceIconAdapter.getCount() == 1) || (getContext() == null))
      return;
    this.mNavigationDot.setCurrentIndex(paramInt - 1);
    if (paramInt == this.mServiceIconAdapter.getCount() - 1)
    {
      this.mRealPosition = paramInt;
      this.mNavigationDot.setCurrentIndex(0);
      return;
    }
    if (paramInt == 0)
    {
      this.mRealPosition = paramInt;
      this.mNavigationDot.setCurrentIndex(this.mServiceIconAdapter.getCount() - 3);
      return;
    }
    GAHelper.instance().contextStatisticsEvent(getContext(), "serviceslide", null, paramInt, "slide");
    this.currentViewPagerPosition = paramInt;
    if (this.mRealPosition != -1)
      paramInt = this.mRealPosition;
    gaView(paramInt);
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void onRefresh()
  {
    this.isRefresh = true;
    stopHomeCategoryRequest();
    sendHomeCategoryRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mHomeCategoryRequest)
    {
      this.mHomeCategoryRequest = null;
      onRefreshComplete();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mHomeCategoryRequest)
    {
      this.mHomeCategoryRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject[]))
      {
        this.mIconsList.clear();
        this.mIconsList.addAll(Arrays.asList((DPObject[])(DPObject[])paramMApiResponse.result()));
        updateIcons();
        saveHomePageObjRawData((DPObject[])(DPObject[])paramMApiResponse.result());
      }
      this.mPagerAdapterItemTags.clear();
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          if (HomeCategoryAgent.this.mServiceIconViewPager != null)
            HomeCategoryAgent.this.gaView(HomeCategoryAgent.this.mServiceIconViewPager.getCurrentItem());
        }
      }
      , 500L);
      dispatchAgentChanged(false);
    }
  }

  public void onResume()
  {
    super.onResume();
    sendHomeCategoryRequest();
  }

  public void onRetry()
  {
    if ((this.mIconsList.size() == 0) || (this.mIconsList.get(0) == FATE_ITEM))
    {
      stopHomeCategoryRequest();
      sendHomeCategoryRequest();
    }
  }

  void sendHomeCategoryRequest()
  {
    if (this.mHomeCategoryRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mindex/indextabicon.api").buildUpon();
    if (!TextUtils.isEmpty(token()))
      localBuilder.appendQueryParameter("token", token());
    Location localLocation = location();
    if (localLocation != null)
    {
      double d1 = localLocation.latitude();
      double d2 = localLocation.longitude();
      if ((d1 != 0.0D) && (d2 != 0.0D))
      {
        localBuilder.appendQueryParameter("lng", Location.FMT.format(d2) + "");
        localBuilder.appendQueryParameter("lat", Location.FMT.format(d1) + "");
      }
      if (localLocation.city() != null)
        localBuilder.appendQueryParameter("loccityid", String.valueOf(localLocation.city().id()));
    }
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    this.mHomeCategoryRequest = getFragment().mapiGet(this, localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mHomeCategoryRequest, this);
    onRefreshRequest();
  }

  public void updateViewPager()
  {
    this.mServiceIconAdapter.notifyDataSetChanged();
    if (this.mServiceIconAdapter.getCount() == 1)
    {
      this.mNavigationDot.setVisibility(8);
      return;
    }
    if (this.currentViewPagerPosition > this.mServiceIconAdapter.getCount() - 2)
      this.currentViewPagerPosition = (this.mServiceIconAdapter.getCount() - 2);
    this.mNavigationDot.setVisibility(0);
    this.mNavigationDot.setTotalDot(this.mServiceIconAdapter.getCount() - 2);
    this.mServiceIconViewPager.setCurrentItem(this.currentViewPagerPosition, false);
  }

  private class Adapter extends HomeAgent.HomeAgentAdapter
  {
    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      return 1;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      HomeCategoryAgent.this.updateViewPager();
    }

    public BasicRecyclerAdapter.BasicHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return new BasicRecyclerAdapter.BasicHolder(HomeCategoryAgent.this.res.inflate(HomeCategoryAgent.this.getContext(), R.layout.home_category, paramViewGroup, false))
      {
        public void init(View paramView)
        {
          HomeCategoryAgent.access$102(HomeCategoryAgent.this, (ViewPager)paramView.findViewById(R.id.serviceslide));
          HomeCategoryAgent.this.mServiceIconViewPager.setOffscreenPageLimit(5);
          HomeCategoryAgent.this.mServiceIconViewPager.setOnPageChangeListener(HomeCategoryAgent.this);
          HomeCategoryAgent.access$302(HomeCategoryAgent.this, (NavigationDot)paramView.findViewById(R.id.category_navigation_dots));
          HomeCategoryAgent.this.mNavigationDot.setDotNormalId(R.drawable.home_serve_dot);
          HomeCategoryAgent.this.mNavigationDot.setDotPressedId(R.drawable.home_serve_dot_pressed);
          HomeCategoryAgent.access$402(HomeCategoryAgent.this, new HomeCategoryAgent.HomeServicePagerAdapter(HomeCategoryAgent.this));
          HomeCategoryAgent.this.mServiceIconViewPager.setAdapter(HomeCategoryAgent.this.mServiceIconAdapter);
          HomeCategoryAgent.this.mPagerAdapterItemTags.clear();
          SkinManager.setCategoryDotSkin(HomeCategoryAgent.this.getContext(), HomeCategoryAgent.this.mNavigationDot);
        }
      };
    }
  }

  public class CategoryAdapter extends BasicAdapter
  {
    private ArrayList<DPObject> mIconArray = null;
    private boolean skinable = false;

    public CategoryAdapter()
    {
    }

    private void setCategory(View paramView, DPObject paramDPObject, int paramInt)
    {
      if (paramDPObject == null)
        return;
      TextView localTextView = (TextView)paramView.findViewById(16908308);
      CategoryIconView localCategoryIconView = (CategoryIconView)paramView.findViewById(16908294);
      ImageView localImageView = (ImageView)paramView.findViewById(R.id.icon_new_hot);
      if (this.skinable)
        SkinManager.setCategoryTextColor(localTextView);
      if (paramDPObject == HomeCategoryAgent.FATE_ITEM)
      {
        localCategoryIconView.setLocalDrawable(HomeCategoryAgent.this.getResources().getDrawable(R.drawable.home_icon_default_big));
        localImageView.setVisibility(8);
        localTextView.setText("");
        paramView.setTag(null);
        return;
      }
      if (paramDPObject == HomeCategoryAgent.EMPTY_ITEM)
      {
        localCategoryIconView.setLocalDrawable(null);
        localImageView.setVisibility(8);
        localTextView.setText("");
        paramView.setTag(null);
        return;
      }
      localTextView.setText(paramDPObject.getString("Title"));
      localCategoryIconView.setImage(paramDPObject.getString("Icon"));
      if ("new".equalsIgnoreCase(paramDPObject.getString("HotName")))
      {
        localImageView.setVisibility(0);
        localImageView.setImageResource(R.drawable.home_icon_new);
      }
      while (true)
      {
        paramView.setTag(paramDPObject);
        ((NovaRelativeLayout)paramView).setGAString(null, paramDPObject.getString("Title"), paramDPObject.getInt("Index"));
        ((NovaRelativeLayout)paramView).gaUserInfo.biz_id = String.valueOf(paramDPObject.getInt("ClickId"));
        paramView.setOnTouchListener(HomeCategoryAgent.this.mIconOnTouchListener);
        return;
        if ("hot".equalsIgnoreCase(paramDPObject.getString("HotName")))
        {
          localImageView.setVisibility(0);
          localImageView.setImageResource(R.drawable.home_icon_hot);
          continue;
        }
        localImageView.setVisibility(8);
      }
    }

    public int getCount()
    {
      if (this.mIconArray != null)
        return this.mIconArray.size();
      return 0;
    }

    public DPObject getItem(int paramInt)
    {
      if ((this.mIconArray != null) && (paramInt < this.mIconArray.size()))
        return (DPObject)this.mIconArray.get(paramInt);
      return null;
    }

    public long getItemId(int paramInt)
    {
      if ((this.mIconArray != null) && (paramInt < this.mIconArray.size()) && (this.mIconArray.get(paramInt) != HomeCategoryAgent.EMPTY_ITEM) && (this.mIconArray.get(paramInt) != HomeCategoryAgent.FATE_ITEM))
        return paramInt;
      return -1L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = getItem(paramInt);
      if (paramInt % 4 == 0)
      {
        paramViewGroup = new LinearLayout(HomeCategoryAgent.this.getContext());
        paramView = HomeCategoryAgent.this.res.inflate(HomeCategoryAgent.this.getContext(), R.layout.category_icon_gird_item, (LinearLayout)paramViewGroup, false);
        ((LinearLayout)paramViewGroup).addView(paramView);
      }
      while (true)
      {
        setCategory(paramView, localDPObject, paramInt);
        return paramViewGroup;
        paramView = HomeCategoryAgent.this.res.inflate(HomeCategoryAgent.this.getContext(), R.layout.category_icon_gird_item, ((CustomLinearLayout)paramViewGroup).getCurSubLinearLayout(), false);
        paramViewGroup = paramView;
      }
    }

    public void setCategoryData(ArrayList<DPObject> paramArrayList)
    {
      this.mIconArray = paramArrayList;
    }

    public void setSkinable(boolean paramBoolean)
    {
      this.skinable = paramBoolean;
    }
  }

  class HomeServicePagerAdapter extends PagerAdapter
  {
    private SparseArray<CustomLinearLayout> linearLayouts = new SparseArray();

    HomeServicePagerAdapter()
    {
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      paramViewGroup.removeView((View)paramObject);
      HomeCategoryAgent.this.mPagerAdapterItemTags.remove(paramInt);
    }

    public int getCount()
    {
      if (HomeCategoryAgent.this.mIconsList.size() <= 8)
        return 1;
      return (int)Math.ceil(HomeCategoryAgent.this.mIconsList.size() / 8.0F) + 2;
    }

    public int getItemPosition(Object paramObject)
    {
      int i = 0;
      while (i < HomeCategoryAgent.this.mPagerAdapterItemTags.size())
      {
        if (((View)paramObject).findViewWithTag(HomeCategoryAgent.this.mPagerAdapterItemTags.get(i)) != null)
          return -1;
        i += 1;
      }
      return -2;
    }

    public ArrayList<DPObject> getPageObjects(int paramInt)
    {
      int i = paramInt;
      if (getCount() != 1)
      {
        if (paramInt != 0)
          break label50;
        i = getCount() - 2 - 1;
      }
      while (true)
      {
        paramInt = i * 8;
        return HomeCategoryAgent.this.getsubArray(HomeCategoryAgent.this.mIconsList, paramInt, paramInt + 8 - 1);
        label50: if (paramInt == getCount() - 1)
        {
          i = 0;
          continue;
        }
        i = paramInt - 1;
      }
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      CustomLinearLayout localCustomLinearLayout;
      if (this.linearLayouts.get(paramInt) == null)
      {
        localCustomLinearLayout = (CustomLinearLayout)HomeCategoryAgent.this.res.inflate(HomeCategoryAgent.this.getContext(), R.layout.home_category_gridview, paramViewGroup, false);
        localCustomLinearLayout.setOnItemClickListener(HomeCategoryAgent.this);
        localCustomLinearLayout.setAdapter(new HomeCategoryAgent.CategoryAdapter(HomeCategoryAgent.this));
        this.linearLayouts.put(paramInt, localCustomLinearLayout);
      }
      while (true)
      {
        HomeCategoryAgent.CategoryAdapter localCategoryAdapter = (HomeCategoryAgent.CategoryAdapter)localCustomLinearLayout.getAdapter();
        SkinManager.setCategorySkin(HomeCategoryAgent.this.getContext(), localCustomLinearLayout, localCategoryAdapter, paramInt, HomeCategoryAgent.this.mServiceIconAdapter.getCount());
        localCategoryAdapter.setCategoryData(getPageObjects(paramInt));
        localCategoryAdapter.notifyDataSetChanged();
        paramViewGroup.addView(localCustomLinearLayout);
        localCustomLinearLayout.setTag(localCustomLinearLayout);
        HomeCategoryAgent.this.mPagerAdapterItemTags.put(paramInt, localCustomLinearLayout);
        return localCustomLinearLayout;
        localCustomLinearLayout = (CustomLinearLayout)this.linearLayouts.get(paramInt);
      }
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeCategoryAgent
 * JD-Core Version:    0.6.0
 */