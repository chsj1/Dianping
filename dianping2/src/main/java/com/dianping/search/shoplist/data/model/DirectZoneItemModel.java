package com.dianping.search.shoplist.data.model;

import com.dianping.archive.DPObject;

public class DirectZoneItemModel
{
  public static final int TYPE_EMPTY = 4;
  public static final int TYPE_LABEL = 1;
  public static final int TYPE_RIBBON = 5;
  public static final int TYPE_SOLID = 3;
  public static final int TYPE_STAR = 2;
  public static final int TYPE_TEXT = 0;
  public int align;
  public String iconUrl;
  public String text;
  public int type = 4;

  public static DirectZoneItemModel fromDPObject(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return null;
    DirectZoneItemModel localDirectZoneItemModel = new DirectZoneItemModel();
    localDirectZoneItemModel.iconUrl = paramDPObject.getString("IconUrl");
    localDirectZoneItemModel.text = paramDPObject.getString("Text");
    localDirectZoneItemModel.type = paramDPObject.getInt("Type");
    localDirectZoneItemModel.align = paramDPObject.getInt("Align");
    return localDirectZoneItemModel;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.data.model.DirectZoneItemModel
 * JD-Core Version:    0.6.0
 */