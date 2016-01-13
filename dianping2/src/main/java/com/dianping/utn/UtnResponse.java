package com.dianping.utn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UtnResponse
{
  public byte[] body;
  public Map<String, String> headers;
  public int statusCode;

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("statusCode = ").append(this.statusCode).append('\n');
    if (this.headers != null)
    {
      Object localObject = new ArrayList(this.headers.keySet());
      Collections.sort((List)localObject);
      localObject = ((ArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext())
      {
        String str = (String)((Iterator)localObject).next();
        localStringBuilder.append("  ").append(str).append(": ").append((String)this.headers.get(str)).append('\n');
      }
    }
    if (this.body != null)
      localStringBuilder.append("body = (").append(this.body.length).append(" bytes)");
    return (String)localStringBuilder.toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.utn.UtnResponse
 * JD-Core Version:    0.6.0
 */