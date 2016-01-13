package com.dianping.utn;

import B;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BaseRequest
{
  private static final byte[] EMPTY = new byte[0];
  public static final int MAX_LEN = 1400;
  public static final int NETWORK_2G = 0;
  public static final int NETWORK_3G = 1;
  public static final int NETWORK_4G = 2;
  public static final int NETWORK_WIFI = 3;
  public static final int TYPE_HTTP_ACK = 5;
  public static final int TYPE_HTTP_REQUEST = 4;
  public static final int TYPE_PING = 1;
  public int network;
  public int protocolVersion = 1;
  public int requestId;
  public int type;

  public static BaseRequest parseProtocol1(DatagramPacket paramDatagramPacket)
    throws Exception
  {
    if (paramDatagramPacket.getLength() < 10)
      throw new Exception("package too small");
    int j = paramDatagramPacket.getOffset();
    Object localObject = paramDatagramPacket.getData();
    int i = localObject[(j + 8)] >>> 4 & 0xF;
    j = localObject[(j + 9)] >>> 4 & 0xF;
    if (i != 1)
      throw new Exception("unsupported protocol (" + i + ")");
    switch (j)
    {
    case 2:
    case 3:
    default:
      throw new Exception("unknown type (" + j + ")");
    case 1:
      localObject = new PingRequest();
      ((PingRequest)localObject).parse(paramDatagramPacket);
      return localObject;
    case 4:
      localObject = new HttpRequest();
      ((HttpRequest)localObject).parse(paramDatagramPacket);
      return localObject;
    case 5:
    }
    localObject = new HttpAck();
    ((HttpAck)localObject).parse(paramDatagramPacket);
    return (BaseRequest)localObject;
  }

  protected byte[] packRaw(byte[] paramArrayOfByte)
    throws Exception
  {
    Object localObject = new ByteArrayOutputStream(1400);
    ((ByteArrayOutputStream)localObject).write(78);
    ((ByteArrayOutputStream)localObject).write(88);
    ((ByteArrayOutputStream)localObject).write(0);
    ((ByteArrayOutputStream)localObject).write(0);
    ((ByteArrayOutputStream)localObject).write(0);
    ((ByteArrayOutputStream)localObject).write(0);
    ((ByteArrayOutputStream)localObject).write(0);
    ((ByteArrayOutputStream)localObject).write(0);
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length < 24))
    {
      i = 0;
      int j = (this.protocolVersion & 0xF) << 12 | (i & 0x3) << 10 | (this.network & 0x3) << 8 | (this.type & 0xF) << 4;
      ((ByteArrayOutputStream)localObject).write(j >>> 8 & 0xFF);
      ((ByteArrayOutputStream)localObject).write(j & 0xFF);
      ((ByteArrayOutputStream)localObject).write(this.requestId >>> 24 & 0xFF);
      ((ByteArrayOutputStream)localObject).write(this.requestId >>> 16 & 0xFF);
      ((ByteArrayOutputStream)localObject).write(this.requestId >>> 8 & 0xFF);
      ((ByteArrayOutputStream)localObject).write(this.requestId & 0xFF);
      if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0))
      {
        if (i != 0)
          break label239;
        ((ByteArrayOutputStream)localObject).write(paramArrayOfByte);
      }
    }
    while (true)
    {
      ((ByteArrayOutputStream)localObject).close();
      paramArrayOfByte = ((ByteArrayOutputStream)localObject).toByteArray();
      if (paramArrayOfByte.length <= 1400)
        break label260;
      throw new Exception("too big to pack");
      i = 1;
      break;
      label239: GZIPOutputStream localGZIPOutputStream = new GZIPOutputStream((OutputStream)localObject);
      localGZIPOutputStream.write(paramArrayOfByte);
      localGZIPOutputStream.close();
    }
    label260: localObject = new CRC32();
    ((CRC32)localObject).update(paramArrayOfByte, 8, paramArrayOfByte.length - 8);
    long l = ((CRC32)localObject).getValue();
    int i = paramArrayOfByte.length - 4;
    paramArrayOfByte[2] = (byte)(i >>> 8 & 0xFF);
    paramArrayOfByte[3] = (byte)(i & 0xFF);
    paramArrayOfByte[4] = (byte)(int)(0xFF & l >>> 24);
    paramArrayOfByte[5] = (byte)(int)(0xFF & l >>> 16);
    paramArrayOfByte[6] = (byte)(int)(0xFF & l >>> 8);
    paramArrayOfByte[7] = (byte)(int)(0xFF & l);
    return (B)paramArrayOfByte;
  }

  protected byte[] parseRaw(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws Exception
  {
    paramInt2 = paramInt1 + paramInt2;
    if (paramInt2 - paramInt1 < 14)
      throw new Exception("package too small");
    int i = paramInt1 + 1;
    int j;
    if (paramArrayOfByte[paramInt1] == 78)
    {
      j = i + 1;
      if (paramArrayOfByte[i] == 88);
    }
    while (true)
    {
      throw new Exception("no magic number");
      paramInt1 = j + 1;
      j = paramArrayOfByte[j];
      i = paramInt1 + 1;
      paramInt1 = (j & 0xFF) << 8 | paramArrayOfByte[paramInt1] & 0xFF;
      if (paramInt2 - i < paramInt1)
        throw new Exception("buffer < length");
      int k = i + 1;
      paramInt2 = paramArrayOfByte[i];
      j = k + 1;
      i = paramArrayOfByte[k];
      k = j + 1;
      int m = paramArrayOfByte[j];
      j = k + 1;
      k = paramArrayOfByte[k];
      Object localObject = new CRC32();
      ((CRC32)localObject).reset();
      ((CRC32)localObject).update(paramArrayOfByte, j, paramInt1 - 4);
      if (((CRC32)localObject).getValue() != (0xFFFFFFFF & ((paramInt2 & 0xFF) << 24 | (i & 0xFF) << 16 | (m & 0xFF) << 8 | k & 0xFF)))
        throw new Exception("crc32 checksum fail");
      paramInt2 = j + 1;
      j = paramArrayOfByte[j];
      i = paramInt2 + 1;
      j = (j & 0xFF) << 8 | paramArrayOfByte[paramInt2] & 0xFF;
      this.protocolVersion = (j >>> 12 & 0xF);
      paramInt2 = j >>> 10 & 0x3;
      this.network = (j >>> 8 & 0x3);
      this.type = (j >>> 4 & 0xF);
      if (this.protocolVersion != 1)
        throw new Exception("unsupported protocol " + this.protocolVersion);
      j = i + 1;
      i = paramArrayOfByte[i];
      m = j + 1;
      j = paramArrayOfByte[j];
      k = m + 1;
      m = paramArrayOfByte[m];
      int n = k + 1;
      this.requestId = ((i & 0xFF) << 24 | (j & 0xFF) << 16 | (m & 0xFF) << 8 | paramArrayOfByte[k] & 0xFF);
      if (paramInt1 > 10)
      {
        if (paramInt2 == 0)
        {
          localObject = new byte[paramInt1 - 10];
          System.arraycopy(paramArrayOfByte, n, localObject, 0, paramInt1 - 10);
          return localObject;
        }
        if (paramInt2 == 1)
        {
          localObject = new ByteArrayInputStream(paramArrayOfByte, n, paramInt1 - 10);
          GZIPInputStream localGZIPInputStream = new GZIPInputStream((InputStream)localObject);
          paramArrayOfByte = new byte[8000];
          paramInt1 = 0;
          while (true)
          {
            paramInt2 = localGZIPInputStream.read(paramArrayOfByte, paramInt1, paramArrayOfByte.length - paramInt1);
            if (paramInt2 == -1)
              break;
            paramInt2 = paramInt1 + paramInt2;
            paramInt1 = paramInt2;
            if (paramInt2 < paramArrayOfByte.length)
              continue;
            throw new Exception("buffer overflow");
          }
          localGZIPInputStream.close();
          ((ByteArrayInputStream)localObject).close();
          localObject = new byte[paramInt1];
          System.arraycopy(paramArrayOfByte, 0, localObject, 0, paramInt1);
          return localObject;
        }
        throw new Exception("unsupported compress type " + paramInt2);
      }
      return EMPTY;
    }
  }

  public void random(Random paramRandom)
  {
    this.protocolVersion = 1;
    this.type = paramRandom.nextInt(16);
    this.network = paramRandom.nextInt(4);
    this.requestId = paramRandom.nextInt();
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("protocol.version = ").append(this.protocolVersion).append('\n');
    localStringBuilder.append("type = ").append(this.type);
    switch (this.type)
    {
    case 2:
    case 3:
    default:
      localStringBuilder.append('\n');
      localStringBuilder.append("network = ").append(this.network);
      switch (this.network)
      {
      default:
      case 0:
      case 1:
      case 2:
      case 3:
      }
    case 1:
    case 4:
    case 5:
    }
    while (true)
    {
      localStringBuilder.append('\n');
      localStringBuilder.append("requestId = ").append(this.requestId).append('\n');
      return localStringBuilder.toString();
      localStringBuilder.append(" (PING)");
      break;
      localStringBuilder.append(" (HTTP REQ)");
      break;
      localStringBuilder.append(" (HTTP ACK)");
      break;
      localStringBuilder.append(" (2G)");
      continue;
      localStringBuilder.append(" (3G)");
      continue;
      localStringBuilder.append(" (4G)");
      continue;
      localStringBuilder.append(" (WIFI)");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.utn.BaseRequest
 * JD-Core Version:    0.6.0
 */