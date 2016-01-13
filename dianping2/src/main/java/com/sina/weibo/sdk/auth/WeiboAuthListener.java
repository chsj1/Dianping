package com.sina.weibo.sdk.auth;

import android.os.Bundle;
import com.sina.weibo.sdk.exception.WeiboException;

public abstract interface WeiboAuthListener
{
  public abstract void onCancel();

  public abstract void onComplete(Bundle paramBundle);

  public abstract void onWeiboException(WeiboException paramWeiboException);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.sina.weibo.sdk.auth.WeiboAuthListener
 * JD-Core Version:    0.6.0
 */