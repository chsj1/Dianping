package com.dianping.movie.activity;

import android.content.Intent;
import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.utils.PayUtils;
import com.dianping.movie.fragment.MoviePayAgentFragement;
import com.dianping.widget.view.GAUserInfo;

public class MoviePayAgentActivity extends AgentActivity
{
  protected String orderId;
  protected String productCode;

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new MoviePayAgentFragement();
    return this.mFragment;
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mFragment.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040008");
    if (paramBundle == null)
    {
      this.orderId = getStringParam("orderid");
      this.productCode = getStringParam("productcode");
      return;
    }
    this.orderId = paramBundle.getString("orderId");
    this.productCode = paramBundle.getString("productCode");
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    GAUserInfo localGAUserInfo = paramGAUserInfo;
    if (paramGAUserInfo == null)
      localGAUserInfo = new GAUserInfo();
    try
    {
      localGAUserInfo.order_id = Integer.valueOf(Integer.parseInt(this.orderId));
      label28: localGAUserInfo.prepay_info = PayUtils.generateGAPrepayInfos(this.orderId, this.productCode);
      super.onNewGAPager(localGAUserInfo);
      return;
    }
    catch (java.lang.Exception paramGAUserInfo)
    {
      break label28;
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("orderId", this.orderId);
    paramBundle.putString("productCode", this.productCode);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MoviePayAgentActivity
 * JD-Core Version:    0.6.0
 */