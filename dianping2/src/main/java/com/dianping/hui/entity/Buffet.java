package com.dianping.hui.entity;

import java.util.List;

public class Buffet
{
  public int count = 0;
  public int couponId;
  public List<BuffetRule> details;
  public String discountPrice;
  public boolean isExpand;
  public int maxCount = 30;
  public int minCount = 0;
  public String originalPrice;
  public int status = 0;
  public String statusDesc;
  public String title;
  public double totalValue;

  public boolean add()
  {
    if (this.count < this.maxCount)
    {
      this.count += 1;
      this.totalValue += Double.parseDouble(this.discountPrice);
      return true;
    }
    return false;
  }

  public boolean input(int paramInt)
  {
    if ((this.count >= this.minCount) && (this.count <= this.maxCount))
    {
      this.count = paramInt;
      this.totalValue = (paramInt * Double.parseDouble(this.discountPrice));
      return true;
    }
    return false;
  }

  public boolean sub()
  {
    if (this.count > this.minCount)
    {
      this.count -= 1;
      this.totalValue -= Double.parseDouble(this.discountPrice);
      return true;
    }
    return false;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.entity.Buffet
 * JD-Core Version:    0.6.0
 */