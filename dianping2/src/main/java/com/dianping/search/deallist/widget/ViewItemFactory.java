package com.dianping.search.deallist.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.base.tuan.widget.TravelDealListItem;
import com.dianping.base.tuan.widget.ViewItemLocation;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.AggDealListItem;
import com.dianping.base.widget.DealListItem;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.search.widget.ShopViewItem;
import com.dianping.v1.R.layout;

public class ViewItemFactory
{
  public static View createAggDeal(Context paramContext, DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    paramContext = (AggDealListItem)LayoutInflater.from(paramContext).inflate(R.layout.deal_list_item_agg, null);
    if (DPObjectUtils.isDPObjectof(paramDPObject))
      paramContext.setDeal(paramDPObject.getObject("Deal"), paramDouble1, paramDouble2, paramBoolean);
    paramContext.setClickable(true);
    return paramContext;
  }

  public static View createAggHui(Context paramContext, DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    paramContext = (AggHuiViewItem)LayoutInflater.from(paramContext).inflate(R.layout.agg_hui_view_item, null);
    if (DPObjectUtils.isDPObjectof(paramDPObject))
      paramContext.setData(paramDPObject.getObject("Hui"), paramDouble1, paramDouble2, paramBoolean);
    paramContext.setClickable(true);
    return paramContext;
  }

  public static View createAggrationView(Context paramContext, DPObject paramDPObject, boolean paramBoolean, double paramDouble1, double paramDouble2)
  {
    paramContext = (AggViewItem)LayoutInflater.from(paramContext).inflate(R.layout.agg_view_item, null);
    if (DPObjectUtils.isDPObjectof(paramDPObject))
      paramContext.setData(paramDPObject.getObject("Agg"), paramDouble1, paramDouble2, paramBoolean);
    return paramContext;
  }

  public static View createBanner(Context paramContext, DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    paramContext = (BannerViewItem)LayoutInflater.from(paramContext).inflate(R.layout.deal_list_header_view, null);
    paramContext.updateData(paramDPObject, 0.0D, 0.0D, true);
    return paramContext;
  }

