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
import com.dianping.model.Location;
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
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;

public class OrderBookQueueTAEntryAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_INDEX_WEDDING = "0250Basic.10Takeaway";
  private static final String TA_URL = "http://waimai.api.dianping.com/shopdetailwm.ta";
  private String CELL_INDEX = "0200Basic.40Takeaway";
  protected DPObject bookConfig;
  protected DPObject bookContext;
  protected MApiRequest bookingContextReq;
  boolean bookingFlag = false;
  String bookingTips = "";
  private BookingBroadCastReceiver broadcastReceiver;
  DPObject dishOrder;
  boolean enableBooking;
  boolean enableDD;
  int entrySum = 0;
  MApiRequest getDishOrderReq;
  MApiRequest getQueueSummaryReq;
  private View.OnClickListener gotoDDBookingListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      int i = paramView.getId();
      if (i == R.id.booking_cell)
      {
        OrderBookQueueTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_booking", OrderBookQueueTAEntryAgent.this.getEventLabelByFromeType(OrderBookQueueTAEntryAgent.this.ordersource), OrderBookQueueTAEntryAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(OrderBookQueueTAEntryAgent.this.getContext(), "reserve", OrderBookQueueTAEntryAgent.this.getEventLabelByFromeType(OrderBookQueueTAEntryAgent.this.ordersource), OrderBookQueueTAEntryAgent.this.ordersource, "tap");
      }
      while (true)
      {
        OrderBookQueueTAEntryAgent.this.gotoDDBooking();
        return;
        if (i != R.id.bussniss_cell)
          continue;
        OrderBookQueueTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_booking", OrderBookQueueTAEntryAgent.this.getEventLabelByFromeType(OrderBookQueueTAEntryAgent.this.ordersource), OrderBookQueueTAEntryAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(OrderBookQueueTAEntryAgent.this.getContext(), "reserve", OrderBookQueueTAEntryAgent.this.getEventLabelByFromeType(OrderBookQueueTAEntryAgent.this.ordersource), OrderBookQueueTAEntryAgent.this.ordersource, "tap");
      }
    }
  };
  private View.OnClickListener gotoDishOrderListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      OrderBookQueueTAEntryAgent.this.gotoDishOrder();
    }
  };
  private View.OnClickListener gotoOnlineBookingListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      int i = paramView.getId();
      if (i == R.id.booking_cell)
      {
        OrderBookQueueTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_booking", OrderBookQueueTAEntryAgent.this.getEventLabelByFromeType(OrderBookQueueTAEntryAgent.this.ordersource), OrderBookQueueTAEntryAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(OrderBookQueueTAEntryAgent.this.getContext(), "reserve", OrderBookQueueTAEntryAgent.this.getEventLabelByFromeType(OrderBookQueueTAEntryAgent.this.ordersource), OrderBookQueueTAEntryAgent.this.ordersource, "tap");
      }
      while (true)
      {
        OrderBookQueueTAEntryAgent.this.gotoBooking();
        return;
        if (i != R.id.bussniss_cell)
          continue;
        OrderBookQueueTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_booking", OrderBookQueueTAEntryAgent.this.getEventLabelByFromeType(OrderBookQueueTAEntryAgent.this.ordersource), OrderBookQueueTAEntryAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(OrderBookQueueTAEntryAgent.this.getContext(), "reserve", OrderBookQueueTAEntryAgent.this.getEventLabelByFromeType(OrderBookQueueTAEntryAgent.this.ordersource), OrderBookQueueTAEntryAgent.this.ordersource, "tap");
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
        OrderBookQueueTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_queue", "", OrderBookQueueTAEntryAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(OrderBookQueueTAEntryAgent.this.getContext(), "queue", "shopinfo", OrderBookQueueTAEntryAgent.this.shopId(), "tap");
      }
      while (true)
      {
        OrderBookQueueTAEntryAgent.this.gotoQueue();
        return;
        if (i != R.id.bussniss_cell)
          continue;
        OrderBookQueueTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_queue", "", OrderBookQueueTAEntryAgent.this.shopId());
        GAHelper.instance().contextStatisticsEvent(OrderBookQueueTAEntryAgent.this.getContext(), "queue", "shopinfo", OrderBookQueueTAEntryAgent.this.shopId(), "tap");
      }
    }
  };
  private View.OnClickListener gotoTAListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      OrderBookQueueTAEntryAgent.this.gotoTA();
    }
  };
  private String[] gps;
  private boolean mFullOpened;
  private boolean mHalfOpened;
  private DPObject mTakeawayObj;
  private MApiRequest mTakeawayReq;
  boolean orderFlag = false;
  int ordersource;
  int payType = 0;
  int preferBookingNum;
  long preferCalTimeMills;
  private QueueBroadCastReceiver queueBroadcastReceiver;
  DPObject queueSummaryObj;
  int shopId;
  private LinearLayout shopinfoCellContainer;
  ShopinfoEntryShowProperty[] shopinfoEntryShowProperties = new ShopinfoEntryShowProperty[4];
  boolean takeawayFlag = false;
  String[] tipInfos = { "", "", "", "" };

  public OrderBookQueueTAEntryAgent(Object paramObject)
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
      createTwoEntry(paramArrayOfShopinfoEntryShowProperty, this.shopinfoCellContainer);
      continue;
      createThreeEntry(paramArrayOfShopinfoEntryShowProperty, this.shopinfoCellContainer);
      continue;
      createFourEntry(paramArrayOfShopinfoEntryShowProperty, this.shopinfoCellContainer);
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

  private LinearLayout createFourEntry(ShopinfoEntryShowProperty[] paramArrayOfShopinfoEntryShowProperty, LinearLayout paramLinearLayout)
  {
    LinearLayout localLinearLayout = createLinearLayout();
    int i = 0;
    if (i < paramArrayOfShopinfoEntryShowProperty.length)
    {
      NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.hui_shopinfo_cell_four_bussiness, getParentView(), false);
      Object localObject2 = (ImageView)localNovaRelativeLayout.findViewById(R.id.business_icon);
      Object localObject1 = (TextView)localNovaRelativeLayout.findViewById(R.id.business_text);
      TextView localTextView = (TextView)localNovaRelativeLayout.findViewById(R.id.ic_off);
      if (paramArrayOfShopinfoEntryShowProperty[i].isShow);
      switch (i)
      {
      default:
      case 0:
      case 1:
      case 2:
        while (true)
        {
          i += 1;
          break;
          ((ImageView)localObject2).setImageResource(R.drawable.detail_service_icon_order);
          ((TextView)localObject1).setText("点菜");
          if (this.dishOrder != null)
          {
            localObject2 = this.dishOrder.getString("Title");
            if (!TextUtils.isEmpty((CharSequence)localObject2))
              ((TextView)localObject1).setText((CharSequence)localObject2);
          }
          localNovaRelativeLayout.setOnClickListener(this.gotoDishOrderListener);
          localNovaRelativeLayout.setGAString("dishorder_ai", getGAExtra());
          if ((isShowTip(i)) && (this.dishOrder != null))
          {
            localObject1 = this.dishOrder.getString("Tips");
            if (!TextUtils.isEmpty((CharSequence)localObject1))
            {
              localTextView.setText((CharSequence)localObject1);
              localTextView.setVisibility(0);
            }
          }
          while (true)
          {
            localLinearLayout.addView(localNovaRelativeLayout);
            break;
            localTextView.setVisibility(8);
            continue;
            localTextView.setVisibility(8);
          }
          if ((this.enableDD == true) && (!this.enableBooking))
          {
            ((ImageView)localObject2).setImageResource(R.drawable.detail_service_icon_book);
            ((TextView)localObject1).setText("订座");
            localNovaRelativeLayout.setOnClickListener(this.gotoDDBookingListener);
            localNovaRelativeLayout.setGAString("reserve_ai", getGAExtra());
          }
          while (true)
          {
            localLinearLayout.addView(localNovaRelativeLayout);
            break;
            ((ImageView)localObject2).setImageResource(R.drawable.detail_service_icon_book);
            ((TextView)localObject1).setText("订座");
            localNovaRelativeLayout.setOnClickListener(this.gotoOnlineBookingListener);
            localNovaRelativeLayout.setGAString("reserve_ai", getGAExtra());
            if ((!isShowTip(i)) || (this.bookContext == null))
              continue;
            localObject1 = this.bookContext.getArray("IconList");
            if (localObject1 != null)
            {
              int k = localObject1.length;
              int j = 0;
              label417: if (j < k)
              {
                localObject2 = localObject1[j];
                if (((DPObject)localObject2).getInt("Type") != 30)
                  break label472;
                localTextView.setText(((DPObject)localObject2).getString("Content"));
                localTextView.setVisibility(0);
              }
              while (true)
              {
                j += 1;
                break label417;
                break;
                label472: localTextView.setVisibility(8);
              }
            }
            localTextView.setVisibility(8);
          }
          ((ImageView)localObject2).setImageResource(R.drawable.detail_service_icon_queue);
          ((TextView)localObject1).setText("排号");
          localNovaRelativeLayout.setOnClickListener(this.gotoQueueListener);
          localNovaRelativeLayout.setGAString("queue_ai", getGAExtra());
          localLinearLayout.addView(localNovaRelativeLayout);
        }
      case 3:
      }
      ((ImageView)localObject2).setImageResource(R.drawable.detail_service_icon_takeaway);
      ((TextView)localObject1).setText("外卖");
      localNovaRelativeLayout.setOnClickListener(this.gotoTAListener);
      localNovaRelativeLayout.setGAString("takeout_ai", getGAExtra());
      if ((isShowTip(i)) && (this.mTakeawayObj != null))
      {
        localObject1 = this.mTakeawayObj.getObject("Activity");
        if (localObject1 != null)
        {
          localObject1 = ((DPObject)localObject1).getString("Message");
          if (!TextUtils.isEmpty((CharSequence)localObject1))
          {
            localTextView.setText((CharSequence)localObject1);
            localTextView.setVisibility(0);
          }
        }
      }
      while (true)
      {
        localLinearLayout.addView(localNovaRelativeLayout);
        break;
        localTextView.setVisibility(8);
        continue;
        localTextView.setVisibility(8);
        continue;
        localTextView.setVisibility(8);
      }
    }
    paramLinearLayout.addView(localLinearLayout);
    return (LinearLayout)(LinearLayout)paramLinearLayout;
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
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.hui_shopinfo_cell_one_bussiness, getParentView(), false);
    ImageView localImageView = (ImageView)localNovaRelativeLayout.findViewById(R.id.business_icon);
    Object localObject3 = (TextView)localNovaRelativeLayout.findViewById(R.id.business_text);
    Object localObject1 = (TextView)localNovaRelativeLayout.findViewById(R.id.ic_off);
    Object localObject2 = (TextView)localNovaRelativeLayout.findViewById(R.id.promo_text);
    int i = paramArrayOfShopinfoEntryShowProperty.length - 1;
    while (true)
    {
      if (i >= 0)
      {
        if (!paramArrayOfShopinfoEntryShowProperty[i].isShow)
          break label844;
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
        localLinearLayout.addView(localNovaRelativeLayout);
        return localLinearLayout;
        localImageView.setImageResource(R.drawable.detail_service_icon_order);
        ((TextView)localObject3).setText("点菜");
        localNovaRelativeLayout.setOnClickListener(this.gotoDishOrderListener);
        localNovaRelativeLayout.setGAString("dishorder_ai", getGAExtra());
        if (this.dishOrder != null)
        {
          paramArrayOfShopinfoEntryShowProperty = this.dishOrder.getString("Title");
          if (!TextUtils.isEmpty(paramArrayOfShopinfoEntryShowProperty))
            ((TextView)localObject3).setText(paramArrayOfShopinfoEntryShowProperty);
          paramArrayOfShopinfoEntryShowProperty = this.dishOrder.getString("Tips");
          if (!TextUtils.isEmpty(paramArrayOfShopinfoEntryShowProperty))
          {
            ((TextView)localObject1).setText(paramArrayOfShopinfoEntryShowProperty);
            ((TextView)localObject1).setVisibility(0);
            continue;
          }
          ((TextView)localObject1).setVisibility(8);
          continue;
        }
        ((TextView)localObject1).setVisibility(8);
        continue;
        if ((this.enableDD == true) && (!this.enableBooking))
        {
          localImageView.setImageResource(R.drawable.detail_service_icon_book);
          ((TextView)localObject3).setText("订座");
          localNovaRelativeLayout.setOnClickListener(this.gotoDDBookingListener);
          localNovaRelativeLayout.setGAString("reserve_ai", getGAExtra());
          continue;
        }
        localImageView.setImageResource(R.drawable.detail_service_icon_book);
        ((TextView)localObject3).setText("订座");
        localNovaRelativeLayout.setOnClickListener(this.gotoOnlineBookingListener);
        localNovaRelativeLayout.setGAString("reserve_ai", getGAExtra());
        paramArrayOfShopinfoEntryShowProperty = (TextView)localNovaRelativeLayout.findViewById(R.id.hot_tag);
        if (this.bookContext == null)
          continue;
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
            break label577;
          int j = localObject2.length;
          i = 0;
          label467: if (i >= j)
            continue;
          localObject3 = localObject2[i];
          if (((DPObject)localObject3).getInt("Type") != 30)
            break label567;
          localObject3 = ((DPObject)localObject3).getString("Content");
          if (!TextUtils.isEmpty((CharSequence)localObject3))
          {
            ((TextView)localObject1).setText((CharSequence)localObject3);
            ((TextView)localObject1).setVisibility(0);
          }
          if (bool)
            ((TextView)localObject1).getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener((TextView)localObject1, (String)localObject3, paramArrayOfShopinfoEntryShowProperty)
            {
              public boolean onPreDraw()
              {
                this.val$rebateText.getViewTreeObserver().removeOnPreDrawListener(this);
                float f = OrderBookQueueTAEntryAgent.this.getHotTagWidth(this.val$rebateText, this.val$content);
                if (this.val$rebateText.getWidth() < f)
                  this.val$hotTag.setVisibility(8);
                return false;
              }
            });
        }
        while (true)
        {
          i += 1;
          break label467;
          i = 8;
          break;
          label567: ((TextView)localObject1).setVisibility(8);
        }
        label577: ((TextView)localObject1).setVisibility(8);
        continue;
        localImageView.setImageResource(R.drawable.detail_service_icon_queue);
        ((TextView)localObject3).setText("排号");
        localNovaRelativeLayout.setOnClickListener(this.gotoQueueListener);
        localNovaRelativeLayout.setGAString("queue_ai", getGAExtra());
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
        localImageView.setImageResource(R.drawable.detail_service_icon_takeaway);
        ((TextView)localObject3).setText("外卖");
        localNovaRelativeLayout.setOnClickListener(this.gotoTAListener);
        localNovaRelativeLayout.setGAString("takeout_ai", getGAExtra());
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
      label844: i -= 1;
    }
  }

  private LinearLayout createThreeEntry(ShopinfoEntryShowProperty[] paramArrayOfShopinfoEntryShowProperty, LinearLayout paramLinearLayout)
  {
    LinearLayout localLinearLayout = createLinearLayout();
    int i = 0;
    if (i < paramArrayOfShopinfoEntryShowProperty.length)
    {
      NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.hui_shopinfo_cell_three_bussiness, getParentView(), false);
      Object localObject2 = (ImageView)localNovaRelativeLayout.findViewById(R.id.business_icon);
      Object localObject1 = (TextView)localNovaRelativeLayout.findViewById(R.id.business_text);
      TextView localTextView = (TextView)localNovaRelativeLayout.findViewById(R.id.ic_off);
      if (paramArrayOfShopinfoEntryShowProperty[i].isShow);
      switch (i)
      {
      default:
      case 0:
      case 1:
      case 2:
        while (true)
        {
          i += 1;
          break;
          ((ImageView)localObject2).setImageResource(R.drawable.detail_service_icon_order);
          ((TextView)localObject1).setText("点菜");
          if (this.dishOrder != null)
          {
            localObject2 = this.dishOrder.getString("Title");
            if (!TextUtils.isEmpty((CharSequence)localObject2))
              ((TextView)localObject1).setText((CharSequence)localObject2);
          }
          localNovaRelativeLayout.setOnClickListener(this.gotoDishOrderListener);
          localNovaRelativeLayout.setGAString("dishorder_ai", getGAExtra());
          if ((isShowTip(i)) && (this.dishOrder != null))
          {
            localObject1 = this.dishOrder.getString("Tips");
            if (!TextUtils.isEmpty((CharSequence)localObject1))
            {
              localTextView.setText((CharSequence)localObject1);
              localTextView.setVisibility(0);
            }
          }
          while (true)
          {
            localLinearLayout.addView(localNovaRelativeLayout);
            break;
            localTextView.setVisibility(8);
            continue;
            localTextView.setVisibility(8);
          }
          if ((this.enableDD == true) && (!this.enableBooking))
          {
            ((ImageView)localObject2).setImageResource(R.drawable.detail_service_icon_book);
            ((TextView)localObject1).setText("订座");
            localNovaRelativeLayout.setOnClickListener(this.gotoDDBookingListener);
            localNovaRelativeLayout.setGAString("reserve_ai", getGAExtra());
          }
          while (true)
          {
            localLinearLayout.addView(localNovaRelativeLayout);
            break;
            ((ImageView)localObject2).setImageResource(R.drawable.detail_service_icon_book);
            ((TextView)localObject1).setText("订座");
            localNovaRelativeLayout.setOnClickListener(this.gotoOnlineBookingListener);
            localNovaRelativeLayout.setGAString("reserve_ai", getGAExtra());
            if ((!isShowTip(i)) || (this.bookContext == null))
              continue;
            localObject1 = this.bookContext.getArray("IconList");
            if (localObject1 != null)
            {
              int k = localObject1.length;
              int j = 0;
              label417: if (j < k)
              {
                localObject2 = localObject1[j];
                if (((DPObject)localObject2).getInt("Type") != 30)
                  break label472;
                localTextView.setText(((DPObject)localObject2).getString("Content"));
                localTextView.setVisibility(0);
              }
              while (true)
              {
                j += 1;
                break label417;
                break;
                label472: localTextView.setVisibility(8);
              }
            }
            localTextView.setVisibility(8);
          }
          ((ImageView)localObject2).setImageResource(R.drawable.detail_service_icon_queue);
          ((TextView)localObject1).setText("排号");
          localNovaRelativeLayout.setOnClickListener(this.gotoQueueListener);
          localNovaRelativeLayout.setGAString("queue_ai", getGAExtra());
          localLinearLayout.addView(localNovaRelativeLayout);
        }
      case 3:
      }
      ((ImageView)localObject2).setImageResource(R.drawable.detail_service_icon_takeaway);
      ((TextView)localObject1).setText("外卖");
      localNovaRelativeLayout.setOnClickListener(this.gotoTAListener);
      localNovaRelativeLayout.setGAString("takeout_ai", getGAExtra());
      if ((isShowTip(i)) && (this.mTakeawayObj != null))
      {
        localObject1 = this.mTakeawayObj.getObject("Activity");
        if (localObject1 != null)
        {
          localObject1 = ((DPObject)localObject1).getString("Message");
          if (!TextUtils.isEmpty((CharSequence)localObject1))
          {
            localTextView.setText((CharSequence)localObject1);
            localTextView.setVisibility(0);
          }
        }
      }
      while (true)
      {
        localLinearLayout.addView(localNovaRelativeLayout);
        break;
        localTextView.setVisibility(8);
        continue;
        localTextView.setVisibility(8);
        continue;
        localTextView.setVisibility(8);
      }
    }
    paramLinearLayout.addView(localLinearLayout);
    return (LinearLayout)(LinearLayout)paramLinearLayout;
  }

  private LinearLayout createTwoEntry(ShopinfoEntryShowProperty[] paramArrayOfShopinfoEntryShowProperty, LinearLayout paramLinearLayout)
  {
    LinearLayout localLinearLayout = createLinearLayout();
    int i = 0;
    while (i < paramArrayOfShopinfoEntryShowProperty.length)
    {
      NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.hui_shopinfo_cell_two_bussiness, getParentView(), false);
      ImageView localImageView = (ImageView)localNovaRelativeLayout.findViewById(R.id.business_icon);
      Object localObject2 = (TextView)localNovaRelativeLayout.findViewById(R.id.business_text);
      Object localObject1 = (TextView)localNovaRelativeLayout.findViewById(R.id.ic_off);
      Object localObject3 = (TextView)localNovaRelativeLayout.findViewById(R.id.promo_text);
      if (paramArrayOfShopinfoEntryShowProperty[i].isShow);
      switch (i)
      {
      default:
        i += 1;
        break;
      case 0:
        localImageView.setImageResource(R.drawable.detail_service_icon_order);
        ((TextView)localObject2).setText("点菜");
        if (this.dishOrder != null)
        {
          localObject3 = this.dishOrder.getString("Title");
          if (!TextUtils.isEmpty((CharSequence)localObject3))
            ((TextView)localObject2).setText((CharSequence)localObject3);
        }
        localNovaRelativeLayout.setOnClickListener(this.gotoDishOrderListener);
        localNovaRelativeLayout.setGAString("dishorder_ai", getGAExtra());
        if ((isShowTip(i)) && (this.dishOrder != null))
        {
          localObject2 = this.dishOrder.getString("Tips");
          if (!TextUtils.isEmpty((CharSequence)localObject2))
          {
            ((TextView)localObject1).setText((CharSequence)localObject2);
            ((TextView)localObject1).setVisibility(0);
          }
        }
        while (true)
        {
          localLinearLayout.addView(localNovaRelativeLayout);
          break;
          ((TextView)localObject1).setVisibility(8);
          continue;
          ((TextView)localObject1).setVisibility(8);
        }
      case 1:
        if ((this.enableDD == true) && (!this.enableBooking))
        {
          localImageView.setImageResource(R.drawable.detail_service_icon_book);
          ((TextView)localObject2).setText("订座");
          localNovaRelativeLayout.setOnClickListener(this.gotoDDBookingListener);
          localNovaRelativeLayout.setGAString("reserve_ai", getGAExtra());
        }
        while (true)
        {
          localLinearLayout.addView(localNovaRelativeLayout);
          break;
          localImageView.setImageResource(R.drawable.detail_service_icon_book);
          ((TextView)localObject2).setText("订座");
          localNovaRelativeLayout.setOnClickListener(this.gotoOnlineBookingListener);
          localNovaRelativeLayout.setGAString("reserve_ai", getGAExtra());
          if ((!isShowTip(i)) || (this.bookContext == null))
            continue;
          localObject2 = this.bookContext.getArray("IconList");
          if (localObject2 != null)
          {
            int k = localObject2.length;
            int j = 0;
            if (j >= k)
              continue;
            localObject3 = localObject2[j];
            if (((DPObject)localObject3).getInt("Type") == 30)
            {
              ((TextView)localObject1).setText(((DPObject)localObject3).getString("Content"));
              ((TextView)localObject1).setVisibility(0);
            }
            while (true)
            {
              j += 1;
              break;
              ((TextView)localObject1).setVisibility(8);
            }
          }
          ((TextView)localObject1).setVisibility(8);
        }
      case 2:
        localImageView.setImageResource(R.drawable.detail_service_icon_queue);
        ((TextView)localObject2).setText("排号");
        localNovaRelativeLayout.setOnClickListener(this.gotoQueueListener);
        localNovaRelativeLayout.setGAString("queue_ai", getGAExtra());
        if (this.queueSummaryObj != null)
        {
          this.queueSummaryObj.getString("ButtonText");
          localObject1 = this.queueSummaryObj.getString("ShortMsg");
          if (TextUtils.isEmpty((CharSequence)localObject1))
            break label601;
          ((TextView)localObject3).setText((CharSequence)localObject1);
          ((TextView)localObject3).setVisibility(0);
        }
        while (true)
        {
          localLinearLayout.addView(localNovaRelativeLayout);
          break;
          ((TextView)localObject3).setVisibility(8);
        }
      case 3:
        label601: localImageView.setImageResource(R.drawable.detail_service_icon_takeaway);
        ((TextView)localObject2).setText("外卖");
        localNovaRelativeLayout.setOnClickListener(this.gotoTAListener);
        localNovaRelativeLayout.setGAString("takeout_ai", getGAExtra());
        if ((isShowTip(i)) && (this.mTakeawayObj != null))
        {
          localObject2 = this.mTakeawayObj.getObject("Activity");
          if (localObject2 != null)
          {
            localObject2 = ((DPObject)localObject2).getString("Message");
            if (!TextUtils.isEmpty((CharSequence)localObject2))
            {
              ((TextView)localObject1).setText((CharSequence)localObject2);
              ((TextView)localObject1).setVisibility(0);
            }
          }
        }
        while (true)
        {
          localLinearLayout.addView(localNovaRelativeLayout);
          break;
          ((TextView)localObject1).setVisibility(8);
          continue;
          ((TextView)localObject1).setVisibility(8);
          continue;
          ((TextView)localObject1).setVisibility(8);
        }
      }
    }
    paramLinearLayout.addView(localLinearLayout);
    return (LinearLayout)(LinearLayout)(LinearLayout)paramLinearLayout;
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

  private String getURLScheme(int paramInt)
  {
    DPObject[] arrayOfDPObject = getShop().getArray("ShopServiceInfoDoList");
    if (arrayOfDPObject != null)
    {
      int i = 0;
      while (i < arrayOfDPObject.length)
      {
        DPObject localDPObject = arrayOfDPObject[i];
        if (localDPObject.getInt("Type") == paramInt)
          return localDPObject.getString("Scheme");
        i += 1;
      }
      return "";
    }
    return "";
  }

  private void gotoDDBooking()
  {
    Object localObject = getURLScheme(5);
    if ((!TextUtils.isEmpty((CharSequence)localObject)) && (super.getFragment() != null))
    {
      super.getFragment().startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)));
      return;
    }
    localObject = super.getShop();
    if ((super.getFragment() != null) && (localObject != null))
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web").buildUpon().appendQueryParameter("url", Uri.parse("http://m.dianping.com/dd/app/shopcall/shopCalledEnter").buildUpon().appendQueryParameter("shopid", String.valueOf(super.shopId())).appendQueryParameter("businesstype", "10").appendQueryParameter("newtoken", "!").toString()).build());
      super.getFragment().startActivity((Intent)localObject);
      return;
    }
    Log.e("dingding", "gotoDD fail");
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
    boolean bool1 = false;
    if (super.getFragment() == null);
    do
    {
      return;
      this.shopId = shopId();
      this.shopinfoEntryShowProperties[0] = new ShopinfoEntryShowProperty(false, 0);
      this.shopinfoEntryShowProperties[1] = new ShopinfoEntryShowProperty(false, 0);
      this.shopinfoEntryShowProperties[2] = new ShopinfoEntryShowProperty(false, 0);
      this.shopinfoEntryShowProperties[3] = new ShopinfoEntryShowProperty(false, 0);
      bool2 = ConfigHelper.enableYY;
    }
    while (super.getShop() == null);
    this.enableDD = super.getShop().getBoolean("DDBookable");
    this.enableBooking = super.getShop().getBoolean("Bookable");
    boolean bool4 = super.getShop().getBoolean("IsOrderDish");
    boolean bool2 = super.getShop().getBoolean("IsQueueable");
    boolean bool5 = super.getShop().getBoolean("Bookable");
    boolean bool3 = super.getShop().getBoolean("HasTakeaway");
    if (bool4)
    {
      this.orderFlag = bool4;
      getDishOrder();
      this.shopinfoEntryShowProperties[0].reqSendStatus = 1;
    }
    if (bool5)
    {
      this.bookingFlag = bool5;
      reqBookingContext();
      this.shopinfoEntryShowProperties[1].reqSendStatus = 1;
    }
    if (bool2)
    {
      reqQueueSummary();
      this.shopinfoEntryShowProperties[2].reqSendStatus = 1;
    }
    if (bool3)
    {
      this.takeawayFlag = bool3;
      reqTakeaway();
      this.shopinfoEntryShowProperties[3].reqSendStatus = 1;
    }
    this.shopinfoEntryShowProperties[0].isShow = bool4;
    ShopinfoEntryShowProperty localShopinfoEntryShowProperty = this.shopinfoEntryShowProperties[1];
    if ((bool5) || (this.enableDD))
      bool1 = true;
    localShopinfoEntryShowProperty.isShow = bool1;
    this.shopinfoEntryShowProperties[2].isShow = bool2;
    this.shopinfoEntryShowProperties[3].isShow = bool3;
  }

  private boolean isShowTip(int paramInt)
  {
    int i;
    if ((!this.bookingFlag) && (!this.orderFlag) && (!this.takeawayFlag) && (!TextUtils.isEmpty(this.tipInfos[paramInt])))
      i = 0;
    while (i < paramInt)
    {
      if (!TextUtils.isEmpty(this.tipInfos[i]))
        return false;
      i += 1;
    }
    return true;
  }

  private void reqTakeaway()
  {
    if (super.getFragment() == null)
      return;
    if (this.mTakeawayReq != null)
      super.getFragment().mapiService().abort(this.mTakeawayReq, this, true);
    int j = shopId();
    double d1 = 0.0D;
    double d2 = 0.0D;
    int i = -1;
    DPObject localDPObject = getFragment().locationService().location();
    if (localDPObject != null)
    {
      d1 = localDPObject.getDouble("Lat");
      d2 = localDPObject.getDouble("Lng");
      i = localDPObject.getObject("City").getInt("ID");
    }
    this.mTakeawayReq = BasicMApiRequest.mapiGet(Uri.parse("http://waimai.api.dianping.com/shopdetailwm.ta").buildUpon().appendQueryParameter("shopid", String.valueOf(j)).appendQueryParameter("cityid", String.valueOf(cityId())).appendQueryParameter("locatelng", Location.FMT.format(d2)).appendQueryParameter("locatelat", Location.FMT.format(d1)).appendQueryParameter("locatecityid", String.valueOf(i)).build().toString(), CacheType.DISABLED);
    super.getFragment().mapiService().exec(this.mTakeawayReq, this);
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

  void getDishOrder()
  {
    if (super.getFragment() == null)
      return;
    if (this.getDishOrderReq != null)
      super.getFragment().mapiService().abort(this.getDishOrderReq, this, true);
    this.getDishOrderReq = BasicMApiRequest.mapiGet(Uri.parse("http://mapi.dianping.com/hobbits/gethobbitentry.hbt?").buildUpon().appendQueryParameter("shopid", String.valueOf(this.shopId)).build().toString(), CacheType.DISABLED);
    super.getFragment().mapiService().exec(this.getDishOrderReq, this);
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
          OrderBookQueueTAEntryAgent.this.statisticsEvent("shopinfo6", "shopinfo6_bookingpay_pay", "", 0);
          paramDialogInterface = new Intent("android.intent.action.VIEW", Uri.parse(String.format("dianping://wxalipay?fomtype=2&shopid=%s&shopname=%s&source=%s&&orderid=%s&channel=20", new Object[] { Integer.valueOf(OrderBookQueueTAEntryAgent.this.shopId()), OrderBookQueueTAEntryAgent.this.getShopName(), Integer.valueOf(11), Integer.valueOf(OrderBookQueueTAEntryAgent.this.bookContext.getInt("PayRecordID")) })));
          OrderBookQueueTAEntryAgent.this.getFragment().startActivity(paramDialogInterface);
        }
      }).setNegativeButton("再次订座", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          OrderBookQueueTAEntryAgent.this.gotoOnlineBooking();
          OrderBookQueueTAEntryAgent.this.statisticsEvent("shopinfo6", "shopinfo6_bookingpay_booking", "", 0);
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
          OrderBookQueueTAEntryAgent.this.getFragment().startActivity("dianping://bookinglist");
          OrderBookQueueTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_booking_orderlist", "", 0);
        }
      });
      ((AlertDialog.Builder)localObject).setNegativeButton("再次订座", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          OrderBookQueueTAEntryAgent.this.gotoOnlineBooking();
          OrderBookQueueTAEntryAgent.this.statisticsEvent("shopinfo5", "shopinfo5_booking_bookingpage", "", 0);
        }
      });
      localObject = ((AlertDialog.Builder)localObject).create();
      ((AlertDialog)localObject).show();
      ((AlertDialog)localObject).setCanceledOnTouchOutside(true);
      return;
    }
    gotoOnlineBooking();
  }

  void gotoDishOrder()
  {
    Object localObject = getURLScheme(1);
    if ((!TextUtils.isEmpty((CharSequence)localObject)) && (super.getFragment() != null))
    {
      super.getFragment().startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)));
      return;
    }
    localObject = super.getShop();
    if ((super.getFragment() != null) && (localObject != null))
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse(String.format("dianping://selectdishmenu?shopid=%s", new Object[] { Integer.valueOf(this.shopId) })));
      super.getFragment().startActivity((Intent)localObject);
      return;
    }
    Log.e("order", "gotoDishOrder fail");
  }

  void gotoQueue()
  {
    Object localObject = getURLScheme(3);
    if ((!TextUtils.isEmpty((CharSequence)localObject)) && (super.getFragment() != null))
    {
      super.getFragment().startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)));
      return;
    }
    localObject = super.getShop();
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
    Object localObject = getURLScheme(2);
    if ((!TextUtils.isEmpty((CharSequence)localObject)) && (super.getFragment() != null))
      getFragment().startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)));
    do
    {
      return;
      localObject = Uri.parse("dianping://takeawaydishlist").buildUpon();
      ((Uri.Builder)localObject).appendQueryParameter("shopid", String.valueOf(shopId()));
      ((Uri.Builder)localObject).appendQueryParameter("shopname", getShop().getString("Name"));
      ((Uri.Builder)localObject).appendQueryParameter("source", String.valueOf(3));
      ((Uri.Builder)localObject).appendQueryParameter("tab", String.valueOf(1));
    }
    while (super.getFragment() == null);
    localObject = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject).build());
    getFragment().startActivity((Intent)localObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    super.removeAllCells();
    this.entrySum = countEntrySum(this.shopinfoEntryShowProperties);
    boolean bool1;
    boolean bool3;
    if ((this.entrySum > 0) && (super.getShopStatus() != 0))
    {
      if (isWeddingShopType())
        super.addCell("0250Basic.10Takeaway", createCellView(this.shopinfoEntryShowProperties), 0);
    }
    else if (super.getShopStatus() == 0)
    {
      this.enableDD = super.getShop().getBoolean("DDBookable");
      this.enableBooking = super.getShop().getBoolean("Bookable");
      bool1 = super.getShop().getBoolean("IsOrderDish");
      boolean bool2 = super.getShop().getBoolean("IsQueueable");
      boolean bool4 = super.getShop().getBoolean("Bookable");
      bool3 = super.getShop().getBoolean("HasTakeaway");
      if ((bool1) && (this.shopinfoEntryShowProperties[0].reqSendStatus == 0))
      {
        this.orderFlag = bool1;
        getDishOrder();
        this.shopinfoEntryShowProperties[0].reqSendStatus = 1;
      }
      if ((bool4) && (this.shopinfoEntryShowProperties[1].reqSendStatus == 0))
      {
        this.takeawayFlag = bool3;
        reqBookingContext();
        this.shopinfoEntryShowProperties[1].reqSendStatus = 1;
      }
      if ((bool2) && (this.shopinfoEntryShowProperties[2].reqSendStatus == 0))
      {
        this.bookingFlag = bool4;
        reqQueueSummary();
        this.shopinfoEntryShowProperties[2].reqSendStatus = 1;
      }
      if ((bool3) && (this.shopinfoEntryShowProperties[3].reqSendStatus == 0))
      {
        reqTakeaway();
        this.shopinfoEntryShowProperties[3].reqSendStatus = 1;
      }
      this.shopinfoEntryShowProperties[0].isShow = bool1;
      paramBundle = this.shopinfoEntryShowProperties[1];
      if ((!bool4) && (!this.enableDD))
        break label483;
      bool1 = true;
      label308: paramBundle.isShow = bool1;
      paramBundle = this.shopinfoEntryShowProperties[2];
      bool1 = bool2;
      if (this.shopinfoEntryShowProperties[2].reqSendStatus == 2)
      {
        if ((!bool2) || ((this.queueSummaryObj != null) && (!this.queueSummaryObj.getBoolean("Enabled"))))
          break label488;
        bool1 = true;
      }
      label361: paramBundle.isShow = bool1;
      if (this.shopinfoEntryShowProperties[3].reqSendStatus != 2)
        break label493;
      this.shopinfoEntryShowProperties[3].isShow = bool3;
    }
    while (true)
    {
      paramBundle = new LinearLayout(getContext());
      paramBundle.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      paramBundle.setOrientation(1);
      if (countEntrySum(this.shopinfoEntryShowProperties) <= 0)
        break label544;
      paramBundle.addView((NovaLinearLayout)createCellView(this.shopinfoEntryShowProperties));
      if (!isWeddingShopType())
        break label533;
      super.addCell("0250Basic.10Takeaway", paramBundle, 0);
      return;
      super.addCell(this.CELL_INDEX, createCellView(this.shopinfoEntryShowProperties), 0);
      break;
      label483: bool1 = false;
      break label308;
      label488: bool1 = false;
      break label361;
      label493: if (this.shopinfoEntryShowProperties[3].reqSendStatus == 3)
      {
        this.shopinfoEntryShowProperties[3].isShow = false;
        continue;
      }
      this.shopinfoEntryShowProperties[3].isShow = bool3;
    }
    label533: super.addCell(this.CELL_INDEX, paramBundle, 0);
    return;
    label544: super.removeAllCells();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.dishOrder = ((DPObject)paramBundle.getParcelable("dishOrder"));
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
    if (this.getDishOrderReq != null)
    {
      super.getFragment().mapiService().abort(this.getDishOrderReq, this, true);
      this.getDishOrderReq = null;
    }
    if (this.getQueueSummaryReq != null)
    {
      super.getFragment().mapiService().abort(this.getQueueSummaryReq, this, true);
      this.getQueueSummaryReq = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.bookingContextReq)
    {
      this.bookingContextReq = null;
      this.bookingFlag = false;
      this.shopinfoEntryShowProperties[1].reqSendStatus = 3;
      super.dispatchAgentChanged(false);
    }
    do
    {
      return;
      if (paramMApiRequest == this.mTakeawayReq)
      {
        this.mTakeawayReq = null;
        this.mTakeawayObj = null;
        this.takeawayFlag = false;
        this.shopinfoEntryShowProperties[3].reqSendStatus = 3;
        super.dispatchAgentChanged(false);
        return;
      }
      if (paramMApiRequest != this.getDishOrderReq)
        continue;
      this.getDishOrderReq = null;
      this.orderFlag = false;
      this.shopinfoEntryShowProperties[0].reqSendStatus = 3;
      super.dispatchAgentChanged(false);
      return;
    }
    while (paramMApiRequest != this.getQueueSummaryReq);
    this.getQueueSummaryReq = null;
    this.shopinfoEntryShowProperties[2].reqSendStatus = 3;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.bookingContextReq)
    {
      this.bookingContextReq = null;
      this.bookContext = ((DPObject)paramMApiResponse.result());
      if (this.bookContext != null)
      {
        this.bookConfig = this.bookContext.getObject("BookingConfig");
        this.payType = this.bookContext.getInt("PayType");
        this.bookingTips = this.bookContext.getString("BookingTips");
        this.shopinfoEntryShowProperties[1].reqSendStatus = 2;
        DPObject[] arrayOfDPObject = this.bookContext.getArray("IconList");
        paramMApiRequest = "";
        paramMApiResponse = paramMApiRequest;
        if (arrayOfDPObject != null)
        {
          int j = arrayOfDPObject.length;
          int i = 0;
          paramMApiResponse = paramMApiRequest;
          if (i < j)
          {
            paramMApiResponse = arrayOfDPObject[i];
            if (paramMApiResponse.getInt("Type") == 30)
            {
              paramMApiRequest = paramMApiResponse.getString("Content");
              if (TextUtils.isEmpty(paramMApiRequest))
                break label162;
            }
            while (true)
            {
              i += 1;
              break;
              label162: paramMApiRequest = "";
            }
          }
        }
        this.tipInfos[1] = paramMApiResponse;
        this.bookingFlag = false;
      }
      super.dispatchAgentChanged(false);
    }
    label288: label376: 
    do
    {
      return;
      if (paramMApiRequest == this.mTakeawayReq)
      {
        this.mTakeawayReq = null;
        this.mTakeawayObj = ((DPObject)paramMApiResponse.result());
        this.shopinfoEntryShowProperties[3].reqSendStatus = 2;
        paramMApiRequest = "";
        paramMApiResponse = new DPObject();
        if (this.mTakeawayObj != null)
          paramMApiResponse = this.mTakeawayObj.getObject("Activity");
        if (paramMApiResponse != null)
        {
          paramMApiRequest = paramMApiResponse.getString("Message");
          if (TextUtils.isEmpty(paramMApiRequest))
            break label288;
        }
        while (true)
        {
          this.tipInfos[3] = paramMApiRequest;
          this.takeawayFlag = false;
          super.dispatchAgentChanged(false);
          return;
          paramMApiRequest = "";
        }
      }
      if (paramMApiRequest != this.getDishOrderReq)
        continue;
      this.dishOrder = ((DPObject)paramMApiResponse.result());
      this.getDishOrderReq = null;
      this.shopinfoEntryShowProperties[0].reqSendStatus = 2;
      paramMApiRequest = "";
      if (this.dishOrder != null)
      {
        paramMApiRequest = this.dishOrder.getString("Tips");
        if (TextUtils.isEmpty(paramMApiRequest))
          break label376;
      }
      while (true)
      {
        this.tipInfos[0] = paramMApiRequest;
        this.orderFlag = false;
        super.dispatchAgentChanged(false);
        return;
        paramMApiRequest = "";
      }
    }
    while (paramMApiRequest != this.getQueueSummaryReq);
    this.getQueueSummaryReq = null;
    this.queueSummaryObj = ((DPObject)paramMApiResponse.result());
    this.shopinfoEntryShowProperties[2].reqSendStatus = 2;
    this.tipInfos[2] = "";
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
        if (OrderBookQueueTAEntryAgent.this.bookingContextReq != null)
          OrderBookQueueTAEntryAgent.this.getFragment().mapiService().exec(OrderBookQueueTAEntryAgent.this.bookingContextReq, OrderBookQueueTAEntryAgent.this);
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
    localBundle.putParcelable("dishOrder", this.dishOrder);
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
        OrderBookQueueTAEntryAgent.this.bookConfig = ((DPObject)paramIntent.getParcelableExtra("bookinginfo"));
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
        OrderBookQueueTAEntryAgent.this.reqQueueSummary();
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
 * Qualified Name:     com.dianping.shopinfo.common.OrderBookQueueTAEntryAgent
 * JD-Core Version:    0.6.0
 */