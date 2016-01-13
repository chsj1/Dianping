package com.dianping.shopinfo.common;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.activity.ShopInfoActivity;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.shopinfo.widget.TuanTicketCell;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShopTuanAgent extends ShopInfoTuanAgent
{
  private static final String CELL_TUAN = "0475HuiPay.30Hui";
  private DPObject[] couponDeals;
  private DPObject[] tuanDeals;

  public ShopTuanAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void createTuanDealsView()
  {
    if (this.tuanDeals == null);
    do
    {
      do
        return;
      while ((this.tuanDeals == null) || (this.tuanDeals.length == 0));
      this.linearLayout = new LinearLayout(getContext());
      this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      this.linearLayout.setOrientation(1);
      this.linearLayout.addView(createDefaultDealCell(this.tuanDeals[0], 0));
      if (this.tuanDeals.length == 2)
      {
        this.linearLayout.addView(line());
        this.linearLayout.addView(createDefaultDealCell(this.tuanDeals[1], 1));
      }
      if (this.tuanDeals.length > 2)
      {
        this.linearLayout.addView(line());
        this.linearLayout.addView(createDefaultDealCell(this.tuanDeals[1], 1));
        this.expandLayout = new LinearLayout(getContext());
        this.expandLayout.setOrientation(1);
        if (!this.isExpand)
          this.expandLayout.setVisibility(8);
        int i = 2;
        int j = this.tuanDeals.length;
        while (i < j)
        {
          this.expandLayout.addView(line());
          this.expandLayout.addView(createDefaultDealCell(this.tuanDeals[i], i));
          i += 1;
        }
        this.linearLayout.addView(this.expandLayout);
        this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false));
        this.expandView.setGAString("tuan_more");
        this.expandView.setTag("EXPAND");
        this.expandView.setPadding(ViewUtils.dip2px(getContext(), 47.0F), 0, 0, 0);
        this.moreText = ("更多" + (this.tuanDeals.length - 2) + "个团购");
        ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
        this.expandView.setClickable(true);
        this.expandView.setOnClickListener(this);
        this.linearLayout.addView(this.expandView);
        setExpandState();
      }
      updateDealSaleCount();
      addCell("0475HuiPay.30Hui", this.linearLayout, 64);
      ShopInfoActivity.speedTest(getContext(), 1013);
    }
    while (this.dealDiscountList == null);
    updateTuanDealTags();
  }

  private void splitDeals()
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    DPObject[] arrayOfDPObject = super.getDeals().getArray("List");
    int i = 0;
    if (i < arrayOfDPObject.length)
    {
      DPObject localDPObject = arrayOfDPObject[i];
      if (localDPObject.getInt("DealType") == 2)
        localArrayList2.add(localDPObject);
      while (true)
      {
        i += 1;
        break;
        localArrayList1.add(localDPObject);
      }
    }
    i = localArrayList1.size();
    if (i > 0)
      this.tuanDeals = ((DPObject[])(DPObject[])localArrayList1.toArray(new DPObject[i]));
    i = localArrayList2.size();
    if (i > 0)
      this.couponDeals = ((DPObject[])(DPObject[])localArrayList2.toArray(new DPObject[i]));
  }

  public CommonCell createDefaultDealCell(DPObject paramDPObject, int paramInt)
  {
    TuanTicketCell localTuanTicketCell = (TuanTicketCell)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.tuan_cell_shopinfo_icon_v2, getParentView(), false);
    if (paramInt == 0);
    for (paramInt = 0; ; paramInt = 4)
    {
      localTuanTicketCell.findViewById(16908295).setVisibility(paramInt);
      localTuanTicketCell.setGAString("tuan", getGAExtra());
      localTuanTicketCell.setSubTitle(paramDPObject.getString("ContentTitle"));
      localTuanTicketCell.setClickable(true);
      localTuanTicketCell.setOnClickListener(this);
      double d1 = paramDPObject.getDouble("Price");
      double d2 = paramDPObject.getDouble("OriginalPrice");
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
      SpannableString localSpannableString = new SpannableString("￥" + PRICE_DF.format(d1));
      localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
      localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, localSpannableString.length(), 33);
      localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, localSpannableString.length(), 33);
      localSpannableStringBuilder.append(localSpannableString);
      localSpannableStringBuilder.append(" ");
      localSpannableString = new SpannableString("￥" + PRICE_DF.format(d2));
      localSpannableString.setSpan(new StrikethroughSpan(), 1, localSpannableString.length(), 33);
      localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_hint)), 0, localSpannableString.length(), 33);
      localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), 0, localSpannableString.length(), 33);
      localSpannableStringBuilder.append(localSpannableString);
      localTuanTicketCell.setTitle(localSpannableStringBuilder);
      localTuanTicketCell.setTag(paramDPObject);
      if (paramDPObject.getString("Photo") != null)
        ((NetworkImageView)localTuanTicketCell.findViewById(R.id.icon)).setImage(paramDPObject.getString("Photo"));
      setBackground(localTuanTicketCell.findViewById(R.id.layout), 0);
      this.tuanCells.append(paramDPObject.getInt("ID"), localTuanTicketCell);
      return localTuanTicketCell;
    }
  }

  protected DPObject getDeals()
  {
    return new DPObject().edit().putArray("List", this.tuanDeals).generate();
  }

  protected View line()
  {
    this.line = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 47.0F);
    this.line.setLayoutParams(localLayoutParams);
    this.line.setBackgroundResource(R.color.background_gray);
    return this.line;
  }

  protected void setupView()
  {
    if (!this.hasRequested)
    {
      ShopInfoActivity.speedTest(getContext(), 1011);
      sendGouponRequest();
      this.hasRequested = true;
    }
    do
      return;
    while (super.getDeals() == null);
    setShopDeals();
    splitDeals();
    DPObject localDPObject = new DPObject().edit().putArray("CouponDeals", this.couponDeals).generate();
    setSharedObject("CouponDeals", localDPObject);
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("CouponDeals", localDPObject);
    dispatchAgentChanged("shopinfo/common_coupon", localBundle);
    createTuanDealsView();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.ShopTuanAgent
 * JD-Core Version:    0.6.0
 */