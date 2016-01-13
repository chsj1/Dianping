package com.dianping.search.deallist.agent;

import com.dianping.archive.DPObject;
import com.dianping.base.tuan.utils.TuanSharedDataKey;

public class DealListTabAgent extends DealListAgent
{
  public static final int NULL_RANGE = -2;
  public static final int NULL_REGION = -2;

  public DealListTabAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected String aggViewItemExpandGaAction()
  {
    return "tuan5_shopjuhe_expand";
  }

  public StringBuilder createDealRequestUrl(int paramInt)
  {
    StringBuilder localStringBuilder = super.createDealRequestUrl(paramInt);
    paramInt = 0;
    if (getSharedBoolean(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA))
    {
      paramInt = getShopCategoryId().intValue();
      localStringBuilder.append("&shopcategoryid=").append(paramInt + "");
      paramInt = 0;
      if (!getSharedBoolean(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA))
        break label162;
      paramInt = getShopSortId().intValue();
      label75: localStringBuilder.append("&shopsortid=").append(paramInt + "");
      if (!getSharedBoolean(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA))
        break label210;
      if (getShopRangeId().intValue() == -2)
        break label182;
      localStringBuilder.append("&distance=").append(getShopRangeId());
    }
    label162: label182: label210: DPObject localDPObject;
    do
    {
      do
      {
        return localStringBuilder;
        if (getCurrentCategory() == null)
          break;
        paramInt = getCurrentCategory().getInt("ExtraId");
        break;
        if (getCurrentSort() == null)
          break label75;
        paramInt = getCurrentSort().getInt("ExtraId");
        break label75;
      }
      while (getShopRegionId().intValue() == -2);
      localStringBuilder.append("&shopregionid=").append(getShopRegionId());
      return localStringBuilder;
      localDPObject = getCurrentRegion();
    }
    while (localDPObject == null);
    if (localDPObject.getInt("Type") == 3)
    {
      localStringBuilder.append("&distance=").append(localDPObject.getInt("ExtraId"));
      return localStringBuilder;
    }
    localStringBuilder.append("shopregionid=").append(localDPObject.getInt("ExtraId"));
    return localStringBuilder;
  }

  protected Integer getShopCategoryId()
  {
    return Integer.valueOf(getSharedInt(TuanSharedDataKey.DEAL_LIST_SHOP_CATEGORY_ID));
  }

  protected Integer getShopRangeId()
  {
    return Integer.valueOf(getSharedInt(TuanSharedDataKey.DEAL_LIST_SHOP_RANGE_ID));
  }

  protected Integer getShopRegionId()
  {
    return Integer.valueOf(getSharedInt(TuanSharedDataKey.DEAL_LIST_SHOP_REGION_ID));
  }

  protected Integer getShopSortId()
  {
    return Integer.valueOf(getSharedInt(TuanSharedDataKey.DEAL_LIST_SHOP_SORT_ID));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.agent.DealListTabAgent
 * JD-Core Version:    0.6.0
 */