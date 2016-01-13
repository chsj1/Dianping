package com.google.zxing.client.android.result;

import android.app.Activity;
import android.telephony.PhoneNumberUtils;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.TelParsedResult;

public final class TelResultHandler extends ResultHandler
{
  private static final int[] buttons = { R.string.button_dial, R.string.button_add_contact };

  public TelResultHandler(Activity paramActivity, ParsedResult paramParsedResult)
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

  public CharSequence getDisplayContents()
  {
    return PhoneNumberUtils.formatNumber(getResult().getDisplayResult().replace("\r", ""));
  }

  public int getDisplayTitle()
  {
    return R.string.result_tel;
  }

  public void handleButtonPress(int paramInt)
  {
    TelParsedResult localTelParsedResult = (TelParsedResult)getResult();
    switch (paramInt)
    {
    default:
      return;
    case 0:
      dialPhoneFromUri(localTelParsedResult.getTelURI());
      getActivity().finish();
      return;
    case 1:
    }
    addPhoneOnlyContact(new String[] { localTelParsedResult.getNumber() }, null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.TelResultHandler
 * JD-Core Version:    0.6.0
 */