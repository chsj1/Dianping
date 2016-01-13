package com.dianping.debug;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.MApiDebugAgent;
import com.dianping.moduleconfig.AgentHelper;
import com.dianping.util.CrashReportHelper;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class DebugPanelActivity extends NovaActivity
  implements View.OnClickListener
{
  public static final String ADCLIENT_ENVIRONMENT_MOCK_APP = "app.dp:8888";
  public static final String ADCLIENT_ENVIRONMENT_MOCK_WENDONG = "wendong.dp:8888";
  static final String BEAUTY_DOMAIN = "http://beauty.api.51ping.com/";
  static final String BEAUTY_DOMAIN_PPE = "http://ppe.beauty.api.dianping.com/";
  static final String BOOKING_DOMIN = "http://rs.api.51ping.com/";
  static final String BOOKING_DOMIN_PPE = "http://ppe.rs.api.dianping.com/";
  static final String DIANPIN_DOMIN = "http://m.api.51ping.com/";
  static final String DIANPIN_DOMIN_PPE = "http://ppe.m.api.dianping.com/";
  static final String HUIHUI_DOMIN = "http://hui.api.51ping.com/";
  static final String HUIHUI_DOMIN_PPE = "http://ppe.hui.api.dianping.com/";
  static final String LOCATION_DOMAIN = "http://l.api.51ping.com/";
  static final String LOCATION_DOMAIN_PPE = "http://ppe.l.api.dianping.com/";
  static final String MAPI_DOMAIN = "http://mapi.51ping.com/";
  static final String MAPI_DOMAIN_PPE = "http://ppe.mapi.dianping.com";
  static final String MEMBERCARD_DOMIN = "http://mc.api.51ping.com/";
  static final String MEMBERCARD_DOMIN_PPE = "http://ppe.mc.api.dianping.com/";
  static final String[] MOCK_LIST = { "wendong.dp:8888", "app.dp:8888" };
  static final String MOVIE_DOMAIN = "http://app.movie.51ping.com/";
  static final String MOVIE_DOMAIN_PPE = "http://ppe.app.movie.dianping.com/";
  static final String NEWGA_DOMAIN = "http://m.api.51ping.com/";
  static final String NEWGA_DOMAIN_PPE = "http://ppe.m.api.dianping.com/";
  static final String PAY_DOMIN = "http://api.p.51ping.com/";
  static final String PAY_DOMIN_PPE = "http://ppe.api.p.dianping.com/";
  static final String TAKEAWAY_DOMIN = "http://waimai.api.51ping.com/";
  static final String TAKEAWAY_DOMIN_PPE = "http://ppe.waimai.api.dianping.com/";
  static final String TUAN_DOMIN = "http://app.t.51ping.com/";
  static final String TUAN_DOMIN_PPE = "http://ppe.app.t.dianping.com/";
  public static final String WEBVIEW_JS_SETTING = "webview_jsbridge_settings";
  MApiDebugAgent debugAgent;

  private void copy(String paramString)
  {
    if (Build.VERSION.SDK_INT < 11)
    {
      ((android.text.ClipboardManager)getSystemService("clipboard")).setText(paramString);
      return;
    }
    ((android.content.ClipboardManager)getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(null, paramString));
  }

  private Intent getSingleIntent(String paramString)
  {
    paramString = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
    paramString.setFlags(67108864);
    return paramString;
  }

  private void sendCrashReport(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("message/rfc822");
    localIntent.putExtra("android.intent.extra.EMAIL", new String[] { "yimin.tu@dianping.com" });
    localIntent.putExtra("android.intent.extra.SUBJECT", "Android Crash Report");
    localIntent.putExtra("android.intent.extra.TEXT", paramString);
    startActivity(Intent.createChooser(localIntent, "Select email application."));
  }

  private void sendShopConfigDetail(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("message/rfc822");
    localIntent.putExtra("android.intent.extra.EMAIL", new String[] { "shop-mobile@dianping.com" });
    localIntent.putExtra("android.intent.extra.SUBJECT", "Android Shop Config Report");
    localIntent.putExtra("android.intent.extra.TEXT", paramString);
    startActivity(Intent.createChooser(localIntent, "Select email application."));
  }

  private void showCrashReport(String paramString)
  {
    showSimpleAlertDialog("crash报告", paramString, "复制", new DialogInterface.OnClickListener(paramString)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        DebugPanelActivity.this.copy(this.val$report);
      }
    }
    , "取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
      }
    });
  }

  private void showShopConfigDetail(String paramString)
  {
    Intent localIntent = new Intent(this, DebugShopConfigActivity.class);
    localIntent.putExtra("shop_config", paramString);
    startActivity(localIntent);
  }

  protected boolean isNeedCity()
  {
    return false;
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.domain_selector_item)
      startActivity(new Intent(this, DebugDomainSelectActivity.class));
    do
    {
      while (true)
      {
        return;
        if (paramView.getId() == R.id.entranceip_selector_item)
        {
          startActivity(new Intent(this, DebugEntranceIpSelectActivity.class));
          return;
        }
        if (paramView.getId() == R.id.clear_mapi_cache)
        {
          mapiCacheService().clear();
          showToast("MAPI 缓存删除成功");
          return;
        }
        if (paramView.getId() == R.id.statistics_item)
        {
          startActivity(new Intent(this, DebugStatisticsActivity.class));
          return;
        }
        if (paramView.getId() == R.id.detail_info_item)
        {
          startActivity(new Intent(this, DebugDetailInfoActivity.class));
          return;
        }
        if (paramView.getId() == R.id.guidance_reset_item)
        {
          startActivity(new Intent(this, DebugGuidanceAndResetActivity.class));
          return;
        }
        if (paramView.getId() == R.id.debug_off)
        {
          stopService(new Intent(this, DebugWindowService.class));
          com.dianping.util.Log.LEVEL = 2147483647;
          finish();
          return;
        }
        Object localObject;
        if (paramView.getId() == R.id.debug_agentconfig)
        {
          localObject = AgentHelper.getInstance().loadFromCacheFile();
          if (com.dianping.util.TextUtils.isEmpty((CharSequence)localObject))
          {
            Toast.makeText(this, "没有缓存文件", 0).show();
            return;
          }
          paramView = new AlertDialog.Builder(this);
          localObject = new DialogInterface.OnClickListener((String)localObject)
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              if (paramInt == 0)
              {
                DebugPanelActivity.this.showShopConfigDetail(this.val$config);
                return;
              }
              DebugPanelActivity.this.sendShopConfigDetail(this.val$config);
            }
          };
          paramView.setItems(new String[] { "查看复制config信息", "发送config报告" }, (DialogInterface.OnClickListener)localObject).show();
          return;
        }
        if (paramView.getId() == R.id.debug_force_network_error)
        {
          this.debugAgent.addNextFail(10);
          return;
        }
        if (paramView.getId() == R.id.debug_send_crash)
        {
          localObject = CrashReportHelper.getReportBak();
          if (localObject == null)
          {
            Toast.makeText(this, "没有崩溃报告", 0).show();
            return;
          }
          paramView = new AlertDialog.Builder(this);
          localObject = new DialogInterface.OnClickListener((String)localObject)
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              if (paramInt == 0)
              {
                DebugPanelActivity.this.showCrashReport(this.val$report);
                return;
              }
              DebugPanelActivity.this.sendCrashReport(this.val$report);
            }
          };
          paramView.setItems(new String[] { "查看crash报告", "发送crash报告" }, (DialogInterface.OnClickListener)localObject).show();
          return;
        }
        if (paramView.getId() == R.id.debug_send_dpid)
        {
          paramView = preferences().getString("dpid", "");
          if (android.text.TextUtils.isEmpty(paramView))
          {
            Toast.makeText(this, "没有 dpid", 0).show();
            return;
          }
          localObject = new Intent("android.intent.action.SEND");
          ((Intent)localObject).setType("message/rfc822");
          ((Intent)localObject).putExtra("android.intent.extra.EMAIL", new String[] { "jie.li@dianping.com" });
          ((Intent)localObject).putExtra("android.intent.extra.SUBJECT", "dpid");
          ((Intent)localObject).putExtra("android.intent.extra.TEXT", paramView);
          startActivity(Intent.createChooser((Intent)localObject, "Select email application."));
          return;
        }
        if (paramView.getId() == R.id.btn_open_url)
        {
          paramView = ((EditText)findViewById(R.id.text_url)).getText().toString();
          if (android.text.TextUtils.isEmpty(paramView))
            continue;
          try
          {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView)));
            return;
          }
          catch (java.lang.Exception paramView)
          {
            return;
          }
        }
        if (paramView.getId() != R.id.btn_open_scheme)
          break;
        if (android.text.TextUtils.isEmpty("http://m.dper.com/models/listschemes"))
          continue;
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://complexweb").buildUpon().appendQueryParameter("url", "http://m.dper.com/models/listschemes").build()));
        return;
      }
      if (paramView.getId() == R.id.gogo_51ping)
      {
        if ((getApplicationInfo().flags & 0x2) != 0)
          getSharedPreferences("environment", 0).edit().putString("net", "beta").commit();
        this.debugAgent.setSwitchDomain("http://m.api.51ping.com/");
        this.debugAgent.setMapiDomain("http://mapi.51ping.com/");
        this.debugAgent.setBookingDebugDomain("http://rs.api.51ping.com/");
        this.debugAgent.setTDebugDomain("http://app.t.51ping.com/");
        this.debugAgent.setPayDebugDomain("http://api.p.51ping.com/");
        this.debugAgent.setMovieDebugDomain("http://app.movie.51ping.com/");
        this.debugAgent.setMembercardDebugDomain("http://mc.api.51ping.com/");
        this.debugAgent.setTakeawayDebugDomain("http://waimai.api.51ping.com/");
        this.debugAgent.setHuihuiDebugDomain("http://hui.api.51ping.com/");
        this.debugAgent.setBeautyDebugDomain("http://beauty.api.51ping.com/");
        this.debugAgent.setConfigDebugDomain("http://m.api.51ping.com/");
        this.debugAgent.setLocateDebugDomain("http://l.api.51ping.com/");
        getSharedPreferences("com.dianping.mapidebugagent", 0).edit().putString("web_url_from_string_dianping", "http://m.dianping.com/").putString("web_url_to_string_dianping", "http://m.51ping.com/").commit();
        accountService().logout();
        startActivity(getSingleIntent("dianping://home"));
        finish();
        return;
      }
      if (paramView.getId() == R.id.gogo_ppe)
      {
        if ((getApplicationInfo().flags & 0x2) != 0)
          getSharedPreferences("environment", 0).edit().putString("net", "ppe").commit();
        this.debugAgent.setSwitchDomain("http://ppe.m.api.dianping.com/");
        this.debugAgent.setMapiDomain("http://ppe.mapi.dianping.com");
        this.debugAgent.setBookingDebugDomain("http://ppe.rs.api.dianping.com/");
        this.debugAgent.setTDebugDomain("http://ppe.app.t.dianping.com/");
        this.debugAgent.setPayDebugDomain("http://ppe.api.p.dianping.com/");
        this.debugAgent.setMovieDebugDomain("http://ppe.app.movie.dianping.com/");
        this.debugAgent.setMembercardDebugDomain("http://ppe.mc.api.dianping.com/");
        this.debugAgent.setTakeawayDebugDomain("http://ppe.waimai.api.dianping.com/");
        this.debugAgent.setHuihuiDebugDomain("http://ppe.hui.api.dianping.com/");
        this.debugAgent.setBeautyDebugDomain("http://ppe.beauty.api.dianping.com/");
        this.debugAgent.setConfigDebugDomain("http://ppe.m.api.dianping.com/");
        this.debugAgent.setLocateDebugDomain("http://ppe.l.api.dianping.com/");
        accountService().logout();
        startActivity(getSingleIntent("dianping://home"));
        finish();
        return;
      }
      if (paramView.getId() == R.id.gogo_dianping)
      {
        getSharedPreferences("environment", 0).edit().putString("net", "product").commit();
        this.debugAgent.setSwitchDomain(null);
        this.debugAgent.setMapiDomain(null);
        this.debugAgent.setBookingDebugDomain(null);
        this.debugAgent.setTDebugDomain(null);
        this.debugAgent.setPayDebugDomain(null);
        this.debugAgent.setMovieDebugDomain(null);
        this.debugAgent.setMembercardDebugDomain(null);
        this.debugAgent.setTakeawayDebugDomain(null);
        this.debugAgent.setHuihuiDebugDomain(null);
        this.debugAgent.setBeautyDebugDomain(null);
        this.debugAgent.setConfigDebugDomain(null);
        getSharedPreferences("com.dianping.mapidebugagent", 0).edit().putString("web_url_from_string_dianping", null).putString("web_url_to_string_dianping", null).commit();
        accountService().logout();
        startActivity(getSingleIntent("dianping://home"));
        finish();
        return;
      }
      if (paramView.getId() == R.id.gogo_mock)
      {
        paramView = new AlertDialog.Builder(this);
        paramView.setItems(MOCK_LIST, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            paramDialogInterface = "http://" + DebugPanelActivity.MOCK_LIST[paramInt] + "/";
            DebugPanelActivity.this.getSharedPreferences("environment", 0).edit().putString("net", DebugPanelActivity.MOCK_LIST[paramInt]).commit();
            DebugPanelActivity.this.debugAgent.setSwitchDomain(paramDialogInterface);
            DebugPanelActivity.this.debugAgent.setMapiDomain(paramDialogInterface);
            DebugPanelActivity.this.debugAgent.setBookingDebugDomain(paramDialogInterface);
            DebugPanelActivity.this.debugAgent.setTDebugDomain(paramDialogInterface);
            DebugPanelActivity.this.debugAgent.setPayDebugDomain(paramDialogInterface);
            DebugPanelActivity.this.debugAgent.setMovieDebugDomain(paramDialogInterface);
            DebugPanelActivity.this.debugAgent.setMembercardDebugDomain(paramDialogInterface);
            DebugPanelActivity.this.debugAgent.setTakeawayDebugDomain(paramDialogInterface);
            DebugPanelActivity.this.debugAgent.setHuihuiDebugDomain(paramDialogInterface);
            DebugPanelActivity.this.debugAgent.setBeautyDebugDomain(paramDialogInterface);
            DebugPanelActivity.this.debugAgent.setConfigDebugDomain(paramDialogInterface);
            DebugPanelActivity.this.accountService().logout();
            DebugPanelActivity.this.startActivity(DebugPanelActivity.this.getSingleIntent("dianping://home"));
            DebugPanelActivity.this.finish();
          }
        });
        paramView.show();
        return;
      }
      if (paramView.getId() == R.id.debug_showcachefilesize)
      {
        startActivity(new Intent(this, DebugExploreCacheFileActivity.class));
        return;
      }
      if (paramView.getId() != R.id.webview_debug)
        continue;
      startActivity(new Intent(this, DebugWebViewDevActivity.class));
      finish();
      return;
    }
    while (paramView.getId() != R.id.permissiontest);
    startActivity(new Intent(this, PermissionTestActivity.class));
  }

  @TargetApi(11)
  public void onCreate(Bundle paramBundle)
  {
    boolean bool2 = true;
    super.onCreate(paramBundle);
    this.debugAgent = ((MApiDebugAgent)getService("mapi_debug"));
    super.setContentView(R.layout.debug_panel);
    if (!Environment.isDebug())
    {
      finish();
      return;
    }
    if (getIntent().getBooleanExtra("resetProcess", false))
    {
      Process.killProcess(Process.myPid());
      return;
    }
    stopService(new Intent(this, DebugWindowService.class));
    findViewById(R.id.gogo_ppe).setOnClickListener(this);
    findViewById(R.id.gogo_dianping).setOnClickListener(this);
    findViewById(R.id.gogo_51ping).setOnClickListener(this);
    findViewById(R.id.gogo_mock).setOnClickListener(this);
    findViewById(R.id.debug_off).setOnClickListener(this);
    findViewById(R.id.debug_agentconfig).setOnClickListener(this);
    findViewById(R.id.debug_force_network_error).setOnClickListener(this);
    findViewById(R.id.debug_send_crash).setOnClickListener(this);
    findViewById(R.id.debug_send_dpid).setOnClickListener(this);
    paramBundle = getSharedPreferences("com.dianping.mapidebugagent", 0);
    ((CheckBox)findViewById(R.id.tunnel_debug)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
      {
        paramCompoundButton = DebugPanelActivity.this.findViewById(R.id.tunnel_debug_frame);
        if (paramBoolean);
        for (int i = 0; ; i = 8)
        {
          paramCompoundButton.setVisibility(i);
          return;
        }
      }
    });
    ((CheckBox)findViewById(R.id.tunnel_debug)).setChecked(paramBundle.getBoolean("tunnelDebug", false));
    ((CheckBox)findViewById(R.id.tunnel_log)).setChecked(paramBundle.getBoolean("tunnelLog", false));
    ((CheckBox)findViewById(R.id.tunnel_config)).setChecked(paramBundle.getBoolean("tunnelConfig", false));
    ((CheckBox)findViewById(R.id.tunnel_enabled)).setChecked(paramBundle.getBoolean("tunnelEnabled", false));
    ((CheckBox)findViewById(R.id.utn_enabled)).setChecked(paramBundle.getBoolean("utnEnabled", false));
    CheckBox localCheckBox = (CheckBox)findViewById(R.id.http_enabled);
    if (!paramBundle.getBoolean("httpDisabled", false))
    {
      bool1 = true;
      localCheckBox.setChecked(bool1);
      ((CheckBox)findViewById(R.id.wns_enabled)).setChecked(paramBundle.getBoolean("wnsEnabled", false));
      paramBundle = (ToggleButton)findViewById(R.id.debug_network_delay);
      if (this.debugAgent.delay() <= 0L)
        break label557;
    }
    label557: for (boolean bool1 = bool2; ; bool1 = false)
    {
      paramBundle.setChecked(bool1);
      ((ToggleButton)findViewById(R.id.debug_network_error)).setChecked(this.debugAgent.failHalf());
      findViewById(R.id.domain_selector_item).setOnClickListener(this);
      findViewById(R.id.entranceip_selector_item).setOnClickListener(this);
      findViewById(R.id.clear_mapi_cache).setOnClickListener(this);
      findViewById(R.id.statistics_item).setOnClickListener(this);
      findViewById(R.id.detail_info_item).setOnClickListener(this);
      findViewById(R.id.guidance_reset_item).setOnClickListener(this);
      findViewById(R.id.btn_open_url).setOnClickListener(this);
      findViewById(R.id.btn_open_scheme).setOnClickListener(this);
      findViewById(R.id.webview_debug).setOnClickListener(this);
      findViewById(R.id.permissiontest).setOnClickListener(this);
      findViewById(R.id.debug_showcachefilesize).setOnClickListener(this);
      return;
      bool1 = false;
      break;
    }
  }

  @TargetApi(11)
  protected void onDestroy()
  {
    super.onDestroy();
    if (Environment.isDebug())
      startService(new Intent(DPApplication.instance(), DebugWindowService.class));
    Object localObject = this.debugAgent;
    long l;
    if (((ToggleButton)findViewById(R.id.debug_network_delay)).isChecked())
    {
      l = 5000L;
      ((MApiDebugAgent)localObject).setDelay(l);
      this.debugAgent.setFailHalf(((ToggleButton)findViewById(R.id.debug_network_error)).isChecked());
      localObject = getSharedPreferences("com.dianping.mapidebugagent", 0).edit().putString("proxy", this.debugAgent.proxy()).putInt("proxyPort", this.debugAgent.proxyPort()).putBoolean("tunnelDebug", ((CheckBox)findViewById(R.id.tunnel_debug)).isChecked()).putBoolean("tunnelLog", ((CheckBox)findViewById(R.id.tunnel_log)).isChecked()).putBoolean("tunnelConfig", ((CheckBox)findViewById(R.id.tunnel_config)).isChecked()).putBoolean("tunnelEnabled", ((CheckBox)findViewById(R.id.tunnel_enabled)).isChecked()).putBoolean("utnEnabled", ((CheckBox)findViewById(R.id.utn_enabled)).isChecked());
      if (((CheckBox)findViewById(R.id.http_enabled)).isChecked())
        break label485;
    }
    label485: for (boolean bool = true; ; bool = false)
    {
      ((SharedPreferences.Editor)localObject).putBoolean("httpDisabled", bool).putBoolean("wnsEnabled", ((CheckBox)findViewById(R.id.wns_enabled)).isChecked()).putString("setSwitchDomain", this.debugAgent.switchDomain()).putString("setMapiDomain", this.debugAgent.mapiDomain()).putString("setBookingDebugDomain", this.debugAgent.bookingDebugDomain()).putString("setTDebugDomain", this.debugAgent.tDebugDomain()).putString("setPayDebugDomain", this.debugAgent.payDebugDomain()).putString("setMovieDebugDomain", this.debugAgent.movieDebugDomain()).putString("setMembercardDebugDomain", this.debugAgent.membercardDebugDomain()).putString("setTakeawayDebugDomain", this.debugAgent.takeawayDebugDomain()).putString("setHuihuiDebugDomain", this.debugAgent.huihuiDebugDomain()).putString("setBeautyDebugDomain", this.debugAgent.beautyDebugDomain()).putString("setLocateDebugDomain", this.debugAgent.locateDebugDomain()).commit();
      return;
      l = 0L;
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugPanelActivity
 * JD-Core Version:    0.6.0
 */