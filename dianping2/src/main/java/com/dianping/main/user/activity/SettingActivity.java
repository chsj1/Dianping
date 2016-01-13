package com.dianping.main.user.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.update.UpdateManager;
import com.dianping.base.update.UpdateUIManager;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TableView.OnItemClickListener;
import com.dianping.cache.DPCache;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;

public class SettingActivity extends NovaActivity
  implements TableView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private DPObject mDPObject;
  private View mInviteFriendContainer;
  private View mInviteFriendGapView;
  private MApiRequest mInviteFriendRequest;
  private View mSettingInviteFriendView;

  private void checkQQSecureStatus(int paramInt)
  {
    TextView localTextView = (TextView)super.findViewById(R.id.secure_info);
    String str = "";
    switch (paramInt)
    {
    default:
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      localTextView.setText(str);
      GAHelper.instance().contextStatisticsEvent(this, "safe", str, 2147483647, "view");
      return;
      str = "未开启";
      continue;
      str = "未运行";
      continue;
      str = "已开启";
    }
  }

  private void createInviteFriendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/framework/getuserset.bin").buildUpon();
    localBuilder.appendQueryParameter("cityid", "" + cityId());
    this.mInviteFriendRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.mInviteFriendRequest, this);
  }

  private void showInviteFriendView(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mInviteFriendContainer.setVisibility(0);
      this.mInviteFriendGapView.setVisibility(0);
      this.mSettingInviteFriendView.setVisibility(0);
      return;
    }
    this.mInviteFriendContainer.setVisibility(8);
    this.mInviteFriendGapView.setVisibility(8);
    this.mSettingInviteFriendView.setVisibility(8);
  }

  private void updateInviteFriendView(DPObject paramDPObject)
  {
    if (!paramDPObject.getBoolean("Show"))
      showInviteFriendView(false);
    String str;
    NovaButton localNovaButton;
    do
    {
      return;
      showInviteFriendView(true);
      str = paramDPObject.getString("Url");
      paramDPObject = paramDPObject.getString("Title");
      TextView localTextView = (TextView)this.mInviteFriendContainer.findViewById(R.id.invite_friend_title);
      localNovaButton = (NovaButton)this.mInviteFriendContainer.findViewById(R.id.invite_friends);
      if (TextUtils.isEmpty(paramDPObject))
        continue;
      localTextView.setText(paramDPObject);
    }
    while (TextUtils.isEmpty(str));
    localNovaButton.setGAString("invite");
    localNovaButton.setOnClickListener(new View.OnClickListener(str)
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$url));
        SettingActivity.this.startActivity(paramView);
      }
    });
  }

  protected void doClearCache()
  {
    new ClearCacheTask(this).execute(new Void[0]);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.more_setting);
    ((TableView)super.findViewById(R.id.settings)).setOnItemClickListener(this);
    Object localObject = (LinearLayout)super.findViewById(R.id.more_check_update);
    if (NovaConfigUtils.instance().disableCheckUpdate())
      ((LinearLayout)localObject).setVisibility(8);
    this.mInviteFriendContainer = findViewById(R.id.invite_friends_container);
    this.mInviteFriendGapView = findViewById(R.id.invite_friends_gap);
    this.mSettingInviteFriendView = findViewById(R.id.setting_invite_friends);
    if (UpdateManager.instance(this).checkNewVersion())
    {
      ((LinearLayout)localObject).findViewById(R.id.ic_new).setVisibility(0);
      boolean bool = NovaActivity.preferences().getBoolean("autodownload", true);
      localObject = (TextView)findViewById(R.id.setting_update_value);
      if (!bool)
        break label201;
      ((TextView)localObject).setText("仅Wi-Fi网络");
    }
    while (true)
    {
      super.cityId();
      super.getResources();
      super.cityConfig();
      if (paramBundle != null)
        this.mDPObject = ((DPObject)paramBundle.getParcelable("inviteFriend"));
      if (this.mDPObject != null)
        break label211;
      createInviteFriendRequest();
      return;
      ((LinearLayout)localObject).findViewById(R.id.ic_new).setVisibility(8);
      break;
      label201: ((TextView)localObject).setText("从不");
    }
    label211: updateInviteFriendView(this.mDPObject);
  }

  protected Dialog onCreateDialog(int paramInt)
  {
    Dialog localDialog = UpdateUIManager.buildDialog(this, paramInt);
    if (localDialog != null)
      return localDialog;
    return super.onCreateDialog(paramInt);
  }

  public void onItemClick(TableView paramTableView, View paramView, int paramInt, long paramLong)
  {
    paramInt = paramView.getId();
    if (paramInt == R.id.setting_clear_cache)
    {
      doClearCache();
      ((NovaLinearLayout)paramView).setGAString("delete");
    }
    do
    {
      do
      {
        do
        {
          return;
          if (paramInt == R.id.setting_img_set)
          {
            ((NovaLinearLayout)paramView).setGAString("imgset");
            super.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://imagesetting")));
            return;
          }
          if (paramInt == R.id.setting_push_setting)
          {
            super.startActivity("dianping://notificationsetting");
            ((NovaLinearLayout)paramView).setGAString("message");
            return;
          }
          if (paramInt == R.id.setting_qq_secure)
          {
            paramInt = QQSecureActivity.checkQQSecureProtected(this);
            super.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://qqsecure?qqSecureStatus=" + paramInt)));
            paramTableView = "";
            switch (paramInt)
            {
            default:
            case 0:
            case 1:
            case 2:
            }
            while (true)
            {
              GAHelper.instance().contextStatisticsEvent(this, "safe", paramTableView, 2147483647, "tap");
              return;
              paramTableView = "未开启";
              continue;
              paramTableView = "未运行";
              continue;
              paramTableView = "已开启";
            }
          }
          if (paramInt == R.id.setting_feedback)
          {
            startActivity("dianping://web?url=http://m.dianping.com/app/feedback");
            ((NovaRelativeLayout)paramView).setGAString("feeds");
            return;
          }
          if (paramInt == R.id.more_check_update)
          {
            ((NovaLinearLayout)paramView).setGAString("update");
            if (!UpdateManager.instance(this).checkNewVersion())
            {
              Toast.makeText(this, "您使用的已是最新版本", 1).show();
              super.findViewById(R.id.ic_new).setVisibility(8);
              return;
            }
            UpdateUIManager.showDialog(this);
            super.findViewById(R.id.ic_new).setVisibility(0);
            return;
          }
          if (paramInt == R.id.setting_update)
          {
            paramTableView = new AlertDialog.Builder(this).setTitle("自动下载安装包");
            if (NovaActivity.preferences().getBoolean("autodownload", true));
            for (paramInt = 0; ; paramInt = 1)
            {
              paramView = new DialogInterface.OnClickListener()
              {
                public void onClick(DialogInterface paramDialogInterface, int paramInt)
                {
                  if (paramInt == 0)
                    DPActivity.preferences().edit().putBoolean("autodownload", true).commit();
                  while (true)
                  {
                    paramDialogInterface.dismiss();
                    return;
                    DPActivity.preferences().edit().putBoolean("autodownload", false).commit();
                  }
                }
              };
              paramTableView = paramTableView.setSingleChoiceItems(new String[] { "仅Wi-Fi网络", "从不" }, paramInt, paramView).setNegativeButton("取消", null).create();
              paramTableView.setOnDismissListener(new DialogInterface.OnDismissListener()
              {
                public void onDismiss(DialogInterface paramDialogInterface)
                {
                  boolean bool = DPActivity.preferences().getBoolean("autodownload", true);
                  paramDialogInterface = (TextView)SettingActivity.this.findViewById(R.id.setting_update_value);
                  if (bool)
                  {
                    paramDialogInterface.setText("仅Wi-Fi网络");
                    return;
                  }
                  paramDialogInterface.setText("从不");
                }
              });
              paramTableView.show();
              return;
            }
          }
          if (paramInt == R.id.more_diag)
          {
            super.startActivity("dianping://networkdiagnose");
            ((NovaLinearLayout)paramView).setGAString("diagnosis");
            return;
          }
          if (paramInt != R.id.more_about)
            continue;
          super.startActivity("dianping://about");
          ((NovaLinearLayout)paramView).setGAString("aboutus");
          return;
        }
        while (paramInt != R.id.setting_invite_friends);
        ((NovaLinearLayout)paramView).setGAString("invite");
      }
      while (this.mDPObject == null);
      paramTableView = this.mDPObject.getString("Url");
    }
    while (TextUtils.isEmpty(paramTableView));
    super.startActivity(paramTableView);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mInviteFriendRequest)
    {
      this.mDPObject = null;
      showInviteFriendView(false);
      this.mInviteFriendRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mInviteFriendRequest)
    {
      paramMApiRequest = paramMApiResponse.result();
      if (!(paramMApiRequest instanceof DPObject))
        break label44;
      this.mDPObject = ((DPObject)paramMApiRequest);
      updateInviteFriendView(this.mDPObject);
    }
    while (true)
    {
      this.mInviteFriendRequest = null;
      return;
      label44: showInviteFriendView(false);
    }
  }

  protected void onResume()
  {
    super.onResume();
    checkQQSecureStatus(QQSecureActivity.checkQQSecureProtected(this));
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("inviteFriend", this.mDPObject);
  }

  private class ClearCacheTask extends AsyncTask<Void, Integer, Boolean>
  {
    private Context context;

    public ClearCacheTask(Context arg2)
    {
      Object localObject;
      this.context = localObject;
    }

    protected Boolean doInBackground(Void[] paramArrayOfVoid)
    {
      ((CacheService)SettingActivity.this.getService("image_cahce")).clear();
      DPCache.getInstance().clearMemoryCache();
      DPCache.getInstance().clearFileCache();
      return Boolean.valueOf(true);
    }

    protected void onPostExecute(Boolean paramBoolean)
    {
      SettingActivity.this.dismissDialog();
      Toast.makeText(this.context, "清除成功", 0).show();
    }

    protected void onPreExecute()
    {
      SettingActivity.this.showProgressDialog("正在清除...");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.SettingActivity
 * JD-Core Version:    0.6.0
 */