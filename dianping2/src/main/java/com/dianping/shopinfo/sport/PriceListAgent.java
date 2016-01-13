package com.dianping.shopinfo.sport;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.app.DPActivity;
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
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class PriceListAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_PRICE_LIST = "0475pricelimit.";
  protected DPObject mPriceInfo;
  private NovaLinearLayout mPriceListView;
  protected MApiRequest mRequest;
  private View.OnClickListener mclickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.getTag();
      if (paramView == null);
      do
      {
        do
          return;
        while (!(paramView instanceof String));
        paramView = paramView.toString();
      }
      while (TextUtils.isEmpty(paramView));
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
      PriceListAgent.this.startActivity(paramView);
    }
  };

  public PriceListAgent(Object paramObject)
  {
    super(paramObject);
  }

  private boolean paramIsValid()
  {
    return shopId() > 0;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (this.mPriceListView != null)
      return;
    super.onAgentChanged(paramBundle);
    setupView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!paramIsValid())
      return;
    sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mRequest != paramMApiRequest);
    do
    {
      do
        return;
      while (paramMApiResponse.result() == null);
      this.mPriceInfo = ((DPObject)paramMApiResponse.result());
    }
    while (this.mPriceInfo == null);
    dispatchAgentChanged(false);
  }

  protected void sendRequest()
  {
    this.mRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/fitness/loadfitnessshopprice.bin").buildUpon().appendQueryParameter("shopid", Integer.toString(shopId())).toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this);
  }

  protected void setupView()
  {
    if (this.mPriceInfo == null);
    do
    {
      return;
      str = this.mPriceInfo.getString("Title");
    }
    while (TextUtils.isEmpty(str));
    this.mPriceListView = new NovaLinearLayout(getContext());
    CommonCell localCommonCell = (CommonCell)this.res.inflate(getContext(), R.layout.onsale_cell, getParentView(), false);
    localCommonCell.setTitle(str);
    localCommonCell.setLeftIconUrl(this.mPriceInfo.getString("IconUrl"));
    String str = this.mPriceInfo.getString("SubTitle");
    if (!TextUtils.isEmpty(str))
      localCommonCell.setSubTitle(str);
    str = this.mPriceInfo.getString("ActionUrl");
    localCommonCell.setClickable(true);
    localCommonCell.setTag(str);
    localCommonCell.setOnClickListener(this.mclickListener);
    localCommonCell.setGAString("fitness_pricelist");
    localCommonCell.gaUserInfo.shop_id = Integer.valueOf(shopId());
    ((DPActivity)getFragment().getActivity()).addGAView(localCommonCell, -1);
    this.mPriceListView.addView(localCommonCell);
    addCell("0475pricelimit.", this.mPriceListView, 64);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.sport.PriceListAgent
 * JD-Core Version:    0.6.0
 */