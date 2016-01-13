package com.dianping.announce;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.dianping.base.web.ui.NovaZeusActivity;
import com.dianping.base.web.ui.NovaZeusFragment;
import java.net.URLDecoder;

public class AnnounceActivity extends NovaZeusActivity
{
  protected Class<? extends NovaZeusFragment> getEfteFragmentClass()
  {
    return AnnounceWebFragment.class;
  }

  protected Bundle handleIntent()
  {
    Bundle localBundle = super.handleIntent();
    String str;
    if (getIntent().getData() != null)
    {
      localObject = getIntent();
      if (((Intent)localObject).getData() == null)
        break label57;
      str = ((Intent)localObject).getData().getQueryParameter("url");
      localObject = str;
      if (str == null);
    }
    label57: for (Object localObject = URLDecoder.decode(str); ; localObject = getIntent().getStringExtra("url"))
    {
      localBundle.putString("url", (String)localObject);
      return localBundle;
    }
  }

  public static abstract interface OnRequestEndListener
  {
    public abstract void onRequestEnd(byte[] paramArrayOfByte);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.announce.AnnounceActivity
 * JD-Core Version:    0.6.0
 */