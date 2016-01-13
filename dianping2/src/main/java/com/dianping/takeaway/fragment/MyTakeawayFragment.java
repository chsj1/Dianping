package com.dianping.takeaway.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.NovaListFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.takeaway.view.OnDialogOperationListener;
import com.dianping.takeaway.view.TAOrderConfirmDialog;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MyTakeawayFragment extends NovaListFragment
  implements PullToRefreshListView.OnRefreshListener, AdapterView.OnItemClickListener
{
  protected String applyCancelDesc;
  protected MApiRequest confirmReceiptRequest;
  protected String dpPhone;
  protected String errorMsg;
  protected MApiRequest getMaxArrivalTimeRequest;
  protected MApiRequest getMyTakeawayListRequest;
  protected MApiRequest getSingleTakeawayItemRequest;
  protected boolean isEnd;
  private View loadingView;
  private IntentFilter mIntentFilter;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.takeaway.UPDATE_ORDER".equals(paramIntent.getAction()))
        MyTakeawayFragment.this.getSingleTakeawayItemTask(paramIntent.getStringExtra("orderviewid"));
    }
  };
  protected RequestHandler<MApiRequest, MApiResponse> mapiHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == MyTakeawayFragment.this.getMyTakeawayListRequest)
      {
        if (paramMApiResponse != null)
        {
          MyTakeawayFragment.this.errorMsg = paramMApiResponse.message().toString();
          MyTakeawayFragment.this.myAdapter.notifyDataSetChanged();
          MyTakeawayFragment.this.loadingView.setVisibility(8);
          MyTakeawayFragment.this.myTakeawayListView.setVisibility(0);
        }
        MyTakeawayFragment.this.getMyTakeawayListRequest = null;
      }
      do
      {
        return;
        if (paramMApiRequest == MyTakeawayFragment.this.confirmReceiptRequest)
        {
          if (paramMApiResponse != null)
            ((NovaActivity)MyTakeawayFragment.this.getActivity()).showMessageDialog(paramMApiResponse.message());
          MyTakeawayFragment.this.confirmReceiptRequest = null;
          return;
        }
        if (paramMApiRequest != MyTakeawayFragment.this.getSingleTakeawayItemRequest)
          continue;
        MyTakeawayFragment.this.showToast("信息更新失败");
        MyTakeawayFragment.this.getSingleTakeawayItemRequest = null;
        return;
      }
      while (paramMApiRequest != MyTakeawayFragment.this.getMaxArrivalTimeRequest);
      if (paramMApiResponse != null)
        ((NovaActivity)MyTakeawayFragment.this.getActivity()).showMessageDialog(paramMApiResponse.message());
      MyTakeawayFragment.this.getMaxArrivalTimeRequest = null;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == MyTakeawayFragment.this.getMyTakeawayListRequest)
        if (paramMApiResponse != null)
        {
          if ((paramMApiResponse.result() instanceof DPObject))
          {
            MyTakeawayFragment.this.myTakeawayListView.onRefreshComplete();
            MyTakeawayFragment.this.loadingView.setVisibility(8);
            MyTakeawayFragment.this.myTakeawayListView.setVisibility(0);
            MyTakeawayFragment.this.appendOrders((DPObject)paramMApiResponse.result());
          }
        }
        else
          MyTakeawayFragment.this.getMyTakeawayListRequest = null;
      int i;
      do
      {
        return;
        MyTakeawayFragment.this.errorMsg = paramMApiResponse.message().toString();
        MyTakeawayFragment.this.myAdapter.notifyDataSetChanged();
        break;
        if (paramMApiRequest == MyTakeawayFragment.this.confirmReceiptRequest)
        {
          if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
          {
            MyTakeawayFragment.this.showToast(((DPObject)paramMApiResponse.result()).getString("Content"));
            paramMApiRequest = paramMApiRequest.url();
            if (!TextUtils.isEmpty(paramMApiRequest))
            {
              paramMApiRequest = Uri.parse(paramMApiRequest).getQueryParameter("orderviewid");
              MyTakeawayFragment.this.getSingleTakeawayItemTask(paramMApiRequest);
              ((NovaActivity)MyTakeawayFragment.this.getActivity()).startActivity("dianping://takeawayreview?orderid=" + paramMApiRequest + "&shopname=" + MyTakeawayFragment.this.getShopNameByViewId(paramMApiRequest) + "&source=1");
            }
          }
          MyTakeawayFragment.this.confirmReceiptRequest = null;
          return;
        }
        if (paramMApiRequest != MyTakeawayFragment.this.getSingleTakeawayItemRequest)
          continue;
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          paramMApiResponse = paramMApiRequest.getString("OrderViewId");
          i = 0;
        }
        while (true)
        {
          if (i < MyTakeawayFragment.this.takeawayList.size())
          {
            if (paramMApiResponse.equals(((DPObject)MyTakeawayFragment.this.takeawayList.get(i)).getString("OrderViewId")))
              MyTakeawayFragment.this.takeawayList.set(i, paramMApiRequest);
          }
          else
          {
            MyTakeawayFragment.this.myAdapter.notifyDataSetChanged();
            MyTakeawayFragment.this.getSingleTakeawayItemRequest = null;
            return;
          }
          i += 1;
        }
      }
      while (paramMApiRequest != MyTakeawayFragment.this.getMaxArrivalTimeRequest);
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        paramMApiResponse = paramMApiRequest.getString("OrderViewId");
        i = paramMApiRequest.getInt("Time");
        int j = paramMApiRequest.getInt("SpanTime");
        MyTakeawayFragment.this.showConfirmDialog(i, j, paramMApiResponse);
      }
      MyTakeawayFragment.this.getMaxArrivalTimeRequest = null;
    }
  };
  private MyTakeawayListAdapter myAdapter;
  private PullToRefreshListView myTakeawayListView;
  protected int nextStartIndex;
  protected MApiRequest submitOldOrderRequest;
  protected ArrayList<DPObject> takeawayList = new ArrayList();

  private String getShopNameByViewId(String paramString)
  {
    Iterator localIterator = this.takeawayList.iterator();
    while (localIterator.hasNext())
    {
      DPObject localDPObject = (DPObject)localIterator.next();
      if (localDPObject.getString("OrderViewId").equals(paramString))
        return localDPObject.getString("ShopName");
    }
    return "";
  }

  private void showConfirmDialog(int paramInt1, int paramInt2, String paramString)
  {
    TAOrderConfirmDialog localTAOrderConfirmDialog = new TAOrderConfirmDialog(getActivity(), paramInt2, paramInt1, paramInt2);
    localTAOrderConfirmDialog.setCanceledOnTouchOutside(true);
    localTAOrderConfirmDialog.setListener(new OnDialogOperationListener(paramString)
    {
      public void cancel()
      {
      }

      public void confirm(int paramInt)
      {
        MyTakeawayFragment localMyTakeawayFragment = MyTakeawayFragment.this;
        if (MyTakeawayFragment.this.getAccount() == null);
        for (String str = ""; ; str = MyTakeawayFragment.this.getAccount().token())
        {
          localMyTakeawayFragment.confirmReceipt(str, Environment.uuid(), this.val$orderViewId, String.valueOf(paramInt));
          return;
        }
      }
    });
    localTAOrderConfirmDialog.show();
  }

  public void appendOrders(DPObject paramDPObject)
  {
    DPObject[] arrayOfDPObject = paramDPObject.getArray("MyTakeAway");
    this.isEnd = paramDPObject.getBoolean("IsEnd");
    this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
    this.applyCancelDesc = paramDPObject.getString("ApplyCancelDesc");
    this.dpPhone = paramDPObject.getString("DianpingPhone");
    if (arrayOfDPObject != null)
    {
      if (this.nextStartIndex == 0)
        this.takeawayList.clear();
      this.takeawayList.addAll(Arrays.asList(arrayOfDPObject));
    }
    this.myAdapter.notifyDataSetChanged();
  }

  protected void confirmReceipt(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (this.confirmReceiptRequest != null)
      return;
    this.confirmReceiptRequest = BasicMApiRequest.mapiGet(Uri.parse("http://waimai.api.dianping.com/arrived.ta").buildUpon().appendQueryParameter("uuid", paramString2).appendQueryParameter("token", paramString1).appendQueryParameter("orderviewid", paramString3).appendQueryParameter("speed", paramString4).toString(), CacheType.DISABLED);
    mapiService().exec(this.confirmReceiptRequest, this.mapiHandler);
  }

  protected void getMaxArrivalTime(String paramString1, String paramString2, String paramString3)
  {
    if (this.getMaxArrivalTimeRequest != null)
      return;
    this.getMaxArrivalTimeRequest = BasicMApiRequest.mapiGet(Uri.parse("http://waimai.api.dianping.com/maxarrivedtime.ta").buildUpon().appendQueryParameter("uuid", paramString2).appendQueryParameter("token", paramString1).appendQueryParameter("orderviewid", paramString3).toString(), CacheType.DISABLED);
    mapiService().exec(this.getMaxArrivalTimeRequest, this.mapiHandler);
  }

  protected void getMyTakeawayListTask(String paramString1, String paramString2, int paramInt)
  {
    if (this.getMyTakeawayListRequest != null)
      return;
    this.getMyTakeawayListRequest = BasicMApiRequest.mapiGet(String.format("%suuid=%s&start=%s&token=%s", new Object[] { "http://waimai.api.dianping.com/mytakeaway.ta?", paramString2, Integer.valueOf(paramInt), paramString1 }), CacheType.CRITICAL);
    mapiService().exec(this.getMyTakeawayListRequest, this.mapiHandler);
  }

  protected void getSingleTakeawayItemTask(String paramString)
  {
    if (this.getSingleTakeawayItemRequest != null);
    do
      return;
    while (TextUtils.isEmpty(paramString));
    this.getSingleTakeawayItemRequest = BasicMApiRequest.mapiGet(String.format("%sorderviewid=%s", new Object[] { "http://waimai.api.dianping.com/takeawayrefresh.ta?", paramString }), CacheType.DISABLED);
    mapiService().exec(this.getSingleTakeawayItemRequest, this.mapiHandler);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.mIntentFilter = new IntentFilter();
    this.mIntentFilter.addAction("com.dianping.takeaway.UPDATE_ORDER");
    registerReceiver(this.mReceiver, this.mIntentFilter);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.my_takeaway_order_layout, null, false);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.getMyTakeawayListRequest != null)
    {
      mapiService().abort(this.getMyTakeawayListRequest, this.mapiHandler, true);
      this.getMyTakeawayListRequest = null;
    }
    if (this.getSingleTakeawayItemRequest != null)
    {
      mapiService().abort(this.getSingleTakeawayItemRequest, this.mapiHandler, true);
      this.getSingleTakeawayItemRequest = null;
    }
    if (this.confirmReceiptRequest != null)
    {
      mapiService().abort(this.confirmReceiptRequest, this.mapiHandler, true);
      this.confirmReceiptRequest = null;
    }
    if (this.getMaxArrivalTimeRequest != null)
    {
      mapiService().abort(this.getMaxArrivalTimeRequest, this.mapiHandler, true);
      this.getMaxArrivalTimeRequest = null;
    }
    if (this.submitOldOrderRequest != null)
    {
      super.mapiService().abort(this.submitOldOrderRequest, this.mapiHandler, true);
      this.submitOldOrderRequest = null;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if ((paramInt <= 0) || (paramInt > this.takeawayList.size()));
    do
    {
      return;
      paramAdapterView = (DPObject)this.takeawayList.get(paramInt - 1);
    }
    while (paramAdapterView == null);
    paramAdapterView = paramAdapterView.getString("OrderViewId");
    startActivity("dianping://takeawayorderdetail?orderviewid=" + paramAdapterView + "&pagefrom=0");
    statisticsEvent("takeaway6", "takeaway6_orderlist_clk", paramAdapterView, 0);
    GAHelper.instance().contextStatisticsEvent(getActivity(), "detail", null, "tap");
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    this.myAdapter.reset();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.loadingView = paramView.findViewById(R.id.loading_view);
    this.loadingView.setVisibility(0);
    paramBundle = paramView.findViewById(R.id.empty_view);
    this.myTakeawayListView = ((PullToRefreshListView)paramView.findViewById(16908298));
    this.myTakeawayListView.setEmptyView(paramBundle);
    this.myTakeawayListView.setOnItemClickListener(this);
    this.myTakeawayListView.setOnRefreshListener(this);
    this.myTakeawayListView.setVisibility(8);
    this.myAdapter = new MyTakeawayListAdapter();
    this.myTakeawayListView.setAdapter(this.myAdapter);
    this.myAdapter.loadNewPage();
  }

  protected void submitOldOrderTask(String paramString)
  {
    if (this.submitOldOrderRequest != null)
      return;
    this.submitOldOrderRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/submitoldorder.ta", new String[] { "orderid", paramString });
    mapiService().exec(this.submitOldOrderRequest, new RequestHandler(paramString)
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        MyTakeawayFragment.this.submitOldOrderRequest = null;
        if (paramMApiResponse != null)
          ((NovaActivity)MyTakeawayFragment.this.getActivity()).showMessageDialog(paramMApiResponse.message());
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        MyTakeawayFragment.this.submitOldOrderRequest = null;
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest != null)
          {
            int i = paramMApiRequest.getInt("PayOrderId");
            paramMApiRequest = paramMApiRequest.getObject("PayProduct");
            int j = paramMApiRequest.getInt("ProductType");
            paramMApiResponse = new Intent("android.intent.action.VIEW", Uri.parse("dianping://minipayorder"));
            paramMApiResponse.putExtra("orderid", String.valueOf(i));
            paramMApiResponse.putExtra("productcode", String.valueOf(j));
            paramMApiResponse.putExtra("mainproductcode", j);
            paramMApiResponse.putExtra("orderinfotitle", paramMApiRequest.getString("Title"));
            paramMApiResponse.putExtra("callbackurl", "dianping://takeawayorderdetail?source=mytakeawayorderlist&pagefrom=1&orderviewid=" + this.val$orderviewid);
            paramMApiResponse.putExtra("callbackfailurl", "dianping://takeawayfailure?source=mytakeawayorderlist&payorderid=" + i + "&orderviewid=" + this.val$orderviewid);
            MyTakeawayFragment.this.startActivity(paramMApiResponse);
          }
        }
      }
    });
  }

  class MyTakeAwayListOrderItem extends NovaLinearLayout
  {
    private LinearLayout operationLayout;
    private String orderViewId;
    private RMBLabelItem priceView;
    private TextView reachTimeView;
    private NetworkImageView shopImageView;
    private String shopName;
    private TextView shopNameView;
    private TextView statusView;

    MyTakeAwayListOrderItem(Context arg2)
    {
      this(localContext, null);
    }

    MyTakeAwayListOrderItem(Context paramAttributeSet, AttributeSet arg3)
    {
      super(localAttributeSet);
      setupOrderItemView();
    }

    private NovaButton createOperationBtn(boolean paramBoolean, String paramString)
    {
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, ViewUtils.dip2px(MyTakeawayFragment.this.getActivity(), 32.0F));
      localLayoutParams.setMargins(ViewUtils.dip2px(MyTakeawayFragment.this.getActivity(), 10.0F), 0, 0, 0);
      NovaButton localNovaButton = new NovaButton(MyTakeawayFragment.this.getActivity());
      localNovaButton.setLayoutParams(localLayoutParams);
      localNovaButton.setText(paramString);
      localNovaButton.setTextSize(0, MyTakeawayFragment.this.getActivity().getResources().getDimensionPixelSize(R.dimen.text_size_15));
      if (paramBoolean)
      {
        i = R.color.text_gray;
        localNovaButton.setTextColor(MyTakeawayFragment.this.getActivity().getResources().getColor(i));
        if (!paramBoolean)
          break label186;
      }
      label186: for (int i = R.drawable.btn_light; ; i = R.drawable.btn_weight)
      {
        localNovaButton.setBackgroundResource(i);
        localNovaButton.setPadding(ViewUtils.dip2px(getContext(), 10.0F), 0, ViewUtils.dip2px(getContext(), 10.0F), 0);
        localNovaButton.setGravity(17);
        localNovaButton.setFocusable(false);
        return localNovaButton;
        i = R.color.white;
        break;
      }
    }

    private void setupOrderItemView()
    {
      View localView = LayoutInflater.from(getContext()).inflate(R.layout.takeaway_mytakeaway_order_item, this, true);
      this.shopImageView = ((NetworkImageView)localView.findViewById(R.id.shop_image));
      this.shopNameView = ((TextView)localView.findViewById(R.id.shop_name));
      this.priceView = ((RMBLabelItem)localView.findViewById(R.id.order_price));
      this.reachTimeView = ((TextView)localView.findViewById(R.id.reach_time));
      this.statusView = ((TextView)localView.findViewById(R.id.order_status));
      this.operationLayout = ((LinearLayout)localView.findViewById(R.id.order_operation_layout));
    }

    private void setupOrderOperations(DPObject paramDPObject)
    {
      paramDPObject = paramDPObject.getArray("Button");
      if ((paramDPObject != null) && (paramDPObject.length > 0))
      {
        this.operationLayout.removeAllViews();
        int j = paramDPObject.length;
        int i = 0;
        if (i < j)
        {
          NovaButton localNovaButton = paramDPObject[i];
          int k = localNovaButton.getInt("Type");
          boolean bool;
          if ((k != 1) && (k != 3))
          {
            bool = true;
            label62: localNovaButton = createOperationBtn(bool, localNovaButton.getString("Message"));
            switch (k)
            {
            default:
            case 1:
            case 2:
            case 3:
            }
          }
          while (true)
          {
            localNovaButton.setOnClickListener(new View.OnClickListener(k)
            {
              public void onClick(View paramView)
              {
                switch (this.val$btnType)
                {
                default:
                  return;
                case 1:
                  MyTakeawayFragment.this.submitOldOrderTask(MyTakeawayFragment.MyTakeAwayListOrderItem.this.orderViewId);
                  MyTakeawayFragment.this.statisticsEvent("takeaway6", "takeaway6_orderlist_payclk", MyTakeawayFragment.MyTakeAwayListOrderItem.this.orderViewId, 0);
                  return;
                case 2:
                  MyTakeawayFragment.this.startActivity("dianping://takeawayreview?orderid=" + MyTakeawayFragment.MyTakeAwayListOrderItem.this.orderViewId + "&shopname=" + MyTakeawayFragment.MyTakeAwayListOrderItem.this.shopName + "&source=2");
                  MyTakeawayFragment.this.statisticsEvent("takeaway6", "takeaway6_orderlist_commentclk", MyTakeawayFragment.MyTakeAwayListOrderItem.this.orderViewId, 0);
                  return;
                case 3:
                }
                MyTakeawayFragment localMyTakeawayFragment = MyTakeawayFragment.this;
                if (MyTakeawayFragment.this.getAccount() == null);
                for (paramView = ""; ; paramView = MyTakeawayFragment.this.getAccount().token())
                {
                  localMyTakeawayFragment.getMaxArrivalTime(paramView, Environment.uuid(), MyTakeawayFragment.MyTakeAwayListOrderItem.this.orderViewId);
                  MyTakeawayFragment.this.statisticsEvent("takeaway6", "takeaway6_orderlist_receivingclk", MyTakeawayFragment.MyTakeAwayListOrderItem.this.orderViewId, 0);
                  return;
                }
              }
            });
            this.operationLayout.addView(localNovaButton);
            i += 1;
            break;
            bool = false;
            break label62;
            localNovaButton.setGAString("pay");
            continue;
            localNovaButton.setGAString("comment");
            continue;
            localNovaButton.setGAString("receiving");
          }
        }
        this.operationLayout.setVisibility(0);
        return;
      }
      this.operationLayout.setVisibility(8);
    }

    public void setupOrderItemData(DPObject paramDPObject)
    {
      Object localObject = paramDPObject.getString("PicUrl");
      Resources localResources;
      if ((NovaConfigUtils.isShowImageInMobileNetwork()) && (!TextUtils.isEmpty((CharSequence)localObject)))
      {
        this.shopImageView.setImage((String)localObject);
        this.shopName = paramDPObject.getString("ShopName");
        this.orderViewId = paramDPObject.getString("OrderViewId");
        this.shopNameView.setText(this.shopName);
        this.priceView.setRMBLabelStyle6(false, getResources().getColor(R.color.light_gray));
        this.priceView.setRMBLabelValue(Double.parseDouble(paramDPObject.getString("Amount")));
        this.reachTimeView.setText(paramDPObject.getString("OrderTime"));
        this.statusView.setText(paramDPObject.getString("OrderStatusStr"));
        localObject = this.statusView;
        localResources = getResources();
        if (paramDPObject.getInt("StatusType") != 1)
          break label187;
      }
      label187: for (int i = R.color.light_red; ; i = R.color.light_gray)
      {
        ((TextView)localObject).setTextColor(localResources.getColor(i));
        setupOrderOperations(paramDPObject);
        return;
        this.shopImageView.setLocalDrawable(getResources().getDrawable(R.drawable.placeholder_empty));
        break;
      }
    }
  }

  class MyTakeawayListAdapter extends BasicAdapter
  {
    MyTakeawayListAdapter()
    {
    }

    private boolean loadNewPage()
    {
      if (MyTakeawayFragment.this.isEnd);
      do
        return false;
      while (MyTakeawayFragment.this.getMyTakeawayListRequest != null);
      MyTakeawayFragment.this.errorMsg = null;
      MyTakeawayFragment localMyTakeawayFragment = MyTakeawayFragment.this;
      if (MyTakeawayFragment.this.getAccount() == null);
      for (String str = ""; ; str = MyTakeawayFragment.this.getAccount().token())
      {
        localMyTakeawayFragment.getMyTakeawayListTask(str, Environment.uuid(), MyTakeawayFragment.this.nextStartIndex);
        return true;
      }
    }

    public int getCount()
    {
      if (MyTakeawayFragment.this.isEnd)
        return MyTakeawayFragment.this.takeawayList.size();
      return MyTakeawayFragment.this.takeawayList.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < MyTakeawayFragment.this.takeawayList.size())
        return MyTakeawayFragment.this.takeawayList.get(paramInt);
      if (MyTakeawayFragment.this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        if ((paramView instanceof MyTakeawayFragment.MyTakeAwayListOrderItem));
        for (paramView = (MyTakeawayFragment.MyTakeAwayListOrderItem)paramView; ; paramView = new MyTakeawayFragment.MyTakeAwayListOrderItem(MyTakeawayFragment.this, MyTakeawayFragment.this.getActivity()))
        {
          paramView.setupOrderItemData((DPObject)localObject);
          return paramView;
        }
      }
      if (localObject == LOADING)
      {
        loadNewPage();
        return MyTakeawayFragment.this.getActivity().getLayoutInflater().inflate(R.layout.loading_item, paramViewGroup, false);
      }
      return getFailedView(MyTakeawayFragment.this.errorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          MyTakeawayFragment.MyTakeawayListAdapter.this.loadNewPage();
        }
      }
      , paramViewGroup, paramView);
    }

    public void reset()
    {
      if (MyTakeawayFragment.this.getMyTakeawayListRequest != null)
      {
        MyTakeawayFragment.this.mapiService().abort(MyTakeawayFragment.this.getMyTakeawayListRequest, null, true);
        MyTakeawayFragment.this.getMyTakeawayListRequest = null;
      }
      MyTakeawayFragment.this.takeawayList.clear();
      MyTakeawayFragment.this.nextStartIndex = 0;
      MyTakeawayFragment.this.isEnd = false;
      MyTakeawayFragment.this.errorMsg = null;
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.fragment.MyTakeawayFragment
 * JD-Core Version:    0.6.0
 */