package com.dianping.tuan.fragment;

import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

@Deprecated
public class TuanOrderPaidFragment extends TuanOrderBaseFragment
  implements AdapterView.OnItemClickListener
{
  protected void handleOrderClicked(DPObject paramDPObject, int paramInt)
  {
    goToOrderDetail(paramDPObject);
  }

  public void initAdapter()
  {
    if (this.mAdapter == null)
      this.mAdapter = new TuanOrderPaidFragment.TuanOrderPaidAdapter(this, (DPActivity)getActivity(), 2);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    this.listView.setOnItemClickListener(this);
    super.onActivityCreated(paramBundle);
  }

  public void showEditView(boolean paramBoolean)
  {
    super.showEditView(paramBoolean);
    if ((paramBoolean) && (isAdded()))
      Toast.makeText(getActivity(), "只能删除待付款，已消费，已发货和已退款的订单哦！", 0).show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.TuanOrderPaidFragment
 * JD-Core Version:    0.6.0
 */