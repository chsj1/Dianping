package com.dianping.takeaway.entity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.takeaway.util.TakeawayPreferencesManager;
import com.dianping.takeaway.util.TakeawayUtils;
import com.dianping.util.DeviceUtils;
import com.dianping.widget.view.GAUserInfo;
import java.math.BigDecimal;
import java.util.ArrayList;

public class TakeawayDeliveryDataSource
{
  public final int ACTIVITY_DP = 11;
  public final int ACTIVITY_ELEME = 12;
  public final int PAYMENT_FACETOFACE = 0;
  public final int PAYMENT_ONLINE = 1;
  private NovaActivity activity;
  public DPObject[] activityList;
  public String activityTips;
  public int activityType;
  public boolean canUseCoupon;
  public boolean canUseThirdpartyCode;
  public String carrier;
  public String cartFee;
  public String cartId;
  private MApiRequest confirmCouponRequest;
  private MApiRequest confirmOrderRequest;
  private DPObject content;
  public DPObject couponInfo;
  public int curPayType;
  private DataLoadListener dataLoadListener;
  public DPObject[] dishList;
  public BigDecimal dpDiscountPrice = BigDecimal.ZERO;
  public String dpDiscountStr;
  public DPObject[] feeList;
  public BigDecimal finalPrice = BigDecimal.ZERO;
  private MApiRequest getUserBindPhoneRequest;
  private String initialLat;
  private String initialLng;
  public String inputInvoiceHeader;
  public String invoiceMinFee;
  public boolean isInvoiceSupported;
  public LoadCause loadCause;
  public BigDecimal midPrice = BigDecimal.ZERO;
  public int needToStartActivity;
  public String onlinePayActi;
  public DPObject orderAddress;
  public int payType;
  public boolean phoneVerified = false;
  SharedPreferences pref;
  public int productCode;
  public String queryId;
  public String remark;
  public int selectedInvoiceType;
  public String shopID;
  private MApiRequest submitOrderRequest;
  public int supportPayType;
  public String thirdpartyCouponReduce;
  public int thirdpartyCouponStatus;
  public String thirdpartyCouponStr;
  public String thirdpartyCouponTitle;
  public int thirdpartyCouponType;
  public String toastMsg;

