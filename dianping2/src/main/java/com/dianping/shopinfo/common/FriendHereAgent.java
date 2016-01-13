package com.dianping.shopinfo.common;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.widget.view.GAHelper;
import java.net.URLEncoder;

public class FriendHereAgent extends ShopCellAgent
{
  private String CELL_INDEX = "2800friend.review";
  private CommonCell friendCell;
  private boolean isShow = false;

  public FriendHereAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      do
        return;
      while (super.getSharedObject("CanBeBind") == null);
      this.isShow = ((Boolean)super.getSharedObject("CanBeBind")).booleanValue();
    }
    while (!this.isShow);
    if (this.friendCell == null)
      this.friendCell = createCommonCell();
    this.friendCell.setTitle("你的好友可能来过");
    this.friendCell.setRightText("看看是谁");
    this.friendCell.setGAString("viewfriends", getGAExtra());
    addCell(this.CELL_INDEX, this.friendCell, 257);
    GAHelper.instance().contextStatisticsEvent(getContext(), "viewfriends", null, "view");
  }

  public void onCellClick(String paramString, View paramView)
  {
    paramString = new StringBuilder();
    paramString.append("http://m.dianping.com/friend/index?utmSource=appShop&token=!&cityid=*");
    try
    {
      paramString.append("&shopid=" + shopId());
      paramView = URLEncoder.encode(paramString.toString(), "utf-8");
      startActivity("dianping://web?url=" + paramView);
      Log.d("friend", paramString.toString());
      return;
    }
    catch (Exception paramString)
    {
      paramString.printStackTrace();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.FriendHereAgent
 * JD-Core Version:    0.6.0
 */