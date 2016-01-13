package com.dianping.queue.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.queue.fragment.QueueMainFragment;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.util.UUID;

public class QueueMainActivity extends AgentActivity
{
  protected int frompush;
  protected String shopId;

  protected AgentFragment getAgentFragment()
  {
    return QueueMainFragment.newInstance(this.shopId, this.frompush);
  }

  public String getPageName()
  {
    return "queuemain";
  }

  public void onCreate(Bundle paramBundle)
  {
    this.shopId = getStringParam("shopid");
    if (TextUtils.isEmpty(this.shopId))
    {
      Toast.makeText(this, "缺少必要参数", 0).show();
      finish();
    }
    this.frompush = getIntParam("frompush", 0);
    super.onCreate(paramBundle);
    setTitle("排队取号");
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    GAUserInfo localGAUserInfo1 = paramGAUserInfo;
    GAUserInfo localGAUserInfo2 = localGAUserInfo1;
    if (localGAUserInfo1 == null)
      localGAUserInfo2 = new GAUserInfo();
    localGAUserInfo2.shop_id = Integer.valueOf(Integer.parseInt(this.shopId));
    GAHelper.instance().setGAPageName(getPageName());
    GAHelper.instance().setRequestId(this, UUID.randomUUID().toString(), localGAUserInfo2, false);
    paramGAUserInfo.utm = null;
    paramGAUserInfo.marketing_source = null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.queue.activity.QueueMainActivity
 * JD-Core Version:    0.6.0
 */