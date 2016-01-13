package com.dianping.statistics.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MaxBlockingItem
{
  public static final int EMPTY = -1;
  private volatile int item = -1;
  final Lock lock = new ReentrantLock();
  final Condition notEmpty = this.lock.newCondition();

  public int peek()
  {
    return this.item;
  }

  public void put(int paramInt)
  {
    if (paramInt < -1)
      throw new IllegalArgumentException("x must be greater than 0");
    this.lock.lock();
    try
    {
      if (paramInt > this.item);
      for (int i = paramInt; ; i = this.item)
      {
        this.item = i;
        if (paramInt != -1)
          this.notEmpty.signal();
        return;
      }
    }
    finally
    {
      this.lock.unlock();
    }
    throw localObject;
  }

  public int take()
    throws InterruptedException
  {
    this.lock.lock();
    try
    {
      while (this.item == -1)
        this.notEmpty.await();
    }
    finally
    {
      this.lock.unlock();
    }
    int i = this.item;
    this.item = -1;
    this.lock.unlock();
    return i;
  }

  public int tryTake(long paramLong)
    throws InterruptedException
  {
    this.lock.lock();
    try
    {
      while (this.item == -1)
      {
        boolean bool = this.notEmpty.await(paramLong, TimeUnit.MILLISECONDS);
        if (!bool)
          return -1;
      }
      int i = this.item;
      this.item = -1;
      return i;
    }
    finally
    {
      this.lock.unlock();
    }
    throw localObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.statistics.impl.MaxBlockingItem
 * JD-Core Version:    0.6.0
 */