package com.dianping.shopinfo.car;

import android.app.Dialog;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;

public class ParkInfoAgent extends ShopCellAgent
{
  private static final String API_URL = "http://mapi.dianping.com/car/shop/parkinfo.car?";
  private static final String CELL_CAR_PARK_INFO = "0300ParkInfo.carPark";
  private static final String TAG = ParkInfoAgent.class.getSimpleName();
  protected DPObject mParkingInfo;
  protected View mParkingView;
  protected MApiRequest mRequest;
  protected Dialog popup;

  public ParkInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void buildDialog(ParkInfoHolder paramParkInfoHolder)
  {
    if (this.popup == null)
      createNewPopup();
    if (!TextUtils.isEmpty(paramParkInfoHolder.remindDesc))
      ((TextView)this.popup.findViewById(R.id.car_parkinfo_popup_tips)).setText(paramParkInfoHolder.remindDesc);
    if (!TextUtils.isEmpty(paramParkInfoHolder.providerInfo))
      ((TextView)this.popup.findViewById(R.id.car_parkinfo_popup_provider)).setText(paramParkInfoHolder.providerInfo);
  }

  private void createNewPopup()
  {
    this.popup = new Dialog(getContext());
    this.popup.requestWindowFeature(1);
    this.popup.setContentView(R.layout.car_park_info_popup);
    this.popup.getWindow().setBackgroundDrawableResource(R.color.transparent);
    this.popup.getWindow().setLayout(-1, -2);
    this.popup.getWindow().setGravity(1);
    this.popup.setCanceledOnTouchOutside(true);
  }

  private ParkInfoHolder createParkInfoHolder()
  {
    ParkInfoHolder localParkInfoHolder = new ParkInfoHolder();
    if (this.mParkingInfo.getInt("IsShowParkInfo") != 0);
    for (int i = 1; i == 0; i = 0)
      return null;
    localParkInfoHolder.remindDesc = this.mParkingInfo.getString("RemindDesc");
    localParkInfoHolder.providerInfo = this.mParkingInfo.getString("ProviderInfo");
    localParkInfoHolder.priceInfo = this.mParkingInfo.getString("PriceInfo");
    localParkInfoHolder.priceUnit = this.mParkingInfo.getString("PriceUnit");
    localParkInfoHolder.price = this.mParkingInfo.getString("Price");
    localParkInfoHolder.distance = this.mParkingInfo.getString("Distance");
    localParkInfoHolder.leftPark = this.mParkingInfo.getInt("LeftPark");
    localParkInfoHolder.totalPark = this.mParkingInfo.getInt("TotalPark");
    return localParkInfoHolder;
  }