  protected static View createExtendShop(Context paramContext, DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    paramDPObject = paramDPObject.getObject("Shop");
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "Shop"))
      return null;
    paramContext = (ShopListItem)LayoutInflater.from(paramContext).inflate(R.layout.shop_item, null, false);
    paramContext.setShop(new ShopDataModel(paramDPObject), -1, paramDouble1, paramDouble2, paramBoolean, false);
    paramContext.useTopLine();
    return paramContext;
  }

  public static View createHotelZeus(Context paramContext, DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (paramDPObject == null)
      return null;
    paramContext = (HotelZuesViewItem)LayoutInflater.from(paramContext).inflate(R.layout.agg_hotel_zues_view_item, null);
    paramContext.updateData(paramDPObject, paramDouble1, paramDouble2, paramBoolean);
    return paramContext;
  }

  public static View createHui(Context paramContext, DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    paramContext = (HuiViewItem)LayoutInflater.from(paramContext).inflate(R.layout.hui_view_item, null);
    if (DPObjectUtils.isDPObjectof(paramDPObject))
      paramContext.updateData(paramDPObject, paramDouble1, paramDouble2, paramBoolean);
    return paramContext;
  }

  public static View createMarketAggration(Context paramContext, DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (paramDPObject == null)
      return null;
    paramContext = (MarketAggViewItem)LayoutInflater.from(paramContext).inflate(R.layout.market_agg_view_item, null);
    paramContext.updateData(paramDPObject, paramDouble1, paramDouble2, paramBoolean);
    return paramContext;
  }

  public static View createMarketShopAgg(Context paramContext, DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (paramDPObject == null)
      return null;
    paramContext = (ShopViewItem)LayoutInflater.from(paramContext).inflate(R.layout.shop_view_item, null);
    paramContext.updateData(paramDPObject, paramDouble1, paramDouble2, paramBoolean);
    return paramContext;
  }

  public static View createNoResultTip(Context paramContext, DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    paramContext = (WarningTitleTipViewItem)LayoutInflater.from(paramContext).inflate(R.layout.search_deal_empty_view, null);
    paramContext.updateData(paramDPObject, 0.0D, 0.0D, true);
    return paramContext;
  }

  public static View createRightTag(Context paramContext, DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    paramContext = (RightTagViewItem)LayoutInflater.from(paramContext).inflate(R.layout.deal_list_recommend_singal_item, null);
    paramContext.setData(paramDPObject);
    return paramContext;
  }

  public static View createShopView(Context paramContext, DPObject paramDPObject, double paramDouble1, double paramDouble2)
  {
    paramContext = (AggMainShopViewItem)LayoutInflater.from(paramContext).inflate(R.layout.agg_main_shop_view_item, null);
    if (DPObjectUtils.isDPObjectof(paramDPObject))
      paramContext.setShopInfo(paramDPObject.getObject("Shop"), paramDouble1, paramDouble2);
    return paramContext;
  }

  public static View createTitleTip(Context paramContext, DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    paramContext = (TitleTipViewItem)LayoutInflater.from(paramContext).inflate(R.layout.search_deal_recommend_view, null);
    paramContext.setData(paramDPObject);
    return paramContext;
  }

  public static View createTuanDeal(Context paramContext, DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    Object localObject2 = null;
    Object localObject1 = localObject2;
    if (DPObjectUtils.isDPObjectof(paramDPObject))
    {
      DPObject localDPObject = paramDPObject.getObject("Deal");
      localObject1 = localObject2;
      if (DPObjectUtils.isDPObjectof(paramDPObject.getObject("Deal")))
      {
        localObject1 = localObject2;
        if (hasTag(localDPObject.getStringArray("DealChannelTags"), "travel"))
          localObject1 = (TravelDealListItem)LayoutInflater.from(paramContext).inflate(R.layout.travel_deal_list_item, null);
      }
    }
    localObject2 = localObject1;
    if (localObject1 == null)
      localObject2 = (DealListItem)LayoutInflater.from(paramContext).inflate(R.layout.deal_list_item, null);
    if ((DPObjectUtils.isDPObjectof(paramDPObject)) && (localObject2 != null))
      ((DealListItem)localObject2).setDeal(paramDPObject.getObject("Deal"), paramDouble1, paramDouble2, paramBoolean, 1);
    return (View)(View)localObject2;
  }

  public static View getView(Context paramContext, DPObject paramDPObject, boolean paramBoolean, double paramDouble1, double paramDouble2, ViewItemLocation paramViewItemLocation)
  {
    View localView = new View(paramContext);
    Object localObject = localView;
    if (DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"))
    {
      localObject = ViewItemType.parseFromValue(paramDPObject.getInt("Type"));
      switch (1.$SwitchMap$com$dianping$base$widget$ViewItemType[localObject.ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      }
    }
    while (localView != null)
    {
      localView.setTag(paramDPObject);
      localObject = localView;
      return localObject;
      localView = createNoResultTip(paramContext, paramDPObject);
      continue;
      localView = createTitleTip(paramContext, paramDPObject);
      continue;
      localView = createRightTag(paramContext, paramDPObject);
      continue;
      if (paramViewItemLocation == ViewItemLocation.TUAN_DEAL_LIST_AGG)
      {
        localView = createShopView(paramContext, paramDPObject, paramDouble1, paramDouble2);
        continue;
      }
      if (paramViewItemLocation == ViewItemLocation.TUAN_DEAL_LIST_MALL_AGG)
      {
        localView = createMarketShopAgg(paramContext, paramDPObject, paramDouble1, paramDouble2, paramBoolean);
        continue;
      }
      if (paramViewItemLocation != ViewItemLocation.TUAN_DEAL_LIST)
        continue;
      localView = createExtendShop(paramContext, paramDPObject, paramDouble1, paramDouble2, paramBoolean);
      continue;
      localView = createAggrationView(paramContext, paramDPObject, paramBoolean, paramDouble1, paramDouble2);
      continue;
      if (paramViewItemLocation == ViewItemLocation.TUAN_DEAL_LIST_AGG)
      {
        localView = createAggHui(paramContext, paramDPObject, paramDouble1, paramDouble2, paramBoolean);
        continue;
      }
      localView = createHui(paramContext, paramDPObject, paramDouble1, paramDouble2, paramBoolean);
      continue;
      if (paramViewItemLocation == ViewItemLocation.TUAN_DEAL_LIST_AGG)
      {
        localView = createAggDeal(paramContext, paramDPObject, paramDouble1, paramDouble2, paramBoolean);
        continue;
      }
      localView = createTuanDeal(paramContext, paramDPObject, paramDouble1, paramDouble2, paramBoolean);
      continue;
      localView = createMarketAggration(paramContext, paramDPObject, paramDouble1, paramDouble2, paramBoolean);
      continue;
      localView = createBanner(paramContext, paramDPObject);
      continue;
      localView = createHotelZeus(paramContext, paramDPObject, paramDouble1, paramDouble2, paramBoolean);
    }
    return (View)new View(paramContext);
  }

  public static boolean hasTag(String[] paramArrayOfString, String paramString)
  {
    boolean bool2 = false;
    try
    {
      int j = paramArrayOfString.length;
      int i = 0;
      while (true)
      {
        boolean bool1 = bool2;
        if (i < j)
        {
          bool1 = paramArrayOfString[i].equals(paramString);
          if (bool1)
            bool1 = true;
        }
        else
        {
          return bool1;
        }
        i += 1;
      }
    }
    catch (java.lang.Exception paramArrayOfString)
    {
    }
    return false;
  }

  public static boolean match(DPObject paramDPObject, View paramView)
  {
    if (paramView == null);
    do
      return false;
    while ((!DPObjectUtils.isDPObjectof(paramView.getTag(), "ViewItem")) || (!(paramView instanceof ViewItemInterface)) || (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem")) || (ViewItemType.parseFromValue(paramDPObject.getInt("Type")) != ((ViewItemInterface)paramView).getType()));
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.widget.ViewItemFactory
 * JD-Core Version:    0.6.0
 */