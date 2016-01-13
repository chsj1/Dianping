package com.google.zxing.client.android;

import android.net.Uri;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.result.ParsedResultType;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

final class ScanFromWebPageManager
{
  private static final CharSequence CODE_PLACEHOLDER = "{CODE}";
  private static final CharSequence FORMAT_PLACEHOLDER;
  private static final CharSequence META_PLACEHOLDER;
  private static final CharSequence RAW_CODE_PLACEHOLDER = "{RAWCODE}";
  private static final String RAW_PARAM = "raw";
  private static final String RETURN_URL_PARAM = "ret";
  private static final CharSequence TYPE_PLACEHOLDER;
  private final boolean returnRaw;
  private final String returnUrlTemplate;

  static
  {
    META_PLACEHOLDER = "{META}";
    FORMAT_PLACEHOLDER = "{FORMAT}";
    TYPE_PLACEHOLDER = "{TYPE}";
  }

  ScanFromWebPageManager(Uri paramUri)
  {
    this.returnUrlTemplate = paramUri.getQueryParameter("ret");
    if (paramUri.getQueryParameter("raw") != null);
    for (boolean bool = true; ; bool = false)
    {
      this.returnRaw = bool;
      return;
    }
  }

  private static String replace(CharSequence paramCharSequence1, CharSequence paramCharSequence2, String paramString)
  {
    if (paramCharSequence2 == null)
      paramCharSequence2 = "";
    try
    {
      while (true)
      {
        String str = URLEncoder.encode(paramCharSequence2.toString(), "UTF-8");
        paramCharSequence2 = str;
        label21: return paramString.replace(paramCharSequence1, paramCharSequence2);
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      break label21;
    }
  }

  String buildReplyURL(Result paramResult, ResultHandler paramResultHandler)
  {
    String str = this.returnUrlTemplate;
    CharSequence localCharSequence = CODE_PLACEHOLDER;
    if (this.returnRaw);
    for (Object localObject = paramResult.getText(); ; localObject = paramResultHandler.getDisplayContents())
    {
      localObject = replace(localCharSequence, (CharSequence)localObject, str);
      localObject = replace(RAW_CODE_PLACEHOLDER, paramResult.getText(), (String)localObject);
      localObject = replace(FORMAT_PLACEHOLDER, paramResult.getBarcodeFormat().toString(), (String)localObject);
      paramResultHandler = replace(TYPE_PLACEHOLDER, paramResultHandler.getType().toString(), (String)localObject);
      return replace(META_PLACEHOLDER, String.valueOf(paramResult.getResultMetadata()), paramResultHandler);
    }
  }

  boolean isScanFromWebPage()
  {
    return this.returnUrlTemplate != null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.ScanFromWebPageManager
 * JD-Core Version:    0.6.0
 */