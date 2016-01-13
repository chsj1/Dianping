package com.dianping.movie.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.base.widget.TwoLineRadio;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.movie.util.MovieUtil;
import com.dianping.movie.view.MovieRefundReasonItem;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.Arrays;

public class MovieRefundApplicationActivity extends NovaActivity
  implements TableView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  public static final String REFUND_SUCCEED = "com.dianping.movie.refund_succeed";
  private static final String TAG = MovieRefundApplicationActivity.class.getSimpleName();
  protected DPObject currentRefundReason = null;
  protected DPObject dpRefundApplyInfo;
  private MApiRequest mApplyRequest;
  private int mOrderId;
  protected String mRefundAmount;
  protected int mRefundReason = -1;
  protected ArrayList<DPObject> mRefundReasonList = new ArrayList();
  protected MApiRequest mSubmitRequest;
  protected RefundReasonAdapter refundReasonAdapter;

  private void queryRefundApply()
  {
    showProgressDialog("正在请求退款信息...");
    if (this.mApplyRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/refundapplicationmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("orderid", String.valueOf(this.mOrderId));
    localBuilder.appendQueryParameter("token", accountService().token());
    this.mApplyRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.mApplyRequest, this);
  }

  protected void initViews()
  {
    setContentView(R.layout.movie_refund_apply_layout);
    Object localObject1 = (TextView)findViewById(R.id.refund_amount_desc);
    Object localObject2 = (RelativeLayout)findViewById(R.id.refund_note);
    ((TwoLineRadio)findViewById(R.id.refund_type_1)).setChecked(true);
    Object localObject3 = (TableView)findViewById(R.id.refundreason_table);
    ((TableView)localObject3).setOnItemClickListener(this);
    findViewById(R.id.refund_apply).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MovieRefundApplicationActivity.this.submitRefund();
      }
    });
    this.refundReasonAdapter = new RefundReasonAdapter();
    ((TableView)localObject3).setAdapter(this.refundReasonAdapter);
    int i;
    TextView localTextView;
    if (this.dpRefundApplyInfo != null)
    {
      i = this.dpRefundApplyInfo.getInt("RefundQuantity");
      this.mRefundAmount = this.dpRefundApplyInfo.getString("RefundAmount");
      if (this.dpRefundApplyInfo.getArray("RefundReasons") != null)
        this.mRefundReasonList.addAll(Arrays.asList(this.dpRefundApplyInfo.getArray("RefundReasons")));
      localTextView = (TextView)findViewById(R.id.tv_refundreason);
      if (this.mRefundReasonList.size() <= 0)
        break label359;
      localTextView.setVisibility(0);
      ((TableView)localObject3).setVisibility(0);
      localObject3 = this.dpRefundApplyInfo.getString("RefundTip");
      if ((localObject3 == null) || (localObject2 == null) || (localObject1 == null) || (((String)localObject3).length() <= 0))
        break label380;
      ((TextView)localObject1).setText((CharSequence)localObject3);
      ((RelativeLayout)localObject2).setVisibility(0);
      label232: localObject1 = this.dpRefundApplyInfo.getString("Title");
      localObject2 = (TextView)findViewById(R.id.title);
      if (localObject1 == null)
        break label389;
    }
    label389: for (localObject1 = ((String)localObject1).replaceAll("：", ":"); ; localObject1 = "")
    {
      ((TextView)localObject2).setText((CharSequence)localObject1);
      ((TextView)findViewById(R.id.refund_quantity)).setText(i + "");
      ((TextView)findViewById(R.id.refund_amount)).setText(MovieUtil.getAmountSpannableString(this, this.mRefundAmount));
      ((ViewGroup.MarginLayoutParams)findViewById(R.id.title).getLayoutParams()).bottomMargin = ViewUtils.dip2px(this, 12.0F);
      this.refundReasonAdapter.notifyDataSetChanged();
      return;
      label359: this.mRefundReason = 0;
      localTextView.setVisibility(8);
      ((TableView)localObject3).setVisibility(8);
      break;
      label380: ((RelativeLayout)localObject2).setVisibility(8);
      break label232;
    }
  }

  protected boolean invalidateSubmit()
  {
    if (this.mRefundReason == -1)
    {
      showToast("请选择退款原因");
      return false;
    }
    return true;
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040013");
    this.mOrderId = getIntParam("orderid");
    if (this.mOrderId == 0)
    {
      showToast("退款信息错误", 1);
      finish();
    }
    do
      return;
    while (!isLogined());
    queryRefundApply();
  }

  protected void onDestroy()
  {
    if (this.mApplyRequest != null)
    {
      mapiService().abort(this.mApplyRequest, this, true);
      this.mApplyRequest = null;
    }
    if (this.mSubmitRequest != null)
    {
      mapiService().abort(this.mSubmitRequest, this, true);
      this.mSubmitRequest = null;
    }
    super.onDestroy();
  }

  public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
  {
    paramTableView = this.refundReasonAdapter.getItem(paramInt);
    if ((DPObjectUtils.isDPObjectof(paramTableView)) && (this.currentRefundReason != paramTableView))
    {
      this.currentRefundReason = ((DPObject)paramTableView);
      this.mRefundReason = this.currentRefundReason.getInt("Type");
      this.refundReasonAdapter.notifyDataSetChanged();
    }
    GAHelper.instance().contextStatisticsEvent(this, "refund_reason", null, paramInt, "tap");
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if ((paramBoolean) && (isNeedLogin()))
      queryRefundApply();
    return super.onLogin(paramBoolean);
  }

  public void onProgressDialogCancel()
  {
    if (this.mSubmitRequest != null)
    {
      mapiService().abort(this.mSubmitRequest, this, true);
      this.mSubmitRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.mSubmitRequest)
    {
      this.mSubmitRequest = null;
      paramMApiRequest = paramMApiResponse.message();
      if (paramMApiRequest.flag() == 1)
      {
        paramMApiResponse = new Intent("com.dianping.movie.ORDER_STATUS_CHANGED");
        paramMApiResponse.putExtra("orderid", this.mOrderId);
        sendBroadcast(paramMApiResponse);
      }
      showAlertDialog("提示", paramMApiRequest.content());
    }
    do
      return;
    while (paramMApiRequest != this.mApplyRequest);
    this.mApplyRequest = null;
    Toast.makeText(this, paramMApiResponse.message().content(), 1).show();
    finish();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.mSubmitRequest)
    {
      this.mSubmitRequest = null;
      showToast("退款申请提交成功");
      paramMApiRequest = new Intent("com.dianping.movie.ORDER_STATUS_CHANGED");
      paramMApiRequest.putExtra("orderid", this.mOrderId);
      sendBroadcast(paramMApiRequest);
      paramMApiRequest = new Intent("android.intent.action.VIEW");
      paramMApiRequest.setData(Uri.parse("dianping://movierefunddetail?orderid=" + this.mOrderId));
      startActivity(paramMApiRequest);
      finish();
    }
    do
    {
      do
        return;
      while (paramMApiRequest != this.mApplyRequest);
      this.mApplyRequest = null;
    }
    while (!DPObjectUtils.isDPObjectof(paramMApiResponse.result(), "MovieRefundApplication"));
    this.dpRefundApplyInfo = ((DPObject)paramMApiResponse.result());
    initViews();
  }

  protected void submitRefund()
  {
    if (!invalidateSubmit())
      return;
    if (this.mSubmitRequest != null)
    {
      Log.i(TAG, "submitRequest is running");
      return;
    }
    new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("退款申请一旦处理成功，电影票将不能用于观影哦").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (MovieRefundApplicationActivity.this.dpRefundApplyInfo != null)
        {
          MovieRefundApplicationActivity.this.mSubmitRequest = MovieRefundApplicationActivity.this.mapiPost(MovieRefundApplicationActivity.this, "http://app.movie.dianping.com/rs/refundsubmitmv.bin", new String[] { "token", MovieRefundApplicationActivity.this.accountService().token(), "orderid", MovieRefundApplicationActivity.access$000(MovieRefundApplicationActivity.this) + "", "reason", MovieRefundApplicationActivity.this.mRefundReason + "", "refundtype", "2" });
          MovieRefundApplicationActivity.this.mapiService().exec(MovieRefundApplicationActivity.this.mSubmitRequest, MovieRefundApplicationActivity.this);
          MovieRefundApplicationActivity.this.showProgressDialog("正在申请退款...");
        }
      }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
      }
    }).setCancelable(false).show();
  }

  class RefundReasonAdapter extends BasicAdapter
  {
    RefundReasonAdapter()
    {
    }

    public int getCount()
    {
      return MovieRefundApplicationActivity.this.mRefundReasonList.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < MovieRefundApplicationActivity.this.mRefundReasonList.size())
        return MovieRefundApplicationActivity.this.mRefundReasonList.get(paramInt);
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = getItem(paramInt);
      if (DPObjectUtils.isDPObjectof(paramView))
      {
        paramView = (DPObject)paramView;
        paramViewGroup = new MovieRefundReasonItem(MovieRefundApplicationActivity.this);
        paramViewGroup.setRefundReason(paramView);
        if (MovieRefundApplicationActivity.this.currentRefundReason == paramView);
        for (boolean bool = true; ; bool = false)
        {
          paramViewGroup.setChecked(bool);
          paramViewGroup.setBackgroundResource(R.drawable.table_view_item);
          paramViewGroup.setClickable(true);
          return paramViewGroup;
        }
      }
      return null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieRefundApplicationActivity
 * JD-Core Version:    0.6.0
 */