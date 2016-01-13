package com.tencent.open.utils;

public final class ZipShort
  implements Cloneable
{
  private int a;

  public ZipShort(int paramInt)
  {
    this.a = paramInt;
  }

  public ZipShort(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, 0);
  }

  public ZipShort(byte[] paramArrayOfByte, int paramInt)
  {
    this.a = (paramArrayOfByte[(paramInt + 1)] << 8 & 0xFF00);
    this.a += (paramArrayOfByte[paramInt] & 0xFF);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof ZipShort)));
    do
      return false;
    while (this.a != ((ZipShort)paramObject).getValue());
    return true;
  }

  public byte[] getBytes()
  {
    return new byte[] { (byte)(this.a & 0xFF), (byte)((this.a & 0xFF00) >> 8) };
  }

  public int getValue()
  {
    return this.a;
  }

  public int hashCode()
  {
    return this.a;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.open.utils.ZipShort
 * JD-Core Version:    0.6.0
 */