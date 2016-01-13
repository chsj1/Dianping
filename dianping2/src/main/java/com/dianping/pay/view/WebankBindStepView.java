package com.dianping.pay.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.TextView;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class WebankBindStepView extends NovaLinearLayout
{
  public static final int STEP_ONE = 1;
  public static final int STEP_TWO = 2;
  protected ImageButton imageStepOne;
  protected ImageButton imageStepThree;
  protected ImageButton imageStepTwo;
  protected TextView labelTwo;

  public WebankBindStepView(Context paramContext)
  {
    this(paramContext, null);
  }

  public WebankBindStepView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    inflate(paramContext, R.layout.pay_webank_step_view, this);
    initView();
  }

  protected void initView()
  {
    this.imageStepOne = ((ImageButton)findViewById(R.id.webank_step_one));
    this.imageStepTwo = ((ImageButton)findViewById(R.id.webank_step_two));
    this.imageStepThree = ((ImageButton)findViewById(R.id.webank_step_three));
    this.labelTwo = ((TextView)findViewById(R.id.label_two));
  }

  public void setStep(int paramInt)
  {
    if (1 == paramInt)
    {
      this.imageStepOne.setImageResource(R.drawable.pay_webank_bind_one_done);
      this.imageStepTwo.setImageResource(R.drawable.pay_webank_bind_two_normal);
      this.imageStepThree.setImageResource(R.drawable.pay_webank_bind_three_normal);
      this.labelTwo.setTextColor(getResources().getColor(R.color.light_gray));
    }
    do
      return;
    while (2 != paramInt);
    this.imageStepOne.setImageResource(R.drawable.pay_webank_bind_one_normal);
    this.imageStepTwo.setImageResource(R.drawable.pay_webank_bind_two_done);
    this.imageStepThree.setImageResource(R.drawable.pay_webank_bind_three_normal);
    this.labelTwo.setTextColor(getResources().getColor(R.color.btn_weight_normal));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.view.WebankBindStepView
 * JD-Core Version:    0.6.0
 */