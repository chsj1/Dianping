package com.dianping.tuan.activity;

import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.base.tuan.fragment.DealInfoAgentFragment;
import com.dianping.widget.view.GAUserInfo;

public class DealInfoAgentActivity extends TuanAgentActivity
{
  private int dealChannel;
  private int dealId;

  private void resolveArguments()
  {
    this.dealId = getIntParam("id");
    Object localObject;
    if (this.dealId == 0)
      localObject = getStringParam("id");
    try
    {
      this.dealId = Integer.parseInt((String)localObject);
      label32: localObject = getObjectParam("deal");
      if ((this.dealId == 0) && (localObject != null))
        this.dealId = ((DPObject)localObject).getInt("ID");
      this.dealChannel = getIntParam("dealchannel", 0);
      if ((this.dealChannel == 0) && (localObject != null))
        this.dealChannel = ((DPObject)localObject).getInt("DealChannel");
      return;
    }
    catch (Exception localException)
    {
      break label32;
    }
  }

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new DealInfoAgentFragment();
    return this.mFragment;
  }

  public String getPageName()
  {
    return "tuandeal";
  }

  public void onCreate(Bundle paramBundle)
  {
    resolveArguments();
    super.onCreate(paramBundle);
    setTitle("");
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    GAUserInfo localGAUserInfo = paramGAUserInfo;
    if (paramGAUserInfo == null)
      localGAUserInfo = new GAUserInfo();
    localGAUserInfo.dealgroup_id = Integer.valueOf(this.dealId);
    super.onNewGAPager(localGAUserInfo);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.DealInfoAgentActivity
 * JD-Core Version:    0.6.0
 */