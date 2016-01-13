package com.dianping.utn;

import java.net.DatagramPacket;
import java.util.Random;
import java.util.zip.CRC32;

public class ResponseBlock
{
  private static final byte[] EMPTY = new byte[0];
  public static final int MAX_FRAGMENT = 1384;
  public static final int MAX_LEN = 1400;
  public int count;
  public byte[] fragment;
  public int index;
  public int protocolVersion = 1;
  public int requestId;

  public void pack(DatagramPacket paramDatagramPacket)
    throws Exception
  {
    byte[] arrayOfByte = pack();
    System.arraycopy(arrayOfByte, 0, paramDatagramPacket.getData(), 0, arrayOfByte.length);
    paramDatagramPacket.setLength(arrayOfByte.length);
  }

  public byte[] pack()
    throws Exception
  {
    if (this.fragment == null);
    for (int i = 0; i > 1384; i = this.fragment.length)
      throw new Exception("fragment is too big");
    if ((this.count > 255) || (this.index > 255))
      throw new Exception("index out fo range (0xff)");
    byte[] arrayOfByte = new byte[i + 16];
    if (i > 0)
      System.arraycopy(this.fragment, 0, arrayOfByte, 16, i);
    arrayOfByte[15] = (byte)this.index;
    arrayOfByte[14] = (byte)this.count;
    arrayOfByte[13] = (byte)(this.requestId & 0xFF);
    arrayOfByte[12] = (byte)(this.requestId >>> 8 & 0xFF);
    arrayOfByte[11] = (byte)(this.requestId >>> 16 & 0xFF);
    arrayOfByte[10] = (byte)(this.requestId >>> 24 & 0xFF);
    i = 0xF000 & this.protocolVersion << 12;
    arrayOfByte[9] = (byte)(i & 0xFF);
    arrayOfByte[8] = (byte)(i >>> 8 & 0xFF);
    CRC32 localCRC32 = new CRC32();
    localCRC32.update(arrayOfByte, 8, arrayOfByte.length - 8);
    long l = localCRC32.getValue();
    i = arrayOfByte.length - 4;
    arrayOfByte[0] = 78;
    arrayOfByte[1] = 88;
    arrayOfByte[2] = (byte)(i >>> 8 & 0xFF);
    arrayOfByte[3] = (byte)(i & 0xFF);
    arrayOfByte[4] = (byte)(int)(l >>> 24 & 0xFF);
    arrayOfByte[5] = (byte)(int)(l >>> 16 & 0xFF);
    arrayOfByte[6] = (byte)(int)(l >>> 8 & 0xFF);
    arrayOfByte[7] = (byte)(int)(0xFF & l);
    return arrayOfByte;
  }

  public void parse(DatagramPacket paramDatagramPacket)
    throws Exception
  {
    byte[] arrayOfByte = paramDatagramPacket.getData();
    int k = paramDatagramPacket.getOffset();
    int i = k + paramDatagramPacket.getLength();
    if (i - k < 16)
      throw new Exception("package too small");
    int j = k + 1;
    if (arrayOfByte[k] == 78)
    {
      k = j + 1;
      if (arrayOfByte[j] == 88);
    }
    while (true)
    {
      throw new Exception("no magic number");
      j = k + 1;
      int m = arrayOfByte[k];
      k = j + 1;
      j = (m & 0xFF) << 8 | arrayOfByte[j] & 0xFF;
      if (i - k < j)
        throw new Exception("buffer < length");
      m = k + 1;
      k = arrayOfByte[k];
      int n = m + 1;
      m = arrayOfByte[m];
      int i1 = n + 1;
      int i2 = arrayOfByte[n];
      n = i1 + 1;
      i1 = arrayOfByte[i1];
      paramDatagramPacket = new CRC32();
      paramDatagramPacket.reset();
      paramDatagramPacket.update(arrayOfByte, n, j - 4);
      if (paramDatagramPacket.getValue() != (0xFFFFFFFF & ((k & 0xFF) << 24 | (m & 0xFF) << 16 | (i2 & 0xFF) << 8 | i1 & 0xFF)))
        throw new Exception("crc32 checksum fail");
      j = n + 1;
      m = arrayOfByte[n];
      k = j + 1;
      this.protocolVersion = ((0xF000 & ((m & 0xFF) << 8 | arrayOfByte[j] & 0xFF)) >> 12);
      if (this.protocolVersion != 1)
        throw new Exception("unsupported protocol " + this.protocolVersion);
      m = k + 1;
      j = arrayOfByte[k];
      n = m + 1;
      k = arrayOfByte[m];
      m = n + 1;
      i1 = arrayOfByte[n];
      n = m + 1;
      this.requestId = ((j & 0xFF) << 24 | (k & 0xFF) << 16 | (i1 & 0xFF) << 8 | arrayOfByte[m] & 0xFF);
      j = n + 1;
      this.count = (arrayOfByte[n] & 0xFF);
      k = j + 1;
      this.index = (arrayOfByte[j] & 0xFF);
      if (i > k)
      {
        this.fragment = new byte[i - k];
        System.arraycopy(arrayOfByte, k, this.fragment, 0, i - k);
        return;
      }
      this.fragment = EMPTY;
      return;
    }
  }

  public void random(Random paramRandom)
  {
    this.protocolVersion = 1;
    this.requestId = paramRandom.nextInt();
    this.count = (paramRandom.nextInt(255) + 1);
    this.index = paramRandom.nextInt(this.count);
    if (paramRandom.nextBoolean());
    for (this.fragment = new byte[1384]; ; this.fragment = new byte[paramRandom.nextInt(1384)])
    {
      paramRandom.nextBytes(this.fragment);
      return;
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("protocol.version = ").append(this.protocolVersion).append('\n');
    localStringBuilder.append("requestId = ").append(this.requestId).append('\n');
    localStringBuilder.append("index = ").append(this.index).append('/').append(this.count).append('\n');
    localStringBuilder.append("fragment = (").append(this.fragment.length).append(" bytes)\n");
    return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.utn.ResponseBlock
 * JD-Core Version:    0.6.0
 */