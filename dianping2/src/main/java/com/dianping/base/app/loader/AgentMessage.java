package com.dianping.base.app.loader;

import android.os.Bundle;

public class AgentMessage
{
  public boolean blocked;
  public Bundle body;
  public boolean callback;
  public boolean dispatched;
  public CellAgent host;
  public CellAgent target;
  public String what;

  public AgentMessage(String paramString)
  {
    this.what = paramString;
    this.body = new Bundle();
    this.callback = false;
    this.dispatched = false;
    this.blocked = false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.AgentMessage
 * JD-Core Version:    0.6.0
 */