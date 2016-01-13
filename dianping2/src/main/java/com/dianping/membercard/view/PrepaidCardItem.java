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
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.v1.R.id;

public class PrepaidCardItem extends MemberCardListItem
  implements View.OnClickListener
{
  private DPObject cardObj;
  private Animation fadeInAnimation;
  private Animation fadeOutAnimation = new AlphaAnimation(1.0F, 0.0F);
  protected TextView mMemberName;
  protected TextView mMemberNumber;
  protected View qrcode;

  public PrepaidCardItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public PrepaidCardItem(Context paramContext, AttributeSet paramAttributeSet)
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

  public void onClick(View paramView)
  {
    gotoQrCodeActivity();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mMemberName = ((TextView)findViewById(R.id.member_name));
    this.mMemberNumber = ((TextView)findViewById(R.id.member_number));
  }

  public void setData(DPObject paramDPObject)
  {
    super.setData(paramDPObject);
    this.cardObj = paramDPObject;
    String str1 = paramDPObject.getString("UserName");
    String str2 = paramDPObject.getString("CardNo");
    paramDPObject = str1;
    if (str1 == null)
      paramDPObject = "姓名";
    this.mMemberName.setText(paramDPObject);
    this.mMemberName.setTextColor(this.mFontColor);
    this.mMemberNumber.setText("NO.  " + str2);
    this.mMemberNumber.setTextColor(this.mFontColor);
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
 * Qualified Name:     com.dianping.membercard.view.PrepaidCardItem
 * JD-Core Version:    0.6.0
 */