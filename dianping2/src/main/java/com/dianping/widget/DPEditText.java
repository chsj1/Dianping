package com.dianping.widget;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.util.AttributeSet;
import android.widget.TextView.BufferType;
import com.dianping.widget.emoji.EmojiEditText;

public class DPEditText extends EmojiEditText
{
  int maxLength;

  public DPEditText(Context paramContext)
  {
    this(paramContext, null);
  }

  public DPEditText(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void setMaxLength(int paramInt)
  {
    this.maxLength = paramInt;
    if (paramInt > 0)
      setFilters(new InputFilter[] { new InputFilter.LengthFilter(paramInt) });
  }

  public void setText(CharSequence paramCharSequence, TextView.BufferType paramBufferType)
  {
    super.setText(paramCharSequence, paramBufferType);
    paramBufferType = paramCharSequence;
    if (paramCharSequence == null)
      paramBufferType = "";
    if ((this.maxLength > 0) && (this.maxLength < paramBufferType.length()));
    for (int i = this.maxLength; ; i = paramBufferType.length())
    {
      setSelection(i);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.DPEditText
 * JD-Core Version:    0.6.0
 */