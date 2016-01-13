package com.dianping.hui.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.util.network.NetworkUtils;
import com.dianping.util.network.WifiModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.json.JSONArray;

public class HuiPayDetailDataSource
  implements IHuiDataSource
{
  private static final long BOOKING_PAY_RESULT_RETRY_PERIOD = 5000L;
  private static final String BOOKING_PAY_RESULT_URL = "http://rs.api.dianping.com/querypaystatus.yy";
  private static final String HOBBIT_MENU_URL = "http://hui.api.dianping.com/queryhobbitordermenu.hui";
  private static final String HUI_GET_ONE_ORDER_URL = "http://hui.api.dianping.com/getonemopayorder.hui";
  private static final String HUI_PAY_RESULT_URL = "http://hui.api.dianping.com/querymopaystatus.bin";
  private static final int MSG_HUI_PAY_RESULT_COUNT_DOWN = 4;
  private static final int MSG_REQ_BOOKING_PAY_RESULT = 1;
  private static final int MSG_REQ_HUI_PAY_RESULT = 2;
  private static final String POI_UPLOAD_WIFI_URL = "http://mapi.dianping.com/poi/poiwifi/addwifi.bin";
  public static final String TAG = "TAG_Hui";
  private NovaActivity activity;
  private MApiRequest addFavoriteRequest;
  public int bizType;
  public DPObject[] bookingActivitys;
  private MApiRequest bookingPayResultReq;
  private int bookingPayResultRetryTimes = 5;
  public String bookingSerializedId;
  public String contactMerchantTip;
  public HuiPayDetailDataLoaderListener dataLoadListener;
  private Handler handler = new HuiPayDetailDataSource.2(this);
  public String hobbitMenu;
  private MApiRequest hobbitResultReq;
  private MApiRequest huiOneOrderReq;
  public String huiOrderId;
  public int huiPayResultCountDownSeconds = 60;
  private MApiRequest huiPayResultReq;
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new HuiPayDetailDataSource.1(this);
  public int orderBizType;
  public OrderDetail orderDetail;
  public String orderId;
  public DPObject orderShareInfo;
  private int orderType;
  public String payOrderId;
  public PayStatus payStatus;
  public PayType payType;
  private MApiRequest poiUploadWifiReq;
  private MApiRequest removeFavoriteRequest;
  public DPObject reviewInfo;
  public ReviewStatus reviewStatus = ReviewStatus.HIDE;
  public int shopId;
  public String statusMsg;
  public String thirdPartyOrderId;
  public int thirdPartyOrderType;
  public DPObject ticketInfo;
  public String token;

  public HuiPayDetailDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
    if (TextUtils.isEmpty(paramNovaActivity.accountService().token()));
    for (paramNovaActivity = ""; ; paramNovaActivity = paramNovaActivity.accountService().token())
    {
      this.token = paramNovaActivity;
      return;
    }
  }

  private String extractBonus(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      return "";
    StringBuilder localStringBuilder = new StringBuilder();
    int j = paramArrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      localStringBuilder.append(paramArrayOfDPObject[i].getString("DefaultPayTip"));
      localStringBuilder.append(",");
      i += 1;
    }
    if (localStringBuilder.length() > 0)
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    return localStringBuilder.toString();
  }

  public void releaseRequests()
  {
    if (this.huiOneOrderReq != null)
    {
      this.activity.mapiService().abort(this.huiOneOrderReq, this.mapiHandler, true);
      this.huiOneOrderReq = null;
    }
    if (this.huiPayResultReq != null)
    {
      this.activity.mapiService().abort(this.huiPayResultReq, this.mapiHandler, true);
      this.huiPayResultReq = null;
    }
    if (this.bookingPayResultReq != null)
    {
      this.activity.mapiService().abort(this.bookingPayResultReq, this.mapiHandler, true);
      this.bookingPayResultReq = null;
    }
    if (this.hobbitResultReq != null)
    {
      this.activity.mapiService().abort(this.hobbitResultReq, this.mapiHandler, true);
      this.hobbitResultReq = null;
    }
    if (this.poiUploadWifiReq != null)
    {
      this.activity.mapiService().abort(this.poiUploadWifiReq, this.mapiHandler, true);
      this.poiUploadWifiReq = null;
    }
    if (this.addFavoriteRequest != null)
    {
      this.activity.mapiService().abort(this.addFavoriteRequest, this.mapiHandler, true);
      this.addFavoriteRequest = null;
    }
    if (this.removeFavoriteRequest != null)
    {
      this.activity.mapiService().abort(this.removeFavoriteRequest, this.mapiHandler, true);
      this.removeFavoriteRequest = null;
    }
  }

  public void reqBookingPayResult()
  {
    if (this.bookingPayResultReq != null)
      this.activity.mapiService().abort(this.bookingPayResultReq, this.mapiHandler, true);
    ArrayList localArrayList = new ArrayList();
    if (this.activity.isLogined())
    {
      localArrayList.add("token");
      localArrayList.add(this.token);
    }
    localArrayList.add("clientUUID");
    localArrayList.add(Environment.uuid());
    localArrayList.add("type");
    localArrayList.add("10");
    localArrayList.add("payorderid");
    localArrayList.add(this.payOrderId);
    this.bookingPayResultReq = BasicMApiRequest.mapiPost("http://rs.api.dianping.com/querypaystatus.yy", (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.activity.mapiService().exec(this.bookingPayResultReq, this.mapiHandler);
  }

  public void reqGetOneOrder()
  {
    if (this.huiOneOrderReq != null)
      this.activity.mapiService().abort(this.huiOneOrderReq, this.mapiHandler, true);
    Uri.Builder localBuilder = Uri.parse("http://hui.api.dianping.com/getonemopayorder.hui").buildUpon();
    localBuilder.appendQueryParameter("orderid", this.huiOrderId);
    localBuilder.appendQueryParameter("biztype", String.valueOf(this.bizType));
    localBuilder.appendQueryParameter("uuid", Environment.uuid());
    if (this.activity.isLogined())
      localBuilder.appendQueryParameter("token", this.token);
    this.huiOneOrderReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    this.activity.mapiService().exec(this.huiOneOrderReq, this.mapiHandler);
  }

  public void reqHobbitResult()
  {
    if (this.hobbitResultReq != null)
      this.activity.mapiService().abort(this.hobbitResultReq, this.mapiHandler, true);
    Uri.Builder localBuilder = Uri.parse("http://hui.api.dianping.com/queryhobbitordermenu.hui").buildUpon();
    localBuilder.appendQueryParameter("orderid", String.valueOf(this.huiOrderId));
    this.hobbitResultReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    this.activity.mapiService().exec(this.hobbitResultReq, this.mapiHandler);
  }

  public void reqHuiPayResult()
  {
    if (this.huiPayResultReq != null)
      this.activity.mapiService().abort(this.huiPayResultReq, this.mapiHandler, true);
    Uri.Builder localBuilder = Uri.parse("http://hui.api.dianping.com/querymopaystatus.bin").buildUpon();
    if (this.activity.isLogined())
      localBuilder.appendQueryParameter("token", this.token);
    localBuilder.appendQueryParameter("uuid", Environment.uuid());
    localBuilder.appendQueryParameter("imei", Environment.imei());
    localBuilder.appendQueryParameter("orderid", this.huiOrderId);
    this.huiPayResultReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    this.activity.mapiService().exec(this.huiPayResultReq, this.mapiHandler);
  }

  public void reqUploadWifi()
  {
    if (this.poiUploadWifiReq != null)
      this.activity.mapiService().abort(this.poiUploadWifiReq, this.mapiHandler, true);
    DPObject localDPObject = this.activity.locationService().location();
    ArrayList localArrayList = new ArrayList();
    WifiModel localWifiModel = NetworkUtils.curWifi();
    localArrayList.add("ssid");
    localArrayList.add(TextUtils.stripHeadAndTailQuotations(localWifiModel.getSsid()));
    localArrayList.add("mac");
    String str = localWifiModel.getBssid();
    Object localObject = str;
    if (str == null)
      localObject = "";
    localArrayList.add(localObject);
    if (localDPObject != null)
    {
      localArrayList.add("lat");
      localArrayList.add(Location.FMT.format(localDPObject.getDouble("Lat")));
      localArrayList.add("lng");
      localArrayList.add(Location.FMT.format(localDPObject.getDouble("Lng")));
      localArrayList.add("srctype");
      localArrayList.add("2");
      localArrayList.add("shopid");
      localArrayList.add(Integer.toString(this.shopId));
      localArrayList.add("weight");
      localArrayList.add(Integer.toString(localWifiModel.getLevel()));
      localObject = NetworkUtils.wifiScanResultInfo2JsonArray();
      localArrayList.add("nearwifis");
      if (((JSONArray)localObject).length() != 0)
        break label373;
    }
    label373: for (localObject = ""; ; localObject = ((JSONArray)localObject).toString())
    {
      localArrayList.add(localObject);
      this.poiUploadWifiReq = BasicMApiRequest.mapiPost("http://mapi.dianping.com/poi/poiwifi/addwifi.bin", (String[])localArrayList.toArray(new String[localArrayList.size()]));
      this.activity.mapiService().exec(this.poiUploadWifiReq, this.mapiHandler);
      this.activity.statisticsEvent("hui7", "hui7_wifi_info_sent", "上传wifi信息", this.shopId);
      Log.d("TAG_Hui", "upload wifi info, params = " + localArrayList);
      return;
      localArrayList.add("lat");
      localArrayList.add("0");
      localArrayList.add("lng");
      localArrayList.add("0");
      break;
    }
  }

  public void restoreData(Bundle paramBundle)
  {
    this.orderId = paramBundle.getString("orderId");
    this.bizType = paramBundle.getInt("bizType");
    this.payOrderId = paramBundle.getString("payOrderId");
  }

  public void saveData(Bundle paramBundle)
  {
    paramBundle.putString("orderId", this.orderId);
    paramBundle.putInt("bizType", this.bizType);
    paramBundle.putString("payOrderId", this.payOrderId);
  }

  public static abstract interface HuiPayDetailDataLoaderListener
  {
    public abstract void countDown(int paramInt, boolean paramBoolean);

    public abstract void loadBookingPayResultFail(Object paramObject);

    public abstract void loadHobbit();

    public abstract void loadHobbitResultFail(Object paramObject);

    public abstract void loadHuiPayResultFail(Object paramObject);

    public abstract void loadOneOrder();

    public abstract void loadOneOrderFail(Object paramObject);
  }

  public static class OrderDetail
    implements Serializable
  {
    public String bonus;
    public String identifyingCode;
    public BigDecimal noDiscountAmount;
    public BigDecimal savedAmount;
    public BigDecimal shopAmount;
    public String shopName;
    public long timeInMillis;
    public BigDecimal totalCostAmount;
    public String userMobileNo;
    public BigDecimal userPayAmount;
    public BigDecimal userPrepayAmount;

    public OrderDetail()
    {
    }

    public OrderDetail(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, long paramLong, String paramString8, String paramString9, String paramString10)
    {
      this.shopName = paramString1;
      paramString1 = paramString2;
      if (TextUtils.isEmpty(paramString2))
        paramString1 = "0";
      this.savedAmount = new BigDecimal(paramString1);
      paramString1 = paramString3;
      if (TextUtils.isEmpty(paramString3))
        paramString1 = "0";
      this.totalCostAmount = new BigDecimal(paramString1);
      paramString1 = paramString4;
      if (TextUtils.isEmpty(paramString4))
        paramString1 = "0";
      this.shopAmount = new BigDecimal(paramString1);
      paramString1 = paramString5;
      if (TextUtils.isEmpty(paramString5))
        paramString1 = "0";
      this.userPayAmount = new BigDecimal(paramString1);
      paramString1 = paramString6;
      if (TextUtils.isEmpty(paramString6))
        paramString1 = "0";
      this.noDiscountAmount = new BigDecimal(paramString1);
      paramString1 = paramString7;
      if (TextUtils.isEmpty(paramString7))
        paramString1 = "0";
      this.userPrepayAmount = new BigDecimal(paramString1);
      this.timeInMillis = paramLong;
      this.userMobileNo = paramString8;
      this.identifyingCode = paramString9;
      this.bonus = paramString10;
    }
  }

  public static enum PayStatus
  {
    static
    {
      FAIL = new PayStatus("FAIL", 2);
      IN_REFUND = new PayStatus("IN_REFUND", 3);
      DONE_REFUND = new PayStatus("DONE_REFUND", 4);
      $VALUES = new PayStatus[] { PENDING, SUCCESS, FAIL, IN_REFUND, DONE_REFUND };
    }

    static PayStatus transferFromBooking(int paramInt)
    {
      switch (paramInt)
      {
      default:
        return PENDING;
      case 0:
        return PENDING;
      case 20:
      }
      return SUCCESS;
    }

    static PayStatus transferFromHui(int paramInt)
    {
      switch (paramInt)
      {
      default:
        return FAIL;
      case 0:
        return PENDING;
      case 1:
        return SUCCESS;
      case -1:
        return FAIL;
      case 2:
        return IN_REFUND;
      case 3:
      }
      return DONE_REFUND;
    }
  }

  public static enum PayType
  {
    static
    {
      BOOKING_PAY_RESULT = new PayType("BOOKING_PAY_RESULT", 3);
      $VALUES = new PayType[] { HUI_ORDER_DETAIL, HUI_ORDER_DETAIL_FROM_USER_ORDER_LIST, HUI_PAY_RESULT, BOOKING_PAY_RESULT };
    }
  }

  public static enum ReviewStatus
  {
    static
    {
      CLOSED = new ReviewStatus("CLOSED", 3);
      $VALUES = new ReviewStatus[] { HIDE, OPEN, OPEN_IN_EDIT, CLOSED };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.entity.HuiPayDetailDataSource
 * JD-Core Version:    0.6.0
 */