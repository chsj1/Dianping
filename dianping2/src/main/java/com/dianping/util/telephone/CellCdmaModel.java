package com.dianping.util.telephone;

public class CellCdmaModel extends CellModel
{
  public CellCdmaModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    super(1, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
  }

  public String toDPString()
  {
    return String.format("%s,%s:%s,%s,%s,%s", new Object[] { Integer.valueOf(this.mMcc), Integer.valueOf(this.mMncSid), Integer.valueOf(this.mCidBid), Integer.valueOf(this.mLacNid), Integer.valueOf(this.mLat), Integer.valueOf(this.mLng) });
  }

  public String toDPStringNoTypeMccMnc()
  {
    return String.format("%s,%s,%s,%s", new Object[] { Integer.valueOf(this.mCidBid), Integer.valueOf(this.mLacNid), Integer.valueOf(this.mLat), Integer.valueOf(this.mLng) });
  }

  public String toString()
  {
    return String.format("{type:%s,mcc:%s,sid:%s,bid:%s,nid:%s,lat:%s,lng:%s}", new Object[] { Integer.valueOf(this.mType), Integer.valueOf(this.mMcc), Integer.valueOf(this.mMncSid), Integer.valueOf(this.mCidBid), Integer.valueOf(this.mLacNid), Integer.valueOf(this.mLat), Integer.valueOf(this.mLng) });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.telephone.CellCdmaModel
 * JD-Core Version:    0.6.0
 */