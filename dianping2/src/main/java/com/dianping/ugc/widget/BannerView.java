package com.dianping.ugc.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.base.app.NovaApplication;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;

public class BannerView extends RelativeLayout
{
  private String mText;
  private ImageView removeButton;
  private TextView textView;

  public BannerView(Context paramContext)
  {
    super(paramContext);
    initViews();
  }

  public BannerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initViews();
  }

  private void initViews()
  {
    setBackgroundResource(R.drawable.background_banner);
    enableRemoveStyle(true);
  }

  public static boolean shouldShow(String paramString)
  {
    int i = 0;
    if (!NovaApplication.instance().getSharedPreferences("banner", 0).getBoolean(paramString, false))
      i = 1;
    return i;
  }

  public void enableRemoveStyle(boolean paramBoolean)
  {
    if (this.removeButton == null)
    {
      this.removeButton = new ImageView(getContext());
      this.removeButton.setImageResource(R.drawable.banner_close);
      this.removeButton.setPadding(ViewUtils.dip2px(getContext(), 15.0F), ViewUtils.dip2px(getContext(), 15.0F), ViewUtils.dip2px(getContext(), 15.0F), ViewUtils.dip2px(getContext(), 15.0F));
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
      localLayoutParams.addRule(11);
      localLayoutParams.addRule(15);
      addView(this.removeButton, localLayoutParams);
    }
    if (!paramBoolean)
    {
      removeView(this.removeButton);
      this.removeButton = null;
      return;
    }
    this.removeButton.setOnClickListener(new BannerView.2(this));
  }

  public void setText(String paramString)
  {
    if (this.textView == null)
    {
      this.textView = new TextView(getContext());
      this.textView.setClickable(false);
      this.textView.setTextColor(getResources().getColor(R.color.review_banner_text));
      this.textView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
      localLayoutParams.addRule(9);
      localLayoutParams.addRule(15);
      localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
      addView(this.textView, localLayoutParams);
    }
    this.mText = paramString;
    this.textView.setText(paramString);
  }

  public void setUrl(String paramString)
  {
    setOnClickListener(new BannerView.1(this, paramString));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.widget.BannerView
 * JD-Core Version:    0.6.0
 */