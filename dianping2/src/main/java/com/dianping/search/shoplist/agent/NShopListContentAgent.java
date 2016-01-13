package com.dianping.search.shoplist.agent;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.shoplist.ShopListAdapter;
import com.dianping.base.shoplist.agent.ShopListContentAgent;
import com.dianping.base.shoplist.agentconfig.ShopListAgentConfig;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.shoplist.data.model.AdShopDataModel;
import com.dianping.search.shoplist.fragment.ShopListDataModelAdapter;
import com.dianping.search.shoplist.fragment.ShopListDataModelAdapter.AdapterItemClickListener;
import com.dianping.search.util.ShopListUtils;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;

public class NShopListContentAgent extends ShopListContentAgent
  implements ShopListDataModelAdapter.AdapterItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private NovaLinearLayout fab;
  private FrameLayout frameLayout;
  MApiRequest infoRequest;
  private int mBannerID = 0;
  private View.OnClickListener mClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      switch (NShopListContentAgent.this.mBannerID)
      {
      default:
        return;
      case 1:
        paramView = (NewShopListDataSource)NShopListContentAgent.this.getDataSource();
        NShopListContentAgent.this.getFragment().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView.weddingBanquetUrl)));
        return;
      case 2:
      }
      if (TextUtils.isEmpty(NShopListContentAgent.this.getToken()))
      {
        NShopListContentAgent.this.accountService().login(new LoginResultListener()
        {
          public void onLoginCancel(AccountService paramAccountService)
          {
          }

          public void onLoginSuccess(AccountService paramAccountService)
          {
            NShopListContentAgent.this.sendHospitalInfoRequest();
          }
        });
        return;
      }
      NShopListContentAgent.this.sendHospitalInfoRequest();
    }
  };
  private boolean showWeddingFab;

  public NShopListContentAgent(Object paramObject)
  {
    super(paramObject);
  }

  private String getToken()
  {
    if (accountService() == null)
      return "";
    return accountService().token();
  }

  private String getUserId()
  {
    if (accountService() == null)
      return "";
    return accountService().id() + "";
  }

  private void removeListView(ViewGroup paramViewGroup)
  {
    int j = paramViewGroup.getChildCount();
    int i = 0;
    while (true)
    {
      if (i < j)
      {
        View localView = paramViewGroup.getChildAt(i);
        if ((localView instanceof PullToRefreshListView))
          paramViewGroup.removeView(localView);
      }
      else
      {
        return;
      }
      i += 1;
    }
  }

  private void sendHospitalInfoRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/medicine/getconsulturl.bin?").buildUpon();
    localBuilder.appendQueryParameter("userid", getUserId());
    this.infoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.infoRequest, this);
  }

  protected void addCell()
  {
    ViewGroup localViewGroup = (ViewGroup)this.frameLayout.findViewById(R.id.content);
    removeListView(localViewGroup);
    this.shopListView.setCacheColorHint(0);
    localViewGroup.addView(this.shopListView);
    this.frameLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0F));
    addCell("050ShopList", this.frameLayout);
  }

  protected void addScrollGA(AbsListView paramAbsListView)
  {
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    NewShopListDataSource localNewShopListDataSource = (NewShopListDataSource)getDataSource();
    int i = 0;
    label119: String str;
    label159: label226: GAUserInfo localGAUserInfo;
    if (localNewShopListDataSource != null)
    {
      if (!TextUtils.isEmpty(localNewShopListDataSource.weddingBanquetTitle))
        i = 1;
    }
    else
    {
      if ((i == 0) || ((this.mBannerID != 0) && (this.mBannerID != i)))
      {
        if (this.fab != null)
          this.frameLayout.removeView(this.fab);
        this.fab = null;
        this.showWeddingFab = false;
      }
      this.mBannerID = i;
      if ((localNewShopListDataSource != null) && (this.mBannerID != 0))
      {
        if (this.fab == null)
        {
          if (this.mBannerID != 1)
            break label458;
          i = R.layout.search_hotel_shoplist;
          this.fab = ((NovaLinearLayout)LayoutInflater.from(getActivity()).inflate(i, null));
          if (this.mBannerID != 1)
            break label466;
          i = ViewUtils.dip2px(getActivity(), 75.0F);
          paramBundle = new FrameLayout.LayoutParams(-2, -2);
          paramBundle.setMargins(0, ViewUtils.dip2px(getActivity(), 350.0F), -i, 0);
          paramBundle.gravity = 5;
          this.fab.setLayoutParams(paramBundle);
          this.frameLayout.addView(this.fab);
        }
        if (this.mBannerID != 1)
          break label481;
        str = "wedding_quicksearch";
        localGAUserInfo = new GAUserInfo();
        localGAUserInfo.query_id = localNewShopListDataSource.queryId();
        paramBundle = localNewShopListDataSource.weddingBanquetTitle;
        if (this.mBannerID == 2)
        {
          paramBundle = localNewShopListDataSource.hospitalEntryTitle;
          if (localNewShopListDataSource.hospitalEntryTitle.length() > 2)
            paramBundle = new StringBuilder(localNewShopListDataSource.hospitalEntryTitle).insert(2, "\n").toString();
        }
        ((TextView)this.frameLayout.findViewById(R.id.name)).setText(paramBundle);
        this.fab.setGAString(str, localGAUserInfo);
        this.fab.setOnClickListener(this.mClickListener);
        if (!this.showWeddingFab)
        {
          this.showWeddingFab = true;
          if (this.mBannerID != 1)
            break label488;
        }
      }
    }
    label458: label466: label481: label488: for (i = ViewUtils.dip2px(getActivity(), 75.0F); ; i = ViewUtils.dip2px(getActivity(), 90.0F))
    {
      paramBundle = ObjectAnimator.ofFloat(this.fab, "translationX", new float[] { 0.0F, ViewUtils.dip2px(getActivity(), 25.0F) - i });
      paramBundle.setStartDelay(100L);
      paramBundle.setDuration(500L);
      paramBundle.start();
      GAHelper.instance().contextStatisticsEvent(getContext(), str, localGAUserInfo, "view");
      return;
      if (!TextUtils.isEmpty(localNewShopListDataSource.hospitalEntryTitle))
      {
        i = 2;
        break;
      }
      i = 0;
      break;
      i = R.layout.search_hospital_shoplist;
      break label119;
      i = ViewUtils.dip2px(getActivity(), 90.0F);
      break label159;
      str = "wenzhen";
      break label226;
    }
  }

  public void onClick(View paramView, Object paramObject)
  {
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        NShopListContentAgent.this.mOnScrollListener.onScrollStateChanged(NShopListContentAgent.this.shopListView, 0);
      }
    }
    , 500L);
  }

  public void onCreate(Bundle paramBundle)
  {
    this.frameLayout = ((FrameLayout)inflater().inflate(R.layout.search_shoplist_content, (ViewGroup)getFragment().contentView(), false));
    super.onCreate(paramBundle);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse == null) || (paramMApiResponse.result() == null));
    do
    {
      do
        return;
      while (this.infoRequest != paramMApiRequest);
      paramMApiRequest = (DPObject)paramMApiResponse.result();
    }
    while ((paramMApiRequest == null) || (TextUtils.isEmpty(paramMApiRequest.getString("ConsultUrl"))));
    startActivity(paramMApiRequest.getString("ConsultUrl"));
  }

  public void resetListView()
  {
    super.resetListView();
    this.shopListView.setDivider(null);
    this.shopListView.resetHeader(0);
    this.shopListView.setBackgroundResource(R.color.common_bk_color);
    this.shopListView.setPullRefreshEnable(2);
    this.shopListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
      {
        GAHelper.instance().contextStatisticsEvent(NShopListContentAgent.this.getContext(), "refreshed", "", -1, "tap");
        NShopListContentAgent.this.getDataSource().pullToRefresh(true);
      }
    });
  }

  protected void sendAdClientGA(int paramInt)
  {
    super.sendAdClientGA(paramInt);
    if ((paramInt == 1) && (((NewShopListDataSource)getDataSource()).adShopModels != null))
    {
      AdShopDataModel[] arrayOfAdShopDataModel = ((NewShopListDataSource)getDataSource()).adShopModels;
      int i = arrayOfAdShopDataModel.length;
      paramInt = 0;
      while (paramInt < i)
      {
        DPObject localDPObject = arrayOfAdShopDataModel[paramInt].shopObj;
        if (localDPObject.getBoolean("IsAdShop"))
        {
          AdClientUtils.sendAdGa(localDPObject, "1", String.valueOf(localDPObject.getInt("ListPosition") + 1));
          ShopListUtils.logAdGa(localDPObject, "1", String.valueOf(localDPObject.getInt("ListPosition") + 1));
        }
        paramInt += 1;
      }
    }
  }

  public void updateListView()
  {
    ShopListAdapter localShopListAdapter = getCurrentAgentConfig().getListAdapter(getActivity(), this);
    if (this.shopListAdapter != localShopListAdapter)
    {
      this.shopListAdapter = localShopListAdapter;
      this.shopListView.setOnItemClickListener(getCurrentAgentConfig().getOnItemClickListener());
      this.shopListAdapter.setShouldShowImage(NovaConfigUtils.isShowImageInMobileNetwork());
      if ((this.shopListAdapter instanceof ShopListDataModelAdapter))
        ((ShopListDataModelAdapter)this.shopListAdapter).setAdapterItemClickListener(this);
      this.shopListView.setAdapter(this.shopListAdapter);
      if ((getDataSource() != null) && (getDataSource().status() == 2))
      {
        sendAdClientGA(1);
        getDataSource().setReloadFlag(false);
        new Handler().postDelayed(new Runnable()
        {
          public void run()
          {
            NShopListContentAgent.this.mOnScrollListener.onScrollStateChanged(NShopListContentAgent.this.shopListView, 0);
          }
        }
        , 500L);
      }
    }
    while (true)
    {
      if ((getDataSource() != null) && ((getDataSource().status() == 2) || (getDataSource().nextStartIndex() == 0)))
      {
        if ((!(this.shopListAdapter instanceof ShopListDataModelAdapter)) || (!(getDataSource() instanceof NewShopListDataSource)))
          break;
        ((ShopListDataModelAdapter)this.shopListAdapter).setShopList((NewShopListDataSource)getDataSource());
      }
      return;
      if ((getDataSource() == null) || (getDataSource().status() != 2) || (!getDataSource().getReloadFlag()))
        continue;
      sendAdClientGA(1);
      getDataSource().setReloadFlag(false);
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          NShopListContentAgent.this.mOnScrollListener.onScrollStateChanged(NShopListContentAgent.this.shopListView, 0);
        }
      }
      , 500L);
    }
    this.shopListAdapter.setShopList(getDataSource());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.NShopListContentAgent
 * JD-Core Version:    0.6.0
 */