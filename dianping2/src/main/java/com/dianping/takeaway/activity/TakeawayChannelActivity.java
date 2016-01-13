package com.dianping.takeaway.activity;

import com.dianping.archive.DPObject;
import com.dianping.base.widget.TitleBar;
import com.dianping.takeaway.entity.TakeawayChannelDataSource;
import com.dianping.takeaway.entity.TakeawaySampleShoplistDataSource;
import com.dianping.v1.R.layout;

public class TakeawayChannelActivity extends TakeawaySampleShopListActivity
{
  private TakeawayChannelDataSource channelDataSource;

  protected TakeawaySampleShoplistDataSource getDataSource()
  {
    if (this.channelDataSource == null)
      this.channelDataSource = new TakeawayChannelDataSource(this);
    return this.channelDataSource;
  }

  protected void initTitleBarView()
  {
    super.getTitleBar().setTitle(this.channelDataSource.curCategory().getString("Name"));
  }

  protected void onDestroy()
  {
    this.channelDataSource.onDestroy();
    super.onDestroy();
  }

  protected void onSetContentView()
  {
    super.setContentView(R.layout.takeaway_channel_activity);
  }

  protected void updateTitleBarView(String paramString)
  {
    super.getTitleBar().setTitle(this.channelDataSource.curCategory().getString("Name"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayChannelActivity
 * JD-Core Version:    0.6.0
 */