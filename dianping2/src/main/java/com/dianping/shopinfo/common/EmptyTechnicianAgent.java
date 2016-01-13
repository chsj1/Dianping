package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.layout;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EmptyTechnicianAgent extends ShopCellAgent
{
  private static final String CELL_EMPTY_TECH = "6000EmptyTech.00Tech";

  public EmptyTechnicianAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    Object localObject = getShop();
    if (localObject == null)
      break label14;
    while (true)
    {
      label14: return;
      if (paramBundle == null)
        continue;
      boolean bool = paramBundle.getBoolean("ShowTechEmptyModule", false);
      if (((DPObject)localObject).getObject("ClientShopStyle") == null)
        break;
      paramBundle = "";
      localObject = "";
      if (isCommonDefalutType())
      {
        paramBundle = "服务人员";
        localObject = "http://m.dianping.com/technician/shop/select?category_id=10&cityid=*&latitude=*&longitude=*&shop_id=" + shopId();
      }
      while ((bool) && (!TextUtils.isEmpty(paramBundle)))
      {
        CommonCell localCommonCell = (CommonCell)this.res.inflate(getContext(), R.layout.shopinfo_empty_technician, getParentView(), false);
        localCommonCell.setGAString("waiter_null");
        localCommonCell.setTag(localObject);
        localCommonCell.setTitle(paramBundle);
        localCommonCell.getRightText().setTextColor(getResources().getColor(R.color.light_gray));
        localCommonCell.setRightText("暂无数据，立即添加");
        addCell("6000EmptyTech.00Tech", localCommonCell, 257);
        return;
        if (!isSportClubType())
          continue;
        paramBundle = "健身教练";
        localObject = "http://m.dianping.com/technician/title/select?category_ids=149,147,148&cityid=*&latitude=*&longitude=*&token=!&shop_id=" + shopId();
      }
    }
  }

  public void onCellClick(String paramString, View paramView)
  {
    super.onCellClick(paramString, paramView);
    if (paramView != null)
    {
      paramView = (String)paramView.getTag();
      paramString = paramView;
      if (paramView == null);
    }
    try
    {
      paramString = URLEncoder.encode(paramView, "utf-8");
      paramString = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramString));
      getFragment().startActivity(paramString);
      return;
    }
    catch (UnsupportedEncodingException paramString)
    {
      while (true)
      {
        paramString.printStackTrace();
        paramString = paramView;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.EmptyTechnicianAgent
 * JD-Core Version:    0.6.0
 */