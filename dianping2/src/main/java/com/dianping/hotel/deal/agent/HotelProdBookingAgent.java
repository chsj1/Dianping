package com.dianping.hotel.deal.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.agent.TuanGroupCellAgent;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.hotel.deal.fragment.HotelProdInfoAgentFragment;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HotelProdBookingAgent extends TuanGroupCellAgent
{
  private static final String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };
  private int bmpW;
  private ImageView bottom_arrow;
  private View contentView;
  private int currIndex = 0;
  private ImageView cursor;
  private DPObject dpDeal;
  private DPObject dpHotelProdBase;
  private DPObject dpHotelProdDetail;
  private int initIndex;
  private List<View> listViews;
  private ViewPager mPager;
  private HotelProdInfoAgentFragment mProdFragment;
  private int offset;
  private List<View> statusList;
  private List<View> tabList;

  public HotelProdBookingAgent(Object paramObject)
  {
    super(paramObject);
    this.mProdFragment = ((HotelProdInfoAgentFragment)paramObject);
  }

  private View createDealBookingList(DPObject[] paramArrayOfDPObject, int paramInt1, int paramInt2)
  {
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localLinearLayout.setOrientation(1);
    localLinearLayout.setBackgroundColor(this.res.getColor(R.color.hotel_deal_booking_list_color));
    Object localObject1;
    Object localObject2;
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length < 1) || (paramInt1 != 1))
    {
      localObject1 = new RelativeLayout(getContext());
      ((RelativeLayout)localObject1).setLayoutParams(new LinearLayout.LayoutParams(-1, paramInt2 * 100));
      paramArrayOfDPObject = new ImageView(getContext());
      localObject2 = new RelativeLayout.LayoutParams(40, 40);
      ((RelativeLayout.LayoutParams)localObject2).addRule(0, 1);
      ((RelativeLayout.LayoutParams)localObject2).addRule(15);
      ((RelativeLayout.LayoutParams)localObject2).rightMargin = 10;
      paramArrayOfDPObject.setLayoutParams((ViewGroup.LayoutParams)localObject2);
      paramArrayOfDPObject.setBackgroundResource(R.drawable.info);
      ((RelativeLayout)localObject1).addView(paramArrayOfDPObject);
      localObject2 = new TextView(getContext());
      ((TextView)localObject2).setId(1);
      paramArrayOfDPObject = new RelativeLayout.LayoutParams(-2, -2);
      paramArrayOfDPObject.addRule(14);
      paramArrayOfDPObject.addRule(15);
      ((TextView)localObject2).setGravity(17);
      ((TextView)localObject2).setLayoutParams(paramArrayOfDPObject);
      ((TextView)localObject2).setTextColor(this.res.getColor(R.color.hotel_roomlist_extra_color));
      ((TextView)localObject2).setTextSize(0, this.res.getDimensionPixelSize(R.dimen.text_size_13));
      if (paramInt1 == 0);
      for (paramArrayOfDPObject = "本日团购已满房"; ; paramArrayOfDPObject = "本日不可在线预订，请致电商户预订")
      {
        ((TextView)localObject2).setText(paramArrayOfDPObject);
        ((RelativeLayout)localObject1).addView((View)localObject2);
        localLinearLayout.addView((View)localObject1);
        return localLinearLayout;
      }
    }
    paramInt1 = 0;
    label285: DPObject localDPObject;
    int i;
    RelativeLayout localRelativeLayout;
    label426: label598: DPObject[] arrayOfDPObject;
    int j;
    if (paramInt1 < paramArrayOfDPObject.length)
    {
      localDPObject = paramArrayOfDPObject[paramInt1];
      i = localDPObject.getInt("Status");
      localRelativeLayout = new RelativeLayout(getContext());
      localRelativeLayout.setBackgroundColor(this.res.getColor(R.color.hotel_deal_booking_list_color));
      localRelativeLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, 100));
      localObject1 = new TextView(getContext());
      localObject2 = new RelativeLayout.LayoutParams(-2, -2);
      ((RelativeLayout.LayoutParams)localObject2).addRule(9);
      ((RelativeLayout.LayoutParams)localObject2).addRule(15);
      ((RelativeLayout.LayoutParams)localObject2).leftMargin = 40;
      ((RelativeLayout.LayoutParams)localObject2).rightMargin = 40;
      ((TextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      if (i != 1)
        break label815;
      paramInt2 = this.res.getColor(R.color.hotel_datepicker_color);
      ((TextView)localObject1).setTextColor(paramInt2);
      ((TextView)localObject1).setTextSize(0, this.res.getDimensionPixelSize(R.dimen.text_size_14));
      if (!TextUtils.isEmpty(localDPObject.getString("DealTitle")))
        ((TextView)localObject1).setText(localDPObject.getString("DealTitle"));
      localRelativeLayout.addView((View)localObject1);
      localObject1 = new TextView(getContext());
      localObject2 = new RelativeLayout.LayoutParams(-2, -2);
      ((RelativeLayout.LayoutParams)localObject2).addRule(11);
      ((RelativeLayout.LayoutParams)localObject2).addRule(15);
      ((RelativeLayout.LayoutParams)localObject2).rightMargin = 40;
      ((TextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      if (!TextUtils.isEmpty(localDPObject.getString("StatusTitle")))
        ((TextView)localObject1).setText(localDPObject.getString("StatusTitle"));
      ((TextView)localObject1).setTextSize(0, this.res.getDimensionPixelSize(R.dimen.text_size_13));
      if (i != 1)
        break label829;
      paramInt2 = this.res.getColor(R.color.hotel_datepicker_color);
      ((TextView)localObject1).setTextColor(paramInt2);
      localRelativeLayout.addView((View)localObject1);
      if (i == 1)
      {
        i = localDPObject.getInt("DealId");
        localObject2 = null;
        localObject1 = localObject2;
        if (this.dpDeal.getArray("DealSelectList") != null)
        {
          localObject1 = localObject2;
          if (this.dpDeal.getArray("DealSelectList").length > 1)
          {
            arrayOfDPObject = this.dpDeal.getArray("DealSelectList");
            j = arrayOfDPObject.length;
            paramInt2 = 0;
          }
        }
      }
    }
    while (true)
    {
      localObject1 = localObject2;
      if (paramInt2 < j)
      {
        localObject1 = arrayOfDPObject[paramInt2];
        if (i != ((DPObject)localObject1).getInt("ID"));
      }
      else
      {
        localRelativeLayout.setOnClickListener(new View.OnClickListener(localDPObject, (DPObject)localObject1)
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
            Bundle localBundle = new Bundle();
            localBundle.putParcelable("bookingDefaultInfo", this.val$roomStatus.getObject("BookingDefaultInfo"));
            paramView.putExtra("extradata", localBundle);
            paramView.putExtra("deal", HotelProdBookingAgent.this.dpDeal);
            paramView.putExtra("dealSelect", this.val$finalDealSelect);
            HotelProdBookingAgent.this.startActivity(paramView);
          }
        });
        localLinearLayout.addView(localRelativeLayout);
        if (paramInt1 != paramArrayOfDPObject.length - 1)
        {
          localObject1 = new ImageView(getContext());
          localObject2 = new LinearLayout.LayoutParams(-1, -2);
          ((LinearLayout.LayoutParams)localObject2).leftMargin = ViewUtils.dip2px(getContext(), 5.0F);
          ((ImageView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
          ((ImageView)localObject1).setBackgroundResource(R.drawable.gray_horizontal_line);
          localLinearLayout.addView((View)localObject1);
        }
        paramInt1 += 1;
        break label285;
        break;
        label815: paramInt2 = this.res.getColor(R.color.hotel_roomlist_extra_color);
        break label426;
        label829: paramInt2 = this.res.getColor(R.color.hotel_roomlist_extra_color);
        break label598;
      }
      paramInt2 += 1;
    }
  }

  private boolean setupView()
  {
    Object localObject1 = this.dpHotelProdDetail.getArray("BookingInfoList");
    if (DPObjectUtils.isArrayEmpty(localObject1))
      return false;
    Object localObject2 = (LinearLayout)this.contentView.findViewById(R.id.day_content);
    Object localObject3 = (LinearLayout)this.contentView.findViewById(R.id.room_status_content);
    this.tabList = new ArrayList();
    this.statusList = new ArrayList();
    int i = 0;
    if ((i < localObject1.length) && (localObject1[i].getInt("Status") != 1))
    {
      if (i + 1 < localObject1.length);
      for (j = i + 1; ; j = -1)
      {
        this.initIndex = j;
        i += 1;
        break;
      }
    }
    this.currIndex = this.initIndex;
    label151: Object localObject6;
    Object localObject4;
    Object localObject5;
    label280: LinearLayout.LayoutParams localLayoutParams;
    if (localObject1.length > 7)
    {
      i = 7;
      j = 0;
      if (j >= i)
        break label661;
      localObject6 = new LinearLayout(getContext());
      localObject4 = new LinearLayout.LayoutParams(-1, -2);
      ((LinearLayout.LayoutParams)localObject4).weight = 1.0F;
      ((LinearLayout)localObject6).setLayoutParams((ViewGroup.LayoutParams)localObject4);
      ((LinearLayout)localObject6).setOrientation(1);
      localObject4 = new TextView(getContext());
      ((TextView)localObject4).setId(1);
      localObject5 = new LinearLayout.LayoutParams(-1, -2);
      ((LinearLayout.LayoutParams)localObject5).weight = 1.0F;
      ((LinearLayout.LayoutParams)localObject5).topMargin = ViewUtils.dip2px(getContext(), 5.0F);
      ((TextView)localObject4).setLayoutParams((ViewGroup.LayoutParams)localObject5);
      ((TextView)localObject4).setGravity(17);
      if (j != this.initIndex)
        break label631;
      k = -1;
      ((TextView)localObject4).setTextColor(k);
      ((TextView)localObject4).setTextSize(0, this.res.getDimensionPixelSize(R.dimen.text_size_11));
      ((LinearLayout)localObject6).addView((View)localObject4);
      localObject5 = new TextView(getContext());
      ((TextView)localObject5).setId(2);
      localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
      localLayoutParams.weight = 1.0F;
      localLayoutParams.bottomMargin = ViewUtils.dip2px(getContext(), 5.0F);
      ((TextView)localObject5).setLayoutParams(localLayoutParams);
      ((TextView)localObject5).setGravity(17);
      if (j != this.initIndex)
        break label646;
    }
    label646: for (int k = -1; ; k = this.res.getColor(R.color.hotel_datepicker_color))
    {
      ((TextView)localObject5).setTextColor(k);
      ((TextView)localObject5).setTextSize(0, this.res.getDimensionPixelSize(R.dimen.text_size_17));
      ((LinearLayout)localObject6).addView((View)localObject5);
      ((LinearLayout)localObject6).setOnClickListener(new View.OnClickListener(localObject1, j)
      {
        public void onClick(View paramView)
        {
          if (this.val$bookingInfoList[this.val$finalI].getInt("Status") == 1)
            HotelProdBookingAgent.this.mPager.setCurrentItem(this.val$finalI);
        }
      });
      this.tabList.add(localObject6);
      ((LinearLayout)localObject2).addView((View)localObject6);
      localObject6 = new TextView(getContext());
      ((TextView)localObject6).setId(3);
      localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
      localLayoutParams.weight = 1.0F;
      ((TextView)localObject6).setLayoutParams(localLayoutParams);
      ((TextView)localObject6).setGravity(17);
      ((TextView)localObject6).setTextColor(-16777216);
      ((TextView)localObject6).setTextSize(0, this.res.getDimensionPixelSize(R.dimen.text_size_13));
      ((LinearLayout)localObject3).addView((View)localObject6);
      ((TextView)localObject6).setOnClickListener(new View.OnClickListener(localObject1, j)
      {
        public void onClick(View paramView)
        {
          if (this.val$bookingInfoList[this.val$finalI].getInt("Status") == 1)
            HotelProdBookingAgent.this.mPager.setCurrentItem(this.val$finalI);
        }
      });
      this.statusList.add(localObject6);
      localLayoutParams = localObject1[j];
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.setTimeInMillis(localLayoutParams.getTime("Date"));
      updateDay((TextView)localObject4, (TextView)localObject5, (TextView)localObject6, localCalendar, localLayoutParams.getInt("Status"), localLayoutParams.getString("StatusTitle"), j);
      j += 1;
      break label151;
      i = localObject1.length;
      break;
      label631: k = this.res.getColor(R.color.hotel_roomlist_extra_color);
      break label280;
    }
    label661: if ((this.tabList == null) || (this.tabList.size() < 1))
      return false;
    this.cursor = ((ImageView)this.contentView.findViewById(R.id.cursor));
    localObject2 = (View)this.tabList.get(0);
    ((View)localObject2).measure(0, 0);
    i = ((View)localObject2).getMeasuredHeight();
    int j = ((View)localObject2).getMeasuredWidth();
    if (i > 0)
      this.cursor.getLayoutParams().height = (i + 10);
    if (j > 0)
      this.cursor.getLayoutParams().width = (j + 45);
    this.bmpW = (j + 45);
    localObject2 = new DisplayMetrics();
    ((NovaActivity)getContext()).getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)localObject2);
    i = ((DisplayMetrics)localObject2).widthPixels;
    this.offset = ((i / 7 - this.bmpW) / 2);
    ((FrameLayout.LayoutParams)this.cursor.getLayoutParams()).leftMargin = this.offset;
    this.bottom_arrow = ((ImageView)this.contentView.findViewById(R.id.bottom_arrow));
    this.bottom_arrow.measure(0, 0);
    ((FrameLayout.LayoutParams)this.bottom_arrow.getLayoutParams()).leftMargin = ((i / 7 - this.bottom_arrow.getMeasuredWidth()) / 2);
    this.mPager = ((ViewPager)this.contentView.findViewById(R.id.vPager));
    this.mPager.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        return true;
      }
    });
    if ((this.initIndex == -1) && (this.mPager != null))
      this.mPager.setVisibility(8);
    i = 0;
    j = 0;
    while (j < this.tabList.size())
    {
      localObject2 = localObject1[j].getArray("StatusList");
      k = i;
      if (localObject2 != null)
      {
        k = i;
        if (localObject2.length > i)
          k = localObject2.length;
      }
      j += 1;
      i = k;
    }
    this.listViews = new ArrayList();
    j = 0;
    while (j < this.tabList.size())
    {
      localObject2 = localObject1[j];
      localObject3 = ((DPObject)localObject2).getArray("StatusList");
      k = ((DPObject)localObject2).getInt("Status");
      this.listViews.add(createDealBookingList(localObject3, k, i));
      j += 1;
    }
    if ((this.listViews == null) || (this.listViews.size() <= 0))
      return false;
    this.mPager.setAdapter(new MyPagerAdapter(this.listViews));
    this.mPager.setCurrentItem(this.initIndex);
    localObject2 = (FrameLayout.LayoutParams)this.cursor.getLayoutParams();
    ((FrameLayout.LayoutParams)localObject2).leftMargin += this.initIndex * (this.offset * 2 + this.bmpW + 1);
    localObject2 = (FrameLayout.LayoutParams)this.bottom_arrow.getLayoutParams();
    ((FrameLayout.LayoutParams)localObject2).leftMargin += this.initIndex * (this.offset * 2 + this.bmpW + 1);
    ((View)this.listViews.get(0)).measure(0, 0);
    this.mPager.getLayoutParams().height = ((View)this.listViews.get(0)).getMeasuredHeight();
    this.mPager.setOnPageChangeListener(new MyOnPageChangeListener(null));
    if ((this.initIndex >= 0) && (this.initIndex < localObject1.length))
    {
      localObject1 = localObject1[this.initIndex].getArray("StatusList");
      if ((localObject1 != null) && (localObject1.length > 0))
      {
        setSharedObject("statusList", localObject1);
        if (localObject1[0] == null)
          break label1415;
      }
    }
    label1415: for (localObject1 = localObject1[0].getObject("BookingDefaultInfo"); ; localObject1 = null)
    {
      setSharedObject("bookingDefaultInfo", localObject1);
      removeAllCells();
      localObject1 = new DealInfoCommonCell(getContext());
      ((DealInfoCommonCell)localObject1).setTitle("在线预订", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          Object localObject = HotelProdBookingAgent.this.dpHotelProdDetail.getString("BookingProcessUrl");
          if (!TextUtils.isEmpty((CharSequence)localObject))
          {
            if (((String)localObject).startsWith("dianping"))
            {
              paramView = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
              HotelProdBookingAgent.this.fragment.getActivity().startActivity(paramView);
            }
          }
          else
            return;
          paramView = Uri.parse("dianping://web");
          localObject = Uri.parse((String)localObject);
          paramView = new Intent("android.intent.action.VIEW", paramView.buildUpon().appendQueryParameter("url", ((Uri)localObject).toString()).build());
          HotelProdBookingAgent.this.fragment.getActivity().startActivity(paramView);
        }
      });
      ((DealInfoCommonCell)localObject1).setArrowPre("预订流程");
      ((DealInfoCommonCell)localObject1).addContent(this.contentView, false);
      addCell("", (View)localObject1);
      return true;
    }
  }

  private void updateDay(TextView paramTextView1, TextView paramTextView2, TextView paramTextView3, Calendar paramCalendar, int paramInt1, String paramString, int paramInt2)
  {
    if (paramInt2 == 0);
    for (String str = "今天"; ; str = weekDays[(paramCalendar.get(7) - 1)])
    {
      paramTextView1.setText(str);
      paramTextView2.setText(paramCalendar.get(5) + "");
      switch (paramInt1)
      {
      default:
        if (!TextUtils.isEmpty(paramString))
          paramTextView3.setText(paramString);
        return;
      case 0:
      case 1:
      case 2:
      }
    }
    paramTextView3.setTag(Integer.valueOf(0));
    paramTextView3.setText("满房");
    paramTextView3.setTextColor(this.res.getColor(R.color.hotel_roomlist_extra_color));
    if (paramInt2 == this.initIndex);
    for (paramInt1 = this.res.getColor(R.color.white); ; paramInt1 = this.res.getColor(R.color.hotel_roomlist_extra_color))
    {
      paramTextView2.setTextColor(paramInt1);
      paramTextView2.setTag("isFull");
      break;
    }
    paramTextView3.setTag(Integer.valueOf(1));
    paramTextView3.setText("有房");
    if (paramInt2 == this.initIndex);
    for (paramInt1 = this.res.getColor(R.color.orange_red); ; paramInt1 = this.res.getColor(R.color.hotel_datepicker_color))
    {
      paramTextView3.setTextColor(paramInt1);
      break;
    }
    paramTextView3.setTag(Integer.valueOf(2));
    paramTextView3.setText("--");
    paramTextView3.setTextColor(this.res.getColor(R.color.hotel_roomlist_extra_color));
    if (paramInt2 == this.initIndex);
    for (paramInt1 = this.res.getColor(R.color.white); ; paramInt1 = this.res.getColor(R.color.hotel_roomlist_extra_color))
    {
      paramTextView2.setTextColor(paramInt1);
      paramTextView2.setTag("isNull");
      break;
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null);
    label12: 
    do
    {
      do
      {
        do
        {
          break label12;
          do
            return;
          while (this.mProdFragment.bizType != 1);
          if (paramBundle == null)
            continue;
          this.dpHotelProdBase = ((DPObject)paramBundle.getParcelable("hotelprodbase"));
          this.dpHotelProdDetail = ((DPObject)paramBundle.getParcelable("hotelproddetail"));
        }
        while ((this.dpHotelProdBase == null) || (!this.dpHotelProdBase.getBoolean("IsHotelBookable")) || (this.dpHotelProdDetail == null));
        this.dpDeal = this.dpHotelProdBase.getObject("Deal");
      }
      while (this.contentView != null);
      this.contentView = this.res.inflate(getContext(), R.layout.hotel_deal_booking, null, false);
    }
    while (setupView());
  }

  private class MyOnPageChangeListener
    implements ViewPager.OnPageChangeListener
  {
    int one = HotelProdBookingAgent.this.offset * 2 + HotelProdBookingAgent.this.bmpW + 1;

    private MyOnPageChangeListener()
    {
    }

    public void onPageScrollStateChanged(int paramInt)
    {
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
    }

    public void onPageSelected(int paramInt)
    {
      if ((paramInt == HotelProdBookingAgent.this.currIndex) || (HotelProdBookingAgent.this.currIndex < 0) || ((HotelProdBookingAgent.this.tabList != null) && (HotelProdBookingAgent.this.currIndex >= HotelProdBookingAgent.this.tabList.size())));
      do
      {
        return;
        ((View)HotelProdBookingAgent.this.listViews.get(paramInt)).measure(0, 0);
        HotelProdBookingAgent.this.mPager.getLayoutParams().height = ((View)HotelProdBookingAgent.this.listViews.get(paramInt)).getMeasuredHeight();
        localObject = new TranslateAnimation(this.one * (HotelProdBookingAgent.this.currIndex - HotelProdBookingAgent.this.initIndex), this.one * (paramInt - HotelProdBookingAgent.this.initIndex), 0.0F, 0.0F);
        ((Animation)localObject).setFillAfter(true);
        ((Animation)localObject).setDuration(100L);
        ((Animation)localObject).setAnimationListener(new Animation.AnimationListener(paramInt)
        {
          public void onAnimationEnd(Animation paramAnimation)
          {
            paramAnimation = (TextView)((View)HotelProdBookingAgent.this.tabList.get(this.val$arg0)).findViewById(2);
            paramAnimation.setTextColor(HotelProdBookingAgent.this.res.getColor(R.color.white));
            ((TextView)((View)HotelProdBookingAgent.this.tabList.get(this.val$arg0)).findViewById(1)).setTextColor(HotelProdBookingAgent.this.res.getColor(R.color.white));
            Object localObject = (TextView)((View)HotelProdBookingAgent.this.statusList.get(this.val$arg0)).findViewById(3);
            int i;
            if ((paramAnimation.getTag() == null) || ((!paramAnimation.getTag().equals("isFull")) && (!paramAnimation.getTag().equals("isNull"))))
            {
              i = HotelProdBookingAgent.this.res.getColor(R.color.hotel_roomlist_price_color);
              ((TextView)localObject).setTextColor(i);
              HotelProdBookingAgent.access$602(HotelProdBookingAgent.this, this.val$arg0);
              if (HotelProdBookingAgent.this.dpDeal.getObject("HotelDealGroupDetailInfo") == null)
                break label335;
              paramAnimation = HotelProdBookingAgent.this.dpDeal.getObject("HotelDealGroupDetailInfo").getArray("BookingInfoList");
              label235: if ((paramAnimation != null) && (paramAnimation.length > 0))
              {
                paramAnimation = paramAnimation[HotelProdBookingAgent.this.currIndex].getArray("StatusList");
                if ((paramAnimation != null) && (paramAnimation.length > 0))
                {
                  HotelProdBookingAgent.this.setSharedObject("statusList", paramAnimation);
                  localObject = HotelProdBookingAgent.this;
                  if (paramAnimation[0] == null)
                    break label340;
                }
              }
            }
            label335: label340: for (paramAnimation = paramAnimation[0].getObject("BookingDefaultInfo"); ; paramAnimation = null)
            {
              ((HotelProdBookingAgent)localObject).setSharedObject("bookingDefaultInfo", paramAnimation);
              return;
              i = HotelProdBookingAgent.this.res.getColor(R.color.hotel_roomlist_extra_color);
              break;
              paramAnimation = null;
              break label235;
            }
          }

          public void onAnimationRepeat(Animation paramAnimation)
          {
          }

          public void onAnimationStart(Animation paramAnimation)
          {
            paramAnimation = (TextView)((View)HotelProdBookingAgent.this.tabList.get(HotelProdBookingAgent.this.currIndex)).findViewById(1);
            label149: TextView localTextView;
            if ((paramAnimation.getTag() != null) && (paramAnimation.getTag().equals("isWeekend")))
            {
              i = HotelProdBookingAgent.this.res.getColor(R.color.orange_red);
              paramAnimation.setTextColor(i);
              paramAnimation = (TextView)((View)HotelProdBookingAgent.this.tabList.get(HotelProdBookingAgent.this.currIndex)).findViewById(2);
              if ((paramAnimation.getTag() == null) || (!paramAnimation.getTag().equals("isFull")))
                break label264;
              i = HotelProdBookingAgent.this.res.getColor(R.color.hotel_roomlist_extra_color);
              paramAnimation.setTextColor(i);
              localTextView = (TextView)((View)HotelProdBookingAgent.this.statusList.get(HotelProdBookingAgent.this.currIndex)).findViewById(3);
              if ((paramAnimation.getTag() == null) || ((!paramAnimation.getTag().equals("isFull")) && (!paramAnimation.getTag().equals("isNull"))))
                break label284;
            }
            label264: label284: for (int i = HotelProdBookingAgent.this.res.getColor(R.color.hotel_roomlist_extra_color); ; i = HotelProdBookingAgent.this.res.getColor(R.color.black))
            {
              localTextView.setTextColor(i);
              return;
              i = HotelProdBookingAgent.this.res.getColor(R.color.hotel_roomlist_extra_color);
              break;
              i = HotelProdBookingAgent.this.res.getColor(R.color.hotel_datepicker_color);
              break label149;
            }
          }
        });
        HotelProdBookingAgent.this.cursor.startAnimation((Animation)localObject);
        localObject = new TranslateAnimation(this.one * (HotelProdBookingAgent.this.currIndex - HotelProdBookingAgent.this.initIndex), this.one * (paramInt - HotelProdBookingAgent.this.initIndex), 0.0F, 0.0F);
        ((Animation)localObject).setFillAfter(true);
        ((Animation)localObject).setDuration(99L);
        HotelProdBookingAgent.this.bottom_arrow.startAnimation((Animation)localObject);
      }
      while ((HotelProdBookingAgent.this.currIndex <= 0) || (HotelProdBookingAgent.this.currIndex >= HotelProdBookingAgent.this.tabList.size()));
      Object localObject = (TextView)((View)HotelProdBookingAgent.this.tabList.get(HotelProdBookingAgent.this.currIndex)).findViewById(2);
      if ((((TextView)localObject).getTag() != null) && (((TextView)localObject).getTag().equals("isFull")))
        localObject = "满房";
      while (true)
      {
        DPApplication.instance().statisticsEvent("tuan5", "hotel_tuan5_calender", (String)localObject, 2);
        return;
        if ((((TextView)localObject).getTag() != null) && (((TextView)localObject).getTag().equals("isNull")))
        {
          localObject = "空";
          continue;
        }
        localObject = "有房";
      }
    }
  }

  private class MyPagerAdapter extends PagerAdapter
  {
    public List<View> mListViews;

    public MyPagerAdapter()
    {
      Object localObject;
      this.mListViews = localObject;
    }

    public void destroyItem(View paramView, int paramInt, Object paramObject)
    {
      ((ViewPager)paramView).removeView((View)this.mListViews.get(paramInt));
    }

    public int getCount()
    {
      return this.mListViews.size();
    }

    public Object instantiateItem(View paramView, int paramInt)
    {
      ((ViewPager)paramView).addView((View)this.mListViews.get(paramInt), 0);
      return this.mListViews.get(paramInt);
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdBookingAgent
 * JD-Core Version:    0.6.0
 */