package com.dianping.accountservice;

public abstract interface AccountListener
{
  public abstract void onAccountChanged(AccountService paramAccountService);

  public abstract void onProfileChanged(AccountService paramAccountService);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.accountservice.AccountListener
 * JD-Core Version:    0.6.0
 */