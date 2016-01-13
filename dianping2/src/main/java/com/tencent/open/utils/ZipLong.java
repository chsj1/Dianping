package com.tencent.open.utils;

public final class ZipLong
  implements Cloneable
{
  private long a;

  public ZipLong(long paramLong)
  {
    this.a = paramLong;
  }

  public ZipLong(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, 0);
  }

  public ZipLong(byte[] paramArrayOfByte, int paramInt)
  {
    this.a = (paramArrayOfByte[(paramInt + 3)] << 24 & 0xFF000000);
    this.a += (paramArrayOfByte[(paramInt + 2)] << 16 & 0xFF0000);
    this.a += (paramArrayOfByte[(paramInt + 1)] << 8 & 0xFF00);
    this.a += (paramArrayOfByte[paramInt] & 0xFF);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof ZipLong)));
    do
      return false;
    while (this.a != ((ZipLong)paramObject).getValue());
    return true;
  }

  public byte[] getBytes()
  {
    return new byte[] { (byte)(int)(this.a & 0xFF), (byte)(int)((this.a & 0xFF00) >> 8), (byte)(int)((this.a & 0xFF0000) >> 16), (byte)(int)((this.a & 0xFF000000) >> 24) };
  }

  public long getValue()
  {
    return this.a;
  }

  public int hashCode()
  {
    return (int)this.a;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.open.utils.ZipLong
 * JD-Core Version:    0.6.0
 */