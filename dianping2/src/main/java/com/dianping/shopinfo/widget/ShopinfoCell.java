package com.dianping.shopinfo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;

@Deprecated
public class ShopinfoCell extends NovaLinearLayout
{
  private NetworkImageView image;
  private TextView subTitle;
  private TextView title;

  public ShopinfoCell(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.title = ((TextView)findViewById(R.id.title));
    this.subTitle = ((TextView)findViewById(R.id.subtitle));
    this.image = ((NetworkImageView)findViewById(R.id.icon));
  }

  public void setDetailIconVisibility(boolean paramBoolean)
  {
    View localView = findViewById(R.id.arrow);
    if (!paramBoolean);
    for (int i = 0; ; i = 4)
    {
      localView.setVisibility(i);
      return;
    }
  }

  public void setLeftIconUrl(int paramInt)
  {
    if (!TextUtils.isEmpty(Integer.toString(paramInt)))
    {
      this.image.setVisibility(0);
      this.image.setLocalDrawable(getResources().getDrawable(paramInt));
      return;
    }
    this.image.setVisibility(8);
  }

  public void setLeftIconUrl(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      this.image.setVisibility(0);
      this.image.setImage(paramString);
      return;
    }
    this.image.setVisibility(8);
  }

  public void setSubTitle(String paramString)
  {
    this.subTitle.setText(paramString);
  }

  public void setTitle(SpannableStringBuilder paramSpannableStringBuilder)
  {
    this.title.setText(paramSpannableStringBuilder);
  }

  public void setTitle(String paramString)
  {
    this.title.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.ShopinfoCell
 * JD-Core Version:    0.6.0
 */