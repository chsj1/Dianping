package com.alipay.sdk.app;

import android.app.Activity;
import com.alipay.sdk.data.MspConfig;
import com.alipay.sdk.sys.GlobalContext;
import com.alipay.sdk.util.AuthHelper;

public class AuthTask
{
  private Activity a;

  public AuthTask(Activity paramActivity)
  {
    this.a = paramActivity;
  }

  public String auth(String paramString)
  {
    monitorenter;
    try
    {
      GlobalContext.a().a(this.a, MspConfig.a());
      paramString = AuthHelper.a(this.a, paramString);
      monitorexit;
      return paramString;
    }
    finally
    {
      paramString = finally;
      monitorexit;
    }
    throw paramString;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.app.AuthTask
 * JD-Core Version:    0.6.0
 */