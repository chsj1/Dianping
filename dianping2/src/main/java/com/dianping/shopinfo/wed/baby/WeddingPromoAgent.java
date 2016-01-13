package com.dianping.shopinfo.wed.baby;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.wed.WedBookingDialog;
import com.dianping.base.widget.wed.WedBookingDialog.OnWedBookingDialogClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.util.DeviceUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONObject;

public class WeddingPromoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, WedBookingDialog.OnWedBookingDialogClickListener
{
  protected static final String CELL_PROMO = "0100Basic.06Promo";
  protected static final String CELL_PROMO_SMALL = "0200Basic.40Promo";
  MApiRequest bookingRequest;
  DPObject historyObject;
  MApiRequest historyRequest;
  String phoneNum;
  DPObject promoObject;
  MApiRequest promoRequest;
  DPObject shopInfoObject;
  private WedBookingDialog wedBookingDialog;

  public WeddingPromoAgent(Object paramObject)
  {
    super(paramObject);
    sendPromoRequest();
    sendHistoryRequest();
  }

  private static String getBookingUrl(String paramString, Map<String, String> paramMap)
  {
    if (com.dianping.util.TextUtils.isEmpty(paramString))
      return "";
    StringBuilder localStringBuilder = new StringBuilder(paramString);
    if (paramMap != null)
    {
      localStringBuilder.append("?");
      paramString = paramMap.entrySet().iterator();
      while (paramString.hasNext())
      {
        paramMap = (Map.Entry)paramString.next();
        localStringBuilder.append((String)paramMap.getKey()).append("=").append((String)paramMap.getValue()).append("&");
      }
      localStringBuilder.append("dpId=").append(DeviceUtils.dpid());
    }
    while (true)
    {
      return localStringBuilder.toString();
      if (paramString.contains("?"))
      {
        localStringBuilder.append("&dpId=").append(DeviceUtils.dpid());
        continue;
      }
      localStringBuilder.append("?dpId=").append(DeviceUtils.dpid());
    }
  }

  private void sendBookingRequest(String paramString)
  {
    if (this.bookingRequest != null)
      return;
    if (this.bookingRequest == null)
    {
      String str = accountService().token();
      HashMap localHashMap = new HashMap();
      localHashMap.put("shopid", getShop().getInt("ID") + "");
      localHashMap.put("phoneNum", paramString);
      localHashMap.put("token", str);
      this.bookingRequest = BasicMApiRequest.mapiPost(getBookingUrl("http://m.api.dianping.com/wedding/commonbooking.bin", localHashMap), new String[0]);
      this.phoneNum = paramString;
    }
    mapiService().exec(this.bookingRequest, this);
    showProgressDialog("正在提交");
  }

  private void sendHistoryRequest()
  {
    if (this.historyRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/weddinghotelbookinghistory.bin").buildUpon();
    localBuilder.appendQueryParameter("dpid", DeviceUtils.dpid());
    localBuilder.appendQueryParameter("userid", accountService().id() + "");
    localBuilder.appendQueryParameter("type", "1");
    this.historyRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.historyRequest, this);
  }

