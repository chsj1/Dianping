package com.dianping.hotel.deal.agent;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.tuan.agent.TuanGroupCellAgent;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.array;
import com.dianping.v1.R.drawable;

public class HotelProdShareAgent extends TuanGroupCellAgent
  implements View.OnClickListener, FragmentManager.OnBackStackChangedListener
{
  private final String SHARE_BTN = "HotelProdCommonShareBtn";
  private DPObject dpHotelProdBase;

  public HotelProdShareAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
      this.dpHotelProdBase = ((DPObject)paramBundle.getParcelable("hotelprodbase"));
  }

  public void onBackStackChanged()
  {
    if (getFragment().getFragmentManager().getBackStackEntryCount() > 0)
    {
      getFragment().getTitleBar().removeRightViewItem("HotelProdCommonShareBtn");
      return;
    }
    getFragment().getTitleBar().addRightViewItem("HotelProdCommonShareBtn", R.drawable.ic_action_share_normal, this);
  }

  public void onClick(View paramView)
  {
    if (this.dpHotelProdBase == null)
      return;
    ShareUtil.gotoShareTo(getContext(), ShareType.HotelProd, this.dpHotelProdBase, R.array.hotel_prod_info_share_item, "tuan5", "tuan5_success_share");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getFragment().getTitleBar().addRightViewItem("HotelProdCommonShareBtn", R.drawable.ic_action_share_normal, this);
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
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdShareAgent
 * JD-Core Version:    0.6.0
 */