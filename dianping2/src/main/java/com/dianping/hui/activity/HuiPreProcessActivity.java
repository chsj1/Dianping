package com.dianping.hui.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.hui.entity.HuiMapiStatus;
import com.dianping.hui.entity.HuiPreProcessDataSource;
import com.dianping.hui.entity.HuiPreProcessDataSource.HuiPreProcessRequestListener;
import com.dianping.hui.entity.Message;
import com.dianping.hui.util.HuiUtils;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HuiPreProcessActivity extends NovaActivity
  implements HuiPreProcessDataSource.HuiPreProcessRequestListener
{
  private HuiPreProcessDataSource dataSource;
  private View errorLayout;
  private View loadingLayout;
  private String requestParams;

  private void initData()
  {
    this.dataSource = new HuiPreProcessDataSource(this);
    this.dataSource.setHuiPreProcessRequestListener(this);
    this.dataSource.shopName = getStringParam("shopname");
    TitleBar localTitleBar = super.getTitleBar();
    if (!TextUtils.isEmpty(this.dataSource.shopName));
    for (String str = this.dataSource.shopName; ; str = "")
    {
      localTitleBar.setTitle(str);
      if (getIntent() == null)
        break;
      this.requestParams = getIntent().getDataString().split("huipreprocess")[1];
      this.dataSource.requestParams = this.requestParams;
      this.dataSource.sendPreProcessRequest(this.requestParams);
      return;
    }
    finish();
  }

  private void initView()
  {
    this.loadingLayout = findViewById(R.id.hui_pre_process_loading_layout);
    this.errorLayout = findViewById(R.id.error_layout);
    this.errorLayout.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HuiPreProcessActivity.this.retry();
      }
    });
  }

  private void retry()
  {
    updateView(HuiMapiStatus.STATUS_START);
    this.dataSource.sendPreProcessRequest(this.dataSource.requestParams);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.hui_pre_process_activity);
    initView();
    initData();
  }

  protected void onDestroy()
  {
    this.dataSource.releaseRequests();
    super.onDestroy();
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.dataSource.restoreData(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.dataSource.saveData(paramBundle);
  }

  public void requestPreProcessComplete(HuiMapiStatus paramHuiMapiStatus, Message paramMessage)
  {
    int i;
    if (paramHuiMapiStatus == HuiMapiStatus.STATUS_FINISH)
    {
      i = paramMessage.code;
      paramHuiMapiStatus = paramMessage.content;
      if (i == 10)
      {
        updateView(HuiMapiStatus.STATUS_FINISH);
        paramMessage = new Intent("android.intent.action.VIEW");
        paramMessage.setData(Uri.parse(paramHuiMapiStatus));
        startActivity(paramMessage);
        new Handler().postDelayed(new Runnable()
        {
          public void run()
          {
            HuiPreProcessActivity.this.finish();
          }
        }
        , 500L);
        overridePendingTransition(0, 0);
      }
    }
    do
    {
      do
      {
        return;
        if (i != 20)
          continue;
        updateView(HuiMapiStatus.STATUS_FAIL);
        showMessageDialog(paramHuiMapiStatus, "知道了", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            HuiPreProcessActivity.this.finish();
          }
        });
        return;
      }
      while (i != 30);
      updateView(HuiMapiStatus.STATUS_FAIL);
      this.errorLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HuiPreProcessActivity.this.retry();
        }
      });
      return;
    }
    while (paramHuiMapiStatus != HuiMapiStatus.STATUS_FAIL);
    updateView(HuiMapiStatus.STATUS_FAIL);
    this.errorLayout.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        HuiPreProcessActivity.this.retry();
      }
    });
  }

  void showMessageDialog(String paramString1, String paramString2, DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setMessage(paramString1);
    localBuilder.setPositiveButton(paramString2, paramOnClickListener);
    paramString1 = localBuilder.create();
    paramString1.setCanceledOnTouchOutside(false);
    this.managedDialogId = 64006;
    this.managedDialog = paramString1;
    paramString1.show();
  }

  protected void updateView(HuiMapiStatus paramHuiMapiStatus)
  {
    switch (6.$SwitchMap$com$dianping$hui$entity$HuiMapiStatus[paramHuiMapiStatus.ordinal()])
    {
    default:
      return;
    case 1:
      HuiUtils.updateViewVisibility(this.loadingLayout, 0);
      HuiUtils.updateViewVisibility(this.errorLayout, 8);
      return;
    case 2:
      HuiUtils.updateViewVisibility(this.loadingLayout, 8);
      HuiUtils.updateViewVisibility(this.errorLayout, 8);
      return;
    case 3:
    }
    HuiUtils.updateViewVisibility(this.loadingLayout, 8);
    HuiUtils.updateViewVisibility(this.errorLayout, 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiPreProcessActivity
 * JD-Core Version:    0.6.0
 */