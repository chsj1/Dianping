package com.dianping.shopinfo.hospital.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.dianping.map.utils.MapUtils;
import com.dianping.model.Location;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class TrafficInfoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_TRAFFICINFO = "0340trafficinfo.";
  private final View.OnClickListener contentListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (TrafficInfoAgent.this.shop != null)
        MapUtils.gotoNavi(TrafficInfoAgent.this.getContext(), TrafficInfoAgent.this.shop);
    }
  };
  private MApiRequest infoRequest;
  private DPObject shop;
  private DPObject shopExtra;

  public TrafficInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createTrafficInfoAgent()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    TextView localTextView = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
    String str1 = this.shopExtra.getString("Path");
    String str2 = this.shopExtra.getString("Pathtime");
    localTextView.setVisibility(0);
    Object localObject = str1;
    if (!TextUtils.isEmpty(str2))
      localObject = str1 + " " + str2;
    localTextView.setText("距离位置： " + (String)localObject);
    ((NovaRelativeLayout)localShopinfoCommonCell.addContent(localTextView, false, this.contentListener)).setGAString("transport", getGAExtra());
    int i = this.shop.getInt("BranchCounts");
    if (i > 0)
    {
      localObject = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
      ((TextView)localObject).setText("其他分院(" + i + ")");
      ((NovaRelativeLayout)localShopinfoCommonCell.addContent((View)localObject, true, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = TrafficInfoAgent.this.getShop();
          if (paramView == null)
            return;
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopidlist?shopid=" + TrafficInfoAgent.this.shopId()));
          localIntent.putExtra("showAddBranchShop", true);
          localIntent.putExtra("shop", paramView);
          TrafficInfoAgent.this.getFragment().startActivity(localIntent);
          TrafficInfoAgent.this.statisticsEvent("shopinfo5", "shopinfo5_branch", "", 0);
        }
      })).setGAString("branch", getGAExtra());
    }
    localShopinfoCommonCell.setTitle("交通信息", this.contentListener);
    localShopinfoCommonCell.getTitleLay().setGAString("transport", getGAExtra());
    return (View)localShopinfoCommonCell;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    this.shop = getShop();
    if (this.shop == null);
    do
    {
      do
        return;
      while ((this.shopExtra == null) || (TextUtils.isEmpty(this.shopExtra.getString("Path"))) || (getShopStatus() != 0));
      paramBundle = createTrafficInfoAgent();
    }
    while (paramBundle == null);
    addCell("0340trafficinfo.", paramBundle, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.infoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.infoRequest = null;
    if ((paramMApiResponse == null) || (paramMApiResponse.result() == null));
    do
    {
      return;
      this.shopExtra = ((DPObject)paramMApiResponse.result());
    }
    while ((this.shopExtra == null) || (this.shopExtra.getString("Path") == null) || (this.shopExtra.getString("Path").length() <= 0));
    dispatchAgentChanged(false);
  }

  public void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/mshop/shopextra.bin?shopid=" + shopId()).buildUpon();
    if (location() != null)
      localBuilder.appendQueryParameter("lng", String.valueOf(location().longitude())).appendQueryParameter("lat", String.valueOf(location().latitude()));
    this.infoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.infoRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hospital.agent.TrafficInfoAgent
 * JD-Core Version:    0.6.0
 */