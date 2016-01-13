package com.dianping.base.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.id;

public class CustomEditText extends LinearLayout
  implements TextWatcher, View.OnClickListener
{
  public ImageButton mClear;
  public EditText mEdit;
  public ImageView mIcon;
  public TextView mTitle;

  public CustomEditText(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void afterTextChanged(Editable paramEditable)
  {
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void onClick(View paramView)
  {
    if (paramView == this.mClear)
      this.mEdit.setText("");
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTitle = ((TextView)findViewById(R.id.title));
    this.mIcon = ((ImageView)findViewById(R.id.icon));
    this.mEdit = ((EditText)findViewById(R.id.edit_text));
    this.mEdit.addTextChangedListener(this);
    this.mClear = ((ImageButton)findViewById(R.id.edit_clear));
    this.mClear.setOnClickListener(this);
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    if (TextUtils.isEmpty(paramCharSequence))
    {
      this.mClear.setVisibility(8);
      return;
    }
    this.mClear.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CustomEditText
 * JD-Core Version:    0.6.0
 */