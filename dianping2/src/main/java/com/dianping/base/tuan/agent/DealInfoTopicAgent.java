package com.dianping.base.tuan.agent;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class DealInfoTopicAgent extends TuanGroupCellAgent
{
  protected TextView content;
  protected DPObject dpDeal;
  protected View rootview;
  protected TextView title;

  public DealInfoTopicAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != paramBundle)
        this.dpDeal = paramBundle;
    }
    if (getContext() == null);
    do
      return;
    while (this.dpDeal == null);
    if (this.rootview == null)
      setupView();
    updateView();
  }

  protected void setupView()
  {
    this.rootview = this.res.inflate(getContext(), R.layout.deal_info_topic_layout, getParentView(), false);
    this.title = ((TextView)this.rootview.findViewById(R.id.deal_info_topic_title));
    this.content = ((TextView)this.rootview.findViewById(R.id.deal_info_topic_content));
  }

  protected void updateView()
  {
    removeAllCells();
    if (this.dpDeal == null);
    do
      return;
    while ((this.dpDeal.getString("GoodShopContent") == null) || ("".equals(this.dpDeal.getString("GoodShopContent"))));
    this.content.setText(this.dpDeal.getString("GoodShopContent"));
    if ((this.dpDeal.getString("GoodShopTitle") != null) && (!"".equals(this.dpDeal.getString("GoodShopTitle"))))
    {
      this.title.setText(this.dpDeal.getString("GoodShopTitle"));
      this.title.setVisibility(0);
    }
    while ((this.fragment instanceof GroupAgentFragment))
    {
      addCell("010Basic.012Topic0", this.rootview);
      return;
      this.title.setVisibility(8);
    }
    addCell("010Basic.012Topic0", this.rootview);
    addDividerLine("010Basic.012Topic1");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoTopicAgent
 * JD-Core Version:    0.6.0
 */