package com.dianping.membercard.utils;

import android.text.TextUtils;
import com.dianping.archive.DPObject;
import java.util.ArrayList;

public class MemberCard
{
  protected DPObject cardObject;
  protected int cardlevel;

  public MemberCard(DPObject paramDPObject)
  {
    this.cardObject = paramDPObject;
    this.cardlevel = paramDPObject.getInt("CardLevel");
  }

  public static DPObject getGeneralCardAddButtonProduct(DPObject paramDPObject, int paramInt)
  {
    Object localObject5 = null;
    Object localObject4 = null;
    Object localObject1 = null;
    Object localObject3 = null;
    Object localObject2 = null;
    DPObject localDPObject = null;
    Object localObject6 = null;
    DPObject[] arrayOfDPObject = paramDPObject.getArray("ProductList");
    new MemberCard(paramDPObject).setCardlevel(paramInt);
    if (paramInt == MCUtils.VIP_CARD_LEVEL)
      paramDPObject = getVIPCard(paramDPObject);
    do
    {
      do
      {
        return paramDPObject;
        paramDPObject = localObject5;
      }
      while (paramInt != MCUtils.GENERAL_CARD_LEVEL);
      if (arrayOfDPObject != null)
      {
        int i = arrayOfDPObject.length;
        paramInt = 0;
        paramDPObject = localObject6;
        localObject3 = localObject2;
        localObject4 = localObject1;
        localDPObject = paramDPObject;
        if (paramInt < i)
        {
          localObject3 = arrayOfDPObject[paramInt];
          switch (1.$SwitchMap$com$dianping$membercard$utils$ProductType[ProductType.valueOf(localObject3).ordinal()])
          {
          default:
          case 1:
          case 2:
          case 3:
          }
          while (true)
          {
            paramInt += 1;
            break;
            localObject2 = localObject3;
            continue;
            localObject1 = localObject3;
            continue;
            paramDPObject = localObject3;
          }
        }
      }
      if (localDPObject != null)
        return localDPObject;
      if (localObject3 != null)
        return localObject3;
      paramDPObject = localObject5;
    }
    while (localObject4 == null);
    return localObject4;
  }

  public static DPObject getVIPCard(DPObject paramDPObject)
  {
    if (paramDPObject.getArray("ProductList") == null)
    {
      paramDPObject = null;
      return paramDPObject;
    }
    DPObject[] arrayOfDPObject = paramDPObject.getArray("ProductList");
    int j = arrayOfDPObject.length;
    int i = 0;
    while (true)
    {
      if (i >= j)
        break label55;
      DPObject localDPObject = arrayOfDPObject[i];
      paramDPObject = localDPObject;
      if (localDPObject.getInt("ProductLevel") == 2)
        break;
      i += 1;
    }
    label55: return null;
  }

  public static boolean isDPObjectSavingCard(DPObject paramDPObject)
  {
    return (paramDPObject != null) && (paramDPObject.isClass("Product")) && (paramDPObject.getInt("ProductType") == 8);
  }

  public static boolean isDPObjectTimesCard(DPObject paramDPObject)
  {
    return (paramDPObject != null) && (paramDPObject.isClass("Product")) && (paramDPObject.getInt("ProductType") == 9);
  }

  public static boolean isScoreCard(DPObject paramDPObject)
  {
    if ((paramDPObject == null) || (!paramDPObject.isClass("Card")))
      throw new IllegalArgumentException("card object can not be null");
    paramDPObject = paramDPObject.getString("ProductTypeList");
    if (TextUtils.isEmpty(paramDPObject));
    while (true)
    {
      return false;
      paramDPObject = paramDPObject.split(",");
      int j = paramDPObject.length;
      int i = 0;
      while (i < j)
      {
        String str = paramDPObject[i];
        if ((str.matches("[0-9]*")) && (ProductType.SCORE.value() == Integer.valueOf(str).intValue()))
          return true;
        i += 1;
      }
    }
  }

  public static boolean isThirdPartyCard(DPObject paramDPObject)
  {
    return paramDPObject.getInt("ThirdPartyType") > 0;
  }

  public DPObject getCardObject()
  {
    return this.cardObject;
  }

  public int getCardlevel()
  {
    return this.cardlevel;
  }

  public DPObject[] getGeneralCardList()
  {
    Object localObject2 = null;
    if (isOnlyGeneralCard())
      localObject1 = this.cardObject.getArray("ProductList");
    ArrayList localArrayList;
    do
    {
      do
      {
        return localObject1;
        localObject1 = localObject2;
      }
      while (isOnlyVipCardType());
      localArrayList = new ArrayList();
      localObject1 = localObject2;
    }
    while (this.cardObject.getArray("ProductList") == null);
    Object localObject1 = this.cardObject.getArray("ProductList");
    int j = localObject1.length;
    int i = 0;
    while (i < j)
    {
      localObject2 = localObject1[i];
      if (localObject2.getInt("ProductLevel") == 1)
        localArrayList.add(localObject2);
      i += 1;
    }
    localObject1 = new DPObject[localArrayList.size()];
    localArrayList.toArray(localObject1);
    return (DPObject)localObject1;
  }

  public int getPower()
  {
    return this.cardObject.getInt("Power");
  }

  public String getSubTitle()
  {
    return this.cardObject.getString("SubTitle");
  }

  public DPObject getVIPCard()
  {
    if ((this.cardlevel == 2) || (this.cardlevel == 3))
      return getVIPCard(this.cardObject);
    return null;
  }

  public boolean isOnlyGeneralCard()
  {
    return this.cardlevel == 1;
  }

  public boolean isOnlyVipCardType()
  {
    return this.cardlevel == 2;
  }

  public void setCardObject(DPObject paramDPObject)
  {
    this.cardObject = paramDPObject;
  }

  public void setCardlevel(int paramInt)
  {
    this.cardlevel = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.MemberCard
 * JD-Core Version:    0.6.0
 */