package com.dianping.membercard.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.membercard.view.AddedTimesItemView;
import com.dianping.membercard.view.OneLineListItemView;
import com.dianping.membercard.view.TwoLineListItemView;
import com.dianping.membercard.view.TwoLineListItemView.ItemStyle;
import com.dianping.membercard.view.WeLifeCardProductListItemView;

public class ViewHolderFactory
{
  public static ViewHolder createDiscount(Context paramContext, DPObject paramDPObject, ProductListItemStyle paramProductListItemStyle)
  {
    ProductType localProductType = ProductType.valueOf(paramDPObject);
    if ((paramProductListItemStyle == null) || (paramProductListItemStyle == ProductListItemStyle.TWO_LINE_TEXT_PRODUCT));
    for (paramContext = createTwoLineExpandbleViewHolder(paramContext, localProductType, MCUtils.filterTextLine(paramDPObject.getString("ProductName")), paramDPObject.getString("ProductDesc"), paramProductListItemStyle); ; paramContext = new BaseViewHolder(paramContext))
    {
      paramContext.getView().setTag(paramDPObject);
      return paramContext;
      paramContext = ProductListItemFactory.createProductItemJumpView(paramContext, localProductType, MCUtils.filterTextLine(paramDPObject.getString("ProductName")));
      paramContext.setIconImage(localProductType);
    }
  }

  public static ViewHolder createFreeSend(Context paramContext, DPObject paramDPObject, ProductListItemStyle paramProductListItemStyle)
  {
    paramContext = createTwoLineExpandbleViewHolder(paramContext, ProductType.valueOf(paramDPObject), paramDPObject.getString("ProductName"), paramDPObject.getString("ProductDesc"), paramProductListItemStyle);
    paramContext.getView().setTag(paramDPObject);
    return paramContext;
  }

  public static ViewHolder createPoint(Context paramContext, DPObject paramDPObject, ProductListItemStyle paramProductListItemStyle)
  {
    paramContext = createTwoLineExpandbleViewHolder(paramContext, ProductType.valueOf(paramDPObject), paramDPObject.getString("CardPointTitle"), paramDPObject.getString("CardPointDesc"), paramProductListItemStyle);
    paramContext.getView().setTag(paramDPObject);
    return paramContext;
  }

  public static ViewHolder createProductViewHolder(Context paramContext, DPObject paramDPObject, ProductListItemStyle paramProductListItemStyle)
  {
    switch (1.$SwitchMap$com$dianping$membercard$utils$ProductType[ProductType.valueOf(paramDPObject).ordinal()])
    {
    default:
      return null;
    case 1:
      return createFreeSend(paramContext, paramDPObject, paramProductListItemStyle);
    case 2:
      return createPoint(paramContext, paramDPObject, paramProductListItemStyle);
    case 3:
      return createDiscount(paramContext, paramDPObject, paramProductListItemStyle);
    case 4:
      return createSuspend(paramContext, paramDPObject, paramProductListItemStyle);
    case 5:
      return createSaving(paramContext, paramDPObject, paramProductListItemStyle);
    case 6:
      return createTimes(paramContext, paramDPObject, paramProductListItemStyle);
    case 7:
      return createScore(paramContext, paramDPObject, paramProductListItemStyle);
    case 8:
      return createWeLifeView(paramContext, paramDPObject, paramProductListItemStyle);
    case 9:
    }
    return createWeLifeView(paramContext, paramDPObject, paramProductListItemStyle);
  }

  public static ViewHolder createProductViewHolderForWeLife(Context paramContext, DPObject paramDPObject, ProductListItemStyle paramProductListItemStyle)
  {
    switch (1.$SwitchMap$com$dianping$membercard$utils$ProductType[ProductType.valueOf(paramDPObject).ordinal()])
    {
    case 2:
    default:
      return createProductViewHolder(paramContext, paramDPObject, paramProductListItemStyle);
    case 1:
      return createWeLifeView(paramContext, paramDPObject, paramProductListItemStyle);
    case 3:
    }
    return createWeLifeView(paramContext, paramDPObject, paramProductListItemStyle);
  }

  public static ViewHolder createSaving(Context paramContext, DPObject paramDPObject, ProductListItemStyle paramProductListItemStyle)
  {
    String str1 = paramDPObject.getString("ProductName");
    String str2 = paramDPObject.getString("ProductDesc");
    if ((paramProductListItemStyle == null) || (ProductListItemStyle.TWO_LINE_TEXT_PRODUCT == paramProductListItemStyle))
    {
      paramContext = createTwoLineViewHolder(paramContext, str1, str2, paramProductListItemStyle);
      ((TwoLineListItemView)paramContext.getView()).setIconImage(ProductType.valueOf(paramDPObject));
      paramContext.getView().setTag(paramDPObject);
      return paramContext;
    }
    if (ProductListItemStyle.ADDED_SAVE_PRODUCT == paramProductListItemStyle)
    {
      paramContext = ProductListItemFactory.createTwoTextLineItem(paramContext, str1, str2);
      paramContext.configStyles(new TwoLineListItemView.ItemStyle[] { TwoLineListItemView.ItemStyle.JUMPABLE });
      paramContext.setIconImage(ProductType.SAVING);
      return new BaseViewHolder(paramContext);
    }
    return null;
  }

