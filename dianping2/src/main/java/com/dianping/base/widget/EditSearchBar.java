package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.dianping.v1.R.id;

public class EditSearchBar extends LinearLayout
  implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener
{
  private static final int CMD_CHANGED = 1;
  private static final long DEALY = 500L;
  private ImageButton mClear;
  private Handler mDelayHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1)
      {
        paramMessage = (String)paramMessage.obj;
        if (EditSearchBar.this.mListener != null)
          EditSearchBar.this.mListener.onKeywordChanged(paramMessage);
      }
    }
  };
  private EditText mEdit;
  OnKeywordChangeListner mListener;
  private OnStartSearchListner mStartListener;

  public EditSearchBar(Context paramContext)
  {
    super(paramContext);
  }

  public EditSearchBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void publishKeywordChange(String paramString)
  {
    this.mDelayHandler.removeMessages(1);
    this.mDelayHandler.sendMessageDelayed(this.mDelayHandler.obtainMessage(1, paramString), 500L);
  }

  public void afterTextChanged(Editable paramEditable)
  {
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public String getKeyword()
  {
    if (this.mEdit != null)
    {
      String str = this.mEdit.getText().toString().trim();
      if (TextUtils.isEmpty(str))
        this.mEdit.setText("");
      return str;
    }
    return "";
  }

  public void onClick(View paramView)
  {
    if ((paramView == this.mClear) && (this.mEdit != null))
    {
      this.mEdit.setText("");
      publishKeywordChange("");
    }
  }

  public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
  {
    paramTextView = getKeyword();
    if ((this.mStartListener != null) && (!TextUtils.isEmpty(paramTextView)))
    {
      this.mStartListener.onStartSearch(paramTextView);
      return true;
    }
    return false;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mEdit = ((EditText)findViewById(R.id.search_edit));
    this.mEdit.addTextChangedListener(this);
    this.mEdit.setOnEditorActionListener(this);
    this.mClear = ((ImageButton)findViewById(R.id.search_clear));
    this.mClear.setOnClickListener(this);
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    if (TextUtils.isEmpty(paramCharSequence))
      this.mClear.setVisibility(8);
    while (true)
    {
      publishKeywordChange(paramCharSequence.toString());
      return;
      this.mClear.setVisibility(0);
    }
  }

  public void setHint(int paramInt)
  {
    setHint(getResources().getString(paramInt));
  }

  public void setHint(String paramString)
  {
    if (this.mEdit != null)
      this.mEdit.setHint(paramString);
  }

  public void setKeyword(String paramString)
  {
    if (paramString == null);
    do
      return;
    while ((this.mEdit == null) || (paramString.compareTo(this.mEdit.getText().toString()) == 0));
    this.mEdit.setText(paramString);
    this.mEdit.setSelection(paramString.length() - 1);
  }

  public void setOnKeywordChangeListner(OnKeywordChangeListner paramOnKeywordChangeListner)
  {
    this.mListener = paramOnKeywordChangeListner;
  }

  public void setOnStartSearchListner(OnStartSearchListner paramOnStartSearchListner)
  {
    this.mStartListener = paramOnStartSearchListner;
  }

  public static abstract interface OnKeywordChangeListner
  {
    public abstract void onKeywordChanged(String paramString);
  }

  public static abstract interface OnStartSearchListner
  {
    public abstract void onStartSearch(String paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.EditSearchBar
 * JD-Core Version:    0.6.0
 */