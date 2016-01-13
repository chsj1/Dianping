package com.dianping.tuan.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.tuan.fragment.TuanAgentFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class CouponDetailAgentFragment extends TuanAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected BroadcastReceiver broadcastReceiver = new CouponDetailAgentFragment.1(this);
  private int couponId;
  private DPObject dpCoupon;
  protected IntentFilter intentFilter;
  private MApiRequest loadCouponRequest;
  private LinearLayout rootView;

  private void dispatchCouponChanged()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("coupon", this.dpCoupon);
    if (this.dpCoupon != null)
      localBundle.putParcelable("deal", this.dpCoupon.getObject("RelativeDeal"));
    dispatchAgentChanged(null, localBundle);
  }

  private void loadCouponDetail(int paramInt)
  {
    if (this.loadCouponRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("coupongn.bin");
    localUrlBuilder.addParam("couponid", Integer.valueOf(paramInt));
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    localUrlBuilder.addParam("token", accountService().token());
    this.loadCouponRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.CRITICAL);
    mapiService().exec(this.loadCouponRequest, this);
    showProgressDialog("正在获取团购券详情...");
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new CouponDetailAgentFragment.2(this));
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.dpCoupon != null)
      dispatchCouponChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
      this.dpCoupon = ((DPObject)paramBundle.getParcelable("coupon"));
    while (true)
    {
      this.intentFilter = new IntentFilter("com.dianping.tuan.orderdetail_refresh");
      this.intentFilter.addAction("com.dianping.tuan.refund_succeed");
      this.intentFilter.addAction("tuan:order_refund_status_changed");
      getContext().registerReceiver(this.broadcastReceiver, this.intentFilter);
      return;
      this.couponId = getIntParam("couponid", 0);
      paramBundle = getObjectParam("coupon");
      if (paramBundle != null)
        this.couponId = paramBundle.getInt("ID");
      if (this.couponId == 0)
      {
        Toast.makeText(getActivity(), "参数传递有误", 0).show();
        getActivity().finish();
        return;
      }
      loadCouponDetail(this.couponId);
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.base_agent_container, paramViewGroup, false);
    this.rootView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.content));
    setAgentContainerView(this.rootView);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    if (this.broadcastReceiver != null)
      getContext().unregisterReceiver(this.broadcastReceiver);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.loadCouponRequest)
    {
      this.loadCouponRequest = null;
      if (getActivity() != null)
        Toast.makeText(getActivity(), paramMApiResponse.message().content(), 0).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.loadCouponRequest)
    {
      this.loadCouponRequest = null;
      this.dpCoupon = ((DPObject)paramMApiResponse.result());
      dispatchCouponChanged();
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if ((paramBundle != null) && (this.dpCoupon != null))
      paramBundle.putParcelable("coupon", this.dpCoupon);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.CouponDetailAgentFragment
 * JD-Core Version:    0.6.0
 */