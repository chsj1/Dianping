package com.dianping.hotel.shopinfo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;

@SuppressLint({"NewApi"})
public class TogetherRoomTypeListItem extends LinearLayout
{
  public static final String RMB = "￥";
  Activity activity;
  Context context;
  private long endtime;
  private Button hotel_roomtype_listitem_button_book;
  public NetworkImageView hotel_roomtype_listitem_icon;
  private LinearLayout hotel_roomtype_listitem_layout_special;
  private TextView hotel_roomtype_listitem_tag;
  private TextView hotel_roomtype_listitem_text_paypolicy;
  private TextView hotel_roomtype_listitem_text_price;
  private TextView hotel_roomtype_listitem_text_refund;
  private TextView hotel_roomtype_listitem_text_roomtinfo;
  private TextView hotel_roomtype_listitem_text_roomtypeextinfo;
  private int middlewidth;
  private String otaid;
  private String shopid;
  private long starttime;

  public TogetherRoomTypeListItem(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
  }

  public TogetherRoomTypeListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  public TogetherRoomTypeListItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.context = paramContext;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.hotel_roomtype_listitem_icon = ((NetworkImageView)findViewById(R.id.hotel_roomtype_listitem_icon));
    this.hotel_roomtype_listitem_text_price = ((TextView)findViewById(R.id.hotel_roomtype_listitem_text_price));
    this.hotel_roomtype_listitem_text_refund = ((TextView)findViewById(R.id.hotel_roomtype_listitem_text_refund));
    this.hotel_roomtype_listitem_button_book = ((Button)findViewById(R.id.hotel_roomtype_listitem_button_book));
    this.hotel_roomtype_listitem_text_paypolicy = ((TextView)findViewById(R.id.hotel_roomtype_listitem_text_paypolicy));
    this.hotel_roomtype_listitem_text_roomtinfo = ((TextView)findViewById(R.id.hotel_roomtype_listitem_text_roomtinfo));
    this.hotel_roomtype_listitem_text_roomtypeextinfo = ((TextView)findViewById(R.id.hotel_roomtype_listitem_text_roomtypeextinfo));
    this.hotel_roomtype_listitem_tag = ((TextView)findViewById(R.id.hotel_roomtype_listitem_tag));
    this.hotel_roomtype_listitem_layout_special = ((LinearLayout)findViewById(R.id.hotel_roomtype_listitem_layout_special));
  }

  public void setParams(String paramString1, long paramLong1, long paramLong2, String paramString2)
  {
    this.shopid = paramString1;
    this.starttime = paramLong1;
    this.endtime = paramLong2;
    this.otaid = paramString2;
  }

  public void setRoomTypeInfo(HotelTogetherRoomTypeListActivity.RoomTypeInfo paramRoomTypeInfo, String paramString)
  {
    this.hotel_roomtype_listitem_text_price.setText(paramRoomTypeInfo.priceText);
    this.hotel_roomtype_listitem_text_refund.setText(paramRoomTypeInfo.reFund);
    Object localObject1 = paramRoomTypeInfo.roomImage;
    if (!TextUtils.isEmpty((CharSequence)localObject1))
      this.hotel_roomtype_listitem_icon.setImage((String)localObject1);
    int i = paramRoomTypeInfo.remains;
    int j = paramRoomTypeInfo.status;
    label134: label160: label227: Object localObject2;
    if (i == -1)
    {
      this.hotel_roomtype_listitem_tag.setBackgroundResource(R.drawable.hotel_list_icon_rest);
      this.hotel_roomtype_listitem_tag.setVisibility(0);
      this.hotel_roomtype_listitem_tag.setText("紧张");
      if (j == 0)
        break label633;
      this.hotel_roomtype_listitem_button_book.setText("预订");
      this.hotel_roomtype_listitem_button_book.setEnabled(true);
      this.hotel_roomtype_listitem_button_book.setTag(new String[] { paramRoomTypeInfo.bookingUrl, paramRoomTypeInfo.payPolicy });
      localObject1 = paramRoomTypeInfo.roomInfo;
      if ((localObject1 == null) || ("".equals(localObject1)))
        break label654;
      this.hotel_roomtype_listitem_text_roomtinfo.setText((CharSequence)localObject1);
      localObject1 = paramRoomTypeInfo.payPolicy;
      if ((localObject1 != null) && (!"".equals(localObject1)))
        break label666;
      localObject1 = " " + paramString;
      paramString = new SpannableString((CharSequence)localObject1);
      paramString.setSpan(new ForegroundColorSpan(-16777216), 1, ((String)localObject1).length(), 33);
      this.hotel_roomtype_listitem_text_paypolicy.setText(paramString);
      paramString = paramRoomTypeInfo.roomTypeExtInfo;
      if ((paramString == null) || ("".equals(paramString)))
        break label730;
      this.hotel_roomtype_listitem_text_roomtypeextinfo.setText(paramString);
      label261: this.hotel_roomtype_listitem_layout_special.removeAllViews();
      this.hotel_roomtype_listitem_layout_special.setOrientation(1);
      paramString = paramRoomTypeInfo.specialActivaties;
      if ((paramString == null) || (paramString.length <= 0))
        return;
      paramRoomTypeInfo = new LinearLayout(this.activity);
      localObject1 = new LinearLayout.LayoutParams(-1, -2);
      ((LinearLayout.LayoutParams)localObject1).setMargins(0, 0, 0, ViewUtils.dip2px(this.activity, 2.0F));
      paramRoomTypeInfo.setOrientation(0);
      this.hotel_roomtype_listitem_layout_special.addView(paramRoomTypeInfo, (ViewGroup.LayoutParams)localObject1);
      i = 0;
      label345: if (i >= paramString.length)
        return;
      paramRoomTypeInfo.measure(0, 0);
      j = paramRoomTypeInfo.getMeasuredWidth();
      localObject1 = new TextView(this.activity);
      ((TextView)localObject1).setText(paramString[i]);
      ((TextView)localObject1).setTextColor(getResources().getColor(R.color.hotel_roomlist_promo_color));
      ((TextView)localObject1).setTextSize(2, 12.0F);
      ((TextView)localObject1).setBackgroundResource(R.drawable.background_blue_border);
      ((TextView)localObject1).setSingleLine();
      localObject2 = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject2).setMargins(0, 0, ViewUtils.dip2px(this.activity, 5.0F), 0);
      ((TextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      ((TextView)localObject1).measure(0, 0);
      int k = ((TextView)localObject1).getMeasuredWidth();
      if ((this.middlewidth - j >= k) || (paramRoomTypeInfo.getChildCount() <= 0))
        break label742;
      paramRoomTypeInfo = new LinearLayout(this.activity);
      localObject2 = new LinearLayout.LayoutParams(-1, -2);
      ((LinearLayout.LayoutParams)localObject2).setMargins(0, 0, 0, ViewUtils.dip2px(this.activity, 2.0F));
      paramRoomTypeInfo.setOrientation(0);
      this.hotel_roomtype_listitem_layout_special.addView(paramRoomTypeInfo, (ViewGroup.LayoutParams)localObject2);
      paramRoomTypeInfo.addView((View)localObject1);
    }
    while (true)
    {
      i += 1;
      break label345;
      if ((i >= 1) && (i <= 5))
      {
        this.hotel_roomtype_listitem_tag.setBackgroundResource(R.drawable.hotel_list_icon_rest);
        this.hotel_roomtype_listitem_tag.setText("剩" + i + "间");
        this.hotel_roomtype_listitem_tag.setVisibility(0);
        break;
      }
      this.hotel_roomtype_listitem_tag.setVisibility(8);
      break;
      label633: this.hotel_roomtype_listitem_button_book.setText("满房");
      this.hotel_roomtype_listitem_button_book.setEnabled(false);
      break label134;
      label654: this.hotel_roomtype_listitem_text_roomtinfo.setVisibility(8);
      break label160;
      label666: localObject2 = (String)localObject1 + " " + paramString;
      paramString = new SpannableString((CharSequence)localObject2);
      paramString.setSpan(new ForegroundColorSpan(-16777216), ((String)localObject1).length() + 1, ((String)localObject2).length(), 33);
      break label227;
      label730: this.hotel_roomtype_listitem_text_roomtypeextinfo.setVisibility(8);
      break label261;
      label742: paramRoomTypeInfo.addView((View)localObject1);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shopinfo.activity.TogetherRoomTypeListItem
 * JD-Core Version:    0.6.0
 */