package com.dianping.shopinfo.hotel.senic;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ScenicTypicalTicketAgent extends ShopCellAgent
{
  private static final String FUN_BOOK = "0460fun.10book";
  protected static final DecimalFormat PRICE_DF = new DecimalFormat("#.###");
  private static final String RMB = "￥";
  private ScenicOnceGATool ONCE_GA_TOOL = new ScenicOnceGATool("scenic_ticket_show", new String[0]);
  DPObject TypicalTicketGroup;
  DPObject[] TypicalTicketList;
  private NovaLinearLayout expandView;
  ArrayList<Integer> expandsItemStatus;
  ArrayList<Boolean> expandsStatus;
  View line;
  private LinearLayout linearLayout;
  protected MApiRequest request;

  public ScenicTypicalTicketAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createTicketItem(DPObject paramDPObject)
  {
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_scenic_new_ticket_item, null);
    localNovaRelativeLayout.setLayoutParams(new ViewGroup.MarginLayoutParams(-1, -2));
    localNovaRelativeLayout.setPadding(0, ViewUtils.dip2px(getContext(), 8.0F), 0, ViewUtils.dip2px(getContext(), 8.0F));
    NovaButton localNovaButton = (NovaButton)localNovaRelativeLayout.findViewById(R.id.preorder);
    Object localObject4 = (TextView)localNovaRelativeLayout.findViewById(R.id.ticket_name);
    Object localObject2 = (TextView)localNovaRelativeLayout.findViewById(R.id.ticket_price);
    Object localObject3 = (TextView)localNovaRelativeLayout.findViewById(R.id.ticket_tag);
    LinearLayout localLinearLayout = (LinearLayout)localNovaRelativeLayout.findViewById(R.id.layout_promo);
    Object localObject1 = (LinearLayout)localNovaRelativeLayout.findViewById(R.id.layout_extra);
    localNovaButton.setText("购买");
    ((TextView)localObject4).setText(paramDPObject.getString("TicketTitle"));
    ((TextView)localObject4).setTextColor(getResources().getColor(R.color.tuan_common_orange));
    localObject4 = new SpannableString("￥" + PRICE_DF.format(paramDPObject.getDouble("Price")));
    ((SpannableString)localObject4).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
    ((SpannableString)localObject4).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, ((SpannableString)localObject4).length(), 33);
    ((SpannableString)localObject4).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, ((SpannableString)localObject4).length(), 33);
    ((TextView)localObject2).setText((CharSequence)localObject4);
    if (!TextUtils.isEmpty(paramDPObject.getString("TitleTag")))
      ((TextView)localObject3).setText(paramDPObject.getString("TitleTag"));
    int i;
    while (true)
    {
      localObject2 = paramDPObject.getStringArray("InfoList");
      if ((localObject2 != null) && (localObject2.length > 0))
        i = 0;
      while (true)
        if (i < localObject2.length)
        {
          localObject3 = new TextView(getContext());
          ((TextView)localObject3).setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
          ((TextView)localObject3).setPadding(0, ViewUtils.dip2px(getContext(), 6.0F), 0, 0);
          ((TextView)localObject3).setTextColor(getResources().getColor(R.color.tuan_common_gray));
          ((TextView)localObject3).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
          ((TextView)localObject3).setText(localObject2[i]);
          ((TextView)localObject3).setMaxLines(2);
          ((TextView)localObject3).setEllipsize(TextUtils.TruncateAt.END);
          ((LinearLayout)localObject1).addView((View)localObject3);
          i += 1;
          continue;
          ((TextView)localObject3).setVisibility(8);
          break;
          ((LinearLayout)localObject1).setVisibility(8);
        }
    }
    localObject1 = paramDPObject.getArray("PromoTagList");
    if ((localObject1 != null) && (localObject1.length > 0))
      i = 0;
    while (i < localObject1.length)
    {
      localObject2 = new TextView(getContext());
      localObject3 = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject3).setMargins(0, ViewUtils.dip2px(getContext(), 2.0F), 0, 0);
      ((TextView)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject3);
      ((TextView)localObject2).setText(localObject1[i].getString("Title"));
      ((TextView)localObject2).setTextColor(getResources().getColor(R.color.hotel_calender_weekend_color));
      ((TextView)localObject2).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_10));
      ((TextView)localObject2).setBackgroundResource(R.drawable.hotel_promo_border);
      ((TextView)localObject2).setSingleLine();
      localLinearLayout.addView((View)localObject2);
      i += 1;
      continue;
      localLinearLayout.setVisibility(8);
    }
    localNovaButton.setGAString("scenic_ticket_booking");
    localNovaButton.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        try
        {
          paramView = this.val$ticket.getString("BuyUrl");
          if ((!TextUtils.isEmpty(paramView)) || ("null".equalsIgnoreCase(paramView)))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            ScenicTypicalTicketAgent.this.startActivity(paramView);
          }
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
    localNovaRelativeLayout.setGAString("scenic_ticket_detail");
    localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        try
        {
          paramView = this.val$ticket.getString("Url");
          if ((!TextUtils.isEmpty(paramView)) || ("null".equalsIgnoreCase(paramView)))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            ScenicTypicalTicketAgent.this.startActivity(paramView);
          }
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
    return (View)(View)(View)(View)localNovaRelativeLayout;
  }

  private void createTypicalTicketGroup()
  {
    int i;
    label20: label49: Object localObject;
    View localView;
    if (this.expandsStatus == null)
    {
      this.expandsStatus = new ArrayList();
      i = 0;
      if (i >= this.TypicalTicketList.length)
        return;
      if (i != 0)
        break label178;
      setTypicalTicketGroupTitle();
      this.expandsStatus.add(Boolean.valueOf(true));
      if (this.expandsItemStatus == null)
        this.expandsItemStatus = new ArrayList();
      this.expandsItemStatus.add(Integer.valueOf(0));
      localObject = this.TypicalTicketList[i];
      localView = createTypicalTitle((DPObject)localObject);
      localObject = createTypicalItemAll((DPObject)localObject, i);
      if (i <= 0)
        break label193;
      ((View)localObject).setVisibility(8);
      localView.findViewById(R.id.up_trig_sep).setVisibility(4);
    }
    while (true)
    {
      localView.setOnClickListener(new View.OnClickListener(i, (View)localObject, localView, (ImageView)localView.findViewById(R.id.arrow))
      {
        public void onClick(View paramView)
        {
          boolean bool = ((Boolean)ScenicTypicalTicketAgent.this.expandsStatus.get(this.val$p)).booleanValue();
          int i;
          if (bool)
          {
            this.val$contentView.setVisibility(8);
            this.val$titleView.findViewById(R.id.up_trig_sep).setVisibility(4);
            this.val$arrow.setBackgroundResource(R.drawable.mini_arrow_down);
            paramView = ScenicTypicalTicketAgent.this.expandsStatus;
            i = this.val$p;
            if (bool)
              break label123;
          }
          label123: for (bool = true; ; bool = false)
          {
            paramView.set(i, Boolean.valueOf(bool));
            return;
            this.val$contentView.setVisibility(0);
            this.val$titleView.findViewById(R.id.up_trig_sep).setVisibility(0);
            this.val$arrow.setBackgroundResource(R.drawable.mini_arrow_up);
            break;
          }
        }
      });
      this.linearLayout.addView(localView);
      this.linearLayout.addView((View)localObject);
      i += 1;
      break label20;
      this.expandsStatus.clear();
      break;
      label178: this.expandsStatus.add(Boolean.valueOf(false));
      break label49;
      label193: localView.findViewById(R.id.up_trig_sep).setVisibility(0);
    }
  }

  private void setTypicalTicketGroupTitle()
  {
    if (this.linearLayout != null)
    {
      NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.shopinfo_senic_tuan_header_view, getParentView(), false);
      ((TextView)localNovaRelativeLayout.findViewById(R.id.title)).setText("门票");
      localNovaRelativeLayout.findViewById(R.id.img_icon).setBackgroundResource(R.drawable.scenic_typical_ticket);
      localNovaRelativeLayout.findViewById(R.id.subtitle).setVisibility(8);
      localNovaRelativeLayout.findViewById(R.id.icon_direction).setVisibility(8);
      localNovaRelativeLayout.findViewById(R.id.tv_tuan_count).setVisibility(8);
      this.linearLayout.addView(localNovaRelativeLayout);
      this.linearLayout.addView(genericLineSepWithoutMarin());
    }
  }

  View createTypicalItemAll(DPObject paramDPObject, int paramInt)
  {
    DPObject[] arrayOfDPObject = paramDPObject.getArray("TypicalTicketDealList");
    int j = paramDPObject.getInt("ShowNum");
    paramDPObject = new LinearLayout(getContext());
    LinearLayout localLinearLayout1 = new LinearLayout(getContext());
    LinearLayout localLinearLayout2 = new LinearLayout(getContext());
    paramDPObject.setOrientation(1);
    localLinearLayout1.setOrientation(1);
    localLinearLayout2.setOrientation(1);
    paramDPObject.setVisibility(0);
    localLinearLayout2.setVisibility(8);
    this.expandView = ((NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.scenic_shop_display_more, null));
    this.expandView.setLayoutParams(new ViewGroup.LayoutParams(-1, ViewUtils.dip2px(getContext(), 45.0F)));
    this.expandView.setGAString("scenic_ticket_all");
    TextView localTextView = (TextView)this.expandView.findViewById(R.id.display_deal_count);
    localTextView.setText("查看全部报价");
    paramDPObject.addView(localLinearLayout1);
    paramDPObject.addView(localLinearLayout2);
    paramDPObject.addView(this.expandView);
    paramDPObject.setBackgroundColor(this.res.getColor(R.color.light_grey));
    int i = 0;
    if (i < arrayOfDPObject.length)
    {
      if (arrayOfDPObject[i] == null);
      while (true)
      {
        i += 1;
        break;
        View localView = createTicketItem(arrayOfDPObject[i]);
        if (i < j)
        {
          localLinearLayout1.addView(localView);
          if (i != arrayOfDPObject.length - 1)
          {
            localLinearLayout1.addView(genericlineSep());
            continue;
          }
          localLinearLayout1.addView(genericLineSepWithoutMarin());
          continue;
        }
        localLinearLayout2.addView(localView);
        localLinearLayout2.addView(genericlineSep());
      }
    }
    if (localLinearLayout2.getChildCount() > 0)
    {
      this.expandView.setVisibility(0);
      this.expandsItemStatus.set(paramInt, Integer.valueOf(2));
    }
    while (true)
    {
      this.expandView.setOnClickListener(new View.OnClickListener(paramInt, localLinearLayout2, localTextView)
      {
        public void onClick(View paramView)
        {
          if (((Integer)ScenicTypicalTicketAgent.this.expandsItemStatus.get(this.val$itemPosition)).intValue() == 1)
          {
            this.val$brotherHideView.setVisibility(8);
            this.val$display_deal_count.setText("查看全部报价");
            paramView = ScenicTypicalTicketAgent.this.getResources().getDrawable(R.drawable.ic_arrow_down_black);
            paramView.setBounds(0, 0, paramView.getMinimumWidth(), paramView.getMinimumHeight());
            this.val$display_deal_count.setCompoundDrawables(null, null, paramView, null);
            ScenicTypicalTicketAgent.this.expandsItemStatus.set(this.val$itemPosition, Integer.valueOf(2));
          }
          do
            return;
          while (((Integer)ScenicTypicalTicketAgent.this.expandsItemStatus.get(this.val$itemPosition)).intValue() != 2);
          this.val$brotherHideView.setVisibility(0);
          this.val$display_deal_count.setText("收起全部报价");
          paramView = ScenicTypicalTicketAgent.this.getResources().getDrawable(R.drawable.ic_arrow_up_black);
          paramView.setBounds(0, 0, paramView.getMinimumWidth(), paramView.getMinimumHeight());
          this.val$display_deal_count.setCompoundDrawables(null, null, paramView, null);
          ScenicTypicalTicketAgent.this.expandsItemStatus.set(this.val$itemPosition, Integer.valueOf(1));
        }
      });
      return paramDPObject;
      this.expandView.setVisibility(8);
      this.expandsItemStatus.set(paramInt, Integer.valueOf(1));
    }
  }

  View createTypicalTitle(DPObject paramDPObject)
  {
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_scenic_ticket_category, null);
    Object localObject = (TextView)localView.findViewById(R.id.title);
    TextView localTextView1 = (TextView)localView.findViewById(R.id.best_price);
    TextView localTextView2 = (TextView)localView.findViewById(R.id.orign_price);
    ((ImageView)localView.findViewById(R.id.arrow)).setBackgroundResource(R.drawable.mini_arrow_down);
    ((TextView)localObject).setMaxLines(2);
    ((TextView)localObject).setMaxEms(10);
    ((TextView)localObject).setEllipsize(TextUtils.TruncateAt.END);
    ((TextView)localObject).setText(paramDPObject.getString("TicketType"));
    localObject = new SpannableString("￥" + PRICE_DF.format(paramDPObject.getDouble("OriginPrice")));
    ((SpannableString)localObject).setSpan(new StrikethroughSpan(), 1, ((SpannableString)localObject).length(), 33);
    ((SpannableString)localObject).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_hint)), 0, ((SpannableString)localObject).length(), 33);
    ((SpannableString)localObject).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), 0, ((SpannableString)localObject).length(), 33);
    localTextView2.setText((CharSequence)localObject);
    paramDPObject = new SpannableString("￥" + PRICE_DF.format(paramDPObject.getDouble("BestPrice")));
    paramDPObject.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
    paramDPObject.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, paramDPObject.length(), 33);
    paramDPObject.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, paramDPObject.length(), 33);
    localTextView1.setText(paramDPObject);
    return (View)localView;
  }

  protected View genericLineSepWithoutMarin()
  {
    this.line = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    this.line.setLayoutParams(localLayoutParams);
    this.line.setBackgroundResource(R.color.review_seperate_line_color);
    return this.line;
  }

  protected View genericlineSep()
  {
    this.line = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
    this.line.setLayoutParams(localLayoutParams);
    this.line.setBackgroundResource(R.color.review_seperate_line_color);
    return this.line;
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if (paramAgentMessage.what.equals("com.dianping.shopinfo.hotel.BaseScenicTicketAgent.SCENIC_COMMON_TICKET_LOAD_DATA_SHOP_TICKET"))
      onAgentChanged(paramAgentMessage.body);
  }

  protected View line()
  {
    this.line = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
    this.line.setLayoutParams(localLayoutParams);
    this.line.setBackgroundResource(R.color.background_gray);
    return this.line;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
      this.TypicalTicketGroup = ((DPObject)paramBundle.getParcelable("TypicalTicketGroup"));
    removeAllCells();
    if (this.TypicalTicketGroup != null)
      this.TypicalTicketList = this.TypicalTicketGroup.getArray("TypicalTicketList");
    this.linearLayout = new LinearLayout(getContext());
    this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.linearLayout.setOrientation(1);
    if ((this.TypicalTicketList != null) && (this.TypicalTicketList.length > 0))
    {
      createTypicalTicketGroup();
      addCell("0460fun.10book", this.linearLayout);
      this.ONCE_GA_TOOL.doGA(getContext(), "scenic_ticket_show");
    }
  }

  public void setBackground(View paramView, int paramInt)
  {
    int i = paramView.getPaddingBottom();
    int j = paramView.getPaddingTop();
    int k = paramView.getPaddingRight();
    int m = paramView.getPaddingLeft();
    paramView.setBackgroundResource(paramInt);
    paramView.setPadding(m, j, k, i);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.senic.ScenicTypicalTicketAgent
 * JD-Core Version:    0.6.0
 */