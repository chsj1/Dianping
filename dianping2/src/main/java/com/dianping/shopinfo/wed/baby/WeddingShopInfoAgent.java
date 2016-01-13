package com.dianping.shopinfo.wed.baby;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;

public class WeddingShopInfoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static final String API_ROOT = "http://m.api.dianping.com/";
  private static final String API_URL = "http://m.api.dianping.com/wedding/shopinfo.bin?";
  private static final String CELL_PHOTO_LINK = "3500Photolink.50CommonLink";
  private static final int REQUEST_AVAILABLE = 1;
  private static final int REQUEST_PENDING = 2;
  private static final String TAG = WeddingShopInfoAgent.class.getSimpleName();
  public static final String WEDDING_SHOP_INFO_KEY = "WeddingShopInfo";
  private DPObject error;
  private MApiRequest request;
  private int requestStatus;
  int shopId;
  private DPObject shopInfo;

  public WeddingShopInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initPhotoLinkCell()
  {
    View localView = this.res.inflate(getContext(), R.layout.shop_wedding_cell, getParentView(), false);
    ((TextView)localView.findViewById(R.id.cell_text)).setText("查看会员相册");
    localView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
        if (WeddingShopInfoAgent.this.getShop() != null)
          paramView.putExtra("objShop", WeddingShopInfoAgent.this.getShop());
        while (true)
        {
          WeddingShopInfoAgent.this.getFragment().startActivity(paramView);
          GAHelper.instance().contextStatisticsEvent(WeddingShopInfoAgent.this.getContext(), "memalbum", WeddingShopInfoAgent.this.getGAExtra(), "tap");
          return;
          if (WeddingShopInfoAgent.this.shopId == 0)
            break;
          paramView.putExtra("shopId", WeddingShopInfoAgent.this.shopId);
        }
        Toast.makeText(WeddingShopInfoAgent.this.getContext(), "无法取得商户信息，请稍后再试。", 0).show();
      }
    });
    addCell("3500Photolink.50CommonLink", localView, 0);
  }

  private void sendRequest()
  {
    if (this.requestStatus == 2)
      return;
    this.requestStatus = 2;
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/shopinfo.bin?");
    localStringBuffer.append("shopid=").append(this.shopId);
    this.request = mapiGet(this, localStringBuffer.toString(), CacheType.DISABLED);
    mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.shopInfo != null) || (this.error != null));
    do
    {
      do
      {
        return;
        paramBundle = getShop();
      }
      while (paramBundle == null);
      this.shopId = paramBundle.getInt("ID");
    }
    while (this.shopId <= 0);
    sendRequest();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (isHomeMarketShopType());
    do
    {
      return;
      paramBundle = getShop();
      if (paramBundle == null)
      {
        Log.e(TAG, "Null shop data. Can not update shop info.");
        return;
      }
      this.shopId = paramBundle.getInt("ID");
      if (this.shopId <= 0)
      {
        Log.e(TAG, "Invalid shop id. Can not update shop info.");
        return;
      }
      sendRequest();
    }
    while (isHomeDesignShopType());
    initPhotoLinkCell();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request == paramMApiRequest)
    {
      this.request = null;
      if (!(paramMApiResponse.error() instanceof DPObject))
        break label49;
    }
    label49: for (this.error = ((DPObject)paramMApiResponse.error()); ; this.error = new DPObject())
    {
      this.requestStatus = 1;
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != this.request) || (this.request != paramMApiRequest));
    do
    {
      return;
      this.request = null;
      this.shopInfo = ((DPObject)paramMApiResponse.result());
      this.requestStatus = 1;
    }
    while (this.shopInfo == null);
    setSharedObject("WeddingShopInfo", this.shopInfo);
    this.error = null;
    paramMApiRequest = new Bundle();
    paramMApiRequest.putParcelable("shop", this.shopInfo);
    dispatchAgentChanged(false);
    dispatchAgentChanged("shopinfo/wed_top", paramMApiRequest);
    dispatchAgentChanged("shopinfo/wed_promo", paramMApiRequest);
    dispatchAgentChanged("shopinfo/wed_toolbar", paramMApiRequest);
    if (!isWeddingType())
    {
      dispatchAgentChanged("shopinfo/wed_cpc", paramMApiRequest);
      dispatchAgentChanged("shopinfo/wed_unco_cpc", paramMApiRequest);
    }
    dispatchAgentChanged("shopinfo/common_review", paramMApiRequest);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingShopInfoAgent
 * JD-Core Version:    0.6.0
 */