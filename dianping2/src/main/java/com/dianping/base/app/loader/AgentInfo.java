package com.dianping.base.app.loader;

public class AgentInfo
{
  public Class<? extends CellAgent> agentClass;
  public String index;

  public AgentInfo(Class<? extends CellAgent> paramClass, String paramString)
  {
    if (paramString == null)
      throw new RuntimeException("index 不许为null 可以传空字符串");
    this.agentClass = paramClass;
    this.index = paramString;
  }

  public String toString()
  {
    return this.agentClass.getSimpleName() + " " + this.index;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.AgentInfo
 * JD-Core Version:    0.6.0
 */