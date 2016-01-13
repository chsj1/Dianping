package com.dianping.shopinfo.wed.weddingfeast;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class WeddingFeastBookingAgent extends ShopCellAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_WEDDING_BOOKING_FEIHEZUO = "0250Basic.09WeddingBooking";
  private static final String CELL_WEDDING_BOOKING_HEZUO = "0200Basic.30WeddingBooking";
  View cell;
  private boolean isStatic;
  DPObject mWeddingHotelExtra;
  String phoneStr = "";
  MApiRequest request;
  private boolean send = false;

  public WeddingFeastBookingAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createBookingAgent()
  {
    View localView1 = this.res.inflate(getContext(), R.layout.shop_info_wedding_booking_item, getParentView(), false);
    Object localObject = getShop().getStringArray("PhoneNos");
    if ((localObject != null) && (localObject.length > 0) && ((isWeddingType()) || (isWeddingShopType())))
      this.phoneStr = localObject[0];
    localObject = localView1.findViewById(R.id.phone);
    ((NovaRelativeLayout)localObject).setGAString("tel");
    if (isWeddingType())
      ((NovaRelativeLayout)localObject).setGAString("tel_main");
    TextView localTextView = (TextView)localView1.findViewById(R.id.phone_num);
    View localView2 = localView1.findViewById(R.id.booking_text);
    View localView3 = localView1.findViewById(R.id.indicator);
    if (this.mWeddingHotelExtra.getInt("CooperateType") != 1)
    {
      ((View)localObject).setVisibility(0);
      localTextView.setVisibility(0);
      localView2.setVisibility(0);
      localView3.setVisibility(0);
    }
    while (true)
    {
      localTextView.setText(this.phoneStr);
      ((View)localObject).setOnClickListener(this);
      return localView1;
      ((View)localObject).setVisibility(0);
      localTextView.setVisibility(0);
      localView2.setVisibility(0);
      localView3.setVisibility(0);
    }
  }

  private void enterBookingPage()
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://weddinghotelbooking").buildUpon().appendQueryParameter("shopid", String.valueOf(getShop().getInt("ID"))).appendQueryParameter("shopname", DPObjectUtils.getShopFullName(getShop())).build().toString())));
  }

  private void sendRequest()
  {
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/weddinghotelextra.bin?");
    localStringBuffer.append("shopid=").append(shopId());
    this.request = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public View getView()
  {
    return this.cell;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!isWeddingType());
    while (true)
    {
      return;
      if (this.mWeddingHotelExtra != null)
        break;
      if (this.send)
        continue;
      sendRequest();
      this.send = true;
      return;
    }
    this.cell = createBookingAgent();
    if (this.mWeddingHotelExtra.getBoolean("Commision"))
    {
      addCell("0200Basic.30WeddingBooking", this.cell);
      return;
    }
    addCell("0250Basic.09WeddingBooking", this.cell);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.phone)
    {
      paramView = getShop();
      if (paramView != null)
        break label20;
    }
    label20: 
    do
      return;
    while ((this.phoneStr == null) || ("".equals(this.phoneStr)));
    new View.OnClickListener(paramView)
    {
      public void onClick(View paramView)
      {
        TelephoneUtils.dial(WeddingFeastBookingAgent.this.getContext(), this.val$shop, WeddingFeastBookingAgent.this.phoneStr);
      }
    }
    .onClick(null);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (shopId() <= 0);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this.request != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.request, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    dispatchAgentChanged(false);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.mWeddingHotelExtra = ((DPObject)paramMApiResponse.result());
    if ((this.mWeddingHotelExtra != null) && (!this.isStatic))
    {
      if (this.mWeddingHotelExtra.getInt("CooperateType") == 1)
        break label242;
      paramMApiRequest = new ArrayList();
      paramMApiRequest.add(new BasicNameValuePair("shopid", shopId() + ""));
      statisticsEvent("shopinfow", "shopinfow_nonpaid", "", 0, paramMApiRequest);
    }
    while (true)
    {
      this.isStatic = true;
      setSharedObject("WeddingHotelExtra", this.mWeddingHotelExtra);
      dispatchAgentChanged(false);
      dispatchAgentChanged("shopinfo/common_oldhead", null);
      dispatchAgentChanged("shopinfo/common_address", null);
      dispatchAgentChanged("shopinfo/wedhotel_promo", null);
      dispatchAgentChanged("shopinfo/wedhotel_fuzzyrecommend", null);
      dispatchAgentChanged("shopinfo/wedhotel_hall", null);
      dispatchAgentChanged("shopinfo/wedhotel_menu", null);
      dispatchAgentChanged("shopinfo/weddingfeastotherrecommend", null);
      dispatchAgentChanged("shopinfo/wedhotel_nearby", null);
      dispatchAgentChanged("shopinfo/wedhotel_toolbar", null);
      paramMApiRequest = new Bundle();
      paramMApiRequest.putParcelable("shop", this.mWeddingHotelExtra);
      dispatchAgentChanged("shopinfo/wed_cpc", paramMApiRequest);
      dispatchAgentChanged("shopinfo/wed_unco_cpc", paramMApiRequest);
      dispatchAgentChanged("shopinfo/wed_toolbar", paramMApiRequest);
      return;
      label242: paramMApiRequest = new ArrayList();
      paramMApiRequest.add(new BasicNameValuePair("shopid", shopId() + ""));
      statisticsEvent("shopinfow", "shopinfow_paid", "", 0, paramMApiRequest);
    }
  }

  public Bundle saveInstanceState()
  {
    return super.saveInstanceState();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.weddingfeast.WeddingFeastBookingAgent
 * JD-Core Version:    0.6.0
 */