package com.dianping.shopinfo.clothes.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.dianping.shopinfo.clothes.view.ClothesBrandGoodsItemView;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;

public class BrandGoodsAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_BRAND_GOODS = "1980Brand.goods";
  public static final String GA_ELEMENT = "branditem";
  private NovaLinearLayout brandGoodsView;
  private DPObject mBrandGoods;
  private MApiRequest request;

  public BrandGoodsAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createGoodsCell()
  {
    if (this.mBrandGoods == null)
      return null;
    if (this.brandGoodsView == null)
      GAHelper.instance().contextStatisticsEvent(getContext(), "branditem", getGAExtra(), "view");
    this.brandGoodsView = ((NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_technician_view, null, false));
    this.brandGoodsView.setPadding(16, 0, 20, 0);
    Object localObject = this.mBrandGoods.getArray("NewGoodses");
    int i = 0;
    while (i < 3)
    {
      this.brandGoodsView.addView(createItemView(this.brandGoodsView, localObject[i]));
      i += 1;
    }
    localObject = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    ((ShopinfoCommonCell)localObject).titleLay.setGAString("branditem", "全部");
    ((ShopinfoCommonCell)localObject).addContent(this.brandGoodsView, false, null);
    ((ShopinfoCommonCell)localObject).setTitle(this.mBrandGoods.getString("Title") + "(" + this.mBrandGoods.getInt("Count") + ")", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = BrandGoodsAgent.this.mBrandGoods.getString("GoUrl");
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse(paramView));
        BrandGoodsAgent.this.startActivity(localIntent);
      }
    });
    return (View)localObject;
  }

  private View createItemView(NovaLinearLayout paramNovaLinearLayout, DPObject paramDPObject)
  {
    paramNovaLinearLayout = ClothesBrandGoodsItemView.createView(getContext(), paramNovaLinearLayout);
    paramNovaLinearLayout.init(paramDPObject.getString("PicUrl"), paramDPObject.getString("MainTitle"), paramDPObject.getString("SubTitle"), paramDPObject.getString("GoUrl"));
    return paramNovaLinearLayout;
  }

  private boolean isParamInVaid(DPObject paramDPObject)
  {
    if ((paramDPObject.getString("GoUrl") == null) || (paramDPObject.getString("GoUrl").length() == 0));
    do
      return true;
    while ((paramDPObject.getInt("Count") == 0) || (paramDPObject.getString("Title") == null) || (paramDPObject.getString("Title").length() == 0) || (paramDPObject.getArray("NewGoodses") == null) || (paramDPObject.getArray("NewGoodses").length < 3));
    return false;
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getquarternewgoods.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.request = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      return;
      if ((this.mBrandGoods == null) && (this.request == null))
        sendRequest();
      paramBundle = createGoodsCell();
    }
    while (paramBundle == null);
    addCell("1980Brand.goods", paramBundle, 0);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.mBrandGoods = ((DPObject)paramMApiResponse.result());
    if (this.mBrandGoods == null)
      return;
    if (isParamInVaid(this.mBrandGoods))
    {
      this.mBrandGoods = null;
      return;
    }
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.clothes.agent.BrandGoodsAgent
 * JD-Core Version:    0.6.0
 */