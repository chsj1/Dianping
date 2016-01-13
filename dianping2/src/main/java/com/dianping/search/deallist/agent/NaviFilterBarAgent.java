package com.dianping.search.deallist.agent;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.tuan.agent.TuanCellAgent;
import com.dianping.base.tuan.agent.TuanFilterDefaultDPObject;
import com.dianping.base.tuan.dialog.filter.NaviFilterDialog;
import com.dianping.base.tuan.dialog.filter.TuanScreeningDialog;
import com.dianping.base.tuan.dialog.filter.TuanScreeningDialog.ScreeningDialogListener;
import com.dianping.base.tuan.utils.TuanSharedDataKey;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ExtendableListView.LayoutParams;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.locationservice.impl286.util.CommonUtil;
import com.dianping.util.CollectionUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class NaviFilterBarAgent extends TuanCellAgent
  implements FilterBar.OnItemClickListener, FilterDialog.OnFilterListener, TuanScreeningDialog.ScreeningDialogListener
{
  protected static final String FILTER_BAR = "20FilterBar";
  protected static final String SCREENING_TITLE = "筛选";
  protected static final String TAG_FILTER_BAR = "20FilterBar1";
  protected View dividerLine;
  protected FilterDialog dlg;
  protected FilterBar filterBar;
  protected boolean isRequestFinish = false;
  protected DPObject naviCategory;
  protected DPObject naviRegion;
  protected NovaLinearLayout naviScreeningLayout;
  protected DPObject naviSort;
  protected DPObject[] screeningData;
  protected int scrollXPosition = 0;
  protected boolean scrolled = false;
  protected View selectedTagView = null;
  protected View.OnClickListener tagClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      Object localObject = paramView.getTag();
      if (!DPObjectUtils.isDPObjectof(localObject, "Navi"));
      do
      {
        return;
        localObject = (DPObject)localObject;
      }
      while (NaviFilterBarAgent.this.getSharedDPObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY) == localObject);
      NaviFilterBarAgent.this.setSharedObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY, localObject);
      paramView.setSelected(true);
      paramView = new AgentMessage("deal_list_filter_data_changed");
      NaviFilterBarAgent.this.dispatchMessage(paramView);
    }
  };
  protected LinearLayout tagFilterBar;
  protected HorizontalScrollView tagFilterBarContainer;
  protected DPObject[] tagNavisCategory;

  public NaviFilterBarAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void addFilterItemNewStatisticEvent(DPObject paramDPObject)
  {
    String str2 = paramDPObject.getString("Name");
    addStaticEventsExtra(new ArrayList());
    String str1 = "";
    int i = paramDPObject.getInt("Type");
    if (i == 1)
      paramDPObject = "category";
    while (true)
    {
      GAHelper.instance().contextStatisticsEvent(getContext(), paramDPObject, str2, 2147483647, "tap");
      return;
      if ((i == 2) || (i == 3))
      {
        paramDPObject = "zone";
        continue;
      }
      paramDPObject = str1;
      if (i != 4)
        continue;
      paramDPObject = "sort";
    }
  }

  protected void addItems()
  {
    this.filterBar.removeAllViews();
    if (this.naviCategory != null)
      this.filterBar.addItem(TuanSharedDataKey.CURRENT_CATEGORY_KEY, getCurrentCategory().getString("Name"));
    if (this.naviRegion != null)
      this.filterBar.addItem(TuanSharedDataKey.CURRENT_REGION_KEY, getCurrentRegion().getString("Name"));
    if (this.naviSort != null)
      this.filterBar.addItem(TuanSharedDataKey.CURRENT_SORT_KEY, getCurrentSort().getString("Name"));
    StringBuilder localStringBuilder = new StringBuilder().append("筛选");
    if (getScreeningCount() > 0);
    for (String str = " " + getScreeningCount(); ; str = "")
    {
      str = str;
      this.naviScreeningLayout = this.filterBar.addItem(TuanSharedDataKey.CURRENT_SCREENING_KEY, str);
      return;
    }
  }

  protected void addScreeningNewGA()
  {
    String str = getCurrentScreening();
    GAHelper.instance().contextStatisticsEvent(getContext(), "filter", str, 2147483647, "tap");
  }

  public ArrayList<NameValuePair> addStaticEventsExtra(ArrayList<NameValuePair> paramArrayList)
  {
    paramArrayList.add(new BasicNameValuePair("from", getSharedString(TuanSharedDataKey.FROM_WHERE_KEY)));
    return paramArrayList;
  }

  protected View createTagNaviItem(DPObject paramDPObject)
  {
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "Navi"))
      return null;
    NovaButton localNovaButton = (NovaButton)LayoutInflater.from(getContext()).inflate(R.layout.tag_navi_item, null);
    Object localObject = new LinearLayout.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams)localObject).setMargins(CommonUtil.dip2px(getContext(), 6.0F), 0, CommonUtil.dip2px(getContext(), 6.0F), 0);
    localNovaButton.setLayoutParams((ViewGroup.LayoutParams)localObject);
    int i = paramDPObject.getInt("Count");
    String str = paramDPObject.getString("Name");
    localObject = str;
    if (i > 0)
      localObject = str + String.format("(%d)", new Object[] { Integer.valueOf(i) });
    localNovaButton.setText((CharSequence)localObject);
    localNovaButton.setTag(paramDPObject);
    localNovaButton.setOnClickListener(this.tagClickListener);
    if (paramDPObject.getBoolean("Selected"))
      localNovaButton.setSelected(true);
    while (true)
    {
      localNovaButton.setGravity(17);
      localNovaButton.setClickable(true);
      localNovaButton.setGAString("category_tag");
      localNovaButton.gaUserInfo.title = paramDPObject.getString("Name");
      localNovaButton.gaUserInfo.category_id = Integer.valueOf(paramDPObject.getInt("ID"));
      return localNovaButton;
      localNovaButton.setSelected(false);
    }
  }

  public DPObject findSelectedNavi(DPObject paramDPObject)
  {
    if (paramDPObject == null)
    {
      paramDPObject = null;
      return paramDPObject;
    }
    if ((paramDPObject.getBoolean("Selected")) && (paramDPObject.getArray("Subs") != null) && (paramDPObject.getArray("Subs").length != 0))
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("Subs");
      int j = arrayOfDPObject.length;
      int i = 0;
      while (true)
      {
        if (i >= j)
          break label116;
        DPObject localDPObject = arrayOfDPObject[i];
        if (localDPObject.getBoolean("Selected"))
        {
          paramDPObject = localDPObject;
          if (localDPObject.getArray("Subs") == null)
            break;
          paramDPObject = localDPObject;
          if (localDPObject.getArray("Subs").length == 0)
            break;
          return findSelectedNavi(localDPObject);
        }
        i += 1;
      }
    }
    label116: return paramDPObject;
  }

  public DPObject getCurrentCategory()
  {
    DPObject localDPObject2 = getSharedDPObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY);
    DPObject localDPObject1 = localDPObject2;
    if (localDPObject2 == null)
      localDPObject1 = TuanFilterDefaultDPObject.ALL_CATEGORY;
    return localDPObject1;
  }

  public DPObject getCurrentRegion()
  {
    DPObject localDPObject2 = getSharedDPObject(TuanSharedDataKey.CURRENT_REGION_KEY);
    DPObject localDPObject1 = localDPObject2;
    if (localDPObject2 == null)
      localDPObject1 = TuanFilterDefaultDPObject.ALL_REGION;
    return localDPObject1;
  }

  public String getCurrentScreening()
  {
    String str2 = getSharedString(TuanSharedDataKey.CURRENT_SCREENING_KEY);
    String str1 = str2;
    if (str2 == null)
      str1 = "";
    return str1;
  }

  public DPObject getCurrentSort()
  {
    DPObject localDPObject2 = getSharedDPObject(TuanSharedDataKey.CURRENT_SORT_KEY);
    DPObject localDPObject1 = localDPObject2;
    if (localDPObject2 == null)
      localDPObject1 = TuanFilterDefaultDPObject.DEFAULT_SORT;
    return localDPObject1;
  }

  protected int getScreeningCount()
  {
    if (!TextUtils.isEmpty(getCurrentScreening()))
    {
      String[] arrayOfString = CollectionUtils.str2Array(getCurrentScreening(), "\\|");
      if ((arrayOfString != null) && (arrayOfString.length >= 0))
        return arrayOfString.length;
    }
    return 0;
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ("deal_list_data_analized".equals(paramAgentMessage.what))
    {
      this.isRequestFinish = true;
      updateView();
    }
    if ("deal_list_filter_reset_commend".equals(paramAgentMessage.what))
    {
      setSharedObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY, TuanFilterDefaultDPObject.ALL_CATEGORY);
      setSharedObject(TuanSharedDataKey.CURRENT_REGION_KEY, TuanFilterDefaultDPObject.ALL_REGION);
      setSharedObject(TuanSharedDataKey.CURRENT_SCREENING_KEY, "");
      setSharedObject(TuanSharedDataKey.CURRENT_SORT_KEY, TuanFilterDefaultDPObject.DEFAULT_SORT);
      dispatchMessage(new AgentMessage("deal_list_filter_data_changed"));
    }
    if ("deal_list_keyword_changed".equals(paramAgentMessage.what))
    {
      setSharedObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY, TuanFilterDefaultDPObject.ALL_CATEGORY);
      setSharedObject(TuanSharedDataKey.CURRENT_REGION_KEY, TuanFilterDefaultDPObject.ALL_REGION);
      setSharedObject(TuanSharedDataKey.CURRENT_SCREENING_KEY, "");
      setSharedObject(TuanSharedDataKey.CURRENT_SORT_KEY, TuanFilterDefaultDPObject.DEFAULT_SORT);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.filterBar == null)
      setupView();
    updateView();
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if (paramObject == null)
      return;
    DPObject localDPObject = null;
    if (TuanSharedDataKey.CURRENT_CATEGORY_KEY.equals(paramObject))
      localDPObject = this.naviCategory;
    while (true)
    {
      if (localDPObject != null)
      {
        if ((localDPObject.getArray("Subs") == null) || (localDPObject.getArray("Subs").length <= 0))
          break;
        this.dlg = new NaviFilterDialog(getFragment().getActivity(), localDPObject, this);
        this.dlg.setTag(paramObject);
        this.dlg.show(paramView);
      }
      if ((!TuanSharedDataKey.CURRENT_SCREENING_KEY.equals(paramObject)) || (this.screeningData == null) || (this.screeningData.length <= 0))
        break;
      this.dlg = new TuanScreeningDialog(getFragment().getActivity(), this.screeningData, getCurrentScreening());
      this.dlg.setTag(paramObject);
      ((TuanScreeningDialog)this.dlg).setListener(this);
      this.dlg.show(paramView);
      return;
      if (TuanSharedDataKey.CURRENT_REGION_KEY.equals(paramObject))
      {
        localDPObject = this.naviRegion;
        continue;
      }
      if (!TuanSharedDataKey.CURRENT_SORT_KEY.equals(paramObject))
        continue;
      localDPObject = this.naviSort;
    }
  }

  public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
  {
    TuanSharedDataKey localTuanSharedDataKey = (TuanSharedDataKey)paramFilterDialog.getTag();
    if (TextUtils.isEmpty(localTuanSharedDataKey.toString()));
    do
    {
      do
        return;
      while (!(paramObject instanceof DPObject));
      paramObject = (DPObject)paramObject;
    }
    while (!paramObject.isClass("Navi"));
    DPObject localDPObject = getSharedDPObject(localTuanSharedDataKey);
    if (localDPObject != paramObject)
    {
      setSharedObject(localTuanSharedDataKey, paramObject);
      addFilterItemNewStatisticEvent(paramObject);
      dispatchMessage(new AgentMessage("deal_list_filter_data_changed"));
      if (localDPObject.getInt("Type") != 4)
        break label105;
      setSharedObject(TuanSharedDataKey.NOT_UPDATE_SCREENING_DATA, Boolean.valueOf(true));
    }
    while (true)
    {
      paramFilterDialog.dismiss();
      return;
      label105: setSharedObject(TuanSharedDataKey.CURRENT_SCREENING_KEY, "");
      setSharedObject(TuanSharedDataKey.NOT_UPDATE_SCREENING_DATA, Boolean.valueOf(false));
    }
  }

  public void onSubmit(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      paramString = "";
    while (paramString.equals(getCurrentScreening()))
      return;
    setSharedObject(TuanSharedDataKey.CURRENT_SCREENING_KEY, paramString);
    toggleNaviButtonState();
    setSharedObject(TuanSharedDataKey.NOT_UPDATE_SCREENING_DATA, Boolean.valueOf(true));
    addScreeningNewGA();
    dispatchMessage(new AgentMessage("deal_list_filter_data_changed"));
  }

  protected void setupView()
  {
    this.filterBar = new FilterBar(getContext());
    Object localObject = new ViewGroup.LayoutParams(-1, CommonUtil.dip2px(getContext(), 50.0F));
    this.filterBar.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.tagFilterBarContainer = new HorizontalScrollView(getContext());
    localObject = new ViewGroup.LayoutParams(-1, CommonUtil.dip2px(getContext(), 50.0F));
    this.tagFilterBarContainer.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.tagFilterBarContainer.setHorizontalScrollBarEnabled(false);
    this.tagFilterBarContainer.setBackgroundColor(getContext().getResources().getColor(R.color.white));
    this.tagFilterBar = new LinearLayout(getContext());
    this.tagFilterBar.setOrientation(0);
    localObject = new ViewGroup.LayoutParams(-2, -1);
    this.tagFilterBar.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.tagFilterBar.setPadding(CommonUtil.dip2px(getContext(), 4.0F), CommonUtil.dip2px(getContext(), 10.0F), 0, CommonUtil.dip2px(getContext(), 10.0F));
    this.tagFilterBarContainer.addView(this.tagFilterBar);
    removeAllCells();
    addCell("20FilterBar", this.filterBar);
    addCell("20FilterBar1", this.tagFilterBarContainer);
    this.dividerLine = new View(getContext());
    localObject = new ExtendableListView.LayoutParams(-2, 1);
    this.dividerLine.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.dividerLine.setBackgroundColor(getContext().getResources().getColor(R.color.divider_line_gray));
    addCell("20FilterBar11", this.dividerLine);
    this.tagFilterBarContainer.setVisibility(8);
    this.dividerLine.setVisibility(8);
  }

  protected final void toggleNaviButtonState()
  {
    if ((this.screeningData != null) && (this.screeningData.length != 0))
    {
      this.naviScreeningLayout.setEnabled(true);
      return;
    }
    this.naviScreeningLayout.setEnabled(false);
  }

  public void updateView()
  {
    this.naviCategory = getSharedDPObject(TuanSharedDataKey.CATEGORY_NAVI_KEY);
    this.tagNavisCategory = getSharedDPObjectArray(TuanSharedDataKey.CATEGORY_TAG_NAVIS_KEY);
    this.naviRegion = getSharedDPObject(TuanSharedDataKey.REGION_NAVI_KEY);
    this.naviSort = getSharedDPObject(TuanSharedDataKey.SORT_NAVI_KEY);
    this.screeningData = getSharedDPObjectArray(TuanSharedDataKey.SCREENING_LIST_KEY);
    Object localObject;
    label82: label98: label114: int i;
    if (this.naviCategory == null)
    {
      localObject = TuanFilterDefaultDPObject.ALL_CATEGORY;
      this.naviCategory = ((DPObject)localObject);
      if (this.naviRegion != null)
        break label327;
      localObject = TuanFilterDefaultDPObject.ALL_REGION;
      this.naviRegion = ((DPObject)localObject);
      if (this.naviSort != null)
        break label335;
      localObject = TuanFilterDefaultDPObject.DEFAULT_SORT;
      this.naviSort = ((DPObject)localObject);
      if (this.screeningData != null)
        break label343;
      localObject = TuanFilterDefaultDPObject.DEFAULT_SCREENING_DATA;
      this.screeningData = ((DPObject)localObject);
      if ((getCurrentCategory() == null) || (this.isRequestFinish))
        setSharedObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY, findSelectedNavi(this.naviCategory));
      if ((getCurrentRegion() == null) || (this.isRequestFinish))
        setSharedObject(TuanSharedDataKey.CURRENT_REGION_KEY, findSelectedNavi(this.naviRegion));
      if ((getCurrentSort() == null) || (this.isRequestFinish))
        setSharedObject(TuanSharedDataKey.CURRENT_SORT_KEY, findSelectedNavi(this.naviSort));
      addItems();
      this.filterBar.setOnItemClickListener(this);
      if (this.tagNavisCategory == null)
        break label393;
      this.tagFilterBar.removeAllViews();
      i = 0;
      label234: if ((i < this.tagNavisCategory.length) && (DPObjectUtils.isDPObjectof(this.tagNavisCategory[i], "Navi")))
        break label351;
      label257: this.tagFilterBarContainer.requestLayout();
      if ((this.selectedTagView != null) && ((this.selectedTagView instanceof NovaButton)))
        this.tagFilterBarContainer.post(new Runnable()
        {
          public void run()
          {
            int i = (NaviFilterBarAgent.this.selectedTagView.getLeft() + NaviFilterBarAgent.this.selectedTagView.getRight()) / 2;
            int j = ViewUtils.getScreenWidthPixels(NaviFilterBarAgent.this.getContext()) / 2;
            NaviFilterBarAgent.this.tagFilterBarContainer.smoothScrollTo(i - j, 0);
          }
        });
      this.tagFilterBarContainer.setVisibility(0);
      this.dividerLine.setVisibility(0);
    }
    while (true)
    {
      this.isRequestFinish = false;
      return;
      localObject = this.naviCategory;
      break;
      label327: localObject = this.naviRegion;
      break label82;
      label335: localObject = this.naviSort;
      break label98;
      label343: localObject = this.screeningData;
      break label114;
      label351: localObject = createTagNaviItem(this.tagNavisCategory[i]);
      if (localObject == null)
        break label257;
      this.tagFilterBar.addView((View)localObject);
      if (((View)localObject).isSelected())
        this.selectedTagView = ((View)localObject);
      i += 1;
      break label234;
      label393: this.tagFilterBarContainer.setVisibility(8);
      this.dividerLine.setVisibility(8);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.agent.NaviFilterBarAgent
 * JD-Core Version:    0.6.0
 */