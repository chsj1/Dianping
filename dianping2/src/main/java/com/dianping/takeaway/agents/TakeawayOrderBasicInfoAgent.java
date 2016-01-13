package com.dianping.takeaway.agents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.loader.MyResources;
import com.dianping.takeaway.entity.OrderDetailAdapter;
import com.dianping.takeaway.entity.TakeawayOrderDetail;
import com.dianping.takeaway.fragment.TakeawayOrderDetailFragment;
import com.dianping.takeaway.util.TakeawayUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaImageView;

public class TakeawayOrderBasicInfoAgent extends CellAgent
{
  private LinearLayout basicInfoContainer;
  private TextView compensateContentView;
  private View compensateView;
  private TextView dishAmountView;
  private ListView dishLayout;
  private View dishMore;
  private NovaImageView dunningBtn;
  private TextView invoiceContentView;
  private View invoiceView;
  private boolean isMore = false;
  private TextView orderAddressView;
  private TextView orderIdView;
  private OrderDetailAdapter orderInfoAdapter;
  private TextView orderPhoneView;
  private TextView orderTimeView;
  private TextView payTypeView;
  private TextView remarkContentView;
  private View remarkView;
  private NovaButton shareButton;
  private View shareLayout;
  private TextView shareTextView;
  private TextView shopNameTextView;
  private View shopNameView;
  private TakeawayOrderDetail takeawayOrderDetail;
  private RMBLabelItem totalPriceView;

  public TakeawayOrderBasicInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setupBillContent(TakeawayOrderDetail paramTakeawayOrderDetail)
  {
    DPObject[] arrayOfDPObject1 = paramTakeawayOrderDetail.dishList;
    DPObject[] arrayOfDPObject2 = paramTakeawayOrderDetail.activityList;
    DPObject[] arrayOfDPObject3 = paramTakeawayOrderDetail.feeList;
    DPObject[] arrayOfDPObject4 = new DPObject[1];
    int j;
    int i;
    int k;
    int m;
    if ((arrayOfDPObject1.length + arrayOfDPObject2.length + arrayOfDPObject3.length <= 1) || (this.isMore))
    {
      this.dishMore.setVisibility(8);
      this.orderInfoAdapter.setData(null, arrayOfDPObject1, arrayOfDPObject2, arrayOfDPObject3);
      j = 0;
      i = 0;
      k = j;
      if (arrayOfDPObject1 != null)
      {
        k = j;
        if (arrayOfDPObject1.length > 0)
        {
          m = arrayOfDPObject1.length;
          j = 0;
        }
      }
    }
    else
    {
      while (true)
      {
        k = i;
        if (j >= m)
          break;
        i += arrayOfDPObject1[j].getInt("Count");
        j += 1;
        continue;
        this.dishMore.setVisibility(0);
        if ((arrayOfDPObject1 != null) && (arrayOfDPObject1.length > 0))
        {
          arrayOfDPObject4[0] = arrayOfDPObject1[0];
          this.orderInfoAdapter.setData(null, arrayOfDPObject4, null, null);
        }
        while (true)
        {
          this.dishMore.setOnClickListener(new View.OnClickListener(arrayOfDPObject1, arrayOfDPObject2, arrayOfDPObject3)
          {
            public void onClick(View paramView)
            {
              TakeawayOrderBasicInfoAgent.access$002(TakeawayOrderBasicInfoAgent.this, true);
              TakeawayOrderBasicInfoAgent.this.orderInfoAdapter.setData(null, this.val$dishList, this.val$activityList, this.val$feeList);
              TakeawayOrderBasicInfoAgent.this.dishMore.setVisibility(8);
            }
          });
          break;
          if ((arrayOfDPObject2 != null) && (arrayOfDPObject2.length > 0))
          {
            arrayOfDPObject4[0] = arrayOfDPObject2[0];
            this.orderInfoAdapter.setData(null, null, arrayOfDPObject4, null);
            continue;
          }
          if ((arrayOfDPObject3 == null) || (arrayOfDPObject3.length <= 0))
            continue;
          arrayOfDPObject4[0] = arrayOfDPObject3[0];
          this.orderInfoAdapter.setData(null, null, null, arrayOfDPObject4);
        }
      }
    }
    if (k == 0)
    {
      this.dishAmountView.setVisibility(8);
      if (Double.parseDouble(paramTakeawayOrderDetail.discountAmount) >= 0.0D)
        break label349;
    }
    label349: for (paramTakeawayOrderDetail = paramTakeawayOrderDetail.originalAmount; ; paramTakeawayOrderDetail = paramTakeawayOrderDetail.discountAmount)
    {
      this.totalPriceView.setRMBLabelStyle6(false, getResources().getColor(R.color.light_red));
      this.totalPriceView.setRMBLabelValue(Double.parseDouble(paramTakeawayOrderDetail));
      return;
      this.dishAmountView.setText("数量: " + k);
      this.dishAmountView.setVisibility(0);
      break;
    }
  }

