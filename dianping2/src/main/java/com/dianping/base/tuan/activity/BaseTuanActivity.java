package com.dianping.base.tuan.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.fragment.BaseTuanFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.Log;
import java.net.URISyntaxException;

public class BaseTuanActivity extends NovaActivity
{
  protected AlertDialog alertDialog;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
      }
      paramMessage = BaseTuanActivity.this.getSupportFragmentManager().findFragmentById(16908290);
      if ((paramMessage instanceof BaseTuanFragment))
      {
        ((BaseTuanFragment)paramMessage).invalidateTitleBar();
        return;
      }
      BaseTuanActivity.this.invalidateTitleBar();
    }
  };
  private boolean mInstanceStateSaved = false;

  public void finish()
  {
    String str = getIntent().getStringExtra("next_redirect_");
    if (!TextUtils.isEmpty(str));
    try
    {
      startActivity(Intent.parseUri(str, 1));
      super.finish();
      return;
    }
    catch (URISyntaxException localURISyntaxException)
    {
      while (true)
        Log.e("basetuanactivity finish ", localURISyntaxException.getLocalizedMessage());
    }
  }

  public void gotoLogin(LoginResultListener paramLoginResultListener)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    accountService().login(paramLoginResultListener);
  }

  public final void invalidateTitleBar()
  {
    onCreateTitleBar(getTitleBar());
  }

  public boolean isLogined()
  {
    if (getAccount() == null);
    do
      return false;
    while (TextUtils.isEmpty(accountService().token()));
    return true;
  }

  public void onBackPressed()
  {
    if (getSupportFragmentManager().getBackStackEntryCount() > 0)
    {
      Fragment localFragment = getSupportFragmentManager().findFragmentById(16908290);
      if ((localFragment instanceof BaseTuanFragment))
        if (((BaseTuanFragment)localFragment).onGoBack())
          getSupportFragmentManager().popBackStackImmediate();
    }
    do
    {
      return;
      getSupportFragmentManager().popBackStackImmediate();
      return;
    }
    while ((!onGoBack()) || (this.mInstanceStateSaved));
    super.onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setRequestedOrientation(1);
    super.getTitleBar().setLeftView(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        KeyboardUtils.hideKeyboard(paramView);
        BaseTuanActivity.this.onBackPressed();
      }
    });
    getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener()
    {
      public void onBackStackChanged()
      {
        BaseTuanActivity.this.handler.sendEmptyMessageDelayed(1, 300L);
      }
    });
  }

  public void onCreateTitleBar(TitleBar paramTitleBar)
  {
  }

  protected void onDestroy()
  {
    if ((this.alertDialog != null) && (this.alertDialog.isShowing()))
      this.alertDialog.dismiss();
    super.onDestroy();
  }

  public boolean onGoBack()
  {
    return true;
  }

  protected void onPostCreate(Bundle paramBundle)
  {
    super.onPostCreate(paramBundle);
    this.handler.sendEmptyMessageDelayed(1, 300L);
  }

  protected void onResume()
  {
    super.onResume();
    this.mInstanceStateSaved = false;
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.mInstanceStateSaved = true;
  }

  protected void onUpdateAccount()
  {
  }

  protected void onUpdateTuanProfile(DPObject paramDPObject)
  {
  }

  public View rightTitleButton()
  {
    return this.rightTitleButton;
  }

  public void showAlertDialog(String paramString)
  {
    if ((this.alertDialog != null) && (this.alertDialog.isShowing()))
      this.alertDialog.dismiss();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle("提示").setMessage(paramString).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    });
    this.alertDialog = localBuilder.create();
    this.alertDialog.show();
  }

  public void showAlertDialog(String paramString, DialogInterface.OnClickListener paramOnClickListener)
  {
    if ((this.alertDialog != null) && (this.alertDialog.isShowing()))
      this.alertDialog.dismiss();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setMessage(paramString).setCancelable(false).setPositiveButton("确定", paramOnClickListener).setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.cancel();
      }
    });
    this.alertDialog = localBuilder.create();
    this.alertDialog.show();
  }

  protected void updateAccount()
  {
    if (!isLogined())
      return;
    mapiService().exec(BasicMApiRequest.mapiGet("http://m.api.dianping.com/user.bin?&userid=0&refresh=true&token=" + accountService().token(), CacheType.DISABLED), new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        try
        {
          BaseTuanActivity.this.onUpdateAccount();
          return;
        }
        catch (java.lang.Exception paramMApiRequest)
        {
        }
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        try
        {
          BaseTuanActivity.this.accountService().update((DPObject)paramMApiResponse.result());
          BaseTuanActivity.this.onUpdateAccount();
          return;
        }
        catch (java.lang.Exception paramMApiRequest)
        {
        }
      }
    });
  }

  protected void updateTuanProfile()
  {
    if (!isLogined())
    {
      onUpdateTuanProfile(null);
      return;
    }
    mapiService().exec(BasicMApiRequest.mapiGet("http://app.t.dianping.com/tuanprofilegn.bin?token=" + accountService().token(), CacheType.CRITICAL), new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        try
        {
          BaseTuanActivity.this.onUpdateTuanProfile(null);
          return;
        }
        catch (java.lang.Exception paramMApiRequest)
        {
        }
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        try
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest != null)
          {
            BaseTuanActivity.this.accountService().update(BaseTuanActivity.this.accountService().profile().edit().putString("GrouponPhone", paramMApiRequest.getString("MobilePhone")).generate());
            BaseTuanActivity.this.onUpdateTuanProfile(paramMApiRequest);
          }
          return;
        }
        catch (java.lang.Exception paramMApiRequest)
        {
        }
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.activity.BaseTuanActivity
 * JD-Core Version:    0.6.0
 */