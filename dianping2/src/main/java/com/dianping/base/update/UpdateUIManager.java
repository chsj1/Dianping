package com.dianping.base.update;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.base.update.utils.SHA1Utils;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import java.io.File;

public class UpdateUIManager
{
  private static final int DLG_EXPIRED = 252;
  private static final int DLG_INSTALL = 243;
  private static final int DLG_UPDATE = 242;
  private static final int DLG_UPDATE_SMART = 254;
  private static final String UPDATE_CANCEL_GA_TITLE = "取消";
  private static final String UPDATE_CONFIRM_GA_TITLE = "更新";
  private static final int UPDATE_INFO_MAX_HEIGHT = 100;

  public static Dialog buildDialog(DPActivity paramDPActivity, int paramInt)
  {
    if ((paramInt != 242) && (paramInt != 254) && (paramInt != 243) && (paramInt != 252))
      return null;
    Dialog localDialog = new Dialog(paramDPActivity, R.style.dialog);
    Object localObject1 = LayoutInflater.from(paramDPActivity).inflate(R.layout.update_apk_version, null);
    NetworkImageView localNetworkImageView = (NetworkImageView)((View)localObject1).findViewById(R.id.update_img);
    ImageView localImageView = (ImageView)((View)localObject1).findViewById(R.id.update_cross_icon);
    TextView localTextView3 = (TextView)((View)localObject1).findViewById(R.id.update_title);
    ScrollView localScrollView = (ScrollView)((View)localObject1).findViewById(R.id.update_scroll);
    TextView localTextView1 = (TextView)((View)localObject1).findViewById(R.id.update_info);
    TextView localTextView2 = (TextView)((View)localObject1).findViewById(R.id.update_suggest_tv);
    Button localButton = (Button)((View)localObject1).findViewById(R.id.update_btn);
    localDialog.setContentView((View)localObject1);
    String str2 = ConfigHelper.versionName;
    localObject1 = ConfigHelper.versionNote;
    Object localObject2 = ConfigHelper.versionImg;
    String str3 = ConfigHelper.versionSuggestion;
    boolean bool = UpdateManager.instance(paramDPActivity).isUpdateApkFromAutoWifi();
    String str1 = ConfigHelper.versionTitle;
    if (localObject1 == null)
    {
      localObject1 = "";
      if (str3 != null)
        break label357;
      str3 = "";
      label193: if (paramInt != 242)
        break label360;
      localImageView.setVisibility(0);
      localButton.setOnClickListener(new UpdateUIManager.1(paramDPActivity, localDialog));
      label221: if (!android.text.TextUtils.isEmpty((CharSequence)localObject2))
      {
        localNetworkImageView.setImage((String)localObject2);
        localNetworkImageView.setVisibility(0);
      }
      if (!android.text.TextUtils.isEmpty(com.dianping.util.TextUtils.jsonParseText(str1)))
        localTextView3.setText(str1);
      localObject1 = com.dianping.util.TextUtils.jsonParseText((String)localObject1);
      if (!android.text.TextUtils.isEmpty((CharSequence)localObject1))
        localTextView1.setText((CharSequence)localObject1);
      localObject1 = com.dianping.util.TextUtils.jsonParseText(str3);
      if (android.text.TextUtils.isEmpty((CharSequence)localObject1))
        break label691;
      localTextView2.setVisibility(0);
      localTextView2.setText((CharSequence)localObject1);
    }
    while (true)
    {
      if (ViewUtils.getViewHeight(localTextView1) < ViewUtils.dip2px(paramDPActivity, 100.0F))
        localScrollView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      localImageView.setOnClickListener(new UpdateUIManager.5(paramDPActivity, localDialog));
      localDialog.setCancelable(false);
      return localDialog;
      break;
      label357: break label193;
      label360: if (paramInt == 254)
      {
        str1 = str1 + "待升级";
        localImageView.setVisibility(0);
        localButton.setOnClickListener(new UpdateUIManager.2(paramDPActivity, localDialog));
        break label221;
      }
      if (paramInt == 243)
      {
        str1 = "等待安装";
        localObject2 = new StringBuilder();
        if (localObject1 != "")
        {
          label436: ((StringBuilder)localObject2).append((String)localObject1);
          if (bool)
          {
            ((StringBuilder)localObject2).append("\n\n").append("Wi-Fi下已为您自动下载安装包");
            paramDPActivity.statisticsEvent("dialog", "dialog_wifidownload_show", null, 0);
          }
          str2 = ((StringBuilder)localObject2).toString();
          localObject2 = null;
          localImageView.setVisibility(0);
          if (!bool)
            break label565;
        }
        label565: for (localObject1 = "一键安装"; ; localObject1 = "立即安装")
        {
          localButton.setText((CharSequence)localObject1);
          localButton.setOnClickListener(new UpdateUIManager.3(paramDPActivity, localDialog));
          localObject1 = str2;
          break;
          StringBuilder localStringBuilder = new StringBuilder().append("新版本");
          localObject1 = str2;
          if (str2 == null)
            localObject1 = "";
          localObject1 = (String)localObject1 + "已下载完成，是否现在安装";
          break label436;
        }
      }
      if (paramInt != 252)
        break label221;
      str1 = "版本更新";
      localObject1 = ConfigHelper.forceUpdateDesc;
      if ((ConfigHelper.forceUpdate) && (!android.text.TextUtils.isEmpty((CharSequence)localObject1)))
      {
        localObject2 = new StringBuilder().append((String)localObject1);
        localObject1 = str2;
        if (str2 == null)
          localObject1 = "";
      }
      for (localObject1 = (String)localObject1; ; localObject1 = (String)localObject1)
      {
        localObject2 = null;
        localButton.setOnClickListener(new UpdateUIManager.4(paramDPActivity, localDialog));
        break;
        localObject2 = new StringBuilder().append("您的版本过低，为了更好的功能和体验，请升级到最新版本");
        localObject1 = str2;
        if (str2 != null)
          continue;
        localObject1 = "";
      }
      label691: localTextView2.setVisibility(4);
    }
  }

  public static void showDialog(DPActivity paramDPActivity)
  {
    if (UpdateManager.instance(paramDPActivity).isUpdateApkOK())
    {
      paramDPActivity.showDialog(243);
      paramDPActivity.statisticsEvent("install5", "install5_show", null, 0);
      return;
    }
    boolean bool1 = UpdateManager.instance(paramDPActivity).isExpired();
    boolean bool2 = UpdateManager.instance(paramDPActivity).canSmartUpdate();
    String str = UpdateManager.instance(paramDPActivity).getOldApkSha1();
    File localFile = new File(UpdateManager.instance(paramDPActivity).getLocalApkPath());
    if ((bool1) || (ConfigHelper.forceUpdate))
      paramDPActivity.showDialog(252);
    while (true)
    {
      GAHelper.instance().contextStatisticsEvent(paramDPActivity, "home_update", "", 0, "view");
      paramDPActivity.statisticsEvent("update5", "update5_show", null, 0);
      return;
      if ((bool2) && (SHA1Utils.testSHA1(localFile, str)))
      {
        paramDPActivity.showDialog(254);
        continue;
      }
      paramDPActivity.showDialog(242);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.update.UpdateUIManager
 * JD-Core Version:    0.6.0
 */