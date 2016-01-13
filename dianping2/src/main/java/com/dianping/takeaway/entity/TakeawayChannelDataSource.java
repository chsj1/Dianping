package com.dianping.takeaway.entity;

import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.model.City;
import com.dianping.model.Location;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TakeawayChannelDataSource extends TakeawaySampleShoplistDataSource
{
  public TakeawayChannelDataSource(NovaActivity paramNovaActivity)
  {
    super(paramNovaActivity);
  }

  protected MApiRequest createShopRequest(int paramInt)
  {
    Object localObject = null;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    Location localLocation = this.activity.location();
    int i;
    if ((!TextUtils.isEmpty(this.lat)) && (!TextUtils.isEmpty(this.lng)))
    {
      localArrayList.add("lat");
      localArrayList.add(this.lat);
      localArrayList.add("lng");
      localArrayList.add(this.lng);
      if (localLocation != null)
      {
        localArrayList.add("locatecityid");
        localArrayList.add(String.valueOf(localLocation.city().id()));
      }
      if (!TextUtils.isEmpty(this.curAddress))
      {
        localArrayList.add("address");
        localArrayList.add(this.curAddress);
      }
      if (curCategory() != null)
        break label336;
      i = 0;
      label157: if (i != 0)
      {
        localArrayList.add("categoryid");
        localArrayList.add(String.valueOf(i));
      }
      if (curSort() != null)
        break label350;
      localObject = null;
      label188: if (localObject != null)
      {
        localArrayList.add("sortid");
        localArrayList.add(localObject);
      }
      if (curMultiFilterIds() != null)
        break label363;
    }
    label336: label350: label363: for (localObject = ""; ; localObject = curMultiFilterIds())
    {
      localArrayList.add("multifilterids");
      localArrayList.add(localObject);
      localArrayList.add("start");
      localArrayList.add(String.valueOf(paramInt));
      localArrayList.add("geotype");
      localArrayList.add(String.valueOf(this.geoType));
      localObject = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/nearbyshoplist.ta?", (String[])localArrayList.toArray(new String[0]));
      do
        return localObject;
      while (localLocation == null);
      localObject = Location.FMT;
      localArrayList.add("lat");
      localArrayList.add(((NumberFormat)localObject).format(localLocation.latitude()));
      localArrayList.add("lng");
      localArrayList.add(((NumberFormat)localObject).format(localLocation.longitude()));
      break;
      i = curCategory().getInt("ID");
      break label157;
      localObject = curSort().getString("ID");
      break label188;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayChannelDataSource
 * JD-Core Version:    0.6.0
 */