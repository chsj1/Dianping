package com.dianping.locationservice.impl286.model;

public class CellLteModel extends CellModel
{
  public CellLteModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    super(2, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
  }

  public String toString()
  {
    return String.format("{type:%s,mcc:%s,mnc:%s,ci:%s,tac:%s,asu:%s}", new Object[] { Integer.valueOf(this.mType), Integer.valueOf(this.mMcc), Integer.valueOf(this.mMncSid), Integer.valueOf(this.mCidBid), Integer.valueOf(this.mLacNid), Integer.valueOf(this.mAsu) });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.model.CellLteModel
 * JD-Core Version:    0.6.0
 */