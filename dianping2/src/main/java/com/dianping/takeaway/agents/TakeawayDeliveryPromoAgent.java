package com.dianping.takeaway.agents;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TakeawayDeliveryPromoAgent extends CellAgent
{
  private TextView activityTipsView;
  private int activityType;
  private TakeawayDeliveryDataSource dataSource = this.fragment.getDataSource();
  private LinearLayout dpActivityListView;
  private int dpActivityNum = 0;
  private View dpActivitySelect;
  private TextView dpActivityTitle;
  private LinearLayout dpActivityView;
  private LinearLayout elemeActivityListView;
  private int elemeActivityNum = 0;
  private View elemeActivitySelect;
  private TextView elemeActivityTitle;
  private LinearLayout elemeActivityView;
  private TakeawayDeliveryDetailFragment fragment = (TakeawayDeliveryDetailFragment)getFragment();
  private View view;

  public TakeawayDeliveryPromoAgent(Object paramObject)
  {
    super(paramObject);
    setupView();
  }

  private void setupView()
  {
    this.view = LayoutInflater.from(getContext()).inflate(R.layout.takeaway_delivery_promo_agent_layout, null);
    this.activityTipsView = ((TextView)this.view.findViewById(R.id.activity_tips));
    this.dpActivityView = ((LinearLayout)this.view.findViewById(R.id.dp_activity_view));
    this.dpActivityTitle = ((TextView)this.dpActivityView.findViewById(R.id.activity_title));
    this.dpActivityListView = ((LinearLayout)this.dpActivityView.findViewById(R.id.activity_list));
    this.dpActivitySelect = this.dpActivityView.findViewById(R.id.activity_selected);
    this.elemeActivityView = ((LinearLayout)this.view.findViewById(R.id.eleme_activity_view));
    this.elemeActivityTitle = ((TextView)this.elemeActivityView.findViewById(R.id.activity_title));
    this.elemeActivityListView = ((LinearLayout)this.elemeActivityView.findViewById(R.id.activity_list));
    this.elemeActivitySelect = this.elemeActivityView.findViewById(R.id.activity_selected);
    updateView();
  }

  private void updateActivityView(DPObject paramDPObject, LinearLayout paramLinearLayout1, View paramView, TextView paramTextView, LinearLayout paramLinearLayout2, int paramInt)
  {
    int k = getContext().getResources().getColor(R.color.light_red);
    paramLinearLayout1.setVisibility(0);
    if (this.dataSource.activityList.length == 1)
    {
      paramTextView.setVisibility(8);
      paramView.setVisibility(8);
      paramLinearLayout1.findViewById(R.id.activity_top_divider).setVisibility(8);
    }
    while (true)
    {
      paramTextView.setText(paramDPObject.getString("Title"));
      boolean bool;
      label107: int j;
      if (this.activityType == this.dataSource.activityType)
      {
        bool = true;
        paramView.setSelected(bool);
        paramLinearLayout1 = paramDPObject.getArray("ActivityList");
        paramDPObject = null;
        int m = paramLinearLayout1.length;
        i = 0;
        if (i >= m)
          break label310;
        paramView = paramLinearLayout1[i];
        j = paramInt;
        if (paramView.getBoolean("IsShow"))
        {
          paramDPObject = LayoutInflater.from(getContext()).inflate(R.layout.takeaway_delivery_promo_fee_item, null);
          paramDPObject.findViewById(R.id.bottom_divider).setVisibility(0);
          TextView localTextView = (TextView)paramDPObject.findViewById(R.id.name_view);
          paramTextView = (RMBLabelItem)paramDPObject.findViewById(R.id.price_view);
          localTextView.setTextColor(k);
          localTextView.setText(paramView.getObject("ActivityButton").getString("Message"));
        }
      }
      try
      {
        d = Double.parseDouble(paramView.getString("ActivityInfo"));
        paramTextView.setRMBLabelStyle6(false, getResources().getColor(R.color.deep_gray));
        paramTextView.setRMBLabelValue(d);
        paramLinearLayout2.addView(paramDPObject);
        j = paramInt + 1;
        paramDPObject = paramDPObject.findViewById(R.id.bottom_divider);
        i += 1;
        paramInt = j;
        break label107;
        paramTextView.setVisibility(0);
        paramView.setVisibility(0);
        paramLinearLayout1.findViewById(R.id.activity_top_divider).setVisibility(0);
        continue;
        bool = false;
      }
      catch (java.lang.Exception paramView)
      {
        while (true)
          double d = 0.0D;
      }
    }
    label310: if (paramDPObject != null)
      paramDPObject.setVisibility(8);
    if (paramInt > 0);
    for (int i = 0; ; i = 8)
    {
      paramLinearLayout2.setVisibility(i);
      if (this.activityType != 1)
        break;
      this.dpActivityNum = paramInt;
      return;
    }
    this.elemeActivityNum = paramInt;
  }

  private void updateView()
  {
    this.dpActivityView.setVisibility(8);
    this.elemeActivityView.setVisibility(8);
    this.dpActivityListView.removeAllViews();
    this.elemeActivityListView.removeAllViews();
    this.dpActivityNum = 0;
    this.elemeActivityNum = 0;
    if ((this.dataSource.activityList != null) && (this.dataSource.activityList.length != 0))
    {
      ViewUtils.setVisibilityAndContent(this.activityTipsView, this.dataSource.activityTips);
      Object localObject = this.dataSource.activityList;
      int j = localObject.length;
      int i = 0;
      if (i < j)
      {
        DPObject localDPObject = localObject[i];
        this.activityType = localDPObject.getInt("Type");
        switch (this.activityType)
        {
        default:
        case 1:
        case 2:
        }
        while (true)
        {
          i += 1;
          break;
          updateActivityView(localDPObject, this.dpActivityView, this.dpActivitySelect, this.dpActivityTitle, this.dpActivityListView, this.dpActivityNum);
          this.dpActivityView.setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              if (1 != TakeawayDeliveryPromoAgent.this.dataSource.activityType)
              {
                TakeawayDeliveryPromoAgent.this.dataSource.activityType = 1;
                TakeawayDeliveryPromoAgent.this.dpActivitySelect.setSelected(true);
                TakeawayDeliveryPromoAgent.this.elemeActivitySelect.setSelected(false);
                TakeawayDeliveryPromoAgent.this.dataSource.loadCause = TakeawayDeliveryDataSource.LoadCause.ACTIVITY_CHANGED;
                TakeawayDeliveryPromoAgent.this.dataSource.confirmOrderTask();
              }
            }
          });
          continue;
          updateActivityView(localDPObject, this.elemeActivityView, this.elemeActivitySelect, this.elemeActivityTitle, this.elemeActivityListView, this.elemeActivityNum);
          this.elemeActivityView.setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              if (2 != TakeawayDeliveryPromoAgent.this.dataSource.activityType)
              {
                TakeawayDeliveryPromoAgent.this.dataSource.activityType = 2;
                TakeawayDeliveryPromoAgent.this.dpActivitySelect.setSelected(false);
                TakeawayDeliveryPromoAgent.this.elemeActivitySelect.setSelected(true);
                TakeawayDeliveryPromoAgent.this.dataSource.loadCause = TakeawayDeliveryDataSource.LoadCause.ACTIVITY_CHANGED;
                TakeawayDeliveryPromoAgent.this.dataSource.confirmOrderTask();
              }
            }
          });
        }
      }
      localObject = this.view;
      if (this.dpActivityNum + this.elemeActivityNum > 0);
      for (i = 0; ; i = 8)
      {
        ((View)localObject).setVisibility(i);
        return;
      }
    }
    this.view.setVisibility(8);
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
    addCell("2000promo", this.view);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.agents.TakeawayDeliveryPromoAgent
 * JD-Core Version:    0.6.0
 */