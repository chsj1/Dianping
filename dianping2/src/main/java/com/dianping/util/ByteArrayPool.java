package com.dianping.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ByteArrayPool
{
  protected static final Comparator<byte[]> BUF_COMPARATOR = new Comparator()
  {
    public int compare(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      return paramArrayOfByte1.length - paramArrayOfByte2.length;
    }
  };
  private List<byte[]> mBuffersByLastUse = new LinkedList();
  private List<byte[]> mBuffersBySize = new ArrayList(64);
  private int mCurrentSize = 0;
  private final int mSizeLimit;

  public ByteArrayPool(int paramInt)
  {
    this.mSizeLimit = paramInt;
  }

  private void trim()
  {
    monitorenter;
    try
    {
      if (this.mCurrentSize > this.mSizeLimit)
      {
        byte[] arrayOfByte = (byte[])this.mBuffersByLastUse.remove(0);
        this.mBuffersBySize.remove(arrayOfByte);
        this.mCurrentSize -= arrayOfByte.length;
      }
    }
    finally
    {
      monitorexit;
    }
  }

  public byte[] getBuf(int paramInt)
  {
    monitorenter;
    int i = 0;
    try
    {
      byte[] arrayOfByte;
      if (i < this.mBuffersBySize.size())
      {
        arrayOfByte = (byte[])this.mBuffersBySize.get(i);
        if (arrayOfByte.length >= paramInt)
        {
          this.mCurrentSize -= arrayOfByte.length;
          this.mBuffersBySize.remove(i);
          this.mBuffersByLastUse.remove(arrayOfByte);
        }
      }
      while (true)
      {
        return arrayOfByte;
        i += 1;
        break;
        arrayOfByte = new byte[paramInt];
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void returnBuf(byte[] paramArrayOfByte)
  {
    monitorenter;
    if (paramArrayOfByte != null);
    try
    {
      int i = paramArrayOfByte.length;
      int j = this.mSizeLimit;
      if (i > j);
      while (true)
      {
        return;
        this.mBuffersByLastUse.add(paramArrayOfByte);
        j = Collections.binarySearch(this.mBuffersBySize, paramArrayOfByte, BUF_COMPARATOR);
        i = j;
        if (j < 0)
          i = -j - 1;
        this.mBuffersBySize.add(i, paramArrayOfByte);
        this.mCurrentSize += paramArrayOfByte.length;
        trim();
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramArrayOfByte;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.ByteArrayPool
 * JD-Core Version:    0.6.0
 */