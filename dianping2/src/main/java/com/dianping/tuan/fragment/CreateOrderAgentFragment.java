package com.dianping.tuan.fragment;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
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
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.tuan.config.BookingCreateOrderConfig;
import com.dianping.tuan.config.DefaultCreateOrderConfig;
import com.dianping.tuan.config.ScenicCreateOrderConfig;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderAgentFragment extends TuanAgentFragment
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, AgentFragment.CellStable
{
  public static final int REQUEST_CODE_CHANGE_DELIVERY = 101;
  private final String TAG = "CreateOrderAgentFragment";
  protected ViewGroup bottomCellContainer;
  protected LinearLayout bottomView;
  protected String callBackFailUrl;
  protected int callBackType;
  protected String callBackUrl;
  protected MApiRequest cancelOrderReqest;
  protected MApiRequest dealRequest;
  protected int dealType;
  protected int dealid;
  protected int dealselectid;
  protected DPObject dpDeal;
  protected DPObject dpDealSelect;
  protected DPObject dpOrder;
  protected Bundle extraData;
  protected MApiRequest orderReqest;
  protected LinearLayout rootView;
  protected DPScrollView scrollView;
  protected int submitBtnHeight;
  protected ViewGroup topCellContainer;
  protected LinearLayout topView;

  protected void cancelOrder()
  {
    if (this.cancelOrderReqest != null)
    {
      Log.i("CreateOrderAgentFragment", "already requesting");
      return;
    }
    if (this.dpOrder == null)
    {
      getActivity().finish();
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://app.t.dianping.com/");
    localStringBuilder.append("cancelordergn.bin");
    this.cancelOrderReqest = mapiPost(this, localStringBuilder.toString(), new String[] { "orderid", "" + this.dpOrder.getInt("ID"), "token", accountService().token() });
    mapiService().exec(this.cancelOrderReqest, this);
    showProgressDialog("正在取消订单...");
  }

  protected void dispatchDataChanged()
  {
    Bundle localBundle = new Bundle();
    if (this.dpDeal != null)
      localBundle.putParcelable("deal", this.dpDeal);
    if (this.dpDealSelect != null)
      localBundle.putParcelable("dealselect", this.dpDealSelect);
    if (this.dpOrder != null)
      localBundle.putParcelable("order", this.dpOrder);
    if (this.extraData != null)
      localBundle.putBundle("extradata", this.extraData);
    if (this.callBackUrl != null)
      localBundle.putString("callbackurl", this.callBackUrl);
    if (this.callBackFailUrl != null)
      localBundle.putString("callbackfailurl", this.callBackFailUrl);
    localBundle.putInt("callbacktype", this.callBackType);
    localBundle.putInt("dealtype", this.dealType);
    localBundle.putInt("promodeskheadstyle", 1);
    localBundle.putInt("promodeskotherpromoheadstyle", 0);
    localBundle.putInt("promodeskfootstyle", 1);
    localBundle.putInt("promodeskforceautopromo", 0);
    dispatchAgentChanged(null, localBundle);
  }

  protected void fetchDeal()
  {
    if (this.dealRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("dealbaseinfogn.bin");
    localUrlBuilder.addParam("id", Integer.valueOf(this.dealid));
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    Object localObject = accountService().token();
    if (!TextUtils.isEmpty((CharSequence)localObject))
      localUrlBuilder.addParam("token", localObject);
    localObject = location();
    if (localObject != null)
    {
      localUrlBuilder.addParam("lat", Double.valueOf(((Location)localObject).latitude()));
      localUrlBuilder.addParam("lng", Double.valueOf(((Location)localObject).longitude()));
    }
    this.dealRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.dealRequest, this);
    showProgressDialog("正在获取订单信息...");
  }

  protected void fetchOrder(DPObject paramDPObject)
  {
    if (this.orderReqest != null)
    {
      Log.i("CreateOrderAgentFragment", "already requesting");
      return;
    }
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("ordergn.bin");
    localUrlBuilder.addParam("dealtype", Integer.valueOf(this.dealType));
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("orderid", Integer.valueOf(paramDPObject.getInt("ID")));
    localUrlBuilder.addParam("cityid", Integer.valueOf(cityId()));
    this.orderReqest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.CRITICAL);
    mapiService().exec(this.orderReqest, this);
    showProgressDialog("正在获取订单信息...");
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new ScenicCreateOrderConfig(this.dpDeal));
    localArrayList.add(new BookingCreateOrderConfig(this.dpDeal, this.dpDealSelect, this.extraData, this));
    localArrayList.add(new DefaultCreateOrderConfig());
    return localArrayList;
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
    AgentMessage localAgentMessage = new AgentMessage("onAccountSwitched");
    localAgentMessage.body.putParcelable("UserProfile", paramUserProfile);
    dispatchMessage(localAgentMessage);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.dealid == 0)
    {
      if ((this.dpDeal == null) && (this.dpOrder == null))
      {
        getActivity().finish();
        return;
      }
      if ((this.dpDeal != null) && (this.dpDealSelect == null))
      {
        getActivity().finish();
        return;
      }
    }
    else
    {
      fetchDeal();
    }
    if (this.dpOrder != null)
    {
      if (!isLogined())
      {
        getActivity().finish();
        return;
      }
      if (this.dpOrder.getInt("ID") <= 0)
      {
        getActivity().finish();
        return;
      }
      fetchOrder(this.dpOrder);
    }
    while (true)
    {
      getActivity().getSupportFragmentManager().addOnBackStackChangedListener(new CreateOrderAgentFragment.2(this));
      return;
      if (this.dpDeal == null)
        continue;
      dispatchDataChanged();
    }
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
      new AlertDialog.Builder(getActivity()).setMessage("您确定要取消此订单吗？").setPositiveButton("取消订单", new CreateOrderAgentFragment.4(this)).setNegativeButton("返回", new CreateOrderAgentFragment.3(this)).show();
  }

  public void onCreate(Bundle paramBundle)
  {
    Log.i("CreateOrderAgentFragment", "onCreate");
    super.onCreate(paramBundle);
    this.extraData = ((Bundle)getActivity().getIntent().getParcelableExtra("extradata"));
    this.dpDeal = ((DPObject)getActivity().getIntent().getParcelableExtra("deal"));
    this.dpOrder = ((DPObject)getActivity().getIntent().getParcelableExtra("order"));
    this.callBackUrl = getStringParam("callbackurl");
    this.callBackFailUrl = getStringParam("callbackfailurl");
    this.callBackType = getIntParam("callbacktype", 0);
    this.dealid = getIntParam("dealid", 0);
    this.dealselectid = getIntParam("dealselectid", 0);
    if ((this.dpDeal == null) && (this.dpOrder == null))
    {
      int i = getIntParam("orderid", 0);
      int j = getIntParam("dealtype", 1);
      if (i > 0)
        this.dpOrder = new DPObject("Order").edit().putInt("ID", i).putInt("DealType", j).generate();
    }
    if (this.dpOrder != null)
      this.dealType = this.dpOrder.getInt("DealType");
    do
    {
      do
        return;
      while (this.dpDeal == null);
      this.dealType = this.dpDeal.getInt("DealType");
      this.dpDealSelect = ((DPObject)getActivity().getIntent().getParcelableExtra("dealSelect"));
    }
    while ((this.dpDealSelect != null) || (DPObjectUtils.isResultListEmpty(this.dpDeal, "DealSelectList")));
    this.dpDealSelect = this.dpDeal.getArray("DealSelectList")[0];
  }

  public void onCreateTitleBar(TitleBar paramTitleBar)
  {
    paramTitleBar.removeAllRightViewItem();
    if ((this.dpOrder != null) && (getActivity() != null))
    {
      TextView localTextView = (TextView)LayoutInflater.from(getActivity()).inflate(R.layout.text_title_bar_item, null, false);
      localTextView.setText("取消");
      localTextView.setId(R.id.title_button);
      paramTitleBar.addRightViewItem(localTextView, "cancel_order", this);
    }
    paramTitleBar.findViewById(R.id.title_bar_left_view_container).setVisibility(0);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.createorder_agent_container, paramViewGroup, false);
    this.rootView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.content));
    this.rootView.setBackgroundResource(R.drawable.main_background);
    this.scrollView = ((DPScrollView)paramLayoutInflater.findViewById(R.id.scrollview));
    this.topView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.top_view));
    this.topCellContainer = ((ViewGroup)View.inflate(getActivity(), R.layout.tuan_agent_cell_parent, null));
    this.topCellContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
    this.topView.setVisibility(8);
    this.topView.addView(this.topCellContainer);
    this.bottomView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.bottom_view));
    this.bottomCellContainer = ((ViewGroup)View.inflate(getActivity(), R.layout.tuan_agent_cell_parent, null));
    this.bottomCellContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
    this.bottomView.addView(this.bottomCellContainer);
    setAgentContainerView(this.rootView);
    this.rootView.getViewTreeObserver().addOnGlobalLayoutListener(new CreateOrderAgentFragment.1(this));
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
    if (this.cancelOrderReqest != null)
    {
      mapiService().abort(this.cancelOrderReqest, this, true);
      this.cancelOrderReqest = null;
    }
  }

  public void onLogin(boolean paramBoolean)
  {
    AgentMessage localAgentMessage = new AgentMessage("loginResult");
    localAgentMessage.body.putBoolean("loginresult", paramBoolean);
    dispatchMessage(localAgentMessage);
  }

  protected void onProgressDialogCancel()
  {
    dispatchMessage(new AgentMessage("onProgressDialogCancel"));
    if (this.orderReqest != null)
    {
      mapiService().abort(this.orderReqest, this, true);
      this.orderReqest = null;
      getActivity().finish();
    }
    if (this.cancelOrderReqest != null)
    {
      mapiService().abort(this.cancelOrderReqest, this, true);
      this.cancelOrderReqest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    paramMApiResponse = paramMApiResponse.message();
    setSharedObject("errorMsg", paramMApiResponse.content());
    if (this.orderReqest == paramMApiRequest)
    {
      this.orderReqest = null;
      resetAgents(null);
    }
    while (paramMApiResponse.flag() == 1)
    {
      new AlertDialog.Builder(getActivity()).setTitle(paramMApiResponse.title()).setMessage(paramMApiResponse.content()).setPositiveButton("确定", new CreateOrderAgentFragment.5(this)).setCancelable(false).show();
      return;
      if (this.dealRequest == paramMApiRequest)
      {
        this.dealRequest = null;
        resetAgents(null);
        continue;
      }
      if (this.cancelOrderReqest != paramMApiRequest)
        continue;
      this.cancelOrderReqest = null;
    }
    Toast.makeText(getActivity().getApplicationContext(), (String)sharedObject("errorMsg"), 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      dismissDialog();
      if (paramMApiRequest != this.orderReqest)
        break label126;
      this.dpOrder = paramMApiResponse;
      this.dealType = this.dpOrder.getInt("DealType");
      this.dpDeal = this.dpOrder.getObject("RelativeDeal");
      if ((this.dpDealSelect == null) && (!DPObjectUtils.isResultListEmpty(this.dpDeal, "DealSelectList")))
        this.dpDealSelect = this.dpDeal.getArray("DealSelectList")[0];
      this.orderReqest = null;
      if (this.dpDealSelect != null)
        break label116;
    }
    label116: label126: 
    do
    {
      do
      {
        return;
        resetAgents(null);
        dispatchDataChanged();
        return;
        if (paramMApiRequest != this.cancelOrderReqest)
          continue;
        this.cancelOrderReqest = null;
        Toast.makeText(getActivity(), paramMApiResponse.getString("Content"), 0).show();
        getActivity().setResult(-1);
        getActivity().finish();
        return;
      }
      while (paramMApiRequest != this.dealRequest);
      this.dpDeal = paramMApiResponse;
      this.dealType = this.dpDeal.getInt("DealType");
      if ((this.dpDealSelect == null) && (!DPObjectUtils.isResultListEmpty(this.dpDeal, "DealSelectList")))
      {
        paramMApiRequest = this.dpDeal.getArray("DealSelectList");
        int i = 0;
        while (i < paramMApiRequest.length)
        {
          if (paramMApiRequest[i].getInt("ID") == this.dealselectid)
            this.dpDealSelect = paramMApiRequest[i];
          i += 1;
        }
        if (this.dpDealSelect == null)
          this.dpDealSelect = paramMApiRequest[0];
      }
      this.dealRequest = null;
    }
    while (this.dpDealSelect == null);
    resetAgents(null);
    dispatchDataChanged();
  }

  public void setBottomCell(View paramView, CellAgent paramCellAgent)
  {
    this.bottomCellContainer.removeAllViews();
    this.bottomCellContainer.addView(paramView);
    this.bottomView.setVisibility(0);
  }

  public void setTopCell(View paramView, CellAgent paramCellAgent)
  {
    this.topCellContainer.removeAllViews();
    this.topCellContainer.addView(paramView);
    this.topView.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.CreateOrderAgentFragment
 * JD-Core Version:    0.6.0
 */