package com.dianping.base.tuan.agent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.network.NetworkUtils;

public class DealInfoDishAgent extends TuanDishAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private DPObject bestShop;
  private DPObject dishList;
  boolean dishLoaded;
  protected DPObject dpDeal;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = DealInfoDishAgent.this.bestShop;
      int j = paramView.getInt("ID");
      if (j == 0)
        return;
      int i;
      Intent localIntent;
      if ((DPActivity.preferences().getBoolean("isShowListImage", true)) || (NetworkUtils.isWIFIConnection(DealInfoDishAgent.this.getContext())))
      {
        i = 1;
        localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://recommendlist"));
        localIntent.putExtra("objShop", paramView);
        localIntent.putExtra("shopId", j);
        if (i == 0)
          break label112;
        localIntent.putExtra("showImage", true);
      }
      while (true)
      {
        DealInfoDishAgent.this.getFragment().startActivity(localIntent);
        return;
        i = 0;
        break;
        label112: localIntent.putExtra("showImage", false);
      }
    }
  };
  private MApiRequest shopDishListReq;
  protected int status;

  public DealInfoDishAgent(Object paramObject)
  {
    super(paramObject);
  }

  private String getDishStr(DPObject paramDPObject)
  {
    StringBuffer localStringBuffer;
    if (paramDPObject != null)
    {
      localStringBuffer = new StringBuffer();
      paramDPObject = paramDPObject.getArray("List");
      if ((paramDPObject != null) && (paramDPObject.length != 0));
    }
    else
    {
      return null;
    }
    int i = 0;
    while (i < paramDPObject.length)
    {
      Object localObject = paramDPObject[i];
      if (i != 0)
        localStringBuffer.append("  ");
      localStringBuffer.append(localObject.getString("Name"));
      i += 1;
    }
    return localStringBuffer.toString();
  }

  private void parseData()
  {
    if (this.dishList != null)
      this.dishStr = getDishStr(this.dishList);
    this.mGAString = "bestfood";
  }

  private void sendRequest()
  {
    if ((this.shopDishListReq != null) || (this.dishLoaded))
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("bestshopdishlistgn.bin");
    localUrlBuilder.addParam("shopid", Integer.valueOf(this.bestShop.getInt("ID")));
    this.shopDishListReq = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.shopDishListReq, this);
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ((paramAgentMessage != null) && ("dealInfoShop".equals(paramAgentMessage.what)) && (paramAgentMessage.body != null) && (paramAgentMessage.body.getParcelable("shop") != null))
    {
      this.bestShop = ((DPObject)paramAgentMessage.body.getParcelable("shop"));
      dispatchAgentChanged(false);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      this.status = paramBundle.getInt("status");
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != paramBundle)
        this.dpDeal = paramBundle;
    }
    if ((this.status == 1) && (hasChannelTag(this.dpDeal, "voucher")) && (this.bestShop != null))
    {
      sendRequest();
      parseData();
      updateAgent();
      setDishOnClickListener(this.mListener);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopDishListReq)
      this.shopDishListReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.shopDishListReq)
    {
      this.shopDishListReq = null;
      this.dishLoaded = true;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (DPObjectUtils.isDPObjectof(paramMApiRequest, "DishList"))
        this.dishList = paramMApiRequest;
      dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoDishAgent
 * JD-Core Version:    0.6.0
 */