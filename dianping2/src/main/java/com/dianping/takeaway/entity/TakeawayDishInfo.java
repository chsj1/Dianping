package com.dianping.takeaway.entity;

import com.dianping.takeaway.util.TakeawayUtils;
import java.text.DecimalFormat;

public class TakeawayDishInfo
{
  public String bigImageUrl;
  public String categoryName;
  public double curPrice;
  public TakeawayDishGroup dishGroup;
  public String dishIntro;
  public String dishName;
  public String hotNum;
  public int id;
  public boolean isSoldout;
  public boolean isTitle;
  public int limitNum;
  public String littleImageUrl;
  public int minFeeCalType;
  public double originPrice;
  public String salesVolume;
  public int selectedNum;
  public int status;
  public String tip;
  public int totalNum;

  public boolean cancel()
  {
    if (this.dishGroup.cancel())
    {
      if (this.selectedNum > 0)
        this.selectedNum -= 1;
      return true;
    }
    return false;
  }

  public String getCurrentPrice()
  {
    return TakeawayUtils.PRICE_DF.format(this.curPrice);
  }

  public String getOldPrice()
  {
    return TakeawayUtils.PRICE_DF.format(this.originPrice);
  }

  public boolean hasOldPrice()
  {
    return this.originPrice >= 0.0D;
  }

  public boolean hasOrder()
  {
    return this.selectedNum != 0;
  }

  public boolean isInShortSupply()
  {
    return (this.limitNum == 0) || (this.totalNum == 0);
  }

  public int maxAvailableNum()
  {
    int j = -1;
    int i;
    if ((this.limitNum > 0) && (this.totalNum > 0))
      i = Math.min(this.limitNum, this.totalNum);
    do
    {
      do
      {
        return i;
        if ((this.limitNum > 0) && (this.totalNum == -1))
          return this.limitNum;
        i = j;
      }
      while (this.limitNum != -1);
      i = j;
    }
    while (this.totalNum <= 0);
    return this.totalNum;
  }

  public boolean order()
  {
    int i = maxAvailableNum();
    if ((i == -1) || ((i > 0) && (this.selectedNum < i)));
    for (i = 1; (i != 0) && (this.dishGroup.order()); i = 0)
    {
      this.selectedNum += 1;
      return true;
    }
    return false;
  }

  public static class Builder
  {
    public static TakeawayDishInfo buildContentItemInfo(TakeawayDishGroup paramTakeawayDishGroup, String paramString1, int paramInt1, String paramString2, String paramString3, double paramDouble1, double paramDouble2, int paramInt2, boolean paramBoolean, int paramInt3, String paramString4, String paramString5, String paramString6, String paramString7, int paramInt4, int paramInt5, String paramString8)
    {
      TakeawayDishInfo localTakeawayDishInfo = new TakeawayDishInfo();
      localTakeawayDishInfo.dishGroup = paramTakeawayDishGroup;
      localTakeawayDishInfo.categoryName = paramString1;
      localTakeawayDishInfo.isTitle = false;
      localTakeawayDishInfo.id = paramInt1;
      localTakeawayDishInfo.dishName = paramString2;
      localTakeawayDishInfo.salesVolume = paramString3;
      localTakeawayDishInfo.originPrice = paramDouble1;
      localTakeawayDishInfo.curPrice = paramDouble2;
      localTakeawayDishInfo.minFeeCalType = paramInt2;
      localTakeawayDishInfo.isSoldout = paramBoolean;
      localTakeawayDishInfo.status = paramInt3;
      localTakeawayDishInfo.hotNum = paramString4;
      localTakeawayDishInfo.bigImageUrl = paramString5;
      localTakeawayDishInfo.littleImageUrl = paramString6;
      localTakeawayDishInfo.dishIntro = paramString7;
      localTakeawayDishInfo.limitNum = paramInt4;
      localTakeawayDishInfo.totalNum = paramInt5;
      localTakeawayDishInfo.tip = paramString8;
      return localTakeawayDishInfo;
    }

    public static TakeawayDishInfo buildTitleItemInfo(int paramInt1, int paramInt2)
    {
      TakeawayDishInfo localTakeawayDishInfo = new TakeawayDishInfo();
      localTakeawayDishInfo.id = paramInt1;
      localTakeawayDishInfo.selectedNum = paramInt2;
      return localTakeawayDishInfo;
    }

    public static TakeawayDishInfo buildTitleItemInfo(TakeawayDishGroup paramTakeawayDishGroup, String paramString)
    {
      TakeawayDishInfo localTakeawayDishInfo = new TakeawayDishInfo();
      localTakeawayDishInfo.dishGroup = paramTakeawayDishGroup;
      localTakeawayDishInfo.isTitle = true;
      localTakeawayDishInfo.categoryName = paramString;
      return localTakeawayDishInfo;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayDishInfo
 * JD-Core Version:    0.6.0
 */