package com.dianping.hotel.tuan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HotelBookingResultActivity extends NovaActivity
{
  Button button;
  int couponId;

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onBackPressed()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuanmain"));
    localIntent.addFlags(67108864);
    startActivity(localIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.couponId = getIntent().getIntExtra("couponid", 0);
    setContentView(R.layout.hotel_booking_result);
    getTitleBar().setLeftView(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuanmain"));
        paramView.addFlags(67108864);
        HotelBookingResultActivity.this.startActivity(paramView);
      }
    });
    this.button = ((Button)findViewById(R.id.button));
    this.button.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://coupondetail?couponid=" + HotelBookingResultActivity.this.couponId));
        HotelBookingResultActivity.this.startActivity(paramView);
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.tuan.activity.HotelBookingResultActivity
 * JD-Core Version:    0.6.0
 */