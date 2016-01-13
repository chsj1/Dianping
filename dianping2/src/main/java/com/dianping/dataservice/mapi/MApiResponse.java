package com.dianping.dataservice.mapi;

import com.dianping.dataservice.http.HttpResponse;
import com.dianping.model.SimpleMsg;

public abstract interface MApiResponse extends HttpResponse
{
  public static final int STATUS_CODE_CACHED = 209;

  public abstract SimpleMsg message();

  public abstract byte[] rawData();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.MApiResponse
 * JD-Core Version:    0.6.0
 */