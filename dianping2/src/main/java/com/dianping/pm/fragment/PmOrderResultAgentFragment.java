package com.dianping.pm.fragment;

import android.os.Bundle;
import android.os.Handler;
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
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.pm.config.DefaultPmOrderResultConfig;
import com.dianping.pm.util.OrderResultHelper;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PmOrderResultAgentFragment extends TuanAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static final int MSG_RETRY_QUERYORDER = 100;
  public static final int ORDER_STATUS_FAIL = 2;
  public static final int ORDER_STATUS_SUCCESS = 1;
  public static final int ORDER_STATUS_UNKNOWN = 0;
  public static final int QUERYORDER_REQUEST_RETRY_COUNT = 2;
  private final String TAG = "OrderResultAgentFragment";
  ArrayList<AgentListConfig> agentListConfigs = new ArrayList();
  protected LinearLayout contentView;
  protected DPObject dpPointProductOrderResult;
  protected View loadingView;
  protected Handler mHandler = new PmOrderResultAgentFragment.1(this);
  protected int orderId;
  protected int orderStatus = 0;
  protected MApiRequest pointProductOrderResultRequest;
  protected int productType;
  protected int queryOrderResultCount = 0;
  protected LinearLayout rootView;

  private void queryPointProductOrderResult()
  {
    if (this.pointProductOrderResultRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("orderresultpm.bin");
    localUrlBuilder.addParam("orderid", Integer.valueOf(this.orderId));
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    localUrlBuilder.addParam("producttype", Integer.valueOf(this.productType));
    localUrlBuilder.addParam("token", accountService().token());
    this.pointProductOrderResultRequest = new BasicMApiRequest(localUrlBuilder.buildUrl(), "GET", null, CacheType.DISABLED, false, null);
    mapiService().exec(this.pointProductOrderResultRequest, this);
    this.loadingView.setVisibility(0);
    this.contentView.setVisibility(8);
  }

  protected void dispatchDataChanged()
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("orderid", this.orderId);
    localBundle.putInt("producttype", this.productType);
    if (this.dpPointProductOrderResult != null)
      localBundle.putParcelable("pointproductorderresult", this.dpPointProductOrderResult);
    dispatchAgentChanged(null, localBundle);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    return this.agentListConfigs;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.orderId <= 0)
    {
      getActivity().finish();
      return;
    }
    getActivity().setTitle("");
    queryPointProductOrderResult();
  }

  public void onCreate(Bundle paramBundle)
  {
    Log.i("OrderResultAgentFragment", "onCreate");
    super.onCreate(paramBundle);
    this.orderId = getIntParam("orderid");
    this.productType = getIntParam("producttype");
    this.agentListConfigs.clear();
    this.agentListConfigs.add(new DefaultPmOrderResultConfig());
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.base_agent_container, paramViewGroup, false);
    this.rootView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.content));
    this.rootView.setBackgroundResource(R.drawable.main_background);
    this.loadingView = paramLayoutInflater.findViewById(R.id.loading);
    this.contentView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.content));
    setAgentContainerView(this.rootView);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.pointProductOrderResultRequest != null)
    {
      mapiService().abort(this.pointProductOrderResultRequest, this, true);
      this.pointProductOrderResultRequest = null;
    }
  }

  public boolean onGoBack()
  {
    String str = "dianping://web?url=" + URLEncoder.encode("http://h5.dianping.com/tuan/score/mall/index.html");
    OrderResultHelper.forwardUrlSchema(getActivity(), str);
    return super.onGoBack();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.pointProductOrderResultRequest)
    {
      this.pointProductOrderResultRequest = null;
      if (this.queryOrderResultCount >= 2)
        break label52;
      this.mHandler.sendEmptyMessageDelayed(100, 3000L);
      this.queryOrderResultCount += 1;
    }
    label52: 
    do
    {
      return;
      this.loadingView.setVisibility(8);
      this.contentView.setVisibility(0);
    }
    while (getActivity() == null);
    if ((paramMApiResponse != null) && (!TextUtils.isEmpty(paramMApiResponse.content())))
      Toast.makeText(getActivity(), paramMApiResponse.content(), 0).show();
    OrderResultHelper.forwardUrlSchema(getActivity(), "dianping://pmorderlist");
    getActivity().finish();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.pointProductOrderResultRequest)
    {
      this.pointProductOrderResultRequest = null;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "PointProductOrderResult"))
      {
        this.dpPointProductOrderResult = ((DPObject)paramMApiResponse);
        this.orderStatus = this.dpPointProductOrderResult.getInt("Status");
        if ((this.orderStatus != 0) || (this.queryOrderResultCount >= 2))
          break label91;
        this.mHandler.sendEmptyMessageDelayed(100, 3000L);
        this.queryOrderResultCount += 1;
      }
    }
    return;
    label91: this.loadingView.setVisibility(8);
    this.contentView.setVisibility(0);
    resetAgents(null);
    dispatchDataChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pm.fragment.PmOrderResultAgentFragment
 * JD-Core Version:    0.6.0
 */