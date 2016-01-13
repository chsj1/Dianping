package com.dianping.shopinfo.hospital.agent;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.BaseTechnicianAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;

public class DoctorAgent extends BaseTechnicianAgent
{
  public DoctorAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/medicine/getdoctorlistinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public DPObject getData(Bundle paramBundle)
  {
    return this.techniciansInfo;
  }

  public void onCreate(Bundle paramBundle)
  {
    sendRequest();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hospital.agent.DoctorAgent
 * JD-Core Version:    0.6.0
 */