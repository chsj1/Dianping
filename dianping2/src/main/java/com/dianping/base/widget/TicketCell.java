package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class TicketCell extends NovaLinearLayout
{
  public static final int BOTTOM = 16;
  public static final int LEFT = 1;
  public static final int RIGHT = 4;
  public static final int TOP = 8;
  private ImageView icon;
  private Drawable mark;
  private int markAlign;
  private ImageView rightIcon;
  private TextView rightText;
  private TextView subTitle;
  private TextView title;

  public TicketCell(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    int k;
    int m;
    int i;
    int j;
    if (this.mark != null)
    {
      k = this.mark.getIntrinsicWidth();
      m = this.mark.getIntrinsicHeight();
      if ((this.markAlign & 0x1) == 0)
        break label79;
      i = 0;
      if ((this.markAlign & 0x8) == 0)
        break label112;
      j = 0;
    }
    while (true)
    {
      this.mark.setBounds(i, j, i + k, j + m);
      this.mark.draw(paramCanvas);
      return;
      label79: if ((this.markAlign & 0x4) != 0)
      {
        i = getWidth() - k;
        break;
      }
      i = (getWidth() - k) / 2;
      break;
      label112: if ((this.markAlign & 0x10) != 0)
      {
        j = getHeight() - m;
        continue;
      }
      j = (getHeight() - m) / 2;
    }
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
    this.icon = ((ImageView)findViewById(16908295));
    this.rightIcon = ((ImageView)findViewById(16908296));
  }

  public void setIcon(Drawable paramDrawable)
  {
    this.icon.setImageDrawable(paramDrawable);
    ImageView localImageView = this.icon;
    if (paramDrawable == null);
    for (int i = 8; ; i = 0)
    {
      localImageView.setVisibility(i);
      return;
    }
  }

  public void setMark(Drawable paramDrawable, int paramInt)
  {
    this.mark = paramDrawable;
    this.markAlign = paramInt;
    invalidate();
  }

  public void setRightIcon(Drawable paramDrawable)
  {
    ImageView localImageView;
    if (this.rightIcon != null)
    {
      this.rightIcon.setImageDrawable(paramDrawable);
      localImageView = this.rightIcon;
      if (paramDrawable != null)
        break label33;
    }
    label33: for (int i = 8; ; i = 0)
    {
      localImageView.setVisibility(i);
      return;
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

  public void setSubtitle(CharSequence paramCharSequence)
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
 * Qualified Name:     com.dianping.base.widget.TicketCell
 * JD-Core Version:    0.6.0
 */