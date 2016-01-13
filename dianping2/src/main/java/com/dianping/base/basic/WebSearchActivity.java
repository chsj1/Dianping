package com.dianping.base.basic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONObject;

public class WebSearchActivity extends NovaActivity
  implements AbstractSearchFragment.OnSearchFragmentListener
{
  private String searchUrl;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getStringExtra("extra");
    if (!TextUtils.isEmpty(paramBundle));
    while (true)
    {
      try
      {
        this.searchUrl = new JSONObject(paramBundle).optString("searchurl");
        hideTitleBar();
        paramBundle = new WebSearchFragment();
        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        localFragmentTransaction.add(16908290, paramBundle);
        localFragmentTransaction.addToBackStack(null);
        localFragmentTransaction.commit();
        return;
      }
      catch (java.lang.Exception paramBundle)
      {
        Log.e("WebSearchActivity", "extract parameters from json failed", paramBundle);
        continue;
      }
      paramBundle = getIntent().getData();
      if (paramBundle == null)
      {
        finish();
        return;
      }
      this.searchUrl = paramBundle.getQueryParameter("searchurl");
    }
  }

  public void onSearchFragmentDetach()
  {
    finish();
  }

  public void startSearch(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    do
    {
      return;
      localObject = paramDPObject.getString("Url");
      if (TextUtils.isEmpty((CharSequence)localObject))
        continue;
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)));
      return;
    }
    while (TextUtils.isEmpty(this.searchUrl));
    Object localObject = new StringBuilder(this.searchUrl);
    String str = paramDPObject.getString("Keyword");
    if (!TextUtils.isEmpty(str));
    try
    {
      ((StringBuilder)localObject).append("&keyword=" + URLEncoder.encode(str, "utf-8"));
      paramDPObject = paramDPObject.getString("Value");
      if (TextUtils.isEmpty(paramDPObject));
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      try
      {
        ((StringBuilder)localObject).append("&value=" + URLEncoder.encode(paramDPObject, "utf-8"));
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(((StringBuilder)localObject).toString())));
        return;
        localUnsupportedEncodingException = localUnsupportedEncodingException;
        localUnsupportedEncodingException.printStackTrace();
      }
      catch (UnsupportedEncodingException paramDPObject)
      {
        while (true)
          paramDPObject.printStackTrace();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.WebSearchActivity
 * JD-Core Version:    0.6.0
 */