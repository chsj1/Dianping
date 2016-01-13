package com.dianping.hotel.deal.share;

import android.content.Context;
import com.dianping.archive.DPObject;
import com.dianping.base.share.action.base.CopyShare;
import com.dianping.base.share.model.ShareHolder;

public class HotelProdCopyShare extends CopyShare
{
  public boolean shareHotelProd(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    String str = "点评团这单不错，快来看看：" + paramDPObject.getString("ShortTitle") + paramDPObject.getString("Title");
    localShareHolder.imageUrl = paramDPObject.getString("Photo");
    localShareHolder.desc = str;
    localShareHolder.webUrl = ("http://t.dianping.com/deal/" + paramDPObject.getInt("ID"));
    return share(paramContext, localShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.share.HotelProdCopyShare
 * JD-Core Version:    0.6.0
 */