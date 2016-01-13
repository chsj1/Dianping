package com.dianping.selectdish.model;

import com.dianping.archive.DPObject;

public class SuggestDishInfo
{
  public boolean checked;
  public String desc;
  public int dishId;
  public String name;
  public String picUrl;

  public SuggestDishInfo(DPObject paramDPObject)
  {
    this.desc = paramDPObject.getString("Desc");
    this.picUrl = paramDPObject.getString("PicUrl");
    this.name = paramDPObject.getString("Name");
    this.dishId = paramDPObject.getInt("DishId");
    this.checked = false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.model.SuggestDishInfo
 * JD-Core Version:    0.6.0
 */