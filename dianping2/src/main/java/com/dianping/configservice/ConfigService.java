package com.dianping.configservice;

import org.json.JSONObject;

public abstract interface ConfigService
{
  public static final String ANY = "*";

  public abstract void addListener(String paramString, ConfigChangeListener paramConfigChangeListener);

  public abstract JSONObject dump();

  public abstract void refresh();

  public abstract void removeListener(String paramString, ConfigChangeListener paramConfigChangeListener);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.configservice.ConfigService
 * JD-Core Version:    0.6.0
 */