package com.dianping.base.app.loader;

import java.util.Map;

public abstract interface AgentListConfig
{
  public abstract Map<String, AgentInfo> getAgentInfoList();

  public abstract Map<String, Class<? extends CellAgent>> getAgentList();

  public abstract boolean shouldShow();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.AgentListConfig
 * JD-Core Version:    0.6.0
 */