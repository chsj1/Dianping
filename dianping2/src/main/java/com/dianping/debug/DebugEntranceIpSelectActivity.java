package com.dianping.debug;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.push.pushservice.dp.DPPushService;
import com.dianping.dataservice.mapi.MApiDebugAgent;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.KeyboardUtils.SoftKeyboardController;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class DebugEntranceIpSelectActivity extends NovaActivity
  implements View.OnClickListener
{
  private static final String BETASPLASHIP = "180.153.132.55";
  private static final String BETASPLASHURL = "http://180.153.132.55/sc/splash/${network_type}/beta/${req_time}(${width}X${height}).jpg";
  MApiDebugAgent debugAgent;

  private void checkPushService()
  {
    ((ToggleButton)findViewById(R.id.dianping_push)).setChecked(DPPushService.isStarted());
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.dianping_push)
    {
      DPPushService.start(this, "debug");
      checkPushService();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.debug_panel_entranceip_select);
    this.debugAgent = ((MApiDebugAgent)getService("mapi_debug"));
    DebugDomainItem localDebugDomainItem = (DebugDomainItem)findViewById(R.id.splash_item);
    String str1 = getSharedPreferences("com.dianping.mapidebugagent", 0).getString("splashUrl", null);
    String str2 = localDebugDomainItem.getDomain(1);
    paramBundle = str1;
    if (str2 != null)
    {
      paramBundle = str1;
      if (str2.equals(str1))
        paramBundle = "180.153.132.55";
    }
    if (!TextUtils.isEmpty(paramBundle))
      localDebugDomainItem.setDomain(paramBundle);
    paramBundle = preferences().getString("siteUrl", null);
    ((DebugDomainItem)findViewById(R.id.plugin_site_item)).setDomain(paramBundle);
    paramBundle = preferences().getString("hotelOrderUrl", "http://www.dianping.com/hotel/order/mlist?token=!&agent=!&cityid=*");
    ((DebugDomainItem)findViewById(R.id.hotel_order_item)).setDomain(paramBundle);
    ((DebugDomainItem)findViewById(R.id.dppush_item)).setDomain(this.debugAgent.pushDebugDomain());
    if (Build.VERSION.SDK_INT >= 11);
    for (paramBundle = getSharedPreferences("dppushservice", 4); ; paramBundle = getSharedPreferences("dppushservice", 0))
    {
      str1 = paramBundle.getString("pullPushUrl", "");
      paramBundle = str1;
      if (TextUtils.isEmpty(str1))
        paramBundle = "http://m.api.dianping.com/pullpush.bin";
      ((DebugDomainItem)findViewById(R.id.dppull_item)).setDomain(paramBundle);
      KeyboardUtils.getSoftKeyboardController(findViewById(R.id.debug_domain)).hide();
      ((ToggleButton)findViewById(R.id.dianping_push)).setChecked(DPPushService.isStarted());
      ((ToggleButton)findViewById(R.id.dianping_push)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
      {
        public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
        {
          if (paramBoolean)
          {
            DPPushService.start(DebugEntranceIpSelectActivity.this, "debug");
            return;
          }
          DPPushService.stop(DebugEntranceIpSelectActivity.this);
        }
      });
      return;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.debugAgent.setPushDebugDomain(((DebugDomainItem)findViewById(R.id.dppush_item)).getCurrentDomain());
    String str = ((DebugDomainItem)findViewById(R.id.splash_item)).getCurrentDomain();
    Object localObject = str;
    if ("180.153.132.55".equals(str))
      localObject = "http://180.153.132.55/sc/splash/${network_type}/beta/${req_time}(${width}X${height}).jpg";
    getSharedPreferences("com.dianping.mapidebugagent", 0).edit().putString("splashUrl", (String)localObject).commit();
    if (Build.VERSION.SDK_INT >= 11)
    {
      localObject = getSharedPreferences("dppushservice", 4);
      ((SharedPreferences)localObject).edit().putString("dpPushUrl", this.debugAgent.pushDebugDomain()).putString("pullPushUrl", ((DebugDomainItem)findViewById(R.id.dppull_item)).getCurrentDomain()).commit();
      localObject = ((DebugDomainItem)findViewById(R.id.plugin_site_item)).getCurrentDomain();
      if (!TextUtils.isEmpty((CharSequence)localObject))
        break label240;
      preferences().edit().remove("siteUrl").commit();
    }
    while (true)
    {
      localObject = ((DebugDomainItem)findViewById(R.id.hotel_order_item)).getCurrentDomain();
      if (!TextUtils.isEmpty((CharSequence)localObject))
        break label265;
      preferences().edit().remove("hotelOrderUrl").commit();
      return;
      localObject = getSharedPreferences("dppushservice", 0);
      break;
      label240: preferences().edit().putString("siteUrl", (String)localObject).commit();
    }
    label265: preferences().edit().putString("hotelOrderUrl", (String)localObject).commit();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugEntranceIpSelectActivity
 * JD-Core Version:    0.6.0
 */