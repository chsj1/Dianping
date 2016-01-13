package com.dianping.locationservice.impl286.model;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;
import java.util.List<Lcom.dianping.locationservice.impl286.model.CoordModel;>;

public class CoordModel
{
  public static final int SOURCE_AMAP = 2;
  public static final int SOURCE_BMAP = 3;
  public static final int SOURCE_GPS = 0;
  public static final int SOURCE_NETWORK = 1;
  protected int mAcc;
  protected long mElapse;
  protected double mLat;
  protected double mLng;
  protected int mSource;

  public CoordModel(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, long paramLong)
  {
    this.mSource = paramInt1;
    this.mLat = paramDouble1;
    this.mLng = paramDouble2;
    this.mAcc = paramInt2;
    this.mElapse = paramLong;
  }

  private static String formatCoord(CoordModel paramCoordModel, String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return paramCoordModel.toDPString();
    return String.format("%s|%s", new Object[] { paramString, paramCoordModel.toDPString() });
  }

  public static String listToAMapString(List<CoordModel> paramList)
  {
    Object localObject = "";
    if (paramList == null)
      return "";
    Iterator localIterator = paramList.iterator();
    paramList = (List<CoordModel>)localObject;
    while (localIterator.hasNext())
    {
      localObject = (CoordModel)localIterator.next();
      if (2 != ((CoordModel)localObject).getSource())
        continue;
      paramList = formatCoord((CoordModel)localObject, paramList);
    }
    return (String)paramList;
  }

  public static String listToBMapString(List<CoordModel> paramList)
  {
    Object localObject = "";
    if (paramList == null)
      return "";
    Iterator localIterator = paramList.iterator();
    paramList = (List<CoordModel>)localObject;
    while (localIterator.hasNext())
    {
      localObject = (CoordModel)localIterator.next();
      if (3 != ((CoordModel)localObject).getSource())
        continue;
      paramList = formatCoord((CoordModel)localObject, paramList);
    }
    return (String)paramList;
  }

  public static String listToGpsString(List<CoordModel> paramList)
  {
    Object localObject = "";
    if (paramList == null)
      return "";
    Iterator localIterator = paramList.iterator();
    paramList = (List<CoordModel>)localObject;
    while (localIterator.hasNext())
    {
      localObject = (CoordModel)localIterator.next();
      if (((CoordModel)localObject).getSource() != 0)
        continue;
      paramList = formatCoord((CoordModel)localObject, paramList);
    }
    return (String)paramList;
  }

  public static String listToNetworkString(List<CoordModel> paramList)
  {
    Object localObject = "";
    if (paramList == null)
      return "";
    Iterator localIterator = paramList.iterator();
    paramList = (List<CoordModel>)localObject;
    while (localIterator.hasNext())
    {
      localObject = (CoordModel)localIterator.next();
      if (1 != ((CoordModel)localObject).getSource())
        continue;
      paramList = formatCoord((CoordModel)localObject, paramList);
    }
    return (String)paramList;
  }

  public static String listToString(List<CoordModel> paramList)
  {
    String str = "";
    if (paramList == null)
      return "";
    Iterator localIterator = paramList.iterator();
    for (paramList = str; localIterator.hasNext(); paramList = formatCoord((CoordModel)localIterator.next(), paramList));
    return paramList;
  }

  public int getAcc()
  {
    return this.mAcc;
  }

  public long getElapse()
  {
    return this.mElapse;
  }

  public double getLat()
  {
    return this.mLat;
  }

  public double getLng()
  {
    return this.mLng;
  }

  public int getSource()
  {
    return this.mSource;
  }

  public String toDPString()
  {
    return String.format("%s,%s@%s,%s", new Object[] { Double.valueOf(this.mLat), Double.valueOf(this.mLng), Integer.valueOf(this.mAcc), Long.valueOf(this.mElapse) });
  }

  public String toString()
  {
    return String.format("{source:%s,Lat:%s,Lng:%s,Accuracy:%s,Elapse:%s}", new Object[] { Integer.valueOf(this.mSource), Double.valueOf(this.mLat), Double.valueOf(this.mLng), Integer.valueOf(this.mAcc), Long.valueOf(this.mElapse) });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.model.CoordModel
 * JD-Core Version:    0.6.0
 */