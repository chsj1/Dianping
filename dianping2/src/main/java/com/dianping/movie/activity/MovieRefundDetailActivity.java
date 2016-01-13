package com.dianping.movie.activity;

import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.movie.util.MovieUtil;
import com.dianping.util.DateUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class MovieRefundDetailActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private View dividerLine;
  private LinearLayout mFlowPanel;
  private int mOrderId = 0;
  private MApiRequest mRefundDetailRequest;

  private void queryRefundDetail()
  {
    if (this.mRefundDetailRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/refundresultmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("orderid", String.valueOf(this.mOrderId));
    localBuilder.appendQueryParameter("token", accountService().token());
    this.mRefundDetailRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.mRefundDetailRequest, this);
  }

  private void showRefundDetail(DPObject paramDPObject)
  {
    ((TextView)findViewById(R.id.text_value)).setText(MovieUtil.getAmountSpannableString(this, paramDPObject.getString("RefundAmount")));
    ((TextView)findViewById(R.id.text_quantity)).setText(String.valueOf(paramDPObject.getInt("RefundQuantity")));
    ((TextView)findViewById(R.id.text_account)).setText(paramDPObject.getString("RefundToAccount"));
    updateFlowView(paramDPObject.getArray("ProcessItems"));
  }

  private void updateFlowView(DPObject[] paramArrayOfDPObject)
  {
    this.mFlowPanel.removeAllViews();
    if (DPObjectUtils.isArrayEmpty(paramArrayOfDPObject))
    {
      this.mFlowPanel.setVisibility(8);
      this.dividerLine.setVisibility(8);
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
    if (i < paramArrayOfDPObject.length)
    {
      localDPObject = paramArrayOfDPObject[i];
      localObject = LayoutInflater.from(this).inflate(R.layout.movie_refund_process_item, null, false);
      TextView localTextView1 = (TextView)((View)localObject).findViewById(R.id.step_view);
      TextView localTextView2 = (TextView)((View)localObject).findViewById(R.id.title);
      TextView localTextView3 = (TextView)((View)localObject).findViewById(R.id.content);
      TextView localTextView4 = (TextView)((View)localObject).findViewById(R.id.time);
      View localView1 = ((View)localObject).findViewById(R.id.process_line);
      View localView2 = ((View)localObject).findViewById(R.id.padding_line);
      if (i == 0)
      {
        localView2.setVisibility(4);
        label237: localTextView1.setText("" + (i + 1));
        localTextView2.setText(localDPObject.getString("Title"));
        localTextView3.setText(localDPObject.getString("Content"));
        long l = localDPObject.getTime("Date");
        if (l <= 0L)
          break label468;
        localTextView4.setText(DateUtils.formatDate2TimeZone(l, "yyyy-MM-dd HH:mm", "GMT+8"));
        label318: localTextView4.setTextColor(getResources().getColor(R.color.tuan_common_black));
        localTextView3.setTextColor(getResources().getColor(R.color.tuan_common_gray));
        if (arrayOfBoolean1[i] == 0)
          break label522;
        if (!localDPObject.getBoolean("IsWarning"))
          break label478;
        localTextView1.setBackgroundResource(R.drawable.movie_refund_flow_circle_warning);
        localTextView2.setTextColor(getResources().getColor(R.color.red));
        label389: localView2.setBackgroundColor(getResources().getColor(R.color.tuan_common_orange));
        if (arrayOfBoolean2[i] == 0)
          break label504;
        localView1.setBackgroundColor(getResources().getColor(R.color.tuan_common_orange));
      }
      while (true)
      {
        if (i == paramArrayOfDPObject.length - 1)
          localView1.setVisibility(4);
        this.mFlowPanel.addView((View)localObject);
        i += 1;
        break;
        localView2.setVisibility(0);
        break label237;
        label468: localTextView4.setText("");
        break label318;
        label478: localTextView1.setBackgroundResource(R.drawable.movie_refund_flow_circle_highlight);
        localTextView2.setTextColor(getResources().getColor(R.color.tuan_common_orange));
        break label389;
        label504: localView1.setBackgroundColor(getResources().getColor(R.color.movie_seperator_line));
        continue;
        label522: localTextView1.setBackgroundResource(R.drawable.movie_refund_flow_circle_gray);
        localView1.setBackgroundColor(getResources().getColor(R.color.movie_seperator_line));
        localView2.setBackgroundColor(getResources().getColor(R.color.movie_seperator_line));
        localTextView2.setTextColor(getResources().getColor(R.color.tuan_common_black));
      }
    }
    this.dividerLine.setVisibility(0);
    this.mFlowPanel.setVisibility(0);
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.movie_refund_detail);
    this.mOrderId = getIntParam("orderid");
    if (this.mOrderId == 0)
    {
      showToast("获取退款详情失败");
      finish();
    }
    do
    {
      return;
      this.mFlowPanel = ((LinearLayout)findViewById(R.id.refund_flow_panel));
      this.mFlowPanel.setVisibility(8);
      this.dividerLine = findViewById(R.id.divider_line);
      this.dividerLine.setVisibility(8);
    }
    while (!isLogined());
    queryRefundDetail();
  }

  protected void onDestroy()
  {
    if (this.mRefundDetailRequest != null)
    {
      mapiService().abort(this.mRefundDetailRequest, this, true);
      this.mRefundDetailRequest = null;
    }
    super.onDestroy();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if ((paramBoolean) && (isNeedLogin()))
      queryRefundDetail();
    return super.onLogin(paramBoolean);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRefundDetailRequest)
    {
      this.mRefundDetailRequest = null;
      showToast("退款信息查询失败", 1);
      finish();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRefundDetailRequest)
    {
      this.mRefundDetailRequest = null;
      showRefundDetail((DPObject)paramMApiResponse.result());
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieRefundDetailActivity
 * JD-Core Version:    0.6.0
 */