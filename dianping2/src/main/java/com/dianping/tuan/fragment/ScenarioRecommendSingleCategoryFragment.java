package com.dianping.tuan.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.fragment.BaseTuanFragment;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;

public class ScenarioRecommendSingleCategoryFragment extends BaseTuanFragment
{
  public static final Object MORE_SCENARIORECOMMEND = new Object();
  private ScenarioRecommendSingleCategoryFragment.RecommendDealListAdapter adapter;
  private DPObject dpRecommendResult = new DPObject();
  private ListView lvRecommendDeal;
  private int tabIndex = 0;
  private LinearLayout vHeader;

  public static ScenarioRecommendSingleCategoryFragment newInstance(FragmentActivity paramFragmentActivity, DPObject paramDPObject, int paramInt)
  {
    paramFragmentActivity = new ScenarioRecommendSingleCategoryFragment();
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("recommendresult", paramDPObject);
    localBundle.putInt("tabindex", paramInt);
    paramFragmentActivity.setArguments(localBundle);
    return paramFragmentActivity;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.dpRecommendResult != null)
      setRecommendDeals(this.dpRecommendResult, this.tabIndex);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getArguments() != null)
    {
      if (getArguments().getParcelable("recommendresult") != null)
        this.dpRecommendResult = ((DPObject)getArguments().getParcelable("recommendresult"));
      if (getArguments().getInt("tabindex") >= 0)
        this.tabIndex = getArguments().getInt("tabindex");
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.tuan_scenariorecommend_singlecategory_fragment, paramViewGroup, false);
    this.lvRecommendDeal = ((ListView)paramLayoutInflater.findViewById(R.id.recommend_deal_list));
    return paramLayoutInflater;
  }

  public void setRecommendDeals(DPObject paramDPObject, int paramInt)
  {
    if ((paramDPObject == null) || (paramDPObject.getObject("DealList") == null) || (paramDPObject.getObject("DealList").getArray("List").length == 0))
      return;
    this.dpRecommendResult = paramDPObject;
    if ((this.lvRecommendDeal.getHeaderViewsCount() == 0) && (!TextUtils.isEmpty(this.dpRecommendResult.getString("ImageUrl"))))
    {
      this.vHeader = ((LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.tuanscenario_headimage_view, null));
      ((NetworkImageView)this.vHeader.findViewById(R.id.tuanscenario_image)).setImage(this.dpRecommendResult.getString("ImageUrl"));
      this.lvRecommendDeal.addHeaderView(this.vHeader);
    }
    this.lvRecommendDeal.setDivider(getResources().getDrawable(R.drawable.list_divider_right_inset));
    this.adapter = new ScenarioRecommendSingleCategoryFragment.RecommendDealListAdapter(this, null);
    this.lvRecommendDeal.setAdapter(this.adapter);
    this.adapter.setData();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.ScenarioRecommendSingleCategoryFragment
 * JD-Core Version:    0.6.0
 */