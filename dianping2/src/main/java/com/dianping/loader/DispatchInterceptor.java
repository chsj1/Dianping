package com.dianping.loader;

import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract interface DispatchInterceptor
{
  public abstract boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent);

  public abstract boolean dispatchKeyEvent(KeyEvent paramKeyEvent);

  public abstract boolean dispatchKeyShortcutEvent(KeyEvent paramKeyEvent);

  public abstract boolean dispatchTouchEvent(MotionEvent paramMotionEvent);

  public abstract boolean dispatchTrackballEvent(MotionEvent paramMotionEvent);

  public abstract void onConfigurationChanged(Configuration paramConfiguration);

  public abstract boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.DispatchInterceptor
 * JD-Core Version:    0.6.0
 */