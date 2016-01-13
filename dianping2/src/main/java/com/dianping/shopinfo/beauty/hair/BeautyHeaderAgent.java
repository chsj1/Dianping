package com.dianping.shopinfo.beauty.hair;

import android.os.Bundle;
import android.view.LayoutInflater;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.beauty.hair.widget.BeautyHairNielShopInfoHeaderView;
import com.dianping.shopinfo.common.TopAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.layout;

public class BeautyHeaderAgent extends TopAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String BEAUTY_SHOP_BASIC_INFO = "beautyShopBasicInfo";
  private DPObject beautyShop;
  private MApiRequest request;
  private String shopView;

  public BeautyHeaderAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    Object localObject = new StringBuilder("http://beauty.api.dianping.com/gethairshop.bin?");
    ((StringBuilder)localObject).append("shopid=").append(shopId());
    localObject = BasicMApiRequest.mapiGet(((StringBuilder)localObject).toString(), CacheType.NORMAL);
    getFragment().mapiService().exec((Request)localObject, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    paramBundle = getShop();
    if (paramBundle == null);
    do
    {
      return;
      removeAllCells();
    }
    while (getFragment() == null);
    if ((this.topView != null) && (!this.topView.getClass().equals(BeautyHairNielShopInfoHeaderView.class)))
      this.topView = null;
    if (this.topView == null)
    {
      this.topView = ((BeautyHairNielShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_beauty_multy_pic_panel, getParentView(), false));
      this.topView.setGAString("beauty_multiphoto");
    }
    ((BeautyHairNielShopInfoHeaderView)this.topView).setBeautyShop(paramBundle, this.beautyShop);
    addCell("0200Basic.05Info", this.topView, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    DPObject localDPObject = getShop();
    if ((localDPObject == null) || (localDPObject.getObject("ClientShopStyle") == null))
      return;
    this.shopView = localDPObject.getObject("ClientShopStyle").getString("ShopView");
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onDestroy()
  {
    if (this.request != null)
    {
      getFragment().mapiService().abort(this.request, this, true);
      this.request = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    dispatchAgentChanged(false);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.beautyShop = ((DPObject)paramMApiResponse.result());
    setSharedObject("beautyShopBasicInfo", this.beautyShop);
    paramMApiRequest = new Bundle();
    paramMApiRequest.putParcelable("beautyShopBasicInfo", this.beautyShop);
    dispatchAgentChanged("beauty_price", paramMApiRequest);
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.beauty.hair.BeautyHeaderAgent
 * JD-Core Version:    0.6.0
 */