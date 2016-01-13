package com.dianping.takeaway.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TakeawayFailureActivity extends NovaActivity
{
  protected String orderViewId;
  protected String source;

  private void exit()
  {
    if (!TextUtils.isEmpty(this.orderViewId))
    {
      localObject = new Intent("com.dianping.takeaway.UPDATE_ORDER");
      ((Intent)localObject).putExtra("orderviewid", this.orderViewId);
      super.sendBroadcast((Intent)localObject);
    }
    String str = "dianping://" + this.source;
    Object localObject = str;
    if (!TextUtils.isEmpty(this.orderViewId))
      localObject = str + "?orderviewid=" + this.orderViewId;
    localObject = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
    ((Intent)localObject).setFlags(67108864);
    super.startActivity((Intent)localObject);
    super.finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.takeaway_failure);
    if (paramBundle == null)
      this.source = super.getStringParam("source");
    for (this.orderViewId = super.getStringParam("orderviewid"); ; this.orderViewId = paramBundle.getString("orderviewid"))
    {
      ((Button)findViewById(R.id.takeaway_failure_retry)).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayshoplist"));
          paramView.setFlags(67108864);
          TakeawayFailureActivity.this.startActivity(paramView);
          TakeawayFailureActivity.this.finish();
        }
      });
      statisticsEvent("takeaway6", "takeaway6_orderfail_track", super.getStringParam("payorderid"), 0);
      return;
      this.source = paramBundle.getString("source");
    }
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      exit();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onLeftTitleButtonClicked()
  {
    exit();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("source", this.source);
    paramBundle.putString("orderviewid", this.orderViewId);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayFailureActivity
 * JD-Core Version:    0.6.0
 */