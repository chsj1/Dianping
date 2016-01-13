package com.tencent.mobileqq.openpay.api;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.tencent.mobileqq.openpay.data.base.BaseApi;
import com.tencent.mobileqq.openpay.data.base.BaseResponse;
import com.tencent.mobileqq.openpay.data.pay.PayResponse;

final class a
  implements IOpenApi
{
  private final String[] a = { "pay" };
  private Context mContext;

  a(Context paramContext)
  {
    this.mContext = paramContext;
  }

  private static int a(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return -1;
    String[] arrayOfString1 = paramString1.split("\\.");
    String[] arrayOfString2 = paramString2.split("\\.");
    int i = 0;
    while (true)
    {
      try
      {
        if ((i < arrayOfString1.length) && (i < arrayOfString2.length))
          continue;
        int j;
        if (arrayOfString1.length > i)
        {
          return 1;
          j = Integer.parseInt(arrayOfString1[i]);
          int k = Integer.parseInt(arrayOfString2[i]);
          if (j < k)
            break;
          if (j > k)
            return 1;
        }
        else
        {
          j = arrayOfString2.length;
          if (j > i)
            break;
          return 0;
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        localNumberFormatException.printStackTrace();
        return paramString1.compareTo(paramString2);
      }
      i += 1;
    }
  }

  private String a()
  {
    try
    {
      Object localObject = this.mContext.getPackageManager().getPackageInfo("com.tencent.mobileqq", 0);
      if (localObject != null)
      {
        if (TextUtils.isEmpty(((PackageInfo)localObject).versionName))
          return null;
        localObject = ((PackageInfo)localObject).versionName;
        return localObject;
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      localNameNotFoundException.printStackTrace();
      return null;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return (String)null;
  }

  private boolean a(BaseApi paramBaseApi)
  {
    Bundle localBundle = new Bundle();
    paramBaseApi.toBundle(localBundle);
    try
    {
      paramBaseApi = this.mContext.getPackageName();
      if (TextUtils.isEmpty(paramBaseApi))
        return false;
      localBundle.putString("_mqqpay_payapi_packageName", paramBaseApi);
      paramBaseApi = new Intent();
      paramBaseApi.setAction("android.intent.action.VIEW");
      paramBaseApi.setData(Uri.parse("mqqwallet://open_pay/"));
      paramBaseApi.setPackage("com.tencent.mobileqq");
      paramBaseApi.putExtras(localBundle);
      paramBaseApi.addFlags(268435456).addFlags(134217728);
      this.mContext.startActivity(paramBaseApi);
      return true;
    }
    catch (Exception paramBaseApi)
    {
      paramBaseApi.printStackTrace();
    }
    return false;
  }

  private boolean b(BaseApi paramBaseApi)
  {
    try
    {
      String str = this.mContext.getPackageName();
      if (TextUtils.isEmpty(str))
        return false;
      Bundle localBundle = new Bundle();
      paramBaseApi.toBundle(localBundle);
      localBundle.putString("_mqqpay_baseapi_pkgname", str);
      paramBaseApi = new Intent();
      paramBaseApi.setAction("android.intent.action.VIEW");
      paramBaseApi.setData(Uri.parse("mqqwallet://open_pay/"));
      paramBaseApi.setPackage("com.tencent.mobileqq");
      paramBaseApi.putExtras(localBundle);
      paramBaseApi.addFlags(268435456).addFlags(134217728);
      this.mContext.startActivity(paramBaseApi);
      return true;
    }
    catch (Exception paramBaseApi)
    {
      paramBaseApi.printStackTrace();
    }
    return false;
  }

  public final boolean execApi(BaseApi paramBaseApi)
  {
    if ((paramBaseApi == null) || (!paramBaseApi.checkParams()));
    String str;
    do
    {
      do
      {
        do
          return false;
        while ("pay".compareTo(paramBaseApi.getApiName()) != 0);
        str = a();
      }
      while (str == null);
      if (a(str, "5.3.0") >= 0)
        return a(paramBaseApi);
    }
    while (a(str, "4.7.2") < 0);
    return b(paramBaseApi);
  }

  public final boolean handleIntent(Intent paramIntent, IOpenApiListener paramIOpenApiListener)
  {
    if ((paramIntent == null) || (paramIOpenApiListener == null));
    while (true)
    {
      return false;
      Object localObject = paramIntent.getStringExtra("com_tencent_mobileqq_open_pay");
      if ((TextUtils.isEmpty((CharSequence)localObject)) || (((String)localObject).compareTo("com.tencent.mobileqq.open.pay") != 0))
        continue;
      localObject = paramIntent.getExtras();
      if (localObject == null)
        continue;
      int i = ((Bundle)localObject).getInt("_mqqpay_baseapi_apimark", -1);
      paramIntent = null;
      switch (i)
      {
      default:
      case 1:
      }
      while ((paramIntent != null) && (paramIntent.checkParams()))
      {
        paramIOpenApiListener.onOpenResponse(paramIntent);
        return true;
        paramIntent = new PayResponse();
        paramIntent.fromBundle((Bundle)localObject);
      }
    }
  }

  public final boolean isMobileQQInstalled()
  {
    return a() != null;
  }

  public final boolean isMobileQQSupportApi(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return false;
    int i = 0;
    while (true)
    {
      String[] arrayOfString = this.a;
      if (i > 0);
      do
      {
        paramString = this.a;
        if (i > 0)
          break;
        paramString = a();
        if ((paramString == null) || (a(paramString, "4.7.2") < 0))
          break;
        return true;
      }
      while (paramString.compareTo(this.a[0]) == 0);
      i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.mobileqq.openpay.api.a
 * JD-Core Version:    0.6.0
 */