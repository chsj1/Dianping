package com.dianping.main.messagecenter.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import com.dianping.base.web.ui.NovaZeusActivity;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.main.messagecenter.fragment.SubscribeDetailWebFragment;

public class SubscribeDetailWebActivity extends NovaZeusActivity
{
  protected Class<? extends NovaZeusFragment> getEfteFragmentClass()
  {
    return SubscribeDetailWebFragment.class;
  }

  protected Bundle handleIntent()
  {
    Bundle localBundle = super.handleIntent();
    if (getIntent().getData() != null)
    {
      String str1 = getIntent().getData().getQueryParameter("switchid");
      String str2 = getIntent().getExtras().getString("title");
      Uri localUri = Uri.parse(getIntent().getData().getQueryParameter("url")).buildUpon().appendQueryParameter("frompageview", "broadcast").build();
      Object localObject = localUri;
      if (localUri.getQueryParameter("title") == null)
        localObject = localUri.buildUpon().appendQueryParameter("title", str2).build();
      localObject = ((Uri)localObject).toString();
      localBundle.putString("switchid", str1);
      localBundle.putString("url", (String)localObject);
      localBundle.putString("desc", getStringParam("desc"));
      localBundle.putInt("subtype", getIntParam("subtype", -1));
    }
    return (Bundle)localBundle;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.messagecenter.activity.SubscribeDetailWebActivity
 * JD-Core Version:    0.6.0
 */