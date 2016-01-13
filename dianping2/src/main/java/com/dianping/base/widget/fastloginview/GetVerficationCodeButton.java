package com.dianping.base.widget.fastloginview;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.SMSHelper;
import com.dianping.base.util.SMSHelper.verCodeComeListener;
import com.dianping.base.widget.CustomEditText;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.widget.view.NovaButton;

public class GetVerficationCodeButton extends NovaButton
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>, SMSHelper.verCodeComeListener
{
  private static final int ERROR_TYPE_PHONE_EXIST = 4;
  private static final int LAST_TIME = 60;
  public static final int TYPE_PHONE_LOGIN = 1;
  public static final int TYPE_SIGNUP = 2;
  public String code;
  public boolean isVerCodeButtonClicked;
  private NovaActivity mActivity;
  private MApiRequest mGetVerificationCode;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (GetVerficationCodeButton.this.mLastTime == 0)
      {
        GetVerficationCodeButton.this.resetButton();
        return;
      }
      GetVerficationCodeButton.this.mHandler.sendEmptyMessageDelayed(0, 1000L);
      GetVerficationCodeButton.access$010(GetVerficationCodeButton.this);
      GetVerficationCodeButton.this.setText("重新发送(" + GetVerficationCodeButton.this.mLastTime + ")");
    }
  };
  private CustomEditText mImageVerificationCodeEditText;
  private int mLastTime = 60;
  private ReplaceRequestListener mListener;
  private boolean mNeedCountryCode = true;
  private ShowVerificationCodeButton mShowVerificationCodeButton;
  private SMSHelper mSmsHelper = null;
  private TextView mTextView;
  private int mType = 0;
  private verCodeComeListener mVerCodeComeListener = null;
  private CustomEditText mVerificationCodeEditText;
  public String phoneNum;
  public String ticket;

  public GetVerficationCodeButton(NovaActivity paramNovaActivity, TextView paramTextView, int paramInt)
  {
    super(paramNovaActivity);
    this.mActivity = paramNovaActivity;
    this.mTextView = paramTextView;
    this.mType = paramInt;
    paramNovaActivity = new LinearLayout.LayoutParams(ViewUtils.dip2px(this.mActivity, 100.0F), -2);
    paramNovaActivity.rightMargin = ViewUtils.dip2px(this.mActivity, 17.0F);
    paramNovaActivity.leftMargin = ViewUtils.dip2px(this.mActivity, 5.0F);
    setLayoutParams(paramNovaActivity);
    setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_medium));
    resetButton();
    setGAString("verification_code");
    setOnClickListener(this);
  }

  public void needCountryCode(boolean paramBoolean)
  {
    this.mNeedCountryCode = paramBoolean;
  }

  public void onClick(View paramView)
  {
    if (android.text.TextUtils.isEmpty(this.mTextView.getText().toString()))
    {
      this.mTextView.requestFocus();
      this.mActivity.showToast("请输入正确的手机号");
    }
    while (true)
    {
      return;
      try
      {
        if (this.mSmsHelper == null)
        {
          this.mSmsHelper = new SMSHelper(this.mActivity);
          this.mSmsHelper.setOnVerCodeComeListener(this);
          this.mSmsHelper.registerSMSHelper();
        }
        if (this.mLastTime != 60)
          continue;
        sendGetVerficationCode();
        return;
      }
      catch (Exception paramView)
      {
        while (true)
          Log.e("短信权限未开启");
      }
    }
  }

  protected void onDetachedFromWindow()
  {
    if ((this.mGetVerificationCode != null) && (this.mActivity != null))
      this.mActivity.mapiService().abort(this.mGetVerificationCode, this, true);
    if (this.mHandler.hasMessages(0))
      this.mHandler.removeMessages(0);
    try
    {
      if (this.mSmsHelper != null)
      {
        this.mSmsHelper.unregisterSMSHelper();
        this.mSmsHelper = null;
      }
      super.onDetachedFromWindow();
      return;
    }
    catch (Exception localException)
    {
      while (true)
        Log.e("短信权限未开启");
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mGetVerificationCode == paramMApiRequest)
    {
      this.mGetVerificationCode = null;
      if (this.mActivity != null);
    }
    else
    {
      return;
    }
    if (paramMApiResponse.message() != null);
    for (paramMApiRequest = paramMApiResponse.message(); ; paramMApiRequest = new SimpleMsg("错误", "网络错误,请重试", 0, 0))
    {
      if ((paramMApiRequest.flag() == 1) && (this.mShowVerificationCodeButton != null))
      {
        this.mShowVerificationCodeButton.sendVerificationCodeRequest(0);
        if (this.mVerificationCodeEditText != null)
        {
          this.mVerificationCodeEditText.setVisibility(8);
          if (this.mImageVerificationCodeEditText != null)
            this.mImageVerificationCodeEditText.setVisibility(0);
        }
      }
      if (4 == paramMApiRequest.statusCode())
        this.phoneNum = com.dianping.util.TextUtils.getRealDialPhoneNum(this.mTextView.getText().toString().trim());
      this.mActivity.showToast(paramMApiRequest.content());
      if (paramMApiRequest.flag() == 1)
        break;
      resetButton();
      return;
    }
    this.mLastTime = 60;
    if (this.mHandler.hasMessages(0))
      this.mHandler.removeMessages(0);
    setText("发送验证码");
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mGetVerificationCode == paramMApiRequest)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        this.ticket = paramMApiRequest.getString("Ticket");
        paramMApiRequest = paramMApiRequest.getString("Msg");
        if (this.mVerificationCodeEditText != null)
        {
          this.mVerificationCodeEditText.setVisibility(0);
          if (this.mImageVerificationCodeEditText != null)
            this.mImageVerificationCodeEditText.setVisibility(8);
        }
        if ((!android.text.TextUtils.isEmpty(paramMApiRequest)) && (this.mActivity != null))
          this.mActivity.showToast(paramMApiRequest);
      }
      this.mGetVerificationCode = null;
      this.isVerCodeButtonClicked = true;
    }
  }

  public void onVerCodeCome(String paramString)
  {
    if (this.mVerCodeComeListener != null)
      this.mVerCodeComeListener.onVerCodeCome(paramString);
  }

  void resetButton()
  {
    setEnabled(true);
    setTextColor(getResources().getColor(R.color.deep_gray));
    setBackgroundResource(R.drawable.getver_normal_bg);
    this.mLastTime = 60;
    if (this.mHandler.hasMessages(0))
      this.mHandler.removeMessages(0);
    setText("获取验证码");
  }

  public void sendGetVerficationCode()
  {
    if (this.mGetVerificationCode != null)
      this.mActivity.mapiService().abort(this.mGetVerificationCode, this, true);
    String str1 = "86";
    if (this.mNeedCountryCode)
      str1 = this.mActivity.getSharedPreferences(this.mActivity.getPackageName(), 0).getString("last_country_code", "86");
    String str3 = com.dianping.util.TextUtils.getRealDialPhoneNum(this.mTextView.getText().toString().trim());
    String str4 = DeviceUtils.cxInfo("sendverifycode");
    Object localObject = DPApplication.instance().locationService().city();
    int i;
    String str2;
    if (localObject == null)
    {
      i = 0;
      localObject = null;
      str2 = null;
      if (this.mShowVerificationCodeButton != null)
        localObject = this.mShowVerificationCodeButton.ticket;
      if (this.mImageVerificationCodeEditText != null)
        str2 = this.mImageVerificationCodeEditText.mEdit.getText().toString();
      if (this.mListener == null)
        break label273;
      this.mGetVerificationCode = this.mListener.replaceRequestListener(str3);
    }
    while (true)
    {
      this.mActivity.mapiService().exec(this.mGetVerificationCode, this);
      this.mHandler.sendEmptyMessageDelayed(0, 1000L);
      setText("重新发送(" + this.mLastTime + ")");
      setTextColor(getResources().getColor(R.color.white));
      setBackgroundResource(R.drawable.getver_unable_bg);
      setEnabled(false);
      return;
      i = ((DPObject)localObject).getInt("ID");
      break;
      label273: if ((localObject != null) && (str2 != null))
      {
        this.mGetVerificationCode = BasicMApiRequest.mapiPost("http://m.api.dianping.com/mlogin/sendverifycode.api", new String[] { "phone", str3, "type", "" + this.mType, "countrycode", str1, "cx", str4, "locationcityid", "" + i, "ticket", localObject, "code", str2 });
        continue;
      }
      this.mGetVerificationCode = BasicMApiRequest.mapiPost("http://m.api.dianping.com/mlogin/sendverifycode.api", new String[] { "phone", str3, "type", "" + this.mType, "countrycode", str1, "cx", str4, "locationcityid", "" + i });
    }
  }

  public void setGetVerCodeType(int paramInt)
  {
    this.mType = paramInt;
  }

  public void setImageVerificationCodeEditText(CustomEditText paramCustomEditText)
  {
    this.mImageVerificationCodeEditText = paramCustomEditText;
  }

  public void setOnVerCodeComeListener(verCodeComeListener paramverCodeComeListener)
  {
    this.mVerCodeComeListener = paramverCodeComeListener;
  }

  public void setReplaceRequestListener(ReplaceRequestListener paramReplaceRequestListener)
  {
    this.mListener = paramReplaceRequestListener;
  }

  public void setShowVerificationCodeButton(ShowVerificationCodeButton paramShowVerificationCodeButton)
  {
    this.mShowVerificationCodeButton = paramShowVerificationCodeButton;
  }

  public void setVerificationCodeEditText(CustomEditText paramCustomEditText)
  {
    this.mVerificationCodeEditText = paramCustomEditText;
  }

  public static abstract interface ReplaceRequestListener
  {
    public abstract MApiRequest replaceRequestListener(String paramString);
  }

  public static abstract interface verCodeComeListener
  {
    public abstract void onVerCodeCome(String paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.fastloginview.GetVerficationCodeButton
 * JD-Core Version:    0.6.0
 */