package com.dianping.membercard.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.membercard.MembersOnlyActivity;
import com.dianping.membercard.utils.MemberCard;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;

public class MemberCardItem extends MemberCardListItem
  implements View.OnClickListener, LoginResultListener
{
  private DPObject cardObj;
  protected TextView cardShopName;
  private int cardType;
  private Animation fadeInAnimation;
  private Animation fadeOutAnimation = new AlphaAnimation(1.0F, 0.0F);
  private boolean isCustBgImage;
  protected TextView mMemberName;
  protected TextView mMemberNumber;
  private boolean needLogin = false;
  protected View qrcode;

  public MemberCardItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public MemberCardItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.fadeOutAnimation.setDuration(300L);
    this.fadeInAnimation = new AlphaAnimation(0.0F, 1.0F);
    this.fadeInAnimation.setDuration(300L);
  }

  private void gotoQrCodeActivity()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://mymcqrcode?membercardid=" + this.cardObj.getInt("MemberCardID")));
    getContext().startActivity(localIntent);
  }

  private void updateChildViewStatus(int paramInt)
  {
    if (paramInt == 2)
      showCardVipIcon();
    while ((this.cardType == 1) && (!this.isCustBgImage))
    {
      this.mTitle.setVisibility(0);
      this.mIcon.setVisibility(0);
      this.cardShopName.setVisibility(4);
      return;
      hideCardVipIcon();
    }
    this.mTitle.setVisibility(4);
    this.mSubTitle.setVisibility(4);
    this.mIcon.setVisibility(4);
    this.cardShopName.setVisibility(0);
    this.cardShopName.setTextColor(this.mFontColor);
    if (TextUtils.isEmpty(this.cardObj.getString("SubTitle")))
    {
      this.cardShopName.setText("");
      this.cardShopName.setVisibility(4);
      return;
    }
    this.cardShopName.setPadding(ViewUtils.dip2px(getContext(), 40.0F), this.cardShopName.getPaddingTop(), this.cardShopName.getPaddingRight(), this.cardShopName.getPaddingBottom());
    String str = this.cardObj.getString("MemberCardGroupID");
    if (MemberCard.isThirdPartyCard(this.cardObj))
    {
      this.cardShopName.setText("");
      this.cardShopName.setVisibility(4);
      return;
    }
    if ((str != null) && (!TextUtils.isEmpty(str)))
    {
      this.cardShopName.setVisibility(0);
      if ((getContext() instanceof MembersOnlyActivity))
      {
        this.cardShopName.setText(this.cardObj.getString("SubTitle"));
        return;
      }
      this.cardShopName.setText("");
      this.cardShopName.setVisibility(4);
      return;
    }
    this.cardShopName.setVisibility(0);
    this.cardShopName.setText(this.cardObj.getString("SubTitle"));
  }

  public void gotoOtherShopList()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("dianping://shopidlist?ids=" + this.cardObj.getString("ShopIDs")));
    getContext().startActivity(localIntent);
  }

  public void hideCardVipIcon()
  {
    super.hideCardVipIcon();
  }

  public void onClick(View paramView)
  {
    paramView = ((DPActivity)getContext()).accountService();
    if (paramView.profile() == null)
    {
      paramView.login(this);
      this.needLogin = true;
      return;
    }
    gotoQrCodeActivity();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mMemberName = ((TextView)findViewById(R.id.member_name));
    this.mMemberNumber = ((TextView)findViewById(R.id.member_number));
    this.cardShopName = ((TextView)findViewById(R.id.card_shop_name));
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    if (this.needLogin)
    {
      this.needLogin = false;
      gotoQrCodeActivity();
    }
  }

  public void setData(DPObject paramDPObject)
  {
    super.setData(paramDPObject);
    this.cardObj = paramDPObject;
    String str2 = paramDPObject.getString("UserName");
    String str3 = paramDPObject.getString("MemberCardNO");
    String str1 = str2;
    if (str2 == null)
      str1 = "姓名";
    this.mMemberName.setText(str1);
    this.mMemberName.setTextColor(this.mFontColor);
    this.mMemberNumber.setText("NO.  " + str3);
    this.mMemberNumber.setTextColor(this.mFontColor);
    this.cardType = paramDPObject.getInt("CardType");
    this.isCustBgImage = paramDPObject.getBoolean("IsCustBgImage");
    updateChildViewStatus(paramDPObject.getInt("UserCardLevel"));
  }

  public void showCardVipIcon()
  {
    super.showCardVipIcon();
  }

  public void updateUserNameOnly(DPObject paramDPObject)
  {
    if ((paramDPObject == null) || (this.mMemberName == null))
      return;
    this.cardObj = paramDPObject;
    String str = paramDPObject.getString("UserName");
    paramDPObject = str;
    if (TextUtils.isEmpty(str))
      paramDPObject = "姓名";
    this.mMemberName.setText(paramDPObject);
  }

  public void updateUserNameOnly(String paramString)
  {
    if ((this.cardObj == null) || (this.mMemberName == null))
      return;
    this.cardObj = this.cardObj.edit().putString("UserName", paramString).generate();
    String str = paramString;
    if (TextUtils.isEmpty(paramString))
      str = "姓名";
    this.mMemberName.setText(str);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.MemberCardItem
 * JD-Core Version:    0.6.0
 */