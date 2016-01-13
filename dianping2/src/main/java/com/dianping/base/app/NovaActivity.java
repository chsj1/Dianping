package com.dianping.base.app;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.CityConfig;
import com.dianping.app.DPActivity;
import com.dianping.app.Environment;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.base.widget.BeautifulProgressDialog;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.TitleBar;
import com.dianping.content.CityUtils;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.util.BitmapUtils;
import com.dianping.util.Log;
import com.dianping.util.log.NovaLog;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;

public class NovaActivity extends DPActivity
  implements AccountListener, LocationListener, LoginResultListener
{
  protected static final int DLG_LOGIN = 64016;
  protected static final int DLG_MESSAGE = 64006;
  protected static final int DLG_PROGRESS = 64005;
  protected static final int DLG_SIMPLE = 64007;
  public static final int REQUEST_SEARCH = 64256;
  public static final int RESULT_LOGIN_CANCEL = 64035;
  public static final int RESULT_LOGIN_FAILED = 64034;
  public static final int RESULT_LOGIN_OK = 64033;
  public static final int RESULT_PHONE_EXIST = 64036;
  private static final String TAG = NovaActivity.class.getSimpleName();
  protected boolean activityFinished = false;
  City cityFromData;
  private ClassLoader classLoader;
  protected SimpleMsg dlgMessage;
  protected String dlgProgressTitle;
  protected DPObject dpojbDlgMessage;
  private WeakReference<View> errorView;
  MApiRequest getCityInfoReq;

  @Deprecated
  protected ImageButton leftTitleButton;
  private WeakReference<View> loadingView;
  private TitleBar mTitleBar;
  protected Dialog managedDialog;
  protected int managedDialogId = 0;

  @Deprecated
  protected ImageButton rightTitleButton;
  private SpeedMonitorHelper speedMonitorHelper;

  @Deprecated
  protected TextView subtitleText;

  @Deprecated
  protected TextView titleButton;

  @Deprecated
  protected TextView titleText;
  Toast toast;

  private View findTitleRoot()
  {
    View localView1 = null;
    View localView2 = findViewById(R.id.title_bar);
    if (localView2 != null)
      localView1 = (View)localView2.getParent();
    do
    {
      return localView1;
      localView2 = findViewById(16908310);
    }
    while (localView2 == null);
    return (View)localView2.getParent();
  }

  private void gotoSelectCity()
  {
    if (super.cityId() <= 0)
    {
      sendAppActivityInfo();
      Toast.makeText(this, "请选择城市", 0).show();
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://switchcity?gotoSelectCityFirstTime=true"));
      localIntent.putExtra("intent", getIntent().getDataString());
      super.startActivity(localIntent);
      finish();
      this.activityFinished = true;
    }
  }

  private void gotoSelectCityFirstTime(Intent paramIntent)
  {
    if (!isNeedCity());
    while (true)
    {
      return;
      int j = 0;
      try
      {
        paramIntent = paramIntent.getData().getQueryParameter("cityid");
        i = j;
        if (!TextUtils.isEmpty(paramIntent))
          i = Integer.parseInt(paramIntent);
        if (i > 0)
        {
          if (i == cityId())
            continue;
          switchCity(i);
          return;
        }
        if (cityId() > 0)
          continue;
        if (location() == null);
        for (paramIntent = null; (paramIntent == null) || (paramIntent.id() <= 0); paramIntent = location().city())
        {
          gotoSelectCity();
          return;
        }
        cityConfig().switchCity(paramIntent);
        return;
      }
      catch (Exception paramIntent)
      {
        while (true)
          int i = j;
      }
    }
  }

  private void sendAppActivityInfo()
  {
    if ((super.cityId() <= 0) && (!DPActivity.preferences().contains("isFirstActivity")))
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("imei");
      localArrayList.add(Environment.imei());
      localArrayList.add("uuid");
      localArrayList.add(Environment.uuid());
      super.mapiService().exec(BasicMApiRequest.mapiPost("http://m.api.dianping.com/appactivate.api", (String[])localArrayList.toArray(new String[0])), null);
      DPActivity.preferences().edit().putBoolean("isFirstActivity", false).commit();
    }
  }

  private void switchCity(int paramInt)
  {
    this.cityFromData = CityUtils.getCityById(paramInt);
    showProgressDialog("正在切换城市请稍候...");
    getCityInfoRequest(paramInt);
  }

  public void addTitleBarShadow()
  {
    Rect localRect = new Rect();
    Object localObject = getWindow();
    if ((localObject != null) && (((Window)localObject).getDecorView() != null))
      ((Window)localObject).getDecorView().getWindowVisibleDisplayFrame(localRect);
    localObject = new int[2];
    FrameLayout localFrameLayout = (FrameLayout)findViewById(16908290);
    localFrameLayout.getLocationOnScreen(localObject);
    View localView;
    int j;
    FrameLayout.LayoutParams localLayoutParams;
    if (localFrameLayout.findViewById(R.id.iv_titleshadow) == null)
    {
      localView = getLayoutInflater().inflate(R.layout.title_shadow, localFrameLayout, false);
      j = (int)getResources().getDimension(R.dimen.titlebar_height);
      int i = getStatusBarHeight();
      localLayoutParams = new FrameLayout.LayoutParams(-1, -2);
      localLayoutParams.gravity = 48;
      if (localRect.top == 0)
        i = 0;
      if (Math.abs(localObject[1] - i - j) > 3)
        break label160;
    }
    label160: for (localLayoutParams.topMargin = 0; ; localLayoutParams.topMargin = j)
    {
      localView.setLayoutParams(localLayoutParams);
      localFrameLayout.addView(localView);
      return;
    }
  }

  public void dismissDialog()
  {
    if (this.isDestroyed);
    do
      return;
    while (this.managedDialogId == 0);
    if ((this.managedDialog != null) && (this.managedDialog.isShowing()))
      this.managedDialog.dismiss();
    this.dlgProgressTitle = null;
    this.dlgMessage = null;
    this.dpojbDlgMessage = null;
    this.managedDialogId = 0;
    this.managedDialog = null;
  }

  protected void doSwitchCityFromData(City paramCity)
  {
    dismissDialog();
    if (paramCity == null)
    {
      gotoSelectCity();
      return;
    }
    cityConfig().switchCity(paramCity);
  }

  public UserProfile getAccount()
  {
    Object localObject = accountService().profile();
    if (localObject != null)
      try
      {
        localObject = (UserProfile)((DPObject)localObject).edit().putString("Token", accountService().token()).generate().decodeToObject(UserProfile.DECODER);
        return localObject;
      }
      catch (ArchiveException localArchiveException)
      {
        Log.w(localArchiveException.getLocalizedMessage());
      }
    return (UserProfile)null;
  }

  protected void getCityInfoRequest(int paramInt)
  {
    if (this.getCityInfoReq == null)
    {
      this.getCityInfoReq = BasicMApiRequest.mapiGet("http://m.api.dianping.com/common/cityinfo.bin?cityid=" + paramInt, CacheType.DISABLED);
      mapiService().exec(this.getCityInfoReq, new RequestHandler()
      {
        public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          if (paramMApiRequest == NovaActivity.this.getCityInfoReq)
          {
            NovaActivity.this.doSwitchCityFromData(NovaActivity.this.cityFromData);
            NovaActivity.this.cityFromData = null;
            NovaActivity.this.getCityInfoReq = null;
          }
        }

        public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          if ((paramMApiRequest != NovaActivity.this.getCityInfoReq) || ((paramMApiResponse.result() instanceof DPObject)));
          try
          {
            paramMApiRequest = (City)((DPObject)paramMApiResponse.result()).decodeToObject(City.DECODER);
            NovaActivity.this.doSwitchCityFromData(paramMApiRequest);
            NovaActivity.this.getCityInfoReq = null;
            return;
          }
          catch (ArchiveException paramMApiRequest)
          {
            while (true)
              Log.e("city decodeToObject error");
          }
        }
      });
    }
  }

  public ClassLoader getClassLoader()
  {
    if (this.classLoader == null)
      return super.getClassLoader();
    return this.classLoader;
  }

  protected View getFailedView(String paramString, LoadingErrorView.LoadRetry paramLoadRetry)
  {
    Object localObject1 = this.errorView;
    if (localObject1 == null);
    Object localObject2;
    for (localObject1 = null; ; localObject1 = (View)((WeakReference)localObject1).get())
    {
      localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = getLayoutInflater().inflate(R.layout.error_item, null, false);
        this.errorView = new WeakReference(localObject2);
      }
      ((TextView)((View)localObject2).findViewById(16908308)).setText(paramString);
      if ((localObject2 instanceof LoadingErrorView))
        break;
      return null;
    }
    ((LoadingErrorView)localObject2).setCallBack(paramLoadRetry);
    return (View)(View)localObject2;
  }

  protected View getLoadingView()
  {
    Object localObject1 = this.loadingView;
    if (localObject1 == null);
    for (localObject1 = null; ; localObject1 = (View)((WeakReference)localObject1).get())
    {
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = getLayoutInflater().inflate(R.layout.loading_item, null, false);
        this.loadingView = new WeakReference(localObject2);
      }
      return localObject2;
    }
  }

  protected int getMessageIconId(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return 17301543;
    case 1:
    }
    return 17301659;
  }

  protected int getStatusBarHeight()
  {
    int i = 0;
    int j = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (j > 0)
      i = getResources().getDimensionPixelSize(j);
    return i;
  }

  public TitleBar getTitleBar()
  {
    return this.mTitleBar;
  }

  public int getUserId()
  {
    return accountService().id();
  }

  public void gotoLogin()
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    accountService().login(this);
  }

  public void gotoLogin(String paramString)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    Intent localIntent = new Intent("com.dianping.action.LOGIN");
    if (paramString != null)
      localIntent.putExtra("user", paramString);
    startActivityForResult(localIntent, 64016);
  }

  public void gotoLogin(List<NameValuePair> paramList)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    accountService().login(this, paramList);
  }

  public void hideTitleBar()
  {
    if (this.mTitleBar != null)
      this.mTitleBar.hide();
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 1);
  }

  protected boolean isDPObjectof(Object paramObject)
  {
    return paramObject instanceof DPObject;
  }

  protected boolean isDPObjectof(Object paramObject, String paramString)
  {
    if (!isDPObjectof(paramObject));
    do
      return false;
    while (!((DPObject)paramObject).isClass(paramString));
    return true;
  }

  public boolean isLogined()
  {
    if (getAccount() == null);
    do
      return false;
    while (TextUtils.isEmpty(accountService().token()));
    return true;
  }

  protected boolean isNeedCity()
  {
    return true;
  }

  protected boolean isNeedLogin()
  {
    return false;
  }

  public Location location()
  {
    if (locationService().location() == null)
      return null;
    try
    {
      Location localLocation = (Location)locationService().location().decodeToObject(Location.DECODER);
      return localLocation;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public boolean locationCare()
  {
    return false;
  }

  public void logout()
  {
    accountService().logout();
  }

  protected boolean needTitleBarShadow()
  {
    return true;
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    onAccountSwitched(getAccount());
  }

  public void onAccountInfoChanged(UserProfile paramUserProfile)
  {
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
  }

  public void onBackPressed()
  {
    GAHelper.instance().contextStatisticsEvent(this, "back", null, "tap");
    try
    {
      super.onBackPressed();
      return;
    }
    catch (Exception localException2)
    {
      try
      {
        if (Build.VERSION.SDK_INT >= 11)
          getFragmentManager().getClass().getMethod("noteStateNotSaved", new Class[0]).invoke(getFragmentManager(), new Object[0]);
        super.onBackPressed();
        return;
      }
      catch (Exception localException2)
      {
        NovaLog.e("onBackPressed", localException2.toString());
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Uri localUri = getIntent().getData();
    if ((paramBundle == null) && (localUri != null))
    {
      paramBundle = localUri.getQueryParameter("pushmsgrowkey");
      if (!TextUtils.isEmpty(paramBundle))
      {
        paramBundle = BasicMApiRequest.mapiPost("http://m.api.dianping.com/setmsgread.bin", new String[] { "msgid", paramBundle });
        mapiService().exec(paramBundle, null);
      }
    }
    gotoSelectCityFirstTime(getIntent());
    if (getParent() == null)
    {
      this.mTitleBar = initCustomTitle();
      this.leftTitleButton = ((ImageButton)this.mTitleBar.findViewById(R.id.left_title_button));
      this.titleText = ((TextView)this.mTitleBar.findViewById(16908310));
      this.subtitleText = ((TextView)this.mTitleBar.findViewById(R.id.subtitle));
      this.rightTitleButton = ((ImageButton)this.mTitleBar.findViewById(R.id.right_title_button));
      this.titleButton = ((TextView)this.mTitleBar.findViewById(R.id.title_button));
      this.mTitleBar.setLeftView(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          NovaActivity.this.onLeftTitleButtonClicked();
        }
      });
    }
    accountService().addListener(this);
    setTitle(getTitle());
    if ((!isLogined()) && (isNeedLogin()))
      gotoLogin();
  }

  protected void onDestroy()
  {
    accountService().removeListener(this);
    super.onDestroy();
  }

  public void onDialogItemClick(int paramInt, Parcelable paramParcelable)
  {
  }

  protected void onLeftTitleButtonClicked()
  {
    onBackPressed();
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    return false;
  }

  public void onLoginCancel()
  {
    if (isNeedLogin())
      finish();
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
    onLoginCancel();
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    onLogin(true);
  }

  public void onLowMemory()
  {
    super.onLowMemory();
    NetworkThumbView.memcache().clear();
  }

  public void onMessageConfirm()
  {
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    if ((paramIntent != null) && (paramIntent.getData() != null))
    {
      Object localObject = paramIntent.getData().getQueryParameter("pushmsgrowkey");
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        localObject = BasicMApiRequest.mapiPost("http://m.api.dianping.com/setmsgread.bin", new String[] { "msgid", localObject });
        mapiService().exec((Request)localObject, null);
      }
    }
    gotoSelectCityFirstTime(paramIntent);
  }

  protected void onPause()
  {
    super.onPause();
    locationService().removeListener(this);
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
    onAccountInfoChanged(getAccount());
  }

  public void onProgressDialogCancel()
  {
  }

  protected void onResume()
  {
    super.onResume();
    locationService().addListener(this);
    if (location() != null)
      onLocationChanged(locationService());
    if (locationCare())
      startLocate();
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    if (paramBoolean)
    {
      if (needTitleBarShadow())
        addTitleBarShadow();
    }
    else
      return;
    removeTitleBarShadow();
  }

  public void removeTitleBarShadow()
  {
    Object localObject = new int[2];
    findViewById(16908290).getLocationOnScreen(localObject);
    localObject = (FrameLayout)findViewById(16908290);
    View localView = ((FrameLayout)localObject).findViewById(R.id.iv_titleshadow);
    if (localView != null)
      ((FrameLayout)localObject).removeView(localView);
  }

  public void setClassLoader(ClassLoader paramClassLoader)
  {
    this.classLoader = paramClassLoader;
  }

  public void setContentView(int paramInt)
  {
    super.setContentView(paramInt);
    FrameLayout localFrameLayout = (FrameLayout)findViewById(16908290);
    localFrameLayout.setBackgroundResource(R.drawable.main_background);
    BitmapUtils.fixBackgroundRepeat(localFrameLayout);
  }

  public void setContentView(View paramView)
  {
    super.setContentView(paramView);
    paramView = (FrameLayout)findViewById(16908290);
    paramView.setBackgroundResource(R.drawable.main_background);
    BitmapUtils.fixBackgroundRepeat(paramView);
  }

  @Deprecated
  public void setDoubleLineTitle(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    if (this.titleText != null)
      this.titleText.setText(paramCharSequence1);
    if (this.subtitleText != null)
      this.subtitleText.setText(paramCharSequence2);
  }

  @Deprecated
  public void setRightTitleButton(int paramInt, View.OnClickListener paramOnClickListener)
  {
    if (this.rightTitleButton == null)
      return;
    if (paramInt == -1)
    {
      this.rightTitleButton.setVisibility(8);
      return;
    }
    this.rightTitleButton.setVisibility(0);
    this.rightTitleButton.setImageResource(paramInt);
    this.rightTitleButton.setOnClickListener(paramOnClickListener);
  }

  @Deprecated
  public void setRightTitleButtonRes(int paramInt)
  {
    if (this.rightTitleButton == null)
      return;
    if (paramInt == -1)
    {
      this.rightTitleButton.setVisibility(8);
      return;
    }
    this.rightTitleButton.setImageResource(paramInt);
  }

  public void setSubtitle(CharSequence paramCharSequence)
  {
    if (this.mTitleBar != null)
      this.mTitleBar.setSubTitle(paramCharSequence);
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    super.setTitle(paramCharSequence);
    if (this.mTitleBar != null)
      this.mTitleBar.setTitle(paramCharSequence);
  }

  @Deprecated
  public void setTitleButton(String paramString, View.OnClickListener paramOnClickListener)
  {
    if (this.titleButton == null)
      return;
    if ((paramString == null) || ("".equals(paramString)))
    {
      this.titleButton.setVisibility(8);
      return;
    }
    this.titleButton.setVisibility(0);
    this.titleButton.setText(paramString);
    this.titleButton.setOnClickListener(paramOnClickListener);
  }

  public void setTitleVisibility(int paramInt)
  {
    View localView = findTitleRoot();
    if (localView != null)
    {
      localView.setVisibility(paramInt);
      return;
    }
    Log.e(TAG, "findTitleRoot null");
  }

  public void showAlertDialog(String paramString1, String paramString2)
  {
    showAlertDialog(paramString1, paramString2, "确定");
  }

  public void showAlertDialog(String paramString1, String paramString2, String paramString3)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(paramString1).setMessage(paramString2).setPositiveButton(paramString3, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.dismiss();
      }
    });
    localBuilder.create().show();
  }

  public void showMessageDialog(DPObject paramDPObject)
  {
    showMessageDialog(paramDPObject, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    });
  }

  public void showMessageDialog(DPObject paramDPObject, DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    if (paramDPObject != null);
    for (this.dpojbDlgMessage = paramDPObject; ; this.dpojbDlgMessage = new DPObject("SimpleMsg").edit().putString("Title", "错误").putString("Content", "操作出错").generate())
    {
      paramDPObject = new AlertDialog.Builder(this);
      paramDPObject.setIcon(getMessageIconId(this.dpojbDlgMessage.getInt("Icon")));
      paramDPObject.setMessage(this.dpojbDlgMessage.getString("Content"));
      paramDPObject.setPositiveButton(R.string.ok, paramOnClickListener);
      paramDPObject = paramDPObject.create();
      paramDPObject.setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramDialogInterface)
        {
          NovaActivity.this.onMessageConfirm();
          if (NovaActivity.this.managedDialogId == 64006)
            NovaActivity.this.managedDialogId = 0;
          NovaActivity.this.dpojbDlgMessage = null;
        }
      });
      this.managedDialogId = 64006;
      this.managedDialog = paramDPObject;
      paramDPObject.show();
      return;
    }
  }

  public void showMessageDialog(SimpleMsg paramSimpleMsg)
  {
    showMessageDialog(paramSimpleMsg, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    });
  }

  public void showMessageDialog(SimpleMsg paramSimpleMsg, DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    if (paramSimpleMsg != null);
    for (this.dlgMessage = paramSimpleMsg; ; this.dlgMessage = new SimpleMsg("错误", "操作出错", 0, 0))
    {
      paramSimpleMsg = new AlertDialog.Builder(this);
      paramSimpleMsg.setIcon(getMessageIconId(this.dlgMessage.icon()));
      paramSimpleMsg.setMessage(this.dlgMessage.content());
      paramSimpleMsg.setPositiveButton(R.string.ok, paramOnClickListener);
      paramSimpleMsg = paramSimpleMsg.create();
      paramSimpleMsg.setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramDialogInterface)
        {
          NovaActivity.this.onMessageConfirm();
          if (NovaActivity.this.managedDialogId == 64006)
            NovaActivity.this.managedDialogId = 0;
          NovaActivity.this.dlgMessage = null;
        }
      });
      this.managedDialogId = 64006;
      this.managedDialog = paramSimpleMsg;
      paramSimpleMsg.show();
      return;
    }
  }

  public void showProgressDialog(String paramString)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    this.dlgProgressTitle = paramString;
    ProgressDialog localProgressDialog = new ProgressDialog(this);
    localProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        if (NovaActivity.this.managedDialogId == 64005)
          NovaActivity.this.managedDialogId = 0;
        NovaActivity.this.dlgProgressTitle = null;
        NovaActivity.this.onProgressDialogCancel();
      }
    });
    localProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
    {
      public boolean onKey(DialogInterface paramDialogInterface, int paramInt, KeyEvent paramKeyEvent)
      {
        return paramInt == 84;
      }
    });
    if (this.dlgProgressTitle == null);
    for (paramString = "载入中..."; ; paramString = this.dlgProgressTitle)
    {
      localProgressDialog.setMessage(paramString);
      this.managedDialogId = 64005;
      this.managedDialog = localProgressDialog;
      localProgressDialog.show();
      return;
    }
  }

  public void showProgressDialog(String paramString, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    this.dlgProgressTitle = paramString;
    BeautifulProgressDialog localBeautifulProgressDialog = new BeautifulProgressDialog(this);
    localBeautifulProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(paramOnCancelListener)
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        if (this.val$cancelListener != null)
          this.val$cancelListener.onCancel(paramDialogInterface);
        if (NovaActivity.this.managedDialogId == 64005)
          NovaActivity.this.managedDialogId = 0;
        NovaActivity.this.dlgProgressTitle = null;
        NovaActivity.this.managedDialog = null;
        NovaActivity.this.onProgressDialogCancel();
      }
    });
    localBeautifulProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
    {
      public boolean onKey(DialogInterface paramDialogInterface, int paramInt, KeyEvent paramKeyEvent)
      {
        return paramInt == 84;
      }
    });
    paramOnCancelListener = paramString;
    if (paramString == null)
      paramOnCancelListener = "载入中...";
    localBeautifulProgressDialog.setMessage(paramOnCancelListener);
    this.managedDialogId = 64005;
    this.managedDialog = localBeautifulProgressDialog;
    localBeautifulProgressDialog.show();
  }

  public void showShortToast(String paramString)
  {
    showToast(paramString, 0);
  }

  public void showSimpleAlertDialog(String paramString1, String paramString2, String paramString3, DialogInterface.OnClickListener paramOnClickListener1, String paramString4, DialogInterface.OnClickListener paramOnClickListener2)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(paramString1).setMessage(paramString2).setPositiveButton(paramString3, paramOnClickListener1).setNegativeButton(paramString4, paramOnClickListener2);
    paramString1 = localBuilder.create();
    this.managedDialog = paramString1;
    this.managedDialogId = 64007;
    paramString1.show();
  }

  public void showTitleBar()
  {
    if (this.mTitleBar != null)
      this.mTitleBar.show();
  }

  public void showToast(String paramString)
  {
    showToast(paramString, 1);
  }

  public void showToast(String paramString, int paramInt)
  {
    if (this.toast == null)
      this.toast = Toast.makeText(this, paramString, paramInt);
    while (true)
    {
      this.toast.show();
      return;
      this.toast.setText(paramString);
      this.toast.setDuration(paramInt);
    }
  }

  public void startLocate()
  {
    if (locationService().status() <= 0)
      locationService().start();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.NovaActivity
 * JD-Core Version:    0.6.0
 */