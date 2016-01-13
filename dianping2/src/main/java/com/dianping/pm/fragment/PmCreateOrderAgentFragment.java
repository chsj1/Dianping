package com.dianping.pm.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.tuan.fragment.TuanAgentFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.DPScrollView;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.pm.config.CreatePointOrderConfig;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PmCreateOrderAgentFragment extends TuanAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, AgentFragment.CellStable
{
  public static final int REQUEST_CODE_CHANGE_DELIVERY = 101;
  protected ViewGroup bottomCellContainer;
  protected LinearLayout bottomView;
  protected MApiRequest orderReqest;
  private ArrayList<DPObject> orderTags = new ArrayList();
  protected int productGroupId;
  protected int productId;
  protected int productType;
  protected LinearLayout rootView;
  protected DPScrollView scrollView;
  protected int submitBtnHeight;

  protected void applyOrderInfo()
  {
    if (this.orderReqest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("orderapplicationpm.bin");
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("productgroupid", Integer.valueOf(this.productGroupId));
    localUrlBuilder.addParam("producttype", Integer.valueOf(this.productType));
    localUrlBuilder.addParam("productid", Integer.valueOf(this.productId));
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    this.orderReqest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.orderReqest, this);
    showProgressDialog("正在获取订单信息...");
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new CreatePointOrderConfig());
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.productGroupId != 0)
      applyOrderInfo();
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 101) && (paramInt2 == -1) && (paramIntent != null))
    {
      ArrayList localArrayList = paramIntent.getParcelableArrayListExtra("mDeliveryList");
      paramIntent = (DPObject)paramIntent.getParcelableExtra("selectedDelivery");
      if ((localArrayList != null) && (paramIntent != null))
        onDeliveryListFragmentDismissed(paramIntent, localArrayList);
    }
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.title_button)
      getActivity().finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!isLogined())
    {
      Toast.makeText(getActivity(), "请先登录点评账号", 0).show();
      getActivity().finish();
      return;
    }
    if (paramBundle == null)
    {
      this.productGroupId = getIntParam("productgroupid", 0);
      this.productType = getIntParam("producttype", 0);
      this.productId = getIntParam("productid", 0);
      return;
    }
    this.productGroupId = paramBundle.getInt("productGroupId");
    this.productType = paramBundle.getInt("productType");
    this.productId = paramBundle.getInt("productId");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.pm_create_point_order_container, paramViewGroup, false);
    this.rootView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.content));
    this.rootView.setBackgroundResource(R.drawable.main_background);
    this.scrollView = ((DPScrollView)paramLayoutInflater.findViewById(R.id.scrollview));
    this.bottomView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.bottom_view));
    this.bottomCellContainer = ((ViewGroup)View.inflate(getActivity(), R.layout.tuan_agent_cell_parent, null));
    this.bottomCellContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
    this.bottomView.addView(this.bottomCellContainer);
    setAgentContainerView(this.rootView);
    this.rootView.getViewTreeObserver().addOnGlobalLayoutListener(new PmCreateOrderAgentFragment.1(this));
    this.bottomCellContainer.setVisibility(0);
    return paramLayoutInflater;
  }

  public void onDeliveryListFragmentDismissed(DPObject paramDPObject, List<DPObject> paramList)
  {
    AgentMessage localAgentMessage = new AgentMessage(" onDeliveryListFragmentDismissed");
    localAgentMessage.body.putParcelable("selectedDelivery", paramDPObject);
    localAgentMessage.body.putParcelableArrayList("deliveryList", (ArrayList)paramList);
    dispatchMessage(localAgentMessage);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.orderReqest != null)
    {
      mapiService().abort(this.orderReqest, this, true);
      this.orderReqest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (this.orderReqest == paramMApiRequest)
      this.orderReqest = null;
    if (paramMApiResponse != null)
    {
      paramMApiRequest = paramMApiResponse.message();
      if (paramMApiRequest != null)
        Toast.makeText(getContext().getApplicationContext(), paramMApiRequest.content(), 0).show();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.orderReqest)
    {
      dismissDialog();
      this.orderReqest = null;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (DPObjectUtils.isDPObjectof(paramMApiRequest, "PointProductOrderApplication"))
      {
        paramMApiResponse = new Bundle();
        getTitleBar().setTitle(paramMApiRequest.getString("PageText"));
        paramMApiResponse.putString("buttonText", paramMApiRequest.getString("ButtonText"));
        paramMApiResponse.putInt("maxBuyCount", paramMApiRequest.getInt("MaxBuyCount"));
        paramMApiResponse.putInt("minBuyCount", paramMApiRequest.getInt("MinBuyCount"));
        paramMApiResponse.putString("totalPointDesc", paramMApiRequest.getString("TotalPointDesc"));
        paramMApiResponse.putString("title", paramMApiRequest.getString("Title"));
        paramMApiResponse.putInt("pointPrice", paramMApiRequest.getInt("PointPrice"));
        paramMApiResponse.putDouble("originalPrice", paramMApiRequest.getDouble("OriginalPrice"));
        if (paramMApiRequest.getArray("Tags") != null)
          this.orderTags.addAll(Arrays.asList(paramMApiRequest.getArray("Tags")));
        paramMApiResponse.putParcelableArray("deliveryType", paramMApiRequest.getArray("DeliveryType"));
        paramMApiResponse.putParcelableArrayList("orderTags", this.orderTags);
        paramMApiResponse.putInt("totalPoint", paramMApiRequest.getInt("TotalPoint"));
        if (paramMApiRequest.getInt("ProductId") != 0)
          this.productId = paramMApiRequest.getInt("ProductId");
        if (paramMApiRequest.getInt("ProductType") != 0)
          this.productType = paramMApiRequest.getInt("ProductType");
        if (paramMApiRequest.getInt("ProductGroupId") != 0)
          this.productGroupId = paramMApiRequest.getInt("ProductGroupId");
        paramMApiResponse.putInt("productGroupId", this.productGroupId);
        paramMApiResponse.putInt("productType", this.productType);
        paramMApiResponse.putInt("productId", this.productId);
        paramMApiResponse.putBoolean("netResponse", true);
        dispatchAgentChanged(null, paramMApiResponse);
      }
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("productGroupId", this.productGroupId);
    paramBundle.putInt("productType", this.productType);
    paramBundle.putInt("productId", this.productId);
  }

  public void setBottomCell(View paramView, CellAgent paramCellAgent)
  {
    this.bottomCellContainer.removeAllViews();
    this.bottomCellContainer.addView(paramView);
    this.bottomView.setVisibility(0);
  }

  public void setTopCell(View paramView, CellAgent paramCellAgent)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pm.fragment.PmCreateOrderAgentFragment
 * JD-Core Version:    0.6.0
 */