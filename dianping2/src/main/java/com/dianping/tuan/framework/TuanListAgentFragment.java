package com.dianping.tuan.framework;

import android.os.Bundle;
import android.widget.ListView;
import com.dianping.base.tuan.fragment.TuanAdapterAgentFragment;
import com.dianping.locationservice.impl286.util.CommonUtil;
import com.dianping.tuan.adapter.ModuleMergeAdapter;
import com.dianping.tuan.adapter.decorator.ModuleDividerAdapter;

public abstract class TuanListAgentFragment extends TuanAdapterAgentFragment
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public void resetAdapter()
  {
    this.mergeAdapter = new ModuleMergeAdapter();
  }

  protected void updateAgentAdapter()
  {
    super.updateAgentAdapter();
    if (!(this.listView.getAdapter() instanceof ModuleDividerAdapter))
    {
      ModuleDividerAdapter localModuleDividerAdapter = new ModuleDividerAdapter(getContext(), this.mergeAdapter);
      localModuleDividerAdapter.setCellDividerPaddingLeft(CommonUtil.dip2px(getContext(), 10.0F));
      this.listView.setAdapter(localModuleDividerAdapter);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.framework.TuanListAgentFragment
 * JD-Core Version:    0.6.0
 */