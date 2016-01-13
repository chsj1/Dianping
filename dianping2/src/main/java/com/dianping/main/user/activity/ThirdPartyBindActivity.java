package com.dianping.main.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.base.web.ui.NovaZeusActivity;

public class ThirdPartyBindActivity extends NovaZeusActivity
{
  protected Bundle handleIntent()
  {
    Bundle localBundle = super.handleIntent();
    Object localObject1;
    if (getIntent().getData() != null)
    {
      localObject2 = getIntent().getData().getQueryParameter("url");
      localObject1 = localObject2;
      if (TextUtils.isEmpty((CharSequence)localObject2))
        localObject1 = getIntent().getStringExtra("url");
      if (!TextUtils.isEmpty((CharSequence)localObject1));
    }
    else
    {
      return localBundle;
    }
    Object localObject2 = Uri.parse((String)localObject1);
    if (((Uri)localObject2).getQueryParameter("dpshare") == null)
      localObject1 = ((Uri)localObject2).buildUpon().appendQueryParameter("dpshare", "false").build().toString();
    localBundle.putString("url", (String)localObject1);
    return (Bundle)(Bundle)localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.ThirdPartyBindActivity
 * JD-Core Version:    0.6.0
 */