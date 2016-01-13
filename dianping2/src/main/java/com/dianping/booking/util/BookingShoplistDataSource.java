package com.dianping.booking.util;

import android.net.Uri;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.DataSource.DataLoader;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.json.JSONObject;

public class BookingShoplistDataSource extends BaseShopListDataSource
  implements DataSource.DataLoader
{
  private static final DPObject BOOKING_DEFAULT_CATEGORY;
  public static final String BOOKING_DEFAULT_CATEGORY_TITLE = "全部美食";
  private static final DPObject BOOKING_DEFAULT_REGION = new DPObject("Region").edit().putInt("ID", 0).putString("Name", "全部商区").putInt("ParentID", -10000).generate();
  public static final String BOOKING_DEFAULT_REGION_TITLE = "全部商区";
  private static final DPObject BOOKING_DEFAULT_SORT;
  public static final String BOOKING_DEFAULT_SORT_TITLE = "智能排序";
  private NovaActivity activity;
  public int bookingPerson;
  public long bookingTime;
  public int cityId;
  public String curMaxPrice;
  public String curMinPrice;
  public DPObject[] curSwitches;
  public DPObject curTag;
  public String curTagId = "";
  public DPObject[] curTags;
  private DataLoadListener dataLoadListener;
  public DataStatus dataStatus = DataStatus.NORMAL;
  private String emptyJSONStr = new JSONObject().toString();
  public boolean firstLoad = true;
  public boolean isRange;
  public boolean isZeus = true;
  public String keyword = "";
  public String lat;
  public String lng;
  public int orderSource;
  private RequestHandler<MApiRequest, MApiResponse> requestHandler = new BookingShoplistDataSource.1(this);
  private MApiRequest shopRequest;
  public String zeusShopList = new JSONObject().toString();

  static
  {
    BOOKING_DEFAULT_CATEGORY = new DPObject("Category").edit().putInt("ID", 10).putString("Name", "全部美食").putInt("ParentID", 10).generate();
    BOOKING_DEFAULT_SORT = new DPObject("Pair").edit().putString("Name", "智能排序").generate();
  }

  public BookingShoplistDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
    setCurRegion(BOOKING_DEFAULT_REGION);
    setCurCategory(BOOKING_DEFAULT_CATEGORY);
    setCurSort(BOOKING_DEFAULT_SORT);
    setNeedLocalRegion(true);
    setDataLoader(this);
  }

  private MApiRequest createRequest(int paramInt)
  {
    StringBuilder localStringBuilder1 = new StringBuilder("http://rs.api.dianping.com/");
    Object localObject;
    label171: label181: label209: label243: StringBuilder localStringBuilder2;
    label275: label291: String str;
    if (this.isZeus)
    {
      localStringBuilder1.append("searchbookableshopjson.yy");
      localStringBuilder1.append("?");
      localStringBuilder1.append("cityid=").append(this.activity.cityId());
      if ((!TextUtils.isEmpty(this.lat)) || (!TextUtils.isEmpty(this.lng)) || (this.cityId != 0))
        break label506;
      localObject = this.activity.location();
      if (localObject != null)
      {
        localDecimalFormat = Location.FMT;
        this.lat = localDecimalFormat.format(((Location)localObject).latitude());
        this.lng = localDecimalFormat.format(((Location)localObject).longitude());
        this.cityId = ((Location)localObject).city().id();
        localStringBuilder1.append("&mylat=").append(this.lat);
        localStringBuilder1.append("&mylng=").append(this.lng);
        localStringBuilder1.append("&mycityid=").append(this.cityId);
      }
      if (curCategory() != null)
        break label551;
      i = 0;
      if (i > 0)
        localStringBuilder1.append("&categoryid=").append(i);
      if (curRegion() != null)
        break label565;
      i = 0;
      if ((i > 0) && (!this.isRange))
        localStringBuilder1.append("&regionid=").append(i);
      if (curRange() != null)
        break label579;
      localObject = null;
      if ((localObject != null) && (this.isRange))
        localStringBuilder1.append("&range=").append((String)localObject);
      if (curSort() != null)
        break label592;
      localObject = null;
      if (localObject == null)
        break label605;
      localStringBuilder1.append("&sortid=").append((String)localObject);
      localStringBuilder1.append("&start=").append(paramInt);
      if ((this.bookingPerson > 0) && (this.bookingTime > 0L))
      {
        localObject = String.format("bookingperson=%s&bookingtime=%s", new Object[] { Integer.valueOf(this.bookingPerson), Long.valueOf(this.bookingTime) });
        localStringBuilder1.append("&").append((String)localObject);
      }
      localStringBuilder1.append("&").append(String.format("tagid=%s", new Object[] { this.curTagId }));
      if ((this.curSwitches == null) || (this.curSwitches.length == 0))
        break label640;
      localObject = this.curSwitches;
      int j = localObject.length;
      paramInt = 0;
      label416: if (paramInt >= j)
        break label640;
      DecimalFormat localDecimalFormat = localObject[paramInt];
      localStringBuilder2 = localStringBuilder1.append("&");
      str = localDecimalFormat.getString("ID");
      if (!localDecimalFormat.getBoolean("On"))
        break label634;
    }
    label551: label565: label579: label592: label605: label634: for (int i = 1; ; i = 0)
    {
      localStringBuilder2.append(String.format("%s=%d", new Object[] { str, Integer.valueOf(i) }));
      paramInt += 1;
      break label416;
      localStringBuilder1.append("searchbookableshop.yy");
      break;
      label506: localStringBuilder1.append("&mylat=").append(this.lat);
      localStringBuilder1.append("&mylng=").append(this.lng);
      localStringBuilder1.append("&mycityid=").append(this.cityId);
      break label171;
      i = curCategory().getInt("ID");
      break label181;
      i = curRegion().getInt("ID");
      break label209;
      localObject = curRange().getString("ID");
      break label243;
      localObject = curSort().getString("ID");
      break label275;
      if (this.cityId == this.activity.cityId())
        break label291;
      localStringBuilder1.append("&sortid=").append(0);
      break label291;
    }
    label640: if (!TextUtils.isEmpty(this.keyword))
      localStringBuilder1.append("&keyword=").append(Uri.encode(this.keyword));
    if (!TextUtils.isEmpty(this.curMinPrice))
      localStringBuilder1.append("&").append(String.format("minPrice=%s", new Object[] { this.curMinPrice }));
    if (!TextUtils.isEmpty(this.curMaxPrice))
      localStringBuilder1.append("&").append(String.format("maxPrice=%s", new Object[] { this.curMaxPrice }));
    return (MApiRequest)BasicMApiRequest.mapiGet(localStringBuilder1.toString(), CacheType.NORMAL);
  }

  public void loadData(int paramInt, boolean paramBoolean)
  {
    if (this.shopRequest != null)
      this.activity.mapiService().abort(this.shopRequest, this.requestHandler, true);
    if (paramInt == 0)
      this.firstLoad = true;
    this.shopRequest = createRequest(paramInt);
    if (paramBoolean)
      this.activity.mapiCacheService().remove(this.shopRequest);
    this.activity.mapiService().exec(this.shopRequest, this.requestHandler);
    changeStatus(1);
  }

  public void setDataLoadListener(DataLoadListener paramDataLoadListener)
  {
    this.dataLoadListener = paramDataLoadListener;
  }

  public void setLoadingDataStatus()
  {
    changeStatus(1);
  }

  public static abstract interface DataLoadListener
  {
    public abstract void loadShopListFinsh(BookingShoplistDataSource.DataStatus paramDataStatus, Object paramObject);
  }

  public static enum DataStatus
  {
    static
    {
      ERROR_NETWORK = new DataStatus("ERROR_NETWORK", 2);
      ERROR_NOSEARCH = new DataStatus("ERROR_NOSEARCH", 3);
      ERROR_LOCATION = new DataStatus("ERROR_LOCATION", 4);
      $VALUES = new DataStatus[] { INITIAL, NORMAL, ERROR_NETWORK, ERROR_NOSEARCH, ERROR_LOCATION };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.util.BookingShoplistDataSource
 * JD-Core Version:    0.6.0
 */