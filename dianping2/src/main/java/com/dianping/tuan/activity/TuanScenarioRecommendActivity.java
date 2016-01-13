package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.tuan.fragment.ScenarioRecommendMultipleCategoryFragment;
import com.dianping.tuan.fragment.ScenarioRecommendSingleCategoryFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;
import java.util.Arrays;

public class TuanScenarioRecommendActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected Context context;
  protected DPObject dpScenarioRecommend;
  protected int eventsceneid = 1;
  protected LinearLayout layerScenarioRecommend;
  protected ScenarioRecommendMultipleCategoryFragment multipleCategoryFragment;
  protected MApiRequest scenarioRecommendRequest;
  protected ScenarioRecommendSingleCategoryFragment singleCategoryFragment;
  protected int tabamount = 0;
  protected String title = "";
  protected View vError;
  protected View vLoading;

  protected void hideScenarioRecommend()
  {
    this.singleCategoryFragment.hideFragmentTitleBar();
    this.multipleCategoryFragment.hideFragmentTitleBar();
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.hide(this.singleCategoryFragment);
    localFragmentTransaction.hide(this.multipleCategoryFragment);
    localFragmentTransaction.commit();
    this.layerScenarioRecommend.setVisibility(8);
  }

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
      setContentView(R.layout.tuan_scenario_recommend_activity);
      this.vLoading = findViewById(R.id.loading);
      this.vError = findViewById(R.id.error);
      this.layerScenarioRecommend = ((LinearLayout)findViewById(R.id.layer_scenariorecommend));
      this.singleCategoryFragment = ((ScenarioRecommendSingleCategoryFragment)getSupportFragmentManager().findFragmentById(R.id.singlecategory_fragment));
      this.multipleCategoryFragment = ((ScenarioRecommendMultipleCategoryFragment)getSupportFragmentManager().findFragmentById(R.id.multiplecategory_fragment));
      hideScenarioRecommend();
      requestScenarioRecommend();
      return;
    }
  }

  protected void onDestroy()
  {
    if (this.scenarioRecommendRequest != null)
    {
      mapiService().abort(this.scenarioRecommendRequest, this, true);
      this.scenarioRecommendRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.scenarioRecommendRequest)
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
            TuanScenarioRecommendActivity.this.vLoading.setVisibility(0);
            TuanScenarioRecommendActivity.this.vError.setVisibility(8);
            TuanScenarioRecommendActivity.this.requestScenarioRecommend();
          }
        });
      ((TextView)this.vError.findViewById(16908308)).setText(paramMApiRequest);
      this.scenarioRecommendRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.scenarioRecommendRequest)
    {
      this.vLoading.setVisibility(8);
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "RecommendDealList"))
      {
        this.dpScenarioRecommend = ((DPObject)paramMApiResponse);
        setupScenarioRecommend();
      }
      this.scenarioRecommendRequest = null;
    }
  }

  protected void requestScenarioRecommend()
  {
    if (this.scenarioRecommendRequest != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://app.t.dianping.com/");
    localStringBuilder.append("scenariorecommendgn.bin");
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
    this.scenarioRecommendRequest = new BasicMApiRequest(localStringBuilder.toString(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.scenarioRecommendRequest, this);
    this.vLoading.setVisibility(0);
  }

  protected void setupScenarioRecommend()
  {
    if (this.dpScenarioRecommend == null)
      return;
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(Arrays.asList(this.dpScenarioRecommend.getArray("List")));
    if ((localArrayList == null) || (localArrayList.size() == 0))
    {
      this.vError.setVisibility(0);
      if ((this.vError instanceof LoadingErrorView))
        ((LoadingErrorView)this.vError).setCallBack(new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            TuanScenarioRecommendActivity.this.vLoading.setVisibility(0);
            TuanScenarioRecommendActivity.this.vError.setVisibility(8);
            TuanScenarioRecommendActivity.this.requestScenarioRecommend();
          }
        });
      ((TextView)this.vError.findViewById(16908308)).setText("您请求的数据不存在，请稍后再试");
      return;
    }
    if (localArrayList.size() == 1)
    {
      this.singleCategoryFragment.setRecommendDeals((DPObject)localArrayList.get(0), 0);
      showSingleCategory();
      return;
    }
    this.multipleCategoryFragment.setRecommendCategory(localArrayList);
    showMultipleCategory();
  }

  protected void showMultipleCategory()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.show(this.multipleCategoryFragment);
    localFragmentTransaction.hide(this.singleCategoryFragment);
    localFragmentTransaction.commitAllowingStateLoss();
    this.layerScenarioRecommend.setVisibility(0);
  }

  protected void showSingleCategory()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.show(this.singleCategoryFragment);
    localFragmentTransaction.hide(this.multipleCategoryFragment);
    localFragmentTransaction.commitAllowingStateLoss();
    this.layerScenarioRecommend.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanScenarioRecommendActivity
 * JD-Core Version:    0.6.0
 */