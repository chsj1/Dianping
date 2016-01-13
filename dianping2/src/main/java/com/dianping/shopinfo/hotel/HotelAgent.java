package com.dianping.shopinfo.hotel;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
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
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class HotelAgent extends ShopCellAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_HOTEL_BOOKING_BRANCH = "7600HotelBranchInf.";
  private static final String CELL_HOTEL_BOOKING_DETAIL = "7500Hoteshopinfo.";
  private static final String CELL_HOTEL_BOOKING_ITEM = "0300Hotel.52HotelBookingItem.";
  private static final String CELL_HOTEL_BOOKING_ITEM_LOWER = "0460Hotel.52HotelBookingItem.";
  private static final String CELL_HOTEL_BOOKING_SELECT_TIME = "0300Hotel.51HotelBookingSelectTime";
  private static final String CELL_HOTEL_BOOKING_TRAFFIC = "7600HotelTraficInfo.";
  private static final int HOTEL_BOOKING_ITEM_LIMIT = 3;
  private static final DecimalFormat PRICE_DF;
  public static final int REQUEST_HOTEL_BOOKING = 4353;
  private static final SimpleDateFormat SDF;
  private static final SimpleDateFormat SDF2;
  private static final String TAG = HotelAgent.class.getSimpleName();
  private static final int TYPE_FULL = 1;
  private static final int TYPE_REBATE = 4;
  private static final int TYPE_TIGHT = 2;
  long checkinTimeMills;
  long checkoutTimeMills;
  LinearLayout expandLayout;
  View expandView;
  ViewGroup hotelBookingView;
  MApiRequest hotelDetailRequest;
  DPObject hotelInfo;
  private JSONObject hotelJson;
  DPObject hotelList;
  private boolean isExpand;
  private boolean isHotelBookable;
  LinearLayout linearLayout;
  DPObject[] otaHotelPriceList;
  MApiRequest request;
  private int sortType = 1;
  private long traceid;

  static
  {
    SDF2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    SDF = new SimpleDateFormat("M月dd日", Locale.getDefault());
    PRICE_DF = new DecimalFormat("#.###");
  }

  public HotelAgent(Object paramObject)
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
          HotelAgent.this.getFragment().startActivity(paramView);
        }
        label75: 
        do
          return;
        while (!((String)localObject).startsWith("dianping"));
        paramView = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
        HotelAgent.this.getFragment().startActivity(paramView);
      }
    });
    this.linearLayout.addView(localView);
  }

  private void clickToTrafficInfo()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hoteltraffic?cityId=" + getShop().getInt("CityID")));
    localIntent.putExtra("hoteldetail", this.hotelInfo);
    localIntent.putExtra("shopInfo", getShop());
    localIntent.putExtra("HotelDetailStatus", 1);
    getFragment().startActivity(localIntent);
    statisticsEvent("shopinfo5", "shopinfo5_hotellocation", "", shopId());
  }

  private View createHotelBookingCell(DPObject paramDPObject, boolean paramBoolean1, boolean paramBoolean2)
  {
    View localView = this.res.inflate(getContext(), R.layout.item_shopinfo_hotel_booking_item, getParentView(), false);
    localView.setClickable(true);
    localView.setOnClickListener(this);
    ((TextView)localView.findViewById(16908308)).setText(paramDPObject.getString("Name"));
    if ((paramDPObject != null) && (paramDPObject.getString("Name") != null) && (paramDPObject.getString("Name").contains("Booking.com")))
    {
      localObject = new Bundle();
      ((Bundle)localObject).putParcelable("booking_price", paramDPObject);
      dispatchAgentChanged("shopinfo/hotelreview", (Bundle)localObject);
    }
    Object localObject = paramDPObject.getArray("TagList");
    boolean bool3 = false;
    boolean bool1 = false;
    boolean bool2 = bool3;
    int j;
    int i;
    LinearLayout localLinearLayout;
    if (localObject != null)
    {
      bool2 = bool3;
      if (localObject.length > 0)
      {
        j = localObject.length;
        i = 0;
        bool2 = bool1;
        if (i < j)
        {
          localLinearLayout = localObject[i];
          bool2 = bool1;
          switch (localLinearLayout.getInt("Type"))
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
            ((TextView)localView.findViewById(R.id.ota_promo_rebate)).setText(localLinearLayout.getString("Title"));
            bool2 = bool1;
          }
        }
      }
    }
    localObject = paramDPObject.getStringArray("OTAPromoList");
    int k;
    TextView localTextView1;
    if ((localObject != null) && (localObject.length > 0))
    {
      localLinearLayout = (LinearLayout)localView.findViewById(R.id.promo_list_1);
      localLinearLayout.setVisibility(0);
      j = 0;
      k = localObject.length;
      i = 0;
      while (i < k)
      {
        localTextView1 = createTagView(localObject[i], 1);
        localTextView1.measure(0, 0);
        if (localTextView1.getMeasuredWidth() + j >= ViewUtils.dip2px(getContext(), 130.0F))
          break;
        localLinearLayout.addView(localTextView1);
        j += localTextView1.getMeasuredWidth();
        i += 1;
      }
    }
    localObject = paramDPObject.getStringArray("OTARefundList");
    if ((localObject != null) && (localObject.length > 0))
    {
      localLinearLayout = (LinearLayout)localView.findViewById(R.id.promo_list_2);
      localLinearLayout.setVisibility(0);
      j = 0;
      k = localObject.length;
      i = 0;
      while (i < k)
      {
        localTextView1 = localObject[i];
        TextView localTextView2 = createTagView(localTextView1, 1);
        localTextView2.measure(0, 0);
        if (localTextView2.getMeasuredWidth() + j >= ViewUtils.dip2px(getContext(), 150.0F))
          break;
        localLinearLayout.addView(createTagView(localTextView1, 1));
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
        break label731;
      localView.findViewById(R.id.booking_item_root_containter).setPadding(ViewUtils.dip2px(getContext(), 12.0F), 0, 0, 0);
      localView.findViewById(16908308).setPadding(ViewUtils.dip2px(getContext(), 32.0F), 0, 0, 0);
    }
    while (true)
    {
      setBackground(localView.findViewById(R.id.layout), R.drawable.cell_item_white);
      return localView;
      ((TextView)localView.findViewById(16908309)).setText(formatHotelPrice(paramDPObject, bool2));
      break;
      label731: localView.findViewById(R.id.booking_item_root_containter).setPadding(ViewUtils.dip2px(getContext(), 42.0F), 0, 0, 0);
      localView.findViewById(16908308).setPadding(ViewUtils.dip2px(getContext(), 2.0F), 0, 0, 0);
    }
  }

  private View createShopInfoAgent()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.shop_hotel_info_layout, null);
    Object localObject1 = (GridView)localView.findViewById(R.id.hotel_detail_facility_grid);
    Object localObject3 = (RelativeLayout)localView.findViewById(R.id.layout_click_grid);
    new LinearLayout.LayoutParams(-2, -2).setMargins((int)TypedValue.applyDimension(1, 8.0F, this.res.getResources().getDisplayMetrics()), 0, (int)TypedValue.applyDimension(1, 8.0F, this.res.getResources().getDisplayMetrics()), 0);
    Object localObject4 = this.hotelInfo.getArray("KeyFacilityList");
    if ((localObject4 != null) && (localObject4.length > 0))
    {
      ((GridView)localObject1).setAdapter(new KeyFacilityListAdapter(localObject4));
      ((RelativeLayout)localObject3).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HotelAgent.this.clickToHotelDetail();
        }
      });
      localObject3 = this.hotelInfo.getStringArray("PhoneList");
      if (this.hotelInfo.getString("OpenningTime") != null)
        break label334;
    }
    StringBuilder localStringBuilder;
    label334: for (localObject1 = ""; ; localObject1 = this.hotelInfo.getString("OpenningTime"))
    {
      localObject4 = (TextView)localView.findViewById(R.id.tv_shopinfo_opentime);
      localStringBuilder = new StringBuilder();
      if (!TextUtils.isEmpty((CharSequence)localObject1))
        localStringBuilder.append((String)localObject1).append("\n");
      if ((localObject3 == null) || (localObject3.length <= 0))
        break label348;
      localStringBuilder.append("电话：");
      int i = 0;
      int j = localObject3.length;
      while (i < j)
      {
        localStringBuilder.append(localObject3[i]);
        if (i != localObject3.length - 1)
          localStringBuilder.append("，");
        i += 1;
      }
      ((GridView)localObject1).setVisibility(8);
      ((ImageView)localView.findViewById(R.id.img_facility_divder)).setVisibility(8);
      ((RelativeLayout)localObject3).setVisibility(8);
      break;
    }
    label348: if ((localStringBuilder != null) && (localStringBuilder.length() > 0))
    {
      ((TextView)localObject4).setVisibility(0);
      ((TextView)localObject4).setText(localStringBuilder.toString());
    }
    localObject4 = getShop().getString("ExtraJson");
    localObject3 = "";
    localObject1 = localObject3;
    if (!TextUtils.isEmpty((CharSequence)localObject4));
    try
    {
      localObject1 = new JSONObject((String)localObject4).optString("path");
      if (!((String)localObject1).isEmpty())
      {
        localObject3 = (TextView)localView.findViewById(R.id.tv_shopinfo_closeto);
        ((TextView)localObject3).setVisibility(0);
        ((TextView)localObject3).setText("位置距离：" + (String)localObject1);
      }
      localShopinfoCommonCell.addContent(localView, false, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HotelAgent.this.clickToHotelDetail();
        }
      });
      localShopinfoCommonCell.setTitle("设施简介", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HotelAgent.this.clickToHotelDetail();
        }
      });
      setBranchAgent(localShopinfoCommonCell);
      return localShopinfoCommonCell;
    }
    catch (Exception localObject2)
    {
      while (true)
      {
        Log.e(localException.toString());
        Object localObject2 = localObject3;
      }
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

  private View createTrafficInfoAgent(DPObject paramDPObject)
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_item_hotel_traffic, null);
    Object localObject5 = paramDPObject.getString("ExtraJson");
    Object localObject4 = "";
    Object localObject2 = paramDPObject.getString("DistrictName");
    Object localObject1 = localObject2;
    if (localObject2 == null)
      localObject1 = "";
    localObject2 = localObject4;
    if (!TextUtils.isEmpty((CharSequence)localObject5));
    try
    {
      localObject2 = new JSONObject((String)localObject5).optString("path");
      paramDPObject = "位于" + (String)localObject1 + paramDPObject.getString("RegionName") + "商区";
      localObject4 = (TextView)localView.findViewById(R.id.tv_shop_location);
      localObject5 = (TextView)localView.findViewById(R.id.tv_shop_closeto);
      if ((localObject4 != null) && (!"".equals(localObject1)))
      {
        ((TextView)localObject4).setVisibility(0);
        ((TextView)localObject4).setText(paramDPObject);
      }
      if ((localObject5 != null) && (!"".equals(localObject2)))
      {
        ((TextView)localObject5).setVisibility(0);
        ((TextView)localObject5).setText((CharSequence)localObject2);
      }
      localShopinfoCommonCell.addContent(localView, false, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HotelAgent.this.clickToTrafficInfo();
        }
      });
      localShopinfoCommonCell.setTitle("位置交通", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HotelAgent.this.clickToTrafficInfo();
        }
      });
      return localShopinfoCommonCell;
    }
    catch (Exception localObject3)
    {
      while (true)
      {
        Log.e(localException.toString());
        Object localObject3 = localObject4;
      }
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

  private String getHotelBookingCellPosition()
  {
    if (this.sortType == 2)
      return "0460Hotel.52HotelBookingItem.";
    return "0300Hotel.52HotelBookingItem.";
  }

  private void parseHotelJson()
  {
    if ((this.hotelJson != null) && (this.hotelJson.length() > 0));
    while (true)
    {
      return;
      Object localObject = getShop();
      if (!((DPObject)localObject).contains("HotelJson"))
        continue;
      localObject = ((DPObject)localObject).getString("HotelJson");
      if (TextUtils.isEmpty((CharSequence)localObject))
        continue;
      try
      {
        this.hotelJson = new JSONObject((String)localObject);
        if ((this.hotelJson == null) || (this.hotelJson.length() == 0) || (!this.hotelJson.has("sortType")))
          continue;
        this.sortType = this.hotelJson.optInt("sortType");
        return;
      }
      catch (JSONException localJSONException)
      {
        while (true)
          Log.e(localJSONException.toString());
      }
    }
  }

  private void scrollToCenter()
  {
    ScrollView localScrollView = getFragment().getScrollView();
    localScrollView.setSmoothScrollingEnabled(true);
    localScrollView.requestChildFocus(this.linearLayout, this.linearLayout);
  }

  private void sendHotelDetailRequest()
  {
    this.hotelDetailRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/hotel/hotelinfo.hotel?shopid=" + shopId(), CacheType.NORMAL);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (HotelAgent.this.hotelDetailRequest != null)
          HotelAgent.this.getFragment().mapiService().exec(HotelAgent.this.hotelDetailRequest, HotelAgent.this);
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

  private void setBranchAgent(ShopinfoCommonCell paramShopinfoCommonCell)
  {
    Object localObject = getShop();
    TextView localTextView = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
    if (isHotelType())
      if ((((DPObject)localObject).getInt("BranchCounts") > 0) || ((((DPObject)localObject).getString("BranchIDs") != null) && (((DPObject)localObject).getString("BranchIDs").length() > 0)))
      {
        if (((DPObject)localObject).getInt("BranchCounts") <= 0)
          break label165;
        i = ((DPObject)localObject).getInt("BranchCounts");
        if (i <= 0)
          break label235;
        localObject = ((DPObject)localObject).getString("Name");
        if ((localObject == null) || (((String)localObject).equals("")))
          break label201;
        localTextView.setText((String)localObject + "-" + i + "家分店");
        label149: paramShopinfoCommonCell.addContent(localTextView, true, new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = HotelAgent.this.getShop();
            if (paramView == null)
              return;
            Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopidlist?shopid=" + HotelAgent.this.shopId()));
            localIntent.putExtra("showAddBranchShop", true);
            localIntent.putExtra("shop", paramView);
            HotelAgent.this.getFragment().startActivity(localIntent);
            HotelAgent.this.statisticsEvent("shopinfo5", "shopinfo5_hotelgroup", "" + HotelAgent.this.shopId(), 0);
          }
        });
      }
    label165: label201: label235: 
    do
    {
      return;
      String str = ((DPObject)localObject).getString("BranchIDs");
      i = str.length() - str.replace(",", "").length() + 1;
      break;
      localTextView.setText("其他" + i + "家分店");
      break label149;
      localTextView.setText("其他分店");
      break label149;
    }
    while ((((DPObject)localObject).getString("BranchIDs") == null) || (((DPObject)localObject).getString("BranchIDs").length() <= 0));
    localObject = ((DPObject)localObject).getString("BranchIDs");
    int i = ((String)localObject).length() - ((String)localObject).replace(",", "").length() + 1;
    if (i > 0)
      localTextView.setText("其他" + i + "家分店");
    while (true)
    {
      paramShopinfoCommonCell.addContent(localTextView, true, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = HotelAgent.this.getShop();
          if (paramView == null)
            return;
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopidlist?ids=" + paramView.getString("BranchIDs")));
          localIntent.putExtra("showAddBranchShop", true);
          localIntent.putExtra("shop", paramView);
          HotelAgent.this.getFragment().startActivity(localIntent);
          HotelAgent.this.statisticsEvent("shopinfo5", "shopinfo5_branch", "", 0);
        }
      });
      return;
      localTextView.setText("其他分店");
    }
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

  void clickToHotelDetail()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hoteldetails?cityId=" + getShop().getInt("CityID")));
    localIntent.putExtra("hoteldetail", this.hotelInfo);
    localIntent.putExtra("HotelDetailStatus", 0);
    getFragment().startActivity(localIntent);
    statisticsEvent("shopinfo5", "shopinfo5_hotelinfo", "", shopId());
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
    int j;
    int i;
    int k;
    Object localObject;
    boolean bool;
    while (true)
    {
      return;
      super.onAgentChanged(paramBundle);
      removeAllCells();
      paramBundle = getShop();
      if ((paramBundle == null) || (!isHotelType()))
        continue;
      if (!this.isHotelBookable)
        this.isHotelBookable = getShop().getBoolean("HotelBooking");
      if (!getFragment().isUrlAvailable("dianping://hotelbookingpicktime"))
        continue;
      parseHotelJson();
      if (this.hotelInfo != null)
        addCell("7500Hoteshopinfo.", createShopInfoAgent(), 0);
      if (!this.isHotelBookable)
        continue;
      if (this.otaHotelPriceList == null)
        this.otaHotelPriceList = paramBundle.getArray("OtaHotelPriceList");
      if (this.hotelList == null)
        break;
      paramBundle = this.hotelList.getArray("List");
      j = this.hotelList.getInt("ShowNum");
      i = j;
      if (j == 0)
        i = 3;
      if ((paramBundle == null) || (paramBundle.length <= 0))
        continue;
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
      k = paramBundle.length;
      DPObject localDPObject;
      if (k <= i)
      {
        i = 0;
        if (i < k)
        {
          localObject = this.linearLayout;
          localDPObject = paramBundle[i];
          if (i == 0);
          for (bool = true; ; bool = false)
          {
            ((LinearLayout)localObject).addView(createHotelBookingCell(localDPObject, bool, false));
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
          localObject = this.linearLayout;
          localDPObject = paramBundle[j];
          if (j == 0);
          for (bool = true; ; bool = false)
          {
            ((LinearLayout)localObject).addView(createHotelBookingCell(localDPObject, bool, false));
            j += 1;
            break;
          }
        }
        this.expandLayout = new LinearLayout(getContext());
        this.expandLayout.setOrientation(1);
        this.expandLayout.removeAllViews();
        localObject = new LinearLayout.LayoutParams(-1, -2);
        if (!this.isExpand)
          this.expandLayout.setVisibility(8);
        this.expandLayout.setLayoutParams((ViewGroup.LayoutParams)localObject);
        if (i < k)
        {
          localObject = this.expandLayout;
          localDPObject = paramBundle[i];
          if (i == 0);
          for (bool = true; ; bool = false)
          {
            ((LinearLayout)localObject).addView(createHotelBookingCell(localDPObject, bool, false));
            i += 1;
            break;
          }
        }
        this.linearLayout.addView(this.expandLayout);
        this.expandView = LayoutInflater.from(getContext()).inflate(R.layout.hotel_booking_expand, getParentView(), false);
        this.expandView.setTag("EXPAND");
        ((TextView)this.expandView.findViewById(16908308)).setText("查看全部" + paramBundle.length + "条");
        this.expandView.setClickable(true);
        this.expandView.setOnClickListener(this);
        this.linearLayout.addView(this.expandView);
        setExpandState();
      }
      paramBundle = this.hotelList.getArray("OTAPromoInfoList");
      if ((paramBundle != null) && (paramBundle.length > 0))
        addPromoInfoExplain(paramBundle[0]);
      addCell(getHotelBookingCellPosition(), this.linearLayout, 0);
      paramBundle = this.hotelList.getString("TaxesTips");
      if (!TextUtils.isEmpty(paramBundle))
      {
        localObject = new TextView(getContext());
        ((TextView)localObject).setText(paramBundle);
        ((TextView)localObject).setTextSize(0, this.res.getDimension(R.dimen.text_medium));
        ((TextView)localObject).setTextColor(this.res.getColor(R.color.light_gray));
        ((TextView)localObject).setLayoutParams(new ViewGroup.LayoutParams(-1, this.res.getDimensionPixelSize(R.dimen.section_height_with_text)));
        ((TextView)localObject).setGravity(5);
        ((TextView)localObject).setBackgroundColor(this.res.getColor(R.color.common_bk_color));
        ((TextView)localObject).setPadding(0, ViewUtils.dip2px(getContext(), 5.0F), ViewUtils.dip2px(getContext(), 10.0F), 0);
        addCell(getHotelBookingCellPosition() + "Tips", (View)localObject, 1024);
      }
      updateSearchDateheaderView(this.checkinTimeMills, this.checkoutTimeMills);
      return;
    }
    if ((this.otaHotelPriceList != null) && (this.otaHotelPriceList.length > 0))
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
          paramBundle = this.linearLayout;
          localObject = this.otaHotelPriceList[i];
          if (i == 0);
          for (bool = true; ; bool = false)
          {
            paramBundle.addView(createHotelBookingCell((DPObject)localObject, bool, true));
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
          paramBundle = this.linearLayout;
          localObject = this.otaHotelPriceList[j];
          if (j == 0);
          for (bool = true; ; bool = false)
          {
            paramBundle.addView(createHotelBookingCell((DPObject)localObject, bool, true));
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
        paramBundle = new LinearLayout.LayoutParams(-1, -2);
        if (!this.isExpand)
          this.expandLayout.setVisibility(8);
        this.expandLayout.setLayoutParams(paramBundle);
        if (i < k)
        {
          paramBundle = this.expandLayout;
          localObject = this.otaHotelPriceList[i];
          if (i == 0);
          for (bool = true; ; bool = false)
          {
            paramBundle.addView(createHotelBookingCell((DPObject)localObject, bool, true));
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
      addCell(getHotelBookingCellPosition(), this.linearLayout, 0);
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
    addCell(getHotelBookingCellPosition(), this.linearLayout, 0);
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
          break label391;
        localObject2 = Uri.parse("dianping://hotelbookingweb");
        localObject3 = Uri.parse((String)localObject3).buildUpon().appendQueryParameter("shopId", shopId() + "").appendQueryParameter("startDate", SDF2.format(Long.valueOf(this.checkinTimeMills))).appendQueryParameter("endDate", SDF2.format(Long.valueOf(this.checkoutTimeMills))).build();
        localObject2 = new Intent("android.intent.action.VIEW", ((Uri)localObject2).buildUpon().appendQueryParameter("url", ((Uri)localObject3).toString()).build());
        ((Intent)localObject2).putExtra("name", (String)localObject1);
        getFragment().startActivity((Intent)localObject2);
      }
    }
    label303: 
    do
    {
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
            break label665;
          localObject2 = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject3));
          ((Intent)localObject2).putExtra("query_id", getFragment().getStringParam("query_id"));
          ((Intent)localObject2).putExtra("trace_id", this.traceid);
          getFragment().startActivityForResult((Intent)localObject2, 4353);
          break;
          if (((String)localObject1).contains("艺龙"))
          {
            statisticsEvent("shopinfo5", "shopinfo5_hotelsupplier", "elong", i);
            break label303;
          }
          statisticsEvent("shopinfo5", "shopinfo5_hotelsupplier", (String)localObject1, i);
          break label303;
          if (!"0300Hotel.51HotelBookingSelectTime".equals(paramView.getTag()))
            break label666;
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
      if ("7500Hoteshopinfo.".equals(paramView.getTag()))
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hoteldetails?cityId=" + getShop().getInt("CityID")));
        paramView.putExtra("hoteldetail", this.hotelInfo);
        paramView.putExtra("HotelDetailStatus", 0);
        getFragment().startActivity(paramView);
        statisticsEvent("shopinfo5", "shopinfo5_hotelinfo", "", shopId());
        return;
      }
      if (!"7600HotelTraficInfo.".equals(paramView.getTag()))
        continue;
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hoteltraffic?cityId=" + getShop().getInt("CityID")));
      paramView.putExtra("hoteldetail", this.hotelInfo);
      paramView.putExtra("shopInfo", getShop());
      paramView.putExtra("HotelDetailStatus", 1);
      getFragment().startActivity(paramView);
      statisticsEvent("shopinfo5", "shopinfo5_hotellocation", "", shopId());
      return;
    }
    while ((paramView.getTag() != "EXPAND") && (paramView.getTag() != "COLLAPSE"));
    label391: label665: label666: boolean bool;
    if (!this.isExpand)
    {
      bool = true;
      this.isExpand = bool;
      if (!this.isExpand)
        break label952;
      statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "展开", 0);
    }
    while (true)
    {
      setExpandAction();
      scrollToCenter();
      return;
      bool = false;
      break;
      label952: statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "收起", 0);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    if (!isHotelType())
      return;
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.hotelList = ((DPObject)paramBundle.getParcelable("hotelDetail"));
      this.checkinTimeMills = paramBundle.getLong("checkinTime");
      this.checkoutTimeMills = paramBundle.getLong("checkoutTime");
      this.isHotelBookable = paramBundle.getBoolean("isHotelBookable");
      this.isExpand = paramBundle.getBoolean("isExpand");
    }
    label259: 
    while (true)
    {
      this.traceid = System.currentTimeMillis();
      if (this.hotelList == null)
        sendRequest();
      if ((this.hotelDetailRequest != null) || (!getFragment().isUrlAvailable("dianping://hoteldetails")))
        break;
      sendHotelDetailRequest();
      return;
      paramBundle = getFragment().getActivity().getIntent();
      String str;
      if (paramBundle != null)
      {
        str = getFragment().getStringParam("begindate");
        if ((TextUtils.isEmpty(str)) || (!TextUtils.isDigitsOnly(str)))
          break label223;
        this.checkinTimeMills = Long.parseLong(str);
        label166: str = getFragment().getStringParam("enddate");
        if ((TextUtils.isEmpty(str)) || (!TextUtils.isDigitsOnly(str)))
          break label240;
      }
      label223: label240: for (this.checkoutTimeMills = Long.parseLong(str); ; this.checkoutTimeMills = paramBundle.getLongExtra("checkoutTime", System.currentTimeMillis() + 86400000L))
      {
        if (getShop() == null)
          break label259;
        this.isHotelBookable = getShop().getBoolean("HotelBooking");
        break;
        this.checkinTimeMills = paramBundle.getLongExtra("checkinTime", System.currentTimeMillis());
        break label166;
      }
    }
  }

  public void onDestroy()
  {
    if (this.request != null)
    {
      getFragment().mapiService().abort(this.request, this, true);
      this.request = null;
    }
    if (this.hotelDetailRequest != null)
    {
      getFragment().mapiService().abort(this.hotelDetailRequest, this, true);
      this.hotelDetailRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
      this.request = null;
    do
      return;
    while (paramMApiRequest != this.hotelDetailRequest);
    this.hotelDetailRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request == paramMApiRequest)
    {
      this.request = null;
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
    do
      return;
    while (this.hotelDetailRequest != paramMApiRequest);
    this.hotelDetailRequest = null;
    this.hotelInfo = ((DPObject)paramMApiResponse.result());
    dispatchAgentChanged(false);
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
    this.request = BasicMApiRequest.mapiGet(str, CacheType.DISABLED);
    if (this.request != null)
      getFragment().mapiService().exec(this.request, this);
  }

  class KeyFacilityListAdapter extends BaseAdapter
  {
    DPObject[] keyFacilityList;

    public KeyFacilityListAdapter(DPObject[] arg2)
    {
      Object localObject;
      this.keyFacilityList = localObject;
    }

    public int getCount()
    {
      return this.keyFacilityList.length;
    }

    public Object getItem(int paramInt)
    {
      return this.keyFacilityList[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramView = LayoutInflater.from(HotelAgent.this.getContext()).inflate(R.layout.shopinfo_hotel_facility_item, null);
        paramViewGroup = new HotelAgent.ViewHolder(HotelAgent.this);
        paramViewGroup.hotel_info_facility_img = ((NetworkImageView)paramView.findViewById(R.id.hotel_info_facility_img));
        paramViewGroup.hotel_info_facility_text = ((TextView)paramView.findViewById(R.id.hotel_info_facility_text));
        paramView.setTag(paramViewGroup);
      }
      while (true)
      {
        paramViewGroup.hotel_info_facility_img.setImage(this.keyFacilityList[paramInt].getString("Icon"));
        paramViewGroup.hotel_info_facility_text.setText(this.keyFacilityList[paramInt].getString("Title"));
        return paramView;
        paramViewGroup = (HotelAgent.ViewHolder)paramView.getTag();
      }
    }
  }

  class ViewHolder
  {
    NetworkImageView hotel_info_facility_img;
    TextView hotel_info_facility_text;

    ViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelAgent
 * JD-Core Version:    0.6.0
 */