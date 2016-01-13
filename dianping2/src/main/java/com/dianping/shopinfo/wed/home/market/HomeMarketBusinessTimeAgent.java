package com.dianping.shopinfo.wed.home.market;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
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
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HomeMarketBusinessTimeAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static final String CELL_BUSINESS_TIME = "0200Basic.40BusinessTime";
  private static final String SHOP_EXTRA_INFO = "shopExtraInfoMall";
  private MApiRequest mShopExtraRequest;
  private DPObject shopExtra;

  public HomeMarketBusinessTimeAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.shopExtra == null);
    do
    {
      return;
      paramBundle = this.shopExtra.getString("Time");
    }
    while (TextUtils.isEmpty(paramBundle));
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    View localView1 = localShopinfoCommonCell.findViewById(R.id.title);
    View localView2 = localShopinfoCommonCell.findViewById(R.id.icon);
    View localView3 = localShopinfoCommonCell.findViewById(R.id.indicator);
    View localView4 = localShopinfoCommonCell.findViewById(R.id.content);
    View localView5 = localShopinfoCommonCell.findViewById(R.id.middle_divder_line);
    TextView localTextView = (TextView)localShopinfoCommonCell.findViewById(R.id.sub_title);
    localView1.setVisibility(8);
    localView2.setVisibility(8);
    localView3.setVisibility(8);
    localView4.setVisibility(8);
    localView5.setVisibility(8);
    localTextView.setVisibility(0);
    localTextView.setText("营业时间： " + paramBundle);
    localTextView.setMaxLines(1);
    localTextView.setEllipsize(TextUtils.TruncateAt.END);
    addCell("0200Basic.40BusinessTime", localShopinfoCommonCell);
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
      this.shopExtra = ((DPObject)paramBundle.getParcelable("shopExtraInfoMall"));
    }
    while (this.shopExtra != null);
    paramBundle = Uri.parse("http://m.api.dianping.com/mshop/shopextra.bin?shopid=" + shopId()).buildUpon();
    if (location() != null)
      paramBundle.appendQueryParameter("lng", String.valueOf(location().longitude())).appendQueryParameter("lat", String.valueOf(location().latitude()));
    this.mShopExtraRequest = BasicMApiRequest.mapiGet(paramBundle.build().toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.mShopExtraRequest, this);
  }

  public void onDestroy()
  {
    if ((this.mShopExtraRequest != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.mShopExtraRequest, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mShopExtraRequest == paramMApiRequest)
      this.mShopExtraRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mShopExtraRequest == paramMApiRequest)
    {
      this.mShopExtraRequest = null;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.shopExtra = ((DPObject)paramMApiResponse.result());
        setSharedObject("shopExtraInfoMall", this.shopExtra);
        paramMApiRequest = new Bundle();
        paramMApiRequest.putParcelable("shopExtraInfoMall", this.shopExtra);
        dispatchAgentChanged(false);
        dispatchAgentChanged("shopinfo/common_mallinfo", paramMApiRequest);
      }
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("shopExtraInfoMall", this.shopExtra);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.market.HomeMarketBusinessTimeAgent
 * JD-Core Version:    0.6.0
 */