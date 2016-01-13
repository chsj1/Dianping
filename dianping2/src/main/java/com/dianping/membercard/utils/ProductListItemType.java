package com.dianping.membercard.utils;

public enum ProductListItemType
{
  private int value;

  static
  {
    $VALUES = new ProductListItemType[] { SAVING, TIMES };
  }

  private ProductListItemType(int paramInt)
  {
    this.value = paramInt;
  }

  public boolean equals(int paramInt)
  {
    return this.value == paramInt;
  }

  public boolean equals(ProductListItemType paramProductListItemType)
  {
    return this.value == paramProductListItemType.value;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.ProductListItemType
 * JD-Core Version:    0.6.0
 */