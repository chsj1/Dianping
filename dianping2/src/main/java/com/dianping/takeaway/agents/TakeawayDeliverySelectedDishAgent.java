package com.dianping.takeaway.agents;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.loader.MyResources;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource.LoadCause;
import com.dianping.takeaway.fragment.TakeawayDeliveryDetailFragment;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TakeawayDeliverySelectedDishAgent extends CellAgent
{
  private TextView carrierView;
  private TakeawayDeliveryDataSource dataSource = this.fragment.getDataSource();
  private LinearLayout dishView;
  private TakeawayDeliveryDetailFragment fragment = (TakeawayDeliveryDetailFragment)getFragment();
  private View view;

  public TakeawayDeliverySelectedDishAgent(Object paramObject)
  {
    super(paramObject);
    setupView();
  }

  private void setupView()
  {
    this.view = LayoutInflater.from(getContext()).inflate(R.layout.takeaway_delivery_dish_agent_layout, null);
    this.dishView = ((LinearLayout)this.view.findViewById(R.id.dish_list));
    this.carrierView = ((TextView)this.view.findViewById(R.id.carrier_name));
  }

  private void updateView(DPObject[] paramArrayOfDPObject, String paramString)
  {
    this.dishView.removeAllViews();
    if ((paramArrayOfDPObject != null) && (paramArrayOfDPObject.length != 0))
    {
      int j = paramArrayOfDPObject.length;
      int i = 0;
      while (i < j)
      {
        DPObject localDPObject = paramArrayOfDPObject[i];
        View localView = LayoutInflater.from(getContext()).inflate(R.layout.takeaway_delivery_dish_item, null);
        ((TextView)localView.findViewById(R.id.name_view)).setText(localDPObject.getString("Title"));
        ((TextView)localView.findViewById(R.id.count_view)).setText(localDPObject.getInt("Count") + "份");
        RMBLabelItem localRMBLabelItem = (RMBLabelItem)localView.findViewById(R.id.price_view);
        localRMBLabelItem.setRMBLabelStyle6(false, getResources().getColor(R.color.deep_gray));
        localRMBLabelItem.setRMBLabelValue(localDPObject.getDouble("Price"));
        this.dishView.addView(localView);
        i += 1;
      }
      this.view.setVisibility(0);
    }
    while (!TextUtils.isEmpty(paramString))
    {
      this.carrierView.setText(Html.fromHtml("本单由<font color=#ff6633>" + paramString + "</font>" + "提供售后服务"));
      this.carrierView.setVisibility(0);
      return;
      this.view.setVisibility(8);
    }
    this.carrierView.setVisibility(8);
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ((this.dataSource.loadCause == TakeawayDeliveryDataSource.LoadCause.FIR_LOAD) && (paramAgentMessage != null) && ("DELIVERY_LOAD_ORDER_SUCCESS".equals(paramAgentMessage.what)))
      updateView(this.dataSource.dishList, this.dataSource.carrier);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    addCell("7000dish", this.view);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.agents.TakeawayDeliverySelectedDishAgent
 * JD-Core Version:    0.6.0
 */