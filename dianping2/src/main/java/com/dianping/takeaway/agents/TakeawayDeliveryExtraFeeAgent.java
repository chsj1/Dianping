package com.dianping.takeaway.agents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.loader.MyResources;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource;
import com.dianping.takeaway.fragment.TakeawayDeliveryDetailFragment;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TakeawayDeliveryExtraFeeAgent extends CellAgent
{
  private TakeawayDeliveryDataSource dataSource = this.fragment.getDataSource();
  private TakeawayDeliveryDetailFragment fragment = (TakeawayDeliveryDetailFragment)getFragment();
  private LinearLayout view;

  public TakeawayDeliveryExtraFeeAgent(Object paramObject)
  {
    super(paramObject);
    setupView();
  }

  private void setupView()
  {
    this.view = new LinearLayout(getContext());
    this.view.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.view.setOrientation(1);
    this.view.setBackgroundResource(R.drawable.wm_list_bkg_line_bottom);
  }

  private void updateView(DPObject[] paramArrayOfDPObject)
  {
    this.view.removeAllViews();
    if ((paramArrayOfDPObject != null) && (paramArrayOfDPObject.length != 0))
    {
      int j = paramArrayOfDPObject.length;
      int i = 0;
      while (i < j)
      {
        DPObject localDPObject = paramArrayOfDPObject[i];
        View localView = LayoutInflater.from(getContext()).inflate(R.layout.takeaway_delivery_promo_fee_item, null);
        localView.findViewById(R.id.top_divider).setVisibility(0);
        ((TextView)localView.findViewById(R.id.name_view)).setText(localDPObject.getString("Title"));
        RMBLabelItem localRMBLabelItem = (RMBLabelItem)localView.findViewById(R.id.price_view);
        localRMBLabelItem.setRMBLabelStyle6(false, getResources().getColor(R.color.deep_gray));
        localRMBLabelItem.setRMBLabelValue(localDPObject.getDouble("Price"));
        this.view.addView(localView);
        i += 1;
      }
      this.view.setVisibility(0);
      return;
    }
    this.view.setVisibility(8);
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ((paramAgentMessage != null) && ("DELIVERY_LOAD_ORDER_SUCCESS".equals(paramAgentMessage.what)))
      updateView(this.dataSource.feeList);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    addCell("5000extrafee", this.view);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.agents.TakeawayDeliveryExtraFeeAgent
 * JD-Core Version:    0.6.0
 */