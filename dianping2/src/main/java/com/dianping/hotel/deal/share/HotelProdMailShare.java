package com.dianping.hotel.deal.share;

import android.content.Context;
import com.dianping.archive.DPObject;
import com.dianping.base.share.action.base.MailShare;
import com.dianping.base.share.model.ShareHolder;

public class HotelProdMailShare extends MailShare
{
  public boolean shareHotelProd(Context paramContext, DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.title = "点评团这个团购不错，推荐给你";
    localShareHolder.desc = ("Hi,\n\n我觉得你会喜欢这单团购：\n\n" + paramDPObject.getString("ShortTitle") + "\n\n" + paramDPObject.getString("Title") + "\n");
    localShareHolder.webUrl = ("\nhttp://t.dianping.com/deal/" + paramDPObject.getInt("ID"));
    return share(paramContext, localShareHolder);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.share.HotelProdMailShare
 * JD-Core Version:    0.6.0
 */