package com.dianping.map.elements;

import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import java.util.HashMap;

public abstract interface OnRouteChangeListener
{
  public abstract void OnFinalRouteModeSelected(int paramInt, boolean paramBoolean);

  public abstract void OnRouteChanged(int paramInt, boolean paramBoolean, HashMap<View, FrameLayout.LayoutParams> paramHashMap, String paramString);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.elements.OnRouteChangeListener
 * JD-Core Version:    0.6.0
 */