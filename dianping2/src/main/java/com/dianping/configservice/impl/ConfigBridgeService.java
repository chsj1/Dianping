package com.dianping.configservice.impl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.dianping.app.DPApplication;

public class ConfigBridgeService extends Service
{
  public static final String ACTION_UPDATE_MAIN_PREFS = "com.dianping.action.UPDATE_MAIN_PREFS";
  public static final String COPY_PREFS_FROM_MAIN_TO_PUSH = "com.dianping.action.COPY_PREFS";
  private MyConfigService serverConfig;

  private MyConfigService serverConfig()
  {
    if (this.serverConfig == null)
      this.serverConfig = ((MyConfigService)((DPApplication)getApplication()).getService("config"));
    return this.serverConfig;
  }

  // ERROR //
  private void updatePrefs(Intent paramIntent)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ldc 41
    //   5: invokevirtual 47	android/content/Intent:hasExtra	(Ljava/lang/String;)Z
    //   8: ifeq +71 -> 79
    //   11: aload_0
    //   12: aload_0
    //   13: invokevirtual 51	com/dianping/configservice/impl/ConfigBridgeService:getPackageName	()Ljava/lang/String;
    //   16: iconst_3
    //   17: invokevirtual 55	com/dianping/configservice/impl/ConfigBridgeService:getSharedPreferences	(Ljava/lang/String;I)Landroid/content/SharedPreferences;
    //   20: invokeinterface 61 1 0
    //   25: astore_2
    //   26: aload_2
    //   27: ldc 63
    //   29: invokeinterface 69 2 0
    //   34: pop
    //   35: aload_2
    //   36: invokeinterface 73 1 0
    //   41: pop
    //   42: aload_1
    //   43: ldc 41
    //   45: invokevirtual 77	android/content/Intent:getStringExtra	(Ljava/lang/String;)Ljava/lang/String;
    //   48: astore_2
    //   49: aload_0
    //   50: invokespecial 79	com/dianping/configservice/impl/ConfigBridgeService:serverConfig	()Lcom/dianping/configservice/impl/MyConfigService;
    //   53: invokevirtual 83	com/dianping/configservice/impl/MyConfigService:dump	()Lorg/json/JSONObject;
    //   56: astore_3
    //   57: aload_3
    //   58: aload_2
    //   59: invokevirtual 88	org/json/JSONObject:has	(Ljava/lang/String;)Z
    //   62: ifeq +17 -> 79
    //   65: aload_3
    //   66: aload_2
    //   67: invokevirtual 90	org/json/JSONObject:remove	(Ljava/lang/String;)Ljava/lang/Object;
    //   70: pop
    //   71: aload_0
    //   72: getfield 21	com/dianping/configservice/impl/ConfigBridgeService:serverConfig	Lcom/dianping/configservice/impl/MyConfigService;
    //   75: aload_3
    //   76: invokevirtual 94	com/dianping/configservice/impl/MyConfigService:setConfig	(Lorg/json/JSONObject;)V
    //   79: aload_1
    //   80: ldc 29
    //   82: invokevirtual 47	android/content/Intent:hasExtra	(Ljava/lang/String;)Z
    //   85: istore 4
    //   87: iload 4
    //   89: ifeq +25 -> 114
    //   92: new 85	org/json/JSONObject
    //   95: dup
    //   96: aload_1
    //   97: ldc 29
    //   99: invokevirtual 77	android/content/Intent:getStringExtra	(Ljava/lang/String;)Ljava/lang/String;
    //   102: invokespecial 97	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   105: astore_1
    //   106: aload_0
    //   107: invokespecial 79	com/dianping/configservice/impl/ConfigBridgeService:serverConfig	()Lcom/dianping/configservice/impl/MyConfigService;
    //   110: aload_1
    //   111: invokevirtual 94	com/dianping/configservice/impl/MyConfigService:setConfig	(Lorg/json/JSONObject;)V
    //   114: aload_0
    //   115: monitorexit
    //   116: return
    //   117: astore_1
    //   118: aload_0
    //   119: monitorexit
    //   120: aload_1
    //   121: athrow
    //   122: astore_1
    //   123: goto -9 -> 114
    //
    // Exception table:
    //   from	to	target	type
    //   2	79	117	finally
    //   79	87	117	finally
    //   92	114	117	finally
    //   92	114	122	java/lang/Exception
  }

  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }

  public void onStart(Intent paramIntent, int paramInt)
  {
    super.onStart(paramIntent, paramInt);
    if ("com.dianping.action.UPDATE_MAIN_PREFS".equals(paramIntent.getAction()))
      updatePrefs(paramIntent);
    stopSelf();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.configservice.impl.ConfigBridgeService
 * JD-Core Version:    0.6.0
 */