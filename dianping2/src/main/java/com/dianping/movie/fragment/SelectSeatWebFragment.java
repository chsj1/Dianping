package com.dianping.movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.zeus.client.ZeusWebViewClient;

public class SelectSeatWebFragment extends NovaZeusFragment
{
  public static final int REQUEST_CODE_SUBMIT_ORDER = 100;
  private String originUrl;

  protected ZeusWebViewClient createWebViewClient()
  {
    return new SelectSeatWebFragment.WebMovieSeatWebViewClient(this, this);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 100) && (paramInt2 == -1))
    {
      if (TextUtils.isEmpty(this.originUrl))
        loadUrl(this.url);
    }
    else
      return;
    loadUrl(this.originUrl);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.originUrl = this.url;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.fragment.SelectSeatWebFragment
 * JD-Core Version:    0.6.0
 */