package com.dianping.hotel.deal.agent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.hotel.deal.agent.common.BaseHotelPariAgent;

public class HotelProdPackageDetailsAgent extends BaseHotelPariAgent
{
  private DPObject dpHotelProdBase;

  public HotelProdPackageDetailsAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected int getPariType()
  {
    return 12;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      this.dpHotelProdBase = ((DPObject)paramBundle.getParcelable("hotelprodbase"));
      if (this.dpHotelProdBase == null);
    }
  }

  public void onFooterViewClick(View paramView)
  {
    paramView = this.dpHotelProdBase.getString("MoreDetailUrl");
    if (paramView != null)
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web").buildUpon().appendQueryParameter("url", Uri.parse(paramView).toString()).build());
      getContext().startActivity(paramView);
    }
  }

  public void setupView()
  {
    super.setupView();
    this.footerTextView.setVisibility(0);
    this.commCell.addContent(this.footerTextView, false, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HotelProdPackageDetailsAgent.this.onFooterViewClick(paramView);
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdPackageDetailsAgent
 * JD-Core Version:    0.6.0
 */