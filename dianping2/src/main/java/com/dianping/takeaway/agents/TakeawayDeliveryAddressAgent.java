package com.dianping.takeaway.agents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource.LoadCause;
import com.dianping.takeaway.fragment.TakeawayDeliveryDetailFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class TakeawayDeliveryAddressAgent extends CellAgent
{
  private TextView addressView;
  protected TakeawayDeliveryDataSource dataSource = this.fragment.getDataSource();
  private View deliveryInfoView;
  private View emptyInfoView;
  protected TakeawayDeliveryDetailFragment fragment = (TakeawayDeliveryDetailFragment)getFragment();
  private TextView phoneView;
  private NovaLinearLayout view;

  public TakeawayDeliveryAddressAgent(Object paramObject)
  {
    super(paramObject);
    setupView();
  }

  private void setupView()
  {
    this.view = ((NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.takeaway_delivery_address_agent_layout, null));
    this.view.setGAString("address");
    this.view.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayaddresslist?shopid=" + TakeawayDeliveryAddressAgent.this.dataSource.shopID));
        paramView.putExtra("address", TakeawayDeliveryAddressAgent.this.dataSource.orderAddress);
        TakeawayDeliveryAddressAgent localTakeawayDeliveryAddressAgent = TakeawayDeliveryAddressAgent.this;
        TakeawayDeliveryAddressAgent.this.fragment.getClass();
        localTakeawayDeliveryAddressAgent.startActivityForResult(paramView, 1);
      }
    });
    this.deliveryInfoView = this.view.findViewById(R.id.delivery_info_view);
    this.emptyInfoView = this.view.findViewById(R.id.empty_info_view);
    this.addressView = ((TextView)this.view.findViewById(R.id.address_view));
    this.phoneView = ((TextView)this.view.findViewById(R.id.phone_view));
  }

  private void updateView()
  {
    if ((this.dataSource.loadCause == TakeawayDeliveryDataSource.LoadCause.PAY_TYPE_CHANGED) || (this.dataSource.loadCause == TakeawayDeliveryDataSource.LoadCause.LOG_IN_SUCCESS) || (this.dataSource.loadCause == TakeawayDeliveryDataSource.LoadCause.ACTIVITY_CHANGED))
      return;
    if (this.dataSource.orderAddress != null)
    {
      this.addressView.setText(this.dataSource.getAddress());
      this.phoneView.setText(this.dataSource.getPhone());
      this.deliveryInfoView.setVisibility(0);
      this.emptyInfoView.setVisibility(8);
      return;
    }
    this.deliveryInfoView.setVisibility(8);
    this.emptyInfoView.setVisibility(0);
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    int i;
    if (paramAgentMessage != null)
    {
      paramAgentMessage = paramAgentMessage.what;
      i = -1;
      switch (paramAgentMessage.hashCode())
      {
      default:
      case 1447863908:
      case -1978961853:
      }
    }
    while (true)
      switch (i)
      {
      default:
        return;
        if (!paramAgentMessage.equals("DELIVERY_LOAD_ORDER_SUCCESS"))
          continue;
        i = 0;
        continue;
        if (!paramAgentMessage.equals("DELIVERY_ADDRESS_DELETED"))
          continue;
        i = 1;
      case 0:
      case 1:
      }
    updateView();
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    addCell("0000address", this.view);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.agents.TakeawayDeliveryAddressAgent
 * JD-Core Version:    0.6.0
 */