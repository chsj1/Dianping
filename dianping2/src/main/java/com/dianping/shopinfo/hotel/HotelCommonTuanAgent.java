package com.dianping.shopinfo.hotel;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.common.ShopInfoTuanAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;

public class HotelCommonTuanAgent extends ShopInfoTuanAgent
{
  private static final String HOTEL_OTHER_TUAN_CELL = "7800Tuan.50Tuan";
  public DPObject TPHotelProductList;

  public HotelCommonTuanAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void renderHotelTuanCell(DPObject paramDPObject)
  {
    paramDPObject = paramDPObject.getArray("OtherProducts");
    if ((paramDPObject == null) || (paramDPObject.length == 0))
      return;
    this.linearLayout = new LinearLayout(getContext());
    this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.linearLayout.setOrientation(1);
    Object localObject = new TextView(getContext());
    ((TextView)localObject).setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 55.0F)));
    ((TextView)localObject).setPadding(ViewUtils.dip2px(getContext(), 15.0F), 0, 0, 0);
    ((TextView)localObject).setGravity(16);
    ((TextView)localObject).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_15));
    ((TextView)localObject).setTextColor(getResources().getColor(R.color.text_color_black));
    ((TextView)localObject).setText("酒店其他团购");
    this.linearLayout.addView((View)localObject);
    localObject = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
    ((View)localObject).setLayoutParams(localLayoutParams);
    ((View)localObject).setBackgroundColor(getResources().getColor(R.color.background_gray));
    this.linearLayout.addView((View)localObject);
    this.linearLayout.addView(createDealCell(paramDPObject[0], true));
    if (paramDPObject.length == 2)
    {
      setDivider(this.linearLayout);
      this.linearLayout.addView(createDealCell(paramDPObject[1], false));
    }
    if (paramDPObject.length > 2)
    {
      setDivider(this.linearLayout);
      this.linearLayout.addView(createDealCell(paramDPObject[1], false));
      this.expandLayout = new LinearLayout(getContext());
      this.expandLayout.setOrientation(1);
      if (!this.isExpand)
        this.expandLayout.setVisibility(8);
      int i = 2;
      while (i < paramDPObject.length)
      {
        setDivider(this.expandLayout);
        this.expandLayout.addView(createDealCell(paramDPObject[i], false));
        i += 1;
      }
      this.linearLayout.addView(this.expandLayout);
      this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false));
      this.expandView.setTag("EXPAND");
      this.moreText = ("全部" + paramDPObject.length + "条团购");
      ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
      this.expandView.setClickable(true);
      this.expandView.setOnClickListener(this);
      this.linearLayout.addView(this.expandView);
      setExpandState();
    }
    addCell("7800Tuan.50Tuan", this.linearLayout, 0);
  }

  private void setDivider(LinearLayout paramLinearLayout)
  {
    View localView = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
    localView.setLayoutParams(localLayoutParams);
    localView.setBackgroundColor(getResources().getColor(R.color.background_gray));
    paramLinearLayout.addView(localView);
  }

  public CommonCell createDefaultDealCell(DPObject paramDPObject, boolean paramBoolean)
  {
    CommonCell localCommonCell = (CommonCell)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.hotel_othertuan_cell, getParentView(), false);
    localCommonCell.setGAString("tuan", getGAExtra());
    localCommonCell.setTitle(paramDPObject.getString("ContentTitle"));
    localCommonCell.setClickable(true);
    localCommonCell.setOnClickListener(this);
    double d = paramDPObject.getDouble("Price");
    paramDPObject.getDouble("OriginalPrice");
    try
    {
      if (paramDPObject.getDouble("MarketPrice") > 0.0D)
        paramDPObject.getDouble("MarketPrice");
      label94: SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
      SpannableString localSpannableString = new SpannableString("￥" + PRICE_DF.format(d));
      localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
      localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, localSpannableString.length(), 33);
      localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, localSpannableString.length(), 33);
      localSpannableStringBuilder.append(localSpannableString);
      localCommonCell.setSubTitle(localSpannableStringBuilder);
      localCommonCell.setTag(paramDPObject);
      if (!paramBoolean)
        setBackground(localCommonCell, 0);
      this.tuanCells.append(paramDPObject.getInt("ID"), localCommonCell);
      paramDPObject = (LinearLayout.LayoutParams)localCommonCell.getLayoutParams();
      paramDPObject.leftMargin = ViewUtils.dip2px(getContext(), 0.0F);
      localCommonCell.setLayoutParams(paramDPObject);
      return localCommonCell;
    }
    catch (Exception localException)
    {
      break label94;
    }
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if (paramAgentMessage.what.equals("com.dianping.shopinfo.hotel.HotelTuanAgent.HOTEL_TUAN_LOAD_DATA_HOTEL_PRODUCTS"))
      onAgentChanged(paramAgentMessage.body);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (paramBundle == null);
    do
    {
      do
        return;
      while (!"shopinfo/hoteltuan".equalsIgnoreCase(paramBundle.getString("_host")));
      this.TPHotelProductList = ((DPObject)paramBundle.getParcelable("TPHotelProductList"));
      this.Deals = this.TPHotelProductList;
    }
    while (this.TPHotelProductList == null);
    removeAllCells();
    renderHotelTuanCell(this.TPHotelProductList);
  }

  public void onClick(View paramView)
  {
    super.onClick(paramView);
    if ((paramView.getTag() instanceof DPObject))
    {
      DPObject localDPObject = (DPObject)paramView.getTag();
      paramView = new GAUserInfo();
      if (localDPObject != null)
        paramView.deal_id = Integer.valueOf(localDPObject.getInt("ID"));
      paramView.shop_id = Integer.valueOf(shopId());
      paramView.query_id = getFragment().getStringParam("query_id");
      localDPObject = getShop();
      if (localDPObject != null)
        paramView.category_id = Integer.valueOf(localDPObject.getInt("CategoryID"));
      GAHelper.instance().contextStatisticsEvent(getContext(), "hotel_tg_other", paramView, "tap");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelCommonTuanAgent
 * JD-Core Version:    0.6.0
 */