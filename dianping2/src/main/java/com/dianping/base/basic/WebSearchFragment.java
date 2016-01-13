package com.dianping.base.basic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONObject;

public class WebSearchFragment extends AbstractSearchFragment
{
  private String hintText;
  private String historyFileName;
  private String hotwordUrl;
  private String suggestUrl;

  public MApiRequest createRequest(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      if (!TextUtils.isEmpty(this.hotwordUrl));
    do
    {
      return null;
      return BasicMApiRequest.mapiGet(this.hotwordUrl, CacheType.CRITICAL);
    }
    while (TextUtils.isEmpty(this.suggestUrl));
    StringBuilder localStringBuilder = new StringBuilder(this.suggestUrl);
    try
    {
      localStringBuilder.append("&keyword=" + URLEncoder.encode(paramString, "utf-8"));
      return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    }
    catch (UnsupportedEncodingException paramString)
    {
      while (true)
        paramString.printStackTrace();
    }
  }

  public String getFileName()
  {
    return this.historyFileName;
  }

  public void onCreate(Bundle paramBundle)
  {
    boolean bool2 = true;
    boolean bool1 = true;
    super.onCreate(paramBundle);
    paramBundle = getActivity().getIntent().getStringExtra("extra");
    if (!TextUtils.isEmpty(paramBundle));
    do
    {
      try
      {
        paramBundle = new JSONObject(paramBundle);
        this.hotwordUrl = paramBundle.optString("hotsuggesturl");
        this.suggestUrl = paramBundle.optString("keywordurl");
        this.hintText = paramBundle.optString("placeholder");
        this.historyFileName = paramBundle.optString("defaultkey");
        if (!TextUtils.isEmpty(this.hotwordUrl));
        while (true)
        {
          this.hasHotwordView = bool1;
          this.searchHint = this.hintText;
          return;
          bool1 = false;
        }
      }
      catch (java.lang.Exception paramBundle)
      {
        Log.e("WebSearchFragment", "extract parameters from json failed", paramBundle);
        return;
      }
      paramBundle = getActivity().getIntent().getData();
    }
    while (paramBundle == null);
    this.hotwordUrl = paramBundle.getQueryParameter("hotsuggesturl");
    this.suggestUrl = paramBundle.getQueryParameter("keywordurl");
    this.hintText = paramBundle.getQueryParameter("placeholder");
    this.historyFileName = paramBundle.getQueryParameter("defaultkey");
    if (!TextUtils.isEmpty(this.hotwordUrl));
    for (bool1 = bool2; ; bool1 = false)
    {
      this.hasHotwordView = bool1;
      this.searchHint = this.hintText;
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.WebSearchFragment
 * JD-Core Version:    0.6.0
 */