  public static ViewHolder createScore(Context paramContext, DPObject paramDPObject, ProductListItemStyle paramProductListItemStyle)
  {
    ProductType localProductType = ProductType.valueOf(paramDPObject);
    if ((paramProductListItemStyle == null) || (paramProductListItemStyle == ProductListItemStyle.TWO_LINE_TEXT_PRODUCT));
    for (paramContext = createTwoLineExpandbleViewHolder(paramContext, localProductType, MCUtils.filterTextLine(paramDPObject.getString("ProductName")), paramDPObject.getString("ProductDesc"), paramProductListItemStyle); ; paramContext = new BaseViewHolder(paramContext))
    {
      paramContext.getView().setTag(paramDPObject);
      return paramContext;
      paramContext = ProductListItemFactory.createProductItemJumpView(paramContext, localProductType, MCUtils.filterTextLine(paramDPObject.getString("ProductName")));
      paramContext.setIconImage(localProductType);
    }
  }

  public static ViewHolder createSuspend(Context paramContext, DPObject paramDPObject, ProductListItemStyle paramProductListItemStyle)
  {
    paramProductListItemStyle = ProductType.valueOf(paramDPObject);
    String str = paramDPObject.getString("ProductName");
    paramDPObject.getString("ProductDesc");
    paramContext = ProductListItemFactory.createProductOneTextItemWithIcon(paramContext, paramProductListItemStyle, str);
    paramContext.setTag(paramDPObject);
    return new BaseViewHolder(paramContext);
  }

  public static ViewHolder createTimes(Context paramContext, DPObject paramDPObject, ProductListItemStyle paramProductListItemStyle)
  {
    ProductType localProductType = ProductType.valueOf(paramDPObject);
    if ((paramProductListItemStyle == null) || (ProductListItemStyle.TWO_LINE_TEXT_PRODUCT == paramProductListItemStyle))
    {
      paramContext = createTwoLineViewHolder(paramContext, paramDPObject.getString("ProductName"), paramDPObject.getString("ProductDesc"), paramProductListItemStyle);
      ((TwoLineListItemView)paramContext.getView()).setIconImage(localProductType);
      paramContext.getView().setTag(paramDPObject);
      return paramContext;
    }
    if (ProductListItemStyle.ADDED_TIMES_PRODUCT == paramProductListItemStyle)
    {
      paramContext = ProductListItemFactory.createAddedTimesItem(paramContext, paramDPObject);
      paramContext.setTag(paramDPObject);
      return new BaseViewHolder(paramContext);
    }
    return null;
  }

  public static ViewHolder createTwoLineExpandbleViewHolder(Context paramContext, ProductType paramProductType, CharSequence paramCharSequence1, CharSequence paramCharSequence2, ProductListItemStyle paramProductListItemStyle)
  {
    return new BaseViewHolder(ProductListItemFactory.createTwoTextLineItemWithIcon(paramContext, paramProductType, paramCharSequence1, paramCharSequence2).configStyles(new TwoLineListItemView.ItemStyle[] { TwoLineListItemView.ItemStyle.EXPANDABLE_CLOSED }));
  }

  public static ViewHolder createTwoLineViewHolder(Context paramContext, String paramString1, String paramString2, ProductListItemStyle paramProductListItemStyle)
  {
    return new BaseViewHolder(ProductListItemFactory.createTwoTextLineItem(paramContext, paramString1, paramString2));
  }

  public static ViewHolder createWeLifeExpandbleViewHolder(Context paramContext, ProductType paramProductType, CharSequence paramCharSequence1, CharSequence paramCharSequence2, ProductListItemStyle paramProductListItemStyle)
  {
    paramContext = ProductListItemFactory.createWeLifeCardProductListItemWithIcon(paramContext, paramProductType, paramCharSequence1, paramCharSequence2);
    paramContext.configStyles(new TwoLineListItemView.ItemStyle[] { TwoLineListItemView.ItemStyle.EXPANDABLE_CLOSED });
    return new BaseViewHolder(paramContext);
  }

  private static ViewHolder createWeLifeView(Context paramContext, DPObject paramDPObject, ProductListItemStyle paramProductListItemStyle)
  {
    ProductType localProductType = ProductType.valueOf(paramDPObject);
    String str3 = paramDPObject.getString("ProductName");
    String str2 = paramDPObject.getString("ProductDesc");
    String str1 = str2;
    if (!TextUtils.isEmpty(str2))
      str1 = "● " + str2.replace("\n", "\n● ");
    paramContext = createWeLifeExpandbleViewHolder(paramContext, localProductType, str3, str1, paramProductListItemStyle);
    paramContext.getView().setTag(paramDPObject);
    return paramContext;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.ViewHolderFactory
 * JD-Core Version:    0.6.0
 */