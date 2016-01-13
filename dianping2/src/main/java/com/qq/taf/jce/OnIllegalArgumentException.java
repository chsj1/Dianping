package com.qq.taf.jce;

import java.nio.ByteBuffer;

public abstract interface OnIllegalArgumentException
{
  public abstract void onException(IllegalArgumentException paramIllegalArgumentException, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.qq.taf.jce.OnIllegalArgumentException
 * JD-Core Version:    0.6.0
 */