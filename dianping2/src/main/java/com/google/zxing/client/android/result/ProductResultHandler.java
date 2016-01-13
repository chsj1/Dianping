package com.google.zxing.client.android.result;

import android.app.Activity;
import com.google.zxing.Result;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;

public final class ProductResultHandler extends ResultHandler
{
  private static final int[] buttons = { R.string.button_product_search, R.string.button_web_search, R.string.button_custom_product_search };

  public ProductResultHandler(Activity paramActivity, ParsedResult paramParsedResult, Result paramResult)
  {
    super(paramActivity, paramParsedResult, paramResult);
  }

  private static String getProductIDFromResult(ParsedResult paramParsedResult)
  {
    if ((paramParsedResult instanceof ProductParsedResult))
      return ((ProductParsedResult)paramParsedResult).getNormalizedProductID();
    if ((paramParsedResult instanceof ExpandedProductParsedResult))
      return ((ExpandedProductParsedResult)paramParsedResult).getRawText();
    throw new IllegalArgumentException(paramParsedResult.getClass().toString());
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
    return R.string.result_product;
  }

  public void handleButtonPress(int paramInt)
  {
    String str = getProductIDFromResult(getResult());
    switch (paramInt)
    {
    default:
      return;
    case 0:
      openProductSearch(str);
      return;
    case 1:
      webSearch(str);
      return;
    case 2:
    }
    openURL(fillInCustomSearchURL(str));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.ProductResultHandler
 * JD-Core Version:    0.6.0
 */