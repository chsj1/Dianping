package com.dianping.tuan.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.DateUtils;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RefundDetailActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final int ADD_NEW_ACCOUNT = 100;
  private Button mAddAccount;
  private Button mBtnConnectCS;
  private int mCouponId = 0;
  private RelativeLayout mFlowAndAccount;
  private LinearLayout mFlowPanel;
  private MApiRequest mRefundDetailRequest;
  private boolean mSupportOnlineCS;
  private TextView mTextAccount;
  private TextView mTextCount;
  private TextView mTextNumber;
  private TextView mTextValue;
  private TextView mTvConnectCS;

  private void queryRefundDetail()
  {
    if (this.mRefundDetailRequest != null)
      return;
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("refunddetailgn.bin");
    localUrlBuilder.addParam("token", accountService().token());
    localUrlBuilder.addParam("receiptid", Integer.valueOf(this.mCouponId));
    this.mRefundDetailRequest = mapiGet(this, localUrlBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.mRefundDetailRequest, this);
  }

  private void setupViews()
  {
    setContentView(R.layout.refund_detail);
    this.mTextValue = ((TextView)findViewById(R.id.text_value));
    this.mTextNumber = ((TextView)findViewById(R.id.text_num));
    this.mTextCount = ((TextView)findViewById(R.id.text_count));
    this.mTextAccount = ((TextView)findViewById(R.id.text_account));
    this.mFlowPanel = ((LinearLayout)findViewById(R.id.refund_flow_panel));
    this.mAddAccount = ((Button)findViewById(R.id.add_new_account));
    this.mFlowAndAccount = ((RelativeLayout)findViewById(R.id.refund_flow_and_account));
    if (Build.VERSION.SDK_INT >= 11);
    for (boolean bool = true; ; bool = false)
    {
      this.mSupportOnlineCS = bool;
      if (this.mSupportOnlineCS)
      {
        this.mTvConnectCS = ((TextView)findViewById(R.id.refund_connect_customer_service));
        this.mBtnConnectCS = ((Button)findViewById(R.id.connect_customer_service));
        this.mBtnConnectCS.setOnClickListener(this);
        this.mTvConnectCS.setOnClickListener(this);
      }
      return;
    }
  }

  private void showRefundDetail(DPObject paramDPObject)
  {
    this.mTextValue.setText("￥" + paramDPObject.getDouble("Amount"));
    Object localObject1;
    Object localObject2;
    if (paramDPObject.getBoolean("SerialShow"))
    {
      localObject1 = paramDPObject.getString("ReceiptList");
      this.mTextNumber.setText((CharSequence)localObject1);
      findViewById(R.id.num_layout).setVisibility(0);
      this.mTextCount.setText("" + paramDPObject.getInt("ReceiptNum"));
      this.mTextAccount.setText(paramDPObject.getString("RefundToAccount"));
      localObject1 = paramDPObject.getArray("ProcessItems");
      if (this.mSupportOnlineCS)
      {
        boolean bool = paramDPObject.getBoolean("CustomButtonShow");
        localObject2 = this.mBtnConnectCS;
        if (!bool)
          break label250;
        i = 0;
        label145: ((Button)localObject2).setVisibility(i);
        localObject2 = this.mTvConnectCS;
        if (!bool)
          break label257;
      }
    }
    label257: for (int i = 8; ; i = 0)
    {
      ((TextView)localObject2).setVisibility(i);
      this.mFlowAndAccount.setVisibility(0);
      updateFlowView(localObject1);
      if (TextUtils.isEmpty(paramDPObject.getString("HuanKaTuiUrl")))
        break label263;
      this.mAddAccount.setVisibility(0);
      ((NovaButton)this.mAddAccount).setGAString("huankatui");
      this.mAddAccount.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$info.getString("HuanKaTuiUrl");
          try
          {
            String str = URLEncoder.encode(paramView, "utf-8");
            paramView = str;
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView));
            RefundDetailActivity.this.startActivityForResult(paramView, 100);
            return;
          }
          catch (UnsupportedEncodingException localUnsupportedEncodingException)
          {
            while (true)
              localUnsupportedEncodingException.printStackTrace();
          }
        }
      });
      return;
      findViewById(R.id.num_layout).setVisibility(8);
      break;
      label250: i = 8;
      break label145;
    }
    label263: this.mAddAccount.setVisibility(8);
  }

  private void updateFlowView(DPObject[] paramArrayOfDPObject)
  {
    this.mFlowPanel.removeAllViews();
    if (DPObjectUtils.isArrayEmpty(paramArrayOfDPObject))
    {
      this.mFlowPanel.setVisibility(8);
      return;
    }
    boolean[] arrayOfBoolean1 = new boolean[paramArrayOfDPObject.length];
    boolean[] arrayOfBoolean2 = new boolean[paramArrayOfDPObject.length];
    int i = 0;
    DPObject localDPObject;
    Object localObject;
    while (i < paramArrayOfDPObject.length - 1)
    {
      localDPObject = paramArrayOfDPObject[i];
      localObject = paramArrayOfDPObject[(i + 1)];
      arrayOfBoolean1[i] = localDPObject.getBoolean("IsFinished");
      arrayOfBoolean2[i] = ((DPObject)localObject).getBoolean("IsFinished");
      i += 1;
    }
    arrayOfBoolean1[(paramArrayOfDPObject.length - 1)] = paramArrayOfDPObject[(paramArrayOfDPObject.length - 1)].getBoolean("IsFinished");
    arrayOfBoolean2[(paramArrayOfDPObject.length - 1)] = false;
    i = 0;
    label121: TextView localTextView1;
    TextView localTextView2;
    TextView localTextView3;
    TextView localTextView4;
    View localView;
    if (i < paramArrayOfDPObject.length)
    {
      localDPObject = paramArrayOfDPObject[i];
      localObject = LayoutInflater.from(this).inflate(R.layout.refund_process_item, null, false);
      localTextView1 = (TextView)((View)localObject).findViewById(R.id.step_view);
      localTextView2 = (TextView)((View)localObject).findViewById(R.id.title);
      localTextView3 = (TextView)((View)localObject).findViewById(R.id.content);
      localTextView4 = (TextView)((View)localObject).findViewById(R.id.time);
      localView = ((View)localObject).findViewById(R.id.process_line);
      localTextView1.setText("" + (i + 1));
      localTextView2.setText(localDPObject.getString("Title"));
      localTextView3.setText(localDPObject.getString("Content"));
      long l = localDPObject.getTime("Date");
      if (l <= 0L)
        break label384;
      localTextView4.setText(DateUtils.formatDate2TimeZone(l, "yyyy-MM-dd HH:mm", "GMT+8"));
      label296: if (arrayOfBoolean1[i] == 0)
        break label430;
      if (!localDPObject.getBoolean("IsWarning"))
        break label394;
      localTextView1.setBackgroundDrawable(getResources().getDrawable(R.drawable.refund_flow_circle_warning));
      label329: if (arrayOfBoolean2[i] == 0)
        break label412;
      localView.setBackgroundColor(getResources().getColor(R.color.orange_red));
    }
    while (true)
    {
      if (i == paramArrayOfDPObject.length - 1)
        localView.setVisibility(4);
      this.mFlowPanel.addView((View)localObject);
      i += 1;
      break label121;
      break;
      label384: localTextView4.setText("");
      break label296;
      label394: localTextView1.setBackgroundDrawable(getResources().getDrawable(R.drawable.refund_flow_circle_highlight));
      break label329;
      label412: localView.setBackgroundColor(getResources().getColor(R.color.separator_color));
      continue;
      label430: localTextView1.setBackgroundDrawable(getResources().getDrawable(R.drawable.refund_flow_circle_gray));
      localView.setBackgroundColor(getResources().getColor(R.color.separator_color));
      localTextView2.setTextColor(getResources().getColor(R.color.separator_color));
      localTextView3.setTextColor(getResources().getColor(R.color.separator_color));
      localTextView4.setTextColor(getResources().getColor(R.color.separator_color));
    }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (100 == paramInt1)
    {
      this.mFlowAndAccount.setVisibility(8);
      queryRefundDetail();
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if ((i == R.id.connect_customer_service) || (i == R.id.refund_connect_customer_service))
    {
      paramView = UrlBuilder.createBuilder("http://kf.dianping.com/third-part/user/app/consultCategory");
      paramView.addParam("d.user_token", accountService().token());
      paramView.addParam("d.user_id", Integer.valueOf(accountService().id()));
      paramView.addParam("d.city_id", Integer.valueOf(cityId()));
      paramView.addParams("&d.user_type=user&d.from=app&d.skill_code=skillFinancialPayments&d.consult_code=tgRefundProblem&activityName=AppRefundDetail&activityTitle=App退款详情");
      startActivity(new Intent("android.intent.action.VIEW", paramView.buildWebSchema()));
    }
    do
      return;
    while (i != R.id.refund_help);
    startActivity("dianping://web?url=http://h5.dianping.com/tuan/help/refund-help.html?dpshare=0");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null);
    while (true)
    {
      try
      {
        this.mCouponId = Integer.valueOf(getIntent().getData().getQueryParameter("couponid")).intValue();
        if (this.mCouponId != 0)
          break;
        Toast.makeText(getApplicationContext(), "获取退款详情失败", 1).show();
        finish();
        return;
      }
      catch (Exception paramBundle)
      {
        paramBundle.printStackTrace();
        continue;
      }
      this.mCouponId = paramBundle.getInt("couponId");
    }
    setupViews();
    queryRefundDetail();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRefundDetailRequest)
    {
      this.mRefundDetailRequest = null;
      Toast.makeText(this, "退款信息查询失败", 1).show();
      finish();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRefundDetailRequest)
    {
      this.mRefundDetailRequest = null;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse.result(), "ReceiptRefundInfo"))
        showRefundDetail((DPObject)paramMApiResponse.result());
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putInt("couponId", this.mCouponId);
    super.onSaveInstanceState(paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.RefundDetailActivity
 * JD-Core Version:    0.6.0
 */