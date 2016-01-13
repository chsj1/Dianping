package com.dianping.wed.baby.fragment;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.drawable;
import com.dianping.wed.baby.widget.WeddingBaseAgent;
import com.dianping.widget.MyScrollView;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import java.util.ArrayList;
import org.json.JSONObject;

public class WeddingProductBaseFragment extends GroupAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>
{
  WeddingProductBaseFragment.CellContainer bottomCellContainer;
  ViewGroup bottomView;
  Drawable cellArrow;
  Drawable cellMaskBottom;
  Drawable cellMaskMiddle;
  Drawable cellMaskSingle;
  Drawable cellMaskTop;
  ViewGroup contentView;
  DPObject dpProduct;
  DPObject dpShop;
  boolean needProductReuqest = true;
  boolean needShopRequest = true;
  MApiRequest productRequest;
  boolean productRequestRetrieved = false;
  int productid;
  PullToRefreshScrollView pullToRefreshScrollView;
  Rect rect = new Rect();
  MyResources res;
  FrameLayout rootView;
  MyScrollView scrollView;
  MApiRequest shopRequest;
  int shopid;
  WeddingBaseAgent topCellAgent;
  WeddingProductBaseFragment.CellContainer topCellContainer;

  public int caseId()
  {
    if (getIntParam("caseid") > 0)
      return getIntParam("caseid");
    return 0;
  }

  protected void dispatchProductChanged()
  {
    dispatchCellChanged(null);
  }

  protected void dispatchShopRequest()
  {
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    return null;
  }

  public DPObject getProduct()
  {
    return this.dpProduct;
  }

  public int getProductId()
  {
    if (this.productid != 0)
      return this.productid;
    return 0;
  }

  public ScrollView getScrollView()
  {
    return (ScrollView)this.pullToRefreshScrollView.getRefreshableView();
  }

  public DPObject getShop()
  {
    if (getObjectParam("shop") != null)
      return getObjectParam("shop");
    return this.dpShop;
  }

  public int getShopId()
  {
    int i;
    if (this.shopid != 0)
      i = this.shopid;
    DPObject localDPObject;
    do
    {
      return i;
      localDPObject = getObjectParam("shop");
      i = 0;
    }
    while (localDPObject == null);
    return localDPObject.getInt("ID");
  }

  protected void initTitleShare()
  {
    ((NovaActivity)getActivity()).getTitleBar().addRightViewItem("", R.drawable.ic_action_share, new WeddingProductBaseFragment.1(this));
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if ((!this.productRequestRetrieved) && (this.productRequest == null) && (this.dpProduct == null))
      sendproductRequest();
    if ((this.dpShop == null) && (this.shopRequest == null))
      sendShopRequest();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.res = MyResources.getResource(WeddingProductBaseFragment.class);
    resolveArguments(paramBundle);
  }

  public void onDestroy()
  {
    if (this.shopRequest != null)
    {
      mapiService().abort(this.shopRequest, this, true);
      this.shopRequest = null;
    }
    if (this.productRequest != null)
    {
      mapiService().abort(this.productRequest, this, true);
      this.productRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.productRequest)
    {
      this.productRequestRetrieved = false;
      this.productRequest = null;
      if (getActivity() != null)
      {
        Toast.makeText(getActivity(), "该产品已不存在", 0).show();
        getActivity().finish();
      }
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.productRequest)
    {
      this.dpProduct = ((DPObject)paramMApiResponse.result());
      this.productRequestRetrieved = true;
      this.productRequest = null;
      dispatchProductChanged();
    }
    do
      return;
    while (paramMApiRequest != this.shopRequest);
    this.shopRequest = null;
    this.dpShop = ((DPObject)paramMApiResponse.result());
    if (this.dpShop != null)
      paramMApiRequest = this.dpShop.getString("ShopStyle");
    try
    {
      paramMApiRequest = new JSONObject(paramMApiRequest);
      paramMApiRequest = new DPObject().edit().putString("ShopView", paramMApiRequest.getString("shopView")).putString("PicMode", paramMApiRequest.getString("picMode")).generate();
      this.dpShop = this.dpShop.edit().putObject("ClientShopStyle", paramMApiRequest).generate();
      dispatchShopRequest();
      return;
    }
    catch (Exception paramMApiRequest)
    {
      while (true)
        paramMApiRequest.printStackTrace();
    }
  }

  protected void resolveArguments(Bundle paramBundle)
  {
    this.shopid = getIntParam("shopid");
    this.productid = getIntParam("productid");
    if (this.shopid == 0)
      this.shopid = getIntParam("shopId");
    if (this.productid == 0)
      this.productid = getIntParam("productId");
    if (this.productid == 0)
      return;
    this.dpShop = getObjectParam("shop");
    dispatchProductChanged();
  }

  void sendShopRequest()
  {
    if (!this.needShopRequest);
    do
      return;
    while ((this.shopRequest != null) || (this.shopid <= 0));
    this.shopRequest = BasicMApiRequest.mapiGet(new StringBuilder().append("http://m.api.dianping.com/shop.bin?shopid=").append(this.shopid).toString(), CacheType.NORMAL);
    mapiService().exec(this.shopRequest, this);
  }

  protected void sendproductRequest()
  {
    if (!this.needProductReuqest);
    do
      return;
    while ((this.productRequest != null) || (this.productid <= 0));
    this.productRequest = BasicMApiRequest.mapiGet(new StringBuilder().append("http://m.api.dianping.com/wedding/productdetail.bin?productid=").append(this.productid).toString(), CacheType.DISABLED);
    mapiService().exec(this.productRequest, this);
  }

  public void setBottomCell(View paramView, WeddingBaseAgent paramWeddingBaseAgent, int paramInt)
  {
    if (this.bottomCellContainer == null)
      return;
    this.bottomCellContainer.reset();
    this.bottomCellContainer.set(paramView, paramWeddingBaseAgent, paramInt, 1, 3);
    this.bottomView.setVisibility(0);
  }

  public void setShop(DPObject paramDPObject)
  {
    this.dpShop = paramDPObject;
  }

  public void setTopCell(View paramView, WeddingBaseAgent paramWeddingBaseAgent, int paramInt)
  {
    if (this.topCellContainer == null)
      return;
    this.topCellContainer.reset();
    this.topCellAgent = paramWeddingBaseAgent;
    this.topCellContainer.set(paramView, paramWeddingBaseAgent, paramInt, 1, 3);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingProductBaseFragment
 * JD-Core Version:    0.6.0
 */