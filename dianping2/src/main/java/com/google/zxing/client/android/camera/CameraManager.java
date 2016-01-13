package com.google.zxing.client.android.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.client.android.camera.open.OpenCameraInterface;
import java.io.IOException;

public final class CameraManager
{
  private static final int MAX_FRAME_HEIGHT = 675;
  private static final int MAX_FRAME_WIDTH = 1200;
  private static final int MIN_FRAME_HEIGHT = 240;
  private static final int MIN_FRAME_WIDTH = 240;
  private static final String TAG = CameraManager.class.getSimpleName();
  private AutoFocusManager autoFocusManager;
  private Camera camera;
  private final CameraConfigurationManager configManager;
  private final Context context;
  private Rect framingRect;
  private Rect framingRectInPreview;
  private boolean initialized;
  private final PreviewCallback previewCallback;
  private boolean previewing;
  private int requestedCameraId = -1;
  private int requestedFramingRectHeight;
  private int requestedFramingRectWidth;

  public CameraManager(Context paramContext)
  {
    this.context = paramContext;
    this.configManager = new CameraConfigurationManager(paramContext);
    this.previewCallback = new PreviewCallback(this.configManager);
  }

  private static int findDesiredDimensionInRange(int paramInt1, int paramInt2, int paramInt3)
  {
    paramInt1 = paramInt1 * 5 / 8;
    if (paramInt1 < paramInt2)
      return paramInt2;
    if (paramInt1 > paramInt3)
      return paramInt3;
    return paramInt1;
  }

  public PlanarYUVLuminanceSource buildLuminanceSource(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    Rect localRect = getFramingRectInPreview();
    if (localRect == null)
      return null;
    return new PlanarYUVLuminanceSource(paramArrayOfByte, paramInt1, paramInt2, localRect.left, localRect.top, localRect.width(), localRect.height(), false);
  }

