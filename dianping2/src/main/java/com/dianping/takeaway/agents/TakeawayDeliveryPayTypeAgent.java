package com.dianping.takeaway.agents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource.LoadCause;
import com.dianping.takeaway.fragment.TakeawayDeliveryDetailFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TakeawayDeliveryPayTypeAgent extends CellAgent
{
  protected TakeawayDeliveryDataSource dataSource = this.fragment.getDataSource();
  private ImageView facetofaceCheckbox;
  private TextView facetofaceDisableView;
  private View facetofacePayView;
  private TakeawayDeliveryDetailFragment fragment = (TakeawayDeliveryDetailFragment)getFragment();
  private TextView onlineDisableView;
  private View onlinePayView;
  private ImageView onlinepayCheckbox;
  private TextView onlinepayDiscount;
  private View view;

  public TakeawayDeliveryPayTypeAgent(Object paramObject)
  {
    super(paramObject);
    setupView();
  }

  private void setupView()
  {
    this.view = LayoutInflater.from(getContext()).inflate(R.layout.takeaway_delivery_pay_type_agent_layout, null);
    this.facetofacePayView = this.view.findViewById(R.id.paytype_facetoface);
    this.facetofacePayView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayDeliveryPayTypeAgent.this.facetofaceCheckbox.setSelected(true);
        TakeawayDeliveryPayTypeAgent.this.onlinepayCheckbox.setSelected(false);
        int i = TakeawayDeliveryPayTypeAgent.this.dataSource.payType;
        TakeawayDeliveryPayTypeAgent.this.dataSource.getClass();
        if (i != 0)
        {
          paramView = TakeawayDeliveryPayTypeAgent.this.dataSource;
          TakeawayDeliveryPayTypeAgent.this.dataSource.getClass();
          paramView.payType = 0;
          TakeawayDeliveryPayTypeAgent.this.dataSource.loadCause = TakeawayDeliveryDataSource.LoadCause.PAY_TYPE_CHANGED;
          TakeawayDeliveryPayTypeAgent.this.dataSource.confirmOrderTask();
        }
      }
    });
    ((TextView)this.facetofacePayView.findViewById(R.id.paytype_name)).setText("餐到付款");
    this.facetofacePayView.findViewById(R.id.paytype_discount).setVisibility(8);
    this.facetofaceDisableView = ((TextView)this.facetofacePayView.findViewById(R.id.disable_desp));
    this.facetofaceCheckbox = ((ImageView)this.facetofacePayView.findViewById(R.id.paytype_selected));
    this.facetofaceCheckbox.setVisibility(0);
    this.onlinePayView = this.view.findViewById(R.id.paytype_online);
    this.onlinePayView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayDeliveryPayTypeAgent.this.onlinepayCheckbox.setSelected(true);
        TakeawayDeliveryPayTypeAgent.this.facetofaceCheckbox.setSelected(false);
        int i = TakeawayDeliveryPayTypeAgent.this.dataSource.payType;
        TakeawayDeliveryPayTypeAgent.this.dataSource.getClass();
        if (i != 1)
        {
          paramView = TakeawayDeliveryPayTypeAgent.this.dataSource;
          TakeawayDeliveryPayTypeAgent.this.dataSource.getClass();
          paramView.payType = 1;
          TakeawayDeliveryPayTypeAgent.this.dataSource.loadCause = TakeawayDeliveryDataSource.LoadCause.PAY_TYPE_CHANGED;
          TakeawayDeliveryPayTypeAgent.this.dataSource.confirmOrderTask();
        }
      }
    });
    ((TextView)this.onlinePayView.findViewById(R.id.paytype_name)).setText("在线支付");
    this.onlinepayDiscount = ((TextView)this.onlinePayView.findViewById(R.id.paytype_discount));
    this.onlineDisableView = ((TextView)this.onlinePayView.findViewById(R.id.disable_desp));
    this.onlinepayCheckbox = ((ImageView)this.onlinePayView.findViewById(R.id.paytype_selected));
  }

  private void updateView()
  {
    if ((this.dataSource.loadCause == TakeawayDeliveryDataSource.LoadCause.ADDRESS_CHANGED) || (this.dataSource.loadCause == TakeawayDeliveryDataSource.LoadCause.LOG_IN_SUCCESS));
    label281: 
    while (true)
    {
      return;
      int i;
      if ((this.dataSource.supportPayType == 1) || (this.dataSource.supportPayType == 2))
      {
        i = 1;
        if (i == 0)
          break label218;
        this.onlineDisableView.setVisibility(8);
        this.onlinepayCheckbox.setVisibility(0);
        ViewUtils.setVisibilityAndContent(this.onlinepayDiscount, this.dataSource.onlinePayActi);
        this.onlinePayView.setClickable(true);
        label94: if ((this.dataSource.supportPayType != 0) && (this.dataSource.supportPayType != 1))
          break label255;
        this.facetofaceDisableView.setVisibility(8);
        this.facetofaceCheckbox.setVisibility(0);
        this.facetofacePayView.setClickable(true);
      }
      while (true)
      {
        if (this.dataSource.loadCause != TakeawayDeliveryDataSource.LoadCause.FIR_LOAD)
          break label281;
        int j = this.dataSource.curPayType;
        this.dataSource.getClass();
        if ((j != 1) || (i == 0))
          break label283;
        this.facetofaceCheckbox.setSelected(false);
        this.onlinepayCheckbox.setSelected(true);
        localTakeawayDeliveryDataSource = this.dataSource;
        this.dataSource.getClass();
        localTakeawayDeliveryDataSource.payType = 1;
        return;
        i = 0;
        break;
        label218: this.onlinepayDiscount.setVisibility(8);
        this.onlineDisableView.setVisibility(0);
        this.onlinepayCheckbox.setVisibility(8);
        this.onlinePayView.setClickable(false);
        break label94;
        label255: this.facetofaceDisableView.setVisibility(0);
        this.facetofaceCheckbox.setVisibility(8);
        this.facetofacePayView.setClickable(false);
      }
    }
    label283: this.facetofaceCheckbox.setSelected(true);
    this.onlinepayCheckbox.setSelected(false);
    TakeawayDeliveryDataSource localTakeawayDeliveryDataSource = this.dataSource;
    this.dataSource.getClass();
    localTakeawayDeliveryDataSource.payType = 0;
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ((paramAgentMessage != null) && ("DELIVERY_LOAD_ORDER_SUCCESS".equals(paramAgentMessage.what)))
      updateView();
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    addCell("1000paytype", this.view);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.agents.TakeawayDeliveryPayTypeAgent
 * JD-Core Version:    0.6.0
 */