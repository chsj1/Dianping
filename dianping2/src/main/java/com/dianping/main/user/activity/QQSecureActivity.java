package com.dianping.main.user.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.NovaButton;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;

public class QQSecureActivity extends NovaActivity
{
  int qqSecureStatus = 0;
  private NovaButton statusButton;
  private ImageView statusIcon;
  private TextView statusInfo;

  static void callQqSecureOnBg(Context paramContext)
  {
    if (paramContext != null)
    {
      Intent localIntent = new Intent();
      localIntent.setClassName("com.tencent.qqpimsecure", "com.tencent.qqpimsecure.service.TMSLiteService");
      paramContext.startService(localIntent);
    }
  }

  public static byte checkQQSecureProtected(Context paramContext)
  {
    int m = 0;
    int i = 0;
    try
    {
      localObject = paramContext.getPackageManager().getPackageInfo("com.tencent.qqpimsecure", 0);
      if (localObject == null)
      {
        j = 0;
        k = i;
        if (j != 0)
        {
          localObject = ((ActivityManager)paramContext.getSystemService("activity")).getRunningAppProcesses();
          k = i;
          if (localObject != null)
          {
            localObject = ((List)localObject).iterator();
            while (true)
            {
              k = i;
              if (!((Iterator)localObject).hasNext())
                break;
              if (!"com.tencent.qqpimsecure".equalsIgnoreCase(((ActivityManager.RunningAppProcessInfo)((Iterator)localObject).next()).processName))
                continue;
              k = 1;
            }
          }
        }
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      try
      {
        int j;
        int k;
        while (true)
        {
          paramContext = paramContext.getPackageManager().getPackageInfo("com.tencent.qqpimsecure", 64).signatures;
          Object localObject = MessageDigest.getInstance("MD5");
          if ((paramContext != null) && (paramContext.length > 0))
            ((MessageDigest)localObject).update(paramContext[0].toByteArray());
          paramContext = ((MessageDigest)localObject).digest();
          localObject = new char[16];
          Object tmp144_143 = localObject;
          tmp144_143[0] = 48;
          Object tmp149_144 = tmp144_143;
          tmp149_144[1] = 49;
          Object tmp154_149 = tmp149_144;
          tmp154_149[2] = 50;
          Object tmp159_154 = tmp154_149;
          tmp159_154[3] = 51;
          Object tmp164_159 = tmp159_154;
          tmp164_159[4] = 52;
          Object tmp169_164 = tmp164_159;
          tmp169_164[5] = 53;
          Object tmp174_169 = tmp169_164;
          tmp174_169[6] = 54;
          Object tmp180_174 = tmp174_169;
          tmp180_174[7] = 55;
          Object tmp186_180 = tmp180_174;
          tmp186_180[8] = 56;
          Object tmp192_186 = tmp186_180;
          tmp192_186[9] = 57;
          Object tmp198_192 = tmp192_186;
          tmp198_192[10] = 65;
          Object tmp204_198 = tmp198_192;
          tmp204_198[11] = 66;
          Object tmp210_204 = tmp204_198;
          tmp210_204[12] = 67;
          Object tmp216_210 = tmp210_204;
          tmp216_210[13] = 68;
          Object tmp222_216 = tmp216_210;
          tmp222_216[14] = 69;
          Object tmp228_222 = tmp222_216;
          tmp228_222[15] = 70;
          tmp228_222;
          StringBuilder localStringBuilder = new StringBuilder(paramContext.length * 2);
          i = 0;
          while (i < paramContext.length)
          {
            localStringBuilder.append(localObject[((paramContext[i] & 0xF0) >>> 4)]);
            localStringBuilder.append(localObject[(paramContext[i] & 0xF)]);
            i += 1;
          }
          localNameNotFoundException = localNameNotFoundException;
          localObject = null;
          localNameNotFoundException.printStackTrace();
          continue;
          j = 1;
        }
        if (!"00B1208638DE0FCD3E920886D658DAF6".equalsIgnoreCase(localNameNotFoundException.toString()))
        {
          boolean bool = "7CC749CFC0FB5677E6ABA342EDBDBA5A".equalsIgnoreCase(localNameNotFoundException.toString());
          i = m;
          if (!bool);
        }
        else
        {
          i = 1;
        }
        if ((j == 0) || (i == 0))
          break label368;
        if (k != 0)
          return 2;
      }
      catch (Exception paramContext)
      {
        while (true)
        {
          paramContext.printStackTrace();
          i = 0;
        }
      }
      return 1;
    }
    label368: return 0;
  }

  private void checkQQSecureStatus(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return;
    case 0:
      this.statusIcon.setImageResource(R.drawable.personal_icon_cross);
      this.statusInfo.setText(getResources().getString(R.string.qq_security_status_not_installed));
      this.statusButton.setText(getResources().getString(R.string.qq_security_button_status_not_installed));
      return;
    case 1:
      this.statusIcon.setImageResource(R.drawable.personal_icon_info);
      this.statusInfo.setText(getResources().getString(R.string.qq_security_status_not_running));
      this.statusButton.setText(getResources().getString(R.string.qq_security_button_status_not_running));
      return;
    case 2:
    }
    this.statusIcon.setImageResource(R.drawable.personal_icon_check);
    this.statusInfo.setText(getResources().getString(R.string.qq_security_status_running));
    this.statusButton.setText(getResources().getString(R.string.qq_security_button_status_running));
  }

  static void jumpToQqSecureView(Context paramContext, String paramString, int paramInt)
  {
    if (paramContext != null)
    {
      Intent localIntent = paramContext.getPackageManager().getLaunchIntentForPackage("com.tencent.qqpimsecure");
      if (localIntent != null)
      {
        Bundle localBundle = new Bundle();
        if ((paramString != null) && (paramString.length() > 0))
          localBundle.putString("platform_Id", paramString);
        if (paramInt > 0)
          localBundle.putInt("dest_view", paramInt);
        localIntent.putExtras(localBundle);
        localIntent.setFlags(402653184);
        paramContext.startActivity(localIntent);
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.activity_qq_secure);
    this.qqSecureStatus = getIntParam("qqSecureStatus");
    this.statusIcon = ((ImageView)findViewById(R.id.icon));
    this.statusInfo = ((TextView)findViewById(R.id.qq_secure_status));
    this.statusButton = ((NovaButton)findViewById(R.id.qq_secure_btn));
    this.statusButton.setGAString("safe_button");
    this.statusButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        switch (QQSecureActivity.this.qqSecureStatus)
        {
        default:
          return;
        case 0:
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("http://qqwx.qq.com/s?aid=index&g_f=478"));
          QQSecureActivity.this.startActivity(paramView);
          QQSecureActivity.this.finish();
          return;
        case 1:
          QQSecureActivity.callQqSecureOnBg(QQSecureActivity.this);
          QQSecureActivity.jumpToQqSecureView(QQSecureActivity.this, "dianping", 7798785);
          return;
        case 2:
        }
        QQSecureActivity.jumpToQqSecureView(QQSecureActivity.this, "dianping", 7798785);
      }
    });
  }

  public void onResume()
  {
    super.onResume();
    checkQQSecureStatus(checkQQSecureProtected(this));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.QQSecureActivity
 * JD-Core Version:    0.6.0
 */