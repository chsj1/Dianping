package com.dianping.booking.util;

import android.widget.EditText;

public class TrimRunnable
  implements Runnable
{
  private EditText mEditText;

  public TrimRunnable(EditText paramEditText)
  {
    this.mEditText = paramEditText;
  }

  public void run()
  {
    if (this.mEditText.getSelectionStart() > this.mEditText.getText().toString().trim().length())
    {
      this.mEditText.setText(this.mEditText.getText().toString().trim());
      this.mEditText.setSelection(this.mEditText.getText().toString().trim().length());
    }
    do
      return;
    while (this.mEditText.getSelectionStart() > this.mEditText.getText().toString().length() - this.mEditText.getText().toString().trim().length());
    this.mEditText.setText(this.mEditText.getText().toString().trim());
    this.mEditText.setSelection(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.util.TrimRunnable
 * JD-Core Version:    0.6.0
 */