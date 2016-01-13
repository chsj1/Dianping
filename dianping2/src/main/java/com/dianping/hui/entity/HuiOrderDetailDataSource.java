package com.dianping.hui.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
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
import com.dianping.util.TextUtils;
import java.math.BigDecimal;

public class HuiOrderDetailDataSource
  implements IHuiDataSource
{
  static final String HUI_ORDER_DETAIL_URL = "http://hui.api.dianping.com/getonemopayorder.hui";
  static final String TAG = HuiOrderDetailDataSource.class.getSimpleName();
  NovaActivity activity;
  public int bizType;
  public String[] buffetDescs;
  public String contactMerchantTip;
  public HuiOrderDetailDataLoaderListener dataLoadListener;
  MApiRequest huiOrderDetailReq;
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new HuiOrderDetailDataSource.1(this);
  public OrderDetail orderDetail;
  public DPObject orderShareInfo;
  public String ordertime;
  public String payFailSubtitle;
  public PayStatus payStatus;
  public int pointMallEnable;
  public String pointMallMessage;
  public String pointMallUrl;
  public String pointsInfo;
  public String repayUri;
  public String serializedId;
  public int shopId;
  public String[] shopPhoneNumbers;
  public String[] shopVouchers;
  public String statusMsg;
  public DPObject ticketInfo;
  public String userAmountStr;

  public HuiOrderDetailDataSource(NovaActivity paramNovaActivity)
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
    if (this.huiOrderDetailReq != null)
    {
      this.activity.mapiService().abort(this.huiOrderDetailReq, this.mapiHandler, true);
      this.huiOrderDetailReq = null;
    }
  }

  public void reqOrderDetail()
  {
    if (this.huiOrderDetailReq != null)
      this.activity.mapiService().abort(this.huiOrderDetailReq, this.mapiHandler, true);
    Uri.Builder localBuilder = Uri.parse("http://hui.api.dianping.com/getonemopayorder.hui").buildUpon();
    localBuilder.appendQueryParameter("serializedid", this.serializedId);
    localBuilder.appendQueryParameter("biztype", String.valueOf(this.bizType));
    localBuilder.appendQueryParameter("uuid", Environment.uuid());
    if (this.activity.isLogined())
      localBuilder.appendQueryParameter("token", this.activity.accountService().token());
    if (!TextUtils.isEmpty(this.ordertime))
      localBuilder.appendQueryParameter("ordertime", this.ordertime);
    this.huiOrderDetailReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    this.activity.mapiService().exec(this.huiOrderDetailReq, this.mapiHandler);
  }

  public void restoreData(Bundle paramBundle)
  {
    this.serializedId = paramBundle.getString("serializedid");
    this.ordertime = paramBundle.getString("ordertime");
    this.bizType = paramBundle.getInt("biztype");
  }

  public void saveData(Bundle paramBundle)
  {
    paramBundle.putString("serializedid", this.serializedId);
    paramBundle.putString("ordertime", this.ordertime);
    paramBundle.putInt("biztype", this.bizType);
  }

  public static abstract interface HuiOrderDetailDataLoaderListener
  {
    public abstract void loadOrderDetail();

    public abstract void loadOrderDetailError();
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.entity.HuiOrderDetailDataSource
 * JD-Core Version:    0.6.0
 */