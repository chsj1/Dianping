package com.dianping.booking.agent;

import com.dianping.base.app.loader.CellAgent;
import java.util.Map;

public abstract class AbstractBookingInfoAgent extends CellAgent
{
  public AbstractBookingInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  public abstract Map<String, String> validateInput();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.agent.AbstractBookingInfoAgent
 * JD-Core Version:    0.6.0
 */