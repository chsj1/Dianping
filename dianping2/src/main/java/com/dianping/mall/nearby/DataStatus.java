package com.dianping.mall.nearby;

public enum DataStatus
{
  static
  {
    ERROR_NETWORK = new DataStatus("ERROR_NETWORK", 2);
    ERROR_NOSHOP = new DataStatus("ERROR_NOSHOP", 3);
    $VALUES = new DataStatus[] { INITIAL, NORMAL, ERROR_NETWORK, ERROR_NOSHOP };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.mall.nearby.DataStatus
 * JD-Core Version:    0.6.0
 */