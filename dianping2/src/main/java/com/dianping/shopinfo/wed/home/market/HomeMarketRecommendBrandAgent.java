package com.dianping.shopinfo.wed.home.market;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HomeMarketRecommendBrandAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static final String API_HOST = "http://m.api.dianping.com";
  private static final String CELL_RECOMMEND_BRAND = "0300HomeMarket.10RecommendBrand";
  public static final String H5_HOST = "http://m.dianping.com";
  private static final String HOME_MARKET_BRANDS = "homeMarketBrand";
  private DPObject[] homeMarketBrands;
  private MApiRequest mHomeMarketBrand;

  public HomeMarketRecommendBrandAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getShop() == null) || (this.homeMarketBrands == null) || (this.homeMarketBrands.length <= 0))
      return;
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.setTitle("推荐品牌", new ClickBrandListener(-1));
    HorizontalScrollView localHorizontalScrollView = (HorizontalScrollView)this.res.inflate(getContext(), R.layout.home_market_recommend_brand_view, getParentView(), false);
    LinearLayout localLinearLayout = (LinearLayout)localHorizontalScrollView.findViewById(R.id.home_market_brand_gallery);
    localLinearLayout.setPadding(ViewUtils.dip2px(getContext(), 15.0F), ViewUtils.dip2px(getContext(), 15.0F), ViewUtils.dip2px(getContext(), 15.0F), ViewUtils.dip2px(getContext(), 15.0F));
    int i = 0;
    while (i < this.homeMarketBrands.length)
    {
      View localView = this.res.inflate(getContext(), R.layout.home_market_brand_item, getParentView(), false);
      NetworkThumbView localNetworkThumbView = (NetworkThumbView)localView.findViewById(R.id.logo_image);
      localObject = this.homeMarketBrands[i].getString("Logo");
      int j = this.homeMarketBrands[i].getInt("MainCategoryID");
      paramBundle = (Bundle)localObject;
      if (TextUtils.isEmpty((CharSequence)localObject))
        paramBundle = "";
      localNetworkThumbView.setImage(paramBundle);
      localView.setOnClickListener(new ClickBrandListener(j, i + 1));
      localLinearLayout.addView(localView);
      i += 1;
    }
    paramBundle = new LinearLayout.LayoutParams(ViewUtils.dip2px(getContext(), 80.0F), ViewUtils.dip2px(getContext(), 60.0F));
    paramBundle.setMargins(0, 0, ViewUtils.dip2px(getContext(), 5.0F), 0);
    Object localObject = new ImageView(getContext());
    ((ImageView)localObject).setLayoutParams(paramBundle);
    ((ImageView)localObject).setScaleType(ImageView.ScaleType.FIT_XY);
    ((ImageView)localObject).setImageDrawable(this.res.getDrawable(R.drawable.home_market_logo_more));
    ((ImageView)localObject).setOnClickListener(new ClickBrandListener(-2));
    localLinearLayout.addView((View)localObject);
    localShopinfoCommonCell.addContent(localHorizontalScrollView, false);
    addCell("0300HomeMarket.10RecommendBrand", localShopinfoCommonCell);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (shopId() <= 0);
    do
    {
      return;
      if (paramBundle == null)
        continue;
      this.homeMarketBrands = ((DPObject[])(DPObject[])paramBundle.getParcelableArray("homeMarketBrand"));
    }
    while (this.homeMarketBrands != null);
    this.mHomeMarketBrand = BasicMApiRequest.mapiGet("http://m.api.dianping.com/wedding/homemarketrecommendbrands.bin?shopid=" + shopId(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mHomeMarketBrand, this);
  }

  public void onDestroy()
  {
    if ((this.mHomeMarketBrand != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.mHomeMarketBrand, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mHomeMarketBrand)
      this.mHomeMarketBrand = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mHomeMarketBrand)
    {
      this.mHomeMarketBrand = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject[])))
      {
        this.homeMarketBrands = ((DPObject[])(DPObject[])paramMApiResponse.result());
        if ((this.homeMarketBrands != null) && (this.homeMarketBrands.length > 0))
          dispatchAgentChanged(false);
      }
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelableArray("homeMarketBrand", this.homeMarketBrands);
    return localBundle;
  }

  private class ClickBrandListener
    implements View.OnClickListener
  {
    private int id;
    private int mainCategoryId;

    public ClickBrandListener(int arg2)
    {
      int i;
      this.mainCategoryId = i;
    }

    public ClickBrandListener(int paramInt1, int arg3)
    {
      this.mainCategoryId = paramInt1;
      int i;
      this.id = i;
    }

    public void onClick(View paramView)
    {
      paramView = new StringBuffer("http://m.dianping.com/wed/mobile/home/market/navigator/");
      paramView.append(HomeMarketRecommendBrandAgent.this.shopId() + "?");
      Object localObject;
      if (this.mainCategoryId > 0)
      {
        paramView.append("brand=");
        paramView.append(this.mainCategoryId);
        paramView.append("&");
        localObject = new ArrayList();
        ((List)localObject).add(new BasicNameValuePair("shopid", HomeMarketRecommendBrandAgent.this.shopId() + ""));
        HomeMarketRecommendBrandAgent.this.statisticsEvent("shopinfoh", "shopinfoh_brand", "", this.id, (List)localObject);
      }
      while (true)
      {
        paramView.append("dpshare=0");
        try
        {
          paramView = URLEncoder.encode(paramView.toString(), "UTF-8");
          localObject = new Intent("android.intent.action.VIEW");
          ((Intent)localObject).setData(Uri.parse("dianping://weddinghotelweb?url=" + paramView));
          HomeMarketRecommendBrandAgent.this.startActivity((Intent)localObject);
          return;
          if (this.mainCategoryId == -1)
          {
            localObject = new ArrayList();
            ((List)localObject).add(new BasicNameValuePair("shopid", HomeMarketRecommendBrandAgent.this.shopId() + ""));
            HomeMarketRecommendBrandAgent.this.statisticsEvent("shopinfoh", "shopinfoh_brand_more1", "", 0, (List)localObject);
            continue;
          }
          if (this.mainCategoryId != -2)
            continue;
          localObject = new ArrayList();
          ((List)localObject).add(new BasicNameValuePair("shopid", HomeMarketRecommendBrandAgent.this.shopId() + ""));
          HomeMarketRecommendBrandAgent.this.statisticsEvent("shopinfoh", "shopinfoh_brand_more2", "", 0, (List)localObject);
        }
        catch (java.io.UnsupportedEncodingException paramView)
        {
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.market.HomeMarketRecommendBrandAgent
 * JD-Core Version:    0.6.0
 */