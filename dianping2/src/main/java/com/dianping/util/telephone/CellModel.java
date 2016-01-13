package com.dianping.util.telephone;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;
import java.util.List<Lcom.dianping.util.telephone.CellModel;>;

public class CellModel
{
  public static final int TYPE_CDMA = 1;
  public static final int TYPE_GSM = 0;
  public static final int TYPE_LTE = 2;
  public static final int TYPE_WCDMA = 3;
  protected int mAsu;
  protected int mCidBid;
  protected int mLacNid;
  protected int mLat;
  protected int mLng;
  protected int mMcc;
  protected int mMncSid;
  protected int mType;

  public CellModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, 0, 0);
  }

  public CellModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, 0, paramInt6, paramInt7);
  }

  private CellModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    this.mType = paramInt1;
    this.mMcc = paramInt2;
    this.mMncSid = paramInt3;
    this.mCidBid = paramInt4;
    this.mLacNid = paramInt5;
    this.mAsu = paramInt6;
    this.mLat = paramInt7;
    this.mLng = paramInt8;
  }

  private static String formatCell(CellModel paramCellModel, String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return paramCellModel.toDPString();
    return String.format("%s|%s", new Object[] { paramString, paramCellModel.toDPStringNoTypeMccMnc() });
  }

  public static String listToCdmaString(List<CellModel> paramList)
  {
    Object localObject = "";
    if (paramList == null)
      return "";
    Iterator localIterator = paramList.iterator();
    paramList = (List<CellModel>)localObject;
    while (localIterator.hasNext())
    {
      localObject = (CellModel)localIterator.next();
      if (1 != ((CellModel)localObject).getType())
        continue;
      paramList = formatCell((CellModel)localObject, paramList);
    }
    return (String)paramList;
  }

  public static String listToGsmString(List<CellModel> paramList)
  {
    Object localObject = "";
    if (paramList == null)
      return "";
    Iterator localIterator = paramList.iterator();
    paramList = (List<CellModel>)localObject;
    while (localIterator.hasNext())
    {
      localObject = (CellModel)localIterator.next();
      if (((CellModel)localObject).getType() != 0)
        continue;
      paramList = formatCell((CellModel)localObject, paramList);
    }
    return (String)paramList;
  }

  public static String listToLteString(List<CellModel> paramList)
  {
    Object localObject = "";
    if (paramList == null)
      return "";
    Iterator localIterator = paramList.iterator();
    paramList = (List<CellModel>)localObject;
    while (localIterator.hasNext())
    {
      localObject = (CellModel)localIterator.next();
      if (2 != ((CellModel)localObject).getType())
        continue;
      paramList = formatCell((CellModel)localObject, paramList);
    }
    return (String)paramList;
  }

  public static String listToString(List<CellModel> paramList)
  {
    String str = "";
    if (paramList == null)
      return "";
    Iterator localIterator = paramList.iterator();
    for (paramList = str; localIterator.hasNext(); paramList = formatCell((CellModel)localIterator.next(), paramList));
    return paramList;
  }

  public static String listToWcdmaString(List<CellModel> paramList)
  {
    Object localObject = "";
    if (paramList == null)
      return "";
    Iterator localIterator = paramList.iterator();
    paramList = (List<CellModel>)localObject;
    while (localIterator.hasNext())
    {
      localObject = (CellModel)localIterator.next();
      if (3 != ((CellModel)localObject).getType())
        continue;
      paramList = formatCell((CellModel)localObject, paramList);
    }
    return (String)paramList;
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject);
    do
    {
      return true;
      if (!(paramObject instanceof CellModel))
        break;
      paramObject = (CellModel)paramObject;
    }
    while ((this.mMcc == paramObject.getMcc()) && (this.mMncSid == paramObject.getMncSid()) && (this.mCidBid == paramObject.getCidBid()) && (this.mLacNid == paramObject.getLacNid()));
    return false;
  }

  public int getAsu()
  {
    return this.mAsu;
  }

  public int getCidBid()
  {
    return this.mCidBid;
  }

  public int getLacNid()
  {
    return this.mLacNid;
  }

  public int getLat()
  {
    return this.mLat;
  }

  public int getLng()
  {
    return this.mLng;
  }

  public int getMcc()
  {
    return this.mMcc;
  }

  public int getMncSid()
  {
    return this.mMncSid;
  }

  public int getType()
  {
    return this.mType;
  }

  public int hashCode()
  {
    return this.mMcc + this.mMncSid + this.mCidBid + this.mLacNid;
  }

  public String toDPString()
  {
    return String.format("%s,%s:%s,%s,%s", new Object[] { Integer.valueOf(this.mMcc), Integer.valueOf(this.mMncSid), Integer.valueOf(this.mCidBid), Integer.valueOf(this.mLacNid), Integer.valueOf(this.mAsu) });
  }

  public String toDPStringNoTypeMccMnc()
  {
    return String.format("%s,%s,%s", new Object[] { Integer.valueOf(this.mCidBid), Integer.valueOf(this.mLacNid), Integer.valueOf(this.mAsu) });
  }

  public String toString()
  {
    return String.format("{type:%s,mcc:%s,mnc:%s,cid:%s,lac:%s,asu:%s}", new Object[] { Integer.valueOf(this.mType), Integer.valueOf(this.mMcc), Integer.valueOf(this.mMncSid), Integer.valueOf(this.mCidBid), Integer.valueOf(this.mLacNid), Integer.valueOf(this.mAsu) });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.telephone.CellModel
 * JD-Core Version:    0.6.0
 */