package com.dianping.takeaway.entity;

import java.util.ArrayList;
import java.util.List;

public class TakeawayDishGroup
{
  public static final int AMONUT_UNLIMIT = -1;
  public final String categoryName;
  public int currentAmount = 0;
  public final List<TakeawayDishInfo> dishes = new ArrayList();
  public int maxAmount = -1;

  public TakeawayDishGroup(String paramString)
  {
    this.categoryName = paramString;
  }

  public boolean cancel()
  {
    if (this.currentAmount == 0)
      return false;
    this.currentAmount -= 1;
    return true;
  }

  public boolean order()
  {
    if (this.currentAmount == this.maxAmount)
      return false;
    this.currentAmount += 1;
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayDishGroup
 * JD-Core Version:    0.6.0
 */