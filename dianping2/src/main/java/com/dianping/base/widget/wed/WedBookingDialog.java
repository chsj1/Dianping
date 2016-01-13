package com.dianping.base.widget.wed;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;

public class WedBookingDialog extends Dialog
  implements View.OnClickListener, TextWatcher, Animation.AnimationListener
{
  TranslateAnimation bottomInAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
  TranslateAnimation bottomOutAnimation;
  Button btnBooking;
  EditText editPhone;
  LinearLayout layoutFrame;
  LinearLayout layoutPromo;
  OnWedBookingDialogClickListener listener;

  public WedBookingDialog(Context paramContext)
  {
    super(paramContext, 16973840);
    this.bottomInAnimation.setDuration(500L);
    this.bottomInAnimation.setAnimationListener(this);
    this.bottomOutAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, 1.0F);
    this.bottomOutAnimation.setDuration(500L);
    this.bottomOutAnimation.setAnimationListener(this);
  }

  public void afterTextChanged(Editable paramEditable)
  {
    if ((this.editPhone != null) && (this.editPhone.getText().toString().length() == 0))
      this.editPhone.setHintTextColor(getContext().getResources().getColor(R.color.text_hint_light_gray));
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void initDialog(String paramString, DPObject[] paramArrayOfDPObject, DPObject paramDPObject)
  {
    setContentView(R.layout.wed_shopinfo_booking_dialog);
    this.layoutPromo = ((LinearLayout)findViewById(R.id.linearlayout_wed_booking_dialog_promo));
    this.btnBooking = ((Button)findViewById(R.id.button_wed_booking_dialog));
    this.editPhone = ((EditText)findViewById(R.id.edittext_wed_booking_dialog));
    this.editPhone.addTextChangedListener(this);
    this.layoutFrame = ((LinearLayout)findViewById(R.id.linearlayout_wed_dialog_frame));
    findViewById(R.id.framelayout_wed_dialog).setOnClickListener(this);
    this.btnBooking.setOnClickListener(this);
    if (!TextUtils.isEmpty(paramString))
      this.btnBooking.setText(paramString);
    if (paramDPObject != null)
    {
      paramString = paramDPObject.getString("BookingUserMobile");
      if ((paramString != null) && (paramString.trim().length() > 0))
        this.editPhone.setText(paramString);
    }
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      this.layoutPromo.setVisibility(8);
    int k;
    int i;
    do
    {
      return;
      k = 0;
      i = 0;
      int j = 0;
      if (j < paramArrayOfDPObject.length)
      {
        paramDPObject = paramArrayOfDPObject[j];
        paramString = paramDPObject.getString("Title");
        int n;
        int m;
        if (paramString.equals("订单礼"))
        {
          paramDPObject = paramDPObject.getString("Content");
          if (TextUtils.isEmpty(paramDPObject))
          {
            n = i;
            m = k;
          }
        }
        while (true)
        {
          j += 1;
          k = m;
          i = n;
          break;
          m = 1;
          ((TextView)findViewById(R.id.textview_wed_promo_gift_01)).setText(paramString);
          ((TextView)findViewById(R.id.textview_wed_promo_gift_des_01)).setText(paramDPObject);
          n = i;
          continue;
          m = k;
          n = i;
          if (!paramString.equals("到店礼"))
            continue;
          paramDPObject = paramDPObject.getString("Content");
          m = k;
          n = i;
          if (TextUtils.isEmpty(paramDPObject))
            continue;
          n = 1;
          ((TextView)findViewById(R.id.textview_wed_promo_gift_02)).setText(paramString);
          ((TextView)findViewById(R.id.textview_wed_promo_gift_des_02)).setText(paramDPObject);
          m = k;
        }
      }
      if ((k == 0) && (i == 0))
      {
        this.layoutPromo.setVisibility(8);
        return;
      }
      if ((k != 0) || (i == 0))
        continue;
      findViewById(R.id.linearlayout_wed_promo_gift_01).setVisibility(8);
      findViewById(R.id.linearlayout_wed_promo_gift_02).setVisibility(0);
      return;
    }
    while ((k == 0) || (i != 0));
    findViewById(R.id.linearlayout_wed_promo_gift_02).setVisibility(8);
    findViewById(R.id.linearlayout_wed_promo_gift_01).setVisibility(0);
  }

  public void initExcellentDialog(String paramString, DPObject paramDPObject1, DPObject paramDPObject2)
  {
    setContentView(R.layout.wed_excellent_booking_dialog);
    this.btnBooking = ((Button)findViewById(R.id.button_wed_booking_dialog));
    this.editPhone = ((EditText)findViewById(R.id.edittext_wed_booking_dialog));
    this.editPhone.addTextChangedListener(this);
    this.layoutFrame = ((LinearLayout)findViewById(R.id.linearlayout_wed_dialog_frame));
    findViewById(R.id.framelayout_wed_dialog).setOnClickListener(this);
    this.btnBooking.setOnClickListener(this);
    if (!TextUtils.isEmpty(paramString))
      this.btnBooking.setText(paramString);
    if (paramDPObject2 != null)
    {
      paramString = paramDPObject2.getString("BookingUserMobile");
      if ((paramString != null) && (paramString.trim().length() > 0))
        this.editPhone.setText(paramString);
    }
    if (paramDPObject1 == null)
      return;
    paramString = (ImageView)findViewById(R.id.intervalLine);
    paramDPObject2 = (MeasuredGridView)findViewById(R.id.wed_excellent_gridview);
    paramDPObject1 = paramDPObject1.getArray("Properties");
    if ((paramDPObject1 == null) || (paramDPObject1.length == 0))
    {
      paramDPObject2.setVisibility(8);
      paramString.setVisibility(8);
      return;
    }
    paramDPObject2.setVisibility(0);
    paramString.setVisibility(0);
    paramString.setLayerType(1, null);
    paramDPObject2.setAdapter(new WedExcellentAdapter(getContext(), paramDPObject1));
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
    if (paramView.getId() == R.id.framelayout_wed_dialog)
      this.layoutFrame.startAnimation(this.bottomOutAnimation);
    do
    {
      do
        return;
      while (paramView.getId() != R.id.button_wed_booking_dialog);
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
    this.listener.onWedBookingDialogClick(this.editPhone.getText().toString());
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void setOnWedBookingDialogClickListener(OnWedBookingDialogClickListener paramOnWedBookingDialogClickListener)
  {
    this.listener = paramOnWedBookingDialogClickListener;
  }

  public void show()
  {
    super.show();
    if (this.layoutFrame != null)
      this.layoutFrame.startAnimation(this.bottomInAnimation);
  }

  public static abstract interface OnWedBookingDialogClickListener
  {
    public abstract void onWedBookingDialogClick(String paramString);
  }

  class WedExcellentAdapter extends WedBaseAdapter<DPObject>
  {
    public WedExcellentAdapter(Context paramArrayOfDPObject, DPObject[] arg3)
    {
      this.context = paramArrayOfDPObject;
      Object localObject;
      this.adapterData = localObject;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = new NovaTextView(WedBookingDialog.this.getContext());
      paramView = (NovaTextView)paramViewGroup;
      paramView.setGravity(16);
      paramView.setTextColor(WedBookingDialog.this.getContext().getResources().getColor(R.color.deep_gray));
      paramView.setCompoundDrawablePadding(ViewUtils.dip2px(WedBookingDialog.this.getContext(), 5.0F));
      paramView.setTextSize(0, WedBookingDialog.this.getContext().getResources().getDimensionPixelSize(R.dimen.text_size_13));
      paramView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wed_icon_gou, 0, 0, 0);
      paramView.setText(((DPObject[])this.adapterData)[paramInt].getString("Name"));
      return paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.wed.WedBookingDialog
 * JD-Core Version:    0.6.0
 */