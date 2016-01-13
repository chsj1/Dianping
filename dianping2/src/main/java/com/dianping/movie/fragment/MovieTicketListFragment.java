package com.dianping.movie.fragment;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MovieTicketListFragment extends NovaFragment
  implements AdapterView.OnItemClickListener, PullToRefreshListView.OnRefreshListener, RequestHandler<MApiRequest, MApiResponse>
{
  private Context context;
  private Button deleteButton;
  private MApiRequest deleteOrdersRequest;
  private String deleteToast;
  private View deleteView;
  private LinearLayout emptyLayer = null;
  private FrameLayout emptyView;
  private int filter;
  private boolean isEdit;
  private OnTitleChangeListener listener;
  private BroadcastReceiver mReceiver = new MovieTicketListFragment.1(this);
  private Set<Integer> movieIds = new HashSet();
  private MovieTicketListFragment.MovieTicketListAdapter movieTicketAdapter;
  private PullToRefreshListView movieTicketListView;
  private ArrayList<String> orderIds = new ArrayList();
  private int recordCount;

  private void delOrders()
  {
    if (this.orderIds.size() == 0)
    {
      Toast.makeText(this.context, "请至少选择一项！", 0).show();
      return;
    }
    new AlertDialog.Builder(this.context).setMessage("确定删除选择的订单？").setPositiveButton("确认", new MovieTicketListFragment.4(this)).setNegativeButton("取消", new MovieTicketListFragment.3(this)).show();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.movieTicketAdapter = new MovieTicketListFragment.MovieTicketListAdapter(this, getActivity());
    this.movieTicketListView.setAdapter(this.movieTicketAdapter);
    paramBundle = getArguments();
    if (paramBundle != null)
      this.filter = paramBundle.getInt("filter");
    try
    {
      this.listener = ((OnTitleChangeListener)getActivity());
      label62: this.context = getActivity();
      paramBundle = new IntentFilter("com.dianping.movie.TICKET_ORDER_CHANGE");
      registerReceiver(this.mReceiver, paramBundle);
      paramBundle = new IntentFilter("com.dianping.movie.CREATE_ORDER");
      registerReceiver(this.mReceiver, paramBundle);
      paramBundle = new IntentFilter("com.dianping.movie.ORDER_STATUS_CHANGED");
      registerReceiver(this.mReceiver, paramBundle);
      paramBundle = new IntentFilter("movie:movie_comment_addormodified");
      registerReceiver(this.mReceiver, paramBundle);
      this.movieIds.clear();
      return;
    }
    catch (java.lang.Exception paramBundle)
    {
      break label62;
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.movie_ticket_list_fragment, paramViewGroup, false);
  }

  public void onDestroyView()
  {
    super.onDestroyView();
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.deleteOrdersRequest != null)
    {
      mapiService().abort(this.deleteOrdersRequest, this, true);
      this.deleteOrdersRequest = null;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramInt <= 0);
    while (true)
    {
      return;
      paramAdapterView = this.movieTicketAdapter.getItem(paramInt - 1);
      if (!DPObjectUtils.isDPObjectof(paramAdapterView, "MovieTicketOrder"))
        continue;
      paramView = (DPObject)paramAdapterView;
      if (!this.isEdit)
        break;
      if (!paramView.getBoolean("CanDelete"))
        continue;
      if (!this.orderIds.remove(paramView.getInt("ID") + ""))
        this.orderIds.add(paramView.getInt("ID") + "");
      this.movieTicketAdapter.notifyDataSetChanged();
      setDeleteButtonView();
      return;
    }
    if (paramView.getInt("OrderStatus") == 0)
    {
      Object localObject = paramView.getObject("PayProduct");
      if (paramView.getBoolean("GoNewPayOrder"))
      {
        paramAdapterView = "dianping://moviepayorder";
        paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse(paramAdapterView));
        paramAdapterView.putExtra("payproduct", (Parcelable)localObject);
        paramAdapterView.putExtra("orderid", paramView.getInt("ID") + "");
        paramAdapterView.putExtra("callbackurl", "dianping://purchasemovieticketresult");
        paramAdapterView.putExtra("callbackfailurl", "dianping://home");
        paramAdapterView.putExtra("movieticketorder", paramView);
        localObject = new Bundle();
        ((Bundle)localObject).putInt("shopid", paramView.getInt("ShopID"));
        ((Bundle)localObject).putInt("orderid", paramView.getInt("ID"));
        paramAdapterView.putExtra("payextra", (Bundle)localObject);
        startActivity(paramAdapterView);
      }
    }
    while (true)
    {
      statisticsEvent("movie5", "movie5_ticket_item", "" + paramInt, 0);
      return;
      paramAdapterView = "dianping://payorder";
      break;
      paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://movieticketdetail"));
      paramAdapterView.putExtra("movieticketorder", paramView);
      startActivity(paramAdapterView);
    }
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    if (paramPullToRefreshListView.getId() == R.id.movieticket_list)
      this.movieTicketAdapter.pullToReset(true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.deleteOrdersRequest)
    {
      this.deleteOrdersRequest = null;
      dismissDialog();
      paramMApiRequest = paramMApiResponse.message();
      paramMApiRequest = paramMApiRequest.title() + ":" + paramMApiRequest.content();
      Toast.makeText(this.context, paramMApiRequest, 0).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.deleteOrdersRequest)
    {
      dismissDialog();
      this.deleteOrdersRequest = null;
      this.movieTicketAdapter.removeOrders();
      this.orderIds.clear();
      this.isEdit = false;
      this.listener.onTitleChanged(this.recordCount, false);
      setIsEdit(false);
      Toast.makeText(this.context, "删除成功", 0).show();
    }
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.movieTicketListView = ((PullToRefreshListView)paramView.findViewById(R.id.movieticket_list));
    this.emptyView = ((FrameLayout)paramView.findViewById(R.id.movieticket_empty));
    this.movieTicketListView.setEmptyView(this.emptyView);
    this.movieTicketListView.setOnItemClickListener(this);
    this.movieTicketListView.setOnRefreshListener(this);
    this.movieTicketListView.setDivider(getResources().getDrawable(R.drawable.moviechannel_listview_divider_right_inset));
    this.deleteView = paramView.findViewById(R.id.delete_layout);
    this.deleteButton = ((Button)this.deleteView.findViewById(R.id.delete_btn));
    this.deleteButton.setOnClickListener(new MovieTicketListFragment.2(this));
  }

  public void setDeleteButtonView()
  {
    int i = this.orderIds.size();
    if (i > 0)
    {
      this.deleteButton.setText("删除(" + i + ")");
      return;
    }
    this.deleteButton.setText("删除");
  }

  public void setIsEdit(boolean paramBoolean)
  {
    this.isEdit = paramBoolean;
    if (this.recordCount > 0)
    {
      if (paramBoolean)
      {
        this.movieTicketListView.setMode(PullToRefreshBase.Mode.DISABLED);
        this.deleteView.setVisibility(0);
        setDeleteButtonView();
        if (!TextUtils.isEmpty(this.deleteToast))
          Toast.makeText(this.context, this.deleteToast, 0).show();
      }
      while (true)
      {
        this.movieTicketAdapter.notifyDataSetChanged();
        return;
        this.movieTicketListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        this.deleteView.setVisibility(8);
        this.orderIds.clear();
      }
    }
    this.deleteView.setVisibility(8);
    this.movieTicketListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
  }

  public static abstract interface OnTitleChangeListener
  {
    public abstract void onTitleChanged(int paramInt, boolean paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.fragment.MovieTicketListFragment
 * JD-Core Version:    0.6.0
 */