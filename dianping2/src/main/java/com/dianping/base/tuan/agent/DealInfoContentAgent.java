package com.dianping.base.tuan.agent;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.RefundSupport;
import com.dianping.base.tuan.widget.RefundSupport.Type;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.MeasuredTextView;
import com.dianping.loader.MyResources;
import com.dianping.util.DateUtil;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Deprecated
public class DealInfoContentAgent extends TuanGroupCellAgent
{
  protected static final String REFUND_SUPPORT_URL = "http://m.dianping.com/events/apprefund";
  protected TextView content;
  protected View contentView;
  private DateFormat df = new SimpleDateFormat("yy年MM月dd日 HH:mm", Locale.getDefault());
  protected DPObject dpDeal;
  protected LinearLayout eventLayout;
  protected RefundSupport mRefundSupport;
  protected TextView region;
  protected TextView remainTime;
  protected TextView shortTitle;

  public DealInfoContentAgent(Object paramObject)
  {
    super(paramObject);
  }

  public static String format2RemainTime(Date paramDate)
  {
    if ((paramDate == null) || (paramDate.getTime() < 1044028800000L))
      return "";
    StringBuilder localStringBuilder = new StringBuilder();
    long l = DateUtil.currentTimeMillis();
    if (paramDate.getTime() <= l)
      return "";
    l = (paramDate.getTime() - l) / 60000L;
    int i = (int)(l / 1440L);
    l %= 1440L;
    int j = (int)(l / 60L);
    int k = (int)(l % 60L);
    if (i >= 3)
      return "3天以上";
    if (i > 0)
      localStringBuilder.append(i).append("天");
    if (j > 0)
      localStringBuilder.append(j).append("小时");
    if (k > 0)
      localStringBuilder.append(k).append("分");
    if (localStringBuilder.length() == 0)
      return "";
    return localStringBuilder.toString();
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
    this.contentView = this.res.inflate(getContext(), R.layout.tuan_deal_info_item, getParentView(), false);
    this.remainTime = ((TextView)this.contentView.findViewById(R.id.remain_time));
    this.region = ((TextView)this.contentView.findViewById(R.id.region_name));
    this.shortTitle = ((TextView)this.contentView.findViewById(R.id.short_title));
    this.content = ((TextView)this.contentView.findViewById(R.id.title));
    this.mRefundSupport = ((RefundSupport)this.contentView.findViewById(R.id.refund_support));
    this.eventLayout = ((LinearLayout)this.contentView.findViewById(R.id.event_info_ll));
  }

  protected void updateDealTime(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    int i = paramDPObject.getInt("Status");
    if ((i & 0x10) != 0)
      paramDPObject = "开始时间：" + this.df.format(new Date(paramDPObject.getTime("Time")));
    while (true)
    {
      this.remainTime.setText(paramDPObject);
      return;
      if (((i & 0x2) != 0) || ((i & 0x4) != 0))
      {
        paramDPObject = "结束于" + this.df.format(new Date(paramDPObject.getTime("Time")));
        continue;
      }
      String str = format2RemainTime(new Date(paramDPObject.getTime("Time")));
      paramDPObject = str;
      if (android.text.TextUtils.isEmpty(str))
      {
        this.remainTime.setVisibility(8);
        continue;
      }
      this.remainTime.setVisibility(0);
    }
  }

  protected void updateView()
  {
    removeAllCells();
    if (this.dpDeal == null)
      return;
    int j = this.dpDeal.getInt("DealType");
    updateDealTime(this.dpDeal);
    this.eventLayout.removeAllViews();
    this.eventLayout.setOnClickListener(null);
    Object localObject = this.dpDeal.getArray("EventList");
    if (!DPObjectUtils.isArrayEmpty(localObject))
    {
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
      localLayoutParams.setMargins(0, 5, 0, 5);
      int i = 0;
      while (i < localObject.length)
      {
        LinearLayout localLinearLayout = (LinearLayout)this.res.inflate(getContext(), R.layout.tuan_deal_event_info, null, false);
        if (!android.text.TextUtils.isEmpty(localObject[i].getString("ShortTitle")))
        {
          MeasuredTextView localMeasuredTextView = (MeasuredTextView)localLinearLayout.findViewById(R.id.event_hint);
          localMeasuredTextView.setFlag(false);
          localMeasuredTextView.setText("");
          localMeasuredTextView.setBackgroundResource(0);
          int k = ViewUtils.dip2px(getContext(), 5.0F);
          int m = ViewUtils.dip2px(getContext(), 3.0F);
          localMeasuredTextView.setBackgroundResource(R.drawable.dealinfo_event_round_corner_rectangle);
          localMeasuredTextView.setPadding(k, m, k, m);
          localMeasuredTextView.setText(localObject[i].getString("ShortTitle"));
          localMeasuredTextView.setVisibility(0);
        }
        if (!android.text.TextUtils.isEmpty(localObject[i].getString("Desc")))
          ((TextView)localLinearLayout.findViewById(R.id.event_desc)).setText(localObject[i].getString("Desc"));
        this.eventLayout.addView(localLinearLayout, localLayoutParams);
        i += 1;
      }
      this.eventLayout.setVisibility(0);
      this.contentView.findViewById(R.id.event_and_refund_divider).setVisibility(0);
      if (android.text.TextUtils.isEmpty(this.dpDeal.getString("RegionName")))
        break label577;
      this.region.setText("【" + this.dpDeal.getString("RegionName") + "】");
      this.region.setPadding(0, 0, ViewUtils.dip2px(getContext(), 4.0F), 0);
      label383: this.shortTitle.setText(this.dpDeal.getString("ShortTitle"));
      this.content.setText(this.dpDeal.getString("DealTitle"));
      localObject = com.dianping.util.TextUtils.jsonParseText(this.dpDeal.getString("SalesDesc"));
      if (j != 4)
        break label591;
      this.mRefundSupport.setType(RefundSupport.Type.PREPAID, this.dpDeal.getInt("Tag"), (CharSequence)localObject);
    }
    while (true)
    {
      this.mRefundSupport.setOnClickListener(new DealInfoContentAgent.1(this));
      this.mRefundSupport.setBackgroundColor(0);
      if ((this.dpDeal.getInt("RemainCount") > 0) && (this.dpDeal.getInt("RemainCount") < 100) && (this.mRefundSupport.getCounIcon() != null))
        this.mRefundSupport.getCounIcon().setVisibility(8);
      addCell("010Basic.030Content0", this.contentView);
      addEmptyCell("010Basic.030Content1");
      return;
      this.eventLayout.setVisibility(8);
      this.contentView.findViewById(R.id.event_and_refund_divider).setVisibility(8);
      break;
      label577: this.region.setPadding(0, 0, 0, 0);
      break label383;
      label591: if (j == 3)
      {
        this.mRefundSupport.setType(RefundSupport.Type.LOTTERY, this.dpDeal.getInt("Tag"), (CharSequence)localObject);
        continue;
      }
      if (j == 2)
      {
        this.mRefundSupport.setType(RefundSupport.Type.DELIVERY, this.dpDeal.getInt("Tag"), (CharSequence)localObject);
        continue;
      }
      this.mRefundSupport.setType(RefundSupport.Type.COMMON, this.dpDeal.getInt("Tag"), (CharSequence)localObject);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoContentAgent
 * JD-Core Version:    0.6.0
 */