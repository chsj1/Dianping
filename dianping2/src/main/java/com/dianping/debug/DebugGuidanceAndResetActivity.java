package com.dianping.debug;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.update.UpdateManager;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class DebugGuidanceAndResetActivity extends NovaActivity
  implements View.OnClickListener
{
  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.guidance_new_item)
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://guidancenewcomer")));
    do
    {
      return;
      if (i == R.id.guidance_update_item)
      {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://guidance")));
        return;
      }
      if (i == R.id.reset_item)
      {
        Process.killProcess(Process.myPid());
        return;
      }
      if (i != R.id.debug_update_start_item)
        continue;
      UpdateManager.instance(this).cancelDownload();
      UpdateManager.instance(this).startDownload(((EditText)findViewById(R.id.debug_update_url)).getText().toString(), 0, 0);
      return;
    }
    while (i != R.id.debug_update_clear_item);
    UpdateManager.instance(this).cancelDownload();
    UpdateManager.instance(this).reset();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.debug_guidance_reset);
    findViewById(R.id.guidance_new_item).setOnClickListener(this);
    findViewById(R.id.guidance_update_item).setOnClickListener(this);
    findViewById(R.id.reset_item).setOnClickListener(this);
    findViewById(R.id.debug_update_start_item).setOnClickListener(this);
    findViewById(R.id.debug_update_clear_item).setOnClickListener(this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugGuidanceAndResetActivity
 * JD-Core Version:    0.6.0
 */