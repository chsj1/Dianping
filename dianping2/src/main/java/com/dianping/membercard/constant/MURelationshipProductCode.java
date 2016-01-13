package com.dianping.membercard.constant;

public enum MURelationshipProductCode
{
  private int codeValue;

  static
  {
    TAKEAWAY = new MURelationshipProductCode("TAKEAWAY", 2, 30);
    BOOKING = new MURelationshipProductCode("BOOKING", 3, 40);
    $VALUES = new MURelationshipProductCode[] { HUI, TUAN, TAKEAWAY, BOOKING };
  }

  private MURelationshipProductCode(int paramInt)
  {
    this.codeValue = paramInt;
  }

  public int getCodeValue()
  {
    return this.codeValue;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.constant.MURelationshipProductCode
 * JD-Core Version:    0.6.0
 */