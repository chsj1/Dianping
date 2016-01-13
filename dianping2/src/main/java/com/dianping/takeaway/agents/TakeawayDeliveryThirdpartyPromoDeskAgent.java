package com.dianping.takeaway.agents;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.loader.MyResources;
import com.dianping.takeaway.entity.TakeawayDeliveryDataSource;
import com.dianping.takeaway.fragment.TakeawayDeliveryDetailFragment;
import com.dianping.takeaway.util.TakeawayPreferencesManager;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TakeawayDeliveryThirdpartyPromoDeskAgent extends CellAgent
{
  private View arrowView;
  private View cantUseView;
  private TakeawayDeliveryDataSource dataSource = this.fragment.getDataSource();
  private TakeawayDeliveryDetailFragment fragment = (TakeawayDeliveryDetailFragment)getFragment();
  private TextView nameView;
  private RMBLabelItem reducePriceView;
  private View view;

  public TakeawayDeliveryThirdpartyPromoDeskAgent(Object paramObject)
  {
    super(paramObject);
    setupView();
  }

  private void setupView()
  {
    this.view = LayoutInflater.from(getContext()).inflate(R.layout.takeaway_delivery_eleme_promo_agent_layout, null);
    this.view.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
    this.nameView = ((TextView)this.view.findViewById(R.id.name_view));
    this.reducePriceView = ((RMBLabelItem)this.view.findViewById(R.id.reduce_price_view));
    this.arrowView = this.view.findViewById(R.id.arrow_view);
    this.cantUseView = this.view.findViewById(R.id.cant_use_view);
  }

  private void updateThirdpartyCouponStatus()
  {
    switch (this.dataSource.thirdpartyCouponStatus)
    {
    default:
      return;
    case 0:
      this.view.setVisibility(8);
      return;
    case 1:
      this.view.setVisibility(0);
      this.cantUseView.setVisibility(0);
      this.arrowView.setVisibility(8);
      this.reducePriceView.setVisibility(8);
      this.view.setClickable(false);
      return;
    case 2:
    }
    this.view.setVisibility(0);
    this.cantUseView.setVisibility(8);
    this.arrowView.setVisibility(0);
    this.reducePriceView.setVisibility(0);
    this.view.setClickable(true);
  }

  private void updateView()
  {
    if (!TextUtils.isEmpty(this.dataSource.thirdpartyCouponTitle))
      this.nameView.setText(this.dataSource.thirdpartyCouponTitle);
    if (!TextUtils.isEmpty(this.dataSource.thirdpartyCouponReduce));
    while (true)
    {
      try
      {
        d = Double.parseDouble(this.dataSource.thirdpartyCouponReduce);
        d = -d;
        this.reducePriceView.setRMBLabelStyle6(false, getResources().getColor(R.color.deep_gray));
        this.reducePriceView.setRMBLabelValue(d);
        this.reducePriceView.setVisibility(0);
        this.view.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            Object localObject = TakeawayPreferencesManager.getTakeawayDeliveryPreferences(TakeawayDeliveryThirdpartyPromoDeskAgent.this.getContext());
            paramView = ((SharedPreferences)localObject).getString("eleme_phone", null);
            localObject = ((SharedPreferences)localObject).getString("eleme_token", null);
            if ((!TextUtils.isEmpty(paramView)) && (!TextUtils.isEmpty((CharSequence)localObject)))
            {
              TakeawayDeliveryThirdpartyPromoDeskAgent.this.dataSource.confirmCouponTask();
              return;
            }
            TakeawayDeliveryThirdpartyPromoDeskAgent.this.dataSource.getUserBindPhoneTask();
          }
        });
        updateThirdpartyCouponStatus();
        return;
      }
      catch (Exception localException)
      {
        double d = 0.0D;
        continue;
      }
      this.reducePriceView.setVisibility(8);
    }
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
    addCell("4000thirdpartypromo", this.view);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.agents.TakeawayDeliveryThirdpartyPromoDeskAgent
 * JD-Core Version:    0.6.0
 */