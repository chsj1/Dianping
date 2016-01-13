package com.dianping.shopinfo.fun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
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
import com.dianping.shopinfo.widget.ExpandView;
import com.dianping.shopinfo.widget.ScheduleBlockView;
import com.dianping.shopinfo.widget.ScheduleBlockView.ScheduleBlockInterface;
import com.dianping.shopinfo.widget.ScheduleListView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaRelativeLayout;

public class KTVBookTableAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_KTVBOOKING = "0450KTV.0100KTVBooking";
  private ScheduleBlockView blockView = null;
  private boolean hasRequested = false;
  private LinearLayout ktvPeriodContainer = null;
  private RadioGroup ktvPeriodRG = null;
  private HorizontalScrollView ktvPeriodScroll = null;
  private DPObject[] ktvRooms = null;
  private DPObject ktvTable = null;
  private MApiRequest mKTVBookTableRequest;
  private ScheduleBlockView.ScheduleBlockInterface scheduleBlockInterface = new ScheduleBlockView.ScheduleBlockInterface()
  {
    public View getDateItemView(DPObject paramDPObject, int paramInt, RadioGroup paramRadioGroup)
    {
      if ((paramDPObject != null) && (paramDPObject.getString("DisplayName") != null))
      {
        paramRadioGroup = (NovaRelativeLayout)LayoutInflater.from(KTVBookTableAgent.this.getContext()).inflate(R.layout.ktv_book_date_item, paramRadioGroup, false);
        Object localObject = (TextView)paramRadioGroup.findViewById(R.id.ktvDateTV);
        TextView localTextView = (TextView)paramRadioGroup.findViewById(R.id.ktvDateDiscount);
        paramRadioGroup.setId(paramInt);
        ((TextView)localObject).setText(paramDPObject.getString("DisplayName"));
        localObject = paramDPObject.getString("Discount");
        if ((localObject != null) && (((String)localObject).length() > 0))
        {
          localTextView.setText((CharSequence)localObject);
          localTextView.setVisibility(0);
        }
        while (true)
        {
          if (paramInt == 0)
            paramRadioGroup.setSelected(true);
          paramRadioGroup.setGAString("bookingdate");
          paramRadioGroup.setOnClickListener(new View.OnClickListener(paramDPObject, paramInt)
          {
            public void onClick(View paramView)
            {
              paramView = this.val$date.getArray("KtvBookPeriods");
              if ((paramView != null) && (paramView.length > 0))
              {
                paramView = paramView[0];
                KTVBookTableAgent.access$402(KTVBookTableAgent.this, paramView.getArray("KtvBookRooms"));
              }
              while (true)
              {
                if (KTVBookTableAgent.this.blockView != null)
                  KTVBookTableAgent.this.blockView.sendDateChangeMsg(this.val$index);
                return;
                KTVBookTableAgent.access$402(KTVBookTableAgent.this, null);
              }
            }
          });
          return paramRadioGroup;
          localTextView.setVisibility(8);
        }
      }
      return (View)null;
    }

    public DPObject[] getScheduleListData()
    {
      return KTVBookTableAgent.this.ktvRooms;
    }

    public View getScheduleListItemView(DPObject paramDPObject, ScheduleListView paramScheduleListView)
    {
      if (paramDPObject == null)
        return null;
      NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(KTVBookTableAgent.this.getContext()).inflate(R.layout.ktv_booktable_room_item, paramScheduleListView, false);
      Object localObject1 = (ExpandView)LayoutInflater.from(KTVBookTableAgent.this.getContext()).inflate(R.layout.shop_expand_view, paramScheduleListView, false);
      ((ExpandView)localObject1).setExpandTextTitle("更多");
      ((ExpandView)localObject1).setTextColor(KTVBookTableAgent.this.getResources().getColor(R.color.shopinfo_single_text_color));
      ((ExpandView)localObject1).setLayoutParams(new ViewGroup.LayoutParams(-1, ViewUtils.dip2px(KTVBookTableAgent.this.getContext(), 38.0F)));
      ((ExpandView)localObject1).setBackgroundColor(KTVBookTableAgent.this.getResources().getColor(R.color.wm_menutitle_gray));
      ((ExpandView)localObject1).setGAString("KTVbookingmore");
      paramScheduleListView.setExpandView((ExpandView)localObject1);
      paramScheduleListView = (TextView)localNovaRelativeLayout.findViewById(R.id.ktvRoomTypeTV);
      localObject1 = (TextView)localNovaRelativeLayout.findViewById(R.id.ktvPrice);
      Object localObject3 = (TextView)localNovaRelativeLayout.findViewById(R.id.ktvOriginalPriceTV);
      Object localObject2 = (TextView)localNovaRelativeLayout.findViewById(R.id.ktvRoomComment);
      TextView localTextView1 = (TextView)localNovaRelativeLayout.findViewById(R.id.booking_reduction);
      TextView localTextView2 = (TextView)localNovaRelativeLayout.findViewById(R.id.bookRoomTV);
      String str = paramDPObject.getString("RoomType");
      Object localObject4 = "(" + paramDPObject.getString("RoomCapacity") + ")";
      paramScheduleListView.setText(str + (String)localObject4);
      localObject4 = paramDPObject.getString("Price");
      localObject4 = new SpannableString("￥" + (String)localObject4);
      ((SpannableString)localObject4).setSpan(new AbsoluteSizeSpan(KTVBookTableAgent.this.res.getDimensionPixelSize(R.dimen.text_size_12)), 0, 1, 33);
      ((SpannableString)localObject4).setSpan(new AbsoluteSizeSpan(KTVBookTableAgent.this.res.getDimensionPixelSize(R.dimen.text_size_15)), 1, ((SpannableString)localObject4).length(), 33);
      ((SpannableString)localObject4).setSpan(new ForegroundColorSpan(KTVBookTableAgent.this.getResources().getColor(R.color.tuan_common_orange)), 0, ((SpannableString)localObject4).length(), 33);
      ((TextView)localObject1).setText((CharSequence)localObject4);
      Object localObject5 = paramDPObject.getString("OriginalPrice");
      if ((localObject5 != null) && (((String)localObject5).length() > 0))
      {
        localObject5 = new SpannableString("￥" + (String)localObject5);
        ((SpannableString)localObject5).setSpan(new StrikethroughSpan(), 1, ((SpannableString)localObject5).length(), 33);
        ((SpannableString)localObject5).setSpan(new AbsoluteSizeSpan(KTVBookTableAgent.this.res.getDimensionPixelSize(R.dimen.text_size_12)), 0, ((SpannableString)localObject5).length(), 33);
        ((SpannableString)localObject5).setSpan(new ForegroundColorSpan(KTVBookTableAgent.this.getResources().getColor(R.color.light_gray)), 0, ((SpannableString)localObject5).length(), 33);
        ((TextView)localObject3).setText((CharSequence)localObject5);
        ((TextView)localObject3).setVisibility(0);
        localObject3 = paramDPObject.getString("RoomComment");
        if ((localObject3 == null) || (((String)localObject3).length() <= 0))
          break label765;
        ((TextView)localObject2).setText((CharSequence)localObject3);
        ((TextView)localObject2).setVisibility(0);
        label589: localObject2 = paramDPObject.getStringArray("Reductions");
        if ((localObject2 == null) || (localObject2.length <= 0) || (TextUtils.isEmpty(localObject2[0])))
          break label775;
        localTextView1.setText(localObject2[0]);
        localTextView1.setVisibility(0);
        label634: localTextView2.setText(paramDPObject.getString("ButtonName"));
        if (paramDPObject.getInt("Status") == 1)
          break label785;
        int i = KTVBookTableAgent.this.getResources().getColor(R.color.text_color_light_gray);
        paramScheduleListView.setTextColor(i);
        ((SpannableString)localObject4).setSpan(new ForegroundColorSpan(KTVBookTableAgent.this.getResources().getColor(R.color.text_color_gray)), 0, ((SpannableString)localObject4).length(), 33);
        ((TextView)localObject1).setText((CharSequence)localObject4);
        localTextView1.setBackgroundResource(R.drawable.ktv_background_lightgray_border);
        localTextView1.setTextColor(i);
        localTextView2.setEnabled(false);
        localNovaRelativeLayout.setEnabled(false);
      }
      while (true)
      {
        localNovaRelativeLayout.setGAString("KTVbooking", str);
        return localNovaRelativeLayout;
        ((TextView)localObject3).setVisibility(8);
        break;
        label765: ((TextView)localObject2).setVisibility(8);
        break label589;
        label775: localTextView1.setVisibility(8);
        break label634;
        label785: localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
        {
          public void onClick(View paramView)
          {
            try
            {
              paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$roomData.getString("BookingUrl")));
              KTVBookTableAgent.this.getFragment().startActivity(paramView);
              return;
            }
            catch (Exception paramView)
            {
              paramView.printStackTrace();
            }
          }
        });
      }
    }

    public View getSecondLevelView(DPObject paramDPObject)
    {
      if ((paramDPObject == null) || (KTVBookTableAgent.this.ktvPeriodContainer == null) || (KTVBookTableAgent.this.ktvPeriodScroll == null) || (KTVBookTableAgent.this.ktvPeriodRG == null));
      do
      {
        return null;
        paramDPObject = paramDPObject.getArray("KtvBookPeriods");
        KTVBookTableAgent.this.ktvPeriodRG.removeAllViews();
        KTVBookTableAgent.this.ktvPeriodRG.clearCheck();
      }
      while ((paramDPObject == null) || (paramDPObject.length <= 0));
      int i = 0;
      while (i < paramDPObject.length)
      {
        localView = paramDPObject[i];
        NovaButton localNovaButton = (NovaButton)LayoutInflater.from(KTVBookTableAgent.this.getContext()).inflate(R.layout.ktv_bookperiod_select_button, KTVBookTableAgent.this.ktvPeriodRG, false);
        localNovaButton.setText(localView.getString("DisplayName"));
        localNovaButton.setId(i);
        localNovaButton.setGAString("bookingperiod");
        localNovaButton.setOnClickListener(new View.OnClickListener(paramDPObject)
        {
          public void onClick(View paramView)
          {
            KTVBookTableAgent.this.onPeriodItemChecked(paramView.getId(), this.val$ktvPeriods);
          }
        });
        if (i == 0)
          localNovaButton.setSelected(true);
        KTVBookTableAgent.this.ktvPeriodRG.addView(localNovaButton);
        i += 1;
      }
      View localView = KTVBookTableAgent.this.ktvPeriodRG.getChildAt(0);
      i = (localView.getLeft() + localView.getRight()) / 2;
      int j = KTVBookTableAgent.this.ktvPeriodScroll.getScrollX();
      int k = KTVBookTableAgent.this.ktvPeriodScroll.getWidth() / 2;
      KTVBookTableAgent.this.ktvPeriodScroll.smoothScrollBy(i - j - k, 0);
      paramDPObject = paramDPObject[0];
      KTVBookTableAgent.access$402(KTVBookTableAgent.this, paramDPObject.getArray("KtvBookRooms"));
      return KTVBookTableAgent.this.ktvPeriodContainer;
    }

    public String getTips()
    {
      if (KTVBookTableAgent.this.ktvTable != null)
        return "今天暂不支持预订哦~";
      return null;
    }
  };

  public KTVBookTableAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void onPeriodItemChecked(int paramInt, DPObject[] paramArrayOfDPObject)
  {
    if ((paramInt < 0) || (this.ktvPeriodRG == null) || (this.ktvPeriodRG.getChildCount() == 0) || (paramArrayOfDPObject == null));
    do
    {
      int j;
      do
      {
        return;
        j = this.ktvPeriodRG.getChildCount();
      }
      while ((paramInt >= j) || (paramInt < 0));
      int i = 0;
      while (i < j)
      {
        this.ktvPeriodRG.getChildAt(i).setSelected(false);
        i += 1;
      }
      this.ktvPeriodRG.getChildAt(paramInt).setSelected(true);
      this.ktvRooms = paramArrayOfDPObject[paramInt].getArray("KtvBookRooms");
    }
    while (this.blockView == null);
    this.blockView.sendTimeChangeMsg(paramInt);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getShop() == null);
    do
    {
      do
        return;
      while (this.ktvTable == null);
      paramBundle = this.ktvTable.getArray("KtvBookDates");
    }
    while ((!this.ktvTable.getBoolean("Showable")) || (paramBundle == null) || (paramBundle.length == 0));
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.setGAString("ktvbooking");
    ((NovaRelativeLayout)localShopinfoCommonCell.findViewById(R.id.title_layout)).setVisibility(8);
    LinearLayout localLinearLayout = (LinearLayout)localShopinfoCommonCell.findViewById(R.id.content);
    Object localObject1 = LayoutInflater.from(getContext()).inflate(R.layout.ktv_booktable_booktag, null, false);
    TextView localTextView = (TextView)((View)localObject1).findViewById(R.id.booking_promo);
    Object localObject2 = this.ktvTable.getStringArray("Promos");
    if ((localObject2 != null) && (localObject2.length > 0) && (!TextUtils.isEmpty(localObject2[0])))
    {
      localTextView.setText(localObject2[0]);
      localTextView.setVisibility(0);
      label183: localTextView = (TextView)((View)localObject1).findViewById(R.id.saleCount);
      localObject2 = this.ktvTable.getString("SaleCountInfo");
      if (!TextUtils.isEmpty((CharSequence)localObject2))
        break label417;
      localTextView.setVisibility(8);
    }
    while (true)
    {
      localLinearLayout.addView((View)localObject1);
      localObject1 = getContext().getSharedPreferences("shopinfo_ktv", 0);
      if (!((SharedPreferences)localObject1).getBoolean("isNewKtvLayerShow", false))
      {
        ((SharedPreferences)localObject1).edit().putBoolean("isNewKtvLayerShow", true).commit();
        setKtvLayVisibility(true);
      }
      if (this.blockView != null)
        break;
      this.blockView = ((ScheduleBlockView)LayoutInflater.from(getContext()).inflate(R.layout.schedule_block_view, null, false));
      this.blockView.setAgentHeaderTitle(null);
      this.ktvPeriodContainer = ((LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.ktv_bookperiod_scroller, null, false));
      this.ktvPeriodScroll = ((HorizontalScrollView)this.ktvPeriodContainer.findViewById(R.id.ktv_bookPeriods_scroll));
      this.ktvPeriodRG = ((RadioGroup)this.ktvPeriodContainer.findViewById(R.id.ktvBookPeroidGroup));
      this.blockView.setScheduleBlockInterface(this.scheduleBlockInterface);
      this.blockView.setScheduleBlockDate(paramBundle);
      localLinearLayout.addView(this.blockView);
      addCell("0450KTV.0100KTVBooking", localShopinfoCommonCell);
      return;
      localTextView.setVisibility(8);
      break label183;
      label417: localTextView.setText((CharSequence)localObject2);
      localTextView.setVisibility(0);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mKTVBookTableRequest != null)
    {
      mapiService().abort(this.mKTVBookTableRequest, this, true);
      this.mKTVBookTableRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mKTVBookTableRequest)
    {
      this.mKTVBookTableRequest = null;
      this.hasRequested = true;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mKTVBookTableRequest)
    {
      this.mKTVBookTableRequest = null;
      this.hasRequested = true;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
        this.ktvTable = paramMApiRequest;
      dispatchAgentChanged(false);
    }
  }

  public void sendRequest()
  {
    if (!this.hasRequested)
    {
      if (this.mKTVBookTableRequest != null)
        getFragment().mapiService().abort(this.mKTVBookTableRequest, this, true);
      Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/fun/shopdetailktvbooktable.fn").buildUpon().appendQueryParameter("shopid", String.valueOf(shopId()));
      String str = accountService().token();
      if (!TextUtils.isEmpty(str))
        localBuilder.appendQueryParameter("token", str);
      this.mKTVBookTableRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      getFragment().mapiService().exec(this.mKTVBookTableRequest, this);
      this.hasRequested = true;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.fun.KTVBookTableAgent
 * JD-Core Version:    0.6.0
 */