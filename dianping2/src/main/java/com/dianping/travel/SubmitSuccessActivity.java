package com.dianping.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class SubmitSuccessActivity extends NovaActivity
  implements View.OnClickListener
{
  private static final int REQUEST_CODE_VIEW_ORDER = 1;
  private int orderID;

  private void onBack()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo"));
    localIntent.setFlags(67108864);
    localIntent.addFlags(536870912);
    startActivity(localIntent);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 1) && (paramInt2 == -1))
      onBack();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.viewOrder)
    {
      startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://sceneryorder?orderid=" + this.orderID)), 1);
      statisticsEvent("ticket5", "ticket5_result_vieworder", "", 0);
    }
    do
      return;
    while (paramView.getId() != R.id.backToShop);
    onBack();
    statisticsEvent("ticket5", "ticket5_result_shopinfo", "", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.submit_success);
    paramBundle = (TextView)findViewById(R.id.orderNum);
    TextView localTextView = (TextView)findViewById(R.id.notice);
    ((Button)findViewById(R.id.viewOrder)).setOnClickListener(this);
    ((Button)findViewById(R.id.backToShop)).setOnClickListener(this);
    DPObject localDPObject = (DPObject)getIntent().getParcelableExtra("successMsg");
    if (localDPObject != null)
    {
      paramBundle.setText("订单号：" + localDPObject.getString("Title"));
      localTextView.setText(localDPObject.getString("Content"));
      this.orderID = localDPObject.getInt("OrderId");
    }
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      onBack();
      statisticsEvent("ticket5", "ticket5_result_back", "key_back", 0);
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onLeftTitleButtonClicked()
  {
    onBack();
    statisticsEvent("ticket5", "ticket5_result_back", "left_title_button", 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.SubmitSuccessActivity
 * JD-Core Version:    0.6.0
 */