package com.tencent.mobileqq.openpay.api;

import android.content.Intent;
import com.tencent.mobileqq.openpay.data.base.BaseApi;

public abstract interface IOpenApi
{
  public abstract boolean execApi(BaseApi paramBaseApi);

  public abstract boolean handleIntent(Intent paramIntent, IOpenApiListener paramIOpenApiListener);

  public abstract boolean isMobileQQInstalled();

  public abstract boolean isMobileQQSupportApi(String paramString);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.mobileqq.openpay.api.IOpenApi
 * JD-Core Version:    0.6.0
 */