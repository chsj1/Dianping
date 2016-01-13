package com.dianping.hotel.shoplist.data.model;

import com.dianping.archive.DPObject;
import java.io.Serializable;
import java.util.ArrayList;

public class HotelUniRegionModel
  implements Serializable
{
  private String name;
  private int parentId;
  private DPObject rawData;
  private String searchParams;
  private ArrayList<HotelUniRegionModel> subNaviItemList = new ArrayList();
  private String tag;
  private int type;
  private int uid;

  public static HotelUniRegionModel parse(DPObject paramDPObject)
  {
    Object localObject;
    if (paramDPObject == null)
      localObject = null;
    HotelUniRegionModel localHotelUniRegionModel;
    do
    {
      return localObject;
      localHotelUniRegionModel = new HotelUniRegionModel();
      localHotelUniRegionModel.rawData = paramDPObject;
      localObject = localHotelUniRegionModel;
    }
    while (paramDPObject.getArray("NaviItemList") == null);
    paramDPObject = paramDPObject.getArray("NaviItemList");
    int j = paramDPObject.length;
    int i = 0;
    while (true)
    {
      localObject = localHotelUniRegionModel;
      if (i >= j)
        break;
      localObject = paramDPObject[i];
      localHotelUniRegionModel.subNaviItemList.add(parseSub(localObject));
      i += 1;
    }
  }

  private static HotelUniRegionModel parseSub(DPObject paramDPObject)
  {
    HotelUniRegionModel localHotelUniRegionModel = new HotelUniRegionModel();
    localHotelUniRegionModel.rawData = paramDPObject;
    localHotelUniRegionModel.uid = paramDPObject.getInt("Id");
    localHotelUniRegionModel.parentId = paramDPObject.getInt("ParentId");
    localHotelUniRegionModel.type = paramDPObject.getInt("Type");
    localHotelUniRegionModel.tag = paramDPObject.getString("Tag");
    localHotelUniRegionModel.name = paramDPObject.getString("Name");
    localHotelUniRegionModel.searchParams = paramDPObject.getString("SearchParams");
    if (paramDPObject.getArray("SubNaviItemList") != null)
    {
      paramDPObject = localHotelUniRegionModel.rawData.getArray("SubNaviItemList");
      int j = paramDPObject.length;
      int i = 0;
      while (i < j)
      {
        DPObject localDPObject = paramDPObject[i];
        localHotelUniRegionModel.subNaviItemList.add(parseSub(localDPObject));
        i += 1;
      }
    }
    return localHotelUniRegionModel;
  }

  public String getName()
  {
    return this.name;
  }

  public int getParentId()
  {
    return this.parentId;
  }

  public String getSearchParams()
  {
    return this.searchParams;
  }

  public ArrayList<HotelUniRegionModel> getSubNaviItemList()
  {
    return this.subNaviItemList;
  }

  public String getTag()
  {
    return this.tag;
  }

  public int getType()
  {
    return this.type;
  }

  public int getUid()
  {
    return this.uid;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setTag(String paramString)
  {
    this.tag = paramString;
  }

  public void setType(int paramInt)
  {
    this.type = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.data.model.HotelUniRegionModel
 * JD-Core Version:    0.6.0
 */