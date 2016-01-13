package com.google.zxing.client.android.result;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.R.string;
import com.google.zxing.client.android.common.executor.AsyncTaskExecInterface;
import com.google.zxing.client.android.common.executor.AsyncTaskExecManager;
import com.google.zxing.client.android.wifi.WifiConfigManager;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.WifiParsedResult;

public final class WifiResultHandler extends ResultHandler
{
  private static final String TAG = WifiResultHandler.class.getSimpleName();
  private final CaptureActivity parent;
  private final AsyncTaskExecInterface taskExec;

  public WifiResultHandler(CaptureActivity paramCaptureActivity, ParsedResult paramParsedResult)
  {
    super(paramCaptureActivity, paramParsedResult);
    this.parent = paramCaptureActivity;
    this.taskExec = ((AsyncTaskExecInterface)new AsyncTaskExecManager().build());
  }

  public int getButtonCount()
  {
    return 1;
  }

  public int getButtonText(int paramInt)
  {
    return R.string.button_wifi;
  }

  public CharSequence getDisplayContents()
  {
    WifiParsedResult localWifiParsedResult = (WifiParsedResult)getResult();
    return localWifiParsedResult.getSsid() + " (" + localWifiParsedResult.getNetworkEncryption() + ')';
  }

  public int getDisplayTitle()
  {
    return R.string.result_wifi;
  }

  public void handleButtonPress(int paramInt)
  {
    WifiParsedResult localWifiParsedResult;
    WifiManager localWifiManager;
    if (paramInt == 0)
    {
      localWifiParsedResult = (WifiParsedResult)getResult();
      localWifiManager = (WifiManager)getActivity().getSystemService("wifi");
      if (localWifiManager == null)
        Log.w(TAG, "No WifiManager available from device");
    }
    else
    {
      return;
    }
    Activity localActivity = getActivity();
    localActivity.runOnUiThread(new Runnable(localActivity)
    {
      public void run()
      {
        Toast.makeText(this.val$activity.getApplicationContext(), R.string.wifi_changing_network, 0).show();
      }
    });
    this.taskExec.execute(new WifiConfigManager(localWifiManager), new WifiParsedResult[] { localWifiParsedResult });
    this.parent.restartPreviewAfterDelay(0L);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.WifiResultHandler
 * JD-Core Version:    0.6.0
 */