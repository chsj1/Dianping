package com.google.zxing.client.android.result.supplement;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;
import com.google.zxing.client.android.HttpHelper;
import com.google.zxing.client.android.HttpHelper.ContentType;
import com.google.zxing.client.android.LocaleManager;
import com.google.zxing.client.android.R.string;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class ProductResultInfoRetriever extends SupplementalInfoRetriever
{
  private static final Pattern[] PRODUCT_NAME_PRICE_PATTERNS = { Pattern.compile(",event\\)\">([^<]+)</a></h3>.+<span class=psrp>([^<]+)</span>"), Pattern.compile("owb63p\">([^<]+).+zdi3pb\">([^<]+)") };
  private final Context context;
  private final String productID;
  private final String source;

  ProductResultInfoRetriever(TextView paramTextView, String paramString, Context paramContext)
  {
    super(paramTextView);
    this.productID = paramString;
    this.source = paramContext.getString(R.string.msg_google_product);
    this.context = paramContext;
  }

  private static String unescapeHTML(String paramString)
  {
    return Html.fromHtml(paramString).toString();
  }

  void retrieveSupplementalInfo()
    throws IOException
  {
    String str = URLEncoder.encode(this.productID, "UTF-8");
    str = "https://www.google." + LocaleManager.getProductSearchCountryTLD(this.context) + "/m/products?ie=utf8&oe=utf8&scoring=p&source=zxing&q=" + str;
    CharSequence localCharSequence = HttpHelper.downloadViaHttp(str, HttpHelper.ContentType.HTML);
    Pattern[] arrayOfPattern = PRODUCT_NAME_PRICE_PATTERNS;
    int j = arrayOfPattern.length;
    int i = 0;
    while (true)
    {
      if (i < j)
      {
        Matcher localMatcher = arrayOfPattern[i].matcher(localCharSequence);
        if (localMatcher.find())
          append(this.productID, this.source, new String[] { unescapeHTML(localMatcher.group(1)), unescapeHTML(localMatcher.group(2)) }, str);
      }
      else
      {
        return;
      }
      i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.supplement.ProductResultInfoRetriever
 * JD-Core Version:    0.6.0
 */