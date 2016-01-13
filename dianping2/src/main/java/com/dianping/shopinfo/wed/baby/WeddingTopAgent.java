package com.dianping.shopinfo.wed.baby;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.wed.baby.widget.WeddingShopHeaderView;
import com.dianping.shopinfo.widget.DefaultShopInfoHeaderView;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.net.URLEncoder;

public class WeddingTopAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String CELL_TOP = "0100Basic.05Info";
  protected static final String CELL_TOP_SMALL = "0200Basic.00Info";
  public static final String WEDDING_SHOP_BASIC_INFO = "weddingShopBasicInfo";
  protected final View.OnClickListener iconClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      boolean bool = true;
      paramView = WeddingTopAgent.this.getShop();
      if (paramView == null)
        return;
      if ((paramView.contains("PicCount")) && (paramView.getInt("PicCount") == 0) && (TextUtils.isEmpty(paramView.getString("DefaultPic"))))
        UploadPhotoUtil.uploadShopPhoto(WeddingTopAgent.this.getContext(), WeddingTopAgent.this.getShop());
      while (true)
      {
        GAHelper.instance().contextStatisticsEvent(WeddingTopAgent.this.getContext(), "viewphoto", WeddingTopAgent.this.getGAExtra(), "tap");
        return;
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
        localIntent.putExtra("objShop", paramView);
        if ((paramView.getInt("Status") == 1) || (paramView.getInt("Status") == 4))
          bool = false;
        localIntent.putExtra("enableUpload", bool);
        WeddingTopAgent.this.getFragment().startActivity(localIntent);
      }
    }
  };
  private MApiRequest request;
  private DPObject shopInfo;
  DPObject shopInfoObject;
  protected ShopInfoHeaderView topView;
  protected DefaultShopInfoHeaderView topViewSmall;

  public WeddingTopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/shopinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", "" + shopId());
    this.request = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  boolean isHeadMiniPic()
  {
    if (this.shopInfoObject == null);
    do
      return false;
    while (this.shopInfoObject.getInt("UiFlag") != 1);
    return true;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    DPObject localDPObject = getShop();
    removeAllCells();
    if (localDPObject == null);
    do
    {
      return;
      if ((paramBundle == null) || (!paramBundle.containsKey("shop")))
        continue;
      this.shopInfoObject = ((DPObject)paramBundle.getParcelable("shop"));
    }
    while (getFragment() == null);
    if ((this.topView != null) && (!this.topView.getClass().equals(WeddingShopHeaderView.class)))
      this.topView = null;
    if (this.topView == null)
    {
      if (isHomeDesignShopType())
      {
        this.topView = ((WeddingShopHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_homedesign_head, getParentView(), false));
        this.topView.setGAString("shopinfoh_toppic", "1");
        this.topView.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = WeddingTopAgent.this.getGAExtra();
            paramView.shop_id = Integer.valueOf(WeddingTopAgent.this.shopId());
            GAHelper.instance().contextStatisticsEvent(WeddingTopAgent.this.getContext(), "viewphoto", paramView, "tap");
            paramView = new StringBuffer("http://m.dianping.com/wed/mobile/homeshopbrief/");
            paramView.append("shopId=").append(WeddingTopAgent.this.shopId()).append("?dpshare=0");
            try
            {
              paramView = URLEncoder.encode(paramView.toString(), "UTF-8");
              Intent localIntent = new Intent("android.intent.action.VIEW");
              localIntent.setData(Uri.parse("dianping://weddinghotelweb?url=" + paramView));
              WeddingTopAgent.this.startActivity(localIntent);
              return;
            }
            catch (java.io.UnsupportedEncodingException paramView)
            {
            }
          }
        });
      }
    }
    else
    {
      label144: if (!isBanquetType())
        break label298;
      if (this.shopInfo != null)
        ((WeddingShopHeaderView)this.topView).setBanquetContent(this.shopInfo);
      label172: if (getShopStatus() != 0)
        break label329;
      this.topView.setShop(localDPObject, 0);
    }
    while (true)
    {
      addCell("0100Basic.05Info", this.topView, 0);
      if (!isHeadMiniPic())
        break;
      removeAllCells();
      this.topViewSmall = ((DefaultShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_common_header_layout, getParentView(), false));
      this.topViewSmall.setIconClickListener(this.iconClickListener);
      this.topViewSmall.setShop(localDPObject, getShopStatus());
      addCell("0200Basic.00Info", this.topViewSmall, 0);
      return;
      this.topView = ((WeddingShopHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_wedding_head, getParentView(), false));
      break label144;
      label298: if (getSharedObject("WeddingShopInfo") == null)
        break label172;
      ((WeddingShopHeaderView)this.topView).setHeaderInfo((DPObject)getSharedObject("WeddingShopInfo"));
      break label172;
      label329: this.topView.setShop(localDPObject);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (isBanquetType())
      sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiResponse == null)
      return;
    this.shopInfo = ((DPObject)paramMApiResponse.result());
    dispatchAgentChanged(false);
    setSharedObject("weddingShopBasicInfo", this.shopInfo);
    dispatchAgentChanged("shopinfo/banquet_bottom_toolbar", null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.baby.WeddingTopAgent
 * JD-Core Version:    0.6.0
 */