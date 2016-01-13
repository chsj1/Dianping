package com.dianping.base.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;

public class Monitor
  implements AccountListener, RequestHandler<MApiRequest, MApiResponse>
{
  public static final String ACTION_NEW_MESSAGE = "com.dianping.action.NEW_MESSAGE";
  private static AccountService accountService;
  private static Monitor instance;
  private static MApiService mapiService;
  private msgCount[] mMsgCounts = { new msgCount("NotificationCount", "notification_count", "notificationCount"), new msgCount("AlertCount", "alert_count", "alertCount"), new msgCount("SubscribeCount", "subscribe_count", "subscribeCount") };
  private MApiRequest msgRequest;

  private static AccountService accountService()
  {
    if (accountService == null)
      accountService = (AccountService)DPApplication.instance().getService("account");
    return accountService;
  }

  public static Monitor instance()
  {
    if (instance == null)
    {
      instance = new Monitor();
      accountService().addListener(instance);
    }
    return instance;
  }

  private static MApiService mapiService()
  {
    if (mapiService == null)
      mapiService = (MApiService)DPApplication.instance().getService("mapi");
    return mapiService;
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    refresh();
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.msgRequest == paramMApiRequest)
      this.msgRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.msgRequest == paramMApiRequest)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
        updateMsg((DPObject)paramMApiResponse.result());
      this.msgRequest = null;
    }
  }

  public boolean refresh()
  {
    resetMsgCount();
    requestNewMessage();
    return true;
  }

  public void requestNewMessage()
  {
    if (this.msgRequest != null)
      mapiService().abort(this.msgRequest, this, true);
    String str = accountService().token();
    if (!TextUtils.isEmpty(str));
    for (str = "http://m.api.dianping.com/getunreadmsgcount.bin?token=" + str; ; str = "http://m.api.dianping.com/getunreadmsgcount.bin")
    {
      this.msgRequest = new BasicMApiRequest(str, "GET", null, CacheType.DISABLED, true, null);
      mapiService().exec(this.msgRequest, this);
      return;
    }
  }

  void resetMsgCount()
  {
    SharedPreferences.Editor localEditor = DPActivity.preferences().edit();
    Intent localIntent = new Intent("com.dianping.action.NEW_MESSAGE");
    msgCount[] arrayOfmsgCount = this.mMsgCounts;
    int j = arrayOfmsgCount.length;
    int i = 0;
    while (i < j)
    {
      msgCount localmsgCount = arrayOfmsgCount[i];
      localEditor.putInt(localmsgCount.getSharePreferencesName(), localmsgCount.getDefaultCount());
      localIntent.putExtra(localmsgCount.getBroadcastName(), localmsgCount.getDefaultCount());
      i += 1;
    }
    localEditor.commit();
    DPApplication.instance().sendBroadcast(localIntent);
  }

  public void updateMsg(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    Intent localIntent;
    int j;
    SharedPreferences.Editor localEditor;
    do
    {
      return;
      SharedPreferences localSharedPreferences = DPActivity.preferences();
      localIntent = new Intent("com.dianping.action.NEW_MESSAGE");
      j = 0;
      localEditor = localSharedPreferences.edit();
      msgCount[] arrayOfmsgCount = this.mMsgCounts;
      int k = arrayOfmsgCount.length;
      int i = 0;
      while (i < k)
      {
        msgCount localmsgCount = arrayOfmsgCount[i];
        if (localSharedPreferences.getInt(localmsgCount.getSharePreferencesName(), localmsgCount.getDefaultCount()) != paramDPObject.getInt(localmsgCount.getDpobjectName()))
        {
          int m = paramDPObject.getInt(localmsgCount.getDpobjectName());
          localEditor.putInt(localmsgCount.getSharePreferencesName(), m);
          j = 1;
          localIntent.putExtra(localmsgCount.getBroadcastName(), m);
        }
        i += 1;
      }
    }
    while (j == 0);
    localEditor.commit();
    DPApplication.instance().sendBroadcast(localIntent);
  }

  static class msgCount
  {
    private String broadcastName;
    private int defaultCount = 0;
    private String dpobjectName;
    private String sharePreferencesName;

    public msgCount(String paramString1, String paramString2, String paramString3)
    {
      this(paramString1, paramString2, paramString3, 0);
    }

    public msgCount(String paramString1, String paramString2, String paramString3, int paramInt)
    {
      this.dpobjectName = paramString1;
      this.sharePreferencesName = paramString2;
      this.defaultCount = paramInt;
      this.broadcastName = paramString3;
    }

    public String getBroadcastName()
    {
      return this.broadcastName;
    }

    public int getDefaultCount()
    {
      return this.defaultCount;
    }

    public String getDpobjectName()
    {
      return this.dpobjectName;
    }

    public String getSharePreferencesName()
    {
      return this.sharePreferencesName;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.Monitor
 * JD-Core Version:    0.6.0
 */