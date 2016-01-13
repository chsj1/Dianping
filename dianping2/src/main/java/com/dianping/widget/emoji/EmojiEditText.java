package com.dianping.widget.emoji;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import java.util.HashMap;

public class EmojiEditText extends EditText
{
  HashMap<String, Emoji> emojiHash = EmojiMap.emojiMap();
  MyResources res = MyResources.getResource(getClass());

  public EmojiEditText(Context paramContext)
  {
    this(paramContext, null);
  }

  public EmojiEditText(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void appendEmoji(Emoji paramEmoji)
  {
    if (getText().length() + paramEmoji.name.length() > 140)
      return;
    SpannableString localSpannableString = new SpannableString(paramEmoji.name);
    try
    {
      Object localObject = this.res.getDrawable(paramEmoji.drawableId);
      localObject = ((BitmapDrawable)localObject).getBitmap();
      localObject = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap((Bitmap)localObject, ViewUtils.dip2px(getContext(), 20.0F), ViewUtils.dip2px(getContext(), 20.0F), true));
      ((Drawable)localObject).setBounds(0, 0, ((Drawable)localObject).getIntrinsicWidth(), ((Drawable)localObject).getIntrinsicHeight());
      localSpannableString.setSpan(new ImageSpan((Drawable)localObject, 0), 0, paramEmoji.name.length(), 33);
      int i = getSelectionStart();
      getText().insert(i, localSpannableString);
      return;
    }
    catch (Resources.NotFoundException localDrawable)
    {
      while (true)
        Drawable localDrawable = this.res.getDrawable(paramEmoji.name);
    }
  }

  public void setEmojiText(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    int j = 0;
    String str = "";
    SpannableString localSpannableString = new SpannableString(paramString);
    int i = 0;
    paramString = str;
    if (i < arrayOfChar.length)
    {
      if (arrayOfChar[i] == '[')
      {
        j = i;
        paramString = "";
      }
      while (true)
      {
        i += 1;
        break;
        if (arrayOfChar[i] == ']')
        {
          paramString = "[" + paramString + "]";
          if ((this.emojiHash.get(paramString) != null) && (((Emoji)this.emojiHash.get(paramString)).drawableId != 0))
          {
            paramString = ((BitmapDrawable)getResources().getDrawable(((Emoji)this.emojiHash.get(paramString)).drawableId)).getBitmap();
            paramString = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(paramString, ViewUtils.dip2px(getContext(), 20.0F), ViewUtils.dip2px(getContext(), 20.0F), true));
            paramString.setBounds(0, 0, paramString.getIntrinsicWidth(), paramString.getIntrinsicHeight());
            localSpannableString.setSpan(new ImageSpan(paramString, 0), j, i + 1, 33);
          }
          paramString = "";
          continue;
        }
        paramString = paramString + arrayOfChar[i];
      }
    }
    setText(localSpannableString);
    setSelection(localSpannableString.length());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.emoji.EmojiEditText
 * JD-Core Version:    0.6.0
 */