package com.dianping.search.shoplist.fragment.agentconfig;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.shoplist.activity.AbstractTabListActivity;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.fragment.AbstractShopListAgentFragment;
import com.dianping.base.shoplist.util.ShopListUtils;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.base.widget.ShopListTabView;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.search.shoplist.agent.WeddingProductShopListAgent;
import com.dianping.search.shoplist.agent.WeddingProductShopListNavigatorFilterAgent;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.base.NShopListAgentConfig;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class WeddingShopListAgentConfig extends NShopListAgentConfig
{
  public static final String CATEGORY_LIST = "CategoryList";
  public static final String COVER_STYLE_TYPE = "CoverStyleType";
  public static final String CURRENT_CATEGORY = "curCategory";
  public static final String CURRENT_PHOTO_LOC = "curPhotoLoc";
  public static final String CURRENT_REGION = "curRegion";
  public static final String CURRENT_SORT = "curSort";
  public static final String EMPTY_MSG = "EmptyMsg";
  public static final String ERROR_MSG = "ErrorMsg";
  public static final String IS_END = "IsEnd";
  public static final String PAGE = "Page";
  public static final String PHOTO_LOC_LIST = "PhotoLocList";
  public static final String REGION_LIST = "RegionList";
  public static final String SHOP_LIST = "List";
  public static final String SORT_LIST = "SortList";
  public static final String TAG_VALUE = "tagValue";
  public static final String WEDDING_SHOP_REQUEST_URL = "http://m.api.dianping.com/wedding/searchproduct.bin";
  private static Map<String, Class<? extends CellAgent>> map = new LinkedHashMap();
  private Map<Integer, Integer> categoryMap;
  private MApiRequest mRequest;
  private ShopListAgentFragment mShopListAgentFragment;

  static
  {
    map.put("shoplist/weddingtypecontent", WeddingProductShopListAgent.class);
    map.put("shoplist/weddingnavigatorfilter", WeddingProductShopListNavigatorFilterAgent.class);
  }

  public WeddingShopListAgentConfig(ShopListAgentFragment paramShopListAgentFragment)
  {
    super(paramShopListAgentFragment);
    this.mShopListAgentFragment = paramShopListAgentFragment;
  }

  @SuppressLint({"NewApi"})
  public MApiRequest createListRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/searchproduct.bin").buildUpon();
    Object localObject = (DPObject)this.mShopListAgentFragment.sharedObject("curCategory");
    if ((localObject == null) || (((DPObject)localObject).getInt("ID") == 0))
      return null;
    if (this.mShopListAgentFragment.sharedObject("curRegion") != null)
      localBuilder.appendQueryParameter("regionid", String.valueOf(((DPObject)this.mShopListAgentFragment.sharedObject("curRegion")).getString("ID")));
    int i = ((DPObject)localObject).getInt("ID");
    int j = ((DPObject)localObject).getInt("ProductCategoryID");
    if ((i == 163) && (j == 1632))
    {
      if (Build.VERSION.SDK_INT >= 11)
        localBuilder.clearQuery();
      if (this.mShopListAgentFragment.sharedObject("curPhotoLoc") != null)
        localBuilder.appendQueryParameter("photoloc", ((DPObject)this.mShopListAgentFragment.sharedObject("curPhotoLoc")).getString("ID"));
    }
    localBuilder.appendQueryParameter("categoryid", String.valueOf(i));
    if (j > 0)
      localBuilder.appendQueryParameter("productcategoryid", String.valueOf(j));
    if (this.mShopListAgentFragment.sharedObject("Page") != null)
    {
      String str = String.valueOf(this.mShopListAgentFragment.sharedObject("Page"));
      localObject = str;
      if ("0".equals(str))
        localObject = "1";
      localBuilder.appendQueryParameter("page", (String)localObject);
    }
    if (!TextUtils.isEmpty(this.shopListAgentFragment.accountService().token()))
      localBuilder.appendQueryParameter("token", this.shopListAgentFragment.accountService().token());
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    if (this.mShopListAgentFragment.sharedObject("curSort") != null)
      localBuilder.appendQueryParameter("sortid", String.valueOf(((DPObject)this.mShopListAgentFragment.sharedObject("curSort")).getString("ID")));
    if (this.mShopListAgentFragment.sharedObject("tagValue") != null)
      localBuilder.appendQueryParameter("tagValue", String.valueOf(((DPObject)this.mShopListAgentFragment.sharedObject("tagValue")).getString("parameters")));
    if (!TextUtils.isEmpty(this.shopListAgentFragment.getDataSource().pageModule))
      localBuilder.appendQueryParameter("pagemodule", this.shopListAgentFragment.getDataSource().pageModule);
    Log.d("wedding_type_request", localBuilder.toString());
    this.mRequest = new BasicMApiRequest(localBuilder.toString(), "GET", null, CacheType.NORMAL, false, null);
    return (MApiRequest)this.mRequest;
  }

  public Map<String, AgentInfo> getAgentInfoList()
  {
    return null;
  }

  public Map<String, Class<? extends CellAgent>> getAgentList()
  {
    return map;
  }

  public CustomImageButton getFilterButton()
  {
    return (CustomImageButton)this.mShopListAgentFragment.getActivity().findViewById(R.id.right_btn);
  }

  public View getFilterView()
  {
    return this.mShopListAgentFragment.contentView().findViewById(R.id.filter);
  }

  public MApiRequest getShopListRequest()
  {
    return this.mRequest;
  }

  public View getTitleBar()
  {
    return this.mShopListAgentFragment.getActivity().findViewById(R.id.tab_title_layout);
  }

  public void parseData(DPObject paramDPObject)
  {
    parseData(paramDPObject, true);
  }

  public void parseData(DPObject paramDPObject, boolean paramBoolean)
  {
    Object localObject1;
    Object localObject3;
    Object localObject2;
    int i;
    int j;
    if (paramDPObject.getInt("Page") == 1)
    {
      localObject1 = paramDPObject.getObject("SearchNav");
      localObject3 = ((DPObject)localObject1).getArray("CategoryList");
      localObject2 = new ArrayList();
      int k = localObject3.length;
      i = 0;
      Object localObject4;
      while (i < k)
      {
        localObject4 = localObject3[i];
        int m = ((DPObject)localObject4).getInt("ID");
        ((ArrayList)localObject2).add(new DPObject().edit().putInt("ParentID", 0).putInt("ID", m).putString("Name", ((DPObject)localObject4).getString("Name")).putInt("ProductCategoryID", ((DPObject)localObject4).getInt("ProductCategoryID")).putInt("Type", ((DPObject)localObject4).getInt("Type")).putInt("ProductCategoryID", ((DPObject)localObject4).getInt("ProductCategoryID")).putInt("Deep", ((DPObject)localObject4).getInt("Deep")).putArray("SubCategories", ((DPObject)localObject4).getArray("SubCategories")).generate());
        if (((DPObject)localObject4).getArray("SubCategories") != null)
        {
          localObject4 = ((DPObject)localObject4).getArray("SubCategories");
          int n = localObject4.length;
          j = 0;
          while (j < n)
          {
            Object localObject5 = localObject4[j];
            ((ArrayList)localObject2).add(new DPObject().edit().putInt("ParentID", m).putInt("ID", localObject5.getInt("ID")).putString("Name", localObject5.getString("Name")).putInt("ProductCategoryID", localObject5.getInt("ProductCategoryID")).putInt("Type", localObject5.getInt("Type")).putInt("ProductCategoryID", localObject5.getInt("ProductCategoryID")).putInt("Deep", localObject5.getInt("Deep")).putArray("SubCategories", localObject5.getArray("SubCategories")).generate());
            j += 1;
          }
        }
        i += 1;
      }
      localObject3 = ((ArrayList)localObject2).iterator();
      while (((Iterator)localObject3).hasNext())
      {
        localObject4 = (DPObject)((Iterator)localObject3).next();
        if (((DPObject)localObject4).getInt("Type") != 1)
          continue;
        this.mShopListAgentFragment.setSharedObject("curCategory", localObject4);
      }
      localObject3 = new DPObject[((ArrayList)localObject2).size()];
      ((ArrayList)localObject2).toArray(localObject3);
      this.mShopListAgentFragment.setSharedObject("CategoryList", localObject3);
      ((ArrayList)localObject2).clear();
      localObject2 = ((DPObject)localObject1).getArray("RegionList");
      this.mShopListAgentFragment.setSharedObject("RegionList", localObject2);
      j = localObject2.length;
      i = 0;
      if (i < j)
      {
        localObject3 = localObject2[i];
        if (((DPObject)localObject3).getInt("Type") != 1)
          break label788;
        this.mShopListAgentFragment.setSharedObject("curRegion", localObject3);
      }
      localObject2 = ((DPObject)localObject1).getArray("PhotoLocList");
      this.mShopListAgentFragment.setSharedObject("PhotoLocList", localObject2);
      localObject2 = ((DPObject)localObject1).getArray("SortList");
      this.mShopListAgentFragment.setSharedObject("SortList", localObject2);
      j = localObject2.length;
      i = 0;
    }
    while (true)
    {
      if (i < j)
      {
        localObject3 = localObject2[i];
        if (((DPObject)localObject3).getInt("Type") == 1)
          this.mShopListAgentFragment.setSharedObject("curSort", localObject3);
      }
      else
      {
        localObject2 = ((DPObject)localObject1).getArray("TagGroupList");
        this.mShopListAgentFragment.setSharedObject("TagGroupList", localObject2);
        localObject1 = ((DPObject)localObject1).getArray("FilterTagGroupList");
        this.mShopListAgentFragment.setSharedObject("FilterTagGroupList", localObject1);
        localObject1 = paramDPObject.getArray("List");
        this.mShopListAgentFragment.setSharedObject("List", localObject1);
        i = paramDPObject.getInt("CoverStyleType");
        this.mShopListAgentFragment.setSharedObject("CoverStyleType", Integer.valueOf(i));
        this.mShopListAgentFragment.setSharedObject("Page", Integer.valueOf(paramDPObject.getInt("Page")));
        this.mShopListAgentFragment.setSharedObject("IsEnd", Boolean.valueOf(paramDPObject.getBoolean("IsEnd")));
        this.mShopListAgentFragment.setSharedObject("ErrorMsg", paramDPObject.getString("ErrorMsg"));
        this.mShopListAgentFragment.setSharedObject("EmptyMsg", paramDPObject.getString("EmptyMsg"));
        if (!paramBoolean)
          this.mShopListAgentFragment.setSharedObject("tagValue", null);
        return;
        label788: i += 1;
        break;
      }
      i += 1;
    }
  }

  public void setCurCategoryToDataSource(int paramInt, String paramString)
  {
    if (this.shopListAgentFragment.getDataSource() == null)
      return;
    String str = paramString;
    DPObject[] arrayOfDPObject;
    int j;
    int i;
    if (TextUtils.isEmpty(paramString))
    {
      str = paramString;
      if (this.shopListAgentFragment.getDataSource().filterCategories() != null)
      {
        arrayOfDPObject = this.shopListAgentFragment.getDataSource().filterCategories();
        j = arrayOfDPObject.length;
        i = 0;
      }
    }
    while (true)
    {
      str = paramString;
      if (i < j)
      {
        str = arrayOfDPObject[i];
        int k = str.getInt("ID");
        str = str.getString("Name");
        if (k != paramInt);
      }
      else
      {
        paramString = new DPObject().edit().putInt("ID", paramInt).putString("Name", str).generate();
        this.shopListAgentFragment.getDataSource().setCurCategory(paramString);
        return;
      }
      i += 1;
    }
  }

  public boolean shouldShow()
  {
    if (this.shopListAgentFragment.getDataSource() == null)
      return false;
    int i = cityId();
    if (this.shopListAgentFragment.getDataSource().curCategory() == null)
      return false;
    int j = this.shopListAgentFragment.getDataSource().curCategory().getInt("ID");
    int k = this.shopListAgentFragment.getDataSource().wedProduct;
    Log.d("debug_wed", "wed product=" + k);
    boolean bool = ShopListUtils.isWedProduct(i, this.shopListAgentFragment.getDataSource().curCategory(), false, this.mShopListAgentFragment.getWedProductType());
    i = this.shopListAgentFragment.getDataSource().currentTabIndex();
    if (this.categoryMap == null)
      this.categoryMap = new HashMap();
    if ((bool) && (i == 2));
    try
    {
      this.shopListAgentFragment.contentView().findViewById(R.id.content).setBackgroundColor(this.shopListAgentFragment.getResources().getColor(17170443));
      label179: if ((this.shopListAgentFragment.updateWedTab()) && (this.shopListAgentFragment.getActivity() != null) && ((this.shopListAgentFragment.getActivity() instanceof AbstractTabListActivity)))
        ((AbstractTabListActivity)this.shopListAgentFragment.getActivity()).getTabView().setCurIndex(i);
      Object localObject = (DPObject)this.mShopListAgentFragment.sharedObject("curCategory");
      if (localObject != null)
      {
        int m = ((DPObject)localObject).getInt("ID");
        if (this.categoryMap.containsKey(Integer.valueOf(m)))
        {
          i = ((Integer)this.categoryMap.get(Integer.valueOf(m))).intValue();
          if (((i == 0) && (m != j)) || ((i != 0) && (m != j) && (i != j)))
          {
            this.shopListAgentFragment.setSharedObject("Page", Integer.valueOf(1));
            localObject = this.shopListAgentFragment.getDataSource().curCategory().getString("Name");
            localObject = new DPObject().edit().putInt("ID", j).putInt("ProductCategoryID", k).putString("Name", (String)localObject).generate();
            this.mShopListAgentFragment.setSharedObject("curCategory", localObject);
            localObject = this.shopListAgentFragment.findAgent("localshoplist/shoplistweddingtypecontent760");
            if (localObject != null)
            {
              this.shopListAgentFragment.setSharedObject("FOCUS_RELOAD", new DPObject());
              ((CellAgent)localObject).onAgentChanged(null);
            }
          }
        }
      }
      while (true)
      {
        return true;
        i = 0;
        break;
        localObject = this.shopListAgentFragment.getDataSource().curCategory().getString("Name");
        localObject = new DPObject().edit().putInt("ID", j).putInt("ProductCategoryID", k).putString("Name", (String)localObject).generate();
        this.mShopListAgentFragment.setSharedObject("curCategory", localObject);
      }
      return false;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      break label179;
    }
  }

  public void storeCategoryAndParent(int paramInt1, int paramInt2)
  {
    this.categoryMap.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.fragment.agentconfig.WeddingShopListAgentConfig
 * JD-Core Version:    0.6.0
 */