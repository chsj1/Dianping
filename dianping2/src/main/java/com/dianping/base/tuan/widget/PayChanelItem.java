package com.dianping.base.tuan.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;

public class PayChanelItem extends FrameLayout
{
  private DPObject dpPaymentTool;
  private ColorBorderTextView highlightTitleView;
  private ImageView ivLogo;
  private FrameLayout layerPayIcon;
  private NetworkImageView nivLogo;
  private RadioButton radioButton;
  private TextView titleView;

  public PayChanelItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public PayChanelItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    inflate(paramContext, R.layout.pay_chanel_item, this);
    setup();
  }

  private void setLogo()
  {
    if (this.dpPaymentTool == null);
    do
    {
      return;
      str1 = this.dpPaymentTool.getString("ID");
    }
    while (TextUtils.isEmpty(str1));
    Object localObject = str1.split(":");
    String str1 = "";
    if (localObject.length >= 2)
      str1 = localObject[0] + ":" + localObject[1];
    int i = 0;
    String str2 = "";
    if ("11:1".equals(str1))
    {
      i = R.drawable.logo_weixinpay;
      localObject = str2;
    }
    while ((i == 0) && (TextUtils.isEmpty((CharSequence)localObject)))
    {
      this.layerPayIcon.setVisibility(4);
      return;
      if (("2:3".equals(str1)) || ("2:6".equals(str1)) || ("7:3".equals(str1)))
      {
        int j = R.drawable.logo_bankpay;
        localObject = str2;
        i = j;
        if (this.dpPaymentTool.getString("IconUrl") == null)
          continue;
        localObject = this.dpPaymentTool.getString("IconUrl");
        i = j;
        continue;
      }
      if ("5:1".equals(str1))
      {
        i = R.drawable.logo_alipay_app;
        localObject = str2;
        continue;
      }
      if ("3:1".equals(str1))
      {
        i = R.drawable.logo_alipay;
        localObject = str2;
        continue;
      }
      if ("3:3".equals(str1))
      {
        i = R.drawable.logo_alipay;
        localObject = str2;
        continue;
      }
      localObject = str2;
      if (!"22:1".equals(str1))
        continue;
      i = R.drawable.logo_qq;
      localObject = str2;
    }
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      this.nivLogo.setImage((String)localObject);
      this.nivLogo.setVisibility(0);
      this.ivLogo.setVisibility(8);
    }
    while (true)
    {
      this.layerPayIcon.setVisibility(0);
      return;
      if (i <= 0)
        continue;
      this.ivLogo.setImageResource(i);
      this.ivLogo.setVisibility(0);
      this.nivLogo.setVisibility(8);
    }
  }

  private void setup()
  {
    this.titleView = ((TextView)findViewById(R.id.title));
    this.highlightTitleView = ((ColorBorderTextView)findViewById(R.id.lighttitle));
    this.radioButton = ((RadioButton)findViewById(R.id.radioBtn));
    this.layerPayIcon = ((FrameLayout)findViewById(R.id.layer_pay_icon));
    this.ivLogo = ((ImageView)findViewById(R.id.iv_icon));
    this.nivLogo = ((NetworkImageView)findViewById(R.id.niv_icon));
    if (!isInEditMode())
    {
      this.titleView.setText("");
      this.highlightTitleView.setText("");
      this.radioButton.setChecked(false);
      this.layerPayIcon.setVisibility(4);
    }
  }

  public void setChecked(boolean paramBoolean)
  {
    this.radioButton.setChecked(paramBoolean);
  }

  public void setPaymentTool(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.dpPaymentTool = paramDPObject;
    this.titleView.setText(paramDPObject.getString("Title"));
    paramDPObject = paramDPObject.getString("HighlightTitle");
    this.highlightTitleView.setText(paramDPObject);
    this.highlightTitleView.setBorderColor("#ff6633");
    if (TextUtils.isEmpty(paramDPObject))
      this.highlightTitleView.setVisibility(8);
    while (true)
    {
      setLogo();
      return;
      this.highlightTitleView.setVisibility(0);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.widget.PayChanelItem
 * JD-Core Version:    0.6.0
 */