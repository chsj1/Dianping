package com.dianping.queue.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.model.SimpleMsg;
import com.dianping.queue.agent.QueueAdvertisementAgent;
import com.dianping.queue.agent.QueueDiningTableInfoAgent;
import com.dianping.queue.agent.QueueHasQueuedAgent;
import com.dianping.queue.agent.QueueNoAgent;
import com.dianping.queue.agent.QueueOrderDishAgent;
import com.dianping.queue.agent.QueueRemindAgent;
import com.dianping.queue.agent.QueueRestAgent;
import com.dianping.queue.agent.QueueShopInfoAgent;
import com.dianping.queue.agent.QueueToQueueAgent;
import com.dianping.queue.entity.DataLoadStatus;
import com.dianping.queue.entity.QueueMainDataSource;
import com.dianping.queue.entity.QueueMainDataSource.QueueDataLoaderListener;
import com.dianping.queue.entity.QueueMapiFailStatus;
import com.dianping.queue.entity.QueueMapiStatus;
import com.dianping.queue.entity.QueueShop;
import com.dianping.queue.entity.QueueShopStatus;
import com.dianping.queue.entity.QueueTable;
import com.dianping.queue.util.QueueViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.ArrayList<Lcom.dianping.base.app.loader.AgentListConfig;>;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueueMainFragment extends AgentFragment
  implements QueueMainDataSource.QueueDataLoaderListener
{
  protected PullToRefreshScrollView containerView;
  protected QueueMainDataSource dataSource;
  protected LoadingErrorView errorView;
  protected DataLoadStatus loadStatus;
  protected View loadingView;
  private final PullToRefreshBase.OnRefreshListener<ScrollView> refreshListener = new QueueMainFragment.1(this);

  public static QueueMainFragment newInstance(String paramString, int paramInt)
  {
    QueueMainFragment localQueueMainFragment = new QueueMainFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("shopid", paramString);
    localBundle.putInt("frompush", paramInt);
    localQueueMainFragment.setArguments(localBundle);
    return localQueueMainFragment;
  }

  public void addInterest(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject)
  {
    super.dismissProgressDialog();
    switch (QueueMainFragment.9.$SwitchMap$com$dianping$queue$entity$QueueMapiStatus[paramQueueMapiStatus.ordinal()])
    {
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      paramQueueMapiStatus = new Bundle();
      paramQueueMapiStatus.putString("type", "interest");
      super.dispatchAgentChanged(null, paramQueueMapiStatus);
      return;
      Toast.makeText(super.getActivity(), "提醒设置成功", 0).show();
      continue;
      Toast.makeText(super.getActivity(), "提醒设置失败", 0).show();
    }
  }

  public void addReminder()
  {
    super.showProgressDialog("添加提醒中...");
    this.dataSource.addReminder();
  }

  public void cancelInterest(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject)
  {
    super.dismissProgressDialog();
    switch (QueueMainFragment.9.$SwitchMap$com$dianping$queue$entity$QueueMapiStatus[paramQueueMapiStatus.ordinal()])
    {
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      paramQueueMapiStatus = new Bundle();
      paramQueueMapiStatus.putString("type", "interest");
      super.dispatchAgentChanged(null, paramQueueMapiStatus);
      return;
      Toast.makeText(super.getActivity(), "已取消提醒", 0).show();
      continue;
      Toast.makeText(super.getActivity(), "取消提醒失败", 0).show();
    }
  }

  public void cancelOrder()
  {
    super.showProgressDialog("取消中...");
    this.dataSource.cancelOrder();
  }

  public void cancelOrderFinish(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject)
  {
    super.dismissProgressDialog();
    switch (QueueMainFragment.9.$SwitchMap$com$dianping$queue$entity$QueueMapiStatus[paramQueueMapiStatus.ordinal()])
    {
    default:
      return;
    case 1:
      this.dataSource.queueShop.shopState = QueueShopStatus.CAN_ORDER.getStatusCode();
      this.dataSource.queueShop.currentOrder = null;
      this.dataSource.orderDishObj = null;
      super.dispatchCellChanged(null);
      return;
    case 2:
    }
    Toast.makeText(super.getActivity(), "取消失败", 0).show();
  }

  public void cancelReminder()
  {
    super.showProgressDialog("取消提醒中...");
    this.dataSource.cancelReminder();
  }

  protected void checkAndGetLastTable()
  {
    QueueTable[] arrayOfQueueTable = this.dataSource.queueShop.tableList;
    if (arrayOfQueueTable.length == 1)
    {
      super.showProgressDialog("取号中...");
      this.dataSource.selectedTable = arrayOfQueueTable[0];
      this.dataSource.createOrder();
      return;
    }
    ArrayList localArrayList = new ArrayList();
    int j = arrayOfQueueTable.length;
    int i = 0;
    while (i < j)
    {
      QueueTable localQueueTable = arrayOfQueueTable[i];
      if ((localQueueTable.minPeople <= this.dataSource.peopleNum) && (this.dataSource.peopleNum <= localQueueTable.maxPeople))
        localArrayList.add(localQueueTable);
      i += 1;
    }
    if (localArrayList.size() > 1)
    {
      selectUltimateTable(super.getActivity(), localArrayList);
      return;
    }
    super.showProgressDialog("取号中...");
    if (localArrayList.size() == 1);
    for (this.dataSource.selectedTable = arrayOfQueueTable[0]; ; this.dataSource.selectedTable = null)
    {
      this.dataSource.createOrder();
      return;
    }
  }

  public void closeForDataInvalid()
  {
    Toast.makeText(super.getActivity(), "数据异常", 0).show();
  }

  public void createOrder()
  {
    checkAndGetLastTable();
  }

  public void createOrderFinish(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject)
  {
    super.dismissProgressDialog();
    switch (QueueMainFragment.9.$SwitchMap$com$dianping$queue$entity$QueueMapiStatus[paramQueueMapiStatus.ordinal()])
    {
    default:
      return;
    case 1:
      paramQueueMapiFailStatus = (DPObject)paramObject;
      int i = paramQueueMapiFailStatus.getInt("ResultCode");
      paramQueueMapiStatus = paramQueueMapiFailStatus.getString("ResultMsg");
      paramQueueMapiFailStatus = paramQueueMapiFailStatus.getObject("Order");
      switch (i)
      {
      default:
        statisticsEvent("queue", "queue_submit", "fail", 0);
        showNotiDialog(paramQueueMapiStatus);
        return;
      case 1:
        statisticsEvent("queue", "queue_submit", "success", 0);
        paramQueueMapiStatus = new Intent();
        paramQueueMapiStatus.setData(Uri.parse("dianping://queueresult?orderid=" + paramQueueMapiFailStatus.getString("OrderId") + "&shopid=" + this.dataSource.shopId + "&firstloopinterval=" + this.dataSource.firstLoopInterval + "&furtherloopinterval=" + this.dataSource.furtherLoopInterval));
        startActivity(paramQueueMapiStatus);
        super.getActivity().finish();
        return;
      case 11:
      case 13:
      case 14:
      }
      statisticsEvent("queue", "queue_submit", "fail", 0);
      showConfirmDialog(paramQueueMapiStatus);
      return;
    case 2:
    }
    Toast.makeText(super.getActivity(), "取号失败", 0).show();
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    Object localObject = new LinkedHashMap();
    ((Map)localObject).put("queue/tablequeue", QueueToQueueAgent.class);
    ((Map)localObject).put("queue/shoprest", QueueRestAgent.class);
    ((Map)localObject).put("queue/toofar", QueueNoAgent.class);
    ((Map)localObject).put("queue/hasqueued", QueueHasQueuedAgent.class);
    ((Map)localObject).put("queue/orderdish", QueueOrderDishAgent.class);
    ((Map)localObject).put("queue/tableinfo", QueueDiningTableInfoAgent.class);
    ((Map)localObject).put("queue/shopinfo", QueueShopInfoAgent.class);
    ((Map)localObject).put("queue/remindinfo", QueueRemindAgent.class);
    ((Map)localObject).put("queue/advertisement", QueueAdvertisementAgent.class);
    localObject = new QueueMainFragment.8(this, (Map)localObject);
    ArrayList localArrayList = new ArrayList(1);
    localArrayList.add(localObject);
    return (ArrayList<AgentListConfig>)localArrayList;
  }

  public QueueMainDataSource getDataSource()
  {
    return this.dataSource;
  }

  public DPObject getQueueOrderDish()
  {
    if (this.dataSource == null)
      return null;
    return this.dataSource.orderDishObj;
  }

  public QueueShop getQueueShop()
  {
    if (this.dataSource == null)
      return null;
    return this.dataSource.queueShop;
  }

  public void loadQueueInfo(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject)
  {
    this.containerView.onRefreshComplete();
    switch (QueueMainFragment.9.$SwitchMap$com$dianping$queue$entity$QueueMapiStatus[paramQueueMapiStatus.ordinal()])
    {
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      updateViewAccordingToStatus();
      return;
      this.loadStatus = DataLoadStatus.LOADED;
      super.dispatchCellChanged(null);
      paramQueueMapiStatus = new GAUserInfo();
      paramQueueMapiStatus.index = Integer.valueOf(this.dataSource.queueShop.shopState);
      paramQueueMapiStatus.shop_id = Integer.getInteger(this.dataSource.shopId);
      GAHelper.instance().contextStatisticsEvent(getActivity(), "status", paramQueueMapiStatus, "view");
      continue;
      this.loadStatus = DataLoadStatus.FAILED;
    }
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    super.getActivity().getWindow().setBackgroundDrawable(null);
    this.dataSource = new QueueMainDataSource((NovaActivity)(NovaActivity)super.getActivity());
    this.dataSource.queueDataLoaderListener = this;
    if (paramBundle != null)
      this.dataSource.shopId = paramBundle.getString("shopid");
    for (this.dataSource.isFromPush = paramBundle.getInt("frompush", 0); ; this.dataSource.isFromPush = paramBundle.getInt("frompush", 0))
    {
      startLoadData();
      return;
      paramBundle = getArguments();
      this.dataSource.shopId = paramBundle.getString("shopid");
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.queue_main_fragment, paramViewGroup, false);
    paramViewGroup = (ViewGroup)paramLayoutInflater.findViewById(R.id.queue_main_container);
    this.containerView = ((PullToRefreshScrollView)paramLayoutInflater.findViewById(R.id.root_view));
    this.containerView.setOnTouchListener(new QueueMainFragment.2(this));
    this.containerView.setOnRefreshListener(this.refreshListener);
    this.loadingView = paramLayoutInflater.findViewById(R.id.loading_layout);
    this.errorView = ((LoadingErrorView)paramLayoutInflater.findViewById(R.id.error_layout));
    this.errorView.setCallBack(new QueueMainFragment.3(this));
    super.setAgentContainerView(paramViewGroup);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    this.dataSource.releaseRequests();
    super.onDestroy();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.dataSource.saveData(paramBundle);
  }

  protected void selectUltimateTable(Context paramContext, ArrayList<QueueTable> paramArrayList)
  {
    Object localObject = new ArrayList();
    if (paramArrayList != null)
    {
      int i = 0;
      if (i < paramArrayList.size())
      {
        QueueTable localQueueTable = (QueueTable)paramArrayList.get(i);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(localQueueTable.tableName);
        if (localQueueTable.maxPeople == localQueueTable.minPeople)
          localStringBuilder.append("（").append(localQueueTable.maxPeople).append("人）");
        while (true)
        {
          ((ArrayList)localObject).add(new QueueMainFragment.DialogItem(localStringBuilder.toString(), true, null));
          i += 1;
          break;
          localStringBuilder.append("（").append(localQueueTable.minPeople).append("-").append(localQueueTable.maxPeople).append("人）");
        }
      }
    }
    localObject = new QueueMainFragment.DialogAdapter((ArrayList)localObject, LayoutInflater.from(paramContext), null);
    paramContext = new AlertDialog.Builder(paramContext);
    paramContext.setTitle("选择桌型").setAdapter((ListAdapter)localObject, new QueueMainFragment.4(this, paramArrayList));
    paramContext = paramContext.create();
    paramContext.show();
    paramContext.setCanceledOnTouchOutside(true);
  }

  public void setHobbitEntry(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject)
  {
    paramQueueMapiStatus = new Bundle();
    paramQueueMapiStatus.putParcelable("hobbitEntryInfoObj", (DPObject)paramObject);
    super.dispatchAgentChanged("queue/orderdish", paramQueueMapiStatus);
  }

  public void setReminder(long paramLong, int paramInt)
  {
    if (paramInt == 10)
      super.showProgressDialog("设置排号提醒中...");
    while (true)
    {
      this.dataSource.setReminder(paramLong, paramInt);
      return;
      super.showProgressDialog("取消排号提醒中...");
    }
  }

  public void setReminderFinish(QueueMapiStatus paramQueueMapiStatus, QueueMapiFailStatus paramQueueMapiFailStatus, Object paramObject)
  {
    super.dismissProgressDialog();
    switch (QueueMainFragment.9.$SwitchMap$com$dianping$queue$entity$QueueMapiStatus[paramQueueMapiStatus.ordinal()])
    {
    default:
      return;
    case 1:
      int i = ((DPObject)paramObject).getInt("ResultCode");
      paramQueueMapiStatus = ((DPObject)paramObject).getString("ResultMsg");
      if (i == 10)
      {
        Toast.makeText(super.getActivity(), paramQueueMapiStatus, 0).show();
        startLoadData();
        return;
      }
      showNotiDialog(paramQueueMapiStatus);
      return;
    case 2:
    }
    Toast.makeText(super.getActivity(), ((SimpleMsg)paramObject).content(), 0).show();
  }

  protected void showConfirmDialog(String paramString)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(super.getActivity());
    localBuilder.setMessage(paramString);
    localBuilder.setPositiveButton("好的", new QueueMainFragment.5(this));
    localBuilder.setNegativeButton("查看我的排号", new QueueMainFragment.6(this));
    localBuilder.show();
  }

  protected void showNotiDialog(String paramString)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(super.getActivity());
    localBuilder.setMessage(paramString);
    localBuilder.setPositiveButton("知道了", new QueueMainFragment.7(this));
    localBuilder.show();
  }

  public void startLoadData()
  {
    this.loadStatus = DataLoadStatus.LOADING;
    updateViewAccordingToStatus();
    this.dataSource.loadData();
  }

  protected void updateViewAccordingToStatus()
  {
    switch (QueueMainFragment.9.$SwitchMap$com$dianping$queue$entity$DataLoadStatus[this.loadStatus.ordinal()])
    {
    default:
      return;
    case 1:
      QueueViewUtils.updateViewVisibility(this.loadingView, 0);
      QueueViewUtils.updateViewVisibility(this.errorView, 8);
      QueueViewUtils.updateViewVisibility(this.containerView, 4);
      return;
    case 2:
      QueueViewUtils.updateViewVisibility(this.loadingView, 8);
      QueueViewUtils.updateViewVisibility(this.errorView, 8);
      QueueViewUtils.updateViewVisibility(this.containerView, 0);
      return;
    case 3:
    }
    QueueViewUtils.updateViewVisibility(this.loadingView, 8);
    QueueViewUtils.updateViewVisibility(this.errorView, 0);
    QueueViewUtils.updateViewVisibility(this.containerView, 4);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.queue.fragment.QueueMainFragment
 * JD-Core Version:    0.6.0
 */