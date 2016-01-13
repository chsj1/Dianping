package com.dianping.shopinfo.movie.agent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.movie.fragment.CinemaInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.layout;

public class CinemaServiceAgent extends ShopCellAgent
{
  protected static final String CELL_TOP = "0300Basic.05Info";

  public CinemaServiceAgent(Object paramObject)
  {
    super(paramObject);
  }

  public CommonCell createCommonCell()
  {
    return (CommonCell)MyResources.getResource(CellAgent.class).inflate(getContext(), R.layout.shopinfo_cinemainfo_service_item_view, getParentView(), false);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getFragment() == null);
    do
    {
      return;
      paramBundle = ((CinemaInfoFragment)getFragment()).getCinema().getArray("ServiceItems");
    }
    while ((paramBundle == null) || (paramBundle.length <= 0));
    LinearLayout localLinearLayout = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_cinemainfo_service_agent_layout, null, false);
    int i = 0;
    while (i < paramBundle.length)
    {
      CommonCell localCommonCell = createCommonCell();
      if (localCommonCell != null)
      {
        localCommonCell.setLeftIconUrl(paramBundle[i].getString("ImageUrl"));
        localCommonCell.setTitle(paramBundle[i].getString("Title"));
        localLinearLayout.addView(localCommonCell);
      }
      i += 1;
    }
    addCell("0300Basic.05Info", localLinearLayout);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.agent.CinemaServiceAgent
 * JD-Core Version:    0.6.0
 */