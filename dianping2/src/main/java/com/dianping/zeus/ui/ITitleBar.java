package com.dianping.zeus.ui;

import android.graphics.Bitmap;
import android.view.View.OnClickListener;
import org.json.JSONObject;

public abstract interface ITitleBar
{
  public abstract String getWebTitle();

  public abstract void performLLClick();

  public abstract void setBackgroundColor(int paramInt);

  public abstract void setLLButton(Bitmap paramBitmap, View.OnClickListener paramOnClickListener);

  public abstract void setLLButton(String paramString, int paramInt, boolean paramBoolean, View.OnClickListener paramOnClickListener);

  public abstract void setLLButton(String paramString1, String paramString2, boolean paramBoolean, View.OnClickListener paramOnClickListener);

  public abstract void setLRButton(Bitmap paramBitmap, View.OnClickListener paramOnClickListener);

  public abstract void setLRButton(String paramString, int paramInt, boolean paramBoolean, View.OnClickListener paramOnClickListener);

  public abstract void setLRButton(String paramString1, String paramString2, boolean paramBoolean, View.OnClickListener paramOnClickListener);

  public abstract void setProgress(int paramInt);

  public abstract void setRLButton(Bitmap paramBitmap, View.OnClickListener paramOnClickListener);

  public abstract void setRLButton(String paramString, int paramInt, boolean paramBoolean, View.OnClickListener paramOnClickListener);

  public abstract void setRLButton(String paramString1, String paramString2, boolean paramBoolean, View.OnClickListener paramOnClickListener);

  public abstract void setRRButton(Bitmap paramBitmap, View.OnClickListener paramOnClickListener);

  public abstract void setRRButton(String paramString, int paramInt, boolean paramBoolean, View.OnClickListener paramOnClickListener);

  public abstract void setRRButton(String paramString1, String paramString2, boolean paramBoolean, View.OnClickListener paramOnClickListener);

  public abstract void setTitleContentParams(JSONObject paramJSONObject);

  public abstract void setWebTitle(String paramString);

  public abstract void showProgressBar(boolean paramBoolean);

  public abstract void showTitleBar(boolean paramBoolean);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.zeus.ui.ITitleBar
 * JD-Core Version:    0.6.0
 */