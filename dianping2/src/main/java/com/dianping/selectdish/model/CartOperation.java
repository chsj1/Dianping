package com.dianping.selectdish.model;

public class CartOperation
{
  public CartItem cartItem;
  public int countChange;

  public CartOperation(CartItem paramCartItem, int paramInt)
  {
    this.cartItem = paramCartItem;
    this.countChange = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.model.CartOperation
 * JD-Core Version:    0.6.0
 */