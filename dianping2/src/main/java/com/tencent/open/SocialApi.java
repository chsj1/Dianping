package com.tencent.open;

import android.app.Activity;
import android.os.Bundle;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;

public class SocialApi
{
  private SocialApiIml a;

  public SocialApi(QQToken paramQQToken)
  {
    this.a = new SocialApiIml(paramQQToken);
  }

  public void ask(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    this.a.ask(paramActivity, paramBundle, paramIUiListener);
  }

  public void gift(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    this.a.gift(paramActivity, paramBundle, paramIUiListener);
  }

  public void invite(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    this.a.invite(paramActivity, paramBundle, paramIUiListener);
  }

  public void story(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    this.a.story(paramActivity, paramBundle, paramIUiListener);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.open.SocialApi
 * JD-Core Version:    0.6.0
 */