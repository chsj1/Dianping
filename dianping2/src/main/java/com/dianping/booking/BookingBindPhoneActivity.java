package com.dianping.booking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.fastloginview.CountryCodeView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import org.json.JSONException;
import org.json.JSONObject;

public class BookingBindPhoneActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String BINDING_HINT = "客官，新号码绑定中...";
  private static final String BIND_PHONE_FAIL = "出问题了，绑定失败，稍后再试试吧";
  private static final String COUNTRY_CODE_KEY = "account:countryCode";
  private static final int COUNT_DOWN_NUM = 60;
  private static final String GET_VALIDATION_CODE_FAIL = "获取验证码失败，请稍后再试";
  private static final int MSG_COUNT_DOWN = 100;
  private static final String TIP_FROM_BOOKING_INFO = "不是常用手机号，需要验证一下哦~";
  private static final String TIP_FROM_EMPTY_LIST = "通过订座手机号查询订单";
  private static final String TIP_FROM_MY_BOOKING_LIST = "需要修改订座手机号？请验证一下哦~";
  private static final String validationCountDownLabel = "重新发送";
  private static final String validationLabel = "获取验证码";
  private MApiRequest bindRequest;
  private CountryCodeView countryCodeView;
  int fromType;
  private TextView getValidationCodeButton;
  private MApiRequest getValidationCodeRequest;
  private Handler handler = new CountDownHandler(null);
  private ImageButton mEditClear;
  private EditText phoneEditText;
  private Button submitBindButton;
  private TextView titleTip;
  private EditText validationCodeEditText;

  private boolean checkInput()
  {
    ForegroundColorSpan localForegroundColorSpan = new ForegroundColorSpan(-16777216);
    SpannableStringBuilder localSpannableStringBuilder;
    if ((this.phoneEditText.getText() == null) || (TextUtils.isEmpty(this.phoneEditText.getText().toString().trim())))
    {
      this.phoneEditText.requestFocus();
      localSpannableStringBuilder = new SpannableStringBuilder("手机号码不能为空");
      localSpannableStringBuilder.setSpan(localForegroundColorSpan, 0, "手机号码不能为空".length(), 0);
      this.phoneEditText.setError(localSpannableStringBuilder);
      return false;
    }
    if ((this.validationCodeEditText.getText() == null) || (TextUtils.isEmpty(this.validationCodeEditText.getText().toString().trim())))
    {
      this.validationCodeEditText.requestFocus();
      localSpannableStringBuilder = new SpannableStringBuilder("验证码不能为空");
      localSpannableStringBuilder.setSpan(localForegroundColorSpan, 0, "验证码不能为空".length(), 0);
      this.validationCodeEditText.setError(localSpannableStringBuilder);
      return false;
    }
    return true;
  }

  private String getCountryCode()
  {
    String str = getSharedPreferences("jsbridge_storage", 0).getString("account:countryCode", "");
    if (!TextUtils.isEmpty(str))
      try
      {
        str = new JSONObject(str).optString("code");
        boolean bool = TextUtils.isEmpty(str);
        if (!bool)
          return str;
      }
      catch (JSONException localJSONException)
      {
      }
    return "86";
  }

  private void getValidationCode()
  {
    if (this.getValidationCodeRequest != null)
      mapiService().abort(this.getValidationCodeRequest, this, true);
    String str = this.phoneEditText.getEditableText().toString();
    this.getValidationCodeRequest = BasicMApiRequest.mapiPost("http://rs.api.dianping.com/getphonevc.yy", new String[] { "phone", getCountryCode() + "_" + str });
    mapiService().exec(this.getValidationCodeRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        BookingBindPhoneActivity.this.showAlertDialog(null, "获取验证码失败，请稍后再试");
        BookingBindPhoneActivity.this.handler.removeMessages(100);
        BookingBindPhoneActivity.this.updateGetValidationCodeButton(-1);
        BookingBindPhoneActivity.access$902(BookingBindPhoneActivity.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest.getBoolean("status"))
            BookingBindPhoneActivity.this.showShortToast("获取成功，请等待验证码短信");
        }
        while (true)
        {
          BookingBindPhoneActivity.access$902(BookingBindPhoneActivity.this, null);
          return;
          paramMApiResponse = paramMApiRequest.getString("reason");
          paramMApiRequest = paramMApiResponse;
          if (TextUtils.isEmpty(paramMApiResponse))
            paramMApiRequest = "获取验证码失败，请稍后再试";
          BookingBindPhoneActivity.this.showAlertDialog(null, paramMApiRequest);
          BookingBindPhoneActivity.this.handler.removeMessages(100);
          BookingBindPhoneActivity.this.updateGetValidationCodeButton(-1);
          continue;
          BookingBindPhoneActivity.this.showAlertDialog(null, "获取验证码失败，请稍后再试");
          BookingBindPhoneActivity.this.handler.removeMessages(100);
          BookingBindPhoneActivity.this.updateGetValidationCodeButton(-1);
        }
      }
    });
  }

  private void hideSoftKeyBoard()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
    if (this.phoneEditText != null)
      localInputMethodManager.hideSoftInputFromWindow(this.phoneEditText.getWindowToken(), 0);
    if (this.validationCodeEditText != null)
      localInputMethodManager.hideSoftInputFromInputMethod(this.validationCodeEditText.getWindowToken(), 0);
  }

  private void submitBind()
  {
    if (this.bindRequest != null)
      mapiService().abort(this.bindRequest, this, true);
    String str2 = this.phoneEditText.getEditableText().toString();
    String str3 = this.validationCodeEditText.getText().toString();
    if (accountService() == null);
    for (String str1 = ""; ; str1 = accountService().token())
    {
      this.bindRequest = BasicMApiRequest.mapiPost("http://rs.api.dianping.com/phonebind.yy", new String[] { "phone", getCountryCode() + "_" + str2, "code", str3, "clientuuid", Environment.uuid(), "token", str1 });
      mapiService().exec(this.bindRequest, this);
      return;
    }
  }

  private void updateGetValidationCodeButton(int paramInt)
  {
    if (paramInt <= 0)
    {
      this.getValidationCodeButton.setEnabled(true);
      this.getValidationCodeButton.setText("获取验证码");
      return;
    }
    this.getValidationCodeButton.setEnabled(false);
    this.getValidationCodeButton.setText("重新发送(" + paramInt + ")");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().setBackgroundDrawable(null);
    super.setContentView(R.layout.booking_bind_phone);
    this.countryCodeView = ((CountryCodeView)findViewById(R.id.country_code_view));
    this.phoneEditText = ((EditText)findViewById(R.id.booking_phone));
    this.validationCodeEditText = ((EditText)findViewById(R.id.validation_code));
    this.getValidationCodeButton = ((TextView)findViewById(R.id.get_validation_btn));
    this.submitBindButton = ((Button)findViewById(R.id.submit_bind_btn));
    this.titleTip = ((TextView)findViewById(R.id.title_tip));
    this.mEditClear = ((ImageButton)findViewById(R.id.edit_clear));
    paramBundle = getIntent().getStringExtra("phone");
    boolean bool = getIntent().getBooleanExtra("editable", false);
    this.fromType = getIntent().getIntExtra("validation", 0);
    if (this.fromType == 0)
    {
      this.titleTip.setText("不是常用手机号，需要验证一下哦~");
      super.setTitle("手机验证");
    }
    while (true)
    {
      if ((paramBundle != null) && (paramBundle.trim().length() != 0))
      {
        int i = getResources().getColor(R.color.light_gray);
        this.phoneEditText.setTextColor(i);
        this.phoneEditText.setText(paramBundle);
        this.countryCodeView.setTextColor(i);
      }
      this.phoneEditText.setEnabled(bool);
      this.countryCodeView.setCountryCode(getCountryCode());
      this.countryCodeView.setClickable(bool);
      this.getValidationCodeButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if ((BookingBindPhoneActivity.this.phoneEditText.getText() == null) || (TextUtils.isEmpty(BookingBindPhoneActivity.this.phoneEditText.getText().toString().trim())))
          {
            paramView = new ForegroundColorSpan(-16777216);
            BookingBindPhoneActivity.this.phoneEditText.requestFocus();
            SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder("手机号码不能为空");
            localSpannableStringBuilder.setSpan(paramView, 0, "手机号码不能为空".length(), 0);
            BookingBindPhoneActivity.this.phoneEditText.setError(localSpannableStringBuilder);
            return;
          }
          BookingBindPhoneActivity.this.getValidationCode();
          BookingBindPhoneActivity.this.handler.removeMessages(100);
          paramView = Message.obtain();
          paramView.what = 100;
          paramView.arg1 = 60;
          BookingBindPhoneActivity.this.handler.sendMessageDelayed(paramView, 0L);
        }
      });
      this.submitBindButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          BookingBindPhoneActivity.this.hideSoftKeyBoard();
          if (!BookingBindPhoneActivity.this.checkInput())
            return;
          BookingBindPhoneActivity.this.submitBind();
          BookingBindPhoneActivity.this.showProgressDialog("客官，新号码绑定中...");
        }
      });
      this.phoneEditText.addTextChangedListener(new TextWatcher()
      {
        public void afterTextChanged(Editable paramEditable)
        {
        }

        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
        {
        }

        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
        {
          if (TextUtils.isEmpty(paramCharSequence))
          {
            BookingBindPhoneActivity.this.mEditClear.setVisibility(8);
            return;
          }
          BookingBindPhoneActivity.this.mEditClear.setVisibility(0);
        }
      });
      this.mEditClear.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          BookingBindPhoneActivity.this.phoneEditText.setText("");
        }
      });
      return;
      if (this.fromType == 1)
      {
        this.titleTip.setText("需要修改订座手机号？请验证一下哦~");
        super.setTitle("修改订座手机");
        this.phoneEditText.setHint("请输入新手机号");
        continue;
      }
      if (this.fromType != 2)
        continue;
      this.titleTip.setText("通过订座手机号查询订单");
      super.setTitle("查询订单");
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.getValidationCodeRequest != null)
    {
      mapiService().abort(this.getValidationCodeRequest, this, true);
      this.getValidationCodeRequest = null;
    }
    if (this.bindRequest != null)
    {
      mapiService().abort(this.bindRequest, this, true);
      this.bindRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.bindRequest)
    {
      showAlertDialog(null, "出问题了，绑定失败，稍后再试试吧");
      if (this.fromType == 2)
        statisticsEvent("mybooking6", "mybooking6_noorder_login_phonebind", "fail", 0);
      this.bindRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.bindRequest)
    {
      if ((paramMApiResponse == null) || (!(paramMApiResponse.result() instanceof DPObject)))
        break label179;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (!paramMApiRequest.getBoolean("status"))
        break label153;
      if (this.fromType != 0)
        break label104;
      statisticsEvent("booking5", "booking5_phonebind_succeed", "", 0);
      showShortToast("绑定成功");
      sendBroadcast(new Intent("com.dianping.booking.BOOKING_BIND_PHONE"));
      setResult(-1);
      finish();
    }
    while (true)
    {
      this.bindRequest = null;
      return;
      label104: if (this.fromType == 1)
      {
        statisticsEvent("mybooking5", "mybooking5_phonebind_succeed", "", 0);
        break;
      }
      if (this.fromType != 2)
        break;
      statisticsEvent("mybooking6", "mybooking6_noorder_login_phonebind", "succeed", 0);
      break;
      label153: paramMApiResponse = paramMApiRequest.getString("reason");
      paramMApiRequest = paramMApiResponse;
      if (paramMApiResponse == null)
        paramMApiRequest = "出问题了，绑定失败，稍后再试试吧";
      showAlertDialog(null, paramMApiRequest);
      continue;
      label179: showAlertDialog(null, "出问题了，绑定失败，稍后再试试吧");
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    this.countryCodeView.setText(paramBundle.getString("countrycodeview"));
    this.phoneEditText.setText(paramBundle.getString("phoneedittext"));
    this.validationCodeEditText.setText(paramBundle.getString("validationcodeedittext"));
    super.onRestoreInstanceState(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("countrycodeview", this.countryCodeView.getText().toString());
    paramBundle.putString("phoneedittext", this.phoneEditText.getText().toString());
    paramBundle.putString("validationcodeedittext", this.validationCodeEditText.getText().toString());
    super.onSaveInstanceState(paramBundle);
  }

  private class CountDownHandler extends Handler
  {
    private CountDownHandler()
    {
    }

    public void handleMessage(Message paramMessage)
    {
      if ((paramMessage.what == 100) && (paramMessage.arg1 >= 0))
      {
        BookingBindPhoneActivity.this.updateGetValidationCodeButton(paramMessage.arg1);
        Message localMessage = Message.obtain();
        localMessage.what = 100;
        paramMessage.arg1 -= 1;
        sendMessageDelayed(localMessage, 1000L);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingBindPhoneActivity
 * JD-Core Version:    0.6.0
 */