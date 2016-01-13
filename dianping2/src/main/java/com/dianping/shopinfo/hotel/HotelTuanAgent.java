package com.dianping.shopinfo.hotel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.City;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HotelTuanAgent extends ShopCellAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_HOTEL_TUAN = "0450Tuan.50HotelTuan";
  private static final int HOTEL_TUAN_ITEM_LIMIT = 2;
  private static final DecimalFormat PRICE_DF = new DecimalFormat("#.###");
  DPObject TPHotelProductList;
  LinearLayout expandLayout;
  View expandView;
  MApiRequest hotelTuanRequest;
  boolean isExpand;
  LinearLayout linearLayout;

  public HotelTuanAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createHotelDealCell(DPObject paramDPObject, boolean paramBoolean)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.item_shopinfo_hotel_tuan_item, getParentView(), false);
    localNovaLinearLayout.setClickable(true);
    localNovaLinearLayout.setOnClickListener(this);
    localNovaLinearLayout.setGAString("hotel_tuan");
    Object localObject2 = (TextView)localNovaLinearLayout.findViewById(16908308);
    Object localObject1;
    int i;
    Object localObject3;
    int j;
    label236: int n;
    int k;
    label375: RelativeLayout.LayoutParams localLayoutParams;
    int i2;
    int i1;
    int m;
    if (!TextUtils.isEmpty(paramDPObject.getString("ShortTitle")))
    {
      localObject1 = paramDPObject.getString("ShortTitle");
      ((TextView)localObject2).setText((CharSequence)localObject1);
      ((TextView)localObject2).setGravity(3);
      ((TextView)localNovaLinearLayout.findViewById(16908309)).setText(formatHotelPrice(paramDPObject, false));
      i = paramDPObject.getInt("Tag");
      localObject3 = paramDPObject.getString("ProductUrl");
      localObject2 = paramDPObject.getString("EndDate");
      localObject1 = paramDPObject.getString("OrderPolicy");
      j = paramDPObject.getInt("Status");
      if ((((i & 0x100) == 0) && (TextUtils.isEmpty((CharSequence)localObject3))) || ((j != 1) && (j != 2)))
        break label803;
      localObject2 = (TextView)localNovaLinearLayout.findViewById(R.id.booking);
      ((TextView)localObject2).setVisibility(0);
      if (!TextUtils.isEmpty((CharSequence)localObject1))
        ((TextView)localObject2).setText((CharSequence)localObject1);
      localObject1 = paramDPObject.getString("StatusText");
      switch (j)
      {
      default:
        localObject3 = paramDPObject.getArray("PromoList");
        if ((localObject3 == null) || (localObject3.length <= 0))
          break label932;
        LinearLayout localLinearLayout = (LinearLayout)localNovaLinearLayout.findViewById(R.id.promo_list);
        localObject1 = null;
        localNovaLinearLayout.findViewById(R.id.item).measure(0, 0);
        localNovaLinearLayout.findViewById(R.id.left).measure(0, 0);
        localObject2 = new DisplayMetrics();
        getFragment().getActivity().getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)localObject2);
        int i3 = ((DisplayMetrics)localObject2).widthPixels - localNovaLinearLayout.findViewById(R.id.left).getMeasuredWidth() - ViewUtils.dip2px(getContext(), 63.0F) - 30;
        n = 0;
        i = i3;
        j = 0;
        int i4 = localObject3.length;
        k = 0;
        if (k >= i4)
          break label932;
        localLayoutParams = localObject3[k];
        i2 = n;
        localObject2 = localObject1;
        i1 = j;
        m = i;
        if (localLayoutParams == null)
          break;
        i2 = n;
        localObject2 = localObject1;
        i1 = j;
        m = i;
        if (TextUtils.isEmpty(localLayoutParams.getString("Title")))
          break;
        m = localLayoutParams.getInt("Type");
        localObject2 = createTagView(localLayoutParams.getString("Title"), m);
        i2 = n + 1;
        ((TextView)localObject2).setId(i2);
        localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
        ((TextView)localObject2).setLayoutParams(localLayoutParams);
        ((TextView)localObject2).measure(0, 0);
        i += ((TextView)localObject2).getMeasuredWidth() + ViewUtils.dip2px(getContext(), 3.0F);
        if ((i >= i3) || (j >= 3))
        {
          m = 1;
          i = ((TextView)localObject2).getMeasuredWidth();
          label544: if (m == 0)
            break label893;
          localObject1 = new RelativeLayout(getContext());
          LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-2, -2);
          localLayoutParams1.topMargin = ViewUtils.dip2px(getContext(), 3.0F);
          ((RelativeLayout)localObject1).setLayoutParams(localLayoutParams1);
          localLinearLayout.addView((View)localObject1);
          m = 1;
          j = 1;
          label606: if (m == 0)
            break label905;
          localLayoutParams.addRule(11);
        }
      case 1:
      case 2:
      }
    }
    while (true)
    {
      ((TextView)localObject2).setLayoutParams(localLayoutParams);
      ((RelativeLayout)localObject1).addView((View)localObject2);
      m = i;
      i1 = j;
      localObject2 = localObject1;
      k += 1;
      n = i2;
      localObject1 = localObject2;
      j = i1;
      i = m;
      break label375;
      if (!TextUtils.isEmpty(paramDPObject.getString("ContentTitle")))
      {
        localObject1 = paramDPObject.getString("ContentTitle");
        break;
      }
      localObject1 = paramDPObject.getString("Title");
      break;
      localObject2 = (TextView)localNovaLinearLayout.findViewById(R.id.room_status);
      ((TextView)localObject2).setVisibility(0);
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        ((TextView)localObject2).setText((CharSequence)localObject1);
        break label236;
      }
      ((TextView)localObject2).setText("有房");
      break label236;
      localNovaLinearLayout.findViewById(R.id.room_full).setVisibility(0);
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        ((TextView)localNovaLinearLayout.findViewById(R.id.room_full)).setText((CharSequence)localObject1);
        break label236;
      }
      ((TextView)localNovaLinearLayout.findViewById(R.id.room_full)).setText("满房");
      break label236;
      label803: if ((TextUtils.isEmpty((CharSequence)localObject2)) && (TextUtils.isEmpty((CharSequence)localObject1)))
        break label236;
      if (!TextUtils.isEmpty((CharSequence)localObject2))
      {
        localObject3 = (TextView)localNovaLinearLayout.findViewById(R.id.end_date);
        ((TextView)localObject3).setVisibility(0);
        ((TextView)localObject3).setText((CharSequence)localObject2);
      }
      if (TextUtils.isEmpty((CharSequence)localObject1))
        break label236;
      localObject2 = (TextView)localNovaLinearLayout.findViewById(R.id.order_policy);
      ((TextView)localObject2).setVisibility(0);
      ((TextView)localObject2).setText((CharSequence)localObject1);
      break label236;
      m = 0;
      break label544;
      label893: m = 0;
      j += 1;
      break label606;
      label905: localLayoutParams.addRule(0, i2 - 1);
      localLayoutParams.rightMargin = ViewUtils.dip2px(getContext(), 3.0F);
    }
    label932: localNovaLinearLayout.setTag(paramDPObject);
    if (paramBoolean)
    {
      localNovaLinearLayout.findViewById(R.id.hotel_tuan_root_containter).setPadding(ViewUtils.dip2px(getContext(), 12.0F), 0, 0, 0);
      localNovaLinearLayout.findViewById(16908308).setPadding(ViewUtils.dip2px(getContext(), 32.0F), 0, 0, 0);
    }
    while (true)
    {
      setBackground(localNovaLinearLayout.findViewById(R.id.layout), R.drawable.cell_item_white);
      return localNovaLinearLayout;
      localNovaLinearLayout.findViewById(R.id.hotel_tuan_root_containter).setPadding(ViewUtils.dip2px(getContext(), 42.0F), 0, 0, 0);
      localNovaLinearLayout.findViewById(16908308).setPadding(ViewUtils.dip2px(getContext(), 2.0F), 0, 0, 0);
    }
  }

  private TextView createTagView(String paramString, int paramInt)
  {
    TextView localTextView = new TextView(getContext());
    localTextView.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
    switch (paramInt)
    {
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      localTextView.setText(paramString);
      localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_10));
      return localTextView;
      localTextView.setTextColor(getResources().getColor(R.color.hotel_calender_weekend_color));
      localTextView.setBackgroundResource(R.drawable.hotel_promo_border);
      continue;
      localTextView.setTextColor(getResources().getColor(R.color.white));
      localTextView.setBackgroundResource(R.drawable.hotel_promo_solid);
    }
  }

  private SpannableStringBuilder formatHotelPrice(DPObject paramDPObject, boolean paramBoolean)
  {
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    if (paramBoolean);
    for (int i = this.res.getColor(R.color.middle_gray); ; i = -39373)
    {
      Object localObject2 = "";
      if (paramDPObject != null)
      {
        localObject2 = paramDPObject.getString("Price");
        localObject1 = localObject2;
        if (TextUtils.isEmpty((CharSequence)localObject2))
        {
          localObject1 = localObject2;
          if (Double.compare(paramDPObject.getDouble("Price"), 0.0D) > 0)
            localObject1 = PRICE_DF.format(paramDPObject.getDouble("Price"));
        }
        localObject2 = localObject1;
        if (TextUtils.isEmpty(paramDPObject.getString("MarketPrice")))
        {
          localObject2 = localObject1;
          if (Double.compare(paramDPObject.getDouble("MarketPrice"), 0.0D) > 0)
          {
            PRICE_DF.format(paramDPObject.getDouble("MarketPrice"));
            localObject2 = localObject1;
          }
        }
      }
      if (TextUtils.isEmpty((CharSequence)localObject2))
        break;
      paramDPObject = new SpannableStringBuilder();
      Object localObject1 = new SpannableString("￥" + (String)localObject2);
      ((SpannableString)localObject1).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
      ((SpannableString)localObject1).setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, ((SpannableString)localObject1).length(), 33);
      ((SpannableString)localObject1).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, ((SpannableString)localObject1).length(), 33);
      paramDPObject.append((CharSequence)localObject1);
      paramDPObject.append(" ");
      return paramDPObject;
    }
    paramDPObject = new SpannableString("惊爆价");
    paramDPObject.setSpan(new ForegroundColorSpan(i), 0, paramDPObject.length(), 18);
    paramDPObject.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_medium_1)), 0, paramDPObject.length(), 18);
    return (SpannableStringBuilder)(SpannableStringBuilder)localSpannableStringBuilder;
  }

  private void renderHotelTuanCell(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0));
    do
    {
      return;
      if (this.linearLayout == null)
      {
        this.linearLayout = new LinearLayout(getContext());
        this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.linearLayout.setOrientation(1);
      }
      this.linearLayout.removeAllViews();
      Object localObject = this.res.inflate(getContext(), R.layout.hotel_tuan_header, null, false);
      this.linearLayout.addView((View)localObject);
      int j = 0;
      if (this.TPHotelProductList != null)
        j = this.TPHotelProductList.getInt("HotelShowNum");
      int i = j;
      if (j == 0)
        i = 2;
      j = 0;
      while ((j < i) && (j < paramArrayOfDPObject.length))
      {
        this.linearLayout.addView(createHotelDealCell(paramArrayOfDPObject[j], false));
        j += 1;
      }
      if (paramArrayOfDPObject.length > i)
      {
        this.expandLayout = new LinearLayout(getContext());
        this.expandLayout.setOrientation(1);
        this.expandLayout.removeAllViews();
        localObject = new LinearLayout.LayoutParams(-1, -2);
        if (!this.isExpand)
          this.expandLayout.setVisibility(8);
        this.expandLayout.setLayoutParams((ViewGroup.LayoutParams)localObject);
        while (i < paramArrayOfDPObject.length)
        {
          this.expandLayout.addView(createHotelDealCell(paramArrayOfDPObject[i], false));
          i += 1;
        }
        this.linearLayout.addView(this.expandLayout);
        this.expandView = LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false);
        this.expandView.setTag("EXPAND");
        ((TextView)this.expandView.findViewById(16908308)).setText("查看全部" + paramArrayOfDPObject.length + "条团购");
        this.expandView.setClickable(true);
        this.expandView.setOnClickListener(this);
        this.linearLayout.addView(this.expandView);
        setExpandState();
      }
      addCell("0450Tuan.50HotelTuan", this.linearLayout, 0);
    }
    while (!this.isExpand);
    this.linearLayout.postDelayed(new Runnable()
    {
      public void run()
      {
        HotelTuanAgent.this.scrollToCenter();
      }
    }
    , 100L);
  }

  private void renderInTimeHotelTuanCell()
  {
    if (this.TPHotelProductList == null)
      return;
    renderHotelTuanCell(this.TPHotelProductList.getArray("HotelProducts"));
  }

  private void scrollToCenter()
  {
    ScrollView localScrollView = getFragment().getScrollView();
    localScrollView.setSmoothScrollingEnabled(true);
    localScrollView.requestChildFocus(this.linearLayout, this.linearLayout);
  }

  private void sendHotelDetailRequest(long paramLong1, long paramLong2)
  {
    String str2 = "http://m.api.dianping.com/hoteltg/tphotelproducts.hoteltg?shopid=" + shopId();
    String str1 = str2;
    if (getCity() != null)
      str1 = str2 + "&cityId=" + getCity().id();
    this.hotelTuanRequest = BasicMApiRequest.mapiGet(str1 + "&begindate=" + paramLong1 + "&enddate=" + paramLong2, CacheType.NORMAL);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (HotelTuanAgent.this.hotelTuanRequest != null)
          HotelTuanAgent.this.getFragment().mapiService().exec(HotelTuanAgent.this.hotelTuanRequest, HotelTuanAgent.this);
      }
    }
    , 100L);
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

  private void setExpandAction()
  {
    if (this.expandLayout == null)
      return;
    if (this.isExpand)
      this.expandLayout.setVisibility(0);
    while (true)
    {
      setExpandState();
      return;
      this.expandLayout.setVisibility(8);
    }
  }

  private void setExpandState()
  {
    if (this.expandView == null)
      return;
    if (this.isExpand)
    {
      ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.arrow_up_tuan);
      this.expandView.findViewById(16908308).setVisibility(8);
      return;
    }
    ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.arrow_down_tuan);
    this.expandView.findViewById(16908308).setVisibility(0);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (!isHotelType())
      return;
    if (paramBundle != null)
    {
      long l1 = paramBundle.getLong("checkin_time");
      long l2 = paramBundle.getLong("checkout_time");
      if ((l1 != 0L) && (l2 != 0L))
        sendHotelDetailRequest(l1, l2);
    }
    super.onAgentChanged(paramBundle);
  }

  public void onClick(View paramView)
  {
    boolean bool = true;
    if ((paramView.getTag() instanceof DPObject))
      try
      {
        paramView = (DPObject)paramView.getTag();
        Object localObject1 = paramView.getString("ProductUrl");
        if (!TextUtils.isEmpty((CharSequence)localObject1))
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + (String)localObject1));
          getContext().startActivity(paramView);
          return;
        }
        localObject1 = getShop();
        Object localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
        ((Intent)localObject2).putExtra("deal", paramView);
        ((Intent)localObject2).putExtra("shopId", shopId());
        getContext().startActivity((Intent)localObject2);
        localObject2 = new ArrayList();
        ((List)localObject2).add(new BasicNameValuePair("shopid", shopId() + ""));
        statisticsEvent("shopinfo5", "shopinfo5_tuan", String.valueOf(paramView.getInt("ID")), 1, (List)localObject2);
        localObject2 = new GAUserInfo();
        ((GAUserInfo)localObject2).deal_id = Integer.valueOf(paramView.getInt("ID"));
        ((GAUserInfo)localObject2).shop_id = Integer.valueOf(shopId());
        ((GAUserInfo)localObject2).query_id = getFragment().getStringParam("query_id");
        if (localObject1 != null)
          ((GAUserInfo)localObject2).category_id = Integer.valueOf(((DPObject)localObject1).getInt("CategoryID"));
        GAHelper.instance().contextStatisticsEvent(getContext(), "hotel_tg", (GAUserInfo)localObject2, "tap");
        return;
      }
      catch (java.lang.Exception paramView)
      {
        Log.e("shop", "fail to launch deal", paramView);
        return;
      }
    if ((paramView.getTag() == "EXPAND") || (paramView.getTag() == "COLLAPSE"))
    {
      if (!this.isExpand)
      {
        this.isExpand = bool;
        if (!this.isExpand)
          break label363;
        statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "展开", 0);
      }
      while (true)
      {
        setExpandAction();
        scrollToCenter();
        return;
        bool = false;
        break;
        label363: statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "收起", 0);
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    if (!isHotelType());
    do
    {
      return;
      super.onCreate(paramBundle);
      if (paramBundle != null)
      {
        this.isExpand = paramBundle.getBoolean("isExpand");
        this.TPHotelProductList = ((DPObject)paramBundle.getParcelable("TPHotelProductList"));
      }
      if (this.TPHotelProductList == null)
        continue;
      renderInTimeHotelTuanCell();
    }
    while (this.hotelTuanRequest != null);
    paramBundle = getFragment().getActivity().getIntent();
    long l2 = System.currentTimeMillis();
    long l3 = l2 + 86400000L;
    if (paramBundle != null)
    {
      l2 = paramBundle.getLongExtra("checkinTime", System.currentTimeMillis());
      l3 = paramBundle.getLongExtra("checkoutTime", System.currentTimeMillis() + 86400000L);
    }
    long l4 = l2;
    try
    {
      paramBundle = getFragment().getActivity().getIntent().getData().getQueryParameter("begindate");
      l4 = l2;
      String str = getFragment().getActivity().getIntent().getData().getQueryParameter("enddate");
      long l1 = l2;
      l4 = l2;
      if (!TextUtils.isEmpty(paramBundle))
      {
        l1 = l2;
        l4 = l2;
        if (TextUtils.isDigitsOnly(paramBundle))
        {
          l4 = l2;
          l1 = Long.parseLong(paramBundle);
        }
      }
      l2 = l1;
      l5 = l3;
      l4 = l1;
      if (!TextUtils.isEmpty(str))
      {
        l2 = l1;
        l5 = l3;
        l4 = l1;
        if (TextUtils.isDigitsOnly(str))
        {
          l4 = l1;
          l5 = Long.parseLong(str);
          l2 = l1;
        }
      }
      sendHotelDetailRequest(l2, l5);
      return;
    }
    catch (java.lang.Exception paramBundle)
    {
      while (true)
      {
        l2 = l4;
        long l5 = l3;
      }
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.hotelTuanRequest != null)
    {
      mapiService().abort(this.hotelTuanRequest, this, true);
      this.hotelTuanRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.hotelTuanRequest)
      this.hotelTuanRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.hotelTuanRequest == paramMApiRequest)
    {
      this.hotelTuanRequest = null;
      this.TPHotelProductList = ((DPObject)paramMApiResponse.result());
      if (this.TPHotelProductList != null)
      {
        removeAllCells();
        renderInTimeHotelTuanCell();
      }
      paramMApiRequest = new Bundle();
      paramMApiRequest.putParcelable("TPHotelProductList", this.TPHotelProductList);
      paramMApiRequest.putString("_host", "shopinfo/hoteltuan");
      paramMApiResponse = new AgentMessage("com.dianping.shopinfo.hotel.HotelTuanAgent.HOTEL_TUAN_LOAD_DATA_HOTEL_PRODUCTS");
      paramMApiResponse.body = paramMApiRequest;
      dispatchMessage(paramMApiResponse);
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putBoolean("isExpand", this.isExpand);
    localBundle.putParcelable("TPHotelProductList", this.TPHotelProductList);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelTuanAgent
 * JD-Core Version:    0.6.0
 */