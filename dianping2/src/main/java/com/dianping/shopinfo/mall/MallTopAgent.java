package com.dianping.shopinfo.mall;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.shopinfo.activity.ShopInfoActivity;
import com.dianping.shopinfo.common.TopAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.mall.view.MallSaleHeaderView;
import com.dianping.shopinfo.widget.MallShopInfoHeaderView;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import org.json.JSONObject;

public class MallTopAgent extends TopAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String CELL_TOP = "0200Basic.05Info";
  DPObject error;
  private String gaString;
  protected final View.OnClickListener iconClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      boolean bool = true;
      paramView = MallTopAgent.this.getShop();
      if (paramView == null);
      do
      {
        return;
        if ((paramView.contains("PicCount")) && (paramView.getInt("PicCount") == 0) && (TextUtils.isEmpty(paramView.getString("DefaultPic"))))
        {
          UploadPhotoUtil.uploadShopPhoto(MallTopAgent.this.getContext(), MallTopAgent.this.getShop());
          return;
        }
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
        localIntent.putExtra("objShop", paramView);
        if ((paramView.getInt("Status") == 1) || (paramView.getInt("Status") == 4))
          bool = false;
        localIntent.putExtra("enableUpload", bool);
        MallTopAgent.this.getFragment().startActivity(localIntent);
      }
      while (!"viewphotocus".equals(MallTopAgent.this.gaString));
      GAHelper.instance().contextStatisticsEvent(MallTopAgent.this.getContext(), MallTopAgent.this.gaString, MallTopAgent.this.getGAExtra(), "tap");
    }
  };
  private MApiRequest mImageReq;
  private int mallBizType = -1;
  private DPObject picInfo;
  protected ShopInfoHeaderView topView;

  public MallTopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private int getMallBizType()
  {
    if (!TextUtils.isEmpty(getFragment().getStringParam("mallbiztype")))
      return getFragment().getIntParam("mallbiztype");
    return -1;
  }

  private int getMallBizType(DPObject paramDPObject)
  {
    int j = -1;
    paramDPObject = paramDPObject.getString("ExtraJson");
    int i = j;
    if (!TextUtils.isEmpty(paramDPObject));
    try
    {
      paramDPObject = new JSONObject(paramDPObject);
      i = j;
      if (paramDPObject.opt("MallBizType") != null)
        i = paramDPObject.optInt("MallBizType");
      return i;
    }
    catch (Exception paramDPObject)
    {
      paramDPObject.printStackTrace();
    }
    return -1;
  }

  private void initHeaderView()
  {
    if (this.topView == null)
      if (this.picInfo == null)
        if (this.mallBizType == 1)
        {
          this.topView = ((MallSaleHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_mall_sale_header_layout, getParentView(), false));
          this.gaString = "viewphotocus";
          GAHelper.instance().contextStatisticsEvent(getContext(), this.gaString, getGAExtra(), "view");
        }
    do
    {
      return;
      if (this.mallBizType == 0)
      {
        this.topView = ((MallShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_panel_mal, getParentView(), false));
        this.gaString = "viewphoto";
        break;
      }
      addCell("0200Basic.05Info", createLoadingCell(), 16);
      return;
      if (this.picInfo.getBoolean("Coop"))
      {
        this.topView = ((MallSaleHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_mall_sale_header_layout, getParentView(), false));
        ((MallSaleHeaderView)this.topView).setPicInfo(this.picInfo);
        this.gaString = "viewphotocus";
        break;
      }
      this.topView = ((MallShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_panel_mal, getParentView(), false));
      this.gaString = "viewphoto";
      break;
    }
    while ((!this.topView.getClass().equals(MallSaleHeaderView.class)) || (this.picInfo == null));
    ((MallSaleHeaderView)this.topView).setPicInfo(this.picInfo);
  }

  private void resetHeaderView()
  {
    if ((this.topView != null) && (!this.topView.getClass().equals(MallShopInfoHeaderView.class)) && (!this.topView.getClass().equals(MallSaleHeaderView.class)))
      this.topView = null;
  }

  private void sendImageReq()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getheaderpic.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(shopId()));
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    this.mImageReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    if (this.mImageReq != null)
      getFragment().mapiService().exec(this.mImageReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    paramBundle = getShop();
    removeAllCells();
    if (paramBundle == null);
    do
    {
      do
        return;
      while (getFragment() == null);
      if (this.mallBizType == -1)
        this.mallBizType = getMallBizType(paramBundle);
      if (this.mallBizType == -1)
        this.mallBizType = getMallBizType();
      resetHeaderView();
      initHeaderView();
    }
    while (this.topView == null);
    if (getShopStatus() == 0)
      this.topView.setShop(paramBundle, 0);
    while (true)
    {
      addCell("0200Basic.05Info", this.topView, 0);
      this.topView.setIconClickListen(this.iconClickListener);
      if (!(getContext() instanceof ShopInfoActivity))
        break;
      ((ShopInfoActivity)getContext()).getSpeedMonitorHelper().setResponseTime(1, System.currentTimeMillis());
      return;
      this.topView.setShop(paramBundle);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.picInfo = ((DPObject)paramBundle.getParcelable("picInfo"));
      this.error = ((DPObject)paramBundle.getParcelable("error"));
    }
    if ((this.picInfo == null) && (this.error == null))
      sendImageReq();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mImageReq = null;
    if ((paramMApiResponse.error() instanceof DPObject));
    for (this.error = ((DPObject)paramMApiResponse.error()); ; this.error = new DPObject())
    {
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mImageReq = null;
    this.picInfo = ((DPObject)paramMApiResponse.result());
    this.error = null;
    dispatchAgentChanged(false);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("picInfo", this.picInfo);
    localBundle.putParcelable("error", this.error);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.mall.MallTopAgent
 * JD-Core Version:    0.6.0
 */