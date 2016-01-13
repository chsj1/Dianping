package com.dianping.base.sso;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.dianping.accountservice.impl.DefaultAccountService;
import com.dianping.app.DPApplication;

public class PosSSOBroadCast extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    Object localObject = (DefaultAccountService)DPApplication.instance().getService("account");
    paramIntent = ((DefaultAccountService)localObject).token();
    String str = ((DefaultAccountService)localObject).newToken();
    int i = ((DefaultAccountService)localObject).id();
    localObject = new Intent();
    ((Intent)localObject).setComponent(new ComponentName("com.dianping.dppos", "com.dianping.dppos.NovaSSOReceiver"));
    ((Intent)localObject).setAction("com.dianping.dppos.action.SSO_NOTIFICATION");
    ((Intent)localObject).putExtra("token", paramIntent);
    ((Intent)localObject).putExtra("newtoken", str);
    ((Intent)localObject).putExtra("uid", i);
    paramContext.sendBroadcast((Intent)localObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.sso.PosSSOBroadCast
 * JD-Core Version:    0.6.0
 */