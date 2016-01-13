package com.dianping.search.deallist.agent;

import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.tuan.utils.TuanSharedDataKey;
import com.dianping.base.widget.FilterBar;

public class NaviFilterBarTabAgent extends NaviFilterBarAgent
{
  public NaviFilterBarTabAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected void addItems()
  {
    this.filterBar.removeAllViews();
    if (this.naviRegion != null)
      this.filterBar.addItem(TuanSharedDataKey.CURRENT_REGION_KEY, getCurrentRegion().getString("Name"));
    if (this.naviCategory != null)
      this.filterBar.addItem(TuanSharedDataKey.CURRENT_CATEGORY_KEY, getCurrentCategory().getString("Name"));
    if (this.naviSort != null)
      this.filterBar.addItem(TuanSharedDataKey.CURRENT_SORT_KEY, getCurrentSort().getString("Name"));
    StringBuilder localStringBuilder = new StringBuilder().append("筛选");
    if (getScreeningCount() > 0);
    for (String str = " " + getScreeningCount(); ; str = "")
    {
      str = str;
      this.naviScreeningLayout = this.filterBar.addItem(TuanSharedDataKey.CURRENT_SCREENING_KEY, str);
      return;
    }
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ("deal_list_keyword_changed".equals(paramAgentMessage.what))
    {
      setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_CATEGORY_ID, Integer.valueOf(0));
      setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_REGION_ID, Integer.valueOf(0));
      setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_RANGE_ID, Integer.valueOf(-2));
      setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_SORT_ID, Integer.valueOf(0));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.agent.NaviFilterBarTabAgent
 * JD-Core Version:    0.6.0
 */