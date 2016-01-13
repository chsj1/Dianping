package com.google.zxing.client.android.result;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;

public final class ResultHandlerFactory
{
  public static ResultHandler makeResultHandler(CaptureActivity paramCaptureActivity, Result paramResult)
  {
    ParsedResult localParsedResult = parseResult(paramResult);
    switch (1.$SwitchMap$com$google$zxing$client$result$ParsedResultType[localParsedResult.getType().ordinal()])
    {
    default:
      return new TextResultHandler(paramCaptureActivity, localParsedResult, paramResult);
    case 1:
      return new AddressBookResultHandler(paramCaptureActivity, localParsedResult);
    case 2:
      return new EmailAddressResultHandler(paramCaptureActivity, localParsedResult);
    case 3:
      return new ProductResultHandler(paramCaptureActivity, localParsedResult, paramResult);
    case 4:
      return new URIResultHandler(paramCaptureActivity, localParsedResult);
    case 5:
      return new WifiResultHandler(paramCaptureActivity, localParsedResult);
    case 6:
      return new GeoResultHandler(paramCaptureActivity, localParsedResult);
    case 7:
      return new TelResultHandler(paramCaptureActivity, localParsedResult);
    case 8:
      return new SMSResultHandler(paramCaptureActivity, localParsedResult);
    case 9:
      return new CalendarResultHandler(paramCaptureActivity, localParsedResult);
    case 10:
    }
    return new ISBNResultHandler(paramCaptureActivity, localParsedResult, paramResult);
  }

  private static ParsedResult parseResult(Result paramResult)
  {
    return ResultParser.parseResult(paramResult);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.ResultHandlerFactory
 * JD-Core Version:    0.6.0
 */