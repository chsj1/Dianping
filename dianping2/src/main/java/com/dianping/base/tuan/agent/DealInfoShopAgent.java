package com.dianping.base.tuan.agent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.tuan.utils.TuanSharedDataKey;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.base.widget.TuanNearestShopInfoLayout;
import com.dianping.base.widget.TuanNearestShopInfoLayout.BestShopListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class DealInfoShopAgent extends TuanGroupCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, TuanNearestShopInfoLayout.BestShopListener
{
  private DealInfoCommonCell commCell;
  protected int dealId;
  protected DPObject dpDeal;
  private DPObject dpNearestDealShop;
  double latitude;
  double longitude;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (DealInfoShopAgent.this.dpNearestDealShop != null)
      {
        paramView = DealInfoShopAgent.this.dpNearestDealShop.getObject("Shop");
        DealInfoShopAgent.this.allShopClicked(paramView, DealInfoShopAgent.this.dealId);
      }
    }
  };
  boolean nearestDealShopLoaded;
  private MApiRequest nearestDealShopReq;
  private TuanNearestShopInfoLayout nearestShopInfo;
  protected int shopId;

  public DealInfoShopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void allShopClicked(DPObject paramDPObject, int paramInt)
  {
    if (paramDPObject == null)
      return;
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopidlist?shopid=" + paramDPObject.getInt("ID") + "&dealid=" + paramInt + "&istuan=1"));
    localIntent.putExtra("showAddBranchShop", true);
    localIntent.putExtra("shop", paramDPObject);
    getContext().startActivity(localIntent);
  }

  private void saveBestShopId()
  {
    if ((this.dpNearestDealShop != null) && (this.dpNearestDealShop.getObject("Shop") != null))
    {
      setSharedObject(TuanSharedDataKey.DEAL_BEST_SHOP, this.dpNearestDealShop.getObject("Shop"));
      return;
    }
    setSharedObject(TuanSharedDataKey.DEAL_BEST_SHOP, null);
  }

  private void sendShopRequest()
  {
    if ((this.nearestDealShopReq != null) || (this.nearestDealShopLoaded))
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("bestshopgn.bin");
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    localUrlBuilder.addParam("dealgroupid", Integer.valueOf(this.dealId));
    localUrlBuilder.addParam("shopid", Integer.valueOf(this.shopId));
    String str = accountService().token();
    if (!TextUtils.isEmpty(str))
      localUrlBuilder.addParam("token", str);
    if (location() != null)
    {
      localUrlBuilder.addParam("lat", Double.valueOf(location().latitude()));
      localUrlBuilder.addParam("lng", Double.valueOf(location().longitude()));
    }
    this.nearestDealShopReq = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.CRITICAL);
    mapiService().exec(this.nearestDealShopReq, this);
  }

  private void setupView()
  {
    this.nearestShopInfo = ((TuanNearestShopInfoLayout)this.res.inflate(getContext(), R.layout.tuan_deal_shop, getParentView(), false));
    this.nearestShopInfo.setOnBestShopClickListener(this);
    this.commCell = new DealInfoCommonCell(getContext());
    this.commCell.setTitleSize(0, getResources().getDimension(R.dimen.deal_info_agent_title_text_size));
    this.commCell.setArrowPreSize(0, getResources().getDimension(R.dimen.deal_info_agent_subtitle_text_size));
    this.commCell.setPaddingLeft((int)getResources().getDimension(R.dimen.deal_info_padding_left));
    this.commCell.setPaddingRight((int)getResources().getDimension(R.dimen.deal_info_padding_right));
    this.commCell.addContent(this.nearestShopInfo, false);
  }

  private void updateView()
  {
    boolean bool;
    int i;
    if (this.dpNearestDealShop != null)
    {
      if (location() != null)
      {
        this.latitude = location().latitude();
        this.longitude = location().longitude();
      }
      if ((this.dpDeal.getInt("Tag") & 0x80) != 0)
        break label148;
      bool = true;
      if (this.nearestShopInfo.setDealShop(this.dpNearestDealShop, this.latitude, this.longitude, bool, this.dealId))
      {
        removeAllCells();
        this.nearestShopInfo.hideAllShop();
        i = this.dpNearestDealShop.getInt("ShopCount");
        if (i > 1)
          break label153;
        this.commCell.setTitle("适用商户");
        this.commCell.hideArrow();
      }
    }
    while (true)
    {
      if (!(this.fragment instanceof GroupAgentFragment))
        break label243;
      addCell("030DealShop.01DealShop0", this.commCell);
      return;
      label148: bool = false;
      break;
      label153: this.commCell.setTitle("适用商户(" + i + ")", this.mListener);
      this.commCell.titleLay.setGAString("moreshop");
      ((NovaActivity)getContext()).addGAView(this.commCell.titleLay, -1, "tuandeal", "tuandeal".equals(((NovaActivity)getContext()).getPageName()));
    }
    label243: addCell("030DealShop.01DealShop0", this.commCell);
    addEmptyCell("030DealShop.01DealShop1");
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null);
    do
    {
      return;
      if (paramBundle == null)
        continue;
      this.dealId = paramBundle.getInt("dealid");
      this.shopId = paramBundle.getInt("shopid", -1);
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal == paramBundle)
        continue;
      this.dpDeal = paramBundle;
      if (this.dpDeal == null)
        continue;
      this.dealId = this.dpDeal.getInt("ID");
    }
    while (this.dpDeal == null);
    sendShopRequest();
    if (this.nearestShopInfo == null)
      setupView();
    updateView();
  }

  public void onAllShopClicked(DPObject paramDPObject, int paramInt)
  {
    allShopClicked(paramDPObject, paramInt);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.dpNearestDealShop = ((DPObject)paramBundle.getParcelable("dpNearestDealShop"));
      this.nearestDealShopLoaded = paramBundle.getBoolean("nearestDealShopLoaded");
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.nearestDealShopReq)
    {
      this.nearestDealShopReq = null;
      paramMApiRequest = new AgentMessage("dealInfoShop");
      paramMApiRequest.body.putParcelable("shop", null);
      dispatchMessage(paramMApiRequest);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.nearestDealShopReq)
    {
      this.nearestDealShopReq = null;
      this.nearestDealShopLoaded = true;
      this.dpNearestDealShop = ((DPObject)paramMApiResponse.result());
      saveBestShopId();
      dispatchAgentChanged(false);
      new Bundle();
      if ((this.dpNearestDealShop != null) && (this.dpNearestDealShop.getObject("Shop") != null))
      {
        paramMApiRequest = new AgentMessage("dealInfoShop");
        paramMApiRequest.body.putParcelable("shop", this.dpNearestDealShop.getObject("Shop"));
        dispatchMessage(paramMApiRequest);
      }
    }
  }

  public void onShopAddressClicked(DPObject paramDPObject)
  {
  }

  public void onShopClicked(DPObject paramDPObject)
  {
  }

  public void onShopTelClicked(DPObject paramDPObject)
  {
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("dpNearestDealShop", this.dpNearestDealShop);
    localBundle.putBoolean("nearestDealShopLoaded", this.nearestDealShopLoaded);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoShopAgent
 * JD-Core Version:    0.6.0
 */