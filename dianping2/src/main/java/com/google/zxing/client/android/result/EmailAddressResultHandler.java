package com.google.zxing.client.android.result;

import android.app.Activity;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.ParsedResult;

public final class EmailAddressResultHandler extends ResultHandler
{
  private static final int[] buttons = { R.string.button_email, R.string.button_add_contact };

  public EmailAddressResultHandler(Activity paramActivity, ParsedResult paramParsedResult)
  {
    super(paramActivity, paramParsedResult);
  }

  public int getButtonCount()
  {
    return buttons.length;
  }

  public int getButtonText(int paramInt)
  {
    return buttons[paramInt];
  }

  public int getDisplayTitle()
  {
    return R.string.result_email_address;
  }

  public void handleButtonPress(int paramInt)
  {
    EmailAddressParsedResult localEmailAddressParsedResult = (EmailAddressParsedResult)getResult();
    switch (paramInt)
    {
    default:
      return;
    case 0:
      sendEmail(localEmailAddressParsedResult.getTos(), localEmailAddressParsedResult.getCCs(), localEmailAddressParsedResult.getBCCs(), localEmailAddressParsedResult.getSubject(), localEmailAddressParsedResult.getBody());
      return;
    case 1:
    }
    addEmailOnlyContact(localEmailAddressParsedResult.getTos(), null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.EmailAddressResultHandler
 * JD-Core Version:    0.6.0
 */