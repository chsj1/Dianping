package com.tencent.stat;

import android.app.ListActivity;

public class EasyListActivity extends ListActivity
{
  protected void onPause()
  {
    super.onPause();
    StatService.onPause(this);
  }

  protected void onResume()
  {
    super.onResume();
    StatService.onResume(this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.stat.EasyListActivity
 * JD-Core Version:    0.6.0
 */