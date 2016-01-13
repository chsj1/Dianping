package com.dianping.shopinfo.hotel.senic;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.utils.SharedDataInferface;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;

public abstract class BaseScenicTicketAgent extends ShopCellAgent
  implements SharedDataInferface, RequestHandler
{
  private static final String FUN_BOOK = "0470fun.10book.%s";
  protected static final DecimalFormat PRICE_DF = new DecimalFormat("#.###");
  protected DPObject[] GeneralTicketGroupList;
  private ScenicOnceGATool ONCE_GA_TOOL = new ScenicOnceGATool("scenic_joint_show", new String[] { "scenic_hotel_show", "scenic_travel_show" });
  protected DPObject TypicalTicketGroup;
  protected View line;
  protected MApiRequest request;
  protected DPObject shopTicket;

  public BaseScenicTicketAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void createGeneralTicketGroupList()
  {
    if ((this.GeneralTicketGroupList == null) || (this.GeneralTicketGroupList.length == 0))
      return;
    int j = 0;
    int i = 0;
    label22: DPObject localDPObject;
    DPObject[] arrayOfDPObject;
    if (i < this.GeneralTicketGroupList.length)
    {
      localDPObject = this.GeneralTicketGroupList[i];
      arrayOfDPObject = localDPObject.getArray("GeneralTicketDealList");
      k = j;
      if (arrayOfDPObject != null)
        if (arrayOfDPObject.length != 0)
          break label77;
    }
    for (int k = j; ; k = 1)
    {
      i += 1;
      j = k;
      break label22;
      break;
      label77: LinearLayout localLinearLayout = new LinearLayout(getContext());
      localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      localLinearLayout.setOrientation(1);
      if (j != 0)
        localLinearLayout.addView(addCellDivider());
      localLinearLayout.addView(ticketGroupLine());
      localLinearLayout.addView(createGeneralTicketGroupTitle(localDPObject));
      localLinearLayout.addView(ticketGroupLine());
      j = 0;
      if (j < arrayOfDPObject.length)
      {
        localLinearLayout.addView(createGenetalDealCell(arrayOfDPObject[j], localDPObject.getInt("IconType")));
        if (j != arrayOfDPObject.length - 1)
          localLinearLayout.addView(ticketDealLine());
        while (true)
        {
          j += 1;
          break;
          localLinearLayout.addView(ticketGroupLine());
        }
      }
      addCell(String.format("0470fun.10book.%s", new Object[] { Integer.valueOf(i) }), localLinearLayout);
    }
  }

  private View createGeneralTicketGroupTitle(DPObject paramDPObject)
  {
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.shopinfo_senic_tuan_header_view, getParentView(), false);
    Object localObject = (TextView)localNovaRelativeLayout.findViewById(R.id.title);
    TextView localTextView1 = (TextView)localNovaRelativeLayout.findViewById(R.id.subtitle);
    TextView localTextView2 = (TextView)localNovaRelativeLayout.findViewById(R.id.tv_tuan_count);
    ImageView localImageView1 = (ImageView)localNovaRelativeLayout.findViewById(R.id.icon_direction);
    ImageView localImageView2 = (ImageView)localNovaRelativeLayout.findViewById(R.id.img_icon);
    ((TextView)localObject).setText(paramDPObject.getString("TicketGroupTitle"));
    localObject = paramDPObject.getString("TicketGroupTitleTag");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      localTextView1.setVisibility(0);
      localTextView1.setText((CharSequence)localObject);
      switch (paramDPObject.getInt("IconType"))
      {
      default:
        localImageView2.setBackgroundResource(R.drawable.scenic_combine);
        localNovaRelativeLayout.setGAString("scenic_joint_all");
        this.ONCE_GA_TOOL.doGA(getContext(), "scenic_joint_show");
      case 1:
      case 2:
      case 3:
      }
    }
    while (true)
    {
      if (paramDPObject.getInt("TotalCount") <= paramDPObject.getInt("ShowNum"))
        break label344;
      localTextView2.setText("共" + paramDPObject.getInt("TotalCount") + "单");
      localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$ticketGroupObj.getString("Url");
          if ((!TextUtils.isEmpty(paramView)) || ("null".equalsIgnoreCase(paramView)))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            BaseScenicTicketAgent.this.startActivity(paramView);
          }
        }
      });
      return localNovaRelativeLayout;
      localTextView1.setVisibility(8);
      break;
      localImageView2.setBackgroundResource(R.drawable.scenic_combine);
      localNovaRelativeLayout.setGAString("scenic_joint_all");
      this.ONCE_GA_TOOL.doGA(getContext(), "scenic_joint_show");
      continue;
      localImageView2.setBackgroundResource(R.drawable.scenic_hotel);
      localNovaRelativeLayout.setGAString("scenic_hotel_all");
      this.ONCE_GA_TOOL.doGA(getContext(), "scenic_hotel_show");
      continue;
      localImageView2.setBackgroundResource(R.drawable.scenic_local);
      localNovaRelativeLayout.setGAString("scenic_travel_all");
      this.ONCE_GA_TOOL.doGA(getContext(), "scenic_travel_show");
    }
    label344: localTextView2.setVisibility(8);
    localImageView1.setVisibility(8);
    return (View)localNovaRelativeLayout;
  }

  private View createGenetalDealCell(DPObject paramDPObject, int paramInt)
  {
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.senic_tuan_cell_shopinfo, null);
    Object localObject4 = (TextView)localNovaRelativeLayout.findViewById(R.id.text1);
    Object localObject3 = (TextView)localNovaRelativeLayout.findViewById(R.id.text_now_price);
    TextView localTextView = (TextView)localNovaRelativeLayout.findViewById(R.id.text_orign_price);
    Object localObject2 = (LinearLayout)localNovaRelativeLayout.findViewById(R.id.layout_extra);
    Object localObject1 = (TextView)localNovaRelativeLayout.findViewById(R.id.text_sell);
    LinearLayout localLinearLayout = (LinearLayout)localNovaRelativeLayout.findViewById(R.id.layout_promo);
    if (paramInt == 1)
    {
      localNovaRelativeLayout.setGAString("scenic_joint_detail");
      localNovaRelativeLayout.setClickable(true);
      localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$deal.getString("Url");
          if ((!TextUtils.isEmpty(paramView)) || ("null".equalsIgnoreCase(paramView)))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            BaseScenicTicketAgent.this.startActivity(paramView);
          }
        }
      });
      setBackground(localNovaRelativeLayout, 0);
      ((TextView)localObject4).setText(paramDPObject.getString("TicketTitle"));
      localObject4 = new SpannableString("￥" + PRICE_DF.format(paramDPObject.getDouble("Price")));
      ((SpannableString)localObject4).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
      ((SpannableString)localObject4).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, ((SpannableString)localObject4).length(), 33);
      ((SpannableString)localObject4).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, ((SpannableString)localObject4).length(), 33);
      ((TextView)localObject3).setText((CharSequence)localObject4);
      localTextView.setVisibility(8);
      localObject3 = paramDPObject.getStringArray("InfoList");
      if ((localObject3 != null) && (localObject3.length != 0))
        break label418;
      ((LinearLayout)localObject2).setVisibility(8);
      label306: localObject2 = paramDPObject.getArray("PromoTagList");
      if ((localObject2 != null) && (localObject2.length != 0))
        break label543;
      localLinearLayout.setVisibility(8);
      paramInt = paramDPObject.getInt("SalesVolume");
      if (paramInt != 0)
      {
        ((TextView)localObject1).setVisibility(0);
        ((TextView)localObject1).setText("已售" + paramInt);
      }
    }
    while (true)
    {
      return localNovaRelativeLayout;
      if (paramInt == 2)
      {
        localNovaRelativeLayout.setGAString("scenic_hotel_detail");
        break;
      }
      if (paramInt == 3)
      {
        localNovaRelativeLayout.setGAString("scenic_travel_detail");
        break;
      }
      localNovaRelativeLayout.setGAString("scenic_joint_detail");
      break;
      label418: paramInt = 0;
      while (paramInt < localObject3.length)
      {
        localTextView = new TextView(getContext());
        localTextView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        localTextView.setPadding(0, ViewUtils.dip2px(getContext(), 6.0F), 0, 0);
        localTextView.setTextColor(getResources().getColor(R.color.tuan_common_gray));
        localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
        localTextView.setText(localObject3[paramInt]);
        localTextView.setMaxLines(2);
        localTextView.setEllipsize(TextUtils.TruncateAt.END);
        ((LinearLayout)localObject2).addView(localTextView);
        paramInt += 1;
      }
      break label306;
      label543: localLinearLayout.setVisibility(0);
      paramInt = 0;
      while (paramInt < localObject2.length)
      {
        paramDPObject = new TextView(getContext());
        localObject1 = new LinearLayout.LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams)localObject1).setMargins(ViewUtils.dip2px(getContext(), 2.0F), 0, 0, 0);
        paramDPObject.setLayoutParams((ViewGroup.LayoutParams)localObject1);
        paramDPObject.setText(localObject2[paramInt].getString("Title"));
        paramDPObject.setTextColor(getResources().getColor(R.color.hotel_calender_weekend_color));
        paramDPObject.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_10));
        paramDPObject.setBackgroundResource(R.drawable.hotel_promo_border);
        paramDPObject.setSingleLine();
        localLinearLayout.addView(paramDPObject);
        paramInt += 1;
      }
    }
  }

  private void setBackground(View paramView, int paramInt)
  {
    int i = paramView.getPaddingBottom();
    int j = paramView.getPaddingTop();
    int k = paramView.getPaddingRight();
    int m = paramView.getPaddingLeft();
    paramView.setBackgroundResource(paramInt);
    paramView.setPadding(m, j, k, i);
  }

  protected View addCellDivider()
  {
    this.line = new View(getContext());
    this.line.setBackgroundResource(R.color.common_bk_color);
    this.line.setLayoutParams(new ViewGroup.LayoutParams(-1, (int)TypedValue.applyDimension(1, 10.0F, getFragment().getResources().getDisplayMetrics())));
    return this.line;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.GeneralTicketGroupList == null);
    do
    {
      return;
      removeAllCells();
    }
    while ((this.GeneralTicketGroupList == null) || (this.GeneralTicketGroupList.length <= 0));
    createGeneralTicketGroupList();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.request != null)
    {
      mapiService().abort(this.request, this, true);
      this.request = null;
    }
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.request)
      this.request = null;
    dispatchAgentChanged(false);
    setSharedObject("book_show", Boolean.valueOf(true));
    dispatchMessage(new AgentMessage("com.dianping.shopinfo.hotel.BaseScenicTicketAgent.SCENIC_COMMON_TICKET_BOOK_SHOW"));
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.request)
    {
      this.request = null;
      this.shopTicket = ((DPObject)paramResponse.result());
      if (this.shopTicket != null)
      {
        this.GeneralTicketGroupList = this.shopTicket.getArray("GeneralTicketGroupList");
        this.TypicalTicketGroup = this.shopTicket.getObject("TypicalTicketGroup");
        paramRequest = new Bundle();
        paramRequest.putParcelable("TypicalTicketGroup", this.TypicalTicketGroup);
        paramResponse = new AgentMessage("com.dianping.shopinfo.hotel.BaseScenicTicketAgent.SCENIC_COMMON_TICKET_LOAD_DATA_SHOP_TICKET");
        paramResponse.body = paramRequest;
        dispatchMessage(paramResponse);
        if (!this.shopTicket.getBoolean("ShouldShow"))
        {
          setSharedObject("book_show", Boolean.valueOf(true));
          dispatchMessage(new AgentMessage("com.dianping.shopinfo.hotel.BaseScenicTicketAgent.SCENIC_COMMON_TICKET_BOOK_SHOW"));
        }
      }
    }
    dispatchAgentChanged(false);
  }

  protected View ticketDealLine()
  {
    this.line = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
    this.line.setLayoutParams(localLayoutParams);
    this.line.setBackgroundResource(R.color.background_gray);
    return this.line;
  }

  protected View ticketGroupLine()
  {
    this.line = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    this.line.setLayoutParams(localLayoutParams);
    this.line.setBackgroundResource(R.color.review_seperate_line_color);
    return this.line;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.senic.BaseScenicTicketAgent
 * JD-Core Version:    0.6.0
 */