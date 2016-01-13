package com.tencent.tencentmap.mapsdk.maps;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.tencent.tencentmap.mapsdk.maps.a.eg;
import com.tencent.tencentmap.mapsdk.maps.a.hn;
import com.tencent.tencentmap.mapsdk.maps.a.hq;

public class MapView extends FrameLayout
{
  private TencentMap mSosoMap = null;
  private hq overlay;

  public MapView(Context paramContext)
  {
    super(paramContext);
    setClickable(true);
    if (isInEditMode() == true)
      return;
    initMap(paramContext);
  }

  public MapView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public MapView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    setClickable(true);
    if (isInEditMode() == true)
      return;
    initMap(paramContext);
  }

  private void addWorldMap()
  {
    if (this.overlay == null)
      this.overlay = this.mSosoMap.addWorldMapOverlay(hn.a());
  }

  private void initMap(Context paramContext)
  {
    setBackgroundColor(Color.rgb(222, 215, 214));
    setEnabled(true);
    this.mSosoMap = new TencentMap(this, paramContext);
    if (worldMapEnabled())
      addWorldMap();
  }

  private void removeWorldMap()
  {
    if (this.overlay != null)
    {
      this.overlay.a();
      this.overlay = null;
    }
  }

  private boolean worldMapEnabled()
  {
    return Boolean.parseBoolean(eg.a(getContext(), eg.c));
  }

  public final TencentMap getMap()
  {
    return this.mSosoMap;
  }

  public void onDestroy()
  {
    removeWorldMap();
    if (this.mSosoMap != null)
    {
      this.mSosoMap.onDestroy();
      this.mSosoMap = null;
    }
  }

  public void onPause()
  {
    if (this.mSosoMap != null)
      this.mSosoMap.onPause();
  }

  public void onRestart()
  {
    if (this.mSosoMap != null)
      this.mSosoMap.onRestart();
  }

  public void onResume()
  {
    if (this.mSosoMap != null)
      this.mSosoMap.onResume();
  }

  public void onStart()
  {
    if (this.mSosoMap != null)
      this.mSosoMap.onStart();
  }

  public void onStop()
  {
    if (this.mSosoMap != null)
      this.mSosoMap.onStop();
  }

  void setOnTop(boolean paramBoolean)
  {
    this.mSosoMap.setOnTop(paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tencentmap.mapsdk.maps.MapView
 * JD-Core Version:    0.6.0
 */