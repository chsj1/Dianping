package com.dianping.membercard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaRelativeLayout;

public class CouponEntryView extends NovaRelativeLayout
{
  private OnCouponEntryViewClickListener mListener;
  private TextView mTvCountNumber;
  private TextView mTvTitle;

  public CouponEntryView(Context paramContext)
  {
    super(paramContext);
  }

  public CouponEntryView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public CouponEntryView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private void initEvents()
  {
    setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        CouponEntryView.this.invokeClickListenr();
      }
    });
  }

  private void invokeClickListenr()
  {
    if (this.mListener == null)
      return;
    this.mListener.couponEntryViewClick();
  }

  public void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTvTitle = ((TextView)findViewById(R.id.coupon_title_textview));
    this.mTvCountNumber = ((TextView)findViewById(R.id.coupon_count_textview));
    initEvents();
  }

  public void setListener(OnCouponEntryViewClickListener paramOnCouponEntryViewClickListener)
  {
    this.mListener = paramOnCouponEntryViewClickListener;
  }

  public void updateCouponEntry(String paramString1, String paramString2)
  {
    this.mTvCountNumber.setText(paramString2);
    this.mTvTitle.setText(paramString1);
  }

  public static abstract interface OnCouponEntryViewClickListener
  {
    public abstract void couponEntryViewClick();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.CouponEntryView
 * JD-Core Version:    0.6.0
 */