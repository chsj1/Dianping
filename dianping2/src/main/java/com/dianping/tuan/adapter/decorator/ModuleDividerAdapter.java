package com.dianping.tuan.adapter.decorator;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;

public class ModuleDividerAdapter extends DividerAdapter
{
  protected static final int CELL_DIVIDER = 2;
  protected static final int MODULE_DIVIDER = 0;
  protected static final int SECTION_DIVIDER = 1;
  protected int cellDividerPaddingLeft;
  protected int cellDividerPaddingRight;

  public ModuleDividerAdapter(Context paramContext, ListAdapter paramListAdapter)
  {
    super(paramContext, paramListAdapter);
  }

  protected View getDivider(int paramInt)
  {
    new View(this.context);
    if (paramInt == 0)
      return LayoutInflater.from(this.context).inflate(R.layout.tuan_agg_view_item_divider, null);
    LinearLayout localLinearLayout = new LinearLayout(this.context);
    localLinearLayout.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
    ImageView localImageView = new ImageView(this.context);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    localLinearLayout.setBackgroundColor(this.context.getResources().getColor(R.color.white));
    localImageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.gray_horizontal_line));
    localImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    ((LinearLayout)localLinearLayout).addView(localImageView, localLayoutParams);
    if (paramInt == 2)
    {
      localLinearLayout.setPadding(this.cellDividerPaddingLeft, localLinearLayout.getPaddingTop(), this.cellDividerPaddingRight, localLinearLayout.getPaddingBottom());
      return localLinearLayout;
    }
    localLinearLayout.setPadding(0, localLinearLayout.getPaddingTop(), 0, localLinearLayout.getPaddingBottom());
    return localLinearLayout;
  }

  public View getDividerAfter(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView != null)
      return paramView;
    return getDivider(getDividerTypeAfter(paramInt));
  }

  public View getDividerBefore(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView != null)
      return paramView;
    return getDivider(getDividerTypeBefore(paramInt));
  }

  public int getDividerTypeAfter(int paramInt)
  {
    if ((this.mDataAdapter instanceof ModuleAdapter))
    {
      ModuleAdapter localModuleAdapter = (ModuleAdapter)this.mDataAdapter;
      if (isModuleLast(paramInt))
        return 0;
      if (isSectionLast(paramInt))
        return 1;
    }
    return 2;
  }

  public int getDividerTypeBefore(int paramInt)
  {
    return 0;
  }

  public int getDividerTypeCount()
  {
    return 3;
  }

  public boolean hasDividerAfter(int paramInt)
  {
    return getDataPosition(paramInt) != getDataAdapter().getCount() - 1;
  }

  public boolean hasDividerBefore(int paramInt)
  {
    return false;
  }

  public boolean isModuleFirst(int paramInt)
  {
    if (paramInt < 1)
      return true;
    if (this.mDataAdapter == null)
      return false;
    return ((ModuleAdapter)this.mDataAdapter).getModuleIndex(paramInt) != ((ModuleAdapter)this.mDataAdapter).getModuleIndex(paramInt - 1);
  }

  public boolean isModuleLast(int paramInt)
  {
    if (this.mDataAdapter == null)
      return false;
    if (paramInt >= this.mDataAdapter.getCount() - 1)
      return true;
    return ((ModuleAdapter)this.mDataAdapter).getModuleIndex(paramInt) != ((ModuleAdapter)this.mDataAdapter).getModuleIndex(paramInt + 1);
  }

  public boolean isSectionFirst(int paramInt)
  {
    if (paramInt < 1)
      return true;
    if (this.mDataAdapter == null)
      return false;
    return ((ModuleAdapter)this.mDataAdapter).getSectionIndex(paramInt) != ((ModuleAdapter)this.mDataAdapter).getSectionIndex(paramInt - 1);
  }

  public boolean isSectionLast(int paramInt)
  {
    if (this.mDataAdapter == null)
      return false;
    if (paramInt >= this.mDataAdapter.getCount() - 1)
      return true;
    return ((ModuleAdapter)this.mDataAdapter).getSectionIndex(paramInt) != ((ModuleAdapter)this.mDataAdapter).getSectionIndex(paramInt + 1);
  }

  public void setCellDividerPaddingLeft(int paramInt)
  {
    this.cellDividerPaddingLeft = paramInt;
  }

  public void setCellDividerPaddingRight(int paramInt)
  {
    this.cellDividerPaddingRight = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.adapter.decorator.ModuleDividerAdapter
 * JD-Core Version:    0.6.0
 */