package com.dianping.takeaway.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.CustomEditText;
import com.dianping.base.widget.fastloginview.FastLoginView;
import com.dianping.base.widget.fastloginview.FastLoginView.FastLoginLoginListener;
import com.dianping.base.widget.fastloginview.FastLoginView.FastLoginReplaceRequestListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.takeaway.util.TakeawayPreferencesManager;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TakeawayPhoneVerifyActivity extends NovaActivity
  implements FastLoginView.FastLoginLoginListener
{
  private final int RESULT_CODE_DELIVERYDETAIL_PHONE_VERIFY_COMMON = 71;
  private final int RESULT_CODE_DELIVERYDETAIL_PHONE_VERIFY_ELEME = 72;
  private boolean isEleme = false;
  private Context mContext;
  protected String mobileNo;
  private String shopId;
  private Button submitBtn;
  protected MApiRequest verifyElemePhoneRequest;
  protected MApiRequest verifyPhoneRequest;
  private TextView verifyTip;
  protected FastLoginView verifyView;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.takeaway_phone_verify);
    this.mContext = this;
    this.mobileNo = super.getStringParam("phone");
    this.isEleme = "eleme".equals(super.getStringParam("type"));
    this.shopId = super.getStringParam("shopid");
    if (((this.mobileNo == null) || (this.mobileNo.trim().length() < 11)) && (!this.isEleme))
    {
      super.showToast("手机号有误");
      super.finish();
    }
    boolean bool = TextUtils.isEmpty(accountService().token());
    this.verifyTip = ((TextView)findViewById(R.id.verify_tip));
    this.verifyView = ((FastLoginView)findViewById(R.id.verify_view));
    this.submitBtn = ((Button)findViewById(R.id.submit_btn));
    this.submitBtn.setEnabled(false);
    this.verifyView.showCountryCode(Boolean.valueOf(false));
    this.verifyView.addPhoneEditTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        if ((TextUtils.isEmpty(paramEditable.toString())) || (TextUtils.isEmpty(TakeawayPhoneVerifyActivity.this.verifyView.getVerificationCodeEditText().mEdit.getText().toString().trim())))
        {
          TakeawayPhoneVerifyActivity.this.submitBtn.setEnabled(false);
          return;
        }
        TakeawayPhoneVerifyActivity.this.submitBtn.setEnabled(true);
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
    this.verifyView.getVerificationCodeEditText().mEdit.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        if ((TextUtils.isEmpty(paramEditable.toString())) || (TextUtils.isEmpty(TakeawayPhoneVerifyActivity.this.verifyView.getPhoneEditText())))
        {
          TakeawayPhoneVerifyActivity.this.submitBtn.setEnabled(false);
          return;
        }
        TakeawayPhoneVerifyActivity.this.submitBtn.setEnabled(true);
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
    if (this.isEleme)
    {
      this.verifyTip.setText("输入您在饿了么绑定的手机号");
      this.submitBtn.setText("查询");
      this.verifyView.setPhoneNum(this.mobileNo, null, true, false);
      this.verifyView.setReplaceVerficationCodeUrlListener(new FastLoginView.FastLoginReplaceRequestListener()
      {
        public MApiRequest replaceRequestListener(String paramString)
        {
          Uri.Builder localBuilder = Uri.parse("http://waimai.api.dianping.com/thirdpartysendverifycode.ta").buildUpon();
          localBuilder.appendQueryParameter("phone", paramString);
          localBuilder.appendQueryParameter("shopid", TakeawayPhoneVerifyActivity.this.shopId);
          return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
        }
      });
    }
    while (true)
    {
      this.submitBtn.setOnClickListener(new View.OnClickListener(bool)
      {
        public void onClick(View paramView)
        {
          ((InputMethodManager)TakeawayPhoneVerifyActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(TakeawayPhoneVerifyActivity.this.getWindow().getDecorView().getWindowToken(), 2);
          if (TakeawayPhoneVerifyActivity.this.isEleme)
          {
            TakeawayPhoneVerifyActivity.this.mobileNo = TakeawayPhoneVerifyActivity.this.verifyView.getPhoneEditText();
            paramView = TakeawayPhoneVerifyActivity.this.verifyView.getVerificationCodeEditText().mEdit.getText().toString().trim();
            if (TextUtils.isEmpty(paramView))
            {
              TakeawayPhoneVerifyActivity.this.showToast("验证码不能为空");
              return;
            }
            TakeawayPhoneVerifyActivity.this.verifyElemeTask(TakeawayPhoneVerifyActivity.this.mobileNo, paramView);
            return;
          }
          if (this.val$needLogin)
          {
            TakeawayPhoneVerifyActivity.this.verifyView.login(TakeawayPhoneVerifyActivity.this);
            return;
          }
          paramView = TakeawayPhoneVerifyActivity.this.verifyView.getVerificationCodeEditText().mEdit.getText().toString().trim();
          if (TextUtils.isEmpty(paramView))
          {
            TakeawayPhoneVerifyActivity.this.showToast("验证码不能为空");
            return;
          }
          TakeawayPhoneVerifyActivity.this.verifyPhoneTask(TakeawayPhoneVerifyActivity.this.mobileNo, paramView);
        }
      });
      return;
      this.verifyTip.setText("验证手机会提升您的订单确认速度");
      this.submitBtn.setText("提交");
      if (!bool)
      {
        paramBundle = Uri.parse("http://waimai.api.dianping.com/sendverifycode.ta").buildUpon();
        paramBundle.appendQueryParameter("mobileno", this.mobileNo);
        this.verifyView.setReplaceVerficationCodeUrlListener(new FastLoginView.FastLoginReplaceRequestListener(paramBundle)
        {
          public MApiRequest replaceRequestListener(String paramString)
          {
            return BasicMApiRequest.mapiGet(this.val$url.toString(), CacheType.DISABLED);
          }
        });
      }
      this.verifyView.setPhoneNum(this.mobileNo, null, false);
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.verifyPhoneRequest != null)
    {
      super.mapiService().abort(this.verifyPhoneRequest, null, true);
      this.verifyPhoneRequest = null;
    }
    do
      return;
    while (this.verifyElemePhoneRequest == null);
    super.mapiService().abort(this.verifyElemePhoneRequest, null, true);
    this.verifyElemePhoneRequest = null;
  }

  public void onLoginFailed(int paramInt, SimpleMsg paramSimpleMsg)
  {
    String str2 = "验证失败，请重试";
    String str1 = str2;
    if (paramSimpleMsg != null)
    {
      str1 = str2;
      if (!TextUtils.isEmpty(paramSimpleMsg.content()))
        str1 = paramSimpleMsg.content();
    }
    super.showToast(str1);
    this.verifyView.clearVerificationCode();
  }

  public void onLoginSucceed()
  {
    super.showToast("验证成功");
    super.setResult(71);
    super.finish();
  }

  protected void verifyElemeTask(String paramString1, String paramString2)
  {
    if (this.verifyElemePhoneRequest != null)
      mapiService().abort(this.verifyElemePhoneRequest, null, true);
    super.showProgressDialog("验证中...");
    this.managedDialog.setCancelable(false);
    Uri.Builder localBuilder = Uri.parse("http://waimai.api.dianping.com/thirdpartyverifyphone.ta").buildUpon();
    localBuilder.appendQueryParameter("phone", paramString1);
    localBuilder.appendQueryParameter("verifycode", paramString2);
    localBuilder.appendQueryParameter("shopid", this.shopId);
    this.verifyElemePhoneRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.verifyElemePhoneRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayPhoneVerifyActivity.this.verifyElemePhoneRequest = null;
        TakeawayPhoneVerifyActivity.this.dismissDialog();
        String str = "验证失败，请重试";
        paramMApiRequest = str;
        if (paramMApiResponse != null)
        {
          paramMApiRequest = str;
          if (paramMApiResponse.message() != null)
          {
            paramMApiRequest = str;
            if (!TextUtils.isEmpty(paramMApiResponse.message().content()))
              paramMApiRequest = paramMApiResponse.message().content();
          }
        }
        TakeawayPhoneVerifyActivity.this.showToast(paramMApiRequest);
        TakeawayPhoneVerifyActivity.this.verifyView.clearVerificationCode();
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayPhoneVerifyActivity.this.verifyElemePhoneRequest = null;
        TakeawayPhoneVerifyActivity.this.dismissDialog();
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        if (paramMApiRequest.getInt("Code") == 1)
        {
          TakeawayPhoneVerifyActivity.this.showToast("验证成功");
          paramMApiResponse = TakeawayPreferencesManager.getTakeawayDeliveryPreferences(TakeawayPhoneVerifyActivity.this.mContext).edit();
          paramMApiResponse.putString("eleme_phone", TakeawayPhoneVerifyActivity.this.mobileNo);
          paramMApiResponse.putString("eleme_token", paramMApiRequest.getString("Content"));
          paramMApiResponse.commit();
          TakeawayPhoneVerifyActivity.this.setResult(72);
          TakeawayPhoneVerifyActivity.this.finish();
          return;
        }
        TakeawayPhoneVerifyActivity.this.showToast(paramMApiRequest.getString("Content"));
        TakeawayPhoneVerifyActivity.this.verifyView.clearVerificationCode();
      }
    });
  }

  protected void verifyPhoneTask(String paramString1, String paramString2)
  {
    if (this.verifyPhoneRequest != null)
      mapiService().abort(this.verifyPhoneRequest, null, true);
    super.showProgressDialog("验证中...");
    this.managedDialog.setCancelable(false);
    Uri.Builder localBuilder = Uri.parse("http://waimai.api.dianping.com/verifyphone.ta").buildUpon();
    localBuilder.appendQueryParameter("phone", paramString1);
    localBuilder.appendQueryParameter("verifycode", paramString2);
    this.verifyPhoneRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.verifyPhoneRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayPhoneVerifyActivity.this.verifyPhoneRequest = null;
        TakeawayPhoneVerifyActivity.this.dismissDialog();
        String str = "验证失败，请重试";
        paramMApiRequest = str;
        if (paramMApiResponse != null)
        {
          paramMApiRequest = str;
          if (paramMApiResponse.message() != null)
          {
            paramMApiRequest = str;
            if (!TextUtils.isEmpty(paramMApiResponse.message().content()))
              paramMApiRequest = paramMApiResponse.message().content();
          }
        }
        TakeawayPhoneVerifyActivity.this.showToast(paramMApiRequest);
        TakeawayPhoneVerifyActivity.this.verifyView.clearVerificationCode();
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        TakeawayPhoneVerifyActivity.this.verifyPhoneRequest = null;
        TakeawayPhoneVerifyActivity.this.dismissDialog();
        Object localObject = "";
        paramMApiRequest = (MApiRequest)localObject;
        if (paramMApiResponse != null)
        {
          paramMApiRequest = (MApiRequest)localObject;
          if ((paramMApiResponse.result() instanceof DPObject))
            paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("Content");
        }
        localObject = TakeawayPhoneVerifyActivity.this;
        paramMApiResponse = paramMApiRequest;
        if (TextUtils.isEmpty(paramMApiRequest))
          paramMApiResponse = "验证成功";
        ((TakeawayPhoneVerifyActivity)localObject).showToast(paramMApiResponse);
        TakeawayPhoneVerifyActivity.this.setResult(71);
        TakeawayPhoneVerifyActivity.this.finish();
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayPhoneVerifyActivity
 * JD-Core Version:    0.6.0
 */