package com.google.zxing.client.android.result;

import android.app.Activity;
import com.google.zxing.Result;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.result.ParsedResult;

public final class TextResultHandler extends ResultHandler
{
  private static final int[] buttons = { R.string.button_web_search, R.string.button_share_by_email, R.string.button_share_by_sms, R.string.button_custom_product_search };

  public TextResultHandler(Activity paramActivity, ParsedResult paramParsedResult, Result paramResult)
  {
    super(paramActivity, paramParsedResult, paramResult);
  }

  public int getButtonCount()
  {
    if (hasCustomProductSearch())
      return buttons.length;
    return buttons.length - 1;
  }

  public int getButtonText(int paramInt)
  {
    return buttons[paramInt];
  }

  public int getDisplayTitle()
  {
    return R.string.result_text;
  }

  public void handleButtonPress(int paramInt)
  {
    String str = getResult().getDisplayResult();
    switch (paramInt)
    {
    default:
      return;
    case 0:
      webSearch(str);
      return;
    case 1:
      shareByEmail(str);
      return;
    case 2:
      shareBySMS(str);
      return;
    case 3:
    }
    openURL(fillInCustomSearchURL(str));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.TextResultHandler
 * JD-Core Version:    0.6.0
 */