package com.dianping.base.sso;

public abstract interface ILogininListener
{
  public abstract void onSSOLoginCancel(int paramInt);

  public abstract void onSSOLoginFailed(int paramInt);

  public abstract void onSSOLoginSucceed(int paramInt, Object paramObject);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.sso.ILogininListener
 * JD-Core Version:    0.6.0
 */