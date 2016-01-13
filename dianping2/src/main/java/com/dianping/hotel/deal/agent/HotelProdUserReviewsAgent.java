package com.dianping.hotel.deal.agent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.agent.TuanGroupCellAgent;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.base.widget.ReviewItem;
import com.dianping.base.widget.TableView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import java.util.Arrays;
import java.util.List;

public class HotelProdUserReviewsAgent extends TuanGroupCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  boolean bestReviewLoaded;
  private DPObject dpHotelProdBase;
  private DPObject dpHotelProdDetail;
  DPObject error;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if ((HotelProdUserReviewsAgent.this.dpHotelProdDetail != null) && (!TextUtils.isEmpty(HotelProdUserReviewsAgent.this.dpHotelProdDetail.getString("ReviewListUrl"))))
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(HotelProdUserReviewsAgent.this.dpHotelProdDetail.getString("ReviewListUrl")));
        HotelProdUserReviewsAgent.this.getContext().startActivity(paramView);
      }
    }
  };
  protected int productId;
  MApiRequest request;
  DPObject reviewList;
  int tagType;

  public HotelProdUserReviewsAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createBestReview(List<DPObject> paramList)
  {
    TableView localTableView = new TableView(getContext());
    localTableView.setBackgroundResource(R.drawable.table_view_item);
    int i = 0;
    while (true)
    {
      if ((i >= paramList.size()) || (i > 1))
        return localTableView;
      ReviewItem localReviewItem = (ReviewItem)this.res.inflate(getContext(), R.layout.hotel_review_item, getParentView(), false);
      localReviewItem.setReview((DPObject)paramList.get(i));
      localReviewItem.setReviewCountVisible(8);
      localReviewItem.setTag("hotel_review");
      localReviewItem.setOnClickListener(this.mListener);
      localTableView.addView(localReviewItem);
      i += 1;
    }
  }

  private void sendRequest()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("http://m.api.dianping.com/hotel/hotelreview.hotel?");
    localStringBuffer.append("dealgroupid=").append(this.productId);
    localStringBuffer.append("&start=0");
    localStringBuffer.append("&limit=2");
    localStringBuffer.append("&tab=2");
    localStringBuffer.append("&filterid=").append(0);
    localStringBuffer.append("&needfilter=").append(0);
    AccountService localAccountService = getFragment().accountService();
    if (localAccountService.token() != null)
      localStringBuffer.append("&token=").append(localAccountService.token());
    this.request = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.request, this);
  }

  private void updateView()
  {
    removeAllCells();
    if (this.reviewList == null)
      if ((this.error == null) && (this.request == null) && (!this.bestReviewLoaded))
        sendRequest();
    do
    {
      return;
      this.tagType = this.reviewList.getInt("TagType");
    }
    while ((this.reviewList.getArray("List") == null) || (this.reviewList.getArray("List").length <= 0));
    View localView = createBestReview(Arrays.asList(this.reviewList.getArray("List")));
    DealInfoCommonCell localDealInfoCommonCell = new DealInfoCommonCell(getContext());
    localDealInfoCommonCell.addContent(localView, false);
    localDealInfoCommonCell.setTitle("网友点评(" + this.reviewList.getInt("RecordCount") + ")", this.mListener);
    addCell("", localDealInfoCommonCell);
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
      this.dpHotelProdBase = ((DPObject)paramBundle.getParcelable("hotelprodbase"));
      this.dpHotelProdDetail = ((DPObject)paramBundle.getParcelable("hotelproddetail"));
    }
    while ((this.dpHotelProdBase == null) || (this.dpHotelProdDetail != null));
    this.productId = this.dpHotelProdBase.getInt("ProductId");
    updateView();
  }

  public void onDestroy()
  {
    if (this.request != null)
    {
      getFragment().mapiService().abort(this.request, this, true);
      this.request = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    if ((paramMApiResponse.error() instanceof DPObject));
    for (this.error = ((DPObject)paramMApiResponse.error()); ; this.error = new DPObject())
    {
      dispatchAgentChanged(false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.reviewList = ((DPObject)paramMApiResponse.result());
    this.error = null;
    this.bestReviewLoaded = true;
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdUserReviewsAgent
 * JD-Core Version:    0.6.0
 */