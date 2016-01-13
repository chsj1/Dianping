package com.dianping.selectdish.model;

import com.dianping.archive.DPObject;

public class GroupOnItem
{
  public int dishId;
  public String groupOnItemAmount;
  public String groupOnName;
  public DPObject[] groupOnSet;

  public GroupOnItem(DPObject paramDPObject)
  {
    this.groupOnItemAmount = paramDPObject.getString("DishItemAmount");
    this.groupOnName = paramDPObject.getString("DishName");
    this.dishId = paramDPObject.getInt("DishId");
    this.groupOnSet = paramDPObject.getArray("DishSet");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.model.GroupOnItem
 * JD-Core Version:    0.6.0
 */