package com.dianping.util;

import java.io.IOException;
import java.io.InputStream;

public class ChainInputStream extends InputStream
{
  protected int curoffset;
  protected int curs;
  protected InputStream[] streams;

  public ChainInputStream(InputStream[] paramArrayOfInputStream)
  {
    this.streams = paramArrayOfInputStream;
    this.curs = 0;
  }

  public int available()
    throws IOException
  {
    int j = 0;
    int i = this.curs;
    int k = this.streams.length;
    while (i < k)
    {
      int m = this.streams[i].available();
      if (m <= 0)
        return 0;
      j += m;
      i += 1;
    }
    return j - this.curoffset;
  }

  public void close()
    throws IOException
  {
    monitorenter;
    try
    {
      InputStream[] arrayOfInputStream = this.streams;
      int j = arrayOfInputStream.length;
      int i = 0;
      while (i < j)
      {
        arrayOfInputStream[i].close();
        i += 1;
      }
      this.curoffset = 0;
      this.curs = 0;
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void mark(int paramInt)
  {
    monitorenter;
    try
    {
      throw new UnsupportedOperationException();
    }
    finally
    {
    }
    throw localObject;
  }

  public boolean markSupported()
  {
    return false;
  }

  public int read()
    throws IOException
  {
    int i = this.streams[this.curs].read();
    if (i < 0)
    {
      if (this.curs < this.streams.length - 1)
      {
        this.curs += 1;
        this.curoffset = 0;
        i = read();
      }
      return i;
    }
    this.curoffset += 1;
    return i;
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = this.streams[this.curs].read(paramArrayOfByte, paramInt1, paramInt2);
    if (i < 0)
    {
      if (this.curs < this.streams.length - 1)
      {
        this.curs += 1;
        this.curoffset = 0;
        i = read(paramArrayOfByte, paramInt1, paramInt2);
      }
      return i;
    }
    this.curoffset += i;
    return i;
  }

  public void reset()
    throws IOException
  {
    monitorenter;
    try
    {
      InputStream[] arrayOfInputStream = this.streams;
      int j = arrayOfInputStream.length;
      int i = 0;
      while (i < j)
      {
        arrayOfInputStream[i].reset();
        i += 1;
      }
      this.curoffset = 0;
      this.curs = 0;
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public long skip(long paramLong)
    throws IOException
  {
    throw new IOException("unsupported operation: skip");
  }

  public InputStream[] streams()
  {
    return this.streams;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.ChainInputStream
 * JD-Core Version:    0.6.0
 */