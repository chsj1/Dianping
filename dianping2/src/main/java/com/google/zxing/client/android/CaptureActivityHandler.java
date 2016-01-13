package com.google.zxing.client.android;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import java.util.Collection;
import java.util.Map;

public final class CaptureActivityHandler extends Handler
{
  private static final String TAG = CaptureActivityHandler.class.getSimpleName();
  private final CaptureActivity activity;
  private final CameraManager cameraManager;
  private final DecodeThread decodeThread;
  private State state;

  CaptureActivityHandler(CaptureActivity paramCaptureActivity, Collection<BarcodeFormat> paramCollection, Map<DecodeHintType, ?> paramMap, String paramString, CameraManager paramCameraManager)
  {
    this.activity = paramCaptureActivity;
    this.decodeThread = new DecodeThread(paramCaptureActivity, paramCollection, paramMap, paramString, new ViewfinderResultPointCallback(paramCaptureActivity.getViewfinderView()));
    this.decodeThread.start();
    this.state = State.SUCCESS;
    this.cameraManager = paramCameraManager;
    paramCameraManager.startPreview();
    restartPreviewAndDecode();
  }

  public void handleMessage(Message paramMessage)
  {
    if (paramMessage.what == R.id.restart_preview)
    {
      Log.d(TAG, "Got restart preview message");
      restartPreviewAndDecode();
    }
    do
    {
      return;
      if (paramMessage.what == R.id.decode_succeeded)
      {
        Log.d(TAG, "Got decode succeeded message");
        this.state = State.SUCCESS;
        localObject2 = paramMessage.getData();
        localObject1 = null;
        str = null;
        float f = 1.0F;
        if (localObject2 != null)
        {
          localObject3 = ((Bundle)localObject2).getByteArray("barcode_bitmap");
          localObject1 = str;
          if (localObject3 != null)
            localObject1 = BitmapFactory.decodeByteArray(localObject3, 0, localObject3.length, null).copy(Bitmap.Config.ARGB_8888, true);
          f = ((Bundle)localObject2).getFloat("barcode_scaled_factor");
        }
        this.activity.handleDecode((Result)paramMessage.obj, (Bitmap)localObject1, f);
        return;
      }
      if (paramMessage.what == R.id.decode_failed)
      {
        this.state = State.PREVIEW;
        this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), R.id.decode);
        return;
      }
      if (paramMessage.what != R.id.return_scan_result)
        continue;
      Log.d(TAG, "Got return scan result message");
      this.activity.setResult(-1, (Intent)paramMessage.obj);
      this.activity.finish();
      return;
    }
    while (paramMessage.what != R.id.launch_product_query);
    Log.d(TAG, "Got product query message");
    String str = (String)paramMessage.obj;
    Object localObject2 = new Intent("android.intent.action.VIEW");
    ((Intent)localObject2).addFlags(524288);
    ((Intent)localObject2).setData(Uri.parse(str));
    Object localObject3 = this.activity.getPackageManager().resolveActivity((Intent)localObject2, 65536);
    Object localObject1 = null;
    paramMessage = (Message)localObject1;
    if (localObject3 != null)
    {
      paramMessage = (Message)localObject1;
      if (((ResolveInfo)localObject3).activityInfo != null)
      {
        paramMessage = ((ResolveInfo)localObject3).activityInfo.packageName;
        Log.d(TAG, "Using browser in package " + paramMessage);
      }
    }
    if (("com.android.browser".equals(paramMessage)) || ("com.android.chrome".equals(paramMessage)))
    {
      ((Intent)localObject2).setPackage(paramMessage);
      ((Intent)localObject2).addFlags(268435456);
      ((Intent)localObject2).putExtra("com.android.browser.application_id", paramMessage);
    }
    try
    {
      this.activity.startActivity((Intent)localObject2);
      return;
    }
    catch (android.content.ActivityNotFoundException paramMessage)
    {
      Log.w(TAG, "Can't find anything to handle VIEW of URI " + str);
    }
  }

  public void quitSynchronously()
  {
    this.state = State.DONE;
    this.cameraManager.stopPreview();
    Message.obtain(this.decodeThread.getHandler(), R.id.quit).sendToTarget();
    try
    {
      this.decodeThread.join(500L);
      label40: removeMessages(R.id.decode_succeeded);
      removeMessages(R.id.decode_failed);
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      break label40;
    }
  }

  public void restartPreviewAndDecode()
  {
    if (this.state == State.SUCCESS)
    {
      this.state = State.PREVIEW;
      this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), R.id.decode);
      this.activity.drawViewfinder();
    }
  }

  private static enum State
  {
    static
    {
      DONE = new State("DONE", 2);
      $VALUES = new State[] { PREVIEW, SUCCESS, DONE };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.CaptureActivityHandler
 * JD-Core Version:    0.6.0
 */