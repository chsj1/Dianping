package com.dianping.base.basic;

import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.NovaFragment;
import com.dianping.locationservice.LocationService;

public abstract class AbstractFilterFragment extends NovaFragment
{
  public static final DPObject ALL_CATEGORY;
  public static final DPObject ALL_REGION = new DPObject("Region").edit().putInt("ID", 0).putString("Name", "全部商区").putInt("ParentID", 0).putString("EnName", "0").putString("ParentEnName", "0").generate();
  public static final DPObject DEFAULT_FILTER;
  public static final DPObject DEFAULT_RANGE;
  protected DPObject[] categoryNavs;
  protected DPObject currentCategory = ALL_CATEGORY;
  protected DPObject currentFilter = DEFAULT_FILTER;
  protected DPObject currentRange = DEFAULT_RANGE;
  protected DPObject currentRegion = ALL_REGION;
  protected FilterBar filterBar;
  protected DPObject[] filterNavs;
  public OnFilterBarClickListener onFilterBarClickListener;
  public OnFilterItemClickListener onFilterItemClickListener;
  protected DPObject[] rangeNavs;
  protected DPObject[] regionNavs;

  static
  {
    ALL_CATEGORY = new DPObject("Category").edit().putInt("ID", 0).putString("Name", "全部分类").putInt("ParentID", 0).putString("EnName", "0").putString("ParentEnName", "0").generate();
    DEFAULT_FILTER = new DPObject("Pair").edit().putString("ID", "0").putString("Name", "默认排序").putInt("Type", 2).generate();
    DEFAULT_RANGE = new DPObject("Pair").edit().putString("ID", "1000").putString("Name", "1000米").generate();
  }

  protected boolean checkFilterable(DPObject paramDPObject)
  {
    if ((paramDPObject != null) && (paramDPObject.getString("Name").contains("距离")) && (((DPActivity)getActivity()).locationService().location() == null))
    {
      Toast.makeText(getActivity(), "正在定位，此功能暂不可用", 0).show();
      return false;
    }
    return true;
  }

  public abstract void setNavs(DPObject[] paramArrayOfDPObject1, DPObject[] paramArrayOfDPObject2, DPObject[] paramArrayOfDPObject3);

  public void setOnFilterBarClickListener(OnFilterBarClickListener paramOnFilterBarClickListener)
  {
    this.onFilterBarClickListener = paramOnFilterBarClickListener;
  }

  public void setOnFilterItemClickListener(OnFilterItemClickListener paramOnFilterItemClickListener)
  {
    this.onFilterItemClickListener = paramOnFilterItemClickListener;
  }

  public abstract void updateNavs(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3);

  public static abstract interface OnFilterBarClickListener
  {
    public abstract void onFilterBarClick(Object paramObject);
  }

  public static abstract interface OnFilterItemClickListener
  {
    public abstract void onFilterItemClick(DPObject paramDPObject1, DPObject paramDPObject2, DPObject paramDPObject3);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.AbstractFilterFragment
 * JD-Core Version:    0.6.0
 */