package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.dianping.util.TextUtils;
import com.dianping.widget.view.NovaTextView;

public class RichTextView extends NovaTextView
{
  public RichTextView(Context paramContext)
  {
    super(paramContext);
  }

  public RichTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void setRichText(String paramString)
  {
    TextUtils.setJsonText(paramString, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.RichTextView
 * JD-Core Version:    0.6.0
 */