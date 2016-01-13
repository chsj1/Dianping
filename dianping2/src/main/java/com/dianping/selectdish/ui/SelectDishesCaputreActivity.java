package com.dianping.selectdish.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.BeautifulProgressDialog;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.SubmitOrderManager;
import com.dianping.selectdish.TogetherSubmitOrderManager;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.NovaTextView;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.CaptureActivityHandler;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.URIParsedResult;

public class SelectDishesCaputreActivity extends CaptureActivity
  implements FullRequestHandle
{
  private boolean isScanClosed;
  private int isTogetherSD;
  private MApiService mApiService;
  NewCartManager mCartManager = NewCartManager.getInstance();
  private TitleBar mTitleBar;
  private BeautifulProgressDialog progressDialog;
  private MApiRequest qrUriReq;

  private void reqQRUri(String paramString)
  {
    if (this.qrUriReq != null)
      this.mApiService.abort(this.qrUriReq, this, true);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/getmerchanttablecode.hbt").buildUpon();
    localBuilder.appendQueryParameter("barcodeurl", paramString);
    this.qrUriReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    this.mApiService.exec(this.qrUriReq, this);
  }

  public void confirmTableNum(String paramString)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    if (paramString != null)
    {
      localBuilder.setTitle(getString(R.string.sd_conform_table).replace("%s", paramString));
      localBuilder.setPositiveButton(R.string.sd_conform_tip, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          Toast.makeText(SelectDishesCaputreActivity.this, R.string.sd_direct_to_pay, 0).show();
          if (SelectDishesCaputreActivity.this.isTogetherSD == 1)
          {
            new TogetherSubmitOrderManager().submit(SelectDishesCaputreActivity.this);
            return;
          }
          new SubmitOrderManager().submit(SelectDishesCaputreActivity.this);
        }
      });
      localBuilder.setNegativeButton(R.string.sd_rescan, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      });
    }
    while (true)
    {
      paramString = localBuilder.create();
      paramString.setOnDismissListener(new DialogInterface.OnDismissListener()
      {
        public void onDismiss(DialogInterface paramDialogInterface)
        {
          ((CaptureActivityHandler)SelectDishesCaputreActivity.this.getHandler()).restartPreviewAndDecode();
        }
      });
      paramString.show();
      return;
      localBuilder.setTitle(R.string.sd_no_tablenum_tip).setNeutralButton(R.string.sd_conform, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      });
    }
  }

  public void handleDecodeInternally(Result paramResult, ResultHandler paramResultHandler, Bitmap paramBitmap)
  {
    if (!this.isScanClosed)
    {
      paramResult = ResultParser.parseResult(paramResult);
      if (paramResult.getType().equals(ParsedResultType.URI))
        reqQRUri(Uri.parse(((URIParsedResult)paramResult).getURI()).toString());
    }
    else
    {
      return;
    }
    confirmTableNum(null);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void initTitleBar()
  {
    if (getParent() == null)
    {
      this.mTitleBar = initCustomTitle();
      this.mTitleBar.setLeftView(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectDishesCaputreActivity.this.onBackPressed();
        }
      });
    }
  }

  public void initView()
  {
    ((NovaTextView)findViewById(R.id.manualinput)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SelectDishesCaputreActivity.access$002(SelectDishesCaputreActivity.this, true);
        if (SelectDishesCaputreActivity.this.isTogetherSD == 1)
        {
          new TogetherSubmitOrderManager().submit(SelectDishesCaputreActivity.this);
          return;
        }
        new SubmitOrderManager().submit(SelectDishesCaputreActivity.this);
      }
    });
  }

  public void onBackPressed()
  {
    super.onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    initTitleBar();
    super.onCreate(paramBundle);
    setContentView(R.layout.select_dishes_capture);
    setTitle(getTitle());
    initView();
    if (this.mApiService == null)
      this.mApiService = DPApplication.instance().mapiService();
    if (paramBundle == null)
    {
      if (getIntent().getData().getQueryParameter("istogethersd") == null);
      for (int i = 0; ; i = Integer.parseInt(getIntent().getData().getQueryParameter("istogethersd")))
      {
        this.isTogetherSD = i;
        return;
      }
    }
    this.isTogetherSD = paramBundle.getInt("istogethersd");
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    this.progressDialog.dismiss();
    Toast.makeText(this, "桌号获取失败，请手动输入...", 0).show();
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    this.progressDialog.dismiss();
    paramRequest = (DPObject)paramResponse.result();
    if (paramRequest != null)
    {
      paramRequest = paramRequest.getString("Tablecode");
      this.mCartManager.tableId = paramRequest;
      confirmTableNum(paramRequest);
      return;
    }
    Toast.makeText(this, "桌号获取失败，请手动输入...", 0).show();
  }

  public void onRequestProgress(Request paramRequest, int paramInt1, int paramInt2)
  {
  }

  public void onRequestStart(Request paramRequest)
  {
    showProgressDialog("加载中...");
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("istogethersd", this.isTogetherSD);
  }

  public void setScanClosed(boolean paramBoolean)
  {
    this.isScanClosed = paramBoolean;
    ((CaptureActivityHandler)getHandler()).restartPreviewAndDecode();
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    super.setTitle(paramCharSequence);
    if (this.mTitleBar != null)
      this.mTitleBar.setTitle(paramCharSequence);
  }

  public void showProgressDialog(String paramString)
  {
    this.progressDialog = new BeautifulProgressDialog(this);
    this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
      }
    });
    this.progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
    {
      public boolean onKey(DialogInterface paramDialogInterface, int paramInt, KeyEvent paramKeyEvent)
      {
        return paramInt == 84;
      }
    });
    BeautifulProgressDialog localBeautifulProgressDialog = this.progressDialog;
    String str = paramString;
    if (paramString == null)
      str = "载入中...";
    localBeautifulProgressDialog.setMessage(str);
    this.progressDialog.show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishesCaputreActivity
 * JD-Core Version:    0.6.0
 */