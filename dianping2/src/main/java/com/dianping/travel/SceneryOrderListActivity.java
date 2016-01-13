package com.dianping.travel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.travel.view.SceneryOrderItem;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class SceneryOrderListActivity extends NovaListActivity
  implements AdapterView.OnItemClickListener
{
  public static final String ACTION_SCENERY_ORDER_LIST_CHANGED = "scenery_order_list_changed";
  private Adapter adapter;
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("scenery_order_list_changed".equals(paramIntent.getAction()))
        SceneryOrderListActivity.this.adapter.reset();
    }
  };

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.adapter = new Adapter();
    this.listView.setAdapter(this.adapter);
    this.listView.setOnItemClickListener(this);
    paramBundle = new IntentFilter("scenery_order_list_changed");
    registerReceiver(this.receiver, paramBundle);
  }

  protected void onDestroy()
  {
    unregisterReceiver(this.receiver);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if (paramAdapterView == this.adapter.HELP)
    {
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=http://m1.s1.dpfile.com/sc/api_res/scenery/sceneryorderhelp.html?v=2")));
      statisticsEvent("myticket5", "myticket5_help", "", 0);
    }
    do
      return;
    while (!(paramAdapterView instanceof DPObject));
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://sceneryorder?orderid=" + ((DPObject)paramAdapterView).getInt("ID"))));
    paramInt = ((DPObject)paramAdapterView).getInt("Status");
    paramAdapterView = "预订成功";
    if (paramInt == 1)
      paramAdapterView = "已取消";
    while (true)
    {
      statisticsEvent("myticket5", "myticket5_list", paramAdapterView, 0);
      return;
      if (paramInt != 2)
        continue;
      paramAdapterView = "已使用";
    }
  }

  protected void setEmptyView()
  {
    super.setEmptyView();
    WebView localWebView = new WebView(this);
    localWebView.loadUrl("http://m1.s1.dpfile.com/sc/api_res/scenery/sceneryorderhelp.html?v=2");
    this.emptyView.addView(localWebView);
  }

  class Adapter extends BasicLoadAdapter
  {
    private final Object HELP = new Object();

    Adapter()
    {
      super();
    }

    public MApiRequest createRequest(int paramInt)
    {
      if (SceneryOrderListActivity.this.accountService() == null);
      for (String str = ""; ; str = SceneryOrderListActivity.this.accountService().token())
        return BasicMApiRequest.mapiGet("http://m.api.dianping.com/getsceneryorders.bin?token=" + str + "&start=" + paramInt + "&clientUUID=" + Environment.uuid(), CacheType.DISABLED);
    }

    public int getCount()
    {
      if (this.mData.size() > 0)
        return super.getCount() + 1;
      return super.getCount();
    }

    public Object getItem(int paramInt)
    {
      if (this.mData.size() > 0)
      {
        if (paramInt == 0)
          return this.HELP;
        return super.getItem(paramInt - 1);
      }
      return super.getItem(paramInt);
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (getItem(paramInt) == this.HELP)
      {
        paramView = SceneryOrderListActivity.this.getLayoutInflater().inflate(R.layout.ticket_item, paramViewGroup, false);
        ((ImageView)paramView.findViewById(R.id.img00)).setImageResource(R.drawable.ic_info);
        ((TextView)paramView.findViewById(16908308)).setText("订票帮助");
        return paramView;
      }
      return super.getView(paramInt, paramView, paramViewGroup);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView != null)
      {
        paramViewGroup = paramView;
        if ((paramView instanceof SceneryOrderItem));
      }
      else
      {
        paramViewGroup = SceneryOrderListActivity.this.getLayoutInflater().inflate(R.layout.scenery_order_item, null);
      }
      ((SceneryOrderItem)paramViewGroup).setData(paramDPObject);
      return paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.SceneryOrderListActivity
 * JD-Core Version:    0.6.0
 */