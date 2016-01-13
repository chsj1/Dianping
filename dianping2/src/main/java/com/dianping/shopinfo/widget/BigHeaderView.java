package com.dianping.shopinfo.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.dianping.archive.DPObject;
import com.dianping.util.ViewUtils;

public class BigHeaderView extends DefaultShopInfoHeaderView
{
  public BigHeaderView(Context paramContext)
  {
    super(paramContext);
  }

  public BigHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected int getAvailableWith()
  {
    return ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 142.0F);
  }

  protected int setChainInfo(DPObject paramDPObject)
  {
    return 0;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.BigHeaderView
 * JD-Core Version:    0.6.0
 */