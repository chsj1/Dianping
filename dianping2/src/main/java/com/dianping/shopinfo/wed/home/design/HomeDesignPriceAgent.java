package com.dianping.shopinfo.wed.home.design;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.net.URLEncoder;

public class HomeDesignPriceAgent extends ShopCellAgent
{
  private static final String CELL_HOMEDESIGN_PRICE = "0300HomeDesign.01Price";
  boolean isSend = false;
  DPObject[] priceList;
  MApiRequest request;
  private RequestHandler<MApiRequest, MApiResponse> requestHandler;
  DPObject shop;

  public HomeDesignPriceAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!isHomeDesignShopType());
    do
      return;
    while ((this.priceList == null) || (this.priceList.length <= 0));
    paramBundle = this.res.inflate(getContext(), R.layout.home_design_common_layout, getParentView(), false);
    Object localObject1 = (TextView)paramBundle.findViewById(R.id.title);
    Object localObject2 = paramBundle.findViewById(R.id.indicator);
    LinearLayout localLinearLayout = (LinearLayout)paramBundle.findViewById(R.id.item_list);
    ((TextView)localObject1).setText("价格/工期");
    ((View)localObject2).setVisibility(8);
    int i = 0;
    while (i < this.priceList.length)
    {
      localObject1 = this.res.inflate(getContext(), R.layout.home_design_price_item, getParentView(), false);
      localObject2 = (TextView)((View)localObject1).findViewById(R.id.mode);
      TextView localTextView1 = (TextView)((View)localObject1).findViewById(R.id.price);
      TextView localTextView2 = (TextView)((View)localObject1).findViewById(R.id.duration);
      String str1 = this.priceList[i].getString("Mode");
      String str2 = this.priceList[i].getString("Price");
      String str3 = this.priceList[i].getString("Duration");
      String str4 = this.priceList[i].getString("Url");
      int j = this.priceList[i].getInt("IntMode");
      ((TextView)localObject2).setText(str1);
      localTextView1.setText(str2);
      localTextView2.setText(str3);
      if (i == 2)
        ((View)localObject1).findViewById(R.id.bottom_divder_line).setVisibility(8);
      ((View)localObject1).setOnClickListener(new ClickListener(str4, j));
      localLinearLayout.addView((View)localObject1);
      i += 1;
    }
    addCell("0300HomeDesign.01Price", paramBundle);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!isHomeDesignShopType());
    do
    {
      return;
      this.shop = getShop();
    }
    while ((this.shop == null) || (shopId() <= 0) || (this.isSend));
    paramBundle = new StringBuffer("http://m.api.dianping.com/wedding/homedesignbid.bin?");
    paramBundle.append("shopid=").append(shopId());
    this.request = BasicMApiRequest.mapiGet(paramBundle.toString(), CacheType.DISABLED);
    this.requestHandler = new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        HomeDesignPriceAgent.this.request = null;
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (HomeDesignPriceAgent.this.request == paramMApiRequest)
        {
          HomeDesignPriceAgent.this.isSend = true;
          HomeDesignPriceAgent.this.request = null;
          if ((paramMApiResponse != null) && (paramMApiResponse.result() != null) && ((paramMApiResponse.result() instanceof DPObject[])))
          {
            HomeDesignPriceAgent.this.priceList = ((DPObject[])(DPObject[])paramMApiResponse.result());
            if ((HomeDesignPriceAgent.this.priceList != null) && (HomeDesignPriceAgent.this.priceList.length > 0))
              HomeDesignPriceAgent.this.dispatchAgentChanged(false);
          }
        }
      }
    };
    getFragment().mapiService().exec(this.request, this.requestHandler);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this.request != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.request, this.requestHandler, true);
  }

  private class ClickListener
    implements View.OnClickListener
  {
    private int mode;
    private String url;

    public ClickListener(String paramInt, int arg3)
    {
      this.url = paramInt;
      int i;
      this.mode = i;
    }

    public void onClick(View paramView)
    {
      if (TextUtils.isEmpty(this.url))
        return;
      try
      {
        paramView = URLEncoder.encode(this.url, "UTF-8");
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse("dianping://weddinghotelweb?url=" + paramView));
        HomeDesignPriceAgent.this.startActivity(localIntent);
        paramView = HomeDesignPriceAgent.this.getGAExtra();
        paramView.shop_id = Integer.valueOf(HomeDesignPriceAgent.this.shop.getInt("ID"));
        paramView.index = Integer.valueOf(this.mode);
        GAHelper.instance().contextStatisticsEvent(HomeDesignPriceAgent.this.getContext(), "price_detail", paramView, "tap");
        return;
      }
      catch (java.io.UnsupportedEncodingException paramView)
      {
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.design.HomeDesignPriceAgent
 * JD-Core Version:    0.6.0
 */