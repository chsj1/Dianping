package com.dianping.search.shoplist.data.model;

import com.dianping.archive.DPObject;

public class DisplayContent
{
  public int count;
  public String countText;
  public String iconUrl;
  public String picUrl;
  public String title;
  public String titleUrl;

  public static DisplayContent fromDPObject(DPObject paramDPObject)
  {
    DisplayContent localDisplayContent = new DisplayContent();
    localDisplayContent.title = paramDPObject.getString("Title");
    localDisplayContent.picUrl = paramDPObject.getString("PicUrl");
    localDisplayContent.titleUrl = paramDPObject.getString("TitleUrl");
    localDisplayContent.iconUrl = paramDPObject.getString("IconUrl");
    localDisplayContent.count = paramDPObject.getInt("Count");
    localDisplayContent.countText = paramDPObject.getString("CountText");
    return localDisplayContent;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.data.model.DisplayContent
 * JD-Core Version:    0.6.0
 */