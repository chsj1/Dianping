package com.dianping.base.tuan.agent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.utils.TuanSharedDataKey;

public class DealInfoReviewAgent extends TuanReviewAgent
{
  private int dealid;
  protected DPObject dpDeal;
  private int reviewType;
  private int tagType;

  public DealInfoReviewAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void doClick()
  {
    if (this.reviewType == 1)
    {
      localObject = new StringBuilder("dianping://review?type=1");
      if (this.tagType != 0)
        ((StringBuilder)localObject).append("&tagtype=").append(this.tagType);
      ((StringBuilder)localObject).append("&dealid=" + this.dpDeal.getInt("ID"));
      DPObject localDPObject = getSharedDPObject(TuanSharedDataKey.DEAL_BEST_SHOP);
      if (localDPObject != null)
        ((StringBuilder)localObject).append("&bestshopid=" + localDPObject.getInt("ID"));
      localObject = new Intent("android.intent.action.VIEW", Uri.parse(((StringBuilder)localObject).toString()));
      getContext().startActivity((Intent)localObject);
      return;
    }
    Object localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandealreviews?dealid=" + this.dealid));
    getContext().startActivity((Intent)localObject);
  }

  private void parseData()
  {
    this.CELL_ID = "020Review.01Review";
    this.mReviewRatio = this.dpDeal.getString("ReviewRatio");
    this.mReviewCount = this.dpDeal.getString("TotalReview");
    this.mGAString = "review";
    this.tagType = this.dpDeal.getInt("TagType");
    this.reviewType = this.dpDeal.getInt("ReviewType");
    this.dealid = this.dpDeal.getInt("ID");
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
    parseData();
    updateAgent();
    setReviewOnClickListener(new TuanReviewAgent.ReviewOnClickListener()
    {
      public void onClick(View paramView)
      {
        DealInfoReviewAgent.this.doClick();
      }
    });
    ((NovaActivity)getContext()).addGAView(getAgentView(), -1, "tuandeal", "tuandeal".equals(((NovaActivity)getContext()).getPageName()));
  }

  public Bundle saveInstanceState()
  {
    return super.saveInstanceState();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoReviewAgent
 * JD-Core Version:    0.6.0
 */