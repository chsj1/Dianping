package com.dianping.shopinfo.market.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;

public class ScheduledBusAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_BUS = "0420Bus.01bus";
  private DPObject busInfo;
  private ShopinfoCommonCell commCell;
  private MApiRequest infoRequest;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (ScheduledBusAgent.this.busInfo != null)
      {
        paramView = ScheduledBusAgent.this.busInfo.getString("Url");
        if ((paramView != null) && (!paramView.equals("")))
        {
          Intent localIntent = new Intent("android.intent.action.VIEW");
          localIntent.setData(Uri.parse(paramView));
          ScheduledBusAgent.this.startActivity(localIntent);
        }
      }
    }
  };

  public ScheduledBusAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createBusCell()
  {
    if (this.commCell == null)
      GAHelper.instance().contextStatisticsEvent(getContext(), "martbus", getGAExtra(), "view");
    this.commCell = ((ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false));
    this.commCell.setTitle(this.busInfo.getString("Title"));
    ((ImageView)this.commCell.findViewById(R.id.indicator)).setVisibility(0);
    this.commCell.setBlankContent(true);
    this.commCell.setOnClickListener(this.mListener);
    this.commCell.setGAString("martbus");
    return this.commCell;
  }

  private void sendInfoRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getmarketbusinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.infoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.infoRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      do
      {
        return;
        if ((this.busInfo != null) || (this.infoRequest != null))
          continue;
        sendInfoRequest();
      }
      while ((this.busInfo == null) || (TextUtils.isEmpty(this.busInfo.getString("Url"))));
      paramBundle = createBusCell();
    }
    while (paramBundle == null);
    addCell("0420Bus.01bus", paramBundle, 0);
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
      this.busInfo = ((DPObject)paramMApiResponse.result());
    }
    while ((this.busInfo == null) || (TextUtils.isEmpty(this.busInfo.getString("Url"))));
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.market.agent.ScheduledBusAgent
 * JD-Core Version:    0.6.0
 */