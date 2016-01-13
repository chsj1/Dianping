package com.dianping.accountservice;

import com.dianping.archive.DPObject;
import java.util.List;
import org.apache.http.NameValuePair;

public abstract interface AccountService
{
  public abstract void addListener(AccountListener paramAccountListener);

  public abstract int id();

  public abstract void login(LoginResultListener paramLoginResultListener);

  public abstract void login(LoginResultListener paramLoginResultListener, List<NameValuePair> paramList);

  public abstract void logout();

  public abstract String newToken();

  public abstract DPObject profile();

  public abstract void removeListener(AccountListener paramAccountListener);

  public abstract void removeLoginResultListener();

  public abstract void signup(LoginResultListener paramLoginResultListener);

  public abstract String token();

  public abstract void update(DPObject paramDPObject);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.accountservice.AccountService
 * JD-Core Version:    0.6.0
 */