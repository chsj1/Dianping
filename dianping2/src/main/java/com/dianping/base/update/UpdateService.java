package com.dianping.base.update;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.Html;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.dianping.app.DPApplication;
import com.dianping.base.update.utils.PatchUtils;
import com.dianping.base.update.utils.SHA1Utils;
import com.dianping.base.update.utils.download.DownloadManagerCompat;
import com.dianping.base.update.utils.download.DownloadManagerCompat.RequestCompat;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UpdateService extends Service
{
  public static final String ACTION_CANCEL = "com.dianping.app.SystemUpgradeService.CANCEL";
  public static final String ACTION_STRAT = "com.dianping.app.SystemUpgradeService.STRAT";
  private static final boolean DEBUG = true;
  private static final int DOWNLOAD_LOCATION_EXTERNAL = 1;
  private static final int DOWNLOAD_LOCATION_INTERNAL = 0;
  static final String[] MSG = { "MSG_START", "MSG_NOTIFICATION_CLICK", "MSG_DOWNLOAD_CANCEL", "MSG_DOWNLOAD_FAILED", "MSG_CANCEL", "MSG_ERROR", "MSG_DOWNLOAD", "MSG_CHECK", "MSG_MERGE", "MSG_FINISH" };
  private static final int MSG_CANCEL = 4;
  private static final int MSG_CHECK = 7;
  private static final int MSG_DOWNLOAD = 6;
  private static final int MSG_DOWNLOAD_CANCEL = 2;
  private static final int MSG_DOWNLOAD_FAILED = 3;
  private static final int MSG_ERROR = 5;
  private static final int MSG_FINISH = 9;
  private static final int MSG_INVALID = -1;
  private static final int MSG_MERGE = 8;
  private static final int MSG_NOTIFICATION_CLICK = 1;
  private static final int MSG_START = 0;
  private static final String TAG = "UpdateService";
  static boolean bStopped;
  StateHandler checkHandler = new StateHandler()
  {
    protected boolean process(Message paramMessage)
    {
      if (paramMessage != null);
      for (int i = paramMessage.what; i == 7; i = -1)
      {
        UpdateService.this.executor.submit(new UpdateService.CheckTask(UpdateService.this, UpdateService.this.handler, 9, 5));
        return true;
      }
      if (i == 5)
      {
        UpdateService.this.getUpdateManager().getDownloadManager().remove(new long[] { UpdateService.this.getUpdateManager().downloadId() });
        UpdateService.this.updateNotification("大众点评更新", "校验文件失败", true, UpdateService.this.getRetryIntent());
        UpdateService.this.changeState(UpdateService.this.idleHandler);
        UpdateService.this.handler.sendEmptyMessage(5);
        return true;
      }
      if (i == 9)
      {
        UpdateService.this.cancelNotification();
        if (UpdateService.this.source == 0)
          UpdateService.this.getUpdateManager().gotoInstall();
        ((StatisticsService)DPApplication.instance().getService("statistics")).event("update5", "update5_install", null, 0, null);
        UpdateService.this.changeState(UpdateService.this.finishHandler);
        UpdateService.this.handler.sendEmptyMessage(9);
        return true;
      }
      return super.process(paramMessage);
    }

    public String toString()
    {
      return "STATE_CHECK";
    }
  };
  StateHandler downloadHandler = new StateHandler()
  {
    protected boolean process(Message paramMessage)
    {
      int i;
      String str;
      label40: int j;
      if (paramMessage != null)
      {
        i = paramMessage.what;
        StringBuilder localStringBuilder = new StringBuilder().append(this).append(" receives message:");
        if (i == -1)
          break label167;
        str = UpdateService.MSG[i];
        Log.d("UpdateService", str);
        if (i != 6)
          break label568;
        j = 0;
        i = j;
        if (0 == 0)
        {
          i = j;
          if (!UpdateService.this.isDownloadurlValid())
          {
            i = 1;
            UpdateService.this.updateNotification("大众点评更新", "非法下载地址", false, null);
          }
        }
        j = i;
        if (i == 0)
        {
          if (!UpdateService.this.hasEnoughRoom(1))
            break label173;
          UpdateService.this.downloadLocation = 1;
          j = i;
        }
      }
      while (true)
      {
        if (j == 0)
          break label221;
        paramMessage = Message.obtain();
        paramMessage.what = 5;
        UpdateService.this.handler.sendMessage(paramMessage);
        return true;
        i = -1;
        break;
        label167: str = "MSG_INVALID";
        break label40;
        label173: if (UpdateService.this.hasEnoughRoom(0))
        {
          UpdateService.this.downloadLocation = 0;
          j = i;
          continue;
        }
        j = 1;
        UpdateService.this.updateNotification(Html.fromHtml("<font color=#ff0000>剩余空间不足</font>"), "请确保存储空间大于60M", false, null);
      }
      label221: paramMessage = new IntentFilter();
      paramMessage.addAction("android.intent.action.DOWNLOAD_COMPLETE");
      paramMessage.addAction("android.intent.action.DOWNLOAD_CANCELLED");
      paramMessage.addAction("android.intent.action.DOWNLOAD_FAILED");
      UpdateService.this.registerReceiver(UpdateService.this.downloadReceiver, paramMessage);
      long l;
      while (true)
      {
        try
        {
          paramMessage = new DownloadManagerCompat.RequestCompat(Uri.parse(UpdateService.this.downloadurl));
          paramMessage.setTitle("大众点评更新");
          paramMessage.setMimeType("application/vnd.android.package-archive");
          paramMessage.setIntent(new Intent());
          if (UpdateService.this.source == 0)
            break label517;
          i = 2;
          paramMessage.setNotificationVisibility(i);
          paramMessage.setVisibleInDownloadsUi(false);
          if (UpdateService.this.downloadLocation != 1)
            break label523;
          paramMessage.setDestinationInExternalPublicDir("Download", "dianping_" + UpdateService.this.getUpdateManager().newVersionCode() + ".apk");
          l = UpdateService.this.getUpdateManager().getDownloadManager().enqueue(paramMessage);
          UpdateService.this.getSharedPreferences("app_update", 0).edit().putLong("downloadID", l).putInt("versionCode", UpdateService.this.getUpdateManager().newVersionCode()).putString("destinationPath", paramMessage.mDestinationUri().getPath()).putString("installPath", paramMessage.mDestinationUri().toString()).commit();
        }
        catch (java.lang.IllegalStateException paramMessage)
        {
          UpdateService.this.unregisterReceiver(UpdateService.this.downloadReceiver);
          paramMessage = Message.obtain();
          paramMessage.what = 5;
          UpdateService.this.handler.sendMessage(paramMessage);
        }
        break;
        label517: i = 0;
        continue;
        label523: paramMessage.setDestinationInInternalFilesDir(UpdateService.this, "dianping_" + UpdateService.this.getUpdateManager().newVersionCode() + ".apk");
      }
      label568: if (i == 4)
      {
        UpdateService.this.changeState(UpdateService.this.idleHandler);
        UpdateService.this.handler.sendEmptyMessage(4);
        return true;
      }
      if ((i == 5) || (i == 3))
      {
        UpdateService.this.getUpdateManager().getDownloadManager().remove(new long[] { UpdateService.this.getUpdateManager().downloadId() });
        if (i == 3)
          UpdateService.this.updateNotification("更新包下载失败", "请检查网络后重试", true, UpdateService.this.getRetryIntent());
        UpdateService.this.changeState(UpdateService.this.idleHandler);
        UpdateService.this.handler.sendEmptyMessage(5);
        return true;
      }
      if (i == 9)
      {
        UpdateService.this.changeState(UpdateService.this.mergeHandler);
        UpdateService.this.handler.sendEmptyMessage(8);
        return true;
      }
      if (i == 2)
      {
        l = UpdateService.this.getUpdateManager().downloadId();
        if (l != -1L)
          UpdateService.this.getUpdateManager().getDownloadManager().remove(new long[] { l });
        return true;
      }
      return super.process(paramMessage);
    }

    public String toString()
    {
      return "STATE_DOWNLOAD";
    }
  };
  int downloadLocation = 1;
  BroadcastReceiver downloadReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      Log.d("UpdateService", "received action=" + paramContext);
      long l = paramIntent.getLongExtra("extra_download_id", 0L);
      if ("android.intent.action.DOWNLOAD_COMPLETE".equals(paramContext))
      {
        Log.d("UpdateService", "downloadid from intent=" + l + " downnload from preference=" + UpdateService.this.getUpdateManager().downloadId());
        if (l == UpdateService.this.getUpdateManager().downloadId())
        {
          UpdateService.this.handler.sendEmptyMessage(9);
          UpdateService.this.unregisterReceiver(UpdateService.this.downloadReceiver);
        }
      }
      do
        while (true)
        {
          return;
          if (!"android.intent.action.DOWNLOAD_CANCELLED".equals(paramContext))
            break;
          if (l != UpdateService.this.getUpdateManager().downloadId())
            continue;
          UpdateService.this.handler.sendEmptyMessage(4);
          UpdateService.this.unregisterReceiver(UpdateService.this.downloadReceiver);
          return;
        }
      while ((!"android.intent.action.DOWNLOAD_FAILED".equals(paramContext)) || (l != UpdateService.this.getUpdateManager().downloadId()));
      UpdateService.this.handler.sendEmptyMessage(3);
      UpdateService.this.unregisterReceiver(UpdateService.this.downloadReceiver);
    }
  };
  String downloadurl;
  ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(4));
  int filetype;
  StateHandler finishHandler = new StateHandler()
  {
    protected boolean process(Message paramMessage)
    {
      if (paramMessage != null);
      for (int i = paramMessage.what; i == 9; i = -1)
      {
        ((StatisticsService)DPApplication.instance().getService("statistics")).event("update5", "update5_download", "成功", 0, null);
        UpdateService.this.stopSelf();
        return true;
      }
      return super.process(paramMessage);
    }

    public String toString()
    {
      return "STATE_FINISH";
    }
  };
  Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      Log.d("UpdateService", "handle message:" + UpdateService.MSG[paramMessage.what]);
      UpdateService.this.mStateHandler.process(paramMessage);
    }
  };
  StateHandler idleHandler = new StateHandler()
  {
    protected boolean process(Message paramMessage)
    {
      boolean bool = false;
      if (paramMessage != null);
      for (int i = paramMessage.what; i == 0; i = -1)
      {
        paramMessage = UpdateService.this.getUpdateManager();
        if (UpdateService.this.source == 1)
          bool = true;
        paramMessage.setUpdateApkFromAutoWifi(bool);
        UpdateService.this.changeState(UpdateService.this.downloadHandler);
        UpdateService.this.handler.sendEmptyMessage(6);
        return true;
      }
      if ((i == 4) || (i == 5))
      {
        ((StatisticsService)DPApplication.instance().getService("statistics")).event("update5", "update5_download", "失败", 0, null);
        UpdateService.this.stopSelf();
        return true;
      }
      return super.process(paramMessage);
    }

    public String toString()
    {
      return "STATE_IDLE";
    }
  };
  private NotificationManager mNotifyManager;
  StateHandler mStateHandler = this.idleHandler;
  StateHandler mergeHandler = new StateHandler()
  {
    protected boolean process(Message paramMessage)
    {
      int i;
      if (paramMessage != null)
        i = paramMessage.what;
      while (i == 8)
      {
        UpdateService.this.updateNotification("大众点评更新", "下载完成 正在校验...", false, null);
        if (!UpdateService.this.hasEnoughRoom(UpdateService.this.downloadLocation))
        {
          paramMessage = Message.obtain();
          paramMessage.what = 5;
          paramMessage.obj = "请确保存储空间大于60M";
          UpdateService.this.handler.sendMessage(paramMessage);
          UpdateService.this.updateNotification(Html.fromHtml("<font color=#ff0000>剩余空间不足</font>"), "请确保存储空间大于60M", false, null);
          return true;
          i = -1;
          continue;
        }
        if (UpdateService.this.filetype == 1)
        {
          UpdateService.this.executor.submit(new UpdateService.MergeTask(UpdateService.this, UpdateService.this.handler, 9, 5));
          return true;
        }
        UpdateService.this.handler.sendEmptyMessage(9);
        return true;
      }
      if (i == 5)
      {
        UpdateService.this.getUpdateManager().getDownloadManager().remove(new long[] { UpdateService.this.getUpdateManager().downloadId() });
        if (paramMessage.obj == null)
          UpdateService.this.updateNotification("大众点评更新", "校验文件失败", true, UpdateService.this.getRetryIntent());
        UpdateService.this.changeState(UpdateService.this.idleHandler);
        UpdateService.this.handler.sendEmptyMessage(5);
        return true;
      }
      if (i == 9)
      {
        UpdateService.this.changeState(UpdateService.this.checkHandler);
        UpdateService.this.handler.sendEmptyMessage(7);
        return true;
      }
      return super.process(paramMessage);
    }

    public String toString()
    {
      return "STATE_MERGE";
    }
  };
  private Notification notification;
  int source;

  static
  {
    System.loadLibrary("apkpatch");
    bStopped = true;
  }

  private long getLeftRoom(String paramString)
  {
    paramString = new StatFs(paramString);
    return paramString.getAvailableBlocks() * paramString.getBlockSize();
  }

  @TargetApi(18)
  private long getLeftRoomLong(String paramString)
  {
    return new StatFs(paramString).getAvailableBytes();
  }

  void cancelNotification()
  {
    this.mNotifyManager.cancel((int)getUpdateManager().downloadId());
  }

  void changeState(StateHandler paramStateHandler)
  {
    Log.d("UpdateService", "change state from " + this.mStateHandler + " to " + paramStateHandler);
    this.mStateHandler = paramStateHandler;
  }

  PendingIntent getRetryIntent()
  {
    Intent localIntent = new Intent(this, UpdateService.class);
    localIntent.setAction("com.dianping.app.SystemUpgradeService.STRAT");
    localIntent.putExtra("url", this.downloadurl);
    localIntent.putExtra("type", this.filetype);
    return PendingIntent.getService(this, 0, localIntent, 134217728);
  }

  UpdateManager getUpdateManager()
  {
    return UpdateManager.instance(this);
  }

  boolean hasEnoughRoom(int paramInt)
  {
    String str;
    if (paramInt == 0)
    {
      str = getFilesDir().getAbsolutePath();
      Log.d("UpdateService", "check path=" + str);
      if (Build.VERSION.SDK_INT < 18)
        break label71;
      if (getLeftRoomLong(str) < 62914560L)
        break label69;
    }
    label69: label71: 
    do
    {
      return true;
      str = Environment.getExternalStorageDirectory().getAbsolutePath();
      break;
      return false;
    }
    while (getLeftRoom(str) >= 62914560L);
    return false;
  }

  boolean isDownloadurlValid()
  {
    return !TextUtils.isEmpty(this.downloadurl);
  }

  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }

  public void onCreate()
  {
    super.onCreate();
    Log.d("UpdateService", "onCreate");
    bStopped = false;
    startForeground(0, new Notification());
    this.mNotifyManager = ((NotificationManager)getSystemService("notification"));
  }

  public void onDestroy()
  {
    super.onDestroy();
    Log.d("UpdateService", "onDestroy");
    bStopped = true;
    this.executor.shutdown();
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    super.onStartCommand(paramIntent, paramInt1, paramInt2);
    StringBuilder localStringBuilder = new StringBuilder().append("intent=").append(paramIntent).append(" action=");
    String str;
    if (paramIntent == null)
    {
      str = "null";
      Log.d("UpdateService", str + " flags=" + paramInt1 + " startId=" + paramInt2);
      if (paramIntent != null)
      {
        if (!"com.dianping.app.SystemUpgradeService.CANCEL".equals(paramIntent.getAction()))
          break label113;
        this.handler.sendEmptyMessage(2);
      }
    }
    label113: 
    do
    {
      return 1;
      str = paramIntent.getAction();
      break;
      if (!"com.dianping.app.SystemUpgradeService.STRAT".equals(paramIntent.getAction()))
        continue;
      paramIntent = paramIntent.getExtras();
      this.downloadurl = paramIntent.getString("url");
      this.filetype = paramIntent.getInt("type");
      this.source = paramIntent.getInt("source");
      paramIntent = getUpdateManager().getDownloadManager();
      if (this.source == 0);
      for (boolean bool = true; ; bool = false)
      {
        paramIntent.setNotificationVisibility(bool);
        Log.d("UpdateService", "ready to download. download url=" + this.downloadurl + " filetype=" + this.filetype + " source=" + this.source);
        this.handler.sendEmptyMessage(0);
        return 1;
      }
    }
    while (!"android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED".equals(paramIntent.getAction()));
    return 1;
  }

  void updateNotification(CharSequence paramCharSequence, String paramString, boolean paramBoolean, PendingIntent paramPendingIntent)
  {
    if (this.source != 0)
      return;
    NotificationCompat.Builder localBuilder;
    if (this.notification == null)
    {
      RemoteViews localRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_update);
      localBuilder = new NotificationCompat.Builder(this);
      if (Build.VERSION.SDK_INT < 21)
        break label213;
      localBuilder.setSmallIcon(R.drawable.push_notification_white_icon);
      localBuilder.setContent(localRemoteViews).setContentIntent(PendingIntent.getService(this, 0, new Intent(), 134217728));
      this.notification = localBuilder.build();
      if (Build.VERSION.SDK_INT <= 10)
        this.notification.contentView = localRemoteViews;
    }
    this.notification.contentView.setTextViewText(16908310, paramCharSequence);
    this.notification.contentView.setTextViewText(16908308, paramString);
    if ((paramBoolean) && (paramPendingIntent != null))
    {
      this.notification.contentView.setViewVisibility(R.id.button_retry, 0);
      this.notification.contentView.setOnClickPendingIntent(R.id.button_retry, paramPendingIntent);
      if (Build.VERSION.SDK_INT <= 10)
        this.notification.contentIntent = paramPendingIntent;
    }
    while (true)
    {
      this.mNotifyManager.notify((int)getUpdateManager().downloadId(), this.notification);
      return;
      label213: localBuilder.setSmallIcon(R.drawable.icon);
      break;
      this.notification.contentView.setViewVisibility(R.id.button_retry, 8);
      if (Build.VERSION.SDK_INT > 10)
        continue;
      this.notification.contentIntent = PendingIntent.getService(this, 0, new Intent(), 134217728);
    }
  }

  private class CheckTask extends UpdateService.Task
  {
    public CheckTask(Handler paramInt1, int paramInt2, int arg4)
    {
      super(paramInt1, paramInt2, i);
    }

    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: ldc 22
      //   2: ldc 24
      //   4: invokestatic 30	com/dianping/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
      //   7: aconst_null
      //   8: astore_3
      //   9: aconst_null
      //   10: astore_2
      //   11: new 32	java/io/File
      //   14: dup
      //   15: aload_0
      //   16: getfield 13	com/dianping/base/update/UpdateService$CheckTask:this$0	Lcom/dianping/base/update/UpdateService;
      //   19: invokevirtual 36	com/dianping/base/update/UpdateService:getUpdateManager	()Lcom/dianping/base/update/UpdateManager;
      //   22: invokevirtual 42	com/dianping/base/update/UpdateManager:destinationPath	()Ljava/lang/String;
      //   25: invokespecial 45	java/io/File:<init>	(Ljava/lang/String;)V
      //   28: astore_1
      //   29: aload_1
      //   30: aload_0
      //   31: getfield 13	com/dianping/base/update/UpdateService$CheckTask:this$0	Lcom/dianping/base/update/UpdateService;
      //   34: invokevirtual 36	com/dianping/base/update/UpdateService:getUpdateManager	()Lcom/dianping/base/update/UpdateManager;
      //   37: invokevirtual 48	com/dianping/base/update/UpdateManager:getNewApkSha1	()Ljava/lang/String;
      //   40: invokestatic 54	com/dianping/base/update/utils/SHA1Utils:testSHA1	(Ljava/io/File;Ljava/lang/String;)Z
      //   43: istore 4
      //   45: iload 4
      //   47: ifne +137 -> 184
      //   50: aload_1
      //   51: ifnull +133 -> 184
      //   54: aload_1
      //   55: invokevirtual 58	java/io/File:exists	()Z
      //   58: ifeq +126 -> 184
      //   61: aload_1
      //   62: invokevirtual 61	java/io/File:delete	()Z
      //   65: pop
      //   66: ldc 22
      //   68: new 63	java/lang/StringBuilder
      //   71: dup
      //   72: invokespecial 65	java/lang/StringBuilder:<init>	()V
      //   75: ldc 67
      //   77: invokevirtual 71	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   80: iload 4
      //   82: invokevirtual 74	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   85: invokevirtual 77	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   88: invokestatic 30	com/dianping/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
      //   91: iload 4
      //   93: ifeq +78 -> 171
      //   96: aload_0
      //   97: invokevirtual 80	com/dianping/base/update/UpdateService$CheckTask:sendSuccessMsg	()V
      //   100: return
      //   101: astore_1
      //   102: aload_2
      //   103: astore_1
      //   104: iconst_0
      //   105: istore 5
      //   107: iload 5
      //   109: istore 4
      //   111: iconst_0
      //   112: ifne -46 -> 66
      //   115: iload 5
      //   117: istore 4
      //   119: aload_1
      //   120: ifnull -54 -> 66
      //   123: iload 5
      //   125: istore 4
      //   127: aload_1
      //   128: invokevirtual 58	java/io/File:exists	()Z
      //   131: ifeq -65 -> 66
      //   134: aload_1
      //   135: invokevirtual 61	java/io/File:delete	()Z
      //   138: pop
      //   139: iload 5
      //   141: istore 4
      //   143: goto -77 -> 66
      //   146: astore_2
      //   147: aload_3
      //   148: astore_1
      //   149: iconst_1
      //   150: ifne +19 -> 169
      //   153: aload_1
      //   154: ifnull +15 -> 169
      //   157: aload_1
      //   158: invokevirtual 58	java/io/File:exists	()Z
      //   161: ifeq +8 -> 169
      //   164: aload_1
      //   165: invokevirtual 61	java/io/File:delete	()Z
      //   168: pop
      //   169: aload_2
      //   170: athrow
      //   171: aload_0
      //   172: invokevirtual 83	com/dianping/base/update/UpdateService$CheckTask:sendFailMsg	()V
      //   175: return
      //   176: astore_2
      //   177: goto -28 -> 149
      //   180: astore_2
      //   181: goto -77 -> 104
      //   184: goto -118 -> 66
      //
      // Exception table:
      //   from	to	target	type
      //   11	29	101	java/lang/Exception
      //   11	29	146	finally
      //   29	45	176	finally
      //   29	45	180	java/lang/Exception
    }
  }

  private class MergeTask extends UpdateService.Task
  {
    public MergeTask(Handler paramInt1, int paramInt2, int arg4)
    {
      super(paramInt1, paramInt2, i);
    }

    public void run()
    {
      Log.d("UpdateService", "start to merge");
      boolean bool1 = true;
      String str2 = UpdateService.this.getUpdateManager().getLocalApkPath();
      String str1 = UpdateService.this.getUpdateManager().destinationPath();
      String str3 = str1.substring(0, str1.lastIndexOf('/') + 1) + "dianping_tem" + ".apk";
      File localFile = new File(str3);
      boolean bool2 = bool1;
      try
      {
        Log.d("UpdateService", "checking patch");
        bool2 = bool1;
        bool1 = SHA1Utils.testSHA1(new File(str1), UpdateService.this.getUpdateManager().getPatchSha1());
        if (bool1)
        {
          bool2 = bool1;
          Log.d("UpdateService", "patch is OK");
          bool2 = bool1;
          PatchUtils.patch(str2, str3, str1);
          bool2 = bool1;
          localFile.renameTo(new File(str1));
          bool2 = bool1;
          Log.d("UpdateService", "merge complete");
        }
        bool2 = bool1;
        if (!bool1)
        {
          localFile.delete();
          new File(str1).delete();
          bool2 = bool1;
        }
        Log.d("UpdateService", "merge result=" + bool2);
        if (bool2)
        {
          sendSuccessMsg();
          return;
        }
      }
      catch (Exception localException)
      {
        while (true)
        {
          bool1 = false;
          bool2 = bool1;
          if (0 != 0)
            continue;
          localFile.delete();
          new File(str1).delete();
          bool2 = bool1;
        }
      }
      finally
      {
        if (!bool2)
        {
          localFile.delete();
          new File(str1).delete();
        }
      }
      sendFailMsg();
    }
  }

  private abstract class StateHandler
  {
    private StateHandler()
    {
    }

    protected boolean process(Message paramMessage)
    {
      Log.w("UpdateService", "ignore this message:" + UpdateService.MSG[paramMessage.what] + " in " + this);
      return false;
    }

    public String toString()
    {
      return "STATE_INVALID";
    }
  }

  private abstract class Task
    implements Runnable
  {
    private final int failMsg;
    private final Handler handler;
    private final int successMsg;

    public Task(Handler paramInt1, int paramInt2, int arg4)
    {
      this.handler = paramInt1;
      this.successMsg = paramInt2;
      int i;
      this.failMsg = i;
    }

    protected void sendFailMsg()
    {
      if (this.handler != null)
        this.handler.sendEmptyMessage(this.failMsg);
    }

    protected void sendSuccessMsg()
    {
      if (this.handler != null)
        this.handler.sendEmptyMessage(this.successMsg);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.update.UpdateService
 * JD-Core Version:    0.6.0
 */