package com.dianping.tuan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.base.web.ui.NovaZeusActivity;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.locationservice.LocationListener;
import com.dianping.tuan.fragment.TuanWebFragment;
import com.dianping.v1.R.anim;

public class TuanWebActivity extends NovaZeusActivity
  implements LocationListener
{
  public static final String DEFAULT_ROOT_URL = "http://m.t.dianping.com/?cityid=!&token=*&sessionid=*&agent=!&screen=!&tag=home";
  public static final String ROOT_URL = "http://m.t.dianping.com/?cityid=!&token=*&sessionid=*&agent=!&screen=!&tag=home";
  protected boolean isModal;

  public void finish()
  {
    super.finish();
    if (this.isModal)
      overridePendingTransition(R.anim.fade_light_in, R.anim.slide_out_to_bottom);
  }

  protected Class<? extends NovaZeusFragment> getEfteFragmentClass()
  {
    return TuanWebFragment.class;
  }

  protected Bundle handleIntent()
  {
    Bundle localBundle = super.handleIntent();
    Object localObject1 = null;
    Intent localIntent = getIntent();
    Object localObject2 = localIntent.getData();
    this.isModal = localIntent.getBooleanExtra("modal", false);
    if (localObject2 != null)
      localObject1 = ((Uri)localObject2).getQueryParameter("url");
    localObject2 = localObject1;
    if (TextUtils.isEmpty((CharSequence)localObject1))
      localObject2 = getIntent().getStringExtra("url");
    if (localObject2 != null)
    {
      localObject1 = localObject2;
      if (((String)localObject2).length() != 0);
    }
    else
    {
      localObject1 = "http://m.t.dianping.com/?cityid=!&token=*&sessionid=*&agent=!&screen=!&tag=home";
    }
    if (localObject1 != null)
    {
      localObject2 = localObject1;
      if (((String)localObject1).length() != 0);
    }
    else
    {
      localObject2 = "http://m.t.dianping.com/?cityid=!&token=*&sessionid=*&agent=!&screen=!&tag=home";
    }
    localObject1 = localObject2;
    if (!TextUtils.isEmpty(utm()))
      localObject1 = (String)localObject2 + "&utm=" + utm();
    localBundle.putString("url", (String)localObject1);
    localBundle.putBoolean("modal", this.isModal);
    return (Bundle)(Bundle)localBundle;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  protected String utm()
  {
    Intent localIntent = getIntent();
    Object localObject2;
    if (localIntent.getData() != null)
    {
      localObject2 = localIntent.getData().getQueryParameter("_utm");
      localObject1 = localObject2;
      if (localObject2 != null);
    }
    for (Object localObject1 = localIntent.getData().getQueryParameter("utm_"); ; localObject1 = getIntent().getStringExtra("utm"))
    {
      localObject2 = localObject1;
      if (localObject1 == null)
        localObject2 = "";
      return localObject2;
    }
  }

  public static abstract interface OnRequestEndListener
  {
    public abstract void onRequestEnd(byte[] paramArrayOfByte);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanWebActivity
 * JD-Core Version:    0.6.0
 */