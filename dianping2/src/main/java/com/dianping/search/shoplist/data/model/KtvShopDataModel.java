package com.dianping.search.shoplist.data.model;

import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import java.util.ArrayList;
import java.util.List;

public class KtvShopDataModel extends ShopDataModel
{
  public List<DealModel> mDealModels = new ArrayList();

  public KtvShopDataModel(DPObject paramDPObject)
  {
    super(paramDPObject);
    if ((paramDPObject.getInt("ViewType") == 0) && (paramDPObject.getObject("Deals") != null) && (paramDPObject.getObject("Deals").getArray("List") != null) && (paramDPObject.getObject("Deals").getArray("List").length > 0))
    {
      DPObject[] arrayOfDPObject = paramDPObject.getObject("Deals").getArray("List");
      int j = arrayOfDPObject.length;
      int i = 0;
      while (i < j)
      {
        DealModel localDealModel = new DealModel(arrayOfDPObject[i], paramDPObject);
        this.mDealModels.add(localDealModel);
        i += 1;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.data.model.KtvShopDataModel
 * JD-Core Version:    0.6.0
 */