  private void setupBillTitle(String paramString1, String[] paramArrayOfString, String paramString2, int paramInt)
  {
    this.shopNameView.setOnClickListener(new View.OnClickListener(paramInt, paramString1)
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawaydishlist"));
        paramView.putExtra("shopid", String.valueOf(this.val$shopId));
        paramView.putExtra("shopname", this.val$shopName);
        paramView.putExtra("source", 5);
        TakeawayOrderBasicInfoAgent.this.startActivity(paramView);
      }
    });
    this.shopNameTextView.setText(paramString1);
    if ((paramArrayOfString != null) && (paramArrayOfString.length != 0))
    {
      this.dunningBtn.setGAString("callshop");
      this.dunningBtn.setOnClickListener(new View.OnClickListener(paramArrayOfString, paramString2)
      {
        public void onClick(View paramView)
        {
          TakeawayUtils.callShopPhones(TakeawayOrderBasicInfoAgent.this.getContext(), this.val$shopPhone);
          TakeawayOrderBasicInfoAgent.this.statisticsEvent("takeaway6", "takeaway6_orderdetails_callshopclk", this.val$orderViewId, 0);
        }
      });
      this.dunningBtn.setVisibility(0);
      return;
    }
    this.dunningBtn.setVisibility(8);
  }

  private void setupShareEntrance(TakeawayOrderDetail paramTakeawayOrderDetail)
  {
    if (paramTakeawayOrderDetail.ableToShareHongBao)
    {
      if (!TextUtils.isEmpty(paramTakeawayOrderDetail.hongBaoPromotion))
        this.shareTextView.setText(paramTakeawayOrderDetail.hongBaoPromotion);
      this.shareButton.setGAString("share");
      this.shareButton.setOnClickListener(new View.OnClickListener(paramTakeawayOrderDetail)
      {
        public void onClick(View paramView)
        {
          ((TakeawayOrderDetailFragment)TakeawayOrderBasicInfoAgent.this.getFragment()).shareObj(this.val$takeawayOrderDetail.shareObj);
          TakeawayOrderBasicInfoAgent.this.statisticsEvent("takeaway6", "takeaway6_orderdetails_shareclk", this.val$takeawayOrderDetail.orderViewId, 0);
        }
      });
      this.shareLayout.setVisibility(0);
      return;
    }
    this.shareLayout.setVisibility(8);
  }

  private void setupView(TakeawayOrderDetail paramTakeawayOrderDetail)
  {
    setupBillTitle(paramTakeawayOrderDetail.shopName, paramTakeawayOrderDetail.shopPhone, paramTakeawayOrderDetail.orderViewId, paramTakeawayOrderDetail.shopId);
    setupBillContent(paramTakeawayOrderDetail);
    setupShareEntrance(paramTakeawayOrderDetail);
    this.orderIdView.setText(paramTakeawayOrderDetail.orderViewId);
    this.orderAddressView.setText(paramTakeawayOrderDetail.address);
    this.orderPhoneView.setText(paramTakeawayOrderDetail.contact);
    this.payTypeView.setText(paramTakeawayOrderDetail.payStatus);
    this.orderTimeView.setText(paramTakeawayOrderDetail.orderTime);
    if (!TextUtils.isEmpty(paramTakeawayOrderDetail.invoice))
    {
      this.invoiceContentView.setText(paramTakeawayOrderDetail.invoice);
      this.invoiceView.setVisibility(0);
      if (TextUtils.isEmpty(paramTakeawayOrderDetail.comment))
        break label185;
      this.remarkContentView.setText(paramTakeawayOrderDetail.comment);
      this.remarkView.setVisibility(0);
    }
    while (true)
    {
      if (TextUtils.isEmpty(paramTakeawayOrderDetail.overtimePaymentDesc))
        break label197;
      this.compensateContentView.setText(paramTakeawayOrderDetail.overtimePaymentDesc);
      this.compensateView.setVisibility(0);
      return;
      this.invoiceView.setVisibility(8);
      break;
      label185: this.remarkView.setVisibility(8);
    }
    label197: this.compensateView.setVisibility(8);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    addCell("02.basic_info.0", this.basicInfoContainer);
    if (paramBundle == null)
    {
      this.basicInfoContainer.setVisibility(8);
      return;
    }
    this.basicInfoContainer.setVisibility(0);
    switch (paramBundle.getInt("type"))
    {
    default:
      return;
    case 0:
    }
    this.takeawayOrderDetail = new TakeawayOrderDetail((DPObject)paramBundle.getParcelable("order"));
    setupView(this.takeawayOrderDetail);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.basicInfoContainer = ((LinearLayout)this.res.inflate(getContext(), R.layout.takeaway_order_basic_info_layout, null, false));
    this.shopNameView = this.basicInfoContainer.findViewById(R.id.shop_name);
    this.shopNameTextView = ((TextView)this.basicInfoContainer.findViewById(R.id.shop_name_text));
    this.dunningBtn = ((NovaImageView)this.basicInfoContainer.findViewById(R.id.dunning_btn));
    this.dishLayout = ((ListView)this.basicInfoContainer.findViewById(R.id.dish_layout));
    this.orderInfoAdapter = new OrderDetailAdapter(getFragment().getActivity());
    this.dishLayout.setAdapter(this.orderInfoAdapter);
    this.dishMore = this.basicInfoContainer.findViewById(R.id.dish_more);
    this.totalPriceView = ((RMBLabelItem)this.basicInfoContainer.findViewById(R.id.total_price));
    this.dishAmountView = ((TextView)this.basicInfoContainer.findViewById(R.id.dish_amount));
    this.orderIdView = ((TextView)this.basicInfoContainer.findViewById(R.id.order_id));
    this.orderAddressView = ((TextView)this.basicInfoContainer.findViewById(R.id.orderer_address));
    this.orderPhoneView = ((TextView)this.basicInfoContainer.findViewById(R.id.orderer_phone));
    this.payTypeView = ((TextView)this.basicInfoContainer.findViewById(R.id.paytype_content));
    this.orderTimeView = ((TextView)this.basicInfoContainer.findViewById(R.id.time_content));
    this.invoiceView = this.basicInfoContainer.findViewById(R.id.invoice_view);
    this.invoiceContentView = ((TextView)this.basicInfoContainer.findViewById(R.id.invoice_content));
    this.remarkView = this.basicInfoContainer.findViewById(R.id.remark_view);
    this.remarkContentView = ((TextView)this.basicInfoContainer.findViewById(R.id.remark_content));
    this.compensateView = this.basicInfoContainer.findViewById(R.id.compensate_view);
    this.compensateContentView = ((TextView)this.basicInfoContainer.findViewById(R.id.compensate_content));
    this.shareLayout = this.basicInfoContainer.findViewById(R.id.share_entrance_layout);
    this.shareButton = ((NovaButton)this.basicInfoContainer.findViewById(R.id.share_button));
    this.shareTextView = ((TextView)this.basicInfoContainer.findViewById(R.id.share_textview));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.agents.TakeawayOrderBasicInfoAgent
 * JD-Core Version:    0.6.0
 */