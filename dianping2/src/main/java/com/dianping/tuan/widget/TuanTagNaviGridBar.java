package com.dianping.tuan.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.base.widget.CustomGridView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import java.util.List;
import java.util.Map;

public class TuanTagNaviGridBar extends LinearLayout
{
  public static final String CATEGORY_NAME = "Name";
  public static final String CATEGORY_SELECTED = "Selected";
  public static final int DEFAULT_COLUMN_NUM = 4;
  private int mButtonHeight;
  private int mButtonWidth;
  protected CustomGridView mCategoryGirdView;
  protected TuanTagNaviGridBar.CategoryGridAdapter mHeaderAdapter;
  private int mMarginTopBottom;
  private OnCategorySelectChangeListener mOnCategorySelectChange;
  private ColorStateList mTextColor;
  private int mTextPadding;
  private float mTextSize;

  public TuanTagNaviGridBar(Context paramContext)
  {
    this(paramContext, null);
  }

  public TuanTagNaviGridBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setupView();
    this.mHeaderAdapter.notifyDataSetChanged();
  }

  private boolean ObjectToBoolean(Object paramObject)
  {
    if (paramObject != null)
      try
      {
        if ((paramObject instanceof Boolean))
        {
          boolean bool = ((Boolean)paramObject).booleanValue();
          return bool;
        }
      }
      catch (java.lang.Exception paramObject)
      {
      }
    return false;
  }

  private void setupView()
  {
    this.mCategoryGirdView = null;
    this.mHeaderAdapter = null;
    setHorizontalScrollBarEnabled(false);
    this.mCategoryGirdView = new CustomGridView(getContext());
    this.mCategoryGirdView.setStretchAllColumns(true);
    this.mHeaderAdapter = new TuanTagNaviGridBar.CategoryGridAdapter(this);
    this.mCategoryGirdView.setAdapter(this.mHeaderAdapter);
    this.mCategoryGirdView.setEndHorizontalDivider(getResources().getDrawable(R.drawable.transparent_bg));
    this.mCategoryGirdView.setHorizontalDivider(getResources().getDrawable(R.drawable.transparent_bg));
    this.mCategoryGirdView.setVerticalDivider(getResources().getDrawable(R.drawable.transparent_bg));
    LinearLayout.LayoutParams localLayoutParams = generateDefaultLayoutParams();
    localLayoutParams.width = -1;
    localLayoutParams.height = -1;
    localLayoutParams.gravity = 17;
    this.mCategoryGirdView.setStretchAllColumns(true);
    addView(this.mCategoryGirdView, localLayoutParams);
    this.mButtonWidth = 0;
    this.mButtonHeight = ViewUtils.dip2px(getContext(), 32.0F);
    this.mMarginTopBottom = ViewUtils.dip2px(getContext(), 3.0F);
    this.mTextColor = getResources().getColorStateList(R.color.tag_navi_text_color);
    this.mTextSize = getResources().getDimension(R.dimen.text_size_14);
    this.mTextPadding = ViewUtils.dip2px(getContext(), 6.0F);
  }

  public void setColumnNum(int paramInt)
  {
    this.mCategoryGirdView.removeAllViews();
    this.mHeaderAdapter.setColumnNum(paramInt);
  }

  public void setNaviDatas(List<Map<String, Object>> paramList)
  {
    this.mHeaderAdapter.setCategoryDatas(paramList);
  }

  public void setOnCategorySelectChangeListener(OnCategorySelectChangeListener paramOnCategorySelectChangeListener)
  {
    this.mOnCategorySelectChange = paramOnCategorySelectChangeListener;
  }

  public void setStyle(int paramInt1, int paramInt2, int paramInt3, ColorStateList paramColorStateList, int paramInt4, int paramInt5, int paramInt6)
  {
    this.mButtonWidth = paramInt1;
    this.mButtonHeight = paramInt2;
    this.mMarginTopBottom = paramInt3;
    this.mTextColor = paramColorStateList;
    this.mTextSize = paramInt4;
    this.mTextPadding = paramInt5;
    this.mCategoryGirdView.removeAllViews();
    this.mHeaderAdapter.setColumnNum(paramInt6);
  }

  public static abstract interface OnCategorySelectChangeListener
  {
    public abstract void onCategorySelectChangeListener(int paramInt1, int paramInt2, Map<String, Object> paramMap);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.widget.TuanTagNaviGridBar
 * JD-Core Version:    0.6.0
 */