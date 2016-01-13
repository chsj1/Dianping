package com.dianping.membercard.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

public class MCIntentUtils
{
  public static Bundle getBundle(Intent paramIntent)
  {
    Bundle localBundle = new Bundle();
    Object localObject = paramIntent.getData().getQuery();
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      paramIntent = ((String)localObject).split("&");
      int j = paramIntent.length;
      int i = 0;
      while (i < j)
      {
        localObject = paramIntent[i].split("=");
        localBundle.putString(localObject[0], localObject[1]);
        i += 1;
      }
    }
    localBundle.putAll(paramIntent.getExtras());
    return (Bundle)localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.MCIntentUtils
 * JD-Core Version:    0.6.0
 */