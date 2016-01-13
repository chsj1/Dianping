package com.tencent.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class ShareHelper
{
  private static ShareHelper instance;
  private String mBackActivity;
  private String mBackPkg;

  public static ShareHelper getInstance()
  {
    if (instance == null)
      instance = new ShareHelper();
    return instance;
  }

  public int init(Intent paramIntent)
  {
    if (paramIntent == null)
      return 1;
    this.mBackActivity = paramIntent.getStringExtra("srcClassName");
    this.mBackPkg = paramIntent.getStringExtra("srcPackageName");
    if ((TextUtils.isEmpty(this.mBackActivity)) || (TextUtils.isEmpty(this.mBackPkg)))
      return 2;
    return 0;
  }

  public void release()
  {
    this.mBackActivity = null;
    this.mBackPkg = null;
    instance = null;
  }

  public int shareToQQ(Context paramContext, long paramLong, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if ((paramContext == null) || (paramString1 == null) || (paramString2 == null) || (paramString3 == null) || (paramString4 == null))
      return 3;
    if ((TextUtils.isEmpty(this.mBackActivity)) || (TextUtils.isEmpty(this.mBackPkg)))
      return 4;
    Intent localIntent = new Intent();
    localIntent.setClassName(this.mBackPkg, this.mBackActivity);
    Bundle localBundle = new Bundle();
    localBundle.putLong("req_share_id", paramLong);
    localBundle.putString("detail_url", paramString1);
    localBundle.putString("title", paramString2);
    localBundle.putString("desc", paramString3);
    localBundle.putString("image_url_remote", paramString4);
    localBundle.putString("share_from", "thridapp");
    localIntent.putExtras(localBundle);
    localIntent.setFlags(67108864);
    try
    {
      paramContext.startActivity(localIntent);
      return 0;
    }
    catch (Exception paramContext)
    {
      while (true)
        paramContext.printStackTrace();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.share.ShareHelper
 * JD-Core Version:    0.6.0
 */