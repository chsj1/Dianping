package com.dianping.queue.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.queue.entity.QueueState;
import com.dianping.queue.util.QueueViewUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyQueueListActivity extends NovaActivity
{
  private MApiRequest cancelQueueRequest;
  private View emptyView;
  private MApiRequest getQueueListRequest;
  private View loadingView;
  private final List<DPObject> queueInfoList = new ArrayList();
  private MyQueueListAdapter queueListAdapter;
  private NovaTextView queueListHeaderView;
  private PullToRefreshListView queueListView;

  private void cancelQueueTask(int paramInt, String paramString)
  {
    if (this.cancelQueueRequest != null)
      return;
    super.showProgressDialog("请稍后...");
    this.cancelQueueRequest = BasicMApiRequest.mapiPost("http://mapi.dianping.com/queue/cancelorder.qu?", new String[] { "orderid", paramString });
    mapiService().exec(this.cancelQueueRequest, new RequestHandler(paramInt)
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        MyQueueListActivity.this.dismissDialog();
        MyQueueListActivity.this.showShortToast("取消失败");
        MyQueueListActivity.access$902(MyQueueListActivity.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse.result() instanceof DPObject))
        {
          MyQueueListActivity.this.queueInfoList.set(this.val$index, (DPObject)paramMApiResponse.result());
          MyQueueListActivity.this.queueListAdapter.notifyDataSetChanged();
        }
        MyQueueListActivity.this.dismissDialog();
        MyQueueListActivity.access$902(MyQueueListActivity.this, null);
      }
    });
  }

  private void getQueueListTask()
  {
    if (this.getQueueListRequest != null)
    {
      super.mapiService().abort(this.getQueueListRequest, null, true);
      this.getQueueListRequest = null;
    }
    this.getQueueListRequest = BasicMApiRequest.mapiGet("http://mapi.dianping.com/queue/getorderlist.qu?", CacheType.DISABLED);
    mapiService().exec(this.getQueueListRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        MyQueueListActivity.this.queueListView.onRefreshComplete();
        String str = "网络不给力哦";
        paramMApiRequest = str;
        if (paramMApiResponse != null)
        {
          paramMApiRequest = str;
          if ((paramMApiResponse.error() instanceof SimpleMsg))
            paramMApiRequest = ((SimpleMsg)paramMApiResponse.error()).content();
        }
        MyQueueListActivity.this.showShortToast(paramMApiRequest);
        QueueViewUtils.updateViewVisibility(MyQueueListActivity.this.loadingView, 8);
        QueueViewUtils.updateViewVisibility(MyQueueListActivity.this.queueListView, 0);
        MyQueueListActivity.access$802(MyQueueListActivity.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        MyQueueListActivity.this.queueListView.onRefreshComplete();
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          ViewUtils.setVisibilityAndContent(MyQueueListActivity.this.queueListHeaderView, paramMApiRequest.getString("ListTip"));
          MyQueueListActivity.this.queueInfoList.clear();
          if (paramMApiRequest.getArray("OrderList") != null)
            MyQueueListActivity.this.queueInfoList.addAll(Arrays.asList(paramMApiRequest.getArray("OrderList")));
          MyQueueListActivity.this.queueListAdapter.notifyDataSetChanged();
        }
        if ((MyQueueListActivity.this.queueInfoList != null) && (!MyQueueListActivity.this.queueInfoList.isEmpty()))
        {
          QueueViewUtils.updateViewVisibility(MyQueueListActivity.this.loadingView, 8);
          QueueViewUtils.updateViewVisibility(MyQueueListActivity.this.emptyView, 8);
          QueueViewUtils.updateViewVisibility(MyQueueListActivity.this.queueListView, 0);
        }
        while (true)
        {
          MyQueueListActivity.access$802(MyQueueListActivity.this, null);
          return;
          QueueViewUtils.updateViewVisibility(MyQueueListActivity.this.loadingView, 8);
          QueueViewUtils.updateViewVisibility(MyQueueListActivity.this.emptyView, 0);
          QueueViewUtils.updateViewVisibility(MyQueueListActivity.this.queueListView, 8);
        }
      }
    });
  }

  private void initView()
  {
    this.loadingView = findViewById(R.id.loading_view);
    this.emptyView = findViewById(R.id.empty_view);
    this.queueListView = ((PullToRefreshListView)findViewById(R.id.queue_list_view));
    this.queueListHeaderView = ((NovaTextView)LayoutInflater.from(this).inflate(R.layout.queue_list_header, null, false));
    this.queueListHeaderView.setLayoutParams(new AbsListView.LayoutParams(-1, ViewUtils.dip2px(this, 45.0F)));
    this.queueListView.addHeaderView(this.queueListHeaderView);
    this.queueListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
      {
        MyQueueListActivity.this.getQueueListTask();
      }
    });
    this.queueListAdapter = new MyQueueListAdapter(null);
    this.queueListView.setAdapter(this.queueListAdapter);
    if ((super.accountService() == null) || (super.accountService().token() == null))
    {
      QueueViewUtils.updateViewVisibility(this.loadingView, 8);
      QueueViewUtils.updateViewVisibility(this.queueListView, 0);
      return;
    }
    QueueViewUtils.updateViewVisibility(this.loadingView, 0);
    QueueViewUtils.updateViewVisibility(this.queueListView, 8);
    getQueueListTask();
  }

  public String getPageName()
  {
    return "myqueuelist";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.queue_list_layout);
    super.getWindow().setBackgroundDrawable(null);
    initView();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.getQueueListRequest != null)
    {
      super.mapiService().abort(this.getQueueListRequest, null, true);
      this.getQueueListRequest = null;
    }
    if (this.cancelQueueRequest != null)
    {
      super.mapiService().abort(this.cancelQueueRequest, null, true);
      this.cancelQueueRequest = null;
    }
  }

  private class MyQueueListAdapter extends BasicAdapter
  {
    private MyQueueListAdapter()
    {
    }

    public int getCount()
    {
      return MyQueueListActivity.this.queueInfoList.size();
    }

    public Object getItem(int paramInt)
    {
      return MyQueueListActivity.this.queueInfoList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = LayoutInflater.from(MyQueueListActivity.this).inflate(R.layout.queue_list_item, null);
      DPObject localDPObject = (DPObject)MyQueueListActivity.this.queueInfoList.get(paramInt);
      QueueState localQueueState;
      Object localObject3;
      Object localObject1;
      TextView localTextView;
      Object localObject2;
      if (localDPObject != null)
      {
        ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.create_time_view), localDPObject.getString("CreateTime"));
        ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.update_time_view), localDPObject.getString("UpdateTime"));
        int i = localDPObject.getInt("ShopId");
        ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.shop_name_view), localDPObject.getString("ShopName"));
        paramViewGroup.findViewById(R.id.shop_entrance_view).setOnClickListener(new MyQueueListActivity.MyQueueListAdapter.1(this, i));
        ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.order_dish), localDPObject.getString("MenuOrderTip"));
        localQueueState = QueueState.getQueueState(localDPObject.getInt("State"));
        localObject3 = localDPObject.getString("TableNum");
        localObject1 = localDPObject.getString("TableName");
        paramView = paramViewGroup.findViewById(R.id.cur_queue_info_view);
        localTextView = (TextView)paramViewGroup.findViewById(R.id.expired_queue_info_view);
        localObject2 = (Button)paramViewGroup.findViewById(R.id.queue_operation_btn);
        if ((localQueueState != QueueState.HaveAMeal) && (localQueueState != QueueState.QueueExpiredInvalid) && (localQueueState != QueueState.QueueCanceled) && (localQueueState != QueueState.QueueFail))
          break label424;
        QueueViewUtils.updateViewVisibility(paramView, 8);
        if (localQueueState != QueueState.QueueFail)
          break label359;
        QueueViewUtils.updateViewVisibility(localTextView, 8);
        QueueViewUtils.updateViewVisibility((View)localObject2, 0);
        ((Button)localObject2).setText("重新取号");
        ((Button)localObject2).setOnClickListener(new MyQueueListActivity.MyQueueListAdapter.2(this, i));
        paramView = (TextView)paramViewGroup.findViewById(R.id.queue_state_view);
        ViewUtils.setVisibilityAndContent(paramView, localDPObject.getString("StateNotice"));
        paramInt = R.color.light_gray;
        if ((localQueueState != QueueState.RequestSended) && (localQueueState != QueueState.Queuing))
          break label811;
      }
      label424: label577: label709: label716: for (paramInt = R.color.light_red; ; paramInt = R.color.queue_state_green)
        label359: label761: 
        do
        {
          paramView.setTextColor(MyQueueListActivity.this.getResources().getColor(paramInt));
          return paramViewGroup;
          if (TextUtils.isEmpty((CharSequence)localObject3));
          for (paramView = (View)localObject1; ; paramView = (String)localObject3 + " " + (String)localObject1)
          {
            QueueViewUtils.updateViewVisibility(localTextView, 0);
            ViewUtils.setVisibilityAndContent(localTextView, paramView.trim());
            QueueViewUtils.updateViewVisibility((View)localObject2, 8);
            break;
          }
          QueueViewUtils.updateViewVisibility(paramView, 0);
          QueueViewUtils.updateViewVisibility(localTextView, 8);
          if ((localQueueState == QueueState.MyTurn) || (localQueueState == QueueState.QueueExpiredValid) || (localQueueState == QueueState.QueueCanceling))
          {
            QueueViewUtils.updateViewVisibility((View)localObject2, 8);
            paramView = paramViewGroup.findViewById(R.id.cur_table_info_view);
            localObject2 = paramViewGroup.findViewById(R.id.no_cur_table_info_view);
            if ((!TextUtils.isEmpty((CharSequence)localObject1)) || (!TextUtils.isEmpty((CharSequence)localObject3)))
              break label716;
            QueueViewUtils.updateViewVisibility(paramView, 8);
            QueueViewUtils.updateViewVisibility((View)localObject2, 0);
            paramView = localDPObject.getString("Wait");
            localObject1 = paramViewGroup.findViewById(R.id.wait_num_unit_view);
            localObject2 = (TextView)paramViewGroup.findViewById(R.id.wait_num_view);
            localObject3 = paramViewGroup.findViewById(R.id.no_wait_num_view);
            if (!TextUtils.isEmpty(paramView))
              break label761;
            QueueViewUtils.updateViewVisibility((View)localObject1, 8);
            QueueViewUtils.updateViewVisibility((View)localObject2, 8);
            QueueViewUtils.updateViewVisibility((View)localObject3, 0);
            paramView = localDPObject.getString("WaitTime");
            localObject1 = (TextView)paramViewGroup.findViewById(R.id.wait_time_view);
            localObject2 = paramViewGroup.findViewById(R.id.no_wait_time_view);
            if (!TextUtils.isEmpty(paramView))
              break label789;
            QueueViewUtils.updateViewVisibility((View)localObject1, 8);
            QueueViewUtils.updateViewVisibility((View)localObject2, 0);
          }
          while (true)
          {
            ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.expire_tip_view), localDPObject.getString("ExpireNotice"));
            break;
            QueueViewUtils.updateViewVisibility((View)localObject2, 0);
            boolean bool;
            if (localQueueState == QueueState.QueueSuccess)
            {
              bool = true;
              if (!bool)
                break label709;
            }
            for (paramView = "放弃"; ; paramView = "取消")
            {
              ((Button)localObject2).setText(paramView);
              ((Button)localObject2).setOnClickListener(new MyQueueListActivity.MyQueueListAdapter.3(this, bool, paramInt, localDPObject));
              break;
              bool = false;
              break label667;
            }
            QueueViewUtils.updateViewVisibility(paramView, 0);
            QueueViewUtils.updateViewVisibility((View)localObject2, 8);
            ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.cur_table_name_view), (String)localObject1);
            ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.cur_table_num_view), (String)localObject3);
            break label512;
            QueueViewUtils.updateViewVisibility((View)localObject1, 0);
            QueueViewUtils.updateViewVisibility((View)localObject2, 0);
            ViewUtils.setVisibilityAndContent((TextView)localObject2, paramView);
            QueueViewUtils.updateViewVisibility((View)localObject3, 8);
            break label577;
            QueueViewUtils.updateViewVisibility((View)localObject1, 0);
            ViewUtils.setVisibilityAndContent((TextView)localObject1, paramView);
            QueueViewUtils.updateViewVisibility((View)localObject2, 8);
          }
        }
        while ((localQueueState != QueueState.QueueSuccess) && (localQueueState != QueueState.MyTurn) && (localQueueState != QueueState.QueueExpiredValid));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.queue.activity.MyQueueListActivity
 * JD-Core Version:    0.6.0
 */