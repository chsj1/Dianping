package com.dianping.main.user.activity;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.mapi.impl.DefaultMApiService;
import com.dianping.util.DeviceUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;

public class DiagnoseActivity extends NovaActivity
{
  private static final boolean DEBUG = false;
  private static final int MSG_TIMEOUT = 1;
  private static final String TAG = "DiagnoseActivity";
  private static final String URL = "http://diag.dianping.com/?dpId=%d&source=app";
  private View mButton;
  private TextView mDiagnoseText;
  Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
      }
      DiagnoseActivity.this.showProgressbar(false);
      paramMessage = Toast.makeText(DiagnoseActivity.this, R.string.diag_failed, 0);
      paramMessage.setGravity(17, 0, 0);
      paramMessage.show();
    }
  };
  private ProgressDialog mProgressDialog;
  View mStart;
  View mSuccess;

  private void copyDiagnoseInfo()
  {
    ((ClipboardManager)getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("diagnose_info", this.mDiagnoseText.getText()));
    Toast.makeText(this, "已复制", 0).show();
  }

  private WebChromeClient createWebChromeClient()
  {
    return new DiagWebChromeClient(null);
  }

  private WebViewClient createWebViewClient()
  {
    return new DiagWebViewClient(null);
  }

  private static void setupWebSettings(WebSettings paramWebSettings)
  {
    paramWebSettings.setBuiltInZoomControls(true);
    paramWebSettings.setSaveFormData(true);
    paramWebSettings.setSavePassword(true);
    paramWebSettings.setJavaScriptEnabled(true);
    paramWebSettings.setDomStorageEnabled(true);
    paramWebSettings.setSupportZoom(true);
    paramWebSettings.setUseWideViewPort(true);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_diag);
    paramBundle = (WebView)findViewById(R.id.diag_webview);
    setupWebSettings(paramBundle.getSettings());
    paramBundle.setScrollBarStyle(0);
    Object localObject = createWebChromeClient();
    if (localObject != null)
      paramBundle.setWebChromeClient((WebChromeClient)localObject);
    localObject = createWebViewClient();
    if (localObject != null)
      paramBundle.setWebViewClient((WebViewClient)localObject);
    this.mButton = findViewById(R.id.diag_start);
    this.mButton.setOnClickListener(new View.OnClickListener(paramBundle)
    {
      public void onClick(View paramView)
      {
        DiagnoseActivity.this.showProgressbar(true);
        String str = DeviceUtils.dpid();
        paramView = str;
        if (str == null)
          paramView = "unknowndpid";
        this.val$webview.loadUrl("http://diag.dianping.com/?dpId=%d&source=app".replace("%d", paramView));
        DiagnoseActivity.this.mHandler.sendEmptyMessageDelayed(1, 10000L);
      }
    });
    this.mDiagnoseText = ((TextView)findViewById(R.id.diag_info));
    if ((getService("mapi_original") instanceof DefaultMApiService))
      this.mDiagnoseText.setText(((DefaultMApiService)getService("mapi_original")).diagnosisInfo());
    this.mDiagnoseText.setOnLongClickListener(new View.OnLongClickListener()
    {
      public boolean onLongClick(View paramView)
      {
        paramView = new AlertDialog.Builder(paramView.getContext());
        paramView.setTitle("提示");
        paramView.setMessage("是否将诊断信息复制到剪贴板？");
        paramView.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            DiagnoseActivity.this.copyDiagnoseInfo();
            paramDialogInterface.dismiss();
          }
        });
        paramView.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            paramDialogInterface.cancel();
          }
        });
        paramView.show();
        return true;
      }
    });
    this.mStart = findViewById(R.id.diag_start_layout);
    this.mSuccess = findViewById(R.id.diag_success);
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.mHandler.removeMessages(1);
  }

  void showProgressbar(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      if ((this.mProgressDialog != null) && (this.mProgressDialog.isShowing()))
      {
        this.mProgressDialog.dismiss();
        this.mProgressDialog = null;
      }
      return;
    }
    this.mProgressDialog = new ProgressDialog(this);
    this.mProgressDialog.setMessage(getResources().getString(R.string.diag_waiting));
    this.mProgressDialog.show();
  }

  private class DiagWebChromeClient extends WebChromeClient
  {
    private DiagWebChromeClient()
    {
    }

    public void onProgressChanged(WebView paramWebView, int paramInt)
    {
      super.onProgressChanged(paramWebView, paramInt);
    }

    public void onReceivedTitle(WebView paramWebView, String paramString)
    {
    }
  }

  private class DiagWebViewClient extends WebViewClient
  {
    private DiagWebViewClient()
    {
    }

    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
      if (paramString.equalsIgnoreCase("js://_?finish"))
      {
        DiagnoseActivity.this.mHandler.removeMessages(1);
        DiagnoseActivity.this.mStart.setVisibility(8);
        DiagnoseActivity.this.mSuccess.setVisibility(0);
        DiagnoseActivity.this.showProgressbar(false);
        return true;
      }
      return super.shouldOverrideUrlLoading(paramWebView, paramString);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.DiagnoseActivity
 * JD-Core Version:    0.6.0
 */