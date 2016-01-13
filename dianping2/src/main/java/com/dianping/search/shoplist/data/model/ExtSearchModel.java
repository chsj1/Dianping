package com.dianping.search.shoplist.data.model;

import com.dianping.archive.DPObject;

public class ExtSearchModel
{
  public static final int DISPLAY_TYPE_BIG_IMG = 0;
  public static final int DISPLAY_TYPE_BLANK = 4;
  public static final int DISPLAY_TYPE_RANK = 5;
  public static final int DISPLAY_TYPE_TEXT_LINK = 2;
  public static final int DISPLAY_TYPE_THREE_BTN = 3;
  public static final int DISPLAY_TYPE_THREE_IMG = 1;
  public static final int DISPLAY_TYPE_XIAOQU = 6;
  public String desc;
  public DisplayContent[] displayContents;
  public int displayType;
  public String distanceText;
  public String feedback;
  public String heat;
  public String id;
  public int index;
  public String keyword;
  public int keywordType;
  public String labelIconUrl;
  public String logoUrl;
  public String priceText;
  public String queryid;
  public String rankInfo;
  public int star;
  public String tagInfo;
  public String title;
  public String titleUrl;
  public String topInfo;
  public String type;

  public static ExtSearchModel fromDPObject(DPObject paramDPObject, int paramInt, String paramString1, String paramString2)
  {
    ExtSearchModel localExtSearchModel = new ExtSearchModel();
    localExtSearchModel.tagInfo = paramDPObject.getString("TagInfo");
    localExtSearchModel.topInfo = paramDPObject.getString("TopInfo");
    localExtSearchModel.titleUrl = paramDPObject.getString("TitleUrl");
    localExtSearchModel.desc = paramDPObject.getString("Desc");
    localExtSearchModel.logoUrl = paramDPObject.getString("LogoUrl");
    localExtSearchModel.type = paramDPObject.getString("Type");
    localExtSearchModel.title = paramDPObject.getString("Title");
    localExtSearchModel.id = paramDPObject.getString("ID");
    localExtSearchModel.keywordType = paramDPObject.getInt("KeywordType");
    localExtSearchModel.displayType = paramDPObject.getInt("DisplayType");
    localExtSearchModel.heat = paramDPObject.getString("Heat");
    localExtSearchModel.rankInfo = paramDPObject.getString("RankInfo");
    localExtSearchModel.labelIconUrl = paramDPObject.getString("LabelIconUrl");
    localExtSearchModel.star = paramDPObject.getInt("Star");
    localExtSearchModel.priceText = paramDPObject.getString("PriceText");
    localExtSearchModel.distanceText = paramDPObject.getString("DistanceText");
    localExtSearchModel.feedback = paramDPObject.getString("Feedback");
    paramDPObject = paramDPObject.getArray("DisplayContents");
    if (paramDPObject != null)
    {
      localExtSearchModel.displayContents = new DisplayContent[paramDPObject.length];
      int i = 0;
      while (i < paramDPObject.length)
      {
        localExtSearchModel.displayContents[i] = DisplayContent.fromDPObject(paramDPObject[i]);
        i += 1;
      }
    }
    localExtSearchModel.displayContents = new DisplayContent[0];
    localExtSearchModel.index = paramInt;
    localExtSearchModel.keyword = paramString1;
    localExtSearchModel.queryid = paramString2;
    return localExtSearchModel;
  }

  public static ExtSearchModel getMorePlaceHolder(String paramString)
  {
    ExtSearchModel localExtSearchModel = new ExtSearchModel();
    localExtSearchModel.id = "moreExtSearch";
    localExtSearchModel.type = paramString;
    return localExtSearchModel;
  }

  public boolean isMorePlaceHolder()
  {
    return "moreExtSearch".equals(this.id);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.data.model.ExtSearchModel
 * JD-Core Version:    0.6.0
 */