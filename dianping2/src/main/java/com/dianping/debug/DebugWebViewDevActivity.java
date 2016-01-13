package com.dianping.debug;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.dianping.app.Environment;
import com.dianping.base.app.NovaActivity;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.io.File;

public class DebugWebViewDevActivity extends NovaActivity
  implements View.OnClickListener
{
  static final String WEB_URL_FROM_SHAREPREFERENCE = "web_url_from_string_dianping";
  static final String WEB_URL_TO_SHAREPREFERENCE = "web_url_to_string_dianping";
  private int currentWebUrlSelection = 0;

  private void delAllFiles(File paramFile)
  {
    if (paramFile.isDirectory())
    {
      File[] arrayOfFile = paramFile.listFiles();
      if ((arrayOfFile != null) && (arrayOfFile.length > 0))
      {
        int j = arrayOfFile.length;
        int i = 0;
        while (i < j)
        {
          delAllFiles(arrayOfFile[i]);
          i += 1;
        }
      }
      paramFile.delete();
      return;
    }
    paramFile.delete();
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    Object localObject;
    if (i == R.id.web_url_domain_selector)
    {
      paramView = new AlertDialog.Builder(this).setTitle("域名选择");
      i = this.currentWebUrlSelection;
      localObject = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          if (paramInt == 0)
          {
            ((TextView)DebugWebViewDevActivity.this.findViewById(R.id.web_url_from_domain)).setText(null);
            ((TextView)DebugWebViewDevActivity.this.findViewById(R.id.web_url_to_domain)).setText(null);
          }
          while (true)
          {
            paramDialogInterface.dismiss();
            return;
            if (paramInt != 1)
              continue;
            ((TextView)DebugWebViewDevActivity.this.findViewById(R.id.web_url_from_domain)).setText("http://m.dianping.com/");
            ((TextView)DebugWebViewDevActivity.this.findViewById(R.id.web_url_to_domain)).setText("http://m.51ping.com/");
          }
        }
      };
      paramView.setSingleChoiceItems(new String[] { "dianping", "beta" }, i, (DialogInterface.OnClickListener)localObject).setNegativeButton("取消", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      }).show();
    }
    do
    {
      return;
      if (i == R.id.web_clearcache)
      {
        paramView = getCacheDir().getParent();
        new Thread(new Runnable(new String[] { paramView + "/app_webview", paramView + "/cache/ApplicationCache.db", paramView + "/cache/webviewCache", paramView + "/cache/webview", paramView + "/cache/webviewCache.db", paramView + "/database/webview.db", paramView + "/database/webviewCache.db" })
        {
          public void run()
          {
            try
            {
              String[] arrayOfString = this.val$dirs;
              int j = arrayOfString.length;
              int i = 0;
              while (i < j)
              {
                File localFile = new File(arrayOfString[i]);
                if (localFile.exists())
                  DebugWebViewDevActivity.this.delAllFiles(localFile);
                i += 1;
              }
            }
            catch (Exception localException)
            {
              Log.e("An error occured while clearing webView cache");
            }
          }
        }).start();
        return;
      }
      if (i != R.id.web_injectjs_weinre)
        continue;
      paramView = new Intent("Intent.Action.Web_InjectJs_Weinre");
      localObject = ((EditText)findViewById(R.id.jsfile_path)).getText().toString();
      if ((!TextUtils.isEmpty((CharSequence)localObject)) && (((String)localObject).startsWith("javascript:")))
        paramView.putExtra("JsCode", (String)localObject);
      while (true)
      {
        sendBroadcast(paramView);
        finish();
        return;
        paramView.putExtra("JsCode", "javascript:(function(e){e.setAttribute(\"src\",\"" + (String)localObject + "\");" + "document.getElementsByTagName(\"body\")[0].appendChild(e);})(" + "document.createElement(\"script\"));");
      }
    }
    while (i != R.id.change_domain_to_beta);
    ((TextView)findViewById(R.id.web_url_from_domain)).setText("h5.dianping.com");
    ((TextView)findViewById(R.id.web_url_to_domain)).setText("h5.51ping.com");
  }

  public void onCreate(Bundle paramBundle)
  {
    boolean bool = true;
    super.onCreate(paramBundle);
    super.setContentView(R.layout.debug_panel_webview_debug);
    findViewById(R.id.web_clearcache).setOnClickListener(this);
    findViewById(R.id.web_injectjs_weinre).setOnClickListener(this);
    findViewById(R.id.web_url_domain_selector).setOnClickListener(this);
    findViewById(R.id.change_domain_to_beta).setOnClickListener(this);
    paramBundle = getSharedPreferences("com.dianping.mapidebugagent", 0).getString("web_url_from_string_dianping", "");
    ((EditText)findViewById(R.id.web_url_from_domain)).setText(paramBundle);
    ((EditText)findViewById(R.id.web_url_to_domain)).setText(getSharedPreferences("com.dianping.mapidebugagent", 0).getString("web_url_to_string_dianping", ""));
    if (!TextUtils.isEmpty(paramBundle))
      this.currentWebUrlSelection = 1;
    paramBundle = (ToggleButton)findViewById(R.id.disable_whitelist);
    SharedPreferences localSharedPreferences = getSharedPreferences("webview_jsbridge_settings", 0);
    if (Environment.isDebug());
    while (true)
    {
      paramBundle.setChecked(localSharedPreferences.getBoolean("whitelistdisable", bool));
      ((ToggleButton)findViewById(R.id.disable_whitelist)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
      {
        public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
        {
          DebugWebViewDevActivity.this.getSharedPreferences("webview_jsbridge_settings", 0).edit().putBoolean("whitelistdisable", paramBoolean).commit();
        }
      });
      return;
      bool = false;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    getSharedPreferences("com.dianping.mapidebugagent", 0).edit().putString("web_url_from_string_dianping", ((TextView)findViewById(R.id.web_url_from_domain)).getText().toString()).putString("web_url_to_string_dianping", ((TextView)findViewById(R.id.web_url_to_domain)).getText().toString()).commit();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugWebViewDevActivity
 * JD-Core Version:    0.6.0
 */