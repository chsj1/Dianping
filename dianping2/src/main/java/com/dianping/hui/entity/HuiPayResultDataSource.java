package com.dianping.hui.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import android.net.wifi.WifiManager;
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
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.util.network.NetworkUtils;
import com.dianping.util.network.WifiModel;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.json.JSONArray;

public class HuiPayResultDataSource
  implements IHuiDataSource
{
  static final String ADD_FAVORITE_URL = "http://m.api.dianping.com/addfavoriteshop.bin";
  static final String HUI_PAY_RESULT_URL = "http://hui.api.dianping.com/querymopaystatus.bin";
  private static final int MSG_HUI_PAY_RESULT_COUNT_DOWN = 2;
  private static final int MSG_HUI_PAY_RESULT_RETRY = 4;
  static final int POINT_MALL_HIDE = 20;
  static final int POINT_MALL_SHOW = 10;
  static final String POI_UPLOAD_WIFI_URL = "http://mapi.dianping.com/poi/poiwifi/addwifi.bin";
  static final String REMOVE_FAVORITE_URL = "http://m.api.dianping.com/delfavoriteshop.bin";
  static final String TAG = HuiPayResultDataSource.class.getSimpleName();
  public int RETRY_INTERVAL = 5000;
  NovaActivity activity;
  public DPObject[] advertisements;
  public boolean backFromAddReviewPage = false;
  public Uri backUri;
  public String[] buffetDescs;
  public String contactMerchantTip;
  public String curAmount;
  public UniPayResultDataLoaderListener dataLoadListener;
  public FavoriteStatus favoriteStatus = FavoriteStatus.UNINITIALIZED;
  private Handler handler = new HuiPayResultDataSource.2(this);
  public boolean hasVoiceReport = false;
  public String huiOrderId;
  public int huiPayResultCountDownSeconds = 60;
  MApiRequest huiPayResultReq;
  public int huiPayResultRetryTimes = 12;
  public boolean isAutoFav = true;
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new HuiPayResultDataSource.1(this);
  public String orderCreateTime;
  public OrderDetail orderDetail;
  public DPObject orderShareInfo;
  public String payFailSubtitle;
  public PayStatus payStatus;
  MApiRequest poiUploadWifiReq;
  public int pointMallEnable;
  public String pointMallMessage;
  public String pointMallUrl;
  public String pointsInfo;
  public String[] remarks;
  public DPObject reviewInfo;
  public ReviewStatus reviewStatus = ReviewStatus.HIDE;
  public DPObject[] rightDoArray;
  public String serializedId;
  public String serviceUrl;
  public int shopId;
  public String[] shopPhoneNumbers;
  public String[] shopVouchers;
  public String statusMsg;
  public DPObject ticketInfo;
  public String userAmountStr;

  public HuiPayResultDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
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
      DPObject localDPObject = paramArrayOfDPObject[i];
      if (localDPObject.getInt("Type") == 2)
      {
        localStringBuilder.append(localDPObject.getString("DefaultPayTip"));
        localStringBuilder.append(",");
      }
      i += 1;
    }
    if (localStringBuilder.length() > 0)
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    return localStringBuilder.toString();
  }

  public void releaseRequests()
  {
    if (this.huiPayResultReq != null)
    {
      this.activity.mapiService().abort(this.huiPayResultReq, this.mapiHandler, true);
      this.huiPayResultReq = null;
    }
    if (this.poiUploadWifiReq != null)
    {
      this.activity.mapiService().abort(this.poiUploadWifiReq, this.mapiHandler, true);
      this.poiUploadWifiReq = null;
    }
  }

  public void reqHuiPayResult()
  {
    if (this.huiPayResultReq != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://hui.api.dianping.com/querymopaystatus.bin").buildUpon();
    if (this.activity.isLogined())
      localBuilder.appendQueryParameter("token", this.activity.accountService().token());
    localBuilder.appendQueryParameter("uuid", Environment.uuid());
    localBuilder.appendQueryParameter("imei", Environment.imei());
    localBuilder.appendQueryParameter("cx", DeviceUtils.cxInfo("hui"));
    localBuilder.appendQueryParameter("serializedid", this.serializedId);
    if (!TextUtils.isEmpty(this.orderCreateTime))
      localBuilder.appendQueryParameter("ordercreatetime", this.orderCreateTime);
    this.huiPayResultReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    this.activity.mapiService().exec(this.huiPayResultReq, this.mapiHandler);
  }

  public void reqUploadWifi()
  {
    boolean bool1 = NetworkUtils.wifiManager().isWifiEnabled();
    boolean bool2 = NetworkUtils.isWIFIConnection(this.activity);
    if (this.poiUploadWifiReq != null)
      this.activity.mapiService().abort(this.poiUploadWifiReq, this.mapiHandler, true);
    DPObject localDPObject = this.activity.locationService().location();
    ArrayList localArrayList = new ArrayList();
    WifiModel localWifiModel = NetworkUtils.curWifi();
    localArrayList.add("ssid");
    Object localObject;
    if ((bool1) && (bool2))
    {
      localArrayList.add(TextUtils.stripHeadAndTailQuotations(localWifiModel.getSsid()));
      localArrayList.add("mac");
      String str = localWifiModel.getBssid();
      if ((!bool1) || (!bool2))
        break label391;
      localObject = str;
      if (str == null)
        localObject = "";
      localArrayList.add(localObject);
      label142: if (localDPObject == null)
        break label402;
      localArrayList.add("lat");
      localArrayList.add(Location.FMT.format(localDPObject.getDouble("Lat")));
      localArrayList.add("lng");
      localArrayList.add(Location.FMT.format(localDPObject.getDouble("Lng")));
      label201: localArrayList.add("srctype");
      localArrayList.add("2");
      localArrayList.add("shopid");
      localArrayList.add(Integer.toString(this.shopId));
      localArrayList.add("weight");
      localArrayList.add(Integer.toString(localWifiModel.getLevel()));
      localObject = NetworkUtils.wifiScanResultInfo2JsonArray();
      localArrayList.add("nearwifis");
      if (!bool1)
        break label445;
      if (((JSONArray)localObject).length() != 0)
        break label437;
      localObject = "";
      label285: localArrayList.add(localObject);
    }
    while (true)
    {
      this.poiUploadWifiReq = BasicMApiRequest.mapiPost("http://mapi.dianping.com/poi/poiwifi/addwifi.bin", (String[])localArrayList.toArray(new String[localArrayList.size()]));
      this.activity.mapiService().exec(this.poiUploadWifiReq, this.mapiHandler);
      this.activity.statisticsEvent("hui7", "hui7_wifi_info_sent", "上传wifi信息", this.shopId);
      Log.d(TAG, "upload wifi info, params = " + localArrayList);
      return;
      localArrayList.add("");
      break;
      label391: localArrayList.add("00:00:00:00:00:00");
      break label142;
      label402: localArrayList.add("lat");
      localArrayList.add("0");
      localArrayList.add("lng");
      localArrayList.add("0");
      break label201;
      label437: localObject = ((JSONArray)localObject).toString();
      break label285;
      label445: localArrayList.add("");
    }
  }

  public void restoreData(Bundle paramBundle)
  {
    this.serializedId = paramBundle.getString("serializedid");
  }

  public void saveData(Bundle paramBundle)
  {
    paramBundle.putString("serializedid", this.serializedId);
  }

  public static enum FavoriteStatus
  {
    static
    {
      HIDE = new FavoriteStatus("HIDE", 1);
      SHOW = new FavoriteStatus("SHOW", 2);
      FAVORITE = new FavoriteStatus("FAVORITE", 3);
      NOT_FAVORITE = new FavoriteStatus("NOT_FAVORITE", 4);
      $VALUES = new FavoriteStatus[] { UNINITIALIZED, HIDE, SHOW, FAVORITE, NOT_FAVORITE };
    }
  }

  public static class OrderDetail
  {
    public String bonus;
    public String boughtVoucher;
    public BigDecimal noDiscountAmount;
    public String phoneNumber;
    public BigDecimal saveAmount;
    public String serialNumber;
    public BigDecimal shopAmount;
    public String shopName;
    public String shopVoucher;
    public long timeInMillis;
    public BigDecimal totalAmount;
    public String usedVoucher;
    public String userPay;

    public OrderDetail(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, long paramLong, String paramString10, String paramString11, String paramString12)
    {
      this.shopName = paramString1;
      paramString1 = paramString2;
      if (TextUtils.isEmpty(paramString2))
        paramString1 = "0";
      this.totalAmount = new BigDecimal(paramString1);
      paramString1 = paramString8;
      if (TextUtils.isEmpty(paramString8))
        paramString1 = "0";
      this.noDiscountAmount = new BigDecimal(paramString1);
      paramString1 = paramString9;
      if (TextUtils.isEmpty(paramString9))
        paramString1 = "0";
      this.saveAmount = new BigDecimal(paramString1);
      paramString1 = paramString3;
      if (TextUtils.isEmpty(paramString3))
        paramString1 = "0";
      this.shopAmount = new BigDecimal(paramString1);
      this.shopVoucher = paramString4;
      this.userPay = paramString5;
      this.usedVoucher = paramString6;
      this.boughtVoucher = paramString7;
      this.timeInMillis = paramLong;
      this.phoneNumber = paramString10;
      this.serialNumber = paramString11;
      this.bonus = paramString12;
    }
  }

  public static enum PayStatus
  {
    static
    {
      FAIL = new PayStatus("FAIL", 2);
      UNPAID = new PayStatus("UNPAID", 3);
      IN_REFUND = new PayStatus("IN_REFUND", 4);
      DONE_REFUND = new PayStatus("DONE_REFUND", 5);
      $VALUES = new PayStatus[] { PENDING, SUCCESS, FAIL, UNPAID, IN_REFUND, DONE_REFUND };
    }

    static PayStatus transferFromHui(int paramInt)
    {
      switch (paramInt)
      {
      default:
        return UNPAID;
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

  public static enum ReviewStatus
  {
    static
    {
      CLOSED = new ReviewStatus("CLOSED", 3);
      $VALUES = new ReviewStatus[] { HIDE, OPEN, OPEN_IN_EDIT, CLOSED };
    }
  }

  public static abstract interface UniPayResultDataLoaderListener
  {
    public abstract void countDown(int paramInt, boolean paramBoolean);

    public abstract void loadHuiPayResultError();

    public abstract void loadOneOrder();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.entity.HuiPayResultDataSource
 * JD-Core Version:    0.6.0
 */