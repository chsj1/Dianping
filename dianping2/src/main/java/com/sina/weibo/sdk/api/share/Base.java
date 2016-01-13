package com.sina.weibo.sdk.api.share;

import android.os.Bundle;

public abstract class Base
{
  public String transaction;

  public abstract void fromBundle(Bundle paramBundle);

  public abstract int getType();

  public abstract void toBundle(Bundle paramBundle);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.sina.weibo.sdk.api.share.Base
 * JD-Core Version:    0.6.0
 */