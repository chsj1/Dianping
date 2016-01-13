package com.dianping.selectdish.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.os.Message;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.ui.SelectDishOrderResultActivity;
import com.dianping.util.TextUtils;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;

public class SelectDishOrderResultDataSource
{
  private static final int MSG_SELECT_DISH_ORDER_RESULT_PENDING = 1;
  private static final int MSG_SELECT_DISH_ORDER_RESULT_TO_BE_CONFIRMED = 2;
  private static final String SELECT_DISH_ORDER_RESULT_URL = "http://mapi.dianping.com/hobbits/queryorderdetail.hbt";
  private SelectDishOrderResultActivity activity;
  public int barCodeFlag;
  public String barCodeNo;
  public String barTitle;
  public String[] buttonList;
  private int confirmQueryInterval = 10;
  private int confirmQueryTimes = 29;
  public SelectDishOrderResultDataLoaderListener dataLoaderListener;
  public DPObject groupOnInfo;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
        paramMessage = SelectDishOrderResultDataSource.this;
        paramMessage.pendingRetry -= 1;
        SelectDishOrderResultDataSource.this.requestPayResult();
        return;
      case 2:
      }
      SelectDishOrderResultDataSource.access$010(SelectDishOrderResultDataSource.this);
      SelectDishOrderResultDataSource.this.requestPayResult();
    }
  };
  public String mainComment;
  public DPObject[] mainInfos;
  public String mainTitle;
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == SelectDishOrderResultDataSource.this.selectDishOrderResultReq)
      {
        if (SelectDishOrderResultDataSource.this.activity.selectDishOrderResultLoadedLayout != null)
          SelectDishOrderResultDataSource.this.activity.selectDishOrderResultLoadedLayout.onRefreshComplete();
        if ((SelectDishOrderResultDataSource.this.status != 40) || (SelectDishOrderResultDataSource.this.confirmQueryTimes <= 0))
          break label84;
        SelectDishOrderResultDataSource.this.handler.sendEmptyMessageDelayed(2, SelectDishOrderResultDataSource.this.confirmQueryInterval * 1000);
      }
      label84: 
      do
        return;
      while ((SelectDishOrderResultDataSource.this.status == 40) && (SelectDishOrderResultDataSource.this.confirmQueryTimes <= 0));
      if (SelectDishOrderResultDataSource.this.pendingRetry <= 0)
      {
        SelectDishOrderResultDataSource.this.dataLoaderListener.loadPayResultFail();
        return;
      }
      SelectDishOrderResultDataSource.this.handler.sendEmptyMessageDelayed(1, 3000L);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == SelectDishOrderResultDataSource.this.selectDishOrderResultReq)
      {
        if (SelectDishOrderResultDataSource.this.activity.selectDishOrderResultLoadedLayout != null)
          SelectDishOrderResultDataSource.this.activity.selectDishOrderResultLoadedLayout.onRefreshComplete();
        paramMApiResponse = (DPObject)paramMApiResponse.result();
        if ((paramMApiResponse.getInt("Status") != 10) || (SelectDishOrderResultDataSource.this.pendingRetry <= 0))
          break label84;
        SelectDishOrderResultDataSource.this.handler.sendEmptyMessageDelayed(1, 5000L);
      }
      label84: 
      do
      {
        return;
        if ((!SelectDishOrderResultDataSource.this.notFirstConfirm) || (paramMApiResponse.getInt("Status") != 40) || (SelectDishOrderResultDataSource.this.confirmQueryTimes <= 0))
          continue;
        SelectDishOrderResultDataSource.this.handler.removeMessages(2);
        SelectDishOrderResultDataSource.this.handler.sendEmptyMessageDelayed(2, SelectDishOrderResultDataSource.this.confirmQueryInterval * 1000);
        return;
      }
      while (SelectDishOrderResultDataSource.this.confirmQueryTimes <= 0);
      SelectDishOrderResultDataSource.this.onlinePayFlag = paramMApiResponse.getInt("OnlinePayFlag");
      SelectDishOrderResultDataSource.this.viewId = paramMApiResponse.getString("ViewId");
      SelectDishOrderResultDataSource.this.shopId = paramMApiResponse.getInt("ShopId");
      SelectDishOrderResultDataSource.this.shopName = paramMApiResponse.getString("ShopName");
      SelectDishOrderResultDataSource.this.tableNum = paramMApiResponse.getString("TableId");
      SelectDishOrderResultDataSource.this.barTitle = paramMApiResponse.getString("BarTitle");
      SelectDishOrderResultDataSource.this.mainTitle = paramMApiResponse.getString("Title");
      SelectDishOrderResultDataSource.this.subTitle = paramMApiResponse.getString("SubTitle");
      SelectDishOrderResultDataSource.this.status = paramMApiResponse.getInt("Status");
      SelectDishOrderResultDataSource.this.barCodeNo = paramMApiResponse.getString("BarCodeNo");
      SelectDishOrderResultDataSource.this.viewBarCodeNo = paramMApiResponse.getString("ViewBarCodeNo");
      SelectDishOrderResultDataSource.this.mainComment = paramMApiResponse.getString("BarCodeComments");
      SelectDishOrderResultDataSource.this.showOrderDetailFlag = paramMApiResponse.getInt("ShowOrderDetailFlag");
      SelectDishOrderResultDataSource.this.shopType = paramMApiResponse.getInt("ShopType");
      SelectDishOrderResultDataSource.this.barCodeFlag = paramMApiResponse.getInt("BarCodeFlag");
      SelectDishOrderResultDataSource.this.mainInfos = paramMApiResponse.getArray("MainInfos");
      SelectDishOrderResultDataSource.this.subInfos = paramMApiResponse.getArray("SubInfos");
      SelectDishOrderResultDataSource.this.orderEntries = paramMApiResponse.getArray("OrderEntries");
      SelectDishOrderResultDataSource.this.reviewInfo = paramMApiResponse.getObject("ReviewInfo");
      SelectDishOrderResultDataSource localSelectDishOrderResultDataSource = SelectDishOrderResultDataSource.this;
      if (SelectDishOrderResultDataSource.this.reviewInfo == null)
        paramMApiRequest = SelectDishOrderResultDataSource.ReviewStatus.HIDE;
      while (true)
      {
        localSelectDishOrderResultDataSource.reviewStatus = paramMApiRequest;
        SelectDishOrderResultDataSource.this.buttonList = paramMApiResponse.getStringArray("ButtonList");
        SelectDishOrderResultDataSource.this.payScheme = paramMApiResponse.getString("PayScheme");
        SelectDishOrderResultDataSource.this.showDelete = paramMApiResponse.getBoolean("ShowDelete");
        SelectDishOrderResultDataSource.this.groupOnInfo = paramMApiResponse.getObject("GroupOnInfo");
        SelectDishOrderResultDataSource.this.orderButtonSubTitle = paramMApiResponse.getString("OrderButtonSubTitle");
        if (SelectDishOrderResultDataSource.this.pendingRetry <= 0)
          SelectDishOrderResultDataSource.this.status = 30;
        if (SelectDishOrderResultDataSource.this.status == 40)
        {
          SelectDishOrderResultDataSource.access$002(SelectDishOrderResultDataSource.this, paramMApiResponse.getInt("QueryTimes"));
          SelectDishOrderResultDataSource.access$302(SelectDishOrderResultDataSource.this, paramMApiResponse.getInt("QueryInterval"));
          SelectDishOrderResultDataSource.this.notFirstConfirm = true;
          SelectDishOrderResultDataSource.this.handler.sendEmptyMessageDelayed(2, SelectDishOrderResultDataSource.this.confirmQueryInterval * 1000);
        }
        SelectDishOrderResultDataSource.this.dataLoaderListener.loadPayResult();
        return;
        if (SelectDishOrderResultDataSource.this.reviewInfo.getInt("Status") == 10)
        {
          paramMApiRequest = SelectDishOrderResultDataSource.ReviewStatus.OPEN;
          continue;
        }
        paramMApiRequest = SelectDishOrderResultDataSource.ReviewStatus.CLOSED;
      }
    }
  };
  boolean notFirstConfirm = false;
  public int onlinePayFlag;
  public String orderButtonSubTitle;
  public DPObject[] orderEntries;
  public int orderId;
  public String payScheme;
  public int pendingRetry = 19;
  public int queryType;
  public DPObject reviewInfo;
  public ReviewStatus reviewStatus;
  MApiRequest selectDishOrderResultReq;
  public int shopId;
  public String shopName;
  public int shopType;
  public boolean showDelete;
  public int showOrderDetailFlag;
  public int status;
  public DPObject[] subInfos;
  public String subTitle;
  public String tableNum;
  private String token;
  public String viewBarCodeNo;
  public String viewId;

  public SelectDishOrderResultDataSource(SelectDishOrderResultActivity paramSelectDishOrderResultActivity)
  {
    this.activity = paramSelectDishOrderResultActivity;
    if (TextUtils.isEmpty(paramSelectDishOrderResultActivity.accountService().token()));
    for (paramSelectDishOrderResultActivity = ""; ; paramSelectDishOrderResultActivity = paramSelectDishOrderResultActivity.accountService().token())
    {
      this.token = paramSelectDishOrderResultActivity;
      return;
    }
  }

  public void releaseRequests()
  {
    if (this.selectDishOrderResultReq != null)
    {
      this.activity.mapiService().abort(this.selectDishOrderResultReq, this.mapiHandler, true);
      this.selectDishOrderResultReq = null;
    }
    this.handler.removeMessages(1);
    this.handler.removeMessages(2);
  }

  public void requestPayResult()
  {
    if (this.selectDishOrderResultReq != null)
      this.activity.mapiService().abort(this.selectDishOrderResultReq, this.mapiHandler, true);
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/hobbits/queryorderdetail.hbt").buildUpon();
    if (this.activity.isLogined())
      localBuilder.appendQueryParameter("token", this.token);
    localBuilder.appendQueryParameter("orderid", String.valueOf(this.orderId));
    localBuilder.appendQueryParameter("querytype", String.valueOf(this.queryType));
    this.selectDishOrderResultReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    this.activity.mapiService().exec(this.selectDishOrderResultReq, this.mapiHandler);
  }

  public static enum ReviewStatus
  {
    static
    {
      CLOSED = new ReviewStatus("CLOSED", 3);
      $VALUES = new ReviewStatus[] { HIDE, OPEN, OPEN_IN_EDIT, CLOSED };
    }
  }

  public static abstract interface SelectDishOrderResultDataLoaderListener
  {
    public abstract void loadPayResult();

    public abstract void loadPayResultFail();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.entity.SelectDishOrderResultDataSource
 * JD-Core Version:    0.6.0
 */