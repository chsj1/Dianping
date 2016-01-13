package com.dianping.archive;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Unarchiver
{
  private byte[] __strBuf;
  protected ByteBuffer byteBuffer;

  public Unarchiver(ByteBuffer paramByteBuffer)
  {
    this.byteBuffer = paramByteBuffer;
    paramByteBuffer.order(ByteOrder.BIG_ENDIAN);
  }

  public Unarchiver(byte[] paramArrayOfByte)
  {
    this.byteBuffer = ByteBuffer.wrap(paramArrayOfByte);
    this.byteBuffer.order(ByteOrder.BIG_ENDIAN);
  }

  public ByteBuffer byteBuffer()
  {
    return this.byteBuffer;
  }

  public boolean eof()
  {
    return !this.byteBuffer.hasRemaining();
  }

  protected byte peekByte()
  {
    return this.byteBuffer.get(this.byteBuffer.position());
  }

  public Object read(DecodingFactory<?> paramDecodingFactory)
    throws ArchiveException
  {
    int i = peekByte();
    if (i == 78)
      return null;
    if (i == 79)
      return readObject(paramDecodingFactory);
    if (i == 65)
      return readArray(paramDecodingFactory);
    throw new ArchiveException("unable to read object: " + this);
  }

  public Object readAnyObject(DecodingFactory<?> paramDecodingFactory)
    throws ArchiveException
  {
    switch (peekByte())
    {
    case 69:
    case 71:
    case 72:
    case 74:
    case 75:
    case 77:
    case 80:
    case 81:
    case 82:
    default:
      throw new ArchiveException("unable to read object (any): " + this);
    case 73:
      return Integer.valueOf(readInt());
    case 83:
      return readString();
    case 78:
      this.byteBuffer.get();
      return null;
    case 84:
      this.byteBuffer.get();
      return Boolean.TRUE;
    case 70:
      this.byteBuffer.get();
      return Boolean.FALSE;
    case 76:
      return Long.valueOf(readLong());
    case 68:
      return Double.valueOf(readDouble());
    case 85:
      return Long.valueOf(readDate());
    case 79:
    }
    return readObject(paramDecodingFactory);
  }

  public <T> T[] readArray(DecodingFactory<T> paramDecodingFactory)
    throws ArchiveException
  {
    int i = this.byteBuffer.get();
    Object localObject;
    if (i == 78)
    {
      localObject = null;
      return localObject;
    }
    if (i == 65)
    {
      int j = this.byteBuffer.getShort() & 0xFFFF;
      Object[] arrayOfObject = paramDecodingFactory.createArray(j);
      i = 0;
      while (true)
      {
        localObject = arrayOfObject;
        if (i >= j)
          break;
        arrayOfObject[i] = readObject(paramDecodingFactory);
        i += 1;
      }
    }
    throw new ArchiveException("unable to read array (object): " + this);
  }

  public boolean readBoolean()
    throws ArchiveException
  {
    int i = this.byteBuffer.get();
    if (i == 84)
      return true;
    if (i == 70)
      return false;
    throw new ArchiveException("unable to read boolean: " + this);
  }

  public DPObject readDPObject()
    throws ArchiveException
  {
    int i = this.byteBuffer.position();
    int j = this.byteBuffer.get(i);
    if (j == 78)
    {
      this.byteBuffer.get();
      return null;
    }
    if (j == 79)
    {
      skipAnyObject();
      return new DPObject(this.byteBuffer.array(), i, this.byteBuffer.position() - i);
    }
    throw new ArchiveException("unable to read dpobject: " + this);
  }

  public long readDate()
    throws ArchiveException
  {
    if (this.byteBuffer.get() == 85)
      return this.byteBuffer.getInt() * 1000L;
    throw new ArchiveException("unable to read date: " + this);
  }

  public double readDouble()
    throws ArchiveException
  {
    if (this.byteBuffer.get() == 68)
      return this.byteBuffer.getDouble();
    throw new ArchiveException("unable to read double: " + this);
  }

  public double[] readDoubleArray()
    throws ArchiveException
  {
    int i = this.byteBuffer.get();
    Object localObject;
    if (i == 78)
    {
      localObject = null;
      return localObject;
    }
    if (i == 65)
    {
      int j = this.byteBuffer.getShort() & 0xFFFF;
      double[] arrayOfDouble = new double[j];
      i = 0;
      while (true)
      {
        localObject = arrayOfDouble;
        if (i >= j)
          break;
        arrayOfDouble[i] = readDouble();
        i += 1;
      }
    }
    throw new ArchiveException("unable to read array (double): " + this);
  }

  public int readInt()
    throws ArchiveException
  {
    if (this.byteBuffer.get() == 73)
      return this.byteBuffer.getInt();
    throw new ArchiveException("unable to read int: " + this);
  }

  public int[] readIntArray()
    throws ArchiveException
  {
    int i = this.byteBuffer.get();
    Object localObject;
    if (i == 78)
    {
      localObject = null;
      return localObject;
    }
    if (i == 65)
    {
      int j = this.byteBuffer.getShort() & 0xFFFF;
      int[] arrayOfInt = new int[j];
      i = 0;
      while (true)
      {
        localObject = arrayOfInt;
        if (i >= j)
          break;
        arrayOfInt[i] = readInt();
        i += 1;
      }
    }
    throw new ArchiveException("unable to read array (int): " + this);
  }

  public long readLong()
    throws ArchiveException
  {
    if (this.byteBuffer.get() == 76)
      return this.byteBuffer.getLong();
    throw new ArchiveException("unable to read long: " + this);
  }

  public long[] readLongArray()
    throws ArchiveException
  {
    int i = this.byteBuffer.get();
    Object localObject;
    if (i == 78)
    {
      localObject = null;
      return localObject;
    }
    if (i == 65)
    {
      int j = this.byteBuffer.getShort() & 0xFFFF;
      long[] arrayOfLong = new long[j];
      i = 0;
      while (true)
      {
        localObject = arrayOfLong;
        if (i >= j)
          break;
        arrayOfLong[i] = readLong();
        i += 1;
      }
    }
    throw new ArchiveException("unable to read array (long): " + this);
  }

  public int readMemberHash16()
    throws ArchiveException
  {
    int i = this.byteBuffer.get();
    if (i == 77)
      return this.byteBuffer.getShort() & 0xFFFF;
    if (i == 90)
      return 0;
    throw new ArchiveException("unable to read member hash 16: " + this);
  }

  public <T> T readObject(DecodingFactory<T> paramDecodingFactory)
    throws ArchiveException
  {
    int i = this.byteBuffer.get();
    if (i == 78)
      return null;
    if (i == 79)
    {
      i = this.byteBuffer.getShort() & 0xFFFF;
      paramDecodingFactory = paramDecodingFactory.createInstance(i);
      if (paramDecodingFactory == null)
        throw new ArchiveException("unable to create instance: " + Integer.toHexString(i));
      if ((paramDecodingFactory instanceof Decoding))
      {
        ((Decoding)paramDecodingFactory).decode(this);
        return paramDecodingFactory;
      }
      throw new ArchiveException("unable to decode class: " + paramDecodingFactory.getClass().getSimpleName());
    }
    throw new ArchiveException("unable to read object: " + this);
  }

  public String readString()
    throws ArchiveException
  {
    int i = this.byteBuffer.get();
    if (i == 83)
    {
      i = this.byteBuffer.getShort() & 0xFFFF;
      int j = (i / 4096 + 1) * 4096;
      if ((this.__strBuf == null) || (this.__strBuf.length < j))
        this.__strBuf = new byte[j];
      this.byteBuffer.get(this.__strBuf, 0, i);
      try
      {
        String str = new String(this.__strBuf, 0, i, "utf-8");
        return str;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        throw new ArchiveException("unable to encode string");
      }
    }
    if (i == 78)
      return null;
    throw new ArchiveException("unable to read string: " + this);
  }

  public String[] readStringArray()
    throws ArchiveException
  {
    int i = this.byteBuffer.get();
    Object localObject;
    if (i == 78)
    {
      localObject = null;
      return localObject;
    }
    if (i == 65)
    {
      int j = this.byteBuffer.getShort() & 0xFFFF;
      String[] arrayOfString = new String[j];
      i = 0;
      while (true)
      {
        localObject = arrayOfString;
        if (i >= j)
          break;
        arrayOfString[i] = readString();
        i += 1;
      }
    }
    throw new ArchiveException("unable to read array (string): " + this);
  }

  public void skipAnyObject()
    throws ArchiveException
  {
    switch (this.byteBuffer.get())
    {
    case 66:
    case 67:
    case 69:
    case 71:
    case 72:
    case 74:
    case 75:
    case 77:
    case 80:
    case 81:
    case 82:
    default:
      throw new ArchiveException("unable to skip object: " + this);
    case 73:
      this.byteBuffer.getInt();
    case 70:
    case 78:
    case 84:
    case 83:
    case 76:
    case 68:
    case 85:
    case 79:
    case 65:
    }
    while (true)
    {
      return;
      int i = this.byteBuffer.getShort();
      this.byteBuffer.position(this.byteBuffer.position() + (i & 0xFFFF));
      return;
      this.byteBuffer.getLong();
      return;
      this.byteBuffer.getDouble();
      return;
      this.byteBuffer.getInt();
      return;
      this.byteBuffer.getShort();
      while (readMemberHash16() > 0)
        skipAnyObject();
      continue;
      int j = this.byteBuffer.getShort();
      i = 0;
      while (i < (j & 0xFFFF))
      {
        skipAnyObject();
        i += 1;
      }
    }
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer(20);
    this.byteBuffer.mark();
    try
    {
      localStringBuffer.append("@").append(this.byteBuffer.position()).append("(x").append(Integer.toHexString(this.byteBuffer.position())).append("): ");
      int i = 0;
      while (i < 6)
      {
        if (this.byteBuffer.hasRemaining())
        {
          localStringBuffer.append(Integer.toHexString(this.byteBuffer.get() & 0xFF));
          i += 1;
          continue;
        }
        localStringBuffer.append("(EOF)");
      }
      return localStringBuffer.toString();
    }
    catch (Exception localException)
    {
      while (true)
      {
        if (eof())
          localStringBuffer.append("EOF");
        this.byteBuffer.reset();
      }
    }
    finally
    {
      this.byteBuffer.reset();
    }
    throw localObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.archive.Unarchiver
 * JD-Core Version:    0.6.0
 */