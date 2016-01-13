package com.dianping.tuan.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.fragment.BaseTuanFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class OrderLogisticsFragment extends BaseTuanFragment
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static String TAG = OrderLogisticsFragment.class.getSimpleName();
  protected DPObject mLogistic;
  protected TextView mLogisticsCompanytTextView;
  protected View mLogisticsError;
  protected LinearLayout mLogisticsInfoView;
  protected TextView mLogisticsNumberTextView;
  protected MApiRequest mLogisticsRequest;
  protected View mLogisticsSuccess;
  protected int mOrderID;
  protected TextView mStatusDescTextView;
  protected TextView mStatusTextView;

  protected void loadLogisticsInfo()
  {
    if (this.mLogisticsRequest != null)
    {
      Log.i(TAG, "mLogisticsRequest is running");
      return;
    }
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("http://app.t.dianping.com/");
    localStringBuffer.append("logisticgn.bin");
    localStringBuffer.append("?token=").append(accountService().token());
    localStringBuffer.append("&cityid=").append(cityId());
    localStringBuffer.append("&orderid=").append(this.mOrderID);
    this.mLogisticsRequest = mapiGet(this, localStringBuffer.toString(), CacheType.DISABLED);
    mapiService().exec(this.mLogisticsRequest, this);
    showProgressDialog("正在获取物流信息...");
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    getActivity().setTitle("查看物流");
    if (this.mLogistic == null)
      loadLogisticsInfo();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.mOrderID = paramBundle.getInt("OrderID");
      return;
    }
    paramBundle = getArguments();
    this.mOrderID = paramBundle.getInt("OrderID");
    this.mLogistic = ((DPObject)paramBundle.getParcelable("Logistic"));
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.order_logistics_layout, paramViewGroup, false);
    this.mLogisticsSuccess = paramLayoutInflater.findViewById(R.id.order_logistics_success);
    this.mLogisticsError = paramLayoutInflater.findViewById(R.id.order_logistics_error);
    this.mStatusTextView = ((TextView)paramLayoutInflater.findViewById(R.id.current_status));
    this.mStatusDescTextView = ((TextView)paramLayoutInflater.findViewById(R.id.status_desc));
    this.mLogisticsCompanytTextView = ((TextView)paramLayoutInflater.findViewById(R.id.logistics_company));
    this.mLogisticsNumberTextView = ((TextView)paramLayoutInflater.findViewById(R.id.logistics_number));
    this.mLogisticsInfoView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.logistics_info_list));
    ((Button)this.mLogisticsError.findViewById(R.id.retry)).setOnClickListener(new OrderLogisticsFragment.1(this));
    return paramLayoutInflater;
  }

  protected void onProgressDialogCancel()
  {
    if (this.mLogisticsRequest != null)
    {
      mapiService().abort(this.mLogisticsRequest, this, true);
      this.mLogisticsRequest = null;
      getActivity().onBackPressed();
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissProgressDialog();
    this.mLogisticsRequest = null;
    ((TextView)this.mLogisticsError.findViewById(R.id.error_info)).setText(paramMApiResponse.message().content());
    this.mLogisticsError.setVisibility(0);
    this.mLogisticsSuccess.setVisibility(8);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissProgressDialog();
    if (paramMApiRequest == this.mLogisticsRequest)
    {
      this.mLogisticsRequest = null;
      this.mLogistic = ((DPObject)paramMApiResponse.result());
      updateView();
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putInt("OrderID", this.mOrderID);
    paramBundle.putParcelable("Logistic", this.mLogistic);
    super.onSaveInstanceState(paramBundle);
  }

  protected void updateView()
  {
    if (this.mLogistic == null)
    {
      getActivity().onBackPressed();
      return;
    }
    this.mLogisticsError.setVisibility(8);
    this.mLogisticsSuccess.setVisibility(0);
    Object localObject;
    String str;
    switch (this.mLogistic.getInt("Status"))
    {
    default:
      localObject = "";
      str = "";
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      this.mStatusTextView.setText((CharSequence)localObject);
      this.mStatusDescTextView.setText(str);
      this.mStatusDescTextView.setVisibility(0);
      if (this.mLogistic.getInt("Status") != 2)
        break label329;
      this.mLogisticsSuccess.findViewById(R.id.logistics_trace_layout).setVisibility(0);
      this.mLogisticsCompanytTextView.setText(this.mLogistic.getString("Company"));
      this.mLogisticsNumberTextView.setText(this.mLogistic.getString("Number"));
      if (this.mLogistic.getArray("Details") == null)
        break;
      localObject = this.mLogistic.getArray("Details");
      int j = localObject.length;
      int i = 0;
      while (i < j)
      {
        str = localObject[i];
        View localView = LayoutInflater.from(getActivity()).inflate(R.layout.order_logistics_item, this.mLogisticsInfoView, false);
        ((TextView)localView.findViewById(R.id.logistics_item_desc)).setText(str.getString("Name"));
        ((TextView)localView.findViewById(R.id.logistics_item_time)).setText(str.getString("ID"));
        this.mLogisticsInfoView.addView(localView);
        i += 1;
      }
      localObject = "未发货";
      str = "正在为您处理订单中，会尽快将订单信息发送给商家";
      continue;
      localObject = "配送中";
      str = "商家正在为您出库及打包货品，请耐心等待";
      continue;
      localObject = "已发货";
      str = "您的货品已经在配送途中，会尽快送达您手中";
      continue;
      localObject = "审核未通过";
      str = "您的订单审核未通过";
    }
    label329: this.mLogisticsSuccess.findViewById(R.id.logistics_trace_layout).setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.OrderLogisticsFragment
 * JD-Core Version:    0.6.0
 */