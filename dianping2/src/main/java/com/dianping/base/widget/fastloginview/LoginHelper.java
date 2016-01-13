package com.dianping.base.widget.fastloginview;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.util.FavoriteHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiFormInputStream;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.DeviceUtils;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;

public class LoginHelper
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String SPKEY_LOGIN_ACCOUNT_NAME = "mainLoginAccountName";
  private static final String SPKEY_LOGIN_PHONE_NUM = "mainLoginPhoneNum";
  private static LoginHelper mLoginHelper;
  private boolean isPhoneNum;
  private LoginListener mListener;
  private MApiRequest mLoginRequest;
  private MApiRequest mSignUpRequest;
  private MApiRequest mUserRequest;
  private String userName;

  public static LoginHelper instance()
  {
    if (mLoginHelper == null)
      mLoginHelper = new LoginHelper();
    return mLoginHelper;
  }

  private MApiService mapiService()
  {
    return DPApplication.instance().mapiService();
  }

  private void syncCard(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(paramString);
    localArrayList.add("uuid");
    localArrayList.add(Environment.uuid());
    mapiService().exec(BasicMApiRequest.mapiPost("http://mc.api.dianping.com/syncard.mc", (String[])localArrayList.toArray(new String[localArrayList.size()])), this);
  }

  public void Signup(String paramString1, String paramString2, String paramString3, LoginListener paramLoginListener)
  {
    if (this.mSignUpRequest != null)
      mapiService().abort(this.mSignUpRequest, this, true);
    this.mListener = paramLoginListener;
    paramLoginListener = new ArrayList();
    paramLoginListener.add(new BasicNameValuePair("countrycode", DPActivity.preferences().getString("last_country_code", "86")));
    paramLoginListener.add(new BasicNameValuePair("user", paramString1));
    paramLoginListener.add(new BasicNameValuePair("psw", paramString2));
    paramLoginListener.add(new BasicNameValuePair("code", paramString3));
    paramLoginListener.add(new BasicNameValuePair("cx", DeviceUtils.cxInfo("register")));
    paramString2 = DPApplication.instance().locationService().location();
    if (paramString2 != null)
    {
      paramLoginListener.add(new BasicNameValuePair("lat", String.valueOf(paramString2.getDouble("Lat"))));
      paramLoginListener.add(new BasicNameValuePair("lng", String.valueOf(paramString2.getDouble("Lng"))));
    }
    this.isPhoneNum = true;
    this.userName = paramString1;
    this.mSignUpRequest = new BasicMApiRequest("http://mapi.dianping.com/mapi/mlogin/signup.api", "POST", new MApiFormInputStream(paramLoginListener), CacheType.DISABLED, false, null);
    mapiService().exec(this.mSignUpRequest, this);
  }

  public void login(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean, LoginListener paramLoginListener)
  {
    if (this.mLoginRequest != null)
      mapiService().abort(this.mLoginRequest, this, true);
    this.mListener = paramLoginListener;
    ArrayList localArrayList = new ArrayList();
    paramLoginListener = "86";
    if (paramBoolean)
      paramLoginListener = DPActivity.preferences().getString("last_country_code", "86");
    localArrayList.add(new BasicNameValuePair("countrycode", paramLoginListener));
    localArrayList.add(new BasicNameValuePair("user", paramString1));
    localArrayList.add(new BasicNameValuePair("psw", paramString2));
    localArrayList.add(new BasicNameValuePair("type", paramString3));
    localArrayList.add(new BasicNameValuePair("cx", DeviceUtils.cxInfo("login")));
    if (!TextUtils.isEmpty(paramString4))
      localArrayList.add(new BasicNameValuePair("ticket", paramString4));
    if (!TextUtils.isEmpty(paramString5))
      localArrayList.add(new BasicNameValuePair("code", paramString5));
    paramString2 = DPApplication.instance().locationService().location();
    if (paramString2 != null)
    {
      localArrayList.add(new BasicNameValuePair("lat", String.valueOf(paramString2.getDouble("Lat"))));
      localArrayList.add(new BasicNameValuePair("lng", String.valueOf(paramString2.getDouble("Lng"))));
    }
    this.isPhoneNum = "1".equals(paramString3);
    this.userName = paramString1;
    this.mLoginRequest = new BasicMApiRequest("http://mapi.dianping.com/mapi/mlogin/login.api", "POST", new MApiFormInputStream(localArrayList), CacheType.DISABLED, false, null);
    mapiService().exec(this.mLoginRequest, this);
  }

  public void onDestroy()
  {
    if (this.mLoginRequest != null)
      mapiService().abort(this.mLoginRequest, this, true);
    if (this.mUserRequest != null)
      mapiService().abort(this.mUserRequest, this, true);
    if (this.mSignUpRequest != null)
      mapiService().abort(this.mSignUpRequest, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiResponse.message() != null)
    {
      paramMApiResponse = paramMApiResponse.message();
      if ((paramMApiRequest != this.mLoginRequest) && (paramMApiRequest != this.mSignUpRequest))
        break label78;
      if (this.mListener != null)
        this.mListener.onLoginFailed(0, paramMApiResponse);
      this.mLoginRequest = null;
      this.mSignUpRequest = null;
    }
    label78: 
    do
    {
      return;
      paramMApiResponse = new SimpleMsg("错误", "网络错误,请重试", 0, 0);
      break;
    }
    while (paramMApiRequest != this.mUserRequest);
    if (this.mListener != null)
      this.mListener.onLoginFailed(1, paramMApiResponse);
    this.mUserRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.mLoginRequest) || (paramMApiRequest == this.mSignUpRequest))
    {
      this.mLoginRequest = null;
      this.mSignUpRequest = null;
      paramMApiRequest = DPActivity.preferences().edit();
      if (this.isPhoneNum)
      {
        paramMApiRequest.putString("mainLoginPhoneNum", this.userName);
        paramMApiRequest.putBoolean("user_mypage_register_hasLogin", true);
        paramMApiRequest.commit();
        if ((paramMApiResponse.result() instanceof DPObject))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          sendUserRequest(paramMApiRequest.getString("Token"), paramMApiRequest.getString("NewToken"), this.mListener);
        }
      }
    }
    do
    {
      return;
      paramMApiRequest.putString("mainLoginAccountName", this.userName);
      break;
    }
    while (paramMApiRequest != this.mUserRequest);
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      syncCard(paramMApiRequest.getString("Token"));
      new FavoriteHelper(null).refresh(0, true);
      DPApplication.instance().accountService().update(paramMApiRequest);
      if (this.mListener != null)
        this.mListener.onLoginSucceed();
    }
    this.mUserRequest = null;
  }

  public void sendUserRequest(String paramString1, String paramString2, LoginListener paramLoginListener)
  {
    if (this.mUserRequest != null)
      mapiService().abort(this.mUserRequest, this, true);
    this.mListener = paramLoginListener;
    this.mUserRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/user.bin?token=" + paramString1 + "&newtoken=" + paramString2 + "&userid=0&refresh=true", CacheType.DISABLED);
    mapiService().exec(this.mUserRequest, this);
  }

  public static abstract interface LoginListener
  {
    public abstract void onLoginFailed(int paramInt, SimpleMsg paramSimpleMsg);

    public abstract void onLoginSucceed();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.fastloginview.LoginHelper
 * JD-Core Version:    0.6.0
 */