package com.dianping.hotel.tuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.DealDetailMoreActivity;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;

public class HotelDealDetailMoreActivity extends DealDetailMoreActivity
{
  protected void loadDealInfo()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://m.api.dianping.com/hoteltg/dealextrainfo.hoteltg");
    localStringBuilder.append("?cityid=").append(city().id());
    String str = accountService().token();
    if (!TextUtils.isEmpty(str))
      localStringBuilder.append("&token=").append(str);
    int i = this.dpobjDeal.getInt("ID");
    if (i > 0)
      localStringBuilder.append("&id=").append(i);
    this.dealInfoRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.dealInfoRequest, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getIntent().getBooleanExtra("fromFlipper", false))
    {
      statisticsEvent("tuan5", "hotel_tuan5_detail_pic_slide", "dealgrpid", this.dpobjDeal.getInt("ID"));
      return;
    }
    statisticsEvent("tuan5", "hotel_tuan5_detail_more", "dealgrpid", this.dpobjDeal.getInt("ID"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.tuan.activity.HotelDealDetailMoreActivity
 * JD-Core Version:    0.6.0
 */