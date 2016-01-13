package com.dianping.travel;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.util.telephone.ContactUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.DPBasicItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SceneryOrderInfoActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private DPBasicItem addressItem;
  private Button cancelOrderBtn;
  private MApiRequest cancelOrderRequest;
  private DPBasicItem countItem;
  private DPBasicItem guestItem;
  private DPBasicItem noticeItem;
  private DPBasicItem orderDateItem;
  private int orderID;
  private DPBasicItem orderNumberItem;
  private MApiRequest request;
  private DPObject result;
  private DPBasicItem serviceNoItem;
  private DPBasicItem shopNameItem;
  private TextView statusTv;
  private DPBasicItem totalPriceItem;
  private DPBasicItem travelDateItem;
  private DPBasicItem typeItem;

  private void handlerResult()
  {
    if (this.result == null)
      return;
    setStatus(this.result.getInt("Status"));
    this.orderNumberItem.setSubTitle(this.result.getString("SerialId"));
    this.totalPriceItem.setSubTitle("￥" + this.result.getInt("TotalPrice"));
    this.addressItem.setSubTitle(this.result.getString("PickUpPlace"));
    this.shopNameItem.setTitle(this.result.getString("ShopName"));
    this.typeItem.setSubTitle(this.result.getString("Type"));
    this.countItem.setSubTitle(String.valueOf(this.result.getInt("Count") + "张"));
    this.travelDateItem.setSubTitle(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(this.result.getTime("TravelDate"))));
    DPObject localDPObject = this.result.getObject("Guest");
    if (localDPObject != null)
      this.guestItem.setSubTitle(localDPObject.getString("Name") + "," + localDPObject.getString("PhoneNo"));
    this.orderDateItem.setSubTitle(new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA).format(new Date(this.result.getTime("OrderDate"))));
    this.serviceNoItem.setSubTitle(this.result.getString("ServicePhoneNo"));
  }

  private void initView()
  {
    this.statusTv = ((TextView)findViewById(R.id.status));
    this.orderNumberItem = ((DPBasicItem)findViewById(R.id.orderNumItem));
    this.totalPriceItem = ((DPBasicItem)findViewById(R.id.totalpriceItem));
    this.addressItem = ((DPBasicItem)findViewById(R.id.addressItem));
    this.addressItem.getItemSubtitle().setSingleLine(false);
    this.shopNameItem = ((DPBasicItem)findViewById(R.id.shopNameItem));
    this.typeItem = ((DPBasicItem)findViewById(R.id.typeItem));
    this.typeItem.getItemSubtitle().setSingleLine(false);
    this.countItem = ((DPBasicItem)findViewById(R.id.countItem));
    this.travelDateItem = ((DPBasicItem)findViewById(R.id.travelDateItem));
    this.guestItem = ((DPBasicItem)findViewById(R.id.guestItem));
    this.guestItem.getItemSubtitle().setSingleLine(false);
    this.orderDateItem = ((DPBasicItem)findViewById(R.id.orderDateItem));
    this.serviceNoItem = ((DPBasicItem)findViewById(R.id.serviceNoItem));
    this.noticeItem = ((DPBasicItem)findViewById(R.id.noticeItem));
    this.cancelOrderBtn = ((Button)findViewById(R.id.cancelOrder));
    this.shopNameItem.setOnClickListener(this);
    this.serviceNoItem.setOnClickListener(this);
    this.noticeItem.setOnClickListener(this);
    this.cancelOrderBtn.setOnClickListener(this);
  }

  private void requestCancle()
  {
    showProgressDialog("正在取消...");
    if (this.cancelOrderRequest != null)
      mapiService().abort(this.cancelOrderRequest, this, true);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("serialid");
    localArrayList.add(this.result.getString("SerialId"));
    this.cancelOrderRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/cancelsceneryorder.bin?", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.cancelOrderRequest, this);
  }

  private void requestData()
  {
    showProgressDialog("正在加载数据...");
    if (this.request != null)
      mapiService().abort(this.request, this, true);
    this.request = BasicMApiRequest.mapiGet("http://m.api.dianping.com/sceneryorder.bin?id=" + this.orderID + "&clientUUID=" + Environment.uuid(), CacheType.DISABLED);
    mapiService().exec(this.request, this);
  }

  private void setStatus(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return;
    case 0:
      this.statusTv.setText("预订成功");
      this.statusTv.setCompoundDrawables(getResources().getDrawable(R.drawable.common_indicator_icon_complete), null, null, null);
      this.cancelOrderBtn.setVisibility(0);
      return;
    case 1:
      this.statusTv.setText("已取消");
      this.statusTv.setCompoundDrawables(getResources().getDrawable(R.drawable.common_indicator_icon_fail), null, null, null);
      return;
    case 2:
    }
    this.statusTv.setText("已使用");
    this.statusTv.setCompoundDrawables(getResources().getDrawable(R.drawable.common_indicator_icon_fail), null, null, null);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.shopNameItem)
      if (this.result != null);
    do
      while (true)
      {
        return;
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + this.result.getInt("ShopID"))));
        statisticsEvent("myticket5", "myticket5_orderdetail_shopinfo", "", 0);
        return;
        if (paramView == this.noticeItem)
        {
          if (this.result == null)
            continue;
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://sceneryordernotice"));
          paramView.putExtra("notices", this.result.getArray("Notices"));
          startActivity(paramView);
          statisticsEvent("myticket5", "myticket5_orderdetail_notice", "", 0);
          return;
        }
        if (paramView != this.serviceNoItem)
          break;
        if (this.result == null)
          continue;
        ContactUtils.dial(this, this.result.getString("ServicePhoneNo"));
        statisticsEvent("myticket5", "myticket5_orderdetail_tel", "", 0);
        return;
      }
    while (paramView != this.cancelOrderBtn);
    requestCancle();
    statisticsEvent("myticket5", "myticket5_orderdetail_cancle", "", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.scenery_order_info);
    initView();
    paramBundle = getIntent().getData().getQueryParameter("orderid");
    try
    {
      this.orderID = Integer.valueOf(paramBundle).intValue();
      requestData();
      return;
    }
    catch (Exception paramBundle)
    {
      while (true)
        Log.e("FormatException , ex = " + paramBundle.getMessage());
    }
  }

  protected void onDestroy()
  {
    if (this.request != null)
      mapiService().abort(this.request, this, true);
    if (this.cancelOrderRequest != null)
      mapiService().abort(this.cancelOrderRequest, this, true);
    super.onDestroy();
  }

  public void onProgressDialogCancel()
  {
    if (this.cancelOrderRequest != null)
      mapiService().abort(this.cancelOrderRequest, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.request)
      showMessageDialog(paramMApiResponse.message(), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          SceneryOrderInfoActivity.this.finish();
        }
      });
    do
      return;
    while (paramMApiRequest != this.cancelOrderRequest);
    paramMApiRequest = new AlertDialog.Builder(this);
    paramMApiRequest.setMessage(paramMApiResponse.message().content() + "\n是否拨打客服电话[]咨询？");
    if (paramMApiResponse.message().flag() == 1)
    {
      paramMApiRequest.setPositiveButton("联系客服", new DialogInterface.OnClickListener(paramMApiResponse)
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          ContactUtils.dial(SceneryOrderInfoActivity.this.getApplicationContext(), this.val$resp.message().title());
        }
      });
      paramMApiRequest.setMessage(paramMApiResponse.message().content() + "\n是否拨打客服电话[" + paramMApiResponse.message().title() + "]咨询？");
    }
    while (true)
    {
      paramMApiRequest.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      });
      paramMApiRequest.create().show();
      return;
      paramMApiRequest.setMessage(paramMApiResponse.message().content());
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.request)
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.result = ((DPObject)paramMApiResponse.result());
        handlerResult();
      }
    do
    {
      return;
      onRequestFailed(paramMApiRequest, paramMApiResponse);
      return;
    }
    while (paramMApiRequest != this.cancelOrderRequest);
    Toast.makeText(this, "取消成功", 0).show();
    setResult(-1);
    finish();
    sendBroadcast(new Intent("scenery_order_list_changed"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.SceneryOrderInfoActivity
 * JD-Core Version:    0.6.0
 */