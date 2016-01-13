package com.dianping.shopinfo.common;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.GPSCoordinate;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class PayBookingTAEntryAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_INDEX_WEDDING = "0250Basic.10Takeaway";
  private static final String TA_URL = "http://waimai.api.dianping.com/shopdetailwm.ta";
  private String CELL_INDEX = "0200Basic.40Takeaway";
  protected DPObject bookConfig;
  protected DPObject bookContext;
  protected MApiRequest bookingContextReq;
  String bookingTips;
  private BookingBroadCastReceiver broadcastReceiver;
  int businessType;
  int entrySum = 0;
  MApiRequest getPayPromosReq;
  MApiRequest getQueueSummaryReq;
  private View.OnClickListener gotoOnlineBookingListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      int i = paramView.getId();
      if (i == R.id.booking_cell)
      {
        PayBookingTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_booking", PayBookingTAEntryAgent.this.getEventLabelByFromeType(PayBookingTAEntryAgent.this.ordersource), PayBookingTAEntryAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(PayBookingTAEntryAgent.this.getContext(), "reserve", PayBookingTAEntryAgent.this.getEventLabelByFromeType(PayBookingTAEntryAgent.this.ordersource), PayBookingTAEntryAgent.this.ordersource, "tap");
      }
      while (true)
      {
        PayBookingTAEntryAgent.this.gotoBooking();
        return;
        if (i != R.id.bussniss_cell)
          continue;
        PayBookingTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_booking", PayBookingTAEntryAgent.this.getEventLabelByFromeType(PayBookingTAEntryAgent.this.ordersource), PayBookingTAEntryAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(PayBookingTAEntryAgent.this.getContext(), "reserve", PayBookingTAEntryAgent.this.getEventLabelByFromeType(PayBookingTAEntryAgent.this.ordersource), PayBookingTAEntryAgent.this.ordersource, "tap");
      }
    }
  };
  private View.OnClickListener gotoPayListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(new BasicNameValuePair("shopId", Integer.toString(PayBookingTAEntryAgent.this.shopId())));
      int i = paramView.getId();
      if (i == R.id.queue_cell)
        PayBookingTAEntryAgent.this.statisticsEvent("hui7", "hui7_coupon_pay", "2", PayBookingTAEntryAgent.this.businessType, localArrayList);
      while (true)
      {
        PayBookingTAEntryAgent.this.gotoPay();
        return;
        if (i != R.id.bussniss_cell)
          continue;
        PayBookingTAEntryAgent.this.statisticsEvent("hui7", "hui7_coupon_pay", "2", PayBookingTAEntryAgent.this.businessType, localArrayList);
      }
    }
  };
  private View.OnClickListener gotoQueueListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      int i = paramView.getId();
      if (i == R.id.queue_cell)
      {
        PayBookingTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_queue", "", PayBookingTAEntryAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(PayBookingTAEntryAgent.this.getContext(), "queue", "shopinfo", PayBookingTAEntryAgent.this.shopId(), "tap");
      }
      while (true)
      {
        PayBookingTAEntryAgent.this.gotoQueue();
        return;
        if (i != R.id.bussniss_cell)
          continue;
        PayBookingTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_queue", "", PayBookingTAEntryAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(PayBookingTAEntryAgent.this.getContext(), "queue", "shopinfo", PayBookingTAEntryAgent.this.shopId(), "tap");
      }
    }
  };
  private View.OnClickListener gotoTAListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      PayBookingTAEntryAgent.this.gotoTA();
    }
  };
  private String[] gps;
  private boolean mFullOpened;
  private boolean mHalfOpened;
  private DPObject mTakeawayObj;
  private MApiRequest mTakeawayReq;
  int ordersource;
  DPObject payPromos;
  int payType;
  int preferBookingNum;
  long preferCalTimeMills;
  private QueueBroadCastReceiver queueBroadcastReceiver;
  DPObject queueSummaryObj;
  int shopId;
  private LinearLayout shopinfoCellContainer;
  ShopinfoEntryShowProperty[] shopinfoEntryShowProperties = new ShopinfoEntryShowProperty[4];

  public PayBookingTAEntryAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createCellView(ShopinfoEntryShowProperty[] paramArrayOfShopinfoEntryShowProperty)
  {
    super.removeAllCells();
    this.shopinfoCellContainer = ((LinearLayout)this.res.inflate(getContext(), R.layout.hui_pay_ta_booking_entry, getParentView(), false));
    switch (countEntrySum(paramArrayOfShopinfoEntryShowProperty))
    {
    default:
    case 1:
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      return this.shopinfoCellContainer;
      this.shopinfoCellContainer.addView(createSingleEntry(paramArrayOfShopinfoEntryShowProperty));
      this.shopinfoCellContainer.addView(createDividerLine(false));
      continue;
      createEvenEntry(paramArrayOfShopinfoEntryShowProperty, this.shopinfoCellContainer);
      continue;
      createEvenEntry(paramArrayOfShopinfoEntryShowProperty, this.shopinfoCellContainer);
      this.shopinfoCellContainer.addView(createSingleEntry(paramArrayOfShopinfoEntryShowProperty));
      this.shopinfoCellContainer.addView(createDividerLine(false));
      continue;
      createEvenEntry(paramArrayOfShopinfoEntryShowProperty, this.shopinfoCellContainer);
    }
  }

  private View createDividerLine(boolean paramBoolean)
  {
    View localView = new View(super.getContext());
    localView.setBackgroundColor(super.getResources().getColor(R.color.inner_divider));
    if (paramBoolean)
    {
      localView.setLayoutParams(new LinearLayout.LayoutParams(1, -1));
      return localView;
    }
    localView.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
    return localView;
  }

  private LinearLayout createEvenEntry(ShopinfoEntryShowProperty[] paramArrayOfShopinfoEntryShowProperty, LinearLayout paramLinearLayout)
  {
    int n = countEntrySum(paramArrayOfShopinfoEntryShowProperty) / 2;
    int j = 0;
    if (j < n)
    {
      LinearLayout localLinearLayout = createLinearLayout();
      int m = 0;
      int k = 0;
      while (true)
      {
        int i;
        if (k < paramArrayOfShopinfoEntryShowProperty.length)
        {
          i = m;
          if (paramArrayOfShopinfoEntryShowProperty[k].isShow)
            switch (k)
            {
            default:
              i = m;
            case 0:
            case 1:
            case 2:
            case 3:
            }
        }
        while (true)
        {
          if (i != (j + 1) * 2)
            break label664;
          paramLinearLayout.addView(localLinearLayout);
          paramLinearLayout.addView(createDividerLine(false));
          j += 1;
          break;
          NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.hui_shopinfo_cell_pay, getParentView(), false);
          localNovaLinearLayout.setOnClickListener(this.gotoPayListener);
          TextView localTextView = (TextView)localNovaLinearLayout.findViewById(R.id.pay_rebate);
          if (this.payPromos != null)
          {
            localObject = this.payPromos.getString("Title");
            if (!TextUtils.isEmpty((CharSequence)localObject))
            {
              localTextView.setText((CharSequence)localObject);
              localTextView.setVisibility(0);
            }
          }
          while (true)
          {
            i = m + 1;
            paramArrayOfShopinfoEntryShowProperty[0].isShow = false;
            localLinearLayout.addView(localNovaLinearLayout);
            break;
            localTextView.setVisibility(8);
            continue;
            localTextView.setVisibility(8);
          }
          localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.hui_shopinfo_cell_queue, getParentView(), false);
          localTextView = (TextView)localNovaLinearLayout.findViewById(R.id.queue_text);
          Object localObject = (TextView)localNovaLinearLayout.findViewById(R.id.queue_tips);
          String str1;
          if (this.queueSummaryObj != null)
          {
            str1 = this.queueSummaryObj.getString("ButtonText");
            String str2 = this.queueSummaryObj.getString("ShortMsg");
            if (!TextUtils.isEmpty(str1))
              localTextView.setText(str1);
            if (TextUtils.isEmpty(str2))
              break label397;
            ((TextView)localObject).setText(str2);
            ((TextView)localObject).setVisibility(0);
          }
          while (true)
          {
            localNovaLinearLayout.setOnClickListener(this.gotoQueueListener);
            i = m + 1;
            paramArrayOfShopinfoEntryShowProperty[1].isShow = false;
            localLinearLayout.addView(localNovaLinearLayout);
            break;
            label397: ((TextView)localObject).setVisibility(8);
          }
          localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.hui_shopinfo_cell_booking, getParentView(), false);
          localNovaLinearLayout.setOnClickListener(this.gotoOnlineBookingListener);
          localObject = (TextView)localNovaLinearLayout.findViewById(R.id.booking_text);
          localTextView = (TextView)localNovaLinearLayout.findViewById(R.id.booking_rebate);
          if (this.bookContext != null)
          {
            if (this.payType == 20)
              ((TextView)localObject).setText("订座买单");
            localObject = this.bookContext.getArray("IconList");
            if (localObject != null)
            {
              int i1 = localObject.length;
              i = 0;
              if (i < i1)
              {
                str1 = localObject[i];
                if (str1.getInt("Type") == 30)
                {
                  localTextView.setText(str1.getString("Content"));
                  localTextView.setVisibility(0);
                }
                while (true)
                {
                  i += 1;
                  break;
                  localTextView.setVisibility(8);
                }
              }
            }
            else
            {
              localTextView.setVisibility(8);
            }
          }
          i = m + 1;
          paramArrayOfShopinfoEntryShowProperty[2].isShow = false;
          localLinearLayout.addView(localNovaLinearLayout);
          continue;
          localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.hui_shopinfo_cell_takeaway, getParentView(), false);
          localNovaLinearLayout.setOnClickListener(this.gotoTAListener);
          i = m + 1;
          paramArrayOfShopinfoEntryShowProperty[3].isShow = false;
          localLinearLayout.addView(localNovaLinearLayout);
        }
        label664: k += 1;
        m = i;
      }
    }
    return (LinearLayout)paramLinearLayout;
  }

  private LinearLayout createLinearLayout()
  {
    LinearLayout localLinearLayout = new LinearLayout(super.getContext());
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1.0F));
    localLinearLayout.setOrientation(0);
    localLinearLayout.setGravity(17);
    return localLinearLayout;
  }

  private LinearLayout createSingleEntry(ShopinfoEntryShowProperty[] paramArrayOfShopinfoEntryShowProperty)
  {
    LinearLayout localLinearLayout = createLinearLayout();
    RelativeLayout localRelativeLayout = (RelativeLayout)this.res.inflate(getContext(), R.layout.hui_shopinfo_cell_single_bussiness, getParentView(), false);
    Object localObject4 = (ImageView)localRelativeLayout.findViewById(R.id.business_icon);
    Object localObject3 = (TextView)localRelativeLayout.findViewById(R.id.business_text);
    Object localObject1 = (TextView)localRelativeLayout.findViewById(R.id.ic_off);
    Object localObject2 = (TextView)localRelativeLayout.findViewById(R.id.promo_text);
    int i = paramArrayOfShopinfoEntryShowProperty.length - 1;
    while (true)
    {
      if (i >= 0)
      {
        if (!paramArrayOfShopinfoEntryShowProperty[i].isShow)
          break label731;
        switch (i)
        {
        default:
        case 0:
        case 1:
        case 2:
        case 3:
        }
      }
      while (true)
      {
        localLinearLayout.addView(localRelativeLayout);
        return localLinearLayout;
        ((ImageView)localObject4).setImageResource(R.drawable.detail_payicon);
        ((TextView)localObject3).setText("买单");
        localRelativeLayout.setOnClickListener(this.gotoPayListener);
        if (this.payPromos == null)
          continue;
        paramArrayOfShopinfoEntryShowProperty = this.payPromos.getString("Title");
        if (!TextUtils.isEmpty(paramArrayOfShopinfoEntryShowProperty))
        {
          ((TextView)localObject1).setText(paramArrayOfShopinfoEntryShowProperty);
          ((TextView)localObject1).setVisibility(0);
          continue;
        }
        ((TextView)localObject1).setVisibility(8);
        continue;
        ((ImageView)localObject4).setImageResource(R.drawable.queue_shopinfo_icon);
        ((TextView)localObject3).setText("排号");
        localRelativeLayout.setOnClickListener(this.gotoQueueListener);
        if (this.queueSummaryObj == null)
          continue;
        paramArrayOfShopinfoEntryShowProperty = this.queueSummaryObj.getString("ButtonText");
        localObject1 = this.queueSummaryObj.getString("Msg");
        if (!TextUtils.isEmpty(paramArrayOfShopinfoEntryShowProperty))
          ((TextView)localObject3).setText(paramArrayOfShopinfoEntryShowProperty);
        if (!TextUtils.isEmpty((CharSequence)localObject1))
        {
          ((TextView)localObject2).setText((CharSequence)localObject1);
          ((TextView)localObject2).setVisibility(0);
          continue;
        }
        ((TextView)localObject2).setVisibility(8);
        continue;
        ((ImageView)localObject4).setImageResource(R.drawable.icon_reservation);
        ((TextView)localObject3).setText("订座");
        localRelativeLayout.setOnClickListener(this.gotoOnlineBookingListener);
        paramArrayOfShopinfoEntryShowProperty = (TextView)localRelativeLayout.findViewById(R.id.hot_tag);
        if (this.bookContext == null)
          continue;
        localObject4 = this.bookContext.getString("BookingTips");
        if (!TextUtils.isEmpty((CharSequence)localObject4))
          ((TextView)localObject3).setText((CharSequence)localObject4);
        boolean bool = this.bookContext.getBoolean("DefaultBookingTimeHot");
        if (bool)
        {
          i = 0;
          paramArrayOfShopinfoEntryShowProperty.setVisibility(i);
          if (this.bookConfig != null)
          {
            i = this.bookConfig.getInt("ShopOrderCount");
            if (i > 0)
            {
              ((TextView)localObject2).setText(i + "人订过");
              ((TextView)localObject2).setVisibility(0);
            }
          }
          localObject2 = this.bookContext.getArray("IconList");
          if (localObject2 == null)
            break label587;
          int j = localObject2.length;
          i = 0;
          label485: if (i >= j)
            continue;
          localObject3 = localObject2[i];
          if (((DPObject)localObject3).getInt("Type") != 30)
            break label577;
          localObject3 = ((DPObject)localObject3).getString("Content");
          ((TextView)localObject1).setText((CharSequence)localObject3);
          ((TextView)localObject1).setVisibility(0);
          if (bool)
            ((TextView)localObject1).getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener((TextView)localObject1, (String)localObject3, paramArrayOfShopinfoEntryShowProperty)
            {
              public boolean onPreDraw()
              {
                this.val$rebateText.getViewTreeObserver().removeOnPreDrawListener(this);
                float f = PayBookingTAEntryAgent.this.getHotTagWidth(this.val$rebateText, this.val$content);
                if (this.val$rebateText.getWidth() < f)
                  this.val$hotTag.setVisibility(8);
                return false;
              }
            });
        }
        while (true)
        {
          i += 1;
          break label485;
          i = 8;
          break;
          label577: ((TextView)localObject1).setVisibility(8);
        }
        label587: ((TextView)localObject1).setVisibility(8);
        continue;
        ((ImageView)localObject4).setImageResource(R.drawable.icon_dilivery);
        ((TextView)localObject3).setText("外卖");
        localRelativeLayout.setOnClickListener(this.gotoTAListener);
        if (this.mTakeawayObj != null)
        {
          ((TextView)localObject2).setText(this.mTakeawayObj.getString("Tips"));
          paramArrayOfShopinfoEntryShowProperty = this.mTakeawayObj.getObject("Activity");
          if (paramArrayOfShopinfoEntryShowProperty != null)
          {
            paramArrayOfShopinfoEntryShowProperty = paramArrayOfShopinfoEntryShowProperty.getString("Message");
            if (!TextUtils.isEmpty(paramArrayOfShopinfoEntryShowProperty))
            {
              ((TextView)localObject1).setText(paramArrayOfShopinfoEntryShowProperty);
              ((TextView)localObject1).setVisibility(0);
            }
          }
          while (true)
          {
            ((TextView)localObject2).setVisibility(0);
            break;
            ((TextView)localObject1).setVisibility(8);
            continue;
            ((TextView)localObject1).setVisibility(8);
          }
        }
        ((TextView)localObject2).setVisibility(8);
        ((TextView)localObject1).setVisibility(8);
      }
      label731: i -= 1;
    }
  }

  private void getCityInfo()
  {
    if (getCity() == null);
    int i;
    do
    {
      return;
      i = getCity().flag();
    }
    while ((0x200000 & i) == 0);
    if ((0x400000 & i) == 0)
    {
      this.mHalfOpened = true;
      return;
    }
    this.mFullOpened = true;
  }

  private String getEventLabelByFromeType(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return "";
    case -1:
      return "others";
    case 0:
      return "channel_keyword";
    case 1:
      return "channel_search";
    case 2:
      return "channel_all";
    case 3:
      return "channel_scene";
    case 4:
    }
    return "channel_shoplist";
  }

  private float getHotTagWidth(TextView paramTextView, String paramString)
  {
    float f = paramTextView.getPaint().measureText(paramString);
    return ViewUtils.dip2px(getContext(), 7.0F) + f;
  }

  private void getPayPromos()
  {
    if (super.getFragment() == null)
      return;
    if (this.getPayPromosReq != null)
      super.getFragment().mapiService().abort(this.getPayPromosReq, this, true);
    this.getPayPromosReq = BasicMApiRequest.mapiGet(Uri.parse("http://hui.api.dianping.com/getpaypromos.hui").buildUpon().appendQueryParameter("shopid", String.valueOf(this.shopId)).appendQueryParameter("clientuuid", Environment.uuid()).appendQueryParameter("promostring", getShopExtraParam()).build().toString(), CacheType.DISABLED);
    super.getFragment().mapiService().exec(this.getPayPromosReq, this);
  }

  private void gotoOnlineBooking()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(String.format("dianping://onlinebooking?shopid=%s&bookingdate=%s&bookingpersonnum=%s&ordersource=%s", new Object[] { Integer.valueOf(super.shopId()), Long.valueOf(this.preferCalTimeMills), Integer.valueOf(this.preferBookingNum), Integer.valueOf(this.ordersource) })));
    if (super.getShop() != null)
      localIntent.putExtra("shop", super.getShop());
    if (this.bookConfig != null)
      localIntent.putExtra("config", this.bookConfig);
    super.getFragment().startActivity(localIntent);
  }

  private void initEntrySwitch()
  {
    this.shopId = shopId();
    if (super.getFragment() == null);
    boolean bool1;
    do
    {
      return;
      this.shopinfoEntryShowProperties[0] = new ShopinfoEntryShowProperty(false, 0);
      this.shopinfoEntryShowProperties[1] = new ShopinfoEntryShowProperty(false, 0);
      this.shopinfoEntryShowProperties[2] = new ShopinfoEntryShowProperty(false, 0);
      this.shopinfoEntryShowProperties[3] = new ShopinfoEntryShowProperty(false, 0);
      bool1 = ConfigHelper.enableMOPay;
      bool2 = ConfigHelper.enableYY;
    }
    while (super.getShop() == null);
    boolean bool3;
    if ((bool1) && (super.getShop().getBoolean("HasPay")))
    {
      bool1 = true;
      bool3 = super.getShop().getBoolean("IsQueueable");
      if ((!bool2) || (!super.getShop().getBoolean("Bookable")))
        break label276;
    }
    label276: for (boolean bool2 = true; ; bool2 = false)
    {
      boolean bool4 = super.getShop().getBoolean("HasTakeaway");
      if (bool1)
      {
        getPayPromos();
        this.shopinfoEntryShowProperties[0].reqSendStatus = 1;
      }
      if (bool3)
      {
        reqQueueSummary();
        this.shopinfoEntryShowProperties[1].reqSendStatus = 1;
      }
      if (bool2)
      {
        reqBookingContext();
        this.shopinfoEntryShowProperties[2].reqSendStatus = 1;
      }
      if (bool4)
      {
        reqTakeaway();
        this.shopinfoEntryShowProperties[3].reqSendStatus = 1;
      }
      this.shopinfoEntryShowProperties[0].isShow = bool1;
      this.shopinfoEntryShowProperties[1].isShow = bool3;
      this.shopinfoEntryShowProperties[2].isShow = bool2;
      this.shopinfoEntryShowProperties[3].isShow = bool4;
      return;
      bool1 = false;
      break;
    }
  }

  private void reqTakeaway()
  {
    if (super.getFragment() == null)
      return;
    if (this.mTakeawayReq != null)
      super.getFragment().mapiService().abort(this.mTakeawayReq, this, true);
    int j = shopId();
    int i = -1;
    if (super.getShop() != null)
      i = super.getShop().getInt("CityID");
    Uri.Builder localBuilder = Uri.parse("http://waimai.api.dianping.com/shopdetailwm.ta").buildUpon().appendQueryParameter("shopid", String.valueOf(j)).appendQueryParameter("cityid", String.valueOf(i));
    if ((!this.mHalfOpened) && (!this.mFullOpened))
      getCityInfo();
    if ((this.mHalfOpened) && (this.gps != null))
    {
      localBuilder.appendQueryParameter("lat", this.gps[0]);
      localBuilder.appendQueryParameter("lng", this.gps[1]);
    }
    while (true)
    {
      this.mTakeawayReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
      super.getFragment().mapiService().exec(this.mTakeawayReq, this);
      return;
      if (!this.mFullOpened)
        continue;
      GPSCoordinate localGPSCoordinate = super.getFragment().locationService().realCoordinate();
      if (localGPSCoordinate == null)
        continue;
      localBuilder.appendQueryParameter("lat", localGPSCoordinate.latitudeString());
      localBuilder.appendQueryParameter("lng", localGPSCoordinate.longitudeString());
    }
  }

  int countEntrySum(ShopinfoEntryShowProperty[] paramArrayOfShopinfoEntryShowProperty)
  {
    int j = 0;
    int i = 0;
    while (i < paramArrayOfShopinfoEntryShowProperty.length)
    {
      int k = j;
      if (paramArrayOfShopinfoEntryShowProperty[i].isShow)
        k = j + 1;
      i += 1;
      j = k;
    }
    return j;
  }

  String getShopName()
  {
    Object localObject = super.getShop();
    String str = ((DPObject)localObject).getString("BranchName");
    localObject = new StringBuilder().append(((DPObject)localObject).getString("Name"));
    if (TextUtils.isEmpty(str));
    for (str = ""; ; str = "(" + str + ")")
      return str;
  }

  void gotoBooking()
  {
    if (this.bookContext == null);
    for (int i = 0; this.payType == 20; i = this.bookContext.getInt("BookCount"))
    {
      statisticsEvent("shopinfo6", "shopinfo6_bookingpay", "", 0);
      new AlertDialog.Builder(getContext()).setTitle(this.bookingTips).setPositiveButton("立刻买单", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          PayBookingTAEntryAgent.this.statisticsEvent("shopinfo6", "shopinfo6_bookingpay_pay", "", 0);
          paramDialogInterface = new Intent("android.intent.action.VIEW", Uri.parse(String.format("dianping://wxalipay?fromtype=2&shopid=%s&shopname=%s&source=%s&&orderid=%s&channel=20", new Object[] { Integer.valueOf(PayBookingTAEntryAgent.this.shopId()), PayBookingTAEntryAgent.this.getShopName(), Integer.valueOf(11), Integer.valueOf(PayBookingTAEntryAgent.this.bookContext.getInt("PayRecordID")) })));
          PayBookingTAEntryAgent.this.getFragment().startActivity(paramDialogInterface);
        }
      }).setNegativeButton("再次订座", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          PayBookingTAEntryAgent.this.gotoOnlineBooking();
          PayBookingTAEntryAgent.this.statisticsEvent("shopinfo6", "shopinfo6_bookingpay_booking", "", 0);
        }
      }).show();
      return;
    }
    if (i > 0)
    {
      Object localObject = new AlertDialog.Builder(getContext());
      ((AlertDialog.Builder)localObject).setTitle("订单信息");
      ((AlertDialog.Builder)localObject).setMessage("您已经提交过" + i + "张订单，是否再次订座?");
      ((AlertDialog.Builder)localObject).setPositiveButton("查看订单", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          PayBookingTAEntryAgent.this.getFragment().startActivity("dianping://bookinglist");
          PayBookingTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_booking_orderlist", "", 0);
        }
      });
      ((AlertDialog.Builder)localObject).setNegativeButton("再次订座", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          PayBookingTAEntryAgent.this.gotoOnlineBooking();
          PayBookingTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_booking_bookingpage", "", 0);
        }
      });
      localObject = ((AlertDialog.Builder)localObject).create();
      ((AlertDialog)localObject).show();
      ((AlertDialog)localObject).setCanceledOnTouchOutside(true);
      return;
    }
    gotoOnlineBooking();
  }

  void gotoPay()
  {
    if ((super.getFragment() != null) && (this.payPromos != null))
    {
      localObject = this.payPromos.getString("UniCashierUrl");
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        localObject = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
        super.getFragment().startActivity((Intent)localObject);
      }
    }
    else
    {
      return;
    }
    Object localObject = super.getShop();
    if ((super.getFragment() != null) && (localObject != null))
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://wxalipay"));
      ((Intent)localObject).putExtra("shopid", this.shopId);
      ((Intent)localObject).putExtra("shopname", getShopName());
      ((Intent)localObject).putExtra("source", 11);
      super.getFragment().startActivity((Intent)localObject);
      return;
    }
    Log.e("huihui", "gotoPay fail");
  }

  void gotoQueue()
  {
    Object localObject = super.getShop();
    if ((super.getFragment() != null) && (localObject != null))
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse(String.format("dianping://queuemain?shopid=%s", new Object[] { Integer.valueOf(this.shopId) })));
      super.getFragment().startActivity((Intent)localObject);
      return;
    }
    Log.e("queue", "gotoQueue fail");
  }

  void gotoTA()
  {
    Object localObject1 = "0.0";
    Object localObject2 = "0.0";
    Object localObject3;
    if (this.mFullOpened)
    {
      localObject3 = getFragment().locationService().realCoordinate();
      if (localObject3 != null)
      {
        localObject1 = ((GPSCoordinate)localObject3).latitudeString();
        localObject2 = ((GPSCoordinate)localObject3).longitudeString();
      }
    }
    while (true)
    {
      localObject3 = new ArrayList();
      BasicNameValuePair localBasicNameValuePair = new BasicNameValuePair("shopid", String.valueOf(shopId()));
      localObject1 = new BasicNameValuePair("lat", (String)localObject1);
      localObject2 = new BasicNameValuePair("lng", (String)localObject2);
      ((List)localObject3).add(localBasicNameValuePair);
      ((List)localObject3).add(localObject1);
      ((List)localObject3).add(localObject2);
      statisticsEvent("shopinfo6", "shopinfo6_takeaway", null, 0, (List)localObject3);
      GAHelper.instance().contextStatisticsEvent(getContext(), "takeout", "shopinfo", shopId(), "tap");
      localObject1 = Uri.parse("dianping://takeawaydishlist").buildUpon();
      ((Uri.Builder)localObject1).appendQueryParameter("shopid", String.valueOf(shopId()));
      ((Uri.Builder)localObject1).appendQueryParameter("shopname", getShop().getString("Name"));
      ((Uri.Builder)localObject1).appendQueryParameter("source", String.valueOf(3));
      if (!this.mHalfOpened)
        break;
      int i = 0;
      if (this.gps != null)
      {
        ((Uri.Builder)localObject1).appendQueryParameter("tab", String.valueOf(0));
        localObject1 = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject1).build());
        ((Intent)localObject1).putExtra("lat", this.gps[0]);
        ((Intent)localObject1).putExtra("lng", this.gps[1]);
        ((Intent)localObject1).putExtra("address", this.gps[2]);
        ((Intent)localObject1).putExtra("shopID", shopId());
        getFragment().startActivity((Intent)localObject1);
        i = 1;
      }
      if (i == 0)
      {
        localObject1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawaychooseregion?source=1&shopid=" + shopId()));
        getFragment().startActivity((Intent)localObject1);
      }
      return;
      if (this.gps == null)
        continue;
      localObject1 = this.gps[0];
      localObject2 = this.gps[1];
    }
    ((Uri.Builder)localObject1).appendQueryParameter("tab", String.valueOf(1));
    localObject1 = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject1).build());
    getFragment().startActivity((Intent)localObject1);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    super.removeAllCells();
    this.entrySum = countEntrySum(this.shopinfoEntryShowProperties);
    boolean bool5;
    boolean bool1;
    if (this.entrySum > 0)
    {
      if (isWeddingShopType())
        super.addCell("0250Basic.10Takeaway", createCellView(this.shopinfoEntryShowProperties), 0);
    }
    else if (super.getShopStatus() == 0)
    {
      boolean bool3 = super.getShop().getBoolean("HasPay");
      boolean bool2 = super.getShop().getBoolean("IsQueueable");
      boolean bool4 = super.getShop().getBoolean("Bookable");
      bool5 = super.getShop().getBoolean("HasTakeaway");
      if ((bool3) && (this.shopinfoEntryShowProperties[0].reqSendStatus == 0))
      {
        getPayPromos();
        this.shopinfoEntryShowProperties[0].reqSendStatus = 1;
      }
      if ((bool2) && (this.shopinfoEntryShowProperties[1].reqSendStatus == 0))
      {
        reqQueueSummary();
        this.shopinfoEntryShowProperties[1].reqSendStatus = 1;
      }
      if ((bool4) && (this.shopinfoEntryShowProperties[2].reqSendStatus == 0))
      {
        reqBookingContext();
        this.shopinfoEntryShowProperties[2].reqSendStatus = 1;
      }
      if ((bool5) && (this.shopinfoEntryShowProperties[3].reqSendStatus == 0))
      {
        reqTakeaway();
        this.shopinfoEntryShowProperties[3].reqSendStatus = 1;
      }
      paramBundle = this.shopinfoEntryShowProperties[0];
      bool1 = bool3;
      if (this.shopinfoEntryShowProperties[0].reqSendStatus == 2)
      {
        if ((!bool3) || ((this.payPromos != null) && (this.payPromos.getInt("ShowStatus") != 10)))
          break label465;
        bool1 = true;
      }
      label279: paramBundle.isShow = bool1;
      paramBundle = this.shopinfoEntryShowProperties[1];
      bool1 = bool2;
      if (this.shopinfoEntryShowProperties[1].reqSendStatus == 2)
      {
        if ((!bool2) || ((this.queueSummaryObj != null) && (!this.queueSummaryObj.getBoolean("Enabled"))))
          break label470;
        bool1 = true;
      }
      label332: paramBundle.isShow = bool1;
      this.shopinfoEntryShowProperties[2].isShow = bool4;
      if (this.shopinfoEntryShowProperties[3].reqSendStatus != 2)
        break label475;
      this.shopinfoEntryShowProperties[3].isShow = bool5;
    }
    while (true)
    {
      paramBundle = new LinearLayout(getContext());
      paramBundle.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      paramBundle.setOrientation(1);
      if (countEntrySum(this.shopinfoEntryShowProperties) <= 0)
        break label526;
      paramBundle.addView((NovaLinearLayout)createCellView(this.shopinfoEntryShowProperties));
      if (!isWeddingShopType())
        break label515;
      super.addCell("0250Basic.10Takeaway", paramBundle, 0);
      return;
      super.addCell(this.CELL_INDEX, createCellView(this.shopinfoEntryShowProperties), 0);
      break;
      label465: bool1 = false;
      break label279;
      label470: bool1 = false;
      break label332;
      label475: if (this.shopinfoEntryShowProperties[3].reqSendStatus == 3)
      {
        this.shopinfoEntryShowProperties[3].isShow = false;
        continue;
      }
      this.shopinfoEntryShowProperties[3].isShow = bool5;
    }
    label515: super.addCell(this.CELL_INDEX, paramBundle, 0);
    return;
    label526: super.removeAllCells();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.payPromos = ((DPObject)paramBundle.getParcelable("payPromos"));
      this.bookContext = ((DPObject)paramBundle.getParcelable("bookContext"));
      this.mTakeawayObj = ((DPObject)paramBundle.getParcelable("takeawayObj"));
      this.queueSummaryObj = ((DPObject)paramBundle.getParcelable("queueSummaryObj"));
    }
    paramBundle = getFragment().getActivity().getIntent();
    if (paramBundle != null)
    {
      this.preferCalTimeMills = paramBundle.getLongExtra("bookingdate", -1L);
      this.preferBookingNum = paramBundle.getIntExtra("bookingpersonnum", -1);
      this.ordersource = paramBundle.getIntExtra("ordersource", -1);
    }
    initEntrySwitch();
    this.broadcastReceiver = new BookingBroadCastReceiver();
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.booking.BOOKING_INFO_CHANGE");
    getContext().registerReceiver(this.broadcastReceiver, paramBundle);
    this.queueBroadcastReceiver = new QueueBroadCastReceiver();
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.queue.QUEUE_STATE_REFRESH");
    getContext().registerReceiver(this.queueBroadcastReceiver, paramBundle);
  }

  public void onDestroy()
  {
    if (this.broadcastReceiver != null)
      getContext().unregisterReceiver(this.broadcastReceiver);
    if (this.queueBroadcastReceiver != null)
      getContext().unregisterReceiver(this.queueBroadcastReceiver);
    if (this.bookingContextReq != null)
    {
      super.getFragment().mapiService().abort(this.bookingContextReq, this, true);
      this.bookingContextReq = null;
    }
    if (this.mTakeawayReq != null)
    {
      super.getFragment().mapiService().abort(this.mTakeawayReq, this, true);
      this.mTakeawayReq = null;
    }
    if (this.getPayPromosReq != null)
    {
      super.getFragment().mapiService().abort(this.getPayPromosReq, this, true);
      this.getPayPromosReq = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.bookingContextReq)
    {
      this.bookingContextReq = null;
      this.shopinfoEntryShowProperties[2].reqSendStatus = 3;
    }
    do
    {
      return;
      if (paramMApiRequest == this.mTakeawayReq)
      {
        this.mTakeawayReq = null;
        this.mTakeawayObj = null;
        this.shopinfoEntryShowProperties[3].reqSendStatus = 3;
        super.dispatchAgentChanged(false);
        return;
      }
      if (paramMApiRequest != this.getPayPromosReq)
        continue;
      this.getPayPromosReq = null;
      this.shopinfoEntryShowProperties[0].reqSendStatus = 3;
      return;
    }
    while (paramMApiRequest != this.getQueueSummaryReq);
    this.getQueueSummaryReq = null;
    this.shopinfoEntryShowProperties[1].reqSendStatus = 3;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    int i;
    if (paramMApiRequest == this.bookingContextReq)
    {
      this.bookingContextReq = null;
      this.bookContext = ((DPObject)paramMApiResponse.result());
      if (this.bookContext != null)
        this.bookConfig = this.bookContext.getObject("BookingConfig");
      if (this.bookContext == null)
      {
        i = 0;
        this.payType = i;
        if (this.bookContext != null)
          break label107;
        paramMApiRequest = "";
        label72: this.bookingTips = paramMApiRequest;
        this.shopinfoEntryShowProperties[2].reqSendStatus = 2;
        super.dispatchAgentChanged(false);
      }
    }
    label107: 
    do
    {
      return;
      i = this.bookContext.getInt("PayType");
      break;
      paramMApiRequest = this.bookContext.getString("BookingTips");
      break label72;
      if (paramMApiRequest == this.mTakeawayReq)
      {
        this.mTakeawayReq = null;
        this.mTakeawayObj = ((DPObject)paramMApiResponse.result());
        super.dispatchAgentChanged(false);
        this.shopinfoEntryShowProperties[3].reqSendStatus = 2;
        return;
      }
      if (paramMApiRequest != this.getPayPromosReq)
        continue;
      this.payPromos = ((DPObject)paramMApiResponse.result());
      this.shopinfoEntryShowProperties[0].reqSendStatus = 2;
      this.businessType = this.payPromos.getInt("BusinessType");
      super.dispatchAgentChanged(false);
      this.getPayPromosReq = null;
      return;
    }
    while (paramMApiRequest != this.getQueueSummaryReq);
    this.getQueueSummaryReq = null;
    this.queueSummaryObj = ((DPObject)paramMApiResponse.result());
    this.shopinfoEntryShowProperties[1].reqSendStatus = 2;
    super.dispatchAgentChanged(false);
  }

  public void reqBookingContext()
  {
    String str2 = "http://rs.api.dianping.com/getbookingcontext.yy?shopID=" + shopId() + "&clientUUID=" + Environment.uuid();
    AccountService localAccountService = super.getFragment().accountService();
    String str1 = str2;
    if (localAccountService != null)
    {
      str1 = str2;
      if (localAccountService.token() != null)
        str1 = str2 + "&token=" + localAccountService.token();
    }
    this.bookingContextReq = BasicMApiRequest.mapiGet(str1, CacheType.DISABLED);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (PayBookingTAEntryAgent.this.bookingContextReq != null)
          PayBookingTAEntryAgent.this.getFragment().mapiService().exec(PayBookingTAEntryAgent.this.bookingContextReq, PayBookingTAEntryAgent.this);
      }
    }
    , 100L);
  }

  void reqQueueSummary()
  {
    if (super.getFragment() == null)
      return;
    if (this.getQueueSummaryReq != null)
      super.getFragment().mapiService().abort(this.getQueueSummaryReq, this, true);
    this.getQueueSummaryReq = BasicMApiRequest.mapiGet(Uri.parse("http://mapi.dianping.com/queue/getshopsummary.qu?").buildUpon().appendQueryParameter("shopid", String.valueOf(this.shopId)).build().toString(), CacheType.DISABLED);
    super.getFragment().mapiService().exec(this.getQueueSummaryReq, this);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("payPromos", this.payPromos);
    localBundle.putParcelable("bookContext", this.bookContext);
    localBundle.putParcelable("takeawayObj", this.mTakeawayObj);
    localBundle.putParcelable("queueSummaryObj", this.queueSummaryObj);
    return localBundle;
  }

  public class BookingBroadCastReceiver extends BroadcastReceiver
  {
    public BookingBroadCastReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.booking.BOOKING_INFO_CHANGE"))
        PayBookingTAEntryAgent.this.bookConfig = ((DPObject)paramIntent.getParcelableExtra("bookinginfo"));
    }
  }

  public class QueueBroadCastReceiver extends BroadcastReceiver
  {
    public QueueBroadCastReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.queue.QUEUE_STATE_REFRESH"))
        PayBookingTAEntryAgent.this.reqQueueSummary();
    }
  }

  class ShopinfoEntryShowProperty
  {
    boolean isShow;
    int reqSendStatus;

    ShopinfoEntryShowProperty(boolean paramInt, int arg3)
    {
      this.isShow = paramInt;
      int i;
      this.reqSendStatus = i;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.PayBookingTAEntryAgent
 * JD-Core Version:    0.6.0
 */