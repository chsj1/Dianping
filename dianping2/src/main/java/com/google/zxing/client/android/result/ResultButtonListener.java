package com.google.zxing.client.android.result;

import android.view.View;
import android.view.View.OnClickListener;

public final class ResultButtonListener
  implements View.OnClickListener
{
  private final int index;
  private final ResultHandler resultHandler;

  public ResultButtonListener(ResultHandler paramResultHandler, int paramInt)
  {
    this.resultHandler = paramResultHandler;
    this.index = paramInt;
  }

  public void onClick(View paramView)
  {
    this.resultHandler.handleButtonPress(this.index);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.google.zxing.client.android.result.ResultButtonListener
 * JD-Core Version:    0.6.0
 */