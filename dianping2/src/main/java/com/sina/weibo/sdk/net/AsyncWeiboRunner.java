package com.sina.weibo.sdk.net;

import android.content.Context;
import com.sina.weibo.sdk.exception.WeiboException;

public class AsyncWeiboRunner
{
  private Context mContext;

  public AsyncWeiboRunner(Context paramContext)
  {
    this.mContext = paramContext;
  }

  public String request(String paramString1, WeiboParameters paramWeiboParameters, String paramString2)
    throws WeiboException
  {
    return HttpManager.openUrl(this.mContext, paramString1, paramString2, paramWeiboParameters);
  }

  public void requestAsync(String paramString1, WeiboParameters paramWeiboParameters, String paramString2, RequestListener paramRequestListener)
  {
    new AsyncWeiboRunner.RequestRunner(this.mContext, paramString1, paramWeiboParameters, paramString2, paramRequestListener).execute(new Void[1]);
  }

  @Deprecated
  public void requestByThread(String paramString1, WeiboParameters paramWeiboParameters, String paramString2, RequestListener paramRequestListener)
  {
    new AsyncWeiboRunner.1(this, paramString1, paramString2, paramWeiboParameters, paramRequestListener).start();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.sina.weibo.sdk.net.AsyncWeiboRunner
 * JD-Core Version:    0.6.0
 */