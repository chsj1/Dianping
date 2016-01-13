package com.dianping.main.guide;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.base.update.UpdateManager;
import com.dianping.base.update.UpdateUIManager;
import com.dianping.base.util.HistoryHelper;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.util.PurchaseResultHelper;
import com.dianping.base.util.RedAlertManager;
import com.dianping.base.widget.CssStyleManager;
import com.dianping.base.widget.DoubleLineDialog;
import com.dianping.base.widget.MeasuredTextView;
import com.dianping.base.widget.NovaFragmentTabActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.configservice.ConfigChangeListener;
import com.dianping.configservice.ConfigService;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.debug.DebugWindowService;
import com.dianping.loader.MyResources;
import com.dianping.loader.MyResources.ResourceOverrideable;
import com.dianping.locationservice.LocationService;
import com.dianping.main.find.MainFindFragment;
import com.dianping.main.home.MainHomeFragment;
import com.dianping.main.user.UserFragment;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.tuan.fragment.TuanHomeFragment;
import com.dianping.util.DateUtil;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.array;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;

public class MainActivity extends NovaFragmentTabActivity
  implements ConfigChangeListener, CityConfig.SwitchListener, AccountListener, MyResources.ResourceOverrideable, SharedPreferences.OnSharedPreferenceChangeListener
{
  private static final String FIND_TAB_IS_FIRST_SHOW = "find_tab_is_first_show";
  public static String LOCAL_REDALERT_NUM;
  private static final String LOG_TAG = MainActivity.class.getSimpleName();
  private static final int MSG_SECOND_BACK = 1;
  private static final String TAB_TAG_FIND = "发现";
  private static final String TAB_TAG_HOME = "首页";
  private static final String TAB_TAG_MINE = "我的";
  private static final String TAB_TAG_TUAN = "闪惠团购";
  private static long lastNetworkUnaMills = 0L;
  private boolean GAFlag = false;
  private AssetManager assetManager;
  private City backupSwithCity;
  private DPObject category;
  private String gaPageName = "home";
  public boolean hasNewUpdate;
  private String host;
  boolean isFindTabFirstShow;
  boolean isFirstTimeGetIntoFindTab = true;
  private boolean isSearchFragmentExist = false;
  boolean isSencondBackDown = false;
  private boolean isStoped;
  private String mLastTab;
  private SharedPreferences mPreferences;
  private Handler mainActHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1)
        MainActivity.this.isSencondBackDown = false;
    }
  };
  private MyResources myResources;
  final BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.RedAlerts".equals(paramIntent.getAction()))
        MainActivity.this.checkRedAlert();
      do
      {
        return;
        if (!"com.dianping.action.ShowPushDialog".equals(paramIntent.getAction()))
          continue;
        paramContext = new Intent("android.intent.action.VIEW", Uri.parse("dianping://pushreview"));
        paramContext.putExtra("contentTitle", paramIntent.getStringExtra("contentTitle"));
        paramContext.putExtra("contentText", paramIntent.getStringExtra("contentText"));
        paramContext.putExtra("url", paramIntent.getStringExtra("url"));
        MainActivity.this.startActivity(paramContext);
        return;
      }
      while (!"com.dianping.action.NEW_MESSAGE".equals(paramIntent.getAction()));
      MainActivity.this.updateMessageCountInTab(MainActivity.this.getMailCount(), "我的");
    }
  };
  private DPObject region;
  private Resources resources;
  private String screening;
  private DPObject sort;
  private SpeedMonitorHelper speedMonitorHelper;
  DoubleLineDialog switchCityDialog;
  private Resources.Theme theme;

  static
  {
    LOCAL_REDALERT_NUM = "local_redalert_num";
  }

  private void checkNewVersionMark()
  {
    if (NovaConfigUtils.instance().disableCheckUpdate());
    SharedPreferences localSharedPreferences;
    do
    {
      return;
      localSharedPreferences = getSharedPreferences(getPackageName(), 0);
    }
    while ((!this.hasNewUpdate) || (!localSharedPreferences.getBoolean("show_new_version_red_mark", true)));
    setRedMarkVisibilityByTab("我的", 0);
  }

  private boolean checkSignature()
  {
    try
    {
      Signature[] arrayOfSignature = getPackageManager().getPackageInfo(getPackageName(), 64).signatures;
      if (arrayOfSignature != null)
      {
        if (arrayOfSignature.length == 0)
          return false;
        int j = arrayOfSignature.length;
        int i = 0;
        while (i < j)
        {
          String str = Integer.toHexString(arrayOfSignature[i].toCharsString().hashCode());
          if (!"ac6fc3fe".equalsIgnoreCase(str))
          {
            boolean bool = "600cf559".equalsIgnoreCase(str);
            if (!bool);
          }
          else
          {
            return true;
          }
          i += 1;
        }
      }
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  private boolean isInMyTab(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    while (true)
    {
      return false;
      String[] arrayOfString = getResources().getStringArray(R.array.mine_tab);
      int i = 0;
      while (i < arrayOfString.length)
      {
        if (paramString.equalsIgnoreCase(arrayOfString[i]))
          return true;
        i += 1;
      }
    }
  }

  private boolean selectTabByHost(String paramString)
  {
    if ("home".equals(paramString))
    {
      this.mTabHost.setCurrentTabByTag("首页");
      return true;
    }
    if ("tuanmain".equals(paramString))
    {
      this.mTabHost.setCurrentTabByTag("闪惠团购");
      return true;
    }
    if ("find".equals(paramString))
    {
      this.mTabHost.setCurrentTabByTag("发现");
      return true;
    }
    if ("me".equals(paramString))
    {
      this.mTabHost.setCurrentTabByTag("我的");
      return true;
    }
    return false;
  }

  // ERROR //
  private void tuanUrlSchema(Intent paramIntent)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +4 -> 5
    //   4: return
    //   5: aload_0
    //   6: invokespecial 250	com/dianping/base/widget/NovaFragmentTabActivity:getIntent	()Landroid/content/Intent;
    //   9: invokevirtual 256	android/content/Intent:getExtras	()Landroid/os/Bundle;
    //   12: astore_1
    //   13: aload_1
    //   14: ifnull +96 -> 110
    //   17: aload_1
    //   18: ldc_w 257
    //   21: invokevirtual 262	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
    //   24: ifeq +17 -> 41
    //   27: aload_0
    //   28: aload_1
    //   29: ldc_w 257
    //   32: invokevirtual 266	android/os/Bundle:getParcelable	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   35: checkcast 268	com/dianping/archive/DPObject
    //   38: putfield 270	com/dianping/main/guide/MainActivity:category	Lcom/dianping/archive/DPObject;
    //   41: aload_1
    //   42: ldc_w 271
    //   45: invokevirtual 262	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
    //   48: ifeq +17 -> 65
    //   51: aload_0
    //   52: aload_1
    //   53: ldc_w 271
    //   56: invokevirtual 266	android/os/Bundle:getParcelable	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   59: checkcast 268	com/dianping/archive/DPObject
    //   62: putfield 273	com/dianping/main/guide/MainActivity:region	Lcom/dianping/archive/DPObject;
    //   65: aload_1
    //   66: ldc_w 274
    //   69: invokevirtual 262	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
    //   72: ifeq +17 -> 89
    //   75: aload_0
    //   76: aload_1
    //   77: ldc_w 274
    //   80: invokevirtual 266	android/os/Bundle:getParcelable	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   83: checkcast 268	com/dianping/archive/DPObject
    //   86: putfield 276	com/dianping/main/guide/MainActivity:sort	Lcom/dianping/archive/DPObject;
    //   89: aload_1
    //   90: ldc_w 277
    //   93: invokevirtual 262	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
    //   96: ifeq +14 -> 110
    //   99: aload_0
    //   100: aload_1
    //   101: ldc_w 277
    //   104: invokevirtual 281	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   107: putfield 283	com/dianping/main/guide/MainActivity:screening	Ljava/lang/String;
    //   110: aload_0
    //   111: getfield 270	com/dianping/main/guide/MainActivity:category	Lcom/dianping/archive/DPObject;
    //   114: ifnonnull +10 -> 124
    //   117: aload_0
    //   118: getstatic 288	com/dianping/tuan/fragment/DealListFragmentWithFilter:ALL_CATEGORY	Lcom/dianping/archive/DPObject;
    //   121: putfield 270	com/dianping/main/guide/MainActivity:category	Lcom/dianping/archive/DPObject;
    //   124: aload_0
    //   125: getfield 273	com/dianping/main/guide/MainActivity:region	Lcom/dianping/archive/DPObject;
    //   128: ifnonnull +10 -> 138
    //   131: aload_0
    //   132: getstatic 291	com/dianping/tuan/fragment/DealListFragmentWithFilter:ALL_REGION	Lcom/dianping/archive/DPObject;
    //   135: putfield 273	com/dianping/main/guide/MainActivity:region	Lcom/dianping/archive/DPObject;
    //   138: aload_0
    //   139: getfield 276	com/dianping/main/guide/MainActivity:sort	Lcom/dianping/archive/DPObject;
    //   142: ifnonnull +10 -> 152
    //   145: aload_0
    //   146: getstatic 294	com/dianping/tuan/fragment/DealListFragmentWithFilter:DEFAULT_SORT	Lcom/dianping/archive/DPObject;
    //   149: putfield 276	com/dianping/main/guide/MainActivity:sort	Lcom/dianping/archive/DPObject;
    //   152: aload_0
    //   153: invokespecial 250	com/dianping/base/widget/NovaFragmentTabActivity:getIntent	()Landroid/content/Intent;
    //   156: invokevirtual 298	android/content/Intent:getData	()Landroid/net/Uri;
    //   159: astore_3
    //   160: aload_3
    //   161: ifnull -157 -> 4
    //   164: aload_3
    //   165: invokevirtual 303	android/net/Uri:toString	()Ljava/lang/String;
    //   168: ldc_w 305
    //   171: invokevirtual 308	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   174: ifne +16 -> 190
    //   177: aload_3
    //   178: invokevirtual 303	android/net/Uri:toString	()Ljava/lang/String;
    //   181: ldc_w 310
    //   184: invokevirtual 308	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   187: ifeq +107 -> 294
    //   190: iconst_0
    //   191: istore 4
    //   193: aload_3
    //   194: ldc_w 312
    //   197: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   200: invokestatic 319	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   203: istore 5
    //   205: iload 5
    //   207: istore 4
    //   209: iconst_0
    //   210: istore 5
    //   212: aload_3
    //   213: ldc_w 321
    //   216: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   219: invokestatic 319	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   222: istore 6
    //   224: iload 6
    //   226: istore 5
    //   228: aload_0
    //   229: aload_0
    //   230: getfield 270	com/dianping/main/guide/MainActivity:category	Lcom/dianping/archive/DPObject;
    //   233: invokevirtual 325	com/dianping/archive/DPObject:edit	()Lcom/dianping/archive/DPObject$Editor;
    //   236: ldc_w 327
    //   239: iload 4
    //   241: invokeinterface 333 3 0
    //   246: ldc_w 335
    //   249: iload 5
    //   251: invokeinterface 333 3 0
    //   256: ldc_w 337
    //   259: aload_3
    //   260: ldc_w 339
    //   263: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   266: invokeinterface 343 3 0
    //   271: ldc_w 345
    //   274: aload_3
    //   275: ldc_w 347
    //   278: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   281: invokeinterface 343 3 0
    //   286: invokeinterface 351 1 0
    //   291: putfield 270	com/dianping/main/guide/MainActivity:category	Lcom/dianping/archive/DPObject;
    //   294: aload_3
    //   295: invokevirtual 303	android/net/Uri:toString	()Ljava/lang/String;
    //   298: ldc_w 353
    //   301: invokevirtual 308	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   304: ifne +16 -> 320
    //   307: aload_3
    //   308: invokevirtual 303	android/net/Uri:toString	()Ljava/lang/String;
    //   311: ldc_w 355
    //   314: invokevirtual 308	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   317: ifeq +136 -> 453
    //   320: iconst_0
    //   321: istore 4
    //   323: aload_3
    //   324: ldc_w 357
    //   327: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   330: invokestatic 319	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   333: istore 5
    //   335: iload 5
    //   337: istore 4
    //   339: iconst_0
    //   340: istore 5
    //   342: aload_3
    //   343: ldc_w 359
    //   346: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   349: invokestatic 319	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   352: istore 6
    //   354: iload 6
    //   356: istore 5
    //   358: iconst_0
    //   359: istore 6
    //   361: aload_3
    //   362: ldc_w 361
    //   365: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   368: invokestatic 319	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   371: istore 7
    //   373: iload 7
    //   375: istore 6
    //   377: aload_0
    //   378: aload_0
    //   379: getfield 273	com/dianping/main/guide/MainActivity:region	Lcom/dianping/archive/DPObject;
    //   382: invokevirtual 325	com/dianping/archive/DPObject:edit	()Lcom/dianping/archive/DPObject$Editor;
    //   385: ldc_w 327
    //   388: iload 4
    //   390: invokeinterface 333 3 0
    //   395: ldc_w 335
    //   398: iload 5
    //   400: invokeinterface 333 3 0
    //   405: ldc_w 337
    //   408: aload_3
    //   409: ldc_w 363
    //   412: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   415: invokeinterface 343 3 0
    //   420: ldc_w 345
    //   423: aload_3
    //   424: ldc_w 365
    //   427: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   430: invokeinterface 343 3 0
    //   435: ldc_w 367
    //   438: iload 6
    //   440: invokeinterface 333 3 0
    //   445: invokeinterface 351 1 0
    //   450: putfield 273	com/dianping/main/guide/MainActivity:region	Lcom/dianping/archive/DPObject;
    //   453: aload_3
    //   454: ldc_w 274
    //   457: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   460: astore_2
    //   461: aload_2
    //   462: astore_1
    //   463: aload_2
    //   464: invokestatic 208	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   467: ifeq +11 -> 478
    //   470: aload_3
    //   471: ldc_w 369
    //   474: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   477: astore_1
    //   478: aload_1
    //   479: invokestatic 208	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   482: ifne +28 -> 510
    //   485: aload_0
    //   486: aload_0
    //   487: getfield 276	com/dianping/main/guide/MainActivity:sort	Lcom/dianping/archive/DPObject;
    //   490: invokevirtual 325	com/dianping/archive/DPObject:edit	()Lcom/dianping/archive/DPObject$Editor;
    //   493: ldc_w 327
    //   496: aload_1
    //   497: invokeinterface 343 3 0
    //   502: invokeinterface 351 1 0
    //   507: putfield 276	com/dianping/main/guide/MainActivity:sort	Lcom/dianping/archive/DPObject;
    //   510: aload_3
    //   511: invokevirtual 303	android/net/Uri:toString	()Ljava/lang/String;
    //   514: ldc_w 371
    //   517: invokevirtual 308	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   520: ifeq -516 -> 4
    //   523: aload_0
    //   524: aload_3
    //   525: ldc_w 277
    //   528: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   531: putfield 283	com/dianping/main/guide/MainActivity:screening	Ljava/lang/String;
    //   534: return
    //   535: astore_1
    //   536: aload_3
    //   537: ldc_w 257
    //   540: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   543: invokestatic 319	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   546: istore 5
    //   548: iload 5
    //   550: istore 4
    //   552: goto -343 -> 209
    //   555: astore_1
    //   556: aload_3
    //   557: ldc_w 271
    //   560: invokevirtual 315	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   563: invokestatic 319	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   566: istore 5
    //   568: iload 5
    //   570: istore 4
    //   572: goto -233 -> 339
    //   575: astore_1
    //   576: goto -199 -> 377
    //   579: astore_1
    //   580: goto -222 -> 358
    //   583: astore_1
    //   584: goto -245 -> 339
    //   587: astore_1
    //   588: goto -360 -> 228
    //   591: astore_1
    //   592: goto -383 -> 209
    //
    // Exception table:
    //   from	to	target	type
    //   193	205	535	java/lang/Exception
    //   323	335	555	java/lang/Exception
    //   361	373	575	java/lang/Exception
    //   342	354	579	java/lang/Exception
    //   556	568	583	java/lang/Exception
    //   212	224	587	java/lang/Exception
    //   536	548	591	java/lang/Exception
  }

  private void updateVersionCode()
  {
    int i = Environment.versionCode();
    if (i != NovaFragmentTabActivity.preferences().getInt("versionCode", 0))
      NovaFragmentTabActivity.preferences().edit().putInt("versionCode", i).commit();
  }

  void checkRedAlert()
  {
    int i;
    int j;
    if ((RedAlertManager.getInstance().checkRedAlertByTag("find.topmodule") != null) && (!RedAlertManager.getInstance().checkRedAlertByTag("find.topmodule").equals(RedAlertManager.FIND_TOPMODULE_EMPTY_FLAG)))
    {
      i = 1;
      if (RedAlertManager.getInstance().checkRedAlertByTag("find.page") == null)
        break label232;
      j = 1;
      label46: this.isFindTabFirstShow = this.mPreferences.getBoolean("find_tab_is_first_show", true);
      if ((!this.isFindTabFirstShow) && (j == 0))
        break label273;
      RedAlertManager.getInstance().updateRedAlert("find.page");
      RedAlertManager.getInstance().updateRedAlert("find.topmodule");
      if ((!isLogined()) || (i == 0) || (RedAlertManager.getInstance().getRedAlertNumByTag("find.topmodule") < 0))
        break label237;
      setRedMarkVisibilityByTab("发现", 8);
      getSharedPreferences(getPackageName(), 32768).edit().putInt(LOCAL_REDALERT_NUM, RedAlertManager.getInstance().getRedAlertNumByTag("find.topmodule")).commit();
      updateMessageCountInTab(RedAlertManager.getInstance().getRedAlertNumByTag("find.topmodule"), "发现");
      label176: if (RedAlertManager.getInstance().checkRedAlertByTag("me.mypage") == null)
        break label372;
      i = 1;
      label190: if (i == 0)
        break label387;
      if ((!isCurTab("我的")) || (i == 0))
        break label377;
      RedAlertManager.getInstance().updateRedAlert("me.mypage");
    }
    while (true)
    {
      updateMessageCountInTab(getMailCount(), "我的");
      return;
      i = 0;
      break;
      label232: j = 0;
      break label46;
      label237: if (!isLogined())
      {
        updateMessageCountInTab(0, "发现");
        setRedMarkVisibilityByTab("发现", 8);
        break label176;
      }
      setRedMarkVisibilityByTab("发现", 8);
      break label176;
      label273: setRedMarkVisibilityByTab("发现", 8);
      if ((isLogined()) && ((RedAlertManager.getInstance().checkRedAlertByTag("find.topmodule") == null) || (!RedAlertManager.getInstance().checkRedAlertByTag("find.topmodule").equals(RedAlertManager.FIND_TOPMODULE_EMPTY_FLAG))))
      {
        SharedPreferences localSharedPreferences = getSharedPreferences(getPackageName(), 32768);
        if (localSharedPreferences.getInt(LOCAL_REDALERT_NUM, -1) < 0)
          break label176;
        updateMessageCountInTab(localSharedPreferences.getInt(LOCAL_REDALERT_NUM, -1), "发现");
        break label176;
      }
      updateMessageCountInTab(0, "发现");
      break label176;
      label372: i = 0;
      break label190;
      label377: setRedMarkVisibilityByTab("我的", 0);
      continue;
      label387: setRedMarkVisibilityByTab("我的", 8);
    }
  }

  public boolean checkShouldShowSwitchCityDialog(boolean paramBoolean)
  {
    int m = 0;
    Log.d(LOG_TAG, "checkShouldShowSwitchCityDialog");
    Location localLocation = location();
    if ((localLocation == null) || (locationService().location().getObject("City") == null) || (this.isStoped))
      Log.w(LOG_TAG, "checkShouldShowSwitchCityDialog locate failed");
    int k;
    int i;
    int j;
    while (true)
    {
      return m;
      if (localLocation.city().id() == super.cityId())
      {
        Log.w(LOG_TAG, "checkShouldShowSwitchCityDialog locate city=" + localLocation.city().id() + " and selected city=" + super.cityId() + " are the same");
        return false;
      }
      k = 0;
      i = NovaFragmentTabActivity.preferences().getInt("lastLocatedCityID", -1);
      j = localLocation.city().id();
      if (i != j)
      {
        Log.d(LOG_TAG, "checkShouldShowSwitchCityDialog show dialog because of city ids lastLocatedCityID=" + i + " and currentLocatedCityID=" + j + " are different");
        k = 1;
        m = k;
        if (k == 0)
          continue;
        this.backupSwithCity = localLocation.city();
        if (i == -1)
          break label388;
        m = k;
        if (!paramBoolean)
          break;
        showSwitchCityDialog(localLocation.city(), city());
      }
    }
    label388: for (m = k; ; m = 0)
    {
      if (j != i)
        NovaFragmentTabActivity.preferences().edit().putInt("lastLocatedCityID", j).commit();
      NovaFragmentTabActivity.preferences().edit().putLong("switchCityDialogCheckTime", System.currentTimeMillis()).commit();
      return m;
      long l = NovaFragmentTabActivity.preferences().getLong("switchCityDialogCheckTime", 0L);
      l = System.currentTimeMillis() - l;
      if (l >= 86400000L)
      {
        Log.d(LOG_TAG, "checkShouldShowSwitchCityDialog show dialog because of timeElapse=" + l + " is more than one day time");
        k = 1;
        break;
      }
      Log.w(LOG_TAG, "checkShouldShowSwitchCityDialog not yet");
      break;
    }
  }

  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    this.isSencondBackDown = false;
    return super.dispatchTouchEvent(paramMotionEvent);
  }

  public AssetManager getAssets()
  {
    if (this.assetManager == null)
      return super.getAssets();
    return this.assetManager;
  }

  public int getMailCount()
  {
    SharedPreferences localSharedPreferences = getSharedPreferences(getPackageName(), 32768);
    return localSharedPreferences.getInt("notification_count", 0) + localSharedPreferences.getInt("alert_count", 0) + localSharedPreferences.getInt("subscribe_count", 0);
  }

  public MyResources getOverrideResources()
  {
    return this.myResources;
  }

  public String getPageName()
  {
    return this.gaPageName;
  }

  public Resources getResources()
  {
    if (this.resources == null)
      return super.getResources();
    return this.resources;
  }

  public Resources.Theme getTheme()
  {
    if (this.theme == null)
      return super.getTheme();
    return this.theme;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public boolean isCurTab(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    do
      return false;
    while (!paramString.equalsIgnoreCase(this.mTabHost.getCurrentTabTag()));
    return true;
  }

  public boolean locationCare()
  {
    return true;
  }

  protected boolean needTitleBarShadow()
  {
    return ("闪惠团购".equals(this.mLastTab)) || ("发现".equals(this.mLastTab)) || ("我的".equals(this.mLastTab));
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    RedAlertManager.getInstance().reloadRedAlertInfo();
    HistoryHelper.getInstance().clearIds();
    HistoryHelper.getInstance().flushIds();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 101) && (paramInt2 == -1) && (NovaFragmentTabActivity.preferences().getBoolean("default_home_setting", false)))
      if (!city().isTuan())
        break label60;
    label60: for (paramIntent = "开团"; ; paramIntent = "非开团")
    {
      super.statisticsEvent("tuan5", "tuan5_homepage_success", paramIntent, 0);
      return;
    }
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    dismissDialog();
    this.backupSwithCity = null;
    if (!paramCity2.equals(paramCity1))
    {
      RedAlertManager.getInstance().getRedAlertInfo();
      if (!paramCity2.isTuan())
        break label95;
      this.mTabHost.getTabWidget().getChildTabViewAt(1).setVisibility(0);
    }
    while (true)
    {
      if ((this.switchCityDialog != null) && (this.switchCityDialog.isShowing()))
        this.switchCityDialog.dismiss();
      NovaFragmentTabActivity.preferences().edit().putLong("switchCityDialogCheckTime", System.currentTimeMillis()).commit();
      return;
      label95: this.mTabHost.getTabWidget().getChildTabViewAt(1).setVisibility(8);
    }
  }

  public void onConfigChange(String paramString, Object paramObject1, Object paramObject2)
  {
    if (UpdateManager.instance(this).checkNewVersion())
    {
      this.hasNewUpdate = true;
      return;
    }
    this.hasNewUpdate = false;
  }

  public void onCreate(Bundle paramBundle)
  {
    this.speedMonitorHelper = new SpeedMonitorHelper("launch_main");
    super.onCreate(paramBundle);
    if (this.activityFinished)
      return;
    Object localObject = super.getIntent().getStringExtra("url");
    if (!TextUtils.isEmpty((CharSequence)localObject))
      super.startActivity((String)localObject);
    tuanUrlSchema(super.getIntent());
    if (!checkSignature())
      Process.killProcess(Process.myPid());
    if (preferences().getBoolean("pushLog", true))
    {
      super.statisticsEvent("push5", "pushTimeBegin", "", preferences().getInt("pushBegin", 9));
      super.statisticsEvent("push5", "pushTimeEnd", "", preferences().getInt("pushEnd", 21));
      NovaFragmentTabActivity.preferences().edit().putBoolean("pushLog", false).commit();
    }
    this.mPreferences = getSharedPreferences(getPackageName(), 0);
    this.mPreferences.registerOnSharedPreferenceChangeListener(this);
    boolean bool;
    if (System.currentTimeMillis() - lastNetworkUnaMills > 120000L)
    {
      localObject = ((ConnectivityManager)super.getSystemService("connectivity")).getActiveNetworkInfo();
      if (localObject != null)
        break label801;
      bool = false;
      if (!bool)
        Toast.makeText(this, "目前网络连接不可用。", 0).show();
    }
    if (paramBundle != null)
    {
      this.mLastTab = paramBundle.getString("mLastTab");
      this.isSearchFragmentExist = paramBundle.getBoolean("isSearchFragmentExist");
    }
    super.setTabWidgetBackground(R.drawable.tab_bar_bg);
    SkinManager.getInstance().refresh(ConfigHelper.appSkinConfig);
    super.addTab("首页", R.layout.tab_indicator_home, MainHomeFragment.class, null);
    super.addTab("闪惠团购", R.layout.tab_indicator_tuan, TuanHomeFragment.class, null);
    super.addTab("发现", R.layout.tab_indicator_search, MainFindFragment.class, null);
    super.addTab("我的", R.layout.tab_indicator_my, UserFragment.class, null);
    this.GAFlag = true;
    if (super.getIntent().getBooleanExtra("fromWX", false))
    {
      ((NovaApplication)super.getApplication()).setStartType(1);
      paramBundle = super.getIntent().getExtras();
      ((NovaApplication)super.getApplication()).setWXBundle(paramBundle);
      label378: paramBundle = super.getIntent().getData();
      if (paramBundle != null)
      {
        this.host = paramBundle.getHost();
        this.gaPageName = this.host;
      }
      super.cityConfig().addListener(this);
      if (!super.cityConfig().currentCity().isTuan())
        this.mTabHost.getTabWidget().getChildTabViewAt(1).setVisibility(8);
      if ((!UpdateManager.instance(this).checkNewVersion()) || (checkShouldShowSwitchCityDialog(false)))
        break label857;
      this.hasNewUpdate = true;
      long l = NovaFragmentTabActivity.preferences().getLong("nextUpdateNotifyTime", 0L);
      Log.d("MainActivity", "notifyTime=" + l + " currentTime=" + DateUtil.currentTimeMillis());
      if (l >= DateUtil.currentTimeMillis())
        break label824;
      NovaFragmentTabActivity.preferences().edit().putLong("nextUpdateNotifyTime", DateUtil.getNextDayTimeMillis()).commit();
      UpdateUIManager.showDialog(this);
      label554: if ((NetworkUtils.isWIFIConnection(this)) && (NovaFragmentTabActivity.preferences().getBoolean("autodownload", true)))
        UpdateManager.instance(this).startSilentDownload();
    }
    while (true)
    {
      updateVersionCode();
      super.configService().addListener("versionCode", this);
      paramBundle = new IntentFilter();
      paramBundle.addAction("com.dianping.action.UPLOAD_PHOTO_STATUS_CHANGED");
      paramBundle.addAction("com.dianping.action.SPLASH_PIC_UPDATE");
      paramBundle.addAction("com.dianping.action.RedAlerts");
      paramBundle.addAction("com.dianping.action.SHOW_TUAN_TAB_RED_MARK");
      paramBundle.addAction("com.dianping.action.ShowPushDialog");
      paramBundle.addAction("com.dianping.action.NEW_MESSAGE");
      registerReceiver(this.receiver, paramBundle);
      super.accountService().addListener(this);
      checkNewVersionMark();
      CssStyleManager.instance(this).syncServerCssFile();
      if (!NovaFragmentTabActivity.preferences().getBoolean("shortcutinstalled", false))
      {
        NovaFragmentTabActivity.preferences().edit().putBoolean("shortcutinstalled", true).commit();
        if (!ShortcutUtils.hasShortcut(this))
        {
          if ((Build.VERSION.SDK_INT >= 16) && (Build.VERSION.SDK_INT <= 18))
            ShortcutUtils.removeShortcut(this, R.string.app_name);
          ShortcutUtils.createShortcut(this, R.drawable.icon, R.string.app_name);
        }
      }
      SkinManager.setTabHostSkin(this, this.mTabHost);
      if (Environment.isDebug())
        startService(new Intent(this, DebugWindowService.class));
      this.speedMonitorHelper.setResponseTime(1, System.currentTimeMillis());
      return;
      label801: bool = ((NetworkInfo)localObject).isConnectedOrConnecting();
      break;
      ((NovaApplication)super.getApplication()).setStartType(0);
      break label378;
      label824: if ((!ConfigHelper.forceUpdate) || ((NetworkUtils.getNetworkType(this) != "WIFI") && (NetworkUtils.getNetworkType(this) != "4G")))
        break label554;
      UpdateUIManager.showDialog(this);
      break label554;
      label857: this.hasNewUpdate = false;
    }
  }

  protected Dialog onCreateDialog(int paramInt)
  {
    Dialog localDialog = UpdateUIManager.buildDialog(this, paramInt);
    if (localDialog != null)
      return localDialog;
    return super.onCreateDialog(paramInt);
  }

  protected void onDestroy()
  {
    super.configService().removeListener("versionCode", this);
    super.cityConfig().removeListener(this);
    super.accountService().removeListener(this);
    SkinManager.getInstance().cancleTask();
    try
    {
      unregisterReceiver(this.receiver);
      if (super.isFinishing())
        lastNetworkUnaMills = 0L;
      if (this.mPreferences != null)
        this.mPreferences.unregisterOnSharedPreferenceChangeListener(this);
      super.onDestroy();
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      if (this.isSearchFragmentExist)
        try
        {
          super.onKeyDown(paramInt, paramKeyEvent);
          this.isSearchFragmentExist = false;
          return true;
        }
        catch (Exception paramKeyEvent)
        {
          return true;
        }
      if (((NovaApplication)super.getApplication()).getStartType() == 1)
      {
        super.finish();
        return false;
      }
      if (this.isSencondBackDown)
        super.finish();
    }
    while (true)
    {
      return false;
      this.isSencondBackDown = true;
      Toast.makeText(this, "再按一次退出程序", 0).show();
      this.mainActHandler.removeMessages(1);
      this.mainActHandler.sendEmptyMessageDelayed(1, 2000L);
      continue;
      this.isSencondBackDown = false;
    }
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    Uri localUri = paramIntent.getData();
    if (localUri != null)
    {
      this.host = localUri.getHost();
      this.gaPageName = this.host;
      if ("main".equals(this.host))
        super.statisticsEvent(localUri.getQueryParameter("category"), localUri.getQueryParameter("action"), this.mTabHost.getCurrentTabTag(), 0);
      tuanUrlSchema(paramIntent);
      selectTabByHost(this.host);
    }
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  protected void onResume()
  {
    super.onResume();
    checkRedAlert();
    PurchaseResultHelper.instance().setIntentAfterBuy(new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuanmain")));
    if (this.host != null)
    {
      if (!selectTabByHost(this.host))
        this.mTabHost.setCurrentTabByTag("首页");
      this.host = null;
    }
    if (this.speedMonitorHelper != null)
    {
      this.speedMonitorHelper.setResponseTime(2, System.currentTimeMillis());
      this.speedMonitorHelper.sendReport();
      this.speedMonitorHelper = null;
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("mLastTab", this.mLastTab);
    paramBundle.putBoolean("isSearchFragmentExist", this.isSearchFragmentExist);
    paramBundle.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
    super.onSaveInstanceState(paramBundle);
  }

  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    if (!paramString.equals("hasNewDraft"));
    while (true)
    {
      return;
      paramSharedPreferences = super.getSupportFragmentManager().findFragmentByTag("我的");
      if ((paramSharedPreferences instanceof UserFragment));
      for (paramSharedPreferences = (UserFragment)paramSharedPreferences; paramSharedPreferences != null; paramSharedPreferences = null)
      {
        paramSharedPreferences.updateDraftRedMark();
        return;
      }
    }
  }

  protected void onStart()
  {
    super.onStart();
    this.isStoped = false;
  }

  protected void onStop()
  {
    super.onStop();
    this.isStoped = true;
  }

  public void onTabChanged(String paramString)
  {
    super.onTabChanged(paramString);
    Object localObject;
    if (("闪惠团购".equals(paramString)) || ("发现".equals(paramString)) || ("我的".equals(paramString)))
    {
      super.addTitleBarShadow();
      this.mLastTab = paramString;
      if ("我的".equals(paramString))
      {
        setRedMarkVisibilityByTab("我的", 8);
        if (RedAlertManager.getInstance().checkRedAlertByTag("me.mypage") != null)
          RedAlertManager.getInstance().updateRedAlert("me.mypage");
        if (this.hasNewUpdate)
        {
          localObject = super.getSharedPreferences(getPackageName(), 0).edit();
          ((SharedPreferences.Editor)localObject).putBoolean("show_new_version_red_mark", false);
          ((SharedPreferences.Editor)localObject).commit();
        }
      }
      if ("发现".equals(paramString))
      {
        if (this.isFindTabFirstShow)
        {
          this.isFindTabFirstShow = false;
          this.mPreferences.edit().putBoolean("find_tab_is_first_show", false).apply();
        }
        if (!this.isFirstTimeGetIntoFindTab)
          break label315;
        this.isFirstTimeGetIntoFindTab = false;
        label173: RedAlertManager.getInstance().getRedAlertInfo();
        if (RedAlertManager.getInstance().checkRedAlertByTag("find.page") != null)
          RedAlertManager.getInstance().updateRedAlert("find.page");
        setRedMarkVisibilityByTab("发现", 8);
      }
      if ("闪惠团购".equals(paramString))
      {
        localObject = super.getSupportFragmentManager().findFragmentByTag("闪惠团购");
        if (!(localObject instanceof TuanHomeFragment))
          break label332;
        localObject = (TuanHomeFragment)localObject;
        label239: if (localObject != null)
        {
          ((TuanHomeFragment)localObject).setFilterSelectedNavs(this.category, this.region, this.sort, this.screening);
          this.category = null;
          this.region = null;
          this.sort = null;
          this.screening = null;
        }
      }
      if (!this.GAFlag)
        break label337;
      super.statisticsEvent("index5", "index5_tab", paramString, 0);
    }
    while (true)
    {
      setGaPageNameByTitle(paramString);
      return;
      super.removeTitleBarShadow();
      break;
      label315: sendBroadcast(new Intent("com.dianping.action.FindFriendsGoWhereAgent_SEND_REQUEST"));
      break label173;
      label332: localObject = null;
      break label239;
      label337: super.statisticsEvent("index5", "index5_tab", "自动加载" + paramString, 0);
    }
  }

  public void registerSearchFragment()
  {
    this.isSearchFragmentExist = true;
  }

  public void setGaPageNameByTitle(String paramString)
  {
    if ("首页".equals(paramString))
      this.gaPageName = "home";
    do
    {
      return;
      if ("闪惠团购".equals(paramString))
      {
        this.gaPageName = "tuanmain";
        return;
      }
      if (!"发现".equals(paramString))
        continue;
      this.gaPageName = "find";
      return;
    }
    while (!"我的".equals(paramString));
    this.gaPageName = "me";
  }

  protected void setOnContentView()
  {
    super.setContentView(R.layout.fragment_tabs_bottom);
  }

  public void setOverrideResources(MyResources paramMyResources)
  {
    if (paramMyResources == null)
    {
      this.myResources = null;
      this.resources = null;
      this.assetManager = null;
      this.theme = null;
      return;
    }
    this.myResources = paramMyResources;
    this.resources = paramMyResources.getResources();
    this.assetManager = paramMyResources.getAssets();
    paramMyResources = paramMyResources.getResources().newTheme();
    paramMyResources.setTo(getTheme());
    this.theme = paramMyResources;
  }

  public void setRedMarkVisibilityByTab(String paramString, int paramInt)
  {
    if ((isCurTab(paramString)) && (paramInt == 0))
      return;
    int i = 0;
    if ("发现".equalsIgnoreCase(paramString))
      i = this.mTabHost.getTabWidget().getChildCount() - 2;
    while (true)
    {
      paramString = this.mTabHost.getTabWidget().getChildAt(i);
      if (paramString == null)
        break;
      paramString = paramString.findViewById(R.id.ic_new);
      if (paramString == null)
        break;
      paramString.setVisibility(paramInt);
      return;
      if (!"我的".equalsIgnoreCase(paramString))
        continue;
      i = this.mTabHost.getTabWidget().getChildCount() - 1;
    }
  }

  protected void showGAViewOnResume(String paramString)
  {
  }

  public void showSwitchCityDialog(City paramCity1, City paramCity2)
  {
    if (paramCity2.id() <= 0);
    do
      return;
    while (paramCity1.equals(paramCity2));
    String str = "定位显示您在" + paramCity1.name() + "，您可以...";
    this.switchCityDialog = new DoubleLineDialog(this);
    this.switchCityDialog.show();
    this.switchCityDialog.setContent(str).setPositiveButton("切换到" + paramCity1.name(), new DialogInterface.OnClickListener(paramCity1)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MainActivity.this.statisticsEvent("index5", "index5_citypop", "切换", 0);
        MainActivity.this.cityConfig().switchCity(this.val$locatedCity);
        MainActivity.this.showProgressDialog("正在切换城市请稍候...");
      }
    }).setNegativeButton("继续浏览" + paramCity2.name(), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MainActivity.this.statisticsEvent("index5", "index5_citypop", "取消", 0);
        MainActivity.this.switchCityDialog = null;
      }
    }).setTips("定位错了，我不在" + paramCity1.name(), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MainActivity.this.statisticsEvent("index5", "index5_citypop", "报错", 0);
        paramDialogInterface = new Intent("android.intent.action.VIEW", Uri.parse("dianping://locatecityfeedback"));
        MainActivity.this.startActivity(paramDialogInterface);
        MainActivity.this.switchCityDialog = null;
      }
    }).setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        MainActivity.this.statisticsEvent("index5", "index5_citypop", "取消", 0);
        MainActivity.this.switchCityDialog = null;
      }
    });
  }

  public void unregisterSearchFragment()
  {
    this.isSearchFragmentExist = false;
  }

  public void updateMessageCountInTab(int paramInt, String paramString)
  {
    if (this.mTabHost == null)
      return;
    if (paramString == "我的");
    FrameLayout.LayoutParams localLayoutParams;
    for (MeasuredTextView localMeasuredTextView = (MeasuredTextView)this.mTabHost.findViewById(R.id.tv_indicator_msg_count); ; localMeasuredTextView = (MeasuredTextView)this.mTabHost.findViewById(R.id.tv_indicator_search_msg_count))
    {
      localLayoutParams = (FrameLayout.LayoutParams)localMeasuredTextView.getLayoutParams();
      if (paramInt > 0)
        break;
      localMeasuredTextView.setVisibility(8);
      return;
    }
    localMeasuredTextView.setVisibility(0);
    String str;
    if (paramString == "发现")
      if (paramInt > 9)
      {
        str = "9";
        localMeasuredTextView.setText(str);
        if (paramString != "我的")
          break label234;
        setRedMarkVisibilityByTab("我的", 8);
      }
    while (true)
    {
      if ((paramInt >= 10) && (paramString != "发现"))
        break label245;
      localMeasuredTextView.setFlag(true);
      localLayoutParams.topMargin = ViewUtils.dip2px(this, 5.0F);
      localLayoutParams.leftMargin = ViewUtils.dip2px(this, 16.0F);
      localMeasuredTextView.setPadding(0, 0, 0, 0);
      localMeasuredTextView.setBackgroundResource(R.drawable.home_navibar_tips_red_b);
      return;
      str = paramInt + "";
      break;
      if (paramInt > 99);
      for (str = "99"; ; str = paramInt + "")
      {
        localMeasuredTextView.setText(str);
        break;
      }
      label234: setRedMarkVisibilityByTab("发现", 8);
    }
    label245: localLayoutParams.topMargin = ViewUtils.dip2px(this, 1.0F);
    localLayoutParams.leftMargin = ViewUtils.dip2px(this, 19.0F);
    paramInt = ViewUtils.dip2px(this, 5.0F);
    localMeasuredTextView.setPadding(paramInt, paramInt, paramInt, paramInt);
    localMeasuredTextView.setBackgroundResource(R.drawable.home_navibar_tips_reddigit);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.guide.MainActivity
 * JD-Core Version:    0.6.0
 */