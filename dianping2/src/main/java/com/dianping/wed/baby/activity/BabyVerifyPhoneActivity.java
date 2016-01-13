package com.dianping.wed.baby.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.util.WedBookingUtil;
import com.dianping.widget.view.NovaButton;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class BabyVerifyPhoneActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  MApiRequest bookingRequest;
  NovaButton codeButton;
  MApiRequest codeRequest;
  CountDownTimer codeTimer;
  EditText editCode;
  EditText editPhone;
  String phoneNum;
  String shopId;
  NovaButton verifyButton;

  void createTimer()
  {
    releaseTimer();
    this.codeTimer = new CountDownTimer(60000L, 1000L)
    {
      public void onFinish()
      {
        BabyVerifyPhoneActivity.this.resetButton();
        BabyVerifyPhoneActivity.this.releaseTimer();
      }

      public void onTick(long paramLong)
      {
        BabyVerifyPhoneActivity.this.codeButton.setText("重新发送(" + paramLong / 1000L + ")");
      }
    };
    this.codeTimer.start();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.wed_button_verify_code)
      if (this.codeRequest == null)
      {
        this.codeButton.setEnabled(false);
        paramView = Uri.parse("http://m.api.dianping.com/wedding/commonverifycode.bin").buildUpon();
        paramView.appendQueryParameter("phonenum", this.phoneNum);
        this.codeRequest = mapiGet(this, paramView.toString(), CacheType.DISABLED);
        mapiService().exec(this.codeRequest, this);
        createTimer();
      }
    do
      return;
    while ((paramView.getId() != R.id.wed_button_verify) || (this.bookingRequest != null));
    paramView = accountService().token();
    HashMap localHashMap = new HashMap();
    localHashMap.put("shopid", this.shopId);
    localHashMap.put("phoneNum", this.phoneNum);
    localHashMap.put("token", paramView);
    localHashMap.put("verifycode", this.editCode.getText().toString());
    this.bookingRequest = mapiPost(this, WedBookingUtil.getBookingUrl("http://m.api.dianping.com/wedding/commonbooking.bin", localHashMap), new String[0]);
    mapiService().exec(this.bookingRequest, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("手机验证");
    setContentView(R.layout.baby_verifyphone_activity);
    if (paramBundle != null)
      this.phoneNum = paramBundle.getString("phonenum");
    for (this.shopId = paramBundle.getString("shopid"); ; this.shopId = getStringParam("shopid"))
    {
      this.codeButton = ((NovaButton)findViewById(R.id.wed_button_verify_code));
      this.verifyButton = ((NovaButton)findViewById(R.id.wed_button_verify));
      this.editPhone = ((EditText)findViewById(R.id.wed_verify_edit_phone));
      this.editCode = ((EditText)findViewById(R.id.wed_verify_edit_code));
      this.editCode.addTextChangedListener(new TextWatcher()
      {
        public void afterTextChanged(Editable paramEditable)
        {
        }

        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
        {
        }

        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
        {
        }
      });
      this.editPhone.setText(this.phoneNum);
      this.editPhone.setEnabled(false);
      this.codeButton.setOnClickListener(this);
      this.verifyButton.setOnClickListener(this);
      return;
      this.phoneNum = getStringParam("phonenum");
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    releaseTimer();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.codeRequest)
    {
      this.codeRequest = null;
      resetButton();
      releaseTimer();
    }
    do
      return;
    while (paramMApiRequest != this.bookingRequest);
    this.bookingRequest = null;
    if ((paramMApiResponse != null) && (paramMApiResponse.message() != null) && (!android.text.TextUtils.isEmpty(paramMApiResponse.message().toString())))
    {
      paramMApiRequest = Toast.makeText(this, paramMApiResponse.message().toString(), 1);
      paramMApiRequest.setGravity(17, 0, 0);
      paramMApiRequest.show();
      return;
    }
    paramMApiRequest = Toast.makeText(this, "网络不给力啊，请稍后再试试", 1);
    paramMApiRequest.setGravity(17, 0, 0);
    paramMApiRequest.show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.codeRequest)
    {
      this.codeRequest = null;
      Toast.makeText(this, "验证码已发送，请查看手机", 1).show();
    }
    while (true)
    {
      return;
      if (paramMApiRequest != this.bookingRequest)
        continue;
      this.bookingRequest = null;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest == null)
        break;
      switch (paramMApiRequest.getInt("Flag"))
      {
      default:
        return;
      case 200:
      }
      try
      {
        paramMApiRequest = new JSONObject(paramMApiRequest.getString("Data")).optString("redirectLink");
        if ((paramMApiRequest == null) || ("null".equals(paramMApiRequest)) || (com.dianping.util.TextUtils.isEmpty(paramMApiRequest)))
          continue;
        paramMApiResponse = Uri.parse("dianping://web").buildUpon();
        paramMApiResponse.appendQueryParameter("url", paramMApiRequest);
        onBackPressed();
        startActivity(new Intent("android.intent.action.VIEW", paramMApiResponse.build()));
        return;
      }
      catch (Exception paramMApiRequest)
      {
        paramMApiRequest.printStackTrace();
      }
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("phonenum", this.phoneNum);
    paramBundle.putString("shopid", this.shopId);
  }

  void releaseTimer()
  {
    if (this.codeTimer != null)
      this.codeTimer.cancel();
    this.codeTimer = null;
  }

  void resetButton()
  {
    this.codeButton.setEnabled(true);
    this.codeButton.setText("获取验证码");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.BabyVerifyPhoneActivity
 * JD-Core Version:    0.6.0
 */