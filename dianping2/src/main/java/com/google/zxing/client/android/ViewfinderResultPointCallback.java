package com.google.zxing.client.android;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

final class ViewfinderResultPointCallback
  implements ResultPointCallback
{
  private final ViewfinderView viewfinderView;

  ViewfinderResultPointCallback(ViewfinderView paramViewfinderView)
  {
    this.viewfinderView = paramViewfinderView;
  }

  public void foundPossibleResultPoint(ResultPoint paramResultPoint)
  {
    this.viewfinderView.addPossibleResultPoint(paramResultPoint);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.ViewfinderResultPointCallback
 * JD-Core Version:    0.6.0
 */