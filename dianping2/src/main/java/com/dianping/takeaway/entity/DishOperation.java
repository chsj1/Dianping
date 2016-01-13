package com.dianping.takeaway.entity;

import com.dianping.base.widget.NumOperateButton;

public class DishOperation
{
  public final TakeawayDishMenuAdapter adapter;
  public final TakeawayDishInfo dishItem;
  public final NumOperateButton operateButton;

  public DishOperation(NumOperateButton paramNumOperateButton, TakeawayDishInfo paramTakeawayDishInfo, TakeawayDishMenuAdapter paramTakeawayDishMenuAdapter)
  {
    this.operateButton = paramNumOperateButton;
    this.dishItem = paramTakeawayDishInfo;
    this.adapter = paramTakeawayDishMenuAdapter;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.DishOperation
 * JD-Core Version:    0.6.0
 */