package com.dianping.base.tuan.agent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.tuan.utils.TuanSharedDataKey;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.base.tuan.widget.TuanDealBestReview;
import com.dianping.base.tuan.widget.TuanDealReviewItem;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.layout;
import java.util.Arrays;
import java.util.List;

public class DealInfoBestReviewAgent extends TuanGroupCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  boolean bestReviewLoaded;
  public DealInfoCommonCell commCell;
  protected int dealId;
  private DPObject dpDeal;
  DPObject error;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (DealInfoBestReviewAgent.this.dealId != 0)
      {
        if (DealInfoBestReviewAgent.this.reviewType == 1)
        {
          paramView = new StringBuilder("dianping://review?type=1");
          if (DealInfoBestReviewAgent.this.tagType != 0)
            paramView.append("&tagtype=").append(DealInfoBestReviewAgent.this.tagType);
          paramView.append("&dealid=" + DealInfoBestReviewAgent.this.dealId);
          DPObject localDPObject = DealInfoBestReviewAgent.this.getSharedDPObject(TuanSharedDataKey.DEAL_BEST_SHOP);
          if (localDPObject != null)
            paramView.append("&bestshopid=" + localDPObject.getInt("ID"));
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView.toString()));
          DealInfoBestReviewAgent.this.getContext().startActivity(paramView);
        }
      }
      else
        return;
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandealreviews?dealid=" + DealInfoBestReviewAgent.this.dealId));
      DealInfoBestReviewAgent.this.getContext().startActivity(paramView);
    }
  };
  MApiRequest request;
  DPObject reviewList;
  int reviewType;
  protected int shopId = 0;
  protected int status;
  int tagType;

  public DealInfoBestReviewAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createBestReview(List<DPObject> paramList)
  {
    TuanDealBestReview localTuanDealBestReview = new TuanDealBestReview(getContext());
    int i = 0;
    while (true)
    {
      if ((i >= paramList.size()) || (i > 1))
        return localTuanDealBestReview;
      TuanDealReviewItem localTuanDealReviewItem = (TuanDealReviewItem)this.res.inflate(getContext(), R.layout.tuan_deal_review_item, getParentView(), false);
      localTuanDealReviewItem.setReview((DPObject)paramList.get(i));
      localTuanDealReviewItem.setOnClickListener(this.mListener);
      localTuanDealReviewItem.setGAString("bestreview");
      ((NovaActivity)getContext()).addGAView(localTuanDealReviewItem, i, "tuandeal", "tuandeal".equals(((NovaActivity)getContext()).getPageName()));
      localTuanDealBestReview.addReview(localTuanDealReviewItem);
      i += 1;
    }
  }

  private void sendRequest()
  {
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("bestshopreviewgn.bin");
    localUrlBuilder.addParam("dealid", Integer.valueOf(this.dealId));
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    localUrlBuilder.addParam("shopid", Integer.valueOf(this.shopId));
    if (location() != null)
    {
      localUrlBuilder.addParam("lat", Double.valueOf(location().latitude()));
      localUrlBuilder.addParam("lng", Double.valueOf(location().longitude()));
    }
    this.request = BasicMApiRequest.mapiGet(localUrlBuilder.buildUrl(), CacheType.DISABLED);
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
      this.reviewType = this.reviewList.getInt("ReviewType");
    }
    while ((this.reviewList.getArray("List") == null) || (this.reviewList.getArray("List").length <= 0));
    View localView = createBestReview(Arrays.asList(this.reviewList.getArray("List")));
    this.commCell = new DealInfoCommonCell(getContext());
    this.commCell.setTitleSize(0, getResources().getDimension(R.dimen.deal_info_agent_title_text_size));
    this.commCell.setArrowPreSize(0, getResources().getDimension(R.dimen.deal_info_agent_subtitle_text_size));
    this.commCell.setPaddingLeft((int)getResources().getDimension(R.dimen.deal_info_padding_left));
    this.commCell.setPaddingRight((int)getResources().getDimension(R.dimen.deal_info_padding_right));
    this.commCell.addContent(localView, false);
    this.commCell.setTitle("网友点评(" + this.reviewList.getInt("TotalReview") + ")", this.mListener);
    if ((this.fragment instanceof GroupAgentFragment))
    {
      addCell("061BestReview.01BestReview0", this.commCell);
      return;
    }
    addCell("061BestReview.01BestReview0", this.commCell);
    addEmptyCell("061BestReview.01BestReview1");
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ((paramAgentMessage != null) && ("dealInfoShop".equals(paramAgentMessage.what)))
    {
      if ((paramAgentMessage.body == null) || (paramAgentMessage.body.getParcelable("shop") == null))
        break label71;
      this.shopId = ((DPObject)paramAgentMessage.body.getParcelable("shop")).getInt("ID");
    }
    while (true)
    {
      dispatchAgentChanged(false);
      return;
      label71: if (this.shopId != 0)
        continue;
      this.shopId = -1;
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      this.dealId = paramBundle.getInt("dealid");
      this.shopId = paramBundle.getInt("shopid", 0);
      this.status = paramBundle.getInt("status");
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != paramBundle)
        this.dpDeal = paramBundle;
    }
    if ((this.status == 1) && (this.shopId != 0) && (this.dpDeal != null))
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
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (DPObjectUtils.isDPObjectof(paramMApiRequest, "BestShopTuanReviewList"))
      this.reviewList = paramMApiRequest;
    this.error = null;
    this.bestReviewLoaded = true;
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoBestReviewAgent
 * JD-Core Version:    0.6.0
 */