package com.dianping.hotel.deal.share;

import android.content.Context;
import com.dianping.archive.DPObject;
import com.dianping.base.share.action.base.SmsShare;
import com.dianping.base.share.model.ShareHolder;

public class HotelProdSmsShare extends SmsShare
{
  public boolean shareHotelProd(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.desc = ("点评团这单不错，你也看看：仅售" + paramDPObject.getDouble("Price") + "元，" + paramDPObject.getString("ContentTitle"));
    localShareHolder.webUrl = ("更多详情：http://dpurl.cn/m/t" + paramDPObject.getInt("ID"));
    return share(paramContext, localShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.share.HotelProdSmsShare
 * JD-Core Version:    0.6.0
 */