package com.dianping.takeaway.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.takeaway.fragment.TakeawayOrderDetailFragment;
import com.dianping.widget.view.GAUserInfo;

public class TakeawayOrderDetailActivity extends AgentActivity
{
  public String queryId;
  private String source;

  private void exit()
  {
    if ((this.source != null) && (!"takeawayorderdetail".equals(this.source)))
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://" + this.source));
      localIntent.setFlags(67108864);
      super.startActivity(localIntent);
    }
    super.finish();
  }

  protected AgentFragment getAgentFragment()
  {
    return new TakeawayOrderDetailFragment();
  }

  public String getPageName()
  {
    return "takeawayorderdetail";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.getWindow().setBackgroundDrawable(null);
    this.source = super.getStringParam("source");
    this.queryId = super.getStringParam("queryid");
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

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    paramGAUserInfo.query_id = this.queryId;
    super.onNewGAPager(paramGAUserInfo);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayOrderDetailActivity
 * JD-Core Version:    0.6.0
 */