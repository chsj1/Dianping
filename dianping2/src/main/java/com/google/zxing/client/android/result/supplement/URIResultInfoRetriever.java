package com.google.zxing.client.android.result.supplement;

import android.content.Context;
import android.widget.TextView;
import com.google.zxing.client.android.HttpHelper;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.result.URIParsedResult;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

final class URIResultInfoRetriever extends SupplementalInfoRetriever
{
  private static final int MAX_REDIRECTS = 5;
  private final String redirectString;
  private final URIParsedResult result;

  URIResultInfoRetriever(TextView paramTextView, URIParsedResult paramURIParsedResult, Context paramContext)
  {
    super(paramTextView);
    this.redirectString = paramContext.getString(R.string.msg_redirect);
    this.result = paramURIParsedResult;
  }

  void retrieveSupplementalInfo()
    throws IOException
  {
    try
    {
      Object localObject = new URI(this.result.getURI());
      URI localURI = HttpHelper.unredirect((URI)localObject);
      int i = 0;
      while ((i < 5) && (!((URI)localObject).equals(localURI)))
      {
        localObject = this.result.getDisplayResult();
        String str1 = this.redirectString + " : " + localURI;
        String str2 = localURI.toString();
        append((String)localObject, null, new String[] { str1 }, str2);
        localObject = localURI;
        localURI = HttpHelper.unredirect(localURI);
        i += 1;
      }
    }
    catch (URISyntaxException localURISyntaxException)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.supplement.URIResultInfoRetriever
 * JD-Core Version:    0.6.0
 */