package com.google.zxing.client.android.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

final class CameraConfigurationManager
{
  private static final String TAG = "CameraConfiguration";
  private Point cameraResolution;
  private final Context context;
  private Point screenResolution;

  CameraConfigurationManager(Context paramContext)
  {
    this.context = paramContext;
  }

  private void doSetTorch(Camera.Parameters paramParameters, boolean paramBoolean1, boolean paramBoolean2)
  {
    CameraConfigurationUtils.setTorch(paramParameters, paramBoolean1);
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
    if ((!paramBoolean2) && (!localSharedPreferences.getBoolean("preferences_disable_exposure", true)))
      CameraConfigurationUtils.setBestExposure(paramParameters, paramBoolean1);
  }

  private void initializeTorch(Camera.Parameters paramParameters, SharedPreferences paramSharedPreferences, boolean paramBoolean)
  {
    if (FrontLightMode.readPref(paramSharedPreferences) == FrontLightMode.ON);
    for (boolean bool = true; ; bool = false)
    {
      doSetTorch(paramParameters, bool, paramBoolean);
      return;
    }
  }

  Point getCameraResolution()
  {
    return this.cameraResolution;
  }

  Point getScreenResolution()
  {
    return this.screenResolution;
  }

  boolean getTorchState(Camera paramCamera)
  {
    int j = 0;
    int i = j;
    if (paramCamera != null)
    {
      paramCamera = paramCamera.getParameters();
      i = j;
      if (paramCamera != null)
      {
        paramCamera = paramCamera.getFlashMode();
        i = j;
        if (paramCamera != null)
          if (!"on".equals(paramCamera))
          {
            i = j;
            if (!"torch".equals(paramCamera));
          }
          else
          {
            i = 1;
          }
      }
    }
    return i;
  }

  void initFromCameraParameters(Camera paramCamera)
  {
    paramCamera = paramCamera.getParameters();
    Display localDisplay = ((WindowManager)this.context.getSystemService("window")).getDefaultDisplay();
    Point localPoint = new Point();
    localDisplay.getSize(localPoint);
    this.screenResolution = localPoint;
    Log.i("CameraConfiguration", "Screen resolution: " + this.screenResolution);
    this.cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(paramCamera, this.screenResolution);
    Log.i("CameraConfiguration", "Camera resolution: " + this.cameraResolution);
  }

  void setDesiredCameraParameters(Camera paramCamera, boolean paramBoolean)
  {
    Camera.Parameters localParameters = paramCamera.getParameters();
    if (localParameters == null)
      Log.w("CameraConfiguration", "Device error: no camera parameters are available. Proceeding without configuration.");
    do
    {
      return;
      Log.i("CameraConfiguration", "Initial camera parameters: " + localParameters.flatten());
      if (paramBoolean)
        Log.w("CameraConfiguration", "In camera config safe mode -- most settings will not be honored");
      SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
      initializeTorch(localParameters, localSharedPreferences, paramBoolean);
      CameraConfigurationUtils.setFocus(localParameters, localSharedPreferences.getBoolean("preferences_auto_focus", true), localSharedPreferences.getBoolean("preferences_disable_continuous_focus", true), paramBoolean);
      if (!paramBoolean)
      {
        if (localSharedPreferences.getBoolean("preferences_invert_scan", false))
          CameraConfigurationUtils.setInvertColor(localParameters);
        if (!localSharedPreferences.getBoolean("preferences_disable_barcode_scene_mode", true))
          CameraConfigurationUtils.setBarcodeSceneMode(localParameters);
        if (!localSharedPreferences.getBoolean("preferences_disable_metering", true))
        {
          CameraConfigurationUtils.setVideoStabilization(localParameters);
          CameraConfigurationUtils.setFocusArea(localParameters);
          CameraConfigurationUtils.setMetering(localParameters);
        }
      }
      localParameters.setPreviewSize(this.cameraResolution.x, this.cameraResolution.y);
      Log.i("CameraConfiguration", "Final camera parameters: " + localParameters.flatten());
      paramCamera.setDisplayOrientation(90);
      paramCamera.setParameters(localParameters);
      paramCamera = paramCamera.getParameters().getPreviewSize();
    }
    while ((paramCamera == null) || ((this.cameraResolution.x == paramCamera.width) && (this.cameraResolution.y == paramCamera.height)));
    Log.w("CameraConfiguration", "Camera said it supported preview size " + this.cameraResolution.x + 'x' + this.cameraResolution.y + ", but after setting it, preview size is " + paramCamera.width + 'x' + paramCamera.height);
    this.cameraResolution.x = paramCamera.width;
    this.cameraResolution.y = paramCamera.height;
  }

  void setTorch(Camera paramCamera, boolean paramBoolean)
  {
    Camera.Parameters localParameters = paramCamera.getParameters();
    doSetTorch(localParameters, paramBoolean, false);
    paramCamera.setParameters(localParameters);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.camera.CameraConfigurationManager
 * JD-Core Version:    0.6.0
 */