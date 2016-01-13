package com.dianping.shopinfo.education.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;

public class EduBookingDialog extends Dialog
  implements View.OnClickListener, TextWatcher, Animation.AnimationListener
{
  TranslateAnimation bottomInAnimation;
  TranslateAnimation bottomOutAnimation;
  NovaButton btnBooking;
  EditText editPhone;
  LinearLayout layoutFrame;
  LinearLayout layoutPromo;
  OnEduBookingDialogClickListener listener;

  public EduBookingDialog(Context paramContext)
  {
    super(paramContext, 16973840);
    super.setContentView(R.layout.edu_shopinfo_booking_dialog);
    this.layoutPromo = ((LinearLayout)findViewById(R.id.linearlayout_edu_booking_dialog_promo));
    this.btnBooking = ((NovaButton)findViewById(R.id.button_edu_booking_dialog));
    this.editPhone = ((EditText)findViewById(R.id.edittext_edu_booking_dialog));
    this.editPhone.addTextChangedListener(this);
    this.layoutFrame = ((LinearLayout)findViewById(R.id.linearlayout_edu_dialog_frame));
    findViewById(R.id.framelayout_edu_dialog).setOnClickListener(this);
    this.btnBooking.setGAString("edu_booking_suc", "");
    this.btnBooking.setOnClickListener(this);
    this.bottomInAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
    this.bottomInAnimation.setDuration(500L);
    this.bottomInAnimation.setAnimationListener(this);
    this.bottomOutAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, 1.0F);
    this.bottomOutAnimation.setDuration(500L);
    this.bottomOutAnimation.setAnimationListener(this);
  }

  public void afterTextChanged(Editable paramEditable)
  {
    if (this.editPhone.getText().toString().length() == 0)
      this.editPhone.setHintTextColor(getContext().getResources().getColor(R.color.text_hint_light_gray));
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void initDialog(String paramString1, DPObject[] paramArrayOfDPObject, String paramString2)
  {
    if (!TextUtils.isEmpty(paramString1))
      this.btnBooking.setText(paramString1);
    if ((paramString2 != null) && (paramString2.trim().length() > 0))
      this.editPhone.setText(paramString2);
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      this.layoutPromo.setVisibility(8);
    int j;
    int i;
    do
    {
      return;
      j = 0;
      i = 0;
      int k = 0;
      if (k < paramArrayOfDPObject.length)
      {
        paramString2 = paramArrayOfDPObject[k];
        paramString1 = paramString2.getString("Title");
        int n;
        int m;
        if (paramString1.equals("预约礼"))
        {
          paramString2 = paramString2.getString("Content");
          if (!TextUtils.isEmpty(paramString2))
            j = 1;
          ((TextView)findViewById(R.id.textview_edu_promo_gift_01)).setText(paramString1);
          ((TextView)findViewById(R.id.textview_edu_promo_gift_des_01)).setText(paramString2);
          n = i;
          m = j;
        }
        while (true)
        {
          k += 1;
          j = m;
          i = n;
          break;
          m = j;
          n = i;
          if (!paramString1.equals("到店礼"))
            continue;
          paramString2 = paramString2.getString("Content");
          if (!TextUtils.isEmpty(paramString2))
            i = 1;
          ((TextView)findViewById(R.id.textview_edu_promo_gift_02)).setText(paramString1);
          ((TextView)findViewById(R.id.textview_edu_promo_gift_des_02)).setText(paramString2);
          m = j;
          n = i;
        }
      }
      if ((j == 0) && (i == 0))
      {
        this.layoutPromo.setVisibility(8);
        return;
      }
      if ((j != 0) || (i == 0))
        continue;
      findViewById(R.id.linearlayout_edu_promo_gift_01).setVisibility(8);
      findViewById(R.id.linearlayout_edu_promo_gift_02).setVisibility(0);
      return;
    }
    while ((j == 0) || (i != 0));
    findViewById(R.id.linearlayout_edu_promo_gift_02).setVisibility(8);
    findViewById(R.id.linearlayout_edu_promo_gift_01).setVisibility(0);
  }

  public void onAnimationEnd(Animation paramAnimation)
  {
    if (this.bottomOutAnimation == paramAnimation)
      dismiss();
  }

  public void onAnimationRepeat(Animation paramAnimation)
  {
  }

  public void onAnimationStart(Animation paramAnimation)
  {
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.framelayout_edu_dialog)
      this.layoutFrame.startAnimation(this.bottomOutAnimation);
    do
    {
      do
        return;
      while (paramView.getId() != R.id.button_edu_booking_dialog);
      if (TextUtils.isEmpty(this.editPhone.getText().toString()))
      {
        this.editPhone.setHintTextColor(getContext().getResources().getColor(R.color.red));
        return;
      }
      if (this.editPhone.getText().toString().length() != 11)
      {
        Toast.makeText(getContext(), "请填写正确的手机号", 0).show();
        return;
      }
      dismiss();
    }
    while (this.listener == null);
    this.listener.onEduBookingDialogClick(this.editPhone.getText().toString());
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void setOnEduBookingDialogClickListener(OnEduBookingDialogClickListener paramOnEduBookingDialogClickListener)
  {
    this.listener = paramOnEduBookingDialogClickListener;
  }

  public void show()
  {
    getWindow().setSoftInputMode(18);
    super.show();
    this.layoutFrame.startAnimation(this.bottomInAnimation);
  }

  public static abstract interface OnEduBookingDialogClickListener
  {
    public abstract void onEduBookingDialogClick(String paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.view.EduBookingDialog
 * JD-Core Version:    0.6.0
 */