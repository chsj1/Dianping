package com.dianping.statistics.impl;

import com.dianping.app.DPActivity;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PVProcessStatisticsService
  implements StatisticsService
{
  private final LinkedList<Con> links;
  private StatisticsService stat;

  public PVProcessStatisticsService(StatisticsService paramStatisticsService)
  {
    this.stat = paramStatisticsService;
    this.links = new LinkedList();
  }

  public void event(String paramString1, String paramString2, String paramString3, int paramInt, List<NameValuePair> paramList)
  {
    this.stat.event(paramString1, paramString2, paramString3, paramInt, paramList);
  }

  public void flush()
  {
    this.stat.flush();
  }

  List<NameValuePair> page(DPActivity paramDPActivity, List<NameValuePair> paramList)
  {
    String str = paramDPActivity.prevPageId();
    if (paramList == null)
    {
      paramList = new ArrayList(3);
      paramList.add(new BasicNameValuePair("page", paramDPActivity.getMyUrl()));
      paramList.add(new BasicNameValuePair("pageid", paramDPActivity.pageId()));
      if (str != null)
        paramList.add(new BasicNameValuePair("prevpageid", str));
      return paramList;
    }
    int k = 0;
    int j = 0;
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      NameValuePair localNameValuePair = (NameValuePair)localIterator.next();
      m = k;
      if ("page".equals(localNameValuePair.getName()))
        m = 1;
      int n = j;
      if ("pageid".equals(localNameValuePair.getName()))
        n = 1;
      k = m;
      j = n;
      if (!"prevpageid".equals(localNameValuePair.getName()))
        continue;
      i = 1;
      k = m;
      j = n;
    }
    int m = i;
    if (i == 0)
    {
      m = i;
      if (str == null)
        m = 1;
    }
    if ((k != 0) && (j != 0) && (m != 0))
      return paramList;
    paramList = new ArrayList(paramList);
    if (k == 0)
      paramList.add(new BasicNameValuePair("page", paramDPActivity.getMyUrl()));
    if (j == 0)
      paramList.add(new BasicNameValuePair("pageid", paramDPActivity.pageId()));
    if ((m == 0) && (str != null))
      paramList.add(new BasicNameValuePair("prevpageid", str));
    return paramList;
  }

  public void pageView(String paramString, List<NameValuePair> paramList)
  {
    monitorenter;
    Object localObject2 = null;
    try
    {
      Iterator localIterator = this.links.iterator();
      Object localObject1;
      while (true)
      {
        localObject1 = localObject2;
        if (!localIterator.hasNext())
          break;
        localObject1 = (Con)localIterator.next();
        if (!((Con)localObject1).request.url().equalsIgnoreCase(paramString))
          continue;
        localObject1 = ((Con)localObject1).activity;
        localIterator.remove();
      }
      if (localObject1 == null)
        this.stat.pageView(paramString, paramList);
      while (true)
      {
        return;
        this.stat.pageView(paramString, page((DPActivity)localObject1, paramList));
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramString;
  }

  public void push(String paramString)
  {
    this.stat.push(paramString);
  }

  public void record(List<NameValuePair> paramList)
  {
    this.stat.record(paramList);
  }

  public void register(DPActivity paramDPActivity, MApiRequest paramMApiRequest)
  {
    monitorenter;
    try
    {
      Con localCon = new Con(null);
      localCon.activity = paramDPActivity;
      localCon.request = paramMApiRequest;
      this.links.add(localCon);
      if (this.links.size() > 32)
      {
        Log.w("pv", "pv process, links exceed size limit(32), some leak in DPActivity?");
        this.links.removeFirst();
      }
      monitorexit;
      return;
    }
    finally
    {
      paramDPActivity = finally;
      monitorexit;
    }
    throw paramDPActivity;
  }

  public void unregister(DPActivity paramDPActivity)
  {
    monitorenter;
    try
    {
      Iterator localIterator = this.links.iterator();
      while (localIterator.hasNext())
      {
        if (((Con)localIterator.next()).activity != paramDPActivity)
          continue;
        localIterator.remove();
      }
    }
    finally
    {
      monitorexit;
    }
    monitorexit;
  }

  private static class Con
  {
    DPActivity activity;
    MApiRequest request;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.statistics.impl.PVProcessStatisticsService
 * JD-Core Version:    0.6.0
 */