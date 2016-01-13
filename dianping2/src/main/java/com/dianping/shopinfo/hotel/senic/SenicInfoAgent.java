package com.dianping.shopinfo.hotel.senic;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class SenicInfoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_SPECIAL_ITEM = "7000ShopInfo.";
  public static final String SHOP_EXTRA_INFO = "shopExtraInfoMall";
  private ArrayList<DPObject> mCharacteristicList = new ArrayList();
  private MApiRequest mShopExtraRequest;
  private MApiRequest mShopFeatureRequest;
  private DPObject shopExtra;

  public SenicInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void reqShopExtra()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mshop/shopextra.bin?shopid=" + shopId()).buildUpon();
    if (location() != null)
      localBuilder.appendQueryParameter("lng", String.valueOf(location().longitude())).appendQueryParameter("lat", String.valueOf(location().latitude()));
    this.mShopExtraRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.mShopExtraRequest, this);
  }

  public CommonCell createCommonCell()
  {
    return (CommonCell)MyResources.getResource(CellAgent.class).inflate(getContext(), R.layout.scenic_common_cell, getParentView(), false);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    StringBuilder localStringBuilder;
    do
    {
      return;
      paramBundle = createCommonCell();
      if (paramBundle != null)
        addCell("7000ShopInfo.", paramBundle, 257);
      paramBundle.setGAString("scenic_info");
      paramBundle.setLeftIcon(R.drawable.scenic_briefintro_icon);
      paramBundle.setTitle("景点详情");
      paramBundle.setTitleMaxLines(2);
      localStringBuilder = new StringBuilder();
      if (this.shopExtra == null)
        continue;
      String str = this.shopExtra.getString("Time");
      if (TextUtils.isEmpty(str))
        continue;
      localStringBuilder.append("营业时间：").append(str);
    }
    while ((localStringBuilder == null) || (localStringBuilder.length() <= 0));
    paramBundle.setSubTitle(localStringBuilder.toString());
  }

  public void onCellClick(String paramString, View paramView)
  {
    super.onCellClick(paramString, paramView);
    if ("7000ShopInfo.".equals(paramString))
    {
      paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://scenicdetails"));
      paramString.putExtra("shop", getShop());
      paramString.putExtra("shopid", shopId());
      paramString.putExtra("shopextra", this.shopExtra);
      paramString.putParcelableArrayListExtra("featurelist", this.mCharacteristicList);
      getFragment().startActivity(paramString);
      paramString = new ArrayList();
      paramString.add(new BasicNameValuePair("shopid", shopId() + ""));
      statisticsEvent("shopinfo5", "shopinfo5_info", "", 0, paramString);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
    reqShopExtra();
  }

  public void onDestroy()
  {
    if (this.mShopFeatureRequest != null)
    {
      getFragment().mapiService().abort(this.mShopFeatureRequest, this, true);
      this.mShopFeatureRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mShopFeatureRequest)
      this.mShopFeatureRequest = null;
    if (paramMApiRequest == this.mShopExtraRequest)
      this.mShopExtraRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mShopFeatureRequest == paramMApiRequest)
    {
      this.mShopFeatureRequest = null;
      this.mCharacteristicList.addAll(Arrays.asList((DPObject[])(DPObject[])paramMApiResponse.result()));
      dispatchAgentChanged(false);
    }
    label41: 
    do
    {
      do
      {
        do
        {
          break label41;
          do
            return;
          while (this.mShopExtraRequest != paramMApiRequest);
          this.mShopExtraRequest = null;
        }
        while ((paramMApiResponse == null) || (!(paramMApiResponse.result() instanceof DPObject)));
        this.shopExtra = ((DPObject)paramMApiResponse.result());
        setSharedObject("shopExtraInfoMall", this.shopExtra);
        paramMApiRequest = new Bundle();
        paramMApiRequest.putParcelable("shopExtraInfoMall", this.shopExtra);
        dispatchAgentChanged(false);
      }
      while (isMallType());
      dispatchAgentChanged("shopinfo/mallinfo", paramMApiRequest);
    }
    while (isEducationType());
    dispatchAgentChanged("shopinfo/brandstory", paramMApiRequest);
  }

  public void sendRequest()
  {
    this.mShopFeatureRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/shopfeaturetags.bin?shopid=" + shopId(), CacheType.DISABLED);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (SenicInfoAgent.this.mShopFeatureRequest != null)
          SenicInfoAgent.this.getFragment().mapiService().exec(SenicInfoAgent.this.mShopFeatureRequest, SenicInfoAgent.this);
      }
    }
    , 100L);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.senic.SenicInfoAgent
 * JD-Core Version:    0.6.0
 */