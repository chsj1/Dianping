package com.tencent.mobileqq.openpay.api;

import android.content.Context;
import android.text.TextUtils;

public class OpenApiFactory
{
  private OpenApiFactory()
  {
    throw new RuntimeException(getClass().getSimpleName() + " should not be created.");
  }

  public static IOpenApi getInstance(Context paramContext, String paramString)
  {
    if ((paramContext == null) || (TextUtils.isEmpty(paramString)))
      return null;
    return new a(paramContext);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.mobileqq.openpay.api.OpenApiFactory
 * JD-Core Version:    0.6.0
 */