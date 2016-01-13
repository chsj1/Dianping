package com.dianping.search.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import com.dianping.util.TextUtils;

public class CustomClickSpan
{
  private final char NONE_TYPE = '9';
  private SpanClick spanClick;
  private char type = '9';

  public CustomClickSpan(SpanClick paramSpanClick)
  {
    this.spanClick = paramSpanClick;
  }

  public int gaType()
  {
    if (this.type == '1')
      return 1;
    if (this.type == '0')
      return 0;
    return -1;
  }

  public SpannableStringBuilder makePartTextClick(Context paramContext, String paramString, int paramInt1, int paramInt2)
  {
    this.type = '9';
    if (TextUtils.isEmpty(paramString))
    {
      paramContext = null;
      return paramContext;
    }
    paramString = TextUtils.highLightShow(paramContext, paramString, paramInt1);
    while (true)
    {
      paramInt1 = paramString.toString().indexOf("<");
      int i = paramString.toString().indexOf(">");
      if ((paramInt1 < 0) || (i < 0) || (paramInt1 >= i))
      {
        paramContext = paramString;
        if (this.type == '9')
          break;
        paramString.append(" ");
        return paramString;
      }
      String str1 = paramString.toString();
      String str2 = str1.substring(paramInt1 + 2, i);
      this.type = str1.charAt(paramInt1 + 1);
      char c = this.type;
      paramString.delete(paramInt1, paramInt1 + 1);
      paramString.delete(i - 1, i);
      paramString.delete(paramInt1, paramInt1 + 1);
      i = str2.length();
      paramString.setSpan(new ClickableSpan(str2, c, paramContext, paramInt2)
      {
        public void onClick(View paramView)
        {
          if (CustomClickSpan.this.spanClick != null)
            CustomClickSpan.this.spanClick.onSpanClick(this.val$keyWord, this.val$clickType);
        }

        public void updateDrawState(TextPaint paramTextPaint)
        {
          paramTextPaint.setColor(this.val$context.getResources().getColor(this.val$clickResid));
          paramTextPaint.setUnderlineText(false);
        }
      }
      , paramInt1, paramInt1 + i, 33);
    }
  }

  public static abstract interface SpanClick
  {
    public abstract void onSpanClick(String paramString, char paramChar);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.util.CustomClickSpan
 * JD-Core Version:    0.6.0
 */