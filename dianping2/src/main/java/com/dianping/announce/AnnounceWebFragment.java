package com.dianping.announce;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.base.web.ui.NovaZeusActivity;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.model.UserProfile;
import com.dianping.v1.R.layout;
import com.dianping.zeus.widget.BaseTitleBar;

public class AnnounceWebFragment extends NovaZeusFragment
{
  AnnounceContentManager contentManager;

  void fillData(String paramString, byte[] paramArrayOfByte)
  {
    this.mLayMask.setVisibility(4);
    String str = "";
    try
    {
      paramArrayOfByte = new String(paramArrayOfByte, "utf-8");
      this.webView.loadDataWithBaseURL(paramString, paramArrayOfByte, "text/html", "utf-8", null);
      return;
    }
    catch (Exception paramArrayOfByte)
    {
      while (true)
      {
        setError(paramString, "公告栏编码错误，请稍后再试");
        paramArrayOfByte = str;
      }
    }
  }

  protected void loadData()
  {
    if ((this.url == null) || (!this.url.startsWith("http://")))
    {
      getActivity().finish();
      return;
    }
    if (this.url.indexOf('?') >= 0)
    {
      loadUrl(this.url);
      return;
    }
    if (this.contentManager.containsKey(this.url))
    {
      fillData(this.url, this.contentManager.get(this.url));
      return;
    }
    this.mLayMask.removeAllViews();
    getActivity().getLayoutInflater().inflate(R.layout.loading_item, this.mLayMask, true);
    this.mLayMask.setVisibility(0);
    this.contentManager.get(this.url);
  }

  public void loadUrl(String paramString)
  {
    Object localObject;
    StringBuilder localStringBuilder;
    if (paramString.contains("token=*"))
    {
      localObject = ((NovaZeusActivity)getActivity()).getAccount();
      localStringBuilder = new StringBuilder().append("token=");
      if (localObject == null)
        localObject = "";
    }
    while (true)
    {
      localObject = paramString.replace("token=*", (String)localObject);
      label55: paramString = (String)localObject;
      if (((String)localObject).contains("screen=*"))
        paramString = ((String)localObject).replace("screen=*", "screen=" + screen());
      try
      {
        if (paramString.contains("scale="))
        {
          localObject = Uri.parse(paramString).getQueryParameter("scale");
          int i = getResources().getDisplayMetrics().widthPixels * 100 / Integer.parseInt((String)localObject);
          this.webView.setInitialScale(i);
        }
        label143: this.mLayMask.setVisibility(4);
        this.webView.loadUrl(paramString);
        return;
        localObject = ((UserProfile)localObject).token();
        continue;
        localObject = paramString;
        if (!paramString.contains("token=!"))
          break label55;
        localObject = ((NovaZeusActivity)getActivity()).getAccount();
        if (localObject != null)
        {
          localObject = paramString.replace("token=!", "token=" + ((UserProfile)localObject).token());
          break label55;
        }
        ((NovaZeusActivity)getActivity()).gotoLogin();
        return;
      }
      catch (Exception localException)
      {
        break label143;
      }
    }
  }

  public void onResume()
  {
    super.onResume();
    if ((this.url != null) && (this.url.contains("refresh=1")))
      loadData();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    getTitleBarHost().showTitleBar(false);
    this.contentManager = AnnounceContentManager.instance(getActivity());
    this.contentManager.setOnRequestEndListener(new AnnounceWebFragment.1(this));
    if ((TextUtils.isEmpty(this.url)) || (!this.url.startsWith("http")))
    {
      Toast.makeText(getActivity(), "非法url链接", 0).show();
      getActivity().finish();
    }
    do
      return;
    while (this.url.contains("refresh=1"));
    loadData();
  }

  String screen()
  {
    DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
    return localDisplayMetrics.widthPixels + "x" + localDisplayMetrics.heightPixels;
  }

  void setError(String paramString1, String paramString2)
  {
    this.mLayMask.removeAllViews();
    getActivity().getLayoutInflater().inflate(R.layout.error_item, this.mLayMask, true);
    ((TextView)this.mLayMask.findViewById(16908308)).setText(paramString2);
    this.mLayMask.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.announce.AnnounceWebFragment
 * JD-Core Version:    0.6.0
 */