  private void sendPromoRequest()
  {
    if (this.promoRequest != null);
    do
      return;
    while (getShop() == null);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/getweddinginfo.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", getShop().getInt("ID") + "");
    this.promoRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.promoRequest, this);
  }

  boolean isHeadMiniPic()
  {
    if (this.shopInfoObject == null);
    do
      return false;
    while (this.shopInfoObject.getInt("UiFlag") != 1);
    return true;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((paramBundle != null) && (paramBundle.containsKey("shop")))
      this.shopInfoObject = ((DPObject)paramBundle.getParcelable("shop"));
    if (this.promoObject == null)
    {
      sendPromoRequest();
      removeAllCells();
    }
    int k;
    int i;
    do
    {
      return;
      removeAllCells();
      paramBundle = LayoutInflater.from(getContext()).inflate(R.layout.wed_shopinfo_promo_agent, getParentView(), false);
      if (isHeadMiniPic())
        addCell("0200Basic.40Promo", paramBundle);
      Object localObject;
      while (true)
      {
        localObject = (NovaTextView)paramBundle.findViewById(R.id.textview_wed_promo_title);
        ((NovaTextView)localObject).setOnClickListener(this);
        ((NovaTextView)localObject).setGAString("discount_title");
        localObject = (NovaLinearLayout)paramBundle.findViewById(R.id.linearlayout_wed_promo_gift);
        ((NovaLinearLayout)localObject).setOnClickListener(this);
        ((NovaLinearLayout)localObject).setGAString("discount_detail");
        localObject = this.promoObject.getArray("WeddingPromoList");
        if ((localObject != null) && (localObject.length != 0))
          break;
        removeAllCells();
        return;
        addCell("0100Basic.06Promo", paramBundle);
      }
      k = 0;
      i = 0;
      int j = 0;
      if (j < localObject.length)
      {
        String str2 = localObject[j];
        String str1 = str2.getString("Title");
        int n;
        int m;
        if (str1.equals("订单礼"))
        {
          str2 = str2.getString("Content");
          if (com.dianping.util.TextUtils.isEmpty(str2))
          {
            n = i;
            m = k;
          }
        }
        while (true)
        {
          j += 1;
          k = m;
          i = n;
          break;
          m = 1;
          ((TextView)paramBundle.findViewById(R.id.textview_wed_promo_gift_01)).setText(str1);
          ((TextView)paramBundle.findViewById(R.id.textview_wed_promo_gift_des_01)).setText(str2);
          n = i;
          continue;
          m = k;
          n = i;
          if (!str1.equals("到店礼"))
            continue;
          str2 = str2.getString("Content");
          m = k;
          n = i;
          if (com.dianping.util.TextUtils.isEmpty(str2))
            continue;
          n = 1;
          ((TextView)paramBundle.findViewById(R.id.textview_wed_promo_gift_02)).setText(str1);
          ((TextView)paramBundle.findViewById(R.id.textview_wed_promo_gift_des_02)).setText(str2);
          m = k;
        }
      }
      if ((k == 0) && (i == 0))
      {
        removeAllCells();
        return;
      }
      if ((k != 0) || (i == 0))
        continue;
      paramBundle.findViewById(R.id.linearlayout_wed_promo_gift_01).setVisibility(8);
      paramBundle.findViewById(R.id.linearlayout_wed_promo_gift_02).setPadding(0, ViewUtils.dip2px(getContext(), 15.0F), 0, ViewUtils.dip2px(getContext(), 15.0F));
      return;
    }
    while ((k == 0) || (i != 0));
    paramBundle.findViewById(R.id.linearlayout_wed_promo_gift_02).setVisibility(8);
    paramBundle.findViewById(R.id.linearlayout_wed_promo_gift_01).setPadding(0, ViewUtils.dip2px(getContext(), 15.0F), 0, ViewUtils.dip2px(getContext(), 15.0F));
  }

  public void onClick(View paramView)
  {
    if (this.wedBookingDialog == null)
    {
      this.wedBookingDialog = new WedBookingDialog(getContext());
      this.wedBookingDialog.setOnWedBookingDialogClickListener(this);
    }
    DPObject[] arrayOfDPObject = null;
    DPObject localDPObject = (DPObject)getSharedObject("WeddingShopInfo");
    Object localObject = null;
    paramView = localObject;
    if (localDPObject != null)
    {
      localDPObject = localDPObject.getObject("BookingInfo");
      paramView = localObject;
      if (localDPObject != null)
        paramView = localDPObject.getString("BookingBtnText");
    }
    if (this.promoObject != null)
      arrayOfDPObject = this.promoObject.getArray("WeddingPromoList");
    this.wedBookingDialog.initDialog(paramView, arrayOfDPObject, this.historyObject);
    this.wedBookingDialog.show();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.promoRequest)
      this.promoRequest = null;
    do
    {
      return;
      if (paramMApiRequest != this.historyRequest)
        continue;
      this.historyRequest = null;
      return;
    }
    while (paramMApiRequest != this.bookingRequest);
    this.bookingRequest = null;
    dismissDialog();
    if ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!android.text.TextUtils.isEmpty(paramMApiResponse.message().toString())))
    {
      paramMApiRequest = Toast.makeText(getContext(), paramMApiResponse.message().toString(), 1);
      paramMApiRequest.setGravity(17, 0, 0);
      paramMApiRequest.show();
      return;
    }
    paramMApiRequest = Toast.makeText(getContext(), "网络不给力啊，请稍后再试试", 1);
    paramMApiRequest.setGravity(17, 0, 0);
    paramMApiRequest.show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.promoRequest)
    {
      this.promoRequest = null;
      this.promoObject = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
      paramMApiRequest = new Bundle();
      paramMApiRequest.putParcelable("promo", this.promoObject);
      dispatchAgentChanged("shopinfo/wed_toolbar", paramMApiRequest);
    }
    while (true)
    {
      return;
      if (paramMApiRequest == this.historyRequest)
      {
        this.historyRequest = null;
        paramMApiRequest = new Bundle();
        this.historyObject = ((DPObject)paramMApiResponse.result());
        paramMApiRequest.putParcelable("history", this.historyObject);
        dispatchAgentChanged("shopinfo/wed_toolbar", paramMApiRequest);
        return;
      }
      if (paramMApiRequest != this.bookingRequest)
        continue;
      this.bookingRequest = null;
      dismissDialog();
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest == null)
        continue;
      switch (paramMApiRequest.getInt("Flag"))
      {
      default:
        return;
      case 200:
        try
        {
          paramMApiRequest = new JSONObject(paramMApiRequest.getString("Data")).optString("redirectLink");
          if ((paramMApiRequest == null) || ("null".equals(paramMApiRequest)) || (com.dianping.util.TextUtils.isEmpty(paramMApiRequest)))
            continue;
          paramMApiResponse = Uri.parse("dianping://web").buildUpon();
          paramMApiResponse.appendQueryParameter("url", paramMApiRequest);
          startActivity(new Intent("android.intent.action.VIEW", paramMApiResponse.build()));
          return;
        }
        catch (Exception paramMApiRequest)
        {
          paramMApiRequest.printStackTrace();
          return;
        }
      case 501:
      }
    }
    paramMApiRequest = Uri.parse("dianping://babyverifyphone").buildUpon();
    paramMApiRequest.appendQueryParameter("phonenum", this.phoneNum);
    paramMApiRequest.appendQueryParameter("shopid", shopId() + "");
    startActivity(new Intent("android.intent.action.VIEW", paramMApiRequest.build()));
  }

  public void onWedBookingDialogClick(String paramString)
  {
    sendBookingRequest(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingPromoAgent
 * JD-Core Version:    0.6.0
 */