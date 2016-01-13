package com.dianping.hui.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import java.util.ArrayList;

public class HuiBuffetDataSource
  implements IHuiDataSource
{
  private static final String GET_MEAL_LIST = "http://hui.api.dianping.com/loadbuffet.hui";
  public static final int ORDER_CODE_STATUS_FAIL_MONEY = 20;
  public static final int ORDER_CODE_STATUS_FAIL_TIME = 30;
  public static final int ORDER_CODE_STATUS_SUCCESS = 10;
  private static final String REQ_CREATE_BUFFET_ORDER = "http://hui.api.dianping.com/commitbuffetorder.hui";
  public String bizOrderId;
  public int bizOrderType;
  public ArrayList<Buffet> buffetList = new ArrayList();
  private NovaActivity mActivity;
  public MApiRequest mGetBuffetListReq;
  public HuiBuffetDataLoaderListener mHuiBuffetDataLoaderListener;
  public MApiRequest mReqCreateBuffetOrder;
  private FullRequestHandle<MApiRequest, MApiResponse> mapiHandler = new HuiBuffetDataSource.1(this);
  public String requestParams;
  public String shopId;
  public String shopName = "";

  public HuiBuffetDataSource(NovaActivity paramNovaActivity)
  {
    this.mActivity = paramNovaActivity;
  }

  public void getBuffetList(String paramString)
  {
    this.mGetBuffetListReq = BasicMApiRequest.mapiGet(Uri.parse("http://hui.api.dianping.com/loadbuffet.hui" + paramString).buildUpon().build().toString(), CacheType.DISABLED);
    this.mActivity.mapiService().exec(this.mGetBuffetListReq, this.mapiHandler);
  }

  public void releaseRequests()
  {
    if (this.mGetBuffetListReq != null)
    {
      this.mActivity.mapiService().abort(this.mGetBuffetListReq, this.mapiHandler, true);
      this.mGetBuffetListReq = null;
    }
    if (this.mReqCreateBuffetOrder != null)
    {
      this.mActivity.mapiService().abort(this.mReqCreateBuffetOrder, this.mapiHandler, true);
      this.mReqCreateBuffetOrder = null;
    }
  }

  public void requestCreateOrder(String paramString)
  {
    if (this.mReqCreateBuffetOrder != null)
      this.mActivity.mapiService().abort(this.mReqCreateBuffetOrder, this.mapiHandler, true);
    Uri.Builder localBuilder = Uri.parse("http://hui.api.dianping.com/commitbuffetorder.hui").buildUpon();
    localBuilder.appendQueryParameter("shopid", this.shopId);
    localBuilder.appendQueryParameter("buffets", paramString);
    localBuilder.appendQueryParameter("outbiztype", this.bizOrderType + "");
    if (this.bizOrderId == null);
    for (paramString = ""; ; paramString = this.bizOrderId)
    {
      localBuilder.appendQueryParameter("outbizid", paramString);
      this.mReqCreateBuffetOrder = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      this.mActivity.mapiService().exec(this.mReqCreateBuffetOrder, this.mapiHandler);
      return;
    }
  }

  public void restoreData(Bundle paramBundle)
  {
    this.requestParams = paramBundle.getString("requestParams");
    this.shopId = paramBundle.getString("shopid");
    this.shopName = paramBundle.getString("shopname");
  }

  public void saveData(Bundle paramBundle)
  {
    paramBundle.putString("requestParams", this.requestParams);
    paramBundle.putString("shopid", this.shopId);
    paramBundle.putString("shopname", this.shopName);
  }

  public void setHuiBuffetDataLoaderListener(HuiBuffetDataLoaderListener paramHuiBuffetDataLoaderListener)
  {
    this.mHuiBuffetDataLoaderListener = paramHuiBuffetDataLoaderListener;
  }

  public static abstract interface HuiBuffetDataLoaderListener
  {
    public abstract void createOrderComplete(HuiMapiStatus paramHuiMapiStatus, Message paramMessage);

    public abstract void loadBuffetListComplete(HuiMapiStatus paramHuiMapiStatus, Object paramObject);
  }

  public static abstract interface ScrollListener
  {
    public abstract void scrollToGivenViewPosition(View paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.entity.HuiBuffetDataSource
 * JD-Core Version:    0.6.0
 */