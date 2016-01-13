package com.dianping.search.history;

import android.os.Bundle;
import com.dianping.base.app.NovaTabBaseFragment;
import com.dianping.base.basic.NovaTabFragmentActivity;

public class HistoryActivity extends NovaTabFragmentActivity
{
  public NovaTabBaseFragment[] getFragments()
  {
    return new NovaTabBaseFragment[] { new HistoryFragment(), new HistoryTuanFragment() };
  }

  public String[] getTabTitles()
  {
    return new String[] { "浏览商户", "浏览团购" };
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("浏览商户");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.history.HistoryActivity
 * JD-Core Version:    0.6.0
 */