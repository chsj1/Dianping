package com.google.zxing.client.android.camera.open;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;

public final class OpenCameraInterface
{
  public static final int NO_REQUESTED_CAMERA = -1;
  private static final String TAG = OpenCameraInterface.class.getName();

  public static Camera open(int paramInt)
  {
    int j = Camera.getNumberOfCameras();
    if (j == 0)
    {
      Log.w(TAG, "No cameras!");
      return null;
    }
    int i;
    if (paramInt >= 0)
    {
      i = 1;
      if (i == 0)
        paramInt = 0;
    }
    while (true)
    {
      if (paramInt < j)
      {
        Camera.CameraInfo localCameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(paramInt, localCameraInfo);
        if (localCameraInfo.facing != 0);
      }
      else
      {
        if (paramInt >= j)
          break label104;
        Log.i(TAG, "Opening camera #" + paramInt);
        return Camera.open(paramInt);
        i = 0;
        break;
      }
      paramInt += 1;
    }
    label104: if (i != 0)
    {
      Log.w(TAG, "Requested camera does not exist: " + paramInt);
      return null;
    }
    Log.i(TAG, "No camera facing back; returning camera #0");
    return Camera.open(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.camera.open.OpenCameraInterface
 * JD-Core Version:    0.6.0
 */