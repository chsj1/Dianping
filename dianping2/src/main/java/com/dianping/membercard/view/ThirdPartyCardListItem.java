package com.dianping.membercard.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaFrameLayout;

public class ThirdPartyCardListItem extends NovaFrameLayout
{
  protected Drawable backgroundDrawable;
  protected DPObject cardData;
  protected NetworkImageView cardGrayFloatView;
  public ImageView cardVipView;
  protected LinearLayout itemLayout;
  protected MemberCardImageView mCardImage;
  protected int mFontColor;
  protected NetworkImageView mIcon;
  protected TextView mSubTitle;
  protected TextView mTitle;
  protected String title;

  public ThirdPartyCardListItem(Context paramContext)
  {
    super(paramContext);
  }

  public ThirdPartyCardListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void updateBackgroundContents()
  {
    if (this.cardData == null)
    {
      setBackgroundResource(R.drawable.main_background);
      return;
    }
    setBackgroundDrawable(this.backgroundDrawable);
  }

  private void updateCardIconContents()
  {
    if (this.cardData == null)
    {
      this.mIcon.setVisibility(4);
      return;
    }
    this.mIcon.setVisibility(0);
    this.mIcon.setImage(this.cardData.getString("Logo"));
  }

  private void updateCardImageContents()
  {
    if (this.cardData == null)
    {
      this.mCardImage.setVisibility(4);
      return;
    }
    this.mCardImage.setVisibility(0);
    this.mCardImage.setImage(this.cardData.getString("BgImage"));
  }

  private void updateMaskViewContents()
  {
    if (this.cardData == null)
    {
      this.cardGrayFloatView.setVisibility(4);
      return;
    }
    String str = this.cardData.getString("MaskPicUrl");
    if (TextUtils.isEmpty(str))
    {
      this.cardGrayFloatView.setVisibility(4);
      return;
    }
    this.cardGrayFloatView.setVisibility(0);
    this.cardGrayFloatView.setImage(str);
  }

  private void updateTitleContents()
  {
    this.mSubTitle.setVisibility(8);
    if (this.cardData == null)
    {
      this.mTitle.setVisibility(4);
      return;
    }
    this.mTitle.setVisibility(0);
    this.title = this.cardData.getString("Title");
    try
    {
      this.mFontColor = Color.parseColor(this.cardData.getString("FontColor"));
      this.mTitle.setTextColor(this.mFontColor);
      this.mTitle.setText(this.title);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        this.mFontColor = getResources().getColor(17170443);
    }
  }

  private void updateVIPIconContents()
  {
    if (this.cardData == null)
      return;
    if (this.cardData.getInt("UserCardLevel") == 2)
    {
      showCardVipIcon();
      return;
    }
    hideCardVipIcon();
  }

  public void hideCardVipIcon()
  {
    this.cardVipView.setVisibility(4);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mCardImage = ((MemberCardImageView)findViewById(R.id.card_image));
    this.mIcon = ((NetworkImageView)findViewById(R.id.card_icon));
    this.mTitle = ((TextView)findViewById(R.id.card_title));
    this.mSubTitle = ((TextView)findViewById(R.id.card_subtitle));
    this.cardVipView = ((ImageView)findViewById(R.id.card_vip_icon));
    this.cardGrayFloatView = ((NetworkImageView)findViewById(R.id.card_float_view));
    this.backgroundDrawable = getBackground();
    this.itemLayout = ((LinearLayout)findViewById(R.id.itemlayout));
  }

  public void setData(DPObject paramDPObject)
  {
    this.cardData = paramDPObject;
    this.mCardImage.setPrepaidCard(false);
    updateBackgroundContents();
    updateCardImageContents();
    updateCardIconContents();
    updateTitleContents();
    updateMaskViewContents();
    updateVIPIconContents();
    invalidate();
  }

  public void showCardVipIcon()
  {
    if (this.itemLayout != null)
      this.itemLayout.setPadding(this.itemLayout.getPaddingLeft(), this.itemLayout.getPaddingTop(), ViewUtils.dip2px(getContext(), 61.0F), this.itemLayout.getPaddingBottom());
    this.cardVipView.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.ThirdPartyCardListItem
 * JD-Core Version:    0.6.0
 */