package com.dianping.accountservice;

public abstract interface LoginResultListener
{
  public abstract void onLoginCancel(AccountService paramAccountService);

  public abstract void onLoginSuccess(AccountService paramAccountService);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.accountservice.LoginResultListener
 * JD-Core Version:    0.6.0
 */