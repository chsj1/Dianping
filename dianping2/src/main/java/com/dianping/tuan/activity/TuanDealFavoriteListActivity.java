package com.dianping.tuan.activity;

import android.os.Bundle;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.layout;

public class TuanDealFavoriteListActivity extends BaseTuanActivity
{
  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.tuan_deal_favor_list);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanDealFavoriteListActivity
 * JD-Core Version:    0.6.0
 */