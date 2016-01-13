package com.dianping.shopinfo.movie.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.DateUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.Date;

public class MovieShowScheduleListItemView extends NovaLinearLayout
{
  private final String RMB = "￥";
  private DPObject dpMovieShow;
  private View huiImg;
  private TextView tvDefaultDiscountPrice;
  private TextView tvEndTime;
  private TextView tvHallName;
  private TextView tvMovieType;
  private TextView tvPrice;
  private TextView tvStartTime;
  private TextView tvTicketBook;

  public MovieShowScheduleListItemView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieShowScheduleListItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private SpannableString getLittleAmountSpannableString(Context paramContext, String paramString)
  {
    paramString = new SpannableString(paramString);
    paramString.setSpan(new AbsoluteSizeSpan(paramContext.getResources().getDimensionPixelSize(R.dimen.text_very_small)), 0, 1, 33);
    return paramString;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.tvStartTime = ((TextView)findViewById(R.id.start_time));
    this.tvEndTime = ((TextView)findViewById(R.id.end_time));
    this.tvMovieType = ((TextView)findViewById(R.id.movie_type));
    this.tvHallName = ((TextView)findViewById(R.id.hall_name));
    this.tvDefaultDiscountPrice = ((TextView)findViewById(R.id.ticket_defaultdiscountprice));
    this.tvPrice = ((TextView)findViewById(R.id.ticket_price));
    this.tvTicketBook = ((TextView)findViewById(R.id.ticket_book));
    this.huiImg = findViewById(R.id.hui_img);
  }

  public void setScheduleListItemView(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.dpMovieShow = paramDPObject;
    paramDPObject = DateUtils.formatDate2TimeZone(new Date(this.dpMovieShow.getTime("ShowTime")), "HH:mm", "GMT+8");
    if (!TextUtils.isEmpty(paramDPObject))
      this.tvStartTime.setText(paramDPObject);
    paramDPObject = DateUtils.formatDate2TimeZone(new Date(this.dpMovieShow.getTime("ShowEndTime")), "HH:mm", "GMT+8");
    if (!TextUtils.isEmpty(paramDPObject))
    {
      paramDPObject = paramDPObject + "结束";
      this.tvEndTime.setText(paramDPObject);
    }
    paramDPObject = this.dpMovieShow.getString("Language");
    String str = this.dpMovieShow.getString("Dimensional");
    Object localObject = null;
    if ((!TextUtils.isEmpty(paramDPObject)) && (!TextUtils.isEmpty(str)))
    {
      paramDPObject = paramDPObject + "/" + str;
      if (!TextUtils.isEmpty(paramDPObject))
        this.tvMovieType.setText(paramDPObject);
      paramDPObject = this.dpMovieShow.getString("HallName");
      if (!TextUtils.isEmpty(paramDPObject))
        this.tvHallName.setText(paramDPObject);
      paramDPObject = this.dpMovieShow.getString("DefaultDiscountPrice");
      str = this.dpMovieShow.getString("Price");
      if (TextUtils.isEmpty(paramDPObject))
        break label516;
      if (TextUtils.isEmpty(str))
        break label462;
      this.tvDefaultDiscountPrice.setText(getLittleAmountSpannableString(getContext(), "￥" + paramDPObject));
      this.tvDefaultDiscountPrice.setVisibility(0);
      this.tvPrice.setText(getLittleAmountSpannableString(getContext(), "￥" + str));
      this.tvPrice.getPaint().setFlags(16);
      this.tvPrice.getPaint().setAntiAlias(true);
      this.tvPrice.setVisibility(0);
      label349: int i = this.dpMovieShow.getInt("BuyTicketButtonStatus");
      str = this.dpMovieShow.getString("BuyTicketButtonText");
      paramDPObject = str;
      if (TextUtils.isEmpty(str))
        paramDPObject = "选座";
      this.tvTicketBook.setText(paramDPObject);
      switch (i)
      {
      default:
      case 2:
      case 1:
      }
    }
    while (true)
    {
      if (1 != this.dpMovieShow.getInt("MovieShowStatus"))
        break label629;
      this.huiImg.setVisibility(0);
      return;
      if (!TextUtils.isEmpty(paramDPObject))
        break;
      paramDPObject = localObject;
      if (TextUtils.isEmpty(str))
        break;
      paramDPObject = str;
      break;
      label462: this.tvDefaultDiscountPrice.setText(getLittleAmountSpannableString(getContext(), "￥" + paramDPObject));
      this.tvDefaultDiscountPrice.setVisibility(0);
      this.tvPrice.setVisibility(8);
      break label349;
      label516: if (!TextUtils.isEmpty(str))
      {
        this.tvDefaultDiscountPrice.setText(getLittleAmountSpannableString(getContext(), "￥" + str));
        this.tvDefaultDiscountPrice.setVisibility(0);
        this.tvPrice.setVisibility(8);
        break label349;
      }
      this.tvDefaultDiscountPrice.setVisibility(8);
      this.tvPrice.setVisibility(8);
      break label349;
      this.tvTicketBook.setVisibility(8);
      continue;
      this.tvTicketBook.setClickable(false);
      this.tvTicketBook.setEnabled(false);
    }
    label629: this.huiImg.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.view.MovieShowScheduleListItemView
 * JD-Core Version:    0.6.0
 */