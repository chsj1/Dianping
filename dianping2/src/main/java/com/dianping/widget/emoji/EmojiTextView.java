package com.dianping.widget.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.widget.view.NovaTextView;
import java.util.HashMap;

public class EmojiTextView extends NovaTextView
{
  HashMap<String, Emoji> emojiHash = EmojiMap.emojiMap();
  MyResources res = MyResources.getResource(getClass());

  public EmojiTextView(Context paramContext)
  {
    this(paramContext, null);
  }

  public EmojiTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public EmojiTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public void setEmojiText(String paramString)
  {
    setText(stringToEmojiString(paramString));
  }

  public SpannableString stringToEmojiString(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      paramString = new SpannableString("");
    char[] arrayOfChar;
    int j;
    String str;
    SpannableString localSpannableString;
    int i;
    do
    {
      return paramString;
      arrayOfChar = paramString.toCharArray();
      j = 0;
      str = "";
      localSpannableString = new SpannableString(paramString);
      i = 0;
      paramString = localSpannableString;
    }
    while (i >= arrayOfChar.length);
    if (arrayOfChar[i] == '[')
      j = i;
    for (paramString = ""; ; paramString = str + arrayOfChar[i])
      while (true)
      {
        i += 1;
        str = paramString;
        break;
        if (arrayOfChar[i] == ']')
        {
          str = "[" + str + "]";
          if ((this.emojiHash.get(str) != null) && (((Emoji)this.emojiHash.get(str)).drawableId != 0));
          try
          {
            paramString = this.res.getDrawable(((Emoji)this.emojiHash.get(str)).drawableId);
            paramString = ((BitmapDrawable)paramString).getBitmap();
            paramString = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(paramString, ViewUtils.dip2px(getContext(), 20.0F), ViewUtils.dip2px(getContext(), 20.0F), true));
            paramString.setBounds(0, 0, paramString.getIntrinsicWidth(), paramString.getIntrinsicHeight());
            localSpannableString.setSpan(new ImageSpan(paramString, 0), j, i + 1, 33);
            paramString = "";
          }
          catch (android.content.res.Resources.NotFoundException paramString)
          {
            while (true)
              paramString = this.res.getDrawable(((Emoji)this.emojiHash.get(str)).drawableName);
          }
        }
      }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.emoji.EmojiTextView
 * JD-Core Version:    0.6.0
 */