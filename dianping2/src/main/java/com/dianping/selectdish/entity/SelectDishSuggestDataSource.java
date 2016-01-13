package com.dianping.selectdish.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.selectdish.model.SuggestDishInfo;
import com.dianping.selectdish.ui.SelectDishSuggestActivity;
import java.util.ArrayList;
import java.util.Iterator;

public class SelectDishSuggestDataSource
{
  private static final String SELECT_DISH_SUGGEST_SUBMIT_URL = "http://mapi.dianping.com/orderdish/submitrecommenddish.hbt";
  private static final String SELECT_DISH_SUGGEST_URL = "http://mapi.dianping.com/orderdish/recommenddishlist.hbt";
  public SelectDishSuggestActivity activity;
  public SelectDishSuggestDataLoaderListener dataLoaderListener;
  public String emptyMsg;
  public String errorMsg;
  public boolean isEnd = false;
  private FullRequestHandle<MApiRequest, MApiResponse> mapiHandler = new FullRequestHandle()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == SelectDishSuggestDataSource.this.selectDishSuggestReq)
      {
        SelectDishSuggestDataSource.access$002(SelectDishSuggestDataSource.this, null);
        if (paramMApiResponse.message() == null)
          break label117;
      }
      label117: for (SimpleMsg localSimpleMsg = paramMApiResponse.message(); ; localSimpleMsg = new SimpleMsg("错误", "网络错误,请重试", 0, 0))
      {
        SelectDishSuggestDataSource.this.errorMsg = localSimpleMsg.content();
        if (SelectDishSuggestDataSource.this.dataLoaderListener != null)
          SelectDishSuggestDataSource.this.dataLoaderListener.loadSuggestDishList(SelectDishSuggestDataSource.DataLoadStatus.FAILED, SelectDishSuggestDataSource.this.errorMsg);
        if ((paramMApiRequest == SelectDishSuggestDataSource.this.submitSuggestDishReq) && (SelectDishSuggestDataSource.this.dataLoaderListener != null))
          SelectDishSuggestDataSource.this.dataLoaderListener.loadSubmitSuggestDish(SelectDishSuggestDataSource.DataLoadStatus.FAILED, paramMApiResponse);
        return;
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest == SelectDishSuggestDataSource.this.selectDishSuggestReq)
      {
        DPObject[] arrayOfDPObject = paramMApiResponse.getArray("RecommendDishs");
        if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
        {
          int j = arrayOfDPObject.length;
          int i = 0;
          while (i < j)
          {
            DPObject localDPObject = arrayOfDPObject[i];
            SelectDishSuggestDataSource.this.suggestDishList.add(new SuggestDishInfo(localDPObject));
            i += 1;
          }
        }
        SelectDishSuggestDataSource.this.isEnd = paramMApiResponse.getBoolean("IfLastPage");
        if (SelectDishSuggestDataSource.this.dataLoaderListener != null)
          SelectDishSuggestDataSource.this.dataLoaderListener.loadSuggestDishList(SelectDishSuggestDataSource.DataLoadStatus.LOADED, null);
      }
      if ((paramMApiRequest == SelectDishSuggestDataSource.this.submitSuggestDishReq) && (SelectDishSuggestDataSource.this.dataLoaderListener != null))
        SelectDishSuggestDataSource.this.dataLoaderListener.loadSubmitSuggestDish(SelectDishSuggestDataSource.DataLoadStatus.LOADED, paramMApiResponse);
    }

    public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
    {
      if ((paramMApiRequest == SelectDishSuggestDataSource.this.selectDishSuggestReq) && (SelectDishSuggestDataSource.this.dataLoaderListener != null))
        SelectDishSuggestDataSource.this.dataLoaderListener.loadSuggestDishList(SelectDishSuggestDataSource.DataLoadStatus.LOADING, null);
      if ((paramMApiRequest == SelectDishSuggestDataSource.this.submitSuggestDishReq) && (SelectDishSuggestDataSource.this.dataLoaderListener != null))
        SelectDishSuggestDataSource.this.dataLoaderListener.loadSubmitSuggestDish(SelectDishSuggestDataSource.DataLoadStatus.LOADING, null);
    }

    public void onRequestStart(MApiRequest paramMApiRequest)
    {
      if ((paramMApiRequest == SelectDishSuggestDataSource.this.selectDishSuggestReq) && (SelectDishSuggestDataSource.this.dataLoaderListener != null))
        SelectDishSuggestDataSource.this.dataLoaderListener.loadSuggestDishList(SelectDishSuggestDataSource.DataLoadStatus.LOADING, null);
      if ((paramMApiRequest == SelectDishSuggestDataSource.this.submitSuggestDishReq) && (SelectDishSuggestDataSource.this.dataLoaderListener != null))
        SelectDishSuggestDataSource.this.dataLoaderListener.loadSubmitSuggestDish(SelectDishSuggestDataSource.DataLoadStatus.LOADING, null);
    }
  };
  private MApiRequest selectDishSuggestReq;
  public int shopId;
  public String shopName;
  public int start;
  private MApiRequest submitSuggestDishReq;
  public ArrayList<SuggestDishInfo> suggestDishList = new ArrayList();

  public SelectDishSuggestDataSource(SelectDishSuggestActivity paramSelectDishSuggestActivity)
  {
    this.activity = paramSelectDishSuggestActivity;
  }

  public void fetchParams(Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      this.shopId = paramBundle.getInt("shopid");
      this.shopName = paramBundle.getString("shopname");
      return;
    }
    this.shopId = this.activity.getIntParam("shopid");
    this.shopName = this.activity.getStringParam("shopname");
  }

  public void releaseRequests()
  {
    if (this.selectDishSuggestReq != null)
    {
      this.activity.mapiService().abort(this.selectDishSuggestReq, this.mapiHandler, true);
      this.selectDishSuggestReq = null;
    }
    if (this.submitSuggestDishReq != null)
    {
      this.activity.mapiService().abort(this.submitSuggestDishReq, this.mapiHandler, true);
      this.submitSuggestDishReq = null;
    }
  }

  public void reqSuggestDish()
  {
    if (this.selectDishSuggestReq != null)
      this.activity.mapiService().abort(this.selectDishSuggestReq, this.mapiHandler, true);
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/orderdish/recommenddishlist.hbt").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    localBuilder.appendQueryParameter("start", String.valueOf(this.suggestDishList.size()));
    this.selectDishSuggestReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    this.activity.mapiService().exec(this.selectDishSuggestReq, this.mapiHandler);
  }

  public void submitSuggestDish()
  {
    if (this.submitSuggestDishReq != null)
      this.activity.mapiService().abort(this.submitSuggestDishReq, this.mapiHandler, true);
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/orderdish/submitrecommenddish.hbt").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = this.suggestDishList.iterator();
    while (localIterator.hasNext())
    {
      SuggestDishInfo localSuggestDishInfo = (SuggestDishInfo)localIterator.next();
      if (!localSuggestDishInfo.checked)
        continue;
      localStringBuilder.append(localSuggestDishInfo.dishId);
      localStringBuilder.append(",");
    }
    localBuilder.appendQueryParameter("dishidlist", localStringBuilder.toString());
    this.submitSuggestDishReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    this.activity.mapiService().exec(this.submitSuggestDishReq, this.mapiHandler);
  }

  public static enum DataLoadStatus
  {
    static
    {
      LOADED = new DataLoadStatus("LOADED", 1);
      FAILED = new DataLoadStatus("FAILED", 2);
      $VALUES = new DataLoadStatus[] { LOADING, LOADED, FAILED };
    }
  }

  public static abstract interface SelectDishSuggestDataLoaderListener
  {
    public abstract void loadSubmitSuggestDish(SelectDishSuggestDataSource.DataLoadStatus paramDataLoadStatus, Object paramObject);

    public abstract void loadSuggestDishList(SelectDishSuggestDataSource.DataLoadStatus paramDataLoadStatus, Object paramObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.entity.SelectDishSuggestDataSource
 * JD-Core Version:    0.6.0
 */