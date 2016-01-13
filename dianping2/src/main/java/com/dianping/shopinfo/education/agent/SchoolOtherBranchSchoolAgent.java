package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.layout;

public class SchoolOtherBranchSchoolAgent extends ShopCellAgent
  implements View.OnClickListener
{
  private static final String CELL_OTHER_BRANCH = "5000branchschool.01";

  public SchoolOtherBranchSchoolAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    int i = getShop().getInt("BranchCounts");
    if (i <= 0)
      return;
    paramBundle = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    paramBundle.setTitle("其他校区(" + i + ")", this);
    paramBundle.setBlankContent(true);
    addCell("5000branchschool.01", paramBundle, 1);
  }

  public void onClick(View paramView)
  {
    try
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopidlist?shopid=" + shopId()));
      paramView.putExtra("showAddBranchShop", true);
      paramView.putExtra("shop", getShop());
      getFragment().startActivity(paramView);
      return;
    }
    catch (java.lang.Exception paramView)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.SchoolOtherBranchSchoolAgent
 * JD-Core Version:    0.6.0
 */