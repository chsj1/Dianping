package com.dianping.selectdish.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class CartItem
  implements Serializable
{
  private int count;
  public final DishInfo dishInfo;

  public CartItem(DishInfo paramDishInfo)
  {
    this.dishInfo = paramDishInfo;
  }

  public int addItem(int paramInt)
  {
    this.count += paramInt;
    return this.count;
  }

  public int getItemCount()
  {
    return this.count;
  }

  public int reduceItem(int paramInt)
  {
    this.count -= paramInt;
    return this.count;
  }

  public void setItemCount(int paramInt)
  {
    this.count = paramInt;
  }

  public boolean updateBasicInfo(CartItem paramCartItem)
  {
    int i = 0;
    if (paramCartItem.dishInfo.status != this.dishInfo.status)
    {
      this.dishInfo.status = paramCartItem.dishInfo.status;
      i = 1;
    }
    int j = i;
    if (paramCartItem.dishInfo.name != null)
    {
      j = i;
      if (!paramCartItem.dishInfo.name.equals(this.dishInfo.name))
      {
        this.dishInfo.name = paramCartItem.dishInfo.name;
        j = 1;
      }
    }
    if (paramCartItem.dishInfo.oldPrice != this.dishInfo.oldPrice)
    {
      this.dishInfo.oldPrice = paramCartItem.dishInfo.oldPrice;
      j = 1;
    }
    if (paramCartItem.dishInfo.currentPrice != this.dishInfo.currentPrice)
    {
      this.dishInfo.currentPrice = paramCartItem.dishInfo.currentPrice;
      j = 1;
    }
    if (paramCartItem.dishInfo.setItems.size() != this.dishInfo.setItems.size())
    {
      i = 1;
      this.dishInfo.setItems.clear();
      this.dishInfo.setItems.addAll(paramCartItem.dishInfo.setItems);
    }
    while (true)
    {
      if (paramCartItem.dishInfo.saleTime != this.dishInfo.saleTime)
      {
        this.dishInfo.saleTime = paramCartItem.dishInfo.saleTime;
        i = 1;
      }
      return i;
      Iterator localIterator = paramCartItem.dishInfo.setItems.iterator();
      SetItem localSetItem;
      do
      {
        i = j;
        if (!localIterator.hasNext())
          break;
        localSetItem = (SetItem)localIterator.next();
      }
      while (this.dishInfo.setItems.contains(localSetItem));
      i = 1;
      this.dishInfo.setItems.clear();
      this.dishInfo.setItems.addAll(paramCartItem.dishInfo.setItems);
    }
  }

  public boolean updateEventInfo(CartItem paramCartItem)
  {
    int i = 0;
    if (paramCartItem.dishInfo.freeRuleType != this.dishInfo.freeRuleType)
    {
      this.dishInfo.freeRuleType = paramCartItem.dishInfo.freeRuleType;
      i = 1;
    }
    if (paramCartItem.dishInfo.bought != this.dishInfo.bought)
    {
      this.dishInfo.bought = paramCartItem.dishInfo.bought;
      i = 1;
    }
    if (paramCartItem.dishInfo.targetCount != this.dishInfo.targetCount)
    {
      this.dishInfo.targetCount = paramCartItem.dishInfo.targetCount;
      i = 1;
    }
    int j = i;
    if (paramCartItem.dishInfo.freeCount != this.dishInfo.freeCount)
    {
      this.dishInfo.freeCount = paramCartItem.dishInfo.freeCount;
      j = 1;
    }
    i = j;
    if (paramCartItem.dishInfo.freeItem != null)
    {
      i = j;
      if (!paramCartItem.dishInfo.freeItem.equals(this.dishInfo.freeItem))
      {
        this.dishInfo.freeItem = paramCartItem.dishInfo.freeItem;
        i = 1;
      }
    }
    j = i;
    if (paramCartItem.dishInfo.eventDesc != null)
    {
      j = i;
      if (paramCartItem.dishInfo.eventDesc.equals(this.dishInfo.eventDesc))
      {
        this.dishInfo.eventDesc = paramCartItem.dishInfo.eventDesc;
        j = 1;
      }
    }
    return j;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.model.CartItem
 * JD-Core Version:    0.6.0
 */