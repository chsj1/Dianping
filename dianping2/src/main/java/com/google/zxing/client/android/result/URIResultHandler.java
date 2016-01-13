package com.google.zxing.client.android.result;

import android.app.Activity;
import com.google.zxing.client.android.LocaleManager;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import java.util.Locale;

public final class URIResultHandler extends ResultHandler
{
  private static final String[] SECURE_PROTOCOLS = { "otpauth:" };
  private static final int[] buttons = { R.string.button_open_browser, R.string.button_share_by_email, R.string.button_share_by_sms };

  public URIResultHandler(Activity paramActivity, ParsedResult paramParsedResult)
  {
    super(paramActivity, paramParsedResult);
  }

  public boolean areContentsSecure()
  {
    String str = ((URIParsedResult)getResult()).getURI().toLowerCase(Locale.ENGLISH);
    String[] arrayOfString = SECURE_PROTOCOLS;
    int j = arrayOfString.length;
    int i = 0;
    while (i < j)
    {
      if (str.startsWith(arrayOfString[i]))
        return true;
      i += 1;
    }
    return false;
  }

  public int getButtonCount()
  {
    if (LocaleManager.isBookSearchUrl(((URIParsedResult)getResult()).getURI()))
      return buttons.length;
    return buttons.length - 1;
  }

  public int getButtonText(int paramInt)
  {
    return buttons[paramInt];
  }

  public Integer getDefaultButtonID()
  {
    return Integer.valueOf(0);
  }

  public int getDisplayTitle()
  {
    return R.string.result_uri;
  }

  public void handleButtonPress(int paramInt)
  {
    String str = ((URIParsedResult)getResult()).getURI();
    switch (paramInt)
    {
    default:
      return;
    case 0:
      openURL(str);
      return;
    case 1:
      shareByEmail(str);
      return;
    case 2:
    }
    shareBySMS(str);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.URIResultHandler
 * JD-Core Version:    0.6.0
 */