package com.sina.weibo.sdk.net;

import com.sina.weibo.sdk.exception.WeiboException;

public abstract interface RequestListener
{
  public abstract void onComplete(String paramString);

  public abstract void onWeiboException(WeiboException paramWeiboException);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.sina.weibo.sdk.net.RequestListener
 * JD-Core Version:    0.6.0
 */