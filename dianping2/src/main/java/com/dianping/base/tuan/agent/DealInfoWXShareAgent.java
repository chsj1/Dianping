package com.dianping.base.tuan.agent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.tuan.utils.DealShareUtils;
import com.dianping.base.tuan.utils.DealShareUtils.ShareType;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class DealInfoWXShareAgent extends TuanGroupCellAgent
  implements View.OnClickListener
{
  protected DPObject dpDeal;
  private final Handler mHandler = new Handler();
  private Button weixinShareBtn;
  private View weixinShareView;

  public DealInfoWXShareAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setupView()
  {
    this.weixinShareView = this.res.inflate(getContext(), R.layout.deal_weixin_share, getParentView(), false);
    this.weixinShareBtn = ((Button)this.weixinShareView.findViewById(R.id.share_weixin));
    this.weixinShareBtn.setOnClickListener(this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      DPObject localDPObject = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != localDPObject)
        this.dpDeal = localDPObject;
    }
    if (getContext() == null);
    do
    {
      do
      {
        do
          return;
        while (this.dpDeal == null);
        removeAllCells();
      }
      while (((NovaApplication)DPApplication.instance()).getStartType() != 1);
      if (this.weixinShareView != null)
        continue;
      setupView();
    }
    while ((paramBundle == null) || (paramBundle.getInt("status") != 1) || (!(this.fragment instanceof AgentFragment.CellStable)));
    ((AgentFragment.CellStable)this.fragment).setBottomCell(this.weixinShareView, this);
  }

  public void onClick(View paramView)
  {
    Bundle localBundle;
    if (paramView.getId() == R.id.share_weixin)
    {
      localBundle = new Bundle();
      if (!TextUtils.isEmpty(this.dpDeal.getString("RegionName")))
        break label207;
    }
    label207: for (paramView = this.dpDeal.getString("ShortTitle"); ; paramView = "【" + this.dpDeal.getString("RegionName") + "】" + this.dpDeal.getString("ShortTitle"))
    {
      String str = "仅售" + this.dpDeal.getDouble("Price") + "元," + this.dpDeal.getString("ContentTitle");
      localBundle.putString("title", paramView);
      localBundle.putString("imageUrl", this.dpDeal.getString("Photo"));
      localBundle.putString("summary", str);
      localBundle.putString("targetUrl", "http://m.dianping.com/tuan/weixinshare/" + this.dpDeal.getInt("ID"));
      DealShareUtils.shareCallback((DPActivity)getContext(), localBundle, DealShareUtils.ShareType.WXFRIEND);
      paramView = new ProgressDialog(getContext());
      paramView.setMessage("正在处理，请稍候...");
      paramView.show();
      this.mHandler.postDelayed(new Runnable(paramView)
      {
        public void run()
        {
          this.val$dialog.dismiss();
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://wxadapter"));
          DealInfoWXShareAgent.this.getContext().startActivity(localIntent);
        }
      }
      , 1500L);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoWXShareAgent
 * JD-Core Version:    0.6.0
 */