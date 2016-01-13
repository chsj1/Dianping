package com.dianping.shopinfo.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.v1.R.id;

public class TuanTicketCell extends CommonCell
{
  private TextView saleCount;

  public TuanTicketCell(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.saleCount = ((TextView)findViewById(R.id.deal_sale_count));
  }

  public void setSaleCount(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      this.saleCount.setVisibility(0);
      this.saleCount.setText(paramString);
      return;
    }
    this.saleCount.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.TuanTicketCell
 * JD-Core Version:    0.6.0
 */