  public void closeDriver()
  {
    monitorenter;
    try
    {
      if (this.camera != null)
      {
        this.camera.release();
        this.camera = null;
        this.framingRect = null;
        this.framingRectInPreview = null;
      }
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public Rect getFramingRect()
  {
    Rect localRect = null;
    monitorenter;
    try
    {
      Object localObject2;
      if (this.framingRect == null)
      {
        localObject2 = this.camera;
        if (localObject2 != null);
      }
      while (true)
      {
        return localRect;
        localObject2 = this.configManager.getScreenResolution();
        if (localObject2 == null)
          continue;
        int i = Math.min(findDesiredDimensionInRange(((Point)localObject2).x, 240, 1200), findDesiredDimensionInRange(((Point)localObject2).y, 240, 675));
        int j = (((Point)localObject2).x - i) / 2;
        int k = (((Point)localObject2).y - i) / 2;
        this.framingRect = new Rect(j, k, j + i, k + i);
        Log.d(TAG, "Calculated framing rect: " + this.framingRect);
        localRect = this.framingRect;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject1;
  }

  public Rect getFramingRectInPreview()
  {
    Object localObject3 = null;
    monitorenter;
    try
    {
      Object localObject1;
      if (this.framingRectInPreview == null)
      {
        localObject1 = getFramingRect();
        if (localObject1 == null)
          localObject1 = localObject3;
      }
      while (true)
      {
        return localObject1;
        Rect localRect = new Rect((Rect)localObject1);
        Point localPoint1 = this.configManager.getCameraResolution();
        Point localPoint2 = this.configManager.getScreenResolution();
        localObject1 = localObject3;
        if (localPoint1 == null)
          continue;
        localObject1 = localObject3;
        if (localPoint2 == null)
          continue;
        localRect.left = (localRect.left * localPoint1.y / localPoint2.x);
        localRect.right = (localRect.right * localPoint1.y / localPoint2.x);
        localRect.top = (localRect.top * localPoint1.x / localPoint2.y);
        localRect.bottom = (localRect.bottom * localPoint1.x / localPoint2.y);
        this.framingRectInPreview = localRect;
        localObject1 = this.framingRectInPreview;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject2;
  }

  public boolean isOpen()
  {
    monitorenter;
    try
    {
      Camera localCamera = this.camera;
      if (localCamera != null)
      {
        i = 1;
        return i;
      }
      int i = 0;
    }
    finally
    {
      monitorexit;
    }
  }

  public void openDriver(SurfaceHolder paramSurfaceHolder)
    throws IOException
  {
    monitorenter;
    Camera localCamera1;
    try
    {
      Camera localCamera2 = this.camera;
      localCamera1 = localCamera2;
      if (localCamera2 != null)
        break label43;
      localCamera1 = OpenCameraInterface.open(this.requestedCameraId);
      if (localCamera1 == null)
        throw new IOException();
    }
    finally
    {
      monitorexit;
    }
    this.camera = localCamera1;
    label43: localCamera1.setPreviewDisplay(paramSurfaceHolder);
    if (!this.initialized)
    {
      this.initialized = true;
      this.configManager.initFromCameraParameters(localCamera1);
      if ((this.requestedFramingRectWidth > 0) && (this.requestedFramingRectHeight > 0))
      {
        setManualFramingRect(this.requestedFramingRectWidth, this.requestedFramingRectHeight);
        this.requestedFramingRectWidth = 0;
        this.requestedFramingRectHeight = 0;
      }
    }
    paramSurfaceHolder = localCamera1.getParameters();
    if (paramSurfaceHolder == null)
      paramSurfaceHolder = null;
    try
    {
      while (true)
      {
        this.configManager.setDesiredCameraParameters(localCamera1, false);
        monitorexit;
        return;
        paramSurfaceHolder = paramSurfaceHolder.flatten();
      }
    }
    catch (RuntimeException localParameters)
    {
      while (true)
      {
        Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
        Log.i(TAG, "Resetting to saved camera params: " + paramSurfaceHolder);
        if (paramSurfaceHolder == null)
          continue;
        Camera.Parameters localParameters = localCamera1.getParameters();
        localParameters.unflatten(paramSurfaceHolder);
        try
        {
          localCamera1.setParameters(localParameters);
          this.configManager.setDesiredCameraParameters(localCamera1, true);
        }
        catch (RuntimeException paramSurfaceHolder)
        {
          Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
        }
      }
    }
  }

  public void requestPreviewFrame(Handler paramHandler, int paramInt)
  {
    monitorenter;
    try
    {
      Camera localCamera = this.camera;
      if ((localCamera != null) && (this.previewing))
      {
        this.previewCallback.setHandler(paramHandler, paramInt);
        localCamera.setOneShotPreviewCallback(this.previewCallback);
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw paramHandler;
  }

  public void setManualCameraId(int paramInt)
  {
    monitorenter;
    try
    {
      this.requestedCameraId = paramInt;
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  public void setManualFramingRect(int paramInt1, int paramInt2)
  {
    monitorenter;
    try
    {
      if (this.initialized)
      {
        Point localPoint = this.configManager.getScreenResolution();
        int i = paramInt1;
        if (paramInt1 > localPoint.x)
          i = localPoint.x;
        paramInt1 = paramInt2;
        if (paramInt2 > localPoint.y)
          paramInt1 = localPoint.y;
        paramInt2 = (localPoint.x - i) / 2;
        int j = (localPoint.y - paramInt1) / 2;
        this.framingRect = new Rect(paramInt2, j, paramInt2 + i, j + paramInt1);
        Log.d(TAG, "Calculated manual framing rect: " + this.framingRect);
        this.framingRectInPreview = null;
      }
      while (true)
      {
        return;
        this.requestedFramingRectWidth = paramInt1;
        this.requestedFramingRectHeight = paramInt2;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void setTorch(boolean paramBoolean)
  {
    monitorenter;
    try
    {
      if ((paramBoolean != this.configManager.getTorchState(this.camera)) && (this.camera != null))
      {
        if (this.autoFocusManager != null)
          this.autoFocusManager.stop();
        this.configManager.setTorch(this.camera, paramBoolean);
        if (this.autoFocusManager != null)
          this.autoFocusManager.start();
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void startPreview()
  {
    monitorenter;
    try
    {
      Camera localCamera = this.camera;
      if ((localCamera != null) && (!this.previewing))
      {
        localCamera.startPreview();
        this.previewing = true;
        this.autoFocusManager = new AutoFocusManager(this.context, this.camera);
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void stopPreview()
  {
    monitorenter;
    try
    {
      if (this.autoFocusManager != null)
      {
        this.autoFocusManager.stop();
        this.autoFocusManager = null;
      }
      if ((this.camera != null) && (this.previewing))
      {
        this.camera.stopPreview();
        this.previewCallback.setHandler(null, 0);
        this.previewing = false;
      }
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.camera.CameraManager
 * JD-Core Version:    0.6.0
 */