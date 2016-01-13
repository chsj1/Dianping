package com.dianping.tuan.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.model.UserProfile;
import com.dianping.tuan.widget.DiscountListItem;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;

public class ExpiredDiscountListActivity extends BaseTuanActivity
{
  private static final int DEFAULT_LIMITE = 25;
  private static final int EXPIRE = 1;
  protected Adapter adapter;
  protected PullToRefreshListView discountListView;

  protected void initList()
  {
    if (this.discountListView.getAdapter() != null)
    {
      this.adapter.reset();
      return;
    }
    this.discountListView.setAdapter(this.adapter);
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
    if (paramUserProfile == null)
    {
      finish();
      return;
    }
    initList();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.discount_list_layout);
    this.discountListView = ((PullToRefreshListView)findViewById(R.id.list));
    this.discountListView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.discountListView.setBackgroundColor(getResources().getColor(R.color.common_bk_color));
    this.discountListView.setDivider(null);
    this.discountListView.setSelector(new ColorDrawable(0));
    this.discountListView.setFastScrollEnabled(true);
    paramBundle = LayoutInflater.from(this).inflate(R.layout.expire_discount_head, null, false);
    this.discountListView.addHeaderView(paramBundle);
    this.adapter = new Adapter(this);
    if (isLogined())
    {
      initList();
      return;
    }
    accountService().login(this);
  }

  protected void onDestroy()
  {
    this.adapter.cancelLoad();
    super.onDestroy();
  }

  public void onLoginCancel()
  {
    finish();
    super.onLoginCancel();
  }

  class Adapter extends BasicLoadAdapter
  {
    public Adapter(Context arg2)
    {
      super();
    }

    public MApiRequest createRequest(int paramInt)
    {
      UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
      localUrlBuilder.appendPath("discountlistgn.bin");
      localUrlBuilder.addParam("token", ExpiredDiscountListActivity.this.accountService().token());
      localUrlBuilder.addParam("start", Integer.valueOf(paramInt));
      localUrlBuilder.addParam("expired", Integer.valueOf(1));
      localUrlBuilder.addParam("limit", Integer.valueOf(25));
      return BasicMApiRequest.mapiGet(localUrlBuilder.buildUrl(), CacheType.DISABLED);
    }

    protected String emptyMessage()
    {
      return "暂无过期抵用券";
    }

    public boolean isEnabled(int paramInt)
    {
      return false;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof DiscountListItem));
      for (paramView = (DiscountListItem)paramView; ; paramView = null)
      {
        paramViewGroup = paramView;
        if (paramView == null)
          paramViewGroup = (DiscountListItem)LayoutInflater.from(ExpiredDiscountListActivity.this).inflate(R.layout.discount_list_item2, null, false);
        paramViewGroup.showItem(paramDPObject, 1);
        return paramViewGroup;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.ExpiredDiscountListActivity
 * JD-Core Version:    0.6.0
 */