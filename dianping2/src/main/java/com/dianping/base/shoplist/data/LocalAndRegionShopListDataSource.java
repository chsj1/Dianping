package com.dianping.base.shoplist.data;

import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.shoplist.util.ShopListUtils;
import java.util.ArrayList;
import java.util.Arrays;

public class LocalAndRegionShopListDataSource extends DefaultSearchShopListDataSource
{
  public static final DPObject DEFAULT_FILTER = new DPObject("Pair").edit().putString("ID", "0").putString("Name", "默认排序").putInt("Type", 2).generate();
  public static final DPObject TOP_REGION = new DPObject("Region").edit().putInt("ID", 0).putString("Name", "全部商区").putInt("ParentID", -10000).generate();
  private boolean isFirst = true;
  private boolean isMetro = false;
  private boolean needLocalRegion = false;

  public LocalAndRegionShopListDataSource(DataSource.DataLoader paramDataLoader)
  {
    super(paramDataLoader);
    this.curRegion = TOP_REGION;
    this.curFilterIdPair = DEFAULT_FILTER;
  }

  private void addLocalRegion()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new DPObject("Region").edit().putInt("ID", -1).putString("Name", "附近").putInt("ParentID", 0).generate());
    DPObject[] arrayOfDPObject = this.filterRanges;
    int j = arrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = arrayOfDPObject[i];
      localArrayList.add(new DPObject("Region").edit().putInt("ID", ShopListUtils.parsePairIntId(localDPObject)).putString("Name", localDPObject.getString("Name")).putInt("ParentID", -1).generate());
      i += 1;
    }
    if (this.filterRegions != null)
      localArrayList.addAll(Arrays.asList(this.filterRegions));
    this.filterRegions = ((DPObject[])localArrayList.toArray(new DPObject[0]));
    if ((this.curRegion == null) && (this.curRangePair != null) && (this.filterRegions != null))
    {
      this.curRegion = new DPObject("Region").edit().putInt("ID", ShopListUtils.parsePairIntId(this.curRangePair)).putString("Name", this.curRangePair.getString("Name")).putInt("ParentID", -1).generate();
      i = 0;
    }
    while (true)
    {
      if (i < this.filterRegions.length)
      {
        if (this.curRegion.getInt("ID") == this.filterRegions[i].getInt("ID"))
          this.curRegion = this.filterRegions[i];
      }
      else
        return;
      i += 1;
    }
  }

  public Boolean isMetro()
  {
    return Boolean.valueOf(this.isMetro);
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.needLocalRegion = paramBundle.getBoolean("needLocalRegion");
    this.isMetro = paramBundle.getBoolean("isMetro");
    this.isFirst = paramBundle.getBoolean("isFirst");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("needLocalRegion", this.needLocalRegion);
    paramBundle.putBoolean("isMetro", this.isMetro);
    paramBundle.putBoolean("isFirst", this.isFirst);
  }

  public void setFirst(boolean paramBoolean)
  {
    this.isFirst = paramBoolean;
  }

  public void setIsMetro(boolean paramBoolean)
  {
    this.isMetro = paramBoolean;
  }

  public void setNeedLocalRegion(boolean paramBoolean)
  {
    this.needLocalRegion = paramBoolean;
  }

  protected void updateNavs(DPObject paramDPObject)
  {
    int i;
    if ((this.curFilterSiftPair != null) && ("".equals(this.curFilterSiftPair.getString("Name"))) && (paramDPObject.getArray("FilterNavs") != null))
    {
      i = 0;
      if (i < paramDPObject.getArray("FilterNavs").length)
      {
        if (!this.curFilterSiftPair.getString("ID").equals(paramDPObject.getArray("FilterNavs")[i].getString("ID")))
          break label619;
        this.curFilterSiftPair = paramDPObject.getArray("FilterNavs")[i];
      }
    }
    if ((this.curCategory != null) && (-1 == this.curCategory.getInt("ParentID")) && (paramDPObject.getArray("CategoryNavs") != null))
    {
      i = 0;
      label117: if (i < paramDPObject.getArray("CategoryNavs").length)
      {
        if (this.curCategory.getInt("ID") != paramDPObject.getArray("CategoryNavs")[i].getInt("ID"))
          break label626;
        this.curCategory = paramDPObject.getArray("CategoryNavs")[i];
      }
    }
    if ((this.curFilterIdPair != null) && ("".equals(this.curFilterIdPair.getString("Name"))) && (paramDPObject.getArray("SortNavs") != null))
    {
      i = 0;
      label200: if (i < paramDPObject.getArray("SortNavs").length)
      {
        if (!this.curFilterIdPair.getString("ID").equals(paramDPObject.getArray("SortNavs")[i].getString("ID")))
          break label633;
        this.curFilterIdPair = paramDPObject.getArray("SortNavs")[i];
      }
    }
    if (paramDPObject.getObject("CurrentFilter") != null)
      this.curFilterSiftPair = paramDPObject.getObject("CurrentFilter");
    if (paramDPObject.getObject("CurrentSort") != null)
      this.curFilterIdPair = paramDPObject.getObject("CurrentSort");
    if ((paramDPObject.getObject("CurrentRange") != null) && (this.isFirst))
    {
      this.isFirst = false;
      this.curRangePair = paramDPObject.getObject("CurrentRange");
      if (this.needLocalRegion)
        this.curRegion = new DPObject("Region").edit().putString("Name", this.curRangePair.getString("Name")).putInt("ID", ShopListUtils.parsePairIntId(this.curRangePair)).putInt("ParentID", -1).generate();
    }
    if (paramDPObject.getObject("CurrentSelect") != null)
      this.curSelectNavs = paramDPObject.getObject("CurrentSelect");
    if ((paramDPObject.getArray("FilterNavs") != null) && (paramDPObject.getArray("FilterNavs").length > 0))
      this.filterSiftPairs = paramDPObject.getArray("FilterNavs");
    if (paramDPObject.getArray("RegionNavs") != null)
    {
      if (this.isMetro)
        this.filterRegions = paramDPObject.getArray("MetroNavs");
    }
    else
    {
      label460: if (paramDPObject.getArray("CategoryNavs") != null)
        this.filterCategories = paramDPObject.getArray("CategoryNavs");
      if (paramDPObject.getArray("RangeNavs") != null)
        this.filterRanges = paramDPObject.getArray("RangeNavs");
      if (paramDPObject.getArray("SortNavs") != null)
        this.filterIdPairs = paramDPObject.getArray("SortNavs");
      if ((this.needLocalRegion) && (paramDPObject.getArray("RegionNavs") != null))
        addLocalRegion();
      if ((this.curRegion != null) && ("".equals(this.curRegion.getString("Name"))) && (this.filterRegions != null))
        i = 0;
    }
    while (true)
    {
      if (i < this.filterRegions.length)
      {
        if (this.curRegion.getInt("ID") == this.filterRegions[i].getInt("ID"))
          this.curRegion = this.filterRegions[i];
      }
      else
      {
        publishDataChange(20);
        return;
        label619: i += 1;
        break;
        label626: i += 1;
        break label117;
        label633: i += 1;
        break label200;
        this.filterRegions = paramDPObject.getArray("RegionNavs");
        break label460;
      }
      i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.data.LocalAndRegionShopListDataSource
 * JD-Core Version:    0.6.0
 */