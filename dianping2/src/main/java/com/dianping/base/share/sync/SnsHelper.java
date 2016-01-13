package com.dianping.base.share.sync;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;

public class SnsHelper
{
  public static void updateFeedFlag(SharedPreferences paramSharedPreferences, int paramInt)
  {
    paramSharedPreferences = DPApplication.instance().accountService();
    DPObject localDPObject = paramSharedPreferences.profile();
    if (paramInt != localDPObject.getInt("FeedFlag"))
      paramSharedPreferences.update(localDPObject.edit().putInt("FeedFlag", paramInt).generate());
  }

  public static void updateSyncMask(SharedPreferences paramSharedPreferences, int paramInt)
  {
    if (paramInt != paramSharedPreferences.getInt("syncMask", 0))
    {
      paramSharedPreferences = paramSharedPreferences.edit();
      paramSharedPreferences.putInt("syncMask", paramInt);
      paramSharedPreferences.commit();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.sync.SnsHelper
 * JD-Core Version:    0.6.0
 */