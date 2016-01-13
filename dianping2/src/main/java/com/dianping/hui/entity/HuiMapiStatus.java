package com.dianping.hui.entity;

public enum HuiMapiStatus
{
  static
  {
    STATUS_FINISH = new HuiMapiStatus("STATUS_FINISH", 1);
    STATUS_FAIL = new HuiMapiStatus("STATUS_FAIL", 2);
    $VALUES = new HuiMapiStatus[] { STATUS_START, STATUS_FINISH, STATUS_FAIL };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.entity.HuiMapiStatus
 * JD-Core Version:    0.6.0
 */