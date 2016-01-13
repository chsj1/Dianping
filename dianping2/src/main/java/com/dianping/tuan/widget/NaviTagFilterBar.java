package com.dianping.tuan.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.dialog.filter.NaviFilterDialog;
import com.dianping.base.tuan.dialog.filter.TuanScreeningDialog;
import com.dianping.base.tuan.dialog.filter.TuanScreeningDialog.ScreeningDialogListener;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.locationservice.impl286.util.CommonUtil;
import com.dianping.util.CollectionUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.HashMap;

public class NaviTagFilterBar extends NovaLinearLayout
  implements FilterBar.OnItemClickListener, FilterDialog.OnFilterListener, TuanScreeningDialog.ScreeningDialogListener
{
  public static final String SCREENING_TAG = "ScreeningTag";
  protected Activity activity;
  protected View bottomDivideLine;
  protected FilterDialog dialog;
  protected FilterBar filterBar;
  protected boolean hasScreening;
  protected DPObject[] mNaviData;
  protected DPObject[] mScreeningData;
  protected DPObject[] mTagCellData;
  protected NovaLinearLayout naviScreeningLayout;
  protected OnSelectionChangedListener onSelectionChangedListener;
  protected String screening = "";
  protected HashMap<Integer, DPObject> selectedNaviMap = new HashMap();
  protected View selectedTagView = null;
  protected NovaLinearLayout tagContainer;
  protected boolean tagNeedCount;
  protected View.OnClickListener tagOnClickListener = new NaviTagFilterBar.1(this);
  protected HorizontalScrollView tagScrollContainer;
  protected ArrayList<View> tagViewList = new ArrayList();

  public NaviTagFilterBar(Activity paramActivity)
  {
    this(paramActivity, null);
  }

  public NaviTagFilterBar(Activity paramActivity, AttributeSet paramAttributeSet)
  {
    super(paramActivity, paramAttributeSet);
    this.activity = paramActivity;
    setOrientation(1);
    this.filterBar = new FilterBar(getContext());
    paramActivity = new ViewGroup.LayoutParams(-1, -2);
    addView(this.filterBar, paramActivity);
    this.tagScrollContainer = new HorizontalScrollView(getContext());
    this.tagScrollContainer.setBackgroundColor(getResources().getColor(R.color.white));
    this.tagContainer = new NovaLinearLayout(getContext());
    this.tagContainer.setOrientation(0);
    paramActivity = new ViewGroup.LayoutParams(-1, -1);
    this.tagContainer.setGravity(16);
    this.tagScrollContainer.addView(this.tagContainer, paramActivity);
    paramActivity = new ViewGroup.LayoutParams(-1, -2);
    this.tagScrollContainer.setHorizontalScrollBarEnabled(false);
    addView(this.tagScrollContainer, paramActivity);
    this.bottomDivideLine = new View(getContext());
    this.bottomDivideLine.setBackgroundColor(getContext().getResources().getColor(R.color.divider_line_gray));
    paramActivity = new ViewGroup.LayoutParams(-1, 1);
    addView(this.bottomDivideLine, paramActivity);
    this.filterBar.setOnItemClickListener(this);
  }

  protected void addFilterItems()
  {
    this.filterBar.removeAllViews();
    Object localObject2;
    if ((this.mNaviData != null) && (this.mNaviData.length > 0))
    {
      localObject1 = this.mNaviData;
      int j = localObject1.length;
      int i = 0;
      while (i < j)
      {
        localObject2 = localObject1[i];
        if (DPObjectUtils.isDPObjectof(localObject2, "Navi"))
        {
          DPObject localDPObject = findSelectedNavi((DPObject)localObject2);
          this.selectedNaviMap.put(Integer.valueOf(localDPObject.getInt("Type")), localDPObject);
          this.filterBar.addItem(localObject2, localDPObject.getString("Name"));
        }
        i += 1;
      }
    }
    if ((this.hasScreening) && (this.mScreeningData != null) && (this.mScreeningData.length > 0))
    {
      localObject2 = new StringBuilder().append("筛选");
      if (getScreeningCount() <= 0)
        break label192;
    }
    label192: for (Object localObject1 = " " + getScreeningCount(); ; localObject1 = "")
    {
      localObject1 = (String)localObject1;
      this.naviScreeningLayout = this.filterBar.addItem("ScreeningTag", (String)localObject1);
      return;
    }
  }

  protected void addTagCells()
  {
    if ((this.mTagCellData == null) || (this.mTagCellData.length <= 0))
    {
      this.tagScrollContainer.setVisibility(8);
      this.bottomDivideLine.setVisibility(8);
      return;
    }
    this.tagContainer.removeAllViews();
    int i = 0;
    while (true)
    {
      DPObject localDPObject;
      View localView;
      if (i < this.mTagCellData.length)
      {
        localDPObject = this.mTagCellData[i];
        localView = createTagNaviItem(localDPObject);
        if (localView != null);
      }
      else
      {
        smothScrollTagToSelected();
        this.tagScrollContainer.setVisibility(0);
        this.bottomDivideLine.setVisibility(0);
        return;
      }
      localView.setTag(R.id.tag, Integer.valueOf(i));
      this.tagContainer.addView(localView);
      this.tagViewList.add(localView);
      if (localView.isSelected())
      {
        this.selectedNaviMap.put(Integer.valueOf(localDPObject.getInt("Type")), localDPObject);
        this.selectedTagView = localView;
      }
      i += 1;
    }
  }

  protected View createTagNaviItem(DPObject paramDPObject)
  {
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "Navi"))
      return null;
    NovaButton localNovaButton = (NovaButton)LayoutInflater.from(getContext()).inflate(R.layout.tag_navi_item, (ViewGroup)null);
    Object localObject = new LinearLayout.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams)localObject).setMargins(CommonUtil.dip2px(getContext(), 6.0F), 0, CommonUtil.dip2px(getContext(), 6.0F), 0);
    localNovaButton.setLayoutParams((ViewGroup.LayoutParams)localObject);
    int i = paramDPObject.getInt("Count");
    String str = paramDPObject.getString("Name");
    localObject = str;
    if (this.tagNeedCount)
    {
      localObject = str;
      if (i > 0)
        localObject = str + String.format("(%d)", new Object[] { Integer.valueOf(i) });
    }
    localNovaButton.setText((CharSequence)localObject);
    if (paramDPObject.getBoolean("HighLight"))
    {
      localNovaButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.screening_filter_table_item_selected));
      localNovaButton.setTextColor(getResources().getColor(R.color.light_red));
    }
    localNovaButton.setTag(paramDPObject);
    localNovaButton.setOnClickListener(this.tagOnClickListener);
    if (paramDPObject.getBoolean("Selected"))
      localNovaButton.setSelected(true);
    while (true)
    {
      localNovaButton.setGravity(17);
      localNovaButton.setClickable(true);
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

  public OnSelectionChangedListener getOnSelectionChangedListener()
  {
    return this.onSelectionChangedListener;
  }

  public String getScreening()
  {
    return this.screening;
  }

  protected int getScreeningCount()
  {
    if (!TextUtils.isEmpty(this.screening))
    {
      String[] arrayOfString = CollectionUtils.str2Array(this.screening, "\\|");
      if ((arrayOfString != null) && (arrayOfString.length >= 0))
        return arrayOfString.length;
    }
    return 0;
  }

  public HashMap<Integer, DPObject> getSelectedNaviMap()
  {
    return this.selectedNaviMap;
  }

  public int getTagCellsScrollX()
  {
    return this.tagScrollContainer.getScrollX();
  }

  public void onClickItem(Object paramObject, View paramView)
  {
    if (this.activity == null);
    do
      while (true)
      {
        return;
        if (!DPObjectUtils.isDPObjectof(paramObject, "Navi"))
          break;
        DPObject localDPObject = (DPObject)paramObject;
        if ((localDPObject.getArray("Subs") == null) || (localDPObject.getArray("Subs").length <= 0))
          continue;
        this.dialog = new NaviFilterDialog(this.activity, localDPObject, this);
        this.dialog.setTag(paramObject);
        this.dialog.show(paramView);
      }
    while ((paramObject != "ScreeningTag") || (this.mScreeningData == null) || (this.mScreeningData.length <= 0));
    this.dialog = new TuanScreeningDialog(this.activity, this.mScreeningData, this.screening);
    this.dialog.setTag(paramObject);
    ((TuanScreeningDialog)this.dialog).setListener(this);
    this.dialog.show(paramView);
  }

  public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
  {
    if (!DPObjectUtils.isDPObjectof(paramObject, "Navi"))
      return;
    paramFilterDialog = (DPObject)paramObject;
    int j = paramFilterDialog.getInt("Type");
    if ((DPObject)this.selectedNaviMap.get(Integer.valueOf(j)) != paramFilterDialog);
    for (int i = 1; ; i = 0)
    {
      if (i != 0)
        this.selectedNaviMap.put(Integer.valueOf(j), paramFilterDialog);
      this.dialog.dismiss();
      if ((this.onSelectionChangedListener == null) || (i == 0))
        break;
      this.onSelectionChangedListener.onFilterSelectionChanged(this.selectedNaviMap, paramFilterDialog);
      return;
    }
  }

  public void onSubmit(String paramString)
  {
    if (!TextUtils.equals(this.screening, paramString));
    for (int i = 1; ; i = 0)
    {
      if (i != 0)
      {
        this.screening = paramString;
        toggleNaviButtonState();
      }
      if ((this.onSelectionChangedListener != null) && (i != 0))
        this.onSelectionChangedListener.onScreeningSelectionChanged(this.selectedNaviMap, this.screening);
      return;
    }
  }

  public void scrollTagTo(int paramInt)
  {
    this.tagScrollContainer.requestLayout();
    if (this.tagScrollContainer != null)
      this.tagScrollContainer.post(new NaviTagFilterBar.3(this, paramInt));
  }

  public void setCurrentScreening(String paramString, boolean paramBoolean)
  {
  }

  public void setFilterData(DPObject[] paramArrayOfDPObject)
  {
    setFilterData(paramArrayOfDPObject, true);
  }

  public void setFilterData(DPObject[] paramArrayOfDPObject, boolean paramBoolean)
  {
    this.mNaviData = paramArrayOfDPObject;
    if (paramBoolean)
      updateView();
  }

  public void setHasScreening(boolean paramBoolean)
  {
    this.hasScreening = paramBoolean;
  }

  public void setLineHegiht(int paramInt)
  {
    if (this.filterBar.getLayoutParams() != null)
      this.filterBar.getLayoutParams().height = paramInt;
    if (this.tagScrollContainer.getLayoutParams() != null)
      this.tagScrollContainer.getLayoutParams().height = paramInt;
  }

  public void setNeedCount(boolean paramBoolean)
  {
    this.tagNeedCount = paramBoolean;
  }

  public void setOnSelectionChangedListener(OnSelectionChangedListener paramOnSelectionChangedListener)
  {
    this.onSelectionChangedListener = paramOnSelectionChangedListener;
  }

  public void setScreening(String paramString)
  {
    setScreening(paramString, true);
  }

  public void setScreening(String paramString, boolean paramBoolean)
  {
    this.screening = paramString;
    if (paramBoolean)
      updateView();
  }

  public void setScreeningData(DPObject[] paramArrayOfDPObject)
  {
    setScreeningData(paramArrayOfDPObject, true);
  }

  public void setScreeningData(DPObject[] paramArrayOfDPObject, boolean paramBoolean)
  {
    if (!this.hasScreening);
    do
    {
      return;
      this.mScreeningData = paramArrayOfDPObject;
    }
    while (!paramBoolean);
    updateView();
  }

  public void setTagCellData(DPObject[] paramArrayOfDPObject)
  {
    setTagCellData(paramArrayOfDPObject, true);
  }

  public void setTagCellData(DPObject[] paramArrayOfDPObject, boolean paramBoolean)
  {
    this.mTagCellData = paramArrayOfDPObject;
    if (paramBoolean)
      updateView();
  }

  public void setTagCellsScrollX(int paramInt)
  {
    if (this.tagScrollContainer != null)
    {
      this.tagScrollContainer.requestLayout();
      scrollTagTo(paramInt);
    }
  }

  public void smothScrollTagToSelected()
  {
    this.tagScrollContainer.requestLayout();
    if ((this.selectedTagView != null) && ((this.selectedTagView instanceof NovaButton)))
    {
      this.tagScrollContainer.requestLayout();
      if (this.tagScrollContainer != null)
        this.tagScrollContainer.post(new NaviTagFilterBar.2(this));
    }
  }

  protected final void toggleNaviButtonState()
  {
    if ((this.mScreeningData != null) && (this.mScreeningData.length != 0))
    {
      this.naviScreeningLayout.setEnabled(true);
      return;
    }
    this.naviScreeningLayout.setEnabled(false);
  }

  public void updateView()
  {
    this.filterBar.removeAllViews();
    addFilterItems();
    this.tagContainer.removeAllViews();
    addTagCells();
  }

  public static abstract interface OnSelectionChangedListener
  {
    public abstract void onFilterSelectionChanged(HashMap<Integer, DPObject> paramHashMap, DPObject paramDPObject);

    public abstract void onScreeningSelectionChanged(HashMap<Integer, DPObject> paramHashMap, String paramString);

    public abstract void onTagCellSelectionChanged(int paramInt, HashMap<Integer, DPObject> paramHashMap, DPObject paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.widget.NaviTagFilterBar
 * JD-Core Version:    0.6.0
 */