package com.tencent.connect.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.open.TDialog;
import com.tencent.open.utils.HttpUtils.HttpStatusException;
import com.tencent.open.utils.HttpUtils.NetworkUnavailableException;
import com.tencent.open.utils.SystemUtils;
import com.tencent.open.utils.Util;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseApi
{
  protected static final String ACTION_CHECK_TOKEN = "action_check_token";
  protected static final String ACTIVITY_AGENT = "com.tencent.open.agent.AgentActivity";
  protected static final String ACTIVITY_ENCRY_TOKEN = "com.tencent.open.agent.EncryTokenActivity";
  protected static final String DEFAULT_PF = "openmobile_android";
  private static final String KEY_REQUEST_CODE = "key_request_code";
  private static final int MSG_COMPLETE = 0;
  protected static final String PARAM_ENCRY_EOKEN = "encry_token";
  protected static final String PLATFORM = "desktop_m_qq";
  protected static final String PREFERENCE_PF = "pfStore";
  private static final String TAG = BaseApi.class.getName();
  protected static final String VERSION = "android";
  public static String businessId;
  public static String installChannel;
  public static boolean isOEM;
  public static String registerChannel;
  protected static int sRequestCode = 1000;
  protected Intent mActivityIntent = null;
  protected ProgressDialog mProgressDialog;
  protected QQAuth mQQAuth;
  protected List<ApiTask> mTaskList = null;
  protected QQToken mToken;
  protected IUiListener mUiListener = null;

  static
  {
    registerChannel = null;
    installChannel = null;
    businessId = null;
    isOEM = false;
  }

  public BaseApi(QQAuth paramQQAuth, QQToken paramQQToken)
  {
    this.mQQAuth = paramQQAuth;
    this.mToken = paramQQToken;
    this.mTaskList = new ArrayList();
  }

  public BaseApi(QQToken paramQQToken)
  {
    this(null, paramQQToken);
  }

  private Intent getAssitIntent(Activity paramActivity)
  {
    return new Intent(paramActivity.getApplicationContext(), AssistActivity.class);
  }

  public static void handleDataToListener(Intent paramIntent, IUiListener paramIUiListener)
  {
    int i = paramIntent.getIntExtra("key_error_code", 0);
    if (i == 0)
    {
      paramIntent = paramIntent.getStringExtra("key_response");
      if (paramIntent != null)
        try
        {
          paramIUiListener.onComplete(Util.parseJson(paramIntent));
          return;
        }
        catch (JSONException localJSONException)
        {
          paramIUiListener.onError(new UiError(-4, "服务器返回数据格式有误!", paramIntent));
          com.tencent.a.a.c.a("openSDK_LOG", "OpenUi, onActivityResult, json error", localJSONException);
          return;
        }
      com.tencent.a.a.c.b("openSDK_LOG", "OpenUi, onActivityResult, onComplete");
      paramIUiListener.onComplete(new JSONObject());
      return;
    }
    com.tencent.a.a.c.d("openSDK_LOG", "OpenUi, onActivityResult, onError = " + i + "");
    paramIUiListener.onError(new UiError(i, paramIntent.getStringExtra("key_error_msg"), paramIntent.getStringExtra("key_error_detail")));
  }

  protected Bundle composeActivityParams()
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("appid", this.mToken.getAppId());
    if (this.mToken.isSessionValid())
    {
      localBundle.putString("keystr", this.mToken.getAccessToken());
      localBundle.putString("keytype", "0x80");
    }
    Object localObject = this.mToken.getOpenId();
    if (localObject != null)
      localBundle.putString("hopenid", (String)localObject);
    localBundle.putString("platform", "androidqz");
    localObject = com.tencent.a.b.c.a().getSharedPreferences("pfStore", 0);
    if (isOEM)
      localBundle.putString("pf", "desktop_m_qq-" + installChannel + "-" + "android" + "-" + registerChannel + "-" + businessId);
    while (true)
    {
      localBundle.putString("sdkv", "2.4.lite");
      localBundle.putString("sdkp", "a");
      return localBundle;
      localBundle.putString("pf", ((SharedPreferences)localObject).getString("pf", "openmobile_android"));
      localBundle.putString("pf", "openmobile_android");
    }
  }

  protected Bundle composeCGIParams()
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("format", "json");
    localBundle.putString("status_os", Build.VERSION.RELEASE);
    localBundle.putString("status_machine", Build.MODEL);
    localBundle.putString("status_version", Build.VERSION.SDK);
    localBundle.putString("sdkv", "2.4.lite");
    localBundle.putString("sdkp", "a");
    if ((this.mToken != null) && (this.mToken.isSessionValid()))
    {
      localBundle.putString("access_token", this.mToken.getAccessToken());
      localBundle.putString("oauth_consumer_key", this.mToken.getAppId());
      localBundle.putString("openid", this.mToken.getOpenId());
    }
    localBundle.putString("appid_for_getting_config", this.mToken.getAppId());
    SharedPreferences localSharedPreferences = com.tencent.a.b.c.a().getSharedPreferences("pfStore", 0);
    if (isOEM)
    {
      localBundle.putString("pf", "desktop_m_qq-" + installChannel + "-" + "android" + "-" + registerChannel + "-" + businessId);
      return localBundle;
    }
    localBundle.putString("pf", localSharedPreferences.getString("pf", "openmobile_android"));
    return localBundle;
  }

  Intent getActivityIntent()
  {
    return this.mActivityIntent;
  }

  protected Intent getAgentIntent()
  {
    return getTargetActivityIntent("com.tencent.open.agent.AgentActivity");
  }

  protected Intent getAgentIntentWithTarget(String paramString)
  {
    Intent localIntent = new Intent();
    paramString = getTargetActivityIntent(paramString);
    if (paramString == null);
    do
      return null;
    while (paramString.getComponent() == null);
    localIntent.setClassName(paramString.getComponent().getPackageName(), "com.tencent.open.agent.AgentActivity");
    return localIntent;
  }

  protected Intent getTargetActivityIntent(String paramString)
  {
    Intent localIntent1 = new Intent();
    localIntent1.setClassName(Constants.PACKAGE_QZONE, paramString);
    Intent localIntent2 = new Intent();
    localIntent2.setClassName(Constants.PACKAGE_QQ, paramString);
    if (SystemUtils.isActivityExist(com.tencent.a.b.c.a(), localIntent2))
      paramString = localIntent2;
    while (true)
    {
      return paramString;
      if ((!SystemUtils.isActivityExist(com.tencent.a.b.c.a(), localIntent1)) || (SystemUtils.compareVersion(SystemUtils.getAppVersionName(com.tencent.a.b.c.a(), Constants.PACKAGE_QZONE), "3.4") < 0))
        break;
      paramString = localIntent1;
      if (!SystemUtils.isAppSignatureValid(com.tencent.a.b.c.a(), localIntent1.getComponent().getPackageName(), Constants.SIGNATRUE_QZONE))
        return null;
    }
    return null;
  }

  protected void handleDownloadLastestQQ(Activity paramActivity, Bundle paramBundle, IUiListener paramIUiListener)
  {
    paramIUiListener = new StringBuilder();
    paramIUiListener.append("http://qzs.qq.com/open/mobile/login/qzsjump.html?");
    paramIUiListener.append(Util.encodeUrl(paramBundle));
    new TDialog(paramActivity, "", paramIUiListener.toString(), null, this.mToken).show();
  }

  protected boolean hasActivityForIntent()
  {
    if (this.mActivityIntent != null)
      return SystemUtils.isActivityExist(com.tencent.a.b.c.a(), this.mActivityIntent);
    return false;
  }

  public void onActivityResult(Activity paramActivity, int paramInt1, int paramInt2, Intent paramIntent)
  {
    Object localObject = this.mTaskList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      ApiTask localApiTask = (ApiTask)((Iterator)localObject).next();
      if (localApiTask.mRequestCode != paramInt1)
        continue;
      localObject = localApiTask.mListener;
      this.mTaskList.remove(localApiTask);
    }
    while (true)
    {
      if (localObject == null)
      {
        Log.d(TAG, "BaseApi--onActivityResult-- listener == null");
        AssistActivity.setResultDataForLogin(paramActivity, paramIntent);
        return;
      }
      if (paramInt2 == -1)
        handleDataToListener(paramIntent, (IUiListener)localObject);
      while (true)
      {
        com.tencent.a.a.c.a().b();
        return;
        com.tencent.a.a.c.b("openSDK_LOG", "OpenUi, onActivityResult, Constants.ACTIVITY_CANCEL");
        ((IUiListener)localObject).onCancel();
      }
      localObject = null;
    }
  }

  public void releaseResource()
  {
  }

  protected void showProgressDialog(Context paramContext, String paramString1, String paramString2)
  {
    String str = paramString1;
    if (TextUtils.isEmpty(paramString1))
      str = "请稍候";
    paramString1 = paramString2;
    if (TextUtils.isEmpty(paramString2))
      paramString1 = "正在加载...";
    this.mProgressDialog = ProgressDialog.show(paramContext, str, paramString1);
    this.mProgressDialog.setCancelable(true);
  }

  protected void startAssitActivity(Activity paramActivity, IUiListener paramIUiListener)
  {
    AssistActivity.setApiObject(this);
    int i = sRequestCode;
    sRequestCode = i + 1;
    this.mActivityIntent.putExtra("key_request_code", i);
    this.mTaskList.add(new ApiTask(i, paramIUiListener));
    paramActivity.startActivityForResult(getAssitIntent(paramActivity), 10100);
  }

  protected void startAssitActivity(Fragment paramFragment, IUiListener paramIUiListener)
  {
    AssistActivity.setApiObject(this);
    int i = sRequestCode;
    sRequestCode = i + 1;
    this.mActivityIntent.putExtra("key_request_code", i);
    this.mTaskList.add(new ApiTask(i, paramIUiListener));
    paramFragment.startActivityForResult(getAssitIntent(paramFragment.getActivity()), 10100);
  }

  public class ApiTask
  {
    public IUiListener mListener;
    public int mRequestCode;

    public ApiTask(int paramIUiListener, IUiListener arg3)
    {
      this.mRequestCode = paramIUiListener;
      Object localObject;
      this.mListener = localObject;
    }
  }

  public class TempRequestListener
    implements IRequestListener
  {
    private Handler mHandler;
    private IUiListener mListener;

    public TempRequestListener(IUiListener arg2)
    {
      Object localObject;
      this.mListener = localObject;
      this.mHandler = new Handler(com.tencent.a.b.c.a().getMainLooper(), BaseApi.this)
      {
        public void handleMessage(Message paramMessage)
        {
          if (paramMessage.what == 0)
          {
            BaseApi.TempRequestListener.this.mListener.onComplete((JSONObject)paramMessage.obj);
            return;
          }
          BaseApi.TempRequestListener.this.mListener.onError(new UiError(paramMessage.what, (String)paramMessage.obj, null));
        }
      };
    }

    public void onComplete(JSONObject paramJSONObject)
    {
      Message localMessage = this.mHandler.obtainMessage();
      localMessage.obj = paramJSONObject;
      localMessage.what = 0;
      this.mHandler.sendMessage(localMessage);
    }

    public void onConnectTimeoutException(ConnectTimeoutException paramConnectTimeoutException)
    {
      Message localMessage = this.mHandler.obtainMessage();
      localMessage.obj = paramConnectTimeoutException.getMessage();
      localMessage.what = -7;
      this.mHandler.sendMessage(localMessage);
    }

    public void onHttpStatusException(HttpUtils.HttpStatusException paramHttpStatusException)
    {
      Message localMessage = this.mHandler.obtainMessage();
      localMessage.obj = paramHttpStatusException.getMessage();
      localMessage.what = -9;
      this.mHandler.sendMessage(localMessage);
    }

    public void onIOException(IOException paramIOException)
    {
      Message localMessage = this.mHandler.obtainMessage();
      localMessage.obj = paramIOException.getMessage();
      localMessage.what = -2;
      this.mHandler.sendMessage(localMessage);
    }

    public void onJSONException(JSONException paramJSONException)
    {
      Message localMessage = this.mHandler.obtainMessage();
      localMessage.obj = paramJSONException.getMessage();
      localMessage.what = -4;
      this.mHandler.sendMessage(localMessage);
    }

    public void onMalformedURLException(MalformedURLException paramMalformedURLException)
    {
      Message localMessage = this.mHandler.obtainMessage();
      localMessage.obj = paramMalformedURLException.getMessage();
      localMessage.what = -3;
      this.mHandler.sendMessage(localMessage);
    }

    public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException paramNetworkUnavailableException)
    {
      Message localMessage = this.mHandler.obtainMessage();
      localMessage.obj = paramNetworkUnavailableException.getMessage();
      localMessage.what = -10;
      this.mHandler.sendMessage(localMessage);
    }

    public void onSocketTimeoutException(SocketTimeoutException paramSocketTimeoutException)
    {
      Message localMessage = this.mHandler.obtainMessage();
      localMessage.obj = paramSocketTimeoutException.getMessage();
      localMessage.what = -8;
      this.mHandler.sendMessage(localMessage);
    }

    public void onUnknowException(Exception paramException)
    {
      Message localMessage = this.mHandler.obtainMessage();
      localMessage.obj = paramException.getMessage();
      localMessage.what = -6;
      this.mHandler.sendMessage(localMessage);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.connect.common.BaseApi
 * JD-Core Version:    0.6.0
 */