package com.dianping.base.tuan.agent;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.RefundSupport;
import com.dianping.base.tuan.widget.RefundSupport.Type;
import com.dianping.loader.MyResources;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DealInfoRefundAgent extends TuanGroupCellAgent
{
  protected static final String REFUND_SUPPORT_URL = "http://m.dianping.com/events/apprefund";
  protected View contentView;
  private DateFormat df = new SimpleDateFormat("yy年MM月dd日 HH:mm", Locale.getDefault());
  protected DPObject dpDeal;
  protected RefundSupport mRefundSupport;

  public DealInfoRefundAgent(Object paramObject)
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
    this.contentView = this.res.inflate(getContext(), R.layout.tuan_deal_info_refund, getParentView(), false);
    this.mRefundSupport = ((RefundSupport)this.contentView.findViewById(R.id.refund_support));
  }

  protected void updateView()
  {
    removeAllCells();
    if (this.dpDeal == null)
      return;
    int i = this.dpDeal.getInt("DealType");
    SpannableStringBuilder localSpannableStringBuilder = TextUtils.jsonParseText(this.dpDeal.getString("SalesDesc"));
    if (i == 4)
      this.mRefundSupport.setType(RefundSupport.Type.PREPAID, this.dpDeal.getInt("Tag"), localSpannableStringBuilder);
    while (true)
    {
      this.mRefundSupport.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          DealInfoRefundAgent.this.startActivity("dianping://web?url=http://m.dianping.com/events/apprefund");
        }
      });
      this.mRefundSupport.setBackgroundColor(0);
      if ((this.dpDeal.getInt("RemainCount") > 0) && (this.dpDeal.getInt("RemainCount") < 100) && (this.mRefundSupport.getCounIcon() != null))
        this.mRefundSupport.getCounIcon().setVisibility(8);
      addCell("010Basic.050Refund", this.contentView);
      return;
      if (i == 3)
      {
        this.mRefundSupport.setType(RefundSupport.Type.LOTTERY, this.dpDeal.getInt("Tag"), localSpannableStringBuilder);
        continue;
      }
      if (i == 2)
      {
        this.mRefundSupport.setType(RefundSupport.Type.DELIVERY, this.dpDeal.getInt("Tag"), localSpannableStringBuilder);
        continue;
      }
      this.mRefundSupport.setType(RefundSupport.Type.COMMON, this.dpDeal.getInt("Tag"), localSpannableStringBuilder);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoRefundAgent
 * JD-Core Version:    0.6.0
 */