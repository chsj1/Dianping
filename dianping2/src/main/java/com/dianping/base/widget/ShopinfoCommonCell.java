package com.dianping.base.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;

public class ShopinfoCommonCell extends NovaLinearLayout
{
  private View mArrowView;
  private LinearLayout mContentLay;
  private ImageView mIconView;
  private View mMiddleDivderLine;
  private TextView mSubTitleView;
  private TextView mTitleView;
  public NovaRelativeLayout titleLay;

  public ShopinfoCommonCell(Context paramContext)
  {
    super(paramContext);
  }

  public ShopinfoCommonCell(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public View addContent(View paramView, boolean paramBoolean, View.OnClickListener paramOnClickListener)
  {
    if (this.mContentLay != null)
    {
      NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_common_cell_item_layout, this.mContentLay, false);
      localNovaRelativeLayout.setBackgroundResource(R.drawable.home_listview_bg);
      ((FrameLayout)localNovaRelativeLayout.findViewById(R.id.item_content)).addView(paramView);
      if (this.mContentLay.getChildCount() > 0)
      {
        localNovaRelativeLayout.findViewById(R.id.divider_line).setVisibility(0);
        if (!paramBoolean)
          break label154;
        localNovaRelativeLayout.findViewById(R.id.indicator).setVisibility(0);
      }
      while (true)
      {
        if (paramOnClickListener != null)
          localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramOnClickListener, paramView)
          {
            public void onClick(View paramView)
            {
              this.val$listener.onClick(this.val$view);
            }
          });
        if (paramView.getTag() != null)
          localNovaRelativeLayout.setTag(paramView.getTag());
        this.mContentLay.addView(localNovaRelativeLayout);
        return localNovaRelativeLayout;
        localNovaRelativeLayout.findViewById(R.id.divider_line).setVisibility(8);
        break;
        label154: localNovaRelativeLayout.findViewById(R.id.indicator).setVisibility(8);
      }
    }
    return null;
  }

  public void addContent(View paramView, boolean paramBoolean)
  {
    addContent(paramView, paramBoolean, null);
  }

  public NovaRelativeLayout getTitleLay()
  {
    return this.titleLay;
  }

  public void hideArrow()
  {
    if (this.mArrowView != null)
      this.mArrowView.setVisibility(8);
  }

  public void hideTitle()
  {
    if (this.titleLay != null)
      this.titleLay.setVisibility(8);
    if (this.mMiddleDivderLine != null)
      this.mMiddleDivderLine.setVisibility(8);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTitleView = ((TextView)findViewById(R.id.title));
    this.mSubTitleView = ((TextView)findViewById(R.id.sub_title));
    this.mContentLay = ((LinearLayout)findViewById(R.id.content));
    this.titleLay = ((NovaRelativeLayout)findViewById(R.id.title_layout));
    this.mIconView = ((ImageView)findViewById(R.id.icon));
    this.mMiddleDivderLine = findViewById(R.id.middle_divder_line);
    this.mArrowView = findViewById(R.id.indicator);
  }

  public void setBlankContent(boolean paramBoolean)
  {
    if (this.mMiddleDivderLine != null)
    {
      if (paramBoolean)
        this.mMiddleDivderLine.setVisibility(8);
    }
    else
      return;
    this.mMiddleDivderLine.setVisibility(0);
  }

  public void setHotelSubTitleStyle()
  {
    this.mTitleView.setSingleLine(false);
    this.mTitleView.setLineSpacing(14.0F, 1.0F);
    this.titleLay.setLayoutParams(new LinearLayout.LayoutParams(-2, ViewUtils.dip2px(getContext(), 60.0F)));
  }

  public void setIcon(int paramInt)
  {
    if (this.mIconView != null)
    {
      this.mIconView.setImageResource(paramInt);
      this.mIconView.setVisibility(0);
    }
  }

  public void setSubTitle(String paramString)
  {
    if (this.mSubTitleView != null)
    {
      if (TextUtils.isEmpty(paramString))
        this.mSubTitleView.setVisibility(8);
    }
    else
      return;
    this.mSubTitleView.setText(paramString);
    this.mSubTitleView.setVisibility(0);
  }

  public void setTitle(String paramString)
  {
    setTitle(paramString, null);
  }

  public void setTitle(String paramString, View.OnClickListener paramOnClickListener)
  {
    if (this.mTitleView != null)
      this.mTitleView.setText(paramString);
    if (paramOnClickListener != null)
      this.titleLay.setOnClickListener(paramOnClickListener);
  }

  public void setTitle(String paramString1, View.OnClickListener paramOnClickListener, String paramString2, GAUserInfo paramGAUserInfo)
  {
    if (this.mTitleView != null)
      this.mTitleView.setText(paramString1);
    if (paramOnClickListener != null)
      this.titleLay.setOnClickListener(paramOnClickListener);
    if (!TextUtils.isEmpty(paramString2))
      this.titleLay.setGAString(paramString2, paramGAUserInfo);
  }

  public void setTitleSpannable(SpannableString paramSpannableString, View.OnClickListener paramOnClickListener)
  {
    if (this.mTitleView != null)
      this.mTitleView.setText(paramSpannableString);
    if (paramOnClickListener != null)
      this.titleLay.setOnClickListener(paramOnClickListener);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ShopinfoCommonCell
 * JD-Core Version:    0.6.0
 */