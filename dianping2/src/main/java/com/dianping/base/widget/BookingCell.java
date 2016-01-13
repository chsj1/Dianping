package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class BookingCell extends NovaLinearLayout
{
  private TextView hotTag;
  private ImageView icDiscount;
  private TextView icOff;
  private ImageView icReward;
  private TextView title;

  public BookingCell(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.title = ((TextView)findViewById(16908308));
    this.hotTag = ((TextView)findViewById(R.id.hot_tag));
    this.icReward = ((ImageView)findViewById(R.id.ic_reward));
    this.icDiscount = ((ImageView)findViewById(R.id.ic_discount));
    this.icOff = ((TextView)findViewById(R.id.ic_off));
  }

  public void setDiscount()
  {
    this.icDiscount.setVisibility(0);
  }

  public void setHotTag(boolean paramBoolean)
  {
    TextView localTextView = this.hotTag;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localTextView.setVisibility(i);
      return;
    }
  }

  public void setOff(String paramString)
  {
    this.icOff.setVisibility(0);
    this.icOff.setText(paramString);
  }

  public void setReward()
  {
    this.icReward.setVisibility(0);
  }

  public void setTitle(String paramString)
  {
    this.title.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.BookingCell
 * JD-Core Version:    0.6.0
 */