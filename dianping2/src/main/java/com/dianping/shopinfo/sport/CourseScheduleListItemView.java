package com.dianping.shopinfo.sport;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.base.widget.RichTextView;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class CourseScheduleListItemView extends NovaLinearLayout
{
  private DPObject dpSceduleShow;
  private LinearLayout promoContainer;
  private TextView tvEndTime;
  private TextView tvHallName;
  private RichTextView tvPrice;
  private TextView tvScheduleType;
  private TextView tvStartTime;
  private RichTextView tvState;

  public CourseScheduleListItemView(Context paramContext)
  {
    super(paramContext);
  }

  public CourseScheduleListItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private ColorBorderTextView buildProductPromoView(String paramString)
  {
    ColorBorderTextView localColorBorderTextView = new ColorBorderTextView(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.setMargins(5, 0, 0, 0);
    localColorBorderTextView.setLayoutParams(localLayoutParams);
    localColorBorderTextView.setBackgroundResource(R.drawable.background_round_textview_lightred);
    localColorBorderTextView.setEllipsize(TextUtils.TruncateAt.END);
    localColorBorderTextView.setGravity(16);
    localColorBorderTextView.setSingleLine(true);
    localColorBorderTextView.setTextColor(getContext().getResources().getColor(R.color.light_red));
    localColorBorderTextView.setTextSize(2, 11.0F);
    localColorBorderTextView.setText(paramString);
    return localColorBorderTextView;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.tvStartTime = ((TextView)findViewById(R.id.start_time));
    this.tvEndTime = ((TextView)findViewById(R.id.end_time));
    this.tvScheduleType = ((TextView)findViewById(R.id.schedule_type));
    this.tvHallName = ((TextView)findViewById(R.id.hall_name));
    this.tvPrice = ((RichTextView)findViewById(R.id.price));
    this.tvState = ((RichTextView)findViewById(R.id.state));
    this.promoContainer = ((LinearLayout)findViewById(R.id.promo_container));
  }

  public void setScheduleListItemView(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    String[] arrayOfString;
    do
    {
      return;
      this.dpSceduleShow = paramDPObject;
      paramDPObject = this.dpSceduleShow.getString("StartTime");
      String str1 = this.dpSceduleShow.getString("EndTime");
      String str2 = this.dpSceduleShow.getString("Name");
      String str3 = this.dpSceduleShow.getString("SubName");
      String str4 = this.dpSceduleShow.getString("Price");
      String str5 = this.dpSceduleShow.getString("StockStatus");
      arrayOfString = this.dpSceduleShow.getStringArray("PromoList");
      if (!TextUtils.isEmpty(paramDPObject))
        this.tvStartTime.setText(paramDPObject);
      if (!TextUtils.isEmpty(str1))
        this.tvEndTime.setText(str1);
      if (!TextUtils.isEmpty(str2))
        this.tvScheduleType.setText(str2);
      if (!TextUtils.isEmpty(str3))
        this.tvHallName.setText(str3);
      if (!TextUtils.isEmpty(str5))
      {
        this.tvState.setRichText(str5);
        this.tvState.setVisibility(0);
      }
      if (TextUtils.isEmpty(str4))
        continue;
      this.tvPrice.setRichText(str4);
    }
    while ((arrayOfString == null) || (arrayOfString.length <= 0));
    this.promoContainer.addView(buildProductPromoView(arrayOfString[0]));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.sport.CourseScheduleListItemView
 * JD-Core Version:    0.6.0
 */