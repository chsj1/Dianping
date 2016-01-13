package com.dianping.ugc.review;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.widget.NovaListFragment;
import com.dianping.v1.R.layout;

public class CommendShopListFragment extends NovaListFragment
  implements LoginResultListener
{
  protected CommendShopListFragment.CommendShopListAdapter adapter;
  protected String source;

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.source = "commendshoplist";
    this.adapter = new CommendShopListFragment.CommendShopListAdapter(this);
    if (accountService().token() == null)
    {
      accountService().login(this);
      return;
    }
    setListAdapter(this.adapter);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.table_content_for_draft, paramViewGroup, false);
  }

  public void onDestroy()
  {
    super.onDestroy();
    accountService().removeLoginResultListener();
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    paramListView = this.adapter.getItem(paramInt);
    if ((paramListView instanceof DPObject))
    {
      paramListView = (DPObject)paramListView;
      AddReviewUtil.addReview(getActivity(), paramListView.getInt("ShopID"), paramListView.getString("ShopName"), this.source, paramListView.getString("ReferToken"));
      statisticsEvent("commendreview5", "commendreview5_item", paramListView.getString("ActionDesc") + "|" + paramListView.getInt("ShopID"), 0);
    }
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
    getActivity().finish();
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    setListAdapter(this.adapter);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.CommendShopListFragment
 * JD-Core Version:    0.6.0
 */