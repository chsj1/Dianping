package com.dianping.pay.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.pay.utils.PayUtil;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;

public class GridPasswordView extends LinearLayout
{
  private static final int DEFAULT_GRIDCOLOR = -1;
  private static final int DEFAULT_LINECOLOR = -1433892728;
  private static final int DEFAULT_PASSWORDLENGTH = 6;
  private static final int DEFAULT_TEXTSIZE = 16;
  private static final String DEFAULT_TRANSFORMATION = "●";
  private int gridColor;
  private int lineColor;
  private Drawable lineDrawable;
  private int lineWidth;
  private OnPasswordChangedListener listener;
  private Drawable outerLineDrawable;
  private String[] passwordArr;
  private int passwordLength;
  private String passwordTransformation;
  private ColorStateList textColor;
  private int textSize = 16;
  private PasswordTransformationMethod transformationMethod;
  private TextView[] viewArr;

  public GridPasswordView(Context paramContext)
  {
    this(paramContext, null);
  }

  public GridPasswordView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initAttrs(paramContext, paramAttributeSet);
    initViews(paramContext);
  }

  private GradientDrawable generateBackgroundDrawable()
  {
    GradientDrawable localGradientDrawable = new GradientDrawable();
    localGradientDrawable.setColor(this.gridColor);
    localGradientDrawable.setStroke(this.lineWidth, this.lineColor);
    return localGradientDrawable;
  }

  private boolean getPassWordVisibility()
  {
    int i = 0;
    if (this.viewArr[0].getTransformationMethod() == null)
      i = 1;
    return i;
  }

  private void inflaterViews(Context paramContext)
  {
    paramContext = LayoutInflater.from(paramContext);
    int i = 0;
    while (i < this.passwordLength)
    {
      Object localObject = paramContext.inflate(R.layout.pay_grid_password_divider, null);
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(this.lineWidth, -1);
      ((View)localObject).setBackgroundDrawable(this.lineDrawable);
      addView((View)localObject, localLayoutParams);
      localObject = (TextView)paramContext.inflate(R.layout.pay_grid_password_textview, null);
      setCustomAttr((TextView)localObject);
      addView((View)localObject, new LinearLayout.LayoutParams(0, -1, 1.0F));
      this.viewArr[i] = localObject;
      i += 1;
    }
  }

  private void initAttrs(Context paramContext, AttributeSet paramAttributeSet)
  {
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.payGridPasswordView, -1, 0);
    this.textColor = paramAttributeSet.getColorStateList(R.styleable.payGridPasswordView_fontColor);
    if (this.textColor == null)
      this.textColor = ColorStateList.valueOf(getResources().getColor(17170435));
    int i = paramAttributeSet.getDimensionPixelSize(R.styleable.payGridPasswordView_fontSize, -1);
    if (i != -1)
      this.textSize = PayUtil.px2sp(paramContext, i);
    this.lineWidth = (int)paramAttributeSet.getDimension(R.styleable.payGridPasswordView_lineWidth, ViewUtils.dip2px(getContext(), 1.0F));
    this.lineColor = paramAttributeSet.getColor(R.styleable.payGridPasswordView_lineColor, -1433892728);
    this.gridColor = paramAttributeSet.getColor(R.styleable.payGridPasswordView_gridColor, -1);
    this.lineDrawable = paramAttributeSet.getDrawable(R.styleable.payGridPasswordView_lineColor);
    if (this.lineDrawable == null)
      this.lineDrawable = new ColorDrawable(this.lineColor);
    this.outerLineDrawable = generateBackgroundDrawable();
    this.passwordLength = paramAttributeSet.getInt(R.styleable.payGridPasswordView_passwordLength, 6);
    this.passwordTransformation = paramAttributeSet.getString(R.styleable.payGridPasswordView_passwordTransformation);
    if (TextUtils.isEmpty(this.passwordTransformation))
      this.passwordTransformation = "●";
    paramAttributeSet.recycle();
    this.passwordArr = new String[this.passwordLength];
    this.viewArr = new TextView[this.passwordLength];
  }

  private void initViews(Context paramContext)
  {
    super.setBackgroundDrawable(this.outerLineDrawable);
    setOrientation(0);
    this.transformationMethod = new GridPasswordView.CustomPasswordTransformationMethod(this, this.passwordTransformation);
    inflaterViews(paramContext);
  }

  private void notifyTextChanged()
  {
    if (this.listener == null);
    String str;
    do
    {
      return;
      str = getPassWord();
      this.listener.onChanged(str);
    }
    while (str.length() != this.passwordLength);
    this.listener.onMaxLength(str);
  }

  private void setCustomAttr(TextView paramTextView)
  {
    if (this.textColor != null)
      paramTextView.setTextColor(this.textColor);
    paramTextView.setTextSize(this.textSize);
    paramTextView.setInputType(18);
    paramTextView.setTransformationMethod(this.transformationMethod);
  }

  public void clearPassword()
  {
    int i = 0;
    while (i < this.passwordArr.length)
    {
      this.passwordArr[i] = null;
      this.viewArr[i].setText(null);
      i += 1;
    }
  }

  public String getPassWord()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < this.passwordArr.length)
    {
      if (this.passwordArr[i] != null)
        localStringBuilder.append(this.passwordArr[i]);
      i += 1;
    }
    return localStringBuilder.toString();
  }

  public void keyChanged(int paramInt)
  {
    if (10 == paramInt)
    {
      paramInt = this.passwordArr.length - 1;
      if (paramInt >= 0)
      {
        if (this.passwordArr[paramInt] == null)
          break label49;
        this.passwordArr[paramInt] = null;
        this.viewArr[paramInt].setText(null);
        notifyTextChanged();
      }
    }
    while (true)
    {
      return;
      label49: this.viewArr[paramInt].setText(null);
      paramInt -= 1;
      break;
      int i = 0;
      while (i < this.passwordArr.length)
      {
        if (this.passwordArr[i] == null)
        {
          this.passwordArr[i] = String.valueOf(paramInt);
          this.viewArr[i].setText(String.valueOf(paramInt));
          notifyTextChanged();
          return;
        }
        i += 1;
      }
    }
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    Parcelable localParcelable = paramParcelable;
    if ((paramParcelable instanceof Bundle))
    {
      paramParcelable = (Bundle)paramParcelable;
      this.passwordArr = paramParcelable.getStringArray("passwordArr");
      localParcelable = paramParcelable.getParcelable("instanceState");
      setPassword(getPassWord());
    }
    super.onRestoreInstanceState(localParcelable);
  }

  protected Parcelable onSaveInstanceState()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("instanceState", super.onSaveInstanceState());
    localBundle.putStringArray("passwordArr", this.passwordArr);
    return localBundle;
  }

  public void setBackground(Drawable paramDrawable)
  {
  }

  public void setBackgroundColor(int paramInt)
  {
  }

  public void setBackgroundDrawable(Drawable paramDrawable)
  {
  }

  public void setBackgroundResource(int paramInt)
  {
  }

  public void setOnPasswordChangedListener(OnPasswordChangedListener paramOnPasswordChangedListener)
  {
    this.listener = paramOnPasswordChangedListener;
  }

  public void setPassword(String paramString)
  {
    clearPassword();
    if (TextUtils.isEmpty(paramString));
    while (true)
    {
      return;
      paramString = paramString.toCharArray();
      int i = 0;
      while (i < paramString.length)
      {
        if (i < this.passwordArr.length)
        {
          this.passwordArr[i] = (paramString[i] + "");
          this.viewArr[i].setText(this.passwordArr[i]);
        }
        i += 1;
      }
    }
  }

  public void setPasswordVisibility(boolean paramBoolean)
  {
    TextView[] arrayOfTextView = this.viewArr;
    int j = arrayOfTextView.length;
    int i = 0;
    if (i < j)
    {
      TextView localTextView = arrayOfTextView[i];
      if (paramBoolean);
      for (Object localObject = null; ; localObject = this.transformationMethod)
      {
        localTextView.setTransformationMethod((TransformationMethod)localObject);
        if ((localTextView instanceof EditText))
        {
          localObject = (EditText)localTextView;
          ((EditText)localObject).setSelection(((EditText)localObject).getText().length());
        }
        i += 1;
        break;
      }
    }
  }

  public void togglePasswordVisibility()
  {
    if (!getPassWordVisibility());
    for (boolean bool = true; ; bool = false)
    {
      setPasswordVisibility(bool);
      return;
    }
  }

  public static abstract interface OnPasswordChangedListener
  {
    public abstract void onChanged(String paramString);

    public abstract void onMaxLength(String paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.view.GridPasswordView
 * JD-Core Version:    0.6.0
 */