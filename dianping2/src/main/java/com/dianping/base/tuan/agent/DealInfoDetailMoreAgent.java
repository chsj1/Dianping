package com.dianping.base.tuan.agent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.layout;

public class DealInfoDetailMoreAgent extends TuanGroupCellAgent
  implements View.OnClickListener
{
  private View contentView;
  public DPObject dpDeal;

  public DealInfoDetailMoreAgent(Object paramObject)
  {
    super(paramObject);
  }

  public View getView()
  {
    if (this.contentView == null)
      setupView();
    return this.contentView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null);
    do
    {
      return;
      if (paramBundle == null)
        continue;
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal == paramBundle)
        continue;
      this.dpDeal = paramBundle;
    }
    while (this.dpDeal == null);
    removeAllCells();
    if (this.contentView == null)
      setupView();
    addCell("050DetailMore.01DetailMore", this.contentView);
  }

  public void onClick(View paramView)
  {
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://dealdetailmore"));
    paramView.putExtra("mDeal", this.dpDeal);
    getContext().startActivity(paramView);
  }

  public void setDealObject(DPObject paramDPObject)
  {
    this.dpDeal = paramDPObject;
  }

  public void setupView()
  {
    this.contentView = this.res.inflate(getContext(), R.layout.tuan_detail_more, getParentView(), false);
    this.contentView.setOnClickListener(this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoDetailMoreAgent
 * JD-Core Version:    0.6.0
 */