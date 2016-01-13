package com.dianping.base.thirdparty.qqapi;

import android.content.Context;
import com.tencent.mobileqq.openpay.api.IOpenApi;
import com.tencent.mobileqq.openpay.api.OpenApiFactory;

public class TenPayHelper
{
  public static final String APP_ID = "200002";
  public static final String TAG = TenPayHelper.class.getSimpleName();
  private static IOpenApi openApi = null;

  public static IOpenApi getQQAPI(Context paramContext)
  {
    if (openApi == null)
    {
      if (paramContext == null)
        return null;
      openApi = OpenApiFactory.getInstance(paramContext.getApplicationContext(), "200002");
    }
    return openApi;
  }

  public static boolean isQQAppInstalled(Context paramContext)
  {
    return getQQAPI(paramContext).isMobileQQInstalled();
  }

  public static boolean isSupportPay(Context paramContext)
  {
    return getQQAPI(paramContext).isMobileQQSupportApi("pay");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.thirdparty.qqapi.TenPayHelper
 * JD-Core Version:    0.6.0
 */