package com.dianping.tuan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.mapi.MApiDebugAgent;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

@Deprecated
public class TuanOrderLotteryFragment extends TuanOrderBaseFragment
  implements AdapterView.OnItemClickListener
{
  private MApiDebugAgent mApiDebugAgent;

  protected void handleOrderClicked(DPObject paramDPObject, int paramInt)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://orderdetail"));
    localIntent.putExtra("order", paramDPObject);
    startActivity(localIntent);
  }

  public void initAdapter()
  {
    if (this.mAdapter == null)
      this.mAdapter = new TuanOrderLotteryFragment.TuanOrderLotteryAdapter(this, (DPActivity)getActivity(), 3);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    this.listView.setOnItemClickListener(this);
    this.mApiDebugAgent = ((MApiDebugAgent)getService("mapi_debug"));
    super.onActivityCreated(paramBundle);
  }

  public void showEditView(boolean paramBoolean)
  {
    super.showEditView(paramBoolean);
    if ((paramBoolean) && (isAdded()))
      Toast.makeText(getActivity(), "只能删除已结束的抽奖单哦！", 0).show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.TuanOrderLotteryFragment
 * JD-Core Version:    0.6.0
 */