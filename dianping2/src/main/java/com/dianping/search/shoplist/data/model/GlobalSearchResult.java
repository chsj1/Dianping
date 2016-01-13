package com.dianping.search.shoplist.data.model;

import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.data.model.ShopDataModel;

public class GlobalSearchResult
{
  public String naviTitle;
  public String naviUrl;
  public ShopDataModel[] shopModels;
  public String title;

  public GlobalSearchResult(DPObject paramDPObject)
  {
    DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
    this.shopModels = new ShopDataModel[arrayOfDPObject.length];
    int i = 0;
    while (i < arrayOfDPObject.length)
    {
      this.shopModels[i] = new ShopDataModel(arrayOfDPObject[i]);
      this.shopModels[i].isGlobal = true;
      this.shopModels[i].index = i;
      i += 1;
    }
    this.naviTitle = paramDPObject.getString("NaviTitle");
    this.naviUrl = paramDPObject.getString("NaviUrl");
    this.title = paramDPObject.getString("Title");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.data.model.GlobalSearchResult
 * JD-Core Version:    0.6.0
 */