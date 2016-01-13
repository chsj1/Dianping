package com.dianping.search.shoplist.agent;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.shoplist.agent.ShopListAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.WeddingShopListAgentConfig;
import com.dianping.search.util.WedShopListUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class WeddingProductShopListNavigatorFilterAgent extends ShopListAgent
  implements FilterBar.OnItemClickListener
{
  public static final DPObject ALL_CATEGORY;
  public static final DPObject ALL_SHOOTING_REGION = new DPObject("Region").edit().putInt("ID", 0).putString("Name", "全部").putInt("ParentID", 0).generate();
  public static final String CATEGORY_LIST = "CategoryList";
  private static final String CELL_NAVI_FILTER = "020WeddingNavi";
  public static final String CURRENT_CATEGORY = "curCategory";
  public static final String CURRENT_PHOTO_LOC = "curPhotoLoc";
  public static final String CURRENT_REGION = "curRegion";
  public static final String CURRENT_SORT = "curSort";
  public static final DPObject DEFAULT_SORT;
  public static final String FilterTagGroupList = "FilterTagGroupList";
  public static final int HORIZONTAL_NUM = 4;
  public static final String PHOTO_LOC_LIST = "PhotoLocList";
  public static final String REGION_LIST = "RegionList";
  public static final String SORT_LIST = "SortList";
  public static final String TagGroupList = "TagGroupList";
  ArrayList<WedCategory> arrayCategory;
  Button btnClean;
  Button btnSure;
  CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener()
  {
    public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
    {
      paramCompoundButton = (WeddingProductShopListNavigatorFilterAgent.WedCategory)paramCompoundButton.getTag();
      if (paramBoolean);
      for (paramCompoundButton.type = 1; ; paramCompoundButton.type = 0)
      {
        WeddingProductShopListNavigatorFilterAgent.this.changeFilterStatus();
        return;
      }
    }
  };
  protected FilterBar filterBar;
  View.OnClickListener filterClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (paramView == WeddingProductShopListNavigatorFilterAgent.this.btnClean)
      {
        int i = 0;
        if (i < WeddingProductShopListNavigatorFilterAgent.this.arrayCategory.size())
        {
          paramView = (WeddingProductShopListNavigatorFilterAgent.WedCategory)WeddingProductShopListNavigatorFilterAgent.this.arrayCategory.get(i);
          if (paramView.categoryType == 0)
          {
            int j = 0;
            while (j < paramView.cateSecondary.size())
            {
              ((WeddingProductShopListNavigatorFilterAgent.WedSecondartCate)paramView.cateSecondary.get(j)).type = 0;
              j += 1;
            }
            ((WeddingProductShopListNavigatorFilterAgent.WedSecondartCate)paramView.cateSecondary.get(0)).type = 1;
          }
          while (true)
          {
            i += 1;
            break;
            if (paramView.categoryType != 1)
              continue;
            paramView.type = 0;
          }
        }
        WeddingProductShopListNavigatorFilterAgent.this.changeFilterStatus();
        WeddingProductShopListNavigatorFilterAgent.this.ReInitViews();
      }
      do
        return;
      while (paramView != WeddingProductShopListNavigatorFilterAgent.this.btnSure);
      WeddingProductShopListNavigatorFilterAgent.this.filterDialog.dismiss();
      ((ShopListAgentFragment)WeddingProductShopListNavigatorFilterAgent.this.getFragment()).setSharedObject("tagValue", WeddingProductShopListNavigatorFilterAgent.this.composeParameters());
      WeddingProductShopListNavigatorFilterAgent.this.getFragment().setSharedObject("Page", Integer.valueOf(1));
      WeddingProductShopListNavigatorFilterAgent.access$402(WeddingProductShopListNavigatorFilterAgent.this, WedShopListUtils.getWedConfig((ShopListAgentFragment)WeddingProductShopListNavigatorFilterAgent.this.getFragment()).createListRequest());
      WeddingProductShopListNavigatorFilterAgent.this.mapiService().exec(WeddingProductShopListNavigatorFilterAgent.this.mFilterRequest, WeddingProductShopListNavigatorFilterAgent.this.filterHandler);
    }
  };
  FilterDialog filterDialog;
  RequestHandler<MApiRequest, MApiResponse> filterHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      WedShopListUtils.getWedConfig((ShopListAgentFragment)WeddingProductShopListNavigatorFilterAgent.this.getFragment()).parseData((DPObject)paramMApiResponse.result());
      paramMApiRequest = new Bundle();
      paramMApiRequest.putBoolean("shoplist/weddingnavigatorfilter", true);
      WeddingProductShopListNavigatorFilterAgent.this.dispatchAgentChanged("shoplist/weddingtypecontent", paramMApiRequest);
    }
  };
  final FilterDialog.OnFilterListener filterListener = new FilterDialog.OnFilterListener()
  {
    public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
    {
      Object localObject = (DPObject)WeddingProductShopListNavigatorFilterAgent.this.getFragment().sharedObject("curCategory");
      int i = 0;
      int j = 0;
      if (localObject != null)
      {
        i = ((DPObject)localObject).getInt("ID");
        j = ((DPObject)localObject).getInt("ProductCategoryID");
      }
      if ("region".equals(paramFilterDialog.getTag()))
        if ((paramObject instanceof DPObject));
      while (true)
      {
        return;
        if (!WeddingProductShopListNavigatorFilterAgent.this.setCurSelectedItem((DPObject)paramObject, "region"))
        {
          paramFilterDialog.dismiss();
          return;
        }
        localObject = new ArrayList();
        ((List)localObject).add(new BasicNameValuePair("cityid", WeddingProductShopListNavigatorFilterAgent.this.cityId() + ""));
        WeddingProductShopListNavigatorFilterAgent.this.statisticsEvent("shopinfoq", "productsearch_page_area", "", Integer.valueOf(i).intValue(), (List)localObject);
        if ("category".equals(paramFilterDialog.getTag()))
        {
          if (!(paramObject instanceof DPObject))
            continue;
          if (!WeddingProductShopListNavigatorFilterAgent.this.setCurSelectedItem((DPObject)paramObject, "category"))
          {
            paramFilterDialog.dismiss();
            return;
          }
          localObject = (DPObject)paramObject;
          int m = ((DPObject)localObject).getInt("ID");
          int n = ((DPObject)localObject).getInt("ParentID");
          localObject = ((DPObject)localObject).getString("Name");
          if (m == 163)
            localObject = "婚纱摄影";
          int k = m;
          if (n != 0)
          {
            WedShopListUtils.getWedConfig((ShopListAgentFragment)WeddingProductShopListNavigatorFilterAgent.this.getFragment()).storeCategoryAndParent(m, n);
            k = n;
            localObject = "";
          }
          WeddingProductShopListNavigatorFilterAgent.this.setCurCategoryToDataSource(k, (String)localObject);
          ((ShopListAgentFragment)WeddingProductShopListNavigatorFilterAgent.this.getFragment()).setSharedObject("tagValue", null);
          WeddingProductShopListNavigatorFilterAgent.this.filterBar.changeItemVisiable("filter", false);
          WeddingProductShopListNavigatorFilterAgent.this.filterBar.setItem("filter", "筛选");
          localObject = new ArrayList();
          ((List)localObject).add(new BasicNameValuePair("cityid", WeddingProductShopListNavigatorFilterAgent.this.cityId() + ""));
          ((List)localObject).add(new BasicNameValuePair("productcategoryid", j + ""));
          WeddingProductShopListNavigatorFilterAgent.this.statisticsEvent("shopinfoq", "productsearch_page_cagegory", "", Integer.valueOf(k).intValue(), (List)localObject);
        }
        if (!"rank".equals(paramFilterDialog.getTag()))
          break;
        if (!(paramObject instanceof DPObject))
          continue;
        if (!WeddingProductShopListNavigatorFilterAgent.this.setCurSelectedItem((DPObject)paramObject, "rank"))
        {
          paramFilterDialog.dismiss();
          return;
        }
        if (j != 1632)
          break label560;
        paramObject = new ArrayList();
        paramObject.add(new BasicNameValuePair("cityid", WeddingProductShopListNavigatorFilterAgent.this.cityId() + ""));
        WeddingProductShopListNavigatorFilterAgent.this.statisticsEvent("shopinfoq", "productsearch_scrpage_order", "", 0, paramObject);
      }
      while (true)
      {
        WeddingProductShopListNavigatorFilterAgent.this.updateNavs();
        paramFilterDialog.dismiss();
        WeddingProductShopListNavigatorFilterAgent.this.loadData();
        return;
        label560: paramObject = new ArrayList();
        paramObject.add(new BasicNameValuePair("cityid", WeddingProductShopListNavigatorFilterAgent.this.cityId() + ""));
        WeddingProductShopListNavigatorFilterAgent.this.statisticsEvent("shopinfoq", "productsearch_page_order", "", Integer.valueOf(i).intValue(), paramObject);
      }
    }
  };
  View.OnClickListener linearClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = (ToggleButton)paramView.getTag();
      if (!paramView.isChecked());
      for (boolean bool = true; ; bool = false)
      {
        paramView.setChecked(bool);
        return;
      }
    }
  };
  private DPObject mCurCategory;
  private DPObject mCurRegion;
  private DPObject mCurSort;
  private MApiRequest mFilterRequest;
  private MApiRequest mRequest;
  private RequestHandler mRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(Request paramRequest, Response paramResponse)
    {
    }

    public void onRequestFinish(Request paramRequest, Response paramResponse)
    {
      if (paramRequest != WeddingProductShopListNavigatorFilterAgent.this.mRequest)
        return;
      WeddingProductShopListNavigatorFilterAgent.access$202(WeddingProductShopListNavigatorFilterAgent.this, null);
      WedShopListUtils.getWedConfig((ShopListAgentFragment)WeddingProductShopListNavigatorFilterAgent.this.getFragment()).parseData((DPObject)paramResponse.result());
      ((ShopListAgentFragment)WeddingProductShopListNavigatorFilterAgent.this.getFragment()).changeViewStatus(8);
      paramRequest = new Bundle();
      paramRequest.putBoolean("shoplist/weddingnavigatorfilter", true);
      WeddingProductShopListNavigatorFilterAgent.this.dispatchAgentChanged(false);
      WeddingProductShopListNavigatorFilterAgent.this.dispatchAgentChanged("shoplist/weddingtypecontent", paramRequest);
    }
  };
  private WeddingShopListAgentConfig mWeddingShopListAgentConfig;
  View.OnClickListener textListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      Object localObject2 = (WeddingProductShopListNavigatorFilterAgent.WedCategory)paramView.getTag(R.id.wed_tag_category);
      Object localObject1 = (WeddingProductShopListNavigatorFilterAgent.WedSecondartCate)paramView.getTag();
      if (((WeddingProductShopListNavigatorFilterAgent.WedSecondartCate)localObject1).type == 0)
      {
        localObject2 = ((WeddingProductShopListNavigatorFilterAgent.WedCategory)localObject2).cateSecondary.iterator();
        while (((Iterator)localObject2).hasNext())
          ((WeddingProductShopListNavigatorFilterAgent.WedSecondartCate)((Iterator)localObject2).next()).type = 0;
        ((WeddingProductShopListNavigatorFilterAgent.WedSecondartCate)localObject1).type = 1;
        localObject1 = (LinearLayout)paramView.getParent().getParent();
        int i = 0;
        while (i < ((LinearLayout)localObject1).getChildCount())
        {
          localObject2 = (LinearLayout)((LinearLayout)localObject1).getChildAt(i);
          int j = 0;
          while (j < ((LinearLayout)localObject2).getChildCount())
          {
            ((LinearLayout)localObject2).getChildAt(j).setSelected(false);
            j += 1;
          }
          i += 1;
        }
        paramView.setSelected(true);
        WeddingProductShopListNavigatorFilterAgent.this.changeFilterStatus();
      }
    }
  };
  View viewFilter;

  static
  {
    ALL_CATEGORY = new DPObject("Category").edit().putInt("ID", 0).putString("Name", "全部分类").putInt("ParentID", 0).generate();
    DEFAULT_SORT = new DPObject("Pair").edit().putString("ID", "0").putString("Name", "智能排序").putInt("Type", 3).generate();
  }

  public WeddingProductShopListNavigatorFilterAgent(Object paramObject)
  {
    super(paramObject);
    this.res = MyResources.getResource(WeddingProductShopListNavigatorFilterAgent.class);
  }

  private DPObject composeParameters()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    if (i < this.arrayCategory.size())
    {
      int j;
      if (((WedCategory)this.arrayCategory.get(i)).categoryType == 0)
      {
        localStringBuilder.append(((WedCategory)this.arrayCategory.get(i)).ID).append(":");
        j = 0;
        label64: if (j < ((WedCategory)this.arrayCategory.get(i)).cateSecondary.size())
        {
          if (((WedSecondartCate)((WedCategory)this.arrayCategory.get(i)).cateSecondary.get(j)).type != 1)
            break label155;
          localStringBuilder.append(((WedSecondartCate)((WedCategory)this.arrayCategory.get(i)).cateSecondary.get(j)).cateName).append(";");
        }
      }
      while (true)
      {
        i += 1;
        break;
        label155: j += 1;
        break label64;
        localStringBuilder.append(";").append(((WedCategory)this.arrayCategory.get(i)).strID).append(":").append(((WedCategory)this.arrayCategory.get(i)).type);
      }
    }
    return new DPObject().edit().putString("parameters", localStringBuilder.toString()).generate();
  }

  private void setCurCategoryToDataSource(int paramInt, String paramString)
  {
    ShopListAgentFragment localShopListAgentFragment = (ShopListAgentFragment)getFragment();
    if (localShopListAgentFragment.getDataSource() == null)
      return;
    String str = paramString;
    DPObject[] arrayOfDPObject;
    int j;
    int i;
    if (TextUtils.isEmpty(paramString))
    {
      str = paramString;
      if (localShopListAgentFragment.getDataSource().filterCategories() != null)
      {
        arrayOfDPObject = localShopListAgentFragment.getDataSource().filterCategories();
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
        localShopListAgentFragment.getDataSource().setCurCategory(paramString);
        return;
      }
      i += 1;
    }
  }

  private boolean setCurSelectedItem(DPObject paramDPObject, String paramString)
  {
    if ((paramDPObject == null) && (paramString.equals("region")))
    {
      this.mCurRegion = null;
      return true;
    }
    if ((paramDPObject == null) && (paramString.equals("category")))
    {
      this.mCurCategory = null;
      return true;
    }
    if ((paramDPObject == null) && (paramString.equals("rank")))
    {
      this.mCurSort = null;
      return true;
    }
    if ((this.mCurRegion == null) && (paramString.equals("region")))
    {
      this.mCurRegion = paramDPObject;
      setSharedObject("curRegion", this.mCurRegion);
      return true;
    }
    if ((this.mCurCategory == null) && (paramString.equals("category")))
    {
      this.mCurCategory = paramDPObject;
      setSharedObject("curCategory", this.mCurCategory);
      return true;
    }
    if ((this.mCurSort == null) && (paramString.equals("rank")))
    {
      this.mCurSort = paramDPObject;
      setSharedObject("curSort", this.mCurSort);
      return true;
    }
    if (paramString.equals("region"))
    {
      if (paramDPObject.getString("ID").equals(this.mCurRegion.getString("ID")))
        return false;
      this.mCurRegion = paramDPObject;
      setSharedObject("curRegion", this.mCurRegion);
      return true;
    }
    if (paramString.equals("category"))
    {
      if ((paramDPObject.getInt("ID") == this.mCurCategory.getInt("ID")) && (paramDPObject.getInt("ProductCategoryID") == this.mCurCategory.getInt("ProductCategoryID")))
        return false;
      this.mCurCategory = paramDPObject;
      setSharedObject("curCategory", this.mCurCategory);
      return true;
    }
    if (paramString.equals("rank"))
    {
      if (paramDPObject.getString("ID").equals(this.mCurSort.getString("ID")))
        return false;
      this.mCurSort = paramDPObject;
      setSharedObject("curSort", this.mCurSort);
      return true;
    }
    return false;
  }

  void ReInitCategory()
  {
    DPObject[] arrayOfDPObject2 = (DPObject[])(DPObject[])getFragment().sharedObject("TagGroupList");
    DPObject[] arrayOfDPObject1 = (DPObject[])(DPObject[])getFragment().sharedObject("FilterTagGroupList");
    int i;
    WedCategory localWedCategory;
    if (this.arrayCategory == null)
    {
      this.arrayCategory = new ArrayList();
      if (arrayOfDPObject2 != null)
        i = 0;
    }
    else
    {
      while (true)
      {
        if (i >= arrayOfDPObject2.length)
          break label214;
        localWedCategory = new WedCategory();
        localWedCategory.cateName = arrayOfDPObject2[i].getString("Name");
        localWedCategory.ID = arrayOfDPObject2[i].getInt("ID");
        DPObject[] arrayOfDPObject3 = arrayOfDPObject2[i].getArray("TagList");
        int j = 0;
        while (true)
          if ((arrayOfDPObject3 != null) && (j < arrayOfDPObject3.length))
          {
            WedSecondartCate localWedSecondartCate = new WedSecondartCate();
            localWedSecondartCate.cateName = arrayOfDPObject3[j].getString("Name");
            localWedSecondartCate.type = arrayOfDPObject3[j].getInt("Type");
            localWedCategory.cateSecondary.add(localWedSecondartCate);
            j += 1;
            continue;
            this.arrayCategory.clear();
            break;
          }
        this.arrayCategory.add(localWedCategory);
        i += 1;
      }
    }
    label214: if (arrayOfDPObject1 != null)
    {
      i = 0;
      while ((arrayOfDPObject1 != null) && (i < arrayOfDPObject1.length))
      {
        arrayOfDPObject2 = arrayOfDPObject1[i];
        localWedCategory = new WedCategory();
        localWedCategory.strID = arrayOfDPObject1[i].getString("ID");
        localWedCategory.cateName = arrayOfDPObject2.getString("Name");
        localWedCategory.categoryType = 1;
        localWedCategory.type = arrayOfDPObject2.getInt("Type");
        this.arrayCategory.add(localWedCategory);
        i += 1;
      }
    }
  }

  void ReInitViews()
  {
    LinearLayout localLinearLayout = (LinearLayout)this.viewFilter.findViewById(R.id.linearlayout_dialog_filter);
    localLinearLayout.removeAllViews();
    int n = (this.res.getResources().getDisplayMetrics().widthPixels - ViewUtils.dip2px(getContext(), 50.0F)) / 4;
    int i = 0;
    if (i < this.arrayCategory.size())
    {
      Object localObject1 = (WedCategory)this.arrayCategory.get(i);
      if (((WedCategory)localObject1).categoryType == 0)
      {
        localView = this.res.inflate(getContext(), R.layout.wed_filter_combo_view, getParentView(), false);
        ((TextView)localView.findViewById(R.id.textview_filter_category_name)).setText(((WedCategory)localObject1).cateName);
        localObject2 = (LinearLayout)localView.findViewById(R.id.linearlayout_filter_category_content);
        int j;
        int k;
        if (((WedCategory)localObject1).cateSecondary.size() % 4 == 0)
        {
          j = ((WedCategory)localObject1).cateSecondary.size() / 4;
          k = 0;
        }
        while (true)
        {
          if (k >= j)
            break label488;
          localObject3 = new LinearLayout(getContext());
          LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
          ((LinearLayout)localObject3).setOrientation(0);
          int m = 0;
          while (true)
            if ((m < 4) && (k * 4 + m < ((WedCategory)localObject1).cateSecondary.size()))
            {
              Object localObject4 = (WedSecondartCate)((WedCategory)localObject1).cateSecondary.get(k * 4 + m);
              TextView localTextView = new TextView(getContext());
              localTextView.setText(((WedSecondartCate)localObject4).cateName);
              localTextView.setGravity(17);
              localTextView.setTextSize(0, ViewUtils.dip2px(getContext(), 13.0F));
              localTextView.setTextColor(getResources().getColorStateList(R.color.wed_color_filter_list));
              localTextView.setSingleLine(true);
              localTextView.setBackgroundResource(R.drawable.wed_selector_filter_textview);
              localTextView.setTag(R.id.wed_tag_category, localObject1);
              localTextView.setTag(localObject4);
              localTextView.setClickable(true);
              localTextView.setOnClickListener(this.textListener);
              if (((WedSecondartCate)localObject4).type == 1)
                localTextView.setSelected(true);
              localObject4 = new LinearLayout.LayoutParams(n, -2);
              ((LinearLayout.LayoutParams)localObject4).leftMargin = ViewUtils.dip2px(getContext(), 10.0F);
              localTextView.setPadding(0, ViewUtils.dip2px(getContext(), 7.0F), 0, ViewUtils.dip2px(getContext(), 7.0F));
              ((LinearLayout)localObject3).addView(localTextView, (ViewGroup.LayoutParams)localObject4);
              m += 1;
              continue;
              j = ((WedCategory)localObject1).cateSecondary.size() / 4 + 1;
              break;
            }
          if (k > 0)
            localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 10.0F);
          ((LinearLayout)localObject2).addView((View)localObject3, localLayoutParams);
          k += 1;
        }
        label488: localObject1 = this.res.inflate(getContext(), R.layout.wed_view_divider, getParentView(), false);
        localObject3 = new LinearLayout.LayoutParams(-1, -2);
        ((LinearLayout.LayoutParams)localObject3).leftMargin = ViewUtils.dip2px(getContext(), 10.0F);
        ((LinearLayout.LayoutParams)localObject3).topMargin = ViewUtils.dip2px(getContext(), 10.0F);
        ((LinearLayout)localObject2).addView((View)localObject1, (ViewGroup.LayoutParams)localObject3);
        localLinearLayout.addView(localView);
      }
      do
      {
        i += 1;
        break;
      }
      while (((WedCategory)localObject1).categoryType != 1);
      View localView = this.res.inflate(getContext(), R.layout.wed_category_item_checkbox, getParentView(), false);
      ((TextView)localView.findViewById(R.id.category_item_name)).setText(((WedCategory)localObject1).cateName);
      Object localObject2 = (ToggleButton)localView.findViewById(R.id.cb_switch);
      ((ToggleButton)localObject2).setTag(localObject1);
      ((ToggleButton)localObject2).setOnCheckedChangeListener(this.checkListener);
      Object localObject3 = (LinearLayout)localView.findViewById(R.id.linearlayout_checkbox);
      ((LinearLayout)localObject3).setTag(localObject2);
      ((LinearLayout)localObject3).setOnClickListener(this.linearClickListener);
      if (((WedCategory)localObject1).type == 1)
        ((ToggleButton)localObject2).setChecked(true);
      while (true)
      {
        localLinearLayout.addView(localView);
        break;
        ((ToggleButton)localObject2).setChecked(false);
      }
    }
  }

  protected void addFilterItems()
  {
    this.filterBar.addItem("region", "全部");
    this.filterBar.addItem("category", "全部分类");
    this.filterBar.addItem("rank", "智能排序");
    this.filterBar.addItem("filter", "筛选");
    this.filterBar.changeItemVisiable("filter", false);
  }

  void changeFilterStatus()
  {
    int k = 0;
    int j = 0;
    if (j < this.arrayCategory.size())
    {
      WedCategory localWedCategory = (WedCategory)this.arrayCategory.get(j);
      int i;
      if ((localWedCategory.categoryType == 0) && (((WedSecondartCate)localWedCategory.cateSecondary.get(0)).type == 0))
        i = k + 1;
      while (true)
      {
        j += 1;
        k = i;
        break;
        i = k;
        if (localWedCategory.categoryType != 1)
          continue;
        i = k;
        if (localWedCategory.type != 1)
          continue;
        i = k + 1;
      }
    }
    if (k == 0)
    {
      this.filterBar.setItem("filter", "筛选");
      return;
    }
    this.filterBar.setItem("filter", "筛选" + k);
  }

  public void loadData()
  {
    ((ShopListAgentFragment)getFragment()).changeViewStatus(0);
    getFragment().setSharedObject("Page", Integer.valueOf(1));
    this.mRequest = WedShopListUtils.getWedConfig((ShopListAgentFragment)getFragment()).createListRequest();
    mapiService().exec(this.mRequest, this.mRequestHandler);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!(getCurrentAgentConfig() instanceof WeddingShopListAgentConfig))
    {
      removeCell("020WeddingNavi");
      this.filterBar = null;
      return;
    }
    if (this.filterBar == null)
    {
      paramBundle = LayoutInflater.from(getContext()).inflate(R.layout.wed_filter_layout, getParentView(), false);
      this.filterBar = ((FilterBar)paramBundle.findViewById(R.id.filterBar));
      addFilterItems();
      this.filterBar.setOnItemClickListener(this);
      addCell("020WeddingNavi", paramBundle);
    }
    updateNavs();
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if ("region".equals(paramObject))
      if (getSharedObject("RegionList") != null);
    do
      while (true)
      {
        return;
        Object localObject = new ListFilterDialog(getActivity());
        ((ListFilterDialog)localObject).setTag(paramObject);
        ((ListFilterDialog)localObject).setItems((DPObject[])(DPObject[])getSharedObject("RegionList"));
        if (this.mCurRegion == null);
        for (paramObject = DEFAULT_SORT; ; paramObject = this.mCurRegion)
        {
          ((ListFilterDialog)localObject).setSelectedItem(paramObject);
          ((ListFilterDialog)localObject).setOnFilterListener(this.filterListener);
          ((ListFilterDialog)localObject).show(paramView);
          return;
        }
        if ("category".equals(paramObject))
        {
          if (getSharedObject("CategoryList") == null)
            continue;
          localObject = new TwinListFilterDialog(getActivity());
          ((TwinListFilterDialog)localObject).setTag(paramObject);
          ((TwinListFilterDialog)localObject).setOnFilterListener(this.filterListener);
          ((TwinListFilterDialog)localObject).setHasAll(false);
          ((TwinListFilterDialog)localObject).setItems((DPObject[])(DPObject[])getSharedObject("CategoryList"));
          if (this.mCurCategory == null);
          for (paramObject = ALL_CATEGORY; ; paramObject = this.mCurCategory)
          {
            ((TwinListFilterDialog)localObject).setSelectedItem(paramObject);
            ((TwinListFilterDialog)localObject).show(paramView);
            return;
          }
        }
        if (!"rank".equals(paramObject))
          break;
        if (getSharedObject("SortList") == null)
          continue;
        localObject = new ListFilterDialog(getActivity());
        ((ListFilterDialog)localObject).setTag(paramObject);
        ((ListFilterDialog)localObject).setItems((DPObject[])(DPObject[])getSharedObject("SortList"));
        if (this.mCurSort == null);
        for (paramObject = DEFAULT_SORT; ; paramObject = this.mCurSort)
        {
          ((ListFilterDialog)localObject).setSelectedItem(paramObject);
          ((ListFilterDialog)localObject).setOnFilterListener(this.filterListener);
          ((ListFilterDialog)localObject).show(paramView);
          return;
        }
      }
    while ((!"filter".equals(paramObject)) || ((getSharedObject("TagGroupList") == null) && (getSharedObject("FilterTagGroupList") == null)));
    if (this.filterDialog == null)
    {
      this.filterDialog = new FilterDialog(getActivity());
      this.viewFilter = this.res.inflate(getContext(), R.layout.wed_dialog_filter, getParentView(), false);
      this.filterDialog.setFilterView(this.viewFilter);
      this.btnClean = ((Button)this.viewFilter.findViewById(R.id.button_filter_clean));
      this.btnClean.setOnClickListener(this.filterClickListener);
      this.btnSure = ((Button)this.viewFilter.findViewById(R.id.button_filter_sure));
      this.btnSure.setOnClickListener(this.filterClickListener);
    }
    ReInitCategory();
    ReInitViews();
    this.filterDialog.show(paramView);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mRequest != null)
    {
      mapiService().abort(this.mRequest, this.mRequestHandler, true);
      this.mRequest = null;
    }
  }

  public void updateNavs()
  {
    if (getSharedObject("curRegion") != null)
      this.mCurRegion = ((DPObject)getSharedObject("curRegion"));
    if (getSharedObject("curCategory") != null)
      this.mCurCategory = ((DPObject)getSharedObject("curCategory"));
    if (getSharedObject("curSort") != null)
      this.mCurSort = ((DPObject)getSharedObject("curSort"));
    Object localObject2;
    if (this.mCurRegion != null)
    {
      localObject2 = this.mCurRegion.getString("Name");
      localObject1 = localObject2;
      if (!TextUtils.isEmpty((CharSequence)localObject2))
      {
        localObject1 = localObject2;
        if (((String)localObject2).contains("（智能范围）"))
          localObject1 = ((String)localObject2).replace("（智能范围）", "");
      }
      if ((getSharedObject("RegionList") != null) && (((DPObject[])(DPObject[])getSharedObject("RegionList")).length != 0))
        break label264;
      this.filterBar.changeItemVisiable("region", false);
      label151: localObject2 = this.filterBar;
      if (this.mCurCategory != null)
        break label300;
      localObject1 = "全部分类";
      label166: ((FilterBar)localObject2).setItem("category", (String)localObject1);
      localObject2 = this.filterBar;
      if (this.mCurSort != null)
        break label313;
    }
    label264: label300: label313: for (Object localObject1 = "智能排序"; ; localObject1 = this.mCurSort.getString("Name"))
    {
      ((FilterBar)localObject2).setItem("rank", (String)localObject1);
      if (((getSharedObject("TagGroupList") != null) && (((DPObject[])(DPObject[])getSharedObject("TagGroupList")).length != 0)) || ((getSharedObject("FilterTagGroupList") != null) && (((DPObject[])(DPObject[])getSharedObject("FilterTagGroupList")).length != 0)))
        break label326;
      this.filterBar.changeItemVisiable("filter", false);
      return;
      localObject2 = null;
      break;
      this.filterBar.changeItemVisiable("region", true);
      FilterBar localFilterBar = this.filterBar;
      localObject2 = localObject1;
      if (localObject1 == null)
        localObject2 = "全部";
      localFilterBar.setItem("region", (String)localObject2);
      break label151;
      localObject1 = this.mCurCategory.getString("Name");
      break label166;
    }
    label326: this.filterBar.changeItemVisiable("filter", true);
    ReInitCategory();
  }

  public class WedCategory
  {
    public int ID;
    public String cateName;
    public ArrayList<WeddingProductShopListNavigatorFilterAgent.WedSecondartCate> cateSecondary = new ArrayList();
    public int categoryType = 0;
    public String strID;
    public int type = 0;

    public WedCategory()
    {
    }
  }

  public class WedSecondartCate
  {
    public String cateName;
    public int type;

    public WedSecondartCate()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.WeddingProductShopListNavigatorFilterAgent
 * JD-Core Version:    0.6.0
 */