package com.dianping.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import com.dianping.util.PermissionCheckHelper;
import com.dianping.util.PermissionCheckHelper.PermissionCallbackListener;
import java.util.Map;

public class PermissionHandlerActivity extends Activity
{
  private static final int OPEN_APPLICATION_SETTING_CODE = 2;
  private static final int PERMISSION_REQUEST_CODE = 0;
  private PermissionRequestInfo requestInfo;

  private void doCallback()
  {
    PermissionCheckHelper.PermissionCallbackListener localPermissionCallbackListener = this.requestInfo.getListener();
    if (localPermissionCallbackListener != null)
      localPermissionCallbackListener.onPermissionCheckCallback(this.requestInfo.getRequestCode(), this.requestInfo.getRequestPermissions(), this.requestInfo.getRequestResults());
  }

  private void handleMultiplePermissionCheck(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if (paramArrayOfString1.length == 1)
    {
      handleSinglePermissionCheck(paramArrayOfString1[0], paramArrayOfString2[0]);
      return;
    }
    int j = 0;
    Object localObject1 = "";
    int i = 0;
    while (i < paramArrayOfString1.length)
    {
      Object localObject2 = localObject1;
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, paramArrayOfString1[i]))
      {
        int k = 1;
        localObject2 = localObject1;
        j = k;
        if (!paramArrayOfString2[i].isEmpty())
        {
          localObject2 = (String)localObject1 + paramArrayOfString2[i];
          j = k;
        }
      }
      i += 1;
      localObject1 = localObject2;
    }
    if (j != 0)
    {
      showNormalRationaleDialog(this, (String)localObject1, new DialogInterface.OnClickListener(paramArrayOfString1)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          ActivityCompat.requestPermissions(PermissionHandlerActivity.this, this.val$permissionArray, 0);
        }
      });
      return;
    }
    ActivityCompat.requestPermissions(this, paramArrayOfString1, 0);
  }

  private boolean handleNeverAsk(String[] paramArrayOfString1, String[] paramArrayOfString2, int[] paramArrayOfInt)
  {
    int j = 0;
    Object localObject1 = "";
    int i = 0;
    while ((i < paramArrayOfString1.length) && (paramArrayOfInt[i] == -1))
    {
      Object localObject2 = localObject1;
      if (!ActivityCompat.shouldShowRequestPermissionRationale(this, paramArrayOfString1[i]))
      {
        int k = 1;
        j = k;
        localObject2 = localObject1;
        if (!paramArrayOfString2[i].isEmpty())
        {
          localObject2 = (String)localObject1 + paramArrayOfString2[i];
          j = k;
        }
      }
      i += 1;
      localObject1 = localObject2;
    }
    if (j != 0)
      showNeverAskRationaleDialog(this, (String)localObject1);
    return j;
  }

  private void handleSinglePermissionCheck(String paramString1, String paramString2)
  {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, paramString1))
    {
      showNormalRationaleDialog(this, paramString2, new DialogInterface.OnClickListener(paramString1)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          ActivityCompat.requestPermissions(PermissionHandlerActivity.this, new String[] { this.val$permission }, 0);
        }
      });
      return;
    }
    ActivityCompat.requestPermissions(this, new String[] { paramString1 }, 0);
  }

  private void openAppSetting()
  {
    Intent localIntent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
    startActivityForResult(localIntent, 2);
  }

  private void requestNextPermission()
  {
    this.requestInfo = PermissionCheckHelper.instance().getNextRequest();
    if (this.requestInfo != null)
    {
      String[] arrayOfString = this.requestInfo.getPermissionArray();
      if ((arrayOfString != null) && (arrayOfString.length == 1))
        handleSinglePermissionCheck(arrayOfString[0], this.requestInfo.getMessageArray()[0]);
      do
        return;
      while ((arrayOfString == null) || (arrayOfString.length <= 1));
      handleMultiplePermissionCheck(arrayOfString, this.requestInfo.getMessageArray());
      return;
    }
    finish();
  }

  private void showNeverAskRationaleDialog(Context paramContext, String paramString)
  {
    String str = paramString;
    if (!"".equals(paramString))
      str = paramString.substring(0, paramString.length() - 1);
    paramString = new StringBuilder(str).append("。").append("\n设置路径：设置->应用->大众点评->权限");
    new AlertDialog.Builder(paramContext).setTitle("我们需要一些权限").setMessage(paramString).setPositiveButton("去设置", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        PermissionHandlerActivity.this.openAppSetting();
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        PermissionHandlerActivity.this.doCallback();
        PermissionHandlerActivity.this.requestNextPermission();
      }
    }).setCancelable(false).create().show();
  }

  private void showNormalRationaleDialog(Context paramContext, String paramString, DialogInterface.OnClickListener paramOnClickListener)
  {
    String str = paramString;
    if (!"".equals(paramString))
      str = paramString.substring(0, paramString.length() - 1);
    paramString = new StringBuilder(str).append("。");
    new AlertDialog.Builder(paramContext).setTitle("我们需要一些权限").setMessage(paramString).setPositiveButton("确定", paramOnClickListener).setCancelable(false).create().show();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 2)
    {
      this.requestInfo = PermissionCheckHelper.instance().findShouldCheckPermission(this.requestInfo);
      if (this.requestInfo != null)
        doCallback();
      requestNextPermission();
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestNextPermission();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    PermissionCheckHelper.instance().setRequestFinish();
  }

  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    if (paramInt == 0)
    {
      paramInt = 0;
      while (paramInt < paramArrayOfString.length)
      {
        this.requestInfo.getPermissionResultMap().put(paramArrayOfString[paramInt], Integer.valueOf(paramArrayOfInt[paramInt]));
        paramInt += 1;
      }
      if (!handleNeverAsk(paramArrayOfString, this.requestInfo.getMessageArray(), paramArrayOfInt))
      {
        doCallback();
        requestNextPermission();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.permission.PermissionHandlerActivity
 * JD-Core Version:    0.6.0
 */