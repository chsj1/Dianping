package com.dianping.shopinfo.hotel.senic;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;

public class SenicOtherTuanAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String HOTEL_OTHER_TUAN_CELL = "4100Othertuan";
  protected static final DecimalFormat PRICE_DF = new DecimalFormat("#.###");
  private ScenicOnceGATool ONCE_GA_TOOL = new ScenicOnceGATool("scenic_other_show", new String[0]);
  DPObject ScenicOtherDealObj;
  boolean ShouldShow = true;
  LinearLayout expandLayout;
  NovaRelativeLayout expandView;
  boolean isExpand = false;
  LinearLayout linearLayout;
  String moreText;
  private MApiRequest request;
  protected SparseArray<CommonCell> tuanCells = new SparseArray();

  public SenicOtherTuanAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View line()
  {
    View localView = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
    localView.setLayoutParams(localLayoutParams);
    localView.setBackgroundColor(getResources().getColor(R.color.background_gray));
    return localView;
  }

  private void renderHotelTuanCell(DPObject paramDPObject)
  {
    this.ShouldShow = paramDPObject.getBoolean("ShouldShow");
    if (!this.ShouldShow)
    {
      setSharedObject("tuan_show", Boolean.valueOf(true));
      dispatchAgentChanged("shopinfo/tuan", null);
    }
    DPObject[] arrayOfDPObject;
    do
    {
      return;
      this.ONCE_GA_TOOL.doGA(getContext(), "scenic_other_show");
      arrayOfDPObject = paramDPObject.getArray("ScenicOtherDealList");
    }
    while ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0));
    this.linearLayout = new LinearLayout(getContext());
    this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.linearLayout.setOrientation(1);
    TextView localTextView = new TextView(getContext());
    localTextView.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(getContext(), 55.0F)));
    localTextView.setPadding(ViewUtils.dip2px(getContext(), 15.0F), 0, 0, 0);
    localTextView.setGravity(16);
    localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_15));
    localTextView.setTextColor(getResources().getColor(R.color.text_color_black));
    if (!TextUtils.isEmpty(paramDPObject.getString("OtherDealTitle")))
      localTextView.setText("景点" + paramDPObject.getString("OtherDealTitle"));
    int j;
    int i;
    while (true)
    {
      this.linearLayout.addView(localTextView);
      this.linearLayout.addView(line());
      j = paramDPObject.getInt("OtherDealShowNum");
      i = 0;
      while (i < j)
      {
        this.linearLayout.addView(createSenicDealCell(arrayOfDPObject[i], false));
        this.linearLayout.addView(line());
        i += 1;
      }
      localTextView.setText(paramDPObject.getString("OtherDealTitle"));
    }
    if (arrayOfDPObject.length > j)
    {
      this.isExpand = true;
      this.expandLayout = new LinearLayout(getContext());
      this.expandLayout.setOrientation(1);
      i = j;
      while (i < arrayOfDPObject.length)
      {
        this.expandLayout.addView(createSenicDealCell(arrayOfDPObject[i], false));
        this.expandLayout.addView(line());
        i += 1;
      }
      this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false));
      this.expandView.setTag("EXPAND");
      this.moreText = ("全部" + arrayOfDPObject.length + "条团购");
      this.expandView.setGAString("scenic_other_all");
      paramDPObject = (TextView)this.expandView.findViewById(16908308);
      paramDPObject.setText(this.moreText);
      paramDPObject.setTextColor(getResources().getColor(R.color.tuan_common_gray));
      this.expandView.setClickable(true);
      this.expandView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (SenicOtherTuanAgent.this.expandView == null)
            return;
          if (SenicOtherTuanAgent.this.isExpand)
          {
            ((ImageView)SenicOtherTuanAgent.this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_up);
            ((TextView)SenicOtherTuanAgent.this.expandView.findViewById(16908308)).setText("收起");
            SenicOtherTuanAgent.this.expandLayout.setVisibility(0);
            paramView = SenicOtherTuanAgent.this;
            if (SenicOtherTuanAgent.this.isExpand)
              break label175;
          }
          label175: for (boolean bool = true; ; bool = false)
          {
            paramView.isExpand = bool;
            return;
            ((ImageView)SenicOtherTuanAgent.this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_down);
            ((TextView)SenicOtherTuanAgent.this.expandView.findViewById(16908308)).setText(SenicOtherTuanAgent.this.moreText);
            SenicOtherTuanAgent.this.expandView.findViewById(16908308).setVisibility(0);
            SenicOtherTuanAgent.this.expandLayout.setVisibility(8);
            break;
          }
        }
      });
      this.linearLayout.addView(this.expandLayout);
      this.expandLayout.setVisibility(8);
      this.linearLayout.addView(this.expandView);
    }
    addCell("4100Othertuan", this.linearLayout, 0);
  }

  private void sendRequset()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/scenic/otherdeal.scenic").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(shopId()));
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    this.request = mapiGet(this, localBuilder.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public View createSenicDealCell(DPObject paramDPObject, boolean paramBoolean)
  {
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.senic_othertuan_cell, getParentView(), false);
    ((TextView)localNovaRelativeLayout.findViewById(R.id.title)).setText(paramDPObject.getString("Title"));
    localNovaRelativeLayout.setClickable(true);
    localNovaRelativeLayout.setGAString("scenic_other_detail");
    localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        paramView = this.val$deal.getString("Url");
        if ((!TextUtils.isEmpty(paramView)) && (!"null".equalsIgnoreCase(paramView)))
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
          SenicOtherTuanAgent.this.startActivity(paramView);
        }
      }
    });
    double d = paramDPObject.getDouble("Price");
    Object localObject1 = new SpannableStringBuilder();
    Object localObject2 = new SpannableString("￥" + PRICE_DF.format(d));
    ((SpannableString)localObject2).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
    ((SpannableString)localObject2).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, ((SpannableString)localObject2).length(), 33);
    ((SpannableString)localObject2).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, ((SpannableString)localObject2).length(), 33);
    ((SpannableStringBuilder)localObject1).append((CharSequence)localObject2);
    ((TextView)localNovaRelativeLayout.findViewById(R.id.text_now_price)).setText((CharSequence)localObject1);
    if (!paramBoolean)
      setBackground(localNovaRelativeLayout, 0);
    localObject1 = (LinearLayout)localNovaRelativeLayout.findViewById(R.id.layout_promo);
    paramDPObject = paramDPObject.getArray("PromoList");
    if ((paramDPObject == null) || (paramDPObject.length == 0))
      ((LinearLayout)localObject1).setVisibility(8);
    while (true)
    {
      return localNovaRelativeLayout;
      ((LinearLayout)localObject1).setVisibility(0);
      int i = 0;
      while (i < paramDPObject.length)
      {
        localObject2 = new TextView(getContext());
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
        localLayoutParams.setMargins(0, 0, 0, 0);
        ((TextView)localObject2).setLayoutParams(localLayoutParams);
        ((TextView)localObject2).setText(paramDPObject[i].getString("Title"));
        ((TextView)localObject2).setTextColor(getResources().getColor(R.color.hotel_calender_weekend_color));
        ((TextView)localObject2).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_10));
        ((TextView)localObject2).setGravity(17);
        ((TextView)localObject2).setBackgroundResource(R.drawable.hotel_promo_border);
        ((TextView)localObject2).setSingleLine();
        ((LinearLayout)localObject1).addView((View)localObject2);
        i += 1;
      }
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (paramBundle == null)
      return;
    renderHotelTuanCell(this.ScenicOtherDealObj);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequset();
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
    setSharedObject("tuan_show", Boolean.valueOf(true));
    dispatchAgentChanged("shopinfo/tuan", null);
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.request)
    {
      this.request = null;
      this.ScenicOtherDealObj = ((DPObject)paramResponse.result());
      if (this.ScenicOtherDealObj != null)
      {
        dispatchAgentChanged(false);
        renderHotelTuanCell(this.ScenicOtherDealObj);
      }
    }
    dispatchAgentChanged(false);
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
 * Qualified Name:     com.dianping.shopinfo.hotel.senic.SenicOtherTuanAgent
 * JD-Core Version:    0.6.0
 */