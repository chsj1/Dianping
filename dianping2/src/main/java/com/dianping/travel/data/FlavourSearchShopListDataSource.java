package com.dianping.travel.data;

import android.content.Context;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.shoplist.data.DataSource.DataLoader;
import com.dianping.base.shoplist.data.DefaultSearchShopListDataSource;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import java.util.ArrayList;
import java.util.Arrays;

public class FlavourSearchShopListDataSource extends DefaultSearchShopListDataSource
  implements LocationListener
{
  private Context context;
  private String tips;

  public FlavourSearchShopListDataSource(DataSource.DataLoader paramDataLoader)
  {
    super(paramDataLoader);
    this.context = ((Context)paramDataLoader);
  }

  private void addLocalRegion()
  {
    monitorenter;
    try
    {
      int i;
      if (this.filterRanges != null)
      {
        i = this.filterRanges.length;
        if (i != 0)
          break label24;
      }
      while (true)
      {
        return;
        label24: ArrayList localArrayList = new ArrayList();
        Object localObject2 = new DPObject("Region").edit().putInt("ID", -1).putString("Name", "附近").putInt("ParentID", 0).generate();
        DPObject[] arrayOfDPObject;
        if (this.filterRegions != null)
        {
          arrayOfDPObject = this.filterRegions;
          j = arrayOfDPObject.length;
          i = 0;
          while (true)
          {
            if (i >= j)
              break label128;
            if (arrayOfDPObject[i].getInt("ID") == ((DPObject)localObject2).getInt("ID"))
              break;
            i += 1;
          }
        }
        label128: localArrayList.add(localObject2);
        localObject2 = this.filterRanges;
        int j = localObject2.length;
        i = 0;
        while (i < j)
        {
          arrayOfDPObject = localObject2[i];
          localArrayList.add(new DPObject("Region").edit().putInt("ID", Integer.valueOf(arrayOfDPObject.getString("ID")).intValue()).putString("Name", arrayOfDPObject.getString("Name")).putInt("ParentID", -1).generate());
          i += 1;
        }
        if (this.filterRegions != null)
          localArrayList.addAll(Arrays.asList(this.filterRegions));
        this.filterRegions = ((DPObject[])(DPObject[])localArrayList.toArray(new DPObject[0]));
        publishDataChange(20);
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject1;
  }

  private boolean shouldShowLocal()
  {
    LocationService localLocationService = (LocationService)((DPActivity)this.context).getService("location");
    return (localLocationService.hasLocation()) && (localLocationService.location().getObject("City").getInt("ID") == ((DPActivity)this.context).city().id());
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    if (shouldShowLocal())
      addLocalRegion();
  }

  public String tips()
  {
    return this.tips;
  }

  protected void updateNavs(DPObject paramDPObject)
  {
    super.updateNavs(paramDPObject);
    this.tips = paramDPObject.getString("Tips");
    if (shouldShowLocal())
      addLocalRegion();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.data.FlavourSearchShopListDataSource
 * JD-Core Version:    0.6.0
 */