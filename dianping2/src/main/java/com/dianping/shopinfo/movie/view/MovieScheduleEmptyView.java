package com.dianping.shopinfo.movie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.util.DateUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.Date;

public class MovieScheduleEmptyView extends NovaLinearLayout
  implements View.OnClickListener
{
  private final String DATE_FORMAT_MONTH_DAY = "M月d日";
  private Button btnMovieScheduleEmptyNextAvailableDate;
  private NextAvailableDateListener listener;
  private TextView tvMovieScheduleEmptyHint;

  public MovieScheduleEmptyView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieScheduleEmptyView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    this.listener.onClickNextAvailableDate();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.tvMovieScheduleEmptyHint = ((TextView)findViewById(R.id.movieschedule_empty_hint));
    this.btnMovieScheduleEmptyNextAvailableDate = ((Button)findViewById(R.id.movieschedule_empty_nextavailabledate));
    this.btnMovieScheduleEmptyNextAvailableDate.setOnClickListener(this);
  }

  public void setMovieScheduleEmpty(Date[] paramArrayOfDate)
  {
    if (paramArrayOfDate.length == 1)
    {
      this.btnMovieScheduleEmptyNextAvailableDate.setVisibility(8);
      return;
    }
    this.btnMovieScheduleEmptyNextAvailableDate.setVisibility(0);
    paramArrayOfDate = DateUtils.formatDate2TimeZone(paramArrayOfDate[1], "M月d日", "GMT+8");
    this.btnMovieScheduleEmptyNextAvailableDate.setText("点击查看" + paramArrayOfDate + "场次");
  }

  public void setOnClickTryNextDateListener(NextAvailableDateListener paramNextAvailableDateListener)
  {
    this.listener = paramNextAvailableDateListener;
  }

  public static abstract interface NextAvailableDateListener
  {
    public abstract void onClickNextAvailableDate();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.view.MovieScheduleEmptyView
 * JD-Core Version:    0.6.0
 */