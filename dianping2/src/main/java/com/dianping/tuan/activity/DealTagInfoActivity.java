package com.dianping.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.widget.BuyDealView;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

public class DealTagInfoActivity extends BaseTuanActivity
{
  private Adapter adapter;
  private BuyDealView buyView;
  private DPObject dpDeal;
  private PullToRefreshListView listView;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.dpDeal = ((DPObject)getIntent().getParcelableExtra("deal"));
    if (this.dpDeal == null)
    {
      finish();
      return;
    }
    setContentView(R.layout.deal_tag_info);
    this.buyView = ((BuyDealView)findViewById(R.id.buy));
    this.buyView.setShowTags(false);
    this.buyView.setDeal(this.dpDeal);
    this.listView = ((PullToRefreshListView)findViewById(R.id.list));
    this.listView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.listView.setDivider(null);
    this.listView.setSelector(new ColorDrawable(0));
    this.adapter = new Adapter(this);
    this.listView.setAdapter(this.adapter);
  }

  protected void onDestroy()
  {
    this.adapter.cancelLoad();
    super.onDestroy();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (this.adapter != null)
      this.adapter.reset();
    return super.onLogin(paramBoolean);
  }

  class Adapter extends BasicLoadAdapter
  {
    public Adapter(Context arg2)
    {
      super();
    }

    public MApiRequest createRequest(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://app.t.dianping.com/");
      localStringBuilder.append("eventdetailgn.bin");
      localStringBuilder.append("?dealid=").append(DealTagInfoActivity.this.dpDeal.getInt("ID"));
      localStringBuilder.append("&cityid=").append(DealTagInfoActivity.this.cityId());
      if (DealTagInfoActivity.this.isLogined())
        localStringBuilder.append("&token=").append(DealTagInfoActivity.this.accountService().token());
      return BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramView = DealTagInfoActivity.this.getLayoutInflater().inflate(R.layout.deal_tag_list_item, paramViewGroup, false);
        paramDPObject = new DealTagInfoActivity.ViewHolder();
        paramDPObject.icon = ((ColorBorderTextView)paramView.findViewById(16908308));
        paramDPObject.title = ((TextView)paramView.findViewById(16908309));
        paramDPObject.desc = ((TextView)paramView.findViewById(R.id.desc));
        paramView.setTag(paramDPObject);
        paramViewGroup = (DPObject)getItem(paramInt);
        if (!TextUtils.isEmpty(paramViewGroup.getString("Tag")))
          break label146;
        paramDPObject.icon.setVisibility(4);
      }
      while (true)
      {
        paramDPObject.title.setText(paramViewGroup.getString("ShortTitle"));
        paramDPObject.desc.setText(paramViewGroup.getString("Desc"));
        return paramView;
        paramDPObject = (DealTagInfoActivity.ViewHolder)paramView.getTag();
        break;
        label146: paramDPObject.icon.setTextColor(paramViewGroup.getString("Color"));
        paramDPObject.icon.setBorderColor("#C8" + paramViewGroup.getString("Color").substring(1));
        paramDPObject.icon.setText(paramViewGroup.getString("Tag"));
        paramDPObject.icon.setVisibility(0);
      }
    }
  }

  static class ViewHolder
  {
    TextView desc;
    ColorBorderTextView icon;
    TextView title;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.DealTagInfoActivity
 * JD-Core Version:    0.6.0
 */