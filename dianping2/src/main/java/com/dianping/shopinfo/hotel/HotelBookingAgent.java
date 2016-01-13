package com.dianping.shopinfo.hotel;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class HotelBookingAgent extends ShopCellAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_HOTEL_BOOKING_SELECT_TIME = "0300Hotel.51HotelBookingSelectTime";
  private static final int HOTEL_BOOKING_ITEM_LIMIT = 3;
  private static final DecimalFormat PRICE_DF;
  public static final int REQUEST_HOTEL_BOOKING = 4353;
  private static final SimpleDateFormat SDF;
  private static final SimpleDateFormat SDF2;
  private static final String TAG = HotelBookingAgent.class.getSimpleName();
  private static final int TYPE_FULL = 1;
  private static final int TYPE_REBATE = 4;
  private static final int TYPE_TIGHT = 2;
  private long checkinTimeMills;
  private long checkoutTimeMills;
  private LinearLayout expandLayout;
  private View expandView;
  private MApiRequest hotelBookingRequest;
  private ViewGroup hotelBookingView;
  private DPObject hotelList;
  private boolean isExpand;
  private boolean isHotelBookable;
  private LinearLayout linearLayout;
  private DPObject[] otaHotelPriceList;
  private long traceid;

  static
  {
    SDF = new SimpleDateFormat("M月dd日", Locale.getDefault());
    SDF2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    PRICE_DF = new DecimalFormat("#.###");
  }

  public HotelBookingAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void addPromoInfoExplain(DPObject paramDPObject)
  {
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.hotel_promo_info_explain, getParentView(), false);
    ((TextView)localView.findViewById(R.id.ota_promo)).setText(paramDPObject.getString("PromoName"));
    ((TextView)localView.findViewById(R.id.ota_promo_explain)).setText(paramDPObject.getString("PromoIntro"));
    localView.getLayoutParams().height = -2;
    localView.setBackgroundResource(R.drawable.clickable_lightyellow_to_gray);
    localView.findViewById(R.id.general_promo_info_content).setPadding(ViewUtils.dip2px(getContext(), 40.0F), 0, 0, 0);
    localView.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        Object localObject = this.val$otaPromoInfo.getString("Url");
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          if (!((String)localObject).startsWith("http"))
            break label75;
          paramView = Uri.parse("dianping://hotelbookingweb");
          localObject = Uri.parse((String)localObject);
          paramView = new Intent("android.intent.action.VIEW", paramView.buildUpon().appendQueryParameter("url", ((Uri)localObject).toString()).build());
          HotelBookingAgent.this.getFragment().startActivity(paramView);
        }
        label75: 
        do
          return;
        while (!((String)localObject).startsWith("dianping"));
        paramView = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
        HotelBookingAgent.this.getFragment().startActivity(paramView);
      }
    });
    this.linearLayout.addView(localView);
  }

  private View createHotelBookingCell(DPObject paramDPObject, boolean paramBoolean1, boolean paramBoolean2)
  {
    View localView = this.res.inflate(getContext(), R.layout.item_shopinfo_hotel_booking_item, getParentView(), false);
    localView.setClickable(true);
    localView.setOnClickListener(this);
    ((TextView)localView.findViewById(16908308)).setText(paramDPObject.getString("Name"));
    Object localObject2;
    if ((paramDPObject != null) && (paramDPObject.getString("Name") != null) && (paramDPObject.getString("Name").contains("Booking.com")))
    {
      localObject1 = new Bundle();
      ((Bundle)localObject1).putParcelable("booking_price", paramDPObject);
      localObject2 = new AgentMessage("com.dianping.shopinfo.hotel.HotelBookingAgent.HOTEL_BOOKING_SEND_BOOK_PRICE");
      ((AgentMessage)localObject2).body = ((Bundle)localObject1);
      dispatchMessage((AgentMessage)localObject2);
    }
    Object localObject1 = paramDPObject.getArray("TagList");
    boolean bool3 = false;
    boolean bool1 = false;
    boolean bool2 = bool3;
    int j;
    int i;
    if (localObject1 != null)
    {
      bool2 = bool3;
      if (localObject1.length > 0)
      {
        j = localObject1.length;
        i = 0;
        bool2 = bool1;
        if (i < j)
        {
          localObject2 = localObject1[i];
          bool2 = bool1;
          switch (((DPObject)localObject2).getInt("Type"))
          {
          default:
            bool2 = bool1;
          case 3:
          case 1:
          case 2:
          case 4:
          }
          while (true)
          {
            i += 1;
            bool1 = bool2;
            break;
            bool2 = true;
            localView.findViewById(R.id.booking_status_full).setVisibility(0);
            ((TextView)localView.findViewById(R.id.ota_promo_rebate)).setTextColor(this.res.getColor(R.color.light_gray));
            continue;
            localView.findViewById(R.id.booking_status_tight).setVisibility(0);
            bool2 = bool1;
            continue;
            localView.findViewById(R.id.ota_promo_rebate).setVisibility(0);
            ((TextView)localView.findViewById(R.id.ota_promo_rebate)).setText(((DPObject)localObject2).getString("Title"));
            bool2 = bool1;
          }
        }
      }
    }
    localObject1 = paramDPObject.getStringArray("OTAPromoList");
    int k;
    TextView localTextView1;
    if ((localObject1 != null) && (localObject1.length > 0))
    {
      localObject2 = (LinearLayout)localView.findViewById(R.id.promo_list_1);
      ((LinearLayout)localObject2).setVisibility(0);
      j = 0;
      k = localObject1.length;
      i = 0;
      while (i < k)
      {
        localTextView1 = createTagView(localObject1[i], 1);
        localTextView1.measure(0, 0);
        if (localTextView1.getMeasuredWidth() + j >= ViewUtils.dip2px(getContext(), 130.0F))
          break;
        ((LinearLayout)localObject2).addView(localTextView1);
        j += localTextView1.getMeasuredWidth();
        i += 1;
      }
    }
    localObject1 = paramDPObject.getStringArray("OTARefundList");
    if ((localObject1 != null) && (localObject1.length > 0))
    {
      localObject2 = (LinearLayout)localView.findViewById(R.id.promo_list_2);
      ((LinearLayout)localObject2).setVisibility(0);
      j = 0;
      k = localObject1.length;
      i = 0;
      while (i < k)
      {
        localTextView1 = localObject1[i];
        TextView localTextView2 = createTagView(localTextView1, 1);
        localTextView2.measure(0, 0);
        if (localTextView2.getMeasuredWidth() + j >= ViewUtils.dip2px(getContext(), 150.0F))
          break;
        ((LinearLayout)localObject2).addView(createTagView(localTextView1, 1));
        j += localTextView2.getMeasuredWidth();
        i += 1;
      }
    }
    if (paramBoolean2)
    {
      ((TextView)localView.findViewById(16908309)).setTextColor(getResources().getColor(R.color.light_gray));
      ((TextView)localView.findViewById(16908309)).setText("加载中....");
      localView.setTag(paramDPObject);
      if (!paramBoolean1)
        break label738;
      localView.findViewById(R.id.booking_item_root_containter).setPadding(ViewUtils.dip2px(getContext(), 12.0F), 0, 0, 0);
      localView.findViewById(16908308).setPadding(ViewUtils.dip2px(getContext(), 32.0F), 0, 0, 0);
    }
    while (true)
    {
      setBackground(localView.findViewById(R.id.layout), R.drawable.cell_item_white);
      return localView;
      ((TextView)localView.findViewById(16908309)).setText(formatHotelPrice(paramDPObject, bool2));
      break;
      label738: localView.findViewById(R.id.booking_item_root_containter).setPadding(ViewUtils.dip2px(getContext(), 42.0F), 0, 0, 0);
      localView.findViewById(16908308).setPadding(ViewUtils.dip2px(getContext(), 2.0F), 0, 0, 0);
    }
  }

  private TextView createTagView(String paramString, int paramInt)
  {
    TextView localTextView = new TextView(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 2.0F);
    localTextView.setLayoutParams(localLayoutParams);
    localTextView.setSingleLine();
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

  private SpannableString formatHotelPrice(DPObject paramDPObject, boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = this.res.getColor(R.color.middle_gray); (paramDPObject != null) && (Double.compare(paramDPObject.getDouble("Price"), 0.0D) >= 0); i = getResources().getColor(R.color.light_red))
    {
      paramDPObject = new SpannableString("￥" + PRICE_DF.format(paramDPObject.getDouble("Price")) + "起");
      paramDPObject.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
      paramDPObject.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, paramDPObject.length() - 1, 33);
      paramDPObject.setSpan(new ForegroundColorSpan(i), 0, paramDPObject.length() - 1, 33);
      paramDPObject.setSpan(new ForegroundColorSpan(this.res.getColor(R.color.light_gray)), paramDPObject.length() - 1, paramDPObject.length(), 18);
      paramDPObject.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_small)), paramDPObject.length() - 1, paramDPObject.length(), 18);
      return paramDPObject;
    }
    if (paramDPObject != null);
    for (paramDPObject = paramDPObject.getString("PriceText"); ; paramDPObject = "惊爆价")
    {
      paramDPObject = new SpannableString(paramDPObject);
      paramDPObject.setSpan(new ForegroundColorSpan(i), 0, paramDPObject.length(), 18);
      paramDPObject.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_medium_1)), 0, paramDPObject.length(), 18);
      return paramDPObject;
    }
  }

  private void scrollToCenter()
  {
    ScrollView localScrollView = getFragment().getScrollView();
    localScrollView.setSmoothScrollingEnabled(true);
    localScrollView.requestChildFocus(this.linearLayout, this.linearLayout);
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

  private void setupView()
  {
    Object localObject1 = getShop();
    int j;
    int i;
    int k;
    Object localObject2;
    boolean bool;
    if (this.isHotelBookable)
    {
      if (this.otaHotelPriceList == null)
        this.otaHotelPriceList = ((DPObject)localObject1).getArray("OtaHotelPriceList");
      if (this.hotelList == null)
        break label745;
      localObject1 = this.hotelList.getArray("List");
      j = this.hotelList.getInt("ShowNum");
      i = j;
      if (j == 0)
        i = 3;
      if ((localObject1 != null) && (localObject1.length > 0))
      {
        if (this.linearLayout == null)
        {
          this.linearLayout = new LinearLayout(getContext());
          this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
          this.linearLayout.setOrientation(1);
        }
        this.linearLayout.removeAllViews();
        this.hotelBookingView = ((ViewGroup)this.res.inflate(getContext(), R.layout.shopinfo_item_hotel_booking, getParentView(), false));
        this.hotelBookingView.setTag("0300Hotel.51HotelBookingSelectTime");
        this.hotelBookingView.setClickable(true);
        this.hotelBookingView.setOnClickListener(this);
        this.linearLayout.addView(this.hotelBookingView);
        k = localObject1.length;
        DPObject localDPObject;
        if (k <= i)
        {
          i = 0;
          if (i < k)
          {
            localObject2 = this.linearLayout;
            localDPObject = localObject1[i];
            if (i == 0);
            for (bool = true; ; bool = false)
            {
              ((LinearLayout)localObject2).addView(createHotelBookingCell(localDPObject, bool, false));
              i += 1;
              break;
            }
          }
        }
        else
        {
          j = 0;
          if (j < i)
          {
            localObject2 = this.linearLayout;
            localDPObject = localObject1[j];
            if (j == 0);
            for (bool = true; ; bool = false)
            {
              ((LinearLayout)localObject2).addView(createHotelBookingCell(localDPObject, bool, false));
              j += 1;
              break;
            }
          }
          this.expandLayout = new LinearLayout(getContext());
          this.expandLayout.setOrientation(1);
          this.expandLayout.removeAllViews();
          localObject2 = new LinearLayout.LayoutParams(-1, -2);
          if (!this.isExpand)
            this.expandLayout.setVisibility(8);
          this.expandLayout.setLayoutParams((ViewGroup.LayoutParams)localObject2);
          if (i < k)
          {
            localObject2 = this.expandLayout;
            localDPObject = localObject1[i];
            if (i == 0);
            for (bool = true; ; bool = false)
            {
              ((LinearLayout)localObject2).addView(createHotelBookingCell(localDPObject, bool, false));
              i += 1;
              break;
            }
          }
          this.linearLayout.addView(this.expandLayout);
          this.expandView = LayoutInflater.from(getContext()).inflate(R.layout.hotel_booking_expand, getParentView(), false);
          this.expandView.setTag("EXPAND");
          ((TextView)this.expandView.findViewById(16908308)).setText("查看全部" + localObject1.length + "条");
          this.expandView.setClickable(true);
          this.expandView.setOnClickListener(this);
          this.linearLayout.addView(this.expandView);
          setExpandState();
        }
        localObject1 = this.hotelList.getArray("OTAPromoInfoList");
        if ((localObject1 != null) && (localObject1.length > 0))
          addPromoInfoExplain(localObject1[0]);
        addCell("", this.linearLayout, 0);
        localObject1 = this.hotelList.getString("TaxesTips");
        if (!TextUtils.isEmpty((CharSequence)localObject1))
        {
          localObject2 = new TextView(getContext());
          ((TextView)localObject2).setText((CharSequence)localObject1);
          ((TextView)localObject2).setTextSize(0, this.res.getDimension(R.dimen.text_medium));
          ((TextView)localObject2).setTextColor(this.res.getColor(R.color.light_gray));
          ((TextView)localObject2).setLayoutParams(new ViewGroup.LayoutParams(-1, this.res.getDimensionPixelSize(R.dimen.section_height_with_text)));
          ((TextView)localObject2).setGravity(5);
          ((TextView)localObject2).setBackgroundColor(this.res.getColor(R.color.common_bk_color));
          ((TextView)localObject2).setPadding(0, ViewUtils.dip2px(getContext(), 5.0F), ViewUtils.dip2px(getContext(), 10.0F), 0);
          addCell("", (View)localObject2, 1024);
        }
        updateSearchDateheaderView(this.checkinTimeMills, this.checkoutTimeMills);
      }
    }
    return;
    label745: if ((this.otaHotelPriceList != null) && (this.otaHotelPriceList.length > 0))
    {
      Log.d(TAG, "add prev otaHotelPriceList , otaHotelPriceList's length = " + this.otaHotelPriceList.length);
      if (this.linearLayout == null)
      {
        this.linearLayout = new LinearLayout(getContext());
        this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.linearLayout.setOrientation(1);
      }
      Log.d(TAG, "linearLayout's childCount = " + this.linearLayout.getChildCount());
      this.linearLayout.removeAllViews();
      this.hotelBookingView = ((ViewGroup)this.res.inflate(getContext(), R.layout.shopinfo_item_hotel_booking, getParentView(), false));
      this.hotelBookingView.setTag("0300Hotel.51HotelBookingSelectTime");
      this.hotelBookingView.setClickable(true);
      this.hotelBookingView.setOnClickListener(this);
      this.linearLayout.addView(this.hotelBookingView);
      k = this.otaHotelPriceList.length;
      i = 3;
      if (this.hotelList != null)
        i = this.hotelList.getInt("ShowNum");
      if (k <= i)
      {
        i = 0;
        if (i < k)
        {
          localObject1 = this.linearLayout;
          localObject2 = this.otaHotelPriceList[i];
          if (i == 0);
          for (bool = true; ; bool = false)
          {
            ((LinearLayout)localObject1).addView(createHotelBookingCell((DPObject)localObject2, bool, true));
            i += 1;
            break;
          }
        }
      }
      else
      {
        j = 0;
        if (j < i)
        {
          localObject1 = this.linearLayout;
          localObject2 = this.otaHotelPriceList[j];
          if (j == 0);
          for (bool = true; ; bool = false)
          {
            ((LinearLayout)localObject1).addView(createHotelBookingCell((DPObject)localObject2, bool, true));
            j += 1;
            break;
          }
        }
        if (this.expandLayout == null)
        {
          this.expandLayout = new LinearLayout(getContext());
          this.expandLayout.setOrientation(1);
        }
        this.expandLayout.removeAllViews();
        localObject1 = new LinearLayout.LayoutParams(-1, -2);
        if (!this.isExpand)
          this.expandLayout.setVisibility(8);
        this.expandLayout.setLayoutParams((ViewGroup.LayoutParams)localObject1);
        if (i < k)
        {
          localObject1 = this.expandLayout;
          localObject2 = this.otaHotelPriceList[i];
          if (i == 0);
          for (bool = true; ; bool = false)
          {
            ((LinearLayout)localObject1).addView(createHotelBookingCell((DPObject)localObject2, bool, true));
            i += 1;
            break;
          }
        }
        this.linearLayout.addView(this.expandLayout);
        this.expandView = LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false);
        this.expandView.setTag("EXPAND");
        ((TextView)this.expandView.findViewById(16908308)).setText("查看全部" + this.otaHotelPriceList.length + "条");
        this.expandView.setClickable(true);
        this.expandView.setOnClickListener(this);
        this.linearLayout.addView(this.expandView);
        setExpandState();
      }
      addCell("", this.linearLayout, 0);
      updateSearchDateheaderView(this.checkinTimeMills, this.checkoutTimeMills);
      return;
    }
    this.linearLayout = new LinearLayout(getContext());
    this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.linearLayout.setOrientation(1);
    this.hotelBookingView = ((ViewGroup)this.res.inflate(getContext(), R.layout.shopinfo_item_hotel_booking, getParentView(), false));
    this.hotelBookingView.setTag("0300Hotel.51HotelBookingSelectTime");
    this.hotelBookingView.setClickable(true);
    this.hotelBookingView.setOnClickListener(this);
    this.linearLayout.addView(this.hotelBookingView);
    this.linearLayout.addView(createLoadingCell());
    updateSearchDateheaderView(this.checkinTimeMills, this.checkoutTimeMills);
    addCell("", this.linearLayout, 0);
  }

  private void updateSearchDateheaderView(long paramLong1, long paramLong2)
  {
    if (this.hotelBookingView != null)
    {
      String str = SDF.format(Long.valueOf(paramLong1));
      SpannableString localSpannableString = new SpannableString(str + "入住" + " - ");
      localSpannableString.setSpan(new ForegroundColorSpan(this.res.getColor(R.color.light_gray)), str.length(), str.length() + 2, 18);
      ((TextView)this.hotelBookingView.findViewById(16908308)).setText(localSpannableString);
      str = SDF.format(Long.valueOf(paramLong2));
      localSpannableString = new SpannableString(str + "退房");
      localSpannableString.setSpan(new ForegroundColorSpan(this.res.getColor(R.color.light_gray)), str.length(), str.length() + 2, 18);
      ((TextView)this.hotelBookingView.findViewById(16908309)).setText(localSpannableString);
      int i = (int)((paramLong2 - paramLong1) / 86400000L);
      ((TextView)this.hotelBookingView.findViewById(R.id.day_num)).setText("共" + i + "天");
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 4353)
    {
      if (paramIntent != null)
      {
        long l1 = paramIntent.getLongExtra("checkin_time", System.currentTimeMillis());
        long l2 = paramIntent.getLongExtra("checkout_time", 86400000L + l1);
        if ((l1 != this.checkinTimeMills) || (l2 != this.checkoutTimeMills))
        {
          this.checkinTimeMills = l1;
          this.checkoutTimeMills = l2;
          sendRequest();
          this.hotelList = null;
          dispatchAgentChanged(false);
          paramIntent = new Bundle();
          paramIntent.putLong("checkin_time", l1);
          paramIntent.putLong("checkout_time", l2);
          dispatchAgentChanged("shopinfo/hoteltuan", paramIntent);
          updateSearchDateheaderView(l1, l2);
          paramIntent = getFragment().getActivity().getIntent();
          paramIntent.putExtra("checkin_time", l1);
          paramIntent.putExtra("checkout_time", l2);
          getFragment().getActivity().setResult(-1, paramIntent);
        }
      }
      return;
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (!isHotelType());
    do
    {
      do
      {
        return;
        super.onAgentChanged(paramBundle);
      }
      while ((getShop() == null) || (!getFragment().isUrlAvailable("dianping://hotelbookingpicktime")));
      if (this.isHotelBookable)
        continue;
      this.isHotelBookable = getShop().getBoolean("HotelBooking");
    }
    while (this.hotelList == null);
    setupView();
  }

  public void onClick(View paramView)
  {
    Object localObject1;
    Object localObject2;
    int i;
    Object localObject3;
    if ((paramView.getTag() instanceof DPObject))
    {
      paramView = (DPObject)paramView.getTag();
      localObject1 = paramView.getString("Name");
      localObject2 = paramView.getArray("TagList");
      i = 0;
      int k = 0;
      if (localObject2 != null)
      {
        int m = localObject2.length;
        int j = 0;
        i = k;
        if (j < m)
        {
          if (localObject2[j].getType("Type") == 3);
          for (i = 1; ; i = 0)
          {
            j += 1;
            break;
          }
        }
      }
      localObject3 = paramView.getString("Url");
      Log.d(TAG, "click url = " + (String)localObject3);
      if (localObject3 != null)
      {
        if (!((String)localObject3).startsWith("http"))
          break label390;
        localObject2 = Uri.parse("dianping://hotelbookingweb");
        localObject3 = Uri.parse((String)localObject3).buildUpon().appendQueryParameter("shopId", shopId() + "").appendQueryParameter("startDate", SDF2.format(Long.valueOf(this.checkinTimeMills))).appendQueryParameter("endDate", SDF2.format(Long.valueOf(this.checkoutTimeMills))).build();
        localObject2 = new Intent("android.intent.action.VIEW", ((Uri)localObject2).buildUpon().appendQueryParameter("url", ((Uri)localObject3).toString()).build());
        ((Intent)localObject2).putExtra("name", (String)localObject1);
        getFragment().startActivity((Intent)localObject2);
      }
    }
    label302: 
    do
      while (true)
      {
        if (((String)localObject1).contains("携程"))
        {
          statisticsEvent("shopinfo5", "shopinfo5_hotelsupplier", "ctrip", i);
          localObject1 = new GAUserInfo();
          ((GAUserInfo)localObject1).shop_id = Integer.valueOf(shopId());
          ((GAUserInfo)localObject1).query_id = getFragment().getStringParam("query_id");
          localObject2 = getShop();
          if (localObject2 != null)
            ((GAUserInfo)localObject1).category_id = Integer.valueOf(((DPObject)localObject2).getInt("CategoryID"));
          ((GAUserInfo)localObject1).butag = Integer.valueOf(paramView.getInt("OtaID"));
          GAHelper.instance().contextStatisticsEvent(getContext(), "hotel_ota", (GAUserInfo)localObject1, "tap");
        }
        while (true)
        {
          return;
          if (!((String)localObject3).startsWith("dianping"))
            break label666;
          localObject2 = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject3));
          ((Intent)localObject2).putExtra("query_id", getFragment().getStringParam("query_id"));
          ((Intent)localObject2).putExtra("trace_id", this.traceid);
          getFragment().startActivityForResult((Intent)localObject2, 4353);
          break;
          if (((String)localObject1).contains("艺龙"))
          {
            statisticsEvent("shopinfo5", "shopinfo5_hotelsupplier", "elong", i);
            break label302;
          }
          statisticsEvent("shopinfo5", "shopinfo5_hotelsupplier", (String)localObject1, i);
          break label302;
          if (!"0300Hotel.51HotelBookingSelectTime".equals(paramView.getTag()))
            break label667;
          paramView = null;
          if (this.hotelList != null)
            paramView = this.hotelList.getArray("List");
          while (paramView != null)
          {
            localObject1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelbookingpicktime?checkin_time=" + this.checkinTimeMills + "&checkout_time=" + this.checkoutTimeMills));
            ((Intent)localObject1).putExtra("shopId", shopId());
            localObject2 = new ArrayList();
            Collections.addAll((Collection)localObject2, paramView);
            ((Intent)localObject1).putParcelableArrayListExtra("hotelDetailArray", (ArrayList)localObject2);
            getFragment().startActivityForResult((Intent)localObject1, 4353);
            statisticsEvent("shopinfo5", "shopinfo5_hoteldate", "", 0);
            return;
            if (this.otaHotelPriceList == null)
              continue;
            paramView = this.otaHotelPriceList;
          }
        }
      }
    while ((paramView.getTag() != "EXPAND") && (paramView.getTag() != "COLLAPSE"));
    label390: label666: label667: boolean bool;
    if (!this.isExpand)
    {
      bool = true;
      this.isExpand = bool;
      if (!this.isExpand)
        break label739;
      statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "展开", 0);
    }
    while (true)
    {
      setExpandAction();
      scrollToCenter();
      return;
      bool = false;
      break;
      label739: statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "收起", 0);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.hotelList = ((DPObject)paramBundle.getParcelable("hotelDetail"));
      this.checkinTimeMills = paramBundle.getLong("checkinTime");
      this.checkoutTimeMills = paramBundle.getLong("checkoutTime");
      this.isHotelBookable = paramBundle.getBoolean("isHotelBookable");
      this.isExpand = paramBundle.getBoolean("isExpand");
    }
    label134: label191: label208: label227: 
    while (true)
    {
      this.traceid = System.currentTimeMillis();
      if (this.hotelList == null)
        sendRequest();
      return;
      paramBundle = getFragment().getActivity().getIntent();
      String str;
      if (paramBundle != null)
      {
        str = getFragment().getStringParam("begindate");
        if ((TextUtils.isEmpty(str)) || (!TextUtils.isDigitsOnly(str)))
          break label191;
        this.checkinTimeMills = Long.parseLong(str);
        str = getFragment().getStringParam("enddate");
        if ((TextUtils.isEmpty(str)) || (!TextUtils.isDigitsOnly(str)))
          break label208;
      }
      for (this.checkoutTimeMills = Long.parseLong(str); ; this.checkoutTimeMills = paramBundle.getLongExtra("checkoutTime", System.currentTimeMillis() + 86400000L))
      {
        if (getShop() == null)
          break label227;
        this.isHotelBookable = getShop().getBoolean("HotelBooking");
        break;
        this.checkinTimeMills = paramBundle.getLongExtra("checkinTime", System.currentTimeMillis());
        break label134;
      }
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.hotelBookingRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.hotelBookingRequest = null;
    this.hotelList = ((DPObject)paramMApiResponse.result());
    paramMApiRequest = this.hotelList.getString("HotelAnnounce");
    dispatchAgentChanged(false);
    if (!TextUtils.isEmpty(paramMApiRequest))
    {
      paramMApiResponse = new Bundle();
      paramMApiResponse.putString("HotelAnnounce", paramMApiRequest);
      dispatchAgentChanged("shopinfo/hotelannounce", paramMApiResponse);
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("hotelDetail", this.hotelList);
    localBundle.putLong("checkinTime", this.checkinTimeMills);
    localBundle.putLong("checkoutTime", this.checkoutTimeMills);
    localBundle.putBoolean("isHotelBookable", this.isHotelBookable);
    localBundle.putBoolean("isExpand", this.isExpand);
    return localBundle;
  }

  public void sendRequest()
  {
    String str = "http://m.api.dianping.com/bookinghotel.bin?shopid=" + shopId() + "&begindate=" + this.checkinTimeMills + "&enddate=" + this.checkoutTimeMills + "&traceid=" + this.traceid;
    GAUserInfo localGAUserInfo = new GAUserInfo();
    localGAUserInfo.query_id = getFragment().getStringParam("query_id");
    localGAUserInfo.title = (this.traceid + "");
    GAHelper.instance().contextStatisticsEvent(getFragment().getActivity(), "hotel_order_trace", localGAUserInfo, "view");
    this.hotelBookingRequest = BasicMApiRequest.mapiGet(str, CacheType.DISABLED);
    if (this.hotelBookingRequest != null)
      getFragment().mapiService().exec(this.hotelBookingRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelBookingAgent
 * JD-Core Version:    0.6.0
 */