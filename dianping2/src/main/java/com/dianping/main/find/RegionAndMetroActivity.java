package com.dianping.main.find;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaLoadActivity;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.base.widget.ShopListTabView.TabChangeListener;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class RegionAndMetroActivity extends NovaLoadActivity
  implements CustomGridView.OnItemClickListener, ShopListTabView.TabChangeListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CATEGORY_RECOM_URL = "http://m.api.dianping.com/categoryrecom.bin";
  private final String FRAGMENT_TAG_CATEGORY = "category";
  private final String FRAGMENT_TAG_METRO = "metro";
  private final String FRAGMENT_TAG_REGION = "region";
  private boolean categoryFinish = false;
  private DPObject categoryList;
  private ScrollView contentContainer;
  FindCategoryFragment findCategoryFragment;
  FindMetroFragment findMetroFragment;
  FindRegionFragment findRegionFragment;
  FragmentManager fragmentManager;
  FragmentTransaction fragmentTransaction;
  private boolean hasMetro;
  private View header;
  private TextView hot;
  private CustomGridView layRegion;
  private MApiRequest mCategoryRequest;
  private MApiRequest mMetroRequest;
  private MApiRequest mRegionRequest;
  MetroAdapter metroAdapter;
  private boolean metroFinish = false;
  private DPObject[] metroList;
  private DPObject[] metroRecomList;
  RegionAdapter regionAdapter;
  private boolean regionFinish = false;
  private DPObject[] regionList;
  private DPObject[] regionRecomList;
  private String schema;
  private ShopListTabView tabBar;
  private ViewGroup titleView;

  private boolean isCategoryTabSelected()
  {
    return this.tabBar.getCurrentIndex() == 0;
  }

  private boolean isMetroTabSelected()
  {
    return (this.tabBar.getCurrentIndex() == 1) && (this.hasMetro);
  }

  private boolean isRegionTabSelected()
  {
    return ((this.tabBar.getCurrentIndex() == 2) && (this.hasMetro)) || ((this.tabBar.getCurrentIndex() == 1) && (!this.hasMetro));
  }

  private void requestMetro()
  {
    String str = "http://m.api.dianping.com/metrorecom.bin?cityid=" + cityId();
    this.metroFinish = false;
    this.mMetroRequest = BasicMApiRequest.mapiGet(str, CacheType.DAILY);
    mapiService().exec(this.mMetroRequest, this);
  }

  private void requestRegion()
  {
    this.regionFinish = false;
    this.mRegionRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/regionrecom.bin?cityid=" + cityId(), CacheType.DAILY);
    mapiService().exec(this.mRegionRequest, this);
  }

  private void showCategoryFragment()
  {
    FragmentManager localFragmentManager = getSupportFragmentManager();
    FragmentTransaction localFragmentTransaction = localFragmentManager.beginTransaction();
    showHideFragment(localFragmentManager, localFragmentTransaction, "region", false);
    showHideFragment(localFragmentManager, localFragmentTransaction, "metro", false);
    showHideFragment(localFragmentManager, localFragmentTransaction, "category", true);
    localFragmentTransaction.commit();
  }

  private void showHideFragment(FragmentManager paramFragmentManager, FragmentTransaction paramFragmentTransaction, String paramString, boolean paramBoolean)
  {
    paramFragmentManager = paramFragmentManager.findFragmentByTag(paramString);
    if (paramFragmentManager != null)
    {
      if (paramBoolean)
        paramFragmentTransaction.show(paramFragmentManager);
    }
    else
      return;
    paramFragmentTransaction.hide(paramFragmentManager);
  }

  private void showHideMetroHot()
  {
    if ((this.metroRecomList == null) || (this.metroRecomList.length == 0))
    {
      this.hot.setVisibility(8);
      this.layRegion.setVisibility(8);
      return;
    }
    this.hot.setVisibility(0);
    this.layRegion.setVisibility(0);
    this.layRegion.setAdapter(this.metroAdapter);
  }

  private void showHideRegionHot()
  {
    if ((this.regionRecomList == null) || (this.regionRecomList.length == 0))
    {
      this.hot.setVisibility(8);
      this.layRegion.setVisibility(8);
      return;
    }
    this.hot.setVisibility(0);
    this.layRegion.setVisibility(0);
    this.layRegion.setAdapter(this.regionAdapter);
  }

  private void showMetroFragment()
  {
    FragmentManager localFragmentManager = getSupportFragmentManager();
    FragmentTransaction localFragmentTransaction = localFragmentManager.beginTransaction();
    showHideFragment(localFragmentManager, localFragmentTransaction, "region", false);
    showHideFragment(localFragmentManager, localFragmentTransaction, "category", false);
    showHideFragment(localFragmentManager, localFragmentTransaction, "metro", true);
    localFragmentTransaction.commit();
  }

  private void showRegionFragment()
  {
    FragmentManager localFragmentManager = getSupportFragmentManager();
    FragmentTransaction localFragmentTransaction = localFragmentManager.beginTransaction();
    showHideFragment(localFragmentManager, localFragmentTransaction, "metro", false);
    showHideFragment(localFragmentManager, localFragmentTransaction, "category", false);
    showHideFragment(localFragmentManager, localFragmentTransaction, "region", true);
    localFragmentTransaction.commit();
  }

  protected View getLoadingView()
  {
    return getLayoutInflater().inflate(R.layout.loading_item_fullscreen, null);
  }

  public DPObject[] getMetroList()
  {
    return this.metroList;
  }

  public DPObject[] getRegionList()
  {
    return this.regionList;
  }

  public String getSchema()
  {
    return this.schema;
  }

  public ScrollView getScrollView()
  {
    return this.contentContainer;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    this.schema = getIntent().getData().toString();
    super.onCreate(paramBundle);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mRegionRequest != null)
      mapiService().abort(this.mRegionRequest, this, true);
    if (this.mMetroRequest != null)
      mapiService().abort(this.mMetroRequest, this, true);
    if (this.mCategoryRequest != null)
      mapiService().abort(this.mCategoryRequest, this, true);
  }

  public void onItemClick(CustomGridView paramCustomGridView, View paramView, int paramInt, long paramLong)
  {
    if (paramCustomGridView == this.layRegion)
    {
      if (!paramCustomGridView.getAdapter().equals(this.regionAdapter))
        break label79;
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://regionshoplist?categoryid=10&regionid=" + paramLong)));
      statisticsEvent("area5", "area5_hotregion", (String)paramView.getTag(), 0);
    }
    label79: 
    do
      return;
    while (!paramCustomGridView.getAdapter().equals(this.metroAdapter));
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://regionshoplist?ismetro=true&categoryid=10&regionid=" + paramLong)));
    statisticsEvent("area5", "area5_hotmetro", (String)paramView.getTag(), 0);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((this.mMetroRequest == paramMApiRequest) || (this.mRegionRequest == paramMApiRequest))
    {
      paramMApiRequest = "";
      if ((paramMApiResponse.result() instanceof DPObject))
        paramMApiRequest = paramMApiResponse.message().content();
      paramMApiResponse = paramMApiRequest;
      if (TextUtils.isEmpty(paramMApiRequest))
        paramMApiResponse = "请求失败，请稍后再试";
      showError(paramMApiResponse);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      this.layRegion.setOnItemClickListener(this);
      if (paramMApiRequest != this.mRegionRequest)
        break label133;
      this.regionFinish = true;
      this.mRegionRequest = null;
      this.regionList = ((DPObject)paramMApiResponse.result()).getArray("RegionList");
      this.regionRecomList = ((DPObject)paramMApiResponse.result()).getArray("RegionRecomList");
      this.regionAdapter = new RegionAdapter(this.regionRecomList);
      this.findRegionFragment.setData();
      showHideRegionHot();
    }
    while (true)
    {
      if ((this.categoryFinish) && (this.regionFinish) && (this.metroFinish))
        showContent();
      return;
      label133: if (paramMApiRequest == this.mMetroRequest)
      {
        this.metroFinish = true;
        this.mMetroRequest = null;
        this.metroList = ((DPObject)paramMApiResponse.result()).getArray("MetroList");
        this.metroRecomList = ((DPObject)paramMApiResponse.result()).getArray("MetroRecomList");
        this.metroAdapter = new MetroAdapter(this.metroRecomList);
        this.findMetroFragment.setData();
        showHideMetroHot();
        continue;
      }
      if (paramMApiRequest != this.mCategoryRequest)
        continue;
      this.categoryFinish = true;
      if (!(paramMApiResponse.result() instanceof DPObject))
        continue;
      this.categoryList = ((DPObject)paramMApiResponse.result());
      this.findCategoryFragment.setCategoryRecom(this.categoryList);
    }
  }

  public void onTabChanged(int paramInt)
  {
    this.contentContainer.scrollTo(0, 0);
    if (isCategoryTabSelected())
    {
      this.header.setVisibility(8);
      showCategoryFragment();
      statisticsEvent("area5", "area5_filter", "分类", 0);
    }
    do
    {
      return;
      if (!isRegionTabSelected())
        continue;
      this.header.setVisibility(0);
      this.layRegion.setAdapter(this.regionAdapter);
      showRegionFragment();
      statisticsEvent("area5", "area5_filter", "商区", 0);
      return;
    }
    while (!isMetroTabSelected());
    this.header.setVisibility(0);
    this.layRegion.setAdapter(this.metroAdapter);
    if (this.hasMetro)
      showMetroFragment();
    while (true)
    {
      statisticsEvent("area5", "area5_filter", "地铁", 0);
      return;
      showRegionFragment();
    }
  }

  protected void reloadContent()
  {
    this.mCategoryRequest = null;
    this.mRegionRequest = null;
    this.mMetroRequest = null;
    if (!this.categoryFinish)
      requestCategory();
    if (!this.regionFinish)
      requestRegion();
    if (!this.metroFinish)
      requestMetro();
  }

  public void requestCategory()
  {
    if ((city() == null) || (cityId() <= 0))
      return;
    this.categoryFinish = false;
    showLoading("努力加载中……");
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/categoryrecom.bin").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    if ((getAccount() != null) && (!TextUtils.isEmpty(getAccount().token())))
      localBuilder.appendQueryParameter("token", getAccount().token());
    this.mCategoryRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.SERVICE);
    mapiService().exec(this.mCategoryRequest, this);
  }

  public void setTitleBar()
  {
    this.tabBar = ((ShopListTabView)LayoutInflater.from(this).inflate(R.layout.regionmetreo_tab_layout, null));
    if (!this.hasMetro)
    {
      ((TextView)this.tabBar.findViewById(R.id.title2)).setText("商区");
      this.tabBar.findViewById(R.id.tab3).setVisibility(8);
    }
    this.tabBar.setTabChangeListener(this);
    this.titleView.addView(this.tabBar);
    this.titleView.setVisibility(4);
  }

  protected void setupView()
  {
    if (city() != null)
      this.hasMetro = city().isMetroCity();
    super.setContentView(R.layout.region_metro_layout);
    this.layRegion = ((CustomGridView)findViewById(R.id.layRegion));
    this.contentContainer = ((ScrollView)findViewById(R.id.focused));
    this.hot = ((TextView)findViewById(R.id.hot));
    this.header = findViewById(R.id.header);
    this.fragmentManager = getSupportFragmentManager();
    this.fragmentTransaction = this.fragmentManager.beginTransaction();
    this.findCategoryFragment = FindCategoryFragment.newInstance(this.schema);
    this.findRegionFragment = new FindRegionFragment();
    this.findMetroFragment = new FindMetroFragment();
    this.fragmentTransaction.add(R.id.fragment_container, this.findCategoryFragment, "category").add(R.id.fragment_container, this.findRegionFragment, "region").hide(this.findRegionFragment).add(R.id.fragment_container, this.findMetroFragment, "metro").hide(this.findMetroFragment).commit();
    this.titleView = ((ViewGroup)super.getTitleBar().findViewById(R.id.title_bar_content_container));
    this.titleView.removeAllViews();
    setTitleBar();
  }

  protected void showContent()
  {
    super.showContent();
    this.titleView.setVisibility(0);
  }

  class MetroAdapter extends RegionAndMetroActivity.RegionAdapter
  {
    public MetroAdapter(DPObject[] arg2)
    {
      super(arrayOfDPObject);
    }
  }

  class RegionAdapter extends FindConditionsMainAdapter
  {
    public RegionAdapter(DPObject[] arg2)
    {
      super();
      this.moreText = null;
    }

    public int getCount()
    {
      if (this.tableData == null)
        return 0;
      return this.tableData.length;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = getItemName(paramInt);
      if (paramInt % 3 == 0)
      {
        localObject = new TableRow(paramViewGroup.getContext());
        paramViewGroup = (NovaRelativeLayout)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_hot_item, (TableRow)localObject, false);
        TextView localTextView = (TextView)paramViewGroup.findViewById(R.id.text);
        paramViewGroup.findViewById(R.id.icon).setVisibility(8);
        localTextView.setText(paramView);
        ((TableRow)localObject).addView(paramViewGroup);
        paramViewGroup.setTag(paramView);
        return localObject;
      }
      paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_hot_item, ((CustomGridView)paramViewGroup).getCurRow(), false);
      Object localObject = (NovaRelativeLayout)paramViewGroup;
      ((NovaRelativeLayout)localObject).findViewById(R.id.icon).setVisibility(8);
      ((TextView)((NovaRelativeLayout)localObject).findViewById(R.id.text)).setText(paramView);
      ((NovaRelativeLayout)localObject).setTag(paramView);
      return (View)paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.RegionAndMetroActivity
 * JD-Core Version:    0.6.0
 */