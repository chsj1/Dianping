package com.dianping.travel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.SinglePickerView;
import com.dianping.base.widget.SinglePickerView.OnDataChangeListener;
import com.dianping.base.widget.wheel.adapter.AbstractWheelTextAdapter;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.travel.view.PriceCalendarView;
import com.dianping.travel.view.PriceCalendarView.OnDateChangeListener;
import com.dianping.util.DateUtil;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.DPBasicItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SubmitSceneryOrderActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final int DEFAULT_NUM = 1;
  private String bName;
  private DPBasicItem bNameItem;
  private String bPhoneNo;
  private DPBasicItem bPhoneNoItem;
  private Calendar bookAbleCal;
  private int bookingNum = 1;
  private Dialog calendarDialog;
  private PriceCalendarView calendarView;
  private int count;
  private DPBasicItem countItem;
  private Calendar curCalendar;
  private TextView name;
  private int price;
  private DPBasicItem priceItem;
  private MApiRequest request;
  private Calendar selCalendar;
  private int shopID;
  private Button submit;
  private DPObject ticket;
  private Dialog ticketsNumDialog;
  private SinglePickerView ticketsNumView;
  private DPBasicItem totalPriceItem;
  private DPBasicItem travelDataItem;

  private String checkRight()
  {
    if (this.count == 0)
    {
      Toast.makeText(this, "您还没有选择张数", 0).show();
      return "您还没有选择张数";
    }
    TextView localTextView = (TextView)this.bNameItem.findViewById(R.id.itemInput);
    this.bName = localTextView.getText().toString().trim();
    if (TextUtils.isEmpty(this.bName))
    {
      localTextView.setError(Html.fromHtml("<font color=#ff0000>取票人不能为空</font>"));
      localTextView.requestFocus();
      return "取票人不能为空";
    }
    localTextView = (TextView)this.bPhoneNoItem.findViewById(R.id.itemInput);
    this.bPhoneNo = localTextView.getText().toString().trim();
    if (TextUtils.isEmpty(this.bPhoneNo))
    {
      localTextView.setError(Html.fromHtml("<font color=#ff0000>手机号不能为空</font>"));
      localTextView.requestFocus();
      return "手机号不能为空";
    }
    if ((this.bPhoneNo != null) && (this.bPhoneNo.length() < 11))
    {
      localTextView.setError(Html.fromHtml("<font color=#ff0000>手机号错误</font>"));
      localTextView.requestFocus();
      return "手机号错误";
    }
    return null;
  }

  private void initCalendarDialog()
  {
    this.curCalendar = Calendar.getInstance();
    this.selCalendar = Calendar.getInstance();
    this.curCalendar.setTimeInMillis(DateUtil.currentTimeMillis());
    Object localObject = getIntent().getData();
    int j = Integer.valueOf(((Uri)localObject).getQueryParameter("aheaddays")).intValue();
    localObject = ((Uri)localObject).getQueryParameter("aheadtime").split(":");
    this.bookAbleCal = Calendar.getInstance();
    this.bookAbleCal.setTimeInMillis(DateUtil.currentTimeMillis());
    this.bookAbleCal.set(11, Integer.valueOf(localObject[0]).intValue());
    this.bookAbleCal.set(12, Integer.valueOf(localObject[1]).intValue());
    localObject = this.bookAbleCal;
    int i = j;
    if (isLate())
      i = j + 1;
    ((Calendar)localObject).add(5, i);
    this.selCalendar.setTimeInMillis(this.bookAbleCal.getTimeInMillis());
    this.calendarDialog = new Dialog(this, R.style.dialog);
    this.calendarDialog.setCanceledOnTouchOutside(true);
    this.calendarView = new PriceCalendarView(this);
    this.calendarDialog.setContentView(this.calendarView);
    this.calendarView.setDate(this.curCalendar, this.bookAbleCal, true, this.ticket.getInt("ID"), this.price);
    this.calendarView.setOnDateChangeListener(new PriceCalendarView.OnDateChangeListener()
    {
      public void onDateChange(Calendar paramCalendar, int paramInt)
      {
        SubmitSceneryOrderActivity.this.selCalendar.setTimeInMillis(paramCalendar.getTimeInMillis());
        if (paramInt > 0)
        {
          SubmitSceneryOrderActivity.access$102(SubmitSceneryOrderActivity.this, paramInt);
          SubmitSceneryOrderActivity.this.priceItem.setSubTitle("￥" + paramInt);
          SubmitSceneryOrderActivity.this.totalPriceItem.setSubTitle(String.valueOf(SubmitSceneryOrderActivity.this.count * paramInt));
        }
        SubmitSceneryOrderActivity.this.travelDataItem.setSubTitle(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(SubmitSceneryOrderActivity.this.selCalendar.getTime()));
        if (SubmitSceneryOrderActivity.this.calendarDialog.isShowing())
          SubmitSceneryOrderActivity.this.calendarDialog.dismiss();
      }
    });
  }

  private void initTicketNumDialog(int paramInt1, int paramInt2)
  {
    if (paramInt1 > paramInt2)
      return;
    this.ticketsNumDialog = new Dialog(this, R.style.dialog);
    this.ticketsNumDialog.setCanceledOnTouchOutside(true);
    this.ticketsNumView = new SinglePickerView(this);
    ArrayList localArrayList = new ArrayList();
    while (paramInt1 < paramInt2 + 1)
    {
      localArrayList.add(String.valueOf(paramInt1));
      paramInt1 += 1;
    }
    StringWheelArrayAdapter localStringWheelArrayAdapter = new StringWheelArrayAdapter(this, (String[])localArrayList.toArray(new String[localArrayList.size()]));
    this.ticketsNumView.setAdapter(localStringWheelArrayAdapter);
    this.ticketsNumView.setOnDataChangeListener(new SinglePickerView.OnDataChangeListener(localArrayList)
    {
      public void onDataChange()
      {
        SubmitSceneryOrderActivity.this.countItem.setSubTitle((String)this.val$numList.get(SubmitSceneryOrderActivity.this.ticketsNumView.getCurrentItem()) + "张");
        SubmitSceneryOrderActivity.access$302(SubmitSceneryOrderActivity.this, Integer.valueOf((String)this.val$numList.get(SubmitSceneryOrderActivity.this.ticketsNumView.getCurrentItem())).intValue());
        if (SubmitSceneryOrderActivity.this.count > 0)
          SubmitSceneryOrderActivity.this.totalPriceItem.setSubTitle("￥" + SubmitSceneryOrderActivity.this.price * SubmitSceneryOrderActivity.this.count);
        SubmitSceneryOrderActivity.this.countItem.setSubTitleTextType(8);
        if (SubmitSceneryOrderActivity.this.ticketsNumDialog.isShowing())
          SubmitSceneryOrderActivity.this.ticketsNumDialog.dismiss();
      }
    });
    this.ticketsNumDialog.setContentView(this.ticketsNumView);
  }

  private void initView()
  {
    getWindow().setSoftInputMode(3);
    if (this.ticket == null);
    String[] arrayOfString;
    do
    {
      return;
      this.name = ((TextView)findViewById(R.id.name));
      this.priceItem = ((DPBasicItem)findViewById(R.id.price));
      this.price = this.ticket.getInt("TCPrice");
      this.count = this.ticket.getInt("MinT");
      this.countItem = ((DPBasicItem)findViewById(R.id.count));
      this.countItem.setSubTitle(this.count + "张");
      this.countItem.setOnClickListener(this);
      this.totalPriceItem = ((DPBasicItem)findViewById(R.id.totalprice));
      this.travelDataItem = ((DPBasicItem)findViewById(R.id.traveldate));
      this.travelDataItem.setOnClickListener(this);
      this.bNameItem = ((DPBasicItem)findViewById(R.id.bname));
      this.bPhoneNoItem = ((DPBasicItem)findViewById(R.id.bphoneno));
      this.submit = ((Button)findViewById(R.id.submit));
      this.submit.setOnClickListener(this);
      this.totalPriceItem.setSubTitle(String.valueOf(this.price * this.count));
      initCalendarDialog();
      initTicketNumDialog(this.ticket.getInt("MinT"), this.ticket.getInt("MaxT"));
      this.name.setText(this.ticket.getString("Name"));
      this.priceItem.setSubTitle("￥" + this.price);
      this.travelDataItem.setSubTitle(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(this.selCalendar.getTime()));
      arrayOfString = preferences().getString("sceneryOrderGuest", "").split("split");
    }
    while (arrayOfString.length != 2);
    this.bNameItem.setInputText(arrayOfString[0]);
    this.bPhoneNoItem.setInputText(arrayOfString[1]);
  }

  private boolean isLate()
  {
    if (this.curCalendar.get(11) > this.bookAbleCal.get(11));
    do
    {
      return true;
      if (this.curCalendar.get(11) < this.bookAbleCal.get(11))
        return false;
    }
    while (this.curCalendar.get(12) >= this.bookAbleCal.get(12));
    return false;
  }

  private void submit()
  {
    Object localObject = checkRight();
    if (localObject != null)
    {
      statisticsEvent("ticket5", "ticket5_submit_failure", (String)localObject, 0);
      return;
    }
    preferences().edit().putString("sceneryOrderGuest", this.bName + "split" + this.bPhoneNo).commit();
    if (this.request != null)
      mapiService().abort(this.request, this, true);
    localObject = new ArrayList();
    if (accountService().token() != null)
    {
      ((ArrayList)localObject).add("token");
      ((ArrayList)localObject).add(accountService().token());
    }
    ((ArrayList)localObject).add("shopid");
    ((ArrayList)localObject).add(String.valueOf(this.shopID));
    ((ArrayList)localObject).add("ticketid");
    ((ArrayList)localObject).add(String.valueOf(this.ticket.getInt("ID")));
    ((ArrayList)localObject).add("count");
    ((ArrayList)localObject).add(String.valueOf(this.count));
    ((ArrayList)localObject).add("totalprice");
    ((ArrayList)localObject).add(String.valueOf(this.price * this.count));
    ((ArrayList)localObject).add("traveldate");
    ((ArrayList)localObject).add(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Long.valueOf(this.selCalendar.getTimeInMillis())).toString());
    ((ArrayList)localObject).add("bname");
    ((ArrayList)localObject).add(this.bName);
    ((ArrayList)localObject).add("bphoneno");
    ((ArrayList)localObject).add(this.bPhoneNo);
    ((ArrayList)localObject).add("clientUUID");
    ((ArrayList)localObject).add(Environment.uuid());
    this.request = BasicMApiRequest.mapiPost("http://m.api.dianping.com/submitsceneryorder.bin?", (String[])((ArrayList)localObject).toArray(new String[0]));
    mapiService().exec(this.request, this);
    showProgressDialog("正在提交...");
    if (this.managedDialog != null)
      this.managedDialog.setCancelable(false);
    statisticsEvent("ticket5", "ticket5_submit_success", "", 0);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.submit)
      submit();
    if (paramView.getId() == R.id.traveldate)
    {
      if (!this.calendarDialog.isShowing())
        break label59;
      this.calendarDialog.dismiss();
    }
    while ((paramView.getId() != R.id.count) || (this.ticket == null))
    {
      return;
      label59: this.calendarDialog.show();
    }
    if (this.ticketsNumDialog.isShowing())
    {
      this.ticketsNumDialog.dismiss();
      return;
    }
    if (this.bookingNum > 0)
      this.ticketsNumView.setCurrentItem(this.bookingNum - 1);
    while (true)
    {
      this.ticketsNumDialog.show();
      return;
      this.ticketsNumView.setCurrentItem(0);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.submit_scenery_order);
    paramBundle = getIntent().getData().getQueryParameter("shopid");
    try
    {
      this.shopID = Integer.valueOf(paramBundle).intValue();
      this.ticket = ((DPObject)getIntent().getParcelableExtra("ticket"));
      initView();
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
    super.onDestroy();
  }

  public void onProgressDialogCancel()
  {
    if (this.request != null)
      mapiService().abort(this.request, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    showMessageDialog(paramMApiResponse.message());
    statisticsEvent("ticket5", "ticket5_submit_failure", paramMApiResponse.message().content(), 0);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiRequest = new Intent("android.intent.action.VIEW", Uri.parse("dianping://submitsuccess"));
      paramMApiRequest.putExtra("successMsg", (DPObject)paramMApiResponse.result());
      startActivity(paramMApiRequest);
      paramMApiResponse = new Intent("com.dianping.action.TICKETCHANGED");
      paramMApiRequest.putExtra("submitnew", true);
      sendBroadcast(paramMApiResponse);
      finish();
    }
  }

  class StringWheelArrayAdapter extends AbstractWheelTextAdapter
  {
    private String[] lists;

    protected StringWheelArrayAdapter(Context paramArrayOfString, String[] arg3)
    {
      super(R.layout.single_picker_item, 0);
      setItemTextResource(R.id.text1);
      Object localObject;
      this.lists = localObject;
    }

    public View getItem(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (TextView)super.getItem(paramInt, paramView, paramViewGroup);
      paramView.setGravity(17);
      return paramView;
    }

    protected CharSequence getItemText(int paramInt)
    {
      return this.lists[paramInt];
    }

    public int getItemsCount()
    {
      return this.lists.length;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.SubmitSceneryOrderActivity
 * JD-Core Version:    0.6.0
 */