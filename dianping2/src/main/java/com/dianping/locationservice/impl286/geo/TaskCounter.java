package com.dianping.locationservice.impl286.geo;

import com.dianping.util.Log;

public class TaskCounter
{
  private int mTaskCount = 0;

  public void addTask()
  {
    monitorenter;
    try
    {
      this.mTaskCount += 1;
      Log.d("task count: " + this.mTaskCount);
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

  public void clearTask()
  {
    monitorenter;
    try
    {
      this.mTaskCount = 0;
      Log.d("task count: " + this.mTaskCount);
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

  public int getTaskCount()
  {
    monitorenter;
    try
    {
      int i = this.mTaskCount;
      monitorexit;
      return i;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public void removeTask()
  {
    monitorenter;
    try
    {
      this.mTaskCount -= 1;
      Log.d("task count: " + this.mTaskCount);
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.geo.TaskCounter
 * JD-Core Version:    0.6.0
 */