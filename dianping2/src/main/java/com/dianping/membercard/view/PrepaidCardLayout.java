package com.dianping.membercard.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrepaidCardLayout extends NovaLinearLayout
{
  TextView balance;
  TextView cardInfo;
  LinearLayout cardLayout;
  PrepaidCardItem mCardInfoView;
  PrepaidCardNearestShop nearestShop;
  MApiRequest request;
  TextView validityPeriod;

  public PrepaidCardLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public PrepaidCardLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mCardInfoView = ((PrepaidCardItem)findViewById(R.id.container));
    this.cardInfo = ((TextView)findViewById(R.id.cardInfo));
    this.balance = ((TextView)findViewById(R.id.balance));
    this.validityPeriod = ((TextView)findViewById(R.id.validityPeriod));
    this.nearestShop = ((PrepaidCardNearestShop)findViewById(R.id.nearestShop));
    this.cardLayout = ((LinearLayout)findViewById(R.id.cardInfoLayout));
    this.nearestShop.setVisibility(8);
  }

  public void setData(DPObject paramDPObject)
  {
    this.mCardInfoView.setData(paramDPObject);
    this.cardInfo.setText(paramDPObject.getString("ProductName"));
    if (!TextUtils.isEmpty(paramDPObject.getString("Balance")))
      this.balance.setText(paramDPObject.getString("Balance"));
    long l = paramDPObject.getTime("ValidateDate");
    String str = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(new Date(l));
    if (paramDPObject.getInt("Status") == 1)
      this.validityPeriod.setText("有效期至" + str);
    while (true)
    {
      this.cardLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          DPApplication.instance().statisticsEvent("paidcardinfo5", "paidcardinfo5_detail", "", 0);
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://prepaidcardconsumelist?prepaidcardid=" + this.val$data.getInt("PrepaidCardID") + "&accountid=" + this.val$data.getInt("AccountID") + "&title=" + this.val$data.getString("Title")));
          PrepaidCardLayout.this.getContext().startActivity(paramView);
        }
      });
      return;
      this.validityPeriod.setText("已过期");
    }
  }

  public void setNearestShop(int paramInt, DPObject paramDPObject, double paramDouble1, double paramDouble2)
  {
    this.nearestShop.setDealShop(paramInt, paramDPObject, paramDouble1, paramDouble2);
  }

  public void updateUserNameOnly(DPObject paramDPObject)
  {
    this.mCardInfoView.updateUserNameOnly(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.PrepaidCardLayout
 * JD-Core Version:    0.6.0
 */