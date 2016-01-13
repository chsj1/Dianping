package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ShopBranchAgent extends ShopCellAgent
{
  private static final String CELL_SPECIAL_ITEM = "7000ShopInfo.80branch";

  public ShopBranchAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    paramBundle = getShop();
    if ((paramBundle == null) || (TextUtils.isEmpty(paramBundle.getString("BranchInfo"))))
      return;
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.findViewById(R.id.middle_divder_line).setVisibility(8);
    localShopinfoCommonCell.setTitle(paramBundle.getString("BranchInfo"));
    localShopinfoCommonCell.setGAString("branch", getGAExtra());
    addCell("7000ShopInfo.80branch", localShopinfoCommonCell, 1);
  }

  public void onCellClick(String paramString, View paramView)
  {
    paramString = getShop();
    if (paramString == null)
      return;
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopidlist?shopid=" + shopId()));
    paramView.putExtra("showAddBranchShop", true);
    paramView.putExtra("shop", paramString);
    getFragment().startActivity(paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.ShopBranchAgent
 * JD-Core Version:    0.6.0
 */