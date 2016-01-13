package com.dianping.takeaway.fragment;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.Cell;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.util.SoundPlayer;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.takeaway.agents.TakeawayOrderBasicInfoAgent;
import com.dianping.takeaway.agents.TakeawayOrderStatusAgent;
import com.dianping.takeaway.util.TakeawayUtils;
import com.dianping.takeaway.view.OnDialogOperationListener;
import com.dianping.takeaway.view.TAOrderConfirmDialog;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.raw;
import com.dianping.v1.R.style;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import com.dianping.widget.view.NovaButton;
import java.util.ArrayList;
import java.util.ArrayList<Lcom.dianping.base.app.loader.AgentListConfig;>;
import java.util.HashMap;
import java.util.Map;

public class TakeawayOrderDetailFragment extends AgentFragment
{
  protected Dialog activityDialog;
  protected MApiRequest cancelOrderRequest;
  protected MApiRequest confirmReceiptRequest;
  protected View contentView;
  private boolean firstShow = true;
  protected MApiRequest getMaxArrivalTimeRequest;
  protected MApiRequest getTAOrderDetailRequest;
  protected View loadingView;
  private IntentFilter mIntentFilter;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.takeaway.UPDATE_ORDER".equals(paramIntent.getAction()))
      {
        TakeawayOrderDetailFragment.this.loadingView.setVisibility(0);
        TakeawayOrderDetailFragment.this.contentView.setVisibility(8);
        TakeawayOrderDetailFragment.this.orderViewId = paramIntent.getStringExtra("orderviewid");
        TakeawayOrderDetailFragment.this.getTakeawayOrderDetailTask(TakeawayOrderDetailFragment.this.orderViewId);
      }
    }
  };
  private LinearLayout operationView;
  protected String orderViewId;
  public int pageFrom;
  protected int shopId;
  protected String shopName;
  protected MApiRequest submitOldOrderRequest;
  protected DPObject takeawayOrder;
  protected PullToRefreshScrollView wholeLayout;

  private View createDividerView()
  {
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(ViewUtils.dip2px(getActivity(), 10.0F), -2);
    View localView = new View(getActivity());
    localView.setLayoutParams(localLayoutParams);
    return localView;
  }

  private NovaButton createOperationBtn(boolean paramBoolean, String paramString)
  {
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, ViewUtils.dip2px(getActivity(), 45.0F));
    localLayoutParams.weight = 1.0F;
    localLayoutParams.gravity = 16;
    NovaButton localNovaButton = new NovaButton(getActivity());
    localNovaButton.setLayoutParams(localLayoutParams);
    localNovaButton.setGravity(17);
    localNovaButton.setText(paramString);
    paramString = getActivity();
    if (paramBoolean)
    {
      i = R.style.NovaLightButtonTheme;
      localNovaButton.setTextAppearance(paramString, i);
      if (!paramBoolean)
        break label132;
      i = R.color.text_gray;
      label91: localNovaButton.setTextColor(getActivity().getResources().getColor(i));
      if (!paramBoolean)
        break label140;
    }
    label132: label140: for (int i = R.drawable.btn_light; ; i = R.drawable.btn_weight)
    {
      localNovaButton.setBackgroundResource(i);
      return localNovaButton;
      i = R.style.NovaWeightButtonTheme;
      break;
      i = R.color.white;
      break label91;
    }
  }

  private void setupOrderOperations()
  {
    DPObject[] arrayOfDPObject = this.takeawayOrder.getArray("OrderOperations");
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
    {
      this.operationView.removeAllViews();
      int i = 0;
      if (i < arrayOfDPObject.length)
      {
        Object localObject = arrayOfDPObject[i];
        int j = ((DPObject)localObject).getInt("Type");
        if (j == 6)
          statisticsEvent("takeaway6", "takeaway6_orderdetails_shareshow", this.orderViewId, 0);
        boolean bool;
        if ((j != 1) && (j != 3))
        {
          bool = true;
          label81: localObject = createOperationBtn(bool, ((DPObject)localObject).getString("Message"));
          switch (j)
          {
          case 6:
          default:
          case 1:
          case 2:
          case 3:
          case 4:
          case 5:
          case 7:
          }
        }
        while (true)
        {
          ((NovaButton)localObject).setOnClickListener(new View.OnClickListener(j)
          {
            public void onClick(View paramView)
            {
              Object localObject;
              switch (this.val$btnType)
              {
              case 6:
              default:
                return;
              case 1:
                TakeawayOrderDetailFragment.this.submitOldOrderTask(TakeawayOrderDetailFragment.this.orderViewId);
                TakeawayOrderDetailFragment.this.statisticsEvent("takeaway6", "takeaway6_orderdetails_payclk", TakeawayOrderDetailFragment.this.orderViewId, 0);
                return;
              case 2:
                TakeawayOrderDetailFragment.this.startActivity("dianping://takeawayreview?orderid=" + TakeawayOrderDetailFragment.this.orderViewId + "&shopname=" + TakeawayOrderDetailFragment.this.shopName + "&source=2");
                TakeawayOrderDetailFragment.this.statisticsEvent("takeaway6", "takeaway6_orderdetails_commentclk", TakeawayOrderDetailFragment.this.orderViewId, 0);
                return;
              case 3:
                localObject = TakeawayOrderDetailFragment.this;
                if (TakeawayOrderDetailFragment.this.getAccount() == null);
                for (paramView = ""; ; paramView = TakeawayOrderDetailFragment.this.getAccount().token())
                {
                  ((TakeawayOrderDetailFragment)localObject).getMaxArrivalTime(paramView, Environment.uuid(), TakeawayOrderDetailFragment.this.orderViewId);
                  TakeawayOrderDetailFragment.this.statisticsEvent("takeaway6", "takeaway6_orderdetails_receivingclk", TakeawayOrderDetailFragment.this.orderViewId, 0);
                  return;
                }
              case 4:
                new AlertDialog.Builder(TakeawayOrderDetailFragment.this.getActivity()).setMessage("您确定不要这单外卖了吗？").setPositiveButton("不要这单了", new DialogInterface.OnClickListener()
                {
                  public void onClick(DialogInterface paramDialogInterface, int paramInt)
                  {
                    TakeawayOrderDetailFragment.this.cancelOrder(TakeawayOrderDetailFragment.this.orderViewId);
                  }
                }).setNegativeButton("取消", null).show();
                TakeawayOrderDetailFragment.this.statisticsEvent("takeaway6", "takeaway6_orderdetails_cancelclk", TakeawayOrderDetailFragment.this.orderViewId, 0);
                return;
              case 5:
                paramView = TakeawayOrderDetailFragment.this.takeawayOrder.getStringArray("ShopPhone");
                localObject = TakeawayOrderDetailFragment.this.getActivity();
                if ((paramView != null) && (paramView.length != 0));
                for (paramView = paramView[0]; ; paramView = "")
                {
                  TakeawayUtils.callCancelOrderPhone((Context)localObject, paramView, TakeawayOrderDetailFragment.this.takeawayOrder.getString("DianpingPhone"), TakeawayOrderDetailFragment.this.takeawayOrder.getString("ThirdPartyName"), TakeawayOrderDetailFragment.this.takeawayOrder.getString("ThirdPartyPhone"), TakeawayOrderDetailFragment.this.takeawayOrder.getString("ApplyCancelDesc"));
                  TakeawayOrderDetailFragment.this.statisticsEvent("takeaway6", "takeaway6_orderdetails_apply_cancelclk", TakeawayOrderDetailFragment.this.orderViewId, 0);
                  return;
                }
              case 7:
              }
              TakeawayUtils.callShopPhones(TakeawayOrderDetailFragment.this.getContext(), TakeawayOrderDetailFragment.this.takeawayOrder.getStringArray("ShopPhone"));
            }
          });
          this.operationView.addView((View)localObject);
          if (i != arrayOfDPObject.length - 1)
            this.operationView.addView(createDividerView());
          i += 1;
          break;
          bool = false;
          break label81;
          ((NovaButton)localObject).setGAString("pay");
          continue;
          ((NovaButton)localObject).setGAString("comment");
          continue;
          ((NovaButton)localObject).setGAString("receiving");
          continue;
          ((NovaButton)localObject).setGAString("cancel");
          continue;
          ((NovaButton)localObject).setGAString("apply_cancel");
          continue;
          ((NovaButton)localObject).setGAString("callshop");
        }
      }
      this.operationView.setVisibility(0);
      return;
    }
    this.operationView.setVisibility(8);
  }

  private void showConfirmDialog(int paramInt1, int paramInt2, String paramString)
  {
    TAOrderConfirmDialog localTAOrderConfirmDialog = new TAOrderConfirmDialog(getActivity(), paramInt2, paramInt1, paramInt2);
    localTAOrderConfirmDialog.setCanceledOnTouchOutside(true);
    localTAOrderConfirmDialog.setListener(new OnDialogOperationListener(paramString)
    {
      public void cancel()
      {
      }

      public void confirm(int paramInt)
      {
        TakeawayOrderDetailFragment localTakeawayOrderDetailFragment = TakeawayOrderDetailFragment.this;
        if (TakeawayOrderDetailFragment.this.getAccount() == null);
        for (String str = ""; ; str = TakeawayOrderDetailFragment.this.getAccount().token())
        {
          localTakeawayOrderDetailFragment.confirmReceipt(str, Environment.uuid(), this.val$orderViewId, String.valueOf(paramInt));
          return;
        }
      }
    });
    localTAOrderConfirmDialog.show();
  }

  private void showPopupDialog(DPObject paramDPObject)
  {
    this.activityDialog = new Dialog(getActivity(), R.style.dialog);
    Object localObject = LayoutInflater.from(getActivity()).inflate(R.layout.takeaway_result_activity_dialog, null, false);
    ((NetworkImageView)((View)localObject).findViewById(R.id.activity_icon)).setImage(paramDPObject.getString("PopUpUrl"));
    ViewUtils.setVisibilityAndContent((TextView)((View)localObject).findViewById(R.id.activity_title), paramDPObject.getString("PopUpTitle"));
    ViewUtils.setVisibilityAndContent((TextView)((View)localObject).findViewById(R.id.activity_content), paramDPObject.getString("PopUpContent"));
    ((View)localObject).findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (TakeawayOrderDetailFragment.this.activityDialog.isShowing())
          TakeawayOrderDetailFragment.this.activityDialog.dismiss();
        TakeawayOrderDetailFragment.this.shareObj(TakeawayOrderDetailFragment.this.getShareByType());
      }
    });
    ((View)localObject).findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (TakeawayOrderDetailFragment.this.activityDialog.isShowing())
          TakeawayOrderDetailFragment.this.activityDialog.dismiss();
      }
    });
    this.activityDialog.setContentView((View)localObject);
    paramDPObject = this.activityDialog.getWindow();
    localObject = paramDPObject.getAttributes();
    ((WindowManager.LayoutParams)localObject).width = (ViewUtils.getScreenWidthPixels(getActivity()) - ViewUtils.dip2px(getActivity(), 30.0F));
    ((WindowManager.LayoutParams)localObject).gravity = 17;
    paramDPObject.setAttributes((WindowManager.LayoutParams)localObject);
    this.activityDialog.setCanceledOnTouchOutside(false);
    this.activityDialog.show();
  }

  protected void addCellToContainerView(String paramString, Cell paramCell)
  {
    ((ViewGroup)agentContainerView().findViewById(R.id.agent_container_layout)).addView(paramCell.view);
  }

  protected void cancelOrder(String paramString)
  {
    if (this.cancelOrderRequest != null)
      return;
    this.cancelOrderRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/cancelorder.ta", new String[] { "orderviewid", paramString });
    mapiService().exec(this.cancelOrderRequest, new RequestHandler(paramString)
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayOrderDetailFragment.this.showToast("网络错误，请重试");
        TakeawayOrderDetailFragment.this.cancelOrderRequest = null;
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          TakeawayOrderDetailFragment.this.showToast(paramMApiRequest.getString("Content"));
        }
        paramMApiRequest = new Intent("com.dianping.takeaway.UPDATE_ORDER");
        paramMApiRequest.putExtra("orderviewid", this.val$orderviewid);
        TakeawayOrderDetailFragment.this.getActivity().sendBroadcast(paramMApiRequest);
        TakeawayOrderDetailFragment.this.cancelOrderRequest = null;
      }
    });
  }

  protected void confirmReceipt(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (this.confirmReceiptRequest != null)
      return;
    this.confirmReceiptRequest = BasicMApiRequest.mapiGet(Uri.parse("http://waimai.api.dianping.com/arrived.ta").buildUpon().appendQueryParameter("uuid", paramString2).appendQueryParameter("token", paramString1).appendQueryParameter("orderviewid", paramString3).appendQueryParameter("speed", paramString4).toString(), CacheType.DISABLED);
    mapiService().exec(this.confirmReceiptRequest, new RequestHandler(paramString3)
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiResponse != null)
          ((NovaActivity)TakeawayOrderDetailFragment.this.getActivity()).showMessageDialog(paramMApiResponse.message());
        TakeawayOrderDetailFragment.this.confirmReceiptRequest = null;
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          TakeawayOrderDetailFragment.this.showToast(((DPObject)paramMApiResponse.result()).getString("Content"));
          paramMApiRequest = new Intent("com.dianping.takeaway.UPDATE_ORDER");
          paramMApiRequest.putExtra("orderviewid", this.val$orderviewid);
          TakeawayOrderDetailFragment.this.getActivity().sendBroadcast(paramMApiRequest);
          paramMApiRequest = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayreview?source=1&orderid=" + TakeawayOrderDetailFragment.this.orderViewId + "&shopname=" + TakeawayOrderDetailFragment.this.shopName));
          TakeawayOrderDetailFragment.this.startActivity(paramMApiRequest);
        }
        TakeawayOrderDetailFragment.this.confirmReceiptRequest = null;
      }
    });
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    Object localObject = new HashMap();
    ((HashMap)localObject).put("takeaway_detail/status", TakeawayOrderStatusAgent.class);
    ((HashMap)localObject).put("takeaway_detail/info", TakeawayOrderBasicInfoAgent.class);
    localObject = new AgentListConfig((HashMap)localObject)
    {
      public Map<String, AgentInfo> getAgentInfoList()
      {
        return null;
      }

      public Map<String, Class<? extends CellAgent>> getAgentList()
      {
        return this.val$defaultAgents;
      }

      public boolean shouldShow()
      {
        return true;
      }
    };
    ArrayList localArrayList = new ArrayList(1);
    localArrayList.add(localObject);
    return (ArrayList<AgentListConfig>)localArrayList;
  }

  protected void getMaxArrivalTime(String paramString1, String paramString2, String paramString3)
  {
    if (this.getMaxArrivalTimeRequest != null)
      return;
    this.getMaxArrivalTimeRequest = BasicMApiRequest.mapiGet(Uri.parse("http://waimai.api.dianping.com/maxarrivedtime.ta").buildUpon().appendQueryParameter("uuid", paramString2).appendQueryParameter("token", paramString1).appendQueryParameter("orderviewid", paramString3).toString(), CacheType.DISABLED);
    mapiService().exec(this.getMaxArrivalTimeRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiResponse != null)
          ((NovaActivity)TakeawayOrderDetailFragment.this.getActivity()).showMessageDialog(paramMApiResponse.message());
        TakeawayOrderDetailFragment.this.getMaxArrivalTimeRequest = null;
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          paramMApiResponse = paramMApiRequest.getString("OrderViewId");
          int i = paramMApiRequest.getInt("Time");
          int j = paramMApiRequest.getInt("SpanTime");
          TakeawayOrderDetailFragment.this.showConfirmDialog(i, j, paramMApiResponse);
        }
        TakeawayOrderDetailFragment.this.getMaxArrivalTimeRequest = null;
      }
    });
  }

  protected DPObject getShareByType()
  {
    if (this.takeawayOrder != null)
      return this.takeawayOrder.getObject("Share");
    return null;
  }

  public void getTakeawayOrderDetailTask(String paramString)
  {
    if (this.getTAOrderDetailRequest != null);
    do
      return;
    while (TextUtils.isEmpty(paramString));
    if ((this.firstShow) && (this.pageFrom != 0));
    for (boolean bool = true; ; bool = false)
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("orderviewid");
      localArrayList.add(paramString);
      localArrayList.add("isfirstfromordersuccess");
      localArrayList.add(String.valueOf(bool));
      this.getTAOrderDetailRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/orderdetail.ta", (String[])localArrayList.toArray(new String[0]));
      mapiService().exec(this.getTAOrderDetailRequest, new RequestHandler()
      {
        public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          TakeawayOrderDetailFragment.this.loadingView.setVisibility(8);
          TakeawayOrderDetailFragment.this.contentView.setVisibility(0);
          if (TakeawayOrderDetailFragment.this.wholeLayout != null)
            TakeawayOrderDetailFragment.this.wholeLayout.onRefreshComplete();
          Object localObject = "";
          paramMApiRequest = (MApiRequest)localObject;
          if (paramMApiResponse != null)
          {
            paramMApiRequest = (MApiRequest)localObject;
            if (paramMApiResponse.message() != null)
              paramMApiRequest = paramMApiResponse.message().content();
          }
          localObject = TakeawayOrderDetailFragment.this;
          paramMApiResponse = paramMApiRequest;
          if (TextUtils.isEmpty(paramMApiRequest))
            paramMApiResponse = "订单信息获取失败";
          ((TakeawayOrderDetailFragment)localObject).showToast(paramMApiResponse);
          TakeawayOrderDetailFragment.this.getTAOrderDetailRequest = null;
        }

        public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          int i = 0;
          TakeawayOrderDetailFragment.this.loadingView.setVisibility(8);
          TakeawayOrderDetailFragment.this.contentView.setVisibility(0);
          if (TakeawayOrderDetailFragment.this.wholeLayout != null)
            TakeawayOrderDetailFragment.this.wholeLayout.onRefreshComplete();
          if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
          {
            TakeawayOrderDetailFragment.this.takeawayOrder = ((DPObject)paramMApiResponse.result());
            TakeawayOrderDetailFragment.this.orderViewId = TakeawayOrderDetailFragment.this.takeawayOrder.getString("OrderViewId");
            TakeawayOrderDetailFragment.this.shopName = TakeawayOrderDetailFragment.this.takeawayOrder.getString("ShopName");
            TakeawayOrderDetailFragment.this.shopId = TakeawayOrderDetailFragment.this.takeawayOrder.getInt("ShopId");
            paramMApiRequest = new Bundle();
            paramMApiRequest.putInt("type", 0);
            paramMApiRequest.putParcelable("order", TakeawayOrderDetailFragment.this.takeawayOrder);
            paramMApiRequest.putBoolean("firstshow", TakeawayOrderDetailFragment.this.firstShow);
            paramMApiRequest.putInt("pagefrom", TakeawayOrderDetailFragment.this.pageFrom);
            TakeawayOrderDetailFragment.this.dispatchAgentChanged(null, paramMApiRequest);
            if (TakeawayOrderDetailFragment.this.takeawayOrder.getBoolean("IsToPlayVoice"))
              SoundPlayer.play(R.raw.paysucc);
            if (TakeawayOrderDetailFragment.this.firstShow)
              TakeawayOrderDetailFragment.access$102(TakeawayOrderDetailFragment.this, false);
            if ((TakeawayOrderDetailFragment.this.activityDialog == null) || (!TakeawayOrderDetailFragment.this.activityDialog.isShowing()))
              i = 1;
            if ((TakeawayOrderDetailFragment.this.takeawayOrder.getObject("PopUp") != null) && (i != 0))
              TakeawayOrderDetailFragment.this.showPopupDialog(TakeawayOrderDetailFragment.this.takeawayOrder.getObject("PopUp"));
            TakeawayOrderDetailFragment.this.setupOrderOperations();
          }
          TakeawayOrderDetailFragment.this.getTAOrderDetailRequest = null;
        }
      });
      return;
    }
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.orderViewId = super.getStringParam("orderviewid");
    this.pageFrom = super.getIntParam("pagefrom");
    getTakeawayOrderDetailTask(this.orderViewId);
    this.mIntentFilter = new IntentFilter("com.dianping.takeaway.UPDATE_ORDER");
    super.registerReceiver(this.mReceiver, this.mIntentFilter);
    super.getTitleBar().addRightViewItem("投诉反馈", "feedback", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://feedback?flag=8"));
        TakeawayOrderDetailFragment.this.startActivity(paramView);
      }
    });
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setHost("takeaway_detail");
    if (paramBundle != null)
      this.firstShow = paramBundle.getBoolean("firstshow");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = (ViewGroup)paramLayoutInflater.inflate(R.layout.takeaway_order_detail_layout, paramViewGroup, false);
    super.setAgentContainerView(paramLayoutInflater);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mReceiver != null)
      super.unregisterReceiver(this.mReceiver);
    if (this.getTAOrderDetailRequest != null)
    {
      super.mapiService().abort(this.getTAOrderDetailRequest, null, true);
      this.getTAOrderDetailRequest = null;
    }
    if (this.confirmReceiptRequest != null)
    {
      super.mapiService().abort(this.confirmReceiptRequest, null, true);
      this.confirmReceiptRequest = null;
    }
    if (this.getMaxArrivalTimeRequest != null)
    {
      super.mapiService().abort(this.getMaxArrivalTimeRequest, null, true);
      this.getMaxArrivalTimeRequest = null;
    }
    if (this.cancelOrderRequest != null)
    {
      super.mapiService().abort(this.cancelOrderRequest, null, true);
      this.cancelOrderRequest = null;
    }
    if (this.submitOldOrderRequest != null)
    {
      super.mapiService().abort(this.submitOldOrderRequest, null, true);
      this.submitOldOrderRequest = null;
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("firstshow", this.firstShow);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.loadingView = paramView.findViewById(R.id.loading_view);
    this.contentView = paramView.findViewById(R.id.content_view);
    this.wholeLayout = ((PullToRefreshScrollView)paramView.findViewById(R.id.whole_layout));
    this.wholeLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
      {
        TakeawayOrderDetailFragment.this.getTakeawayOrderDetailTask(TakeawayOrderDetailFragment.this.orderViewId);
      }
    });
    this.operationView = ((LinearLayout)paramView.findViewById(R.id.operation_layout));
  }

  protected void resetAgentContainerView()
  {
    ((ViewGroup)agentContainerView().findViewById(R.id.agent_container_layout)).removeAllViews();
  }

  public void shareObj(DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.title = paramDPObject.getString("Title");
    localShareHolder.imageUrl = paramDPObject.getString("PicUrl");
    localShareHolder.webUrl = paramDPObject.getString("Url");
    localShareHolder.desc = paramDPObject.getString("Content");
    ShareUtil.gotoShareTo(getActivity(), ShareType.WEB, localShareHolder, "", "", 7);
  }

  protected void submitOldOrderTask(String paramString)
  {
    if (this.submitOldOrderRequest != null)
      return;
    this.submitOldOrderRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/submitoldorder.ta", new String[] { "orderid", paramString });
    mapiService().exec(this.submitOldOrderRequest, new RequestHandler(paramString)
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayOrderDetailFragment.this.submitOldOrderRequest = null;
        if (paramMApiResponse != null)
          ((NovaActivity)TakeawayOrderDetailFragment.this.getActivity()).showMessageDialog(paramMApiResponse.message());
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayOrderDetailFragment.this.submitOldOrderRequest = null;
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest != null)
          {
            int i = paramMApiRequest.getInt("PayOrderId");
            paramMApiRequest = paramMApiRequest.getObject("PayProduct");
            int j = paramMApiRequest.getInt("ProductType");
            paramMApiResponse = new Intent("android.intent.action.VIEW", Uri.parse("dianping://minipayorder"));
            paramMApiResponse.putExtra("orderid", String.valueOf(i));
            paramMApiResponse.putExtra("productcode", String.valueOf(j));
            paramMApiResponse.putExtra("mainproductcode", j);
            paramMApiResponse.putExtra("orderinfotitle", paramMApiRequest.getString("Title"));
            paramMApiResponse.putExtra("callbackurl", "dianping://takeawayorderdetail?source=takeawayorderdetail&pagefrom=1&orderviewid=" + this.val$orderviewid);
            paramMApiResponse.putExtra("callbackfailurl", "dianping://takeawayfailure?source=takeawayorderdetail&payorderid=" + i + "&orderviewid=" + this.val$orderviewid);
            TakeawayOrderDetailFragment.this.startActivity(paramMApiResponse);
          }
        }
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.fragment.TakeawayOrderDetailFragment
 * JD-Core Version:    0.6.0
 */