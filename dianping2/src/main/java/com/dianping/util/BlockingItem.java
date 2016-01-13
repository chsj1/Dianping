package com.dianping.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingItem<T>
{
  private volatile T item;
  final Lock lock = new ReentrantLock();
  final Condition notEmpty = this.lock.newCondition();

  public T peek()
  {
    return this.item;
  }

  public void put(T paramT)
  {
    this.lock.lock();
    try
    {
      this.item = paramT;
      if (paramT != null)
        this.notEmpty.signal();
      return;
    }
    finally
    {
      this.lock.unlock();
    }
    throw paramT;
  }

  public T take()
    throws InterruptedException
  {
    this.lock.lock();
    try
    {
      while (this.item == null)
        this.notEmpty.await();
    }
    finally
    {
      this.lock.unlock();
    }
    Object localObject2 = this.item;
    this.item = null;
    this.lock.unlock();
    return localObject2;
  }

  public T tryTake(long paramLong)
    throws InterruptedException
  {
    this.lock.lock();
    try
    {
      while (this.item == null)
      {
        boolean bool = this.notEmpty.await(paramLong, TimeUnit.MILLISECONDS);
        if (!bool)
          return null;
      }
      Object localObject1 = this.item;
      this.item = null;
      return localObject1;
    }
    finally
    {
      this.lock.unlock();
    }
    throw localObject2;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.BlockingItem
 * JD-Core Version:    0.6.0
 */