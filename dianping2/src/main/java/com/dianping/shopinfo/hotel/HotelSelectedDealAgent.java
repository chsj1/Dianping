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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
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
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.text.DecimalFormat;

public class HotelSelectedDealAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String CELL_SELECTEDTUAN = "0250selectedtuan.";
  private static final int HOTEL_TUAN_ITEM_LIMIT = 2;
  private static final DecimalFormat PRICE_DF = new DecimalFormat("#.###");
  DPObject hotelSelectedDealList;
  MApiRequest hotelSelectedDealRequest;
  LinearLayout linearLayout;

  public HotelSelectedDealAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createHotelSelectedDealCell(DPObject paramDPObject, boolean paramBoolean)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.item_shopinfo_hotel_selected_tuan_item, getParentView(), false);
    localNovaLinearLayout.setClickable(true);
    localNovaLinearLayout.setGAString("hotel_select");
    Object localObject1;
    if (!TextUtils.isEmpty(paramDPObject.getString("Title")))
    {
      localObject1 = (TextView)localNovaLinearLayout.findViewById(16908308);
      ((TextView)localObject1).setText(paramDPObject.getString("Title"));
      ((TextView)localObject1).setGravity(3);
    }
    ((TextView)localNovaLinearLayout.findViewById(16908309)).setText(formatHotelPrice(paramDPObject, false));
    if (!TextUtils.isEmpty(paramDPObject.getString("Recommendation")))
    {
      localNovaLinearLayout.findViewById(R.id.select_reason).setVisibility(0);
      ((TextView)localNovaLinearLayout.findViewById(R.id.select_reason)).setText(paramDPObject.getString("Recommendation"));
    }
    DPObject[] arrayOfDPObject = paramDPObject.getArray("PromoList");
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
    {
      LinearLayout localLinearLayout = (LinearLayout)localNovaLinearLayout.findViewById(R.id.promo_list);
      localObject1 = null;
      localNovaLinearLayout.findViewById(R.id.item).measure(0, 0);
      localNovaLinearLayout.findViewById(R.id.left).measure(0, 0);
      Object localObject2 = new DisplayMetrics();
      getFragment().getActivity().getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)localObject2);
      int i3 = ((DisplayMetrics)localObject2).widthPixels - localNovaLinearLayout.findViewById(R.id.left).getMeasuredWidth() - ViewUtils.dip2px(getContext(), 63.0F) - 30;
      int n = 0;
      int i = i3;
      int j = 0;
      int i4 = arrayOfDPObject.length;
      int k = 0;
      if (k < i4)
      {
        Object localObject3 = arrayOfDPObject[k];
        int i2 = n;
        localObject2 = localObject1;
        int i1 = j;
        int m = i;
        if (localObject3 != null)
        {
          i2 = n;
          localObject2 = localObject1;
          i1 = j;
          m = i;
          if (!TextUtils.isEmpty(((DPObject)localObject3).getString("Title")))
          {
            m = ((DPObject)localObject3).getInt("Type");
            localObject2 = createTagView(((DPObject)localObject3).getString("Title"), m);
            i2 = n + 1;
            ((TextView)localObject2).setId(i2);
            localObject3 = new RelativeLayout.LayoutParams(-2, -2);
            ((TextView)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject3);
            ((TextView)localObject2).measure(0, 0);
            i += ((TextView)localObject2).getMeasuredWidth() + ViewUtils.dip2px(getContext(), 3.0F);
            if ((i < i3) && (j < 3))
              break label568;
            m = 1;
            i = ((TextView)localObject2).getMeasuredWidth();
            label446: if (m == 0)
              break label574;
            localObject1 = new RelativeLayout(getContext());
            LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
            localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 3.0F);
            ((RelativeLayout)localObject1).setLayoutParams(localLayoutParams);
            localLinearLayout.addView((View)localObject1);
            m = 1;
            j = 1;
            label508: if (m == 0)
              break label586;
            ((RelativeLayout.LayoutParams)localObject3).addRule(11);
          }
        }
        while (true)
        {
          ((TextView)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject3);
          ((RelativeLayout)localObject1).addView((View)localObject2);
          m = i;
          i1 = j;
          localObject2 = localObject1;
          k += 1;
          n = i2;
          localObject1 = localObject2;
          j = i1;
          i = m;
          break;
          label568: m = 0;
          break label446;
          label574: m = 0;
          j += 1;
          break label508;
          label586: ((RelativeLayout.LayoutParams)localObject3).addRule(0, i2 - 1);
          ((RelativeLayout.LayoutParams)localObject3).rightMargin = ViewUtils.dip2px(getContext(), 3.0F);
        }
      }
    }
    localNovaLinearLayout.setTag(paramDPObject);
    if (paramBoolean)
    {
      localNovaLinearLayout.findViewById(R.id.hotel_tuan_root_containter).setPadding(ViewUtils.dip2px(getContext(), 0.0F), 0, 0, 0);
      localNovaLinearLayout.findViewById(16908308).setPadding(ViewUtils.dip2px(getContext(), 44.0F), 0, 0, 0);
      localNovaLinearLayout.findViewById(R.id.select_reason).setPadding(ViewUtils.dip2px(getContext(), 44.0F), 0, 0, 0);
    }
    while (true)
    {
      setBackground(localNovaLinearLayout.findViewById(R.id.layout), R.drawable.cell_item_white);
      localNovaLinearLayout.setGAString("hotel_selected");
      localNovaLinearLayout.gaUserInfo.query_id = getFragment().getStringParam("query_id");
      localNovaLinearLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$deal.getString("ProductURL");
          if (!TextUtils.isEmpty(paramView))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView));
            HotelSelectedDealAgent.this.getContext().startActivity(paramView);
          }
        }
      });
      return localNovaLinearLayout;
      localNovaLinearLayout.findViewById(R.id.hotel_tuan_root_containter).setPadding(ViewUtils.dip2px(getContext(), 42.0F), 0, 0, 0);
      localNovaLinearLayout.findViewById(16908308).setPadding(ViewUtils.dip2px(getContext(), 2.0F), 0, 0, 0);
      localNovaLinearLayout.findViewById(R.id.select_reason).setPadding(ViewUtils.dip2px(getContext(), 2.0F), 0, 0, 0);
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
    for (int i = this.res.getColor(R.color.middle_gray); ; i = this.res.getColor(R.color.light_red))
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

  private void renderHotelSelectedDealCell(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      return;
    if (this.linearLayout == null)
    {
      this.linearLayout = new LinearLayout(getContext());
      this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      this.linearLayout.setOrientation(1);
    }
    this.linearLayout.removeAllViews();
    Object localObject = this.res.inflate(getContext(), R.layout.hotel_selected_deal_header, null, false);
    if ((this.hotelSelectedDealList != null) && (!TextUtils.isEmpty(this.hotelSelectedDealList.getString("Title"))))
      ((TextView)((View)localObject).findViewById(16908309)).setText(this.hotelSelectedDealList.getString("Title"));
    this.linearLayout.addView((View)localObject);
    int i = 0;
    if (this.hotelSelectedDealList != null)
      i = this.hotelSelectedDealList.getInt("ShowNum");
    int j = i;
    if (i == 0)
      j = 2;
    i = 0;
    if ((i < j) && (i < paramArrayOfDPObject.length))
    {
      localObject = this.linearLayout;
      DPObject localDPObject = paramArrayOfDPObject[i];
      if (i == 0);
      for (boolean bool = true; ; bool = false)
      {
        ((LinearLayout)localObject).addView(createHotelSelectedDealCell(localDPObject, bool));
        i += 1;
        break;
      }
    }
    addCell("0250selectedtuan.", this.linearLayout, 0);
  }

  private void sendHotelDetailRequest(long paramLong1, long paramLong2)
  {
    String str2 = "http://m.api.dianping.com/hoteltg/highqualitydealgns.hoteltg?shopid=" + shopId();
    String str1 = str2;
    if (getCity() != null)
      str1 = str2 + "&cityid=" + getCity().id();
    this.hotelSelectedDealRequest = BasicMApiRequest.mapiGet(str1 + "&begindate=" + paramLong1 + "&enddate=" + paramLong2, CacheType.NORMAL);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (HotelSelectedDealAgent.this.hotelSelectedDealRequest != null)
          HotelSelectedDealAgent.this.getFragment().mapiService().exec(HotelSelectedDealAgent.this.hotelSelectedDealRequest, HotelSelectedDealAgent.this);
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

  public void onCreate(Bundle paramBundle)
  {
    if (!isHotelType());
    do
    {
      return;
      super.onCreate(paramBundle);
      if (paramBundle != null)
        this.hotelSelectedDealList = ((DPObject)paramBundle.getParcelable("hotelSelectedDealList"));
      if (this.hotelSelectedDealList == null)
        continue;
      renderHotelSelectedDealCell(this.hotelSelectedDealList.getArray("DealList"));
    }
    while (this.hotelSelectedDealRequest != null);
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
    if (this.hotelSelectedDealRequest != null)
    {
      mapiService().abort(this.hotelSelectedDealRequest, this, true);
      this.hotelSelectedDealRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.hotelSelectedDealRequest)
      this.hotelSelectedDealRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.hotelSelectedDealRequest == paramMApiRequest)
    {
      this.hotelSelectedDealRequest = null;
      this.hotelSelectedDealList = ((DPObject)paramMApiResponse.result());
      if (this.hotelSelectedDealList != null)
      {
        removeAllCells();
        renderHotelSelectedDealCell(this.hotelSelectedDealList.getArray("DealList"));
      }
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("hotelSelectedDealList", this.hotelSelectedDealList);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelSelectedDealAgent
 * JD-Core Version:    0.6.0
 */