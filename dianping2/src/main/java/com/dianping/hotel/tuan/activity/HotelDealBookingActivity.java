package com.dianping.hotel.tuan.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HotelDealBookingActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final SimpleDateFormat SDF = new SimpleDateFormat("M月dd日", Locale.getDefault());
  private long checkinTime;
  private long checkoutTime;
  private long dayNum = 1L;
  private int maxCoupon;
  private int maxRoom;
  private int minCoupon;
  private int minRoom;
  private MApiRequest request;
  DPObject result;
  private MApiRequest submitPostRequest;

  private void sendRequest()
  {
    this.request = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/hoteltg/hotelbookingdefaultinfo.hoteltg").buildUpon().appendQueryParameter("orderid", getIntParam("orderid") + "").toString(), CacheType.DISABLED);
    mapiService().exec(this.request, this);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    long l1;
    long l3;
    long l2;
    long l4;
    if (paramInt1 == 1)
    {
      l1 = paramIntent.getLongExtra("checkinTime", -1L);
      l3 = paramIntent.getLongExtra("checkoutTime", -1L);
      l2 = Integer.parseInt(((EditText)findViewById(R.id.num)).getText().toString());
      paramInt1 = (int)((l3 - l1) / 86400000L);
      l4 = paramInt1 * l2;
      if ((l1 != -1L) && (l3 != -1L))
      {
        if (l4 >= this.minCoupon)
          break label139;
        Toast.makeText(this, "请增加您的入住天数或房间数，您最少需预订" + this.minCoupon + "间夜", 1).show();
      }
    }
    label139: 
    do
    {
      while (true)
      {
        return;
        if (l4 > this.maxCoupon)
        {
          paramIntent = new StringBuilder().append("您的点评券不足，只能预约");
          l3 = this.maxCoupon;
          l1 = l2;
          if (l2 <= 0L)
            l1 = 1L;
          Toast.makeText(this, l3 / l1 + "晚", 1).show();
          return;
        }
        this.dayNum = paramInt1;
        this.checkinTime = l1;
        this.checkoutTime = l3;
        findViewById(R.id.submit).setEnabled(true);
        if (this.checkinTime > 0L)
          ((TextView)findViewById(R.id.checkin)).setText("入住" + SDF.format(Long.valueOf(this.checkinTime)) + "－");
        if (this.checkoutTime > 0L)
          ((TextView)findViewById(R.id.checkout)).setText("退房 " + SDF.format(Long.valueOf(this.checkoutTime)));
        ((TextView)findViewById(R.id.day_num)).setText((this.checkoutTime - this.checkinTime) / 86400000L + "晚");
        ((TextView)findViewById(R.id.tip2)).setText("本次预订需要使用" + l4 + "张团购券");
        paramIntent = Calendar.getInstance();
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(this.checkinTime);
        l1 = this.result.getTime("CancelTime");
        if ((paramIntent.get(5) != localCalendar.get(5)) || (paramIntent.get(2) != localCalendar.get(2)))
          break;
        if (paramIntent.getTimeInMillis() < l1)
        {
          paramIntent = this.result.getString("TipsCanCancel");
          if (TextUtils.isEmpty(paramIntent))
            continue;
          paramIntent = paramIntent.replace("%s", SDF.format(Long.valueOf(this.checkinTime)));
          ((TextView)findViewById(R.id.tip1)).setText(paramIntent);
          return;
        }
        paramIntent = this.result.getString("TipsCannotCancel");
        if (TextUtils.isEmpty(paramIntent))
          continue;
        paramIntent = paramIntent.replace("%s", SDF.format(Long.valueOf(this.checkinTime)));
        ((TextView)findViewById(R.id.tip1)).setText(paramIntent);
        return;
      }
      paramIntent = this.result.getString("TipsCanCancel").replace("%s", SDF.format(Long.valueOf(this.checkinTime)));
    }
    while (TextUtils.isEmpty(paramIntent));
    ((TextView)findViewById(R.id.tip1)).setText(paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_hotel_deal_booking);
    sendRequest();
    showProgressDialog("正在获取预订信息...");
    paramBundle = (Button)findViewById(R.id.sub_btn);
    Button localButton = (Button)findViewById(R.id.add_btn);
    EditText localEditText = (EditText)findViewById(R.id.num);
    localEditText.addTextChangedListener(new TextWatcher(paramBundle, localButton, localEditText)
    {
      public void afterTextChanged(Editable paramEditable)
      {
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
        paramCharSequence = paramCharSequence.toString();
        if (TextUtils.isEmpty(paramCharSequence))
          return;
        long l1 = Long.parseLong(paramCharSequence);
        long l2 = HotelDealBookingActivity.this.dayNum * l1;
        if (l2 > HotelDealBookingActivity.this.maxCoupon)
        {
          paramCharSequence = HotelDealBookingActivity.this;
          StringBuilder localStringBuilder = new StringBuilder().append("您的点评券不足，只能预约");
          l2 = HotelDealBookingActivity.this.maxCoupon;
          if (HotelDealBookingActivity.this.dayNum <= 0L)
            l1 = 1L;
          while (true)
          {
            Toast.makeText(paramCharSequence, l2 / l1 + "间", 1).show();
            HotelDealBookingActivity.this.findViewById(R.id.submit).setEnabled(false);
            this.val$sub_btn.setEnabled(true);
            return;
            l1 = HotelDealBookingActivity.this.dayNum;
          }
        }
        if (l2 < HotelDealBookingActivity.this.minCoupon)
        {
          Toast.makeText(HotelDealBookingActivity.this, "请增加您的入住天数或房间数，您最少需预订" + HotelDealBookingActivity.this.minCoupon + "间夜", 1).show();
          HotelDealBookingActivity.this.findViewById(R.id.submit).setEnabled(false);
          this.val$add_btn.setEnabled(true);
          return;
        }
        HotelDealBookingActivity.this.findViewById(R.id.submit).setEnabled(true);
        if (l2 >= HotelDealBookingActivity.this.maxCoupon)
        {
          this.val$add_btn.setEnabled(false);
          if (l2 > HotelDealBookingActivity.this.minCoupon)
            break label394;
          this.val$sub_btn.setEnabled(false);
          label288: if (l1 <= 9999L)
            break label405;
          this.val$edit_num.setText("9999");
          Toast.makeText(HotelDealBookingActivity.this, "最多选择9999间", 1).show();
          this.val$add_btn.setEnabled(false);
          HotelDealBookingActivity.this.findViewById(R.id.submit).setEnabled(false);
        }
        while (true)
        {
          ((TextView)HotelDealBookingActivity.this.findViewById(R.id.tip2)).setText("本次预订需要使用" + l2 + "张团购券");
          return;
          this.val$add_btn.setEnabled(true);
          break;
          label394: this.val$sub_btn.setEnabled(true);
          break label288;
          label405: if (l1 >= 1L)
            continue;
          this.val$edit_num.setText("1");
          Toast.makeText(HotelDealBookingActivity.this, "最少选择1间", 1).show();
          this.val$sub_btn.setEnabled(false);
          HotelDealBookingActivity.this.findViewById(R.id.submit).setEnabled(false);
        }
      }
    });
    paramBundle.setOnClickListener(new View.OnClickListener(localEditText, paramBundle)
    {
      public void onClick(View paramView)
      {
        try
        {
          i = Integer.parseInt(this.val$edit_num.getText().toString());
          if (i <= 1)
            this.val$sub_btn.setEnabled(false);
          this.val$edit_num.setText(i - 1 + "");
          return;
        }
        catch (Exception paramView)
        {
          while (true)
            int i = 0;
        }
      }
    });
    localButton.setOnClickListener(new View.OnClickListener(localEditText)
    {
      public void onClick(View paramView)
      {
        try
        {
          i = Integer.parseInt(this.val$edit_num.getText().toString());
          this.val$edit_num.setText(i + 1 + "");
          return;
        }
        catch (Exception paramView)
        {
          while (true)
            int i = 0;
        }
      }
    });
    findViewById(R.id.select_time).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("dianping://hotelbookingpicktime?channel=5&orderid=").append(HotelDealBookingActivity.this.getIntParam("orderid"));
        if (HotelDealBookingActivity.this.checkinTime > 0L)
        {
          paramView = "&checkin_time=" + HotelDealBookingActivity.this.checkinTime;
          localStringBuilder = localStringBuilder.append(paramView);
          if (HotelDealBookingActivity.this.checkoutTime <= 0L)
            break label144;
        }
        label144: for (paramView = "&checkout_time=" + HotelDealBookingActivity.this.checkoutTime; ; paramView = "")
        {
          paramView = Uri.parse(paramView);
          HotelDealBookingActivity.this.startActivityForResult(new Intent("android.intent.action.VIEW", paramView), 1);
          return;
          paramView = "";
          break;
        }
      }
    });
    findViewById(R.id.submit).setOnClickListener(new View.OnClickListener(localEditText)
    {
      public void onClick(View paramView)
      {
        paramView = new ArrayList();
        paramView.add("checkindate");
        paramView.add(HotelDealBookingActivity.this.checkinTime + "");
        paramView.add("checkoutdate");
        paramView.add(HotelDealBookingActivity.this.checkoutTime + "");
        paramView.add("roomcount");
        int i = 0;
        try
        {
          int j = Integer.parseInt(this.val$edit_num.getText().toString());
          i = j;
          paramView.add(j + "");
          i = j;
          label137: paramView.add("couponcount");
          paramView.add(HotelDealBookingActivity.this.dayNum * i + "");
          paramView.add("orderid");
          paramView.add(HotelDealBookingActivity.this.getIntParam("orderid") + "");
          String str = ((EditText)HotelDealBookingActivity.this.findViewById(R.id.username)).getText().toString();
          if (TextUtils.isEmpty(str))
          {
            Toast.makeText(HotelDealBookingActivity.this, "入住人不能为空！", 1).show();
            return;
          }
          paramView.add("username");
          paramView.add(str);
          str = ((EditText)HotelDealBookingActivity.this.findViewById(R.id.telephone)).getText().toString();
          if (TextUtils.isEmpty(str))
          {
            Toast.makeText(HotelDealBookingActivity.this, "联系电话不能为空！", 1).show();
            return;
          }
          paramView.add("telephone");
          paramView.add(str);
          paramView.add("from");
          paramView.add("1");
          HotelDealBookingActivity.access$502(HotelDealBookingActivity.this, BasicMApiRequest.mapiPost("http://m.api.dianping.com/hoteltg/onlinebooking.hoteltg", (String[])paramView.toArray(new String[0])));
          HotelDealBookingActivity.this.mapiService().exec(HotelDealBookingActivity.this.submitPostRequest, HotelDealBookingActivity.this);
          HotelDealBookingActivity.this.showProgressDialog("正在提交预订...");
          return;
        }
        catch (Exception localException)
        {
          break label137;
        }
      }
    });
    paramBundle = (EditText)findViewById(R.id.username);
    paramBundle.setSelection(paramBundle.getText().length());
    paramBundle.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramView, boolean paramBoolean)
      {
        paramView = (EditText)paramView;
        if (paramBoolean)
        {
          paramView.setTag(paramView.getHint().toString());
          paramView.setHint("");
          paramView = paramView.getContext();
          localObject = HotelDealBookingActivity.this;
          ((InputMethodManager)paramView.getSystemService("input_method")).toggleSoftInput(0, 2);
          return;
        }
        paramView.setHint(paramView.getTag().toString());
        Object localObject = paramView.getContext();
        HotelDealBookingActivity localHotelDealBookingActivity = HotelDealBookingActivity.this;
        ((InputMethodManager)((Context)localObject).getSystemService("input_method")).hideSoftInputFromWindow(paramView.getWindowToken(), 0);
      }
    });
    paramBundle = (EditText)findViewById(R.id.telephone);
    paramBundle.setSelection(paramBundle.getText().length());
    paramBundle.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramView, boolean paramBoolean)
      {
        paramView = (EditText)paramView;
        if (paramBoolean)
        {
          paramView.setTag(paramView.getHint().toString());
          paramView.setHint("");
          paramView = paramView.getContext();
          localObject = HotelDealBookingActivity.this;
          ((InputMethodManager)paramView.getSystemService("input_method")).toggleSoftInput(0, 2);
          return;
        }
        paramView.setHint(paramView.getTag().toString());
        Object localObject = paramView.getContext();
        HotelDealBookingActivity localHotelDealBookingActivity = HotelDealBookingActivity.this;
        ((InputMethodManager)((Context)localObject).getSystemService("input_method")).hideSoftInputFromWindow(paramView.getWindowToken(), 0);
      }
    });
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
    {
      this.request = null;
      findViewById(R.id.sub_btn).setEnabled(false);
      findViewById(R.id.add_btn).setEnabled(false);
      findViewById(R.id.submit).setEnabled(false);
      Toast.makeText(this, "抱歉！已满房！", 1).show();
    }
    do
      return;
    while (paramMApiRequest != this.submitPostRequest);
    this.submitPostRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
    {
      this.request = null;
      this.result = ((DPObject)paramMApiResponse.result());
      this.minCoupon = this.result.getInt("MinCoupon");
      this.maxCoupon = this.result.getInt("MaxCoupon");
      if (this.maxCoupon < this.minCoupon)
        this.maxCoupon = this.minCoupon;
      this.minRoom = this.result.getInt("MinRoom");
      this.maxRoom = this.result.getInt("MaxRoom");
      paramMApiRequest = (EditText)findViewById(R.id.num);
      if (this.minRoom > 0)
        paramMApiRequest.setText(this.minRoom + "");
      paramMApiRequest = this.result.getString("DealTitle");
      if (!TextUtils.isEmpty(paramMApiRequest))
        ((TextView)findViewById(R.id.dealTitle)).setText(paramMApiRequest);
      this.checkinTime = this.result.getTime("DefaultCheckin");
      ((TextView)findViewById(R.id.checkin)).setText("入住" + SDF.format(Long.valueOf(this.checkinTime)) + "－");
      this.checkoutTime = this.result.getTime("DefaultCheckout");
      ((TextView)findViewById(R.id.checkout)).setText("退房 " + SDF.format(Long.valueOf(this.checkoutTime)));
      this.dayNum = (int)((this.checkoutTime - this.checkinTime) / 86400000L);
      ((TextView)findViewById(R.id.day_num)).setText(this.dayNum + "晚");
      if (this.dayNum <= 0L)
      {
        findViewById(R.id.sub_btn).setEnabled(false);
        findViewById(R.id.add_btn).setEnabled(false);
        findViewById(R.id.submit).setEnabled(false);
        Toast.makeText(this, "抱歉！已满房！", 1).show();
      }
      this.result.getInt("CancelDaysLimit");
      long l1 = this.result.getTime("CancelTime");
      paramMApiRequest = Calendar.getInstance();
      paramMApiResponse = Calendar.getInstance();
      paramMApiResponse.setTimeInMillis(this.checkinTime);
      if ((paramMApiRequest.get(5) == paramMApiResponse.get(5)) && (paramMApiRequest.get(2) == paramMApiResponse.get(2)))
        if (paramMApiRequest.getTimeInMillis() < l1)
        {
          paramMApiRequest = this.result.getString("TipsCanCancel");
          if (!TextUtils.isEmpty(paramMApiRequest))
          {
            paramMApiRequest = paramMApiRequest.replace("%s", SDF.format(Long.valueOf(this.checkinTime)));
            ((TextView)findViewById(R.id.tip1)).setText(paramMApiRequest);
          }
          l1 = this.dayNum;
          long l2 = this.minRoom;
          ((TextView)findViewById(R.id.tip2)).setText("本次预订需要使用" + l1 * l2 + "张团购券");
        }
    }
    while (true)
    {
      dismissDialog();
      return;
      paramMApiRequest = this.result.getString("TipsCannotCancel");
      if (TextUtils.isEmpty(paramMApiRequest))
        break;
      paramMApiRequest = paramMApiRequest.replace("%s", SDF.format(Long.valueOf(this.checkinTime)));
      ((TextView)findViewById(R.id.tip1)).setText(paramMApiRequest);
      break;
      paramMApiRequest = this.result.getString("TipsCanCancel");
      if (TextUtils.isEmpty(paramMApiRequest))
        break;
      paramMApiRequest = paramMApiRequest.replace("%s", SDF.format(Long.valueOf(this.checkinTime)));
      ((TextView)findViewById(R.id.tip1)).setText(paramMApiRequest);
      break;
      if (paramMApiRequest != this.submitPostRequest)
        continue;
      this.submitPostRequest = null;
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(((DPObject)paramMApiResponse.result()).getString("Content"))));
      DPApplication.instance().statisticsEvent("tuan5", "hotel_tuan5_reserve_submit", "", 0);
      finish();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.tuan.activity.HotelDealBookingActivity
 * JD-Core Version:    0.6.0
 */