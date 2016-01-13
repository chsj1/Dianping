package com.dianping.queue.entity;

public abstract interface QueueMainDataSource$QueueDataLoaderListener
{
  public abstract void addInterest(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject);

  public abstract void cancelInterest(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject);

  public abstract void cancelOrderFinish(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject);

  public abstract void createOrderFinish(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject);

  public abstract void loadQueueInfo(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject);

  public abstract void setHobbitEntry(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject);

  public abstract void setReminderFinish(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.queue.entity.QueueMainDataSource.QueueDataLoaderListener
 * JD-Core Version:    0.6.0
 */