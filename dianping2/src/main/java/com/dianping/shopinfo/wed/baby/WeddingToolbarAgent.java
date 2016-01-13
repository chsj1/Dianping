package com.dianping.shopinfo.wed.baby;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.ToolbarButton;
import com.dianping.base.widget.ToolbarImageButton;
import com.dianping.base.widget.wed.WedBookingDialog;
import com.dianping.base.widget.wed.WedBookingDialog.OnWedBookingDialogClickListener;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.SimpleMsg;
import com.dianping.shopinfo.base.ShopInfoToolbarAgent;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class WeddingToolbarAgent extends ShopInfoToolbarAgent
  implements View.OnClickListener, WedBookingDialog.OnWedBookingDialogClickListener
{
  protected ToolbarButton addReviewButton;
  MApiRequest bookingRequest;
  String btnText;
  protected ToolbarButton chatButton;
  DPObject chatObj;
  MApiRequest chatRequest;
  int cooperateType = 0;
  DPObject excellentObject;
  DPObject historyObject;
  boolean initBookingView;
  View.OnClickListener listener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = WeddingToolbarAgent.this.getShop();
      if (paramView == null)
        return;
      switch (paramView.getInt("Status"))
      {
      case 2:
      case 3:
      default:
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("shop", paramView);
        AddReviewUtil.addReview(WeddingToolbarAgent.this.getContext(), paramView.getInt("ID"), paramView.getString("Name"), localBundle);
        return;
      case 1:
      case 4:
      }
      Toast.makeText(WeddingToolbarAgent.this.getContext(), "暂停收录点评", 0).show();
    }
  };
  String phoneNum;
  DPObject promoObject;
  DPObject shopInfo;
  protected ToolbarButton telephoneButton;
  WedBookingDialog wedBookingDialog;
  DPObject weddingHotelExtra;

  public WeddingToolbarAgent(Object paramObject)
  {
    super(paramObject);
  }

  void addChatButton()
  {
    int i = 0;
    if (this.chatObj != null)
      if (this.chatObj.getInt("Visible") != 0)
        break label224;
    label224: for (i = 0; ; i = 1)
    {
      if ((i != 0) && (this.chatButton == null))
      {
        Object localObject = getToolbarView().findViewWithTag("4Telephone");
        if (localObject != null)
          getToolbarView().removeView((View)localObject);
        View localView = getToolbarView().findViewWithTag("4Chat");
        if (localObject != null)
          getToolbarView().removeView(localView);
        int j = this.chatObj.getInt("MessageCount");
        i = R.drawable.wed_icon_shop_chat_red;
        if (j <= 0)
          i = R.drawable.wed_icon_shop_chat;
        localObject = new View.OnClickListener(this.chatObj.getString("RedirectLink"))
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + this.val$url));
            WeddingToolbarAgent.this.startActivity(paramView);
          }
        };
        this.chatButton = ((ToolbarButton)LayoutInflater.from(getContext()).inflate(R.layout.wed_toolbar_button, getToolbarView(), false));
        this.chatButton.setGAString("actionbar_im");
        GAHelper.instance().contextStatisticsEvent(getContext(), "actionbar_im", getGAExtra(), "view");
        ((ToolbarImageButton)this.chatButton.findViewById(16908294)).setImageResource(i);
        ((TextView)this.chatButton.findViewById(16908308)).setText("咨询");
        this.chatButton.setOnClickListener((WedBookingDialog.OnWedBookingDialogClickListener)localObject);
        addToolbarButton(this.chatButton, "4Chat");
      }
      return;
    }
  }

  void enterBookingPage()
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://weddinghotelbooking").buildUpon().appendQueryParameter("shopid", String.valueOf(getShop().getInt("ID"))).appendQueryParameter("shopname", DPObjectUtils.getShopFullName(getShop())).build().toString())));
  }

  String getBookingUrl(String paramString, Map<String, String> paramMap)
  {
    if (android.text.TextUtils.isEmpty(paramString))
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

  void initWedReviewButton(int paramInt)
  {
    Object localObject2 = getToolbarView().findViewWithTag("8Review");
    Object localObject1 = localObject2;
    if (localObject2 != null)
    {
      getToolbarView().removeView((View)localObject2);
      localObject1 = null;
    }
    localObject2 = "写点评";
    if (paramInt == 1)
      localObject2 = "点评";
    if (localObject1 == null)
    {
      this.addReviewButton = ((ToolbarButton)LayoutInflater.from(getContext()).inflate(R.layout.wed_toolbar_button, getToolbarView(), false));
      ((ToolbarImageButton)this.addReviewButton.findViewById(16908294)).setImageResource(R.drawable.detail_footerbar_icon_comment_u);
      ((TextView)this.addReviewButton.findViewById(16908308)).setText((CharSequence)localObject2);
      this.addReviewButton.setOnClickListener(this.listener);
      this.addReviewButton.setGAString("actionbar_toreview");
      addToolbarButton(this.addReviewButton, "8Review");
    }
    localObject1 = getShop();
    if (localObject1 != null);
    switch (((DPObject)localObject1).getInt("Status"))
    {
    case 2:
    case 3:
    default:
      this.addReviewButton.setEnabled(true);
      return;
    case 1:
    case 4:
    }
    this.addReviewButton.setEnabled(false);
  }

  void initWedToolbar()
  {
    Object localObject1 = getToolbarView().findViewWithTag("7Review");
    if (localObject1 != null)
      getToolbarView().removeView((View)localObject1);
    localObject1 = getToolbarView().findViewWithTag("4CheckIn");
    if (localObject1 != null)
      getToolbarView().removeView((View)localObject1);
    localObject1 = getToolbarView().findViewWithTag("6Photo");
    if (localObject1 != null)
      getToolbarView().removeView((View)localObject1);
    localObject1 = new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        WeddingToolbarAgent.this.enterBookingPage();
      }
    };
    Object localObject2 = this.shopInfo.getObject("BookingInfo");
    if (localObject2 != null)
      this.btnText = ((DPObject)localObject2).getString("BookingBtnText");
    if (!this.initBookingView)
    {
      localObject2 = (ToolbarButton)LayoutInflater.from(getContext()).inflate(R.layout.wed_toolbar_booking_view, getToolbarView(), false);
      if (!com.dianping.util.TextUtils.isEmpty(this.btnText))
        ((ToolbarButton)localObject2).setTitle(this.btnText);
      if (!isWeddingType())
        break label285;
      ((ToolbarButton)localObject2).setOnClickListener((WedBookingDialog.OnWedBookingDialogClickListener)localObject1);
    }
    while (true)
    {
      ((ToolbarButton)localObject2).setGAString("actionbar_wedbooking");
      getToolbarView().setBackgroundColor(getResources().getColor(R.color.white));
      addToolbarButton((View)localObject2, "5Booking");
      this.initBookingView = true;
      if ((getShop() == null) || (this.telephoneButton != null))
        break;
      localObject1 = getShop().getStringArray("PhoneNos");
      localObject2 = new StringBuffer();
      if ((localObject1 != null) && (localObject1.length > 0))
      {
        int i = 0;
        int j = localObject1.length;
        while (true)
          if (i < j)
          {
            ((StringBuffer)localObject2).append(localObject1[i]);
            if (i != localObject1.length - 1)
              ((StringBuffer)localObject2).append("，");
            i += 1;
            continue;
            label285: ((ToolbarButton)localObject2).setOnClickListener(this);
            break;
          }
      }
      localObject1 = new View.OnClickListener(localObject1)
      {
        public void onClick(View paramView)
        {
          if ((this.val$phoneNos != null) && (this.val$phoneNos.length > 0))
          {
            if (this.val$phoneNos.length != 1)
              break label123;
            TelephoneUtils.dial(WeddingToolbarAgent.this.getContext(), WeddingToolbarAgent.this.getShop(), this.val$phoneNos[0]);
            if (WeddingToolbarAgent.this.isWeddingType())
            {
              paramView = new ArrayList();
              paramView.add(new BasicNameValuePair("shopid", WeddingToolbarAgent.this.shopId() + ""));
              WeddingToolbarAgent.this.statisticsEvent("shopinfow", "shopinfow_tel", "", 0, paramView);
            }
          }
          return;
          label123: paramView = new String[this.val$phoneNos.length];
          int i = 0;
          while (i < this.val$phoneNos.length)
          {
            paramView[i] = ("拨打电话: " + this.val$phoneNos[i]);
            i += 1;
          }
          AlertDialog.Builder localBuilder = new AlertDialog.Builder(WeddingToolbarAgent.this.getContext());
          localBuilder.setTitle("联系商户").setItems(paramView, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              TelephoneUtils.dial(WeddingToolbarAgent.this.getContext(), WeddingToolbarAgent.this.getShop(), WeddingToolbarAgent.2.this.val$phoneNos[paramInt]);
              if (WeddingToolbarAgent.this.isWeddingType())
              {
                paramDialogInterface = new ArrayList();
                paramDialogInterface.add(new BasicNameValuePair("shopid", WeddingToolbarAgent.this.shopId() + ""));
                WeddingToolbarAgent.this.statisticsEvent("shopinfow", "shopinfow_tel", "", 0, paramDialogInterface);
              }
              if (WeddingToolbarAgent.this.isHomeDesignShopType())
              {
                paramDialogInterface = new ArrayList();
                paramDialogInterface.add(new BasicNameValuePair("shopid", WeddingToolbarAgent.this.shopId() + ""));
                WeddingToolbarAgent.this.statisticsEvent("shopinfoh", "shopinfoh_tel", "", 1, paramDialogInterface);
              }
              if (WeddingToolbarAgent.this.isHomeMarketShopType())
              {
                paramDialogInterface = new ArrayList();
                paramDialogInterface.add(new BasicNameValuePair("shopid", WeddingToolbarAgent.this.shopId() + ""));
                WeddingToolbarAgent.this.statisticsEvent("shopinfoh", "shopinfoh_tel", "", 2, paramDialogInterface);
              }
            }
          });
          localBuilder.create().show();
        }
      };
      this.telephoneButton = ((ToolbarButton)LayoutInflater.from(getContext()).inflate(R.layout.wed_toolbar_button, getToolbarView(), false));
      ((ToolbarImageButton)this.telephoneButton.findViewById(16908294)).setImageResource(R.drawable.wed_shopinfo_tel);
      ((TextView)this.telephoneButton.findViewById(16908308)).setText("咨询");
      this.telephoneButton.setOnClickListener((WedBookingDialog.OnWedBookingDialogClickListener)localObject1);
      this.telephoneButton.setGAString("actionbar_tel");
      addToolbarButton(this.telephoneButton, "4Telephone");
      initWedReviewButton(this.cooperateType);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((paramBundle != null) && (paramBundle.containsKey("promo")))
      this.promoObject = ((DPObject)paramBundle.getParcelable("promo"));
    label183: label212: 
    while (true)
    {
      return;
      if ((paramBundle != null) && (paramBundle.containsKey("history")))
      {
        this.historyObject = ((DPObject)paramBundle.getParcelable("history"));
        return;
      }
      if ((paramBundle != null) && (paramBundle.containsKey("excellent")))
      {
        this.excellentObject = ((DPObject)paramBundle.getParcelable("excellent"));
        return;
      }
      if ((paramBundle != null) && (paramBundle.containsKey("shop")))
      {
        if (!isWeddingType())
          break label183;
        this.weddingHotelExtra = ((DPObject)paramBundle.getParcelable("shop"));
        if ((this.weddingHotelExtra == null) || (!isWeddingType()));
      }
      for (this.cooperateType = this.weddingHotelExtra.getInt("CooperateType"); ; this.cooperateType = this.shopInfo.getInt("CooperateType"))
      {
        if (this.cooperateType != 1)
          break label212;
        initWedToolbar();
        sendChatRequest();
        if (this.chatObj == null)
          break;
        addChatButton();
        return;
        this.shopInfo = ((DPObject)paramBundle.getParcelable("shop"));
      }
    }
  }

  public void onClick(View paramView)
  {
    if (this.wedBookingDialog == null)
    {
      this.wedBookingDialog = new WedBookingDialog(getContext());
      this.wedBookingDialog.setOnWedBookingDialogClickListener(this);
    }
    if ((this.excellentObject != null) && (this.excellentObject.getArray("Properties") != null) && (this.excellentObject.getArray("Properties").length > 0))
      this.wedBookingDialog.initExcellentDialog(this.btnText, this.excellentObject, this.historyObject);
    while (true)
    {
      this.wedBookingDialog.show();
      return;
      paramView = null;
      if (this.promoObject != null)
        paramView = this.promoObject.getArray("WeddingPromoList");
      this.wedBookingDialog.initDialog(this.btnText, paramView, this.historyObject);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    super.onRequestFailed(paramMApiRequest, paramMApiResponse);
    if (paramMApiRequest == this.bookingRequest)
    {
      this.bookingRequest = null;
      dismissDialog();
      if ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!android.text.TextUtils.isEmpty(paramMApiResponse.message().toString())))
      {
        paramMApiRequest = Toast.makeText(getContext(), paramMApiResponse.message().toString(), 1);
        paramMApiRequest.setGravity(17, 0, 0);
        paramMApiRequest.show();
      }
    }
    do
    {
      return;
      paramMApiRequest = Toast.makeText(getContext(), "网络不给力啊，请稍后再试试", 1);
      paramMApiRequest.setGravity(17, 0, 0);
      paramMApiRequest.show();
      return;
    }
    while (paramMApiRequest != this.chatRequest);
    this.chatRequest = null;
    this.chatObj = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    super.onRequestFinish(paramMApiRequest, paramMApiResponse);
    if (paramMApiRequest == this.bookingRequest)
    {
      this.bookingRequest = null;
      dismissDialog();
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
        switch (paramMApiRequest.getInt("Flag"))
        {
        default:
        case 501:
        case 200:
        }
    }
    do
      while (true)
      {
        return;
        paramMApiRequest = Uri.parse("dianping://babyverifyphone").buildUpon();
        paramMApiRequest.appendQueryParameter("phonenum", this.phoneNum);
        paramMApiRequest.appendQueryParameter("shopid", shopId() + "");
        startActivity(new Intent("android.intent.action.VIEW", paramMApiRequest.build()));
        return;
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
      }
    while (this.chatRequest != paramMApiRequest);
    this.chatObj = ((DPObject)paramMApiResponse.result());
    dispatchAgentChanged(false);
  }

  public void onWedBookingDialogClick(String paramString)
  {
    sendBookingRequest(paramString);
  }

  void sendBookingRequest(String paramString)
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

  void sendChatRequest()
  {
    if (this.chatRequest != null);
    do
      return;
    while (shopId() <= 0);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/realcomentrance.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    if (isLogined())
      localBuilder.appendQueryParameter("token", accountService().token());
    this.chatRequest = mapiGet(this, localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.chatRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingToolbarAgent
 * JD-Core Version:    0.6.0
 */