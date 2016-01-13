package com.dianping.base.app;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import com.dianping.base.widget.TitleBar;

public class AlertActivity extends NovaActivity
{
  public static final String KEY_CONTENT = "content";
  public static final String KEY_TITLE = "title";

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(new TextView(this));
    paramBundle = getIntent();
    String str2 = paramBundle.getStringExtra("content");
    if (TextUtils.isEmpty(str2))
    {
      finish();
      return;
    }
    String str1 = paramBundle.getStringExtra("title");
    paramBundle = str1;
    if (TextUtils.isEmpty(str1))
      paramBundle = "提示";
    new AlertDialog.Builder(this).setTitle(paramBundle).setMessage(str2).setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        AlertActivity.this.finish();
      }
    }).setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramDialogInterface)
      {
        AlertActivity.this.finish();
      }
    }).show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.AlertActivity
 * JD-Core Version:    0.6.0
 */