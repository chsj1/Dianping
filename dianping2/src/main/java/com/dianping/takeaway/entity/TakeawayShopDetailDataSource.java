package com.dianping.takeaway.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.widget.view.GAUserInfo;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TakeawayShopDetailDataSource
{
  public DPObject[] activityList;
  public String address;
  public String announceText;
  public String arrivedInterval;
  public boolean basicInfoLoadSuccess = false;
  private DataLoadListener dataLoadListener;
  public String defaultPicUrl;
  public int dpReviewCount;
  public DPObject[] extraServices;
  private NovaFragment fragment;
  public boolean isDeliveryFree = false;
  public String lat;
  public DPObject[] legalImgs;
  public String legalInfo;
  public String lng;
  protected RequestHandler<MApiRequest, MApiResponse> mapiHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == TakeawayShopDetailDataSource.this.shopDetailRequest)
      {
        TakeawayShopDetailDataSource.access$002(TakeawayShopDetailDataSource.this, null);
        TakeawayShopDetailDataSource.this.basicInfoLoadSuccess = false;
        if (TakeawayShopDetailDataSource.this.dataLoadListener != null)
          TakeawayShopDetailDataSource.this.dataLoadListener.loadBasicDataFinsh(TakeawayNetLoadStatus.STATUS_FAILED, null);
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == TakeawayShopDetailDataSource.this.shopDetailRequest)
      {
        TakeawayShopDetailDataSource.access$002(TakeawayShopDetailDataSource.this, null);
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          TakeawayShopDetailDataSource.this.defaultPicUrl = paramMApiRequest.getString("DefaultPic");
          TakeawayShopDetailDataSource.this.picCount = paramMApiRequest.getInt("PicCount");
          TakeawayShopDetailDataSource.this.extraServices = paramMApiRequest.getArray("ExtraServices");
          TakeawayShopDetailDataSource.this.minAmount = paramMApiRequest.getString("DeliveryFee");
          TakeawayShopDetailDataSource.this.minFee = paramMApiRequest.getString("MinFeeText");
          paramMApiResponse = TakeawayShopDetailDataSource.this;
          if (paramMApiRequest.getInt("DeliveryFree") != 1)
            break label382;
        }
      }
      label382: for (boolean bool = true; ; bool = false)
      {
        paramMApiResponse.isDeliveryFree = bool;
        TakeawayShopDetailDataSource.this.arrivedInterval = paramMApiRequest.getString("Speed");
        TakeawayShopDetailDataSource.this.starScore = paramMApiRequest.getInt("ShopPower");
        TakeawayShopDetailDataSource.this.phoneNums = paramMApiRequest.getStringArray("Phones");
        TakeawayShopDetailDataSource.this.announceText = paramMApiRequest.getString("AnnounceText");
        TakeawayShopDetailDataSource.this.address = paramMApiRequest.getString("Address");
        TakeawayShopDetailDataSource.this.saleText = paramMApiRequest.getString("OrderCount");
        TakeawayShopDetailDataSource.this.shopLat = String.valueOf(paramMApiRequest.getDouble("Lat"));
        TakeawayShopDetailDataSource.this.shopLng = String.valueOf(paramMApiRequest.getDouble("Lng"));
        TakeawayShopDetailDataSource.this.tasteScore = paramMApiRequest.getString("Taste");
        TakeawayShopDetailDataSource.this.packageScore = paramMApiRequest.getString("PackageScore");
        TakeawayShopDetailDataSource.this.dpReviewCount = paramMApiRequest.getInt("DpReviewCount");
        TakeawayShopDetailDataSource.this.thirdPartyName = paramMApiRequest.getString("ThirdPartyName");
        TakeawayShopDetailDataSource.this.serveTimeList = paramMApiRequest.getStringArray("ServeTimes");
        TakeawayShopDetailDataSource.this.activityList = paramMApiRequest.getArray("Activities");
        TakeawayShopDetailDataSource.this.legalInfo = paramMApiRequest.getString("LegalInfo");
        TakeawayShopDetailDataSource.this.legalImgs = paramMApiRequest.getArray("TaLicencePics");
        TakeawayShopDetailDataSource.this.basicInfoLoadSuccess = true;
        if (TakeawayShopDetailDataSource.this.dataLoadListener != null)
          TakeawayShopDetailDataSource.this.dataLoadListener.loadBasicDataFinsh(TakeawayNetLoadStatus.STATUS_SUCCESS, null);
        return;
      }
    }
  };
  public String minAmount;
  public String minFee;
  public String packageScore;
  public String[] phoneNums;
  public int picCount;
  public String saleText;
  public String[] serveTimeList;
  private MApiRequest shopDetailRequest;
  public String shopId;
  public String shopLat;
  public String shopLng;
  public String shopName;
  public int starScore;
  public String tasteScore;
  public String thirdPartyName;

  public TakeawayShopDetailDataSource(NovaFragment paramNovaFragment)
  {
    this.fragment = paramNovaFragment;
  }

  public GAUserInfo getGAUserInfo()
  {
    GAUserInfo localGAUserInfo = new GAUserInfo();
    try
    {
      localGAUserInfo.shop_id = Integer.valueOf(Integer.parseInt(this.shopId));
      return localGAUserInfo;
    }
    catch (Exception localException)
    {
      localGAUserInfo.shop_id = Integer.valueOf(0);
    }
    return localGAUserInfo;
  }

  public void loadBasicData()
  {
    Uri.Builder localBuilder = Uri.parse("http://waimai.api.dianping.com/shopinfo.ta").buildUpon().appendQueryParameter("shopid", this.shopId);
    if ((!TextUtils.isEmpty(this.lat)) && (!TextUtils.isEmpty(this.lng)))
    {
      localBuilder.appendQueryParameter("lat", this.lat);
      localBuilder.appendQueryParameter("lng", this.lng);
    }
    while (true)
    {
      this.shopDetailRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      this.fragment.mapiService().exec(this.shopDetailRequest, this.mapiHandler);
      return;
      Location localLocation = this.fragment.location();
      if (localLocation == null)
        continue;
      DecimalFormat localDecimalFormat = Location.FMT;
      localBuilder.appendQueryParameter("lat", localDecimalFormat.format(localLocation.latitude()));
      localBuilder.appendQueryParameter("lng", localDecimalFormat.format(localLocation.longitude()));
      localBuilder.appendQueryParameter("locatecityid", String.valueOf(localLocation.city().id()));
    }
  }

  public void onDestroy()
  {
    if (this.shopDetailRequest != null)
    {
      this.fragment.mapiService().abort(this.shopDetailRequest, null, true);
      this.shopDetailRequest = null;
    }
  }

  public void setDataLoadListener(DataLoadListener paramDataLoadListener)
  {
    this.dataLoadListener = paramDataLoadListener;
  }

  public static abstract interface DataLoadListener
  {
    public abstract void loadBasicDataFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayShopDetailDataSource
 * JD-Core Version:    0.6.0
 */