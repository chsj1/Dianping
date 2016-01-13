package com.dianping.shopinfo.district;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class HotMarketAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final String CELL_HOTMARKET_REVIEW = "0400District.01HotMarket";
  private MApiRequest hotMarketReq = null;
  private String itemUrl = null;
  private List<DPObject> mHotMarketLists = null;
  private DPObject mHotMarketObject = null;
  private NovaLinearLayout mLayout1;
  private NovaLinearLayout mLayout2;
  private NovaLinearLayout mLayout3;

  public HotMarketAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createHotMarkets()
  {
    ViewGroup localViewGroup = (ViewGroup)this.res.inflate(getContext(), R.layout.shopinfo_hotmarket_cell_layout, getParentView(), false);
    CommonCell localCommonCell = (CommonCell)localViewGroup.findViewById(R.id.hotmarket_title);
    localCommonCell.setGAString("shoppingmall_more");
    ((NovaActivity)getContext()).addGAView(localCommonCell, -1);
    localCommonCell.setTitle(this.mHotMarketObject.getString("Title"));
    localCommonCell.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = HotMarketAgent.this.mHotMarketObject.getString("Url");
        if (!TextUtils.isEmpty(paramView))
          HotMarketAgent.this.startActivity(paramView);
      }
    });
    ((TextView)localViewGroup.findViewById(R.id.hot_market_name_1)).setText(((DPObject)this.mHotMarketLists.get(0)).getString("Title"));
    ((TextView)localViewGroup.findViewById(R.id.hot_market_distance_1)).setText(((DPObject)this.mHotMarketLists.get(0)).getString("Distance"));
    ((NetworkImageView)localViewGroup.findViewById(R.id.hot_market_img_1)).setImage(((DPObject)this.mHotMarketLists.get(0)).getString("Pic"));
    this.mLayout1 = ((NovaLinearLayout)localViewGroup.findViewById(R.id.hotmarket_layout_1));
    this.mLayout1.setOnClickListener(this);
    this.mLayout1.gaUserInfo.shop_id = Integer.valueOf(getShopId(((DPObject)this.mHotMarketLists.get(0)).getString("Shopid")));
    this.mLayout1.gaUserInfo.index = Integer.valueOf(0);
    this.mLayout1.setGAString("shoppingmall");
    ((NovaActivity)getContext()).addGAView(this.mLayout1, 0);
    if (this.mHotMarketLists.size() > 1)
    {
      ((TextView)localViewGroup.findViewById(R.id.hot_market_name_2)).setText(((DPObject)this.mHotMarketLists.get(1)).getString("Title"));
      ((TextView)localViewGroup.findViewById(R.id.hot_market_distance_2)).setText(((DPObject)this.mHotMarketLists.get(1)).getString("Distance"));
      ((NetworkImageView)localViewGroup.findViewById(R.id.hot_market_img_2)).setImage(((DPObject)this.mHotMarketLists.get(1)).getString("Pic"));
      this.mLayout2 = ((NovaLinearLayout)localViewGroup.findViewById(R.id.hotmarket_layout_2));
      this.mLayout2.setVisibility(0);
      this.mLayout2.setOnClickListener(this);
      this.mLayout2.gaUserInfo.shop_id = Integer.valueOf(getShopId(((DPObject)this.mHotMarketLists.get(1)).getString("Shopid")));
      this.mLayout2.gaUserInfo.index = Integer.valueOf(1);
      this.mLayout2.setGAString("shoppingmall");
      ((NovaActivity)getContext()).addGAView(this.mLayout2, 1);
    }
    if (this.mHotMarketLists.size() > 2)
    {
      ((TextView)localViewGroup.findViewById(R.id.hot_market_name_3)).setText(((DPObject)this.mHotMarketLists.get(2)).getString("Title"));
      ((TextView)localViewGroup.findViewById(R.id.hot_market_distance_3)).setText(((DPObject)this.mHotMarketLists.get(2)).getString("Distance"));
      ((NetworkImageView)localViewGroup.findViewById(R.id.hot_market_img_3)).setImage(((DPObject)this.mHotMarketLists.get(2)).getString("Pic"));
      this.mLayout3 = ((NovaLinearLayout)localViewGroup.findViewById(R.id.hotmarket_layout_3));
      this.mLayout3.setVisibility(0);
      this.mLayout3.setOnClickListener(this);
      this.mLayout3.gaUserInfo.shop_id = Integer.valueOf(getShopId(((DPObject)this.mHotMarketLists.get(2)).getString("Shopid")));
      this.mLayout3.gaUserInfo.index = Integer.valueOf(2);
      this.mLayout3.setGAString("shoppingmall");
      ((NovaActivity)getContext()).addGAView(this.mLayout3, 2);
    }
    return localViewGroup;
  }

  private int getShopId(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return 0;
    try
    {
      int i = Integer.parseInt(paramString);
      return i;
    }
    catch (java.lang.NumberFormatException paramString)
    {
    }
    return 0;
  }

  private void reqHotMarket()
  {
    if (this.hotMarketReq != null)
      getFragment().mapiService().abort(this.hotMarketReq, this, true);
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/mshop/hotmarket.bin");
    localStringBuffer.append("?shopid=").append(getShop().getInt("ID"));
    DPObject localDPObject = getFragment().locationService().location();
    if (localDPObject != null)
    {
      localStringBuffer.append("&lat=").append(Location.FMT.format(localDPObject.getDouble("Lat")));
      localStringBuffer.append("&lng=").append(Location.FMT.format(localDPObject.getDouble("Lng")));
    }
    localStringBuffer.append("&cityid=").append(cityId());
    this.hotMarketReq = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.hotMarketReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.mHotMarketObject == null)
      removeAllCells();
    do
    {
      return;
      this.mHotMarketLists = Arrays.asList(this.mHotMarketObject.getArray("HotMarketInfos"));
    }
    while ((this.mHotMarketLists == null) || (this.mHotMarketLists.size() <= 0));
    addCell("0400District.01HotMarket", createHotMarkets());
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.hotmarket_layout_1)
    {
      this.itemUrl = ((DPObject)this.mHotMarketLists.get(0)).getString("Scheme");
      if (!TextUtils.isEmpty(this.itemUrl))
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.itemUrl)));
    }
    do
    {
      do
        while (true)
        {
          return;
          if (i != R.id.hotmarket_layout_2)
            break;
          this.itemUrl = ((DPObject)this.mHotMarketLists.get(1)).getString("Scheme");
          if (TextUtils.isEmpty(this.itemUrl))
            continue;
          startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.itemUrl)));
          return;
        }
      while (i != R.id.hotmarket_layout_3);
      this.itemUrl = ((DPObject)this.mHotMarketLists.get(2)).getString("Scheme");
    }
    while (TextUtils.isEmpty(this.itemUrl));
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.itemUrl)));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ((getFragment() == null) || (getShop() == null))
      return;
    reqHotMarket();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.hotMarketReq != null)
    {
      getFragment().mapiService().abort(this.hotMarketReq, this, true);
      this.hotMarketReq = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.hotMarketReq == paramMApiRequest)
    {
      this.hotMarketReq = null;
      dispatchAgentChanged(false);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.hotMarketReq == paramMApiRequest))
    {
      this.hotMarketReq = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mHotMarketObject = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.district.HotMarketAgent
 * JD-Core Version:    0.6.0
 */