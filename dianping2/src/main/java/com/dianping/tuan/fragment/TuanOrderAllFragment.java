package com.dianping.tuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.tuan.adapter.TuanOrderAdapter;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

@Deprecated
public class TuanOrderAllFragment extends TuanOrderBaseFragment
  implements AdapterView.OnItemClickListener
{
  private DPObject dpSelectedOrder;

  protected void handleOrderClicked(DPObject paramDPObject, int paramInt)
  {
    if (this.mAdapter.getItemViewType(paramInt) == 4)
    {
      if (goToCreateOrder(paramDPObject))
        this.dpSelectedOrder = paramDPObject;
      return;
    }
    goToOrderDetail(paramDPObject);
  }

  public void initAdapter()
  {
    if (this.mAdapter == null)
      this.mAdapter = new TuanOrderAllFragment.TuanOrderAllAdapter(this, (DPActivity)getActivity(), 0);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    this.listView.setOnItemClickListener(this);
    super.onActivityCreated(paramBundle);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1))
      this.mAdapter.remove(this.dpSelectedOrder);
  }

  public void showEditView(boolean paramBoolean)
  {
    super.showEditView(paramBoolean);
    if ((paramBoolean) && (isAdded()))
      Toast.makeText(getActivity(), "只能删除待付款，已消费，已发货和已退款的订单哦！", 0).show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.TuanOrderAllFragment
 * JD-Core Version:    0.6.0
 */