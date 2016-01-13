package com.dianping.base.tuan.agent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.OtherDealListItem;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DealInfoOtherDealsAgent extends TuanGroupCellAgent
  implements TuanGroupCellAgent.OnCellRefreshListener, RequestHandler<MApiRequest, MApiResponse>
{
  protected static final int OTHER_DEAL_ITEM = 1000;
  protected DealInfoCommonCell commCell;
  protected int dealId;
  protected DPObject dpDeal;
  protected DPObject[] dpOtherDeals;
  protected TextView header;
  protected double latitude;
  protected double longitude;
  protected double offsetLatitude;
  protected double offsetLongitude;
  protected TableView otherDealsLayout;
  protected boolean otherDealsLoaded;
  protected MApiRequest otherDealsReq;
  protected String queryId;
  protected boolean shouldShowImage;

  public DealInfoOtherDealsAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected void loadOtherDeals()
  {
    if ((this.otherDealsReq != null) || (this.otherDealsLoaded))
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("seeagaindealsgn.bin");
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    localUrlBuilder.addParam("dealid", Integer.valueOf(this.dealId));
    String str = accountService().token();
    if (!TextUtils.isEmpty(str))
      localUrlBuilder.addParam("token", str);
    if (location() != null)
    {
      localUrlBuilder.addParam("lat", Double.valueOf(location().latitude()));
      localUrlBuilder.addParam("lng", Double.valueOf(location().longitude()));
    }
    this.otherDealsReq = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.otherDealsReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    int i = 1;
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      this.dealId = paramBundle.getInt("dealid");
      DPObject localDPObject = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != localDPObject)
        this.dpDeal = localDPObject;
    }
    if (getContext() == null)
      return;
    if ((paramBundle != null) && (paramBundle.getInt("status") == 1));
    while (true)
    {
      if (i != 0)
        loadOtherDeals();
      if (this.otherDealsLayout == null)
        setupView();
      updateView();
      return;
      i = 0;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      localObject = paramBundle.getParcelableArrayList("OtherDeals");
      if (localObject != null)
        break label70;
    }
    label70: for (Object localObject = null; ; localObject = (DPObject[])((List)localObject).toArray(new DPObject[((List)localObject).size()]))
    {
      this.dpOtherDeals = ((DPObject)localObject);
      this.otherDealsLoaded = paramBundle.getBoolean("otherDealsLoaded");
      this.shouldShowImage = NovaConfigUtils.isShowImageInMobileNetwork();
      paramBundle = location();
      if (paramBundle != null)
      {
        this.offsetLatitude = paramBundle.offsetLatitude();
        this.offsetLongitude = paramBundle.offsetLongitude();
      }
      return;
    }
  }

  public void onRefresh()
  {
    if (this.otherDealsReq != null)
    {
      mapiService().abort(this.otherDealsReq, this, true);
      this.otherDealsReq = null;
    }
    this.otherDealsLoaded = false;
    loadOtherDeals();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.otherDealsReq == paramMApiRequest)
      this.otherDealsReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.otherDealsReq == paramMApiRequest)
    {
      this.otherDealsReq = null;
      this.otherDealsLoaded = true;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      this.dpOtherDeals = paramMApiRequest.getArray("List");
      this.queryId = paramMApiRequest.getString("QueryID");
      dispatchAgentChanged(false);
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = new Bundle();
    if (this.dpOtherDeals != null)
      localBundle.putParcelableArrayList("OtherDeals", new ArrayList(Arrays.asList(this.dpOtherDeals)));
    localBundle.putBoolean("otherDealsLoaded", this.otherDealsLoaded);
    return localBundle;
  }

  protected void setupView()
  {
    this.header = createGroupHeaderCell();
    this.otherDealsLayout = ((TableView)this.res.inflate(getContext(), R.layout.tuan_deal_more, getParentView(), false));
    this.commCell = new DealInfoCommonCell(getContext());
    this.commCell.setTitleSize(0, getResources().getDimension(R.dimen.deal_info_agent_title_text_size));
    this.commCell.setArrowPreSize(0, getResources().getDimension(R.dimen.deal_info_agent_subtitle_text_size));
    this.commCell.setPaddingLeft((int)getResources().getDimension(R.dimen.deal_info_padding_left));
    this.commCell.setPaddingRight((int)getResources().getDimension(R.dimen.deal_info_padding_right));
    this.commCell.addContent(this.otherDealsLayout, false);
  }

  protected void updateView()
  {
    removeAllCells();
    if ((this.dpOtherDeals != null) && (this.dpOtherDeals.length > 0))
    {
      this.otherDealsLayout.setAdapter(new MyAdapter(this.dpOtherDeals));
      this.otherDealsLayout.setOnItemClickListener(new TableView.OnItemClickListener()
      {
        public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
        {
          paramTableView = DealInfoOtherDealsAgent.this.dpOtherDeals[paramInt];
          if (!DealInfoOtherDealsAgent.this.handleAction(1000))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
            paramView.putExtra("deal", paramTableView);
            DealInfoOtherDealsAgent.this.getContext().startActivity(paramView);
          }
        }
      });
      this.commCell.hideArrow();
      this.commCell.setTitle("小伙伴们还看了");
      if ((this.fragment instanceof GroupAgentFragment))
        addCell("070OtherDeals.01OtherDeals0", this.commCell);
    }
    else
    {
      return;
    }
    addCell("070OtherDeals.01OtherDeals0", this.commCell);
    addEmptyCell("070OtherDeals.01OtherDeals1");
  }

  class MyAdapter extends BasicAdapter
  {
    private DPObject[] deals;

    public MyAdapter(DPObject[] arg2)
    {
      Object localObject;
      this.deals = localObject;
    }

    public int getCount()
    {
      return this.deals.length;
    }

    public Object getItem(int paramInt)
    {
      return this.deals[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject1 = null;
      Object localObject2 = null;
      Object localObject3 = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(localObject3, "Deal"))
      {
        localObject1 = localObject2;
        if ((paramView instanceof OtherDealListItem))
          localObject1 = (OtherDealListItem)paramView;
        paramView = (View)localObject1;
        if (localObject1 == null)
          paramView = (OtherDealListItem)LayoutInflater.from(DealInfoOtherDealsAgent.this.getContext()).inflate(R.layout.other_deal_list_item, paramViewGroup, false);
        paramViewGroup = (DPObject)localObject3;
        paramView.setDeal(paramViewGroup, DealInfoOtherDealsAgent.this.offsetLatitude, DealInfoOtherDealsAgent.this.offsetLongitude, DealInfoOtherDealsAgent.this.shouldShowImage, 1);
        paramView.setClickable(true);
        paramView.gaUserInfo.index = Integer.valueOf(paramInt);
        paramView.gaUserInfo.dealgroup_id = Integer.valueOf(paramViewGroup.getInt("ID"));
        paramView.gaUserInfo.query_id = DealInfoOtherDealsAgent.this.queryId;
        paramView.setGAString("recdeal");
        ((NovaActivity)DealInfoOtherDealsAgent.this.getContext()).addGAView(paramView, paramInt, "tuandeal", "tuandeal".equals(((NovaActivity)DealInfoOtherDealsAgent.this.getContext()).getPageName()));
        localObject1 = paramView;
      }
      return (View)localObject1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoOtherDealsAgent
 * JD-Core Version:    0.6.0
 */