package com.dianping.wed.baby.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class WeddingBookingSuccessActivity extends NovaActivity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.activity_wedding_reservation_success);
    paramBundle = getIntent().getData();
    if (paramBundle != null)
    {
      paramBundle = paramBundle.getQueryParameter("shopname");
      paramBundle = "恭喜您已成功预约：\n" + paramBundle;
      ((TextView)findViewById(R.id.congratulation)).setText(paramBundle);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.WeddingBookingSuccessActivity
 * JD-Core Version:    0.6.0
 */