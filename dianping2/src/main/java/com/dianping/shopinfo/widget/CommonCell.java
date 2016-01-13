package com.dianping.shopinfo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;

public class CommonCell extends NovaLinearLayout
{
  private NetworkImageView icon;
  private NetworkImageView rightIcon;
  private TextView rightText;
  private TextView subTitle;
  private TextView title;

  public CommonCell(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public TextView getRightText()
  {
    return this.rightText;
  }

  public CharSequence getSubtitle()
  {
    if (this.subTitle == null)
      return "";
    return this.subTitle.getText();
  }

  public CharSequence getTitle()
  {
    return this.title.getText();
  }

  public TextView getTitleView()
  {
    return this.title;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.title = ((TextView)findViewById(16908308));
    this.subTitle = ((TextView)findViewById(16908309));
    this.rightText = ((TextView)findViewById(R.id.text3));
    this.icon = ((NetworkImageView)findViewById(16908295));
    this.rightIcon = ((NetworkImageView)findViewById(16908296));
  }

  public void setArrowIconVisibility(boolean paramBoolean)
  {
    View localView = findViewById(R.id.arrow);
    if (!paramBoolean);
    for (int i = 0; ; i = 4)
    {
      localView.setVisibility(i);
      return;
    }
  }

  public void setLeftIcon(int paramInt)
  {
    if (paramInt > 0)
    {
      this.icon.setVisibility(0);
      this.icon.setLocalDrawable(getResources().getDrawable(paramInt));
      return;
    }
    this.icon.setVisibility(8);
  }

  public void setLeftIconUrl(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      this.icon.setVisibility(0);
      this.icon.setImage(paramString);
      return;
    }
    this.icon.setVisibility(8);
  }

  public void setRightIcon(int paramInt)
  {
    if (paramInt > 0)
    {
      this.rightIcon.setVisibility(0);
      this.rightIcon.setLocalDrawable(getResources().getDrawable(paramInt));
      return;
    }
    this.rightIcon.setVisibility(8);
  }

  public void setRightIconUrl(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      this.rightIcon.setVisibility(0);
      this.rightIcon.setImage(paramString);
      return;
    }
    this.rightIcon.setVisibility(8);
  }

  public void setRightText(CharSequence paramCharSequence)
  {
    if (this.rightText != null)
    {
      this.rightText.setVisibility(0);
      this.rightText.setText(paramCharSequence);
    }
  }

  public void setRightText(String paramString)
  {
    if (this.rightText != null)
    {
      this.rightText.setVisibility(0);
      this.rightText.setText(paramString);
    }
  }

  public void setRightTextcolor(int paramInt)
  {
    if (this.rightText != null)
      this.rightText.setTextColor(paramInt);
  }

  public void setSubTitle(CharSequence paramCharSequence)
  {
    TextView localTextView;
    if (this.subTitle != null)
    {
      this.subTitle.setText(paramCharSequence);
      localTextView = this.subTitle;
      if (paramCharSequence != null)
        break label33;
    }
    label33: for (int i = 8; ; i = 0)
    {
      localTextView.setVisibility(i);
      return;
    }
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    this.title.setText(paramCharSequence);
  }

  public void setTitleLineSpacing(float paramFloat)
  {
    this.title.setLineSpacing(ViewUtils.dip2px(getContext(), paramFloat), 1.0F);
  }

  public void setTitleMaxLines(int paramInt)
  {
    if (paramInt <= 1)
    {
      this.title.setSingleLine(true);
      return;
    }
    this.title.setSingleLine(false);
    this.title.setMaxLines(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.CommonCell
 * JD-Core Version:    0.6.0
 */