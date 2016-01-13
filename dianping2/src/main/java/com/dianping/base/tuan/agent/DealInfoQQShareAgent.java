package com.dianping.base.tuan.agent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.app.loader.AgentFragment.CellStable;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.QQAio;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.tencent.share.ShareHelper;

public class DealInfoQQShareAgent extends TuanGroupCellAgent
  implements View.OnClickListener
{
  protected DPObject dpDeal;
  private Button qqShareBtn;
  private View qqShareView;

  public DealInfoQQShareAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setupView()
  {
    this.qqShareView = this.res.inflate(getContext(), R.layout.deal_qq_share, getParentView(), false);
    this.qqShareBtn = ((Button)this.qqShareView.findViewById(R.id.share_qq));
    this.qqShareBtn.setOnClickListener(this);
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
      while (((NovaApplication)DPApplication.instance()).getStartType() != 2);
      if (this.qqShareView != null)
        continue;
      setupView();
    }
    while ((paramBundle == null) || (paramBundle.getInt("status") != 1) || (!(this.fragment instanceof AgentFragment.CellStable)));
    ((AgentFragment.CellStable)this.fragment).setBottomCell(this.qqShareView, this);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.share_qq)
      if (!TextUtils.isEmpty(this.dpDeal.getString("RegionName")))
        break label200;
    label200: for (paramView = this.dpDeal.getString("ShortTitle"); ; paramView = "【" + this.dpDeal.getString("RegionName") + "】" + this.dpDeal.getString("ShortTitle"))
    {
      Object localObject = "仅售" + this.dpDeal.getDouble("Price") + "元," + this.dpDeal.getString("ContentTitle");
      String str1 = this.dpDeal.getString("Photo");
      String str2 = "http://m.dianping.com/tuan/weixinshare/" + this.dpDeal.getInt("ID");
      ShareHolder localShareHolder = new ShareHolder();
      localShareHolder.title = paramView;
      localShareHolder.desc = ((String)localObject);
      localShareHolder.imageUrl = str1;
      localShareHolder.webUrl = str2;
      paramView = QQAio.getInstance().getQQIntent();
      localObject = ShareHelper.getInstance();
      ((ShareHelper)localObject).init(paramView);
      ((ShareHelper)localObject).shareToQQ(getContext(), 200002L, localShareHolder.webUrl, localShareHolder.title, localShareHolder.desc, localShareHolder.imageUrl);
      ((ShareHelper)localObject).release();
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoQQShareAgent
 * JD-Core Version:    0.6.0
 */