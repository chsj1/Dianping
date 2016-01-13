package com.dianping.membercard.utils;

import android.content.Context;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.membercard.view.AddedTimesItemView;
import com.dianping.membercard.view.OneLineListItemView;
import com.dianping.membercard.view.OneLineListItemView.ItemStyle;
import com.dianping.membercard.view.ShopPowerItemView;
import com.dianping.membercard.view.TwoLineListItemView;
import com.dianping.membercard.view.TwoLineListItemView.ItemStyle;
import com.dianping.membercard.view.WeLifeCardProductListItemView;

public class ProductListItemFactory
{
  public static View create(Context paramContext, ProductListItemStyle paramProductListItemStyle)
  {
    switch (1.$SwitchMap$com$dianping$membercard$utils$ProductListItemStyle[paramProductListItemStyle.ordinal()])
    {
    default:
      return null;
    case 1:
      return new AddedTimesItemView(paramContext);
    case 2:
      paramContext = new TwoLineListItemView(paramContext);
      paramContext.configStyles(new TwoLineListItemView.ItemStyle[] { TwoLineListItemView.ItemStyle.ICON_GONE, TwoLineListItemView.ItemStyle.UNCLICKABLE, TwoLineListItemView.ItemStyle.ARROW_GONE });
      return paramContext;
    case 3:
      paramContext = new OneLineListItemView(paramContext);
      paramContext.configStyles(new OneLineListItemView.ItemStyle[] { OneLineListItemView.ItemStyle.ARROW_GONE, OneLineListItemView.ItemStyle.ICON_GONE });
      return paramContext;
    case 4:
      return new ShopPowerItemView(paramContext);
    case 5:
    }
    paramContext = new WeLifeCardProductListItemView(paramContext);
    paramContext.configStyles(new TwoLineListItemView.ItemStyle[] { TwoLineListItemView.ItemStyle.ICON_GONE, TwoLineListItemView.ItemStyle.UNCLICKABLE, TwoLineListItemView.ItemStyle.ARROW_GONE });
    return paramContext;
  }

  public static AddedTimesItemView createAddedTimesItem(Context paramContext, DPObject paramDPObject)
  {
    return ((AddedTimesItemView)create(paramContext, ProductListItemStyle.ADDED_TIMES_PRODUCT)).loadData(paramDPObject);
  }

  public static OneLineListItemView createProductItemJumpView(Context paramContext, ProductType paramProductType, CharSequence paramCharSequence)
  {
    return ((OneLineListItemView)create(paramContext, ProductListItemStyle.ONE_LINE_TEXT_PRODUCT)).configStyles(new OneLineListItemView.ItemStyle[] { OneLineListItemView.ItemStyle.ARROW_JUMPABLE }).setIconImage(paramProductType).setTitleText(paramCharSequence);
  }

  public static OneLineListItemView createProductOneTextItem(Context paramContext, CharSequence paramCharSequence)
  {
    return ((OneLineListItemView)create(paramContext, ProductListItemStyle.ONE_LINE_TEXT_PRODUCT)).setTitleText(paramCharSequence);
  }

  public static OneLineListItemView createProductOneTextItemWithIcon(Context paramContext, ProductType paramProductType, CharSequence paramCharSequence)
  {
    paramContext = (OneLineListItemView)create(paramContext, ProductListItemStyle.ONE_LINE_TEXT_PRODUCT);
    paramContext.setIconImage(paramProductType);
    return paramContext.setTitleText(paramCharSequence);
  }

  public static ShopPowerItemView createShopItemView(Context paramContext, CharSequence paramCharSequence, int paramInt)
  {
    paramContext = (ShopPowerItemView)create(paramContext, ProductListItemStyle.SHOP_POWER_PRODUCT);
    paramContext.configStyles(new OneLineListItemView.ItemStyle[] { OneLineListItemView.ItemStyle.ARROW_JUMPABLE, OneLineListItemView.ItemStyle.ICON_GONE });
    paramContext.setTitleText(paramCharSequence);
    paramContext.setShopPower(paramInt);
    return paramContext;
  }

  public static TwoLineListItemView createTwoTextLineItem(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    paramContext = (TwoLineListItemView)create(paramContext, ProductListItemStyle.TWO_LINE_TEXT_PRODUCT);
    paramContext.setTitle(paramCharSequence1);
    paramContext.setDescription(paramCharSequence2);
    return paramContext;
  }

  public static TwoLineListItemView createTwoTextLineItemWithIcon(Context paramContext, ProductType paramProductType, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    return createTwoTextLineItem(paramContext, paramCharSequence1, paramCharSequence2).setIconImage(paramProductType);
  }

  public static WeLifeCardProductListItemView createWeLifeCardProductListItemWithIcon(Context paramContext, ProductType paramProductType, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    paramContext = (WeLifeCardProductListItemView)create(paramContext, ProductListItemStyle.WELIFE_PRODUCT);
    paramContext.setTitle(paramCharSequence1);
    paramContext.setDescription(paramCharSequence2);
    paramContext.setIconImage(paramProductType);
    return paramContext;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.ProductListItemFactory
 * JD-Core Version:    0.6.0
 */