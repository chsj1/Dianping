package com.dianping.base.tuan.agent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.base.tuan.utils.OrderCenterUtils;
import com.dianping.base.tuan.widget.BasicSingleItem;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public abstract class BasicOrderDetailDeliveryAgent extends TuanCellAgent
{
  private final String ORDER_DELIVERY;
  private ViewGroup deliveryActionView;
  private BasicSingleItem deliveryInfoItem;
  private BasicSingleItem deliveryStatusItem;
  private ViewGroup footerView;
  private DetailDeliveryModel model;
  private View rootView;

  public BasicOrderDetailDeliveryAgent(Object paramObject, String paramString)
  {
    super(paramObject);
    this.ORDER_DELIVERY = paramString;
  }

  private boolean canViewLogistics()
  {
    if (!this.model.getIsDeliveryDeal());
    int i;
    do
    {
      return false;
      i = this.model.getRefundStatus();
    }
    while ((i == 1) || (i == 2) || (i == 3));
    return true;
  }

  private void setupView()
  {
    if (this.rootView == null)
    {
      this.rootView = this.res.inflate(getContext(), R.layout.order_detail_delivery, null, false);
      this.deliveryInfoItem = ((BasicSingleItem)this.rootView.findViewById(R.id.order_delivery_info));
      this.deliveryStatusItem = ((BasicSingleItem)this.rootView.findViewById(R.id.order_delivery_status));
      this.deliveryActionView = ((ViewGroup)this.rootView.findViewById(R.id.order_delivery_action));
      this.footerView = ((ViewGroup)this.rootView.findViewById(R.id.order_footer));
    }
  }

  private void setupViewForDelivery()
  {
    this.deliveryStatusItem.setVisibility(0);
    this.deliveryActionView.setVisibility(0);
    Object localObject2 = this.model.getExtras();
    Object localObject1;
    if ((localObject2 != null) && (((List)localObject2).size() > 0))
    {
      this.deliveryInfoItem.setVisibility(0);
      localObject1 = (TextView)this.deliveryInfoItem.findViewById(R.id.s_subtitle);
      ((TextView)localObject1).setMaxWidth(ViewUtils.dip2px(getContext(), 165.0F));
      ((TextView)localObject1).setGravity(21);
      localObject1 = new String[3];
      localObject2 = ((List)localObject2).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        Pair localPair = (Pair)((Iterator)localObject2).next();
        if ("姓名".equals(localPair.first))
        {
          localObject1[0] = ((String)localPair.second);
          continue;
        }
        if ("电话".equals(localPair.first))
        {
          localObject1[1] = ((String)localPair.second);
          continue;
        }
        if (!"地址".equals(localPair.first))
          continue;
        localObject1[2] = ((String)localPair.second);
      }
      this.deliveryInfoItem.setSubTitle(localObject1[0] + " " + localObject1[1] + "\n" + localObject1[2]);
    }
    this.deliveryStatusItem.setSubTitle(this.model.getShipment());
    if (this.model.getShipmentStatus() == 1)
    {
      this.deliveryActionView.findViewById(R.id.order_confirm_delivery).setVisibility(0);
      localObject1 = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.simple_table_item_text, this.footerView, false);
      this.footerView.addView((View)localObject1);
      ((TextView)localObject1).setTextSize(10.0F);
      ((TextView)localObject1).setTextColor(getResources().getColor(R.color.tuan_common_orange));
      ((TextView)localObject1).setText("* 订单自动确认收货时间 " + OrderCenterUtils.SDF_DATE.format(new Date(this.model.getReceiveDate())));
    }
    while (true)
    {
      this.deliveryActionView.findViewById(R.id.order_check_delivery).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://logisticview"));
          paramView.putExtra("OrderID", BasicOrderDetailDeliveryAgent.this.model.getOrderID());
          BasicOrderDetailDeliveryAgent.this.getContext().startActivity(paramView);
        }
      });
      this.deliveryActionView.findViewById(R.id.order_confirm_delivery).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
        }
      });
      localObject1 = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.simple_table_item_text, null, false);
      if (!TextUtils.isEmpty(this.model.getRefundTip()))
      {
        this.footerView.addView((View)localObject1);
        ((TextView)localObject1).setTextSize(10.0F);
        ((TextView)localObject1).setTextColor(getResources().getColor(R.color.text_color_gray));
        ((TextView)localObject1).setText(this.model.getRefundTip());
      }
      if ((this.footerView != null) && (this.footerView.getChildCount() <= 0))
        this.footerView.setVisibility(8);
      return;
      if (canViewLogistics())
      {
        this.deliveryActionView.findViewById(R.id.order_confirm_delivery).setVisibility(8);
        continue;
      }
      this.deliveryActionView.setVisibility(8);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setupView();
  }

  protected final void updateView(DetailDeliveryModel paramDetailDeliveryModel)
  {
    removeAllCells();
    if (paramDetailDeliveryModel == null);
    do
    {
      return;
      this.model = paramDetailDeliveryModel;
    }
    while (!paramDetailDeliveryModel.getIsDeliveryDeal());
    setupViewForDelivery();
    addCell(this.ORDER_DELIVERY, this.rootView);
  }

  public static class DetailDeliveryModel
  {
    private final List<Pair<String, String>> extras;
    private final boolean isDeliveryDeal;
    private final int orderID;
    private final long receiveDate;
    private final int refundStatus;
    private final String refundTip;
    private final String shipment;
    private final int shipmentStatus;

    private DetailDeliveryModel(int paramInt1, boolean paramBoolean, int paramInt2, String paramString1, int paramInt3, long paramLong, List<Pair<String, String>> paramList, String paramString2)
    {
      this.orderID = paramInt1;
      this.isDeliveryDeal = paramBoolean;
      this.shipmentStatus = paramInt2;
      this.shipment = paramString1;
      this.receiveDate = paramLong;
      this.extras = paramList;
      this.refundStatus = paramInt3;
      this.refundTip = paramString2;
    }

    public static DeliveryModelBuilder builder()
    {
      return new DeliveryModelBuilder(null);
    }

    public List<Pair<String, String>> getExtras()
    {
      return this.extras;
    }

    public boolean getIsDeliveryDeal()
    {
      return this.isDeliveryDeal;
    }

    public int getOrderID()
    {
      return this.orderID;
    }

    public long getReceiveDate()
    {
      return this.receiveDate;
    }

    public int getRefundStatus()
    {
      return this.refundStatus;
    }

    public String getRefundTip()
    {
      return this.refundTip;
    }

    public String getShipment()
    {
      return this.shipment;
    }

    public int getShipmentStatus()
    {
      return this.shipmentStatus;
    }

    public static class DeliveryModelBuilder
    {
      private List<Pair<String, String>> extras;
      private boolean isDeliveryDeal;
      private int orderID;
      private long receiveDate;
      private int refundStatus;
      private String refundTip;
      private String shipment;
      private int shipmentStatus;

      public BasicOrderDetailDeliveryAgent.DetailDeliveryModel build()
      {
        return new BasicOrderDetailDeliveryAgent.DetailDeliveryModel(this.orderID, this.isDeliveryDeal, this.shipmentStatus, this.shipment, this.refundStatus, this.receiveDate, this.extras, this.refundTip, null);
      }

      public DeliveryModelBuilder setExtras(List<Pair<String, String>> paramList)
      {
        this.extras = paramList;
        return this;
      }

      public DeliveryModelBuilder setIsDeliveryDeal(boolean paramBoolean)
      {
        this.isDeliveryDeal = paramBoolean;
        return this;
      }

      public DeliveryModelBuilder setOrderID(int paramInt)
      {
        this.orderID = paramInt;
        return this;
      }

      public DeliveryModelBuilder setReceiveDate(long paramLong)
      {
        this.receiveDate = paramLong;
        return this;
      }

      public DeliveryModelBuilder setRefundStatus(int paramInt)
      {
        this.refundStatus = paramInt;
        return this;
      }

      public DeliveryModelBuilder setRefundTip(String paramString)
      {
        this.refundTip = paramString;
        return this;
      }

      public DeliveryModelBuilder setShipment(String paramString)
      {
        this.shipment = paramString;
        return this;
      }

      public DeliveryModelBuilder setShipmentStatus(int paramInt)
      {
        this.shipmentStatus = paramInt;
        return this;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.BasicOrderDetailDeliveryAgent
 * JD-Core Version:    0.6.0
 */