package com.dianping.base.tuan.agent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.MeasuredTextView;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class DealInfoEventAgent extends TuanGroupCellAgent
{
  protected View contentView;
  protected DPObject dpDeal;
  protected LinearLayout eventLayout;

  public DealInfoEventAgent(Object paramObject)
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
    if (this.contentView == null)
      setupView();
    updateView();
  }

  protected void setupView()
  {
    this.contentView = this.res.inflate(getContext(), R.layout.tuan_deal_info_event_list, getParentView(), false);
    this.eventLayout = ((LinearLayout)this.contentView.findViewById(R.id.event_info_ll));
  }

  protected void updateView()
  {
    removeAllCells();
    if (this.dpDeal == null)
      return;
    this.eventLayout.removeAllViews();
    this.eventLayout.setOnClickListener(null);
    DPObject[] arrayOfDPObject = this.dpDeal.getArray("EventList");
    if (!DPObjectUtils.isArrayEmpty(arrayOfDPObject))
    {
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
      localLayoutParams.setMargins(0, 5, 0, 5);
      int i = 0;
      while (i < arrayOfDPObject.length)
      {
        LinearLayout localLinearLayout = (LinearLayout)this.res.inflate(getContext(), R.layout.tuan_deal_event_info, null, false);
        if (!TextUtils.isEmpty(arrayOfDPObject[i].getString("ShortTitle")))
        {
          MeasuredTextView localMeasuredTextView = (MeasuredTextView)localLinearLayout.findViewById(R.id.event_hint);
          localMeasuredTextView.setFlag(false);
          localMeasuredTextView.setText("");
          localMeasuredTextView.setBackgroundResource(0);
          int j = ViewUtils.dip2px(getContext(), 5.0F);
          int k = ViewUtils.dip2px(getContext(), 3.0F);
          localMeasuredTextView.setBackgroundResource(R.drawable.dealinfo_event_round_corner_rectangle);
          localMeasuredTextView.setPadding(j, k, j, k);
          localMeasuredTextView.setText(arrayOfDPObject[i].getString("ShortTitle"));
          localMeasuredTextView.setVisibility(0);
        }
        if (!TextUtils.isEmpty(arrayOfDPObject[i].getString("Desc")))
          ((TextView)localLinearLayout.findViewById(R.id.event_desc)).setText(arrayOfDPObject[i].getString("Desc"));
        this.eventLayout.addView(localLinearLayout, localLayoutParams);
        i += 1;
      }
      this.eventLayout.setVisibility(0);
      addCell("010Basic.040Event", this.contentView);
      return;
    }
    this.eventLayout.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoEventAgent
 * JD-Core Version:    0.6.0
 */