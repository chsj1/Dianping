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
import com.dianping.membercard.MembersOnlyActivity;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaFrameLayout;

public class MemberCardListItem extends NovaFrameLayout
{
  protected Drawable backgroundDrawable;
  public ImageView cardVipView;
  protected LinearLayout itemLayout;
  protected MemberCardImageView mCardImage;
  protected int mFontColor;
  protected NetworkImageView mIcon;
  protected TextView mSubTitle;
  protected TextView mTitle;
  protected String title;

  public MemberCardListItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public MemberCardListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
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
    this.backgroundDrawable = getBackground();
    this.itemLayout = ((LinearLayout)findViewById(R.id.itemlayout));
  }

  public void setData(DPObject paramDPObject)
  {
    if (paramDPObject == null)
    {
      setBackgroundResource(R.drawable.main_background);
      this.mCardImage.setVisibility(4);
      this.mIcon.setVisibility(4);
      this.mTitle.setVisibility(4);
      this.mSubTitle.setVisibility(4);
      return;
    }
    setBackgroundDrawable(this.backgroundDrawable);
    this.mCardImage.setVisibility(0);
    this.mIcon.setVisibility(0);
    this.mTitle.setVisibility(0);
    this.mSubTitle.setVisibility(0);
    try
    {
      this.mFontColor = Color.parseColor(paramDPObject.getString("FontColor"));
      if (this.mCardImage != null)
        this.mCardImage.setImage(paramDPObject.getString("BgImage"));
      this.mIcon.setImage(paramDPObject.getString("Logo"));
      this.mTitle.setVisibility(0);
      this.mTitle.setTextColor(this.mFontColor);
      this.title = paramDPObject.getString("Title");
      this.mTitle.setText(paramDPObject.getString("Title"));
      this.mSubTitle.setTextColor(this.mFontColor);
      String str = paramDPObject.getString("MemberCardGroupID");
      if (TextUtils.isEmpty(paramDPObject.getString("SubTitle")))
      {
        this.mSubTitle.setText("");
        this.mSubTitle.setVisibility(8);
        if (MemberCard.isThirdPartyCard(paramDPObject))
          this.mSubTitle.setVisibility(8);
        if (paramDPObject.getInt("UserCardLevel") != 2)
          break label362;
        showCardVipIcon();
        invalidate();
        return;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        this.mFontColor = getResources().getColor(17170443);
        continue;
        if ((localException != null) && (!TextUtils.isEmpty(localException)))
        {
          this.mSubTitle.setVisibility(0);
          if ((getContext() instanceof MembersOnlyActivity))
          {
            this.mSubTitle.setText(paramDPObject.getString("SubTitle"));
            continue;
          }
          this.mSubTitle.setText("");
          this.mSubTitle.setVisibility(8);
          continue;
        }
        this.mSubTitle.setVisibility(0);
        this.mSubTitle.setText(paramDPObject.getString("SubTitle"));
        continue;
        label362: hideCardVipIcon();
      }
    }
  }

  public void setData(DPObject paramDPObject, boolean paramBoolean)
  {
    this.mCardImage.setPrepaidCard(paramBoolean);
    setData(paramDPObject);
  }

  public void showCardVipIcon()
  {
    if (this.itemLayout != null)
      this.itemLayout.setPadding(this.itemLayout.getPaddingLeft(), this.itemLayout.getPaddingTop(), ViewUtils.dip2px(getContext(), 61.0F), this.itemLayout.getPaddingBottom());
    this.cardVipView.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.MemberCardListItem
 * JD-Core Version:    0.6.0
 */