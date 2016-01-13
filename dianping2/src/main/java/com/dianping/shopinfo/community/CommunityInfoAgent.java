package com.dianping.shopinfo.community;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;

public class CommunityInfoAgent extends ShopCellAgent
{
  private String CELL_INDEX = "0200Basic.40info";
  private CommonCell communityCell;

  public CommunityInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    removeAllCells();
    if ((getShop() == null) || (getShop().getObject("CommunityDesc") == null))
      return;
    if (this.communityCell == null)
      this.communityCell = ((CommonCell)this.res.inflate(getContext(), R.layout.shopinfo_community_cell, getParentView(), false));
    this.communityCell.setLeftIcon(R.drawable.community_icon);
    this.communityCell.setTitle(getShop().getObject("CommunityDesc").getString("Title"));
    this.communityCell.setSubTitle(getShop().getObject("CommunityDesc").getString("SubTitle"));
    this.communityCell.setGAString("info", getGAExtra());
    addCell(this.CELL_INDEX, this.communityCell, 1);
  }

  public void onCellClick(String paramString, View paramView)
  {
    paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://communitydetails"));
    paramString.putExtra("shopname", getShop().getString("Name"));
    if ((getShop().getObject("CommunityDesc").getArray("Attributes") != null) && (getShop().getObject("CommunityDesc").getArray("Attributes").length > 0))
      paramString.putExtra("info", new ArrayList(Arrays.asList(getShop().getObject("CommunityDesc").getArray("Attributes"))));
    startActivity(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.community.CommunityInfoAgent
 * JD-Core Version:    0.6.0
 */