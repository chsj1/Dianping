package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpiredCouponListActivity extends BaseTuanPtrListActivity
{
  protected static final DateFormat FMT = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
  private Adapter adapter;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.listView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.listView.setDivider(null);
    this.listView.setDividerHeight(0);
    this.listView.setBackgroundResource(R.color.common_bk_color);
    this.listView.setBackgroundResource(R.color.common_bk_color);
    this.listView.setSelector(new ColorDrawable(0));
    this.adapter = new Adapter(this);
    this.listView.setAdapter(this.adapter);
  }

  public void onDestroy()
  {
    this.adapter.cancelLoad();
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
    if (isDPObjectof(paramAdapterView, "Coupon"))
      paramAdapterView = (DPObject)paramAdapterView;
    try
    {
      paramView = new Intent("android.intent.action.VIEW");
      paramView.setData(Uri.parse("dianping://coupondetail"));
      paramView.putExtra("coupon", paramAdapterView);
      startActivity(paramView);
      return;
    }
    catch (Exception paramAdapterView)
    {
      paramAdapterView.printStackTrace();
    }
  }

  class Adapter extends BasicLoadAdapter
  {
    public Adapter(Context arg2)
    {
      super();
    }

    public void appendData(DPObject paramDPObject)
    {
      super.appendData(paramDPObject);
      ExpiredCouponListActivity.this.listView.onRefreshComplete();
    }

    public MApiRequest createRequest(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://app.t.dianping.com/");
      localStringBuilder.append("couponlistgn.bin");
      localStringBuilder.append("?token=").append(ExpiredCouponListActivity.this.accountService().token());
      localStringBuilder.append("&filter=").append("4");
      localStringBuilder.append("&start=").append(paramInt);
      return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.CRITICAL);
    }

    protected String emptyMessage()
    {
      return "您没有已过期的团购券";
    }

    public View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (ExpiredCouponListActivity.this.isDPObjectof(paramDPObject, "Coupon"))
      {
        ExpiredCouponListActivity.ViewHolder localViewHolder;
        if ((paramView == null) || (!(paramView.getTag() instanceof ExpiredCouponListActivity.ViewHolder)))
        {
          localViewHolder = new ExpiredCouponListActivity.ViewHolder();
          paramView = LayoutInflater.from(ExpiredCouponListActivity.this).inflate(R.layout.expired_coupon_list_item, paramViewGroup, false);
          paramView.setBackgroundColor(ExpiredCouponListActivity.this.getResources().getColor(R.color.common_bk_color));
          localViewHolder.titleView = ((TextView)paramView.findViewById(16908310));
          localViewHolder.timeView = ((TextView)paramView.findViewById(R.id.expired_time));
          paramView.setTag(localViewHolder);
        }
        for (paramViewGroup = localViewHolder; ; paramViewGroup = (ExpiredCouponListActivity.ViewHolder)paramView.getTag())
        {
          paramViewGroup.titleView.setText(paramDPObject.getString("Title"));
          paramViewGroup.timeView.setText(ExpiredCouponListActivity.FMT.format(new Date(paramDPObject.getTime("Date"))) + "过期");
          return paramView;
        }
      }
      return null;
    }
  }

  static class ViewHolder
  {
    public TextView timeView;
    public TextView titleView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.ExpiredCouponListActivity
 * JD-Core Version:    0.6.0
 */