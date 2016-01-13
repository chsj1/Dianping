package com.dianping.map.utils;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds.Builder;
import java.util.ArrayList;
import java.util.List;

public class ThirdpartyMapUtils
{
  public static void moveToLatLng(TencentMap paramTencentMap, LatLng paramLatLng, boolean paramBoolean)
  {
    if ((paramLatLng == null) || (paramTencentMap == null))
      return;
    if (paramBoolean)
    {
      paramTencentMap.animateCamera(CameraUpdateFactory.newLatLng(paramLatLng));
      return;
    }
    paramTencentMap.moveCamera(CameraUpdateFactory.newLatLng(paramLatLng));
  }

  public static void zoomTo(TencentMap paramTencentMap, float paramFloat)
  {
    if (paramTencentMap == null);
    do
      return;
    while ((paramFloat < paramTencentMap.getMinZoomLevel()) || (paramFloat > paramTencentMap.getMaxZoomLevel()));
    paramTencentMap.setOnMapLoadedCallback(new ThirdpartyMapUtils.1(paramTencentMap, paramFloat));
  }

  public static void zoomToSpan(TencentMap paramTencentMap, List<LatLng> paramList, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramList == null) || (paramTencentMap == null));
    int j;
    do
    {
      return;
      j = paramList.size();
    }
    while (j <= 0);
    LatLngBounds.Builder localBuilder = new LatLngBounds.Builder();
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    int i = 0;
    if (i < j)
    {
      LatLng localLatLng = (LatLng)paramList.get(i);
      if (localLatLng == null);
      while (true)
      {
        i += 1;
        break;
        localArrayList1.add(Double.valueOf(localLatLng.latitude));
        localArrayList2.add(Double.valueOf(localLatLng.longitude));
        localBuilder.include(localLatLng);
      }
    }
    paramTencentMap.animateCamera(CameraUpdateFactory.newLatLngBoundsRect(localBuilder.build(), paramInt1, paramInt2, paramInt3, paramInt4));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.utils.ThirdpartyMapUtils
 * JD-Core Version:    0.6.0
 */