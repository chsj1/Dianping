package com.dianping.util;

import java.io.IOException;
import java.io.InputStream;

public abstract class WrapInputStream extends InputStream
{
  private IOException ex;
  private InputStream ins;

  private InputStream inputStream()
    throws IOException
  {
    monitorenter;
    try
    {
      if (this.ex != null)
        throw this.ex;
    }
    finally
    {
      monitorexit;
    }
    InputStream localInputStream = this.ins;
    if (localInputStream == null);
    try
    {
      this.ins = wrappedInputStream();
      localInputStream = this.ins;
      monitorexit;
      return localInputStream;
    }
    catch (IOException localIOException)
    {
      this.ex = localIOException;
    }
    throw this.ex;
  }

  public int available()
    throws IOException
  {
    return inputStream().available();
  }

  public void close()
    throws IOException
  {
    if (this.ins == null)
      return;
    inputStream().close();
  }

  public void mark(int paramInt)
  {
  }

  public boolean markSupported()
  {
    return true;
  }

  public int read()
    throws IOException
  {
    return inputStream().read();
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return inputStream().read(paramArrayOfByte);
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    return inputStream().read(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void reset()
    throws IOException
  {
    monitorenter;
    try
    {
      this.ins = null;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public long skip(long paramLong)
    throws IOException
  {
    return inputStream().skip(paramLong);
  }

  protected abstract InputStream wrappedInputStream()
    throws IOException;
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.WrapInputStream
 * JD-Core Version:    0.6.0
 */