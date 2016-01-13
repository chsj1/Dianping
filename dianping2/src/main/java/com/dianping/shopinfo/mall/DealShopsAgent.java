package com.dianping.shopinfo.mall;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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
import com.dianping.shopinfo.mall.view.MallDealListItem;
import com.dianping.v1.R.color;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.Arrays;

public class DealShopsAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_DEALSHOPS = "0630Basic.50DealShops";
  private static final int DISPLAY_ITEM_COUNT = 3;
  DPObject dealList;
  ArrayList<DPObject> deals;
  DPObject error;
  MApiRequest request;

  public DealShopsAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createContentCell()
  {
    NovaLinearLayout localNovaLinearLayout = new NovaLinearLayout(getContext());
    localNovaLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localNovaLinearLayout.setOrientation(1);
    int i = 0;
    while (i < 3)
    {
      localNovaLinearLayout.addView(getItemView(i));
      if (i < 2)
      {
        View localView = new View(getContext());
        localView.setLayoutParams(new ViewGroup.LayoutParams(-1, 1));
        localView.setBackgroundColor(getResources().getColor(R.color.inner_divider));
        localNovaLinearLayout.addView(localView);
      }
      i += 1;
    }
    return localNovaLinearLayout;
  }

  private View createDealShopsView()
  {
    Object localObject;
    if ((this.dealList == null) || (this.deals == null) || (this.deals.size() < 3))
      localObject = null;
    ShopinfoCommonCell localShopinfoCommonCell;
    int i;
    do
    {
      return localObject;
      localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
      localObject = this.dealList.getString("Title");
      i = this.dealList.getInt("Count");
      localShopinfoCommonCell.addContent(createContentCell(), false, null);
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        localShopinfoCommonCell.setTitle((String)localObject, new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = DealShopsAgent.this.dealList.getString("Url");
            if (!TextUtils.isEmpty(paramView))
            {
              paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
              DealShopsAgent.this.getFragment().startActivity(paramView);
            }
          }
        });
        localShopinfoCommonCell.titleLay.setGAString("inmall_tuangou_more", getGAExtra());
      }
      localObject = localShopinfoCommonCell;
    }
    while (i <= 0);
    localShopinfoCommonCell.setSubTitle("(" + i + ")");
    return (View)localShopinfoCommonCell;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getfoodshopdeallist.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", "" + shopId());
    localBuilder.appendQueryParameter("cityid", "" + cityId());
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public View getItemView(int paramInt)
  {
    Object localObject = this.deals.get(paramInt);
    MallDealListItem localMallDealListItem = (MallDealListItem)this.res.inflate(getContext(), R.layout.shop_deal_list_item, null, false);
    if ((localObject instanceof DPObject))
    {
      localObject = (DPObject)localObject;
      localMallDealListItem.setDeal((DPObject)localObject);
      localMallDealListItem.setOnClickListener(new View.OnClickListener((DPObject)localObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$deal.getString("DealUrl");
          if (!TextUtils.isEmpty(paramView))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            DealShopsAgent.this.getFragment().startActivity(paramView);
          }
        }
      });
    }
    localMallDealListItem.setGAString("inmall_tuangou_single", "", paramInt);
    return (View)localMallDealListItem;
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
      while (getShopStatus() != 0);
      paramBundle = createDealShopsView();
    }
    while (paramBundle == null);
    addCell("0630Basic.50DealShops", paramBundle, "inmall_tuangou", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.dealList = ((DPObject)paramBundle.getParcelable("dealList"));
      this.error = ((DPObject)paramBundle.getParcelable("error"));
    }
    if ((this.dealList == null) && (this.error == null))
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
    if ((paramMApiResponse.error() instanceof DPObject));
    for (this.error = ((DPObject)paramMApiResponse.error()); ; this.error = new DPObject())
    {
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.dealList = ((DPObject)paramMApiResponse.result());
    if (this.dealList.getArray("Deals") != null)
      this.deals = new ArrayList(Arrays.asList(this.dealList.getArray("Deals")));
    this.error = null;
    dispatchAgentChanged(false);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("dealList", this.dealList);
    localBundle.putParcelable("error", this.error);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.mall.DealShopsAgent
 * JD-Core Version:    0.6.0
 */