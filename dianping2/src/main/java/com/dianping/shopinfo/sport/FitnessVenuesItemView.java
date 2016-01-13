package com.dianping.shopinfo.sport;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.RichTextView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class FitnessVenuesItemView extends NovaLinearLayout
{
  private DPObject dpSceduleShow;
  private RichTextView tvPrice;
  private TextView tvStartTime;
  private RichTextView tvStock;

  public FitnessVenuesItemView(Context paramContext)
  {
    super(paramContext);
  }

  public FitnessVenuesItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.tvStartTime = ((TextView)findViewById(R.id.start_time));
    this.tvPrice = ((RichTextView)findViewById(R.id.price));
    this.tvStock = ((RichTextView)findViewById(R.id.stock));
  }

  public void setFitnessVenuesItemView(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    String str2;
    do
    {
      return;
      this.dpSceduleShow = paramDPObject;
      paramDPObject = this.dpSceduleShow.getString("StartTime");
      String str1 = this.dpSceduleShow.getString("Price");
      str2 = this.dpSceduleShow.getString("StockStatus");
      if (!TextUtils.isEmpty(paramDPObject))
        this.tvStartTime.setText(paramDPObject);
      if (TextUtils.isEmpty(str1))
        continue;
      this.tvPrice.setRichText(str1);
    }
    while (TextUtils.isEmpty(str2));
    this.tvStock.setRichText(str2);
    this.tvStock.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.sport.FitnessVenuesItemView
 * JD-Core Version:    0.6.0
 */