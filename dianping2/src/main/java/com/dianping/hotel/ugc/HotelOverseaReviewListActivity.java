package com.dianping.hotel.ugc;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.ShopPower;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HotelOverseaReviewListActivity extends NovaActivity
{
  private static final int REQUEST_HOTEL_BOOKING = 111;
  private static final SimpleDateFormat SDF = new SimpleDateFormat("MM-dd");
  private static final SimpleDateFormat SDF2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
  long checkingTime;
  long checkoutTime;
  private View mBookingLayout;
  private DPObject mBookingPrice;
  private DPObject mReviewList;
  private int mShopId;
  private DPObject mShopObj;

  private SpannableString formatHotelPrice(DPObject paramDPObject, boolean paramBoolean)
  {
    DecimalFormat localDecimalFormat = new DecimalFormat("¥ #.###");
    if (paramBoolean);
    for (int i = getResources().getColor(R.color.middle_gray); (paramDPObject != null) && (Double.compare(paramDPObject.getDouble("Price"), 0.0D) >= 0); i = -39373)
    {
      paramDPObject = new SpannableString(localDecimalFormat.format(paramDPObject.getDouble("Price")) + "起");
      paramDPObject.setSpan(new ForegroundColorSpan(i), 0, paramDPObject.length() - 1, 18);
      paramDPObject.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), paramDPObject.length() - 1, paramDPObject.length(), 18);
      paramDPObject.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_large)), 0, paramDPObject.length() - 1, 18);
      paramDPObject.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_small)), paramDPObject.length() - 1, paramDPObject.length(), 18);
      paramDPObject.setSpan(new StyleSpan(1), 0, paramDPObject.length() - 1, 18);
      return paramDPObject;
    }
    if (paramDPObject != null);
    for (paramDPObject = paramDPObject.getString("PriceText"); ; paramDPObject = "惊爆价")
    {
      paramDPObject = new SpannableString(paramDPObject);
      paramDPObject.setSpan(new ForegroundColorSpan(i), 0, paramDPObject.length(), 18);
      paramDPObject.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_medium_1)), 0, paramDPObject.length(), 18);
      return paramDPObject;
    }
  }

  private View initHotelBookingView()
  {
    if (this.mBookingPrice == null)
      return null;
    View localView = LayoutInflater.from(this).inflate(R.layout.hotel_temp_item_hotel_oversea_booking_price, null, false);
    localView.setClickable(true);
    localView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = HotelOverseaReviewListActivity.this.mBookingPrice.getString("Url");
        if (paramView != null)
        {
          if (paramView.startsWith("http"))
          {
            Object localObject = Uri.parse("dianping://hotelbookingweb").buildUpon();
            paramView = HotelOverseaReviewListActivity.this.mBookingPrice.getString("Name");
            ((Uri.Builder)localObject).appendQueryParameter("shopId", String.valueOf(HotelOverseaReviewListActivity.this.mShopId));
            ((Uri.Builder)localObject).appendQueryParameter("startDate", HotelOverseaReviewListActivity.SDF2.format(Long.valueOf(HotelOverseaReviewListActivity.this.checkingTime)));
            ((Uri.Builder)localObject).appendQueryParameter("endDate", String.valueOf(HotelOverseaReviewListActivity.SDF2.format(Long.valueOf(HotelOverseaReviewListActivity.this.checkoutTime))));
            localObject = new Intent("android.intent.action.VIEW", ((Uri.Builder)localObject).build());
            ((Intent)localObject).putExtra("name", paramView);
            HotelOverseaReviewListActivity.this.startActivity((Intent)localObject);
          }
        }
        else
          return;
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView).buildUpon().build());
        HotelOverseaReviewListActivity.this.startActivity(paramView);
      }
    });
    if (!TextUtils.isEmpty(this.mBookingPrice.getString("Name")))
      ((TextView)localView.findViewById(16908308)).setText(this.mBookingPrice.getString("Name"));
    ((TextView)localView.findViewById(16908309)).setText(formatHotelPrice(this.mBookingPrice, false));
    localView.setTag(this.mBookingPrice);
    localView.findViewById(R.id.booking_item_root_containter).setPadding(ViewUtils.dip2px(this, 5.0F), 0, 0, 0);
    localView.findViewById(16908308).setPadding(ViewUtils.dip2px(this, 2.0F), 0, 0, 0);
    return localView;
  }

  private View initReviewListView()
  {
    LinearLayout localLinearLayout = new LinearLayout(this);
    localLinearLayout.setId(R.id.hotel_oversea_reviewlist_container_dp);
    localLinearLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
    ReviewListFragment localReviewListFragment = new ReviewListFragment();
    localReviewListFragment.setShopId(this.mShopId);
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.add(R.id.hotel_oversea_reviewlist_container_dp, localReviewListFragment, "ReviewListFragment");
    localFragmentTransaction.commitAllowingStateLoss();
    return localLinearLayout;
  }

  private void initViews()
  {
    setContentView(R.layout.hotel_temp_hotel_oversea_review_list_layout);
    Object localObject1 = (ShopPower)findViewById(R.id.shop_power);
    if (this.mShopObj != null)
      ((ShopPower)localObject1).setPower(this.mShopObj.getInt("ShopPower"));
    localObject1 = (TextView)findViewById(R.id.total_score);
    double d = this.mReviewList.getDouble("AvgScore");
    Object localObject3 = d + "分";
    Object localObject2 = new String((String)localObject3);
    if (!TextUtils.isEmpty((CharSequence)localObject2))
    {
      i = ((String)localObject3).indexOf((String)localObject2);
      localObject3 = new SpannableString((CharSequence)localObject3);
      ((SpannableString)localObject3).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), i, ((String)localObject2).length() + i, 17);
      ((TextView)localObject1).setText((CharSequence)localObject3);
    }
    View localView;
    while (true)
    {
      ((TextView)localObject1).setVisibility(0);
      localObject1 = (TextView)findViewById(R.id.ota_score);
      localObject2 = this.mReviewList.getArray("OTAScoreList");
      localObject3 = new StringBuilder();
      if ((localObject2 == null) || (localObject2.length <= 0))
        break;
      j = localObject2.length;
      i = 0;
      while (i < j)
      {
        localView = localObject2[i];
        ((StringBuilder)localObject3).append(localView.getString("Name")).append(" ").append(localView.getDouble("Score")).append("分").append(" ");
        i += 1;
      }
      ((TextView)localObject1).setText((CharSequence)localObject3);
    }
    if (((StringBuilder)localObject3).length() > 0)
    {
      ((TextView)localObject1).setVisibility(0);
      ((TextView)localObject1).setText(((StringBuilder)localObject3).toString());
    }
    while (true)
    {
      localObject1 = (TabHost)findViewById(16908306);
      ((TabHost)localObject1).setup();
      localObject2 = new ArrayList();
      localObject3 = this.mReviewList.getArray("ScoreList");
      if ((localObject3 == null) || (localObject3.length <= 0))
        break;
      j = 0;
      int m = localObject3.length;
      i = 0;
      while (i < m)
      {
        String str = localObject3[i];
        localView = LayoutInflater.from(this).inflate(R.layout.hotel_temp_hotel_oversea_review_tab_item, null, false);
        TextView localTextView1 = (TextView)localView.findViewById(R.id.tab_name);
        TextView localTextView2 = (TextView)localView.findViewById(R.id.tab_score);
        d = str.getDouble("Score");
        str = str.getString("ScoreName");
        int k = j;
        if (!TextUtils.isEmpty(str))
        {
          ((ArrayList)localObject2).add(localView);
          localTextView1.setText(str);
          localTextView2.setText(Double.valueOf(d) + "分");
          ((TabHost)localObject1).addTab(((TabHost)localObject1).newTabSpec(String.valueOf(j)).setIndicator(localView).setContent(new TabHost.TabContentFactory(localObject3)
          {
            public View createTabContent(String paramString)
            {
              int i = Integer.parseInt(paramString);
              Object localObject1;
              if (i >= this.val$scoreList.length)
                localObject1 = null;
              LinearLayout localLinearLayout;
              do
              {
                do
                {
                  return localObject1;
                  localObject1 = this.val$scoreList[i].getArray("OTAShortReviewList");
                  localLinearLayout = new LinearLayout(HotelOverseaReviewListActivity.this);
                  paramString = new LinearLayout.LayoutParams(-1, -2);
                  localLinearLayout.setOrientation(1);
                  localLinearLayout.setLayoutParams(paramString);
                  i = 0;
                  if (localObject1 != null)
                  {
                    paramString = (String)localObject1;
                    if (localObject1.length >= 1);
                  }
                  else
                  {
                    paramString = new DPObject[1];
                    paramString[0] = new DPObject().edit().putString("default", "暂时没有该项评分的相关评论").generate();
                    i = 1;
                  }
                  localObject1 = localLinearLayout;
                }
                while (paramString == null);
                localObject1 = localLinearLayout;
              }
              while (paramString.length <= 0);
              int k = 0;
              int m = paramString.length;
              int j = 0;
              Object localObject2;
              while (true)
              {
                localObject1 = localLinearLayout;
                if (j >= m)
                  break;
                str = paramString[j];
                localObject1 = new LinearLayout(HotelOverseaReviewListActivity.this);
                localObject2 = new LinearLayout.LayoutParams(-1, -2);
                ((LinearLayout.LayoutParams)localObject2).leftMargin = ViewUtils.dip2px(HotelOverseaReviewListActivity.this, 18.0F);
                ((LinearLayout.LayoutParams)localObject2).topMargin = ViewUtils.dip2px(HotelOverseaReviewListActivity.this, 18.0F);
                ((LinearLayout)localObject1).setOrientation(1);
                ((LinearLayout)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
                localObject2 = new TextView(HotelOverseaReviewListActivity.this);
                ((TextView)localObject2).setTextSize(0, HotelOverseaReviewListActivity.this.getResources().getDimensionPixelSize(R.dimen.text_size_17));
                ((TextView)localObject2).setTextColor(HotelOverseaReviewListActivity.this.getResources().getColor(R.color.deep_black));
                localObject3 = new LinearLayout.LayoutParams(-1, -2);
                ((LinearLayout.LayoutParams)localObject3).rightMargin = ViewUtils.dip2px(HotelOverseaReviewListActivity.this, 18.0F);
                ((LinearLayout.LayoutParams)localObject3).bottomMargin = ViewUtils.dip2px(HotelOverseaReviewListActivity.this, 18.0F);
                ((TextView)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject3);
                if (i == 0)
                  break label412;
                ((TextView)localObject2).setText(str.getString("default"));
                if (k < paramString.length - 1)
                {
                  localObject2 = new View(HotelOverseaReviewListActivity.this);
                  ((View)localObject2).setBackgroundColor(HotelOverseaReviewListActivity.this.getResources().getColor(R.color.background_gray));
                  ((View)localObject2).setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
                  ((LinearLayout)localObject1).addView((View)localObject2);
                }
                localLinearLayout.addView((View)localObject1);
                k += 1;
                j += 1;
              }
              label412: Object localObject3 = str.getString("ReviewBody");
              String str = "(" + str.getString("AddDate") + ")";
              Object localObject4 = (String)localObject3 + str;
              int n = ((String)localObject4).indexOf(str);
              localObject4 = new SpannableString((CharSequence)localObject4);
              ((SpannableString)localObject4).setSpan(new ForegroundColorSpan(HotelOverseaReviewListActivity.this.getResources().getColor(R.color.light_gray)), n, str.length() + n, 17);
              if (!TextUtils.isEmpty(str))
                ((TextView)localObject2).setText((CharSequence)localObject4);
              while (true)
              {
                ((LinearLayout)localObject1).addView((View)localObject2);
                break;
                ((TextView)localObject2).setText((CharSequence)localObject3);
              }
            }
          }));
          k = j + 1;
        }
        i += 1;
        j = k;
      }
      ((TextView)localObject1).setVisibility(8);
    }
    ((TabHost)localObject1).setOnTabChangedListener(new TabHost.OnTabChangeListener((TabHost)localObject1, (ArrayList)localObject2)
    {
      public void onTabChanged(String paramString)
      {
        this.val$tabHost.setCurrentTabByTag(paramString);
        int i = 0;
        while (i < this.val$tabs.size())
        {
          if (this.val$tabs.get(i) == null)
          {
            i += 1;
            continue;
          }
          View localView = ((View)this.val$tabs.get(i)).findViewById(R.id.bottom_arrow);
          if (i == Integer.valueOf(paramString).intValue());
          for (int j = 0; ; j = 8)
          {
            localView.setVisibility(j);
            break;
          }
        }
      }
    });
    ((TabHost)localObject1).setCurrentTab(0);
    if ((localObject2 != null) && (((ArrayList)localObject2).size() > 0))
      ((View)((ArrayList)localObject2).get(0)).findViewById(R.id.bottom_arrow).setVisibility(0);
    int i = this.mReviewList.getInt("GlobalReviewCount");
    int j = this.mReviewList.getInt("BookingReviewCount");
    localObject1 = (TabHost)findViewById(R.id.hotel_oversea_reviewlist_content);
    ((TabHost)localObject1).setup();
    if (i != 0)
    {
      localObject2 = (TextView)LayoutInflater.from(this).inflate(R.layout.tuan_tab_indicator, null, false);
      ((TextView)localObject2).setText("点评网友(" + (i - j) + ")");
      ((TabHost)localObject1).addTab(((TabHost)localObject1).newTabSpec("dianping").setIndicator((View)localObject2).setContent(new TabHost.TabContentFactory()
      {
        public View createTabContent(String paramString)
        {
          return HotelOverseaReviewListActivity.this.initReviewListView();
        }
      }));
    }
    if (j != 0)
    {
      localObject2 = (TextView)LayoutInflater.from(this).inflate(R.layout.tuan_tab_indicator, null, false);
      ((TextView)localObject2).setText("Booking网友(" + j + ")");
      ((TabHost)localObject1).addTab(((TabHost)localObject1).newTabSpec("booking").setIndicator((View)localObject2).setContent(new TabHost.TabContentFactory()
      {
        public View createTabContent(String paramString)
        {
          return HotelOverseaReviewListActivity.this.initWebView();
        }
      }));
    }
    ((TabHost)localObject1).setOnTabChangedListener(new TabHost.OnTabChangeListener()
    {
      public void onTabChanged(String paramString)
      {
        if ("dianping".equals(paramString))
          HotelOverseaReviewListActivity.this.mBookingLayout.setVisibility(8);
        do
          return;
        while ((!"booking".equals(paramString)) || (HotelOverseaReviewListActivity.this.mBookingPrice == null));
        HotelOverseaReviewListActivity.this.mBookingLayout.setVisibility(0);
      }
    });
    if ((i == 0) || (j == 0))
      ((TabHost)localObject1).findViewById(16908307).setVisibility(8);
    this.mBookingLayout = findViewById(R.id.booking);
    this.mBookingLayout.setVisibility(8);
    localObject1 = findViewById(R.id.hotel_date_layout);
    ((View)localObject1).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HotelOverseaReviewListActivity.this.checkingTime = System.currentTimeMillis();
        HotelOverseaReviewListActivity.this.checkoutTime = (HotelOverseaReviewListActivity.this.checkingTime + 86400000L);
        HotelOverseaReviewListActivity.this.startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelbookingpicktime?checkin_time=" + System.currentTimeMillis() + "&checkout_time=" + HotelOverseaReviewListActivity.this.checkoutTime)), 111);
      }
    });
    this.checkingTime = System.currentTimeMillis();
    this.checkoutTime = (this.checkingTime + 86400000L);
    ((TextView)((View)localObject1).findViewById(16908308)).setText("入住 " + SDF.format(Long.valueOf(this.checkingTime)));
    ((TextView)((View)localObject1).findViewById(16908309)).setText("退房 " + SDF.format(Long.valueOf(this.checkoutTime)));
    if (this.mBookingPrice != null)
    {
      localObject1 = (LinearLayout)findViewById(R.id.ota_content);
      localObject2 = initHotelBookingView();
      if (localObject2 != null)
        ((LinearLayout)localObject1).addView((View)localObject2);
    }
  }

  private View initWebView()
  {
    String str = this.mReviewList.getString("OTAReviewUrl");
    LinearLayout localLinearLayout = new LinearLayout(this);
    localLinearLayout.setId(R.id.hotel_oversea_reviewlist_container_booking);
    localLinearLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
    Object localObject = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)localObject);
    int i = ((DisplayMetrics)localObject).heightPixels;
    localObject = new WebView(this);
    boolean bool2 = true;
    boolean bool1 = bool2;
    if (this.mReviewList != null)
    {
      bool1 = bool2;
      if (this.mReviewList.contains("IsDianpingURL"))
        bool1 = this.mReviewList.getBoolean("IsDianpingURL");
    }
    if (bool1);
    for (i = (int)(i * 2.5D); ; i = -2)
    {
      ((WebView)localObject).setLayoutParams(new LinearLayout.LayoutParams(-1, i));
      ((WebView)localObject).setScrollBarStyle(0);
      ((WebView)localObject).getSettings().setBuiltInZoomControls(true);
      ((WebView)localObject).getSettings().setJavaScriptEnabled(true);
      ((WebView)localObject).getSettings().setUseWideViewPort(true);
      ((WebView)localObject).getSettings().setLoadWithOverviewMode(true);
      ((WebView)localObject).getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
      ((WebView)localObject).loadUrl(str);
      ((WebView)localObject).setWebViewClient(new WebViewClient((WebView)localObject, localLinearLayout)
      {
        public void onPageFinished(WebView paramWebView, String paramString)
        {
          super.onPageFinished(paramWebView, paramString);
          this.val$webView.getLayoutParams().height = -2;
          this.val$container.removeAllViews();
          this.val$container.addView(this.val$webView);
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
        {
          this.val$webView.getLayoutParams().height = 10;
          return super.shouldOverrideUrlLoading(paramWebView, paramString);
        }
      });
      return localLinearLayout;
    }
  }

  private void processParams(Bundle paramBundle)
  {
    this.mShopId = getIntParam("id");
    this.mReviewList = ((DPObject)getIntent().getExtras().get("reviewList"));
    this.mShopObj = ((DPObject)getIntent().getExtras().get("shop"));
    this.mBookingPrice = ((DPObject)getIntent().getExtras().get("booking_price"));
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 111) && (paramInt2 == -1))
    {
      this.checkingTime = paramIntent.getLongExtra("checkin_time", System.currentTimeMillis());
      this.checkoutTime = paramIntent.getLongExtra("checkout_time", this.checkingTime + 86400000L);
      ((TextView)this.mBookingLayout.findViewById(16908308)).setText("入住 " + SDF.format(Long.valueOf(this.checkingTime)));
      ((TextView)this.mBookingLayout.findViewById(16908309)).setText("退房 " + SDF.format(Long.valueOf(this.checkoutTime)));
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    processParams(paramBundle);
    initViews();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.ugc.HotelOverseaReviewListActivity
 * JD-Core Version:    0.6.0
 */