package com.dianping.tuan.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.fragment.BaseTuanFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.tuan.widget.CustomViewPager;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingItem;

public class TuanMeiOrderListFragment extends BaseTuanFragment
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected TuanMeiOrderListFragment.FragmentAdapter mFragmentAdapter;
  private boolean mHasDoneRequest = false;
  private LoadingErrorView mLoadingErrorView;
  private LoadingItem mLoadingView;
  protected MApiRequest mMeiTuanOrderListRequest;
  private ProgressBar mProgressBar;
  private String mUrl;
  protected CustomViewPager mViewPager;
  private FrameLayout mWebViewContent;
  private NovaZeusFragment mWebViewFragment;

  private void sendRequest()
  {
    if (this.mMeiTuanOrderListRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/uc/");
    localUrlBuilder.appendPath("meituanorderlistgn.bin");
    localUrlBuilder.addParam("token", accountService().token());
    this.mMeiTuanOrderListRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.mMeiTuanOrderListRequest, this);
    this.mHasDoneRequest = true;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.mLoadingView.setVisibility(0);
    this.mLoadingErrorView.setVisibility(8);
  }

  public void onBackPressed()
  {
    if ((onGoBack()) && (this.mWebViewFragment != null))
      this.mWebViewFragment.goBack();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.tuan_meituan_order_list_layout, null, false);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mMeiTuanOrderListRequest != null)
    {
      mapiService().abort(this.mMeiTuanOrderListRequest, this, true);
      this.mMeiTuanOrderListRequest = null;
    }
  }

  public boolean onGoBack()
  {
    if (this.mWebViewFragment == null)
      return false;
    return this.mWebViewFragment.canGoBack();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mMeiTuanOrderListRequest)
    {
      if (this.mLoadingView != null)
        this.mLoadingView.setVisibility(8);
      if (this.mLoadingErrorView != null)
      {
        paramMApiRequest = paramMApiResponse.message();
        if ((paramMApiRequest != null) && (!TextUtils.isEmpty(paramMApiRequest.content())))
          this.mLoadingErrorView.setErrorMessage(paramMApiRequest.content());
        this.mLoadingErrorView.setVisibility(0);
      }
      this.mMeiTuanOrderListRequest = null;
      this.mWebViewContent.setVisibility(4);
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mMeiTuanOrderListRequest)
    {
      if (this.mLoadingView != null)
        this.mLoadingView.setVisibility(8);
      if (this.mLoadingErrorView != null)
        this.mLoadingErrorView.setVisibility(8);
      this.mMeiTuanOrderListRequest = null;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      this.mWebViewContent.setVisibility(4);
      if (DPObjectUtils.isDPObjectof(paramMApiRequest, "MeiTuanOrderListTab"))
      {
        paramMApiRequest = paramMApiRequest.getString("RedirectUrl");
        if ((!TextUtils.isEmpty(paramMApiRequest)) && (this.mWebViewFragment != null) && (this.mWebViewFragment.isResumed()))
          break label145;
        this.mLoadingErrorView.setVisibility(0);
        this.mLoadingErrorView.setErrorMessage("没有请求到订单信息");
        if ((this.mWebViewFragment == null) || (!this.mWebViewFragment.isResumed()))
          this.mFragmentAdapter.notifyDataSetChanged();
      }
    }
    return;
    label145: this.mUrl = paramMApiRequest;
    this.mWebViewFragment.replaceTitleBar(new TuanMeiOrderListFragment.ProgressTitleBar(this, getContext()));
    this.mWebViewContent.setVisibility(0);
    this.mWebViewFragment.loadUrl(this.mUrl);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.mLoadingView = ((LoadingItem)paramView.findViewById(R.id.loading_view));
    this.mLoadingErrorView = ((LoadingErrorView)paramView.findViewById(R.id.loading_error_view));
    this.mLoadingErrorView.setCallBack(new TuanMeiOrderListFragment.1(this));
    this.mLoadingView.setVisibility(8);
    this.mLoadingErrorView.setVisibility(8);
    this.mWebViewContent = ((FrameLayout)paramView.findViewById(R.id.webview_content));
    this.mProgressBar = ((ProgressBar)paramView.findViewById(R.id.pb_progress));
    this.mViewPager = ((CustomViewPager)paramView.findViewById(R.id.fragment_viewpager));
    this.mFragmentAdapter = new TuanMeiOrderListFragment.FragmentAdapter(this, getChildFragmentManager());
    this.mViewPager.setAdapter(this.mFragmentAdapter);
    this.mFragmentAdapter.notifyDataSetChanged();
    this.mViewPager.setCurrentItem(0);
    this.mWebViewContent.setVisibility(4);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.TuanMeiOrderListFragment
 * JD-Core Version:    0.6.0
 */