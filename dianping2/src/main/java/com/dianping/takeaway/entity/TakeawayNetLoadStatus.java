package com.dianping.takeaway.entity;

public enum TakeawayNetLoadStatus
{
  static
  {
    STATUS_FAILED = new TakeawayNetLoadStatus("STATUS_FAILED", 2);
    STATUS_FINISH_BEFORE = new TakeawayNetLoadStatus("STATUS_FINISH_BEFORE", 3);
    $VALUES = new TakeawayNetLoadStatus[] { STATUS_START, STATUS_SUCCESS, STATUS_FAILED, STATUS_FINISH_BEFORE };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayNetLoadStatus
 * JD-Core Version:    0.6.0
 */