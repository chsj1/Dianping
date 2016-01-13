package com.dianping.base.tuan.agent;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.widget.BuyDealView;
import com.dianping.base.widget.BuyDealView.OnBuyClickListener;
import com.dianping.v1.R.id;

public class DealInfoBuyerAgent extends TuanGroupCellAgent
{
  protected static final int BUY_BUTTON_CLICKED = R.id.buy;
  protected BuyDealView buyItemView;
  protected BuyDealView buyItemViewTop;
  protected DPObject dpDeal;

  public DealInfoBuyerAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void updateView()
  {
    removeAllCells();
    this.buyItemView.setDeal(this.dpDeal);
    this.buyItemViewTop.setDeal(this.dpDeal);
    addCell("010Basic.020Buyer", this.buyItemView);
    if ((this.fragment instanceof AgentFragment.CellStable))
    {
      ((AgentFragment.CellStable)this.fragment).setBottomCell(null, this);
      ((AgentFragment.CellStable)this.fragment).setTopCell(this.buyItemViewTop, this);
    }
  }

  public View getView()
  {
    if (this.buyItemView == null)
      setupView();
    return this.buyItemView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != paramBundle)
        this.dpDeal = paramBundle;
    }
    if (getContext() == null);
    do
      return;
    while (this.dpDeal == null);
    if (this.buyItemView == null)
      setupView();
    updateView();
  }

  protected void setupView()
  {
    this.buyItemView = new BuyDealView(getContext());
    this.buyItemViewTop = new BuyDealView(getContext());
    this.buyItemView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    this.buyItemViewTop.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    1 local1 = new BuyDealView.OnBuyClickListener()
    {
      public void onClick(View paramView)
      {
        if (!DealInfoBuyerAgent.this.handleAction(DealInfoBuyerAgent.BUY_BUTTON_CLICKED))
          DealInfoBuyerAgent.this.buyItemView.buy();
      }
    };
    this.buyItemView.setOnBuyClickListener(local1);
    this.buyItemViewTop.setOnBuyClickListener(local1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoBuyerAgent
 * JD-Core Version:    0.6.0
 */