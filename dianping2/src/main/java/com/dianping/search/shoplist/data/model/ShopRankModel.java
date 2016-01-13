package com.dianping.search.shoplist.data.model;

import com.dianping.archive.DPObject;

public class ShopRankModel
{
  public String desc;
  public DisplayContent[] displayContents;
  public String heat;
  public String id;
  public int index;
  public String keyword;
  public String labelIconUrl;
  public String logoUrl;
  public String queryid;
  public String rankInfo;
  public String title;
  public String titleUrl;

  public static ShopRankModel fromDPObject(DPObject paramDPObject, int paramInt, String paramString1, String paramString2)
  {
    ShopRankModel localShopRankModel = new ShopRankModel();
    localShopRankModel.id = paramDPObject.getString("ID");
    localShopRankModel.logoUrl = paramDPObject.getString("LogoUrl");
    localShopRankModel.titleUrl = paramDPObject.getString("TitleUrl");
    localShopRankModel.desc = paramDPObject.getString("Desc");
    localShopRankModel.title = paramDPObject.getString("Title");
    localShopRankModel.heat = paramDPObject.getString("Heat");
    localShopRankModel.rankInfo = paramDPObject.getString("RankInfo");
    localShopRankModel.labelIconUrl = paramDPObject.getString("LabelIconUrl");
    paramDPObject = paramDPObject.getArray("DisplayContents");
    if (paramDPObject != null)
    {
      localShopRankModel.displayContents = new DisplayContent[paramDPObject.length];
      int i = 0;
      while (i < paramDPObject.length)
      {
        localShopRankModel.displayContents[i] = DisplayContent.fromDPObject(paramDPObject[i]);
        i += 1;
      }
    }
    localShopRankModel.displayContents = new DisplayContent[0];
    localShopRankModel.index = paramInt;
    localShopRankModel.keyword = paramString1;
    localShopRankModel.queryid = paramString2;
    return localShopRankModel;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.data.model.ShopRankModel
 * JD-Core Version:    0.6.0
 */