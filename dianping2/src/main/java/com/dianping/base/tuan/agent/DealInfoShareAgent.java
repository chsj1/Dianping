package com.dianping.base.tuan.agent;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.array;
import com.dianping.v1.R.drawable;

public class DealInfoShareAgent extends TuanGroupCellAgent
  implements View.OnClickListener, FragmentManager.OnBackStackChangedListener
{
  private static final String SHARE_BTN = "01Share";
  private DPObject dpDeal;

  public DealInfoShareAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void share()
  {
    if (this.dpDeal == null)
      return;
    ShareUtil.gotoShareTo(getContext(), ShareType.DEAL, this.dpDeal, R.array.deal_info_share_item, "tuan5", "tuan5_success_share");
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
  }

  public void onBackStackChanged()
  {
    if (getFragment().getFragmentManager().getBackStackEntryCount() > 0)
    {
      getFragment().getTitleBar().removeRightViewItem("01Share");
      return;
    }
    getFragment().getTitleBar().addRightViewItem("01Share", R.drawable.ic_action_share_normal, this);
  }

  public void onClick(View paramView)
  {
    share();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getFragment().getTitleBar().addRightViewItem("01Share", R.drawable.ic_action_share_normal, this);
    if (getFragment().getFragmentManager() != null)
      getFragment().getFragmentManager().addOnBackStackChangedListener(this);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (getFragment().getFragmentManager() != null)
      getFragment().getFragmentManager().removeOnBackStackChangedListener(this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoShareAgent
 * JD-Core Version:    0.6.0
 */