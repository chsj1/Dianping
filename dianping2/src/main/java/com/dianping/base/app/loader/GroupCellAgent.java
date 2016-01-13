package com.dianping.base.app.loader;

import android.view.View;
import com.dianping.util.Log;
import com.dianping.v1.R.id;

public class GroupCellAgent extends CellAgent
{
  public static final int STYLE_CLICKABLE = 1;
  public static final int STYLE_FLAT_CELL = 1024;
  public static final int STYLE_INDICATOR = 256;
  public static final int STYLE_INNER_DIVIDER = 64;
  public static final int STYLE_NO_BACKGROUND = 16;
  private static final String TAG = GroupCellAgent.class.getSimpleName();
  private final GroupAgentFragment groupAgentFragment;

  public GroupCellAgent(Object paramObject)
  {
    super(paramObject);
    if (!(this.fragment instanceof GroupAgentFragment))
    {
      this.groupAgentFragment = null;
      Log.e(TAG, "fragment cannot be cast to GroupAgentFragment");
      return;
    }
    this.groupAgentFragment = ((GroupAgentFragment)this.fragment);
  }

  public void addCell(String paramString, View paramView)
  {
    addCell(paramString, paramView, 0);
  }

  public void addCell(String paramString, View paramView, int paramInt)
  {
    if ((paramInt & 0x40) != 0)
      paramView.setTag(R.id.group_agent_inner_divider, Integer.valueOf(64));
    if (this.groupAgentFragment != null)
    {
      this.groupAgentFragment.addCell(this, paramString, paramView, paramInt);
      return;
    }
    Log.e(TAG, "fragment is not GroupAgentFragment's instance");
    super.addCell(paramString, paramView);
  }

  public void addCellAndReorder(String paramString, View paramView, int paramInt)
  {
    this.index = paramString;
    addCell(paramString, paramView, paramInt);
  }

  public void onCellClick(String paramString, View paramView)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.GroupCellAgent
 * JD-Core Version:    0.6.0
 */