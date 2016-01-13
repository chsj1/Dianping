package com.dianping.takeaway.entity;

import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.widget.view.GAUserInfo;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TakeawayOverRangeDataSource extends TakeawaySampleShoplistDataSource
{
  public final int REQUEST_CODE_OVER_RANGE_CHANGE_ADDRESS = 5;
  public String overRangeAddress;
  public String overRangeErrorMsg;
  public int overRangeShopCateId;
  public String overRangeShopID;
  public String overRangeShopName;

  public TakeawayOverRangeDataSource(NovaActivity paramNovaActivity)
  {
    super(paramNovaActivity);
  }

  protected MApiRequest createShopRequest(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(this.activity.cityId()));
    Location localLocation = this.activity.location();
    if ((!TextUtils.isEmpty(this.lat)) && (!TextUtils.isEmpty(this.lng)))
    {
      localArrayList.add("lat");
      localArrayList.add(this.lat);
      localArrayList.add("lng");
      localArrayList.add(this.lng);
    }
    while (true)
    {
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
      localArrayList.add("start");
      localArrayList.add(String.valueOf(paramInt));
      localArrayList.add("geotype");
      localArrayList.add(String.valueOf(this.geoType));
      localArrayList.add("categoryid");
      localArrayList.add(String.valueOf(this.overRangeShopCateId));
      return BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/nearbyshoplist.ta?", (String[])localArrayList.toArray(new String[0]));
      if (localLocation == null)
        break;
      DecimalFormat localDecimalFormat = Location.FMT;
      localArrayList.add("lat");
      localArrayList.add(localDecimalFormat.format(localLocation.latitude()));
      localArrayList.add("lng");
      localArrayList.add(localDecimalFormat.format(localLocation.longitude()));
    }
    return null;
  }

  public GAUserInfo getGAUserInfo()
  {
    GAUserInfo localGAUserInfo = new GAUserInfo();
    try
    {
      localGAUserInfo.shop_id = Integer.valueOf(Integer.parseInt(this.overRangeShopID));
      return localGAUserInfo;
    }
    catch (Exception localException)
    {
      localGAUserInfo.shop_id = Integer.valueOf(0);
    }
    return localGAUserInfo;
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.overRangeShopName = paramBundle.getString("overrangeshopname");
    this.overRangeShopID = paramBundle.getString("overrangeshopid");
    this.overRangeShopCateId = paramBundle.getInt("overrangecateid");
    this.overRangeErrorMsg = paramBundle.getString("overrangeerrormsg");
    this.overRangeAddress = paramBundle.getString("overrangeaddress");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("overrangeshopname", this.overRangeShopName);
    paramBundle.putString("overrangeshopid", this.overRangeShopID);
    paramBundle.putInt("overrangecateid", this.overRangeShopCateId);
    paramBundle.putString("overrangeerrormsg", this.overRangeErrorMsg);
    paramBundle.putString("overrangeaddress", this.overRangeAddress);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayOverRangeDataSource
 * JD-Core Version:    0.6.0
 */