package com.dianping.search.shoplist.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment.InstantiationException;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.Cell;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.basic.AbstractSearchFragment;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.shoplist.activity.AbstractTabListActivity;
import com.dianping.base.shoplist.activity.AbstractTabListActivity.FilterChangeListener;
import com.dianping.base.shoplist.activity.AbstractTabListActivity.GaPager;
import com.dianping.base.shoplist.activity.AbstractTabListActivity.TitleListener;
import com.dianping.base.shoplist.activity.AbstractTabListActivity.TitleTabListener;
import com.dianping.base.shoplist.agentconfig.ShopListAgentConfig;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.ShopListConst;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.fragment.agentconfig.DefaultShopListAgentConfig;
import com.dianping.search.shoplist.fragment.agentconfig.GetAroundShopListAgentConfig;
import com.dianping.search.shoplist.fragment.agentconfig.GlobalShopListAgentConfig;
import com.dianping.search.shoplist.fragment.agentconfig.MallShopListAgentConfig;
import com.dianping.search.shoplist.fragment.agentconfig.NearByShopListAgentConfig;
import com.dianping.search.shoplist.fragment.agentconfig.NearByTopShopListAgentConfig;
import com.dianping.search.shoplist.fragment.agentconfig.QQShopListAgentConfig;
import com.dianping.search.shoplist.fragment.agentconfig.SearchShopListWithBrandAgentConfig;
import com.dianping.search.shoplist.fragment.agentconfig.WeddingShopListAgentConfig;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class ShopListAgentFragment extends AbstractShopListAgentFragment
  implements AbstractTabListActivity.TitleListener, AbstractTabListActivity.TitleTabListener, AbstractTabListActivity.FilterChangeListener, AbstractSearchFragment.OnSearchFragmentListener, AbstractTabListActivity.GaPager
{
  public static final String CONTENT_GUIDE = "shoplist/layguide";
  public static final int SHOP_LIST_TAB = 0;
  public static final int TUAN_LIST_TAB = 1;
  public static final String WEDPRODUCT_FRAME_FILTER = "localshoplist/weddingframefilter760";
  public static final int WED_LIST_TAB = 2;
  final ArrayList<AgentListConfig> agentListConfigs = new ArrayList();
  private ViewGroup contentView;
  int curIndex = 0;
  private ShopListAgentConfig currentAgentListConfig;
  private ViewGroup filterView;
  int lastCityId = 0;
  private ViewGroup layGuideView;
  private View mSearch_keyword_title_keep_show;
  boolean onFocus = false;
  boolean searchAvailable = false;
  private Bundle searchBundle = new Bundle();
  private ViewGroup statusView;
  private String wedProductType;

  private void abortConfigRequests(ShopListAgentConfig paramShopListAgentConfig)
  {
    if (paramShopListAgentConfig != null)
    {
      MApiRequest localMApiRequest = paramShopListAgentConfig.getShopListRequest();
      paramShopListAgentConfig = paramShopListAgentConfig.getShopEventRequest();
      if (localMApiRequest != null)
        mapiService().abort(localMApiRequest, null, true);
      if (paramShopListAgentConfig != null)
        mapiService().abort(paramShopListAgentConfig, null, true);
    }
  }

  private void handleDefaultShopList(Uri paramUri)
  {
    String str1 = paramUri.getQueryParameter("regionid");
    String str2 = paramUri.getQueryParameter("cityid");
    paramUri = paramUri.getQueryParameter("keyword");
    if ((TextUtils.isEmpty(str1)) && (TextUtils.isEmpty(str2)) && (TextUtils.isEmpty(paramUri)))
      handleLocalShoplist();
    while (true)
    {
      return;
      this.GATag = "search";
      try
      {
        if ((!TextUtils.isEmpty(str2)) && (city().id() != Integer.parseInt(str2)))
          this.shopListDataSource.setNeedLocalRegion(false);
        if (location() == null)
          continue;
        this.shopListDataSource.setOffsetGPS(location().offsetLatitude(), location().offsetLongitude());
        return;
      }
      catch (Exception paramUri)
      {
        while (true)
          Log.d("shoplist", "city id not integer");
      }
    }
  }

  private void handleGetAroundShoplist()
  {
    this.GATag = "search";
    this.shopListDataSource.setCurRegion(ShopListConst.GET_AROUND_DEFAULT_REGION);
  }

  private void handleGlobalShopList(Activity paramActivity)
  {
    if ((paramActivity instanceof AbstractTabListActivity))
      ((AbstractTabListActivity)paramActivity).showTabTitle(false);
    this.shopListDataSource.targetPage = "globalshoplist";
    if ((this.shopListDataSource instanceof NewShopListDataSource))
    {
      paramActivity = (NewShopListDataSource)this.shopListDataSource;
      paramActivity.needAdvFilter = true;
      paramActivity.title = "商户列表";
    }
    this.GATag = "global_shop_list";
  }

  private boolean handleKeyword(Bundle paramBundle)
  {
    Intent localIntent = getActivity().getIntent();
    Object localObject3 = null;
    if (localIntent == null)
      return false;
    paramBundle = localIntent.getStringExtra("value");
    Object localObject2 = paramBundle;
    if (paramBundle == null)
      localObject2 = getStringParam("value");
    Object localObject1 = localIntent.getStringExtra("query");
    paramBundle = (Bundle)localObject1;
    if (TextUtils.isEmpty((CharSequence)localObject1))
      paramBundle = localIntent.getStringExtra("keyword");
    localObject1 = paramBundle;
    if (TextUtils.isEmpty(paramBundle))
      localObject1 = localIntent.getData().getQueryParameter("keyword");
    Object localObject4;
    if (TextUtils.isEmpty((CharSequence)localObject1))
    {
      localObject4 = localIntent.getDataString();
      if ((((String)localObject4).equals("没有搜索记录")) || (((String)localObject4).equals("没有相关记录")))
      {
        getActivity().finish();
        return true;
      }
      if (((String)localObject4).startsWith("查找 “"))
      {
        paramBundle = ((String)localObject4).substring("查找 “".length());
        if (!TextUtils.isEmpty(paramBundle))
        {
          localObject1 = paramBundle;
          if (paramBundle.endsWith("”"))
            localObject1 = paramBundle.substring(0, paramBundle.length() - 1);
          localObject3 = localObject1;
          localIntent.putExtra("keyword", (String)localObject1);
          localObject4 = new SearchRecentSuggestions(getActivity(), "com.dianping.app.DianpingSuggestionProvider", 3);
          paramBundle = (Bundle)localObject3;
          if (TextUtils.isEmpty((CharSequence)localObject2))
          {
            ((SearchRecentSuggestions)localObject4).saveRecentQuery((String)localObject1, null);
            paramBundle = (Bundle)localObject3;
          }
        }
      }
    }
    while (true)
    {
      this.shopListDataSource.setSuggestKeyword(paramBundle);
      this.shopListDataSource.setSuggestValue((String)localObject2);
      return false;
      getActivity().finish();
      return true;
      paramBundle = (Bundle)localObject3;
      if (((String)localObject4).startsWith("dianping://"))
        continue;
      localObject1 = localObject4;
      localIntent.putExtra("keyword", (String)localObject4);
      localObject3 = new SearchRecentSuggestions(getActivity(), "com.dianping.app.DianpingSuggestionProvider", 3);
      paramBundle = (Bundle)localObject1;
      if (!TextUtils.isEmpty((CharSequence)localObject2))
        continue;
      ((SearchRecentSuggestions)localObject3).saveRecentQuery((String)localObject4, null);
      paramBundle = (Bundle)localObject1;
      continue;
      if (((String)localObject1).equals("清空搜索记录"))
      {
        new SearchRecentSuggestions(getActivity(), "com.dianping.app.DianpingSuggestionProvider", 3).clearHistory();
        getActivity().finish();
        return true;
      }
      localObject3 = localObject1;
      localIntent.putExtra("keyword", (String)localObject1);
      localObject4 = new SearchRecentSuggestions(getActivity(), "com.dianping.app.DianpingSuggestionProvider", 3);
      paramBundle = (Bundle)localObject3;
      if (!TextUtils.isEmpty((CharSequence)localObject2))
        continue;
      ((SearchRecentSuggestions)localObject4).saveRecentQuery((String)localObject1, null);
      paramBundle = (Bundle)localObject3;
    }
  }

  private void handleLocalShoplist()
  {
    this.GATag = "nearby";
    if (isCurrentCity())
    {
      this.shopListDataSource.setCurRegion(ShopListConst.NEARBY);
      if (location() != null)
        this.shopListDataSource.setOffsetGPS(location().offsetLatitude(), location().offsetLongitude());
    }
  }

  private void handleOtherShopList()
  {
    if ("qqshoplist".equals(this.host))
    {
      this.GATag = "qqaio6";
      return;
    }
    if ("wxshoplist".equals(this.host))
    {
      this.GATag = "weixinplus6";
      return;
    }
    this.GATag = "search";
  }

  private void parseCommonURL(Activity paramActivity)
  {
    if (paramActivity == null);
    label184: label481: label1650: label1779: 
    do
    {
      Uri localUri;
      do
      {
        return;
        localUri = paramActivity.getIntent().getData();
      }
      while (localUri == null);
      Log.d("debug_url", "url=" + localUri);
      this.host = localUri.getHost();
      if (location() != null)
        this.shopListDataSource.setOffsetGPS(location().offsetLatitude(), location().offsetLongitude());
      if (isCurrentCity())
      {
        this.shopListDataSource.setShowDistance(true);
        if (("qqshoplist".equals(this.host)) || ("wxshoplist".equals(this.host)))
          this.shopListDataSource.setNeedLocalRegion(false);
      }
      while (true)
      {
        Object localObject;
        int i;
        if ("shoplist".equals(this.host))
        {
          this.targetPage = localUri.getQueryParameter("target");
          this.shopListDataSource.targetPage = this.targetPage;
          if (TextUtils.isEmpty(this.targetPage))
          {
            handleDefaultShopList(localUri);
            paramActivity = localUri.getQueryParameter("ismetro");
            if ((!TextUtils.isEmpty(paramActivity)) && (paramActivity.equals("true")))
              this.shopListDataSource.setIsMetro(true);
            localObject = localUri.getQueryParameter("categoryid");
            paramActivity = (Activity)localObject;
            if (TextUtils.isEmpty((CharSequence)localObject))
              paramActivity = localUri.getQueryParameter("c");
            i = 0;
            if (!TextUtils.isEmpty(paramActivity))
              i = Integer.parseInt(paramActivity);
            if (i >= 0)
            {
              localObject = localUri.getQueryParameter("categoryname");
              paramActivity = (Activity)localObject;
              if (localObject == null)
                paramActivity = "";
              if (i == 0)
                paramActivity = "全部分类";
              this.shopListDataSource.setCurCategory(new DPObject("Category").edit().putInt("ID", i).putString("Name", paramActivity).putInt("ParentID", -1).putInt("Distance", 500).generate());
            }
            localObject = localUri.getQueryParameter("regionid");
            paramActivity = (Activity)localObject;
            if (TextUtils.isEmpty((CharSequence)localObject))
              paramActivity = localUri.getQueryParameter("r");
            i = 0;
            if (!TextUtils.isEmpty(paramActivity))
              i = Integer.parseInt(paramActivity);
            if (i != 0)
            {
              if (!this.shopListDataSource.isMetro())
                break label1591;
              this.shopListDataSource.setCurMetro(new DPObject("Metro").edit().putInt("ID", i).putString("Name", "").putInt("ParentID", 0).generate());
            }
            paramActivity = localUri.getQueryParameter("sort");
            if (!TextUtils.isEmpty(paramActivity))
              this.shopListDataSource.setCurSort(new DPObject("Pair").edit().putString("ID", paramActivity).putString("Name", "").generate());
            paramActivity = localUri.getQueryParameter("range");
            i = 0;
            if (!TextUtils.isEmpty(paramActivity))
              i = Integer.parseInt(paramActivity);
            if (i <= 0)
              break label1650;
            this.shopListDataSource.setCurRange(new DPObject("Pair").edit().putString("ID", String.valueOf(i)).putString("Name", "" + i + "米").generate());
            if ((i != 0) && (this.shopListDataSource.needLocalRegion))
            {
              localObject = this.shopListDataSource;
              DPObject.Editor localEditor = new DPObject("Region").edit().putInt("ID", i);
              if (i == -1)
                paramActivity = "附近";
              ((BaseShopListDataSource)localObject).setCurRegion(localEditor.putString("Name", paramActivity).putInt("ParentID", -1).generate());
            }
            getActivity().getIntent().putExtra("keyword", localUri.getQueryParameter("q"));
            paramActivity = localUri.getQueryParameter("cityid");
            int j = 0;
            i = j;
            if (!TextUtils.isEmpty(paramActivity))
            {
              i = j;
              if (TextUtils.isDigitsOnly(paramActivity))
                i = Integer.parseInt(paramActivity);
            }
            if (i > 0)
              this.shopListDataSource.setCityId(i);
            paramActivity = localUri.getQueryParameter("minprice");
            if (!TextUtils.isEmpty(paramActivity))
              this.shopListDataSource.setMinPrice(Integer.parseInt(paramActivity));
            paramActivity = localUri.getQueryParameter("maxprice");
            if (!TextUtils.isEmpty(paramActivity))
              this.shopListDataSource.setMaxPrice(Integer.parseInt(paramActivity));
            paramActivity = localUri.getQueryParameter("begindate");
            long l1 = 0L;
            if (!TextUtils.isEmpty(paramActivity))
              l1 = Long.parseLong(paramActivity);
            paramActivity = localUri.getQueryParameter("enddate");
            long l2 = 0L;
            if (!TextUtils.isEmpty(paramActivity))
              l2 = Long.valueOf(paramActivity).longValue();
            if ((l1 != 0L) && (l2 != 0L))
            {
              this.shopListDataSource.setHotelCheckinTime(l1);
              this.shopListDataSource.setHotelCheckoutTime(l2);
            }
            paramActivity = localUri.getQueryParameter("keyword");
            if (!TextUtils.isEmpty(paramActivity))
              this.shopListDataSource.setSuggestKeyword(paramActivity);
            localObject = localUri.getQueryParameter("latitude");
            paramActivity = localUri.getQueryParameter("longitude");
            if ((TextUtils.isEmpty((CharSequence)localObject)) || (TextUtils.isEmpty(paramActivity)));
          }
        }
        try
        {
          d1 = Double.valueOf((String)localObject).doubleValue();
        }
        catch (NumberFormatException localNumberFormatException)
        {
          try
          {
            while (true)
            {
              d2 = Double.valueOf(paramActivity).doubleValue();
              if ((Double.compare(d1, 0.0D) != 0) && (Double.compare(d2, 0.0D) != 0))
                this.shopListDataSource.setCustomGPS(d1, d2);
              paramActivity = localUri.getQueryParameter("placetype");
              if (!TextUtils.isEmpty(paramActivity))
                this.shopListDataSource.setPlaceType(Integer.parseInt(paramActivity));
              paramActivity = localUri.getQueryParameter("fromhome");
              if ((!TextUtils.isEmpty(paramActivity)) && (paramActivity.equals("true")))
                this.shopListDataSource.setIsFromHome(true);
              paramActivity = localUri.getQueryParameter("tab");
              if ((!TextUtils.isEmpty(paramActivity)) && (TextUtils.isDigitsOnly(paramActivity)))
              {
                if (Integer.valueOf(paramActivity).intValue() != 0)
                  break label1723;
                this.shopListDataSource.setCurrentTabIndex(0);
              }
              this.wedProductType = localUri.getQueryParameter("type");
              if (i <= 0)
                break label1767;
              if ((ShopListUtils.isWedProduct(i, this.shopListDataSource.curCategory(), true, this.wedProductType)) && (ShopListUtils.hasWedProductType(this.wedProductType)))
                this.shopListDataSource.setCurrentTabIndex(2);
              paramActivity = localUri.getQueryParameter("productcategoryid");
              if ((!TextUtils.isEmpty(paramActivity)) && (TextUtils.isDigitsOnly(paramActivity)))
                this.shopListDataSource.wedProduct = Integer.valueOf(paramActivity).intValue();
              paramActivity = localUri.getQueryParameter("filter");
              if ((!TextUtils.isEmpty(paramActivity)) && (TextUtils.isDigitsOnly(paramActivity)))
              {
                paramActivity = new DPObject("FilterItem").edit().putInt("FilterId", Integer.valueOf(paramActivity).intValue()).putString("Name", "").putBoolean("isSelected", true).generate();
                this.shopListDataSource.setCurSelectNav(paramActivity);
              }
              paramActivity = localUri.getQueryParameter("filters");
              if (!TextUtils.isEmpty(paramActivity))
                this.shopListDataSource.filters = paramActivity;
              paramActivity = localUri.getQueryParameter("attributes");
              if (!TextUtils.isEmpty(paramActivity))
                this.shopListDataSource.attributes = paramActivity;
              paramActivity = localUri.getQueryParameter("pagemodule");
              if (TextUtils.isEmpty(paramActivity))
                break label1779;
              this.shopListDataSource.pageModule = paramActivity;
              return;
              this.shopListDataSource.setNeedLocalRegion(true);
              break;
              this.shopListDataSource.setShowDistance(false);
              this.shopListDataSource.setNeedLocalRegion(false);
              break;
              if ("localshoplist".equals(this.targetPage))
              {
                handleLocalShoplist();
                break label184;
              }
              if ("gettingaround".equals(this.targetPage))
              {
                handleGetAroundShoplist();
                break label184;
              }
              handleOtherShopList();
              break label184;
              if ("globalshoplist".equals(this.host))
              {
                handleGlobalShopList(paramActivity);
                break label184;
              }
              if ("localshoplist".equals(this.host))
              {
                handleLocalShoplist();
                break label184;
              }
              if ((!"regionshoplist".equals(this.host)) && (!"categoryshoplist".equals(this.host)) && (!"qqshoplist".equals(this.host)) && (!"wxshoplist".equals(this.host)) && (!"searchshoplist".equals(this.host)))
                break label184;
              handleOtherShopList();
              break label184;
              this.shopListDataSource.setCurRegion(new DPObject("Region").edit().putInt("ID", i).putString("Name", "").putInt("ParentID", 0).generate());
              break label481;
              if (i != -1)
                break label647;
              this.shopListDataSource.setCurRange(new DPObject("Pair").edit().putString("ID", String.valueOf(i)).putString("Name", "全城").generate());
              break label647;
              localNumberFormatException = localNumberFormatException;
              double d1 = 0.0D;
            }
          }
          catch (NumberFormatException paramActivity)
          {
            while (true)
            {
              double d2 = 0.0D;
              continue;
              if (Integer.valueOf(paramActivity).intValue() == 1)
              {
                this.shopListDataSource.setCurrentTabIndex(1);
                continue;
              }
              if (Integer.valueOf(paramActivity).intValue() != 2)
                continue;
              this.shopListDataSource.setCurrentTabIndex(2);
              continue;
              i = city().id();
            }
          }
        }
      }
    }
    while (!this.shopListDataSource.isMetro());
    label647: label1591: label1723: label1767: this.shopListDataSource.pageModule = "discover_allmetro";
  }

  protected void addCellToContainerView(String paramString, Cell paramCell)
  {
    if (("shoplist/framefilter".equals(paramString)) || ("localshoplist/weddingframefilter760".equals(paramString)))
    {
      this.filterView.addView(paramCell.view);
      return;
    }
    this.contentView.addView(paramCell.view);
  }

  public void changeViewStatus(int paramInt)
  {
    this.statusView.setVisibility(paramInt);
  }

  public void clearWedProductType()
  {
    this.wedProductType = "";
  }

  protected BaseShopListDataSource createDataSource()
  {
    NewShopListDataSource localNewShopListDataSource = new NewShopListDataSource();
    localNewShopListDataSource.cityName = city().name();
    if (!(this.currentAgentListConfig instanceof NearByTopShopListAgentConfig))
    {
      localNewShopListDataSource.setLastExtraView(R.layout.search_add_shop_item, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          String str1 = "dianping://web?url=http://m.dianping.com/poi/app/shop/addShop?newtoken=!";
          paramView = str1;
          String str2;
          if (ShopListAgentFragment.this.getActivity() != null)
          {
            paramView = str1;
            if (ShopListAgentFragment.this.getActivity().getIntent() != null)
            {
              str2 = ShopListAgentFragment.this.getActivity().getIntent().getStringExtra("keyword");
              paramView = str1;
              if (TextUtils.isEmpty(str2));
            }
          }
          try
          {
            paramView = URLEncoder.encode(str2, "UTF-8");
            paramView = "dianping://web?url=http://m.dianping.com/poi/app/shop/addShop?newtoken=!" + "&shopName=" + paramView;
            ShopListAgentFragment.this.startActivity(paramView);
            return;
          }
          catch (Exception paramView)
          {
            while (true)
            {
              paramView.printStackTrace();
              paramView = str1;
            }
          }
        }
      }
      , getActivity());
      localNewShopListDataSource.setEmptyMsg("没有找到任何商户");
    }
    return localNewShopListDataSource;
  }

  protected MApiRequest createRequest(int paramInt)
  {
    return getCurrentAgentListConfig().createListRequest(paramInt);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    return this.agentListConfigs;
  }

  public ShopListAgentConfig getCurrentAgentListConfig()
  {
    Object localObject = this.agentListConfigs.iterator();
    while (((Iterator)localObject).hasNext())
    {
      AgentListConfig localAgentListConfig = (AgentListConfig)((Iterator)localObject).next();
      if (!localAgentListConfig.shouldShow())
        continue;
      if (this.shopListDataSource != null)
        this.shopListDataSource.isShopNearBy = (localAgentListConfig instanceof NearByShopListAgentConfig);
      localObject = this.currentAgentListConfig;
      this.currentAgentListConfig = ((ShopListAgentConfig)localAgentListConfig);
      if (localObject != this.currentAgentListConfig)
        abortConfigRequests((ShopListAgentConfig)localObject);
      return this.currentAgentListConfig;
    }
    return (ShopListAgentConfig)null;
  }

  public String getPageName()
  {
    if ((getCurrentAgentListConfig() instanceof NearByTopShopListAgentConfig))
      return "nearby_headlines";
    return "shoplist";
  }

  public String getWedProductType()
  {
    return this.wedProductType;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    parseCommonURL(getActivity());
    getCurrentAgentListConfig().parseExtraUrl(getActivity(), this.shopListDataSource);
    if (handleKeyword(paramBundle))
    {
      getActivity().finish();
      return;
    }
    if ((getActivity() instanceof AbstractTabListActivity))
    {
      paramBundle = ((AbstractTabListActivity)getActivity()).getKeyword();
      if (TextUtils.isEmpty(paramBundle))
        break label94;
      this.shopListDataSource.setSuggestKeyword(paramBundle);
    }
    while (true)
    {
      resetAgents(null);
      this.shopListDataSource.reload(true);
      return;
      label94: if (TextUtils.isEmpty(this.shopListDataSource.suggestKeyword()))
        continue;
      ((AbstractTabListActivity)getActivity()).setKeyword(this.shopListDataSource.suggestKeyword());
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 64256) && (paramIntent != null))
      onRefreshSearchResult(paramIntent);
  }

  public void onBlur()
  {
    if (this.shopListRequest != null)
    {
      mapiService().abort(this.shopListRequest, this, true);
      this.shopListRequest = null;
    }
    if ((this.onFocus) && (this.shopListDataSource != null))
      this.shopListDataSource.clearFilterFlags();
    this.onFocus = false;
    int i;
    if (((getActivity() instanceof AbstractTabListActivity)) && (getDataSource() != null))
    {
      if (getDataSource().curCategory() == null)
        break label222;
      i = getDataSource().curCategory().getInt("ID");
      Log.d("debug_keyword", "save cate=" + i);
      if (i >= 0)
        ((AbstractTabListActivity)getActivity()).setShopCategoryId(i);
      if (getDataSource().curRegion() != null)
      {
        if (getDataSource().curRegion().getInt("ParentID") != -1)
          break label227;
        ((AbstractTabListActivity)getActivity()).setShopRangeId(getDataSource().curRegion().getInt("ID"));
      }
      label186: if (getDataSource().curSort() == null)
        break label253;
    }
    label222: label227: label253: for (String str = getDataSource().curSort().getString("ID"); ; str = "0")
    {
      ((AbstractTabListActivity)getActivity()).setShopSordId(str);
      return;
      i = -1;
      break;
      ((AbstractTabListActivity)getActivity()).setShopRegionId(getDataSource().curRegion().getInt("ID"));
      break label186;
    }
  }

  public void onCategoryChange(int paramInt)
  {
    if (getDataSource() != null)
    {
      DPObject localDPObject = ShopListUtils.getCurNavById(paramInt, getDataSource().curCategory(), getDataSource().filterCategories(), ShopListConst.ALL_CATEGORY, 0);
      getDataSource().setCurCategory(localDPObject);
      clearWedProductType();
      updateWedTab();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    Object localObject8 = null;
    try
    {
      Object localObject9 = Class.forName("com.dianping.hotel.shoplist.base.HotelShopListAgentConfig");
      Constructor[] arrayOfConstructor = ((Class)localObject9).getDeclaredConstructors();
      Object localObject1 = localObject8;
      if (arrayOfConstructor != null)
      {
        localObject1 = localObject8;
        if (arrayOfConstructor.length > 0)
        {
          localObject9 = ((Class)localObject9).getConstructor(arrayOfConstructor[0].getParameterTypes()).newInstance(new Object[] { this });
          localObject1 = localObject8;
          if ((localObject9 instanceof AgentListConfig))
            localObject1 = (AgentListConfig)localObject9;
        }
      }
      this.agentListConfigs.add(new QQShopListAgentConfig(this));
      this.agentListConfigs.add(new MallShopListAgentConfig(this));
      this.agentListConfigs.add(new GetAroundShopListAgentConfig(this));
      this.agentListConfigs.add(new NearByShopListAgentConfig(this));
      this.agentListConfigs.add(new GlobalShopListAgentConfig(this));
      if (localObject1 != null)
        this.agentListConfigs.add(localObject1);
      this.agentListConfigs.add(new SearchShopListWithBrandAgentConfig(this));
      this.agentListConfigs.add(new WeddingShopListAgentConfig(this));
      this.agentListConfigs.add(new NearByTopShopListAgentConfig(this));
      this.agentListConfigs.add(new DefaultShopListAgentConfig(this));
      super.onCreate(paramBundle);
      return;
    }
    catch (ClassNotFoundException localObject2)
    {
      while (true)
      {
        localClassNotFoundException.printStackTrace();
        Object localObject2 = localObject8;
      }
    }
    catch (InvocationTargetException localObject3)
    {
      while (true)
      {
        localInvocationTargetException.printStackTrace();
        Object localObject3 = localObject8;
      }
    }
    catch (NoSuchMethodException localObject4)
    {
      while (true)
      {
        localNoSuchMethodException.printStackTrace();
        Object localObject4 = localObject8;
      }
    }
    catch (Fragment.InstantiationException localObject5)
    {
      while (true)
      {
        localInstantiationException.printStackTrace();
        Object localObject5 = localObject8;
      }
    }
    catch (IllegalAccessException localObject6)
    {
      while (true)
      {
        localIllegalAccessException.printStackTrace();
        Object localObject6 = localObject8;
      }
    }
    catch (InstantiationException localObject7)
    {
      while (true)
      {
        localInstantiationException1.printStackTrace();
        Object localObject7 = localObject8;
      }
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.shop_list_layout, paramViewGroup, false);
    this.filterView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.filter));
    this.contentView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.content));
    this.statusView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.status));
    this.layGuideView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.lay_guide));
    this.mSearch_keyword_title_keep_show = paramLayoutInflater.findViewById(R.id.search_keyword_title_keep_show);
    setKeyWordsTitle(false);
    this.layGuideView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ViewUtils.hideView(paramView, false);
        if (ShopListAgentFragment.this.getActivity() != null)
          DPActivity.preferences().edit().putBoolean("isNotFirstEntry", true).commit();
      }
    });
    if ((getActivity() != null) && (!DPActivity.preferences().getBoolean("isNotFirstEntry", false)))
    {
      paramViewGroup = getActivity().getIntent().getData().getHost();
      if ((paramViewGroup.equals("qqshoplist")) || (paramViewGroup.equals("wxshoplist")))
      {
        paramLayoutInflater.findViewById(R.id.lay_guide).setVisibility(0);
        return paramLayoutInflater;
      }
      paramLayoutInflater.findViewById(R.id.lay_guide).setVisibility(8);
      return paramLayoutInflater;
    }
    paramLayoutInflater.findViewById(R.id.lay_guide).setVisibility(8);
    return paramLayoutInflater;
  }

  public void onDataChanged(int paramInt)
  {
    if (getActivity() == null);
    do
    {
      return;
      if ((this.currentAgentListConfig instanceof NearByTopShopListAgentConfig))
        ((NearByTopShopListAgentConfig)this.currentAgentListConfig).setDataTag();
      switch (paramInt)
      {
      default:
        return;
      case 1:
        if (this.shopListDataSource.status() != 3)
          continue;
        dispatchAgentChanged("shoplist/contentlist", null);
        this.statusView.setVisibility(8);
        return;
      case 20:
        dispatchAgentChanged("shoplist/navifilter", null);
        return;
      case 10:
        if (this.shopListDataSource.status() == 2)
        {
          if ((this.shopListDataSource instanceof NewShopListDataSource))
            ((NewShopListDataSource)this.shopListDataSource).isCurrentCity = isCurrentCity();
          resetAgents(null);
        }
        this.searchAvailable = true;
        this.statusView.setVisibility(8);
        return;
      case 12:
        if ("shoplist/contentlist" == this.currentAgentListConfig.getContentAgentKey())
        {
          dispatchAgentChanged("shoplist/contentlist", null);
          return;
        }
        dispatchAgentChanged(this.currentAgentListConfig.getContentAgentKey(), null);
        dispatchAgentChanged("shoplist/contentlist", null);
        return;
      case 22:
      }
    }
    while ((this.shopListDataSource.status() != 1) || (this.shopListDataSource.nextStartIndex() != 0));
    this.searchAvailable = false;
    resetAgents(null);
    this.statusView.setVisibility(8);
    return;
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.shopListDataSource.getExternalUrl())));
    getActivity().finish();
  }

  public void onDestroy()
  {
    abortConfigRequests(this.currentAgentListConfig);
    super.onDestroy();
  }

  public void onFocus()
  {
    int i;
    int j;
    if ((!this.onFocus) && ((getActivity() instanceof AbstractTabListActivity)) && (getDataSource() != null))
    {
      localObject = ShopListUtils.getCurNavById(((AbstractTabListActivity)getActivity()).getShopCategoryId(), getDataSource().curCategory(), getDataSource().filterCategories(), ShopListConst.ALL_CATEGORY, 0);
      getDataSource().setCurCategory((DPObject)localObject);
      i = ((AbstractTabListActivity)getActivity()).getShopRegionId();
      j = ((AbstractTabListActivity)getActivity()).getShopRangeId();
      if (j != -2)
        break label191;
    }
    while (true)
    {
      localObject = ShopListUtils.getCurNavById(i, getDataSource().curRegion(), getDataSource().filterRegions(), null, 1);
      getDataSource().setCurRegion((DPObject)localObject);
      localObject = ((AbstractTabListActivity)getActivity()).getShopSortId();
      DPObject localDPObject = getDataSource().curSort();
      if (localDPObject == null)
        break;
      if (!localDPObject.getString("ID").equals(localObject))
      {
        localObject = localDPObject.edit().putString("ID", (String)localObject).generate();
        getDataSource().setCurSort((DPObject)localObject);
      }
      return;
      label191: i = j;
    }
    Object localObject = new DPObject("Pair").edit().putString("ID", (String)localObject).putString("Name", "").putInt("Type", 3).generate();
    Log.d("debug_sort", "cur=" + ((DPObject)localObject).getString("ID") + " default=" + ShopListConst.DEFAULT_SORT.getString("ID"));
    getDataSource().setCurSort((DPObject)localObject);
  }

  public void onKeywordChange(String paramString)
  {
    updateWedTab();
  }

  protected void onRefreshSearchResult(Intent paramIntent)
  {
    Object localObject;
    if (("dianping://shoplist".equals(getActivity().getIntent().getDataString())) || ("dianping://searchshoplist".equals(getActivity().getIntent().getDataString())))
    {
      localObject = (Bundle)paramIntent.getParcelableExtra("app_data");
      if ((localObject == null) || ("com.dianping.action.SHOPLIST".equals(((Bundle)localObject).getString("source"))))
        break label70;
    }
    while (true)
    {
      return;
      label70: getActivity().setIntent(paramIntent);
      try
      {
        localObject = (CellAgent)this.agents.get("shoplist/searchtitle");
        if (localObject == null)
          continue;
        localObject.getClass().getDeclaredMethod("refreshSearchResult", new Class[0]).invoke(localObject, new Object[] { paramIntent.getStringExtra("keyword") });
        return;
      }
      catch (Exception paramIntent)
      {
      }
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    getCurrentAgentListConfig().onRequestFailed(paramMApiRequest, paramMApiResponse);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    getCurrentAgentListConfig().onRequestFinish(paramMApiRequest, paramMApiResponse);
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    this.requestId = UUID.randomUUID().toString();
  }

  public void onResume()
  {
    if ((this.lastCityId > 0) && (cityId() != this.lastCityId))
      getActivity().finish();
    this.lastCityId = cityId();
    super.onResume();
  }

  public void onSearchClick()
  {
    if (!this.searchAvailable)
      return;
    this.currentAgentListConfig.createSuggestFragment(getActivity(), this.shopListDataSource).setOnSearchFragmentListener(this);
  }

  public void onSearchFragmentDetach()
  {
  }

  public void onTabChange(int paramInt)
  {
    if (this.shopListDataSource == null)
    {
      this.onFocus = true;
      return;
    }
    if (paramInt == 2)
    {
      this.shopListDataSource.setCurrentTabIndex(2);
      this.searchAvailable = true;
      resetAgents(null);
    }
    while (true)
    {
      this.curIndex = paramInt;
      return;
      if (paramInt != 0)
        continue;
      clearWedProductType();
      this.shopListDataSource.setCurrentTabIndex(0);
      if (this.curIndex == 2)
      {
        resetAgents(null);
        if (!(getCurrentAgentListConfig() instanceof DefaultShopListAgentConfig))
        {
          this.shopListDataSource.reset(true);
          this.shopListDataSource.reload(false);
        }
      }
      if ((!this.onFocus) || ((this.curIndex == 2) && ((getCurrentAgentListConfig() instanceof DefaultShopListAgentConfig))))
      {
        this.searchBundle.clear();
        this.searchBundle.putBoolean("onFocus", true);
        dispatchAgentChanged("shoplist/searchtitle", this.searchBundle);
      }
      this.onFocus = true;
    }
  }

  protected void resetAgentContainerView()
  {
    this.contentView.removeAllViews();
    this.filterView.removeAllViews();
  }

  public void setKeyWordsTitle(boolean paramBoolean)
  {
    if (this.mSearch_keyword_title_keep_show == null)
      return;
    if (paramBoolean)
    {
      this.mSearch_keyword_title_keep_show.setVisibility(0);
      return;
    }
    this.mSearch_keyword_title_keep_show.setVisibility(8);
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    Object localObject = paramDPObject.getString("Url");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      startActivity((String)localObject);
      return;
    }
    localObject = Uri.parse("dianping://shoplist").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("tab", String.valueOf(0));
    String str = paramDPObject.getString("Keyword");
    if (!TextUtils.isEmpty(str))
      ((Uri.Builder)localObject).appendQueryParameter("keyword", str);
    if (!TextUtils.isEmpty(this.shopListDataSource.targetPage))
      ((Uri.Builder)localObject).appendQueryParameter("target", this.shopListDataSource.targetPage);
    localObject = new Intent("android.intent.action.VIEW", Uri.parse(((Uri.Builder)localObject).toString()));
    paramDPObject = paramDPObject.getString("Value");
    if (!TextUtils.isEmpty(paramDPObject))
      ((Intent)localObject).putExtra("value", paramDPObject);
    startActivity((Intent)localObject);
  }

  public boolean updateWedTab()
  {
    if (!(getActivity() instanceof AbstractTabListActivity))
      return false;
    ShopListTabView localShopListTabView = ((AbstractTabListActivity)getActivity()).getTabView();
    if (localShopListTabView == null)
      return false;
    if (!TextUtils.isEmpty(((AbstractTabListActivity)getActivity()).getKeyword()))
    {
      localShopListTabView.setMidTitleText("");
      return false;
    }
    if (ShopListUtils.isWedProduct(cityId(), getDataSource().curCategory(), false, this.wedProductType))
    {
      localShopListTabView.setMidTitleText("套餐");
      return true;
    }
    localShopListTabView.setMidTitleText("");
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.ShopListAgentFragment
 * JD-Core Version:    0.6.0
 */