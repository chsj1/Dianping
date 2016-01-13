package com.dianping.utn;

import B;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.json.JSONObject;

public class ResponseChain
{
  public ResponseBlock[] blocks;

  public ResponseChain()
  {
  }

  public ResponseChain(BaseRequest paramBaseRequest, UtnResponse paramUtnResponse)
  {
    ResponseBlock localResponseBlock = null;
    try
    {
      localObject = new ByteArrayOutputStream();
      localGZIPOutputStream = new GZIPOutputStream((OutputStream)localObject);
      localJSONObject1 = new JSONObject();
      localJSONObject1.put("c", paramUtnResponse.statusCode);
      if (paramUtnResponse.headers != null)
      {
        localJSONObject2 = new JSONObject();
        Iterator localIterator = paramUtnResponse.headers.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          localJSONObject2.put((String)localEntry.getKey(), localEntry.getValue());
        }
      }
    }
    catch (Exception paramUtnResponse)
    {
      Object localObject;
      GZIPOutputStream localGZIPOutputStream;
      JSONObject localJSONObject1;
      JSONObject localJSONObject2;
      paramUtnResponse = localResponseBlock;
      int j = (paramUtnResponse.length + 1384 - 1) / 1384;
      int i = j;
      if (j == 0)
        i = 1;
      this.blocks = new ResponseBlock[i];
      j = 0;
      label168: if (j < i)
      {
        localResponseBlock = new ResponseBlock();
        localResponseBlock.requestId = paramBaseRequest.requestId;
        localResponseBlock.protocolVersion = paramBaseRequest.protocolVersion;
        localResponseBlock.count = i;
        localResponseBlock.index = j;
        if (j < i - 1);
        for (int k = 1384; ; k = paramUtnResponse.length - j * 1384)
        {
          localObject = new byte[k];
          System.arraycopy(paramUtnResponse, j * 1384, localObject, 0, k);
          localResponseBlock.fragment = ((B)localObject);
          this.blocks[j] = localResponseBlock;
          j += 1;
          break label168;
          localJSONObject1.put("h", localJSONObject2);
          localGZIPOutputStream.write(localJSONObject1.toString().getBytes("utf-8"));
          localGZIPOutputStream.write(0);
          if (paramUtnResponse.body != null)
            localGZIPOutputStream.write(paramUtnResponse.body);
          localGZIPOutputStream.close();
          ((ByteArrayOutputStream)localObject).close();
          paramUtnResponse = ((ByteArrayOutputStream)localObject).toByteArray();
          break;
        }
      }
    }
  }

  public void append(ResponseBlock paramResponseBlock)
  {
    if (this.blocks == null)
      this.blocks = new ResponseBlock[paramResponseBlock.count];
    this.blocks[paramResponseBlock.index] = paramResponseBlock;
  }

  public int finished()
  {
    int k;
    if (this.blocks == null)
    {
      k = 0;
      return k;
    }
    int i = 0;
    int j = 0;
    int m = this.blocks.length;
    while (true)
    {
      k = i;
      if (j >= m)
        break;
      k = i;
      if (this.blocks[j] != null)
        k = i + 1;
      j += 1;
      i = k;
    }
  }

  public UtnResponse getResponse()
    throws Exception
  {
    if (!isFinished())
      throw new Exception("response chain not finished");
    Object localObject1 = new ByteArrayOutputStream(this.blocks.length * 1384);
    Object localObject2 = this.blocks;
    int j = localObject2.length;
    int i = 0;
    while (i < j)
    {
      ((ByteArrayOutputStream)localObject1).write(localObject2[i].fragment);
      i += 1;
    }
    ((ByteArrayOutputStream)localObject1).close();
    localObject2 = new ByteArrayInputStream(((ByteArrayOutputStream)localObject1).toByteArray());
    Object localObject3 = new GZIPInputStream((InputStream)localObject2);
    ((ByteArrayOutputStream)localObject1).reset();
    Object localObject4 = new byte[4096];
    while (true)
    {
      i = ((GZIPInputStream)localObject3).read(localObject4);
      if (i == -1)
        break;
      ((ByteArrayOutputStream)localObject1).write(localObject4, 0, i);
    }
    ((GZIPInputStream)localObject3).close();
    ((ByteArrayInputStream)localObject2).close();
    ((ByteArrayOutputStream)localObject1).close();
    localObject2 = ((ByteArrayOutputStream)localObject1).toByteArray();
    j = localObject2.length;
    i = 0;
    while (true)
    {
      Iterator localIterator;
      if ((i >= j) || (localObject2[i] == 0))
      {
        localObject3 = new JSONObject(new String(localObject2, 0, i, "utf-8"));
        localObject1 = new byte[j - i - 1];
        System.arraycopy(localObject2, i + 1, localObject1, 0, j - i - 1);
        localObject2 = new UtnResponse();
        ((UtnResponse)localObject2).statusCode = ((JSONObject)localObject3).getInt("c");
        localObject3 = ((JSONObject)localObject3).optJSONObject("h");
        if (localObject3 == null)
          break;
        localObject4 = new HashMap();
        localIterator = ((JSONObject)localObject3).keys();
      }
      else
      {
        while (true)
          if (localIterator.hasNext())
          {
            String str = (String)localIterator.next();
            ((HashMap)localObject4).put(str, ((JSONObject)localObject3).getString(str));
            continue;
            i += 1;
            break;
          }
        ((UtnResponse)localObject2).headers = ((Map)localObject4);
      }
    }
    ((UtnResponse)localObject2).body = ((B)localObject1);
    return (UtnResponse)(UtnResponse)(UtnResponse)(UtnResponse)localObject2;
  }

  public boolean isFinished()
  {
    int k = 0;
    int i = total();
    int j = k;
    if (i > 0)
    {
      j = k;
      if (finished() == i)
        j = 1;
    }
    return j;
  }

  public int total()
  {
    if (this.blocks == null)
      return 0;
    return this.blocks.length;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.utn.ResponseChain
 * JD-Core Version:    0.6.0
 */