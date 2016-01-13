package com.dianping.dataservice.mapi;

import com.dianping.app.DPActivity;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ContextMApiService extends WrapperMApiService
{
  private DPActivity activity;

  public ContextMApiService(DPActivity paramDPActivity, MApiService paramMApiService)
  {
    super(paramMApiService);
    this.activity = paramDPActivity;
  }

  private static boolean needEscape(String paramString)
  {
    paramString = paramString.toCharArray();
    int j = paramString.length;
    int i = 0;
    while (i < j)
    {
      int k = paramString[i];
      if ((k < 32) || (k > 126))
        return true;
      i += 1;
    }
    return false;
  }

  public WrapperMApiRequest wrap(MApiRequest paramMApiRequest)
  {
    return new Request(paramMApiRequest, this.activity.getMyUrl(), this.activity.pageId(), this.activity.prevPageId());
  }

  private static class Request extends WrapperMApiRequest
  {
    String page;
    String pageId;
    String prevPageId;

    public Request(MApiRequest paramMApiRequest, String paramString1, String paramString2, String paramString3)
    {
      super();
      this.page = paramString1;
      this.pageId = paramString2;
      this.prevPageId = paramString3;
    }

    public List<NameValuePair> headers()
    {
      ArrayList localArrayList = new ArrayList();
      List localList = super.headers();
      if (localList != null)
        localArrayList.addAll(localList);
      if (this.page != null);
      try
      {
        localArrayList.add(new BasicNameValuePair("pragma-page", URLEncoder.encode(this.page, "utf-8")));
        label53: if (this.pageId != null)
          localArrayList.add(new BasicNameValuePair("pragma-page-id", this.pageId));
        if (this.prevPageId != null)
          localArrayList.add(new BasicNameValuePair("pragma-prev-page-id", this.prevPageId));
        return localArrayList;
      }
      catch (Exception localException)
      {
        break label53;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.ContextMApiService
 * JD-Core Version:    0.6.0
 */