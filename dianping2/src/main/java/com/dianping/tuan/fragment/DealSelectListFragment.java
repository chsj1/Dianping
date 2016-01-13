package com.dianping.tuan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.drawable;
import java.util.ArrayList;
import java.util.Arrays;

public class DealSelectListFragment extends BaseTuanListFragment
{
  protected ArrayList<DPObject> dealSelectList = new ArrayList();
  protected DPObject mDeal;
  protected DPObject mExtraData;

  public static DealSelectListFragment newInstance(FragmentActivity paramFragmentActivity, DPObject paramDPObject1, DPObject paramDPObject2)
  {
    DealSelectListFragment localDealSelectListFragment = new DealSelectListFragment();
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("deal", paramDPObject1);
    localBundle.putParcelable("extradata", paramDPObject2);
    localDealSelectListFragment.setArguments(localBundle);
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localDealSelectListFragment);
    paramFragmentActivity.setTransition(4097);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commit();
    return localDealSelectListFragment;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    getListView().setPadding(10, 10, 10, 10);
    getListView().setHeaderDividersEnabled(false);
    getListView().setBackgroundResource(R.drawable.main_background);
    getActivity().setTitle("选择套餐");
    ((NovaActivity)getActivity()).removeTitleBarShadow();
    ((NovaActivity)getActivity()).addTitleBarShadow();
    setListAdapter(new DealSelectListFragment.Adapter(this, null));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mDeal = ((DPObject)getArguments().getParcelable("deal"));
    if (this.mDeal == null)
    {
      getFragmentManager().popBackStackImmediate();
      return;
    }
    this.dealSelectList.addAll(Arrays.asList(this.mDeal.getArray("DealSelectList")));
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    paramListView = paramListView.getItemAtPosition(paramInt);
    if ((paramListView instanceof DPObject))
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
      paramView.addFlags(67108864);
      paramView.putExtra("deal", this.mDeal);
      paramView.putExtra("dealSelect", (DPObject)paramListView);
      paramView.putExtra("extradata", this.mExtraData);
      startActivityForResult(paramView, 1);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.DealSelectListFragment
 * JD-Core Version:    0.6.0
 */