package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.DealListItem;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.tuan.widget.TuanScenarioPastChoiceItem;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import java.util.ArrayList;
import java.util.Arrays;

public class TuanScenarioRankingActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static final Object ITEM_CELL_TITLE = new Object();
  protected ScenarioRankingListAdapter adapter;
  protected Context context;
  protected DPObject dpCellResult;
  protected DPObject dpRecommendResult;
  protected DPObject dpScenarioRankingResult;
  protected int eventsceneid = 1;
  protected ListView lvScenarioRanking;
  protected MApiRequest scenarioRankingRequest;
  protected int tabamount = 0;
  protected String title = "";
  protected View vError;
  protected LinearLayout vHeader;
  protected View vLoading;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.context = this;
    paramBundle = getIntent().getData();
    if (paramBundle.getQueryParameter("eventsceneid") == null)
      return;
    this.eventsceneid = Integer.parseInt(paramBundle.getQueryParameter("eventsceneid"));
    if (paramBundle.getQueryParameter("tabamount") == null);
    for (this.tabamount = 0; ; this.tabamount = Integer.parseInt(paramBundle.getQueryParameter("tabamount")))
    {
      this.title = paramBundle.getQueryParameter("title");
      if (!TextUtils.isEmpty(this.title))
        setTitle(this.title);
      setContentView(R.layout.tuan_scenario_ranking_activity);
      this.vLoading = findViewById(R.id.loading);
      this.vError = findViewById(R.id.error);
      this.lvScenarioRanking = ((ListView)findViewById(R.id.scenario_ranking_list));
      requestScenarioRanking();
      return;
    }
  }

  protected void onDestroy()
  {
    if (this.scenarioRankingRequest != null)
    {
      mapiService().abort(this.scenarioRankingRequest, this, true);
      this.scenarioRankingRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.scenarioRankingRequest)
    {
      this.vLoading.setVisibility(8);
      paramMApiResponse = paramMApiResponse.content();
      paramMApiRequest = paramMApiResponse;
      if (TextUtils.isEmpty(paramMApiResponse))
        paramMApiRequest = "请求失败，请稍后再试";
      this.vError.setVisibility(0);
      if ((this.vError instanceof LoadingErrorView))
        ((LoadingErrorView)this.vError).setCallBack(new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            TuanScenarioRankingActivity.this.vLoading.setVisibility(0);
            TuanScenarioRankingActivity.this.vError.setVisibility(8);
            TuanScenarioRankingActivity.this.requestScenarioRanking();
          }
        });
      ((TextView)this.vError.findViewById(16908308)).setText(paramMApiRequest);
      this.scenarioRankingRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.scenarioRankingRequest)
    {
      this.vLoading.setVisibility(8);
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "ScenarioRankingResult"))
      {
        this.dpScenarioRankingResult = ((DPObject)paramMApiResponse);
        setupScenarioRanking();
      }
      this.scenarioRankingRequest = null;
    }
  }

  protected void requestScenarioRanking()
  {
    if (this.scenarioRankingRequest != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://app.t.dianping.com/");
    localStringBuilder.append("scenariorankinggn.bin");
    localStringBuilder.append("?eventsceneid=" + this.eventsceneid);
    if (this.tabamount > 0)
      localStringBuilder.append("&tabamount=" + this.tabamount);
    localStringBuilder.append("&cityid=" + city().id());
    if (isLogined())
      localStringBuilder.append("&token=" + accountService().token());
    if (location() != null)
    {
      localStringBuilder.append("&lat=" + location().latitude());
      localStringBuilder.append("&lng=" + location().longitude());
    }
    localStringBuilder.append("&dpid=").append(preferences(this.context).getString("dpid", ""));
    this.scenarioRankingRequest = new BasicMApiRequest(localStringBuilder.toString(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.scenarioRankingRequest, this);
    this.vLoading.setVisibility(0);
  }

  protected void setupScenarioRanking()
  {
    if (this.dpScenarioRankingResult == null)
      return;
    this.dpRecommendResult = this.dpScenarioRankingResult.getObject("RecommendResult");
    this.dpCellResult = this.dpScenarioRankingResult.getObject("CellList");
    int i = 1;
    if (this.dpRecommendResult == null)
    {
      i = 0;
      j = 1;
      if (this.dpCellResult != null)
        break label165;
    }
    for (int j = 0; ; j = 0)
      label165: 
      do
      {
        if ((i != 0) || (j != 0))
          break label197;
        this.vError.setVisibility(0);
        if ((this.vError instanceof LoadingErrorView))
          ((LoadingErrorView)this.vError).setCallBack(new LoadingErrorView.LoadRetry()
          {
            public void loadRetry(View paramView)
            {
              TuanScenarioRankingActivity.this.vLoading.setVisibility(0);
              TuanScenarioRankingActivity.this.vError.setVisibility(8);
              TuanScenarioRankingActivity.this.requestScenarioRanking();
            }
          });
        ((TextView)this.vError.findViewById(16908308)).setText("您请求的数据不存在，请稍后再试");
        return;
        if ((this.dpRecommendResult.getObject("DealList").getArray("List") != null) && (this.dpRecommendResult.getObject("DealList").getArray("List").length != 0))
          break;
        i = 0;
        break;
      }
      while ((this.dpCellResult.getArray("List") != null) && (this.dpCellResult.getArray("List").length != 0));
    label197: if ((this.lvScenarioRanking.getHeaderViewsCount() == 0) && (this.dpRecommendResult != null) && (!TextUtils.isEmpty(this.dpRecommendResult.getString("ImageUrl"))))
    {
      this.vHeader = ((LinearLayout)LayoutInflater.from(this.context).inflate(R.layout.tuanscenario_headimage_view, null));
      ((NetworkImageView)this.vHeader.findViewById(R.id.tuanscenario_image)).setImage(this.dpRecommendResult.getString("ImageUrl"));
      this.lvScenarioRanking.addHeaderView(this.vHeader);
    }
    this.lvScenarioRanking.setDivider(getResources().getDrawable(R.drawable.list_divider_right_inset));
    this.adapter = new ScenarioRankingListAdapter();
    this.lvScenarioRanking.setAdapter(this.adapter);
    this.adapter.setData();
  }

  protected class ScenarioRankingListAdapter extends BasicAdapter
  {
    protected ArrayList<DPObject> dpPastChoiceList = new ArrayList();
    protected ArrayList<DPObject> dpRecommendDealList = new ArrayList();

    protected ScenarioRankingListAdapter()
    {
    }

    public int getCount()
    {
      if (this.dpRecommendDealList.size() == 0)
      {
        if (this.dpPastChoiceList.size() == 0)
          return 0;
        return this.dpPastChoiceList.size() + 1;
      }
      if (this.dpPastChoiceList.size() == 0)
        return this.dpRecommendDealList.size();
      return this.dpRecommendDealList.size() + 1 + this.dpPastChoiceList.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.dpRecommendDealList.size())
        return this.dpRecommendDealList.get(paramInt);
      if (paramInt == this.dpRecommendDealList.size())
        return TuanScenarioRankingActivity.ITEM_CELL_TITLE;
      return this.dpPastChoiceList.get(paramInt - this.dpRecommendDealList.size() - 1);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      DPObject localDPObject;
      if (TuanScenarioRankingActivity.this.isDPObjectof(localObject, "Deal"))
      {
        localDPObject = (DPObject)localObject;
        if ((paramView instanceof DealListItem))
        {
          paramView = (DealListItem)paramView;
          localObject = paramView;
          if (paramView == null)
            localObject = (DealListItem)LayoutInflater.from(TuanScenarioRankingActivity.this.context).inflate(R.layout.deal_list_item, paramViewGroup, false);
          if (TuanScenarioRankingActivity.this.location() == null)
            break label143;
          ((DealListItem)localObject).setDeal(localDPObject, TuanScenarioRankingActivity.this.location().offsetLatitude(), TuanScenarioRankingActivity.this.location().offsetLongitude(), NovaConfigUtils.isShowImageInMobileNetwork(), 1, true, paramInt + 1);
        }
        while (true)
        {
          ((DealListItem)localObject).setTag(localDPObject);
          ((DealListItem)localObject).setOnClickListener(new TuanScenarioRankingActivity.ScenarioRankingListAdapter.1(this));
          return localObject;
          paramView = null;
          break;
          label143: ((DealListItem)localObject).setDeal(localDPObject, 0.0D, 0.0D, NovaConfigUtils.isShowImageInMobileNetwork(), 1, true, paramInt + 1);
        }
      }
      if (localObject == TuanScenarioRankingActivity.ITEM_CELL_TITLE)
      {
        paramView = LayoutInflater.from(TuanScenarioRankingActivity.this.context).inflate(R.layout.tuanscenario_title_cell, paramViewGroup, false);
        paramViewGroup = (TextView)paramView.findViewById(R.id.title);
        localObject = TuanScenarioRankingActivity.this.dpCellResult.getString("Title");
        if (!TextUtils.isEmpty((CharSequence)localObject))
          paramViewGroup.setText((CharSequence)localObject);
        return paramView;
      }
      if (TuanScenarioRankingActivity.this.isDPObjectof(localObject, "Cell"))
      {
        localDPObject = (DPObject)localObject;
        if ((paramView instanceof TuanScenarioPastChoiceItem));
        for (paramView = (TuanScenarioPastChoiceItem)paramView; ; paramView = null)
        {
          localObject = paramView;
          if (paramView == null)
            localObject = (TuanScenarioPastChoiceItem)LayoutInflater.from(TuanScenarioRankingActivity.this.context).inflate(R.layout.tuan_scenario_ranking_past_choice_item, paramViewGroup, false);
          ((TuanScenarioPastChoiceItem)localObject).setPastChoiceItem(localDPObject);
          ((TuanScenarioPastChoiceItem)localObject).setTag(localDPObject);
          ((TuanScenarioPastChoiceItem)localObject).setOnClickListener(new TuanScenarioRankingActivity.ScenarioRankingListAdapter.2(this));
          return localObject;
        }
      }
      return (View)null;
    }

    public void setData()
    {
      this.dpRecommendDealList.clear();
      if ((TuanScenarioRankingActivity.this.dpRecommendResult != null) && (TuanScenarioRankingActivity.this.dpRecommendResult.getObject("DealList").getArray("List") != null))
        this.dpRecommendDealList.addAll(Arrays.asList(TuanScenarioRankingActivity.this.dpRecommendResult.getObject("DealList").getArray("List")));
      this.dpPastChoiceList.clear();
      if ((TuanScenarioRankingActivity.this.dpCellResult != null) && (TuanScenarioRankingActivity.this.dpCellResult.getArray("List") != null))
        this.dpPastChoiceList.addAll(Arrays.asList(TuanScenarioRankingActivity.this.dpCellResult.getArray("List")));
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanScenarioRankingActivity
 * JD-Core Version:    0.6.0
 */