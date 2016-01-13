package com.dianping.main.login.nativelogin;

import android.content.Intent;
import com.dianping.base.web.ui.NovaZeusActivity;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.zeus.ui.ZeusFragment;

public class LoginWebActivity extends NovaZeusActivity
{
  protected Class<? extends NovaZeusFragment> getEfteFragmentClass()
  {
    return LoginWebFragment.class;
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (this.mZeusFragment != null)
      this.mZeusFragment.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void refresh(String paramString)
  {
    if (this.mZeusFragment != null)
      ((LoginWebFragment)this.mZeusFragment).refresh(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.login.nativelogin.LoginWebActivity
 * JD-Core Version:    0.6.0
 */