  private View createParkInfoView(ParkInfoHolder paramParkInfoHolder)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shop_car_parkinfo_cell, getParentView(), false);
    int i;
    int j;
    if (paramParkInfoHolder.leftPark >= 0)
    {
      i = 1;
      if (paramParkInfoHolder.totalPark <= 0)
        break label603;
      j = 1;
      label44: if ((i == 0) && (j == 0))
        break label609;
      String str3 = paramParkInfoHolder.leftPark + "";
      Object localObject = "";
      String str1 = "";
      if (i != 0)
      {
        String str2 = String.format("%s个车位可用", new Object[] { Integer.valueOf(paramParkInfoHolder.leftPark) });
        localObject = str2;
        if (j != 0)
          localObject = str2 + "，";
      }
      if (j != 0)
        str1 = String.format("共%s个", new Object[] { Integer.valueOf(paramParkInfoHolder.totalPark) });
      localObject = new SpannableString(String.format("车位：%s%s", new Object[] { localObject, str1 }));
      if (i != 0)
        ((SpannableString)localObject).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 3, str3.length() + 3, 33);
      ((TextView)localNovaLinearLayout.findViewById(R.id.parkinfo_content_leftpark)).setText((CharSequence)localObject);
      label240: if (TextUtils.isEmpty(paramParkInfoHolder.distance))
        break label625;
      ((TextView)localNovaLinearLayout.findViewById(R.id.parkinfo_content_distance)).setText(String.format("距离：%s", new Object[] { paramParkInfoHolder.distance }));
      label281: if (TextUtils.isEmpty(paramParkInfoHolder.price))
        break label641;
      i = 1;
      label294: if (TextUtils.isEmpty(paramParkInfoHolder.priceUnit))
        break label647;
      j = 1;
      label307: if ((i == 0) && (j == 0))
        break label653;
      if (paramParkInfoHolder.price == null)
        paramParkInfoHolder.price = "";
      if (paramParkInfoHolder.priceUnit == null)
        paramParkInfoHolder.priceUnit = "";
      localObject = new SpannableString(String.format("价格：%s%s", new Object[] { paramParkInfoHolder.price, paramParkInfoHolder.priceUnit }));
      if (j != 0)
        ((SpannableString)localObject).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 3, paramParkInfoHolder.price.length() + 3, 33);
      ((TextView)localNovaLinearLayout.findViewById(R.id.parkinfo_content_price)).setText((CharSequence)localObject);
      label428: if ((TextUtils.isEmpty(paramParkInfoHolder.priceInfo)) || ((paramParkInfoHolder.priceInfo == "免费") && (paramParkInfoHolder.price == "免费")))
        break label669;
      i = 1;
      label461: if (i == 0)
        break label675;
      ((TextView)localNovaLinearLayout.findViewById(R.id.parkinfo_content_message)).setText("收费描述：" + paramParkInfoHolder.priceInfo);
    }
    while (true)
    {
      if ((TextUtils.isEmpty(paramParkInfoHolder.remindDesc)) && (TextUtils.isEmpty(paramParkInfoHolder.providerInfo)))
        localNovaLinearLayout.findViewById(R.id.car_parkinfo_title_icon).setVisibility(8);
      localNovaLinearLayout.setTag(paramParkInfoHolder);
      localNovaLinearLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = paramView.getTag();
          if (paramView == null);
          do
          {
            do
              return;
            while (!(paramView instanceof ParkInfoAgent.ParkInfoHolder));
            paramView = (ParkInfoAgent.ParkInfoHolder)paramView;
          }
          while ((TextUtils.isEmpty(paramView.remindDesc)) && (TextUtils.isEmpty(paramView.providerInfo)));
          ParkInfoAgent.this.buildDialog(paramView);
          ParkInfoAgent.this.popup.show();
          paramView = new GAUserInfo();
          paramView.shop_id = Integer.valueOf(ParkInfoAgent.this.shopId());
          GAHelper.instance().contextStatisticsEvent(ParkInfoAgent.this.getContext(), "shopinfo_car_park_info_tips", paramView, "view");
        }
      });
      localNovaLinearLayout.gaUserInfo.shop_id = Integer.valueOf(shopId());
      localNovaLinearLayout.setGAString("shopinfo_car_park_info");
      ((DPActivity)getFragment().getActivity()).addGAView(localNovaLinearLayout, -1);
      return localNovaLinearLayout;
      i = 0;
      break;
      label603: j = 0;
      break label44;
      label609: localNovaLinearLayout.findViewById(R.id.parkinfo_content_leftpark).setVisibility(8);
      break label240;
      label625: localNovaLinearLayout.findViewById(R.id.parkinfo_content_distance).setVisibility(8);
      break label281;
      label641: i = 0;
      break label294;
      label647: j = 0;
      break label307;
      label653: localNovaLinearLayout.findViewById(R.id.parkinfo_content_price).setVisibility(8);
      break label428;
      label669: i = 0;
      break label461;
      label675: localNovaLinearLayout.findViewById(R.id.parkinfo_content_message).setVisibility(8);
    }
  }

  private boolean paramIsValid()
  {
    if (getShop() == null)
      Log.e(TAG, "Null shop data. Can not update shop info.");
    do
      return false;
    while (shopId() <= 0);
    return true;
  }

  private void sendRequest()
  {
    double d1 = 0.0D;
    double d2 = 0.0D;
    Object localObject = location();
    if (localObject != null)
    {
      d1 = ((Location)localObject).latitude();
      d2 = ((Location)localObject).longitude();
    }
    localObject = Uri.parse("http://mapi.dianping.com/car/shop/parkinfo.car?").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("shopid", shopId() + "");
    ((Uri.Builder)localObject).appendQueryParameter("lat", d1 + "");
    ((Uri.Builder)localObject).appendQueryParameter("lng", d2 + "");
    this.mRequest = BasicMApiRequest.mapiGet(((Uri.Builder)localObject).build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        ParkInfoAgent.this.mRequest = null;
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (ParkInfoAgent.this.mRequest != paramMApiRequest);
        do
        {
          return;
          ParkInfoAgent.this.mParkingInfo = ((DPObject)paramMApiResponse.result());
        }
        while (ParkInfoAgent.this.mParkingInfo == null);
        ParkInfoAgent.this.dispatchAgentChanged(false);
      }
    });
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (this.mParkingView != null);
    do
    {
      do
        return;
      while (this.mParkingInfo == null);
      super.onAgentChanged(paramBundle);
      paramBundle = createParkInfoHolder();
    }
    while (paramBundle == null);
    this.mParkingView = createParkInfoView(paramBundle);
    addCell("0300ParkInfo.carPark", this.mParkingView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!paramIsValid())
      return;
    sendRequest();
  }

  class ParkInfoHolder
  {
    protected String distance;
    protected int leftPark;
    protected String price;
    protected String priceInfo;
    protected String priceUnit;
    protected String providerInfo;
    protected String remindDesc;
    protected int totalPark;

    ParkInfoHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.car.ParkInfoAgent
 * JD-Core Version:    0.6.0
 */