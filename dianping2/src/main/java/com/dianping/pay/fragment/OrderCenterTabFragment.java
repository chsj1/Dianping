package com.dianping.pay.fragment;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.fragment.BaseTuanTabPagerFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;

public class OrderCenterTabFragment extends BaseTuanTabPagerFragment
{
  private String mInitTab;
  private Map<String, OrderListBaseFragment> mOrderListFragments;
  private int mTabIndex = 0;
  private Map<String, Integer> mTabToIndex;
  private MApiRequest mTabUpdateRequest;
  private BroadcastReceiver mUpdateNotificationReceiver = new OrderCenterTabFragment.1(this);

  private OrderListBaseFragment addOrderListTab(String paramString1, int paramInt, String paramString2)
  {
    if (("unused".equals(paramString1)) || ("unpaid".equals(paramString1)) || ("refund".equals(paramString1)))
    {
      OrderListBaseFragment localOrderListBaseFragment = new OrderListBaseFragment();
      Bundle localBundle = new Bundle();
      localBundle.putInt("orderFilter", paramInt);
      localBundle.putString("tab", paramString1);
      switch (paramInt)
      {
      default:
      case 1:
      case 2:
      case 3:
      }
      while (true)
      {
        localOrderListBaseFragment.setArguments(localBundle);
        addTab(paramString2, R.layout.tuan_tab_indicator, localOrderListBaseFragment, null);
        this.mOrderListFragments.put(paramString1, localOrderListBaseFragment);
        paramString2 = this.mTabToIndex;
        paramInt = this.mTabIndex;
        this.mTabIndex = (paramInt + 1);
        paramString2.put(paramString1, Integer.valueOf(paramInt));
        return localOrderListBaseFragment;
        localBundle.putString("pageName", "integrateunusedorder");
        continue;
        localBundle.putString("pageName", "integrateunpaidorder");
        continue;
        localBundle.putString("pageName", "integraterefundorder");
      }
    }
    return null;
  }

  private void initTabs()
  {
    Object localObject = new ArrayList(4);
    ((List)localObject).add(new BasicNameValuePair("unused", "可使用"));
    ((List)localObject).add(new BasicNameValuePair("unpaid", "待付款"));
    ((List)localObject).add(new BasicNameValuePair("refund", "退款单"));
    int i = 0;
    while (i < ((List)localObject).size())
    {
      addOrderListTab(((BasicNameValuePair)((List)localObject).get(i)).getName(), i + 1, ((BasicNameValuePair)((List)localObject).get(i)).getValue());
      i += 1;
    }
    localObject = new OrderCategoriesFragment();
    ((OrderCategoriesFragment)localObject).setArguments(getArguments());
    addTab("全部订单", R.layout.tuan_tab_indicator, (Fragment)localObject, null);
    this.mTabToIndex.put("all", Integer.valueOf(this.mTabIndex));
    requestNewTabLabels();
  }

  private void requestNewTabLabels()
  {
    if (this.mTabUpdateRequest == null)
    {
      UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
      localUrlBuilder.appendPath("uniordertabsinfogn.bin");
      localUrlBuilder.addParam("token", accountService().token());
      this.mTabUpdateRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.CRITICAL);
    }
    mapiService().exec(this.mTabUpdateRequest, this);
  }

  private void setupViews()
  {
    if (this.mTabHost.getTabWidget().getChildCount() == 0)
    {
      this.mTabToIndex = new HashMap(4);
      this.mOrderListFragments = new HashMap(3);
      initTabs();
      if ((this.mInitTab != null) && (this.mTabToIndex.containsKey(this.mInitTab)))
      {
        int i = ((Integer)this.mTabToIndex.get(this.mInitTab)).intValue();
        if (i != -1)
          this.mTabHost.setCurrentTab(i);
      }
      if (this.mTabIndex == 0)
        this.mTabHost.getTabWidget().setVisibility(8);
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("com.dianping.t.order_count_invalid");
      localIntentFilter.addAction("tuan:uniorder_booking_paid");
      localIntentFilter.addAction("tuan:order_refund_status_changed");
      registerReceiver(this.mUpdateNotificationReceiver, localIntentFilter);
    }
  }

  public void gotoOrderTab(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      this.mInitTab = paramString;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    GAHelper.instance().setGAPageName("integrateordertab");
  }

  public void onDestroyView()
  {
    unregisterReceiver(this.mUpdateNotificationReceiver);
    if (this.mOrderListFragments != null)
      this.mOrderListFragments.clear();
    super.onDestroyView();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mTabUpdateRequest)
      return;
    super.onRequestFailed(paramMApiRequest, paramMApiResponse);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mTabUpdateRequest)
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (DPObjectUtils.isDPObjectof(paramMApiRequest, "UniOrderTabsInfo"))
      {
        paramMApiRequest = paramMApiRequest.getArray("TabsInfo");
        if (paramMApiRequest != null)
          break label41;
      }
      while (true)
      {
        return;
        label41: int j = paramMApiRequest.length;
        int i = 0;
        while (i < j)
        {
          paramMApiResponse = paramMApiRequest[i];
          updateTabLabel(paramMApiResponse.getString("ID"), paramMApiResponse.getString("Name"));
          i += 1;
        }
      }
    }
    super.onRequestFinish(paramMApiRequest, paramMApiResponse);
  }

  public void onTabChanged(String paramString)
  {
    super.onTabChanged(paramString);
    GAHelper.instance().contextStatisticsEvent(getActivity(), paramString, null, "tap");
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    setupViews();
  }

  public void updateTabLabel(String paramString1, String paramString2)
  {
    if ((TextUtils.isEmpty(paramString1)) || (TextUtils.isEmpty(paramString2)));
    do
    {
      return;
      int i = ((Integer)this.mTabToIndex.get(paramString1)).intValue();
      paramString1 = (TextView)this.mTabHost.getTabWidget().getChildAt(i);
      paramString1.setText(paramString2);
      paramString2 = paramString1.getLayout();
    }
    while ((paramString2 == null) || (paramString2.getEllipsisCount(0) <= 0));
    paramString1.setTextSize(2, 12.0F);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pay.fragment.OrderCenterTabFragment
 * JD-Core Version:    0.6.0
 */