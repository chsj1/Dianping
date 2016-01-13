package com.dianping.base.shoplist.data;

import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.data.model.ShopListDataSource;

@Deprecated
public abstract interface SearchShopListDataSource extends ShopListDataSource
{
  public abstract String GATag();

  public abstract DPObject curCategory();

  public abstract DPObject curFilterId();

  public abstract DPObject curRange();

  public abstract DPObject curRegion();

  public abstract DPObject[] filterCategories();

  public abstract DPObject[] filterIds();

  public abstract DPObject[] filterRanges();

  public abstract DPObject[] filterRegions();

  public abstract String[] searchTips();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.shoplist.data.SearchShopListDataSource
 * JD-Core Version:    0.6.0
 */