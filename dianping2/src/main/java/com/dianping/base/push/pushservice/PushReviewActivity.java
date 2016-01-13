package com.dianping.base.push.pushservice;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.widget.view.GAHelper;

public class PushReviewActivity extends NovaActivity
{
  private void showPushReviewDialog(String paramString1, String paramString2, String paramString3)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(paramString1).setMessage(paramString2);
    localBuilder.setPositiveButton("现在点评", new DialogInterface.OnClickListener(paramString3)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        GAHelper.instance().contextStatisticsEvent(PushReviewActivity.this, "push_comment_ok", "push_comment_ok", 0, "tap");
        paramDialogInterface = new Intent("android.intent.action.VIEW", Uri.parse(this.val$url));
        paramDialogInterface.putExtra("isFromPush", true);
        PushReviewActivity.this.startActivity(paramDialogInterface);
        PushReviewActivity.this.finish();
      }
    });
    localBuilder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        GAHelper.instance().contextStatisticsEvent(PushReviewActivity.this, "push_comment_cancel", "push_comment_cancel", 0, "tap");
        PushReviewActivity.this.finish();
      }
    });
    localBuilder.setCancelable(false);
    localBuilder.create().show();
    GAHelper.instance().contextStatisticsEvent(this, "push_comment_view", "push_comment_view", 0, "view");
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject2 = getIntent();
    Object localObject1 = ((Intent)localObject2).getStringExtra("contentTitle");
    paramBundle = (Bundle)localObject1;
    if (TextUtils.isEmpty((CharSequence)localObject1))
      paramBundle = "大众点评";
    String str = ((Intent)localObject2).getStringExtra("contentText");
    if (TextUtils.isEmpty(str))
      finish();
    localObject2 = ((Intent)localObject2).getStringExtra("url");
    localObject1 = localObject2;
    if (TextUtils.isEmpty((CharSequence)localObject2))
      localObject1 = "dianping://home";
    showPushReviewDialog(paramBundle, str, (String)localObject1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.push.pushservice.PushReviewActivity
 * JD-Core Version:    0.6.0
 */