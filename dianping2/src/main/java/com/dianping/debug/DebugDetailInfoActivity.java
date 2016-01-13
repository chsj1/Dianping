package com.dianping.debug;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.app.Environment;
import com.dianping.base.app.NovaActivity;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class DebugDetailInfoActivity extends NovaActivity
  implements View.OnLongClickListener
{
  private static final int MESSAGE_SHOP = 2;
  private TextView debugInfo;
  final Handler handler = new Handler()
  {
    @TargetApi(13)
    public void handleMessage(Message paramMessage)
    {
      boolean bool = true;
      if (paramMessage.what == 1);
      try
      {
        Object localObject = DebugDetailInfoActivity.this.getPackageManager().getPackageInfo(DebugDetailInfoActivity.this.getPackageName(), 0);
        paramMessage = new StringBuilder();
        paramMessage.append("versionName=").append(((PackageInfo)localObject).versionName).append('\n');
        paramMessage.append("versionCode=").append(((PackageInfo)localObject).versionCode).append('\n');
        paramMessage.append("realVersionCode=").append(Environment.versionCode()).append('\n');
        paramMessage.append("realVersionName=").append(Environment.versionName()).append('\n');
        paramMessage.append("source=").append(Environment.source()).append('\n');
        paramMessage.append("source2=").append(Environment.source2()).append('\n');
        StringBuilder localStringBuilder = paramMessage.append("debuggable=");
        if ((((PackageInfo)localObject).applicationInfo.flags & 0x2) != 0);
        while (true)
        {
          localStringBuilder.append(bool).append('\n');
          paramMessage.append('\n');
          paramMessage.append('\n');
          paramMessage.append("deviceId=").append(Environment.deviceId()).append('\n');
          paramMessage.append("sessionId=").append(Environment.sessionId()).append('\n');
          paramMessage.append("cityId=").append(DebugDetailInfoActivity.this.cityId()).append('\n');
          paramMessage.append("token=").append(((AccountService)DebugDetailInfoActivity.this.getService("account")).token()).append('\n');
          paramMessage.append("DPID=").append(DPActivity.preferences().getString("dpid", "")).append("\n");
          paramMessage.append("UUID=").append(Environment.uuid()).append("\n");
          paramMessage.append('\n');
          paramMessage.append("android.SDK=").append(Build.VERSION.SDK).append('\n');
          paramMessage.append("android.VERSION=").append(Build.VERSION.RELEASE).append('\n');
          paramMessage.append("android.ID=").append(Build.ID).append('\n');
          paramMessage.append("android.BRAND=").append(Build.BRAND).append('\n');
          paramMessage.append("android.MODEL=").append(Build.MODEL).append('\n');
          paramMessage.append("buildNumber=").append(Environment.buildNumber()).append('\n');
          localObject = new DisplayMetrics();
          ((WindowManager)DebugDetailInfoActivity.this.getSystemService("window")).getDefaultDisplay().getMetrics((DisplayMetrics)localObject);
          paramMessage.append('\n');
          paramMessage.append("heapSize=" + Runtime.getRuntime().maxMemory() / 1024L / 1024L + "MB").append("\n");
          paramMessage.append("widthPixels=" + ((DisplayMetrics)localObject).widthPixels).append('\n');
          paramMessage.append("heightPixels=" + ((DisplayMetrics)localObject).heightPixels).append('\n');
          paramMessage.append("density=" + ((DisplayMetrics)localObject).density).append('\n');
          paramMessage.append("densityDpi=" + ((DisplayMetrics)localObject).densityDpi).append('\n');
          paramMessage.append("scaledDensity=" + ((DisplayMetrics)localObject).scaledDensity).append('\n');
          paramMessage.append("xdpi=" + ((DisplayMetrics)localObject).xdpi).append('\n');
          paramMessage.append("ydpi=" + ((DisplayMetrics)localObject).ydpi).append('\n');
          paramMessage.append("DENSITY_LOW=120").append('\n');
          paramMessage.append("DENSITY_MEDIUM=160").append('\n');
          if (Build.VERSION.SDK_INT >= 13)
          {
            paramMessage.append("DENSITY_TV=213").append('\n');
            paramMessage.append("DENSITY_XHIGH=320").append('\n');
          }
          paramMessage.append("DENSITY_HIGH=240").append('\n');
          paramMessage.append("dm=" + ((DisplayMetrics)localObject).toString()).append('\n');
          DebugDetailInfoActivity.this.debugInfo.setText(paramMessage.toString());
          return;
          bool = false;
        }
      }
      catch (Exception paramMessage)
      {
        paramMessage.printStackTrace();
      }
    }
  };
  private String infoDetail;
  private int message = 1;

  @TargetApi(11)
  private void copy(String paramString)
  {
    ((ClipboardManager)getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(null, paramString));
  }

  private View findTitleRoot()
  {
    View localView1 = null;
    View localView2 = super.findViewById(R.id.title_bar);
    if (localView2 != null)
      localView1 = (View)localView2.getParent();
    do
    {
      return localView1;
      localView2 = super.findViewById(16908310);
    }
    while (localView2 == null);
    return (View)localView2.getParent();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.debug_detail_info_activity);
    this.debugInfo = ((TextView)findViewById(R.id.debug_info));
    this.debugInfo.setOnLongClickListener(this);
    setTitle("长按复制");
    if (findTitleRoot() != null)
      findTitleRoot().setOnLongClickListener(this);
    this.handler.sendEmptyMessage(this.message);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.handler.removeCallbacksAndMessages(null);
  }

  public boolean onLongClick(View paramView)
  {
    if (TextUtils.isEmpty(this.debugInfo.getText()))
      return true;
    copy(this.debugInfo.getText().toString());
    Toast.makeText(this, "Debug info has been copped to system Clipboard!!!", 0).show();
    return true;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugDetailInfoActivity
 * JD-Core Version:    0.6.0
 */