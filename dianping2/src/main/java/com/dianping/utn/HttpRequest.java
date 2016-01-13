package com.dianping.utn;

import B;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import org.json.JSONObject;

public class HttpRequest extends BaseRequest
{
  public static final int CHINA_MOBILE = 1;
  public static final int CHINA_TELECOM = 3;
  public static final int CHINA_UNICOM = 2;
  public static final int DELETE = 4;
  public static final int GET = 0;
  public static final int HEAD = 1;
  public static final int POST = 2;
  public static final int PUT = 3;
  public byte[] body;
  public Map<String, String> headers;
  public int method;
  public int operator;
  public String url;

  public HttpRequest()
  {
    this.type = 4;
  }

  public String methodName()
  {
    switch (this.method)
    {
    default:
      return "GET";
    case 0:
      return "GET";
    case 1:
      return "HEAD";
    case 2:
      return "POST";
    case 3:
      return "PUT";
    case 4:
    }
    return "DELETE";
  }

  public DatagramPacket pack()
    throws Exception
  {
    Object localObject1 = new JSONObject();
    if (this.operator != 0)
      ((JSONObject)localObject1).put("o", this.operator);
    if (this.method != 0)
      ((JSONObject)localObject1).put("m", this.method);
    ((JSONObject)localObject1).put("u", this.url);
    if (this.headers != null)
    {
      localObject2 = new JSONObject();
      Iterator localIterator = this.headers.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        ((JSONObject)localObject2).put((String)localEntry.getKey(), localEntry.getValue());
      }
      ((JSONObject)localObject1).put("h", localObject2);
    }
    localObject1 = ((JSONObject)localObject1).toString();
    Object localObject2 = new ByteArrayOutputStream(1400);
    ((ByteArrayOutputStream)localObject2).write(((String)localObject1).getBytes("utf-8"));
    if (this.body != null)
    {
      ((ByteArrayOutputStream)localObject2).write(0);
      ((ByteArrayOutputStream)localObject2).write(this.body);
    }
    localObject1 = packRaw(((ByteArrayOutputStream)localObject2).toByteArray());
    return (DatagramPacket)(DatagramPacket)new DatagramPacket(localObject1, localObject1.length);
  }

  public void parse(DatagramPacket paramDatagramPacket)
    throws Exception
  {
    paramDatagramPacket = parseRaw(paramDatagramPacket.getData(), paramDatagramPacket.getOffset(), paramDatagramPacket.getLength());
    int i = 0;
    Object localObject;
    HashMap localHashMap;
    while (true)
    {
      if ((i >= paramDatagramPacket.length) || (paramDatagramPacket[i] == 0))
      {
        localObject = new JSONObject(new String(paramDatagramPacket, 0, i, "utf-8"));
        this.operator = ((JSONObject)localObject).optInt("o");
        this.method = ((JSONObject)localObject).optInt("m");
        this.url = ((JSONObject)localObject).getString("u");
        localObject = ((JSONObject)localObject).optJSONObject("h");
        if (localObject == null)
          break label204;
        localHashMap = new HashMap();
        Iterator localIterator = ((JSONObject)localObject).keys();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          localHashMap.put(str, ((JSONObject)localObject).getString(str));
        }
      }
      i += 1;
    }
    label204: for (this.headers = localHashMap; i + 1 < paramDatagramPacket.length; this.headers = null)
    {
      localObject = new byte[paramDatagramPacket.length - i - 1];
      System.arraycopy(paramDatagramPacket, i + 1, localObject, 0, paramDatagramPacket.length - i - 1);
      this.body = ((B)localObject);
      return;
    }
    this.body = null;
  }

  public void random(Random paramRandom)
  {
    super.random(paramRandom);
    this.operator = paramRandom.nextInt(4);
    this.method = 2;
    this.url = "http://m.api.dianping.com/login?id=";
    int i = 0;
    int j = paramRandom.nextInt(6);
    while (i < j + 1)
    {
      this.url += paramRandom.nextInt();
      i += 1;
    }
    if (paramRandom.nextBoolean())
    {
      this.headers = new HashMap();
      i = 0;
      j = paramRandom.nextInt(6);
      while (i < j + 1)
      {
        this.headers.put("key_" + paramRandom.nextInt(), "value_" + paramRandom.nextLong());
        i += 1;
      }
    }
    if (paramRandom.nextBoolean())
    {
      this.body = new byte[paramRandom.nextInt(400) + 400];
      paramRandom.nextBytes(this.body);
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(super.toString());
    if (this.operator != 0)
      localStringBuilder.append("operator = ").append(this.operator).append('\n');
    localStringBuilder.append("method = ").append(this.method).append('\n');
    localStringBuilder.append("url = ").append(this.url).append('\n');
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
 * Qualified Name:     com.dianping.utn.HttpRequest
 * JD-Core Version:    0.6.0
 */