package com.dianping.tuan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.activity.TuanAgentActivity;
import com.dianping.base.tuan.utils.PayUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.tuan.fragment.CommonPayAgentFragement;
import com.dianping.v1.R.id;
import com.dianping.widget.view.GAUserInfo;

public class CommonPayAgentActivity extends TuanAgentActivity
{
  protected String backUrl;
  protected String orderId;
  protected String productCode;

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new CommonPayAgentFragement();
    return this.mFragment;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    getAgentFragment().onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onBackPressed()
  {
    if (TextUtils.isEmpty(this.backUrl))
    {
      super.onBackPressed();
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse(this.backUrl));
    startActivity(localIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null)
    {
      this.orderId = getStringParam("orderid");
      this.productCode = getStringParam("productcode");
    }
    for (this.backUrl = getStringParam("backurl"); ; this.backUrl = paramBundle.getString("backUrl"))
    {
      if (!TextUtils.isEmpty(this.backUrl))
        super.getTitleBar().setLeftView(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW");
            paramView.setData(Uri.parse(CommonPayAgentActivity.this.backUrl));
            CommonPayAgentActivity.this.startActivity(paramView);
          }
        });
      return;
      this.orderId = paramBundle.getString("orderId");
      this.productCode = paramBundle.getString("productCode");
    }
  }

  public void onCreateTitleBar(TitleBar paramTitleBar)
  {
    super.onCreateTitleBar(paramTitleBar);
    paramTitleBar.removeAllRightViewItem();
    paramTitleBar.findViewById(R.id.title_bar_left_view_container).setVisibility(0);
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    GAUserInfo localGAUserInfo = paramGAUserInfo;
    if (paramGAUserInfo == null)
      localGAUserInfo = new GAUserInfo();
    localGAUserInfo.prepay_info = PayUtils.generateGAPrepayInfos(this.orderId, this.productCode);
    super.onNewGAPager(localGAUserInfo);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("orderId", this.orderId);
    paramBundle.putString("productCode", this.productCode);
    paramBundle.putString("backUrl", this.backUrl);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.CommonPayAgentActivity
 * JD-Core Version:    0.6.0
 */