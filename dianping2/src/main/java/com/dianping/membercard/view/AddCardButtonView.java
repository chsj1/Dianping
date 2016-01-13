package com.dianping.membercard.view;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaFrameLayout;

public class AddCardButtonView extends NovaFrameLayout
{
  private NovaButton mAddButton;
  private TextView mBigText;
  private RelativeLayout mButtonContainer;
  private FrameLayout mChildFrameLayout;
  private TextView mGrayText;
  private TextView mSmallText;

  public AddCardButtonView(Context paramContext)
  {
    super(paramContext);
    setupView(paramContext);
  }

  public AddCardButtonView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setupView(paramContext);
  }

  private void setupView(Context paramContext)
  {
    this.mChildFrameLayout = ((FrameLayout)LayoutInflater.from(getContext()).inflate(R.layout.mc_noadded_card_button, this, true));
    this.mButtonContainer = ((RelativeLayout)this.mChildFrameLayout.findViewById(R.id.view_card_button_container));
    this.mAddButton = ((NovaButton)this.mChildFrameLayout.findViewById(R.id.submit));
    this.mAddButton.setGAString("dpcard_add_button");
    setGAString(getClass().getSimpleName());
    this.mBigText = ((TextView)this.mChildFrameLayout.findViewById(R.id.view_card_text));
    this.mSmallText = ((TextView)this.mChildFrameLayout.findViewById(R.id.view_card_text_small));
    this.mGrayText = ((TextView)this.mChildFrameLayout.findViewById(R.id.view_card_text_gray));
    this.mGrayText.getPaint().setFlags(16);
  }

  public RelativeLayout addButtonView()
  {
    this.mChildFrameLayout.addView(this.mButtonContainer);
    return this.mButtonContainer;
  }

  public RelativeLayout getButtonView()
  {
    return this.mButtonContainer;
  }

  public RelativeLayout removeButtonView()
  {
    ((FrameLayout)this.mButtonContainer.getParent()).removeView(this.mButtonContainer);
    return this.mButtonContainer;
  }

  public AddCardButtonView setAddButtonText(CharSequence paramCharSequence)
  {
    this.mAddButton.setText(paramCharSequence);
    return this;
  }

  public AddCardButtonView setChargeCardText(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    this.mAddButton.setText("立即抢购");
    this.mBigText.setText("¥" + paramCharSequence1);
    this.mGrayText.setText("¥" + paramCharSequence2);
    return this;
  }

  public AddCardButtonView setDiscountText(CharSequence paramCharSequence)
  {
    this.mBigText.setText(paramCharSequence);
    this.mSmallText.setVisibility(0);
    return this;
  }

  public AddCardButtonView setFreePresent()
  {
    this.mBigText.setText("享好礼");
    return this;
  }

  public AddCardButtonView setMallAndBrandProduct()
  {
    this.mBigText.setText("享优惠");
    return this;
  }

  public AddCardButtonView setMallScoreProduct()
  {
    this.mBigText.setText("享积分");
    return this;
  }

  public void setOnAddButtonClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mAddButton.setOnClickListener(paramOnClickListener);
  }

  public void setProduct(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    switch (1.$SwitchMap$com$dianping$membercard$utils$ProductType[com.dianping.membercard.utils.ProductType.valueOf(paramDPObject).ordinal()])
    {
    default:
      return;
    case 1:
    case 2:
      setChargeCardText(paramDPObject.getString("Price"), paramDPObject.getString("OldPrice"));
      return;
    case 3:
      paramDPObject = paramDPObject.getString("Price");
      if (paramDPObject == "0")
      {
        setFreePresent();
        return;
      }
      setDiscountText(paramDPObject);
      return;
    case 4:
      setFreePresent();
      return;
    case 5:
    }
    setScoreCardText();
  }

  public AddCardButtonView setScoreCardText()
  {
    this.mBigText.setText("享积分");
    return this;
  }

  public AddCardButtonView setThirdPartyTypeProduct()
  {
    this.mBigText.setText("");
    return this;
  }

  public void updateGAuserinfo(GAUserInfo paramGAUserInfo)
  {
    this.gaUserInfo = paramGAUserInfo;
    this.mAddButton.gaUserInfo = paramGAUserInfo;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.AddCardButtonView
 * JD-Core Version:    0.6.0
 */