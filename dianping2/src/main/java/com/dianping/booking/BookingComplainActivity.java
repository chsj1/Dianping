package com.dianping.booking;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.booking.view.CustomDialogView;
import com.dianping.booking.view.PeoplePickerView;
import com.dianping.booking.view.TimePickerView;
import com.dianping.booking.view.TimePickerView.OnButtonClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BookingComplainActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  int bookingNum;
  DPObject bookingRecord;
  TextView datePicker;
  private MApiRequest mRequest;
  EditText messageEdit;
  EditText moneyEdit;
  ArrayList<String> numList;
  private Dialog peopleNumDialog;
  TextView peoplePicker;
  private PeoplePickerView peoplePickerDialogView;
  int recordID;
  Calendar setCal = Calendar.getInstance();
  Button submitBtn;
  private Dialog timeDialog;
  TextView timePicker;
  private TimePickerView timePickerDialogView;

  private void clearError()
  {
    this.moneyEdit.setError(null);
    this.messageEdit.setError(null);
  }

  private String formatDate(Calendar paramCalendar)
  {
    return new SimpleDateFormat("yyyy.MM.dd EE", Locale.getDefault()).format(paramCalendar.getTime());
  }

  private String formateTime(Calendar paramCalendar)
  {
    return paramCalendar.get(11) + ":" + String.format("%02d", new Object[] { Integer.valueOf(paramCalendar.get(12)) });
  }

  private void initPeopleNumDialog()
  {
    this.peopleNumDialog = new Dialog(this, R.style.dialog);
    this.peopleNumDialog.setCanceledOnTouchOutside(true);
    this.peoplePickerDialogView = ((PeoplePickerView)LayoutInflater.from(this).inflate(R.layout.people_picker_view, null, false));
    this.peoplePickerDialogView.setMaxValue(50);
    this.peoplePickerDialogView.setMinValue(1);
    CustomDialogView localCustomDialogView = (CustomDialogView)LayoutInflater.from(this).inflate(R.layout.number_picker_dialog, null, false);
    localCustomDialogView.setTitle("选择就餐人数").setView(this.peoplePickerDialogView).setPositiveButton("确定", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingComplainActivity.this.peopleNumDialog.dismiss();
        BookingComplainActivity.this.bookingNum = BookingComplainActivity.this.peoplePickerDialogView.getValue();
        BookingComplainActivity.this.setPeopleNum();
      }
    });
    this.peopleNumDialog.setContentView(localCustomDialogView);
  }

  private void initTimeDialog()
  {
    this.timeDialog = new Dialog(this, R.style.dialog);
    this.timeDialog.setCanceledOnTouchOutside(true);
    this.timePickerDialogView = ((TimePickerView)LayoutInflater.from(this).inflate(R.layout.time_picker_dialog, null, false));
    Object localObject = new ArrayList();
    SparseArray localSparseArray = new SparseArray();
    int i = 0;
    while (i < 24)
    {
      ((ArrayList)localObject).add(new BookingDateItem(i + "", 1));
      ArrayList localArrayList = new ArrayList();
      int j = 0;
      while (j < 4)
      {
        localArrayList.add(new BookingDateItem(j * 15 + "", 1));
        j += 1;
      }
      localSparseArray.put(i, localArrayList);
      i += 1;
    }
    this.timePickerDialogView.setDate((ArrayList)localObject, localSparseArray, 15);
    this.timePickerDialogView.setOnButtonClickListener(new TimePickerView.OnButtonClickListener()
    {
      public void onNegativeButtonClick()
      {
        BookingComplainActivity.this.timeDialog.dismiss();
      }

      public void onPositiveButtonClick()
      {
        BookingComplainActivity.this.timeDialog.dismiss();
        BookingComplainActivity.this.setCal.set(11, BookingComplainActivity.this.timePickerDialogView.getSelectHour());
        BookingComplainActivity.this.setCal.set(12, BookingComplainActivity.this.timePickerDialogView.getSelectMinute());
        BookingComplainActivity.this.timePicker.setText(BookingComplainActivity.this.formateTime(BookingComplainActivity.this.setCal));
      }
    });
    localObject = (CustomDialogView)LayoutInflater.from(this).inflate(R.layout.number_picker_dialog, null, false);
    ((CustomDialogView)localObject).setTitle("选择就餐时间").setView(this.timePickerDialogView);
    this.timeDialog.setContentView((View)localObject);
  }

  private void requestTask()
  {
    if (this.mRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    if (accountService() == null);
    for (String str = ""; ; str = accountService().token())
    {
      if (!TextUtils.isEmpty(str))
      {
        localArrayList.add("token");
        localArrayList.add(str);
      }
      localArrayList.add("clientUUID");
      localArrayList.add(Environment.uuid());
      localArrayList.add("orderId");
      localArrayList.add(String.valueOf(this.recordID));
      str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Long.valueOf(this.setCal.getTimeInMillis())).toString();
      localArrayList.add("arriveTime");
      localArrayList.add(str);
      localArrayList.add("sum");
      localArrayList.add(String.valueOf(this.moneyEdit.getText().toString().trim()));
      localArrayList.add("peoplecount");
      localArrayList.add(String.valueOf(this.bookingNum));
      localArrayList.add("remark");
      localArrayList.add(String.valueOf(this.messageEdit.getText().toString().trim()));
      this.mRequest = BasicMApiRequest.mapiPost("http://rs.api.dianping.com/revisearrival.yy", (String[])localArrayList.toArray(new String[0]));
      mapiService().exec(this.mRequest, this);
      return;
    }
  }

  private void setPeopleNum()
  {
    this.peoplePicker.setText(this.bookingNum + "");
  }

  private void setupValue()
  {
    if (this.bookingRecord != null)
    {
      this.bookingNum = this.bookingRecord.getInt("PeopleNumber");
      long l = this.bookingRecord.getTime("BookingTime");
      this.recordID = this.bookingRecord.getInt("ID");
      this.setCal.setTimeInMillis(l);
      this.datePicker.setText(formatDate(this.setCal));
      this.timePicker.setText(formateTime(this.setCal));
      this.peoplePicker.setText("" + this.bookingNum);
    }
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.time_picker)
    {
      this.timePickerDialogView.setSelectDate(this.setCal);
      this.timeDialog.show();
    }
    do
    {
      return;
      if (i != R.id.people_num)
        continue;
      if (this.bookingNum > 0)
        this.peoplePickerDialogView.setValue(this.bookingNum);
      this.peopleNumDialog.show();
      return;
    }
    while (i != R.id.submit_complain);
    if (TextUtils.isEmpty(this.moneyEdit.getText().toString()))
    {
      this.moneyEdit.requestFocus();
      this.moneyEdit.setError(Html.fromHtml("<font color=#ff0000> 消费金额不能为空 </font>"));
      return;
    }
    if (TextUtils.isEmpty(this.messageEdit.getText().toString()))
    {
      this.messageEdit.requestFocus();
      this.messageEdit.setError(Html.fromHtml("<font color=#ff0000> 请填写补充信息 </font>"));
      return;
    }
    try
    {
      i = Integer.valueOf(this.moneyEdit.getText().toString().trim()).intValue();
      if (i <= 0)
      {
        this.moneyEdit.requestFocus();
        this.moneyEdit.setError(Html.fromHtml("<font color=#ff0000> 消费金额不能为零 </font>"));
        return;
      }
    }
    catch (NumberFormatException paramView)
    {
      this.moneyEdit.requestFocus();
      this.moneyEdit.setError(Html.fromHtml("<font color=#ff0000> 输入不合法 </font>"));
      paramView.printStackTrace();
      return;
    }
    showProgressDialog("正在提交申请，请稍候...");
    requestTask();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().setBackgroundDrawable(null);
    super.setContentView(R.layout.booking_complain);
    this.bookingRecord = ((DPObject)getIntent().getParcelableExtra("bookingRecord"));
    this.datePicker = ((TextView)findViewById(R.id.date_picker));
    this.timePicker = ((TextView)findViewById(R.id.time_picker));
    this.moneyEdit = ((EditText)findViewById(R.id.money));
    this.messageEdit = ((EditText)findViewById(R.id.message));
    this.peoplePicker = ((TextView)findViewById(R.id.people_num));
    this.submitBtn = ((Button)findViewById(R.id.submit_complain));
    paramBundle = new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        BookingComplainActivity.this.clearError();
        return false;
      }
    };
    this.moneyEdit.setOnTouchListener(paramBundle);
    this.messageEdit.setOnTouchListener(paramBundle);
    this.timePicker.setOnClickListener(this);
    this.peoplePicker.setOnClickListener(this);
    this.submitBtn.setOnClickListener(this);
    initTimeDialog();
    initPeopleNumDialog();
    setupValue();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mRequest != null)
    {
      mapiService().abort(this.mRequest, this, true);
      this.mRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    showShortToast("提交失败，请重试..");
    this.mRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    paramMApiRequest = new Intent("com.dianping.booking.BOOKING_COMPLAIN");
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("bookingRecord", (DPObject)paramMApiResponse.result());
    paramMApiRequest.putExtras(localBundle);
    sendBroadcast(paramMApiRequest);
    finish();
    this.mRequest = null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingComplainActivity
 * JD-Core Version:    0.6.0
 */