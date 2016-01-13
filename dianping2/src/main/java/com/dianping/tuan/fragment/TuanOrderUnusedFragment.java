package com.dianping.tuan.fragment;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

@Deprecated
public class TuanOrderUnusedFragment extends TuanOrderBaseFragment
{
  public static final String UPDATE_ORDER_LIST = "com.dianping.action.UPDATE_ORDER_UNUSED_LIST";
  private IntentFilter mFilter;
  private BroadcastReceiver mReceiver = new TuanOrderUnusedFragment.1(this);

  protected void handleOrderClicked(DPObject paramDPObject, int paramInt)
  {
    goToOrderDetail(paramDPObject);
  }

  public void initAdapter()
  {
    if (this.mAdapter == null)
      this.mAdapter = new TuanOrderUnusedFragment.TuanOrderUnusedAdapter(this, (DPActivity)getActivity(), 4);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    this.listView.setOnItemClickListener(this);
    super.onActivityCreated(paramBundle);
  }

  public void onCreate(Bundle paramBundle)
  {
    this.mFilter = new IntentFilter();
    this.mFilter.addAction("com.dianping.action.UPDATE_ORDER_UNUSED_LIST");
    registerReceiver(this.mReceiver, this.mFilter);
    super.onCreate(paramBundle);
  }

  public void onDestroy()
  {
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    super.onDestroy();
  }

  public void showEditView(boolean paramBoolean)
  {
    super.showEditView(paramBoolean);
    if ((paramBoolean) && (isAdded()))
      Toast.makeText(getActivity(), "不能删除未消费的订单哦！", 0).show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.TuanOrderUnusedFragment
 * JD-Core Version:    0.6.0
 */