package com.dianping.tuan.fragment;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.fragment.BaseTuanTabPagerFragment;
import com.dianping.base.tuan.fragment.BaseTuanTabPagerFragment.TabsAdapter;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TuanOrderListFragment extends BaseTuanTabPagerFragment
  implements FullRequestHandle<MApiRequest, MApiResponse>
{
  TuanOrderCommonFragment allFragment;
  private DPObject mLuckyMoneyObj;
  protected final BroadcastReceiver mLuckyMoneyReceiver = new TuanOrderListFragment.1(this);
  private MApiRequest mLuckyMoneyRequest;
  private View mLuckyMoneyView;
  private ViewGroup mTopLayout;
  TuanOrderCommonFragment refundFragment;
  TuanOrderCommonFragment unpaidFragment;
  TuanOrderCommonFragment unusedFragment;

  private void queryLuckyMoneyInfo()
  {
    if (this.mLuckyMoneyRequest != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder("http://app.t.dianping.com/");
    localStringBuilder.append("orderlistmodulegn.bin");
    localStringBuilder.append("?token=").append(accountService().token());
    this.mLuckyMoneyRequest = mapiGet(this, localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.mLuckyMoneyRequest, this);
  }

  private void setupView()
  {
    this.mTabHost.setVisibility(0);
    if (this.mTabHost.getTabWidget().getChildCount() == 0)
    {
      this.unpaidFragment = new TuanOrderCommonFragment();
      this.unpaidFragment.setFragmentFilter(1);
      this.unusedFragment = new TuanOrderCommonFragment();
      this.unusedFragment.setFragmentFilter(4);
      this.allFragment = new TuanOrderCommonFragment();
      this.allFragment.setFragmentFilter(0);
      this.refundFragment = new TuanOrderCommonFragment();
      this.refundFragment.setFragmentFilter(5);
      addTab("全部", R.layout.tuan_common_tab_indicator, this.allFragment, null);
      addTab("待付款", R.layout.tuan_common_tab_indicator, this.unpaidFragment, null);
      addTab("未消费", R.layout.tuan_common_tab_indicator, this.unusedFragment, null);
      addTab("退款单", R.layout.tuan_common_tab_indicator, this.refundFragment, null);
    }
    this.mTopLayout = ((ViewGroup)getView().findViewById(R.id.tab_and_content));
    this.mLuckyMoneyView = View.inflate(getActivity(), R.layout.layout_lucky_money, null);
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.dianping.orderdetail.lucky_money_clicked");
    registerReceiver(this.mLuckyMoneyReceiver, localIntentFilter);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setupView();
    queryLuckyMoneyInfo();
  }

  public void onDestroyView()
  {
    super.onDestroyView();
    if (this.mLuckyMoneyReceiver != null)
      unregisterReceiver(this.mLuckyMoneyReceiver);
    if (this.mLuckyMoneyRequest != null)
    {
      mapiService().abort(this.mLuckyMoneyRequest, this, true);
      this.mLuckyMoneyRequest = null;
    }
  }

  public void onHiddenChanged(boolean paramBoolean)
  {
    super.onHiddenChanged(paramBoolean);
    if (!paramBoolean)
    {
      if (this.mTabHost.getCurrentTab() == 2)
        getActivityTitleBar().findRightViewItemByTag("remove").setVisibility(8);
    }
    else
      return;
    getActivityTitleBar().findRightViewItemByTag("remove").setVisibility(0);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mLuckyMoneyRequest)
      this.mLuckyMoneyRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.mLuckyMoneyRequest)
    {
      this.mLuckyMoneyRequest = null;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "PageModule"))
      {
        this.mLuckyMoneyObj = ((DPObject)paramMApiResponse).getObject("LuckyMoney");
        if (this.mLuckyMoneyObj != null)
          break label50;
      }
    }
    return;
    label50: paramMApiRequest = this.mLuckyMoneyObj.getString("TitleJL");
    paramMApiResponse = (TextView)this.mLuckyMoneyView.findViewById(R.id.title);
    if (paramMApiRequest != null)
    {
      paramMApiRequest = com.dianping.util.TextUtils.jsonParseText(paramMApiRequest);
      paramMApiResponse.setText(paramMApiRequest);
      paramMApiRequest = this.mLuckyMoneyObj.getString("SubTitleJL");
      paramMApiResponse = (TextView)this.mLuckyMoneyView.findViewById(R.id.subtitle);
      if (android.text.TextUtils.isEmpty(paramMApiRequest))
        break label238;
      paramMApiResponse.setVisibility(0);
      paramMApiResponse.setText(com.dianping.util.TextUtils.jsonParseText(paramMApiRequest));
    }
    while (true)
    {
      paramMApiResponse = this.mLuckyMoneyObj.getString("ButtonText");
      TextView localTextView = (TextView)this.mLuckyMoneyView.findViewById(R.id.btn_send_lucky_money);
      paramMApiRequest = paramMApiResponse;
      if (paramMApiResponse == null)
        paramMApiRequest = "发红包";
      localTextView.setText(paramMApiRequest);
      this.mLuckyMoneyView.setOnClickListener(new TuanOrderListFragment.2(this));
      paramMApiRequest = new LinearLayout.LayoutParams(-1, -2);
      paramMApiRequest.setMargins(0, 0, 0, ViewUtils.dip2px(getActivity(), 12.0F));
      this.mTopLayout.addView(this.mLuckyMoneyView, 0, paramMApiRequest);
      return;
      paramMApiRequest = "";
      break;
      label238: paramMApiResponse.setVisibility(8);
    }
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    if (paramMApiRequest == this.mLuckyMoneyRequest)
      this.mTopLayout.removeView(this.mLuckyMoneyView);
  }

  public void onTabChanged(String paramString)
  {
    super.onTabChanged(paramString);
    if ((getActivityTitleBar() != null) && (getActivityTitleBar().findRightViewItemByTag("remove") != null))
    {
      if (this.mTabHost.getCurrentTab() == 2)
        getActivityTitleBar().findRightViewItemByTag("remove").setVisibility(8);
    }
    else
      return;
    getActivityTitleBar().findRightViewItemByTag("remove").setVisibility(0);
  }

  public void setCurrentTab(String paramString)
  {
    if (paramString != null)
    {
      if (!"all".equals(paramString))
        break label23;
      this.mTabHost.setCurrentTab(0);
    }
    label23: 
    do
    {
      return;
      if ("unpaid".equals(paramString))
      {
        this.mTabHost.setCurrentTab(1);
        return;
      }
      if ((!"paid".equals(paramString)) && (!"unused".equals(paramString)))
        continue;
      this.mTabHost.setCurrentTab(2);
      return;
    }
    while (!"refund".equals(paramString));
    this.mTabHost.setCurrentTab(3);
  }

  public void showEditView(boolean paramBoolean)
  {
    boolean bool2 = true;
    int i = 0;
    Object localObject = this.mTabHost.getTabWidget();
    if (!paramBoolean)
    {
      bool1 = true;
      ((TabWidget)localObject).setEnabled(bool1);
      if (paramBoolean)
        break label92;
    }
    label92: for (boolean bool1 = bool2; ; bool1 = false)
    {
      setCanScroll(bool1);
      ((TuanOrderCommonFragment)tabsAdapter().getItem(this.mTabHost.getCurrentTab())).showEditView(paramBoolean);
      if (this.mLuckyMoneyObj != null)
      {
        localObject = this.mLuckyMoneyView;
        if (paramBoolean)
          i = 8;
        ((View)localObject).setVisibility(i);
      }
      return;
      bool1 = false;
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.TuanOrderListFragment
 * JD-Core Version:    0.6.0
 */