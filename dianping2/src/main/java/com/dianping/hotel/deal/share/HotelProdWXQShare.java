package com.dianping.hotel.deal.share;

import android.content.Context;
import com.dianping.archive.DPObject;
import com.dianping.base.share.action.base.WXQShare;
import com.dianping.base.share.model.ShareHolder;

public class HotelProdWXQShare extends WXQShare
{
  public boolean shareHotelProd(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    String str1 = "仅售" + paramDPObject.getDouble("Price") + "元," + paramDPObject.getString("ContentTitle");
    String str2 = "仅售" + paramDPObject.getDouble("Price") + "元," + paramDPObject.getString("ContentTitle");
    localShareHolder.title = str1;
    localShareHolder.imageUrl = paramDPObject.getString("Photo");
    localShareHolder.desc = str2;
    localShareHolder.webUrl = ("http://mm.dianping.com/weixin/deal/detail?showwxpaytitle=1&utm_source=appshare&id=" + paramDPObject.getInt("ID"));
    return share(paramContext, localShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.share.HotelProdWXQShare
 * JD-Core Version:    0.6.0
 */