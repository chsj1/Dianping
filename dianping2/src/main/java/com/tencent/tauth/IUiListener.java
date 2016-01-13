package com.tencent.tauth;

public abstract interface IUiListener
{
  public abstract void onCancel();

  public abstract void onComplete(Object paramObject);

  public abstract void onError(UiError paramUiError);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tauth.IUiListener
 * JD-Core Version:    0.6.0
 */