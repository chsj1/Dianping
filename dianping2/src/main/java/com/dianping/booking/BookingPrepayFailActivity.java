package com.dianping.booking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class BookingPrepayFailActivity extends NovaActivity
  implements View.OnClickListener
{
  private Button btnBackToShop;
  private Button btnRebook;
  private int shopId;
  private String shopName;

  private void gotoBooking()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://onlinebooking?shopid=" + this.shopId + "&shopname=" + this.shopName));
    localIntent.setFlags(67108864);
    startActivity(localIntent);
    finish();
  }

  private void gotoShopInfo()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + this.shopId));
    localIntent.setFlags(67108864);
    super.startActivity(localIntent);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.btnBackToShop)
      gotoShopInfo();
    do
      return;
    while (paramView != this.btnRebook);
    gotoBooking();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.booking_prepay_fail);
    super.getWindow().setBackgroundDrawable(null);
    this.shopId = getIntParam("shopid");
    this.shopName = getStringParam("shopname");
    ((TextView)super.findViewById(R.id.fail_title)).setText(getStringParam("title"));
    ((TextView)super.findViewById(R.id.fail_desc)).setText(getStringParam("desc"));
    this.btnBackToShop = ((Button)super.findViewById(R.id.back_to_shop));
    this.btnBackToShop.setOnClickListener(this);
    this.btnRebook = ((Button)super.findViewById(R.id.rebook));
    this.btnRebook.setOnClickListener(this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingPrepayFailActivity
 * JD-Core Version:    0.6.0
 */