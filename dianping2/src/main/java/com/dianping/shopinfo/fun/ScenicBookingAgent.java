package com.dianping.shopinfo.fun;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.PriceFormatUtils;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaRelativeLayout;

public class ScenicBookingAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String FUN_BOOK = "0470fun.10book";
  private static final String RMB = "￥";
  private NovaRelativeLayout expandView;
  private boolean isShow = false;
  private LinearLayout linearLayout;
  private MApiRequest request;
  private DPObject shopTicket;
  private DPObject[] ticketGroup;

  public ScenicBookingAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void addTicketFamily(TicketFamily paramTicketFamily)
  {
    if (paramTicketFamily.grandView != null)
      this.linearLayout.addView(paramTicketFamily.grandView);
    if (paramTicketFamily.fatherView != null)
      this.linearLayout.addView(paramTicketFamily.fatherView);
  }

  private void createTicketCategoryItem(DPObject paramDPObject, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramDPObject == null) || (paramDPObject.getArray("ShopTickets") == null))
      return;
    View localView1 = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_scenic_category, null);
    Object localObject1 = (TextView)localView1.findViewById(R.id.min_price);
    Object localObject2 = (TextView)localView1.findViewById(R.id.category);
    localView1.findViewById(R.id.arrow).setBackgroundResource(R.drawable.arrow_full_down);
    double d = paramDPObject.getDouble("MinPrice");
    String str = paramDPObject.getString("TicketType");
    ((TextView)localObject1).setText("￥" + PriceFormatUtils.formatPrice(d));
    ((TextView)localObject2).setText(str);
    ((NovaRelativeLayout)localView1.findViewById(R.id.book_item)).setGAString("ticket_booking", str);
    localObject1 = new LinearLayout(getContext());
    localObject2 = new LinearLayout(getContext());
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    ((LinearLayout)localObject1).setOrientation(1);
    ((LinearLayout)localObject2).setOrientation(1);
    localLinearLayout.setOrientation(1);
    int i;
    if (paramBoolean1)
    {
      ((LinearLayout)localObject1).setVisibility(0);
      localLinearLayout.setVisibility(8);
      this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_scenic_expand, null));
      if (paramBoolean2)
      {
        localView1.findViewById(R.id.line).setVisibility(8);
        this.expandView.findViewById(R.id.line).setVisibility(8);
      }
      ((LinearLayout)localObject1).addView((View)localObject2);
      ((LinearLayout)localObject1).addView(localLinearLayout);
      ((LinearLayout)localObject1).addView(this.expandView);
      ((LinearLayout)localObject1).setBackgroundColor(this.res.getColor(R.color.light_grey));
      i = 0;
      label307: if (i >= paramDPObject.getArray("ShopTickets").length)
        break label391;
      if (paramDPObject.getArray("ShopTickets")[i] != null)
        break label350;
    }
    while (true)
    {
      i += 1;
      break label307;
      ((LinearLayout)localObject1).setVisibility(8);
      break;
      label350: View localView2 = createTicketItem(paramDPObject.getArray("ShopTickets")[i]);
      if (i < 2)
      {
        ((LinearLayout)localObject2).addView(localView2);
        continue;
      }
      localLinearLayout.addView(localView2);
    }
    label391: if (localLinearLayout.getChildCount() > 0)
    {
      this.expandView.setVisibility(0);
      paramDPObject = new TicketFamily(localView1, (View)localObject1, (View)localObject2, localLinearLayout, this.expandView);
      addTicketFamily(paramDPObject);
      if (!paramBoolean1)
        break label488;
    }
    label488: for (paramDPObject.isFatherVisible = true; ; paramDPObject.isFatherVisible = false)
    {
      localView1.setOnClickListener(new View.OnClickListener(paramDPObject, paramBoolean2)
      {
        public void onClick(View paramView)
        {
          ScenicBookingAgent.this.setFatherViewExpandAction(this.val$ticketFamily, this.val$isLast);
        }
      });
      this.expandView.setOnClickListener(new View.OnClickListener(paramDPObject, str)
      {
        public void onClick(View paramView)
        {
          if (this.val$ticketFamily.isBrotherHideVisible)
            GAHelper.instance().contextStatisticsEvent(ScenicBookingAgent.this.getContext(), "ticket_booking", this.val$ticketType + "_收起", 0, "tap");
          while (true)
          {
            ScenicBookingAgent.this.setBrotherHideViewExpandAction(this.val$ticketFamily);
            return;
            GAHelper.instance().contextStatisticsEvent(ScenicBookingAgent.this.getContext(), "ticket_booking", this.val$ticketType + "_更多", 0, "tap");
          }
        }
      });
      return;
      this.expandView.setVisibility(8);
      break;
    }
  }

  private View createTicketItem(DPObject paramDPObject)
  {
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_scenic_item, null);
    NovaButton localNovaButton = (NovaButton)localNovaRelativeLayout.findViewById(R.id.preorder);
    localNovaRelativeLayout.setGAString("ticket_booking", paramDPObject.getString("Name"));
    localNovaButton.setGAString("bookingticket", paramDPObject.getString("Name") + "_预订");
    Object localObject2 = (TextView)localNovaRelativeLayout.findViewById(R.id.ticket_name);
    Object localObject3 = (TextView)localNovaRelativeLayout.findViewById(R.id.price);
    Object localObject1 = (TextView)localNovaRelativeLayout.findViewById(R.id.original_price);
    LinearLayout localLinearLayout = (LinearLayout)localNovaRelativeLayout.findViewById(R.id.discount);
    ((TextView)localObject2).setText(paramDPObject.getString("Name"));
    ((TextView)localObject3).setText("￥" + PriceFormatUtils.formatPrice(paramDPObject.getDouble("Price")));
    localObject2 = new SpannableString("￥" + PriceFormatUtils.formatPrice(paramDPObject.getDouble("MarketPrice")));
    ((SpannableString)localObject2).setSpan(new StrikethroughSpan(), 1, ((SpannableString)localObject2).length(), 33);
    ((SpannableString)localObject2).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), 0, ((SpannableString)localObject2).length(), 33);
    ((TextView)localObject1).setText((CharSequence)localObject2);
    localObject1 = paramDPObject.getStringArray("Promos");
    localObject2 = new LinearLayout.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams)localObject2).setMargins(10, 0, 0, 0);
    if (localObject1 != null)
    {
      int i = 0;
      while ((i < localObject1.length) && (i < 2))
      {
        localObject3 = new ColorBorderTextView(getContext());
        ((ColorBorderTextView)localObject3).setBorderColor(getResources().getColor(R.color.tuan_common_orange));
        ((ColorBorderTextView)localObject3).setTextColor(getResources().getColor(R.color.tuan_common_orange));
        ((ColorBorderTextView)localObject3).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
        ((ColorBorderTextView)localObject3).setSingleLine();
        ((ColorBorderTextView)localObject3).setEllipsize(TextUtils.TruncateAt.END);
        ((ColorBorderTextView)localObject3).setPadding(ViewUtils.dip2px(getContext(), 4.0F), 0, ViewUtils.dip2px(getContext(), 4.0F), 0);
        ((ColorBorderTextView)localObject3).setText(localObject1[i]);
        localLinearLayout.addView((View)localObject3, (ViewGroup.LayoutParams)localObject2);
        i += 1;
      }
    }
    localNovaButton.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        try
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$ticket.getString("BuyUrl")));
          ScenicBookingAgent.this.startActivity(paramView);
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
    localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        try
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$ticket.getString("DetailUrl")));
          ScenicBookingAgent.this.startActivity(paramView);
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
    return (View)(View)(View)localNovaRelativeLayout;
  }

  private void sendRequset()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/fun/getshoptickets.fn").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(shopId()));
    this.request = mapiGet(this, localBuilder.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  private void setBrotherHideViewExpandAction(TicketFamily paramTicketFamily)
  {
    if (paramTicketFamily.isBrotherHideVisible)
    {
      paramTicketFamily.setBrotherHideViewVisible(false);
      paramTicketFamily.brotherHideView.setVisibility(8);
      ((ImageView)paramTicketFamily.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_down);
      ((TextView)paramTicketFamily.expandView.findViewById(16908308)).setText("更多");
      return;
    }
    paramTicketFamily.setBrotherHideViewVisible(true);
    paramTicketFamily.brotherHideView.setVisibility(0);
    ((ImageView)paramTicketFamily.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_up);
    ((TextView)paramTicketFamily.expandView.findViewById(16908308)).setText("收起");
  }

  private void setFatherViewExpandAction(TicketFamily paramTicketFamily, boolean paramBoolean)
  {
    if (paramTicketFamily.isFatherVisible)
    {
      paramTicketFamily.setFatherVisible(false);
      paramTicketFamily.fatherView.setVisibility(8);
      if (paramBoolean)
        paramTicketFamily.grandView.findViewById(R.id.line).setVisibility(8);
      paramTicketFamily.grandView.findViewById(R.id.arrow).setBackgroundResource(R.drawable.arrow_full_down);
      return;
    }
    paramTicketFamily.setFatherVisible(true);
    paramTicketFamily.fatherView.setVisibility(0);
    if (paramBoolean)
      paramTicketFamily.grandView.findViewById(R.id.line).setVisibility(0);
    paramTicketFamily.grandView.findViewById(R.id.arrow).setBackgroundResource(R.drawable.arrow_full_up);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((!this.isShow) || (this.ticketGroup == null) || (getShop() == null))
      return;
    this.linearLayout = new LinearLayout(getContext());
    this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.linearLayout.setOrientation(1);
    paramBundle = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_scenic_header, null);
    this.linearLayout.addView(paramBundle);
    paramBundle = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_line, null);
    paramBundle.setPadding(ViewUtils.dip2px(getContext(), 15.0F), 0, 0, 0);
    this.linearLayout.addView(paramBundle);
    boolean bool1 = false;
    int i = 0;
    if (i < this.ticketGroup.length)
    {
      if (i == 0);
      for (boolean bool2 = true; ; bool2 = false)
      {
        if (i == this.ticketGroup.length - 1)
          bool1 = true;
        createTicketCategoryItem(this.ticketGroup[i], bool2, bool1);
        i += 1;
        break;
      }
    }
    addCell("0470fun.10book", this.linearLayout);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequset();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.request)
      this.request = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.request)
    {
      this.request = null;
      this.shopTicket = ((DPObject)paramResponse.result());
      if (this.shopTicket != null)
      {
        this.ticketGroup = this.shopTicket.getArray("TicketGroups");
        this.isShow = this.shopTicket.getBoolean("Showable");
        dispatchAgentChanged(false);
      }
    }
  }

  class TicketFamily
  {
    View brotherHideView = null;
    View expandView = null;
    View fatherView = null;
    View grandView = null;
    boolean isBrotherHideVisible = false;
    boolean isFatherVisible = false;

    public TicketFamily(View paramView1, View paramView2, View paramView3, View paramView4, View arg6)
    {
      this.grandView = paramView1;
      this.fatherView = paramView2;
      this.brotherHideView = paramView4;
      Object localObject;
      this.expandView = localObject;
    }

    public void setBrotherHideViewVisible(boolean paramBoolean)
    {
      this.isBrotherHideVisible = paramBoolean;
    }

    public void setFatherVisible(boolean paramBoolean)
    {
      this.isFatherVisible = paramBoolean;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.fun.ScenicBookingAgent
 * JD-Core Version:    0.6.0
 */