  public TakeawayDeliveryDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
    this.pref = TakeawayPreferencesManager.getTakeawayDeliveryPreferences(paramNovaActivity);
  }

  private String[] generateConfirmCouponParams()
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = TakeawayPreferencesManager.getTakeawayDeliveryPreferences(this.activity);
    String str = ((SharedPreferences)localObject).getString("eleme_phone", null);
    localObject = ((SharedPreferences)localObject).getString("eleme_token", null);
    localArrayList.add("phone");
    localArrayList.add(str);
    localArrayList.add("token");
    localArrayList.add(localObject);
    localArrayList.add("cartid");
    localArrayList.add(this.cartId);
    return (String)(String[])localArrayList.toArray(new String[0]);
  }

  private String[] generateConfirmOderParams()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("shopid");
    localArrayList.add(this.shopID);
    localArrayList.add("content");
    localArrayList.add(this.content.getString("server"));
    Object localObject = this.activity.location();
    if (localObject != null)
    {
      localArrayList.add("locatelat");
      localArrayList.add(String.valueOf(((Location)localObject).latitude()));
      localArrayList.add("locatelng");
      localArrayList.add(String.valueOf(((Location)localObject).longitude()));
    }
    if (this.orderAddress != null)
    {
      localArrayList.add("lat");
      if (TextUtils.isEmpty(getLat()))
      {
        localObject = "0.0";
        localArrayList.add(localObject);
        localArrayList.add("lng");
        if (!TextUtils.isEmpty(getLng()))
          break label478;
        localObject = "0.0";
        label148: localArrayList.add(localObject);
        localArrayList.add("poi");
        localArrayList.add(this.orderAddress.getString("Poi"));
        localArrayList.add("address");
        localArrayList.add(this.orderAddress.getString("Address"));
        localArrayList.add("phone");
        localArrayList.add(getPhone());
      }
    }
    else
    {
      localArrayList.add("initiallat");
      if (!TextUtils.isEmpty(this.initialLat))
        break label486;
      localObject = "0.0";
      label232: localArrayList.add(localObject);
      localArrayList.add("initiallng");
      if (!TextUtils.isEmpty(this.initialLng))
        break label494;
    }
    label478: label486: label494: for (localObject = "0.0"; ; localObject = this.initialLng)
    {
      localArrayList.add(localObject);
      if (this.loadCause != LoadCause.FIR_LOAD)
      {
        localArrayList.add("curpaytype");
        localArrayList.add(String.valueOf(this.payType));
      }
      if (this.activityType != 0)
      {
        localArrayList.add("curactivityprovider");
        localArrayList.add(String.valueOf(this.activityType));
      }
      if (this.thirdpartyCouponType != 0)
      {
        localArrayList.add("thirdpartycouponstr");
        localArrayList.add(this.thirdpartyCouponStr);
        localArrayList.add("thirdpartycoupontype");
        localArrayList.add(String.valueOf(this.thirdpartyCouponType));
      }
      if (!TextUtils.isEmpty(this.cartId))
      {
        localArrayList.add("cartid");
        localArrayList.add(this.cartId);
      }
      localObject = this.pref.getString("eleme_phone", null);
      String str = this.pref.getString("eleme_token", null);
      if ((!TextUtils.isEmpty((CharSequence)localObject)) && (!TextUtils.isEmpty(str)))
      {
        localArrayList.add("thirdpartybindphone");
        localArrayList.add(localObject);
        localArrayList.add("thirdpartytoken");
        localArrayList.add(str);
      }
      return (String[])localArrayList.toArray(new String[0]);
      localObject = getLat();
      break;
      localObject = getLng();
      break label148;
      localObject = this.initialLat;
      break label232;
    }
  }

  private String[] generateCreateOrderParams()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("shopid");
    localArrayList.add(this.shopID);
    localArrayList.add("queryid");
    localArrayList.add(this.queryId);
    Object localObject1 = this.activity.location();
    if (localObject1 != null)
    {
      localArrayList.add("locatelat");
      localArrayList.add(String.valueOf(((Location)localObject1).latitude()));
      localArrayList.add("locatelng");
      localArrayList.add(String.valueOf(((Location)localObject1).longitude()));
    }
    label144: label249: DPObject[] arrayOfDPObject;
    label275: int k;
    int i;
    if (this.orderAddress != null)
    {
      localArrayList.add("lat");
      if (TextUtils.isEmpty(getLat()))
      {
        localObject1 = "0.0";
        localArrayList.add(localObject1);
        localArrayList.add("lng");
        if (!TextUtils.isEmpty(getLng()))
          break label415;
        localObject1 = "0.0";
        localArrayList.add(localObject1);
        localArrayList.add("poi");
        localArrayList.add(this.orderAddress.getString("Poi"));
        localArrayList.add("address");
        localArrayList.add(this.orderAddress.getString("Address"));
        localArrayList.add("phonenumber");
        localArrayList.add(getPhone());
      }
    }
    else
    {
      localArrayList.add("phoneverified");
      localArrayList.add(String.valueOf(this.phoneVerified));
      localArrayList.add("initiallat");
      if (!TextUtils.isEmpty(this.initialLat))
        break label423;
      localObject1 = "0.0";
      localArrayList.add(localObject1);
      localArrayList.add("initiallng");
      if (!TextUtils.isEmpty(this.initialLng))
        break label431;
      localObject1 = "0.0";
      localArrayList.add(localObject1);
      localObject1 = new StringBuilder();
      if (this.activityList == null)
        break label448;
      arrayOfDPObject = this.activityList;
      k = arrayOfDPObject.length;
      i = 0;
    }
    while (true)
    {
      if (i >= k)
        break label448;
      Object localObject2 = arrayOfDPObject[i];
      if (this.activityType == ((DPObject)localObject2).getInt("Type"))
      {
        localObject2 = ((DPObject)localObject2).getArray("ActivityList");
        int m = localObject2.length;
        int j = 0;
        while (true)
          if (j < m)
          {
            Object localObject3 = localObject2[j];
            if (localObject3.getBoolean("IsShow"))
              ((StringBuilder)localObject1).append(localObject3.getInt("ActivityId")).append("|");
            j += 1;
            continue;
            localObject1 = getLat();
            break;
            label415: localObject1 = getLng();
            break label144;
            label423: localObject1 = this.initialLat;
            break label249;
            label431: localObject1 = this.initialLng;
            break label275;
          }
      }
      i += 1;
    }
    label448: if (!TextUtils.isEmpty((CharSequence)localObject1))
    {
      if (((StringBuilder)localObject1).charAt(((StringBuilder)localObject1).length() - 1) == '|')
        ((StringBuilder)localObject1).deleteCharAt(((StringBuilder)localObject1).length() - 1);
      localArrayList.add("activitycontent");
      localArrayList.add(((StringBuilder)localObject1).toString());
    }
    localArrayList.add("remark");
    localArrayList.add(this.remark);
    localArrayList.add("content");
    localArrayList.add(this.content.getString("server"));
    localArrayList.add("paytype");
    localArrayList.add(String.valueOf(this.payType));
    localArrayList.add("invoicetype");
    localArrayList.add(String.valueOf(this.selectedInvoiceType));
    localArrayList.add("invoiceheader");
    localArrayList.add(this.inputInvoiceHeader);
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("takeaway"));
    if (!TextUtils.isEmpty(this.dpDiscountStr))
    {
      localArrayList.add("dpdiscountprice");
      localArrayList.add(TakeawayUtils.bigDecimalTrailingZerosToString(this.dpDiscountPrice));
      localArrayList.add("dpdiscountstr");
      localArrayList.add(this.dpDiscountStr);
    }
    localArrayList.add("curprice");
    localArrayList.add(TakeawayUtils.bigDecimalTrailingZerosToString(this.finalPrice));
    if (this.activityType != 0)
    {
      localArrayList.add("curactivityprovider");
      localArrayList.add(String.valueOf(this.activityType));
    }
    if (this.cartId != null)
    {
      localArrayList.add("cartid");
      localArrayList.add(this.cartId);
      localArrayList.add("cartfee");
      localArrayList.add(this.cartFee);
    }
    return (String)(String)(String[])localArrayList.toArray(new String[0]);
  }

  public void abortRequests()
  {
    if (this.getUserBindPhoneRequest != null)
    {
      this.activity.mapiService().abort(this.getUserBindPhoneRequest, null, true);
      this.getUserBindPhoneRequest = null;
    }
    if (this.confirmCouponRequest != null)
    {
      this.activity.mapiService().abort(this.confirmCouponRequest, null, true);
      this.confirmCouponRequest = null;
    }
    if (this.confirmOrderRequest != null)
    {
      this.activity.mapiService().abort(this.confirmOrderRequest, null, true);
      this.confirmOrderRequest = null;
    }
    if (this.submitOrderRequest != null)
    {
      this.activity.mapiService().abort(this.submitOrderRequest, null, true);
      this.submitOrderRequest = null;
    }
  }

  public void confirmCouponTask()
  {
    if (this.confirmCouponRequest != null)
      this.activity.mapiService().abort(this.confirmCouponRequest, null, true);
    this.confirmCouponRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/getelemecoupon.ta", generateConfirmCouponParams());
    this.activity.mapiService().exec(this.confirmCouponRequest, new FullRequestHandle()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayDeliveryDataSource.access$202(TakeawayDeliveryDataSource.this, null);
        if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
          TakeawayDeliveryDataSource.this.dataLoadListener.elemeCouponOrder(TakeawayNetLoadStatus.STATUS_FAILED, paramMApiResponse.error());
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayDeliveryDataSource.access$202(TakeawayDeliveryDataSource.this, null);
        paramMApiRequest = paramMApiResponse.result();
        if ((paramMApiRequest instanceof DPObject))
        {
          paramMApiRequest = (DPObject)paramMApiRequest;
          boolean bool = paramMApiRequest.getBoolean("NeedVerify");
          TakeawayDeliveryDataSource.this.couponInfo = paramMApiRequest.getObject("CouponInfo");
          if (!bool)
            break label88;
        }
        label88: for (TakeawayDeliveryDataSource.this.needToStartActivity = 7; ; TakeawayDeliveryDataSource.this.needToStartActivity = 8)
        {
          if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
            TakeawayDeliveryDataSource.this.dataLoadListener.elemeCouponOrder(TakeawayNetLoadStatus.STATUS_SUCCESS, null);
          return;
        }
      }

      public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
      {
      }

      public void onRequestStart(MApiRequest paramMApiRequest)
      {
        if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
          TakeawayDeliveryDataSource.this.dataLoadListener.elemeCouponOrder(TakeawayNetLoadStatus.STATUS_START, null);
      }
    });
  }

  public void confirmOrderTask()
  {
    if (this.confirmOrderRequest != null)
      this.activity.mapiService().abort(this.confirmOrderRequest, null, true);
    this.confirmOrderRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/confirmorderinfo.ta", generateConfirmOderParams());
    this.activity.mapiService().exec(this.confirmOrderRequest, new FullRequestHandle()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayDeliveryDataSource.access$302(TakeawayDeliveryDataSource.this, null);
        if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
          TakeawayDeliveryDataSource.this.dataLoadListener.loadOrder(TakeawayNetLoadStatus.STATUS_FAILED, paramMApiResponse.error());
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayDeliveryDataSource.access$302(TakeawayDeliveryDataSource.this, null);
        paramMApiRequest = paramMApiResponse.result();
        if ((paramMApiRequest instanceof DPObject))
        {
          paramMApiRequest = (DPObject)paramMApiRequest;
          TakeawayDeliveryDataSource.this.supportPayType = paramMApiRequest.getInt("ShopPayType");
          TakeawayDeliveryDataSource.this.curPayType = paramMApiRequest.getInt("CurPayType");
          TakeawayDeliveryDataSource.this.toastMsg = paramMApiRequest.getString("ToastMsg");
          paramMApiResponse = paramMApiRequest.getObject("ActivityInfo");
          TakeawayDeliveryDataSource.this.onlinePayActi = paramMApiResponse.getString("OnlinePayActivity");
          TakeawayDeliveryDataSource.this.activityList = paramMApiResponse.getArray("ActivityInfoList");
          TakeawayDeliveryDataSource.this.activityTips = paramMApiResponse.getString("ActivityTips");
          TakeawayDeliveryDataSource.this.activityType = paramMApiResponse.getInt("DefaultActivityProvider");
          TakeawayDeliveryDataSource.this.canUseCoupon = paramMApiRequest.getBoolean("CanUseDPCoupon");
          TakeawayDeliveryDataSource.this.productCode = paramMApiRequest.getInt("ProductCode");
          paramMApiResponse = paramMApiRequest.getObject("ThirdpartyCouponInfo");
          TakeawayDeliveryDataSource.this.thirdpartyCouponTitle = paramMApiResponse.getString("Title");
          TakeawayDeliveryDataSource.this.thirdpartyCouponStr = paramMApiResponse.getString("ThirdpartyCouponStr");
          TakeawayDeliveryDataSource.this.thirdpartyCouponType = paramMApiResponse.getInt("ThirdpartyCouponType");
          TakeawayDeliveryDataSource.this.canUseThirdpartyCode = paramMApiResponse.getBoolean("CanUseThirdpartyCode");
          TakeawayDeliveryDataSource.this.thirdpartyCouponStatus = paramMApiResponse.getInt("ThirdpartyCouponStatus");
          TakeawayDeliveryDataSource.this.thirdpartyCouponReduce = paramMApiResponse.getString("ThirdpartyCouponReduce");
          TakeawayDeliveryDataSource.this.feeList = paramMApiRequest.getArray("FeeList");
          TakeawayDeliveryDataSource.this.cartId = paramMApiRequest.getString("CartId");
          TakeawayDeliveryDataSource.this.cartFee = paramMApiRequest.getString("CartFee");
          TakeawayDeliveryDataSource.this.invoiceMinFee = paramMApiRequest.getString("InvoiceMinFee");
          TakeawayDeliveryDataSource.this.isInvoiceSupported = paramMApiRequest.getBoolean("InvoiceSupported");
          TakeawayDeliveryDataSource.this.dishList = paramMApiRequest.getArray("DishList");
          TakeawayDeliveryDataSource.this.updateContents();
          TakeawayDeliveryDataSource.this.carrier = paramMApiRequest.getString("CarrierName");
          TakeawayDeliveryDataSource.this.midPrice = BigDecimal.valueOf(paramMApiRequest.getDouble("CurAmount"));
          if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
            TakeawayDeliveryDataSource.this.dataLoadListener.loadOrder(TakeawayNetLoadStatus.STATUS_SUCCESS, null);
        }
      }

      public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
      {
      }

      public void onRequestStart(MApiRequest paramMApiRequest)
      {
        if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
          TakeawayDeliveryDataSource.this.dataLoadListener.loadOrder(TakeawayNetLoadStatus.STATUS_START, null);
      }
    });
  }

  public void fetchParams(Bundle paramBundle)
  {
    if (paramBundle == null)
    {
      this.shopID = this.activity.getStringParam("shopid");
      this.queryId = this.activity.getStringParam("queryid");
      this.initialLat = this.activity.getStringParam("initiallat");
      this.initialLng = this.activity.getStringParam("initiallng");
      this.carrier = this.activity.getStringParam("carrier");
      paramBundle = this.activity.getIntent();
      this.orderAddress = ((DPObject)paramBundle.getParcelableExtra("useraddress"));
    }
    for (this.content = ((DPObject)paramBundle.getParcelableExtra("content")); ; this.content = ((DPObject)paramBundle.getParcelable("content")))
    {
      if (this.content != null)
        this.dishList = this.content.getArray("page");
      return;
      this.shopID = paramBundle.getString("shopid");
      this.queryId = paramBundle.getString("queryid");
      this.initialLat = paramBundle.getString("initiallat");
      this.initialLng = paramBundle.getString("initiallng");
      this.carrier = paramBundle.getString("carrier");
      this.orderAddress = ((DPObject)paramBundle.getParcelable("useraddress"));
    }
  }

  public String getAddress()
  {
    if (this.orderAddress == null)
      return "";
    Object localObject2 = this.orderAddress.getString("Poi");
    String str = this.orderAddress.getString("Address");
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject1 = localObject2;
    if (localObject2 == null)
      localObject1 = "";
    localObject2 = localStringBuilder.append((String)localObject1);
    localObject1 = str;
    if (str == null)
      localObject1 = "";
    return (String)(String)(String)localObject1;
  }

  public GAUserInfo getGAUserInfo()
  {
    GAUserInfo localGAUserInfo = new GAUserInfo();
    try
    {
      localGAUserInfo.shop_id = Integer.valueOf(Integer.parseInt(this.shopID));
      localGAUserInfo.query_id = this.queryId;
      return localGAUserInfo;
    }
    catch (Exception localException)
    {
      while (true)
        localGAUserInfo.shop_id = Integer.valueOf(0);
    }
  }

  public String getLat()
  {
    if (this.orderAddress == null)
      return "";
    return String.valueOf(this.orderAddress.getDouble("Lat"));
  }

  public String getLng()
  {
    if (this.orderAddress == null)
      return "";
    return String.valueOf(this.orderAddress.getDouble("Lng"));
  }

  public String getPhone()
  {
    if (this.orderAddress == null)
      return "";
    return this.orderAddress.getString("Phone");
  }

  public void getUserBindPhoneTask()
  {
    if (this.getUserBindPhoneRequest != null)
      this.activity.mapiService().abort(this.getUserBindPhoneRequest, null, true);
    this.getUserBindPhoneRequest = BasicMApiRequest.mapiGet("http://waimai.api.dianping.com/getuserphone.ta", CacheType.DISABLED);
    this.activity.mapiService().exec(this.getUserBindPhoneRequest, new FullRequestHandle()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayDeliveryDataSource.access$102(TakeawayDeliveryDataSource.this, null);
        if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
          TakeawayDeliveryDataSource.this.dataLoadListener.elemeCouponOrder(TakeawayNetLoadStatus.STATUS_FAILED, paramMApiResponse.error());
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayDeliveryDataSource.access$102(TakeawayDeliveryDataSource.this, null);
        paramMApiRequest = paramMApiResponse.result();
        if ((paramMApiRequest instanceof DPObject))
        {
          paramMApiRequest = (DPObject)paramMApiRequest;
          int i = paramMApiRequest.getInt("Code");
          paramMApiResponse = TakeawayDeliveryDataSource.this.pref.edit();
          switch (i)
          {
          default:
            paramMApiResponse.commit();
            if (TextUtils.isEmpty(TakeawayDeliveryDataSource.this.pref.getString("eleme_token", null)))
              break;
            TakeawayDeliveryDataSource.this.confirmCouponTask();
          case 0:
          case 1:
          }
        }
        while (true)
        {
          if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
            TakeawayDeliveryDataSource.this.dataLoadListener.elemeCouponOrder(TakeawayNetLoadStatus.STATUS_SUCCESS, null);
          return;
          paramMApiResponse.putString("eleme_phone", TakeawayDeliveryDataSource.this.getPhone());
          break;
          paramMApiResponse.putString("eleme_phone", paramMApiRequest.getString("Content"));
          break;
          TakeawayDeliveryDataSource.this.needToStartActivity = 7;
        }
      }

      public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
      {
      }

      public void onRequestStart(MApiRequest paramMApiRequest)
      {
        if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
          TakeawayDeliveryDataSource.this.dataLoadListener.elemeCouponOrder(TakeawayNetLoadStatus.STATUS_START, null);
      }
    });
  }

  public void saveData(Bundle paramBundle)
  {
    paramBundle.putString("shopid", this.shopID);
    paramBundle.putString("queryid", this.queryId);
    paramBundle.putString("initiallat", this.initialLat);
    paramBundle.putString("initiallng", this.initialLng);
    paramBundle.putString("carrier", this.carrier);
    paramBundle.putParcelable("useraddress", this.orderAddress);
    paramBundle.putParcelable("content", this.content);
  }

  public void setDataLoadListener(DataLoadListener paramDataLoadListener)
  {
    this.dataLoadListener = paramDataLoadListener;
  }

  public void submitOrderTask()
  {
    if (this.submitOrderRequest != null)
      return;
    this.submitOrderRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/createorder.ta", generateCreateOrderParams());
    this.activity.mapiService().exec(this.submitOrderRequest, new FullRequestHandle()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayDeliveryDataSource.access$402(TakeawayDeliveryDataSource.this, null);
        if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
          TakeawayDeliveryDataSource.this.dataLoadListener.submitOrder(TakeawayNetLoadStatus.STATUS_FAILED, paramMApiResponse.error());
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayDeliveryDataSource.access$402(TakeawayDeliveryDataSource.this, null);
        paramMApiRequest = paramMApiResponse.result();
        if (((paramMApiRequest instanceof DPObject)) && (TakeawayDeliveryDataSource.this.dataLoadListener != null))
          TakeawayDeliveryDataSource.this.dataLoadListener.submitOrder(TakeawayNetLoadStatus.STATUS_SUCCESS, paramMApiRequest);
      }

      public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
      {
      }

      public void onRequestStart(MApiRequest paramMApiRequest)
      {
        if (TakeawayDeliveryDataSource.this.dataLoadListener != null)
          TakeawayDeliveryDataSource.this.dataLoadListener.submitOrder(TakeawayNetLoadStatus.STATUS_START, null);
      }
    });
  }

  public DPObject updateContents()
  {
    Object localObject = new StringBuilder();
    DPObject[] arrayOfDPObject = this.dishList;
    int j = arrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = arrayOfDPObject[i];
      ((StringBuilder)localObject).append(localDPObject.getInt("DishId")).append(",").append(localDPObject.getInt("Count")).append("|");
      i += 1;
    }
    if ((((StringBuilder)localObject).length() > 0) && (((StringBuilder)localObject).charAt(((StringBuilder)localObject).length() - 1) == '|'));
    for (localObject = ((StringBuilder)localObject).substring(0, ((StringBuilder)localObject).length() - 1); ; localObject = ((StringBuilder)localObject).toString())
      return this.content.edit().putString("server", (String)localObject).generate();
  }

  public static abstract interface DataLoadListener
  {
    public abstract void elemeCouponOrder(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);

    public abstract void loadOrder(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);

    public abstract void submitOrder(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject);
  }

  public static enum LoadCause
  {
    static
    {
      ADDRESS_CHANGED = new LoadCause("ADDRESS_CHANGED", 1);
      PAY_TYPE_CHANGED = new LoadCause("PAY_TYPE_CHANGED", 2);
      LOG_IN_SUCCESS = new LoadCause("LOG_IN_SUCCESS", 3);
      ACTIVITY_CHANGED = new LoadCause("ACTIVITY_CHANGED", 4);
      $VALUES = new LoadCause[] { FIR_LOAD, ADDRESS_CHANGED, PAY_TYPE_CHANGED, LOG_IN_SUCCESS, ACTIVITY_CHANGED };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayDeliveryDataSource
 * JD-Core Version:    0.6.0
 */