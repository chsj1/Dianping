package com.dianping.base.shoplist.data;

import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import java.util.ArrayList;
import java.util.Arrays;

@Deprecated
public class DefaultSearchShopListDataSource extends DefaultShopListDataSource
  implements SearchShopListDataSource
{
  public static final DPObject DEFAULT_FILTER;
  public static final DPObject DEFAULT_RANGE;
  public static final DPObject DEFAULT_SIFT;
  public static final DPObject TOP_CATEGORY = new DPObject("Category").edit().putInt("ID", 0).putString("Name", "全部分类").putInt("ParentID", 0).putString("FavIcon", null).putInt("Distance", 500).generate();
  public static final DPObject TOP_REGION = new DPObject("Region").edit().putInt("ID", 0).putString("Name", "全部商区").putInt("ParentID", 0).generate();
  private String GATag = "";
  protected DPObject curCategory = TOP_CATEGORY;
  protected DPObject curFilterIdPair = DEFAULT_FILTER;
  protected DPObject curFilterSiftPair = DEFAULT_SIFT;
  protected DPObject curRangePair;
  protected DPObject curRegion = TOP_REGION;
  protected DPObject curSelectNavs = null;
  protected DPObject[] filterCategories;
  protected DPObject[] filterIdPairs;
  protected DPObject[] filterRanges;
  protected DPObject[] filterRegions;
  protected DPObject[] filterSiftPairs;
  private double latitude;
  private double longitude;
  private int mapType;
  private String placeName;
  private String queryId = "";
  private String[] searchTips;

  static
  {
    DEFAULT_FILTER = new DPObject("Pair").edit().putString("ID", "0").putString("Name", "默认排序").putInt("Type", 2).generate();
    DEFAULT_SIFT = new DPObject("Pair").edit().putString("ID", "0").putString("Name", "全部商户").putInt("Type", 0).generate();
    DEFAULT_RANGE = new DPObject("Pair").edit().putString("ID", "1000").putString("Name", "1000米").generate();
  }

  public DefaultSearchShopListDataSource(DataSource.DataLoader paramDataLoader)
  {
    super(paramDataLoader);
  }

  public String GATag()
  {
    return this.GATag;
  }

  public void appendShops(DPObject paramDPObject)
  {
    if (paramDPObject.getInt("StartIndex") == nextStartIndex())
    {
      updateNavs(paramDPObject);
      this.searchTips = paramDPObject.getStringArray("SearchTips");
      this.queryId = paramDPObject.getString("QueryID");
      publishDataChange(21);
    }
    super.appendShops(paramDPObject);
  }

  public DPObject curCategory()
  {
    return this.curCategory;
  }

  public DPObject curFilterId()
  {
    return this.curFilterIdPair;
  }

  public DPObject curRange()
  {
    return this.curRangePair;
  }

  public DPObject curRegion()
  {
    return this.curRegion;
  }

  public DPObject curSelectNavs()
  {
    return this.curSelectNavs;
  }

  public DPObject[] filterCategories()
  {
    return this.filterCategories;
  }

  public DPObject[] filterIds()
  {
    return this.filterIdPairs;
  }

  public DPObject[] filterRanges()
  {
    return this.filterRanges;
  }

  public DPObject[] filterRegions()
  {
    return this.filterRegions;
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.curRegion = ((DPObject)paramBundle.getParcelable("curRegion"));
    if (this.curRegion == null)
      this.curRegion = TOP_REGION;
    this.curRangePair = ((DPObject)paramBundle.getParcelable("curRange"));
    this.curCategory = ((DPObject)paramBundle.getParcelable("curCategory"));
    if (this.curCategory != null)
      this.curCategory = TOP_CATEGORY;
    this.curFilterIdPair = ((DPObject)paramBundle.getParcelable("curFilterId"));
    ArrayList localArrayList = paramBundle.getParcelableArrayList("filterRegions");
    if (localArrayList != null)
      this.filterRegions = ((DPObject[])localArrayList.toArray(new DPObject[0]));
    localArrayList = paramBundle.getParcelableArrayList("filterRanges");
    if (localArrayList != null)
      this.filterRanges = ((DPObject[])localArrayList.toArray(new DPObject[0]));
    localArrayList = paramBundle.getParcelableArrayList("filterSift");
    if (localArrayList != null)
      this.filterSiftPairs = ((DPObject[])localArrayList.toArray(new DPObject[0]));
    localArrayList = paramBundle.getParcelableArrayList("filterCategories");
    if (localArrayList != null)
      this.filterCategories = ((DPObject[])localArrayList.toArray(new DPObject[0]));
    localArrayList = paramBundle.getParcelableArrayList("filterIds");
    if (localArrayList != null)
      this.filterIdPairs = ((DPObject[])localArrayList.toArray(new DPObject[0]));
    localArrayList = paramBundle.getStringArrayList("searchTips");
    if (localArrayList != null)
      this.searchTips = ((String[])localArrayList.toArray(new String[0]));
    this.queryId = paramBundle.getString("queryId");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("curRegion", this.curRegion);
    paramBundle.putParcelable("curRange", this.curRangePair);
    paramBundle.putParcelable("curCategory", this.curCategory);
    paramBundle.putParcelable("curFilterId", this.curFilterIdPair);
    ArrayList localArrayList;
    if (this.filterRegions != null)
    {
      localArrayList = new ArrayList();
      localArrayList.addAll(Arrays.asList(this.filterRegions));
      paramBundle.putParcelableArrayList("filterRegions", localArrayList);
    }
    if (this.filterRanges != null)
    {
      localArrayList = new ArrayList();
      localArrayList.addAll(Arrays.asList(this.filterRanges));
      paramBundle.putParcelableArrayList("filterRanges", localArrayList);
    }
    if (this.filterSiftPairs != null)
    {
      localArrayList = new ArrayList();
      localArrayList.addAll(Arrays.asList(this.filterSiftPairs));
      paramBundle.putParcelableArrayList("filterSift", localArrayList);
    }
    if (this.filterCategories != null)
    {
      localArrayList = new ArrayList();
      localArrayList.addAll(Arrays.asList(this.filterCategories));
      paramBundle.putParcelableArrayList("filterCategories", localArrayList);
    }
    if (this.filterIdPairs != null)
    {
      localArrayList = new ArrayList();
      localArrayList.addAll(Arrays.asList(this.filterIdPairs));
      paramBundle.putParcelableArrayList("filterIds", localArrayList);
    }
    if (this.searchTips != null)
    {
      localArrayList = new ArrayList();
      localArrayList.addAll(Arrays.asList(this.searchTips));
      paramBundle.putStringArrayList("searchTips", localArrayList);
    }
    paramBundle.putString("queryId", this.queryId);
  }

  public String queryId()
  {
    return this.queryId;
  }

  public void reset(boolean paramBoolean)
  {
    super.reset(paramBoolean);
  }

  public void resetFilter()
  {
    super.reset(true);
    this.curRegion = TOP_REGION;
    this.curCategory = TOP_CATEGORY;
    this.curFilterIdPair = DEFAULT_FILTER;
    this.curRangePair = DEFAULT_RANGE;
    publishDataChange(20);
  }

  public String[] searchTips()
  {
    return this.searchTips;
  }

  public boolean setCurCategory(DPObject paramDPObject)
  {
    int i = 0;
    if ((paramDPObject == null) || (this.curCategory == null))
      if ((paramDPObject == null) && (this.curCategory == null))
      {
        this.curCategory = TOP_CATEGORY;
        i = 1;
      }
    do
    {
      do
        return i;
      while (paramDPObject == null);
      this.curCategory = paramDPObject;
      return true;
    }
    while (paramDPObject.getInt("ID") == this.curCategory.getInt("ID"));
    this.curCategory = paramDPObject;
    return true;
  }

  public boolean setCurFilterId(DPObject paramDPObject)
  {
    int i = 0;
    if ((paramDPObject == null) || (this.curFilterIdPair == null))
      if ((paramDPObject == null) && (this.curFilterIdPair == null))
      {
        this.curFilterIdPair = DEFAULT_FILTER;
        i = 1;
      }
    do
    {
      do
        return i;
      while (paramDPObject == null);
      this.curFilterIdPair = paramDPObject;
      return true;
    }
    while (paramDPObject.getString("ID").equals(this.curFilterIdPair.getString("ID")));
    this.curFilterIdPair = paramDPObject;
    return true;
  }

  public boolean setCurRange(DPObject paramDPObject)
  {
    int j = 0;
    int i;
    if ((paramDPObject == null) || (this.curRangePair == null))
      if ((paramDPObject == null) && (this.curRangePair == null))
      {
        this.curRangePair = DEFAULT_RANGE;
        i = 1;
      }
    do
    {
      do
      {
        do
        {
          return i;
          i = j;
        }
        while (paramDPObject == null);
        this.curRangePair = paramDPObject;
        return true;
        i = j;
      }
      while (paramDPObject.getString("ID") == null);
      i = j;
    }
    while (paramDPObject.getString("ID").equals(this.curRangePair.getString("ID")));
    this.curRangePair = paramDPObject;
    return true;
  }

  public boolean setCurRegion(DPObject paramDPObject)
  {
    int i = 0;
    if ((paramDPObject == null) || (this.curRegion == null))
      if ((paramDPObject == null) && (this.curRegion == null))
      {
        this.curRegion = TOP_REGION;
        i = 1;
      }
    do
    {
      do
        return i;
      while (paramDPObject == null);
      this.curRegion = paramDPObject;
      return true;
    }
    while ((paramDPObject.getInt("ID") == this.curRegion.getInt("ID")) && (paramDPObject.getInt("ParentID") == this.curRegion.getInt("ParentID")));
    DPObject localDPObject = paramDPObject;
    if (paramDPObject.getString("Name") != null)
    {
      localDPObject = paramDPObject;
      if (paramDPObject.getString("Name").contains("（智能范围）"))
        localDPObject = new DPObject("Pair").edit().putInt("ID", paramDPObject.getInt("ID")).putString("Name", paramDPObject.getString("Name").replace("（智能范围）", "")).putInt("ParentID", paramDPObject.getInt("ParentID")).generate();
    }
    this.curRegion = localDPObject;
    return true;
  }

  public boolean setCurSelectNavs(DPObject paramDPObject)
  {
    if ((paramDPObject == null) || (this.curSelectNavs == null))
    {
      this.curSelectNavs = paramDPObject;
      return true;
    }
    if (paramDPObject.getInt("FilterId") == this.curSelectNavs.getInt("FilterId"))
      return false;
    this.curSelectNavs = paramDPObject;
    return true;
  }

  public void setGATag(String paramString)
  {
    this.GATag = paramString;
  }

  protected void updateNavs(DPObject paramDPObject)
  {
    int i;
    if ((this.curFilterSiftPair != null) && ("".equals(this.curFilterSiftPair.getString("Name"))) && (paramDPObject.getArray("SiftNavs") != null))
    {
      i = 0;
      if (i < paramDPObject.getArray("SiftNavs").length)
      {
        if (!this.curFilterSiftPair.getString("ID").equals(paramDPObject.getArray("SiftNavs")[i].getString("ID")))
          break label561;
        this.curFilterSiftPair = paramDPObject.getArray("SiftNavs")[i];
      }
    }
    if ((this.curCategory != null) && ("".equals(this.curCategory.getString("Name"))) && (paramDPObject.getArray("CategoryNavs") != null))
    {
      i = 0;
      label126: if (i < paramDPObject.getArray("CategoryNavs").length)
      {
        if (this.curCategory.getInt("ID") != paramDPObject.getArray("CategoryNavs")[i].getInt("ID"))
          break label568;
        this.curCategory = paramDPObject.getArray("CategoryNavs")[i];
      }
    }
    if ((this.curRegion != null) && ("".equals(this.curRegion.getString("Name"))) && (paramDPObject.getArray("RegionNavs") != null))
    {
      i = 0;
      label213: if (i < paramDPObject.getArray("RegionNavs").length)
      {
        if (this.curRegion.getInt("ID") != paramDPObject.getArray("RegionNavs")[i].getInt("ID"))
          break label575;
        this.curRegion = paramDPObject.getArray("RegionNavs")[i];
      }
    }
    if ((this.curFilterIdPair != null) && ("".equals(this.curFilterIdPair.getString("Name"))) && (paramDPObject.getArray("FilterNavs") != null))
      i = 0;
    while (true)
    {
      if (i < paramDPObject.getArray("FilterNavs").length)
      {
        if (this.curFilterIdPair.getString("ID").equals(paramDPObject.getArray("FilterNavs")[i].getString("ID")))
          this.curFilterIdPair = paramDPObject.getArray("FilterNavs")[i];
      }
      else
      {
        if (paramDPObject.getObject("CurrentSift") != null)
          this.curFilterSiftPair = paramDPObject.getObject("CurrentSift");
        if (paramDPObject.getObject("CurrentFilter") != null)
          this.curFilterIdPair = paramDPObject.getObject("CurrentFilter");
        if (paramDPObject.getObject("CurrentRange") != null)
          this.curRangePair = paramDPObject.getObject("CurrentRange");
        if (paramDPObject.getObject("CurrentSelect") != null)
          this.curSelectNavs = paramDPObject.getObject("CurrentSelect");
        if ((paramDPObject.getArray("SiftNavs") != null) && (paramDPObject.getArray("SiftNavs").length > 0))
          this.filterSiftPairs = paramDPObject.getArray("SiftNavs");
        if (paramDPObject.getArray("RegionNavs") != null)
          this.filterRegions = paramDPObject.getArray("RegionNavs");
        if (paramDPObject.getArray("CategoryNavs") != null)
          this.filterCategories = paramDPObject.getArray("CategoryNavs");
        if (paramDPObject.getArray("RangeNavs") != null)
          this.filterRanges = paramDPObject.getArray("RangeNavs");
        if (paramDPObject.getArray("FilterNavs") != null)
          this.filterIdPairs = paramDPObject.getArray("FilterNavs");
        publishDataChange(20);
        return;
        label561: i += 1;
        break;
        label568: i += 1;
        break label126;
        label575: i += 1;
        break label213;
      }
      i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.data.DefaultSearchShopListDataSource
 * JD-Core Version:    0.6.0
 */