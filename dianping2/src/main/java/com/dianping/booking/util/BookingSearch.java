package com.dianping.booking.util;

public abstract interface BookingSearch
{
  public abstract String getSearchKey();

  public abstract BookingSearch parseAddress(String paramString);

  public abstract String toInfoString();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.util.BookingSearch
 * JD-Core Version:    0.6.0
 */