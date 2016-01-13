package com.dianping.hotel.deal.share;

import android.content.Context;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.share.action.base.QQShare;
import com.dianping.base.share.model.ShareHolder;

public class HotelProdQQShare extends QQShare
{
  public boolean shareHotelProd(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    if (TextUtils.isEmpty(paramDPObject.getString("RegionName")));
    for (String str1 = paramDPObject.getString("ShortTitle"); ; str1 = "【" + paramDPObject.getString("RegionName") + "】" + paramDPObject.getString("ShortTitle"))
    {
      String str2 = "仅售" + paramDPObject.getDouble("Price") + "元," + paramDPObject.getString("ContentTitle");
      localShareHolder.title = str1;
      localShareHolder.imageUrl = paramDPObject.getString("Photo");
      localShareHolder.desc = str2;
      localShareHolder.webUrl = ("http://m.dianping.com/tuan/weixinshare/" + paramDPObject.getInt("ID"));
      return share(paramContext, localShareHolder);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.share.HotelProdQQShare
 * JD-Core Version:    0.6.0
 */