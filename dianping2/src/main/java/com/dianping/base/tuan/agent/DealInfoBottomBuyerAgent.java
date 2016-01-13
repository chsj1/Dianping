package com.dianping.base.tuan.agent;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.widget.BuyDealView;
import com.dianping.base.widget.BuyDealView.OnBuyClickListener;
import com.dianping.v1.R.id;

public class DealInfoBottomBuyerAgent extends TuanGroupCellAgent
{
  protected static final int BUY_BUTTON_CLICKED = R.id.buy;
  protected BuyDealView buyItemView;
  protected DPObject dpDeal;

  public DealInfoBottomBuyerAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void updateView()
  {
    removeAllCells();
    this.buyItemView.setDeal(this.dpDeal);
    if ((this.fragment instanceof AgentFragment.CellStable))
    {
      ((AgentFragment.CellStable)this.fragment).setTopCell(null, this);
      ((AgentFragment.CellStable)this.fragment).setBottomCell(this.buyItemView, this);
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
    this.buyItemView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    1 local1 = new BuyDealView.OnBuyClickListener()
    {
      public void onClick(View paramView)
      {
        if (!DealInfoBottomBuyerAgent.this.handleAction(DealInfoBottomBuyerAgent.BUY_BUTTON_CLICKED))
          DealInfoBottomBuyerAgent.this.buyItemView.buy();
      }
    };
    this.buyItemView.setOnBuyClickListener(local1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoBottomBuyerAgent
 * JD-Core Version:    0.6.0
 */