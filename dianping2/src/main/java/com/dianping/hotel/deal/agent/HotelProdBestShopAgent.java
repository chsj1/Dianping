package com.dianping.hotel.deal.agent;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.tuan.agent.TuanGroupCellAgent;
import com.dianping.base.tuan.agent.TuanGroupCellAgent.OnCellRefreshListener;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.base.widget.HotelNearestShopInfoLayout;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.hotel.deal.fragment.HotelProdInfoAgentFragment;
import com.dianping.hotel.deal.widget.HotelProdBestShopWidget;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HotelProdBestShopAgent extends TuanGroupCellAgent
  implements TuanGroupCellAgent.OnCellRefreshListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int STYLE_PLAIN = 1;
  private static final int STYLE_RICH = 2;
  private static final int TYPE_FOOD = 3;
  private static final int TYPE_HOTEL = 1;
  private static final int TYPE_OTHER = 4;
  private static final int TYPE_SCENIC = 2;
  private boolean bestShopLoaded;
  private DPObject dpHotelBestShop;
  private HotelProdInfoAgentFragment mProdFragment;
  private MApiRequest prodBestShopRequest;
  private int status;

  public HotelProdBestShopAgent(Object paramObject)
  {
    super(paramObject);
    this.mProdFragment = ((HotelProdInfoAgentFragment)paramObject);
  }

  private View generateShopGroupLayout(DPObject[] paramArrayOfDPObject, int paramInt1, int paramInt2)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      paramArrayOfDPObject = null;
    LinearLayout localLinearLayout1;
    Object localObject1;
    label148: label151: label461: 
    do
    {
      return paramArrayOfDPObject;
      localLinearLayout1 = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.hotel_prod_best_shop_group, null);
      localObject1 = (FrameLayout)localLinearLayout1.findViewById(R.id.left_category_layout);
      Object localObject2 = (ImageView)localLinearLayout1.findViewById(R.id.icon);
      String str = "更多";
      LinearLayout localLinearLayout3;
      LinearLayout localLinearLayout4;
      int i;
      int j;
      Object localObject3;
      switch (paramInt1)
      {
      default:
        LinearLayout localLinearLayout2 = (LinearLayout)localLinearLayout1.findViewById(R.id.top_shops);
        localLinearLayout3 = (LinearLayout)localLinearLayout1.findViewById(R.id.ext_shops);
        localLinearLayout4 = (LinearLayout)localLinearLayout1.findViewById(R.id.display_more);
        localObject1 = null;
        if ((paramInt1 != 1) || (paramArrayOfDPObject.length <= 1))
          break;
        i = 1;
        j = 0;
        if (j >= paramArrayOfDPObject.length)
          break label461;
        localObject3 = paramArrayOfDPObject[j];
        localObject2 = (HotelProdBestShopWidget)LayoutInflater.from(getContext()).inflate(R.layout.hotel_prod_best_shop_item, null);
        localObject1 = null;
        if (i != 0)
          localObject1 = String.format("酒店%d", new Object[] { Integer.valueOf(j + 1) });
        ((HotelProdBestShopWidget)localObject2).setShop((DPObject)localObject3, paramInt1, (String)localObject1);
        ((HotelProdBestShopWidget)localObject2).setOnClickListener(new View.OnClickListener((DPObject)localObject3)
        {
          public void onClick(View paramView)
          {
            HotelProdBestShopAgent.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + this.val$shop.getInt("ID"))));
          }
        });
        if (j < paramInt2)
        {
          localLinearLayout2.addView((View)localObject2);
          localLinearLayout2.setVisibility(0);
        }
      case 1:
      case 2:
      case 3:
      case 4:
      }
      for (localObject1 = localObject2; ; localObject1 = null)
      {
        j += 1;
        break label151;
        ((FrameLayout)localObject1).setBackgroundColor(Color.parseColor("#f0f6ff"));
        ((ImageView)localObject2).setBackgroundResource(R.drawable.hotel_prod_best_shop_hotel_tag);
        str = "更多酒店";
        break;
        ((FrameLayout)localObject1).setBackgroundColor(Color.parseColor("#ebfff8"));
        ((ImageView)localObject2).setBackgroundResource(R.drawable.hotel_prod_best_shop_senice_tag);
        str = "更多景点";
        break;
        ((FrameLayout)localObject1).setBackgroundColor(Color.parseColor("#fff4eb"));
        ((ImageView)localObject2).setBackgroundResource(R.drawable.hotel_prod_best_shop_food_tag);
        str = "更多餐馆";
        break;
        ((FrameLayout)localObject1).setBackgroundColor(Color.parseColor("#feffeb"));
        ((ImageView)localObject2).setBackgroundResource(R.drawable.hotel_prod_best_shop_other_tag);
        break;
        i = 0;
        break label148;
        if (j == paramInt2)
        {
          localObject1 = (NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.hotel_prod_best_shop_desplay_more, null);
          localObject3 = (TextView)((NovaLinearLayout)localObject1).findViewById(R.id.display_more);
          localLinearLayout4.addView((View)localObject1);
          localLinearLayout4.setVisibility(0);
          ((NovaLinearLayout)localObject1).setOnClickListener(new View.OnClickListener(localLinearLayout3, (TextView)localObject3, str)
          {
            Drawable drawable;

            public void onClick(View paramView)
            {
              if (this.val$extShopGroupLayout.getVisibility() == 0)
              {
                this.val$extShopGroupLayout.setVisibility(8);
                this.val$displayView.setText(this.val$finalMoreText);
                this.drawable = HotelProdBestShopAgent.this.getResources().getDrawable(R.drawable.ic_arrow_down_black);
                this.drawable.setBounds(0, 0, this.drawable.getMinimumWidth(), this.drawable.getMinimumHeight());
                this.val$displayView.setCompoundDrawables(null, null, this.drawable, null);
                return;
              }
              this.val$extShopGroupLayout.setVisibility(0);
              this.val$displayView.setText("收起");
              this.drawable = HotelProdBestShopAgent.this.getResources().getDrawable(R.drawable.ic_arrow_up_black);
              this.drawable.setBounds(0, 0, this.drawable.getMinimumWidth(), this.drawable.getMinimumHeight());
              this.val$displayView.setCompoundDrawables(null, null, this.drawable, null);
            }
          });
        }
        localLinearLayout3.addView((View)localObject2);
        localLinearLayout3.setVisibility(8);
      }
      paramArrayOfDPObject = localLinearLayout1;
    }
    while (localObject1 == null);
    ((HotelProdBestShopWidget)localObject1).findViewById(R.id.bottom_sep).setVisibility(8);
    return (View)(View)(View)localLinearLayout1;
  }

  private void sendProdBestShopRequest()
  {
    if ((this.prodBestShopRequest != null) || (this.bestShopLoaded))
      return;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://m.api.dianping.com/hotelm/hotelbestshop.hotel");
    localStringBuilder.append("?cityid=" + this.mProdFragment.cityId());
    localStringBuilder.append("&biztype=" + this.mProdFragment.bizType);
    localStringBuilder.append("&productid=" + this.mProdFragment.productId);
    localStringBuilder.append("&lng=" + this.mProdFragment.location().longitude());
    localStringBuilder.append("&lat=" + this.mProdFragment.location().latitude());
    this.prodBestShopRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.prodBestShopRequest, this);
  }

  private void updateView()
  {
    removeAllCells();
    if (this.dpHotelBestShop == null);
    LinearLayout localLinearLayout;
    int k;
    int m;
    int i;
    Object localObject2;
    do
    {
      return;
      while (true)
      {
        localLinearLayout = new LinearLayout(getContext());
        localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        localLinearLayout.setOrientation(1);
        k = 0;
        j = 0;
        m = this.dpHotelBestShop.getInt("Style");
        if (m != 1)
          break;
        localObject1 = this.dpHotelBestShop.getObject("BestShop");
        i = j;
        if (localObject1 != null)
        {
          localObject2 = (HotelNearestShopInfoLayout)this.res.inflate(getContext(), R.layout.hotel_applicabledeal_item, getParentView(), false);
          double d1 = 0.0D;
          double d2 = 0.0D;
          if (location() != null)
          {
            d1 = location().latitude();
            d2 = location().longitude();
          }
          i = j;
          if (((HotelNearestShopInfoLayout)localObject2).setDealShop(new DPObject().edit().putInt("ShopCount", 1).putObject("Shop", (DPObject)localObject1).generate(), d1, d2, true, 0, 1))
          {
            localLinearLayout.addView((View)localObject2);
            i = 1;
          }
        }
        if (i == 0)
          continue;
        localObject1 = new DealInfoCommonCell(getContext());
        ((DealInfoCommonCell)localObject1).setTitle("适用商户");
        ((DealInfoCommonCell)localObject1).hideArrow();
        ((DealInfoCommonCell)localObject1).addContent(localLinearLayout, false);
        addCell("", (View)localObject1);
        return;
      }
      i = j;
    }
    while (m != 2);
    Object localObject1 = new LinkedHashMap();
    ((Map)localObject1).put(Integer.valueOf(1), "HotelList");
    ((Map)localObject1).put(Integer.valueOf(2), "ScenicList");
    ((Map)localObject1).put(Integer.valueOf(3), "RestaurantList");
    ((Map)localObject1).put(Integer.valueOf(4), "OtherShopList");
    localObject1 = ((Map)localObject1).entrySet().iterator();
    int j = k;
    while (true)
    {
      i = j;
      if (!((Iterator)localObject1).hasNext())
        break;
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      localObject2 = generateShopGroupLayout(this.dpHotelBestShop.getArray((String)((Map.Entry)localObject2).getValue()), ((Integer)((Map.Entry)localObject2).getKey()).intValue(), this.dpHotelBestShop.getInt("HotelShowNum"));
      if (localObject2 == null)
        continue;
      localLinearLayout.addView((View)localObject2);
      j = 1;
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null)
      return;
    if (paramBundle != null);
    for (this.status = paramBundle.getInt("status"); ; this.status = 0)
    {
      if ((this.status == 1) && (this.prodBestShopRequest == null))
        sendProdBestShopRequest();
      updateView();
      return;
    }
  }

  public void onDestroy()
  {
    if (this.prodBestShopRequest != null)
      mapiService().abort(this.prodBestShopRequest, this, true);
    super.onDestroy();
  }

  public void onRefresh()
  {
    if (this.prodBestShopRequest != null)
    {
      mapiService().abort(this.prodBestShopRequest, this, true);
      this.prodBestShopRequest = null;
    }
    this.bestShopLoaded = false;
    sendProdBestShopRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.prodBestShopRequest)
    {
      this.prodBestShopRequest = null;
      this.bestShopLoaded = false;
      this.dpHotelBestShop = null;
      dispatchAgentChanged(false);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.prodBestShopRequest)
    {
      this.prodBestShopRequest = null;
      this.bestShopLoaded = true;
      this.dpHotelBestShop = ((DPObject)paramMApiResponse.result());
      dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdBestShopAgent
 * JD-Core Version:    0.6.0
 */