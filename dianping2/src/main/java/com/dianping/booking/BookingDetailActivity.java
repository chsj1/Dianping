package com.dianping.booking;

import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.booking.fragment.BookingDetailFragment;

public class BookingDetailActivity extends AgentActivity
{
  private BookingDetailFragment bookingDetailFragment = new BookingDetailFragment();

  protected AgentFragment getAgentFragment()
  {
    return this.bookingDetailFragment;
  }

  public String getPageName()
  {
    return "bookinginfo";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingDetailActivity
 * JD-Core Version:    0.6.0
 */