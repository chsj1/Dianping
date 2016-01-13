package com.dianping.shopinfo.common;

import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.shopinfo.base.BaseBrandStoryAgent;

public class CommonBrandStoryAgent extends BaseBrandStoryAgent
{
  public CommonBrandStoryAgent(Object paramObject)
  {
    super(paramObject);
  }

  public DPObject getData(Bundle paramBundle)
  {
    Object localObject = null;
    DPObject localDPObject2 = (DPObject)getSharedObject("shopExtraInfo");
    DPObject localDPObject1 = localDPObject2;
    if (localDPObject2 == null)
    {
      localDPObject1 = localDPObject2;
      if (paramBundle != null)
        localDPObject1 = (DPObject)paramBundle.getParcelable("shopExtraInfo");
    }
    paramBundle = localObject;
    if (localDPObject1 != null)
      paramBundle = localDPObject1.getObject("BrandStory");
    return paramBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.CommonBrandStoryAgent
 * JD-Core Version:    0.6.0
 */