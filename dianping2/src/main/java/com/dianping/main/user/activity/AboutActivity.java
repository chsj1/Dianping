package com.dianping.main.user.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.mapi.impl.DefaultMApiService;
import com.dianping.model.City;
import com.dianping.model.UserProfile;
import com.dianping.util.DPUrl;
import com.dianping.util.Log;
import com.dianping.util.LoginUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AboutActivity extends NovaActivity
  implements View.OnClickListener, TableView.OnItemClickListener
{
  ImageView aboutIcon;
  boolean clearFlag = false;
  Handler handler;
  boolean invokeFlag = true;
  boolean isDebug;
  String sequenceUrl;
  long startMills;
  TextView versionText;

  void doLogin()
  {
    accountService().login(this);
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

  public void onClick(View paramView)
  {
    if ((paramView == this.versionText) && (Environment.isDebug()))
    {
      paramView = new Intent("com.dianping.action.VIEW");
      paramView.setData(Uri.parse("dianping://debugpanel"));
      startActivity(paramView);
      finish();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.about);
    this.handler = new Handler()
    {
      private int count = 0;

      public void handleMessage(Message paramMessage)
      {
        super.handleMessage(paramMessage);
        switch (paramMessage.what)
        {
        default:
          if (!AboutActivity.this.clearFlag)
            break;
          this.count = 0;
          AboutActivity.this.clearFlag = false;
          AboutActivity.this.invokeFlag = true;
        case 1:
        }
        do
        {
          do
          {
            return;
            this.count += 1;
          }
          while (this.count != 5);
          AboutActivity.this.clearFlag = false;
          AboutActivity.this.invokeFlag = true;
          Log.LEVEL = 2;
          AboutActivity.this.updateVersionText();
        }
        while ((AboutActivity.this.isDebug) || (!Environment.isDebug()));
        AboutActivity.this.isDebug = true;
        AboutActivity.this.startActivity("dianping://debugpanel");
        AboutActivity.this.finish();
      }
    };
    this.aboutIcon = ((ImageView)findViewById(R.id.about_imv));
    this.aboutIcon.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        AboutActivity.this.handler.post(new Runnable()
        {
          public void run()
          {
            Message localMessage = AboutActivity.this.handler.obtainMessage();
            localMessage.what = 1;
            AboutActivity.this.handler.sendMessage(localMessage);
            if (AboutActivity.this.invokeFlag)
            {
              AboutActivity.this.clearFlag = true;
              AboutActivity.this.invokeFlag = false;
              AboutActivity.this.handler.sendEmptyMessageDelayed(0, 5000L);
            }
          }
        });
      }
    });
    ((TableView)findViewById(R.id.more_tableview)).setOnItemClickListener(this);
    if (TextUtils.isEmpty(ConfigHelper.cooperationUrl))
      findViewById(R.id.shop_cooperation).setVisibility(8);
    this.versionText = ((TextView)findViewById(16908308));
    this.versionText.setClickable(true);
    this.versionText.setOnClickListener(this);
    if (!"om_dt_xtqg".equals(Environment.source()))
      ((TextView)findViewById(R.id.text_school)).setVisibility(8);
    updateVersionText();
    this.startMills = System.currentTimeMillis();
    this.isDebug = Environment.isDebug();
  }

  public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
  {
    paramInt = paramView.getId();
    if (paramInt == R.id.more_share)
    {
      paramTableView = new ShareHolder();
      paramTableView.desc = "我正在用大众点评手机客户端，看看网友的点评和推荐，吃饭或逛街时还挺管用的！你也试试哈~";
      paramTableView.webUrl = "http://dpurl.cn/CMG";
      ShareUtil.gotoShareTo(this, ShareType.APP, paramTableView, "app5", "app5_share");
      statisticsEvent("setting5", "setting5_aboutus_share", "", 0);
    }
    do
      while (true)
      {
        return;
        if (paramInt == R.id.more_help_shop)
        {
          paramTableView = new String[4];
          paramTableView[0] = "随手拍帮助附近商户";
          paramTableView[1] = "找出附近已关商户";
          paramTableView[2] = "写点评";
          paramTableView[3] = "添加商户";
          new AlertDialog.Builder(this).setItems(paramTableView, new DialogInterface.OnClickListener(paramTableView)
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              paramDialogInterface = new Intent("android.intent.action.VIEW");
              if (paramInt == 0)
              {
                paramDialogInterface.setData(Uri.parse("dianping://commendaddphoto"));
                AboutActivity.this.startActivity(paramDialogInterface);
                AboutActivity.this.statisticsEvent("setting5", "setting5_aboutus_help", this.val$item[paramInt], 0);
              }
              do
              {
                return;
                if (paramInt == 1)
                {
                  paramDialogInterface.setData(Uri.parse("dianping://addclosedshops"));
                  AboutActivity.this.startActivity(paramDialogInterface);
                  AboutActivity.this.statisticsEvent("setting5", "setting5_aboutus_help", this.val$item[paramInt], 0);
                  return;
                }
                if (paramInt != 2)
                  continue;
                if (AboutActivity.this.accountService().token() == null)
                {
                  AboutActivity.this.sequenceUrl = "dianping://commendaddreview";
                  LoginUtils.setLoginGASource(AboutActivity.this, "more_rev");
                  AboutActivity.this.doLogin();
                }
                while (true)
                {
                  AboutActivity.this.statisticsEvent("setting5", "setting5_aboutus_help", this.val$item[paramInt], 0);
                  return;
                  AboutActivity.this.startActivity("dianping://commendaddreview");
                }
              }
              while (paramInt != 3);
              paramDialogInterface = new DPUrl("dianping://web");
              paramDialogInterface.putParam("url", "http://m.dianping.com/poi/app/shop/addShop");
              if (AboutActivity.this.accountService().token() == null)
              {
                LoginUtils.setLoginGASource(AboutActivity.this, "more_addsp");
                AboutActivity.this.sequenceUrl = paramDialogInterface.toString();
                AboutActivity.this.doLogin();
              }
              while (true)
              {
                AboutActivity.this.statisticsEvent("setting5", "setting5_aboutus_help_addshop", null, 0);
                return;
                AboutActivity.this.startActivity(paramDialogInterface.toString());
              }
            }
          }).setTitle("帮助商户").show();
          return;
        }
        if (paramInt != R.id.shop_cooperation)
          break;
        paramTableView = ConfigHelper.cooperationUrl;
        if (TextUtils.isEmpty(paramTableView))
          continue;
        if ((paramTableView.startsWith("http://")) || (paramTableView.startsWith("https://")))
        {
          startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://complexweb").buildUpon().appendQueryParameter("url", paramTableView).build()));
          return;
        }
        startActivity(paramTableView);
        return;
      }
    while (paramInt != R.id.text_school);
    paramTableView = null;
    paramView = DPApplication.instance().getService("mapi_original");
    if ((paramView instanceof DefaultMApiService))
      paramTableView = ((DefaultMApiService)paramView).getDpid();
    while (true)
    {
      String str = "http://m.dianping.com/app/campus/verify?cityid=" + city().id() + "&deviceid=" + Environment.uuid() + "&imei=" + Environment.imei() + "&agent=android&version=" + Environment.versionName();
      paramView = str;
      if (paramTableView != null)
        paramView = str + "&dpid=" + paramTableView;
      try
      {
        paramTableView = URLEncoder.encode(paramView, "utf-8");
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramTableView)));
        statisticsEvent("setting5", "setting5_aboutus_school", "", 0);
        return;
        paramView = DPApplication.instance().getService("mapi");
        if (!(paramView instanceof DefaultMApiService))
          continue;
        paramTableView = ((DefaultMApiService)paramView).getDpid();
      }
      catch (UnsupportedEncodingException paramTableView)
      {
        paramTableView.printStackTrace();
      }
    }
  }

  public boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.sequenceUrl)));
    return paramBoolean;
  }

  public void onLoginCancel()
  {
    this.sequenceUrl = null;
  }

  public void updateVersionText()
  {
    try
    {
      PackageInfo localPackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("大众点评 ").append(localPackageInfo.versionName);
      if (Environment.isDebug())
        localStringBuilder.append('\n').append("DEBUG");
      this.versionText.setText(localStringBuilder.toString());
      return;
    }
    catch (Exception localException)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.AboutActivity
 * JD-Core Version:    0.6.0
 */