package com.dianping.hotel.shoplist.data.model;

import com.dianping.archive.DPObject;
import java.util.ArrayList;

public abstract interface HotelShopListModel
{
  public abstract boolean hasSearchDate();

  public abstract long hotelCheckinTime();

  public abstract long hotelCheckoutTime();

  public abstract ArrayList<DPObject> hotelTuanList();

  public abstract boolean isFromHome();

  public abstract int placeType();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.data.model.HotelShopListModel
 * JD-Core Version:    0.6.0
 */