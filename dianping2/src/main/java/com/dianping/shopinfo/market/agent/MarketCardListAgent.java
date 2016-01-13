package com.dianping.shopinfo.market.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
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
import com.dianping.shopinfo.market.view.MarketProductListItem;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.Arrays;

public class MarketCardListAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_SALESPRODUCT = "0410Product.01product";
  private ShopinfoCommonCell commCell;
  private DPObject productInfo;
  private ArrayList<DPObject> productList;
  private MApiRequest request;

  public MarketCardListAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createContentCell()
  {
    NovaLinearLayout localNovaLinearLayout = new NovaLinearLayout(getContext());
    localNovaLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localNovaLinearLayout.setOrientation(1);
    int i = 0;
    while (i < this.productList.size())
    {
      localNovaLinearLayout.addView(getItemView(i));
      i += 1;
    }
    return localNovaLinearLayout;
  }

  private View createSalesProductView()
  {
    if (this.commCell == null)
      GAHelper.instance().contextStatisticsEvent(getContext(), "martcoupon", getGAExtra(), "view");
    this.commCell = ((ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false));
    String str = this.productInfo.getString("Title");
    int i = this.productInfo.getInt("Count");
    this.commCell.addContent(createContentCell(), false, null);
    if (!TextUtils.isEmpty(str))
    {
      this.commCell.setTitle(str, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = MarketCardListAgent.this.productInfo.getString("Url");
          if (!TextUtils.isEmpty(paramView))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            MarketCardListAgent.this.getFragment().startActivity(paramView);
          }
        }
      });
      this.commCell.titleLay.setGAString("martcoupon", getGAExtra());
    }
    if (i > 0)
      this.commCell.setSubTitle("(" + i + ")");
    return this.commCell;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getmarketcardlist.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", "" + shopId());
    localBuilder.appendQueryParameter("cityid", "" + cityId());
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public View getItemView(int paramInt)
  {
    Object localObject = this.productList.get(paramInt);
    MarketProductListItem localMarketProductListItem = (MarketProductListItem)this.res.inflate(getContext(), R.layout.shopinfo_market_product_list_item, null, false);
    if ((localObject instanceof DPObject))
    {
      localObject = (DPObject)localObject;
      localMarketProductListItem.setProduct((DPObject)localObject, this.productList.size(), paramInt);
      localMarketProductListItem.setOnClickListener(new View.OnClickListener((DPObject)localObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$product.getString("Url");
          if (!TextUtils.isEmpty(paramView))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            MarketCardListAgent.this.getFragment().startActivity(paramView);
          }
        }
      });
    }
    localMarketProductListItem.setGAString("martcoupon", "", paramInt);
    return (View)localMarketProductListItem;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      do
        return;
      while ((this.productInfo == null) || (this.productList == null) || (this.productList.size() <= 0));
      if (this.productList.size() > 4)
        this.productList = ((ArrayList)this.productList.subList(0, 4));
      paramBundle = createSalesProductView();
    }
    while (paramBundle == null);
    addCell("0410Product.01product", paramBundle, null, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    if ((paramMApiResponse == null) || (paramMApiResponse.result() == null));
    do
    {
      return;
      this.productInfo = ((DPObject)paramMApiResponse.result());
    }
    while ((this.productInfo == null) || (this.productInfo.getArray("MarketCards") == null) || (this.productInfo.getArray("MarketCards").length <= 0));
    this.productList = new ArrayList(Arrays.asList(this.productInfo.getArray("MarketCards")));
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.market.agent.MarketCardListAgent
 * JD-Core Version:    0.6.0
 */